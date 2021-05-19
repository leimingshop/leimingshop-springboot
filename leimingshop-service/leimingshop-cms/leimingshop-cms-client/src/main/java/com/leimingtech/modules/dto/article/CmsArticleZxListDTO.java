/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资讯文章管理列表DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "资讯文章管理列表")
public class CmsArticleZxListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章所属")
    private String articleBelong;

    @ApiModelProperty(value = "文章作者")
    private String author;

    @ApiModelProperty(value = "文章来源")
    private String articleSource;

    @ApiModelProperty(value = "分类ID")
    private Long acId;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "URL路径")
    private String articleUrl;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "置顶标识（0：未置顶（默认），1：已置顶）")
    private Integer topFlag;

    @ApiModelProperty(value = "头条标识（0：未头条（默认），1：已头条）")
    private Integer headFlag;

    @ApiModelProperty(value = "审核标识（0：未审核（默认），1：审核通过，2：审核驳回）")
    private Integer audit;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;

    @ApiModelProperty(value = "评论标识（0：停用 1：启用（默认））")
    private Integer commentFlag;

    @ApiModelProperty(value = "文章区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}