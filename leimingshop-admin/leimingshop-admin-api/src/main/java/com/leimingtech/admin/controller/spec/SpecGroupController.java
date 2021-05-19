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
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dto.spec.InsertSpecGroupDTO;
import com.leimingtech.modules.dto.spec.SpecGroupAndSpecDTO;
import com.leimingtech.modules.dto.spec.SpecGroupDTO;
import com.leimingtech.modules.dto.spec.UpdateSpecGroupDTO;
import com.leimingtech.modules.enums.spec.GroupStatusEnum;
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

    @PostMapping
    @ApiOperation("保存规格分组")
    @LogOperation("保存规格分组")
    public Result save(@RequestBody InsertSpecGroupDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        try {
            Map<String, Object> map = specGroupService.saveSpecGroup(dto);
            return new Result<>().error((int) map.get("code"), (String) map.get("message"));
        } catch (Exception e) {
            log.error("保存规格分组数据异常：", e);
            return new Result().error("保存规格分组失败");
        }
    }

    @PutMapping
    @ApiOperation("修改规格分组")
    @LogOperation("修改规格分组")
    public Result update(@RequestBody UpdateSpecGroupDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        try {
            Map<String, Object> map = specGroupService.updateSpecGroup(dto);
            return new Result<>().error((int) map.get("code"), (String) map.get("message"));
        } catch (Exception e) {
            log.error("修改规格分组数据异常：", e);
            return new Result().error("修改规格分组失败");
        }
    }


    @DeleteMapping("{id}")
    @ApiOperation("删除规格分组")
    @LogOperation("删除规格分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "规格分组id", required = true, paramType = "path", dataType = "Long")
    })
    public Result delete(@PathVariable("id") Long id) {

        int count = specGroupService.deleteSpecGroup(id);
        if (count > 0) {
            return new Result<>().ok(null, "删除规格分组成功");
        } else {
            return new Result().error(SpecResultCodeConstants.ERR_NO_RESULT, "规格分组不存在");
        }
    }

    @DeleteMapping("batch")
    @ApiOperation("批量删除规格分组")
    @LogOperation("批量删除规格分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "规格分组索引数组", required = true, paramType = "body", dataType = "Long[]")
    })
    public Result deleteBatch(@RequestBody Long[] ids) {
        Result<Object> result = new Result<>();
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        try {
            int count = specGroupService.deleteSpecGroupBatch(ids);
            if (count > 0) {
                result = new Result<>().ok(null, "规格分组删除成功");
            } else {
                result = new Result<>().error(SpecResultCodeConstants.ERR_INV_PARAMS, "参数错误");
            }
        } catch (Exception e) {
            log.error("删除规格分组数据异常：{}", e);
            new Result().error("规格删除分组失败");
        }
        return result;
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

    @PostMapping("status")
    @ApiOperation("启用/停用规格分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "规格分组id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "groupStatus", value = "分组状态 1启用 2停用", required = true, paramType = "query", dataType = "int")
    })
    public Result enabledGroupStatus(@RequestParam("groupStatus") Integer groupStatus, @RequestParam("id") Long id) {
        if (groupStatus == GroupStatusEnum.ENABLED.value() || groupStatus == GroupStatusEnum.NOTENABLED.value()) {
            int count = specGroupService.updateGroupStatus(groupStatus, id);
            if (count > 0) {
                return new Result<>().ok(null, "更新规格分组状态成功");
            } else {
                return new Result().error("更新规格分组状态失败");
            }
        }
        return new Result().error(SpecResultCodeConstants.ERR_INV_PARAMS, "传入分组状态不合法");

    }

}
