/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.EmailEnum;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMailTemplateService;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 验证码
 *
 * @author DY 2019/5/12
 * @since 1.0.0
 */
@Slf4j
@Service

public class CaptchaCodeCodeServiceImpl implements CaptchaCodeService {
    /**
     * 注册验证码,五分钟
     */
    public static final long ONE_MINUTE = 300L;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SysMailTemplateService sysMailTemplateService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private SysMessageService sysMessageService;

    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @param prefix 短信验证码redis前缀
     *               DY 2019/5/13
     */
    @Override

    public void moblieCaptcha(@RequestParam("mobile") String mobile, @RequestParam("prefix") String prefix) {

        Object o = redisUtils.get(MemberRedisConstants.AVOID_REPEAT_SEND_PREFIX + mobile);
        if (!BeanUtil.isEmpty(o)) {
            throw new ServiceException(MemberStatusCode.MESSAGE_SEND_TIME_FAILED);
        }

        //生成短信验证码
        String valid = String.valueOf(new Random().nextInt(899999) + 100000);
        //保存redis
        redisUtils.set(prefix + mobile, valid, ONE_MINUTE);

        // 创建短信所需参数Map
        Map<String, String> paramsMap = new HashMap<>(5);
        paramsMap.put("code", valid);

        Map<String, Object> map = new HashMap<>(16);
        map.put("mobile", mobile);
        map.put("paramJson", JSON.toJSONString(paramsMap));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(MessageCodeEnum.TEMPLATECODE_LOGIN_REGISTER.value());
        messageDTO.setSendName("系统");
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);

        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
        //避免频繁发送短信
        redisUtils.set(MemberRedisConstants.AVOID_REPEAT_SEND_PREFIX + mobile, "验证码已发送:" + valid, MemberRedisConstants.AVOID_REPEAT_TIME);
    }

    /**
     * 获取邮箱验证码
     *
     * @param email    邮箱
     * @param sendType 1 忘记密码 2 登录
     *                 DY 2019/5/13
     */
    @Override

    public void emailCaptha(@RequestParam("email") String email, @RequestParam("sendType") Integer sendType) {
        //生成短信验证码
        Integer valid = new Random().nextInt(899999) + 100000;

        //保存redis
        redisUtils.set(email, valid, ONE_MINUTE);
        //邮件参数  验证码需要增加''  否则格式化按照数字处理会增加,
        String params = "{'code':'" + valid + "'}";
        String templateId = null;
        if (sendType == MemberEnum.SEND_TYPE_FORGET_PWD.value()) {
            templateId = EmailEnum.FORGET_PWD_EMAIL_TEMPLATE.value();
        } else if (sendType == MemberEnum.SEND_TYPE_LOGIN.value()) {

            templateId = EmailEnum.FORGET_PWD_EMAIL_TEMPLATE.value();
        }

        //发送邮件
        try {
            sysMailTemplateService.sendMail(Long.valueOf(templateId), email, "", params);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }

    }
}
