/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http.jodd;

import com.leimingtech.util.http.RequestHttp;
import com.leimingtech.util.http.SimplePostRequestExecutor;
import jodd.http.HttpConnectionProvider;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.ProxyInfo;
import jodd.util.StringPool;

import java.io.IOException;

/**
 * @date 2017/5/4
 */
public class JoddHttpSimplePostRequestExecutor extends SimplePostRequestExecutor<HttpConnectionProvider, ProxyInfo> {
    public JoddHttpSimplePostRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, String postEntity) throws IOException {
        HttpConnectionProvider provider = requestHttp.getRequestHttpClient();
        ProxyInfo proxyInfo = requestHttp.getRequestHttpProxy();

        HttpRequest request = HttpRequest.post(uri);
        if (proxyInfo != null) {
            provider.useProxy(proxyInfo);
        }
        request.withConnectionProvider(provider);
        if (postEntity != null) {
            request.bodyText(postEntity);
        }
        HttpResponse response = request.send();
        response.charset(StringPool.UTF_8);

        return response.bodyText();
    }

}
