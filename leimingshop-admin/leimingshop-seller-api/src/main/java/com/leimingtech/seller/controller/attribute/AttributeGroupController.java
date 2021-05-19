/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.attribute;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dto.attribute.AttrGroupAndAttrDTO;
import com.leimingtech.modules.dto.attribute.AttributeGroupDTO;
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
        if (CollectionUtils.isNotEmpty(page.getList())) {
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


}
