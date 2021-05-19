/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GoodsShowDTO
 * @Description 根据商品标签查询实体
 * @Author DY
 * @Date 2019/7/16 20:26
 * @Version 1.0
 **/
@Data
@ApiModel(description = "GoodsLabelSelectDTO")
public class GoodsLabelSelectDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "店铺数量")
    private Integer storeNum;
}
