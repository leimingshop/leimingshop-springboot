/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色菜单关系
 *
 * @since 1.0.0
 */

public interface SysRoleMenuService {

    /**
     * 根据角色ID，获取菜单ID列表
     */

    List<Long> getMenuIdList(Long roleId);

    /**
     * 保存或修改
     *
     * @param roleId     角色ID
     * @param menuIdList 菜单ID列表
     */

    void saveOrUpdate(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "menuIdList") List<Long> menuIdList);

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