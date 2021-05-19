/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config.weibo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangtai
 * @date 2020/4/9 17:21
 * @Description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "weibo")
@PropertySource(value = "weibo/weibo-${spring.profiles.active}.properties")
public class WeiboConfig {

    // 申请应用时分配的AppKey
    public String APP_KEY;

    // 申请应用时分配的AppSecret
    public String APP_SECRET;

    // 回调页面
    public String REDIRECT_URI;

}
