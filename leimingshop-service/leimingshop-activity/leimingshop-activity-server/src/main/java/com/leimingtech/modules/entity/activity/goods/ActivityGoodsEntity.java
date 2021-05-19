/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.activity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 活动商品实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_activity_goods")
public class ActivityGoodsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀场次id
     */
    private Long sessionId;
    /**
     * 活动id
     */
    private Long activityId;
    /**
     * 活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购
     */
    private Integer activityType;
    /**
     * 商品spu id
     */
    private Long goodsId;
    /**
     * 商品sku id
     */
    private Long specId;
    /**
     * 活动库存
     */
    private Integer activityStorage;
    /**
     * 活动剩余库存
     */
    private Integer activitySurplusStorage;
    /**
     * 活动价格
     */
    private BigDecimal activityPrice;
    /**
     * 下单数
     */
    private Integer orderNum;
    /**
     * spu订单数（包含已删除的sku下单数）
     */
    private Integer spuOrderNum;
    /**
     * 单次购买件数（仅限制拼团活动）
     */
    private Integer onceBuyLimit;
    /**
     * 参团次数限制（仅限制拼团活动）
     */
    private Integer joinLimit;
    /**
     * 成团人数（仅限制拼团活动）
     */
    private Integer regimentNum;
    /**
     * 排序
     */
    private Integer sort;
}
