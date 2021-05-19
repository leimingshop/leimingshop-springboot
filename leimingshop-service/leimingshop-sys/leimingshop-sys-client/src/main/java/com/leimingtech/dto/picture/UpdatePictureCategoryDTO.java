/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.picture;

import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName:UpdatePictureDTO
 * @Data:2019/5/14 10:48
 * @Author：chengqian
 * @Version 1.0
 */
@Data
@ApiModel(description = "UpdatePictureCategoryDTO")
public class UpdatePictureCategoryDTO extends SavePictureCategoryDTO implements Serializable {


    @ApiModelProperty(value = "主键")
    private Long id;
    @NotBlank(message = "图片分组名称不能为空", groups = UpdateGroup.class)
    @Length(max = 20, message = "图片分组名称不能超过20位", groups = DefaultGroup.class)
    @ApiModelProperty(value = "图片分类名称")
    private String categoryName;
//    @ApiModelProperty(value = "父分类ID")
//    private Long parentCategoryId;
//    @ApiModelProperty(value = "父类名称")
//    private String parentCategoryName;
}
