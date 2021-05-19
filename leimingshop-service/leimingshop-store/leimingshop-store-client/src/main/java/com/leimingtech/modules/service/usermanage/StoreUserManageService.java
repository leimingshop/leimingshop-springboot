/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.usermanage;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户和店铺的管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreUserManageService {
    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<StoreUserManageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreUserManageDTO get(Long id);

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    void save(@RequestBody StoreUserManageDTO dto);

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody StoreUserManageDTO dto);


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);


    /**
     * 根据用户ID 获取用户和店铺管理信息
     *
     * @param userId
     * @return
     */

    StoreUserManageDTO findUserManage(@RequestParam("userId") Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @param roleId 角色ID
     * @return
     */

    List<StoreMenuDTOs> findMenuByRoleId(@RequestParam("roleId") String roleId);

    /**
     * 根据店铺ID 查询出用户ID
     *
     * @param ids 店铺ID
     * @return
     */

    Long[] findUserIdByUserId(@RequestBody Long[] ids);

    /**
     * 根据用户ID 更新店铺用户表
     *
     * @param storeUserManageDTO 参数
     */

    void updateByUserId(@RequestBody StoreUserManageDTO storeUserManageDTO);

    /**
     * 删除用户店铺关联信息
     *
     * @param userId 用户ID
     */

    void deleteByUserId(@RequestBody Long[] userId);

    /**
     * 查询所有菜单
     *
     * @return
     */

    List<StoreMenuDTOs> findAllMenu();

    /**
     * 根据角色ID 更新店铺ID
     * 根据店铺id更新用户和角色信息
     *
     * @param storeId
     * @param roleId
     */

    void updateStoreByRoleId(@RequestParam("storeId") Long storeId, @RequestParam("roleId") Long roleId);

    /**
     * 根据店铺ID获取用户ID
     *
     * @param storeId
     * @return
     */

    Long getUserIdByStoreId(@RequestParam("storeId") Long storeId);

    /**
     * 删除店铺下角色，用户，去掉用户角色关联
     *
     * @param storeId
     */

    void updateByStoreId(@RequestParam("storeId") Long storeId);
}