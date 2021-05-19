/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 订单活动表
 *
 * @author weixianchun@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(description = "OrderActivityDTO")
public class OrderActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单id")
    private Long orderId;
    @ApiModelProperty(value = "活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动规则id")
    private Long ruleId;
    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty(value = "下单数量")
    private Integer orderNum;

}
