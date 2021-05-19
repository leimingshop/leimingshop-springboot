/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attribute;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 属性值实体（新增）
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Data
@ApiModel(description = "InsertAttributeValueDTO")
public class InsertAttributeValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "属性值名称")
    @NotBlank(message = "属性值名称不能为空", groups = AddGroup.class)
    @Size(max = 20, message = "属性值名称长度不能超过20", groups = AddGroup.class)
    private String attrValueName;

    @ApiModelProperty(value = "属性图片")
    private String attrValueImage;

}
