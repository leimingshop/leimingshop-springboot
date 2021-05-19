/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户名与ID信息
 *
 * @author xuzhch
 * @date 2020年5月18日22:13:28
 */
@Data
@ApiModel(description = "MemberNameDTO")
public class MemberNameDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String memberName;

}
