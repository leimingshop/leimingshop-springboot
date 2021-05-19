/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.enums.ResourceAuthEnum;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.SysResourceDao;
import com.leimingtech.dto.MenuResourceDTO;
import com.leimingtech.entity.SysResourceEntity;
import com.leimingtech.enums.MenuFlagEnum;
import com.leimingtech.redis.SysResourceRedis;
import com.leimingtech.service.sys.SysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 资源管理
 *
 * @since 1.0.0
 */
@Service

public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceDao, SysResourceEntity> implements SysResourceService {
    @Autowired
    private SysResourceRedis sysResourceRedis;

    /**
     * 获取菜单资源列表
     *
     * @param menuId 菜单ID
     */

    @Override
    public List<MenuResourceDTO> getMenuResourceList(@RequestParam("menuId") Long menuId) {
        List<SysResourceEntity> entityList = baseDao.getMenuResourceList(menuId + "");

        return ConvertUtils.sourceToTarget(entityList, MenuResourceDTO.class);
    }

    /**
     * 获取所有资源列表
     */

    @Override
    public List<ResourceBO> getResourceList() {
        List<SysResourceEntity> entityList = sysResourceRedis.get();
        if (entityList == null) {
            entityList = baseDao.getResourceList();

            sysResourceRedis.set(entityList);
        }
        return ConvertUtils.sourceToTarget(entityList, ResourceBO.class);
    }

    /**
     * 获取用户资源列表
     *
     * @param userId 用户ID
     */

    @Override
    public List<ResourceBO> getUserResourceList(@RequestParam("userId") Long userId) {
        List<SysResourceEntity> entityList = baseDao.getUserResourceList(userId);

        return ConvertUtils.sourceToTarget(entityList, ResourceBO.class);
    }

    /**
     * 保存菜单资源
     *
     * @param menuId       菜单ID
     * @param menuName     菜单名称
     * @param resourceList 资源列表
     */

    @Override
    public void saveMenuResource(@RequestParam("menuId") Long menuId,
                                 @RequestParam("menuName") String menuName,
                                 @RequestBody List<MenuResourceDTO> resourceList) {
        //先删除菜单资源关系
        baseDao.deleteByCode(menuId + "");

        //删除缓存
        sysResourceRedis.delete();

        //菜单没有一个资源的情况
        if (CollUtil.isEmpty(resourceList)) {
            return;
        }

        //保存菜单资源关系
        for (MenuResourceDTO dto : resourceList) {
            SysResourceEntity entity = new SysResourceEntity();
            entity.setResourceCode(menuId + "");
            entity.setResourceName(menuName);
            entity.setResourceUrl(dto.getResourceUrl());
            entity.setResourceMethod(dto.getResourceMethod());
            entity.setAuthLevel(ResourceAuthEnum.PERMISSIONS_AUTH.value());
            entity.setMenuFlag(MenuFlagEnum.YES.value());

            //保存
            insert(entity);
        }
    }

}