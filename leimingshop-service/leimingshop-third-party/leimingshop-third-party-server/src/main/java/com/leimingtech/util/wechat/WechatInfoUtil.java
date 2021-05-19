/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.wechat;

/**
 * @ClassName WechatInfoUtil
 * @Description 微信用户工具类
 * @Author DY
 * @Date 2019/5/24 10:23
 * @Version 1.0
 **/
public class WechatInfoUtil {

    /**
     * 获取code后,请求以下链接获取access_token
     * <p>
     * appid        公众号唯一标识
     * secret       公众号的appsecret
     * code         填写获取的code
     * grant_type   固定填写为authorization_code
     * <p>
     * 返回:access_token,expires_in(超时时间),refresh_token(用户刷新token:
     * 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新
     * ，refresh_token有效期为30天，当refresh_token失效之后，需要用户重新授权。)
     * ,openid(唯一标识),scope
     */
    public static final String ACCESS_TOKEN_HTTPS = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=%s" +
            "&secret=%s" +
            "&code=%s" +
            "&grant_type=authorization_code";

    //获取token地址
    /**
     * access_token   上步中获得的token
     * openid         上步中的用户唯一标识
     * lang           语言:zh_CN 简体，zh_TW 繁体，en 英语
     */
    public static final String USER_MESSAGE = "https://api.weixin.qq.com/sns/userinfo?" +
            "access_token=%s" +
            "&openid=%s" +
            "&lang=zh_CN";

    //拉取用户信息的请求地址
    private WechatInfoUtil() {
    }

    //替换字符串
    public static String getCode(String appid, String secrt, String code) {
        return String.format(ACCESS_TOKEN_HTTPS, appid, secrt, code);
    }

    //获取用户信息替换字符串
    public static String getUserMessage(String token, String openId) {
        return String.format(USER_MESSAGE, token, openId);
    }
}
