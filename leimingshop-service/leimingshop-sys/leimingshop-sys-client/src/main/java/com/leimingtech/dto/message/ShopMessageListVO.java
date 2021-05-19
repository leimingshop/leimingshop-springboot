/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.message;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.Serializable;


/**
 * 消息模板配置表
 *
 * @author tyl@leimingtech.com
 * @since v1.0.0 2019-08-22
 */
@Data
@ApiModel(description = "ShopMessageListVO")
public class ShopMessageListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @Null(message = "{id.null}", groups = {AddGroup.class})
    private Long id;
    @ApiModelProperty(value = "消息类型的id")
    @NotBlank(message = "消息类型的id必填", groups = {AddGroup.class})
    private String messageTypeId;
    @ApiModelProperty(value = "是否发送站内信")
    @Null(message = "{是否发送站内信必填}", groups = {AddGroup.class})
    private Integer isSendInner;
    @ApiModelProperty(value = "站内信模板标题")
    @NotBlank(message = "站内信模板标题必填", groups = {AddGroup.class})
    private String tempTitle;
    @ApiModelProperty(value = "站内信模块内容")
    @NotBlank(message = "站内信模块内容必填", groups = {AddGroup.class})
    private String tempInnerContent;
    @ApiModelProperty(value = "是否发送短信")
    @Null(message = "{是否发送短信必填}", groups = {AddGroup.class})
    private Integer isSendSms;
    @ApiModelProperty(value = "短信模板内容")
    @NotBlank(message = "短信模板内容必填", groups = {AddGroup.class})
    private String tempSmsContent;
    @ApiModelProperty(value = "阿里云短信模板code")
    @NotBlank(message = "阿里云短信模板code必填", groups = {AddGroup.class})
    private String tempSmsCode;
    @ApiModelProperty(value = "模板code")
    private String tempCode;
    @ApiModelProperty(value = "模板内容可选值")
    private String selectValue;
    @ApiModelProperty(value = "是否发送友盟")
    private Integer isSendUmeng;
    @ApiModelProperty(value = "友盟模板标题")
    private String umengTitle;
    @ApiModelProperty(value = "友盟模板内容")
    private String tempUmengContent;
    @ApiModelProperty(value = "消息模板名称")
    private String messageTypeName;
    @ApiModelProperty(value = "短信模板是否开启 0 不可开启 , 1 可以开启")
    private Integer innerCanOpen;
    @ApiModelProperty(value = "友盟模板是否开启 0 不可开启 , 1 可以开启")
    private Integer umengCanOpen;
    @ApiModelProperty(value = "短信模板是否开启 0 不可开启 , 1 可以开启")
    private Integer smsCanOpen;
    @ApiModelProperty(value = "是否推送微信消息 0 不可开启 , 1 可以开启")
    private Integer isSendWechat;
    @ApiModelProperty(value = "微信消息模版ID")
    private String wechatTemplateId;
}
