/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xuzhch
 * @className IndexStoreSellRakingDTO
 * @description 首页店铺销量排名数据
 * @date 2020/3/17/017
 */
@Data
@ApiModel(description = "IndexStoreSellRakingDTO")
public class IndexStoreSellRakingDTO implements Serializable {
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "销售订单数量")
    private Integer sellOrderCount;
    @ApiModelProperty(value = "销售订单金额")
    private BigDecimal sellOrderAmount;

}
