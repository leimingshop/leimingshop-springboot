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
 * 优惠券列表实体(可使用/不可使用)
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@ApiModel(description = "OrderCouponsListDTO")
public class OrderCouponsListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "可使用优惠券列表")
    private List<FrontMyCouponsPageDTO> canUseList = new ArrayList<>();

    @ApiModelProperty(value = "不可使用优惠券列表")
    private List<FrontMyCouponsPageDTO> canntUseList = new ArrayList<>();

}
