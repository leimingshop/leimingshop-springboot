/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.dto;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author pixiaoyong@leimingtech.com
 * @date 2020/4/10 11:56
 * @email 1609973595@qq.com
 */
@Data
public class Base64VideoDTO {

    @ApiModelProperty(value = "base64编码的视频字符串")
    @NotBlank(message = "base64编码的视频字符串不能为空", groups = AddGroup.class)
    private String videoStr;

    @ApiModelProperty(value = "视频名称")
    @Length(min = 0, max = 100, message = "最多可输入100个字符", groups = AddGroup.class)
    private String videoName;

}
