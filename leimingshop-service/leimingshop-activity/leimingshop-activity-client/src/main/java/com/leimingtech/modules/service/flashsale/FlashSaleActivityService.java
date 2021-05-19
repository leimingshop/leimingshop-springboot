/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.flashsale;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.activity.goods.InsertActivityGoodsDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityEditDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityPageDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivitySaveDTO;

import java.util.List;
import java.util.Map;

/**
 * 限时抢购活动表
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */

public interface FlashSaleActivityService {

    /**
     * 分页查看限时抢购活动列表
     *
     * @param params 查询条件
     * @return PageData 查询结果
     * @author xuzhch
     * @date 2020年10月15日
     */

    PageData<FlashSaleActivityDTO> page(Map<String, Object> params);

    /**
     * 查看限时抢购活动列表
     *
     * @param params 查询条件
     * @return list 查询结果
     * @author xuzhch
     * @date 2020年10月15日
     */

    List<FlashSaleActivityDTO> list(Map<String, Object> params);

    /**
     * 查看限时抢购活动
     *
     * @param id 活动ID
     * @return FlashSaleActivityDTO 限时抢购活动信息
     * @author xuzhch
     * @date 2020年10月15日
     */

    FlashSaleActivityDTO get(Long id);

    /**
     * 保存限时抢购活动
     *
     * @param dto 限时抢购活动信息
     * @author xuzhch
     * @date 2020年10月15日
     */

    void save(FlashSaleActivityDTO dto);

    /**
     * 修改限时抢购活动
     *
     * @param dto 限时抢购活动信息
     * @author xuzhch
     * @date 2020年10月15日
     */

    void update(FlashSaleActivityDTO dto);

    /**
     * 删除限时抢购活动
     *
     * @param ids 活动id数组
     * @author xuzhch
     * @date 2020年10月15日
     */

    void delete(Long[] ids);


    /**
     * 查看限时抢购活动分页列表
     *
     * @param params 查询条件
     * @return list 查询结果
     * @author xuzhch
     * @date 2020年10月15日
     */

    PageData<FlashSaleActivityPageDTO> managePage(Map<String, Object> params);

    /**
     * 保存限时抢购活动
     *
     * @param dto 限时抢购活动保存信息
     * @author xuzhch
     * @date 2020年10月15日
     */

    void saveFlashSaleActivity(FlashSaleActivitySaveDTO dto);

    /**
     * 修改限时抢购活动
     *
     * @param dto 限时抢购活动保存信息
     * @author xuzhch
     * @date 2020年10月15日
     */

    void updateFlashSaleActivity(FlashSaleActivityEditDTO dto);

    /**
     * 保存活动商品
     *
     * @param insertActivityGoodsDTO 活动商品信息
     * @param storeId                店铺ID
     * @return boolean              结果
     * @author xuzhch
     * @date 2020年10月19日
     */

    Boolean saveActivityGoods(InsertActivityGoodsDTO insertActivityGoodsDTO, Long storeId);


    /**
     * 根据活动ID和店铺ID获取活动信息
     *
     * @param id      活动ID
     * @param storeId 店铺ID
     * @return FlashSaleActivityDTO 活动信息
     * @author xuzhch
     * @date 2020年10月20日
     */

    FlashSaleActivityDTO getByIdOrStoreId(Long id,
                                          Long storeId);

    /**
     * 限时购活动定时开始
     *
     * @param time 定时执行时间
     * @date 2020年10月20日
     * @author xuzhch
     */

    void startFlashSalelActivityTiming(Long time);

    /**
     * 限时购活动定时结束
     *
     * @param time 定时执行时间
     * @date 2020年10月20日
     * @author xuzhch
     **/

    void stopFlashSaleActivityTiming(Long time);

    /**
     * 根据活动id集合查询活动信息
     *
     * @param activityIds 活动ID集合
     * @return 活动信息集合
     */

    List<FlashSaleActivityDTO> getInfoListByActivityIds(List<Long> activityIds);


    /**
     * 获取活动订单取消时间
     *
     * @param activicyIds 活动ID集合
     * @return 取消时间 最大不超过1440。（单位：分钟）
     */

    Integer getCancelTime(List<Long> activicyIds);

    /**
     * 停止限时抢购活动
     *
     * @param id      活动ID
     * @param storeId store id
     * @return 操作结果
     */

    Boolean stop(Long id,
                 Long storeId);

    /**
     * 活动浏览记录
     *
     * @param ids 活动ID
     * @date 2020年10月31日
     * @author xuzhch
     **/

    void increaseBrowserNum(List<Long> ids);


    void increaseOrderNum(List<Long> ids);
}
