/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件模板
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_mail_template")
public class SysMailTemplateEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    private String name;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件正文
     */
    private String content;

}