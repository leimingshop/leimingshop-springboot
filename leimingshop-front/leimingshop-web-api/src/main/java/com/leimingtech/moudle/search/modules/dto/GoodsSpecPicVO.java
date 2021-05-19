/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author tyl
 * @Date 2019/7/4 11:55
 * @Description
 **/
@Data
@ApiModel(description = "GoodsSpecPicVO")
public class GoodsSpecPicVO {

    @ApiModelProperty(value = "商品规格id")
    private Long id;

    @ApiModelProperty(value = "商品规格属性值")
    private Long specAttrValueId;

    @ApiModelProperty(value = "商品规格属性Id")
    private Long specAttrId;

    @ApiModelProperty(value = "商品图片地址")
    private String pictureUrl;

    @ApiModelProperty(value = "100*100图片地址")
    private String oneHundredPxPictureUrl;

    @ApiModelProperty(value = "200*200图片地址")
    private String twoHundredPxPictureUrl;

    @ApiModelProperty(value = "400*400图片地址")
    private String fourHundredPxPictureUrl;

    @ApiModelProperty(value = "800*800图片地址")
    private String eightHundredPxPictureUrl;

    @ApiModelProperty(value = "是否主图（0：否，1：是）")
    private Integer isMainPicture;
}
