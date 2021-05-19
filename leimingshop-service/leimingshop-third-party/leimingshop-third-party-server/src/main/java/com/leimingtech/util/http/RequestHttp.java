/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http;

/**
 * Create date  2017/4/22.
 */
public interface RequestHttp<H, P> {

    /**
     * 返回httpClient.
     *
     * @return 返回httpClient
     */
    H getRequestHttpClient();

    /**
     * 返回httpProxy.
     *
     * @return 返回httpProxy
     */
    P getRequestHttpProxy();

    /**
     * 返回HttpType.
     *
     * @return HttpType
     */
    HttpType getRequestType();

}
