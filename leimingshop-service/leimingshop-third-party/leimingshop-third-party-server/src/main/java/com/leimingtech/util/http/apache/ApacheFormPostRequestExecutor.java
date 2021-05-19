/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http.apache;

import com.leimingtech.util.http.FormPostRequestExecutor;
import com.leimingtech.util.http.RequestHttp;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date 2017/5/4
 */
public class ApacheFormPostRequestExecutor extends FormPostRequestExecutor<CloseableHttpClient, HttpHost> {
    public ApacheFormPostRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, Map<String, Object> paramEntity) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
            httpPost.setConfig(config);
        }
        if (paramEntity != null && paramEntity.size() > 0) {
            List<NameValuePair> list = paramEntity.entrySet().stream().map(a -> new BasicNameValuePair(a.getKey(), a.getValue() + "")).collect(Collectors.toList());
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            entity.setContentType("application/x-www-form-urlencoded; charset=utf-8");
            httpPost.setEntity(entity);
        }

        try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            return responseContent;
        } finally {
            httpPost.releaseConnection();
        }
    }

    @Override
    public String execute(String uri, Map<String, Object> paramEntity, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
            httpPost.setConfig(config);
        }
        if (paramEntity != null && paramEntity.size() > 0) {
            List<NameValuePair> list = paramEntity.entrySet().stream().map(a ->
                    new BasicNameValuePair(a.getKey(), a.getValue() + "")).collect(Collectors.toList());
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            entity.setContentType("application/x-www-form-urlencoded; charset=utf-8");
            httpPost.setEntity(entity);
        }
        if (headers != null && headers.size() > 0) {
            headers.entrySet().stream().forEach(a -> {
                httpPost.addHeader(a.getKey(), a.getValue());
            });
        }

        try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            return responseContent;
        } finally {
            httpPost.releaseConnection();
        }
    }

}
