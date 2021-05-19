/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wxpay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.config.wechat.WXAppPayConfig;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.wxpay.AppWXPayDTO;
import com.leimingtech.modules.dto.wxpay.AppWXPayResponseDTO;
import com.leimingtech.modules.dto.wxpay.AppWxRefundDTO;
import com.leimingtech.modules.dto.wxpay.AppWxRefundResponseDTO;
import com.leimingtech.modules.handle.PrepayIdRequestHandler;
import com.leimingtech.modules.handle.RefundRequestHandler;
import com.leimingtech.modules.handle.TenpayHttpClient;
import com.leimingtech.modules.service.wxpay.WeChatAppPayService;
import com.leimingtech.modules.statuscode.ThirdTransferStatusCode;
import com.leimingtech.modules.util.XMLUtil;
import com.leimingtech.util.wechat.ClientCustomSSL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author zhangtai
 * @date 2020/4/7 11:28
 * @Description:
 */
@Slf4j
@Service
@Transactional
public class WeChatAppPayServiceImpl implements WeChatAppPayService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(WeChatAppPayServiceImpl.class);

    @Autowired
    private WXAppPayConfig wxAppPayConfig;


    /**
     * 功能描述: 微信统一下单接口
     *
     * @param: [params]
     * @return: java.lang.String
     * @auther: zhangtai
     * @date: 2020/4/7 11:54
     */

    @Override
    public String unifiedOrder(@RequestParam("params") String params) {
        // https://api.mch.weixin.qq.com/pay/unifiedorder
        String requestUrl = "https://" + WXPayConstants.DOMAIN_API + WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
        log.info("调用微信统一下单接口，参数：{},url：{}", params, requestUrl);

        // 预支付接口地址
        String result = "";
        TenpayHttpClient httpClient = new TenpayHttpClient();
        httpClient.setReqContent(requestUrl);
        if (httpClient.callHttpPost(requestUrl, params)) {
            result = httpClient.getResContent();
        } else {
            log.info("调用微信统一下单接口异常");
        }
        log.info("调用微信统一下单接口返回结果：{}", result);
        return result;
    }

    /**
     * 功能描述: 微信app预支付
     *
     * @param: [appWXPayDTO]
     * @return: com.leimingtech.modules.dto.wxpay.AppWXPayResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/7 17:00
     */

    @Override
    public AppWXPayResponseDTO payWX(@RequestBody AppWXPayDTO appWXPayDTO) throws Exception {
        AppWXPayResponseDTO appWXPayResponseDTO = new AppWXPayResponseDTO();
        String nonceStr = IdGenerator.defaultSnowflakeId().toString();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        //封装微信统一下单接口参数
        String params = getUnifiedOrderParams(appWXPayDTO, nonceStr);

        // 2.调用微信支付接口，返回预支付id
        String resultXml = this.unifiedOrder(params);
        Map<String, String> result = WXPayUtil.xmlToMap(resultXml);
        String prepayId = "";
        String mwebUrl = "";
        String secondNonceStr = "";
        if ("SUCCESS".equals(result.get("return_code")) && result.containsKey("prepay_id")) {
            prepayId = result.get("prepay_id");
            mwebUrl = result.get("mweb_url");
            secondNonceStr = result.get("nonce_str");
            log.info("获取prepayid成功：prepay_id:{} ,mwebUrl:{}, secondNonceStr:{}", prepayId, mwebUrl, secondNonceStr);
        } else {
            log.info("获取prepayid失败：prepay_id:{},mwebUrl:{}, secondNonceStr:{}", prepayId, mwebUrl, secondNonceStr);
            mlogger.info(ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID.getCode(),
                    ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID.getMessage());
            throw new ServiceException(ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID);
        }

        // 若获取prepayid成功，将相关信息返回客户端
        if (StringUtils.isNotBlank(prepayId)) {
            // 4.签名生成参数
            Map<String, String> map = new HashMap<>();

            // h5和公众号支付用的是同一个appid
            map.put("appid", wxAppPayConfig.APP_ID);
            map.put("partnerid", wxAppPayConfig.MCH_ID);
            map.put("prepayid", prepayId);
            map.put("package", "Sign=WXPay");
            map.put("noncestr", secondNonceStr);
            map.put("timestamp", timestamp);

            // 返回结果封装
            appWXPayResponseDTO.setAppId(wxAppPayConfig.APP_ID);
            appWXPayResponseDTO.setPartnerId(wxAppPayConfig.MCH_ID);
            appWXPayResponseDTO.setPrepayId(prepayId);
            appWXPayResponseDTO.setSign(WXPayUtil.generateSignature(map, wxAppPayConfig.API_KEY, WXPayConstants.SignType.MD5));
            appWXPayResponseDTO.setTimeStamp(timestamp);
            appWXPayResponseDTO.setNonceStr(secondNonceStr);
            appWXPayResponseDTO.setPackageValue("Sign=WXPay");
            return appWXPayResponseDTO;
        } else {
            mlogger.info(ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID.getCode(),
                    ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID.getMessage());
            throw new ServiceException(ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID);
        }
    }

    /**
     * 功能描述:
     * 来自App的微信支付
     *
     * @param appWXPayDTO 微信统一下单实体
     * @param nonceStr    随机字符串
     * @author : 刘远杰
     */
    private String getUnifiedOrderParams(AppWXPayDTO appWXPayDTO, String nonceStr) throws Exception {
        // 1.获取生成预支付订单的请求类
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler("UTF-8");
        prepayReqHandler.setParameter("appid", wxAppPayConfig.APP_ID);
        prepayReqHandler.setParameter("mch_id", wxAppPayConfig.MCH_ID);
        prepayReqHandler.setParameter("nonce_str", nonceStr);
        prepayReqHandler.setParameter("sign_type", wxAppPayConfig.SIGN_TYPE);
        prepayReqHandler.setParameter("body", appWXPayDTO.getBody());
        prepayReqHandler.setParameter("out_trade_no", appWXPayDTO.getPaySn());
        prepayReqHandler.setParameter("total_fee", appWXPayDTO.getTotalFee());
        prepayReqHandler.setParameter("spbill_create_ip", appWXPayDTO.getIp());
        prepayReqHandler.setParameter("notify_url", wxAppPayConfig.NOTIFY_URL);
        prepayReqHandler.setParameter("trade_type", wxAppPayConfig.TRADE_TYPE);
        /**
         * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
         */
        String signKey = wxAppPayConfig.API_KEY;
        prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign(signKey));
        String str = prepayReqHandler.getXmlParams();
        log.info("构造app微信支付微信统一下单接口参数成功,param:{}", str);
        return str;
    }


    /**
     * @param params:
     * @return java.lang.String
     * @Description 查询微信支付结果
     * @Author huangkeyuan
     * @Date 20:24 2019-12-05
     */

    @Override
    public String orderquery(@RequestParam("params") String params) throws Exception {

        //构造微信查询订单接口所需参数
        String nonceStr = IdGenerator.defaultSnowflakeId().toString();

        // 1.获取生成预支付订单的请求类
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler("UTF-8");
        // 2.封装预支付接口参数
        prepayReqHandler.setParameter("appid", wxAppPayConfig.APP_ID);
        prepayReqHandler.setParameter("mch_id", wxAppPayConfig.MCH_ID);
        prepayReqHandler.setParameter("nonce_str", nonceStr);
        prepayReqHandler.setParameter("out_trade_no", params);

        String signKey = wxAppPayConfig.API_KEY;
        prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign(signKey));
        String wxStr = prepayReqHandler.getXmlParams();

        String requestUrl = "https://" + WXPayConstants.DOMAIN_API + WXPayConstants.ORDERQUERY_URL_SUFFIX;
        log.info("调用微信订单查询订接口，参数：{},url：{}", wxStr, requestUrl);

        // 预支付接口地址
        String result = "";
        TenpayHttpClient httpClient = new TenpayHttpClient();
        httpClient.setReqContent(requestUrl);
        if (httpClient.callHttpPost(requestUrl, wxStr)) {
            result = httpClient.getResContent();
        } else {
            log.info("调用微信查询订单接口异常");
        }
        log.info("调用微信订单查询订接口返回结果：{}", result);
        return result;
    }

    /**
     * 功能描述: app微信支付退款
     *
     * @param: [appWxRefundDTO]
     * @return: com.leimingtech.modules.dto.wxpay.AppWxRefundResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/8 11:00
     */

    @Override
    public AppWxRefundResponseDTO orderRefund(@RequestBody AppWxRefundDTO appWxRefundDTO) {
        AppWxRefundResponseDTO appWxRefundResponseDTO = new AppWxRefundResponseDTO();
        Map<String, Object> result = new HashMap<>();
        // 退款单号
        String out_refund_no = appWxRefundDTO.getOutrefundno();
        // 订单号
        String out_trade_no = appWxRefundDTO.getOuttradeno();
        // 总金额
        String total_fee = String.valueOf(appWxRefundDTO.getTotalfee());
        //退款金额
        String refund_fee = String.valueOf(appWxRefundDTO.getRefundfee());
        // 随机字符串
        String nonce_str = String.valueOf(appWxRefundDTO.getNonceStr());
        //微信公众号apid
        String appid = wxAppPayConfig.APP_ID;
        //微信公众号appsecret
        String appsecret = wxAppPayConfig.APP_SECRET;
        //微信商户id
        String mch_id = wxAppPayConfig.MCH_ID;
        //就是MCHID
        String op_user_id = wxAppPayConfig.MCH_ID;
        //商户平台上的那个KEY
        String apikey = wxAppPayConfig.API_KEY;

        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("out_refund_no", out_refund_no);
        packageParams.put("total_fee", total_fee);
        packageParams.put("refund_fee", refund_fee);
        packageParams.put("op_user_id", op_user_id);
        RefundRequestHandler refundRequestHandler = new RefundRequestHandler("UTF-8");
        refundRequestHandler.init(appid, appsecret, apikey);
        //创建签名
        String sign = refundRequestHandler.createSign(packageParams);
        packageParams.put("sign", sign);

        String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
                + mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
                + "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
                + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                + "<out_refund_no>" + out_refund_no + "</out_refund_no>"
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<refund_fee>" + refund_fee + "</refund_fee>"
                + "<op_user_id>" + op_user_id + "</op_user_id>" + "</xml>";
        String refundURL = "https://" + WXPayConstants.DOMAIN_API + WXPayConstants.REFUND_URL_SUFFIX;
        try {
            ClientCustomSSL clientCustomSSL = new ClientCustomSSL();
            log.info("微信退款请求参数：{}", xml);
            String s = clientCustomSSL.doRefund(refundURL, xml, mch_id);
            //xml解析成相应的map
            result = XMLUtil.doXMLParse(s);
            log.info("当前退款操作返回{}", result);
            JSONObject json = new JSONObject(result);
            //转换成最终返回的实体
            appWxRefundResponseDTO = JSON.toJavaObject(json, AppWxRefundResponseDTO.class);
        } catch (Exception e) {
            log.error("调用外部微信退款接口错误:{}", e);
            appWxRefundResponseDTO.setReturn_code("SYSTEMERROR");
            appWxRefundResponseDTO.setReturn_msg("调用微信退款服务异常，请稍后再试");
            return appWxRefundResponseDTO;
        }
        return appWxRefundResponseDTO;
    }

    /**
     * 功能描述: 根据appId判断支付是否是app支付
     *
     * @param: [appId]
     * @return: java.lang.Boolean
     * @auther: zhangtai
     * @date: 2020/4/8 16:40
     */

    @Override
    public Boolean isAppPay(@RequestParam("appId") String appId) {
        if (StringUtils.equals(appId, wxAppPayConfig.APP_ID)) {
            return true;
        }
        return false;
    }


}
