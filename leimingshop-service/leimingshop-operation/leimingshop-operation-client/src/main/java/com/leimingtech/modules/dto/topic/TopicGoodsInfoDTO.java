/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.topic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 专题页商品信息
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "TopicGoodsInfoDTO")
public class TopicGoodsInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long id;

    @ApiModelProperty(value = "规格id")
    private Long specId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(name = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(name = "成本价")
    private BigDecimal specCostPrice;

    @ApiModelProperty("商品销量")
    private Integer saleNum;

    @ApiModelProperty("评论次数")
    private Integer commentNum;

    @ApiModelProperty(value = "好评率")
    private String goodEvaluate;

}
