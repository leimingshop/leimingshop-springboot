/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.reduce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 满减活动实体(编辑)
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "ReduceActivityEditDTO")
public class ReduceActivityEditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "满减ID")
    @NotNull(message = "满减ID不能为空", groups = UpdateGroup.class)
    private Long id;
    @ApiModelProperty(value = "满减活动名称")
    @Length(max = 20, message = "满减活动名称不可超过20个字", groups = UpdateGroup.class)
    @NotBlank(message = "满减活动名称不能为空", groups = UpdateGroup.class)
    private String activityName;

    @ApiModelProperty(value = "活动开始时间")
    @NotNull(message = "活动开始时间不能为空", groups = UpdateGroup.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "活动结束时间不能为空", groups = UpdateGroup.class)
    @ApiModelProperty(value = "活动结束时间")
    private Date endDate;

    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    @NotNull(message = "活动商品范围不能为空", groups = UpdateGroup.class)
    private Integer activityGoodsScope;

    @ApiModelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    @NotNull(message = "活动状态不能为空", groups = UpdateGroup.class)
    private Integer activityState;

    @ApiModelProperty(value = "活动规则 0普通满减 1每满减 2阶梯满减")
    private Integer ruleType;

    @ApiModelProperty("满减关联商品集合")
    private List<Long> relationIds;

    @ApiModelProperty("满减规则集合")
    @NotEmpty(message = "活动规则不能为空", groups = UpdateGroup.class)
    @Size(max = 6, message = "阶梯满减规则数量不能超过6条", groups = UpdateGroup.class)
    private List<ReduceRuleSaveDTO> reduceRuleSaveDTOList;
}
