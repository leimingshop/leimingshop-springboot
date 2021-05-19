/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author xuzhch
 * @className MessageMemberReceiverDTO
 * @description 站内信接收人信息
 * @date 2020年4月22日15:59:59
 **/
@ApiModel(description = "MessageMemberReceiverDTO")
@Data
public class MessageMemberReceiverDTO {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "昵称")
    private String nickName;
}
