/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.spec;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dto.spec.SpecGroupAndSpecDTO;
import com.leimingtech.modules.dto.spec.SpecGroupDTO;
import com.leimingtech.modules.service.spec.SpecGroupService;
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
 * 规格分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Slf4j
@RestController
@RequestMapping("specgroup")
@Api(tags = "规格分组管理")
public class SpecGroupController {
    @Autowired
    private SpecGroupService specGroupService;

    @GetMapping("page")
    @ApiOperation("规格分组分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupName", value = "分组名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupStatus", value = "规格分组状态 1启用 2停用", paramType = "query", dataType = "Integer")
    })
    public Result<PageData<SpecGroupDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SpecGroupDTO> page = specGroupService.page(params);
        return new Result<PageData<SpecGroupDTO>>().ok(page, "查询规格分组成功");
    }

    /**
     * 功能描述:
     * 〈查询规格分组及规格列表〉
     *
     * @param groupName 规格分组名称
     * @author : 刘远杰
     */
    @GetMapping("list/spec")
    @ApiOperation("查询规格分组及规格列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "规格分组名称", required = false, paramType = "query", dataType = "String")
    })
    public Result<List<SpecGroupAndSpecDTO>> listSpec(@RequestParam(value = "groupName", required = false) String groupName) {
        Map<String, Object> params = new HashMap<>(16);
        if (StringUtils.isNotBlank(groupName)) {
            params.put("groupName", groupName);
        }
        List<SpecGroupAndSpecDTO> specGroupAndSpecDTOList = specGroupService.listSpec(params);
        if (CollectionUtils.isNotEmpty(specGroupAndSpecDTOList)) {
            return new Result<List<SpecGroupAndSpecDTO>>().ok(specGroupAndSpecDTOList, "查询成功");
        } else {
            return new Result<List<SpecGroupAndSpecDTO>>().error(SpecResultCodeConstants.ERR_NO_RESULT, "暂无数据");
        }
    }

    @GetMapping("{id}")
    @ApiOperation("查询规格详情")
    public Result<SpecGroupAndSpecDTO> get(@PathVariable("id") Long id) {
        SpecGroupAndSpecDTO specGroupAndSpec = specGroupService.findSpecGroupAndSpec(id);
        if (specGroupAndSpec == null) {
            return new Result<SpecGroupAndSpecDTO>().error(SpecResultCodeConstants.ERR_NO_RESULT, "规格分组不存在");
        }

        return new Result<SpecGroupAndSpecDTO>().ok(specGroupAndSpec, "查询规格分组成功");
    }

    @GetMapping("check/name/add")
    @ApiOperation("新增规格分组校验规格分组名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specGroupName", value = "规格分组名称", required = true, paramType = "query", dataType = "String")
    })
    public Result checkSpecGroupNameWhenAdd(@RequestParam("specGroupName") String specGroupName) {
        Map<String, Object> map = specGroupService.checkSpecGroupNameWhenAdd(specGroupName);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @GetMapping("check/name/update")
    @ApiOperation("修改规格分组校验规格分组名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specGroupName", value = "规格分组名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "规格分组id", required = true, paramType = "query", dataType = "Long")
    })
    public Result checkSpecGroupNameWhenAdd(@RequestParam("specGroupName") String specGroupName,
                                            @RequestParam("id") Long id) {
        Map<String, Object> map = specGroupService.checkSpecGroupNameWhenUpdate(specGroupName, id);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

}
