/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营销明细
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(value = "营销明细")
public class ActivityDetailStatisDTO implements Serializable {


    @ApiModelProperty("活动标记 1 优惠券 2 满减")
    private Integer activityType;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品图片")
    private String goodsImage;

    @ApiModelProperty("支付人数")
    private Integer payPerson;

    @ApiModelProperty("下单人数")
    private Integer downOrderPerson;

    @ApiModelProperty("下单件数件数")
    private Integer orderGoodsNum;

    @ApiModelProperty("支付件数")
    private Integer payOrderGoodsNum;

    @ApiModelProperty("应收金额")
    private BigDecimal realityAmount;

    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("活动减免金额")
    private BigDecimal activityReduceAmount;

}
