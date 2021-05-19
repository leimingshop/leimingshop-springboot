/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.modules.dto.order.OrderGoodsConfirmDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单商品确定服务实现类
 *
 * @author LX lixiangx@leimingtech.com
 * @since v1.0.0 2019-06-22
 */

public interface OrderGoodsConfirmService {

    /**
     * 查询订单商品确认集合
     *
     * @param params: 封装的查询条件
     * @return 订单商品确认信息集合
     * @date 2019/6/22 14:08
     * @author LX lixiangx@leimingtech.com
     **/

    List<OrderGoodsConfirmDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询订单商品确认信息
     *
     * @param id: 主键
     * @return 订单商品确认信息
     * @date 2019/6/22 14:10
     * @author LX lixiangx@leimingtech.com
     **/

    OrderGoodsConfirmDTO findById(String id);

    /**
     * 保存订单商品确认信息
     *
     * @param dto: 订单商品确认实体
     * @date 2019/6/22 14:11
     * @author LX lixiangx@leimingtech.com
     **/

    void save(@RequestBody OrderGoodsConfirmDTO dto);

    /**
     * 批量保存订单商品确认数据
     *
     * @param dtoList: 订单商品确认数据集合
     * @date 2019/6/22 16:27
     * @author LX lixiangx@leimingtech.com
     **/

    void saveList(@RequestBody List<OrderGoodsConfirmDTO> dtoList);

    /**
     * 修改订单商品确认信息
     *
     * @param dto: 订单商品确认实体
     * @date 2019/6/22 14:11
     * @author LX lixiangx@leimingtech.com
     **/

    void update(@RequestBody OrderGoodsConfirmDTO dto);

    /**
     * 批量删除
     *
     * @param ids: 批量删除主键ID集合
     * @date 2019/6/22 14:11
     * @author LX lixiangx@leimingtech.com
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * 根据订单确认ID查询确认订单商品数据
     *
     * @param orderConfrimId: 订单确认表ID
     * @return 订单商品确认集合
     * @date 2019/6/22 17:31
     * @author LX lixiangx@leimingtech.com
     **/

    List<OrderGoodsConfirmDTO> findListByOrderConfirmId(Long orderConfrimId);
}
