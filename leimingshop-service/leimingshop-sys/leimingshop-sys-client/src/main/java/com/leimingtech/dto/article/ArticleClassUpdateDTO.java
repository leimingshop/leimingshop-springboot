/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 文章分类表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "ArticleClassUpdateDTO")
public class ArticleClassUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "分类名称")
    @NotBlank(message = "分类名称不能为空", groups = UpdateGroup.class)
    private String acName;

    @ApiModelProperty(value = "分类状态（0停用，默认为1启用）")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}