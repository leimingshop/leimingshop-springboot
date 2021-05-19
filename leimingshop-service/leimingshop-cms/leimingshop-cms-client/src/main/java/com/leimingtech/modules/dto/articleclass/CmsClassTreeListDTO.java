/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import com.leimingtech.commons.tools.utils.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台分类管理列表DTO
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2020-1-10
 */
@Data
@ApiModel(value = "分类管理列表")
public class CmsClassTreeListDTO extends TreeNode {

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "分类层级")
    private Integer acLevel;

    @ApiModelProperty(value = "分类idpaths")
    private String acIdpaths;

}
