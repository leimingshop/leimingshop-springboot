/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.util;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 * <HttpClientUtil类>
 *
 * @author 刘远杰
 * @Date 2019/5/23 9:51
 * Version 7.0
 **/
public class HttpClientUtil {

    /**
     * http客户端工具类
     */
    public static final String SUNX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";

    private HttpClientUtil() {
    }

    /**
     * get HttpURLConnection
     *
     * @param strUrl url地址
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        return (HttpURLConnection) url.openConnection();
    }

    /**
     * get HttpsURLConnection
     *
     * @param strUrl url地址ַ
     * @return HttpsURLConnection
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        return (HttpsURLConnection) url.openConnection();
    }

    /**
     * 获取不带查询串的url
     *
     * @param strUrl
     * @return String
     */
    public static String getURL(String strUrl) {

        if (null != strUrl) {
            int indexOf = strUrl.indexOf('?');
            if (-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }

        return strUrl;

    }

    /**
     * 获取查询串
     *
     * @param strUrl
     * @return String
     */
    public static String getQueryString(String strUrl) {

        if (null != strUrl) {
            int indexOf = strUrl.indexOf('?');
            if (-1 != indexOf) {
                return strUrl.substring(indexOf + 1, strUrl.length());
            }

            return "";
        }

        return strUrl;
    }

    /**
     * 查询字符串转化为map
     * name1=key1&name2=key2&...
     *
     * @param queryString
     * @return
     */
    public static Map queryString2Map(String queryString) {
        if (null == queryString || "".equals(queryString)) {
            return null;
        }

        Map m = new HashMap();
        String[] strArray = queryString.split("&");
        for (int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            HttpClientUtil.putMapByPair(pair, m);
        }

        return m;

    }

    /**
     * 把键值添加到map
     * pair:name=value
     *
     * @param pair name=value
     * @param m
     */
    public static void putMapByPair(String pair, Map m) {

        if (null == pair || "".equals(pair)) {
            return;
        }

        int indexOf = pair.indexOf('=');
        if (-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf + 1, pair.length());
            if (null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }

    /**
     * BufferedReader转换成String<br/>
     * 注意:流关闭需要自行处理
     *
     * @param reader
     * @return
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuilder buf = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    /**
     * 处理输出<br/>
     * 注意:流关闭需要自行处理
     *
     * @param out
     * @param data
     * @param len
     * @throws IOException
     */
    public static void doOutput(OutputStream out, byte[] data, int len)
            throws IOException {
        int dataLen = data.length;
        int off = 0;
        while (off < data.length) {
            if (len >= dataLen) {
                out.write(data, off, dataLen);
                off += dataLen;
            } else {
                out.write(data, off, len);
                off += len;
                dataLen -= len;
            }
            out.flush();
        }

    }

    /**
     * 获取SSLContext
     *
     * @param trustFileInputStream
     * @param trustPasswd
     * @param keyFileInputStream
     * @param keyPasswd
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static SSLContext getSSLContext(
            FileInputStream trustFileInputStream, String trustPasswd,
            FileInputStream keyFileInputStream, String keyPasswd)
            throws NoSuchAlgorithmException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException,
            KeyManagementException {

        // ca
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(HttpClientUtil.SUNX509);
        KeyStore trustKeyStore = KeyStore.getInstance(HttpClientUtil.JKS);
        trustKeyStore.load(trustFileInputStream, HttpClientUtil
                .str2CharArray(trustPasswd));
        tmf.init(trustKeyStore);

        final char[] kp = HttpClientUtil.str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(HttpClientUtil.SUNX509);
        KeyStore ks = KeyStore.getInstance(HttpClientUtil.PKCS12);
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);

        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance(HttpClientUtil.TLS);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

        return ctx;
    }

    /**
     * 字符串转换成char数组
     *
     * @param str
     * @return char[]
     */
    public static char[] str2CharArray(String str) {
        if (null == str) {
            return null;
        }

        return str.toCharArray();
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * InputStream转换成Byte
     * 注意:流关闭需要自行处理
     *
     * @param in
     * @return byte
     * @throws Exception
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        int BUFFER_SIZE = 4096;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;

        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }

        data = null;
        byte[] outByte = outStream.toByteArray();
        outStream.close();

        return outByte;
    }

    /**
     * InputStream转换成String
     * 注意:流关闭需要自行处理
     *
     * @param in
     * @param encoding 编码
     * @return String
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding) throws IOException {

        return new String(InputStreamTOByte(in), encoding);

    }


}
