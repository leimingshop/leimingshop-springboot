/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http.okhttp;

import com.leimingtech.util.http.RequestHttp;
import com.leimingtech.util.http.SimpleGetRequestExecutor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * .
 *
 * @date 2017/5/4
 */
public class OkHttpSimpleGetRequestExecutor extends SimpleGetRequestExecutor<OkHttpClient, OkHttpProxyInfo> {
    public OkHttpSimpleGetRequestExecutor(RequestHttp requestHttp) {
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

        //得到httpClient
        OkHttpClient client = requestHttp.getRequestHttpClient();
        Request request = new Request.Builder().url(uri).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
