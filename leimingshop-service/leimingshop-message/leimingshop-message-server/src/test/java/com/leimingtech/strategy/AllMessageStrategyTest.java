//package com.leimingtech.strategy;
//
//import com.leimingtech.dto.MessageDTO;
//import com.leimingtech.enums.MessageCodeEnum;
//import junit.framework.TestCase;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//
//import static org.junit.Assert.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest()
//public class AllMessageStrategyTest extends TestCase {
//    @Autowired
//    private AllMessageStrategy allMessageStrategy;
//
//    @Test
//    public void handle() {
//
//        MessageDTO messageDTO = new MessageDTO();
//        messageDTO.setMessageCode(MessageCodeEnum.REFUNDS_MESSAGE.value());
//        messageDTO.setMessageSource(0);
//        messageDTO.setParamJson("{\"memberMap\":{1250010902049181698:\"13487473971\"},\"mobile\":\"13487473971\",\"paramMap\":{\"orderSn\":\"1254678290197692416\"},\"wehcatClickUrl\":\"http://b2b2c.leimingtech.com/#/pagesD/order/orderDet?paySn=1254678290197692417&id=1254678286720614400\",\"wechatParamsJson\":\"{\\\"keyword5\\\":\\\"2020-05-09 08:00:00\\\",\\\"keyword3\\\":\\\"支付宝\\\",\\\"keyword4\\\":\\\"0.01\\\",\\\"keyword1\\\":\\\"1254678290197692416\\\",\\\"keyword2\\\":\\\"dengxin的店铺\\\",\\\"remark\\\":\\\"感谢您使用我们的产品\\\",\\\"first\\\":\\\"您的订单已成功退款\\\"}\"}");
//        messageDTO.setSendName("dengxin的店铺");
//        messageDTO.setSendTime(new Date());
////        allMessageStrategy.execute(messageDTO);
//        allMessageStrategy.handle(messageDTO);
//
//    }
//}