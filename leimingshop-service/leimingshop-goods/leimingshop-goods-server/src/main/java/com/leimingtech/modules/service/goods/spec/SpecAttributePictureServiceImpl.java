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
import com.leimingtech.modules.dao.goods.spec.SpecAttributePictureDao;
import com.leimingtech.modules.dto.goods.spec.DefaultSpecPictureDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributePictureDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributePictureSaveDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecAttrPicEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品规格属性值与图片关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Service

public class SpecAttributePictureServiceImpl extends CrudServiceImpl<SpecAttributePictureDao, GoodsSpecAttrPicEntity, SpecAttributePictureDTO> implements SpecAttributePictureService {

    @Override
    public QueryWrapper<GoodsSpecAttrPicEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsSpecAttrPicEntity> wrapper = new QueryWrapper<>();
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
    public PageData<SpecAttributePictureDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID查询商品属性关联的图片信息
     *
     * @param id
     * @return
     */

    @Override
    public SpecAttributePictureDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 保存商品属性关联的图片信息
     *
     * @param dtoList
     */

    @Override
    public void save(@RequestBody List<SpecAttributePictureSaveDTO> dtoList) {
        List<GoodsSpecAttrPicEntity> goodsSpecAttrPicEntities = ConvertUtils.sourceToTarget(dtoList, GoodsSpecAttrPicEntity.class);
        insertBatch(goodsSpecAttrPicEntities);
    }

    /**
     * 修改商品属性的图片信息
     *
     * @param dto
     */

    @Override
    public void update(@RequestBody SpecAttributePictureDTO dto) {
        super.update(dto);
    }

    /**
     * 根据ID删除商品属性的图片信息
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 导出商品属性的图片信息
     *
     * @param params
     * @return
     */

    @Override
    public List<SpecAttributePictureDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 根据规格属性id删除图片
     *
     * @param ids
     */

    @Override
    public void deleteBySpecattrId(Long[] ids) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("spec_attr_id", ids);
        baseDao.delete(wrapper);
    }

    /**
     * 根据goodsid删除图片
     *
     * @param goodsId
     */

    @Override
    public void deleteByGoodsId(Long goodsId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("goods_id", goodsId);
        baseDao.delete(wrapper);
    }

    /**
     * 查询商品图片信息
     *
     * @param goodsId
     * @return
     */

    @Override
    public List<DefaultSpecPictureDTO> getDefaultPic(Long goodsId) {
        return baseDao.getDefaultPic(goodsId);
    }


    @Override
    public void saveBatch(@RequestParam("specAttributePictureSaveDTOS") List<SpecAttributePictureSaveDTO> specAttributePictureSaveDTOList) {

        baseDao.saveBatch(specAttributePictureSaveDTOList);
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
        baseDao.delete(Wrappers.<GoodsSpecAttrPicEntity>lambdaQuery()
                .in(GoodsSpecAttrPicEntity::getGoodsId, goodsIds)
                .eq(GoodsSpecAttrPicEntity::getDelFlag, DelFlagEnum.NORMAL.value()));

    }
}
