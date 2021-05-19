/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.auth;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.auth.StoreAuthDTO;
import com.leimingtech.modules.entity.auth.StoreAuthEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 店铺认证信息表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreAuthDao extends BaseDao<StoreAuthEntity> {
    /**
     * 根据店铺信息获取店铺认证信息
     *
     * @param storeId 店铺ID
     * @return
     */
    StoreAuthDTO findByStoreId(Long storeId);

    /**
     * 根据店铺ID 删除店铺认证信息
     *
     * @param ids 店铺ID
     */
    void deleteByStoreId(@Param("ids") Long[] ids);

    /**
     * 根据店铺ID 获取店铺公司信息ID
     *
     * @param storeId
     * @return
     */
    Long findAuthIdByStoreId(Long storeId);

}