/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.grade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MemberGradeSaveDTO
 * @Description 会员等级保存表
 * @Author DY
 * @Date 2019/7/1 18:15
 * @Version 1.0
 **/
@Data
@ApiModel(description = "MemberGradeSaveDTO")
public class MemberGradeSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String gradeName;

    /**
     * 所需积分
     */
    @ApiModelProperty(value = "所需积分")
    private Integer integration;

    /**
     * 等级积分最大值
     */
    @ApiModelProperty(value = "最大值")
    private Integer maxIntegration;

    /**
     * 是否是默认（默认0 非默认，1已默认）
     */
    @ApiModelProperty(value = "是否是默认（默认0 非默认，1已默认）")
    private Integer defaultFlag;

}
