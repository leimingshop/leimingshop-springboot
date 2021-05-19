/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.order.LastestOrderLogisticsDTO;
import com.leimingtech.modules.dto.order.OrderLogisticsDTO;
import com.leimingtech.modules.dto.order.OrderLogisticsLogDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单物流消息记录管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */

public interface OrderLogisticsLogService {

    /**
     * 功能描述:
     * 〈物流消息记录分页〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    PageData<OrderLogisticsLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据物流消息主键查询物流消息记录〉
     *
     * @param id 物流消息主键id
     * @author : 刘远杰
     */

    OrderLogisticsLogDTO get(Long id);

    /**
     * 功能描述:
     * 〈根据orderId查询物流消息记录〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    List<OrderLogisticsLogDTO> getByOrderId(Long orderId,
                                            @RequestParam(value = "buyerId", required = false) Long buyerId);

    /**
     * 功能描述:
     * 〈根据shipmentNumber查询物流消息记录〉
     *
     * @param shipmentNumber 物流单号
     * @author : 刘远杰
     */

    List<OrderLogisticsLogDTO> getByShipmentNumber(String shipmentNumber);

    /**
     * 功能描述:
     * 〈根据主键删除物流消息记录〉
     *
     * @param id 物流消息主键id
     * @author : 刘远杰
     */

    boolean deleteById(Long id);

    /**
     * 功能描述:
     * 〈根据orderId删除物流消息记录〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    int deleteByOrderId(Long orderId);

    /**
     * 功能描述:
     * 〈根据shipmentNumber删除物流消息记录〉
     *
     * @param shipmentNumber 订单id
     * @author : 刘远杰
     */

    int deleteByShipmentNumber(String shipmentNumber);

    /**
     * 功能描述:
     * 〈保存订单物流消息记录〉
     *
     * @param dto 订单物流消息
     * @author : 刘远杰
     */

    void saveOrderLogisticsLogService(@RequestBody OrderLogisticsLogDTO dto);

    /**
     * 根据用户id查询最新一条物流信息
     *
     * @param id
     * @return
     */

    LastestOrderLogisticsDTO findNewOrderLogisticsLog(Long id);

    /**
     * 根据物流单号从mongodb中查询物流进度
     *
     * @return
     */

    OrderLogisticsDTO findLogistics(String orderId);


}
