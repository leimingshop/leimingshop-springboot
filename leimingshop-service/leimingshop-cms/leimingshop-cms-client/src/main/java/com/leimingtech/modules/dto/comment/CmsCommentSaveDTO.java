/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangyuhao
 * @email 2625024471@qq.com
 * @date 2020/1/3 11:29
 */
@Data
@ApiModel(value = "新增评论表单")
public class CmsCommentSaveDTO {

    @NotNull(message = "被评论文章ID不能为空")
    @ApiModelProperty(value = "被评论文章ID")
    private Long articleId;

    @NotNull(message = "评论内容不能为空")
    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "评论图片url")
    private String commentPicture;

    @NotNull(message = "被评论的评论ID不能为空")
    @ApiModelProperty(value = "被评论的评论ID（0为一级评论）")
    private Long commentParentId;

    @NotNull(message = "评论一级ID不能为空")
    @ApiModelProperty(value = "评论一级ID（0为一级评论）")
    private Long commentFirstId;

    @NotNull(message = "评论层级不能为空")
    @ApiModelProperty(value = "评论层级")
    private Integer commentLevel;

    @ApiModelProperty(value = "分类区分标识（1：圈子2：资讯）(后台默认)")
    private Integer acCode;

    @ApiModelProperty(value = "评论人ID(后台获取)")
    private Long commentCreaterId;

    @ApiModelProperty(value = "创建者(后台获取)")
    private String creator;

    @ApiModelProperty(value = "更新人(后台获取)")
    private String updater;

    @ApiModelProperty(value = "被评论人ID(0为一级评论)")
    private Long commentedId;

    /**
     * @的json数组[{"name":"","id":""}]
     */
    @ApiModelProperty(value = "@的json数组[{'name':'','id':''}]")
    private String remind;
}
