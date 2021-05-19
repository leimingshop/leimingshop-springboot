/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.rolemenu.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.rolemenu.StoreRoleMenuDao;
import com.leimingtech.modules.dto.rolemenu.StoreRoleMenuDTO;
import com.leimingtech.modules.entity.rolemenu.StoreRoleMenuEntity;
import com.leimingtech.modules.service.rolemenu.StoreRoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 店铺角色菜单管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service
public class StoreRoleMenuServiceImpl extends CrudServiceImpl<StoreRoleMenuDao, StoreRoleMenuEntity, StoreRoleMenuDTO> implements StoreRoleMenuService {

    @Override
    public QueryWrapper<StoreRoleMenuEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreRoleMenuEntity> wrapper = new QueryWrapper<>();
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

    public PageData<StoreRoleMenuDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreRoleMenuDTO get(@PathVariable Long id) {
        return super.get(id);
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    @Override

    public void save(@RequestBody StoreRoleMenuDTO dto) {
        super.save(dto);
    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody StoreRoleMenuDTO dto) {
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
     * 保存角色菜单
     *
     * @param storeRoleMenuDTOList
     */
    @Override

    public void saveBatch(@RequestBody List<StoreRoleMenuDTO> storeRoleMenuDTOList) {
        List<StoreRoleMenuEntity> storeRoleMenuEntities = ConvertUtils.sourceToTarget(storeRoleMenuDTOList, StoreRoleMenuEntity.class);
        insertBatch(storeRoleMenuEntities);

    }

    /**
     * 更新角色菜单
     *
     * @param menuIdList 菜单集合
     * @param id         角色id
     */

    @Override
    public void updateRoleMenu(@RequestBody List<Long> menuIdList, @RequestParam("id") Long id) {
        Long[] ids = {id};
        // 先清除角色菜单
        baseDao.deleteByRoleId(ids);
        //再保存角色菜单
        List<StoreRoleMenuEntity> storeRoleMenuEntities = new ArrayList<>();
        for (Long menuId : menuIdList) {
            StoreRoleMenuEntity storeRoleMenuEntity = new StoreRoleMenuEntity();
            storeRoleMenuEntity.setRoleId(id);
            storeRoleMenuEntity.setMenuId(menuId);
            storeRoleMenuEntities.add(storeRoleMenuEntity);
        }
        insertBatch(storeRoleMenuEntities);

    }

    /**
     * 删除角色菜单
     *
     * @param ids 角色ID
     */

    @Override

    public void deleteByRoleId(@RequestBody Long[] ids) {
        baseDao.deleteByRoleId(ids);

    }

    /**
     * 根据角色ID 获取关联的菜单信息
     *
     * @param roleId 角色ID
     * @return
     */

    @Override
    public List<Long> getListByRoleId(@RequestParam("roleId") Long roleId) {
        return baseDao.getListByRoleId(roleId);
    }


}