/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.activity;

import com.leimingtech.modules.service.flashsale.FlashSaleActivityService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 限时抢购活动状态更新定时任务
 *
 * @author xuzhch
 * @date 2020年10月20日
 **/
@Slf4j
@Component
public class FlashSaleActivityStatusUpdateHandler implements Job {

    @Autowired
    private FlashSaleActivityService flashSaleActivityService;

    @Override
    public void run(String params) {

        long time = System.currentTimeMillis();
        log.info("限时抢购活动状态更新定时任务开始执行,时间:{}", time);

        // 限时抢购活动状态更新
        flashSaleActivityService.startFlashSalelActivityTiming(time);
        flashSaleActivityService.stopFlashSaleActivityTiming(time);

        log.info("限时抢购活动状态更新定时任务执行完成,时间:{}", time);

    }
}
