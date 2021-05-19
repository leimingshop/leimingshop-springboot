/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.payment.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.exception.ServiceStatusCode;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.payment.PaymentDTO;
import com.leimingtech.modules.dto.payment.alipay.AliPayAppReqestDTO;
import com.leimingtech.modules.dto.payment.alipay.AliPayAppResponseDTO;
import com.leimingtech.modules.dto.payment.wechat.ApiWXPayDTO;
import com.leimingtech.modules.dto.payment.wechat.H5WXPayRequestDTO;
import com.leimingtech.modules.dto.payment.wechat.PCPayResponseDTO;
import com.leimingtech.modules.service.payment.PaymentService;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import com.leimingtech.moudle.payment.code.PaymentCode;
import com.leimingtech.moudle.payment.vo.PcPayVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 支付模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("payment")
@Api(tags = "支付")
public class PaymentController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * 查询支付方式
     *
     * @return
     */
    @GetMapping("type/{clientType}")
    @ApiOperation("查询支付方式 客户端类型（WEB、WAP、APP、MINI）")
    public Result<List<PaymentDTO>> list(@PathVariable("clientType") String clientType) {
        Map<String, Object> map = new HashMap<>(5);
        map.put("clientType", clientType);
        List<PaymentDTO> list = paymentService.list(map);
        return new Result<List<PaymentDTO>>().ok(list);
    }


    /**
     * PC端统一下单接口
     *
     * @param h5WxPayRequestDTO: 统一下单参数
     * @return pc统一下单接口返回数据
     * @date 2020/5/20 9:43
     * @author lixiangx@leimingtech.com
     **/
    @PostMapping("pc")
    @ApiOperation("PC微信支付")
    @LogContext(code = PaymentCode.PC_PAY_ORDER_CODE, note = PaymentCode.PC_PAY_ORDER_DESC)
    public Result<PcPayVO> payByPaySn(@RequestBody H5WXPayRequestDTO h5WxPayRequestDTO) {

        // 校验参数
        ValidatorUtils.validateEntity(h5WxPayRequestDTO, AddGroup.class);

        Map<String, String> logMap = new HashMap<>(3);
        logMap.put("paySn", h5WxPayRequestDTO.getPaySn().toString());

        ApiWXPayDTO wxPayDTO = new ApiWXPayDTO();

        wxPayDTO.setPaySn(h5WxPayRequestDTO.getPaySn().toString());
        wxPayDTO.setIp(IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
        wxPayDTO.setOpenId(h5WxPayRequestDTO.getOpenId());
        // 微信JSAPI支付或者h5支付
        wxPayDTO.setTradeType("NATIVE");
        try {
            PCPayResponseDTO pcPayResponseDTO = paymentService.pcWechatPay(wxPayDTO);
            Optional.ofNullable(pcPayResponseDTO).orElseThrow(() -> new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR));

            return new Result<PcPayVO>().ok(ConvertUtils.sourceToTarget(pcPayResponseDTO, PcPayVO.class));
        } catch (Exception e) {
            log.error("微信去支付异常," + e);
            throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
        }

    }

    /**
     * pc支付宝支付，根据支付单号调用支付宝下单接口
     *
     * @return
     * @date 2020-05-20 12:24
     * @author huangkeyuan@leimingtech.com
     **/
    @PostMapping(value = "pcpay")
    @ApiOperation("pc支付宝支付，根据支付单号调用支付宝下单接口")
    @LogContext(code = PaymentCode.PC_ALI_PAY_ORDER_CODE, note = PaymentCode.PC_ALI_PAY_ORDER_DESC)
    public Result<String> pcPayByPaySn(@RequestBody AliPayAppReqestDTO aliPayAppRequestDTO) {
        // 校验参数
        ValidatorUtils.validateEntity(aliPayAppRequestDTO, AddGroup.class);

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("paySn", aliPayAppRequestDTO.getPaySn());

        try {
            AliPayAppResponseDTO aliPayAppResponseDTO = paymentService.aliPayPcCertUnifiedOrder(aliPayAppRequestDTO);
            if (aliPayAppResponseDTO != null && aliPayAppResponseDTO.getCode() == ErrorCode.SUCCESS) {
                mlogger.info(PaymentStatusCode.ALI_PRE_PAY_PAYSN_SUCCESS.getCode(), PaymentStatusCode.ALI_PRE_PAY_PAYSN_SUCCESS.getMessage(), logMap);
                return new Result<String>().ok(aliPayAppResponseDTO.getOrderString());
            } else {
                throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
            }
        } catch (Exception e) {
            mlogger.info(PaymentStatusCode.H5_ALI_PAY_PAYSN_SERVER_INTERNAL_ERROR_CODE,
                    PaymentStatusCode.H5_ALI_PAY_PAYSN_SERVER_INTERNAL_ERROR_MESSAGE, logMap, e);
            log.error("支付宝去支付异常," + e);
            throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
        }
    }


}
