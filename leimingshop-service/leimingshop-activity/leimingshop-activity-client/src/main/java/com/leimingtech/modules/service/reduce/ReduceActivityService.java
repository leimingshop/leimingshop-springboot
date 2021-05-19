/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.reduce;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.reduce.*;

import java.util.List;
import java.util.Map;

/**
 * 满减活动管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */

public interface ReduceActivityService {

    /**
     * 满减列表
     *
     * @param params
     * @return java.util.List<com.leimingtech.modules.dto.reduce.ReduceActivityDTO>
     * @date 2019/12/27 17:28
     * @author weixianchun@leimingtech.com
     **/

    List<ReduceActivityDTO> list(Map<String, Object> params);

    /**
     * 根据ID查询满减信息
     *
     * @param id
     * @return com.leimingtech.modules.dto.reduce.ReduceActivityDTO
     * @date 2019/12/27 17:29
     * @author weixianchun@leimingtech.com
     **/

    ReduceActivityDTO get(Long id);

    /**
     * 功能描述：
     * <根据活动id集合促销活动>
     *
     * @param activityIds 满减活动id集合
     * @return
     * @date 2020/2/27 15:52
     * @author 刘远杰
     **/

    List<ReduceActivityDTO> getByIds(List<Long> activityIds);

    /**
     * 保存
     *
     * @param dto
     * @return void
     * @date 2019/12/27 17:30
     * @author weixianchun@leimingtech.com
     **/

    void save(ReduceActivityDTO dto);

    /**
     * 修改
     *
     * @param dto
     * @return java.lang.Boolean
     * @date 2019/12/27 17:30
     * @author weixianchun@leimingtech.com
     **/

    Boolean update(ReduceActivityDTO dto);

    /**
     * 功能描述：
     * <满减活动停止>
     *
     * @param id 活动id
     * @return
     * @date 2020/1/10 10:06
     * @author 刘远杰
     **/

    Boolean stop(Long id);

    /**
     * 删除
     *
     * @param ids
     * @return void
     * @date 2019/12/27 17:30
     * @author weixianchun@leimingtech.com
     **/

    void delete(Long[] ids);


    /**
     * 功能描述:
     * <分页查询满减列表>
     *
     * @param params
     * @return 分页实体
     * @date 2020/1/3 18:28
     * @author weixianchun
     **/

    PageData<ReduceActivityPageDTO> reduceActivityPage(Map<String, Object> params);

    /**
     * 功能描述:
     * <满减详情>
     *
     * @param id
     * @param storeId
     * @return
     * @date 2020/1/3 18:28
     * @author weixianchun
     **/

    ReduceActivityDeatilDTO reduceActivityDeatil(Long id, Long storeId);

    /**
     * @Author weixianchun
     * @Description 查询店铺满减活动
     * @Param id
     * @Param storeId
     * @Date 2019/12/26 13:56
     * @Return com.leimingtech.modules.dto.reduce.ReduceActivityDTO
     * @version 1.0
     */

    ReduceActivityDTO findByIdAndStoreId(Long id, Long storeId);

    /**
     * @Author weixianchun
     * @Description 根据ID删除满减及关联商品信息
     * @Param id
     * @Date 2019/12/26 14:06
     * @Return java.lang.Boolean
     * @version 1.0
     */

    Boolean deleteByActivityId(Long id);


    /**
     * 功能描述:
     * 〈获取进行中的满减活动〉
     *
     * @param
     * @author : 刘远杰
     */

    List<ReduceActivityIndexDTO> selectStartReduceActivity();

    /**
     * 功能描述:
     * 〈根据活动id获取满减活动es数据〉
     *
     * @param
     * @author : 刘远杰
     */

    List<ReduceActivityIndexDTO> selectReduceActivityEsByIds(List<Long> ids);

    /**
     * 功能描述:
     * <满减活动保存>
     *
     * @param dto
     * @return
     * @date 2020/1/3 18:24
     * @author weixianchun
     **/

    Boolean saveReduceActivity(ReduceAndGoodsDTO dto);


    List<ReduceGoodsDTO> selectAllReduceGoodsByStoreId(Long storeId);

    /**
     * 功能描述:
     * <满减活动开始定时任务>
     *
     * @param time
     * @return
     * @date 2020/1/3 18:25
     * @author weixianchun
     **/

    void startActivityTiming(Long time);

    /**
     * 功能描述:
     * <满减活动结束定时任务>
     *
     * @param time
     * @return
     * @date 2020/1/3 18:25
     * @author weixianchun
     **/

    void stopActivityTiming(Long time);

    /**
     * 功能描述:
     * <满减活动编辑>
     *
     * @param dto
     * @return
     * @date 2020/1/3 18:26
     * @author weixianchun
     **/

    Boolean editReduceActivity(ReduceAndGoodsDTO dto);

    /**
     * 功能描述:
     * 〈根据指定关联id获取进行中的活动〉
     *
     * @param relationId         关联id
     * @param activityGoodsScope 关联商品类型
     * @author : 刘远杰
     */

    List<ReduceActivityDTO> getStartByRelationId(Long relationId,
                                                 Integer activityGoodsScope);

    /**
     * 功能描述:
     * 〈查询指定商品id，店铺id，品牌id商品的最优满减〉
     *
     * @param goodsId            商品id
     * @param storeId            店铺id
     * @param brandId            品牌id
     * @param secondStoreClassId 店铺二级分类id
     * @author : 刘远杰
     */

    ReduceActivityDTO getGoodsBestActivity(Long goodsId,
                                           Long storeId,
                                           Long brandId,
                                           Long secondStoreClassId);

    /**
     * 功能描述:
     * 〈查询指定商品id，店铺id，品牌id商品的全部满减活动及活动规则〉
     *
     * @param goodsId            商品id
     * @param storeId            店铺id
     * @param brandId            品牌id
     * @param secondStoreClassId 分类id
     * @author : 刘远杰
     */

    List<ReduceActivityIndexDTO> getGoodsAllActivity(Long goodsId,
                                                     Long storeId,
                                                     Long brandId,
                                                     Long secondStoreClassId);

    /**
     * 功能描述：
     * <查询所有满减活动关联商品>
     *
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 13:59
     * @author 刘远杰
     **/

    List<ReduceGoodsDTO> getAllReduceGoods(Long storeId,
                                           Long brandId,
                                           Long goodsId);
}
