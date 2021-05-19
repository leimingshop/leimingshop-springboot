/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.group;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 拼团表
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_group")
public class GroupEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 拼团名称
     */
    private String groupName;
    /**
     * 拼团开始时间
     */
    private Date startTime;
    /**
     * 拼团结束时间
     */
    private Date endTime;
    /**
     * 参团条件（默认0无限制，1新用户）
     */
    private Integer joinFlag;
    /**
     * 活动预热时间（小时）
     */
    private Integer groupPreheat;
    /**
     * 推荐拼团（0开启，1关闭）
     */
    private Integer recommendFlag;
    /**
     * 模拟成团（0开启，1关闭）
     */
    private Integer simulateFlag;
    /**
     * 成团有效时间（小时）
     */
    private Integer validTime;
    /**
     * 订单支付有效期（分钟）
     */
    private Integer payEndTime;
    /**
     * 下单使用优惠（0允许，默认1不允许）
     */
    private Integer discountFlag;
    /**
     * 下单可用抵扣（（0允许，默认1不允许））
     */
    private Integer deductionFlag;
    /**
     * 审核状态（10：发布 20：审核 30：审核通过 40：审核未通过 50：取消）
     */
    private Integer auditStatus;
    /**
     * 拼团众筹活动状态 0：未开始 1：活动中 2：活动结束
     */
    private Integer activityStatus;
    /**
     * 商品个数（待定）
     */
    private Integer goodsNum;
    /**
     * 成团个数（待定）
     */
    private Integer groupNum;
    /**
     * 支付订单数（待定）
     */
    private Integer paymentNum;
}
