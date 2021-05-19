/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pixiaoyong
 * @date 2020/3/24 9:40
 * @email pixiaoyong@leimingtech.com
 */

@Data
@ApiModel(value = "分类管理列表")
public class CmsSimpleClassList {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 分类名
     */
    @NotNull(message = "分类名称不能为空")
    @ApiModelProperty(value = "分类名称")
    private String acName;

    /**
     * 分类icon
     */
    @ApiModelProperty(value = "分类icon")
    private String acIcon;


}
