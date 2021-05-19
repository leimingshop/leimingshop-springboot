/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import com.leimingtech.mq.service.RabbitMqSendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: LX 17839193044@162.com
 * @Description:
 * @Date: 22:15 2019/6/18
 * @Version: V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MqTest {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    public void test() {
        rabbitMqSendService.declareTopicRoutingQueue("exchange.test",
                "routingKey.test.*", "queue.name.test", false);
    }


}
