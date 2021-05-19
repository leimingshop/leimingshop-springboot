/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.mail")
public class SpringMailProperties {

    private String host;

    private String username;

    private String password;

    private Integer port;
}
