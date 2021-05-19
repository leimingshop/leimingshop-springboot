/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.entity.MessageMongoEntity;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.message.config.CollectionName;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.enums.MessageSendTypeEnum;
import com.leimingtech.message.enums.MessageSourceEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.service.NosqlService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

/**
 * 消息发送记录
 *
 * @author xuzhch
 * @date 2020年5月13日13:56:06
 */
@Service

@Slf4j
public class SysMessageServiceImpl implements SysMessageService {


    @Autowired
    private NosqlService nosqlService;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 发送消息保存到MongoDB
     *
     * @param messageDTO 消息实体
     * @author xuzhch
     * @date 2020年5月13日09:47:51
     */

    @Override
    public void save(@RequestBody MessageDTO messageDTO) {
        if (BeanUtil.isEmpty(messageDTO)) {
            return;
        }
        messageDTO.setMessageSendType(setSendType(messageDTO.getMessageCode()));
        MessageMongoEntity mongoEntity = new MessageMongoEntity();
        BeanCopier.create(MessageDTO.class, MessageMongoEntity.class, false)
                .copy(messageDTO, mongoEntity, null);
        mongoEntity.setId(IdGenerator.defaultSnowflakeId());
        mongoEntity.setMessageSource(MessageSourceEnum.LEIMING_SOURCE.value());
        mongoEntity.setSendTime(new Date());
        mongoEntity.setSendFlag(MessageEnum.MESSAGE_UN_SEND.value());
        nosqlService.saveObj(mongoEntity, CollectionName.MESSAGE_NAME);
    }

    /**
     * 设置消息发送类型（0：站内信，1：短信，2：微信，3：邮件，4：全部）
     *
     * @param code 消息编码
     * @return 消息发送类型
     * @author xuzhch
     * @date 2020年5月13日10:12:13
     */
    private String setSendType(String code) {
        if (MessageCodeEnum.MESSAGE_TYPE_PRVT.value().equals(code) || MessageCodeEnum.MESSAGE_TYPE_SYSINFO.value().equals(code)) {
            return MessageSendTypeEnum.INNER_MESSAGE.value();
        }
        if (MessageCodeEnum.TEMPLATECODE_LOGIN_REGISTER.value().equals(code) || MessageCodeEnum.TEMPLATECODE_RESET_PWD_CODE.value().equals(code)) {
            return MessageSendTypeEnum.SMS_MESSAGE.value();
        }
        return MessageSendTypeEnum.ALL_MESSAGE.value();
    }


    @Override
    public void sendSuccess(String uniqueMark) {
        UpdateResult updateResult =
                mongoTemplate.updateMulti(Query.query(Criteria.where("uniqueMark").is(uniqueMark)), Update.update(
                        "sendFlag", MessageEnum.MESSAGE_SEND_SUCCESS.value()), CollectionName.MESSAGE_NAME);
        long defaultCounter = 0L;
        if (updateResult.getModifiedCount() <= defaultCounter) {
            log.error("消息发送失败，{}", uniqueMark);
        }
    }


    @Override
    public String getSendResult(String uniqueMark) {
        MessageMongoEntity mongoEntity =
                mongoTemplate.findOne(Query.query(Criteria.where("uniqueMark").is(uniqueMark)),
                        MessageMongoEntity.class);
        if (!BeanUtil.isEmpty(mongoEntity) && null != mongoEntity.getSendFlag() && mongoEntity.getSendFlag().equals(MessageEnum.MESSAGE_UN_SEND.value())) {
            return "0";
        } else {
            return "1";
        }
    }
}
