/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * PC端接口层启动类
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/8 17:44
 **/
@SpringBootApplication
@ServletComponentScan
public class WebApiApplication {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(WebApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class);
        mlogger.info("200101", "portal-api启动成功");
    }
}
