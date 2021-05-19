/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件发送记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_mail_log")
public class SysMailLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件模板ID
     */
    private Long templateId;
    /**
     * 发送者
     */
    private String mailFrom;
    /**
     * 收件人
     */
    private String mailTo;
    /**
     * 抄送者
     */
    private String mailCc;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件正文
     */
    private String content;
    /**
     * 发送状态  0：失败  1：成功
     */
    private Integer status;

}