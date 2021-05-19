/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import com.alibaba.fastjson.JSON;
import com.leimingtech.modules.constants.PaymentCodeConstants;
import com.leimingtech.modules.dto.alipay.AppAlipayRefundRequestDTO;
import com.leimingtech.modules.dto.payment.wechat.H5WXPayNotifyDTO;
import com.leimingtech.modules.service.alipay.AliPayService;
import com.leimingtech.modules.service.payment.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest()
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class RefundTest {

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private PaymentService paymentService;


    @Test
    public void aliPayRefundTest() {
        AppAlipayRefundRequestDTO refundRequestDTO = new AppAlipayRefundRequestDTO();
        //支付时传入的商户订单号
        refundRequestDTO.setOutTradeNo("1290929422716682241");
        //支付时返回的支付宝交易号
        refundRequestDTO.setOutRequestNo("2020080522001497361440617298");
        // 金额处理h5WxRefundBaseDTO中的金额单位是"分"，支付宝要求的单位是"元"且是字符串类型
        //退款金额，单位（元）
        String finalRefundFee = String.valueOf(100.01);
        refundRequestDTO.setRefundAmount(finalRefundFee);

        log.info("支付宝APP退款请求数据信息：{}", JSON.toJSONString(refundRequestDTO));
        String alipayResponceString = aliPayService.appAlipayRefund(refundRequestDTO);
        log.info("支付宝退款返回数据，{}", alipayResponceString);
    }

    @Test
    public void notifyAlipay() {
        H5WXPayNotifyDTO h5WXPayNotifyDTO = new H5WXPayNotifyDTO();
        h5WXPayNotifyDTO.setResultCode("TRADE_SUCCESS");
        h5WXPayNotifyDTO.setTotalFee("0.01");
        h5WXPayNotifyDTO.setPaySn("1290937469748953089");
        h5WXPayNotifyDTO.setTransactionId("2020080522001453911436914662");
        paymentService.finishPay(h5WXPayNotifyDTO, "2020080522001453911436914662", PaymentCodeConstants.ALIPAY);
    }

}
