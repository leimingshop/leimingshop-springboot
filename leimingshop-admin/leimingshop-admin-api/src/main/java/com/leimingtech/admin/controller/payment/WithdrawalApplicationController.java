/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.payment;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.payment.WithdrawalApplicationDTO;
import com.leimingtech.modules.dto.payment.WithdrawalAuditDTO;
import com.leimingtech.modules.excel.payment.WithdrawalApplicationExcel;
import com.leimingtech.modules.service.payment.WithdrawalApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 提现申请
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@RestController
@RequestMapping("withdrawalapplication")
@Api(tags = "提现申请")
public class WithdrawalApplicationController {

    @Autowired
    private WithdrawalApplicationService withdrawalApplicationService;

    @GetMapping("page")
    @ApiOperation("提现申请审核分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankPeople", value = "收款人姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditStatus", value = "提现申请进度（1提现申请中,2申请取消,3提现申请驳回,4提现审核通过发放申请中,5发放驳回,6发放通过）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "申请开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "申请结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<WithdrawalApplicationDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<WithdrawalApplicationDTO> page = withdrawalApplicationService.page(params);

        return new Result<PageData<WithdrawalApplicationDTO>>().ok(page);
    }


    @GetMapping("export")
    @ApiOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<WithdrawalApplicationDTO> list = withdrawalApplicationService.list(params);

        EasyExcelUtils.exportExcelToTarget(response, null, list, WithdrawalApplicationExcel.class);
    }

    /**
     * 提现申请审核通过或者驳回
     *
     * @param dto
     * @return
     * @date 2020-10-20 16:32
     * @author huangkeyuan@leimingtech.com
     **/
    @PutMapping("audit")
    @ApiOperation("提现申请审核通过或者驳回")
    public Result audit(@RequestBody WithdrawalAuditDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        withdrawalApplicationService.audit(dto);

        return new Result();
    }

    @GetMapping("issue/page")
    @ApiOperation("提现发放审核分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankPeople", value = "收款人姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "issueStatus", value = "提现发放进度（1提现申请中,2申请取消,3提现申请驳回,4提现审核通过发放申请中,5发放驳回,6发放通过\n）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "申请开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "申请结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<WithdrawalApplicationDTO>> issuePage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<WithdrawalApplicationDTO> page = withdrawalApplicationService.issuePage(params);

        return new Result<PageData<WithdrawalApplicationDTO>>().ok(page);
    }

    /**
     * 提现发放审核通过或者驳回
     *
     * @param dto
     * @return
     * @date 2020-10-20 16:32
     * @author huangkeyuan@leimingtech.com
     **/
    @PutMapping("issue")
    @ApiOperation("提现发放审核通过或者驳回")
    public Result issue(@RequestBody WithdrawalAuditDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        withdrawalApplicationService.issue(dto);

        return new Result();
    }

    /**
     * 提现记录分页
     *
     * @param params
     * @return
     * @date 2020-10-20 18:58
     * @author huangkeyuan@leimingtech.com
     **/
    @GetMapping("record/page")
    @ApiOperation("提现记录分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankPeople", value = "收款人姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankAccount", value = "收款账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "issueStatus", value = "提现成功6、驳回记录3）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "支付开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "支付结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<WithdrawalApplicationDTO>> recordPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<WithdrawalApplicationDTO> page = withdrawalApplicationService.recordPage(params);

        return new Result<PageData<WithdrawalApplicationDTO>>().ok(page);
    }

}
