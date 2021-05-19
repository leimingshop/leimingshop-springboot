/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.usermanage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.UserDetailRedis;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.enums.MenuTypeEnum;
import com.leimingtech.modules.dao.usermanage.StoreUserManageDao;
import com.leimingtech.modules.dto.role.StoreRoleDTO;
import com.leimingtech.modules.dto.role.UpdateStoreRoleDTO;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import com.leimingtech.modules.entity.usermanage.StoreUserManageEntity;
import com.leimingtech.modules.enums.store.RoleEnum;
import com.leimingtech.modules.service.menu.StoreMenuService;
import com.leimingtech.modules.service.role.StoreRoleService;
import com.leimingtech.modules.service.usermanage.StoreUserManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户和店铺的管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service

public class StoreUserManageServiceImpl extends CrudServiceImpl<StoreUserManageDao, StoreUserManageEntity, StoreUserManageDTO> implements StoreUserManageService {

    @Autowired

    private StoreRoleService storeRoleService;
    @Autowired

    private StoreMenuService storeMenuService;
    @Autowired
    private UserDetailRedis userDetailRedis;


    @Override
    public QueryWrapper<StoreUserManageEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreUserManageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<StoreUserManageDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreUserManageDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    @Override

    public void save(@RequestBody StoreUserManageDTO dto) {
        super.save(dto);
    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody StoreUserManageDTO dto) {
        super.update(dto);
    }


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }


    /**
     * 根据用户ID 获取用户和店铺管理信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override

    public StoreUserManageDTO findUserManage(@RequestParam("userId") Long userId) {

        return baseDao.findUserManage(userId);
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param roleId 角色ID
     * @return
     */
    @Override

    public List<StoreMenuDTOs> findMenuByRoleId(@RequestParam("roleId") String roleId) {
        // 查询角色权限
        StoreRoleDTO storeRoleDTO = storeRoleService.get(Long.parseLong(roleId));
        List<StoreMenuDTOs> dtoList = new ArrayList<>();
        if (storeRoleDTO.getRoleMark() == 1) {
            dtoList = baseDao.findAllMenu(MenuTypeEnum.MENU.value());
        } else if (storeRoleDTO.getRoleMark() == RoleEnum.REGISTER_ROLE.value()) {
            //TODO 查询注册角色菜单待优化
            dtoList = storeMenuService.findRegisterMenu();
        } else {
            dtoList = baseDao.getUserMenuList(roleId, MenuTypeEnum.MENU.value());
        }

        return TreeUtils.build(dtoList, Constant.MENU_ROOT);
    }

    /**
     * 根据店铺id查询出用户ID
     *
     * @param ids 店铺ID
     * @return
     */
    @Override

    public Long[] findUserIdByUserId(@RequestBody Long[] ids) {

        return baseDao.findUserIdByUserId(ids);
    }

    /**
     * 根据用户ID 更新店铺用户表
     *
     * @param storeUserManageDTO 参数
     */

    @Override

    public void updateByUserId(@RequestBody StoreUserManageDTO storeUserManageDTO) {

        baseDao.updateByUserId(storeUserManageDTO);

    }

    /**
     * 删除用户信息
     *
     * @param userId 用户ID
     */

    @Override
    public void deleteByUserId(@RequestBody Long[] userId) {
        baseDao.deleteByUserId(userId);
    }

    /**
     * 查询所有菜单
     *
     * @return
     */
    @Override

    public List<StoreMenuDTOs> findAllMenu() {
        List<StoreMenuDTOs> dtoList = baseDao.findAllMenu(null);
        return TreeUtils.build(dtoList, Constant.MENU_ROOT);
    }

    /**
     * 跟用角色ID 更新店铺ID
     *
     * @param storeId
     * @param roleId
     */

    @Override
    public void updateStoreByRoleId(@RequestParam("storeId") Long storeId, @RequestParam("roleId") Long roleId) {
        baseDao.updateStoreByRoleId(storeId, roleId);
        baseDao.updateUserIdAndRoleId(storeId, roleId);

    }

    /**
     * 根据店铺ID获取用户ID
     *
     * @param storeId
     * @return
     */

    @Override
    public Long getUserIdByStoreId(@RequestParam("storeId") Long storeId) {
        return baseDao.getUserIdByStoreId(storeId);
    }

    /**
     * 删除店铺下角色，用户，去掉用户角色关联
     *
     * @param storeId
     */

    @Override
    public void updateByStoreId(@RequestParam("storeId") Long storeId) {
        StoreUserManageDTO storeUserManageDTO = baseDao.findByStoreId(storeId);

        UpdateStoreRoleDTO storeRoleDTO = new UpdateStoreRoleDTO();
        storeRoleDTO.setId(storeUserManageDTO.getRoleId());
        storeRoleDTO.setRoleName("新注册商户");
        storeRoleDTO.setRoleMark(2);
        storeRoleDTO.setRoleRemark("新注册商户，未通过资深审核");
        storeRoleService.update(storeRoleDTO);

        baseDao.updateByStoreId(storeId);

        StoreUserManageEntity storeUserManageEntity = new StoreUserManageEntity();
        storeUserManageEntity.setRoleId(storeRoleDTO.getId());
        storeUserManageEntity.setStoreUserId(storeUserManageDTO.getStoreUserId());
        insert(storeUserManageEntity);

        userDetailRedis.logout(storeUserManageDTO.getStoreUserId());
    }
}
