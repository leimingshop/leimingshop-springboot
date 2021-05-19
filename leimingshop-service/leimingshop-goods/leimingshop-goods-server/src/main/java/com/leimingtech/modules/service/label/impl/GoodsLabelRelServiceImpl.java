/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.label.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.label.GoodsLabelRelDao;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelSaveDTO;
import com.leimingtech.modules.entity.label.GoodsLabelRelEntity;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.goodslabel.GoodsLabelRelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品与标签关联表
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-13
 */
@Service

public class GoodsLabelRelServiceImpl extends CrudServiceImpl<GoodsLabelRelDao, GoodsLabelRelEntity, GoodsLabelRelDTO> implements GoodsLabelRelService {


    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    /**
     * @Author weixianchun
     * @Description 关联列表查询
     * @Param params
     * @Date 2019/12/17 10:37
     * @Return java.util.List<com.leimingtech.modules.dto.goodslable.GoodsLabelRelDTO>
     * @version 1.0
     */
    @Override

    public List<GoodsLabelRelDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    @Override
    public QueryWrapper<GoodsLabelRelEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        Object labelId = params.get("labelId");
        String goodsId = (String) params.get("goodsId");

        QueryWrapper<GoodsLabelRelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(null != labelId, "label_id", labelId);
        wrapper.eq(StringUtils.isNotBlank(goodsId), "goods_id", goodsId);

        return wrapper;
    }


    /**
     * @Author weixianchun
     * @Description 查询标签关联的商品数量, 店铺数量
     * @Param goodsId
     * @Param labelId
     * @Date 2019/12/13 17:11
     * @Return com.leimingtech.modules.dto.goodslable.GoodsLabelRelDTO
     * @version 1.0
     */

    @Override
    public GoodsLabelRelDTO findNumGoodsAndStore(@RequestParam("goodsId") Long goodsId, @RequestParam("labelId") Long labelId) {
        return baseDao.findNumGoodsAndStore(goodsId, labelId);
    }

    /**
     * @Author weixianchun
     * @Description 批量保存商品标签关联信息
     * @Param dtoList
     * @Date 2019/12/11 9:26
     * @Return boolean
     * @version 1.0
     */

    @Override
    public boolean insertBatch(@RequestBody List<GoodsLabelRelSaveDTO> dtoList) {
        List<GoodsLabelRelEntity> goodsLabelRelEntities = ConvertUtils.sourceToTarget(dtoList, GoodsLabelRelEntity.class);
        return this.insertBatch(goodsLabelRelEntities, 100);
    }

    /**
     * @Author weixianchun
     * @Description 根据商品ID删除数据
     * @Param labelId
     * @Date 2019/12/12 10:43
     * @Return int
     * @version 1.0
     */

    @Override
    public int deleteByGoodsId(Long goodsId) {
        return baseDao.deleteByGoodsId(goodsId);
    }

    /**
     * @Author weixianchun
     * @Description 根据标签ID查询商品列表
     * @Param labelId
     * @Date 2019/12/17 16:56
     * @Return java.util.List<com.leimingtech.modules.dto.goods.GoodsDTO>
     * @version 1.0
     */

    @Override
    public List<GoodsDTO> findGoodsByLabelId(@RequestParam(("labelId")) Long labelId) {
        return baseDao.findGoodsByLabelId(labelId);
    }

    /**
     * 根据商品Id 查找标签名称
     *
     * @param goodsId
     * @return
     */

    @Override
    public String selectLabelNameByGoodsId(Long goodsId) {
        List<GoodsLabelRelEntity> goodsLabelRelEntities = baseDao.selectList(Wrappers.<GoodsLabelRelEntity>lambdaQuery()
                .eq(GoodsLabelRelEntity::getGoodsId, goodsId)
                .eq(GoodsLabelRelEntity::getDelFlag, 0)
                .orderByDesc(GoodsLabelRelEntity::getUpdateDate));
        if (CollectionUtils.isNotEmpty(goodsLabelRelEntities)) {
            return goodsLabelRelEntities.get(0).getLabelName();
        }
        return "";
    }

    /**
     * 功能描述 批量保存 手写sql
     *
     * @param * @param goodsLabelRelSaveDTOList
     * @return void
     * @author lishuo
     * @date 22/7/2020
     */
    @Override

    public void saveBatch(@RequestBody List<GoodsLabelRelSaveDTO> goodsLabelRelSaveDTOList) {

        baseDao.saveBatch(goodsLabelRelSaveDTOList);
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
        baseDao.delete(Wrappers.<GoodsLabelRelEntity>lambdaQuery()
                .in(GoodsLabelRelEntity::getGoodsId, goodsIds)
                .eq(GoodsLabelRelEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }


    @Override
    public void deleteBatchByLabelIds(@RequestBody Long[] ids) {
        List<GoodsLabelRelEntity> goodsLabelRelEntities = baseDao.selectList(Wrappers.<GoodsLabelRelEntity>lambdaQuery()
                .in(GoodsLabelRelEntity::getLabelId, ids)
                .eq(GoodsLabelRelEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        List<Long> goodsIds = goodsLabelRelEntities.stream().map(GoodsLabelRelEntity::getGoodsId).collect(Collectors.toList());
        baseDao.delete(Wrappers.<GoodsLabelRelEntity>lambdaQuery()
                .in(GoodsLabelRelEntity::getLabelId, ids)
                .eq(GoodsLabelRelEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        goodsSyncIndexService.batchGoodsIndexSync(goodsIds);
    }

}
