/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tyl
 * @version 1.0
 * @date 2019/7/9 10:46 AM
 */
@ApiModel(description = "GoodsDetailsSearchDTO")
@Data
public class GoodsDetailsSearchDTO {

    @ApiModelProperty(value = "规格id", required = true)
    private Long specId;

    @ApiModelProperty(value = "商品ID", required = false)
    private Long goodsId;
}
