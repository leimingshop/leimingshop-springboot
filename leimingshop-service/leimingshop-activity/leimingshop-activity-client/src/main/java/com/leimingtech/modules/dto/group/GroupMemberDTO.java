/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 拼团记录用户管理
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(description = "GroupMemberDTO")
public class GroupMemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long memberId;
    @ApiModelProperty(value = "用户名称")
    private String memberName;
    @ApiModelProperty(value = "用户头像")
    private String memberImage;
    @ApiModelProperty(value = "拼团活动id")
    private Long groupId;
    @ApiModelProperty(value = "拼团记录id")
    private Long groupRecordId;
}
