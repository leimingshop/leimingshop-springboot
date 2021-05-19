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
 * @author: lishuo
 * @Date: 9/7/2020 09:32
 * @email: lishuo@leimingtech.com
 * @Description: 定时更新商品的收藏量 评论数量 浏览数量
 */
@Slf4j
@Component
public class UpdateGoodsInfoHandler implements Job {
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Override
    public void run(String params) {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INFO_INDEX_UPDATE_DATE, "time");

        log.info("执行定时更新商品评论收藏浏览索引");

    }
}
