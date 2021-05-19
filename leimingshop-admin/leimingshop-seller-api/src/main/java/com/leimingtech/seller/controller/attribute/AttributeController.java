/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.attribute;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.attribute.AttributeAndAttributeValueDTO;
import com.leimingtech.modules.dto.attribute.AttributeDTO;
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
