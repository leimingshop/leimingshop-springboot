/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.message;

import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.store.StoreDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 站内信内容表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Data
@ApiModel(description = "FindMessageTextDTO")
public class FindMessageTextDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "短消息标题")
    private String messageTitle;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "信息类型(0:私信;1:系统信息)")
    private Integer messageType;

    @ApiModelProperty(value = "收件人类型;全部  0:会员  1:商户 2")
    private Integer receiverType;

    @ApiModelProperty(value = "发送方式(0:站内信;1短信;2微信;3邮件),站内信必填,多条件以逗号分隔")
    private String sendMode;

    @ApiModelProperty(value = "接收人信息")
    private List<MemberDTO> memberId;

    @ApiModelProperty(value = "店铺信息")
    private List<StoreDTO> storeList;

    @ApiModelProperty(value = "收件人")
    private String creator;

    @ApiModelProperty(value = "发件时间")
    private Date sendTime;

}
