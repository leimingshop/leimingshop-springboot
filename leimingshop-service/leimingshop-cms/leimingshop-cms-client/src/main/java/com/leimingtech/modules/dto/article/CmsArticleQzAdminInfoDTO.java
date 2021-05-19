/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 圈子文章信息DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "圈子文章信息")
public class CmsArticleQzAdminInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）")
    private Integer articleFlag;

    @ApiModelProperty(value = "圈子分类名称")
    private String acName;

    @ApiModelProperty(value = "圈子话题名称")
    private String topicName;

    @ApiModelProperty(value = "视频URL")
    private String videoUrl;

    @ApiModelProperty(value = "文章图片URL")
    private String articlePicture;

    @ApiModelProperty(value = "稿件封面图")
    private String indexImage;

}