/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.vo.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠券列表实体(可领取/已领取)
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/16 17:34
 **/
@Data
@ApiModel(value = "优惠券列表实体(可领取/已领取)")
public class GoodsDetailCouponsListVO implements Serializable {

    private static final long serialVersionUID = -2955739049472555335L;
    @ApiModelProperty(value = "可使用优惠券列表")
    private List<CouponsVO> canRecList;

    @ApiModelProperty(value = "已领取优惠券列表")
    private List<MyCouponsVO> recedList;

}
