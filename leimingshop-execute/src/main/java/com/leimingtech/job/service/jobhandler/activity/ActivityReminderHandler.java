/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.activity;

import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 功能描述：
 * <活动通知定时推送（一分钟一次）>
 *
 * @author 刘远杰
 * @Date 2020/03/24
 * Version 1.0
 **/
@Slf4j
@Component
public class ActivityReminderHandler implements Job {

    @Resource
    private ActivityGoodsService activityGoodsService;

    @Override
    public void run(String params) {
        long time = System.currentTimeMillis();
        log.info("活动通知定时推送定时任务开始执行,时间:{}", time);

        // 发送mq推送
        activityGoodsService.activityReminder(time);

        log.info("活动通知定时推送定时任务执行完成,时间:{}", time);

    }
}
