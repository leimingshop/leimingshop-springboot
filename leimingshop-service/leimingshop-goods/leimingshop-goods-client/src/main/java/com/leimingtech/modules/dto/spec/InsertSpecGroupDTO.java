/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 规格分组实体(新增)
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@ApiModel(description = "InsertSpecGroupDTO")
public class InsertSpecGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规格分组名称")
    @Size(max = 20, message = "规格分组名称长度不能超过20", groups = AddGroup.class)
    @NotBlank(message = "分组名称不能为空", groups = AddGroup.class)
    private String groupName;

    @ApiModelProperty(value = "关联规格id")
    @NotEmpty(message = "关联规格id不能为空", groups = AddGroup.class)
    private Long[] specIds;

}
