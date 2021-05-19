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
 * 满减活动规则实体(保存)
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@ApiModel(description = "ReduceRuleSaveDTO")
public class ReduceRuleSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 规则id (映射)
     */
    private Long ruleId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动规则 0普通满减 1每满减 2阶梯满减")
    private Integer ruleType;
    @ApiModelProperty(value = "使用条件金额")
    private BigDecimal limitAmount;
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal reduceAmount;
}
