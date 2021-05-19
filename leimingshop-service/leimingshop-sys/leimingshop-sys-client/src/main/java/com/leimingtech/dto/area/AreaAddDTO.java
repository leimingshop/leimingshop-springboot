/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 新增地区DTO
 *
 * @author anjun
 * @email anjun_314914423@126.com
 * @since 1.0.0 2019-05-14
 */
@Data
@ApiModel(description = "AreaAddDTO")
public class AreaAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "地区名称")
    @NotBlank(message = "不能为空", groups = AddGroup.class)
    @NotNull(message = "不能为null", groups = AddGroup.class)
    private String areaName;

    @ApiModelProperty(value = "父节点id")
    private Long areaParentId;
    @ApiModelProperty(value = "所有大区ID")
    private Long regionId;

    @ApiModelProperty(value = "排序")
    private Integer areaSort;

    @ApiModelProperty(value = "地区深度")
    @DecimalMin("1")
    private Integer areaDeep;

}