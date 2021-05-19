/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodsclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品分类和品牌关联表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2019-06-25
 */
@Data
@ApiModel(description = "GoodsClassBrandSaveDTO")
public class GoodsClassBrandSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "品牌Id")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "分类Id(不用传)")
    private Long gcClassId;
}