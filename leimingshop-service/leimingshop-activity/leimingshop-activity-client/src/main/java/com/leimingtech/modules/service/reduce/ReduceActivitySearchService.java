/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.reduce;


import com.leimingtech.modules.dto.coupons.CouponsToRelationDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;

import java.util.List;
import java.util.Map;

/**
 * <满减活动es搜索>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */

public interface ReduceActivitySearchService {

    /**
     * 功能描述:
     * 〈商品详情页满减活动列表〉
     *
     * @author : 刘远杰
     */

    List<FrontReduceActivityPageDTO> goodsDetailCouponsList(Map<String, Object> params);

    /**
     * 功能描述：
     * <front订单活动详情>
     *
     * @param activityId 活动id
     * @return
     * @date 2020/1/8 17:35
     * @author 刘远杰
     **/

    FrontReduceActivityPageDTO frontActivityDetail(Long activityId);

    /**
     * 功能描述:
     * 〈满减活动列表〉
     *
     * @author : 刘远杰
     */

    List<ReduceActivityIndexDTO> goodsReduceList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈满减活动详情数据〉
     *
     * @param activityId 活动id
     * @author : 刘远杰
     */

    ReduceActivityIndexDTO activityDetail(Long activityId);

    /**
     * 功能描述:
     * 〈获得满减活动跳转类型〉
     *
     * @param id 满减活动id
     * @author : 刘远杰
     */

    CouponsToRelationDTO goGoodsList(Long id);

    /**
     * 功能描述:
     * 〈获得店铺满减活动商品spuids〉
     *
     * @param activityId
     * @author : 刘远杰
     */

    List<Long> getAllGoodsByActivityId(Long activityId);

}
