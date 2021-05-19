/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "ArticleSaveDTO")
public class ArticleSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空", groups = AddGroup.class)
    private String articleTitle;

    @ApiModelProperty(value = "分类id")
    @NotNull(message = "分类id不能为空", groups = AddGroup.class)
    private Long acId;

    @ApiModelProperty(value = "跳转链接")
    private String articleUrl;

    @ApiModelProperty(value = "内容")
    @NotBlank(message = "内容不能为空", groups = AddGroup.class)
    private String articleContent;

    @ApiModelProperty(value = "是否显示（0不显示，默认为1显示）")
    private Integer showFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}