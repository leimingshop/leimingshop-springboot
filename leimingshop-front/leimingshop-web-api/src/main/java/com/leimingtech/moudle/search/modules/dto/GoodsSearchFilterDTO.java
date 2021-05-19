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
 * @date 2019/6/29 2:46 PM
 */
@ApiModel(description = "GoodsSearchFilterDTO")
@Data
public class GoodsSearchFilterDTO implements Serializable {

    private static final long serialVersionUID = -2329345733029038572L;

    @ApiModelProperty("搜索关键字")
    private String keyword;

    @ApiModelProperty("分类id")
    private String[] gcIds;

//    @ApiModelProperty("一级分类id")
//    private String[] firstGcIds;
//
//    @ApiModelProperty("二级分类id")
//    private String[] secondGcIds;

    @ApiModelProperty("店铺一级商品分类Id")
    private String[] storeFirstGcIds;

    @ApiModelProperty("店铺分类Id")
    private String[] storeGcIds;

    @ApiModelProperty("品牌id")
    private String[] brandIds;

    @ApiModelProperty("商品属性集合")
    private List<AttrListDTO> attrListList;

    @ApiModelProperty("最低价格")
    private Double minPrice;

    @ApiModelProperty("最高价")
    private Double maxPrice;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty(value = "仅显示有货")
    private String hasStorage;

    @ApiModelProperty(value = "仅显示特惠")
    private String hasPreferential;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序类型：DESC倒叙，ASC正序", required = true)
    private String sortType;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "第几页", required = true)
    private Integer pageNo;
    @ApiModelProperty(value = "商品销量")
    private Integer goodsSaleNum;

    @ApiModelProperty(value = "商品标签ids")
    private String[] labelIds;
}
