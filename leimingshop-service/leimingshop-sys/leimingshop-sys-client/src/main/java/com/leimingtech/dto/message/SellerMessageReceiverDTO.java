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
 * 短消息接收人表
 *
 * @author chengqian
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-12-16
 */
@Data
@ApiModel(description = "SellerMessageReceiverDTO")
public class SellerMessageReceiverDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "站内信编号")
    private Long messageId;

    @ApiModelProperty(value = "发送者id")
    private Long sendId;

    @ApiModelProperty(value = "发送者名称")
    private String sendName;

    @ApiModelProperty(value = "发送时间")
    private Date sendTime;

    @ApiModelProperty(value = "站内信类别 0未读 1 已读")
    private Integer messageType;

    @ApiModelProperty("站内信标题")
    private String messageTitle;

    @ApiModelProperty(value = "查看状态 0未读 1已读")
    private Integer status;

    @ApiModelProperty(value = "读取时间")
    private Date updateDate;

}