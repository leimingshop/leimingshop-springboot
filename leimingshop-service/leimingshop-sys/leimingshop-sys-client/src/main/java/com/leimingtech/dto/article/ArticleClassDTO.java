/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 文章分类表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "ArticleClassDTO")
public class ArticleClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "分类标识码")
    private String acCode;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long acParentId;

    @ApiModelProperty(value = "分类状态（0停用，默认为1启用）")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}