/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.recommend.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.dao.recommend.CartRecommendDao;
import com.leimingtech.modules.dto.goods.GoodsRecommendDTO;
import com.leimingtech.modules.dto.recommend.*;
import com.leimingtech.modules.entity.recommend.CartRecommendEntity;
import com.leimingtech.modules.service.recommend.CartRecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 购物车推荐商品表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-6
 */
@Service
@Slf4j

public class CartRecommendServiceImpl extends CrudServiceImpl<CartRecommendDao, CartRecommendEntity, CartRecommendDTO> implements CartRecommendService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 商品推荐分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<CartRecommendPageDTO> pageList(@RequestParam Map<String, Object> params) {
        IPage<CartRecommendEntity> page = getPage(params, null, false);
        List<CartRecommendPageDTO> list = baseDao.pageList(page, params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 批量更新商品排序
     *
     * @param list
     */

    @Override
    public void updateSort(@RequestBody List<UpdateCartRecommendDTO> list) {
        List<CartRecommendEntity> cartRecommendEntities = ConvertUtils.sourceToTarget(list, CartRecommendEntity.class);
        updateBatchById(cartRecommendEntities);
        this.updateRedisCache();

    }

    private void updateRedisCache() {
        String key = RedisConstants.RECOMMEND_GOODS;
        redisUtils.delete(key);
        List<GoodsRecommendDTO> list = baseDao.findList();
        if (CollectionUtils.isNotEmpty(list)) {
            redisUtils.set(key, list, RedisConstants.JEDIS_EXPIRE);
        }
    }

    /**
     * 删除推荐商品
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
        this.updateRedisCache();
    }

    /**
     * 保存推荐商品
     *
     * @param list
     */

    @Override
    public void save(@RequestBody List<SaveCartRecommendDTO> list) {
        List<CartRecommendEntity> cartRecommendEntities = ConvertUtils.sourceToTarget(list, CartRecommendEntity.class);
        insertBatch(cartRecommendEntities);
        this.updateRedisCache();

    }

    /**
     * 查询商品列表
     *
     * @return
     */

    @Override
    public PageData<GoodsPageDTO> goodsPage(@RequestParam Map<String, Object> params) {
        IPage<CartRecommendEntity> page = getPage(params, Constant.CREATE_DATE, false);
        List<GoodsPageDTO> goodsPage = baseDao.findGoodsPage(page, params);
        for (GoodsPageDTO goodsPageDTO : goodsPage) {
            goodsPageDTO.setChecked(false);
        }
        return new PageData<>(goodsPage, page.getTotal());
    }


    @Override
    public QueryWrapper<CartRecommendEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    /**
     * 查询购物车商品推荐
     *
     * @return
     */

    @Override
    public List<GoodsRecommendDTO> findList() {
        return baseDao.findList();
    }
}
