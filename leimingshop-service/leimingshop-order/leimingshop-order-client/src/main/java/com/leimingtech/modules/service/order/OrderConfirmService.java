/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.modules.dto.order.OrderConfirmDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单确定服务接口
 *
 * @author LX lixiangx@leimingtech.com
 * @since v1.0.0 2019-06-22
 */

public interface OrderConfirmService {

    /**
     * 列表查询
     *
     * @param params 参数
     * @return 数据集合
     * @author LX lixiangx@leimingtech.com
     */

    List<OrderConfirmDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询订单确认实体
     *
     * @param id 主键ID
     * @return 订单确认实体
     * @author LX lixiangx@leimingtech.com
     **/

    OrderConfirmDTO findById(Long id);

    /**
     * 保存订单确认信息
     *
     * @param dto: 订单确认DTO
     * @author LX  lixiangx@leimingtech.com
     **/

    void save(@RequestBody OrderConfirmDTO dto);

    /**
     * 修改订单确认信息
     *
     * @param dto: 订单确认DTO
     * @author LX lixiangx@leimingtech.com
     **/

    void update(@RequestBody OrderConfirmDTO dto);

    /**
     * 批量删除订单确认信息
     *
     * @param ids: ID集合
     * @date 2019/6/22 14:05
     * @author LX lixiangx@leimingtech.com
     **/

    void delete(@RequestBody Long[] ids);
}
