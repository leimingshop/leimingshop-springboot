/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 文章分类表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "ArticleClassSaveDTO")
public class ArticleClassSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类标识码")
    private String acCode;

    @ApiModelProperty(value = "分类名称")
    @NotBlank(message = "分类名称不能为空", groups = AddGroup.class)
    private String acName;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long acParentId;

    @ApiModelProperty(value = "分类状态（0停用，默认为1启用）")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}