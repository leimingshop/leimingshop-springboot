/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.grade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 会员等级表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Data
@ApiModel(description = "MemberGradeDTO")
public class MemberGradeDTO implements Serializable {
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
     * 会员人数
     */
    @ApiModelProperty(value = "会员人数")
    private Long memeberNum;
    /**
     * 所需积分
     */
    @ApiModelProperty(value = "所需积分")
    private Integer integration;

    @ApiModelProperty(value = "所需积分最大值")
    private Integer maxIntegration;

    /**
     * 等级所对应的图片
     */
    @ApiModelProperty(value = "等级所对应的图片")
    private String gradeImg;
    /**
     * 会员帐号有效期
     */
    @ApiModelProperty(value = "会员帐号有效期")
    private String deadline;
    /**
     * 优惠百分比
     */
    @ApiModelProperty(value = "优惠百分比")
    private Integer preferential;
    /**
     * 是否是默认（默认0 非默认，1已默认）
     */
    @ApiModelProperty(value = "是否是默认（默认0 非默认，1已默认）")
    private Integer defaultFlag;

}