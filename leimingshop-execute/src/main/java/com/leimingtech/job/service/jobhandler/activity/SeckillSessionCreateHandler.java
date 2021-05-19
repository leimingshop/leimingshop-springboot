/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.activity;

import com.leimingtech.modules.service.seckill.SeckillSessionService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * <秒杀场次创建定时任务（一天一次）>
 *
 * @author 刘远杰
 * @Date 2019/12/06
 * Version 7.0
 **/
@Slf4j
@Component
public class SeckillSessionCreateHandler implements Job {

    @Autowired
    private SeckillSessionService seckillSessionService;

    @Override
    public void run(String params) {
        long time = System.currentTimeMillis();
        log.info("秒杀场次创建定时任务开始执行,时间:{}", time);

        seckillSessionService.createSeckillSessionTiming(time);

        log.info("秒杀场次创建定时任务执行完成,时间:{}", time);

    }
}
