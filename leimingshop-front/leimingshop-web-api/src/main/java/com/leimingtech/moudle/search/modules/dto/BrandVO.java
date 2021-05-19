/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 3:01 PM
 */
@ApiModel(value = "品牌信息")
@Data
public class BrandVO implements Serializable {

    private static final long serialVersionUID = -2451442252431295405L;
    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("品牌名称")
    private String brandName;
}
