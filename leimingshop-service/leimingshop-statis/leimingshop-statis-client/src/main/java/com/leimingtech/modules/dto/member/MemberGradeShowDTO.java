/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员等级统计查询结果展示对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(description = "MemberGradeShowDTO")
public class MemberGradeShowDTO implements Serializable {
    @ApiModelProperty(value = "会员等级ID")
    private Long gradeId;

    @ApiModelProperty(value = "会员等级名称")
    private String memberGraderName;

    @ApiModelProperty(value = "会员数量")
    private Integer memberNumber;

    @ApiModelProperty(value = "创建时间（小时）")
    private Date createHourTime;

    @ApiModelProperty(value = "创建时间（天）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（月）")
    private Date createMonthTime;

    @ApiModelProperty(value = "创建时间（详细）")
    private Date createDate;

    @ApiModelProperty(value = "会员等级占比")
    private String gradeConversion;


}
