/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.log;

import com.leimingtech.commons.tools.log.BaseLog;
import com.leimingtech.commons.tools.log.enums.LogTypeEnum;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.SysLogErrorDao;
import com.leimingtech.dao.SysLogLoginDao;
import com.leimingtech.dao.SysLogOperationDao;
import com.leimingtech.entity.SysLogErrorEntity;
import com.leimingtech.entity.SysLogLoginEntity;
import com.leimingtech.entity.SysLogOperationEntity;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 从Redis队列中获取Log，保存到DB
 *
 * @since 1.0.0
 */
@Component
public class LogConsumer implements CommandLineRunner {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SysLogErrorDao sysLogErrorDao;
    @Autowired
    private SysLogLoginDao sysLogLoginDao;
    @Autowired
    private SysLogOperationDao sysLogOperationDao;
    private ScheduledExecutorService scheduledService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("log-consumer-schedule-pool-%d").daemon(true).build());

    @Override
    public void run(String... args) {
        //上次任务结束后，等待10秒钟，再执行下次任务
        scheduledService.scheduleWithFixedDelay(() -> receiveQueue(), 1, 10, TimeUnit.SECONDS);
    }

    private void receiveQueue() {
        String key = RedisKeys.getSysLogKey();
        //每次插入100条
        int count = 100;
        for (int i = 0; i < count; i++) {
            BaseLog log = (BaseLog) redisUtils.rightPop(key);
            if (log == null) {
                return;
            }

            //登录日志
            if (log.getType() == LogTypeEnum.LOGIN.value()) {
                SysLogLoginEntity entity = ConvertUtils.sourceToTarget(log, SysLogLoginEntity.class);
                sysLogLoginDao.insert(entity);
            }

            //操作日志
            if (log.getType() == LogTypeEnum.OPERATION.value()) {
                SysLogOperationEntity entity = ConvertUtils.sourceToTarget(log, SysLogOperationEntity.class);
                sysLogOperationDao.insert(entity);
            }

            //异常日志
            if (log.getType() == LogTypeEnum.ERROR.value()) {
                SysLogErrorEntity entity = ConvertUtils.sourceToTarget(log, SysLogErrorEntity.class);
                sysLogErrorDao.insert(entity);
            }
        }
    }

}