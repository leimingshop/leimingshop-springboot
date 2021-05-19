/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 图片分类表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@ApiModel(description = "PictureCategoryDTO")
public class PictureCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "图片分类名称")
    private String categoryName;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "分组下图片数量")
    private Integer pictureCount;
    @ApiModelProperty(value = "父分类ID")
    private Long parentCategoryId;
    @ApiModelProperty(value = "父分类名称")
    private String parentCategoryName;
    @ApiModelProperty(value = "父分组下图片数量")
    private Integer parentCount;
}