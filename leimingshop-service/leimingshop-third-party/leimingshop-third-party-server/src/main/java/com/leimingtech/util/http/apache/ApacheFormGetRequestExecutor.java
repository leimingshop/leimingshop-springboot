/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http.apache;

import com.leimingtech.util.http.FormGetRequestExecutor;
import com.leimingtech.util.http.RequestHttp;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * @date 2017/5/4
 */
public class ApacheFormGetRequestExecutor extends FormGetRequestExecutor<CloseableHttpClient, HttpHost> {
    public ApacheFormGetRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, String queryParam) throws IOException {
        if (queryParam != null) {
            if (uri.indexOf('?') == -1) {
                uri += '?';
            }
            uri += uri.endsWith("?") ? queryParam : '&' + queryParam;
        }
        HttpGet httpGet = new HttpGet(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
            httpGet.setConfig(config);
        }

        try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpGet)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            return responseContent;
        } finally {
            httpGet.releaseConnection();
        }
    }

    @Override
    public String execute(String uri, String queryParam, Map<String, String> headers) throws IOException {
        if (queryParam != null) {
            if (uri.indexOf('?') == -1) {
                uri += '?';
            }
            uri += uri.endsWith("?") ? queryParam : '&' + queryParam;
        }
        HttpGet httpGet = new HttpGet(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
            httpGet.setConfig(config);
        }

        if (headers != null && headers.size() > 0) {
            headers.forEach((key, value) -> {
                httpGet.addHeader(key, value);
            });
        }

        try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpGet)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            return responseContent;
        } finally {
            httpGet.releaseConnection();
        }
    }

}
