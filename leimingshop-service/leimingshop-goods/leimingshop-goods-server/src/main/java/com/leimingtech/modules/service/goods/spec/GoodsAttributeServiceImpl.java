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
import com.leimingtech.modules.dao.goods.spec.GoodsAttributeDao;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeInfoDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeSaveDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsAttributeEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Service
public class GoodsAttributeServiceImpl extends CrudServiceImpl<GoodsAttributeDao, GoodsAttributeEntity, GoodsAttributeDTO> implements GoodsAttributeService {


    @Override
    public QueryWrapper<GoodsAttributeEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsAttributeEntity> wrapper = new QueryWrapper<>();
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
    public PageData<GoodsAttributeDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID查询商品属性
     *
     * @param goodsId
     * @return
     */

    @Override
    public List<GoodsAttributeInfoDTO> getByGoodsId(Long goodsId) {

        List<GoodsAttributeEntity> goodsAttributeEntities = this.baseDao.selectList(Wrappers.<GoodsAttributeEntity>lambdaQuery()
                .eq(GoodsAttributeEntity::getGoodsId, goodsId)
                .select(GoodsAttributeEntity::getId, GoodsAttributeEntity::getAttrName,
                        GoodsAttributeEntity::getAttrValue, GoodsAttributeEntity::getAttributeId,
                        GoodsAttributeEntity::getGoodsId, GoodsAttributeEntity::getShowType));

        return ConvertUtils.sourceToTarget(goodsAttributeEntities, GoodsAttributeInfoDTO.class);
    }

    /**
     * 保存商品属性
     *
     * @param dtoList
     */

    @Override
    public void save(@RequestBody List<GoodsAttributeSaveDTO> dtoList) {
        if (!CollectionUtils.isEmpty(dtoList)) {
            List<GoodsAttributeEntity> goodsAttributeEntities = ConvertUtils.sourceToTarget(dtoList, GoodsAttributeEntity.class);
            insertBatch(goodsAttributeEntities);
        }
    }

    /**
     * 修改商品属性
     *
     * @param dtoList
     */

    @Override
    public void update(@RequestBody List<GoodsAttributeSaveDTO> dtoList) {
        if (!dtoList.isEmpty()) {
            this.deleteByGoodsId(dtoList.get(0).getGoodsId());
            List<GoodsAttributeEntity> goodsAttributeEntities = ConvertUtils.sourceToTarget(dtoList, GoodsAttributeEntity.class);
            insertBatch(goodsAttributeEntities);
        }
    }

    /**
     * 根据商品Id 删除属性信息
     *
     * @param goodsId
     */

    @Override
    public void deleteByGoodsId(Long goodsId) {
        baseDao.delete(new QueryWrapper<GoodsAttributeEntity>().eq("goods_id", goodsId));
    }

    /**
     * 功能描述 根据goodsId 查找属性和属性值
     *
     * @param goodsId goodsId
     * @return java.util.List<com.leimingtech.modules.dto.goods.spec.GoodsAttributeDTO>
     * @author lishuo
     * @date 27/6/2020
     */

    @Override
    public List<GoodsAttributeDTO> findAttributeByGoodsId(Long goodsId) {
        QueryWrapper<GoodsAttributeEntity> goodsAttributeEntityQueryWrapper = new QueryWrapper<>();
        goodsAttributeEntityQueryWrapper.eq("goods_id", goodsId);
        List<GoodsAttributeEntity> goodsAttributeEntities = baseDao.selectList(goodsAttributeEntityQueryWrapper);
        List<GoodsAttributeDTO> list = new ArrayList<>();
        for (GoodsAttributeEntity goodsAttributeEntity : goodsAttributeEntities) {
            GoodsAttributeDTO goodsAttributeDTO = new GoodsAttributeDTO();
            goodsAttributeDTO.setAttrName(goodsAttributeEntity.getAttrName());
            goodsAttributeDTO.setAttrValue(goodsAttributeEntity.getAttrValue());
            list.add(goodsAttributeDTO);
        }
        return list;
    }

    @Override

    public void saveBatch(@RequestParam("attributeSaveDTOList") List<GoodsAttributeSaveDTO> attributeSaveDTOList) {

        baseDao.saveBatch(attributeSaveDTOList);
    }


    @Override
    public void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds) {
        baseDao.delete(Wrappers.<GoodsAttributeEntity>lambdaQuery()
                .in(GoodsAttributeEntity::getGoodsId, goodsIds)
                .eq(GoodsAttributeEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }

    /**
     * 根据ID删除商品属性
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 导出商品属性
     *
     * @param params
     * @return
     */

    @Override
    public List<GoodsAttributeDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

}
