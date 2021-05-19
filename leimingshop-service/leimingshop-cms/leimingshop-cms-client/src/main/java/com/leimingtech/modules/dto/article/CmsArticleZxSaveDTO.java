/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资讯新增展示类
 *
 * @author zhangyuhao
 */
@Data
@ApiModel(value = "资讯新增展示类")
public class CmsArticleZxSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类ID")
    private Long acId;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章短标题")
    private String articleShortTitle;

    @ApiModelProperty(value = "文章作者")
    private String author;

    @ApiModelProperty(value = "文章来源")
    private String articleSource;

    @ApiModelProperty(value = "URL路径")
    private String articleUrl;

    @ApiModelProperty(value = "文章引导图URL")
    private String articlePicture;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "文章摘要")
    private String articleAbstract;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "文章所属")
    private String articleBelong;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;

    @ApiModelProperty(value = "评论标识（0：停用 1：启用（默认））")
    private Integer commentFlag;

    @ApiModelProperty(value = "文章区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;

    @ApiModelProperty(value = "分类acIdpaths")
    private String[] acIdpaths;
}