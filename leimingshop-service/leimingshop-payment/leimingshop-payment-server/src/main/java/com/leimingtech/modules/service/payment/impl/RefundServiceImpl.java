/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dto.alipay.AlipayRefundRequestDTO;
import com.leimingtech.modules.dto.alipay.AppAlipayRefundRequestDTO;
import com.leimingtech.modules.dto.balance.MemberChangeBalanceDTO;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.payment.H5WxRefundBaseDTO;
import com.leimingtech.modules.dto.payment.PaymentTallyDTO;
import com.leimingtech.modules.dto.wxpay.AppWxRefundDTO;
import com.leimingtech.modules.dto.wxpay.AppWxRefundResponseDTO;
import com.leimingtech.modules.dto.wxpay.H5WxRefundDTO;
import com.leimingtech.modules.dto.wxpay.H5WxRefundResponseDTO;
import com.leimingtech.modules.enums.balance.BalanceEnum;
import com.leimingtech.modules.enums.payment.PaymentCodeEnum;
import com.leimingtech.modules.enums.payment.PaymentIdEnum;
import com.leimingtech.modules.enums.payment.PaymentTallyEnum;
import com.leimingtech.modules.enums.payment.WXRefundStateEnum;
import com.leimingtech.modules.service.alipay.AliPayService;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.payment.PaymentTallyService;
import com.leimingtech.modules.service.payment.RefundService;
import com.leimingtech.modules.service.wxpay.WeChatAppPayService;
import com.leimingtech.modules.service.wxpay.WeChatRefundservice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:微信退款
 * @Date: 2019/6/23 15:52
 * @Version: V1.0
 */
@Service

@Slf4j
public class RefundServiceImpl implements RefundService {

    @Autowired
    private WeChatRefundservice weChatRefundservice;

    @Autowired
    private WeChatAppPayService weChatAppPayService;

    @Autowired
    private PaymentTallyService paymentTallyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private MemberInfoService memberInfoService;

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:所有退款入口
     * @Date: 2019/6/23 15:52
     * @Version: V1.0
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> wechatRefund(@RequestBody H5WxRefundBaseDTO h5WxRefundBaseDTO) {
        try {
            Map<String, Object> resultMap = new HashMap<>();

            log.info("退款数据信息为：{}", JSON.toJSONString(h5WxRefundBaseDTO));
            //封装微信退款实体
            H5WxRefundDTO h5WxRefundDTO = new H5WxRefundDTO();
            BeanCopier.create(H5WxRefundBaseDTO.class, H5WxRefundDTO.class, false).copy(h5WxRefundBaseDTO, h5WxRefundDTO, null);
            //封装配置信息
            h5WxRefundDTO.setNonceStr(IdGenerator.defaultSnowflakeId());

            // 查询当前订单的支付方式，如果是微信走微信退款，支付宝支付走支付宝退款
            List<OrderDTO> orderDTOList = orderService.getByPaySn(Long.parseLong(h5WxRefundBaseDTO.getOuttradeno()));
            if (CollectionUtils.isNotEmpty(orderDTOList)) {
                OrderDTO orderDTO = orderDTOList.get(0);
                Long paymentId = orderDTO.getPaymentId();
                // 判断第三方退款金额是否为0
                //第三方退款金额不为0说明是混合退款
                if (h5WxRefundBaseDTO.getRefundfee() != 0) {
                    log.info("微信退款请求数据信息：{}", JSON.toJSONString(h5WxRefundDTO));
                    // 如果支付方式为微信则调用thirdparty中的的微信退款，否则支付退款
                    // 微信h5、pc支付
                    if (paymentId.equals(PaymentIdEnum.WECHAT_WAP_PAY.value())) {
                        //调用third-party微信退款服务
                        return wechatRefund(h5WxRefundBaseDTO, resultMap, h5WxRefundDTO);

                        // 支付宝H5、pc支付
                    } else if (paymentId.equals(PaymentIdEnum.ALI_WAP_PAY.value())) {
                        return zfbRefund(h5WxRefundBaseDTO, resultMap);

                        // 微信APP支付
                    } else if (paymentId.equals(PaymentIdEnum.WECHAT_APP_PAY.value())) {
                        return wechatAppRefund(h5WxRefundBaseDTO, resultMap, h5WxRefundDTO);

                        // 支付宝APP支付
                    } else if (paymentId.equals(PaymentIdEnum.ALI_APP_PAY.value())) {
                        return zfbAppRefund(h5WxRefundBaseDTO, resultMap);

                        // 余额支付
                    } else if (paymentId.equals(PaymentIdEnum.BALANCE_PAY.value())) {
                        Boolean b = this.balanceRefund(h5WxRefundBaseDTO);
                        if (!b) {
                            resultMap.put("code", ResultCodeEnum.RETRY.value());
                            resultMap.put("data", h5WxRefundBaseDTO);
                            resultMap.put("message", "余额退款失败");
                        } else {
                            log.info("余额退款成功");
                            resultMap.put("code", ResultCodeEnum.SUCCESS.value());
                            resultMap.put("message", "余额退款成功");

                        }
                        return resultMap;
                    }
                } else {
                    //第三方退款金额为0，说明是只退余额
                    if (null != h5WxRefundBaseDTO.getBalanceRefundAmount()
                            && h5WxRefundBaseDTO.getBalanceRefundAmount().compareTo(BigDecimal.ZERO) == 1) {
                        Boolean b = this.balanceRefund(h5WxRefundBaseDTO);
                        if (!b) {
                            resultMap.put("code", ResultCodeEnum.RETRY.value());
                            resultMap.put("data", h5WxRefundBaseDTO);
                            resultMap.put("message", "混合支付余额退款失败");
                        } else {
                            log.info("余额退款成功");
                            resultMap.put("code", ResultCodeEnum.SUCCESS.value());
                            resultMap.put("message", "余额退款成功");
                            return resultMap;
                        }
                    }
                }
            }
            return resultMap;
        } catch (Exception e) {
            log.error("异常为:{}", e);
        }
        return null;
    }

    private Map<String, Object> zfbAppRefund(@RequestBody H5WxRefundBaseDTO h5WxRefundBaseDTO, Map<String, Object> resultMap) {
        AppAlipayRefundRequestDTO refundRequestDTO = new AppAlipayRefundRequestDTO();
        refundRequestDTO.setOutTradeNo(h5WxRefundBaseDTO.getOuttradeno());
        refundRequestDTO.setOutRequestNo(h5WxRefundBaseDTO.getOutrefundno());
        // 金额处理h5WxRefundBaseDTO中的金额单位是"分"，支付宝要求的单位是"元"且是字符串类型
        Integer money = h5WxRefundBaseDTO.getRefundfee();
        double refundfee = new BigDecimal((float) money / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String finalRefundFee = String.valueOf(refundfee);
        refundRequestDTO.setRefundAmount(finalRefundFee);
        log.info("支付宝APP退款请求数据信息：{}", JSON.toJSONString(refundRequestDTO));
        String alipayResponceString = aliPayService.appAlipayRefund(refundRequestDTO);
        log.info("支付宝退款返回数据，{}", alipayResponceString);

        if (alipayResponceString != null) {
            JSONObject alipayResponce = JSONObject.parseObject(alipayResponceString);
            String alipayTrRefundResponse = alipayResponce.getString("alipay_trade_refund_response");
            JSONObject jsondata = JSON.parseObject(alipayTrRefundResponse);
            String msg = jsondata.getString("msg");
            String code = jsondata.getString("code");

            // 说明退款成功
            if ("Success".equals(msg)) {
                String tradeNo = jsondata.getString("trade_no");
                //保存退款流水表
                PaymentTallyDTO paymentTallyDTO = new PaymentTallyDTO();
                BeanCopier.create(H5WxRefundBaseDTO.class, PaymentTallyDTO.class, false)
                        .copy(h5WxRefundBaseDTO, paymentTallyDTO, null);
                paymentTallyDTO.setId(IdGenerator.defaultSnowflakeId());
                paymentTallyDTO.setTransactionStatus(PaymentCodeEnum.TRANSACTION_STATUS_IN.value());
                paymentTallyDTO.setOrderSn(Long.parseLong(h5WxRefundBaseDTO.getOrdersn()));
                paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
                paymentTallyDTO.setAftersaleSn(Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()));
                paymentTallyDTO.setCreateDate(new Date());
                paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_REFUND.value());
                paymentTallyDTO.setPaymentAmount(BigDecimal.valueOf(h5WxRefundBaseDTO.getRefundfee()).divide(new BigDecimal(100)));
                paymentTallyDTO.setTradeSn(tradeNo);
                paymentTallyDTO.setPaymentCode("ZFBAPP");
                paymentTallyDTO.setPaymentName("支付宝");
                paymentTallyDTO.setPaymentStatus(1);
                paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
                paymentTallyService.save(paymentTallyDTO);

                log.info("支付宝退款成功，code:{},message:{}", code, msg);
                resultMap.put("code", ResultCodeEnum.SUCCESS.value());
                resultMap.put("message", msg);
                Boolean b = this.balanceRefund(h5WxRefundBaseDTO);
                if (!b) {
                    resultMap.put("code", ResultCodeEnum.RETRY.value());
                    resultMap.put("data", h5WxRefundBaseDTO);
                    resultMap.put("message", "混合支付余额退款失败");
                }
            } else {
                String sub_msg = jsondata.getString("sub_msg");
                log.info("支付宝退款失败，code:{},message:{}", code, sub_msg);
                resultMap.put("code", ResultCodeEnum.ERROR.value());
                resultMap.put("message", sub_msg);
            }
        }
        return resultMap;
    }

    private Map<String, Object> wechatAppRefund(@RequestBody H5WxRefundBaseDTO h5WxRefundBaseDTO, Map<String, Object> resultMap, H5WxRefundDTO h5WxRefundDTO) {
        AppWxRefundDTO appWxRefundDTO = new AppWxRefundDTO();
        //调用third-party微信app支付退款服务
        appWxRefundDTO.setOutrefundno(h5WxRefundDTO.getOutrefundno());
        appWxRefundDTO.setOuttradeno(h5WxRefundDTO.getOuttradeno());
        appWxRefundDTO.setTotalfee(h5WxRefundDTO.getTotalfee());
        appWxRefundDTO.setRefundfee(h5WxRefundDTO.getRefundfee());
        appWxRefundDTO.setNonceStr(h5WxRefundDTO.getNonceStr());
        log.info("微信APP退款请求数据信息：{}", JSON.toJSONString(appWxRefundDTO));
        AppWxRefundResponseDTO appWxRefundResponseDTO = weChatAppPayService.orderRefund(appWxRefundDTO);
        log.info("第三方服务返回退款实体信息为{}", appWxRefundResponseDTO);

        //根据响应实体判断是否成功
        if (null == appWxRefundResponseDTO) {
            resultMap.put("code", ResultCodeEnum.ERROR);
            resultMap.put("message", "微信退款发生异常");
            return resultMap;
        }
        if (appWxRefundResponseDTO.getReturn_code().equals(WXRefundStateEnum.SUCCESS.value())
                && appWxRefundResponseDTO.getReturn_msg().equals(WXRefundStateEnum.RETURN_MSG_OK.value())
                && appWxRefundResponseDTO.getResult_code().equals(WXRefundStateEnum.SUCCESS.value())) {
            log.info("开始保存退款流水表");
            //保存退款流水表
            PaymentTallyDTO paymentTallyDTO = new PaymentTallyDTO();
            BeanCopier.create(H5WxRefundBaseDTO.class, PaymentTallyDTO.class, false)
                    .copy(h5WxRefundBaseDTO, paymentTallyDTO, null);
            paymentTallyDTO.setId(IdGenerator.defaultSnowflakeId());
            paymentTallyDTO.setTransactionStatus(PaymentCodeEnum.TRANSACTION_STATUS_IN.value());
            paymentTallyDTO.setOrderSn(Long.parseLong(h5WxRefundBaseDTO.getOrdersn()));
            paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
            paymentTallyDTO.setAftersaleSn(Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()));
            paymentTallyDTO.setCreateDate(new Date());
            paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_REFUND.value());
            paymentTallyDTO.setPaymentAmount(BigDecimal.valueOf(h5WxRefundBaseDTO.getRefundfee()).divide(new BigDecimal(100)));
            paymentTallyDTO.setTradeSn(appWxRefundResponseDTO.getTransaction_id());
            paymentTallyDTO.setPaymentCode("WXAPP");
            paymentTallyDTO.setPaymentName("微信");
            paymentTallyDTO.setPaymentStatus(1);
            paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
            paymentTallyService.save(paymentTallyDTO);

            resultMap.put("code", ResultCodeEnum.SUCCESS.value());
            resultMap.put("message", appWxRefundResponseDTO.toString());
            Boolean b = this.balanceRefund(h5WxRefundBaseDTO);
            if (!b) {
                resultMap.put("code", ResultCodeEnum.RETRY.value());
                resultMap.put("data", h5WxRefundBaseDTO);
                resultMap.put("message", "混合支付余额退款失败");
            }
        } else if (null != appWxRefundResponseDTO.getErr_code()) {
            if (appWxRefundResponseDTO.getResult_code().equals(WXRefundStateEnum.ERR_CODE_SYSTEMERROR.value()) ||
                    appWxRefundResponseDTO.getResult_code().equals(WXRefundStateEnum.ERR_CODE_BIZERR_NEED_RETRY.value())) {
                resultMap.put("code", ResultCodeEnum.RETRY.value());
                resultMap.put("data", appWxRefundResponseDTO);
                resultMap.put("message", "退款异常，需要使用原单号发起重试");
            } else {
                resultMap.put("code", ResultCodeEnum.ERROR.value());
                resultMap.put("data", appWxRefundResponseDTO);
                resultMap.put("message", "退款异常，请检查原因");
            }
        }

        return resultMap;
    }

    private Map<String, Object> zfbRefund(@RequestBody H5WxRefundBaseDTO h5WxRefundBaseDTO, Map<String, Object> resultMap) {
        AlipayRefundRequestDTO refundRequestDTO = new AlipayRefundRequestDTO();
        refundRequestDTO.setOrdersn(h5WxRefundBaseDTO.getOrdersn());
        refundRequestDTO.setOuttradeno(h5WxRefundBaseDTO.getOuttradeno());
        refundRequestDTO.setOutrefundno(h5WxRefundBaseDTO.getOutrefundno());
        // 金额处理h5WxRefundBaseDTO中的金额单位是"分"，支付宝要求的单位是"元"且是字符串类型
        Integer money = h5WxRefundBaseDTO.getRefundfee();
        double refundfee = new BigDecimal((float) money / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String finalRefundFee = String.valueOf(refundfee);
        refundRequestDTO.setRefundfee(finalRefundFee);
        log.info("支付宝退款请求数据信息：{}", JSON.toJSONString(refundRequestDTO));
        String alipayResponceString = aliPayService.alipayRefund(refundRequestDTO);
        log.info("支付宝退款返回数据，{}", alipayResponceString);

        if (alipayResponceString != null) {
            JSONObject alipayResponce = JSONObject.parseObject(alipayResponceString);
            String alipayTrRefundResponse = alipayResponce.getString("alipay_trade_refund_response");
            JSONObject jsondata = JSON.parseObject(alipayTrRefundResponse);
            String msg = jsondata.getString("msg");
            String code = jsondata.getString("code");

            // 说明退款成功
            if ("Success".equals(msg)) {
                String tradeNo = jsondata.getString("trade_no");
                //保存退款流水表
                PaymentTallyDTO paymentTallyDTO = new PaymentTallyDTO();
                BeanCopier.create(H5WxRefundBaseDTO.class, PaymentTallyDTO.class, false)
                        .copy(h5WxRefundBaseDTO, paymentTallyDTO, null);
                paymentTallyDTO.setId(IdGenerator.defaultSnowflakeId());
                paymentTallyDTO.setTransactionStatus(PaymentCodeEnum.TRANSACTION_STATUS_IN.value());
                paymentTallyDTO.setOrderSn(Long.parseLong(h5WxRefundBaseDTO.getOrdersn()));
                paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
                paymentTallyDTO.setAftersaleSn(Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()));
                paymentTallyDTO.setCreateDate(new Date());
                paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_REFUND.value());
                paymentTallyDTO.setPaymentAmount(BigDecimal.valueOf(h5WxRefundBaseDTO.getRefundfee()).divide(new BigDecimal(100)));
                paymentTallyDTO.setTradeSn(tradeNo);
                paymentTallyDTO.setPaymentCode("ZFB");
                paymentTallyDTO.setPaymentName("支付宝");
                paymentTallyDTO.setPaymentStatus(1);
                paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
                paymentTallyService.save(paymentTallyDTO);

                log.info("支付宝退款成功，code:{},message:{}", code, msg);
                resultMap.put("code", ResultCodeEnum.SUCCESS.value());
                resultMap.put("message", msg);
                Boolean b = this.balanceRefund(h5WxRefundBaseDTO);
                if (!b) {
                    resultMap.put("code", ResultCodeEnum.RETRY.value());
                    resultMap.put("data", h5WxRefundBaseDTO);
                    resultMap.put("message", "混合支付余额退款失败");
                }
            } else {
                String sub_msg = jsondata.getString("sub_msg");

                log.info("支付宝退款失败，code:{},message:{}", code, sub_msg);
                resultMap.put("code", ResultCodeEnum.ERROR.value());
                resultMap.put("message", sub_msg);
            }
        }
        return resultMap;
    }

    private Map<String, Object> wechatRefund(@RequestBody H5WxRefundBaseDTO h5WxRefundBaseDTO, Map<String, Object> resultMap, H5WxRefundDTO h5WxRefundDTO) {
        H5WxRefundResponseDTO h5WxRefundResponseDTO = weChatRefundservice.refundWX(h5WxRefundDTO);
        log.info("第三方服务返回退款实体信息为{}", h5WxRefundResponseDTO);

        //根据响应实体判断是否成功
        if (BeanUtil.isEmpty(h5WxRefundResponseDTO)) {
            resultMap.put("code", ResultCodeEnum.ERROR);
            resultMap.put("message", "微信退款发生异常");
            return resultMap;
        }
        if (h5WxRefundResponseDTO.getReturn_code().equals(WXRefundStateEnum.SUCCESS.value())
                && h5WxRefundResponseDTO.getReturn_msg().equals(WXRefundStateEnum.RETURN_MSG_OK.value())
                && h5WxRefundResponseDTO.getResult_code().equals(WXRefundStateEnum.SUCCESS.value())) {
            log.info("开始保存退款流水表");
            //保存退款流水表
            PaymentTallyDTO paymentTallyDTO = new PaymentTallyDTO();
            BeanCopier.create(H5WxRefundBaseDTO.class, PaymentTallyDTO.class, false)
                    .copy(h5WxRefundBaseDTO, paymentTallyDTO, null);
            paymentTallyDTO.setId(IdGenerator.defaultSnowflakeId());
            paymentTallyDTO.setTransactionStatus(PaymentCodeEnum.TRANSACTION_STATUS_IN.value());
            paymentTallyDTO.setOrderSn(Long.parseLong(h5WxRefundBaseDTO.getOrdersn()));
            paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
            paymentTallyDTO.setAftersaleSn(Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()));
            paymentTallyDTO.setCreateDate(new Date());
            paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_REFUND.value());
            paymentTallyDTO.setPaymentAmount(BigDecimal.valueOf(h5WxRefundBaseDTO.getRefundfee()).divide(new BigDecimal(100)));
            paymentTallyDTO.setTradeSn(h5WxRefundResponseDTO.getTransaction_id());
            paymentTallyDTO.setPaymentCode("WX");
            paymentTallyDTO.setPaymentName("微信");
            paymentTallyDTO.setPaymentStatus(1);
            paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
            paymentTallyService.save(paymentTallyDTO);

            resultMap.put("code", ResultCodeEnum.SUCCESS.value());
            resultMap.put("message", h5WxRefundResponseDTO.toString());
            Boolean b = this.balanceRefund(h5WxRefundBaseDTO);
            if (!b) {
                resultMap.put("code", ResultCodeEnum.RETRY.value());
                resultMap.put("data", h5WxRefundBaseDTO);
                resultMap.put("message", "混合支付余额退款失败");
            }
        } else if (null != h5WxRefundResponseDTO.getErr_code()) {
            if (h5WxRefundResponseDTO.getResult_code().equals(WXRefundStateEnum.ERR_CODE_SYSTEMERROR.value()) ||
                    h5WxRefundResponseDTO.getResult_code().equals(WXRefundStateEnum.ERR_CODE_BIZERR_NEED_RETRY.value())) {
                resultMap.put("code", ResultCodeEnum.RETRY.value());
                resultMap.put("data", h5WxRefundResponseDTO);
                resultMap.put("message", "退款异常，需要使用原单号发起重试");
            } else {
                resultMap.put("code", ResultCodeEnum.ERROR.value());
                resultMap.put("data", h5WxRefundResponseDTO);
                resultMap.put("message", "退款异常，请检查原因");
            }
        }
        return resultMap;
    }

    // 退货退款：混合支付下的退款
    private Boolean balanceRefund(H5WxRefundBaseDTO h5WxRefundBaseDTO) {

        // 变更用户余额和新增用户余额变更明细表
        MemberChangeBalanceDTO memberChangeBalanceDTO = new MemberChangeBalanceDTO();
        memberChangeBalanceDTO.setMemberId(h5WxRefundBaseDTO.getBuyerId());
        memberChangeBalanceDTO.setChangeBalance(h5WxRefundBaseDTO.getBalanceRefundAmount());
        memberChangeBalanceDTO.setBalanceType(BalanceEnum.ORDER_REFUND.value());
        // todo keyuan 退款的时候增加用户的可提现金额


        log.info("余额退款数据，memberChangeBalanceDTO{}", memberChangeBalanceDTO);

        // 退款余额为0，不进行后续操作，不保存支付流水
        if (h5WxRefundBaseDTO.getBalanceRefundAmount().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }

        try {
            memberInfoService.changeMemberBalance(memberChangeBalanceDTO);

            String tradeNo = IdGenerator.defaultSnowflakeId().toString();
            //保存退款流水表
            PaymentTallyDTO paymentTallyDTO = new PaymentTallyDTO();
            BeanCopier.create(H5WxRefundBaseDTO.class, PaymentTallyDTO.class, false)
                    .copy(h5WxRefundBaseDTO, paymentTallyDTO, null);
            paymentTallyDTO.setId(IdGenerator.defaultSnowflakeId());
            paymentTallyDTO.setTransactionStatus(PaymentCodeEnum.TRANSACTION_STATUS_IN.value());
            paymentTallyDTO.setOrderSn(Long.parseLong(h5WxRefundBaseDTO.getOrdersn()));
            paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
            paymentTallyDTO.setAftersaleSn(Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()));
            paymentTallyDTO.setCreateDate(new Date());
            paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_REFUND.value());
            paymentTallyDTO.setPaymentAmount(h5WxRefundBaseDTO.getBalanceRefundAmount());
            paymentTallyDTO.setTradeSn(tradeNo);
            paymentTallyDTO.setPaymentCode("BALANCE");
            paymentTallyDTO.setPaymentName("余额");
            paymentTallyDTO.setPaymentStatus(1);
            paymentTallyDTO.setPaymentSn(Long.valueOf(h5WxRefundBaseDTO.getOuttradeno()));
            paymentTallyService.save(paymentTallyDTO);
//            OrderDTO dto = new OrderDTO();
//            dto.setOrderSn(Long.parseLong(h5WxRefundBaseDTO.getOrdersn()));
//            dto.setBalanceRefundAmount(h5WxRefundBaseDTO.getBalanceRefundAmount());
//            orderService.putOrderDTO(dto);
            log.info("余额退款成功");
            return true;
        } catch (ServiceException e) {
            log.info("余额退款失败，e:{}", e);
            return false;
        }
    }
}
