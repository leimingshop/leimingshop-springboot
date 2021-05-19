/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.util.http.*;
import com.leimingtech.util.http.apache.ApacheHttpClientBuilder;
import com.leimingtech.util.http.apache.DefaultApacheHttpClientBuilder;
import com.leimingtech.util.neteasy.utils.CheckSumBuilder;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

public abstract class BaseServiceImpl implements RequestHttp<CloseableHttpClient, HttpHost> {

    private CloseableHttpClient httpClient;
    private HttpHost httpProxy;

    @Override
    public CloseableHttpClient getRequestHttpClient() {
        return httpClient;
    }

    @Override
    public HttpHost getRequestHttpProxy() {
        return httpProxy;
    }

    @Override
    public HttpType getRequestType() {
        return HttpType.APACHE_HTTP;
    }

    @PostConstruct
    public void initHttp() {
        ApacheHttpClientBuilder apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
        this.httpClient = apacheHttpClientBuilder.build();
    }

    /**
     * @param queryParam 请求参数
     * @param url        请求接口地址
     * @return 接口响应字符串
     */
    public String get(String url, String queryParam) throws IOException {
        String resultContent = this.execute(FormGetRequestExecutor.create(this), url, queryParam);
        return resultContent;
    }

    /**
     * @param postData 请求参数json值
     * @param url      请求接口地址
     * @return 接口响应字符串
     */
    public String formPost(String url, String postData) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(postData);
        String resultContent = this.execute(FormPostRequestExecutor.create(this), url, jsonObject);
        return resultContent;
    }

    /**
     * @param paramMap 请求参数map
     * @param url      请求接口地址
     * @return 接口响应字符串
     */
    public String formPost(String url, Map<String, Object> paramMap) throws IOException {
        String resultContent = this.execute(FormPostRequestExecutor.create(this), url, paramMap);
        return resultContent;
    }

    /**
     * @param postData 请求参数json值
     * @param url      请求接口地址
     * @return 接口响应字符串
     */
    public String post(String url, String postData) throws IOException {
        String resultContent = this.execute(SimplePostRequestExecutor.create(this), url, postData);
        return resultContent;
    }

    public <T, E> T execute(RequestExecutor<T, E> executor, String url, E data) throws IOException {
        return executor.execute(url, data, CheckSumBuilder.getSignParam());
    }

}
