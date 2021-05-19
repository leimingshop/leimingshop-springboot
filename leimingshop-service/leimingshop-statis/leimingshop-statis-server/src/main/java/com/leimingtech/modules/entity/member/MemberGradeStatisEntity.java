/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.member;

import com.leimingtech.modules.constants.CollectionName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员等级统计保存对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@Document(collection = CollectionName.MEMBER_GRADER_STATISTICS)
public class MemberGradeStatisEntity implements Serializable {

    @ApiModelProperty(value = "主键ID")
    @Id
    private Long id;

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

}
