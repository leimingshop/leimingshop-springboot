/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 规格实体（新增）
 *
 * @author tyl
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@ApiModel(description = "GoodsSpecAttrValurAttrDTO")
public class GoodsSpecAttrValurAttrDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规格值id")
    private Long specAttrValueId;
    @ApiModelProperty(value = "规格名id")
    private Long specAttrId;
    @ApiModelProperty(value = "规格id")
    private Long specId;
    @ApiModelProperty(value = "规格名")
    private String specAttrName;
    @ApiModelProperty(value = "规格值")
    private String specAttrValue;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "是否是规格主规格名，主规格值")
    private String isMain;


}
