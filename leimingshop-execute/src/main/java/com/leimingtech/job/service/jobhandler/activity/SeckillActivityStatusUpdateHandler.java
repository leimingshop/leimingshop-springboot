/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.activity;

import com.leimingtech.modules.service.seckill.SeckillActivityService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * <秒杀活动状态更新定时任务（三十分钟一次）>
 *
 * @author 刘远杰
 * @Date 2020/03/11
 * Version 1.0
 **/
@Slf4j
@Component
public class SeckillActivityStatusUpdateHandler implements Job {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Override
    public void run(String params) {

        long time = System.currentTimeMillis();
        log.info("秒杀活动状态更新定时任务开始执行,时间:{}", time);

        // 优惠券活动状态更新
        seckillActivityService.startSeckillActivityTiming(time);
        seckillActivityService.stopSeckillActivityTiming(time);

        log.info("秒杀活动状态更新定时任务执行完成,时间:{}", time);

    }
}
