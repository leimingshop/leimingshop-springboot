/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_goods")
public class OrderGoodsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private Long orderSn;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * spu货号
     */
    private Long spuSerial;

    /**
     * spu名称
     */
    private String spuName;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 商品规格货号
     */
    private String specSerial;

    /**
     * 规格描述
     */
    private String specInfo;

    /**
     * 规格属性与属性值对应关系（key:value）
     */
    private String specAttrValueName;

    /**
     * 商品规格成本价
     */
    private BigDecimal specCostPrice;

    /**
     * 商品规格价格
     */
    private BigDecimal specPrice;

    /**
     * 商品规格实际成交价
     */
    private BigDecimal specPayPrice;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 商品图片
     */
    private String goodsImage;

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
     * 均摊优惠金额
     */
    private BigDecimal avgPreferentialAmount;

    /**
     * 折扣金额
     */
    private BigDecimal discountActivityAmount;

    /**
     * 优惠券均摊金额
     */
    private BigDecimal couponAmount;

    /**
     * 满减均摊金额
     */
    private BigDecimal reduceAmount;

    /**
     * 是否已退分摊金额（0未退，1已退）
     */
    private Integer returnPreferential;

    /**
     * 可申请售后数量
     */
    private Integer aftersaleQuantity;

    /**
     * 评价状态(0为评价，1已评价)
     */
    private Integer evaluationStatus;

    /**
     * 评价时间
     */
    private Date evaluationTime;

    /**
     * 商品分类一级id
     */
    private Long firstGcId;

    /**
     * 商品分类二级id
     */
    private Long secondGcId;

    /**
     * 商品最底级分类ID
     */
    private Long gcId;

    /**
     * 是否赠送商品(0:否，1:是)
     */
    private Integer isGift;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;


}
