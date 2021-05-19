/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.complain;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.complain.OrderComplainDTO;
import com.leimingtech.modules.dto.complain.OrderComplainDetailDTO;
import com.leimingtech.modules.dto.complain.OrderComplainPageDTO;
import com.leimingtech.modules.dto.complain.SaveOrderComplainDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单投诉表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */

public interface OrderComplainService {
    /**
     * H5投诉分页
     *
     * @param params
     * @return
     */

    PageData<OrderComplainPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * admin 端投诉分页
     *
     * @param params
     * @return
     */

    PageData<OrderComplainDTO> adminPage(@RequestParam Map<String, Object> params);

    /**
     * 条件查询所有信息
     *
     * @param params
     * @return
     */

    List<OrderComplainDetailDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 投诉详情
     *
     * @param id
     * @return
     */

    OrderComplainDTO get(Long id);

    /**
     * 新增投诉
     *
     * @param dto
     */

    void save(@RequestBody SaveOrderComplainDTO dto);

    /**
     * 修改
     *
     * @param dto
     */

    void update(@RequestBody OrderComplainDTO dto);

    /**
     * 用户删除投诉
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * admin投诉详情
     *
     * @param id
     * @return
     */

    OrderComplainDetailDTO info(Long id);

    /**
     * 根据订单ID 查询投诉详情
     *
     * @param orderId
     * @param memberId
     */

    OrderComplainDTO orderComInfo(@RequestParam("orderId") Long orderId,
                                  @RequestParam("memberId") Long memberId);
}
