/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
@ApiModel(description = "OrderGoodsDTO")
public class OrderGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "买家ID")
    private Long buyerId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "spu货号")
    private Long spuSerial;

    @ApiModelProperty(value = "spu名称")
    private String spuName;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "规格货号")
    private String specSerial;

    @ApiModelProperty(value = "规格描述")
    private String specInfo;

    @ApiModelProperty(value = "规格属性与属性值对应关系（key:value）")
    private String specAttrValueName;

    @ApiModelProperty(value = "商品规格成本价")
    private BigDecimal specCostPrice;

    @ApiModelProperty(value = "商品规格价格 ")
    private BigDecimal specPrice;

    @ApiModelProperty(value = "商品规格实际成交价")
    private BigDecimal specPayPrice;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty("满减活动id")
    private Long reduceActivityId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty(value = "均摊优惠金额")
    private BigDecimal avgPreferentialAmount;

    @ApiModelProperty(value = "折扣金额")
    private BigDecimal discountActivityAmount;

    @ApiModelProperty(value = "优惠券均摊金额")
    private BigDecimal couponAmount;

    @ApiModelProperty(value = "满减均摊金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty(value = "是否已退分摊金额（0未退，1已退）")
    private Integer returnPreferential;

    @ApiModelProperty(value = "可申请售后数量")
    private Integer aftersaleQuantity;

    @ApiModelProperty(value = "评价状态(0为评价，1已评价)")
    private Integer evaluationStatus;

    @ApiModelProperty(value = "评价时间")
    private Date evaluationTime;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "商品分类一级id")
    private Long firstGcId;

    @ApiModelProperty(value = "商品分类二级id")
    private Long secondGcId;

    @ApiModelProperty(value = "商品最底级分类ID")
    private Long gcId;

    @ApiModelProperty(value = "是否赠送商品(0:否，1:是)")
    private Integer isGift;

    @ApiModelProperty(value = "电子提货码")
    private String fetchCode;

    @ApiModelProperty(value = "商品总价")
    private BigDecimal goodsTotalPrice;

    @ApiModelProperty(value = "发票类型(多选 1:电子、2：纸质、3：增值税普通发票)1,2,3")
    private String invoiceType;

    @ApiModelProperty(value = "店铺商品二级分类ID")
    private Long secondStoreGoodsGcId;

    @ApiModelProperty(value = "店铺商品二级分类名称")
    private String secondStoreGoodsGcName;
    @ApiModelProperty(value = "商品支付总价")
    private BigDecimal goodsTotalPayPrice;

    public BigDecimal getGoodsTotalPrice() {
        if (specPrice != null && goodsNum != null) {
            return specPrice.multiply(BigDecimal.valueOf(goodsNum));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getGoodsTotalPayPrice() {
        // 满减优惠券商品支付金额 = 商品金额 * 购买数量 - 优惠金额
        if (this.getGoodsTotalPrice() != null && discountActivityAmount != null) {
            return this.getGoodsTotalPrice().subtract(discountActivityAmount);
        }
        return BigDecimal.ZERO;
    }

}
