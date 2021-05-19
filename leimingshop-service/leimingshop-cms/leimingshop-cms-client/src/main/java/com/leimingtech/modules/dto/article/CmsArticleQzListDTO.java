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
 * 文章管理列表DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "文章管理列表")
public class CmsArticleQzListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）")
    private Integer articleFlag;

    @ApiModelProperty(value = "圈子分类名称")
    private String acName;

    @ApiModelProperty(value = "圈子话题名称")
    private String topicName;

    @ApiModelProperty(value = "阅读量")
    private Integer pvNum;

    @ApiModelProperty(value = "点赞数")
    private Integer articlePraiseNum;

    @ApiModelProperty(value = "回复数")
    private Integer articleCommentNum;

    @ApiModelProperty(value = "分享数")
    private Integer shareNum;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;

    @ApiModelProperty(value = "精华帖标识（0：非精华帖（默认），1：精华帖）")
    private Integer essenceFlag;

    @ApiModelProperty(value = "审核标识（0：未审核（默认），1：审核通过，2：审核驳回）")
    private Integer audit;

}