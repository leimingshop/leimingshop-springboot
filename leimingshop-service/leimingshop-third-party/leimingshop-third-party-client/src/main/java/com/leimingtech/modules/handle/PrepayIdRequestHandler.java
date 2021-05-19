/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.handle;

import com.leimingtech.modules.util.MD5Util;
import com.leimingtech.modules.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 功能描述：
 * <>
 *
 * @author 刘远杰
 * @Date 2019/5/23 11:04
 * Version 7.0
 **/
@Slf4j
public class PrepayIdRequestHandler extends RequestHandler {

    public PrepayIdRequestHandler(String enc) {
        super(enc);
    }

    public String createMD5Sign(String signKey) {
        StringBuilder sb = new StringBuilder();
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        String params = sb.append("key=" + signKey).substring(0);
        String sign = MD5Util.MD5Encode(params, "utf8");
        return sign.toUpperCase();
    }

    // 提交预支付
    public String sendPrepay() throws Exception {
        String prepayid = "";
        String params = this.getXmlParams();
        log.debug("请求参数：" + params);
        String requestUrl = super.getGateUrl();
        log.debug("请求url：" + requestUrl);
        TenpayHttpClient httpClient = new TenpayHttpClient();
        httpClient.setReqContent(requestUrl);
        String resContent = "";
        if (httpClient.callHttpPost(requestUrl, params)) {
            resContent = httpClient.getResContent();
            System.out.println("获取prepayid的返回值：" + resContent);
            Map<String, String> map = XMLUtil.doXMLParse(resContent);
            if (map.containsKey("prepay_id")) {
                prepayid = map.get("prepay_id");
            }

        }
        return prepayid;
    }

    /**
     * 功能描述:
     * 〈获得参数xml〉
     *
     * @param
     * @return : java.lang.String
     * @author : 刘远杰
     */
    public String getXmlParams() throws Exception {
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        StringBuilder sb = new StringBuilder("<xml>");
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append("<" + k + ">" + v + "</" + k + ">");
        }
        sb.append("</xml>");
        return sb.substring(0);
    }

}
