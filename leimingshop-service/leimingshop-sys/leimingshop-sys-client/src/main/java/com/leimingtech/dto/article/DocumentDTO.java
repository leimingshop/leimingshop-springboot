/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 系统文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "DocumentDTO")
public class DocumentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "文章分类ID")
    private Long acId;

    @ApiModelProperty(value = "标识码")
    private String docCode;

    @ApiModelProperty(value = "标题")
    private String docTitle;

    @ApiModelProperty(value = "内容")
    private String docContent;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;
}