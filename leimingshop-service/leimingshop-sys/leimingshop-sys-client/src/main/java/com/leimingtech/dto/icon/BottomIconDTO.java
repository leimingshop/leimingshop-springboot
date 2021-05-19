/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.icon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * app底部图片配置表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-9
 */
@Data
@ApiModel(description = "BottomIconDTO")
public class BottomIconDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "icon名称")
    private String menuName;

    @ApiModelProperty(value = "未选择图标")
    private String unselectedIcon;

    @ApiModelProperty(value = "已选择图标")
    private String selectedIcon;

    @ApiModelProperty(value = "按钮标识")
    private String iconCode;
}
