/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.goods;

import com.leimingtech.modules.task.Job;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 定时全量同步商品索引
 * @Data: 2020/3/4 13:48
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@Component
public class UpdateGoodsHandler implements Job {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Override
    public void run(String params) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_DATE, "time");

        log.info("执行定时更新商品索引");

    }
}
