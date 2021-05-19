/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.usermanage;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import com.leimingtech.modules.entity.usermanage.StoreUserManageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户和店铺的管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreUserManageDao extends BaseDao<StoreUserManageEntity> {
    /**
     * 根据店铺ID 获取登录信息
     *
     * @param storeId 店铺ID
     * @return
     */
    StoreUserManageDTO findByStoreId(Long storeId);


    /**
     * 查询用户菜单列表
     *
     * @param roleId 角色ID
     * @param type   菜单类型
     * @return 返回店铺菜单资源信息
     */
    List<StoreMenuDTOs> getUserMenuList(@Param("roleId") String roleId, @Param("type") Integer type);

    /**
     * 根据用户ID 获取用户和店铺管理信息
     *
     * @param userId
     * @return
     */
    StoreUserManageDTO findUserManage(Long userId);

    /**
     * 根据店铺id查询出用户id
     *
     * @param ids
     * @return
     */
    Long[] findUserIdByUserId(@Param("ids") Long[] ids);

    /**
     * 根据用户ID 更新店铺用户表
     *
     * @param storeUserManageDTO 参数
     */
    void updateByUserId(StoreUserManageDTO storeUserManageDTO);

    /**
     * 删除用户和店铺关联信息
     *
     * @param ids
     */
    void deleteByUserId(@Param("ids") Long[] ids);

    /**
     * 查询所有菜单
     *
     * @param menuType 菜单类型
     * @return 返回菜单信息
     */
    List<StoreMenuDTOs> findAllMenu(@Param("menuType") Integer menuType);

    /**
     * 根据角色ID 更新店铺ID
     *
     * @param storeId 店铺id
     * @param roleId  角色id
     */
    void updateStoreByRoleId(@Param("storeId") Long storeId, @Param("roleId") Long roleId);

    /***
     * 根据店铺ID 更新角色和用户id
     * @param storeId
     * @param roleId
     */
    void updateUserIdAndRoleId(@Param("storeId") Long storeId, @Param("roleId") Long roleId);

    /**
     * 根据ID 更新所有，可以更新为NULL
     *
     * @param dto
     */
    void updateAllById(StoreUserManageDTO dto);

    /**
     * 根据店铺ID 获取用户ID
     *
     * @param storeId
     * @return
     */
    Long getUserIdByStoreId(Long storeId);

    /**
     * \删除当前店铺所关联信息
     *
     * @param storeId
     */
    void updateByStoreId(Long storeId);

}