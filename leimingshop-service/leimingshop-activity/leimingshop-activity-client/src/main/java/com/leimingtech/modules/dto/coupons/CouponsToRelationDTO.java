/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <优惠券跳转商品列表实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/20
 */
@Data
@ApiModel(description = "CouponsToRelationDTO")
public class CouponsToRelationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "跳转类型 0店铺 1指定商品 2商品详情")
    private Integer redType;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "商品规格id")
    private Long specId;

}
