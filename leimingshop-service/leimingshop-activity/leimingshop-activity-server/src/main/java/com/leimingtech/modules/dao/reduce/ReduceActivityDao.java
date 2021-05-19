/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.reduce;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.reduce.ReduceActivityDeatilDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityPageDTO;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.entity.reduce.ReduceActivityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 满减活动dao
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Mapper
public interface ReduceActivityDao extends BaseDao<ReduceActivityEntity> {

    /**
     * @Author weixianchun
     * @Description 分页查询满减列表
     * @Param params
     * @Date 2019/12/26 11:00
     * @Return com.leimingtech.modules.dto.reduce.ReduceActivityPageDTO
     * @version 1.0
     */
    List<ReduceActivityPageDTO> reduceActivityPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * @Author weixianchun
     * @Description 满减详情
     * @Param id
     * @Param storeId
     * @Date 2019/12/26 12:04
     * @Return com.leimingtech.modules.dto.reduce.ReduceActivityDeatilDTO
     * @version 1.0
     */
    ReduceActivityDeatilDTO reduceActivityDeatil(@Param("id") Long id, @Param("storeId") Long storeId);

    /**
     * 功能描述:
     * 〈获得某关联id指定的活动类型〉
     *
     * @param relationId
     * @param activityGoodsScope
     * @author : 刘远杰
     */
    List<ReduceActivityEntity> selectStartByRelationId(@Param("relationId") Long relationId, @Param("activityGoodsScope") Integer activityGoodsScope);

    /**
     * 查询指定商品最优满减活动
     *
     * @param goodIds 商品spu id
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @return
     * @date
     * @author 刘远杰
     **/
    ReduceActivityEntity getGoodsBestActivity(@Param("goodsId") Long goodIds,
                                              @Param("storeId") Long storeId,
                                              @Param("brandId") Long brandId,
                                              @Param("secondStoreClassId") Long secondStoreClassId);

    /**
     * 查询指定商品id，店铺id，品牌id商品的全部满减活动及活动规则
     *
     * @param goodsId            商品spu id
     * @param storeId            店铺id
     * @param brandId            品牌id
     * @param secondStoreClassId 分类id
     * @return
     * @date
     * @author 刘远杰
     **/
    List<ReduceActivityIndexDTO> getGoodsAllActivity(@Param("goodsId") Long goodsId, @Param("storeId") Long storeId, @Param("brandId") Long brandId, @Param("secondStoreClassId") Long secondStoreClassId);

    /**
     * 功能描述：
     * <查询所有满减商品>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/3/10 13:57
     * @author 刘远杰
     **/
    List<ReduceGoodsDTO> selectAllReduceGoods(@Param("storeId") Long storeId, @Param("brandId") Long brandId, @Param("goodsId") Long goodsId);

    /**
     * 功能描述：
     * <根据店铺查询所有满减商品>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/3/10 13:57
     * @author 刘远杰
     **/
    List<ReduceGoodsDTO> selectAllReduceGoodsByStoreId(@Param("storeId") Long storeId);


}
