/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.picture;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 图片分类表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@ApiModel(description = "SavePictureCategoryDTO")
public class SavePictureCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "分组名称不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "图片分类名称")
    @Length(max = 20, message = "图片分组名称不能超过20位", groups = DefaultGroup.class)
    private String categoryName;
    @ApiModelProperty(value = "父分类ID")
    private Long parentCategoryId;
    @ApiModelProperty(value = "父类名称")
    private String parentCategoryName;
}