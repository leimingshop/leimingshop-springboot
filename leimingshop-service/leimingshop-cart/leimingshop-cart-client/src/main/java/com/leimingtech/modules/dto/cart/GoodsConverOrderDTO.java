/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * @Author: LX 17839193044@162.com
 * @Description: 商品转化订单实体
 * @Date: 16:30 2019/6/14
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "GoodsConverOrderDTO")
public class GoodsConverOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 立即购买和去结算商品信息
     */
    @ApiModelProperty(value = "立即购买和去结算商品信息")
    private List<CartGoodsDTO> cartGoodsDTOList;

    /**
     * 商品总价格
     */
    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsTotalPrice;

    /**
     * 实际支付金额
     */
    @ApiModelProperty(value = "商品总价格")
    private BigDecimal orderPrice;

    /**
     * 优惠券金额
     */
    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponsPrice;

    /**
     * 是否选择满减 0未选择 1已选择
     */
    @ApiModelProperty(value = "是否选择满减 0未选择 1已选择")
    private Integer reduceFlag;

    /**
     * 活动立减
     */
    @ApiModelProperty(value = "活动立减")
    private BigDecimal reducePrice;

    /**
     * 立减活动id
     */
    @ApiModelProperty(value = "立减活动id")
    private Long reduceActivityId;

    /**
     * 优惠总金额
     */
    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal activityPrice;

    /**
     * 商品总运费
     */
    @ApiModelProperty(value = "商品总运费")
    private BigDecimal goodsTotalFreight;

    /**
     * 商品店铺id
     */
    @ApiModelProperty(value = "商品店铺id")
    private Long storeId;

    /**
     * 商品店铺名称
     */
    @ApiModelProperty(value = "商品店铺名称")
    private String storeName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "商品店铺图标")
    private String storeLogo;

    /**
     * 可用优惠券列表
     */
    @ApiModelProperty(value = "可用优惠券列表")
    private List<SettlementCouponsPageDTO> canList;

    /**
     * 不可用优惠券列表
     */
    @ApiModelProperty(value = "不可用优惠券列表")
    private List<SettlementCouponsPageDTO> canntList;
}
