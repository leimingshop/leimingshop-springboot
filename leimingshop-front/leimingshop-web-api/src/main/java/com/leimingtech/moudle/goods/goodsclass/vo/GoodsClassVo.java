/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goodsclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品分类信息
 *
 * @author xuzhch
 * @author xuzhch@leimingtech.com
 */
@Data
@ApiModel(description = "GoodsClassVo")
public class GoodsClassVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String gcName;

    @ApiModelProperty(value = "父ID")
    private Long gcParentId;

    @ApiModelProperty(value = "排序")
    private Integer gcSort;

    @ApiModelProperty(value = "分类类型（0:实体商品分类，1:虚拟商品分类）")
    private Integer classType;

    @ApiModelProperty(value = "层级path")
    private String gcIdpath;

    @ApiModelProperty(value = "商品分类")
    private GoodsClassVo children;

}