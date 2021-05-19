/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.OrderActivityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单活动表
 *
 * @author weixianchun@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Mapper
public interface OrderActivityDao extends BaseDao<OrderActivityEntity> {

    /**
     * 功能描述:
     * (下单数量查询)
     *
     * @param activityType 活动类型 1优惠券 2满减
     * @param activityId   活动id
     * @return 下单数量
     * @date 2020/1/3 14:15
     * @author weixianchun
     **/
    int findOrderNum(@Param("activityType") int activityType, @Param("activityId") Long activityId);

}
