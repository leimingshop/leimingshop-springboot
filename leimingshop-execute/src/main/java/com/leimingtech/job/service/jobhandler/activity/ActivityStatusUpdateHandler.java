/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.activity;

import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * <活动状态更新定时任务（一分钟一次）>
 *
 * @author 刘远杰
 * @Date 2019/12/06
 * Version 7.0
 **/
@Slf4j
@Component
public class ActivityStatusUpdateHandler implements Job {

    @Autowired
    private CouponsActivityService couponsActivityService;
    @Autowired
    private ReduceActivityService reduceActivityService;

    @Override
    public void run(String params) {
        long time = System.currentTimeMillis();
        log.info("活动状态更新定时任务开始执行,时间:{}", time);

        // 优惠券活动状态更新
        couponsActivityService.startActivityTiming(time);
        couponsActivityService.stopActivityTiming(time);

        //满减活动状态更新
        reduceActivityService.startActivityTiming(time);
        reduceActivityService.stopActivityTiming(time);

        log.info("活动状态更新定时任务执行完成,时间:{}", time);

    }
}
