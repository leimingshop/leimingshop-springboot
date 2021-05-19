/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.dto.SysMenuDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 *
 * @since 1.0.0
 */

public interface SysMenuService {

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据id查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */

    SysMenuDTO get(Long id);

    /**
     * @return void
     * @Description 保存菜单信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:05 2019-05-27
     */

    void save(@RequestBody SysMenuDTO dto);

    /**
     * @return void
     * @Description 修改菜单信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:07 2019-05-27
     */

    void update(@RequestBody SysMenuDTO dto);

    /**
     * @return void
     * @Description 删除菜单信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 15:09 2019-05-27
     */

    void delete(@RequestParam("id") Long id);

    /**
     * 菜单列表
     *
     * @param type 菜单类型
     */

    List<SysMenuDTO> getMenuList(@RequestParam(value = "type", required = false) Integer type);

    /**
     * 用户菜单列表
     *
     * @param type 菜单类型
     */

    List<SysMenuDTO> getUserMenuList(@RequestParam("superAdmin") Integer superAdmin, @RequestParam("id") Long id,
                                     @RequestParam(value = "type", required = false) Integer type);

    /**
     * 用户菜单导航
     *
     * @param userDetail 用户信息
     */

    List<SysMenuDTO> getUserMenuNavList(@RequestBody UserDetail userDetail);

    /**
     * 获取用户权限标识
     */

    Set<String> getUserPermissions(@RequestParam("superAdmin") Integer superAdmin, @RequestParam("id") Long id);

    /**
     * 根据父菜单，查询子菜单
     *
     * @param pid 父菜单ID
     */

    List<SysMenuDTO> getListPid(Long pid);

}
