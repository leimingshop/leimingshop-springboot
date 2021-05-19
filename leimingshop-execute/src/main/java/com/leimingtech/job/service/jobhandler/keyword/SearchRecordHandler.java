/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.keyword;

import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 保存统计搜索词记录
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/13 16:48
 **/
@Slf4j
@Component
public class SearchRecordHandler implements Job {

    @Override
    public void run(String params) {
        log.info("保存统计搜索词记录定时任务开始执行");


        log.info("保存统计搜索词记录执行完成");

    }
}
