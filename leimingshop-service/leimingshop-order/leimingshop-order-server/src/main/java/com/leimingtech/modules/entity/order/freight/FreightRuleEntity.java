/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order.freight;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运费规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_freight_rule")
public class FreightRuleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 规则类型：0叠加运费 1最高运费 2最低运费 3智能组合
     */
    private Integer ruleType;
}
