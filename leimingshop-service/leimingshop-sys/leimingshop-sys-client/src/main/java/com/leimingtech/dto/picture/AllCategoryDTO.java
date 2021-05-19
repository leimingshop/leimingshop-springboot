/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 所有图片
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@ApiModel(description = "AllCategoryDTO")
public class AllCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "图片分类名称")
    private String categoryName;

    @ApiModelProperty(value = "分组下图片数量")
    private Integer pictureCount;
    @ApiModelProperty(value = "分组下图片数量")
    private List<FindPictureCategoryDTO> list;

}