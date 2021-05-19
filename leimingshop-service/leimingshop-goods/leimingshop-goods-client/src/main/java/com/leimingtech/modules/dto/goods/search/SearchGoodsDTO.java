/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName : SearchGoodsDTO
 * @Description: TODO
 * @Author : xuzhch
 * @Date ： 2019/6/18
 * @Version ：v1.0
 */

@Data
@ApiModel(description = "SearchGoodsDTO")
public class SearchGoodsDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品货号")
    private Long goodsSerial;

    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;

    @ApiModelProperty(value = "商品分类id")
    private Long gcId;

    @ApiModelProperty(value = "商品分类名称")
    private String gcName;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品默认对应的规格id")
    private Long specId;

    @ApiModelProperty(value = "商品上下架状态（默认0:下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodsStatus;

    @ApiModelProperty(value = "商品关键字")
    private String goodsKeywords;

    @ApiModelProperty(value = "商品评价数")
    private Integer evaluateCount;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;

    @ApiModelProperty(value = "商品销量")
    private Integer goodsSaleNum;


}
