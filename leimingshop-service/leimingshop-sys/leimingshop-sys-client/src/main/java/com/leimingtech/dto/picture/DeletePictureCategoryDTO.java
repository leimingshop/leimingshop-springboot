/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 删除分组实体
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@ApiModel(description = "DeletePictureCategoryDTO")
public class DeletePictureCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键集合")
    private Long[] id;
    @ApiModelProperty(value = "是否删除图片 0 不删除，1 删除")
    private Integer delFlag;
}