/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.statis;

import com.leimingtech.modules.service.activity.ActivityStatisService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * @ClassName activityStatisHandler
 * @Description 营销统计定时任务
 * @Author chengqian
 * @Date 2020-4-1
 * @Version 1.0
 **/
@Slf4j
@Component
public class ActivityStatisHandler implements Job {
    @Autowired
    private ActivityStatisService activityStatisService;

    @Override
    public void run(String params) {
        try {
            activityStatisService.timeActivityStatis();
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("执行营销统计定时任务异常", e);
        }
        log.info("执行营销统计定时任务");

    }
}
