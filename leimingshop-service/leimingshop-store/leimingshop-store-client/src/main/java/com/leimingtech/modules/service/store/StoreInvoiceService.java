/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.store.StoreGoodsInvoiceSettingDTO;
import com.leimingtech.modules.dto.store.StoreInvoiceAddressDTO;
import com.leimingtech.modules.dto.store.StoreInvoiceDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺发票设置表
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-27
 */

public interface StoreInvoiceService {
    /**
     * 店铺发票分页信息
     *
     * @param params 查询参数
     * @return 返回发票分页信息
     */

    PageData<StoreInvoiceDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询发票信息
     *
     * @param params 查询参数
     * @return 返回发票信息
     */

    List<StoreInvoiceDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询发票详情
     *
     * @param id 主键id
     * @return 返回发票详情
     */

    StoreInvoiceDTO get(Long id);

    /**
     * 根据店铺查询发票详情
     *
     * @param storeId 店铺id
     * @return 返回店铺发票详情
     */

    StoreInvoiceDTO getInvoiceDetail(Long storeId);

    /**
     * 保存店铺发票
     *
     * @param dto 保存参数
     */

    void save(@RequestBody StoreInvoiceDTO dto);

    /**
     * 更新店铺发票
     *
     * @param dto 更新参数
     */

    void update(@RequestBody StoreInvoiceDTO dto);

    /**
     * 删除发票信息
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据店铺id 查询发票详情
     *
     * @param storeId 店铺id
     * @return 返回发票详情
     */

    StoreInvoiceAddressDTO getStoreInvoiceInfo(Long storeId);

    /**
     * 获取店铺开票设置信息
     *
     * @param storeId
     * @return StoreGoodsInvoiceSettingDTO 店铺开票设置信息
     * @author xuzhch
     * @date 2020年5月14日11:52:47
     */

    StoreGoodsInvoiceSettingDTO getInvoiceSetting(Long storeId);
}