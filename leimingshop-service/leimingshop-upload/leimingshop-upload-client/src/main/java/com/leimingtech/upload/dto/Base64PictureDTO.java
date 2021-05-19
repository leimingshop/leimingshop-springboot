/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.dto;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 功能描述：
 * <base64图片上传>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/6/10 15:28
 **/
@Data
@ApiModel(description = "Base64PictureDTO")
public class Base64PictureDTO {

    @ApiModelProperty(value = "base64编码的图片字符串")
    @NotBlank(message = "base64编码的图片字符串不能为空", groups = AddGroup.class)
    private String imgStr;

    @ApiModelProperty(value = "图片名称")
    @Length(min = 0, max = 100, message = "最多可输入100个字符", groups = AddGroup.class)
    private String pictureName;

}
