/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.dto.MenuResourceDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 资源管理
 *
 * @since 1.0.0
 */

public interface SysResourceService {

    /**
     * 获取菜单资源列表
     *
     * @param menuId 菜单ID
     */

    List<MenuResourceDTO> getMenuResourceList(@RequestParam("menuId") Long menuId);

    /**
     * 获取所有资源列表
     */

    List<ResourceBO> getResourceList();

    /**
     * 获取用户资源列表
     *
     * @param userId 用户ID
     */

    List<ResourceBO> getUserResourceList(@RequestParam("userId") Long userId);

    /**
     * 保存菜单资源
     *
     * @param menuId       菜单ID
     * @param menuName     菜单名称
     * @param resourceList 资源列表
     */

    void saveMenuResource(@RequestParam("menuId") Long menuId,
                          @RequestParam("menuName") String menuName,
                          @RequestBody List<MenuResourceDTO> resourceList);
}