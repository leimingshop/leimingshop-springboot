/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.handle;

import com.leimingtech.modules.util.MD5Util;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 应答处理类
 * 应答处理类继承此类，重写isTenpaySign方法即可。
 */
public class ResponseHandler {

    /**
     * 密钥
     */
    private String key;

    /**
     * 应答的参数
     */
    private SortedMap parameters;

//    private HttpServletRequest request;

//    private HttpServletResponse response;

    private String uriEncoding;

    private String enc;

    /**
     * 构造函数
     *
     * @param m
     */
    public ResponseHandler(Map m, String enc) {
//        this.request = request;
//        this.response = response;

        this.enc = enc;
        this.key = "";
        this.parameters = new TreeMap();
        this.uriEncoding = "";

        // Map m = this.request.getParameterMap();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            String v = "";
            v = ((String) m.get(k));
            this.setParameter(k, v);
        }

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
    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
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
     * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     * @return boolean
     */
    public boolean isTenpaySign() {
        StringBuilder sb = new StringBuilder();
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + this.getKey());

        //算出摘要
        // String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

        String tenpaySign = this.getParameter("sign").toLowerCase();

        return tenpaySign.equals(sign);
    }

    /**
     * 获取uri编码
     *
     * @return String
     */
    public String getUriEncoding() {
        return uriEncoding;
    }

    /**
     * 设置uri编码
     *
     * @param uriEncoding
     * @throws UnsupportedEncodingException
     */
    public void setUriEncoding(String uriEncoding)
            throws UnsupportedEncodingException {
        if (!"".equals(uriEncoding.trim())) {
            this.uriEncoding = uriEncoding;

            // 编码转换
//             String enc = TenpayUtil.getCharacterEncoding(request, response);
            Iterator it = this.parameters.keySet().iterator();
            while (it.hasNext()) {
                String k = (String) it.next();
                String v = this.getParameter(k);
                v = new String(v.getBytes(uriEncoding.trim()), enc);
                this.setParameter(k, v);
            }
        }
    }

}