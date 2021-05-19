/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.user.fuction.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.dao.user.function.UserFunctionDao;
import com.leimingtech.dto.index.IndexUserFunctionDTO;
import com.leimingtech.dto.user.function.UserFunctionDTO;
import com.leimingtech.dto.user.function.UserFunctionInfoDTO;
import com.leimingtech.entity.user.function.UserFunctionEntity;
import com.leimingtech.service.sys.SysMenuService;
import com.leimingtech.service.user.function.UserFunctionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-16
 */
@Service
@Transactional

@Slf4j
public class UserFunctionServiceImpl extends BaseServiceImpl<UserFunctionDao, UserFunctionEntity> implements UserFunctionService {
    @Autowired

    private SysMenuService sysMenuService;

    @Override

    public PageData<UserFunctionDTO> page(@RequestParam Map<String, Object> params) {
        IPage<UserFunctionEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, UserFunctionDTO.class);
    }

    /**
     * 不分页
     *
     * @param params map
     * @return List<UserFunctionDTO> 集合
     * @date 2020/4/7/007 11:48
     * @author xuzhch
     */
    @Override

    public List<UserFunctionDTO> list(Map<String, Object> params) {
        List<UserFunctionEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, UserFunctionDTO.class);
    }

    private QueryWrapper<UserFunctionEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<UserFunctionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    /**
     * 查询详情
     *
     * @param id 常用功能ID
     * @return UserFunctionDTO 数据
     * @date 2020/4/7/007 11:49
     * @author xuzhch
     */
    @Override

    public UserFunctionDTO get(Long id) {
        UserFunctionEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, UserFunctionDTO.class);
    }

    /**
     * 保存常用功能
     *
     * @param dto UserFunctionDTO
     * @date 2020/4/7/007 11:49
     * @author xuzhch
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody UserFunctionDTO dto) {
        UserFunctionEntity entity = ConvertUtils.sourceToTarget(dto, UserFunctionEntity.class);

        insert(entity);
    }

    /**
     * 修改常用功能
     *
     * @param dto UserFunctionDTO
     * @date 2020/4/7/007 11:49
     * @author xuzhch
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody UserFunctionDTO dto) {
        UserFunctionEntity entity = ConvertUtils.sourceToTarget(dto, UserFunctionEntity.class);

        updateById(entity);
    }

    /**
     * 删除常用功能
     *
     * @param ids 常用功能ID数组
     * @date 2020/4/7/007 11:51
     * @author xuzhch
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, UserFuctionEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 批量保存常用功能
     *
     * @param userId  用户ID
     * @param menuIds 菜单ID集合
     * @date 2020/4/7/007 11:52
     * @author xuzhch
     */

    @Override
    public void saveBatch(@RequestParam("userId") Long userId, @RequestBody List<Long> menuIds) {
        baseDao.delete(Wrappers.<UserFunctionEntity>lambdaQuery().eq(UserFunctionEntity::getUserId, userId));
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        List<UserFunctionEntity> list = new ArrayList<>();
        menuIds.stream().forEach((menuid) -> {
            UserFunctionEntity userFunctionEntity = new UserFunctionEntity();
            userFunctionEntity.setUserId(userId);
            userFunctionEntity.setMenuId(menuid);
            list.add(userFunctionEntity);
        });
        super.insertBatch(list);
    }

    /**
     * 批量修改常用功能
     *
     * @param dtos List<UserFunctionDTO>
     * @date 2020/4/7/007 11:52
     * @author xuzhch
     */

    @Override
    public void updateBatch(@RequestBody List<UserFunctionDTO> dtos) {
        List<UserFunctionEntity> userFunctionEntities = ConvertUtils.sourceToTarget(dtos, UserFunctionEntity.class);
        for (UserFunctionEntity userFunctionEntity : userFunctionEntities) {
            if (userFunctionEntity.getId() != null) {
                super.updateById(userFunctionEntity);
            } else {
                baseDao.insert(userFunctionEntity);
            }
        }
    }

    /**
     * 获取用户常用功能
     *
     * @param userId 用户ID
     * @return List<UserFunctionInfoDTO> 当前用户关联的常用功能
     * @date 2020/4/7/007 11:53
     * @author xuzhch
     */

    @Override
    public List<UserFunctionInfoDTO> getUserFunctionByUserId(Long userId) {
        return baseDao.selectUserFunctionByUserId(userId);
    }

    /**
     * 首页常用功能
     *
     * @param userDetail 用户信息
     * @return List<IndexUserFunctionDTO> 常用功能数据
     * @date 2020/4/7/007 11:46
     * @author xuzhch
     */

    @Override
    public List<IndexUserFunctionDTO> getListByUserId(Long userId) {
        return baseDao.selectListByUserId(userId, HttpContextUtils.getLanguage());
    }

//
//    @Override
//    public List<UserFunctionInfoDTO> getAllUserFunctionByUserId(@RequestBody UserDetail userDetail) {
//        List<SysMenuDTO> userMenuList = sysMenuService.getUserMenuList(userDetail.getSuperAdmin(), userDetail.getId(), null);
//        log.info(JSON.toJSONString(userMenuList));
//    return null;
//    }
}