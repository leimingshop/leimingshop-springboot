/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 营销明细
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "营销明细")
public class ActivityDetailStatisEntity implements Serializable {


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty("活动标记 1 优惠券 2 满减")
    private Integer activityType;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品图片")
    private String goodsImage;

    @ApiModelProperty("支付状态 0:未付款;1:已付款")
    private Integer paymentStatus;

    @ApiModelProperty("商品件数")
    private Integer goodsNum;

    @ApiModelProperty("应收金额")
    private BigDecimal realityAmount;

    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("活动减免金额")
    private BigDecimal activityReduceAmount;

    @ApiModelProperty(value = "创建时间（精确到日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（精确到秒）")
    private Date createTime;

    public void setRealityAmount(BigDecimal realityAmount) {
        if (realityAmount == null) {
            this.realityAmount = BigDecimal.ZERO;
        } else {
            this.realityAmount = realityAmount;
        }

    }

    public void setPayAmount(BigDecimal payAmount) {
        if (payAmount == null) {
            this.payAmount = BigDecimal.ZERO;
        } else {
            this.payAmount = payAmount;
        }
    }

    public void setActivityReduceAmount(BigDecimal activityReduceAmount) {
        if (activityReduceAmount == null) {
            this.activityReduceAmount = BigDecimal.ZERO;
        } else {
            this.activityReduceAmount = activityReduceAmount;
        }
    }

}
