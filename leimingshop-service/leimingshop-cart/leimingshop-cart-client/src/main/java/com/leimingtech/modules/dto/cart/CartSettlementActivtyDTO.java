/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: 刘远杰
 * @Description: 购物车结算页优惠信息选择实体
 * @Date: 15:23 2019/12/31
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "CartSettlementActivtyDTO")
public class CartSettlementActivtyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员优惠券id")
    private String memberCouponsId;

    @ApiModelProperty(value = "会员地址id")
    private Long addressId;

    @ApiModelProperty(value = "是否使用余额付款 0不使用 1使用")
    private Integer useBalance;

}
