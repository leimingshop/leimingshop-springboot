/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.role;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.role.StoreRoleDTO;
import com.leimingtech.modules.dto.role.UpdateStoreRoleDTO;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import com.leimingtech.modules.entity.role.StoreRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 店铺角色表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreRoleDao extends BaseDao<StoreRoleEntity> {
    /**
     * 检查角色名称是否重复
     *
     * @param roleName 角色名称
     * @param storeId  店铺id
     * @param roleId   角色id
     * @return 返回重复
     */
    Integer checkRoleName(@Param("roleName") String roleName,
                          @Param("storeId") Long storeId,
                          @Param("roleId") Long roleId);

    /**
     * 店铺角色分页查询
     *
     * @param params 查询参数
     * @return 返回角色信息
     */
    List<StoreRoleDTO> findPage(Map<String, Object> params);

    /**
     * 查询店铺角色数量及信息, 并实现角色名称模糊查询
     *
     * @param params 查询条件
     * @Author: weixianchun
     * @Description: 查询店铺角色数量及信息, 并实现角色名称模糊查询
     * @return: java.util.List<com.leimingtech.modules.dto.role.StoreRoleDTO>
     * @Date: 2019/8/13 13:23
     * @Version 1.0
     */
    List<StoreRoleDTO> selectCountInfo(Map<String, Object> params);

    /**
     * 根据店铺id获取角色信息
     *
     * @param storeId
     * @return
     */
    UpdateStoreRoleDTO getRoleByStoreId(Long storeId);

    /**
     * 根据用户ID 查询角色ID
     *
     * @param userId
     * @return
     */
    StoreUserManageDTO getByUserId(Long userId);

    /**
     * 查询是否有首页菜单
     *
     * @param roleId
     * @return
     */
    Integer getIndexMenu(String roleId);
}