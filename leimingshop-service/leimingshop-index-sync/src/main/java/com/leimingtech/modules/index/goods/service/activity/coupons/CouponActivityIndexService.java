/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.goods.service.activity.coupons;

import org.springframework.stereotype.Service;

/**
 * <优惠券活动索引管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/9
 */
@Service
public interface CouponActivityIndexService {

    /**
     * 功能描述:
     * 〈同步优惠券活动数据〉
     *
     * @param
     * @author : 刘远杰
     */
    void syncCouponsActivity();

}
