/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.store.StoreAuditDTO;
import com.leimingtech.modules.dto.store.StoreAuditPageDTO;
import com.leimingtech.modules.dto.store.UpdateStoreBasicDTO;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO;
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

public interface StoreAuditService {

    /**
     * 店铺信息修改记录分页
     *
     * @param params
     * @return
     */

    PageData<StoreAuditPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 获取所有信息，不分页
     *
     * @param params
     * @return
     */

    List<StoreAuditDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */

    StoreAuditDTO get(Long id);

    /**
     * 保存修改记录
     *
     * @param dto
     */

    void save(@RequestBody UpdateStoreBasicDTO dto);

    /**
     * 、
     * 更新修改记录
     *
     * @param dto
     */

    void update(@RequestBody UpdateStoreBasicDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 店铺基础信息回显
     *
     * @param storeId    店铺ID
     * @param createDate 申请时间
     * @return
     */

    StoreAuditDTO getInfoByStoreId(@RequestParam("storeId") Long storeId,
                                   @RequestParam(value = "createDate", required = false) String createDate);

    /**
     * 修改店铺普通信息审核状态 审核通过，同步信息到店铺入住表
     *
     * @param updateStoreStatusDTO
     */

    void updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO);

    /**
     * 获取待审核店铺分类
     *
     * @param id 审核记录ID
     * @return
     */

    List<StoreGoodsClassDTO> getAuditStoreClass(@RequestParam("id") Long id);

}