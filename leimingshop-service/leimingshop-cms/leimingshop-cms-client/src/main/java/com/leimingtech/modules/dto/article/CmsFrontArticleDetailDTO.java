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
 * 圈子文章前台详情管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "圈子文章前台详情管理")
public class CmsFrontArticleDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章id")
    private Long id;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "创建人id")
    private Long articleCreaterId;

    @ApiModelProperty(value = "话题ID")
    private Long topicId;

    @ApiModelProperty(value = "话题名称")
    private String topicName;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "文章区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;


    @ApiModelProperty(value = "位置")
    private String location;

    /**
     * 文章图片URL
     */
    @ApiModelProperty(value = "文章图片URL")
    private String articlePicture;

    /**
     * 评论数
     */
    @ApiModelProperty(value = "评论数")
    private Integer articleCommentNum;
    /**
     * 浏览数
     */
    @ApiModelProperty(value = "浏览数")
    private Integer pvNum;

    @ApiModelProperty(value = "点赞数")
    private Integer articlePraiseNum;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date auditDate;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createDate;


    /**
     * 稿件封面图
     */
    @ApiModelProperty(value = "稿件封面图")
    private String indexImage;

    @ApiModelProperty(value = "视频URL")
    private String videoUrl;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）
     */
    @ApiModelProperty(value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）")
    private Integer articleFlag;

    @ApiModelProperty(value = "点赞标识（1：已点赞 0：未点赞）")
    private Integer praiseFlag;

    @ApiModelProperty(value = "是否关注（1：已关注 0：未关注）")
    private Integer concernedFlag;

    @ApiModelProperty(value = "是否举报（1：已举报 0：未举报）")
    private Integer reportFlag;

    @ApiModelProperty(value = "是否为当前登录用户（1：当前登录用户 0：其他用户）")
    private Integer loginFlag;

    /**
     * @的json数组[{"name":"","id":""}]
     */
    @ApiModelProperty(value = "@的json数组[{'name':'','id':''}]")
    private String remind;

}
