/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.modules.dto.order.OrderActivityDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单活动表
 *
 * @author weixianchun@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface OrderActivityService {

    /**
     * 功能描述:
     * (列表查询-订单活动关联信息)
     *
     * @param params 可变参数
     * @return 列表list
     * @date 2020/1/3 14:31
     * @author weixianchun
     **/

    List<OrderActivityDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * (根据id查询订单活动关联信息)
     *
     * @param id
     * @return
     * @date 2020/1/3 14:32
     * @author weixianchun
     **/

    OrderActivityDTO get(Long id);

    /**
     * 功能描述：
     * <根据订单id集合查询订单活动集合>
     *
     * @param orderIds 订单id集合
     * @return
     * @date 2020/2/24 13:34
     * @author 刘远杰
     **/

    List<OrderActivityDTO> getByOrderIds(@RequestBody List<Long> orderIds);

    /**
     * 功能描述：
     * <根据订单id查询订单活动集合>
     *
     * @param orderId 订单id
     * @return
     * @date 2020/2/24 13:34
     * @author 刘远杰
     **/

    List<OrderActivityDTO> getByOrderId(Long orderId);

    /**
     * 功能描述:
     * (保存订单活动信息)
     *
     * @param dto
     * @return
     * @date 2020/1/3 14:33
     * @author weixianchun
     **/

    void save(@RequestBody OrderActivityDTO dto);

    /**
     * 功能描述:
     * (修改订单活动信息)
     *
     * @param dto
     * @return
     * @date 2020/1/3 14:33
     * @author weixianchun
     **/

    void update(@RequestBody OrderActivityDTO dto);

    /**
     * 功能描述:
     * (根据ids删除订单活动信息)
     *
     * @param ids
     * @return
     * @date 2020/1/3 14:33
     * @author weixianchun
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * (下单数量查询)
     *
     * @param activityType 活动类型 1优惠券 2满减
     * @param activityId   活动id
     * @return 下单数量
     * @date 2020/1/3 14:15
     * @author weixianchun
     **/

    int findOrderNum(@RequestParam("activityType") int activityType, @RequestParam("activityId") Long activityId);

    /**
     * 功能描述:
     * 〈批量保存订单活动〉
     *
     * @param dtoList 订单活动
     * @author : 刘远杰
     */

    Boolean saveBatch(@RequestBody List<OrderActivityDTO> dtoList);
}
