/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.activity;

import com.leimingtech.modules.service.group.GroupRecordService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拼团活动定时任务（每分钟执行一次）
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-03-12 11:29
 **/
@Slf4j
@Component
public class GroupActivityStatusUpdateHandler implements Job {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRecordService groupRecordService;

    @Override
    public void run(String params) {
        long time = System.currentTimeMillis();
        log.info("拼团活动状态更新定时任务开始执行,时间:{}", time);

        // 拼团活动状态更新
        groupService.startGroupActivityTiming(time);

        groupService.stopGroupActivityTiming(time);

        // 拼团预热活动商品es索引更新
        groupService.preheatGroupActivityTiming(time);

        // 拼团成团超时时间已到，更新拼团的状态
        groupRecordService.overTimeGroupActivityTiming(time);

        log.info("拼团活动状态更新定时任务执行完成,时间:{}", time);

    }
}
