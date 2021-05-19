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
 * 评论管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "评论管理")
public class CmsCommentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "评论时间")
    private Date createDate;

    @ApiModelProperty(value = "状态标识（0：屏蔽 1：展示（默认））")
    private Integer status;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章发布人ID")
    private Long articleCreaterId;

    @ApiModelProperty(value = "文章发布人名称")
    private String articleCreator;

    @ApiModelProperty(value = "评论图片")
    private String commentPicture;

}