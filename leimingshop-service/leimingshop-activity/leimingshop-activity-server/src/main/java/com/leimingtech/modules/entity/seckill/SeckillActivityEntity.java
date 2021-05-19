/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.seckill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 秒杀活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_seckill_activity")
public class SeckillActivityEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 场次id
     */
    private Long sessionId;
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
     * 活动有效时间
     */
    private Integer activityEffectiveTime;
    /**
     * 会员等级id
     */
    private Long memberGradeId;
    /**
     * 会员等级名称
     */
    private String memberGradeName;
    /**
     * 会员等级id
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
     * 审核状态 0未审核 1审核通过 2审核拒绝
     */
    private Integer auditState;
    /**
     * 活动状态 0未开始 1进行中 2已结束
     */
    private Integer activityState;
    /**
     * 积分是否可用 0不可用 1可用
     */
    private Integer pointFlag;
    /**
     * 余额是否可用 0不可用 1可用
     */
    private Integer balanceFlag;
    /**
     * 优惠券是否可用 0不可用 1可用
     */
    private Integer couponsFlag;
    /**
     * 满减是否可用 0不可用 1可用
     */
    private Integer reduceFlag;
    /**
     * 满包邮是否可用 0不可用 1可用
     */
    private Integer freeShippingFlag;
}
