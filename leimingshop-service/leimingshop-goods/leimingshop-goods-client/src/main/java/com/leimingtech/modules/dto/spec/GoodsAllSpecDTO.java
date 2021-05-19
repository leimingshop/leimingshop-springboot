/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author tyl
 * @Date 2019/6/29 14:46
 * @Description
 **/
@Data
@ApiModel(description = "GoodsAllSpecDTO")
public class GoodsAllSpecDTO {

    @ApiModelProperty(value = "规格值id")
    private Long goodsId;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "规格名称")
    private String specAttrName;
    @ApiModelProperty(value = "规格名称id")
    private Long lgsaId;
    @ApiModelProperty(value = "规格值id")
    private Long lgsavId;
    @ApiModelProperty(value = "规格值名称")
    private String specAttrValue;

}
