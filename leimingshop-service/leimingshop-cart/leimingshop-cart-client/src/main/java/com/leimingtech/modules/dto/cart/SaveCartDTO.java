/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 加入购物车
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ApiModel(description = "SaveCartDTO")
public class SaveCartDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "购买数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品规格ID")
    private Long specId;
    @ApiModelProperty(value = "购物车ID")
    private Long cartId;
    @ApiModelProperty(value = "活动id")
    private Long activityId;
    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

}
