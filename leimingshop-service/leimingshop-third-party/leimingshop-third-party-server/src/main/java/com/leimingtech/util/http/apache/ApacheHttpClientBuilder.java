/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http.apache;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;

public interface ApacheHttpClientBuilder {
    CloseableHttpClient build();

    ApacheHttpClientBuilder httpProxyHost(String var1);

    ApacheHttpClientBuilder httpProxyPort(int var1);

    ApacheHttpClientBuilder httpProxyUsername(String var1);

    ApacheHttpClientBuilder httpProxyPassword(String var1);

    ApacheHttpClientBuilder sslConnectionSocketFactory(SSLConnectionSocketFactory var1);
}
