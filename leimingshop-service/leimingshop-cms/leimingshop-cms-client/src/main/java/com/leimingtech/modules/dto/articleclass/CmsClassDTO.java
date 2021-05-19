/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分类管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "分类管理")
public class CmsClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotNull(message = "分类名称不能为空")
    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "父ID(0:一级分类)")
    private Long acParentId;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;

    @NotNull(message = "分类区分标识不能为空")
    @ApiModelProperty(value = "分类区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;

    @ApiModelProperty(value = "分类图标")
    private String imageUrl;

    @ApiModelProperty(value = "分类icon")
    private String acIcon;

}
