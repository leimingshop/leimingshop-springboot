/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodsclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品分类和规格关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-03
 */
@Data
@ApiModel(description = "GoodsClassSpecSaveDTO")
public class GoodsClassSpecSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "商品分类ID")
    private Long gcClassId;
    @ApiModelProperty(value = "规格ID")
    private Long specId;
    @ApiModelProperty(value = "规格名称")
    private String specName;

}