/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.SuperAdminEnum;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.dao.SysMenuDao;
import com.leimingtech.dto.SysMenuDTO;
import com.leimingtech.entity.SysMenuEntity;
import com.leimingtech.enums.MenuTypeEnum;
import com.leimingtech.redis.SysMenuRedis;
import com.leimingtech.service.sys.SysLanguageService;
import com.leimingtech.service.sys.SysMenuService;
import com.leimingtech.service.sys.SysResourceService;
import com.leimingtech.service.sys.SysRoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 *
 * @since 1.0.0
 */
@Service

public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
    @Autowired
    private SysMenuRedis sysMenuRedis;
    @Autowired

    private SysRoleMenuService sysRoleMenuService;
    @Autowired

    private SysResourceService sysResourceService;
    @Autowired

    private SysLanguageService sysLanguageService;

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据id查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */
    @Override

    public SysMenuDTO get(Long id) {
        SysMenuEntity entity = baseDao.getById(id, HttpContextUtils.getLanguage());

        return ConvertUtils.sourceToTarget(entity, SysMenuDTO.class);
    }

    /**
     * @return void
     * @Description 保存菜单信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:05 2019-05-27
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@RequestBody SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //保存菜单
        insert(entity);
        saveLanguage(entity.getId(), "name", entity.getName());

        //保存菜单资源
        sysResourceService.saveMenuResource(entity.getId(), entity.getName(), dto.getResourceList());

        //清空当前用户，菜单导航、权限标识
        sysMenuRedis.delete(SecurityUser.getUserId());
    }

    /**
     * @return void
     * @Description 修改菜单信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:07 2019-05-27
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //上级菜单不能为自身
        if (entity.getId().equals(entity.getPid())) {
            throw new CustomException(ErrorCode.SUPERIOR_MENU_ERROR);
        }

        //更新菜单
        updateById(entity);
        saveLanguage(entity.getId(), "name", entity.getName());

        //更新菜单资源
        sysResourceService.saveMenuResource(entity.getId(), entity.getName(), dto.getResourceList());

        //清空当前用户，菜单导航、权限标识
        sysMenuRedis.delete(SecurityUser.getUserId());
    }

    /**
     * @return void
     * @Description 删除菜单信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 15:09 2019-05-27
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(@RequestParam("id") Long id) {
        //逻辑删除
//        logicDelete(new Long[]{id}, SysMenuEntity.class);
        this.deleteById(id);
        //删除角色菜单关系
        sysRoleMenuService.deleteByMenuId(id);

        //清空当前用户，菜单导航、权限标识
        sysMenuRedis.delete(SecurityUser.getUserId());
    }

    /**
     * 菜单列表
     *
     * @param type 菜单类型
     */


    @Override
    public List<SysMenuDTO> getMenuList(@RequestParam(value = "type", required = false) Integer type) {
        List<SysMenuEntity> menuList = baseDao.getMenuList(type, HttpContextUtils.getLanguage());

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList, Constant.MENU_ROOT);
    }

    /**
     * 用户菜单列表
     *
     * @param type 菜单类型
     */

    @Override
    public List<SysMenuDTO> getUserMenuList(@RequestParam("superAdmin") Integer superAdmin, @RequestParam("id") Long id,
                                            @RequestParam(value = "type", required = false) Integer type) {
        List<SysMenuEntity> menuList;

        //系统管理员，拥有最高权限
        if (superAdmin == SuperAdminEnum.YES.value()) {
            menuList = baseDao.getMenuList(type, HttpContextUtils.getLanguage());
        } else {
            menuList = baseDao.getUserMenuList(id, type, HttpContextUtils.getLanguage());
        }

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList);
    }

    /**
     * 用户菜单导航
     *
     * @param userDetail 用户信息
     */

    @Override
    public List<SysMenuDTO>
    getUserMenuNavList(@RequestBody UserDetail userDetail) {
        List<SysMenuDTO> menuList = sysMenuRedis.getUserMenuNavList(userDetail.getId());
        if (menuList == null) {
            menuList = getUserMenuList(userDetail.getSuperAdmin(), userDetail.getId(), MenuTypeEnum.MENU.value());

            sysMenuRedis.setUserMenuNavList(userDetail.getId(), menuList);
        }

        return menuList;
    }

    /**
     * 获取用户权限标识
     */
    @Override

    public Set<String> getUserPermissions(@RequestParam("superAdmin") Integer superAdmin,
                                          @RequestParam("id") Long id) {
        //用户权限列表
        Set<String> permsSet = sysMenuRedis.getUserPermissions(id);
        if (permsSet != null) {
            return permsSet;
        }

        //超级管理员，拥有最高权限
        List<SysMenuEntity> menuList;
        if (superAdmin == SuperAdminEnum.YES.value()) {
            menuList = baseDao.getMenuList(MenuTypeEnum.BUTTON.value(), HttpContextUtils.getLanguage());
        } else {
            menuList = baseDao.getUserMenuList(id, MenuTypeEnum.BUTTON.value(), HttpContextUtils.getLanguage());
        }

        permsSet = new HashSet<>();
        for (SysMenuEntity menu : menuList) {
            if (StringUtils.isNotBlank(menu.getPermissions())) {
                permsSet.add(menu.getPermissions());
            }
        }

        //保存到缓存
        sysMenuRedis.setUserPermissions(id, permsSet);

        return permsSet;
    }

    /**
     * 根据父菜单，查询子菜单
     *
     * @param pid 父菜单ID
     */

    @Override
    public List<SysMenuDTO> getListPid(Long pid) {
        List<SysMenuEntity> menuList = baseDao.getListPid(pid);

        return ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);
    }

    private void saveLanguage(Long tableId, String fieldName, String fieldValue) {
        sysLanguageService.saveOrUpdate("sys_menu", tableId, fieldName, fieldValue, HttpContextUtils.getLanguage());
    }

}