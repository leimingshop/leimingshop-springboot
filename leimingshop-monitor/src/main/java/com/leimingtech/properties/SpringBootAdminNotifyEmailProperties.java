/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.boot.admin.notify.mail")
public class SpringBootAdminNotifyEmailProperties {

    private Boolean enabled;

    private String from;

    private String to;

    private String cc;
}
