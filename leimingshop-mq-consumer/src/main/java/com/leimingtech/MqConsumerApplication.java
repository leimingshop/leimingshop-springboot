/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MqConsumerApplication {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(MqConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MqConsumerApplication.class);
        mlogger.info("200101", "leimingtech-mq-consumer启动成功");
    }
}
