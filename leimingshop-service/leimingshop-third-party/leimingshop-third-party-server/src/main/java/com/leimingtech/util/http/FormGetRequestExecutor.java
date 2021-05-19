/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http;


import com.leimingtech.util.http.apache.ApacheFormGetRequestExecutor;

import java.io.IOException;
import java.util.Map;

/**
 * 简单的GET请求执行器.
 * 请求的参数是String, 返回的结果也是String
 */
public abstract class FormGetRequestExecutor<H, P> implements RequestExecutor<String, String> {
    protected RequestHttp<H, P> requestHttp;

    public FormGetRequestExecutor(RequestHttp<H, P> requestHttp) {
        this.requestHttp = requestHttp;
    }

    public static RequestExecutor<String, String> create(RequestHttp requestHttp) {
        return new ApacheFormGetRequestExecutor(requestHttp);
    }

    @Override
    public void execute(String uri, String param, ResponseHandler<String> handler) throws IOException {
        handler.handle(this.execute(uri, param));
    }

    @Override
    public void execute(String uri, String param, Map<String, String> headers, ResponseHandler<String> handler) throws IOException {
        handler.handle(this.execute(uri, param, headers));
    }

}
