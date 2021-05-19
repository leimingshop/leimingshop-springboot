/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.reduce;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 满减活动规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_reduce_rule")
public class ReduceRuleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券活动id
     */
    private Long activityId;
    /**
     * 活动规则 0普通满减 1每满减 2阶梯满减
     */
    private Integer ruleType;
    /**
     * 使用条件金额
     */
    private BigDecimal limitAmount;
    /**
     * 折扣金额
     */
    private BigDecimal reduceAmount;
    /**
     * 规则排序
     */
    private Integer sort;
}
