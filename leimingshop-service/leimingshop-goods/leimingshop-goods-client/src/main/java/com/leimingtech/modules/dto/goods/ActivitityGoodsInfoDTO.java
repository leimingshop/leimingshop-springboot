/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <活动添加商品分页>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/10
 */
@Data
@ApiModel("活动添加商品分页")
public class ActivitityGoodsInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "商品实际销量")
    private Integer saleNum;

    @ApiModelProperty(value = "商品库存")
    private Integer specStorage;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

}
