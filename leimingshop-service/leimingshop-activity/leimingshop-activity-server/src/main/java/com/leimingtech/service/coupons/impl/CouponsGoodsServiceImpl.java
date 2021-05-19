/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.coupons.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.coupons.CouponsGoodsDao;
import com.leimingtech.modules.dto.coupons.CouponsGoodsDTO;
import com.leimingtech.modules.entity.coupons.CouponsGoodsEntity;
import com.leimingtech.modules.service.coupons.CouponsGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 优惠券活动商品service
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CouponsGoodsServiceImpl extends BaseServiceImpl<CouponsGoodsDao, CouponsGoodsEntity> implements CouponsGoodsService {

    @Override

    public PageData<CouponsGoodsDTO> page(@RequestParam Map<String, Object> params) {
        IPage<CouponsGoodsEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, CouponsGoodsDTO.class);
    }

    @Override

    public List<CouponsGoodsDTO> list(Map<String, Object> params) {
        List<CouponsGoodsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, CouponsGoodsDTO.class);
    }

    private QueryWrapper<CouponsGoodsEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<CouponsGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public CouponsGoodsDTO get(Long id) {
        CouponsGoodsEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, CouponsGoodsDTO.class);
    }

    /**
     * 功能描述:
     * 〈获取活动关联的商品集合〉
     *
     * @param activityId 优惠券活动id
     * @author : 刘远杰
     */

    @Override
    public List<CouponsGoodsDTO> getByActivityId(Long activityId) {
        QueryWrapper<CouponsGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        List<CouponsGoodsEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, CouponsGoodsDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody CouponsGoodsDTO dto) {
        CouponsGoodsEntity entity = ConvertUtils.sourceToTarget(dto, CouponsGoodsEntity.class);

        insert(entity);
    }

    /**
     * 功能描述:
     * 〈优惠券商品批量保存〉
     *
     * @param dtoList 优惠券商品集合
     * @author : 刘远杰
     */

    @Override
    public Boolean saveBatch(@RequestBody List<CouponsGoodsDTO> dtoList) {
        List<CouponsGoodsEntity> entityList = ConvertUtils.sourceToTarget(dtoList, CouponsGoodsEntity.class);
        return super.insertBatch(entityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody CouponsGoodsDTO dto) {
        CouponsGoodsEntity entity = ConvertUtils.sourceToTarget(dto, CouponsGoodsEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, CouponsGoodsEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * 〈根据优惠券活动id物理删除优惠券商品〉
     *
     * @param activityId 优惠券活动id
     * @author : 刘远杰
     */

    @Override
    public void noLogicDeleteByActivityId(@RequestParam("activityId") Long activityId) {
        baseDao.noLogicDeleteByActivityId(activityId);
    }

    /**
     * 功能描述:
     * 〈根据优惠券活动id逻辑删除优惠券商品〉
     *
     * @param activityId 优惠券活动id
     * @author : 刘远杰
     */

    @Override
    public void deleteByActivityId(@RequestParam("activityId") Long activityId) {
        UpdateWrapper<CouponsGoodsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                .eq("activity_id", activityId);
        baseDao.delete(updateWrapper);
    }

}
