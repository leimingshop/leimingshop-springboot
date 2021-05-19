/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.role.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.role.StoreRoleDao;
import com.leimingtech.modules.dto.role.SaveStoreRoleDTO;
import com.leimingtech.modules.dto.role.StoreRoleDTO;
import com.leimingtech.modules.dto.role.UpdateStoreRoleDTO;
import com.leimingtech.modules.dto.rolemenu.StoreRoleMenuDTO;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import com.leimingtech.modules.entity.role.StoreRoleEntity;
import com.leimingtech.modules.service.role.StoreRoleService;
import com.leimingtech.modules.service.rolemenu.StoreRoleMenuService;
import com.leimingtech.modules.service.usermanage.StoreUserManageService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 店铺角色表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service

public class StoreRoleServiceImpl extends CrudServiceImpl<StoreRoleDao, StoreRoleEntity, StoreRoleDTO> implements StoreRoleService {

    @Autowired

    private StoreRoleMenuService storeRoleMenuService;
    @Autowired

    private StoreUserManageService storeUserManageService;

    @Override
    public QueryWrapper<StoreRoleEntity> getWrapper(Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        String roleMark = (String) params.get("roleMark");

        QueryWrapper<StoreRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(roleMark), "role_Mark", roleMark);
        wrapper.like(StringUtils.isNotBlank(roleName), "role_name", roleName);

        return wrapper;
    }

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<StoreRoleDTO> page(@RequestParam Map<String, Object> params) {

        // 分页
        IPage<StoreRoleEntity> page = getPage(params, "lsr.create_date", false);
        // 查询
        List<StoreRoleDTO> list = baseDao.findPage(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreRoleDTO get(@PathVariable Long id) {
        return super.get(id);
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    @Override

    public void save(@RequestBody SaveStoreRoleDTO dto) {
        // 新增角色
        StoreRoleEntity storeRoleEntity = ConvertUtils.sourceToTarget(dto, StoreRoleEntity.class);
        insert(storeRoleEntity);
        // 新增角色店铺关联
        StoreUserManageDTO storeUserManageDTO = new StoreUserManageDTO();
        storeUserManageDTO.setStoreId(dto.getStoreId());
        storeUserManageDTO.setRoleId(storeRoleEntity.getId());
        storeUserManageService.save(storeUserManageDTO);
        // 新增角色菜单
        List<Long> menuIdList = dto.getMenuIdList();

        if (CollectionUtils.isNotEmpty(menuIdList)) {
            List<StoreRoleMenuDTO> storeRoleMenuDTOList = new ArrayList<>();
            for (Long menuId : menuIdList) {
                StoreRoleMenuDTO storeRoleMenuDTO = new StoreRoleMenuDTO();
                storeRoleMenuDTO.setMenuId(menuId);
                storeRoleMenuDTO.setRoleId(storeRoleEntity.getId());
                storeRoleMenuDTOList.add(storeRoleMenuDTO);
            }
            storeRoleMenuService.saveBatch(storeRoleMenuDTOList);
        }


    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody UpdateStoreRoleDTO dto) {
        StoreRoleDTO storeRoleDTO = ConvertUtils.sourceToTarget(dto, StoreRoleDTO.class);
        super.update(storeRoleDTO);
        // 更新角色菜单
        if (CollectionUtils.isNotEmpty(dto.getMenuIdList())) {
            storeRoleMenuService.updateRoleMenu(dto.getMenuIdList(), dto.getId());
        }
    }


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        // 删除角色
        logicDelete(ids);
        // 删除角色菜单
        storeRoleMenuService.deleteByRoleId(ids);
        // 删除店铺角色关联

    }

    /**
     * 查询角色名称是否重复
     *
     * @param roleName
     * @return
     */

    @Override
    public Integer checkRoleName(@RequestParam("roleName") String roleName,
                                 @RequestParam("storeId") Long storeId,
                                 @RequestParam(value = "roleId", required = false) Long roleId) {

        return baseDao.checkRoleName(roleName, storeId, roleId);
    }

    /**
     * 新增角色，
     *
     * @param storeRoleEntity
     */

    @Override
    public void saveRole(@RequestBody StoreRoleDTO storeRoleEntity) {
        insert(ConvertUtils.sourceToTarget(storeRoleEntity, StoreRoleEntity.class));

    }

    /**
     * 根据店铺ID 获取角色信息
     *
     * @param storeId
     * @return
     */

    @Override
    public UpdateStoreRoleDTO getRoleByStoreId(@RequestParam("storeId") Long storeId) {

        return baseDao.getRoleByStoreId(storeId);
    }

    /**
     * 根据用户ID  查询角色信息
     *
     * @param userId
     * @return
     */

    @Override
    public StoreUserManageDTO getByUserId(@RequestParam("userId") Long userId) {
        return baseDao.getByUserId(userId);
    }

    /**
     * 查询是否有首页菜单
     *
     * @param roleId
     * @return
     */

    @Override
    public Integer getIndexMenu(@RequestParam("roleId") String roleId) {
        return baseDao.getIndexMenu(roleId);
    }
}