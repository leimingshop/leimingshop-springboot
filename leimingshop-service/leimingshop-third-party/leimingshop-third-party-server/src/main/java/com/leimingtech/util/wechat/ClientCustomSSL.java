/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.wechat;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:微信退款双向验证
 * @Date: 2019/6/22 21:27
 * @Version: V1.0
 */
public class ClientCustomSSL {

    public String doRefund(String url, String data, String mch_id) throws Exception {
        /**
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
         */
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //P12文件目录（更换账户需要替换下面路径下的文件）
        InputStream instream = this.getClass().getResourceAsStream("/config/wxpay/apiclient_cert.p12");
        try {
            /**
             * 此处要改
             * */
            // keyStore.load(instream, "1266063701".toCharArray());//这里写密码..默认是你的MCHID
            keyStore.load(instream, mch_id.toCharArray());//这里写密码..默认是你的MCHID

        } finally {
            instream.close();
        }
        /**
         * 此处要改
         * */
        SSLContext sslcontext = SSLContexts.custom()
                // .loadKeyMaterial(keyStore, "1266063701".toCharArray())//这里也是写密码的
                .loadKeyMaterial(keyStore, mch_id.toCharArray())//这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            // 设置响应头信息
            HttpPost httpost = new HttpPost(url);
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
