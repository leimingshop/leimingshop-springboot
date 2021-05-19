/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wxpay.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.config.wechat.WXMiniPayConfig;
import com.leimingtech.config.wechat.WXPayConfig;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.wxpay.H5WXPayDTO;
import com.leimingtech.modules.dto.wxpay.WXPayResponseDTO;
import com.leimingtech.modules.enums.WechatEnum;
import com.leimingtech.modules.handle.PrepayIdRequestHandler;
import com.leimingtech.modules.handle.TenpayHttpClient;
import com.leimingtech.modules.service.wxpay.WeChatPayservice;
import com.leimingtech.modules.statuscode.ThirdTransferStatusCode;
import com.leimingtech.util.common.HttpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 * <微信支付>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/5/24 16:31
 **/
@Slf4j
@Service
@Transactional
public class WeChatPayServieImpl implements WeChatPayservice {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(WeChatPayServieImpl.class);

    @Autowired
    private WXPayConfig wxPayConfig;

    @Autowired
    private WXMiniPayConfig wxMiniPayConfig;

    /**
     * 功能描述:
     * 〈微信统一下单接口〉
     *
     * @param params 请求参数转xml格式
     * @author : 刘远杰
     */
    public String unifiedOrder(String params) {
        String requestUrl = "https://" + WXPayConstants.DOMAIN_API + WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
        log.info("调用微信统一下单接口url:[{}],params:[{}]", params, requestUrl);

        // 预支付接口地址
        String result = "";
        TenpayHttpClient httpClient = new TenpayHttpClient();
        httpClient.setReqContent(requestUrl);
        if (httpClient.callHttpPost(requestUrl, params)) {
            result = httpClient.getResContent();
            log.info("调用微信统一下单接口返回结果：{}", result);
        } else {
            log.error("调用微信统一下单接口异常");
            throw new ServiceException(ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID);
        }
        return result;
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
        prepayReqHandler.setParameter("appid", wxPayConfig.APP_ID);
        prepayReqHandler.setParameter("mch_id", wxPayConfig.MCH_ID);
        prepayReqHandler.setParameter("nonce_str", nonceStr);
        prepayReqHandler.setParameter("out_trade_no", params);

        String signKey = wxPayConfig.API_KEY;
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
     * 微信小程序根据微信code获取openid
     *
     * @param code 微信小程序获取到的code
     * @return
     * @date 2020-01-02 17:13
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public JSONObject getOpenid(String code) {
        log.debug("------------------收到微信code:{}------------------", code);

        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxMiniPayConfig.APP_ID + "&secret=" + wxMiniPayConfig.APP_SECRET + "&js_code=" + code + "&grant_type=authorization_code";

        log.info("调用微信token查询订接口，参数：{},url：{}", code, requestUrl);
        //通过code换取token
        //顺序不能变化
        log.debug("获取token的URL为:{}", requestUrl);
        return HttpsUtil.httpGet(requestUrl);
    }

    /**
     * 获取预支付id并再次签名
     *
     * @param h5WxPayDTO: 交易单信息
     * @return: 支付后结果
     * @date 2020-01-02 15:34
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public WXPayResponseDTO wechatPay(@RequestBody H5WXPayDTO h5WxPayDTO) throws Exception {
        WXPayResponseDTO WxPayResponseDTO = new WXPayResponseDTO();
        String nonceStr = IdGenerator.defaultSnowflakeId().toString();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        //封装微信统一下单接口参数
        String params = getUnifiedOrderParams(h5WxPayDTO, nonceStr);

        log.info("微信统一下单接口入参:[{}]", params);
        // 2.调用微信支付接口，返回预支付id
        String resultXml = this.unifiedOrder(params);
        log.info("微信统一下单接口出参:[{}]", resultXml);

        Map<String, String> result = WXPayUtil.xmlToMap(resultXml);

        // 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
        String prepayId = "";

        // mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟。
        String mwebUrl = "";

        // trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
        String codeUrl = "";

        // 微信支付接口详细可查看：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
        // 只有return_code和result_code同时为SUCCESS代表支付成功
        if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code"))) {
            prepayId = result.get("prepay_id");
            // 交易类型trade_type
            //JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付
            if ("NATIVE".equals(result.get("trade_type"))) {
                codeUrl = result.get("code_url");
            } else {
                mwebUrl = result.get("mweb_url");
            }
        } else {
            mlogger.info(ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID.getCode(),
                    ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID.getMessage());
            throw new ServiceException(ThirdTransferStatusCode.WECHAT_PRE_PAY_PAYSN_NO_PREPAYID);
        }

        // 4.签名生成参数
        Map<String, String> map = new HashMap<>();

        // h5和公众号支付用的是同一个appid
        map.put("appId", wxPayConfig.APP_ID);

        // 小程序支付用的是小程序的APPID
        if ("wxMiniProgram".equals(h5WxPayDTO.getTradeType())) {
            map.put("appId", wxMiniPayConfig.APP_ID);
        }

        map.put("timeStamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("package", "prepay_id=" + prepayId);
        map.put("signType", wxPayConfig.SIGN_TYPE);

        // 返回结果封装
        WxPayResponseDTO.setAppId(wxPayConfig.APP_ID);

        if ("wxMiniProgram".equals(h5WxPayDTO.getTradeType())) {
            WxPayResponseDTO.setAppId(wxMiniPayConfig.APP_ID);
        }
        WxPayResponseDTO.setPaySign(WXPayUtil.generateSignature(map, wxPayConfig.API_KEY, WXPayConstants.SignType.MD5));
        WxPayResponseDTO.setTimeStamp(timestamp);
        WxPayResponseDTO.setNonceStr(nonceStr);
        WxPayResponseDTO.setSignType(wxPayConfig.SIGN_TYPE);
        WxPayResponseDTO.setPackages("prepay_id=" + prepayId);
        WxPayResponseDTO.setMwebUrl(mwebUrl);
        WxPayResponseDTO.setCodeUrl(codeUrl);
        WxPayResponseDTO.setTradeType(result.get("trade_type"));
        return WxPayResponseDTO;
    }


    /**
     * 功能描述:
     * 〈封装微信统一下单接口参数〉
     *
     * @param h5WXPayDTO 微信统一下单实体
     * @param nonceStr   随机字符串
     * @author : 刘远杰
     */
    private String getUnifiedOrderParams(H5WXPayDTO h5WXPayDTO, String nonceStr) throws Exception {
        // 1.获取生成预支付订单的请求类
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler("UTF-8");

        // 封装必要参数
        prepayReqHandler.setParameter("appid", wxPayConfig.APP_ID);
        prepayReqHandler.setParameter("attach", wxPayConfig.APP_ID);
        prepayReqHandler.setParameter("body", h5WXPayDTO.getBody());
        prepayReqHandler.setParameter("mch_id", wxPayConfig.MCH_ID);
        prepayReqHandler.setParameter("nonce_str", nonceStr);
        prepayReqHandler.setParameter("notify_url", wxPayConfig.NOTIFY_URL);
        prepayReqHandler.setParameter("out_trade_no", h5WXPayDTO.getPaySn());
        prepayReqHandler.setParameter("spbill_create_ip", h5WXPayDTO.getIp());
        prepayReqHandler.setParameter("total_fee", h5WXPayDTO.getTotalFee());
        prepayReqHandler.setParameter("trade_type", h5WXPayDTO.getTradeType());

        // 根据前端传递的trade-type不同而区分，微信小程序支付、h5支付、公众号支付
        // 如果是h5(非微信浏览器支付)则scene_info为必填
        if ("MWEB".equals(h5WXPayDTO.getTradeType())) {
            String sceneInfo = "{\"h5_info\": {\"type\":\"" + wxPayConfig.SCENE_INFO_TYPE + "\",\"wap_url\": \"" + wxPayConfig.WAP_URL + "\",\"wap_name\": \"" + wxPayConfig.WAP_NAME + "\"}}";
            prepayReqHandler.setParameter("scene_info", sceneInfo);
        } else if ("JSAPI".equals(h5WXPayDTO.getTradeType())) {
            // trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
            prepayReqHandler.setParameter("openid", h5WXPayDTO.getOpenId());
        } else if ("wxMiniProgram".equals(h5WXPayDTO.getTradeType())) {
            prepayReqHandler.setParameter("trade_type", "JSAPI");
            // 如果是小程序支付，则根据code值查询openid值
            JSONObject jsonObject = this.getOpenid(h5WXPayDTO.getCode());
            if (jsonObject == null) {
                return null;
            }
            String openId = jsonObject.getString(WechatEnum.WECHAT_OPENID.value());
            // 微信小程序的支付 appid和appSECRET和公众号支付不一样要单独赋值
            prepayReqHandler.setParameter("appid", wxMiniPayConfig.APP_ID);
            prepayReqHandler.setParameter("attach", wxMiniPayConfig.APP_ID);
            prepayReqHandler.setParameter("openid", openId);
        }

        // 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
        prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign(wxPayConfig.API_KEY));
        return prepayReqHandler.getXmlParams();
    }


}
