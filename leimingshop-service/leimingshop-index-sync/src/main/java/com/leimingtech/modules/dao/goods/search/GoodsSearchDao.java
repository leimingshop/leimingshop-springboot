/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.search;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.modules.dto.goods.search.GoodsSearchDTO;
import com.leimingtech.modules.dto.spec.search.SpecVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 5:47 PM
 */
@Mapper
public interface GoodsSearchDao {


    /**
     * 查询一个时间段更新的商品信息
     *
     * @param startTime: 开始时间
     * @param endtTime:  结束时间
     * @return 商品集合
     */
    List<GoodsSearchDTO> selectGoodsIndexInfoByUpdateTime(@Param("startTime") Date startTime, @Param("endTime") Date endtTime);

    GoodsSearchDTO getGoodsIndexInfoByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * @return
     * @Param("startTime") Date startTime,@Param("endTime") Date endtTime
     */
    List<SpecVO> listSpec(@Param("startTime") Date startTime, @Param("endTime") Date endtTime, @Param("goodsId") Long goodsId, @Param("specIds") List<Long> specIds);


    List<Long> findDeletedSpec(@Param("goodsId") Long goodsId);

    List<GoodsSearchDTO> goodsIndexSyncByStoreId(@Param("storeId") String storeId);

    List<Long> listSpecIdsByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 更加商品ID集合查询商品集合信息
     *
     * @param goodsIds
     * @date 2019/11/19 18:15
     * @author lixiangx@leimingtech.com
     **/
    List<GoodsSearchDTO> findByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 查询好评率
     *
     * @param id
     * @return
     */
    String reputably(Long id);

    /**
     * 查询店铺中商品好评率
     *
     * @param storeId
     * @return
     */
    String storeReputably(Long storeId);

    List<SpecVO> pageListSpec(@Param("start") int start, @Param("end") int end, @Param("startTime") Date startTime, @Param("endTime") Date endtTime);

    int getTotalGoodsSpecIndex(@Param("startTime") Date startTime, @Param("endTime") Date endtTime);

    int getTotalGoodsIndex(@Param("startTime") Date startTime, @Param("endTime") Date endtTime);

    List<SpecVO> listSpecPriceOrStorage(@Param("flag") Integer flag, @Param("specIds") List<Long> specIds);

    Integer selectStoreSale(Long storeId);

    /**
     * 查询商品的评价数量
     *
     * @param goodsId
     * @return
     */
    GoodsSearchDTO findGoodsEvaluateNum(Long goodsId);

    List<GoodsSearchDTO> pageListGoods(@Param("start") int start, @Param("end") int end, @Param("startTime") Date startTime, @Param("endTime") Date endtTime);

    /**
     * 功能描述 分页查询出所有的goodsId
     *
     * @param page
     * @return java.util.List<java.lang.Long> goodsIds
     * @author lishuo
     * @date 9/7/2020
     */
    List<Long> selectGoodsId(Page<List<Long>> page);

    /**
     * 功能描述 根据goods id 查找收藏数量
     *
     * @param * @param goodsId
     * @return java.lang.Integer
     * @author lishuo
     * @date 10/7/2020
     */
    Integer findGoodsCollectNum(Long goodsId);

    /**
     * 功能描述 根据list集合查询评论数量
     *
     * @param * @param goodsIds
     * @return java.util.List<com.leimingtech.modules.dto.goods.search.GoodsSearchDTO>
     * @author lishuo
     * @date 10/7/2020
     */
    List<GoodsSearchDTO> findGoodsEvaluateNumList(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 功能描述 根据goods id 集合 查询收藏的集合
     *
     * @param * @param goodsIds
     * @return java.util.List<com.leimingtech.modules.dto.goods.search.GoodsSearchDTO>
     * @author lishuo
     * @date 10/7/2020
     */
    List<GoodsSearchDTO> findGoodsCollectNumList(@Param("goodsIds") List<Long> goodsIds);
}
