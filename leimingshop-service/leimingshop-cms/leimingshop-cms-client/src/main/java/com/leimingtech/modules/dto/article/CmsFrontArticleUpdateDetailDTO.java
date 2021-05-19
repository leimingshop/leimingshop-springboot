/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 圈子文章前台修改详情管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "圈子文章前台修改详情管理")
public class CmsFrontArticleUpdateDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "分类id")
    private Long acId;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "话题id")
    private Long topicId;

    @ApiModelProperty(value = "话题名称")
    private String topicName;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "稿件封面图")
    private String indexImage;

    @ApiModelProperty(value = "位置")
    private String location;

    @ApiModelProperty(value = "圈人的json数组[{'name':'','id':''}]")
    private String remind;

    @ApiModelProperty(value = "是否举报（1：已举报 0：未举报）")
    private Integer reportFlag;
}
