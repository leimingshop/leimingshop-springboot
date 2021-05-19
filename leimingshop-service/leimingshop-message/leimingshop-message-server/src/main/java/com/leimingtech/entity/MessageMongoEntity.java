/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity;

import com.leimingtech.message.config.CollectionName;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 消息发送记录保存MongoDB对象
 *
 * @author xuzhch
 * @date 2020年9月16日
 */
@Data
@ToString
@Document(collection = CollectionName.MESSAGE_NAME)
public class MessageMongoEntity {
    private static final long serialVersionUID = 8566384919418814807L;
    private Long id;
    /**
     * 消息编码
     */
    private String messageCode;

    /**
     * 消息发送方式（0：站内信，1：短信，2：微信，3：邮件，4：全部）
     */
    private String messageSendType;
    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 发送者
     */
    private String sendName;

    /**
     * 消息来源  0雷铭系统  目前没有其他系统对接，来源固定为雷铭系统
     */
    private int messageSource;

    /**
     * 全部参数JSON
     */
    private String paramJson;

    /**
     * 消息发送标记（1：已发送，0待发送）
     */
    private Integer sendFlag;
    /**
     * 消息标识 UUID
     */
    private String uniqueMark;
}
