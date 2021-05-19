/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articlerecommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资讯添加推荐列表DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "资讯添加推荐列表DTO")
public class CmsArticleAddRecommendDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章id")
    private Long articleId;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章所属")
    private String articleBelong;

    @ApiModelProperty(value = "文章作者")
    private String author;

    @ApiModelProperty(value = "分类ID")
    private Long acId;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "评论标识（0：停用 1：启用（默认））")
    private Integer commentFlag;

}