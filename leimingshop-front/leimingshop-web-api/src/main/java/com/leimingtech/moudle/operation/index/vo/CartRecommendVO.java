/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 购物车推荐商品表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-6
 */
@Data
@ApiModel(description = "CartRecommendPageDTO")
public class CartRecommendVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "商品spu的id")
    private Long goodsId;
    @ApiModelProperty(value = "规格ID")
    private Long specId;
    @ApiModelProperty(value = "排序号")
    private Integer sortNum;
    @ApiModelProperty(value = "所属分类")
    private String category;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;
    @ApiModelProperty(value = "商品标签")
    private String labelName;
    @ApiModelProperty(value = "商品销售价")
    private BigDecimal specSellPrice;
    @ApiModelProperty(value = "商品库存")
    private Integer storage;
    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;
    @ApiModelProperty("评论次数")
    private Integer commentNum;
    @ApiModelProperty("销量")
    private Integer saleNum;
}