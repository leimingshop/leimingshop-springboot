/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.rolemenu;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.rolemenu.StoreRoleMenuDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺角色菜单管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreRoleMenuService {
    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<StoreRoleMenuDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreRoleMenuDTO get(@PathVariable Long id);

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    void save(@RequestBody StoreRoleMenuDTO dto);

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody StoreRoleMenuDTO dto);


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 保存角色菜单
     *
     * @param storeRoleMenuDTOList
     */

    void saveBatch(@RequestBody List<StoreRoleMenuDTO> storeRoleMenuDTOList);

    /**
     * 更新角色菜单
     *
     * @param menuIdList 菜单集合
     * @param id         角色id
     */

    void updateRoleMenu(@RequestBody List<Long> menuIdList, @RequestParam("id") Long id);

    /**
     * 删除角色菜单
     *
     * @param ids 角色ID
     */

    void deleteByRoleId(@RequestBody Long[] ids);

    /**
     * 根据角色ID 获取关联的菜单信息
     *
     * @param roleId 角色ID
     * @return
     */

    List<Long> getListByRoleId(@RequestParam("roleId") Long roleId);

}