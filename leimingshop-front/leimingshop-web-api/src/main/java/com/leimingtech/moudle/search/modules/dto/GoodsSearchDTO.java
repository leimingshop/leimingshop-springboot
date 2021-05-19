/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 2:46 PM
 */
@ApiModel(description = "GoodsSearchDTO")
@Data
public class GoodsSearchDTO implements Serializable {

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

    @ApiModelProperty("店铺二级商品分类Id")
    private String[] storeGcIds;

    @ApiModelProperty(value = "店铺id", required = true)
    private Long storeId;

    @ApiModelProperty("标签id")
    private String[] labelIds;

    @ApiModelProperty(value = "排序字段(价格：specSellPrice，销量：goodsSaleNum，时间：goodsUpTime)")
    private String sortField;

    @ApiModelProperty(value = "排序类型：DESC倒叙，ASC正序", required = true)
    private String sortType;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "第几页", required = true)
    private Integer pageNo;
}
