/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 调用微信服务返回状态码
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0 2019/7/2
 */
public class WechatStatusCode extends ServiceStatusCode {

    /**
     * 获取微信OPENID失败
     */
    public static final ServiceStatusCode CLIENT_WECHAT_GET_CODE_FAIL = new WechatStatusCode("400101", "获取微信OPENID失败");
    /**
     * 获取微信OPENID失败
     */
    public static final ServiceStatusCode CLIENT_WECHAT_GET_WECHAT_USER_INFO_FAIL = new WechatStatusCode("400102", "获取微信用户信息失败");


    protected WechatStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }

}
