/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.member;

import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 会员成长值清除定时任务（每天凌晨0点执行一次）
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/25 14:58
 **/
@Component
@Slf4j
public class GrowthClearHandler implements Job {

    @Autowired
    private PointLogService pointLogService;

    @Override
    public void run(String params) {
        log.info("执行会员成长值清除定时任务开始");

        // 清除用户定期成长值
        Boolean result = pointLogService.clearGrowth();

        if (result) {
            log.info("执行会员成长值清除定时任务结束");
            return;
        } else {
            log.info("执行会员成长值清除定时任务失败");
            return;
        }
    }
}
