/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.settle.OrderBillDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */

public interface OrderBillService {
    /**
     * 订单结算分页查询
     *
     * @param params 查询参数
     * @return 返回 订单结算分页信息
     */

    PageData<OrderBillDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询订单结算信息
     *
     * @param params 查询 参数
     * @return 返回订单 结算信息
     */

    List<OrderBillDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据 ID查询 订单结算详情
     *
     * @param id 主键ID
     * @return 返回订单结算 详情
     */

    OrderBillDTO get(Long id);

    /**
     * 保存订单结算信息
     *
     * @param dto 保存实体
     */

    void save(@RequestBody OrderBillDTO dto);

    /**
     * 跟新订单 结算
     *
     * @param dto 更新实体
     */

    void update(@RequestBody OrderBillDTO dto);

    /**
     * 删除订单结算信息
     *
     * @param ids 主键信息
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 批量保存结算订单
     *
     * @param orderBillDTOList
     */

    void saveBatch(@RequestBody List<OrderBillDTO> orderBillDTOList);
}