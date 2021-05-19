/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.message;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信内容表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_message_text")
public class MessageTextEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短消息标题
     */
    private String messageTitle;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 信息类型(0:私信;1:系统信息)
     */
    private Integer messageType;

    /**
     * 收件人类型;   0:全部  1:会员  2:商户
     */
    private Integer receiverType;

    /**
     * 发送信息时间
     */
    private Date sendTime;

    /**
     * 发送方式(0:站内信;1短信;2微信)
     */
    private String sendMode;

}