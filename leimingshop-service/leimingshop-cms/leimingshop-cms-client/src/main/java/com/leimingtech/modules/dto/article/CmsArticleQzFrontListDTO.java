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
 * 圈子文章前台列表管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "圈子文章前台列表管理")
public class CmsArticleQzFrontListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "圈子ID")
    private Long topicId;

    @ApiModelProperty(value = "圈子名称")
    private String topicName;

    @ApiModelProperty(value = "点赞数")
    private Integer articlePraiseNum;

    @ApiModelProperty(value = "评论数")
    private Integer articleCommentNum;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "精华帖标识（0：非精华帖（默认），1：精华帖）")
    private Integer essenceFlag;

    @ApiModelProperty(value = "文章区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;

    @ApiModelProperty(value = "排序规则（1:精华帖 2:我的帖子 3:点赞数 4:评论数）")
    private Integer sortRule;

}
