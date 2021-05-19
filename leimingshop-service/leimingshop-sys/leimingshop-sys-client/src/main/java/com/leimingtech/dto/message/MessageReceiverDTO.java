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
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Data
@ApiModel(description = "MessageReceiverDTO")
public class MessageReceiverDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "站内信编号")
    private Long messageId;

    @ApiModelProperty(value = "发送者id")
    private Long sendId;

    @ApiModelProperty(value = "发送者名称")
    private String sendName;

    @ApiModelProperty(value = "接受者id")
    private Long receiveId;

    @ApiModelProperty(value = "接受者名称")
    private String receiveName;

    @ApiModelProperty(value = "查看状态 0未读 1已读")
    private Integer status;

    @ApiModelProperty(value = "读取时间")
    private Date updateDate;
    @ApiModelProperty(value = "信中的内容")
    private String messageContent;
}