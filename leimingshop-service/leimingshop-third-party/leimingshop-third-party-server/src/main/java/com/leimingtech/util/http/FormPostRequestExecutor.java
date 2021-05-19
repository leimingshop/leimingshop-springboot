/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http;


import com.leimingtech.util.http.apache.ApacheFormPostRequestExecutor;

import java.io.IOException;
import java.util.Map;

/**
 * 简单的POST请求执行器，请求的参数是String, 返回的结果也是String
 */
public abstract class FormPostRequestExecutor<H, P> implements RequestExecutor<String, Map<String, Object>> {
    protected RequestHttp<H, P> requestHttp;

    public FormPostRequestExecutor(RequestHttp requestHttp) {
        this.requestHttp = requestHttp;
    }

    public static RequestExecutor<String, Map<String, Object>> create(RequestHttp requestHttp) {
        return new ApacheFormPostRequestExecutor(requestHttp);
    }

    @Override
    public void execute(String uri, Map<String, Object> data, ResponseHandler<String> handler)
            throws IOException {
        handler.handle(this.execute(uri, data));
    }

    @Override
    public void execute(String uri, Map<String, Object> data, Map<String, String> headers, ResponseHandler<String> handler)
            throws IOException {
        handler.handle(this.execute(uri, data, headers));
    }


}
