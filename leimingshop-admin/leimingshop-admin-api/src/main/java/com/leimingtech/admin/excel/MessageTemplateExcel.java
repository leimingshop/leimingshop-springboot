/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 消息模板配置表
 *
 * @author wangxiaogang wangxiaogang@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Data
public class MessageTemplateExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "模板编号")
    private String tempNum;
    @ExcelProperty(value = "模板名称")
    private String tempName;
    @ExcelProperty(value = "站内信模块内容")
    private String tempInnerContent;
    @ExcelProperty(value = "是否发送短信")
    private Integer isSendSms;
    @ExcelProperty(value = "短信模板内容")
    private String tempSmsContent;
    @ExcelProperty(value = "阿里云短信模板code")
    private String tempSmsCode;
    @ExcelProperty(value = "是否发送邮件")
    private Integer isSendEmail;
    @ExcelProperty(value = "邮件模板标题")
    private String tempEmailTitle;
    @ExcelProperty(value = "邮件模板内容")
    private String tempEmailContent;
    @ExcelProperty(value = "消息模板标识")
    private String tempCode;
    @ExcelProperty(value = "模板内容可选值")
    private String selectValue;
    @ExcelProperty(value = "是否发送极光")
    private Integer isSendAurora;
    @ExcelProperty(value = "激光模板内容")
    private String tempAuroraContent;
    @ExcelProperty(value = "是否发送微信")
    private Integer isSendWechat;
    @ExcelProperty(value = "微信模板编号")
    private String wechatTempNum;
    @ExcelProperty(value = "微信模板id")
    private String wechatTempId;
    @ExcelProperty(value = "微信模板内容")
    private String wechatTempContent;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
