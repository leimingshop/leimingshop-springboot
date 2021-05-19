/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.store;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.store.StoreAuditDTO;
import com.leimingtech.modules.dto.store.StoreAuditPageDTO;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.entity.store.StoreAuditEntity;
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
public interface StoreAuditDao extends BaseDao<StoreAuditEntity> {

    /**
     * 获取店铺基础信息
     *
     * @param storeId
     * @param createDate
     * @return
     */
    StoreAuditDTO getInfoByStoreId(@Param("storeId") Long storeId, @Param("createDate") String createDate);

    /**
     * 根据店铺ID 删除待审核分类
     *
     * @param storeId
     */
    void deleteByStoreId(Long storeId);

    /**
     * 保存店铺分类
     *
     * @param list 保存参数
     */
    void saveStoreClass(@Param("list") List<StoreClassDTO> list);

    /**
     * 获取店铺分类信息
     *
     * @param storeId 店铺ID
     * @return 返回分类ID数组
     */
    List<Long> getStoreClass(Long storeId);

    /**
     * 店铺审核分页
     *
     * @param params 查询参数
     * @return 返回店铺审核分页信息
     */
    List<StoreAuditPageDTO> auditPage(Map<String, Object> params);

    /**
     * 更改店铺审核状态
     *
     * @param updateStoreStatusDTO
     */
    void updateStoreStatus(UpdateStoreStatusDTO updateStoreStatusDTO);

}