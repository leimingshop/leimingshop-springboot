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
 * 圈子文章列表管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "圈子文章列表管理DTO")
public class CmsFrontCircleArticleListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "话题ID")
    private Long topicId;

    @ApiModelProperty(value = "话题名称")
    private String topicName;

    @ApiModelProperty(value = "点赞数")
    private Integer articlePraiseNum;

    @ApiModelProperty(value = "评论数")
    private Integer articleCommentNum;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "浏览数")
    private Integer pvNum;

    @ApiModelProperty(value = "位置")
    private String location;

    @ApiModelProperty(value = "精华帖标识（0：非精华帖（默认），1：精华帖）")
    private Integer essenceFlag;

    @ApiModelProperty(value = "文章区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;

    @ApiModelProperty(value = "排序规则（1:精华帖 2:我的帖子 3:点赞数 4:评论数）")
    private Integer sortRule;

    private Integer articleFlag;

    @ApiModelProperty(value = "视频URL")
    private String videoUrl;

    @ApiModelProperty(value = "稿件封面图")
    private String indexImage;

    @ApiModelProperty(value = "文章URL路径")
    private String articleUrl;

    @ApiModelProperty(value = "文章图片URL")
    private String articlePicture;

    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "文章作者id")
    private Long articleCreaterId;

    @ApiModelProperty(value = "点赞标识（1：已点赞 0：未点赞）")
    private Integer praiseFlag;

    @ApiModelProperty(value = "关注区分标识（1：关注用户；2：关注分类；3：文章点赞；4：评论点赞）")
    private Integer attentionFlag;

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

    @ApiModelProperty(value = "艾特记录的主键")
    private Long remindId;

    @ApiModelProperty(value = "艾特是否已读（1：已读 0：未读）")
    private Integer remindReadFlag;

}
