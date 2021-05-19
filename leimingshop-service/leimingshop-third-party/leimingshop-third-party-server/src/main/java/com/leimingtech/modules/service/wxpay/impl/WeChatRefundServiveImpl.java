/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wxpay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.leimingtech.config.wechat.WXPayConfig;
import com.leimingtech.modules.dto.wxpay.H5WxRefundDTO;
import com.leimingtech.modules.dto.wxpay.H5WxRefundResponseDTO;
import com.leimingtech.modules.handle.RefundRequestHandler;
import com.leimingtech.modules.service.wxpay.WeChatRefundservice;
import com.leimingtech.modules.util.XMLUtil;
import com.leimingtech.util.wechat.ClientCustomSSL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:微信退款
 * @Date: 2019/6/22 15:25
 * @Version: V1.0
 */
@Slf4j
@Service
@Transactional
public class WeChatRefundServiveImpl implements WeChatRefundservice {

    @Autowired
    private WXPayConfig wxPayConfig;

    /**
     * @param h5WxRefundDTO:微信退款实体
     * @Author: SWH ab4856812@163.com
     * @Description:微信退款
     * @Date: 2019/6/22 15:28
     * @Version: V1.0
     */

    @Override
    public H5WxRefundResponseDTO refundWX(@RequestBody H5WxRefundDTO h5WxRefundDTO) {

        H5WxRefundResponseDTO h5WxRefundResponseDTO = new H5WxRefundResponseDTO();
        Map<String, Object> result = new HashMap<>();
        // 退款单号
        String out_refund_no = h5WxRefundDTO.getOutrefundno();
        // 订单号
        String out_trade_no = h5WxRefundDTO.getOuttradeno();
        // 总金额
        String total_fee = String.valueOf(h5WxRefundDTO.getTotalfee());
        //退款金额
        String refund_fee = String.valueOf(h5WxRefundDTO.getRefundfee());
        // 随机字符串
        String nonce_str = String.valueOf(h5WxRefundDTO.getNonceStr());
        //微信公众号apid
        String appid = wxPayConfig.APP_ID;
        //微信公众号appsecret
        String appsecret = wxPayConfig.APP_SECRET;
        //微信商户id
        String mch_id = wxPayConfig.MCH_ID;
        //就是MCHID
        String op_user_id = wxPayConfig.MCH_ID;
        //商户平台上的那个KEY
        String apikey = wxPayConfig.API_KEY;

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
            h5WxRefundResponseDTO = JSON.toJavaObject(json, H5WxRefundResponseDTO.class);
        } catch (Exception e) {
            log.error("调用外部微信退款接口错误:{}", e);
            h5WxRefundResponseDTO.setReturn_code("SYSTEMERROR");
            h5WxRefundResponseDTO.setReturn_msg("调用微信退款服务异常，请稍后再试");
            return h5WxRefundResponseDTO;
        }
        return h5WxRefundResponseDTO;
    }
}
