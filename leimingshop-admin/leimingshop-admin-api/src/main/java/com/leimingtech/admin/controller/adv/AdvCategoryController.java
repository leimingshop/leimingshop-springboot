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
import com.leimingtech.modules.constants.adv.AdvResultCodeConstants;
import com.leimingtech.modules.dto.adv.AdvCategoryDTO;
import com.leimingtech.modules.dto.adv.InsertAdvCategoryDTO;
import com.leimingtech.modules.dto.adv.UpdateAdvCategoryDTO;
import com.leimingtech.modules.enums.adv.StatusEnum;
import com.leimingtech.modules.enums.adv.SysFlagEnum;
import com.leimingtech.modules.service.adv.AdvCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 广告类别管理
 *
 * @author 刘远杰
 * @since 7.0 2019-05-13
 */
@Slf4j
@RestController
@RequestMapping("adv/category")
@Api(tags = "广告类别管理")
public class AdvCategoryController {

    @Autowired
    private AdvCategoryService advCategoryService;

    @GetMapping("page")
    @ApiOperation("广告类别分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "sysFlag", value = "广告分类类型 0普通广告 1楼层广告 2分类广告", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryName", value = "广告类别名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryType", value = "展示方式 0单图 1多图", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "广告类别状态 1启用 2停用", paramType = "query", dataType = "int")
    })
    public Result<PageData<AdvCategoryDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<AdvCategoryDTO> page = advCategoryService.findAdvCategoryPageList(params);

        return new Result<PageData<AdvCategoryDTO>>().ok(page, "查询广告类别成功");
    }

    @GetMapping("list")
    @ApiOperation("查询广告类别集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryName", value = "广告类别名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryType", value = "展示方式 0单图 1多图", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "广告类别状态 1启用 2停用", paramType = "query", dataType = "Integer")
    })
    public Result<List<AdvCategoryDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<AdvCategoryDTO> advCategoryDTOList = advCategoryService.findAdvCategoryList(params);

        if (CollectionUtils.isNotEmpty(advCategoryDTOList)) {
            return new Result<List<AdvCategoryDTO>>().ok(advCategoryDTOList, "查询广告类别成功");
        } else {
            return new Result<List<AdvCategoryDTO>>().error(AdvResultCodeConstants.ERR_NO_RESULT, "查询广告类别失败");
        }
    }

    @GetMapping("list/enabled")
    @ApiOperation("查询可用普通广告类别列表")
    public Result<List<AdvCategoryDTO>> listEnadbled() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("orderField", "sort");
        params.put("order", "desc");
        params.put("sysFlag", SysFlagEnum.NOMARL.value());
        params.put("status", StatusEnum.ENABLED.value());
        List<AdvCategoryDTO> advCategoryDTOList = advCategoryService.findAdvCategoryList(params);

        if (CollectionUtils.isNotEmpty(advCategoryDTOList)) {
            return new Result<List<AdvCategoryDTO>>().ok(advCategoryDTOList, "查询广告类别成功");
        } else {
            return new Result<List<AdvCategoryDTO>>().error(AdvResultCodeConstants.ERR_NO_RESULT, "查询广告类别失败");
        }
    }

    @GetMapping("{id}")
    @ApiOperation("广告类别详情查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "广告类别id", required = true, paramType = "path", dataType = "Long")
    })
    public Result<AdvCategoryDTO> get(@PathVariable("id") Long id) {
        AdvCategoryDTO data = advCategoryService.get(id);
        if (data != null) {
            return new Result<AdvCategoryDTO>().ok(data, "查询广告类别成功");
        } else {
            return new Result<AdvCategoryDTO>().error(AdvResultCodeConstants.ERR_INV_PARAMS, "查询广告类别失败");
        }
    }

    @PostMapping
    @ApiOperation("广告类别保存")
    @LogOperation("广告类别保存")
    public Result save(@RequestBody InsertAdvCategoryDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        Result result;
        try {
            Map<String, Object> map = advCategoryService.checkAdvKey(dto.getAdvKey());
            result = new Result().error((int) map.get("code"), (String) map.get("message"));
            if ((int) map.get("code") == AdvResultCodeConstants.SUCCESS) {
                AdvCategoryDTO advCategoryDTO = ConvertUtils.sourceToTarget(dto, AdvCategoryDTO.class);
                advCategoryService.save(advCategoryDTO);
                result = new Result<>().ok(null, "保存广告类别成功");
            }
        } catch (Exception e) {
            log.error("保存广告类别异常：{}", e);
            return new Result().error("保存广告类别失败");
        }
        return result;
    }

    @PutMapping
    @ApiOperation("广告类别修改")
    @LogOperation("广告类别修改")
    public Result update(@RequestBody UpdateAdvCategoryDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        try {
            AdvCategoryDTO advCategoryDTO = ConvertUtils.sourceToTarget(dto, AdvCategoryDTO.class);
            Map<String, Object> map = advCategoryService.updateAdvCategory(advCategoryDTO);
            return new Result().error((int) map.get("code"), (String) map.get("message"));
        } catch (Exception e) {
            log.error("修改广告类别异常：{}", e);
            return new Result().error("修改广告类别失败");
        }
    }

    @DeleteMapping
    @ApiOperation("广告类别删除")
    @LogOperation("广告类别删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "广告类别索引数组", required = true, paramType = "body", dataType = "Long[]")
    })
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        try {
            Map<String, Object> map = advCategoryService.logicDeleteAdvCategory(ids);
            return new Result().error((int) map.get("code"), (String) map.get("message"));
        } catch (Exception e) {
            log.error("删除广告类别异常：{}", e);
            return new Result().error("删除广告类别失败");
        }
    }

    @GetMapping("check/key")
    @ApiOperation("校验广告类别标识重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "advKey", value = "广告标识", required = true, paramType = "query", dataType = "String")
    })
    public Result checkAdvKey(String advKey) {
        Map<String, Object> map = advCategoryService.checkAdvKey(advKey);
        return new Result().error((int) map.get("code"), (String) map.get("message"));
    }

    @PostMapping("status")
    @ApiOperation("启用/停用广告类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "广告类别id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "status", value = "广告分组状态", required = true, paramType = "query", dataType = "int")
    })
    public Result enableAdvCategory(@RequestParam("status") Integer status, @RequestParam("id") Long id) {
        if (status == StatusEnum.ENABLED.value() || status == StatusEnum.NO_ENABLED.value()) {
            int count = advCategoryService.updateStatus(status, id);
            if (count > 0) {
                return new Result<>().ok(null, "更新广告类目状态成功");
            } else {
                return new Result().error("更新广告类目状态失败");
            }
        } else {
            return new Result().error(AdvResultCodeConstants.ERR_INV_PARAMS, "广告分组状态不合法");
        }

    }

}
