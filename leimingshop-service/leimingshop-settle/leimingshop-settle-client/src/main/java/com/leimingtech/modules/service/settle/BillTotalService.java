/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.settle.BillTotalDTO;
import com.leimingtech.modules.dto.settle.BillTotalInfoDTO;
import com.leimingtech.modules.dto.settle.StoreConfirmBillTotalDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 对账汇总表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */

public interface BillTotalService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<BillTotalDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有
     *
     * @param params
     * @return
     */

    List<BillTotalDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */

    BillTotalDTO get(Long id);

    /**
     * 对账详情
     *
     * @param id
     * @return
     */

    BillTotalInfoDTO info(@RequestParam("id") Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody BillTotalDTO dto);

    /**
     * 修改
     *
     * @param dto
     */

    void update(@RequestBody BillTotalDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);


    /**
     * 定时生成对账
     */

    void timeBill();

    /**
     * 店铺备注和确认
     *
     * @param storeConfirmBillTotalDTO
     * @param userName
     */

    void updateStatus(@RequestBody StoreConfirmBillTotalDTO storeConfirmBillTotalDTO,
                      @RequestParam("userName") String userName);
}