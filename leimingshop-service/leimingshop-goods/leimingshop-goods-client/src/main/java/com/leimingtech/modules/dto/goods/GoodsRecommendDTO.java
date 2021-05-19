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
 * @Author: SWH ab4856812@163.com
 * @Description: 商品推荐DTO
 * @Date: 2019/7/29 15:27
 * @Version: V1.0
 */
@Data
@ApiModel(description = "GoodsRecommendDTO")
public class GoodsRecommendDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品默认对应的规格id")
    private Long specId;

    @ApiModelProperty(value = "商品店铺价格")
    private BigDecimal goodsStorePrice;

    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private String storeType;

    @ApiModelProperty(value = "标签名称")
    private String labelName;

    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;

    @ApiModelProperty("评论次数")
    private Integer commentNum;

    @ApiModelProperty("销量")
    private Integer saleNum;

}