/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.index.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.index.StoreUserFunctionDao;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import com.leimingtech.modules.entity.index.StoreUserFunctionEntity;
import com.leimingtech.modules.service.index.StoreUserFunctionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户首页按钮配置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-03-16
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class StoreUserFunctionServiceImpl extends BaseServiceImpl<StoreUserFunctionDao, StoreUserFunctionEntity> implements StoreUserFunctionService {
    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<StoreUserFunctionDTO> page(@RequestParam Map<String, Object> params) {
        IPage<StoreUserFunctionEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, StoreUserFunctionDTO.class);
    }

    /**
     * 条件查询相关信息
     *
     * @param params
     * @return
     */
    @Override

    public List<StoreUserFunctionDTO> list(@RequestParam Map<String, Object> params) {
        List<StoreUserFunctionDTO> entityList = baseDao.getList(params);

        return entityList;
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<StoreUserFunctionEntity> getWrapper(Map<String, Object> params) {
        String userId = params.get("userId").toString();

        QueryWrapper<StoreUserFunctionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(userId), "user_id", userId);

        return wrapper;
    }

    /**
     * 获取信息
     *
     * @param id
     * @return
     */
    @Override

    public StoreUserFunctionDTO get(Long id) {
        StoreUserFunctionEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, StoreUserFunctionDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody StoreUserFunctionDTO dto) {
        StoreUserFunctionEntity entity = ConvertUtils.sourceToTarget(dto, StoreUserFunctionEntity.class);

        insert(entity);
    }

    /**
     * 更新
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody StoreUserFunctionDTO dto) {
        StoreUserFunctionEntity entity = ConvertUtils.sourceToTarget(dto, StoreUserFunctionEntity.class);

        updateById(entity);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, StoreUserFunctionEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 批量保存首页菜单按钮
     *
     * @param
     */

    @Override
    public void saveBatch(@RequestParam("userId") Long userId, @RequestBody List<Long> menuIds) {
        baseDao.deleteByUserId(userId);

        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        List<StoreUserFunctionEntity> list = new ArrayList<>();
        menuIds.stream().forEach((menuid) -> {
            StoreUserFunctionEntity userFunctionEntity = new StoreUserFunctionEntity();
            userFunctionEntity.setUserId(userId);
            userFunctionEntity.setMenuId(menuid);
            list.add(userFunctionEntity);
        });
        super.insertBatch(list);

    }
}