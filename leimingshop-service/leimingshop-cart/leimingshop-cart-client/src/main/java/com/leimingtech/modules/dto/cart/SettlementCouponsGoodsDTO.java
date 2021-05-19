/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 订单结算页优惠券活动商品
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@ApiModel(description = "SettlementCouponsGoodsDTO")
public class SettlementCouponsGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "优惠券活动id")
    private Long activityId;

    @ApiModelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    @ApiModelProperty(value = "关联id")
    private Long relationId;

    @ApiModelProperty(value = "关联名称")
    private String relationName;

}
