/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * <读取微信支付配置proprities文件>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/5/30 16:37
 **/
@Data
@Component
@ConfigurationProperties(prefix = "wxpay")
@PropertySource(value = "wxpay/weixin-${spring.profiles.active}.properties")
public class WXPayConfig {

    /**
     * 微信支付分配的公众账号ID（企业号corpid即为此appId）
     */
    public String APP_ID;

    /**
     * 应用密钥
     */
    public String APP_SECRET;

    /**
     * 微信支付分配的商户号
     */
    public String MCH_ID;

    /**
     * API密钥，在商户平台设置
     */
    public String API_KEY;

    /**
     * 同步接收微信支付结果通知的回调地址，不能携带参数。
     */
    public String BACK_URL;

    /**
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     */
    public String NOTIFY_URL;

    /**
     * 交易类型
     */
    public String TRADE_TYPE;

    /**
     * 加密方式
     */
    public String SIGN_TYPE;

    /**
     * 微信授权重定向地址
     */
    public String REDIRECT_URI;

    /**
     * 场景类型
     */
    public String SCENE_INFO_TYPE;

    /**
     * WAP网站URL地址
     */
    public String WAP_URL;

    /**
     * WAP网站名
     */
    public String WAP_NAME;


}
