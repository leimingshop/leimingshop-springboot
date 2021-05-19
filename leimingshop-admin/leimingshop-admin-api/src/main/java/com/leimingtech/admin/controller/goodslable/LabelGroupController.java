/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.goodslable;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.SentinelBadRequestException;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.constants.spec.SpecResultCodeConstants;
import com.leimingtech.modules.dto.goodslable.GoodsGroupFindDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupSaveDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupUpdateDTO;
import com.leimingtech.modules.enums.spec.GroupStatusEnum;
import com.leimingtech.modules.service.goodslabel.LabelGroupService;
import com.leimingtech.modules.statuscode.GoodsLabelGroupStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 标签分组表
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@RestController
@RequestMapping("labelgroup")
@Api(tags = "商品标签分组")
public class LabelGroupController {

    @Autowired
    private LabelGroupService labelGroupService;

    @GetMapping("page")
    @ApiOperation("分页查询分组信息")
    @LogContext(note = GoodsLabelGroupStatusCode.LABEL_GROUP_PAGE_SUCCESS_MESSAGE, code = GoodsLabelGroupStatusCode.LABEL_GROUP_PAGE_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "id", value = "编号/名称", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "groupStatus", value = "分组状态(默认1:启用,2:禁用）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<LabelGroupDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<LabelGroupDTO> page = labelGroupService.page(params);

        return new Result<PageData<LabelGroupDTO>>().ok(page, "分页查询成功");
    }

    @GetMapping("group/label/{id}")
    @ApiOperation("根据ID查询分组,标签信息")
    @LogContext(note = GoodsLabelGroupStatusCode.LABEL_GROUP_BY_ID_SUCCESS_MESSAGE, code = GoodsLabelGroupStatusCode.LABEL_GROUP_BY_ID_SUCCESS_CODE)
    public Result<GoodsGroupFindDTO> findByGroupId(@PathVariable("id") Long id) {
        GoodsGroupFindDTO data = labelGroupService.findByGroupId(id);
        if (null == data) {
            return new Result<GoodsGroupFindDTO>().error("标签分组不存在");
        }

        return new Result<GoodsGroupFindDTO>().ok(data, "查询分组,标签成功");
    }


    @PostMapping
    @ApiOperation("保存分组信息")
    @LogContext(note = GoodsLabelGroupStatusCode.LABEL_GROUP_SAVE_SUCCESS_MESSAGE, code = GoodsLabelGroupStatusCode.LABEL_GROUP_SAVE_SUCCESS_CODE)
    public Result save(@RequestBody LabelGroupSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        //移除空格
        String groupName = StringUtils.remove(dto.getGroupName(), " ");
        dto.setGroupName(groupName);
        //校验数据库分组名称数量
        int count = labelGroupService.checkGroupNameSave(dto.getGroupName());
        if (count > 0) {
            return new Result().error("分组名称不可重复");
        }
        labelGroupService.save(dto);
        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("修改分组信息")
    @LogContext(note = GoodsLabelGroupStatusCode.LABEL_GROUP_UPDATE_MESSAGE, code = GoodsLabelGroupStatusCode.LABEL_GROUP_UPDATE_CODE)
    public Result update(@RequestBody LabelGroupUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        //移除空格
        String groupName = StringUtils.remove(dto.getGroupName(), " ");
        dto.setGroupName(groupName);
        //校验分组名称数量
        int count = labelGroupService.checkGroupNameUpate(dto);
        if (count > 0) {
            return new Result().error("分组名称不可重复");
        }
        int i = labelGroupService.update(dto);
        if (i > 0) {
            return new Result().ok(null, "修改成功");
        }

        return new Result().error("修改失败");
    }

    @DeleteMapping
    @ApiOperation("批量删除分组信息")
    @LogContext(note = GoodsLabelGroupStatusCode.LABEL_GROUP_DELETE_MESSAGE, code = GoodsLabelGroupStatusCode.LABEL_GROUP_DELETE_CODE)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        labelGroupService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    @PutMapping("status")
    @ApiOperation("修改分组状态(1启用,2禁用)")
    @LogContext(note = GoodsLabelGroupStatusCode.LABEL_GROUP_UPDATE_STATUS_MESSAGE, code = GoodsLabelGroupStatusCode.LABEL_GROUP_UPDATE_STATUS_CODE)
    public Result updateGroupStatus(@RequestParam("groupStatus") Integer groupStatus, @RequestParam("id") Long id) {
        try {
            if (groupStatus == 1 || groupStatus == 2) {
                int count = labelGroupService.updateGroupStatus(groupStatus, id);
                if (count > 0) {
                    return new Result<>().ok(null, "修改分组状态成功");
                } else {
                    return new Result().error("修改分组状态失败");
                }
            }
            return new Result().error(SpecResultCodeConstants.ERR_INV_PARAMS, "传入分组状态不合法");
        } catch (SentinelBadRequestException e) {
            return new Result().error(e.getMessage());
        }
    }

    @GetMapping("list")
    @ApiOperation("查询启用状态所有标签分组信息")
    @LogContext(note = GoodsLabelGroupStatusCode.LABEL_GROUP_LIST_SUCCESS_MESSAGE, code = GoodsLabelGroupStatusCode.LABEL_GROUP_LIST_SUCCESS_CODE)
    @ApiImplicitParam(name = "groupName", value = "分组名称", paramType = "query", dataType = "String")
    public Result<List<LabelGroupDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("groupStatus", GroupStatusEnum.ENABLED.value());
        List<LabelGroupDTO> data = labelGroupService.list(params);
        if (CollectionUtils.isEmpty(data)) {
            return new Result<List<LabelGroupDTO>>().ok(null, "暂无数据");
        }
        return new Result<List<LabelGroupDTO>>().ok(data, "查询分组列表成功");
    }
}
