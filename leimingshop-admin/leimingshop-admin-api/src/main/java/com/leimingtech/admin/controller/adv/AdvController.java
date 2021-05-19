/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.adv;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.constants.adv.AdvDefaultConstants;
import com.leimingtech.modules.constants.adv.AdvResultCodeConstants;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.adv.InsertAdvDTO;
import com.leimingtech.modules.dto.adv.InsertSpecialAdvDTO;
import com.leimingtech.modules.dto.adv.UpdateAdvDTO;
import com.leimingtech.modules.enums.adv.AdvTypeEnum;
import com.leimingtech.modules.enums.adv.ApplyStateEnum;
import com.leimingtech.modules.service.adv.AdvService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 广告管理
 *
 * @author 刘远杰
 * @since 7.0 2019-05-13
 */
@Slf4j
@RestController
@RequestMapping("adv")
@Api(tags = "广告管理")
public class AdvController {
    @Autowired
    private AdvService advService;

    @GetMapping("page")
    @ApiOperation("admin广告分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "advType", value = "广告类型（0普通广告 1楼层广告 2分类广告）", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "categoryId", value = "广告类目id", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "categoryName", value = "广告类目名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "floorName", value = "楼层名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcName", value = "分类名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "广告类别 1 H5 2 PC", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "advTitle", value = "广告标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate1", value = "广告开始时间起", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "startDate2", value = "广告开始时间止", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate1", value = "广告结束时间起", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate2", value = "广告结束时间止", paramType = "query", dataType = "Date")
    })
    public Result<PageData<AdvDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {


        PageData<AdvDTO> page = advService.findAdvShowList(params);

        return new Result<PageData<AdvDTO>>().ok(page, "查询广告成功");
    }

    @GetMapping("{id}")
    @ApiOperation("广告详情查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "广告id", required = true, paramType = "path", dataType = "Long")
    })
    public Result<AdvDTO> get(@PathVariable("id") Long id) {
        AdvDTO data = advService.findAdvById(id);
        if (data != null) {
            return new Result<AdvDTO>().ok(data, "查询广告成功");
        } else {
            return new Result<AdvDTO>().error(AdvResultCodeConstants.ERR_NO_RESULT, "查询广告失败");
        }
    }

    @PostMapping
    @ApiOperation("广告保存")
    @LogOperation("广告保存")
    public Result save(@RequestBody InsertAdvDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        AdvDTO advDTO = ConvertUtils.sourceToTarget(dto, AdvDTO.class);

        if (getAdvDTO(advDTO)) {
            // 广告开始开始时间晚于结束时间
            return new Result().error(AdvResultCodeConstants.ERR_INV_PARAMS, "广告展示开始时间不能大于展示结束时间");
        }

        // 一般广告类型
        advDTO.setAdvType(AdvTypeEnum.NORMAL.value());
        return saveAdv(advDTO);
    }

    @PostMapping("special")
    @ApiOperation("楼层、分类广告保存")
    @LogOperation("楼层、分类广告保存")
    public Result saveSpecialAdv(@RequestBody InsertSpecialAdvDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        AdvDTO advDTO = ConvertUtils.sourceToTarget(dto, AdvDTO.class);

        if (getAdvDTO(advDTO)) {
            // 广告开始开始时间晚于结束时间
            return new Result().error(AdvResultCodeConstants.ERR_INV_PARAMS, "广告展示开始时间不能大于展示结束时间");
        }
        return saveAdv(advDTO);
    }

    @PutMapping
    @ApiOperation("广告修改")
    @LogOperation("广告修改")
    public Result update(@RequestBody UpdateAdvDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        if (dto.getStartDate() == null) {
            dto.setStartDate(new Date());
        }

        if (dto.getEndDate() != null && dto.getEndDate().before(dto.getStartDate())) {
            // 广告开始开始时间晚于结束时间
            return new Result().error(AdvResultCodeConstants.ERR_INV_PARAMS, "广告展示开始时间不能大于展示结束时间");
        }

        try {
            AdvDTO advDTO = ConvertUtils.sourceToTarget(dto, AdvDTO.class);
            // 启用
            advDTO.setAdvState(AdvDefaultConstants.ADV_STATE);
            // 审核通过
            advDTO.setApplyState(AdvDefaultConstants.APPLY_STATE);
            advService.updateAdv(advDTO);
        } catch (Exception e) {
            log.error("修改广告数据异常：{}", e);
            new Result().error("广告修改失败");
        }

        return new Result<>().ok(null, "广告修改成功");
    }

    @DeleteMapping
    @ApiOperation("广告删除")
    @LogOperation("广告删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "广告索引数组", required = true, paramType = "body", dataType = "Long[]")
    })
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        try {
            advService.logicDeleteAdv(ids);
        } catch (Exception e) {
            log.error("删除广告数据异常：{}", e);
            new Result().error("广告删除失败");
        }

        return new Result<>().ok(null, "广告删除成功");
    }

    private Result saveAdv(@RequestBody AdvDTO advDTO) {
        try {
            advDTO.setApplyState(ApplyStateEnum.PASS.value());
            Map<String, Object> map = advService.saveAdv(advDTO);
            return new Result().error((int) map.get("code"), (String) map.get("message"));
        } catch (Exception e) {
            log.error("保存广告数据异常：{}", e);
            return new Result().error("保存广告失败");
        }
    }

    @GetMapping("display")
    @ApiOperation("正在展示中的广告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "广告类别id", required = true, paramType = "query", dataType = "Long")
    })
    public Result<List<AdvDTO>> listIngAdv(@ApiIgnore @RequestParam Map<String, Object> params) {
        try {

            List<AdvDTO> advDTOList = advService.listIngAdv(params);
            return new Result<List<AdvDTO>>().ok(advDTOList, "查询广告成功");
        } catch (Exception e) {
            log.error("查询广告异常：{}", e);
            return new Result<List<AdvDTO>>().error("查询广告失败");
        }
    }

    @GetMapping("display/special")
    @ApiOperation("楼层、分类广告正在展示中的广告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "advType", value = "广告类型（1：楼层广告 2：分类广告）", required = true,
                    paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "relationId", value = "关联内容索引", required = true,
                    paramType = "query", dataType = "Long")
    })
    public Result<List<AdvDTO>> listIngSpecialAdv(@ApiIgnore @RequestParam Map<String, Object> params) {
        Result<List<AdvDTO>> result = new Result<>();
        try {
            Integer advType = Integer.parseInt((String) params.get("advType"));
            if (advType != AdvTypeEnum.FLOOR.value() && advType != AdvTypeEnum.CLASS.value()) {
                result = new Result<List<AdvDTO>>().error(AdvResultCodeConstants.ERR_INV_PARAMS, "请输入正确的广告类型");
            } else {
                List<AdvDTO> advDTOList = advService.listIngAdv(params);
                result = new Result<List<AdvDTO>>().ok(advDTOList, "查询广告成功");
            }
        } catch (Exception e) {
            log.error("查询广告异常：{}", e);
            return new Result<List<AdvDTO>>().error("查询广告失败");
        }
        return result;
    }

    /**
     * 功能描述:
     * 〈封装广告默认参数，校验展示时间〉
     *
     * @param advDTO 广告实体
     * @author : 刘远杰
     */
    private boolean getAdvDTO(AdvDTO advDTO) {
        if (advDTO.getStartDate() == null) {
            // 未设置开始时间，使用当前时间作为广告开始时间
            advDTO.setStartDate(new Date());
        }

        if (advDTO.getEndDate() != null && advDTO.getEndDate().before(advDTO.getStartDate())) {
            // 开始时间不能晚于结束时间
            return true;
        }
        // 启用
        advDTO.setAdvState(AdvDefaultConstants.ADV_STATE);
        // 审核通过
        advDTO.setApplyState(AdvDefaultConstants.APPLY_STATE);
        // 关联url
        if (StringUtils.isBlank(advDTO.getRelationType())) {
            advDTO.setRelationType(AdvDefaultConstants.RELATION_TYPE);
        }
        return false;
    }

    @GetMapping("relevance")
    @ApiOperation("查询已关联的pc展示分类")
    public Result<List<Long>> relevance() {
        List<Long> ids = advService.relevanceClass();
        return new Result<List<Long>>().ok(ids);
    }
}
