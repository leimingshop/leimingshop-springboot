/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 圈子新增修改文章管理DTO
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2019-01-15
 */
@Data
@ApiModel(value = "圈子新增修改文章管理")
public class CmsArticleQzUpdateDTO extends CmsArticleQzSaveDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "分类ID")
    private Long acId;

    @ApiModelProperty(value = "圈子ID")
    private Long topicId;

    @ApiModelProperty(value = "圈子名称")
    private String topicName;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "精华帖标识（0：非精华帖（默认），1：精华帖）")
    private Integer essenceFlag;

    @ApiModelProperty(value = "文章区分标识（1：圈子2：资讯3：服务指南）")
    private Integer acCode;

}
