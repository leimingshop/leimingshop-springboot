/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.resource;


import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.modules.dto.menu.StoreMenuResourceDTO;
import com.leimingtech.modules.dto.resource.StoreResourceDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 资源管理
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-17
 */

public interface StoreResourceService {
    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */

    StoreResourceDTO get(Long id);

    /**
     * 保存菜单资源
     *
     * @param dto
     */

    void save(@RequestBody StoreResourceDTO dto);

    /**
     * 更新菜单资源
     *
     * @param dto
     */

    void update(@RequestBody StoreResourceDTO dto);

    /**
     * 根据ID 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 保存菜单资源
     *
     * @param menuId       菜单ID
     * @param menuName     菜单名称
     * @param resourceList 资源列表
     * @param operation    修改类型
     */

    void saveResource(@RequestParam("menuId") Long menuId,
                      @RequestParam("menuName") String menuName,
                      @RequestBody List<StoreMenuResourceDTO> resourceList, @RequestParam(name = "operation", required = false) String operation);

    /**
     * 获取菜单资源
     *
     * @param menuId
     * @return
     */

    List<StoreMenuResourceDTO> getResourceByMenuId(@RequestParam("menuId") Long menuId);

    /**
     * 获取用户权限标识
     *
     * @param id 用户id
     * @return 返回权限标识
     */

    Set<String> getUserPermissions(@RequestParam("id") String id);

    /**
     * 根据菜单ID 删除资源权限
     *
     * @param id 主键id
     */

    void deleteByCode(@RequestParam("id") Long id);

    /**
     * 获取全部资源列表
     *
     * @return 资源列表
     * @date 2019/12/19 15:23
     * @author lixiangx@leimingtech.com
     **/

    List<ResourceBO> getResourceList();

    /**
     * 根据用户信息查询用户的授权资源
     *
     * @param userId: 用户ID
     * @return 资源集合信息
     * @date 2019/12/19 15:44
     * @author lixiangx@leimingtech.com
     **/

    List<ResourceBO> getUserResourceList(@RequestParam("userId") Long userId);
}