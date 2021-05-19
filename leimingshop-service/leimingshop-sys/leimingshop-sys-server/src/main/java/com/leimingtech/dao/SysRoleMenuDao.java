/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色菜单关系
 *
 * @since 1.0.0
 */
@Mapper
public interface SysRoleMenuDao extends BaseDao<SysRoleMenuEntity> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> getMenuIdList(Long roleId);

    /**
     * 根据角色id，删除角色菜单关系
     *
     * @param roleId 角色id
     */
    void deleteByRoleId(Long roleId);

    /**
     * 根据菜单id，删除角色菜单关系
     *
     * @param menuId 菜单id
     */
    void deleteByMenuId(Long menuId);
}