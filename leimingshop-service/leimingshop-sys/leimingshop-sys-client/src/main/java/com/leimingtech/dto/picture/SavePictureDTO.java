/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.picture;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 图片表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Data
@ApiModel(description = "SavePictureDTO")
public class SavePictureDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "图片分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "分组名称")
    private String categoryName;
    @ApiModelProperty(value = "图片名称")
    private String pictureName;
    @ApiModelProperty(value = "缩略图地址")
    private String thumbPath;

    @ApiModelProperty(value = "图片地址")
    @NotBlank(message = "图片地址不能为空", groups = AddGroup.class)
    private String picturePath;

}