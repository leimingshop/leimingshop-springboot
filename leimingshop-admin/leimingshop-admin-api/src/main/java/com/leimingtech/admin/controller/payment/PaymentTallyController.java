/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.payment;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.payment.PaymentTallyDTO;
import com.leimingtech.modules.dto.payment.PaymentTallyExcelDTO;
import com.leimingtech.modules.service.payment.PaymentTallyService;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @Author: weixianchun
 * @Description: 支付流水管理
 * @Date :2019/6/18 11:55
 * @Version V1.0
 **/
@RestController
@RequestMapping("payment/tally")
@Api(tags = "支付流水管理")
@Slf4j
public class PaymentTallyController {

    @Autowired
    private PaymentTallyService paymentTallyService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tradeSn", value = "交易流水号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentCode", value = "交易渠道:WX(微信) ZFB(支付宝) YL(银联)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentSn", value = "交易编号", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "buyerName", value = "用户昵称(买家名称)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "transactionStatus", value = "收支状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "交易开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "交易结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<PaymentTallyDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<PaymentTallyDTO> page = paymentTallyService.page(params);

        return new Result<PageData<PaymentTallyDTO>>().ok(page, "查询成功");
    }

    @GetMapping("export")
    @ApiOperation("导出支付流水信息")
    @LogOperation("导出支付流水信息")
    @LogContext(code = PaymentStatusCode.ADMIN_PAYMENT_TALLY_EXPORT_CODE, note = PaymentStatusCode.ADMIN_PAYMENT_TALLY_EXPORT_CODE_MESSAGE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tradeSn", value = "交易流水号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentName", value = "交易渠道:微信 支付宝 银联", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentSn", value = "交易编号", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "buyerName", value = "用户昵称(买家名称)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "transactionStatus", value = "收支状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "交易开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "交易结束时间", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<PaymentTallyExcelDTO> list = paymentTallyService.listExport(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, PaymentTallyExcelDTO.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}