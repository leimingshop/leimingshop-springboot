/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品规格属性值与图片关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "SpecAttributePictureSaveDTO")
public class SpecAttributePictureSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "商品规格属性值ID")
    private Long specAttrValueId;

    @ApiModelProperty(value = "商品规格属性ID")
    private Long specAttrId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "图片地址")
    private String pictureUrl;

    @ApiModelProperty(value = "100*100图片地址")
    private String oneHundredPxPictureUrl;

    @ApiModelProperty(value = "200*200图片地址")
    private String twoHundredPxPictureUrl;

    @ApiModelProperty(value = "400*400图片地址")
    private String fourHundredPxPictureUrl;

    @ApiModelProperty(value = "800*800图片地址")
    private String eightHundredPxPictureUrl;

    @ApiModelProperty(value = "是否主图（默认0否，1是）")
    private Integer isMainPicture;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
