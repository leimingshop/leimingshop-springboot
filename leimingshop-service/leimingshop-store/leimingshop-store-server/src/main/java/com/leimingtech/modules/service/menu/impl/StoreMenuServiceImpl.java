/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.menu.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.SellerDetailRedis;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.menu.StoreMenuDao;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import com.leimingtech.modules.dto.menu.StoreMenuDTO;
import com.leimingtech.modules.dto.menu.StoreMenuResourceDTO;
import com.leimingtech.modules.entity.menu.StoreMenuEntity;
import com.leimingtech.modules.service.menu.StoreMenuService;
import com.leimingtech.modules.service.resource.StoreResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 店铺菜单表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service

public class StoreMenuServiceImpl extends CrudServiceImpl<StoreMenuDao, StoreMenuEntity, StoreMenuDTO> implements StoreMenuService {

    @Autowired

    private StoreResourceService storeResourceService;
    @Autowired
    private SellerDetailRedis sellerDetailRedis;

    @Override
    public QueryWrapper<StoreMenuEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreMenuEntity> wrapper = new QueryWrapper<>();
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

    public PageData<StoreMenuDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreMenuDTO get(Long id) {
        StoreMenuDTO storeMenuDTO = super.get(id);
        storeMenuDTO.setParentName(baseDao.getParentName(storeMenuDTO.getParentId()));
        List<StoreMenuResourceDTO> resourceList = storeResourceService.getResourceByMenuId(id);
        storeMenuDTO.setResourceList(resourceList);
        return storeMenuDTO;
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    @Override

    public void save(@RequestBody StoreMenuDTO dto) {
        StoreMenuEntity storeMenuEntity = ConvertUtils.sourceToTarget(dto, StoreMenuEntity.class);
        insert(storeMenuEntity);

        if (CollectionUtils.isNotEmpty(dto.getResourceList())) {
            //保存菜单资源
            storeResourceService.saveResource(storeMenuEntity.getId(), storeMenuEntity.getMenuName(), dto.getResourceList(), "save");
        }
    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody StoreMenuDTO dto) {
        super.update(dto);
        //更新菜单资源
        storeResourceService.saveResource(dto.getId(), dto.getMenuName(), dto.getResourceList(), "update");
    }


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
        storeResourceService.deleteByCode(ids[0]);
    }


    /**
     * 查询注册菜单
     *
     * @return
     */

    @Override
    public List<StoreMenuDTOs> findRegisterMenu() {
        return baseDao.findRegisterMenu();
    }

    /**
     * 查询用户默认首页菜单按钮
     *
     * @param roleId   角色ID
     * @param roleMark 0 普通 1 超级管理员
     * @return
     */

    @Override
    public List<StoreUserFunctionDTO> defaultMenu(@RequestParam("roleId") String roleId,
                                                  @RequestParam(value = "roleMark", required = false) Integer roleMark,
                                                  @RequestParam(value = "type", required = false) Integer type) {
        List<StoreUserFunctionDTO> storeUserFunctionDTOList = new ArrayList<>();
        if (type != null) {

            storeUserFunctionDTOList = baseDao.activityMenu();
        } else {
            storeUserFunctionDTOList = baseDao.defaultMenu(roleId, roleMark);
        }

        return storeUserFunctionDTOList;
    }
}
