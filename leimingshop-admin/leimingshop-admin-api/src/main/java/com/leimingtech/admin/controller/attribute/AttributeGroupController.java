/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.attribute;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.constants.attribute.AttrResultCodeConstants;
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dto.attribute.AttrGroupAndAttrDTO;
import com.leimingtech.modules.dto.attribute.AttributeGroupDTO;
import com.leimingtech.modules.dto.attribute.InsertAttributeGroupDTO;
import com.leimingtech.modules.dto.attribute.UpdateAttributeGroupDTO;
import com.leimingtech.modules.enums.attribute.GroupStatusEnum;
import com.leimingtech.modules.service.attribute.AttributeGroupService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 属性分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Slf4j
@RestController
@RequestMapping("attributegroup")
@Api(tags = "属性分组管理")
public class AttributeGroupController {
    @Autowired
    private AttributeGroupService attributeGroupService;

    @GetMapping("page")
    @ApiOperation("属性分组分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupName", value = "分组名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupStatus", value = "属性分组状态 1启用 2停用", paramType = "query", dataType = "Integer")
    })
    public Result<PageData<AttributeGroupDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<AttributeGroupDTO> page = attributeGroupService.page(params);
        if (null != page && CollectionUtils.isNotEmpty(page.getList())) {
            return new Result<PageData<AttributeGroupDTO>>().ok(page, "查询属性分组成功");
        } else {
            return new Result<PageData<AttributeGroupDTO>>().ok(null, "暂无属性分组数据");
        }
    }

    @GetMapping("{id}")
    @ApiOperation("查询属性分组详情")
    public Result<AttrGroupAndAttrDTO> get(@PathVariable("id") Long id) {
        AttrGroupAndAttrDTO attrGroupAndAttr = attributeGroupService.findAttrGroupAndAttr(id);
        if (attrGroupAndAttr == null) {
            return new Result<AttrGroupAndAttrDTO>().ok(null, "属性分组不存在");
        }

        return new Result<AttrGroupAndAttrDTO>().ok(attrGroupAndAttr, "查询属性分组成功");
    }

    @PostMapping
    @ApiOperation("保存属性分组")
    @LogOperation("保存属性分组")
    public Result save(@RequestBody InsertAttributeGroupDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        Map<String, Object> map = attributeGroupService.saveAttrGroup(dto);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @PutMapping
    @ApiOperation("修改属性分组")
    @LogOperation("修改属性分组")
    public Result update(@RequestBody UpdateAttributeGroupDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        Map<String, Object> map = attributeGroupService.updateAttrGroup(dto);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除属性分组")
    @LogOperation("删除属性分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "属性分组id", required = true, paramType = "path", dataType = "Long")
    })
    public Result delete(@PathVariable("id") Long id) {
        int count = attributeGroupService.deleteAttrGroup(id);
        if (count > 0) {
            return new Result<>().ok(null, "删除属性分组成功");
        } else {
            return new Result().error(SpecResultCodeConstants.ERR_NO_RESULT, "属性分组不存在");
        }
    }

    @DeleteMapping("batch")
    @ApiOperation("批量删除属性分组")
    @LogOperation("批量删除属性分组")
    public Result deleteAttrGroupBatch(@RequestBody Long[] ids) {
        int count = attributeGroupService.deleteAttrGroupBatch(ids);
        if (count > 0) {
            return new Result<>().ok(null, "删除属性分组成功");
        } else {
            return new Result().error(SpecResultCodeConstants.ERR_NO_RESULT, "属性分组不存在");
        }
    }

    @GetMapping("check/add")
    @ApiOperation("新增属性分组校验属性分组名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrtGroupName", value = "属性分组名称", required = true, paramType = "query", dataType = "String")
    })
    public Result checkAttrGroupNameWhenAdd(@RequestParam("arrtGroupName") String arrtGroupName) {
        Map<String, Object> map = attributeGroupService.checkAttrGroupNameWhenAdd(arrtGroupName);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @GetMapping("check/update")
    @ApiOperation("修改属性分组校验属性分组名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrtGroupName", value = "属性分组名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "属性分组id", required = true, paramType = "query", dataType = "Long")
    })
    public Result checkAttrGroupNameWhenUpdate(@RequestParam("arrtGroupName") String arrtGroupName,
                                               @RequestParam("id") Long id) {
        Map<String, Object> map = attributeGroupService.checkAttrGroupNameWhenUpdate(arrtGroupName, id);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    /**
     * 功能描述:
     * 〈查询属性分组及属性列表〉
     *
     * @param groupName 属性分组名称
     * @author : 刘远杰
     */
    @GetMapping("list/spec")
    @ApiOperation("查询属性分组及属性列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "属性分组名称", paramType = "query", dataType = "String")
    })
    public Result<List<AttrGroupAndAttrDTO>> listSpec(@RequestParam(value = "groupName", required = false) String groupName) {
        Map<String, Object> params = new HashMap<>(16);
        if (StringUtils.isNotBlank(groupName)) {
            params.put("groupName", groupName);
        }
        List<AttrGroupAndAttrDTO> attrGroupAndAttrDTOList = attributeGroupService.listAttr(params);
        if (CollectionUtils.isNotEmpty(attrGroupAndAttrDTOList)) {
            return new Result<List<AttrGroupAndAttrDTO>>().ok(attrGroupAndAttrDTOList, "查询成功");
        } else {
            return new Result<List<AttrGroupAndAttrDTO>>().error(SpecResultCodeConstants.ERR_NO_RESULT, "暂无数据");
        }
    }


    @PostMapping("status")
    @ApiOperation("启用/停用属性分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "属性分组id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "groupStatus", value = "分组状态  1启用 2停用", required = true, paramType = "query", dataType = "int")
    })
    public Result enabledGroupStatus(@RequestParam("groupStatus") Integer groupStatus, @RequestParam("id") Long id) {
        if (groupStatus == GroupStatusEnum.ENABLED.value() || groupStatus == GroupStatusEnum.NOTENABLED.value()) {
            int count = attributeGroupService.updateGroupStatus(groupStatus, id);
            if (count > 0) {
                return new Result<>().ok(null, "更新属性分组状态成功");
            } else {
                return new Result().error("更新属性分组状态失败");
            }
        }
        return new Result().error(AttrResultCodeConstants.ERR_INV_PARAMS, "传入分组状态不合法");

    }

}
