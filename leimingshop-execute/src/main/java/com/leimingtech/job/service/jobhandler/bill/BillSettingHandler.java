/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.bill;

import com.leimingtech.modules.task.Job;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * <定时替换对账周期>
 *
 * @author chengqian
 * @Date 2019/12/30
 * Version 7.0
 **/
@Slf4j
@Component
public class BillSettingHandler implements Job {

    @Autowired
    private SettingService settingService;


    @Override
    public void run(String params) {
        settingService.timeReplace();
        log.info("更新对账周期定时任务执行完成");

    }
}
