/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色数据权限
 *
 * @since 1.0.0
 */

public interface SysRoleDataScopeService {

    /**
     * 根据角色ID，获取部门ID列表
     */

    List<Long> getDeptIdList(Long roleId);

    /**
     * 获取用户对应的部门数据权限
     *
     * @param userId 用户ID
     * @return 返回部门ID列表
     */

    List<Long> getDataScopeList(Long userId);

    /**
     * 保存或修改
     *
     * @param roleId     角色ID
     * @param deptIdList 部门ID列表
     */

    void saveOrUpdate(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "deptIdList") List<Long> deptIdList);

    /**
     * 根据角色id，删除角色数据权限关系
     *
     * @param roleId 角色id
     */

    void deleteByRoleId(Long roleId);


}