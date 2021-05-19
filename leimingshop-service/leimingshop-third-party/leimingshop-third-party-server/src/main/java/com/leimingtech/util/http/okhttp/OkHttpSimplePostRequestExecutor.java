/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http.okhttp;

import com.leimingtech.util.http.RequestHttp;
import com.leimingtech.util.http.SimplePostRequestExecutor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

/**
 * .
 *
 * @date 2017/5/4
 */
@Slf4j
public class OkHttpSimplePostRequestExecutor extends SimplePostRequestExecutor<OkHttpClient, OkHttpProxyInfo> {
    public OkHttpSimplePostRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, String postEntity) throws IOException {
        RequestBody body = RequestBody.Companion.create(postEntity, MediaType.parse("text/plain; charset=utf-8"));
        Request request = new Request.Builder().url(uri).post(body).build();
        Response response = requestHttp.getRequestHttpClient().newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

}
