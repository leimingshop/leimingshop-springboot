/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件发送记录
 */
@Data
@ApiModel(description = "SysMailLogDTO")
public class SysMailLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "邮件模板ID")
    private Long templateId;

    @ApiModelProperty(value = "发送者")
    private String mailFrom;

    @ApiModelProperty(value = "收件人")
    private String mailTo;

    @ApiModelProperty(value = "抄送者")
    private String mailCc;

    @ApiModelProperty(value = "邮件主题")
    private String subject;

    @ApiModelProperty(value = "邮件正文")
    private String content;

    @ApiModelProperty(value = "发送状态  0：失败  1：成功")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
