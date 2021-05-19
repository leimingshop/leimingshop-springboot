/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pixiaoyong
 * @date 2020/3/24 10:56
 * @email pixiaoyong@leimingtech.com
 */
@Data
@ApiModel(value = "圈子文章H5前台发布DTO")
public class CmsFrontArticlePublishDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "发布人用户ID")
    private Long articleCreaterId;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "位置")
    private String location;

    @ApiModelProperty(value = "话题ID")
    private Long topicId;

    @ApiModelProperty(value = "话题名称")
    private String topicName;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "文章区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;

    @ApiModelProperty(value = "文章图片URL")
    private String articlePicture;

    @ApiModelProperty(value = "视频URL")
    private String videoUrl;

    @ApiModelProperty(value = "所属板块/分类")
    private Long acId;

    /**
     * 圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）
     */
    @ApiModelProperty(value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）")
    private Integer articleFlag;

    /**
     * 稿件封面图
     */
    @ApiModelProperty(value = "稿件封面图")
    private String indexImage;


    /**
     * @的json数组[{"name":"","id":""}]
     */
    @ApiModelProperty(value = "@的json数组[{'name':'','id':''}]")
    private String remind;
}
