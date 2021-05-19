/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 刘远杰 17839193044@162.com
 * @Description: 结算商品按照活动分组实体
 * @Date: 16:30 2019/12/17
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "CouponsActivityGoodsGroupDTO")
public class CouponsActivityGoodsGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "立即购买和去结算商品信息")
    private List<CartGoodsDTO> cartGoodsDTOList;

    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsTotalPrice;

    @ApiModelProperty(value = "商品店铺id")
    private Long storeId;
}
