/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.service;

import com.leimingtech.commons.tools.security.user.SellerDetail;

/**
 * 资源服务
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/7/16 17:46
 **/
public interface ResourceService {
    /**
     * 获取Seller资源信息
     *
     * @param token:  jwt-token
     * @param url:    请求URL
     * @param method: GET/POST
     * @return 商户ID
     * @date 2019/6/25 18:06
     * @author lixiang
     **/
    SellerDetail resource(String token, String url, String method);
}
