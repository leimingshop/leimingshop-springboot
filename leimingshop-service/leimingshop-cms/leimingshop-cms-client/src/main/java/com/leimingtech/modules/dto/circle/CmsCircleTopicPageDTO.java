/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.circle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 圈子话题管理列表DTO
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2020-01-06
 */
@Data
@ApiModel(value = "圈子话题管理列表")
public class CmsCircleTopicPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "话题名称")
    private String topicName;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "话题logo")
    private String topicLogo;

    @ApiModelProperty(value = "文章统计数")
    private Integer articleNum;

    @ApiModelProperty(value = "浏览量")
    private Integer pvNum;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "超话标识（0：普话（默认），1：超话）")
    private Integer superTopicFlag;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;

}
