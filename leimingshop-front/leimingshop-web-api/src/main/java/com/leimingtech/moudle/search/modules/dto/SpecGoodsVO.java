/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author tyl
 * @Date 2019/7/4 14:42
 * @Description
 **/
@Data
@ApiModel(description = "SpecGoodsVO")
public class SpecGoodsVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格id")
    private Long id;

    @ApiModelProperty("分类id")
    private Long gcId;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("运费模板id")
    private Long freightTemplateId;

    @ApiModelProperty("商品对应的规格Id")
    private Long specId;

    @ApiModelProperty("分类名称")
    private String gcName;

    @ApiModelProperty("商品分类一级id")
    private Long firstGcId;

    @ApiModelProperty("商品分类一级名称")
    private String firstGcName;

    @ApiModelProperty("商品分类二级id")
    private Long secondGcId;

    @ApiModelProperty("商品分类二级名称")
    private String secondGcName;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("店铺头像")
    private String storeLogo;
    @ApiModelProperty("店铺状态 0 启用 1 禁用")
    private Integer storeIsEnable;

    @ApiModelProperty("店铺类型")
    private String storeType;

    @ApiModelProperty("店铺等级")
    private String storeGrade;

    @ApiModelProperty("店铺商品一级分类ID")
    private Long firstStoreGoodsGcId;

    @ApiModelProperty("店铺商品一级分类名称")
    private String firstStoreGoodsGcName;

    @ApiModelProperty(value = "店铺商品二级分类ID")
    private Long secondStoreGoodsGcId;

    @ApiModelProperty(value = "店铺商品二级分类名称")
    private String secondStoreGoodsGcName;

    @ApiModelProperty("商品属性集合")
    private List<SpecGoodsAttrVO> goodsAttrVOList;

    @ApiModelProperty("商品规格属性名集合")
    private List<GoodsSpecAttrVO> goodsSpecAttrVOList;

    @ApiModelProperty("规格属性值关联集合")
    private List<SpecAttrValueRefVO> specAttrValueRefVO;


}
