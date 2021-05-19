/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.statis;

import com.leimingtech.modules.service.member.MemberStatisService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * @ClassName MemberGradeStatisHandler
 * @Description 会员等级统计数据保存
 * @Author xuzhch
 * @Date 2019年12月19日
 * @Version 1.0
 **/
@Slf4j
@Component
public class MemberGradeStatisHandler implements Job {

    @Autowired
    private MemberStatisService memberStatisService;

    @Override
    public void run(String params) {
        try {
            memberStatisService.saveMemberGradeStatis(params);
        } catch (ParseException e) {
            log.error("执行商品统计定时任务异常", e);
        }
        log.info("执行商品统计定时任务");

    }
}
