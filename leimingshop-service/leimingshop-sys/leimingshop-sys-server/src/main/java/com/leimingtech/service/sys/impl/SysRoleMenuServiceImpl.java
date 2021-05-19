/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.dao.SysRoleMenuDao;
import com.leimingtech.entity.SysRoleMenuEntity;
import com.leimingtech.service.sys.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色菜单关系
 *
 * @since 1.0.0
 */
@Service

public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

    /**
     * 根据角色ID查询列表
     *
     * @param roleId 角色ID
     * @return
     */
    @Override

    public List<Long> getMenuIdList(Long roleId) {
        return baseDao.getMenuIdList(roleId);
    }

    /**
     * 保存or修改
     *
     * @param roleId     角色ID
     * @param menuIdList 菜单ID列表
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "menuIdList") List<Long> menuIdList) {
        //先删除角色菜单关系
        deleteByRoleId(roleId);

        //角色没有一个菜单权限的情况
        if (CollUtil.isEmpty(menuIdList)) {
            return;
        }

        //保存角色菜单关系
        for (Long menuId : menuIdList) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);

            //保存
            insert(sysRoleMenuEntity);
        }
    }

    /**
     * 根据角色id删除
     *
     * @param roleId 角色id
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleId(Long roleId) {
        baseDao.deleteByRoleId(roleId);
    }

    /**
     * 根据菜单id删除
     *
     * @param menuId 菜单id
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuId(Long menuId) {
        baseDao.deleteByMenuId(menuId);
    }

}