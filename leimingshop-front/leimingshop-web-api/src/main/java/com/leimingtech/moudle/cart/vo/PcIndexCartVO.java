/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.cart.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Data: 2020/5/18 16:36
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel("pc首页购物车弹窗")
public class PcIndexCartVO {

    @ApiModelProperty("购物车数量")
    private Integer num;

    @ApiModelProperty("购物车商品总价钱")
    private BigDecimal goodsTotal;

    @ApiModelProperty("购物车集合")
    private List<CartVO> list;


}
