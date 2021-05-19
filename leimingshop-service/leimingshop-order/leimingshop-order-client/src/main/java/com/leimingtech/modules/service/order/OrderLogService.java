/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.modules.dto.order.OrderLogDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 订单状态记录管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */

public interface OrderLogService {

    /**
     * 功能描述:
     * 〈保存订单操作日志〉
     *
     * @param dto 订单操作日志
     * @author : 刘远杰
     */

    void saveOrderLog(@RequestBody OrderLogDTO dto);

    /**
     * 功能描述:
     * 〈根据订单日志id查询订单日志详情〉
     *
     * @param id 订单日志id
     * @author : 刘远杰
     */

    OrderLogDTO get(Long id);

    /**
     * 功能描述:
     * 〈根据订单id查询订单日志详情〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    List<OrderLogDTO> getByOrderId(Long orderId);


    void deleteByOrderId(Long orderId);

}
