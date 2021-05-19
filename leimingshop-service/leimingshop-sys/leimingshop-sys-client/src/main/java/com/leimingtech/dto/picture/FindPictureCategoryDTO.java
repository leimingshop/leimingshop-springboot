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
 * 查询分组
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@ApiModel(description = "FindPictureCategoryDTO")
public class FindPictureCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "子分组集合")
    List<SubordinateCategoryDTO> list;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "图片分类名称")
    private String categoryName;
    @ApiModelProperty(value = "分组下图片数量")
    private Integer pictureCount;
}