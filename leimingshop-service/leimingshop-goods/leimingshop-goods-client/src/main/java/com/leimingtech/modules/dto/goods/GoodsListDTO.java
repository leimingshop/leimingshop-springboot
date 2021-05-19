/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName GoodsListDTO
 * @Description 商品列表
 * @Author DY
 * @Date 2019/6/5 15:12
 * @Version 1.0
 **/
@Data
@ApiModel(description = "GoodsListDTO")
public class GoodsListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品货号")
    private Long goodsSerial;

    @ApiModelProperty(value = "规格id")
    private Long specId;

    @ApiModelProperty(value = "商品规格主图")
    private String pictureUrl;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "成本价")
    private BigDecimal specCostPrice;
    @ApiModelProperty(value = "商品库存")
    private Integer goodsStock;
    @ApiModelProperty(value = "商品分类id")
    private Long gcId;

    @ApiModelProperty(value = "商品分类名称")
    private String gcName;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "是否发布(上架)过（默认0:不是，1:是）")
    private Integer publishFlag;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    /**
     * 商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布
     */
    @ApiModelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布")
    private Integer goodsStatus;

    @ApiModelProperty(value = "商品审核描述")
    private String remarks;

}
