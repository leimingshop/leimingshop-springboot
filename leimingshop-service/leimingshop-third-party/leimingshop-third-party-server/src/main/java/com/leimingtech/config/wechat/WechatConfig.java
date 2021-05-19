/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName WechatConfig
 * @Description 微信配置config
 * @Author DY
 * @Date 2019/5/24 10:13
 * @Version 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {

    /**
     * 公众号id
     */
    private String appid;
    /**
     * 公众号secret
     */
    private String secret;
}
