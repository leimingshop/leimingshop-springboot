/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.role;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.role.SaveStoreRoleDTO;
import com.leimingtech.modules.dto.role.StoreRoleDTO;
import com.leimingtech.modules.dto.role.UpdateStoreRoleDTO;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 店铺角色表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreRoleService {
    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<StoreRoleDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreRoleDTO get(@PathVariable Long id);

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    void save(@RequestBody SaveStoreRoleDTO dto);

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody UpdateStoreRoleDTO dto);


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 查询角色名称是否重复
     *
     * @param roleName 角色名称
     * @param storeId  店铺id
     * @param roleId   角色id
     * @return 返回重复数量
     */

    Integer checkRoleName(@RequestParam("roleName") String roleName,
                          @RequestParam("storeId") Long storeId,
                          @RequestParam(value = "roleId", required = false) Long roleId);

    /**
     * 新增角色，
     *
     * @param storeRoleEntity
     */

    void saveRole(@RequestBody StoreRoleDTO storeRoleEntity);

    /**
     * 根据店铺ID 获取角色信息
     *
     * @param storeId
     * @return
     */

    UpdateStoreRoleDTO getRoleByStoreId(@RequestParam("storeId") Long storeId);

    /**
     * 根据用户ID  查询角色信息
     *
     * @param userId
     * @return
     */

    StoreUserManageDTO getByUserId(@RequestParam("userId") Long userId);

    /**
     * 查询是否有首页菜单
     *
     * @param roleId
     * @return
     */

    Integer getIndexMenu(@RequestParam("roleId") String roleId);
}