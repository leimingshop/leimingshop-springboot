/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.leimingtech.modules.constants.CollectionName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单统计
 *
 * @author xuzhch
 * @date 2019年12月10日
 */

@Data
@Document(collection = CollectionName.ORDER_STATISTICS)
public class OrderStatisEntity {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单实付金额
     */
    private BigDecimal orderAmount;
    /**
     * 创建时间（日）
     */
    private Date createDayTime;
    /**
     * 创建时间（月）
     */
    private Date createMonthTime;
    /**
     * 创建时间
     */
    private Date createTime;

}
