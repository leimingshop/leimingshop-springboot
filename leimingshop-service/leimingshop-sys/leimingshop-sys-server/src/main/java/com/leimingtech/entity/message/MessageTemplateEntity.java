/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.message;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * 消息模板配置表
 *
 * @author tyl@leimingtech.com
 * @since v1.0.0 2019-08-22
 */
@Data
@ToString
@TableName("sys_message_template")
public class MessageTemplateEntity extends BaseEntity implements Serializable {

    /**
     * 消息类型的id
     */
    private String messageTypeId;

    /**
     * 是否发送站内信
     */
    private Integer isSendInner;

    /**
     * 站内信模板标题
     */
    private String tempTitle;

    /**
     * 站内信模块内容
     */
    private String tempInnerContent;

    /**
     * 是否发送短信
     */
    private Integer isSendSms;

    /**
     * 短信模板内容
     */
    private String tempSmsContent;

    /**
     * 阿里云短信模板code
     */
    private String tempSmsCode;

    /**
     * 模板code
     */
    private String tempCode;

    /**
     * 模板内容可选值
     */
    private String selectValue;

    /**
     * 是否发送友盟
     */
    private Integer isSendUmeng;

    /**
     * 友盟模板标题
     */
    private String umengTitle;

    /**
     * 友盟模板内容
     */
    private String tempUmengContent;

    /**
     * 是否推送微信消息
     */
    private Integer isSendWechat;

    /**
     * 微信消息模版ID
     */
    private String wechatTemplateId;
}
