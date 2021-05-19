/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.navigation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.navigation.NavigationDao;
import com.leimingtech.modules.dto.custom.GoodsClassCustomDetailDTO;
import com.leimingtech.modules.dto.navigation.AddNavigationDTO;
import com.leimingtech.modules.dto.navigation.NavigationDTO;
import com.leimingtech.modules.entity.navigation.NavigationEntity;
import com.leimingtech.modules.enums.adv.NavigationEnum;
import com.leimingtech.modules.service.custom.GoodsClassCustomService;
import com.leimingtech.modules.service.navigation.NavigationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 首页导航设置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-06-11
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class NavigationServiceImpl extends BaseServiceImpl<NavigationDao, NavigationEntity> implements NavigationService {


    @Autowired
    private GoodsClassCustomService goodsClassCustomService;

    /**
     * 导航分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<NavigationDTO> page(@RequestParam Map<String, Object> params) {
        IPage<NavigationEntity> page = baseDao.selectPage(
                getPage(params, "sort", true),
                getWrapper(params)
        );

        return getPageData(page, NavigationDTO.class);
    }

    /**
     * 查询所有
     *
     * @param params
     * @return
     */
    @Override

    public List<NavigationDTO> list(@RequestParam Map<String, Object> params) {
        List<NavigationEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, NavigationDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<NavigationEntity> getWrapper(Map<String, Object> params) {
        String title = (String) params.get("title");
        String storeId = (String) params.get("storeId");
        String orderBy = (String) params.get("orderBy");

        QueryWrapper<NavigationEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(storeId)) {
            wrapper.eq("store_id", storeId);
        } else {
            wrapper.isNull("store_id");
        }
        if (StringUtils.isNotBlank(orderBy)) {
            wrapper.orderByAsc("sort");
        }
        wrapper.like(StringUtils.isNotBlank(title), "title", title);
        return wrapper;
    }

    /**
     * 导航详情
     *
     * @param id
     * @return
     */
    @Override

    public NavigationDTO get(Long id) {
        NavigationEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, NavigationDTO.class);
    }

    /**
     * 保存导航配置
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody AddNavigationDTO dto) {
        NavigationEntity entity = ConvertUtils.sourceToTarget(dto, NavigationEntity.class);
        if (dto.getRelationType() == NavigationEnum.STORE_CLASS.value() && dto.getStoreId() == null) {
            GoodsClassCustomDetailDTO goodsClassCustomDetailDTO = goodsClassCustomService.selectDetailById(Long.valueOf(dto.getRelationParams()));
            entity.setRelationParams(goodsClassCustomDetailDTO.getClassId().toString());
        }

        insert(entity);
    }

    /**
     * 修改导航
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody NavigationDTO dto) {
        NavigationEntity entity = ConvertUtils.sourceToTarget(dto, NavigationEntity.class);
        if (dto.getRelationType() == NavigationEnum.STORE_CLASS.value() && dto.getStoreId() == null) {
            GoodsClassCustomDetailDTO goodsClassCustomDetailDTO = goodsClassCustomService.selectDetailById(Long.valueOf(dto.getRelationParams()));
            entity.setRelationParams(goodsClassCustomDetailDTO == null ? dto.getRelationParams() : goodsClassCustomDetailDTO.getClassId().toString());
        }
        updateById(entity);
    }

    /**
     * 删除导航
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}