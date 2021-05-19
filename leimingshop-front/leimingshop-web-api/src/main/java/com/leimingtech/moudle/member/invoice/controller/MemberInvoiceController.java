/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.invoice.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.dto.invoice.MemberInvoiceDTO;
import com.leimingtech.modules.service.invoice.MemberInvoiceService;
import com.leimingtech.moudle.member.invoice.code.MemberInvoiceCode;
import com.leimingtech.moudle.member.invoice.vo.MemberInvoiceVO;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * pc端地址相关接口
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-05-18 11:43
 **/
@RestController
@RequestMapping("/member/memberinvoice")
@Api(tags = "会员发票管理")
public class MemberInvoiceController {
    @Autowired
    private MemberInvoiceService memberInvoiceService;

    @GetMapping("list")
    @ApiOperation("会员发票列表")
    @LogContext(code = MemberInvoiceCode.MEMBER_INVOICE_LIST_CODE, note = MemberInvoiceCode.MEMBER_INVOICE_LIST_DESC)
    public Result<List<MemberInvoiceVO>> list() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<List<MemberInvoiceVO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Map<String, Object> params = new HashMap<>(1);
        params.put("memberId", SecurityCurrentUser.getUserDetail().getId().toString());
        List<MemberInvoiceDTO> invoiceDTOList = memberInvoiceService.list(params);
        List<MemberInvoiceVO> memberInvoiceVOList = ConvertUtils.sourceToTarget(invoiceDTOList, MemberInvoiceVO.class);
        return new Result<List<MemberInvoiceVO>>().ok(memberInvoiceVOList);
    }


    @GetMapping("detail/{invoiceId}")
    @ApiOperation("发票详情")
    @LogContext(code = MemberInvoiceCode.MEMBER_INVOICE_DETAIL_CODE, note = MemberInvoiceCode.MEMBER_INVOICE_DETAIL_DESC)
    public Result<MemberInvoiceVO> getDetail(@PathVariable("invoiceId") Long invoiceId) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<MemberInvoiceVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        MemberInvoiceDTO memberInvoiceDTO = memberInvoiceService.get(invoiceId);
        MemberInvoiceVO memberInvoiceVO = ConvertUtils.sourceToTarget(memberInvoiceDTO, MemberInvoiceVO.class);
        return new Result<MemberInvoiceVO>().ok(memberInvoiceVO);
    }

    @PutMapping("change/invoice")
    @ApiOperation("提交订单/申请发票时更新用户发票信息")
    public Result changeInvoice(@RequestBody MemberInvoiceVO vo) {
        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        ValidatorUtils.validateEntity(vo, AddGroup.class, DefaultGroup.class);
        MemberInvoiceDTO dto = ConvertUtils.sourceToTarget(vo, MemberInvoiceDTO.class);
        //效验数据
        dto.setMemberId(memberId);
        memberInvoiceService.changeInvoiceInfo(dto);
        return new Result().ok("操作成功");
    }

    @GetMapping("default")
    @ApiOperation("获取用户默认发票")
    @LogContext(code = MemberInvoiceCode.MEMBER_INVOICE_DEFAULT_CODE, note = MemberInvoiceCode.MEMBER_INVOICE_DEFAULT_DESC)
    public Result<MemberInvoiceVO> getDetail() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<MemberInvoiceVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        MemberInvoiceDTO memberInvoiceDTO = memberInvoiceService.getDefaultInvoice(SecurityCurrentUser.getUserDetail().getId());
        MemberInvoiceVO memberInvoiceVO = ConvertUtils.sourceToTarget(memberInvoiceDTO, MemberInvoiceVO.class);
        return new Result<MemberInvoiceVO>().ok(memberInvoiceVO);
    }

    @PostMapping
    @ApiOperation("保存或修改发票")
    @LogContext(code = MemberInvoiceCode.MEMBER_INVOICE_SAVE_UPDATE_CODE, note = MemberInvoiceCode.MEMBER_INVOICE_SAVE_UPDATE_DESC)
    public Result saveOrUpdate(@RequestBody MemberInvoiceVO vo) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<List<MemberInvoiceVO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        ValidatorUtils.validateEntity(vo, AddGroup.class, DefaultGroup.class);
        MemberInvoiceDTO dto = new MemberInvoiceDTO();
        BeanCopier.create(MemberInvoiceVO.class, MemberInvoiceDTO.class, false)
                .copy(vo, dto, null);
        dto.setMemberId(SecurityCurrentUser.getUserDetail().getId());
        memberInvoiceService.saveOrUpdate(dto);
        return new Result().ok("操作成功");
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogContext(code = MemberInvoiceCode.MEMBER_INVOICE_DELETE_CODE, note = MemberInvoiceCode.MEMBER_INVOICE_DELETE_DESC)
    public Result detele(@RequestBody Long[] ids) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<List<MemberInvoiceVO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        memberInvoiceService.delete(ids);
        return new Result().ok("操作成功");
    }

    @PutMapping("setting/default")
    @ApiOperation("设为默认发票")
    @LogContext(code = MemberInvoiceCode.MEMBER_INVOICE_SETTING_DEFAULT_CODE, note = MemberInvoiceCode.MEMBER_INVOICE_SETTING_DEFAULT_DESC)
    public Result detele(@RequestParam("invoiceId") Long invoiceId) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<List<MemberInvoiceVO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        memberInvoiceService.settingDefault(invoiceId, memberId);
        return new Result().ok("操作成功");
    }

}
