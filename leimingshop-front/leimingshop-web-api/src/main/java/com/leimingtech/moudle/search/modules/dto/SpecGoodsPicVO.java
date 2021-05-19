/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 商品详情页返回商品默认图集合（保存编辑商品第一行图片）
 * @Date: 2019/7/24 13:54
 * @Version: V1.0
 */
@Data
@ApiModel(description = "SpecGoodsPicVO")
public class SpecGoodsPicVO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "是否是主图（0：否，1是）")
    private Integer isMainPicture;
}
