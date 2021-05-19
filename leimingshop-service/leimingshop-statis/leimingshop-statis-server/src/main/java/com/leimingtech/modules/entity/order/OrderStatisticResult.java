/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.leimingtech.modules.constants.CollectionName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author xuzhch
 * @className OrderStatisticResult
 * @description 交易统计查询结果
 * @date 2020/2/25/025
 */
@Data
@Document(collection = CollectionName.TRANSACTION_STATISTICS)
public class OrderStatisticResult {


    @ApiModelProperty(value = "订单数")
    private Integer orderNumber;

    @ApiModelProperty(value = "下单件数")
    private Integer orderGoodsNumber;

    @ApiModelProperty(value = "下单金额")
    private BigDecimal orderAmount;
}
