/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodsclass.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.goodsclass.GoodsClassBrandDao;
import com.leimingtech.modules.dto.goodsclass.GoodsClassBrandDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassBrandSaveDTO;
import com.leimingtech.modules.entity.goodsclass.GoodsClassBrandEntity;
import com.leimingtech.modules.service.goodsclass.GoodsClassBrandService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品分类和品牌关联表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2019-06-25
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class GoodsClassBrandServiceImpl extends BaseServiceImpl<GoodsClassBrandDao, GoodsClassBrandEntity> implements GoodsClassBrandService {

//    @Override
//
//    public PageData<GoodsClassBrandDTO> page(@RequestParam Map<String, Object> params) {
//        IPage<GoodsClassBrandEntity> page = baseDao.selectPage(
//                getPage(params, Constant.UPDATE_DATE, false),
//                getWrapper(params)
//        );
//
//        return getPageData(page, GoodsClassBrandDTO.class);
//    }

    @Override

    public List<GoodsClassBrandDTO> list(Map<String, Object> params) {
        List<GoodsClassBrandEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, GoodsClassBrandDTO.class);
    }

    private QueryWrapper<GoodsClassBrandEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsClassBrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public GoodsClassBrandDTO get(String id) {
        GoodsClassBrandEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, GoodsClassBrandDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void saveBatch(@RequestBody List<GoodsClassBrandSaveDTO> dto, @RequestParam("goodsClassId") Long goodsClassId) {
        baseDao.delete(new QueryWrapper<GoodsClassBrandEntity>().eq("gc_class_id", goodsClassId));
        if (CollectionUtils.isNotEmpty(dto)) {
            for (GoodsClassBrandSaveDTO goodsClassBrandSaveDTO : dto) {
                goodsClassBrandSaveDTO.setGcClassId(goodsClassId);
            }
            List<GoodsClassBrandEntity> goodsClassBrandEntities = ConvertUtils.sourceToTarget(dto, GoodsClassBrandEntity.class);
            insertBatch(goodsClassBrandEntities);

        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody String[] ids) {
        //逻辑删除
        //logicDelete(ids, GoodsClassBrandEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 根据分类id查询数量
     *
     * @return
     */

    @Override
    public Integer findBrandByGcId(Long gcId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("gc_class_id", gcId);
        return baseDao.selectCount(wrapper);
    }


}
