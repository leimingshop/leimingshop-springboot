/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: xuzhch
 * @Description: 商品SKU编辑
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSpecUpdateSkuDTO")
public class GoodsSpecUpdateSkuDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空", groups = UpdateGroup.class)
    private Long id;
    /**
     * 商品规格名称
     */
    @ApiModelProperty(value = "商品规格名称")
    @NotBlank(message = "商品规格名称不能为空", groups = UpdateGroup.class)
    private String specName;
    /**
     * 销售价
     */
    @ApiModelProperty(value = "销售价")
    @NotNull(message = "销售价不能为空", groups = UpdateGroup.class)
    @Min(value = 0, message = "销售价不能小于0", groups = UpdateGroup.class)
    private BigDecimal specSellPrice;
    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    @NotNull(message = "成本价不能为空", groups = UpdateGroup.class)
    @Min(value = 0, message = "成本价不能小于0", groups = UpdateGroup.class)
    private BigDecimal specCostPrice;
    /**
     * 规格库存
     */
    @ApiModelProperty(value = "规格库存")
    @NotNull(message = "库存不能为空", groups = UpdateGroup.class)
    @Min(value = 0, message = "库存不能小于0", groups = UpdateGroup.class)
    @Max(value = 999999, message = "库存最大不超过999999,", groups = UpdateGroup.class)
    private Integer specStorage;

}