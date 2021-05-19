/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.storeclass;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.entity.storeclass.StoreClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺分类表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreClassDao extends BaseDao<StoreClassEntity> {
    /**
     * 根据店铺ID查询 店铺分类
     *
     * @param storeId 店铺ID
     * @return
     */
    List<StoreClassDTO> findByStoreId(Long storeId);

    /**
     * 根据店铺ID 删除店铺分类数据
     *
     * @param id 店铺ID
     */
    void deleteByStoreId(Long id);

    /**
     * 根据分类ID 删除店铺所关联的分类
     *
     * @param ids
     */
    void deleteByClassId(@Param("ids") Long[] ids);
}