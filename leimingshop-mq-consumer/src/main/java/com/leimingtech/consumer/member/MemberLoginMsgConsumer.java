/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.member;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.modules.dto.log.point.PointLogDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 用户登陆后维护用户登陆信息
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/11/11 10:12
 **/
@Slf4j
@Component
public class MemberLoginMsgConsumer {
    private static final String PARAMS_LAST_LOGIN_DATE = "lastLoginDate";
    private static final String PARAMS_LAST_LOGIN_IP = "lastLoginIp";

    @Resource

    private MemberService memberService;

    @Resource
    private PointLogService pointLogService;


    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_USER_LOGIN_MESSAGE_QUEUE)
    public void updateMemberLoginMsg(Channel channel, Message message) {

        String msgText = new String(message.getBody());
        log.info("用户登陆后维护用户登陆信息，内容为：{}", msgText);

        MemberDTO memberDTO = new MemberDTO();
        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(msgText);
        Long memberId = (Long) map.get("memberId");
        if (map.get(PARAMS_LAST_LOGIN_DATE) != null) {
            Long lastLoginDate = (Long) map.get(PARAMS_LAST_LOGIN_DATE);
            memberDTO.setLastLoginDate(new Date(lastLoginDate));
        }
        if (map.get(PARAMS_LAST_LOGIN_IP) != null) {
            String lastLoginIp = (String) map.get(PARAMS_LAST_LOGIN_IP);
            memberDTO.setLastLoginIp(lastLoginIp);
        }
        String memberLoginIp = (String) map.get("memberLoginIp");
        Long memberLoginTime = (Long) map.get("memberLoginTime");

        memberDTO.setId(memberId);
        memberDTO.setMemberLoginTime(new Date(memberLoginTime));
        memberDTO.setMemberLoginIp(memberLoginIp);

        String deviceToken = (String) map.get("deviceToken");
        Integer umengSource = (Integer) map.get("umengSource");
        if (StringUtils.isNotBlank(deviceToken) && umengSource != null) {
            memberService.logoutDeviceToken(deviceToken);
            memberDTO.setDeviceToken(deviceToken);
            memberDTO.setUmengSource(umengSource);
        }

        memberService.updateBase(memberDTO);

        // 每日登录增加积分之前先查询当天的积分是否增加
        Map<String, Object> memberParams = new HashMap<>(5);
        memberParams.put("memberId", memberDTO.getId());
        memberParams.put("sourceType", MemberPointLogEnum.LOGIN_SOURCE_TYPE.code());

        // 获取当前日期的0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        String from = DateUtils.format(zero, DateUtils.DATE_TIME_PATTERN);
        memberParams.put("endTime", DateUtils.format(DateUtils.getToday(), DateUtils.DATE_TIME_PATTERN));
        memberParams.put("startTime", from);
        List<PointLogDTO> pointLogDTOList = pointLogService.queryByTime(memberParams);
        if (CollectionUtils.isEmpty(pointLogDTOList)) {
            Map<String, Object> timeParams = new HashMap<>(5);
            timeParams.put("memberId", memberDTO.getId());
            timeParams.put("memberName", map.get("memberName"));
            timeParams.put("sourceType", MemberPointLogEnum.LOGIN_SOURCE_TYPE.code());
            timeParams.put("pointDesc", MemberPointLogEnum.LOGIN_SOURCE_TYPE.value());
            timeParams.put("repeat", 1);
            memberService.savePoint(timeParams);
        }

        // ACK手动确认处理消息
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("用户登陆后维护用户登陆信息处理完毕，手动执行ACK失败!");
            e.printStackTrace();
        }

        log.info("用户登陆后维护用户登陆消息处理完毕，手动执行ACK完成!");
    }
}
