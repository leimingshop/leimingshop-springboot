/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 站内信内容表
 *
 * @author chengqian
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-12-16
 */
@Data
@ApiModel(description = "SellerMessageTextDTO")
public class SellerMessageTextDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "站内信Id")
    private Long id;

    @ApiModelProperty("接收人详情ID")
    private Long receiveInfoId;

    @ApiModelProperty(value = "短消息标题")
    private String messageTitle;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "发件人")
    private String sendName;

    @ApiModelProperty(value = "发件时间")
    private Date sendTime;

}