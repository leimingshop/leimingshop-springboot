/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 3:00 PM
 */
@ApiModel(description = "GoodsVO")
@Data
public class GoodsVO implements Serializable {

    private static final long serialVersionUID = 6895412133409850237L;
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 商品货号
     */
    @ApiModelProperty(value = "商品货号")
    private Long goodsSerial;

    /**
     * 商品副标题
     */
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubTitle;

    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品分类id")
    private Long gcId;

    @ApiModelProperty(value = "商品分类名称")
    private String gcName;

    @ApiModelProperty(value = "商品分类一级id")
    private Long firstGcId;

    @ApiModelProperty(value = "商品分类一级名称")
    private String firstGcName;

    @ApiModelProperty(value = "商品分类二级id")
    private Long secondGcId;

    @ApiModelProperty(value = "商品分类二级名称")
    private String secondGcName;

    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private String storeType;

    @ApiModelProperty(value = "商品默认对应的规格id")
    private Long specId;

    @ApiModelProperty(value = "商品标签")
    private List<GoodsLabel> goodsLabels;

    @ApiModelProperty(value = "商品图片")
    private List<GoodsPictureVo> goodsPicList;


    @ApiModelProperty(value = "商品上下架状态（默认0:下架状态，1上架状态）")
    private Integer goodsShow;

    @ApiModelProperty(value = "商品状态，默认10:待审核，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodsStatus;

    @ApiModelProperty(value = "商品关键字")
    private String goodsKeywords;

    @ApiModelProperty(value = "商品评价数")
    private Integer evaluateCount;
    @ApiModelProperty(value = "好评率")
    private String goodEvaluate;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "商品规格主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "100*100图片地址")
    private String oneHundredPxPictureUrl;

    @ApiModelProperty(value = "200*200图片地址")
    private String twoHundredPxPictureUrl;

    @ApiModelProperty(value = "400*400图片地址")
    private String fourHundredPxPictureUrl;

    @ApiModelProperty(value = "800*800图片地址")
    private String eightHundredPxPictureUrl;

    @ApiModelProperty(value = "商品销量")
    private Integer goodsSaleNum;

    @ApiModelProperty("是否为满减商品 0否 1是")
    private Integer reduceFlag;

    @ApiModelProperty("是否删除 0 未删除 1 已删除")
    private Integer delFlag;
}
