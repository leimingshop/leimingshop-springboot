/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.message;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 消息模板配置表
 *
 * @author chengqian
 * @email
 * @since 1.0.0 2019-11-19
 */
@Data
@ApiModel(description = "FindMessageTemplateDTO")
public class FindMessageTemplateDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "是否发送短信 0 是 1 否")
    private Integer isSendSms;
    @ApiModelProperty(value = "是否发送站内信 0 是 1 否")
    private Integer isSendInner;
    @ApiModelProperty(value = "是否发送微信公众号推送 0 是 1 否")
    private Integer isSendWechat;
    @ApiModelProperty(value = "是否发送友盟推送 0 是 1 否")
    private Integer isSendUmeng;
    @ApiModelProperty(value = "短信模板code")
    private String tempSmsCode;
    @ApiModelProperty(value = "站内信标题")
    private String tempTitle;
    @ApiModelProperty(value = "站内信内容")
    private String tempInnerContent;
    @ApiModelProperty(value = "微信模板ID")
    private String wechatTemplateId;
    @ApiModelProperty(value = "友盟推送标题")
    private String umengTitle;
    @ApiModelProperty(value = "友盟推送内容")
    private String tempUmengContent;

}
