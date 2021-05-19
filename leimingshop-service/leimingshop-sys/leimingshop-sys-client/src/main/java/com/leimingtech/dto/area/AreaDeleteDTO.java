/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description = "AreaDeleteDTO")
public class AreaDeleteDTO implements Serializable {

    @ApiModelProperty(value = "索引ID")
    private Long id;

}
