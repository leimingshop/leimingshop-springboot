/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 规格分组实体(修改)
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Data
@ApiModel(description = "UpdateSpecGroupDTO")
public class UpdateSpecGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "规格分组名称")
    @NotBlank(message = "规格分组名称不能为空", groups = UpdateGroup.class)
    @Size(max = 20, message = "规格分组名称长度不能超过20", groups = UpdateGroup.class)
    private String groupName;

    @ApiModelProperty(value = "关联规格id")
    @NotEmpty(message = "关联规格id不能为空", groups = UpdateGroup.class)
    private Long[] specIds;

}
