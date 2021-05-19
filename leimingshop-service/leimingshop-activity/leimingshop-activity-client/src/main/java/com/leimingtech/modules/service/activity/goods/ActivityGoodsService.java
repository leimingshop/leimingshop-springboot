/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.activity.goods;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.activity.goods.*;
import com.leimingtech.modules.dto.group.GroupGoodsDTO;

import java.util.List;
import java.util.Map;

/**
 * 活动商品管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */

public interface ActivityGoodsService {


    PageData<ActivityGoodsDTO> page(Map<String, Object> params);


    List<ActivityGoodsDTO> list(Map<String, Object> params);


    ActivityGoodsDTO get(Long id);

    /**
     * 功能描述：
     * <根据活动id 活动类型查询活动商品数量>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @return
     * @date 2020/3/18 15:29
     * @author 刘远杰
     **/

    Integer countByActivityId(Long activityId, Integer activityType);

    /**
     * 功能描述：
     * <批量查询活动商品>
     *
     * @param activityIds  活动id集合
     * @param activityType 活动类型
     * @return
     * @date 2020/3/12 11:37
     * @author 刘远杰
     **/

    List<ActivityGoodsDTO> getByActivityIds(List<Long> activityIds, Integer activityType);


    Boolean save(ActivityGoodsDTO dto);

    /**
     * 功能描述：
     * <活动商品批量保存>
     *
     * @param dtoList 活动商品集合
     * @return
     * @date 2020/3/10 18:00
     * @author 刘远杰
     **/

    Boolean saveBatch(List<ActivityGoodsDTO> dtoList);

    /**
     * 批量更新活动商品
     *
     * @return
     * @date 2020-08-25 16:32
     * @author huangkeyuan@leimingtech.com
     **/

    Boolean updateBatch(List<ActivityGoodsDTO> dtoList);

    /**
     * 功能描述：
     * <活动商品批量保存>
     *
     * @param dtoList      活动商品集合
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsId      商品id
     * @return
     * @date 2020/3/10 18:00
     * @author 刘远杰
     **/

    Boolean saveBatch(List<ActivityGoodsDTO> dtoList,
                      Long activityId,
                      Integer activityType,
                      Long goodsId);


    Boolean update(ActivityGoodsDTO dto);


    void delete(Long[] ids);

    /**
     * 功能描述：
     * <物理删除指定商品id活动数据>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsId      商品id
     * @return
     * @date 2020/3/10 18:29
     * @author 刘远杰
     **/

    void noLogicDeleteByActivityAndGoodsId(Long activityId,
                                           Integer activityType,
                                           Long goodsId);

    /**
     * 功能描述：
     * <删除指定商品id活动数据>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsIds     商品id集合
     * @return
     * @date 2020/3/11 15:27
     * @author 刘远杰
     **/

    void deleteByActivityAndGoodsId(Long activityId,
                                    Integer activityType,
                                    List<Long> goodsIds);

    /**
     * 功能描述：
     * <删除指定活动id数据>
     *
     * @param activityIds  活动id集合
     * @param activityType 活动类型
     * @return
     * @date 2020/3/11 15:27
     * @author 刘远杰
     **/

    void deleteByActivityIds(List<Long> activityIds,
                             Integer activityType);

    /**
     * 功能描述：
     * <添加秒杀活动商品分页查询>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/10 11:03
     * @author 刘远杰
     **/

    PageData<AddSeckillGoodsPageDTO> canAddActiveGoods(Map<String, Object> params);

    /**
     * 添加拼团活动商品的分页查询
     *
     * @param params 查询条件
     * @return
     * @date 2020-04-08 14:58
     * @author huangkeyuan@leimingtech.com
     **/

    PageData<AddSeckillGoodsPageDTO> getAddGroupGoodsList(Map<String, Object> params);

    /**
     * 功能描述：
     * <查询秒杀已选择商品列表>
     *
     * @param params 查询参数
     * @return
     * @date 2020/3/11 11:49
     * @author 刘远杰
     **/

    PageData<AdminSeckillGoodsDTO> adminSeckillGoodsPage(Map<String, Object> params);

    /**
     * 功能描述：
     * <查询所有秒杀商品>
     *
     * @param storeId 店铺id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 15:06
     * @author 刘远杰
     **/

    List<ActivityGoodsDTO> getAllSeckillGoods(Long storeId,
                                              Long goodsId);

    /**
     * 功能描述：
     * <查询所有拼团商品>
     *
     * @param storeId 店铺id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 15:06
     * @author 刘远杰
     **/

    List<ActivityGoodsDTO> getAllGroupGoods(Long storeId,
                                            Long goodsId);

    /**
     * 功能描述：
     * <保存或者修改秒杀商品sku列表数据查询>
     *
     * @param activityId 活动id
     * @param goodsId    商品spu id
     * @param storeId    店铺id
     * @return
     * @date 2020/3/10 15:53
     * @author 刘远杰
     **/

    List<AddSeckillSkuListDTO> getAddSeckillSkuList(Long activityId,
                                                    Long goodsId,
                                                    Long storeId);

    /**
     * 拼团活动添加或管理商品获取商品列表
     *
     * @param activityId 拼团活动id
     * @param goodsId    商品spu id
     * @param storeId    店铺id
     * @return
     * @date 2020-03-10 17:53
     * @author huangkeyuan@leimingtech.com
     **/

    List<AddSeckillSkuListDTO> getAddGropGoodsList(Long activityId,
                                                   Long goodsId,
                                                   Long storeId);

    /**
     * 拼团商品列表
     *
     * @return
     * @date 2020-03-11 16:35
     * @author huangkeyuan@leimingtech.com
     **/

    PageData<GroupGoodsDTO> groupGoodsList(Map<String, Object> params);

    /**
     * 功能描述：
     * <保存活动商品浏览记录>
     *
     * @param goodsId 商品id
     * @param ip      ip
     * @return
     * @date 2020/3/16 14:25
     * @author 刘远杰
     **/

    void saveActivityGoodsBrowser(Long goodsId,
                                  String ip);

    /**
     * 功能描述：
     * <设置秒杀提醒>
     *
     * @param setRemingDTO 是指秒杀提醒实体
     * @param memberId     会员id
     * @return
     * @date 2020/3/16 17:43
     * @author 刘远杰
     **/

    String remindSetting(SetRemingDTO setRemingDTO, Long memberId);

    /**
     * 功能描述：
     * <修改秒杀商品库存、更新下单数量>
     *
     * @param updateActivitySurplusStorageList 秒杀库存更新实体集合
     * @param type                             0扣减库存操作 1增加库存操作（规格删除不操做）
     * @return
     * @date 2020/3/30 11:14
     * @author 刘远杰
     **/

    Boolean updateStorageAndIncreaseOrderNum(List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList,
                                             Integer type);

    /**
     * 功能描述:
     * 〈更新活动商品剩余库存〉
     *
     * @param updateActivitySurplusStorageList 秒杀库存更新实体集合
     * @param type                             0扣减库存操作 1增加库存操作（规格删除不操做）
     * @return : void
     * @author : 刘远杰
     */

    boolean updateStorage(List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList, Integer type);

    /**
     * 功能描述：
     * <活动订单+1>
     *
     * @param updateActivitySurplusStorageList
     * @return
     * @date 2020/3/30 10:59
     * @author 刘远杰
     **/

    void increaseOrderNum(List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList);

    /**
     * 查询具体商品信息
     *
     * @param activityId   活动id
     * @param specId       活动商品规格id
     * @param activityType 活动类型
     * @return
     * @date 2020-03-23 18:05
     * @author huangkeyuan@leimingtech.com
     **/

    ActivityGoodsDTO goodsAndSpec(Long activityId, Long specId, Integer activityType);

    /**
     * 功能描述：
     * <查询所有秒杀活动商品>
     *
     * @param
     * @return
     * @date 2020/3/26 17:31
     * @author 刘远杰
     **/

    List<SpecActivityEsDTO> getAllSeckillSpecActivity();

    /**
     * 获取限时购活动商品数据（同步索引使用）
     *
     * @return 活动商品数据
     */

    List<SpecActivityEsDTO> getAllFlashSaleSpecActivity();

    /**
     * 功能描述：
     * <校验商品是否参见促销活动>
     *
     * @param goodsIds 商品id集合
     * @return
     * @date 2020/4/1 17:24
     * @author 刘远杰
     **/

    UpdateGoodsCheckActivityDTO checkGoodsActivity(List<Long> goodsIds);

    /**
     * 功能描述：
     * <编辑商品更新商品活动 促销未开始：删除活动商品 促销进行中：编辑失败>
     *
     * @param goodsIds 商品id集合
     * @return
     * @date 2020/4/1 17:24
     * @author 刘远杰
     **/
    Boolean updateGoodsActivityHandle(List<Long> goodsIds);

    /**
     * 功能描述：
     * <计算商品活动剩余总库存>
     *
     * @param goodsId 商品spu id
     * @return
     * @date 2020/4/7 10:57
     * @author 刘远杰
     **/

    Map<Long, Integer> getActivityGoodsSurplusStorage(Long goodsId);

    /**
     * 查询所有拼团活动商品
     *
     * @param
     * @return
     * @date 2020-04-01 17:15
     * @author huangkeyuan@leimingtech.com
     **/

    List<SpecActivityEsDTO> getAllGroupSpecActivity();

    /**
     * 查询活动提醒集合并发送消息推送
     *
     * @date 2020/4/23 9:07
     * @author lixiangx@leimingtech.com
     **/

    void activityReminder(Long time);

    /**
     * 已添加限时抢购活动信息
     *
     * @param params 查询条件
     * @return 查询结果
     * @author xuzhch
     * @date 2020年10月20日
     */

    PageData<AdminSeckillGoodsDTO> alreadyActivityGoodsList(Map<String, Object> params);

    /**
     * 更新活动商品ES 根据活动ID集合
     *
     * @param activityIds  活动ID集合
     * @param activityType 活动类型
     * @return the boolean
     * @author xuzhch
     * @date 2020年10月20日
     */

    Boolean updateSpecIndexByActivityId(List<Long> activityIds, Integer activityType);

    /**
     * 更新活动购物车ES 根据活动ID集合
     *
     * @param activityIds  活动ID集合
     * @param activityType 活动类型
     * @return the boolean
     * @author xuzhch
     * @date 2020年10月20日
     */

    Boolean updateCartIndexByActivityId(List<Long> activityIds, Integer activityType);
}
