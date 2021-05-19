/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 商品规格属性值与图片关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "SpecAttributePictureDetailDTO")
public class SpecAttributePictureDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

    @ApiModelProperty(value = "商品规格属性值ID")
    private Long specAttrValueId;

    @ApiModelProperty(value = "商品规格属性值")
    private String specAttrValue;

    @ApiModelProperty(value = "商品规格属性ID")
    private Long specAttrId;

    @ApiModelProperty(value = "图片路径集合")
    private List<SpecAttributePictureUrlDTO> specAttributePictureDTOList;

}