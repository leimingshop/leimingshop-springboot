/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.modules.dto.goods.GoodsStatisDTO;
import com.leimingtech.modules.dto.order.OrderSourceStatisticDTO;
import com.leimingtech.modules.dto.order.SevenTransactionStatisShowDTO;
import com.leimingtech.modules.dto.order.TransactionStatisShowDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author xuzhch
 * @version 1.0
 * @className OrderStatisService
 * @description 交易统计接口
 * @date 2019年12月6日
 **/


public interface OrderStatisService {

    /**
     * 订单来源数据展示
     *
     * @param params 查询条件
     * @return map
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<Integer, OrderSourceStatisticDTO> selectOrderSource(@RequestParam Map params);

    /**
     * 交易订单信息统计（交易统计，订单统计保存）
     *
     * @param params 日期 参数格式为  url?params=yyyy-MM-dd
     * @throws ParseException 日期转换异常
     * @author xuzhch 2019年12月17日17:07
     */

    void saveTransactionStatis(@RequestParam(required = false) String params) throws ParseException;

    /**
     * 近七日交易数据统计
     *
     * @param storeId 店铺ID  非必传
     * @return 近七天的销售统计
     * @author xuzhch 2019年12月17日17:11
     */

    Map<String, SevenTransactionStatisShowDTO> sevenDayStatis(@RequestParam(required = false, name = "storeId") Long storeId);

    /**
     * 交易统计数据展示
     *
     * @param params Map {
     *               (name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间)"
     *               (name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date" ),
     *               (name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date" )
     *               }
     * @return 交易统计展示DTO
     * @author xuzhch 2019年12月17日17:09
     */

    TransactionStatisShowDTO transactionStatisByParams(@RequestParam Map params);

    /**
     * 单人消费数据统计展示
     *
     * @param params Map {
     *               (name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间)"
     *               (name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date" ),
     *               (name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date" )
     *               }
     * @return 每个消费区间的 消费人数
     * @author xuzhch 2019年12月17日17:12
     */

    Map<String, Integer> singleConsumerStatis(@RequestParam Map params);

    /**
     * 商品销量统计
     *
     * @param date 日期
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<GoodsStatisDTO> goodsStatisSales(@RequestParam(name = "date") String date);
}
