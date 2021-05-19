/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.settle.ReturnBillDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 退货结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */

public interface ReturnBillService {
    /**
     * 退货结算分页
     *
     * @param params 查询条件
     * @return 返回退货结算分页信息
     */

    PageData<ReturnBillDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有
     *
     * @param params
     * @return
     */

    List<ReturnBillDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询退货结算详情
     *
     * @param id 主键id
     * @return 返回退货结算详情
     */

    ReturnBillDTO get(Long id);

    /**
     * 保存退货结算信息
     *
     * @param dto 保存实体
     */

    void save(@RequestBody ReturnBillDTO dto);

    /**
     * 更新退货结算信息
     *
     * @param dto 更新实体
     */

    void update(@RequestBody ReturnBillDTO dto);

    /**
     * 删除信息
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 批量保存退款订单
     *
     * @param returnBillDTOList
     */

    void saveBatch(@RequestBody List<ReturnBillDTO> returnBillDTOList);
}