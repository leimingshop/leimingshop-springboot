/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xuzhch
 * @className IndexOrderDTO
 * @description 首页订单数据
 * @date 2020/3/16/016
 */
@Data
public class IndexOrderDataDTO implements Serializable {
    private BigDecimal subOrderAmount;
    private Integer orderCount;
}
