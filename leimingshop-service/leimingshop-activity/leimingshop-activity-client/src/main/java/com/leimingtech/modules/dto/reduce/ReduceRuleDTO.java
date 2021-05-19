/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.reduce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 满减活动规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "ReduceRuleDTO")
public class ReduceRuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "满减活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动规则 0普通满减 1每满减 2阶梯满减")
    private Integer ruleType;
    @ApiModelProperty(value = "使用条件金额")
    private BigDecimal limitAmount;
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal reduceAmount;
    @ApiModelProperty(value = "规则排序")
    private Integer sort;
}
