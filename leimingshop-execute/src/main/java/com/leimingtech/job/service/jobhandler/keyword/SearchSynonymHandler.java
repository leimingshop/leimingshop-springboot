/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.keyword;

import com.leimingtech.modules.task.Job;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步搜索同义词定时任务
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/13 16:48
 **/
@Slf4j
@Component
public class SearchSynonymHandler implements Job {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Override
    public void run(String params) {

        log.info("同步搜索同义词定时任务开始执行");

        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SYNC_SEARCH_SYNONYM_QUEUE, "");

        log.info("同步搜索同义词定消息发送成功");

    }
}
