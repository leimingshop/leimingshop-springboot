/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.util;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 功能描述：
 * <XMLUtil类>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/5/23 9:43
 **/
public class XMLUtil {

    private XMLUtil() {
    }

    /**
     * 功能描述:
     * 〈解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。〉
     *
     * @param strxml xml字符串
     * @author : 刘远杰
     */
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (StringUtils.isBlank(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = XMLUtil.getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    /**
     * 功能描述:
     * 〈获取子结点的xml〉
     *
     * @param children 子节点集合
     * @author : 刘远杰
     */
    public static String getChildrenText(List children) {
        StringBuilder sb = new StringBuilder();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    /**
     * 功能描述:
     * 〈获取xml编码字符集〉
     *
     * @param strxml xml字符串
     * @author : 刘远杰
     */
    public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
        InputStream in = HttpClientUtil.String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        in.close();
        return (String) doc.getProperty("encoding");
    }

    /**
     * 功能描述:
     * 〈支付成功，返回微信服务器〉
     *
     * @param return_code 错误码
     * @param return_msg  错误信息
     * @author : 刘远杰
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    /**
     * 功能描述:
     * 〈map转xml格式字符串〉
     *
     * @param map map对象
     * @author : 刘远杰
     */
    public static String createXML(Map<String, Object> map) {
        Set<Map.Entry<String, Object>> set = map.entrySet();
        set.iterator();
        return null;
    }

}
