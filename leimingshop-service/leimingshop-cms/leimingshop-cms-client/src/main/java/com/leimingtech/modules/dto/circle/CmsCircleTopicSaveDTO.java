/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.circle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 话题新增管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "话题新增管理")
public class CmsCircleTopicSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "创建人id")
    private Long createrId;

    @ApiModelProperty(value = "话题名称")
    private String topicName;

    @ApiModelProperty(value = "所属分类ID")
    private Long acId;

    @ApiModelProperty(value = "话题描述")
    private String topicAbstract;

    @ApiModelProperty(value = "话题logo")
    private String topicLogo;

    @ApiModelProperty(value = "话题底图")
    private String topicPicture;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "超话标识（0：普话（默认），1：超话）")
    private Integer superTopicFlag;

    @ApiModelProperty(value = "状态标识（0：停用 1：启用（默认））")
    private Integer status;


}
