/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xuzhch
 * @className OrderSourceEntity
 * @description 订单来源统计数据
 * @date 2020/3/12/012
 */
@Data
public class OrderSourceEntity {
    private Long memberId;
    private Integer buyerCount;
    private BigDecimal orderAmount;
    private Integer orderSource;

}
