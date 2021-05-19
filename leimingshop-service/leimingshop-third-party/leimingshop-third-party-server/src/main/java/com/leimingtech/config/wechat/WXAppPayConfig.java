/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhangtai
 * @date 2020/4/7 11:34
 * @Description:
 */
@Data
@Component
@ConfigurationProperties("wxpayapp")
@PropertySource(value = "wxpay/weixin-app-${spring.profiles.active}.properties")
public class WXAppPayConfig {

    /**
     * 微信支付分配的公众账号ID（企业号corpid即为此appId）
     */
    public String APP_ID;

    /**
     * 微信支付分配的商户号
     */
    public String MCH_ID;

    /**
     * 应用密钥
     */
    public String APP_SECRET;

    /**
     * API密钥，在商户平台设置
     */
    public String API_KEY;

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
}
