/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.handle;

import com.leimingtech.modules.util.MD5Util;
import com.leimingtech.modules.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.*;

/**
 * 功能描述：
 * <>
 *
 * @author 刘远杰
 * @Date 2019/5/23 11:08
 * Version 7.0
 **/
@Slf4j
public class ClientResponseHandler {

    /**
     * 应答原始内容
     */
    private String content;

    /**
     * 应答的参数
     */
    private SortedMap parameters;

    /**
     * 密钥
     */
    private String key;

    /**
     * 字符集
     */
    private String charset;

    public ClientResponseHandler() {
        this.content = "";
        this.parameters = new TreeMap();
        this.key = "";
        this.charset = "utf8";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;

        try {
            this.doParse();
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
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

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
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
        String sign = MD5Util.MD5Encode(sb.toString(), this.charset).toLowerCase();

        String tenpaySign = this.getParameter("sign").toLowerCase();

        return tenpaySign.equals(sign);
    }

    /**
     * 是否财付通签名
     *
     * @param signParameterArray 签名的参数数组
     * @return boolean
     */
    protected boolean isTenpaySign(String signParameterArray[]) {

        StringBuilder signPars = new StringBuilder();
        for (int index = 0; index < signParameterArray.length; index++) {
            String k = signParameterArray[index];
            String v = this.getParameter(k);
            if (null != v && !"".equals(v)) {
                signPars.append(k + "=" + v + "&");
            }
        }

        signPars.append("key=" + this.getKey());

        //算出摘要
        String sign = MD5Util.MD5Encode(
                signPars.toString(), this.charset).toLowerCase();

        String tenpaySign = this.getParameter("sign").toLowerCase();

        return tenpaySign.equals(sign);
    }

    /**
     * 解析XML内容
     */
    protected void doParse() throws JDOMException, IOException {
        String xmlContent = this.getContent();

        //解析xml,得到map
        Map m = XMLUtil.doXMLParse(xmlContent);

        //设置参数
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            String v = (String) m.get(k);
            this.setParameter(k, v);
        }

    }


}
