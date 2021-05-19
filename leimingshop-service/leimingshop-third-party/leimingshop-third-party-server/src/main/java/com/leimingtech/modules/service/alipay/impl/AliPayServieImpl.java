/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.alipay.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.leimingtech.config.alipay.AliPayAppConfig;
import com.leimingtech.config.alipay.AliPayConfig;
import com.leimingtech.config.alipay.AliPayPcConfig;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.wxpay.PaymentResultCodeConstants;
import com.leimingtech.modules.dto.alipay.*;
import com.leimingtech.modules.service.alipay.AliPayService;
import com.leimingtech.util.common.HttpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 支付宝发起支付
 * @Author huangkeyuan
 * @Date 10:43 2019-12-10
 * @return
 */
@Slf4j
@Service
@Transactional
public class AliPayServieImpl implements AliPayService {

    /**
     * 根据银行账户查询所属银行
     */
    private static final String BANK_NAME_HTTPS = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?cardBinCheck=true&cardNo=%s";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(AliPayServieImpl.class);
    @Autowired
    private AliPayConfig aliPayConfig;
    @Autowired
    private AliPayAppConfig aliPayAppConfig;
    @Autowired
    private AliPayPcConfig aliPayPcConfig;

    /**
     * @param h5AliPayRequestDTO : 支付宝支付所需参数
     * @return com.leimingtech.modules.dto.alipay.H5AlipayResponseDTO
     * @Description 创建订单并支付接口
     * @Author huangkeyuan
     * @Date 10:21 2019-12-10
     */

    @Override
    public H5AlipayResponseDTO aliPayCertUnifiedOrder(@RequestBody H5AliPayRequestDTO h5AliPayRequestDTO) {
        H5AlipayResponseDTO h5AlipayResponseDTO = new H5AlipayResponseDTO();

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(h5AliPayRequestDTO.getBody());
        model.setSubject(h5AliPayRequestDTO.getSubject());
        model.setOutTradeNo(h5AliPayRequestDTO.getOutTradeNo());
        model.setTotalAmount(h5AliPayRequestDTO.getTotalAmount() + "");
        model.setProductCode(h5AliPayRequestDTO.getProductCode());
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfig.getALIPAY_NOTIFY_URL());

        String returnFinishUrl = aliPayConfig.getRETURNURL() + h5AliPayRequestDTO.getReturnUrl();
        // 判断支付单号，如果6开头那么为充值业务，否则是支付业务
        String paySn = h5AliPayRequestDTO.getOutTradeNo();
        if (paySn.startsWith(PaymentResultCodeConstants.RECHARGEPAY)) {
            returnFinishUrl = aliPayConfig.getRECHARGERETURNURL() + h5AliPayRequestDTO.getReturnUrl();
        }
        request.setReturnUrl(returnFinishUrl);

        log.info(">>>>支付宝统一下单接口请求参数：" + model.getBody() + "," + model.getOutTradeNo() + "," + model.getTotalAmount());

        /**实例化客户端*/
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(aliPayConfig.getSERVERURL());
        certAlipayRequest.setAppId(aliPayConfig.getAPPID());
        certAlipayRequest.setPrivateKey(aliPayConfig.getAPP_PRIVATE_KEY());
        certAlipayRequest.setFormat(aliPayConfig.getFORMAT());
        certAlipayRequest.setCharset(aliPayConfig.getCHARSET());
        certAlipayRequest.setSignType(aliPayConfig.getSIGN_TYPE());
        certAlipayRequest.setCertPath(aliPayConfig.getAPP_CERT_PATH());
        certAlipayRequest.setAlipayPublicCertPath(aliPayConfig.getALIPAY_CERT_PATH());
        certAlipayRequest.setRootCertPath(aliPayConfig.getALIPAY_ROOT_CERT_PATH());
        try {
            //构造client
            AlipayClient alipayClient = new LMAlipayClient(certAlipayRequest);
            String form = alipayClient.pageExecute(request).getBody();

            //就是orderString 可以直接给客户端请求，无需再做处理。
            log.info(">>>生成调用支付宝参数" + form);
            h5AlipayResponseDTO.setOrderString(form);
            h5AlipayResponseDTO.setReturnCode("200");
            h5AlipayResponseDTO.setReturnMsg("支付完成");
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            h5AlipayResponseDTO.setReturnCode("500");
            h5AlipayResponseDTO.setReturnMsg("支付异常");
        }

        return h5AlipayResponseDTO;
    }

    /**
     * @param pcAliPayRequestDTO : 支付宝支付所需参数
     * @return com.leimingtech.modules.dto.alipay.PcAlipayResponseDTO
     * @Description 创建订单并支付接口
     * @Author huangkeyuan
     * @Date 10:21 2020-5-20
     */

    @Override
    public PcAlipayResponseDTO aliPayPagePay(@RequestBody PcAliPayRequestDTO pcAliPayRequestDTO) {
        PcAlipayResponseDTO pcAlipayResponseDTO = new PcAlipayResponseDTO();

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(pcAliPayRequestDTO.getBody());
        model.setSubject(pcAliPayRequestDTO.getSubject());
        model.setOutTradeNo(pcAliPayRequestDTO.getOutTradeNo());
        model.setTotalAmount(pcAliPayRequestDTO.getTotalAmount() + "");
        model.setProductCode(aliPayPcConfig.getPAYTYPE());
        request.setBizModel(model);
        request.setNotifyUrl(aliPayPcConfig.getALIPAY_NOTIFY_URL());
        String returnFinishUrl = aliPayPcConfig.getRETURNURL() + pcAliPayRequestDTO.getReturnUrl();
        request.setReturnUrl(returnFinishUrl);
        log.info(">>>>支付宝pc统一下单接口请求参数：" + model.getBody() + "," + model.getOutTradeNo() + "," + model.getTotalAmount() + "," + returnFinishUrl);

        /**实例化客户端*/
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(aliPayPcConfig.getSERVERURL());
        certAlipayRequest.setAppId(aliPayPcConfig.getAPPID());
        certAlipayRequest.setPrivateKey(aliPayPcConfig.getAPP_PRIVATE_KEY());
        certAlipayRequest.setFormat(aliPayPcConfig.getFORMAT());
        certAlipayRequest.setCharset(aliPayPcConfig.getCHARSET());
        certAlipayRequest.setSignType(aliPayPcConfig.getSIGN_TYPE());
        certAlipayRequest.setCertPath(aliPayPcConfig.getAPP_CERT_PATH());
        certAlipayRequest.setAlipayPublicCertPath(aliPayPcConfig.getALIPAY_CERT_PATH());
        certAlipayRequest.setRootCertPath(aliPayPcConfig.getALIPAY_ROOT_CERT_PATH());
        try {
            //构造client
            AlipayClient alipayClient = new LMAlipayClient(certAlipayRequest);
            String form = alipayClient.pageExecute(request).getBody();

            //就是orderString 可以直接给客户端请求，无需再做处理。
            log.info(">>>生成调用支付宝参数" + form);
            pcAlipayResponseDTO.setOrderString(form);
            pcAlipayResponseDTO.setReturn_code("200");
            pcAlipayResponseDTO.setReturn_msg("支付完成");
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            pcAlipayResponseDTO.setReturn_code("500");
            pcAlipayResponseDTO.setReturn_msg("支付异常");
        }

        return pcAlipayResponseDTO;
    }

    /**
     * @return boolean
     * @Description 收到支付宝的成功回调之后验证签名是否正确
     * @Author huangkeyuan
     * @Date 17:01 2019-12-12
     */

    @Override
    public boolean checkSignature(@RequestParam Map<String, String> requestParams) {
        try {
            String alipaypublickey = this.getAlipayPublicKey(aliPayConfig.getALIPAY_CERT_PATH());
            log.info("读取服务器的支付宝公钥证书,{}", alipaypublickey);
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            Map<String, String> logMap = new HashMap<>();
            logMap.put("requestParams", requestParams.toString());
            logMap.put("alipaypublickey", alipaypublickey);
            logMap.put("charset", aliPayConfig.getCHARSET());
            logMap.put("signType", aliPayConfig.getSIGN_TYPE());
            mlogger.info("200101", "开始支付宝签名校验", logMap);
            boolean flag = AlipaySignature.rsaCheckV1(requestParams, alipaypublickey, aliPayConfig.getCHARSET(),
                    aliPayConfig.getSIGN_TYPE());

            return flag;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param alipayRefundRequestDTO :
     * @return java.lang.String
     * @Description 支付宝退款接口
     * @Author huangkeyuan
     * @Date 20:21 2019-12-12
     */

    @Override
    public String alipayRefund(@RequestBody AlipayRefundRequestDTO alipayRefundRequestDTO) {

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();

        model.setOutTradeNo(alipayRefundRequestDTO.getOuttradeno());
        model.setOutRequestNo(alipayRefundRequestDTO.getOutrefundno());
        model.setRefundAmount(alipayRefundRequestDTO.getRefundfee());
        request.setBizModel(model);

        /**实例化客户端*/
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(aliPayConfig.getSERVERURL());
        certAlipayRequest.setAppId(aliPayConfig.getAPPID());
        certAlipayRequest.setPrivateKey(aliPayConfig.getAPP_PRIVATE_KEY());
        certAlipayRequest.setFormat(aliPayConfig.getFORMAT());
        certAlipayRequest.setCharset(aliPayConfig.getCHARSET());
        certAlipayRequest.setSignType(aliPayConfig.getSIGN_TYPE());
        certAlipayRequest.setCertPath(aliPayConfig.getAPP_CERT_PATH());
        certAlipayRequest.setAlipayPublicCertPath(aliPayConfig.getALIPAY_CERT_PATH());
        certAlipayRequest.setRootCertPath(aliPayConfig.getALIPAY_ROOT_CERT_PATH());
        try {
            //构造client
            AlipayClient alipayClient = new LMAlipayClient(certAlipayRequest);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            log.info("支付宝退款返回信息,{}", response.getBody());
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    /**
     * @param alipayPublicCertPath: 支付宝证书在项目中的相对位置
     * @return java.lang.String
     * @Description 获取支付宝的公钥证书绝对路径
     * @Author huangkeyuan
     * @Date 10:45 2019-12-12
     */
    private String getAlipayPublicKey(String alipayPublicCertPath) throws AlipayApiException {
        InputStream inputStream = null;

        String var7;
        try {
            inputStream = LMAbstractAlipayClient.class.getResourceAsStream(alipayPublicCertPath);
            CertificateFactory e = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) e.generateCertificate(inputStream);
            PublicKey publicKey = cert.getPublicKey();
            Base64.Encoder encoder = java.util.Base64.getEncoder();
            String apipayPublicKey = encoder.encodeToString(publicKey.getEncoded());
            var7 = apipayPublicKey;
        } catch (CertificateException var17) {
            throw new AlipayApiException(var17);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException var18) {
                throw new AlipayApiException(var18);
            }

        }

        return var7;
    }

    /**
     * 功能描述: app创建订单并支付的接口
     *
     * @param: [appAlipayRequestDTO]
     * @return: com.leimingtech.modules.dto.alipay.AppAlipayResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/3 15:56
     */

    @Override
    public AppAlipayResponseDTO aliPayAppPay(@RequestBody AppAlipayRequestDTO appAlipayRequestDTO) {
        AppAlipayResponseDTO appAlipayResponseDTO = new AppAlipayResponseDTO();
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(aliPayAppConfig.URL);
        //设置应用Id
        certAlipayRequest.setAppId(aliPayAppConfig.APP_ID);
        //设置应用私钥
        certAlipayRequest.setPrivateKey(aliPayAppConfig.APP_PRIVATE_KEY);
        //设置请求格式，固定值json
        certAlipayRequest.setFormat(aliPayAppConfig.FORMAT);
        //设置字符集
        certAlipayRequest.setCharset(aliPayAppConfig.CHARSET);
        //设置签名类型
        certAlipayRequest.setSignType(aliPayAppConfig.SIGN_TYPE);
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(aliPayAppConfig.CERT_PATH);
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(aliPayAppConfig.ALIPAY_PUBLIC_CERT_PATH);
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(aliPayAppConfig.ROOT_CERT_PATH);

        try {
            //构造client
            AlipayClient alipayClient = new LMAlipayClient(certAlipayRequest);

            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            // 商品名称
            model.setBody(appAlipayRequestDTO.getBody());
            // 商品名称
            model.setSubject(appAlipayRequestDTO.getSubject());
            // 支付单号
            model.setOutTradeNo(appAlipayRequestDTO.getOutTradeNo());

            model.setTotalAmount(appAlipayRequestDTO.getTotalAmount().toString());
            model.setProductCode(aliPayAppConfig.PRODUCT_CODE);
            request.setBizModel(model);
            request.setNotifyUrl(aliPayAppConfig.ALIPAY_NOTIFY_URL);
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            response.getBody();//就是orderString 可以直接给客户端请求，无需再做处理。
            log.info(">>>app支付生成调用支付宝参数" + response.getBody());
            appAlipayResponseDTO.setOrderString(response.getBody());
            appAlipayResponseDTO.setReturn_code("200");
            appAlipayResponseDTO.setReturn_msg("支付完成");
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            appAlipayResponseDTO.setReturn_code("500");
            appAlipayResponseDTO.setReturn_msg("支付异常");
        }
        return appAlipayResponseDTO;
    }

    /**
     * 功能描述: app支付收到支付宝的成功回调之后验证签名是否正确
     *
     * @param: [requestParams]
     * @return: boolean
     * @auther: zhangtai
     * @date: 2020/4/3 16:55
     */

    @Override
    public boolean appCheckSignature(@RequestParam Map<String, String> requestParams) {
        try {
            log.info("支付宝app支付回调验签开始");
            String alipaypublickey = this.getAlipayPublicKey(aliPayAppConfig.getALIPAY_PUBLIC_CERT_PATH());
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            Map<String, String> logMap = new HashMap<>();
            logMap.put("requestParams", requestParams.toString());
            logMap.put("charset", aliPayAppConfig.CHARSET);
            logMap.put("signType", aliPayAppConfig.SIGN_TYPE);
            mlogger.info("200101", "app支付开始支付宝签名校验", logMap);
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCertCheckV1(Map<String, String> params, String publicKeyCertPath, String charset,String signType)
            boolean flag = AlipaySignature.rsaCheckV1(requestParams, alipaypublickey, aliPayAppConfig.CHARSET, "RSA2");
            return flag;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }

    /**
     * 功能描述: 支付宝app支付的退款
     *
     * @param: [alipayRefundRequestDTO]
     * @return: java.lang.String
     * @auther: zhangtai
     * @date: 2020/4/3 17:19
     */

    @Override
    public String appAlipayRefund(@RequestBody AppAlipayRefundRequestDTO appAlipayRefundRequestDTO) {
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(aliPayAppConfig.URL);
        //设置应用Id
        certAlipayRequest.setAppId(aliPayAppConfig.APP_ID);
        //设置应用私钥
        certAlipayRequest.setPrivateKey(aliPayAppConfig.APP_PRIVATE_KEY);
        //设置请求格式，固定值json
        certAlipayRequest.setFormat(aliPayAppConfig.FORMAT);
        //设置字符集
        certAlipayRequest.setCharset(aliPayAppConfig.CHARSET);
        //设置签名类型
        certAlipayRequest.setSignType(aliPayAppConfig.SIGN_TYPE);
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(aliPayAppConfig.CERT_PATH);
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(aliPayAppConfig.ALIPAY_PUBLIC_CERT_PATH);
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(aliPayAppConfig.ROOT_CERT_PATH);

        try {
            //构造client
            AlipayClient alipayClient = new LMAlipayClient(certAlipayRequest);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(appAlipayRefundRequestDTO.getOutTradeNo());
            model.setTradeNo(appAlipayRefundRequestDTO.getTradeNo());
            model.setOutRequestNo(appAlipayRefundRequestDTO.getOutRequestNo());
            model.setRefundAmount(appAlipayRefundRequestDTO.getRefundAmount());
            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.execute(request); //通过alipayClient调用API，获得对应的response类
            log.info("app支付支付宝退款返回信息,{}", response.getBody());
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    /**
     * 根据银行卡号查询所属银行
     *
     * @param bankAccount 银行卡号
     * @return
     * @date 2020-10-21 15:56
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public JSONObject getBankName(@RequestParam("bankAccount") String bankAccount) {
        String url = String.format(BANK_NAME_HTTPS, bankAccount);
        log.info("获取银行查询请求url信息：{}", url);
        JSONObject jsonObject = HttpsUtil.httpGet(url);
        log.info("银行获取到的信息为：{}", jsonObject);
        return jsonObject;
    }
}
