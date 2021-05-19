/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.reduce;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 满减活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_reduce_activity")
public class ReduceActivityEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 满减活动名称
     */
    private String activityName;
    /**
     * 活动规则名称
     */
    private String activityRuleName;
    /**
     * 活动范围 0平台 1店铺
     */
    private Integer activityScope;
    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 活动规则 0普通满减 1每满减 2阶梯满减
     */
    private Integer ruleType;
    /**
     * 会员等级id
     */
    private Long memberGraderId;
    /**
     * 会员等级名称
     */
    private String memberGraderName;
    /**
     * 活动开始时间
     */
    private Date startDate;
    /**
     * 活动结束时间
     */
    private Date endDate;
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
     * 秒杀活动是否可用 0不可用 1可用
     */
    private Integer seckillFlag;
    /**
     * 活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌
     */
    private Integer activityGoodsScope;
    /**
     * 审核状态 0未审核 1审核通过 2审核未通过
     */
    private Integer auditState;
    /**
     * 活动状态 0未开始 1进行中 2已结束
     */
    private Integer activityState;
}
