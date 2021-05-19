/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.traffic;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 浏览量保存对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@ApiModel(description = "PvStatisticShowDTO")
public class PvStatisticShowDTO implements Serializable {

    @ApiModelProperty(value = "pv,浏览量")
    private Integer pv;

    @ApiModelProperty(value = "uv，访问量")
    private Integer uv;

    public PvStatisticShowDTO() {

    }

    public PvStatisticShowDTO(Integer pv, Integer uv) {
        this.pv = pv;
        this.uv = uv;
    }

}
