/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @author anjun
 * @email anjun_314914423@126.com
 * @since 1.0.0 2019-05-14
 */
@Data
@ApiModel(description = "AreaAndRegionTreeDTO")
public class AreaAndRegionTreeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "索引ID")
    private Long id;
    @ApiModelProperty(value = "地区名称")
    private String areaName;
    @ApiModelProperty(value = "父节点id")
    private Long areaParentId;
    @ApiModelProperty(value = "地区深度，从1开始")
    private Integer areaDeep;
    @ApiModelProperty(value = "子地区集合")
    private List<AreaAndRegionTreeDTO> children;

}
