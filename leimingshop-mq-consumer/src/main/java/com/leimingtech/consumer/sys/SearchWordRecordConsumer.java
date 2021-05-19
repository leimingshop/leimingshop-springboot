/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.sys;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.entity.search.SearchRecordMongoEntity;
import com.leimingtech.modules.constants.mongodb.CollectionName;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 保存H5搜索记录消费者
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/16 13:54
 **/
@Slf4j
@Component
public class SearchWordRecordConsumer {

    @Autowired
    private NosqlService nosqlService;


    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SAVE_SEACH_HISTORY_QUEUE)
    public void saveSearchWordRecord(Channel channel, Message message) throws Exception {
        log.info("接受保存H5搜索记录消息");

        String msgText = new String(message.getBody());
        if (StringUtils.isNotBlank(msgText)) {
            msgText = msgText.replaceAll("%26", "&");
        }
        log.info("接收到日志消息，内容为：{}", msgText);


        if (StringUtils.isBlank(msgText)) {
            return;
        }

        Map<String, String> map = JSON.parseObject(msgText, Map.class);
        String searchIp = map.get("searchIp");
        String keyword = map.get("keyword");
        String memberId = map.get("memberId");
        if (StringUtils.isNotBlank(searchIp) && StringUtils.isNotBlank(keyword)) {
            SearchRecordMongoEntity searchRecordMongoEntity = new SearchRecordMongoEntity();
            searchRecordMongoEntity.setId(IdWorker.getId());
            searchRecordMongoEntity.setMemberId(null != memberId ? Long.valueOf(memberId) : null);
            searchRecordMongoEntity.setKeyword(keyword);
            searchRecordMongoEntity.setSearchIp(searchIp);
            long l = System.currentTimeMillis();
            searchRecordMongoEntity.setCreateTime(DateUtils.longToDate(l, DateUtils.DATE_TIME_PATTERN));
            searchRecordMongoEntity.setCreateDayTime(DateUtils.longToDate(l, DateUtils.DATE_PATTERN));
            searchRecordMongoEntity.setCreateMonthTime(DateUtils.longToDate(l, DateUtils.DATE_MONTH_PATTERN));
            nosqlService.saveObj(searchRecordMongoEntity, CollectionName.SEARCH_RECORD);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("保存H5搜索记录消息处理完毕，手动执行ACK!");
    }
}
