/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.activity.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.activity.goods.ActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.AdminSeckillGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.SpecActivityEsDTO;
import com.leimingtech.modules.dto.activity.goods.UpdateActivitySurplusStorageDTO;
import com.leimingtech.modules.dto.group.GroupGoodsDTO;
import com.leimingtech.modules.entity.activity.goods.ActivityGoodsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 活动商品管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Mapper
public interface ActivityGoodsDao extends BaseDao<ActivityGoodsEntity> {

    List<AdminSeckillGoodsDTO> adminSeckillGoodsList(@Param("activityId") Long activityId);


    /**
     * 已添加活动商品信息.
     *
     * @param params 查询条件
     * @param page   the page
     * @return the list
     */
    List<AdminSeckillGoodsDTO> alreadyAddActivityGoodsPage(@Param("params") Map<String, Object> params, IPage page);

    /**
     * 查询拼团活动已选择商品列表(分页)
     *
     * @return
     * @date 2020-03-11 16:47
     * @author huangkeyuan@leimingtech.com
     **/
    List<GroupGoodsDTO> groupGoodsPage(@Param("params") Map<String, Object> params, IPage page);

    /**
     * 功能描述：
     * <查询所有秒杀商品>
     *
     * @param storeId 店铺id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 14:41
     * @author 刘远杰
     **/
    List<ActivityGoodsDTO> selectAllSeckillGoods(@Param("storeId") Long storeId, @Param("goodsId") Long goodsId);

    /**
     * 功能描述：
     * <查询所有拼团商品>
     *
     * @param storeId 店铺id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 14:41
     * @author 刘远杰
     **/
    List<ActivityGoodsDTO> selectAllGroupGoods(@Param("storeId") Long storeId, @Param("goodsId") Long goodsId);

    /**
     * 功能描述：
     * <物理删除指定商品id活动数据>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsId      商品id
     * @return
     * @date 2020/3/10 18:22
     * @author 刘远杰
     **/
    int noLogicDeleteByActivityAndGoodsId(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("goodsId") Long goodsId);

    /**
     * 查询拼团活动已选择商品列表
     *
     * @param activityId 活动id
     * @return
     * @date 2020-03-11 16:47
     * @author huangkeyuan@leimingtech.com
     **/
    List<GroupGoodsDTO> groupGoodsList(@Param("activityId") Long activityId);

    /**
     * 功能描述：
     * <更新秒杀剩余库存>
     *
     * @param
     * @return
     * @date 2020/3/23 12:22
     * @author 刘远杰
     **/
    Integer updateActivitySurplusStorage(UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO);

    /**
     * 功能描述：
     * <查询所有秒杀活动商品>
     *
     * @param
     * @return
     * @date 2020/3/26 17:30
     * @author 刘远杰
     **/
    List<SpecActivityEsDTO> selectAllSeckillSpecActivity();

    /**
     * 查询所有拼团活动商品
     *
     * @param
     * @return
     * @date 2020-04-01 17:17
     * @author huangkeyuan@leimingtech.com
     **/
    List<SpecActivityEsDTO> selectAllGroupSpecActivity();

    /**
     * 功能描述：
     * <根据商品id查询秒杀活动>
     *
     * @param
     * @return
     * @date 2020/4/1 17:30
     * @author 刘远杰
     **/
    List<SpecActivityEsDTO> selectSeckillActivityByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 功能描述：
     * <根据商品id查询秒杀活动商品>
     *
     * @param goodsIds 商品id集合
     * @return
     * @date 2020/4/2 17:36
     * @author 刘远杰
     **/
    List<SpecActivityEsDTO> selectSeckillActivityGoodsByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 功能描述：
     * <根据商品id查询拼团活动>
     *
     * @param
     * @return
     * @date 2020/4/1 17:30
     * @author 刘远杰
     **/
    List<SpecActivityEsDTO> selectGroupActivityByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 功能描述：
     * <根据商品id查询拼团活动商品>
     *
     * @param goodsIds 商品id集合
     * @return
     * @date 2020/4/2 17:36
     * @author 刘远杰
     **/
    List<SpecActivityEsDTO> selectGroupActivityGoodsByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 功能描述：
     * <秒杀订单+1>
     *
     * @param updateActivitySurplusStorageDTO
     * @return
     * @date 2020/3/30 10:53
     * @author 刘远杰
     **/
    int increaseOrderNum(@Param("dtoList") List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageDTO);

    /**
     * 功能描述：
     * <秒杀订单+1>
     *
     * @param dtoList
     * @return
     * @date 2020/3/30 10:53
     * @author 刘远杰
     **/
    int increaseSpuOrderNum(@Param("dtoList") List<UpdateActivitySurplusStorageDTO> dtoList);

    /**
     * 功能描述：
     * <查询商品限时抢购活动数据>
     *
     * @param storeId 店铺id
     * @param goodsId 商品id
     * @return list
     * @date 2020 /3/10 13:39
     * @author 刘远杰
     */
    List<ActivityGoodsDTO> selectFlashSaleAllGoods(@Param("storeId") Long storeId, @Param("goodsId") Long goodsId);

    /**
     * 查询所有限时购商品数据
     *
     * @return the list
     */
    List<SpecActivityEsDTO> selectAllFlashSaleSpecActivity();

    List<SpecActivityEsDTO> selectFlashSaleActivityGoodsByGoodsIds(@Param("goodsIds") List<Long> goodsIds);
}
