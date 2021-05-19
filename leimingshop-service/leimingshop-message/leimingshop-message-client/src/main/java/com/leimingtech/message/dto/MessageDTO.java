/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送消息封装实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 9:23
 **/
@Data
@ToString
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 8566384919418814807L;

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
    private Integer messageSource;

    /**
     * 全部参数JSON
     */
    private String paramJson;
    /**
     * 消息唯一标识
     */
    private String uniqueMark;
}
