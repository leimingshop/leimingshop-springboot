/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.message.vo;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.member.MessageMemberReceiverDTO;
import com.leimingtech.modules.dto.store.MessageStoreReceiverDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 站内信内容表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Data
@ApiModel(description = "MessageTextVO")
public class MessageTextVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @NotBlank(message = "标题为必填", groups = AddGroup.class)
    @NotBlank(message = "标题为必填", groups = UpdateGroup.class)
    @ApiModelProperty(value = "短消息标题")
    private String messageTitle;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "信息类型(0:私信;1:系统信息)")
    @NotNull(message = "信息类型必填", groups = AddGroup.class)
    @NotNull(message = "信息类型必填", groups = UpdateGroup.class)
    private Integer messageType;

    @ApiModelProperty(value = "收件人类型;全部  0:会员  1:商户 2")
    private Integer receiverType;

    @ApiModelProperty(value = "发送方式(0:站内信;1短信;2微信;3邮件),站内信必填,多条件以逗号分隔")
    private Integer[] sendMode;

    @ApiModelProperty(value = "接收人信息")
    private List<MessageMemberReceiverDTO> memberList;

    @ApiModelProperty(value = "店铺信息")
    private List<MessageStoreReceiverDTO> storeList;

    @ApiModelProperty(value = "收件人")
    private String creator;

    @ApiModelProperty("发送人id")
    private Long userId;

    @ApiModelProperty("发送人名称")
    private String userName;

    @ApiModelProperty(value = "替换参数")
    private Map<String, String> paramsMap;

    @ApiModelProperty(value = "发件时间")
    private Date createDate;

}
