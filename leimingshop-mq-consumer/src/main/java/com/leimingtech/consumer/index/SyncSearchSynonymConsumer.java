/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.index;

import com.leimingtech.modules.index.synonym.service.SearchSynonymService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 同步搜索同义词消费者
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/13 17:01
 **/
@Slf4j
@Component
public class SyncSearchSynonymConsumer {

    @Autowired
    private SearchSynonymService searchSynonymService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SYNC_SEARCH_SYNONYM_QUEUE)
    public void goodsIndexSync(Channel channel, Message message) throws IOException {
        log.info("接收到批量同步商品索引消息");

        searchSynonymService.synchSynonym();

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("批量同步商品索引消息处理完毕，手动执行ACK!");
    }
}
