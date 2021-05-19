/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.rolemenu;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.rolemenu.StoreRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺角色菜单管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreRoleMenuDao extends BaseDao<StoreRoleMenuEntity> {
    /**
     * 删除角色菜单
     *
     * @param ids 角色ID
     */
    void deleteByRoleId(@Param("ids") Long[] ids);

    /**
     * 根据角色id获取关联的菜单信息
     *
     * @param roleId 角色ID
     * @return
     */
    List<Long> getListByRoleId(Long roleId);
}