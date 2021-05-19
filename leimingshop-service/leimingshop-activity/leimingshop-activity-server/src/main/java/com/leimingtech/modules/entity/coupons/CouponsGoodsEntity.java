/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.coupons;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠券活动商品实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_coupons_goods")
public class CouponsGoodsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券活动id
     */
    private Long activityId;

    /**
     * 活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌
     */
    private Integer activityGoodsScope;

    /**
     * 关联id
     */
    private Long relationId;

    /**
     * 关联名称
     */
    private String relationName;

}
