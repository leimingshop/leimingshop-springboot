/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.spec;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.exception.ServiceStatusCode;
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dto.spec.*;
import com.leimingtech.modules.service.spec.SpecService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规格管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Slf4j
@RestController
@RequestMapping("spec")
@Api(tags = "规格管理")
public class SpecController {
    @Autowired
    private SpecService specService;

    @GetMapping("page")
    @ApiOperation("规格分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "specName", value = "规格名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupStatus", value = "规格分组状态（默认1:启用,2:禁用）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupName", value = "规格分组名称", paramType = "query", dataType = "String")
    })
    public Result<PageData<SpecDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SpecDTO> page = specService.findSpecPageList(params);

        if (CollectionUtils.isNotEmpty(page.getList())) {
            return new Result<PageData<SpecDTO>>().ok(page, "查询规格数据成功");
        } else {
            return new Result<PageData<SpecDTO>>().ok(null, "暂无规格");
        }
    }

    @GetMapping("list")
    @ApiOperation("规格列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specName", value = "规格名称", paramType = "query", dataType = "String")
    })
    public Result<List<SpecDTO>> findSpecList(@RequestParam(value = "specName", required = false) String specName) {
        Map<String, Object> params = new HashMap<>(16);

        // 查询规格
        params.put(Constant.ORDER_FIELD, "spec_sort");
        params.put(Constant.ORDER, "desc");
        if (StringUtils.isNotBlank(specName)) {
            params.put("specName", specName);
        }
        List<SpecDTO> specList = specService.findSpecList(params);

        if (CollectionUtils.isNotEmpty(specList)) {
            return new Result<List<SpecDTO>>().ok(specList, "查询规格数据成功");
        } else {
            return new Result<List<SpecDTO>>().ok(null, "暂无规格");
        }


    }

    @GetMapping("{id}")
    @ApiOperation("查询规格详情")
    public Result<SpecAndSpecValueDTO> get(@PathVariable("id") Long id) {
        // 查询规格及规格值
        SpecAndSpecValueDTO specAndSpecValueDTO = specService.findSpecAndSpecValueBySpecId(id);
        if (specAndSpecValueDTO == null) {
            return new Result<SpecAndSpecValueDTO>().ok(null, "暂无数据");
        } else {
            return new Result<SpecAndSpecValueDTO>().ok(specAndSpecValueDTO, "查询规格详情成功");
        }

    }

    @PostMapping
    @ApiOperation("保存规格")
    @LogOperation("保存规格")
    public Result save(@RequestBody InsertSpecDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        for (InsertSpecValueDTO insertSpecValueDTO : dto.getSpecValueDTOList()) {
            ValidatorUtils.validateEntity(insertSpecValueDTO, AddGroup.class, DefaultGroup.class);
        }

        List<InsertSpecValueDTO> specValueDTOList = dto.getSpecValueDTOList();

        if (!this.checkValueName(specValueDTOList)) {
            return new Result().error(SpecResultCodeConstants.ERR_INV_PARAMS, "保存失败，规格值名称重复");
        }

        try {
            Map<String, Object> map = specService.saveSpec(dto);
            return new Result<>().error((int) map.get("code"), (String) map.get("message"));
        } catch (Exception e) {
            log.error("保存规格异常：", e);
            return new Result().error("保存规格失败");
        }
    }

    @PutMapping
    @ApiOperation("修改规格")
    @LogOperation("修改规格")
    public Result update(@RequestBody UpdateSpecDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        List<InsertSpecValueDTO> specValueDTOList = dto.getSpecValueDTOList();
        if (!this.checkValueName(specValueDTOList)) {
            return new Result().error(SpecResultCodeConstants.ERR_INV_PARAMS, "修改失败，规格值名称重复");
        }

        try {
            Map<String, Object> map = specService.updateSpec(dto);
            return new Result<>().error((int) map.get("code"), (String) map.get("message"));
        } catch (Exception e) {
            log.error("修改规格异常：", e);
            return new Result().error("修改规格失败");
        }
    }

    /**
     * 功能描述:
     * 〈校验规格值名称是否重复〉
     *
     * @param specValueDTOList 规格值集合
     * @author : 刘远杰
     */
    private boolean checkValueName(List<InsertSpecValueDTO> specValueDTOList) {
        // 规格值名称集合
        List<String> valueList = new ArrayList<>();
        for (InsertSpecValueDTO specValueDTO : specValueDTOList) {
            // 判断是否与已有的规格值名称重复
            if (valueList.contains(specValueDTO.getSpecValueName())) {
                // 已存在
                return false;
            }
            // 添加到规格值名称集合
            valueList.add(specValueDTO.getSpecValueName());
        }
        return true;
    }

    @PostMapping("check/class")
    @ApiOperation("查询规格是否关联后台分类，true 关联 false 未关联")
    public Result<Boolean> checkClass(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "ids");

        List<SpecClassDTO> specClassDTOS = specService.checkClass(ids);

        Boolean flag = true;
        StringBuilder message = new StringBuilder();
        message.append("您选择的规格");
        if (CollectionUtils.isNotEmpty(specClassDTOS)) {
            throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
        }
        for (SpecClassDTO specClassDTO : specClassDTOS) {
            if (specClassDTO.getNumber() != 0) {
                flag = false;
                message.append(" ").append(specClassDTO.getSpecName());
            }
        }
        message.append(" 已关联后台分类，是否确认删除？");
        if (flag) {
            return new Result<Boolean>().ok(flag, "规格未关联后台分类");
        } else {
            return new Result<Boolean>().ok(flag, message.toString());
        }

    }

    @DeleteMapping("{id}")
    @ApiOperation("删除规格")
    @LogOperation("删除规格")
    public Result delete(@PathVariable Long id) {

        try {
            specService.deleteSpec(id);
        } catch (Exception e) {
            log.error("删除规格异常：{}", e);
            return new Result().error("修改规格失败");
        }
        return new Result<>().ok(null, "删除规格成功");
    }

    @DeleteMapping("batch")
    @ApiOperation("批量删除规格")
    @LogOperation("批量删除规格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "规格索引数组", required = true, paramType = "body", dataType = "Long[]")
    })
    public Result deleteSpecBatch(@RequestBody Long[] ids) {
        Result<Object> result = new Result<>();
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        try {
            int count = specService.deleteSpecBatch(ids);
            if (count > 0) {
                result = new Result<>().ok(null, "规格删除成功");
            } else {
                result = new Result<>().error(SpecResultCodeConstants.ERR_INV_PARAMS, "参数错误");
            }
        } catch (Exception e) {
            log.error("删除规格数据异常：{}", e);
            new Result().error("规格删除失败");
        }
        return result;
    }

    @GetMapping("check/name/add")
    @ApiOperation("新增规格校验规格名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specName", value = "规格名称", required = true, paramType = "query", dataType = "String")
    })

    public Result checkSpecNameWhenAdd(@RequestParam("specName") String specName) {
        Map<String, Object> map = specService.checkSpecNameWhenAdd(specName);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @GetMapping("check/name/update")
    @ApiOperation("修改规格校验规格名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specName", value = "规格名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "规格id", required = true, paramType = "query", dataType = "Long")
    })
    public Result checkSpecName(@RequestParam("specName") String specName,
                                @RequestParam("id") Long id) {
        Map<String, Object> map = specService.checkSpecNameWhenUpdate(specName, id);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

}
