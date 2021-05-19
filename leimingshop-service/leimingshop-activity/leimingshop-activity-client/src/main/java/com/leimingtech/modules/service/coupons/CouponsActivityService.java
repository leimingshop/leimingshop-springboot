/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.coupons;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.coupons.*;

import java.util.List;
import java.util.Map;

/**
 * 优惠券活动service
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */

public interface CouponsActivityService {

    /**
     * 功能描述：
     * <后台优惠券分页>
     *
     * @param params 查询条件
     * @return
     * @date 2020/1/17 14:53
     * @author 刘远杰
     **/

    PageData<AdminCouponsActivityPageDTO> adminPage(Map<String, Object> params);

    /**
     * 功能描述：
     * <优惠券条件查询>
     *
     * @param params 查询条件
     * @return
     * @date 2020/1/17 14:53
     * @author 刘远杰
     **/

    List<CouponsActivityDTO> list(Map<String, Object> params);


    CouponsActivityDTO get(Long id);

    /**
     * 功能描述：
     * <查询优惠券活动集合>
     *
     * @param ids 优惠券活动id集合
     * @return
     * @date 2020/2/24 10:23
     * @author 刘远杰
     **/

    List<CouponsActivityDTO> getByIds(List<Long> ids);

    /**
     * 功能描述:
     * 〈获取店铺优惠券活动〉
     *
     * @param id      活动id
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    CouponsActivityDTO getByIdAndStoreId(Long id, Long storeId);

    /**
     * 功能描述:
     * 〈后台优惠券详情查询〉
     *
     * @param id      优惠券活动id
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    AdminCouponsActivityDetailDTO adminDetail(Long id, Long storeId);


    void save(CouponsActivityDTO dto);

    /**
     * 功能描述:
     * 〈新增优惠券活动〉
     *
     * @param dto 优惠券活动及活动商品
     * @author : 刘远杰
     */

    Boolean saveCouponsActivity(CouponsActivityAndGoodsDTO dto);

    /**
     * 功能描述:
     * 〈活动编辑〉
     *
     * @param dto 修改实体
     * @author : 刘远杰
     */

    Boolean update(CouponsActivityDTO dto);

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
     * 功能描述:
     * 〈编辑优惠券活动〉
     *
     * @param dto 优惠券活动及活动商品
     * @author : 刘远杰
     */

    Boolean editCouponsActivity(CouponsActivityAndGoodsDTO dto);


    void delete(Long[] ids);

    /**
     * 功能描述:
     * 〈优惠券活动删除〉
     *
     * @param id 优惠券活动id
     * @author : 刘远杰
     */

    Boolean deleteCouponsActivityById(Long id);

    /**
     * 功能描述:
     * 〈优惠券活动开始定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    void startActivityTiming(Long time);

    /**
     * 功能描述:
     * 〈优惠券活动结束定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    void stopActivityTiming(Long time);

    /**
     * 功能描述:
     * 〈获取进行中、已结束的优惠券活动〉
     *
     * @author : 刘远杰
     */

    List<CouponsActivityIndexDTO> selectStartCouponsActivity();

    /**
     * 功能描述:
     * 〈优惠券领取〉
     *
     * @param activityId 优惠券活动id
     * @param memberId   会员id
     * @author : 刘远杰
     */

    Boolean receivedCoupons(Long activityId, Long memberId);

    /**
     * 功能描述：
     * <查询所有优惠券商品>
     *
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 12:07
     * @author 刘远杰
     **/

    List<CouponsGoodsDTO> getAllCouponsGoods(Long storeId,
                                             Long brandId,
                                             Long goodsId);

    /**
     * 功能描述：
     * <根据店铺id查询所有优惠券商品>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/3/10 12:07
     * @author 刘远杰
     **/

    List<CouponsGoodsDTO> selectAllCouponsGoodsByStoreId(Long storeId);

    /***
     * 查询失效优惠券
     * @param memberId
     * @return
     */

    List<MemberCouponsDTO> getDisableActivity(Long memberId);
}
