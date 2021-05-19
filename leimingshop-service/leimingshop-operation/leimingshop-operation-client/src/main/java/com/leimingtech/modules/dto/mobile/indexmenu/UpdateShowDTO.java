/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.mobile.indexmenu;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 编辑移动首页菜单实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */
@Data
@ApiModel(description = "UpdateShowDTO")
public class UpdateShowDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "是否显示 0不显示 1显示")
    @NotNull(message = "是否显示不能为空", groups = UpdateGroup.class)
    @Max(value = 1, message = "请填写正确的显示类型", groups = UpdateGroup.class)
    @Min(value = 0, message = "请填写正确的显示类型", groups = UpdateGroup.class)
    private Integer showFlag;


}