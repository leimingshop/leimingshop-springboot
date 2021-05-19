/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.order;

import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderCancelHandler
 * @Description 订单定时取消
 * @Author DY
 * @Date 2019/6/18 17:39
 * @Version 1.0
 **/
@Slf4j
@Component
public class OrderCancelHandler implements Job {

    @Autowired
    private OrderPayService orderPayService;

    @Override
    public void run(String params) {
        //取消订单
        orderPayService.canceledOrderTiming();

        log.info("执行取消订单定时任务");

    }
}
