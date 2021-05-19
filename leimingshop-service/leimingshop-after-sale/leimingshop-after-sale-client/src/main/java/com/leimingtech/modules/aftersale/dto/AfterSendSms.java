/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 售后日志
 *
 * @author chenqgian
 */
@Data
@ApiModel(description = "AfterSendSms")
public class AfterSendSms implements Serializable {


    @ApiModelProperty(value = "审核结果 1 审核通过 2 审核未通过")
    private Integer status;

    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;

    @ApiModelProperty(value = "短信模板")
    private String code;

    @ApiModelProperty(value = "是否发送站内信 0 是 1 否")
    private Integer isSendInner;
    @ApiModelProperty(value = "站内信标题")
    private String tempTitle;
    @ApiModelProperty(value = "站内信内容")
    private String tempInnerContent;
    @ApiModelProperty(value = "发送人")
    private String sendPeople;


}