/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.store.StoreAuthAuditDTO;
import com.leimingtech.modules.dto.store.StoreAuthAuditPageDTO;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺审核表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-20
 */

public interface StoreAuthAuditService {
    /**
     * 分页信息
     *
     * @param params
     * @return
     */

    PageData<StoreAuthAuditPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有，
     *
     * @param params
     * @returnd
     */

    List<StoreAuthAuditDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */

    StoreAuthAuditDTO get(Long id);

    /**
     * 保存信息
     *
     * @param dto
     */

    void save(@RequestBody StoreAuthAuditDTO dto);

    /**
     * 更改信息
     *
     * @param dto
     */

    void update(@RequestBody StoreAuthAuditDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 店铺公司信息回显
     *
     * @param storeId    店铺ID
     * @param createDate 申请时间
     * @return
     */

    StoreAuthAuditDTO getInfoByStoreId(@RequestParam("storeId") Long storeId,
                                       @RequestParam(value = "createDate", required = false) String createDate);

    /**
     * 更新店铺公司审核状态
     *
     * @param updateStoreStatusDTO
     */

    void updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO);
}