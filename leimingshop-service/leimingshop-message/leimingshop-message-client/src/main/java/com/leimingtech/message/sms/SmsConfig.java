/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.sms;

import com.leimingtech.commons.tools.validator.group.AliyunGroup;
import com.leimingtech.commons.tools.validator.group.QcloudGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 短信配置信息
 */
@Data
@ApiModel(description = "SmsConfig")
public class SmsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "平台 1：阿里云  2：腾讯云")
    @Range(min = 1, max = 2, message = "{sms.platform.range}")
    private Integer platform;

    @ApiModelProperty(value = "阿里云AccessKeyId")
    @NotBlank(message = "{aliyun.accesskeyid.require}", groups = AliyunGroup.class)
    private String aliyunAccessKeyId;

    @ApiModelProperty(value = "阿里云AccessKeySecret")
    @NotBlank(message = "{aliyun.accesskeysecret.require}", groups = AliyunGroup.class)
    private String aliyunAccessKeySecret;

    @ApiModelProperty(value = "阿里云短信签名")
    @NotBlank(message = "{aliyun.signname.require}", groups = AliyunGroup.class)
    private String aliyunSignName;

    @ApiModelProperty(value = "阿里云短信模板")
    @NotBlank(message = "{aliyun.templatecode.require}", groups = AliyunGroup.class)
    private String aliyunTemplateCode;

    @ApiModelProperty(value = "腾讯云AppId")
    @NotNull(message = "{qcloud.appid.require}", groups = QcloudGroup.class)
    private Integer qcloudAppId;

    @ApiModelProperty(value = "腾讯云AppKey")
    @NotBlank(message = "{qcloud.appkey.require}", groups = QcloudGroup.class)
    private String qcloudAppKey;

    @ApiModelProperty(value = "腾讯云短信签名")
    @NotBlank(message = "{qcloud.signname.require}", groups = QcloudGroup.class)
    private String qcloudSignName;

    @ApiModelProperty(value = "腾讯云短信模板ID")
    @NotBlank(message = "{qcloud.templateid.require}", groups = QcloudGroup.class)
    private String qcloudTemplateId;

}