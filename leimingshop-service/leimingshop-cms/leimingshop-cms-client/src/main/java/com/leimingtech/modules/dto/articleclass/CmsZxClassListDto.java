/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import com.leimingtech.commons.tools.utils.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhangyuhao
 * @email 2625024471@qq.com
 * @date 2020/1/7 17:18
 */

@Data
@ApiModel(value = "资讯分类层级展示对象")
public class CmsZxClassListDto extends TreeNode {
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "分类名")
    private String acName;
    @ApiModelProperty(value = "父级ID")
    private Long acParentId;
    @ApiModelProperty(value = "所属层级")
    private Integer acLevel;
    @ApiModelProperty(value = "IDPATH")
    private String acIdpaths;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;
    @ApiModelProperty(value = "子集分类")
    private List<CmsZxClassListDto> cmsZxClassListDto;
}
