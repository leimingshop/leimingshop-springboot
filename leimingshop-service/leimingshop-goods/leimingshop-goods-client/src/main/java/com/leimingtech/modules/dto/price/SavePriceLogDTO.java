/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.price;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新增价格修改记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Data
@ApiModel(description = "SavePriceLogDTO")
public class SavePriceLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sku")
    private Long sku;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "修改前成本价")
    @NotBlank(message = "修改前成本价不能为空", groups = AddGroup.class)
    private BigDecimal beforeCostPrice;
    @ApiModelProperty(value = "修改前销售价")
    @NotBlank(message = "修改前销售价不能为空", groups = AddGroup.class)
    private BigDecimal beforeMarketPrice;
    @ApiModelProperty(value = "修改后成本价")
    @NotBlank(message = "修改后成本价不能为空", groups = AddGroup.class)
    private BigDecimal afterCostPrice;
    @ApiModelProperty(value = "修改后销售价")
    @NotBlank(message = "修改后销售价不能为空", groups = AddGroup.class)
    private BigDecimal afterMarketPrice;


}