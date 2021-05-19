/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.recommend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.GoodsRecommendDTO;
import com.leimingtech.modules.dto.recommend.CartRecommendPageDTO;
import com.leimingtech.modules.dto.recommend.GoodsPageDTO;
import com.leimingtech.modules.dto.recommend.SaveCartRecommendDTO;
import com.leimingtech.modules.dto.recommend.UpdateCartRecommendDTO;
import com.leimingtech.modules.entity.recommend.CartRecommendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 购物车推荐商品表
 *
 * @author sunyanling
 * @since v1.0.0 2019-08-21
 */
@Mapper
public interface CartRecommendDao extends BaseDao<CartRecommendEntity> {


    /**
     * 购物车推荐商品分页查询
     *
     * @param page
     * @param params
     * @return
     */
    List<CartRecommendPageDTO> pageList(IPage<CartRecommendEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 批量更新排序
     *
     * @param list
     */
    void updateBatch(@Param("list") List<UpdateCartRecommendDTO> list);

    /**
     * 批量保存推荐商品
     *
     * @param list
     */
    void insertBatch(@Param("list") List<SaveCartRecommendDTO> list);

    /**
     * 查询商品列表，（添加购物车推荐商品使用）
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回商品信息
     */
    List<GoodsPageDTO> findGoodsPage(IPage<CartRecommendEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 查询购物车商品推荐
     *
     * @return
     */
    List<GoodsRecommendDTO> findList();

}
