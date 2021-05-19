/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SpringBootAdminNotifyEmailProperties.class)
public class SpringBootAdminNotifyEmailPropertiesConfig {
}
