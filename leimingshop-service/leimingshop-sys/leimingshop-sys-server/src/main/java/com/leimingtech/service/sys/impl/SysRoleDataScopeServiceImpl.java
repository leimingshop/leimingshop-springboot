/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.dao.SysRoleDataScopeDao;
import com.leimingtech.entity.SysRoleDataScopeEntity;
import com.leimingtech.service.sys.SysRoleDataScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色数据权限
 *
 * @since 1.0.0
 */
@Service

public class SysRoleDataScopeServiceImpl extends BaseServiceImpl<SysRoleDataScopeDao, SysRoleDataScopeEntity>
        implements SysRoleDataScopeService {
    /**
     * 根据角色id查询列表
     *
     * @param roleId
     * @return
     */
    @Override

    public List<Long> getDeptIdList(Long roleId) {
        return baseDao.getDeptIdList(roleId);
    }

    /**
     * 根据用户id获取数据权限
     *
     * @param userId 用户ID
     * @return
     */
    @Override

    public List<Long> getDataScopeList(Long userId) {
        return baseDao.getDataScopeList(userId);
    }

    /**
     * 保存或修改
     *
     * @param roleId     角色ID
     * @param deptIdList 部门ID列表
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "deptIdList") List<Long> deptIdList) {
        //先删除角色数据权限关系
        deleteByRoleId(roleId);

        //角色没有一个数据权限的情况
        if (CollUtil.isEmpty(deptIdList)) {
            return;
        }

        //保存角色数据权限关系
        for (Long deptId : deptIdList) {
            SysRoleDataScopeEntity sysRoleDataScopeEntity = new SysRoleDataScopeEntity();
            sysRoleDataScopeEntity.setDeptId(deptId);
            sysRoleDataScopeEntity.setRoleId(roleId);

            //保存
            insert(sysRoleDataScopeEntity);
        }
    }

    /**
     * 删除
     *
     * @param roleId 角色id
     */
    @Override

    public void deleteByRoleId(Long roleId) {
        baseDao.deleteByRoleId(roleId);
    }
}