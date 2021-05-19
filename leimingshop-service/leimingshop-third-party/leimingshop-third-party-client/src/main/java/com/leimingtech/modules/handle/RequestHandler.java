/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.handle;

import com.leimingtech.modules.util.MD5Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 请求处理类
 * 请求处理类继承此类，重写createSign方法即可。
 */
public class RequestHandler {

    /**
     * 网关url地址
     */
    private String gateUrl;

    /**
     * 密钥
     */
    private String key;

    /**
     * 请求的参数
     */
    private SortedMap parameters;

    private String enc;

    private String charset;

    /**
     * debug信息
     */
    private String debugInfo;
    private String last_errcode;
    private String Token;
    private String appid;
    private String partnerkey;
    private String appsecret;

    /**
     * 构造函数
     *
     * @param enc
     */
    public RequestHandler(String enc) {

        this.enc = enc;
        this.gateUrl = "https://gw.tenpay.com/gateway/pay.htm";
        this.key = "";
        this.parameters = new TreeMap();
    }

    /**
     * 初始化函数。
     */
    public void init(String app_id, String app_secret, String partner_key) {
    }

    /**
     * 获取入口地址,不包含参数值
     */
    public String getGateUrl() {
        return gateUrl;
    }

    /**
     * 设置入口地址,不包含参数值
     */
    public void setGateUrl(String gateUrl) {
        this.gateUrl = gateUrl;
    }

    /**
     * 获取密钥
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置密钥
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取参数值
     *
     * @param parameter 参数名称
     * @return String
     */
    public String getParameter(String parameter) {
        String s = (String) this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }

    /**
     * 设置参数值
     *
     * @param parameter      参数名称
     * @param parameterValue 参数值
     */
    public void setParameter(String parameter, Object parameterValue) {
        String v = "";
        if (null != parameterValue && parameterValue instanceof String) {
            v = ((String) parameterValue).trim();
        }
        this.parameters.put(parameter, v);
    }

    /**
     * 返回所有的参数
     *
     * @return SortedMap
     */
    public SortedMap getAllParameters() {
        return this.parameters;
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:创建签名
     * @Date: 2019/6/22 16:13
     * @Version: V1.0
     */
    public String createSign(SortedMap<String, String> packageParams) {
        StringBuilder sb = new StringBuilder();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + this.getKey());
        return MD5Util.MD5Encode(sb.toString(), this.charset)
                .toUpperCase();

    }

    /**
     * 获取带参数的请求URL
     *
     * @return String
     * @throws UnsupportedEncodingException
     */
    public String getRequestURL() throws UnsupportedEncodingException {

        this.createSign();

        StringBuilder sb = new StringBuilder();
        // String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();

            if (!"spbill_create_ip".equals(k)) {
                sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
            } else {
                sb.append(k + "=" + v.replace("\\.", "%2E") + "&");
            }
        }

        //去掉最后一个&
        String reqPars = sb.substring(0, sb.lastIndexOf("&"));

        return this.getGateUrl() + "?" + reqPars;

    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    protected void createSign() {
        StringBuilder sb = new StringBuilder();
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + this.getKey());
        // String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toUpperCase();

        this.setParameter("sign", sign);

    }

}
