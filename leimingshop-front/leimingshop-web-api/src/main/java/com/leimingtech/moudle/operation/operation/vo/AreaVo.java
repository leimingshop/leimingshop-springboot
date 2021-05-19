/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 地区信息
 *
 * @author xuzhch
 * @since 1.0.0 2019-05-14
 */
@Data
@ApiModel(description = "AreaVo")
public class AreaVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "索引ID")
    private Long id;
    @ApiModelProperty(value = "地区名称")
    private String areaName;
    @ApiModelProperty(value = "父节点id")
    private Long areaParentId;
    @ApiModelProperty(value = "子级数量（无子级为0）")
    private Integer count;

}