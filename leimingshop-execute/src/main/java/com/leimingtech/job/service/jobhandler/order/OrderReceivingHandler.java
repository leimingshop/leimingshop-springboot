/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.order;

import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderReceivingHandler
 * @Description 自动确认收货
 * @Author DY
 * @Date 2019/8/15 17:49
 * @Version 1.0
 **/
@Slf4j
@Component
public class OrderReceivingHandler implements Job {

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String params) {
        orderService.orderReceivingTiming();

        log.info("执行确认收货定时任务");

    }
}
