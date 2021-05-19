/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articleclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台分类分页管理列表DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "后台分类分页管理列表")
public class CmsClassPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String acName;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "分类图标")
    private String imageUrl;

    @ApiModelProperty(value = "分类icon")
    private String acIcon;

    @ApiModelProperty(value = "关注数")
    private Integer attentionNum;

    @ApiModelProperty(value = "文章统计数")
    private Integer articleNum;

    @ApiModelProperty(value = "浏览量")
    private Integer pvNum;
}
