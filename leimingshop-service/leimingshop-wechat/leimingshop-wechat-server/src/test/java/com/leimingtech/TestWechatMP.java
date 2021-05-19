/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import com.leimingtech.modules.service.WechatMpService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试微信公众号发送信息
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/1/19 16:09
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWechatMP {
    @Autowired
    private WechatMpService wechatMPService;


    @Test
    public void sendMessageTest() {
        String paramsJson = "{\"productType\":\"华为P30 128G\",\"name\":\"ThisLX\",\"number\":10,\"expDate\":\"15天\"}";
        wechatMPService.sendTemplateMsg(paramsJson,
                "M33lq4ybOMg5kzHdBpbVz3EP2G4O66M8GyRnsGJs63M", "http://b2b2c.leimingtech.com/h5",
                "oYU6MuA4Esv-Fg3O9dWH_y4_uthY");
    }

}
