/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * 定时任务配置
 */
@Configuration
@PropertySource("classpath:quartz.properties")
public class ScheduleConfig {
    @Value("${org.quartz.scheduler.instanceName}")
    private String schedulerName;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);

        //PostgreSQL数据库，需要打开此注释
        //prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        Properties properties = PropertiesLoaderUtils.loadAllProperties("quartz.properties");
        factory.setQuartzProperties(properties);

        factory.setSchedulerName(schedulerName);
        //延时启动
        factory.setStartupDelay(1);
        factory.setApplicationContextSchedulerContextKey("/");
        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //设置自动启动，默认为true
        factory.setAutoStartup(true);

        return factory;
    }
}
