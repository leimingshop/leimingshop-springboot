/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.goods.spec.SpecAttributeRelationDao;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeRelationDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecAttrRelEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品规格属性与属性值关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Service
public class SpecAttributeRelationServiceImpl extends CrudServiceImpl<SpecAttributeRelationDao, GoodsSpecAttrRelEntity, SpecAttributeRelationDTO> implements SpecAttributeRelationService {

    @Override
    public QueryWrapper<GoodsSpecAttrRelEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsSpecAttrRelEntity> wrapper = new QueryWrapper<>();
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
    public PageData<SpecAttributeRelationDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID查询商品属性
     *
     * @param id
     * @return
     */

    @Override
    public SpecAttributeRelationDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 保存商品属性
     *
     * @param dtoList
     */

    @Override
    public void save(@RequestBody List<SpecAttributeRelationDTO> dtoList) {
        List<GoodsSpecAttrRelEntity> goodsSpecAttrRelEntities = ConvertUtils.sourceToTarget(dtoList, GoodsSpecAttrRelEntity.class);
        insertBatch(goodsSpecAttrRelEntities);
    }

    /**
     * 修改商品属性
     *
     * @param dto
     */

    @Override
    public void update(@RequestBody SpecAttributeRelationDTO dto) {
        super.update(dto);
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
    public List<SpecAttributeRelationDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 根据属性值id查询关联数据
     *
     * @param ids
     * @return
     */
    @Override

    public List<SpecAttributeRelationDTO> selectSpecIdBySpecAttrValueId(@RequestBody Long[] ids) {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", DelFlagEnum.NORMAL.value());
        wrapper.in("spec_attr_value_id", ids);
        wrapper.groupBy("spec_id");
        List<GoodsSpecAttrRelEntity> list = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(list, SpecAttributeRelationDTO.class);
    }

    /**
     * 根据属性值判断是否为原有属性
     *
     * @param specValueIdList
     * @param num
     * @return
     */

    @Override
    public Long selectSpecId(@RequestBody List<Long> specValueIdList, @RequestParam("num") Integer num) {

        return baseDao.selectSpecId(specValueIdList, num);
    }

    /**
     * 根据规格属性id查询是否存在   存在返回false
     *
     * @param specAttributeModifyId
     * @return
     */

    @Override
    public boolean selectBySpecAttrId(Long specAttributeModifyId) {
        QueryWrapper<GoodsSpecAttrRelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("spec_attr_id", specAttributeModifyId);
        List<GoodsSpecAttrRelEntity> goodsSpecAttrRelEntities = baseDao.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(goodsSpecAttrRelEntities)) {
            return false;
        }
        return true;
    }

    /**
     * 根据规格id删除中间表
     *
     * @param specIds
     */

    @Override
    public void deleteBySpecId(@RequestBody Long[] specIds) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("spec_id", specIds);
        baseDao.delete(wrapper);
    }

    /**
     * 根据商品id删除
     *
     * @param goodsId
     */

    @Override
    public void deleteByGoodsId(Long goodsId) {
        QueryWrapper<GoodsSpecAttrRelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodsId);
        baseDao.delete(wrapper);
    }

    /**
     * 获取先有属性数量
     *
     * @param goodsId
     */

    @Override
    public Integer selectCountBygoodsId(@RequestParam("goodsId") Long goodsId) {
        QueryWrapper<GoodsSpecAttrRelEntity> wrapper = new QueryWrapper();
        wrapper.eq("goods_id", goodsId);
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.groupBy("spec_attr_id");
        List<GoodsSpecAttrRelEntity> goodsSpecAttrRelEntities = baseDao.selectList(wrapper);
        Integer num = 0;
        if (CollectionUtils.isNotEmpty(goodsSpecAttrRelEntities)) {
            num = goodsSpecAttrRelEntities.size();
        }
        return num;
    }


    @Override
    public void saveBatch(@RequestParam("specAttributeRelationDTOS") List<SpecAttributeRelationDTO> specAttributeRelationDTOList) {

        baseDao.saveBatch(specAttributeRelationDTOList);
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
        baseDao.delete(Wrappers.<GoodsSpecAttrRelEntity>lambdaQuery()
                .in(GoodsSpecAttrRelEntity::getGoodsId, goodsIds)
                .eq(GoodsSpecAttrRelEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }
}
