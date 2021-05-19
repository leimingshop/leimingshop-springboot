/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.modules.dto.order.OrderSourceStatisticDTO;
import com.leimingtech.modules.dto.order.OrderStatisDTO;
import com.leimingtech.modules.entity.order.TransactionStatisEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单统计DB查询接口
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Mapper
public interface OrderStatisDao {
    /**
     * 查询交易统计数据
     *
     * @param storeIds 店铺ID集合
     * @param date     日期
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<TransactionStatisEntity> transactionData(@Param("storeIds") List<Long> storeIds, @Param("date") String date);


    /**
     * 查询单个商品购买人数
     *
     * @param storeIds 店铺ID
     * @param date     日期
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<Map<String, Object>> selectPaymentGoodsNumber(@Param("storeIds") List<Long> storeIds, @Param("date") String date);

    /**
     * 查询订单销量
     *
     * @param date 日期
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<OrderStatisDTO> selectOrderByDate(@Param("date") String date);

    /**
     * 查询不通来源下订单销量
     *
     * @param dateStr 日期字符串
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<OrderSourceStatisticDTO> selectOrderSourceByDate(String dateStr);
}
