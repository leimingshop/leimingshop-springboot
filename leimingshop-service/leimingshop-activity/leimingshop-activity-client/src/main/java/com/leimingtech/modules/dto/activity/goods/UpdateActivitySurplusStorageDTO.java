/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动剩余库存更新实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-23
 */
@Data
@ApiModel(value = "活动剩余库存更新实体")
public class UpdateActivitySurplusStorageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long activityId;
    @ApiModelProperty(value = "活动类型 0无 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;
    @ApiModelProperty(value = "商品sku id")
    private Long specId;
    @ApiModelProperty(value = "商品spu id")
    private Long goodsId;
    @ApiModelProperty(value = "活动剩余库存")
    private Integer activitySurplusStorage;
}
