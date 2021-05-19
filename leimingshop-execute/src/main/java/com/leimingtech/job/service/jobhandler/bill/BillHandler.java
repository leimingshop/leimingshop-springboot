/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.bill;

import com.leimingtech.modules.service.settle.BillTotalService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * <定时结算>
 *
 * @author chengqian
 * @Date 2019/12/30
 * Version 7.0
 **/
@Slf4j
@Component
public class BillHandler implements Job {

    @Autowired
    private BillTotalService billTotalService;

    @Override
    public void run(String params) {
        billTotalService.timeBill();
        log.info("定时对账定时任务执行完成");

    }
}
