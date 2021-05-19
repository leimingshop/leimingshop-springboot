/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 会员友盟信息对象
 *
 * @author xuzhch
 * @date 2020年4月24日09:47:48
 */
@Data
@ApiModel(description = "MemberUmengTokenInfo")
public class MemberUmengTokenInfo implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String memberName;
    @ApiModelProperty(value = "友盟token")
    private String deviceToken;
    @ApiModelProperty(value = "友盟来源（1：安卓，2：ios）")
    private Integer umengSource;

}
