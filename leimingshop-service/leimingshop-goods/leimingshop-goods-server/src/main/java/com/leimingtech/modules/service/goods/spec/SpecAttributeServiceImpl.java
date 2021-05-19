/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.goods.spec.SpecAttributeDao;
import com.leimingtech.modules.dto.goods.detail.SpecAttributeDetailDTO;
import com.leimingtech.modules.dto.goods.price.SpecAttrNameDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeAndValueSaveDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeSaveDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeUpdateDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecAttrEntity;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 商品规格属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Service

public class SpecAttributeServiceImpl extends CrudServiceImpl<SpecAttributeDao, GoodsSpecAttrEntity, SpecAttributeDTO> implements SpecAttributeService {


    @Autowired

    private SpecAttributeValueService specAttributeValueService;

    @Autowired
    private SpecAttributeDao specAttributeDao;

    @Override
    public QueryWrapper<GoodsSpecAttrEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsSpecAttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<SpecAttributeDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID查询商品规格属性
     *
     * @param id
     * @return
     */

    @Override
    public SpecAttributeDTO get(Long id) {
        return super.get(id);
    }


    @Override
    public List<SpecAttributeDetailDTO> getByGoodsId(Long goodsId) {
        return specAttributeDao.getByGoodsId(goodsId);
    }

    /**
     * 保存商品规格属性
     *
     * @param dtoList
     */

    @Override
    public void save(@RequestBody List<SpecAttributeSaveDTO> dtoList) {
        List<GoodsSpecAttrEntity> goodsSpecAttrEntities = ConvertUtils.sourceToTarget(dtoList, GoodsSpecAttrEntity.class);

        super.insertBatch(goodsSpecAttrEntities);
    }

    /**
     * 保存商品规格属性
     *
     * @param dtoList
     */

    @Override
    public void saveBatch(@RequestBody List<SpecAttributeAndValueSaveDTO> dtoList) {

        List<GoodsSpecAttrEntity> goodsSpecAttrEntities = new ArrayList<>();
        long specAttrId = 0L;
        for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : dtoList) {
            if (specAttributeAndValueSaveDTO.getAddType() == null || specAttributeAndValueSaveDTO.getAddType() == GoodsStatusEnum.GOODS_ADD_NEWS.value()) {
                long attrId = specAttributeAndValueSaveDTO.getSpecAttrId().longValue();
                if (specAttrId != attrId) {
                    specAttrId = attrId;
                    GoodsSpecAttrEntity goodsSpecAttrEntity = new GoodsSpecAttrEntity();
                    goodsSpecAttrEntity.setId(specAttributeAndValueSaveDTO.getSpecAttrId());
                    goodsSpecAttrEntity.setSpecAttrName(specAttributeAndValueSaveDTO.getSpecAttrName());
                    goodsSpecAttrEntity.setIsMain(specAttributeAndValueSaveDTO.getIsMain());
                    goodsSpecAttrEntity.setGoodsId(specAttributeAndValueSaveDTO.getGoodsId());
                    if (specAttributeAndValueSaveDTO.getIsSelect() == 1) {
                        goodsSpecAttrEntity.setIsSelect(specAttributeAndValueSaveDTO.getIsSelect());
                    }
                    goodsSpecAttrEntities.add(goodsSpecAttrEntity);
                } else {
                    for (GoodsSpecAttrEntity goodsSpecAttrEntity : goodsSpecAttrEntities) {
                        if (specAttrId == goodsSpecAttrEntity.getId() && null != specAttributeAndValueSaveDTO.getIsSelect() && specAttributeAndValueSaveDTO.getIsSelect() == 1) {
                            goodsSpecAttrEntity.setIsSelect(specAttributeAndValueSaveDTO.getIsSelect());
                        }
                    }
                }
            }
        }
        super.insertBatch(goodsSpecAttrEntities);
        specAttributeValueService.saveBatch(dtoList);
    }

    /**
     * 修改商品规格属性
     *
     * @param dtoList
     */

    @Override
    public void updateSpecAttrVal(@RequestBody List<SpecAttributeAndValueSaveDTO> dtoList) {
        Set<GoodsSpecAttrEntity> goodsSpecAttrEntities = new HashSet<>();
        Set<Long> ids = new HashSet<>();
        for (SpecAttributeAndValueSaveDTO dto : dtoList) {
            GoodsSpecAttrEntity goodsSpecAttrEntity = new GoodsSpecAttrEntity();
            goodsSpecAttrEntity.setId(dto.getSpecAttrId());
            goodsSpecAttrEntity.setSpecAttrName(dto.getSpecAttrName());
            goodsSpecAttrEntity.setIsMain(dto.getIsMain());
            goodsSpecAttrEntity.setIsSelect(0);
            if (dto.getIsSelect() == 1) {
                ids.add(dto.getSpecAttrId());
            }
            goodsSpecAttrEntities.add(goodsSpecAttrEntity);
            specAttributeValueService.updateSpecAttrVal(dto);
        }
        if (CollectionUtils.isNotEmpty(ids)) {
            for (Long id : ids) {
                for (GoodsSpecAttrEntity goodsSpecAttrEntity : goodsSpecAttrEntities) {
                    if (id.compareTo(goodsSpecAttrEntity.getId()) == 0) {
                        goodsSpecAttrEntity.setIsSelect(1);
                    }
                }
            }
        }
        super.updateBatchById(goodsSpecAttrEntities);
    }

    /**
     * 修改商品规格属性
     *
     * @param dtoList
     */

    @Override
    public void update(List<SpecAttributeUpdateDTO> dtoList) {
        if (!dtoList.isEmpty()) {
            Long goodsId = dtoList.get(0).getGoodsId();
            this.deleteByGoodsId(goodsId);
            List<GoodsSpecAttrEntity> goodsSpecAttrEntities = ConvertUtils.sourceToTarget(dtoList, GoodsSpecAttrEntity.class);
            insertBatch(goodsSpecAttrEntities);
        }
    }

    private void deleteByGoodsId(Long goodsId) {
        specAttributeDao.deleteByGoodsId(goodsId);
    }

    /**
     * 根据ID删除商品规格属性
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 导出商品规格属性
     *
     * @param params
     * @return
     */

    @Override
    public List<SpecAttributeDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 查询商品规格属性名集合
     *
     * @param goodsId
     * @return
     */

    @Override
    public List<SpecAttrNameDTO> getNameListByGoodsId(Long goodsId) {
        return baseDao.getNameListByGoodsId(goodsId);
    }

    /**
     * 功能描述 根据goodsId和specValue 查找goods选中的specAttr
     *
     * @param goodsId
     * @param specValues
     * @return com.leimingtech.modules.dto.goods.spec.SpecAttributeDTO
     * @author lishuo
     * @date 28/6/2020
     */

    @Override
    public List<SpecAttributeDTO> findSpecNameByGoodsId(@RequestParam("goodsId") Long goodsId, @RequestParam("specValues") List<String> specValues) {
        return baseDao.findSpecNameByGoodsId(goodsId, specValues);
    }


    @Override
    public void insertBatch(@RequestParam("specAttributeSaveTotalDTO") List<SpecAttributeSaveDTO> specAttributeSaveTotalDTO) {

        baseDao.insertBatch(specAttributeSaveTotalDTO);
    }

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    @Override
    public void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds) {
        baseDao.delete(Wrappers.<GoodsSpecAttrEntity>lambdaQuery()
                .in(GoodsSpecAttrEntity::getGoodsId, goodsIds)
                .eq(GoodsSpecAttrEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }

}
