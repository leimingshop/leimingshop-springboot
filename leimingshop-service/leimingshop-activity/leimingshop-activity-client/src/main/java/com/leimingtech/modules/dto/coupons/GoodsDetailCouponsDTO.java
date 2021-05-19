/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description = "GoodsDetailCouponsDTO")
public class GoodsDetailCouponsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "可领取最优的优惠券")
    private FrontCouponsActivityPageDTO frontCouponsActivityPageDTO;

    @ApiModelProperty(value = "已领取最优的优惠券")
    private FrontMyCouponsPageDTO frontMyCouponsPageDTO;

}
