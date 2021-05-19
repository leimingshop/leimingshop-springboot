/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.store;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.store.StoreAuthAuditDTO;
import com.leimingtech.modules.dto.store.StoreAuthAuditPageDTO;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import com.leimingtech.modules.entity.store.StoreAuthAuditEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 店铺审核表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-20
 */
@Mapper
public interface StoreAuthAuditDao extends BaseDao<StoreAuthAuditEntity> {
    /**
     * 店铺公司信息回显
     *
     * @param storeId    店铺ID
     * @param createDate 申请时间
     * @return
     */
    StoreAuthAuditDTO getInfoByStoreId(@Param("storeId") Long storeId, @Param("createDate") String createDate);

    /**
     * 更新店铺公司信息审核状态
     *
     * @param updateStoreStatusDTO
     */
    void updateStoreStatus(UpdateStoreStatusDTO updateStoreStatusDTO);

    /**
     * 公司信息审核列表
     *
     * @param params
     * @return
     */
    List<StoreAuthAuditPageDTO> authAuditPage(Map<String, Object> params);
}