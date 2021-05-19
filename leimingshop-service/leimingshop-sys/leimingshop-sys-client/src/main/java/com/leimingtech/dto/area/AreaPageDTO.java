/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author chengqian
 * @since 1.0.0 2020-4-21
 */
@Data
@ApiModel(description = "地区分页")
public class AreaPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "索引ID")
    private Long id;
    @ApiModelProperty(value = "地区名称")
    private String areaName;
    @ApiModelProperty(value = "父节点id")
    private Long areaParentId;
    @ApiModelProperty(value = "地区深度，从1开始")
    private Integer areaDeep;
    @ApiModelProperty(value = "大区Id")
    private Long regionId;
    @ApiModelProperty(value = "大区名称")
    private String regionName;


}