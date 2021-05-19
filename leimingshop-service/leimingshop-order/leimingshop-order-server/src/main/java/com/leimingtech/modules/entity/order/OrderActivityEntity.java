/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单活动表
 *
 * @author weixianchun@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_activity")
public class OrderActivityEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动规则id
     */
    private Long ruleId;
    /**
     * 活动类型 0无活动 1优惠券 2秒杀
     */
    private Integer activityType;
}
