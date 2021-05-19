/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.flashsale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 限时抢购活动表
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_flash_sale_activity")
public class FlashSaleActivityEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 活动开始时间
     */
    private Date activityStartDate;
    /**
     * 活动结束时间
     */
    private Date activityEndDate;
    /**
     * 活动状态(默认:0未开始，1进行中，2已结束)
     */
    private Integer activityState;
    /**
     * 订单支付有效时间(单位：分钟)
     */
    private Integer payValidTime;
    /**
     * 会员等级id
     */
    private Long memberGradeId;
    /**
     * 会员等级名称
     */
    private String memberGradeName;
    /**
     * 会员等级积分
     */
    private Integer memberGradeLimit;
    /**
     * 活动限购数量
     */
    private Integer restrictionQuantity;
    /**
     * 浏览数
     */
    private Integer browseNum;
    /**
     * 下单数
     */
    private Integer orderNum;
    /**
     * 积分是否可用(默认：0不可用，1可用)
     */
    private Integer pointFlag;
    /**
     * 余额是否可用(默认：0不可用，1可用)
     */
    private Integer balanceFlag;
    /**
     * 优惠券是否可用 (默认：0不可用，1可用)
     */
    private Integer couponsFlag;
    /**
     * 满减是否可用(默认：0不可用，1可用)
     */
    private Integer reduceFlag;
}
