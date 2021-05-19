//package com.leimingtech.strategy;
//
//import cn.hutool.core.util.IdUtil;
//import com.alibaba.fastjson.JSON;
//import com.leimingtech.dto.MessageDTO;
//import com.leimingtech.enums.MessageCodeEnum;
//import com.leimingtech.service.SysMessageService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class SysMessageTest {
//    @Autowired
//    private SysMessageService sysMessageService;
//    @Test
//    public void saveTest() {
//        // 创建消息实体
//        MessageDTO messageDTO = new MessageDTO();
//        messageDTO.setMessageCode(MessageCodeEnum.MESSAGE_TYPE_PRVT.value());
//        messageDTO.setSendName("系统");
//        messageDTO.setParamJson("许志成测试消息保存方法");
//        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
//        sysMessageService.save(messageDTO);
//    }
//    @Test
//    public void updateTest() {
//        sysMessageService.sendSuccess("3f321d96ff0a42218c2c137256231f35");
//    }
//}
