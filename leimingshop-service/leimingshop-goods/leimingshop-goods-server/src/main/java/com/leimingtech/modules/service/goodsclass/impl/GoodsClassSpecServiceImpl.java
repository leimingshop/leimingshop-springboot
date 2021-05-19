/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodsclass.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.modules.dao.goodsclass.GoodsClassSpecDao;
import com.leimingtech.modules.dto.goodsclass.GoodsClassSpecDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassUpdateDTO;
import com.leimingtech.modules.dto.spec.SpecIdAndNameDTO;
import com.leimingtech.modules.entity.goodsclass.GoodsClassSpecEntity;
import com.leimingtech.modules.service.goodsclass.GoodsClassSpecService;
import com.leimingtech.modules.service.spec.SpecService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品分类和规格关联
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-03
 */
@Service

public class GoodsClassSpecServiceImpl extends CrudServiceImpl<GoodsClassSpecDao, GoodsClassSpecEntity, GoodsClassSpecDTO> implements GoodsClassSpecService {
    @Resource
    private GoodsClassSpecDao goodsClassSpecDao;

    @Autowired
    private SpecService specService;

    @Override
    public QueryWrapper<GoodsClassSpecEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        Long gcClassId = (Long) params.get("gcClassId");
        QueryWrapper<GoodsClassSpecEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(null != gcClassId, "gc_class_id", gcClassId);

        return wrapper;
    }

    /**
     * 批量保存商品分类关联属性
     *
     * @param dto
     */

    @Override
    public void insertBatch(@RequestBody GoodsClassUpdateDTO dto) {
        if (ArrayUtils.isNotEmpty(dto.getSpecIds())) {
            List<SpecIdAndNameDTO> specIdAndNameDTOList = specService.selectNameBatchByids(dto.getSpecIds());
            List<GoodsClassSpecEntity> goodsClassSpecEntities = new ArrayList<>();
            for (SpecIdAndNameDTO specIdAndNameDTO : specIdAndNameDTOList) {
                GoodsClassSpecEntity goodsClassSpecEntity = new GoodsClassSpecEntity();
                goodsClassSpecEntity.setSpecId(specIdAndNameDTO.getId());
                goodsClassSpecEntity.setSpecName(specIdAndNameDTO.getSpecName());
                goodsClassSpecEntity.setGcClassId(dto.getId());
                goodsClassSpecEntities.add(goodsClassSpecEntity);
            }
            this.insertBatch(goodsClassSpecEntities);
        }
    }

    /**
     * 修改商品分类关联规格
     *
     * @param dto
     */

    @Override
    public void updateGoodsClassSpec(@RequestBody GoodsClassUpdateDTO dto) {
        baseDao.delete(new QueryWrapper<GoodsClassSpecEntity>().eq("gc_class_id", dto.getId()));
        this.insertBatch(dto);
    }

    /**
     * 根据分类id查询关联数量
     *
     * @param gcId
     * @return
     */
    @Override
    public Integer findSpecByGcId(Long gcId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("gc_class_id", gcId);
        return baseDao.selectCount(wrapper);
    }

    /**
     * 根据分类ID查询关联规格
     *
     * @param params
     * @return
     */

    @Override
    public List<GoodsClassSpecDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }
}