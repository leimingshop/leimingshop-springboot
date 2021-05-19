/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.resource.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.commons.tools.security.enums.ResourceAuthEnum;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.enums.MenuFlagEnum;
import com.leimingtech.enums.MenuTypeEnum;
import com.leimingtech.modules.dao.resource.StoreResourceDao;
import com.leimingtech.modules.dao.usermanage.StoreUserManageDao;
import com.leimingtech.modules.dto.menu.StoreMenuResourceDTO;
import com.leimingtech.modules.dto.resource.StoreResourceDTO;
import com.leimingtech.modules.dto.role.StoreRoleDTO;
import com.leimingtech.modules.entity.resource.StoreResourceEntity;
import com.leimingtech.modules.enums.store.RoleEnum;
import com.leimingtech.modules.service.resource.StoreResourceService;
import com.leimingtech.modules.service.role.StoreRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 资源管理
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-17
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class StoreResourceServiceImpl extends BaseServiceImpl<StoreResourceDao, StoreResourceEntity> implements StoreResourceService {


    private static final String UPDATE = "update";

    @Autowired
    private StoreRoleService storeRoleService;
    @Autowired
    private StoreUserManageDao storeUserManageDao;

    @Autowired
    private RedisUtils redisUtils;


    private QueryWrapper<StoreResourceEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreResourceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */
    @Override

    public StoreResourceDTO get(Long id) {
        StoreResourceEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, StoreResourceDTO.class);
    }

    /**
     * 保存菜单资源
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody StoreResourceDTO dto) {
        StoreResourceEntity entity = ConvertUtils.sourceToTarget(dto, StoreResourceEntity.class);

        insert(entity);
    }

    /**
     * 更新菜单资源
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody StoreResourceDTO dto) {
        StoreResourceEntity entity = ConvertUtils.sourceToTarget(dto, StoreResourceEntity.class);

        updateById(entity);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, StoreResourceEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 保存菜单资源
     *
     * @param menuId       菜单ID
     * @param menuName     菜单名称
     * @param resourceList 资源列表
     * @param operation    操作类型
     */

    @Override
    public void saveResource(@RequestParam("menuId") Long menuId,
                             @RequestParam("menuName") String menuName,
                             @RequestBody List<StoreMenuResourceDTO> resourceList, @RequestParam(name = "operation", required = false) String operation) {

        // 判断操作类型
        if (UPDATE.equals(operation) && CollUtil.isEmpty(resourceList)) {
            baseDao.deleteByCode(menuId + "");
        }
        //菜单没有一个资源的情况
        if (CollUtil.isEmpty(resourceList)) {
            return;
        }

        //先删除菜单资源关系
        baseDao.deleteByCode(menuId + "");

        //保存菜单资源关系
        for (StoreMenuResourceDTO dto : resourceList) {
            StoreResourceEntity entity = new StoreResourceEntity();
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

    /**
     * 获取菜单资源
     *
     * @param menuId
     * @return
     */

    @Override
    public List<StoreMenuResourceDTO> getResourceByMenuId(@RequestParam("menuId") Long menuId) {

        return baseDao.getResourceByMenuId(menuId);
    }

    /**
     * 获取用户权限标识
     */
    @Override

    public Set<String> getUserPermissions(@RequestParam("id") String id) {

        // 查询角色权限
        StoreRoleDTO storeRoleDTO = storeRoleService.get(Long.valueOf(id));
        List<StoreMenuDTOs> dtoList = new ArrayList<>();
        if (storeRoleDTO.getRoleMark() == RoleEnum.ADMIN_ROLE.value()) {
            dtoList = storeUserManageDao.findAllMenu(MenuTypeEnum.BUTTON.value());
        } else if (storeRoleDTO.getRoleMark() == RoleEnum.REGISTER_ROLE.value()) {
            //TODO 查询注册角色权限 待优化
            dtoList = baseDao.findRegisterResource();
        } else {
            dtoList = storeUserManageDao.getUserMenuList(id, MenuTypeEnum.BUTTON.value());
        }

        Set<String> permsSet = new HashSet<>();
        for (StoreMenuDTOs menu : dtoList) {
            if (StringUtils.isNotBlank(menu.getPermission())) {
                permsSet.add(menu.getPermission());
            }
        }
        return permsSet;
    }


    /**
     * 根据菜单ID 删除资源权限
     *
     * @param id
     */

    @Override
    public void deleteByCode(@RequestParam("id") Long id) {
        baseDao.deleteByCode(id + "");
    }


    /**
     * 获取全部资源列表
     *
     * @return 资源集合
     * @date 2019/12/19 15:23
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public List<ResourceBO> getResourceList() {
        // 从缓存中获取全部菜单权限
        List<StoreResourceEntity> entityList = (List<StoreResourceEntity>) redisUtils.get(RedisKeys.getSellerResourceKey());
        if (entityList == null) {
            // 缓存中数据
            entityList = baseDao.getResourceList();
            redisUtils.set(RedisKeys.getSellerResourceKey(), entityList);
        }
        return ConvertUtils.sourceToTarget(entityList, ResourceBO.class);
    }

    /**
     * 根据用户信息查询用户的授权资源
     *
     * @param userId: 用户ID
     * @return 资源集合信息
     * @date 2019/12/19 15:44
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public List<ResourceBO> getUserResourceList(@RequestParam("userId") Long userId) {
        return baseDao.getUserResourceList(userId);
    }

}