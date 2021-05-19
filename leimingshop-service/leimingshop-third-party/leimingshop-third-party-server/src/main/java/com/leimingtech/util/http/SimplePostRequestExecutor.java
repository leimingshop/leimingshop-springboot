/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http;


import com.leimingtech.util.http.apache.ApacheSimplePostRequestExecutor;
import com.leimingtech.util.http.jodd.JoddHttpSimplePostRequestExecutor;
import com.leimingtech.util.http.okhttp.OkHttpSimplePostRequestExecutor;

import java.io.IOException;
import java.util.Map;

/**
 * 简单的POST请求执行器，请求的参数是String, 返回的结果也是String
 */
public abstract class SimplePostRequestExecutor<H, P> implements RequestExecutor<String, String> {
    protected RequestHttp<H, P> requestHttp;

    public SimplePostRequestExecutor(RequestHttp requestHttp) {
        this.requestHttp = requestHttp;
    }

    public static RequestExecutor<String, String> create(RequestHttp requestHttp) {
        switch (requestHttp.getRequestType()) {
            case APACHE_HTTP:
                return new ApacheSimplePostRequestExecutor(requestHttp);
            case JODD_HTTP:
                return new JoddHttpSimplePostRequestExecutor(requestHttp);
            case OK_HTTP:
                return new OkHttpSimplePostRequestExecutor(requestHttp);
            default:
                throw new IllegalArgumentException("非法请求参数");
        }
    }

    @Override
    public void execute(String uri, String data, ResponseHandler<String> handler)
            throws IOException {
        handler.handle(this.execute(uri, data));
    }

    @Override
    public String execute(String uri, String data, Map<String, String> headers) throws IOException {
        return this.execute(uri, data);
    }

    @Override
    public void execute(String uri, String data, Map<String, String> headers, ResponseHandler<String> handler) throws IOException {
        this.execute(uri, data, handler);

    }

}
