/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.coupons;


import com.leimingtech.modules.dto.coupons.*;

import java.util.List;
import java.util.Map;

/**
 * <优惠券惠活动es搜索>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */

public interface CouponsActivitySearchService {

    /**
     * 功能描述：
     * <查询会员优惠券详情>
     *
     * @param id 会员优惠券id
     * @return
     * @date 2020/1/13 12:32
     * @author 刘远杰
     **/

    MemberCouponsIndexDTO getMemberCoupons(Long id);

    /**
     * 功能描述:
     * 〈推荐优惠券列表查询〉
     *
     * @author : 刘远杰
     */

    List<FrontCouponsActivityPageDTO> recommendCouponsList(Long memberId);

    /**
     * 功能描述:
     * 〈店铺分组优惠券列表查询〉
     *
     * @author : 刘远杰
     */

    List<FrontCouponsActivityStoreGroupDTO> storeGroupCouponsList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈我的优惠券列表〉
     *
     * @author : 刘远杰
     */

    List<FrontMyCouponsPageDTO> myCouponsList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈优惠券详情数据〉
     *
     * @param activityId 活动id
     * @param memberId   会员id
     * @author : 刘远杰
     */

    FrontCouponsActivityDetailDTO activityDetail(Long activityId,
                                                 Long couponsId,
                                                 Long memberId);

    /**
     * 功能描述:
     * 〈获得优惠券活动跳转类型〉
     *
     * @param id 优惠券活动id
     * @author : 刘远杰
     */

    CouponsToRelationDTO goGoodsList(Long id);

    /**
     * 功能描述:
     * 〈商品详情页优惠券列表〉
     *
     * @author : 刘远杰
     */

    GoodsDetailCouponsListDTO goodsDetailCouponsList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈购物车店铺优惠券列表〉
     *
     * @author : 刘远杰
     */

    GoodsDetailCouponsListDTO cartStoreCouponsList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈获得店铺优惠券活动商品spuids〉
     *
     * @param activityId
     * @author : 刘远杰
     */

    List<Long> getAllGoodsByActivityId(Long activityId);

}
