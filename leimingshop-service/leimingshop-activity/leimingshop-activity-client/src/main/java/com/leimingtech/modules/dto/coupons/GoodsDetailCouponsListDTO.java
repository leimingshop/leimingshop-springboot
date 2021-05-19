/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券列表实体(可领取/已领取)
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@ApiModel(description = "GoodsDetailCouponsListDTO")
public class GoodsDetailCouponsListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "可领取优惠券列表")
    private List<FrontCouponsActivityPageDTO> canRecList = new ArrayList<>();

    @ApiModelProperty(value = "已领取优惠券列表")
    private List<FrontMyCouponsPageDTO> recedList = new ArrayList<>();

}
