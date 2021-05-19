/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.price;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSpecPriceUpdateDTO")
public class GoodsSpecPriceUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "规格ID")
    @NotNull(message = "规格ID不能为空", groups = UpdateGroup.class)
    private Long id;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "规格编号")
    @NotNull(message = "规格编号不能为空", groups = UpdateGroup.class)
    private String specSerial;

    /**
     * 销售价
     */
    @ApiModelProperty(value = "销售价")
    @Min(value = 0, message = "销售价不能小于0", groups = UpdateGroup.class)
    @NotNull(message = "销售价不能为空", groups = UpdateGroup.class)
    private BigDecimal specSellPrice;
    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    @Min(value = 0, message = "成本价不能小于0", groups = UpdateGroup.class)
    @NotNull(message = "成本价不能为空", groups = UpdateGroup.class)
    private BigDecimal specCostPrice;


    /**
     * 销售价
     */
    @ApiModelProperty(value = "修改前价格销售价（后台使用）")
    private BigDecimal beforeSpecSellPrice;
    /**
     * 成本价
     */
    @ApiModelProperty(value = "修改前成本价价（后台使用）")
    private BigDecimal beforeSpecCostPrice;
}