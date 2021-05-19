/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 订单商品确认Entity
 * @Date: 2019/6/22 11:28
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_goods_confirm")
public class OrderGoodsConfirmEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单确认表ID
     */
    private Long orderConfirmId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商品实际成交价
     */
    private BigDecimal goodsPayPrice;

    /**
     * 商品销售价
     */
    private BigDecimal specPrice;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 是否赠送商品(0:否，1:是)
     */
    private Integer isGift;

    /**
     * 商品成本价
     */
    private BigDecimal specCostPrice;

    /**
     * 满减活动id
     */
    private Long reduceActivityId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购
     */
    private Integer activityType;

    /**
     * 活动记录id
     */
    private Long activityRecordId;

    /**
     * 虚拟订单标记（0:否，1是）
     */
    private Integer virtualFlag;

    /**
     * 配送方式 0:无需物流 1:快递 2自提 3电子提货码
     */
    private Integer devlierType;

}
