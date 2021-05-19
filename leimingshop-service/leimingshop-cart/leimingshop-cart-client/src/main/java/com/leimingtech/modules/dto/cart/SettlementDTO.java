/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import com.leimingtech.modules.dto.address.MemberAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 去结算与提交订单返回DTO
 * @Date: 15:23 2019/6/14
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "SettlementDTO")
public class SettlementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员地址信息
     */
    @ApiModelProperty(value = "会员地址")
    private MemberAddressDTO memberAddress;

    /**
     * 虚拟订单标记（0:否，1是）
     */
    @ApiModelProperty(value = "虚拟订单标记（0:否，1是）")
    private Integer virtualFlag;

    /**
     * 是否有默认地址true:有，false:没有
     */
    @ApiModelProperty(value = "是否有默认地址true:有，false:没有")
    private Boolean hasDefaultAddress;

    /**
     * 温馨提示
     */
    @ApiModelProperty(value = "温馨提示")
    private String kindlyeminder;

    /**
     * 购物车转化订单数据
     */
    @ApiModelProperty(value = "购物车转化订单数据")
    private List<GoodsConverOrderDTO> cartToOrderList;

    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderPrice;

    /**
     * 应支付总金额
     */
    @ApiModelProperty(value = "应支付金额")
    private BigDecimal payPrice;

    /**
     * 优惠券金额
     */
    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponsPrice;

    /**
     * 活动立减
     */
    @ApiModelProperty(value = "活动立减")
    private BigDecimal reducePrice;

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
     * 立即购买商品单价
     */
    @ApiModelProperty(value = "立即购买商品单价")
    private BigDecimal specSellPrice;

    /**
     * 商品总数量
     */
    @ApiModelProperty(value = "商品总数量")
    private Integer totalNum;

    /**
     * 用户可用余额
     */
    @ApiModelProperty(value = "用户可用余额")
    private BigDecimal availableBalance;

    /**
     * 用户是否设置余额支付密码
     */
    @ApiModelProperty(value = "是否设置余额支付密码(1：有密码，0：无密码)")
    private Integer payPwdFlag;

    /**
     * 应支付总金额
     */
    @ApiModelProperty(value = "第三方支付金额")
    private BigDecimal thirdPayPrice;
    /**
     * 积分是否可用(默认：0不可用，1可用)
     */
    @ApiModelProperty(value = "积分是否可用(默认：0不可用，1可用)")
    private Integer pointFlag;
    /**
     * 余额是否可用(默认：0不可用，1可用)
     */
    @ApiModelProperty(value = "余额是否可用(默认：0不可用，1可用)")
    private Integer balanceFlag;
    /**
     * 优惠券是否可用 (默认：0不可用，1可用)
     */
    @ApiModelProperty(value = "优惠券是否可用 (默认：0不可用，1可用)")
    private Integer couponsFlag;
    /**
     * 满减是否可用(默认：0不可用，1可用)
     */
    @ApiModelProperty(value = "满减是否可用(默认：0不可用，1可用)")
    private Integer reduceFlag;

}
