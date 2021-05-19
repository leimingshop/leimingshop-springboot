/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xuzhch
 * @className OrderSourceStatisticDTO
 * @description 订单来源统计DTO
 * @date 2020/3/12/012
 */
@Data
public class OrderSourceStatisticDTO {
    private Long id;
    private Integer orderSource;
    private Integer currentBuyerCount;
    private Double buyerUptrend;
    private BigDecimal currentOrderAmount;
    private Integer lastMonthBuyerCount;
    private BigDecimal lastMonthOrderAmount;
    private Double orderAmountUptrend;

    private Date createTime;
    private Date createMonthTime;
    private Date lastMonthTime;

    public static OrderSourceStatisticDTO newOrderSourceStatisticDTO() {
        OrderSourceStatisticDTO orderSourceStatisticDTO = new OrderSourceStatisticDTO();
        orderSourceStatisticDTO.setCurrentBuyerCount(0);
        orderSourceStatisticDTO.setBuyerUptrend(0.0);
        orderSourceStatisticDTO.setCurrentOrderAmount(BigDecimal.ZERO);
        orderSourceStatisticDTO.setLastMonthBuyerCount(0);
        orderSourceStatisticDTO.setLastMonthOrderAmount(BigDecimal.ZERO);
        orderSourceStatisticDTO.setOrderAmountUptrend(0.0);
        return orderSourceStatisticDTO;
    }
}
