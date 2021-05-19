/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.icon;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author chengqian
 * @date 2019/12-9
 * @since v1.0.0
 */
@Data
@ApiModel(description = "BottomIconUpdateDTO")
public class BottomIconUpdateDTO {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "icon名称")
    @NotBlank(message = "icon名称不能为空", groups = UpdateGroup.class)
    @Size(max = 5, message = "名称长度最多5个字符")
    private String menuName;

    @ApiModelProperty(value = "未选择图标")
    @NotBlank(message = "未选择图标不能为空", groups = UpdateGroup.class)
    private String unselectedIcon;

    @ApiModelProperty(value = "已选择图标")
    @NotBlank(message = "已图标不能为空", groups = UpdateGroup.class)
    private String selectedIcon;
}
