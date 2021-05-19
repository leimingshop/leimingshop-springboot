/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.message.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信列表
 *
 * @Author DY
 * @Date 2019/6/1 16:40
 * @Version 1.0
 **/
@Data
@ApiModel(description = "MessageTextListVO")
public class MessageTextListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 短消息标题
     */
    @ApiModelProperty(value = "短消息标题")
    private String messageTitle;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    /**
     * 信息类型(0:私信;1:系统信息)
     */
    @ApiModelProperty(value = "信息类型(0:私信;1:系统信息)")
    private Integer messageType;

    /**
     * 发送方式(0:站内信;1短信;2微信;3邮件),站内信必填,多条件以逗号分隔
     */
    @ApiModelProperty(value = "发送方式(0:站内信;1短信;2微信;3邮件),站内信必填,多条件以逗号分隔")
    private String sendMode;

    /**
     * 发送人
     */
    @ApiModelProperty(value = "发送人")
    private String creator;

    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    private Date createDate;

    /**
     * 查看状态 0未读 1已读
     */
    @ApiModelProperty(value = "查看状态 0未读 1已读")
    private Integer status;

}
