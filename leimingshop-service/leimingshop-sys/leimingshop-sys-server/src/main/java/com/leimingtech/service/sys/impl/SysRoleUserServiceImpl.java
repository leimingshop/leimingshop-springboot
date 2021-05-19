/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.dao.sys.SysRoleUserDao;
import com.leimingtech.entity.sys.SysRoleUserEntity;
import com.leimingtech.service.sys.SysRoleUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色用户关系
 *
 * @since 1.0.0
 */

@Service
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUserDao, SysRoleUserEntity> implements SysRoleUserService {

    /**
     * 保存或修改
     *
     * @param userId 用户ID
     * @param roleId 角色ID列表
     */
    @Override

    public void saveOrUpdate(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {

        //先删除角色用户关系
        deleteByUserId(userId);

        //用户没有一个角色权限的情况
        if (roleId == null) {
            return;
        }

        //保存角色用户关系
        SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
        sysRoleUserEntity.setUserId(userId);
        sysRoleUserEntity.setRoleId(roleId);

        //保存
        insert(sysRoleUserEntity);
    }

    /**
     * 根据角色ids，删除角色用户关系
     *
     * @param roleIds 角色ids
     */

    @Override
    public void deleteByRoleIds(@RequestBody Long[] roleIds) {
        baseDao.deleteByRoleIds(roleIds);
    }

    /**
     * 根据用户id，删除角色用户关系
     *
     * @param userId 用户id
     */
    @Override

    public void deleteByUserId(Long userId) {
//        baseDao.deleteByUserId(userId);
        baseDao.delete(new QueryWrapper<SysRoleUserEntity>().eq("user_id", userId));
    }

    /**
     * 角色ID列表
     *
     * @param userId 用户ID
     */
    @Override

    public List<Long> getRoleIdList(@RequestParam("userId") Long userId) {

        return baseDao.getRoleIdList(userId);
    }


    /**
     * 查询角色下是否关联用户
     *
     * @param ids 角色ID
     * @return
     */

    @Override
    public Integer findUserCount(@RequestBody Long[] ids) {
        return baseDao.findUserCount(ids);
    }
}