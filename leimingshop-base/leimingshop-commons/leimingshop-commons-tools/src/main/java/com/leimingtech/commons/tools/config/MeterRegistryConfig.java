/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Micrometer监控配置文件
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/24 14:01
 **/
@Configuration
public class MeterRegistryConfig {


    /**
     * 配置自定义MeterRegistry
     **/
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }
}
