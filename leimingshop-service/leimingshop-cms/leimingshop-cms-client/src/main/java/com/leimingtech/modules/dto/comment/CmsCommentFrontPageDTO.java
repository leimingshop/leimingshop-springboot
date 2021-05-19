/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 前台评论分页管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "前台评论分页管理")
public class CmsCommentFrontPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 一条下级评论
     */
    CmsCommentFrontPageDTO commentFront;
    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "评论内容")
    private String commentContent;
    @ApiModelProperty(value = "评论人Id")
    private Long commentCreaterId;
    @ApiModelProperty(value = "被评论人Id")
    private Long commentedId;
    @ApiModelProperty(value = "被评论的人名称")
    private String commentedName;
    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "被评论的评论ID（0为一级评论）")
    private Long commentParentId;
    @ApiModelProperty(value = "评论时间")
    private Date createDate;
    @ApiModelProperty(value = "点赞标识（1：未点赞 0：已点赞）")
    private Integer praiseFlag;
    @ApiModelProperty(value = "用户标识（1：当前用户 0：其他用户）")
    private Integer memberFlag;
    @ApiModelProperty(value = "点赞数")
    private Integer praiseNum;
    @ApiModelProperty(value = "评论层级")
    private Integer commentLevel;
    @ApiModelProperty(value = "二级评论条数")
    private Integer commentSecondNum;

    /**
     * @的json数组[{"name":"","id":""}]
     */
    @ApiModelProperty(value = "@的json数组[{'name':'','id':''}]")
    private String remind;

    @ApiModelProperty(value = "评论图片")
    private String commentPicture;
}