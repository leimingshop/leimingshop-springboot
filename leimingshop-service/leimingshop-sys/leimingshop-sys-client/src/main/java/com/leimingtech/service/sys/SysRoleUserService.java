/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 角色用户关系
 * @Date :2019/5/28 14:54
 * @Version V1.0
 **/

public interface SysRoleUserService {

    /**
     * @Author: weixianchun
     * @Description: 保存或修改
     * @Date :2019/5/28 14:55
     * @Param userId 用户id
     * @Param roleId 角色数组
     * @Version V1.0
     **/

    void saveOrUpdate(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId);

    /**
     * @Author: weixianchun
     * @Description: 根据角色ids，删除角色用户关系
     * @Date :2019/5/28 15:04
     * @Param roleIds
     * @Version V1.0
     **/

    void deleteByRoleIds(@RequestBody Long[] roleIds);

    /**
     * @Author: weixianchun
     * @Description: 根据用户id，删除角色用户关系
     * @Date :2019/5/28 14:55
     * @Param :
     * @Param userId 用户id
     * @Version V1.0
     **/

    void deleteByUserId(Long userId);

    /**
     * @Author: weixianchun
     * @Description: 查询角色id列表
     * @Date :2019/5/28 15:03
     * @Param userId 用户id
     * @Version V1.0
     **/

    List<Long> getRoleIdList(@RequestParam("userId") Long userId);

    /**
     * 查询角色下是否关联用户
     *
     * @param ids 角色ID
     * @return
     */

    Integer findUserCount(@RequestBody Long[] ids);
}