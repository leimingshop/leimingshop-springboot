/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.membergrade;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.dto.grade.MemberGradeSaveDTO;
import com.leimingtech.modules.service.grade.MemberGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 会员等级表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@RestController
@RequestMapping("membergrade")
@Api(tags = "会员等级管理")
public class MemberGradeController {
    @Autowired
    private MemberGradeService memberGradeService;

    @GetMapping("page")
    @ApiOperation("会员等级表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gradeName", value = "等级名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<MemberGradeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MemberGradeDTO> page = memberGradeService.page(params);

        return new Result<PageData<MemberGradeDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("会员等级详情")
    public Result<MemberGradeSaveDTO> get(@PathVariable("id") Long id) {
        MemberGradeSaveDTO memberGradeSaveDTO = memberGradeService.getMember(id);

        return new Result<MemberGradeSaveDTO>().ok(memberGradeSaveDTO);
    }

    @PostMapping
    @ApiOperation("会员等级保存")
    @LogOperation("会员等级保存")
    public Result save(@RequestBody MemberGradeSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        // 校验会员等级名称是否重复
        Integer nameCount = memberGradeService.findNameCount(dto.getGradeName(), null, null);
        if (nameCount > 0) {
            return new Result().error("等级名称重复");
        }
        Integer integration = memberGradeService.findNameCount(null, dto.getIntegration(), null);
        if (integration > 0) {
            return new Result().error("该范围的积分已经存在");
        }
        memberGradeService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("会员等级修改")
    @LogOperation("会员等级修改")
    public Result update(@RequestBody MemberGradeDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        //验证是否是最低等级
        MemberGradeSaveDTO member = memberGradeService.getMember(dto.getId());
        if (member.getIntegration() == 0 && !member.getIntegration().equals(dto.getIntegration())) {
            return new Result().error("此等级积分不可修改");
        }

        // 校验会员等级名称是否重复
        Integer nameCount = memberGradeService.findNameCount(dto.getGradeName(), null, dto.getId());
        if (nameCount > 0) {
            return new Result().error("等级名称重复");
        }
        Integer integration = memberGradeService.findNameCount(null, dto.getIntegration(), dto.getId());
        if (integration > 0) {
            return new Result().error("该范围的积分已经存在");
        }

        memberGradeService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    @DeleteMapping
    @ApiOperation("会员等级删除")
    @LogOperation("会员等级删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        //验证是否是最低等级
        for (Long id : ids) {
            MemberGradeSaveDTO member = memberGradeService.getMember(id);
            if (member.getIntegration() == 0) {
                return new Result().error(member.getGradeName() + "不可删除");
            }
        }


        memberGradeService.logicDelete(ids);

        return new Result().ok(null, "删除成功");
    }

    @PutMapping("state/{id}")
    @ApiOperation("会员等级状态修改")
    @LogOperation("会员等级状态修改")
    @ApiImplicitParam(name = "id", value = "等级id", paramType = "query", required = true, dataType = "Long")
    public Result updateState(@PathVariable("id") Long id) {
        //memberGradeService.updateState(id)
        return new Result().ok(null, "状态修改成功");
    }

    @GetMapping("list")
    @ApiOperation("会员等级列表")
    @LogOperation("会员等级列表")
    public Result list() {

        List<MemberGradeDTO> list = memberGradeService.list(new HashMap<>(16));

        return new Result().ok(list, "查询成功");
    }
}
