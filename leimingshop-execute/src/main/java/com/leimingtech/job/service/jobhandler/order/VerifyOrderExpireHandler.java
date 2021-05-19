/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.order;

import com.leimingtech.modules.service.order.FetchCodeService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 虚拟订单过期
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-04-30 14:48
 **/
@Slf4j
@Component
public class VerifyOrderExpireHandler implements Job {

    @Autowired
    private FetchCodeService fetchCodeService;

    @Override
    public void run(String params) {
        //设置电子提货码过期
        fetchCodeService.codeExpireTiming();

        log.info("执行电子提货码过期定时任务");
    }
}
