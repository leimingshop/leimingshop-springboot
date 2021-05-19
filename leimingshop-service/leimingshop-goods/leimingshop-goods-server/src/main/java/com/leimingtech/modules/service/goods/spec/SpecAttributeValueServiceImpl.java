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
import com.leimingtech.modules.dao.goods.spec.SpecAttributeValueDao;
import com.leimingtech.modules.dto.goods.detail.SpecAttributePictureDetailDTO;
import com.leimingtech.modules.dto.goods.spec.*;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecAttrValueEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品规格属性值表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Service

public class SpecAttributeValueServiceImpl extends CrudServiceImpl<SpecAttributeValueDao, GoodsSpecAttrValueEntity, SpecAttributeValueDTO> implements SpecAttributeValueService {

    @Autowired
    private SpecAttributeRelationService specAttributeRelationService;

    @Autowired
    private SpecAttributePictureService specAttributePictureService;

    @Override
    public QueryWrapper<GoodsSpecAttrValueEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsSpecAttrValueEntity> wrapper = new QueryWrapper<>();
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
    public PageData<SpecAttributeValueDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID查询规格属性值
     *
     * @param id
     * @return
     */

    @Override
    public SpecAttributeValueDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 保存商品规格属性值
     *
     * @param dtoList
     */

    @Override
    public void save(@RequestBody List<SpecAttributeValueSaveDTO> dtoList) {

        List<GoodsSpecAttrValueEntity> goodsSpecAttrValueEntities = ConvertUtils.sourceToTarget(dtoList, GoodsSpecAttrValueEntity.class);
        insertBatch(goodsSpecAttrValueEntities);
        List<SpecAttributePictureSaveDTO> specAttributePictureSaveListDTO = null;
        List<SpecAttributeRelationDTO> specAttributeRelationDTOList = new ArrayList<>();
        for (SpecAttributeValueSaveDTO specAttributeValueSaveDTO : dtoList) {
            for (GoodsSpecAttrValueEntity goodsSpecAttrValueEntity : goodsSpecAttrValueEntities) {
                specAttributePictureSaveListDTO = specAttributeValueSaveDTO.getSpecAttributePictureSaveDTOList();
                for (SpecAttributePictureSaveDTO specAttributePictureSaveDTO : specAttributePictureSaveListDTO) {
                    specAttributePictureSaveDTO.setSpecAttrValueId(goodsSpecAttrValueEntity.getId());
                    specAttributePictureSaveDTO.setSpecAttrId(goodsSpecAttrValueEntity.getSpecAttrId());
                }
                if (specAttributeValueSaveDTO.getSpecAttrId().compareTo(goodsSpecAttrValueEntity.getSpecAttrId()) == 0) {
                    SpecAttributeRelationDTO specAttributeRelationDTO = new SpecAttributeRelationDTO();
                    specAttributeRelationDTO.setSpecId(specAttributeValueSaveDTO.getSpecId());
                    specAttributeRelationDTO.setSpecAttrId(goodsSpecAttrValueEntity.getSpecAttrId());
                    specAttributeRelationDTO.setSpecAttrValueId(goodsSpecAttrValueEntity.getId());
                    specAttributeRelationDTOList.add(specAttributeRelationDTO);
                }
            }
        }
        specAttributePictureService.save(specAttributePictureSaveListDTO);
        specAttributeRelationService.save(specAttributeRelationDTOList);
    }

    /**
     * 保存商品规格属性值
     *
     * @param dtoList
     */

    @Override
    public void saveBatch(@RequestBody List<SpecAttributeAndValueSaveDTO> dtoList) {
        ArrayList<GoodsSpecAttrValueEntity> goodsSpecAttrValueEntities = new ArrayList<>();
        for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : dtoList) {
            GoodsSpecAttrValueEntity goodsSpecAttrValueEntity = new GoodsSpecAttrValueEntity();
            goodsSpecAttrValueEntity.setId(specAttributeAndValueSaveDTO.getSpecAttrValueId());
            goodsSpecAttrValueEntity.setSpecAttrId(specAttributeAndValueSaveDTO.getSpecAttrId());
            goodsSpecAttrValueEntity.setGoodsId(specAttributeAndValueSaveDTO.getGoodsId());
            goodsSpecAttrValueEntity.setIsMain(specAttributeAndValueSaveDTO.getIsMain());
            goodsSpecAttrValueEntity.setIsSelect(specAttributeAndValueSaveDTO.getIsSelect());
            goodsSpecAttrValueEntity.setSpecAttrValue(specAttributeAndValueSaveDTO.getSpecAttrValue());
            goodsSpecAttrValueEntities.add(goodsSpecAttrValueEntity);
        }
        super.insertBatch(goodsSpecAttrValueEntities);
    }


    /**
     * 查询主规格对应的图片
     *
     * @param goodsId
     */

    @Override
    public List<SpecAttributePictureDetailDTO> getPicListByGoodsId(Long goodsId) {
        return baseDao.getPicListByGoodsId(goodsId);
    }

    /**
     * 修改商品规格属性值
     *
     * @param dto
     */

    @Override
    public void updateSpecAttrVal(SpecAttributeAndValueSaveDTO dto) {
        SpecAttributeValueDTO specAttributeValueDTO = new SpecAttributeValueDTO();
        specAttributeValueDTO.setId(dto.getSpecAttrValueId());
        specAttributeValueDTO.setSpecAttrId(dto.getSpecAttrId());
        specAttributeValueDTO.setGoodsId(dto.getGoodsId());
        specAttributeValueDTO.setIsMain(dto.getIsMain());
        specAttributeValueDTO.setIsSelect(dto.getIsSelect());
        specAttributeValueDTO.setSpecAttrValue(dto.getSpecAttrValue());
        super.update(specAttributeValueDTO);
    }

    /**
     * 功能描述 list 批量保存
     *
     * @param * @param specAttributeValueSaveDTOList
     * @return void
     * @author lishuo
     * @date 30/6/2020
     */
    @Override

    public void saveByList(@RequestParam("specAttributeValueSaveDTOList") List<SpecAttributeValueSaveDTO> specAttributeValueSaveDTOList) {

        baseDao.insertBatch(specAttributeValueSaveDTOList);
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
        baseDao.delete(Wrappers.<GoodsSpecAttrValueEntity>lambdaQuery()
                .in(GoodsSpecAttrValueEntity::getGoodsId, goodsIds)
                .eq(GoodsSpecAttrValueEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }

    /**
     * 修改商品规格属性值
     *
     * @param dto
     */

    @Override
    public void update(@RequestBody SpecAttributeValueDTO dto) {
        super.update(dto);
    }

    /**
     * 根据ID删除商品规格属性值
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 导出商品规格属性值
     *
     * @param params
     * @return
     */

    @Override
    public List<SpecAttributeValueDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

}
