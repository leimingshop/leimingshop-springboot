/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodsclass.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.modules.dao.goodsclass.GoodsClassAttrDao;
import com.leimingtech.modules.dto.attribute.AttributeIdAndNameDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassAttrDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassUpdateDTO;
import com.leimingtech.modules.entity.goodsclass.GoodsClassAttrEntity;
import com.leimingtech.modules.service.attribute.AttributeService;
import com.leimingtech.modules.service.goodsclass.GoodsClassAttrService;
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
 * 商品分类和属性关联
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-03
 */
//@Service
@Service


public class GoodsClassAttrServiceImpl extends CrudServiceImpl<GoodsClassAttrDao, GoodsClassAttrEntity, GoodsClassAttrDTO> implements GoodsClassAttrService {
    @Resource
    private GoodsClassAttrDao goodsClassAttrDao;

    @Autowired

    private AttributeService attributeService;

    @Override
    public QueryWrapper<GoodsClassAttrEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        Long gcClassId = (Long) params.get("gcClassId");

        QueryWrapper<GoodsClassAttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(null != gcClassId, "gc_class_id", gcClassId);

        return wrapper;
    }

    /**
     * 批量保存商品分类关联规格
     *
     * @param dto
     */

    @Override
    public void insertBatch(@RequestBody GoodsClassUpdateDTO dto) {
        if (ArrayUtils.isNotEmpty(dto.getAttrIds())) {
            List<GoodsClassAttrEntity> goodsClassAttrEntities = new ArrayList<>();
            List<AttributeIdAndNameDTO> attributeIdAndNameDTOList = attributeService.selectNameBatchByids(dto.getAttrIds());
            for (AttributeIdAndNameDTO attributeIdAndNameDTO : attributeIdAndNameDTOList) {
                GoodsClassAttrEntity goodsClassAttrEntity = new GoodsClassAttrEntity();
                goodsClassAttrEntity.setAttrId(attributeIdAndNameDTO.getId());
                goodsClassAttrEntity.setAttrName(attributeIdAndNameDTO.getAttrName());
                goodsClassAttrEntity.setGcClassId(dto.getId());
                goodsClassAttrEntities.add(goodsClassAttrEntity);
            }
            this.insertBatch(goodsClassAttrEntities);
        }
    }

    /**
     * 修改商品分类关联规格
     *
     * @param dto
     */

    @Override
    public void updateGoodsClassAttr(@RequestBody GoodsClassUpdateDTO dto) {
        baseDao.delete(new QueryWrapper<GoodsClassAttrEntity>().eq("gc_class_id", dto.getId()));
        this.insertBatch(dto);
    }

    /**
     * 根据分类id查询关联属性数量
     *
     * @param gcId
     * @return
     */

    @Override
    public Integer findAttrByGcId(Long gcId) {
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
    public List<GoodsClassAttrDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

}