/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 监控中心
 *
 * @since 1.0.0
 */
@EnableAdminServer
@SpringBootApplication
public class MonitorApplication {

    private static final MonitorLogger MONITOR_LOGGER = MonitorLoggerFactory.getMonitorLogger(MonitorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
        MONITOR_LOGGER.info("200002", "MonitorApplication启动成功");
    }

}