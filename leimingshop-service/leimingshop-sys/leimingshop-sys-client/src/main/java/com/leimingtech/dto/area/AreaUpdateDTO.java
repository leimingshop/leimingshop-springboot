/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel(description = "AreaUpdateDTO")
public class AreaUpdateDTO implements Serializable {


    @ApiModelProperty(value = "索引ID")
    @NotNull(message = "id不能为空", groups = AddGroup.class)
    private Long id;

    @ApiModelProperty(value = "地区名称")
    @NotNull(message = "地区名称不能为空", groups = AddGroup.class)
    private String areaName;

    @ApiModelProperty(value = "父节点id")
    private String areaParentId;

    @ApiModelProperty(value = "排序")
    private Integer areaSort;

    @ApiModelProperty(value = "地区深度，从1开始")
    @DecimalMin("1")
    private Integer areaDeep;

}
