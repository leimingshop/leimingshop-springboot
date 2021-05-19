/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 2:59 PM
 */
@ApiModel(description = "GoodsSearchVO")
@Data
public class GoodsSearchVO implements Serializable {

    private static final long serialVersionUID = -3699978443425761935L;

    @ApiModelProperty(value = "商品信息")
    private List<GoodsVO> goodsVOList;

    @ApiModelProperty(value = "品牌信息")
    private List<BrandVO> brandVOList;

    @ApiModelProperty(value = "商品分类信息")
    private List<GoodsClassVO> goodsClassVOList;

    @ApiModelProperty(value = "一级商品分类信息")
    private List<GoodsClassVO> firstGoodsClassVOList;

    @ApiModelProperty(value = "二级商品分类信息")
    private List<GoodsClassVO> secondGoodsClassVOList;

    @ApiModelProperty(value = "店铺商品一级分类ID")
    private List<GoodsClassVO> storeGoodsClassVOList;

    @ApiModelProperty(value = "商品属性信息")
    private List<GoodsAttrVO> goodsAttrVOList;

    @ApiModelProperty(value = "商品标签集合")
    private List<GoodsLabelNameVO> goodsLabelVOList;

    @ApiModelProperty(value = "总页数")
    private Long totalCount;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "页码")
    private Integer pageNo;
}
