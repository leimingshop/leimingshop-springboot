/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.activity;

import com.leimingtech.modules.service.coupons.MemberCouponsService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * <会员优惠券状态更新定时任务（一天一次）>
 *
 * @author 刘远杰
 * @Date 2019/12/13
 * Version 7.0
 **/
@Slf4j
@Component
public class CouponsStatusUpdateHandler implements Job {

    @Autowired
    private MemberCouponsService memberCouponsService;

    @Override
    public void run(String params) {
        long time = System.currentTimeMillis();
        log.info("会员优惠券状态更新定时任务开始执行,时间:{}", time);

        // 会员优惠券状态更新
        memberCouponsService.canCouponsTiming(time);
        memberCouponsService.canntCouponsTiming(time);

        log.info("会员优惠券状态更新定时任务执行完成,时间:{}", time);

    }
}
