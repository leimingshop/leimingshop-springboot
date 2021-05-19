/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@ApiModel(description = "ArticleDTO")
public class ArticleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "索引id")
    private Long id;

    @ApiModelProperty(value = "分类id")
    private Long acId;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "跳转链接")
    private String articleUrl;

    @ApiModelProperty(value = "标题")
    private String articleTitle;

    @ApiModelProperty(value = "内容")
    private String articleContent;

    @ApiModelProperty(value = "是否显示（0不显示，默认为1显示）")
    private Integer showFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}