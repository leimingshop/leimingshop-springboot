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
 * @date 2020/4/9 15:18
 * @Description:
 */
@Data
@Component
@ConfigurationProperties("wxpayapp")
@PropertySource(value = "wxpay/weixin-app-${spring.profiles.active}.properties")
public class WechatAppConfig {

    /**
     * 微信分配的公众账号ID（企业号corpid即为此appId）
     */
    public String APP_ID;

    /**
     * 应用密钥
     */
    public String APP_SECRET;

}
