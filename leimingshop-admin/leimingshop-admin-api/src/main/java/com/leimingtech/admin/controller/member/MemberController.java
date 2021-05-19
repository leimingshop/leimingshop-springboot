/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.member;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.dto.balance.BatchChangeBalanceDTO;
import com.leimingtech.modules.dto.balance.MemberChangeBalanceDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.member.MemberListDTO;
import com.leimingtech.modules.dto.member.MemberUpdateDTO;
import com.leimingtech.modules.enums.balance.BalanceEnum;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.utils.MobileUtil;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.sys.SysExportManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 会员表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@RestController
@RequestMapping("member")
@Api(tags = "会员管理")
@Slf4j
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private SysExportManagementService sysExportManagementService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("会员列表")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_PAGE_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_PAGE_SUCCESS_MSG)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "Id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gradeId", value = "会员等级id", paramType = "query", dataType = "String")
    })
    public Result<PageData<MemberListDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        //todo  参数过滤查询代修改
        PageData<MemberListDTO> page = memberService.page(params);

        return new Result<PageData<MemberListDTO>>().ok(page);
    }

    /**
     * 分页查询(站内信)
     *
     * @param params
     * @return
     */
    @GetMapping("page/message")
    @ApiOperation("会员列表(站内信)")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_MESSAGE_PAGE_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_MESSAGE_PAGE_SUCCESS_MSG)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "查询类型 默认会员:0,邮箱:2,姓名:3", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberName", value = "对应查询", paramType = "query", dataType = "String")
    })
    public Result<PageData<MemberDTO>> pageMessage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MemberDTO> page = memberService.pageMessage(params);

        return new Result<PageData<MemberDTO>>().ok(page);
    }

    /**
     * 查询会员详情
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("会员详情")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_DETAILS_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_DETAILS_SUCCESS_MSG)
    public Result<MemberVoDTO> get(@PathVariable("id") Long id) {
        MemberVoDTO memberVoDto = memberService.selectMemberById(id);

        //查看
        return new Result<MemberVoDTO>().ok(memberVoDto);
    }


    /**
     * 修改
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("会员信息修改")
    @LogOperation("会员信息修改")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_UPDATE_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_UPDATE_SUCCESS_MSG)
    public Result update(@RequestBody MemberUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        //修改用户基本信息
        memberService.updateMember(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 会员编辑信息查询
     *
     * @param id
     * @return
     */
    @GetMapping("edit/{id}")
    @ApiOperation("会员编辑信息查询")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_EDIT_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_EDIT_SUCCESS_MSG)
    public Result selectMemberUpdate(@PathVariable Long id) {

        MemberUpdateDTO memberUpdateDTO = memberService.selectMemberUpdateDTO(id);

        return new Result().ok(memberUpdateDTO, "查询成功");
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除会员")
    @LogOperation("删除会员")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_DEL_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_DEL_SUCCESS_MSG)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "至少选中一个会员");

        memberService.logicDelete(ids);

        return new Result().ok(null, "删除成功");
    }

    @GetMapping("export")
    @ApiOperation("用户信息导出")
    @LogOperation("用户信息导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberName", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "Id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gradeId", value = "会员等级id", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {

        // 保存导出记录表 状态为导出中
        String assignmentName = ExcelEnum.MEMBER_EXPORT.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(0L);
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);
        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 20);
        params.put("managementId", sysExportManagementVO.getId());
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_MEMBER_EXPORT_QUEUE, JSON.toJSONString(params));
    }

    /**
     * 获取全部用户
     *
     * @return
     */
    @GetMapping("list")
    public List<MemberDTO> getAllMember() {
        return memberService.selectAllMember(null);
    }

    /**
     * 重置密码
     *
     * @param params
     * @return
     */
    @PutMapping("reset/passwd")
    @ApiOperation("重置密码")
    @LogOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员id", paramType = "query", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", required = true, dataType = "String")
    })
    public Result resetPasswd(@ApiIgnore @RequestParam Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String mobile = params.get("mobile").toString();

        if (!MobileUtil.isMobile(mobile)) {
            return new Result().error("请输入正确手机号");
        }

        memberService.resetPasswd(id, mobile);

        return new Result().ok(null, "验证码已发送");
    }

    @PutMapping("state/{id}")
    @ApiOperation("修改用户状态")
    @LogOperation("修改用户状态")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_STATE_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_STATE_SUCCESS_MSG)
    @ApiImplicitParam(name = "id", value = "用户id", paramType = "query", required = true, dataType = "Long")
    public Result updateSta(@PathVariable("id") Long id) {
        Map<String, Object> res = memberService.updateState(id);
        if (CollectionUtil.isNotEmpty(res)) {
            return new Result().error(Integer.valueOf(res.get("code").toString()),
                    res.get("msg").toString());
        }
        return new Result().ok("null", "修改成功");
    }

    @PutMapping("change/balance")
    @ApiOperation("变更用户余额")
    @LogOperation("变更用户余额")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_STATE_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_STATE_SUCCESS_MSG)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "用户ID", paramType = "query", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "memberName", value = "用户名", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "balance", value = "变更余额", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "query", dataType = "String")
    })
    public Result changeMemberBalance(@ApiIgnore @RequestParam Map<String, Object> params) {
        AssertUtils.isMapEmpty(params, ErrorCode.PARAMS_GET_ERROR, "提交参数不能为空");
        if (null == params.get("memberId") || null == params.get("memberName")) {
            return new Result().error(ErrorCode.PARAMS_GET_ERROR, "用户信息不能为空");
        }
        if (null == params.get("balance")) {
            return new Result().error(ErrorCode.PARAMS_GET_ERROR, "用户信息不能为空");
        }
        MemberChangeBalanceDTO memberChangeBalanceDTO = new MemberChangeBalanceDTO();
        memberChangeBalanceDTO.setMemberId(Long.valueOf(params.get("memberId").toString()));
        memberChangeBalanceDTO.setChangeBalance(new BigDecimal(params.get("balance").toString()));
        memberChangeBalanceDTO.setBalanceType(BalanceEnum.ADMIN_CHANGE.value());
        memberChangeBalanceDTO.setMemberName(params.get("memberName").toString());
        memberChangeBalanceDTO.setRemark(ObjectUtil.isNotNull(params.get("remark")) ? params.get("remark").toString() : null);
        memberInfoService.changeMemberBalance(memberChangeBalanceDTO);
        return new Result().ok("null", "修改成功");
    }

    @PutMapping("batch/change/balance")
    @ApiOperation("变更用户余额")
    @LogOperation("变更用户余额")
    @LogContext(code = MemberStatusCode.ADMIN_MEMBER_STATE_SUCCESS_CODE, note = MemberStatusCode.ADMIN_MEMBER_STATE_SUCCESS_MSG)
    public Result batchChangeMemberBalance(@RequestBody BatchChangeBalanceDTO batchChangeBalanceDTO) {
        ValidatorUtils.validateEntity(batchChangeBalanceDTO, AddGroup.class, DefaultGroup.class);
        if (1 != batchChangeBalanceDTO.getChangeType() && 0 != batchChangeBalanceDTO.getChangeType()) {
            return new Result().error("null", "未选择正确的余额变更类型");
        }
        if (1 == batchChangeBalanceDTO.getChangeType() && batchChangeBalanceDTO.getChangeBalance().compareTo(BigDecimal.ZERO) == -1) {
            return new Result().error("null", "余额不能小于0");
        }
        if (CollectionUtils.isEmpty(batchChangeBalanceDTO.getMemberNameDTOList())) {
            return new Result().error("null", "请选择要调整余额的用户");
        }
        batchChangeBalanceDTO.setBalanceType(BalanceEnum.ADMIN_CHANGE.value());
        memberInfoService.batchChangeBalance(batchChangeBalanceDTO);

        return new Result().ok("null", "修改成功");
    }


}
