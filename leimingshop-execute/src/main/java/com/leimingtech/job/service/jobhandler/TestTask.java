/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler;

import com.leimingtech.modules.task.Job;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 * <p>
 * testTask为spring bean的名称
 */
@Component("testTask")
public class TestTask implements Job {

    @Override
    public void run(String params) {
        System.out.println("1111111111111111111111111");
        System.out.println("1111111111111111111111111");
        System.out.println("1111111111111111111111111");
        System.out.println("1111111111111111111111111");

    }

//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        System.out.println("1111111111111111111111111");
//        System.out.println("1111111111111111111111111");
//        System.out.println("1111111111111111111111111");
//        System.out.println("1111111111111111111111111");
//
//    }
}
