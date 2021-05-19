/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config.netease;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "netease")
public class NeteaseConfig {

    public String appKey;

    public String appSecret;
}
