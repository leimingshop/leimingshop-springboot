/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.attribute;

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
import com.leimingtech.modules.constants.attribute.AttrResultCodeConstants;
import com.leimingtech.modules.dto.attribute.*;
import com.leimingtech.modules.service.attribute.AttributeService;
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
 * 属性表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Slf4j
@RestController
@RequestMapping("attribute")
@Api(tags = "属性管理")
public class AttributeController {
    @Autowired
    private AttributeService attributeService;

    @GetMapping("page")
    @ApiOperation("属性分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "attrName", value = "属性名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupName", value = "属性分组名称", paramType = "query", dataType = "String")
    })
    public Result<PageData<AttributeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<AttributeDTO> page = attributeService.page(params);
        return new Result<PageData<AttributeDTO>>().ok(page, "查询属性数据成功");
    }

    @GetMapping("list")
    @ApiOperation("属性列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attrName", value = "属性名称", paramType = "query", dataType = "String")
    })
    public Result<List<AttributeDTO>> findSpecList(@RequestParam(value = "attrName", required = false) String attrName) {
        Map<String, Object> params = new HashMap<>(16);
        params.put(Constant.ORDER_FIELD, "attr_sort");
        params.put(Constant.ORDER, "desc");
        if (StringUtils.isNotBlank(attrName)) {
            params.put("attrName", attrName);
        }
        List<AttributeDTO> attributeDTOList = attributeService.list(params);
        if (CollectionUtils.isNotEmpty(attributeDTOList)) {
            return new Result<List<AttributeDTO>>().ok(attributeDTOList, "查询属性数据成功");
        } else {
            return new Result<List<AttributeDTO>>().ok(null, "暂无属性");
        }
    }

    @GetMapping("{id}")
    @ApiOperation("查询属性详情")
    public Result<AttributeAndAttributeValueDTO> get(@PathVariable("id") Long id) {
        AttributeAndAttributeValueDTO dto = attributeService.findAttrAndAttrValueByAttrId(id);
        if (dto == null) {
            return new Result<AttributeAndAttributeValueDTO>().ok(null, "暂无数据");
        } else {
            return new Result<AttributeAndAttributeValueDTO>().ok(dto, "查询属性详情成功");
        }
    }

    @PostMapping
    @ApiOperation("保存属性")
    @LogOperation("保存属性")
    public Result save(@RequestBody InsertAttributeDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        List<InsertAttributeValueDTO> attributeValueDTOList = dto.getAttributeValueDTOList();

        if (!this.checkValueName(attributeValueDTOList)) {
            return new Result().error(AttrResultCodeConstants.ERR_INV_PARAMS, "保存失败，属性值名称重复");
        }

        Map<String, Object> map = attributeService.saveAttr(dto);

        return new Result<>().error((int) map.get("code"), (String) map.get("message"));

    }

    /**
     * 功能描述:
     * 〈校验属性值名称是否重复〉
     *
     * @param attributeValueDTOList 属性值集合
     * @author : 刘远杰
     */
    private boolean checkValueName(List<InsertAttributeValueDTO> attributeValueDTOList) {
        // 属性值名称集合
        List<String> valueList = new ArrayList<>();
        for (InsertAttributeValueDTO attributeValueDTO : attributeValueDTOList) {
            // 判断是否与已有的属性值名称重复
            if (valueList.contains(attributeValueDTO.getAttrValueName())) {
                // 已存在
                return false;
            }
            // 添加到属性值名称集合
            valueList.add(attributeValueDTO.getAttrValueName());
        }
        return true;
    }

    @PutMapping
    @ApiOperation("修改属性")
    @LogOperation("修改属性")
    public Result update(@RequestBody UpdateAttributeDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        List<InsertAttributeValueDTO> attributeValueDTOList = dto.getAttributeValueDTOList();

        if (!this.checkValueName(attributeValueDTOList)) {
            return new Result().error(AttrResultCodeConstants.ERR_INV_PARAMS, "修改失败，属性值名称重复");
        }

        Map<String, Object> map = attributeService.updateAttr(dto);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @PostMapping("check/class")
    @ApiOperation("查询属性是否关联后台分类，true 关联 false 未关联")
    public Result<Boolean> checkClass(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "ids");

        List<AttrClassDTO> attrClassDTOS = attributeService.checkClass(ids);

        Boolean flag = true;
        StringBuilder message = new StringBuilder();
        message.append("您选择的属性");
        if (CollectionUtils.isEmpty(attrClassDTOS)) {
            throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
        }
        for (AttrClassDTO attrClassDTO : attrClassDTOS) {
            if (attrClassDTO.getNumber() != 0) {
                flag = false;
                message.append(" ").append(attrClassDTO.getAttrName());
            }
        }
        message.append(" 已关联后台分类，是否确认删除？");
        if (flag) {
            return new Result<Boolean>().ok(flag, "属性未关联后台分类");
        } else {
            return new Result<Boolean>().ok(flag, message.toString());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除属性")
    @LogOperation("删除属性")
    public Result delete(@PathVariable Long id) {
        try {
            Integer count = attributeService.deleteAttr(id);
            if (count > 0) {
                return new Result<>().ok(null, "删除属性成功");
            } else {
                return new Result<>().error(AttrResultCodeConstants.ERR_NO_RESULT, "删除属性失败，不存在对应的属性");
            }
        } catch (Exception e) {
            log.error("删除属性异常：{}", e);
            return new Result().error("修改属性失败");
        }

    }

    @DeleteMapping("batch")
    @ApiOperation("批量删除属性")
    @LogOperation("批量删除属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "属性索引数组", required = true, paramType = "body", dataType = "Long[]")
    })
    public Result deleteAttrBatch(@RequestBody Long[] ids) {
        Result<Object> result = new Result<>();
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        try {
            int count = attributeService.deleteAttrBatch(ids);
            if (count > 0) {
                result = new Result<>().ok(null, "属性删除成功");
            } else {
                result = new Result<>().error(AttrResultCodeConstants.ERR_INV_PARAMS, "参数错误");
            }
        } catch (Exception e) {
            log.error("删除属性数据异常：{}", e);
            new Result().error("属性删除失败");
        }
        return result;
    }

    @GetMapping("check/add")
    @ApiOperation("新增属性校验属性名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attrName", value = "属性名称", required = true, paramType = "query", dataType = "String")
    })
    public Result checkAttrNameWhenAdd(@RequestParam("attrName") String attrName) {
        Map<String, Object> map = attributeService.checkAttrNameWhenAdd(attrName);

        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @GetMapping("check/update")
    @ApiOperation("修改属性校验属性名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attrName", value = "属性名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "属性id", required = true, paramType = "query", dataType = "Long")
    })
    public Result checkSpecName(@RequestParam("attrName") String attrName,
                                @RequestParam("id") Long id) {
        Map<String, Object> map = attributeService.checkAttrNameWhenUpdate(attrName, id);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

}
