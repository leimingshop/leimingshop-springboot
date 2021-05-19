/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.activity;

import com.leimingtech.modules.entity.activity.ActivityDetailStatisEntity;
import com.leimingtech.modules.entity.activity.ActivityStatisEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动统计查询Dao
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Mapper
public interface ActivityStatisDao {

    /***
     * 查询出活动所属店铺
     * @param activityId
     * @return
     */
    //@DataSource("activity")
    Long getStoreId(Long activityId);

    /**
     * 查询出所有优惠券活动
     *
     * @return
     */
    //@DataSource("activity")
    List<ActivityStatisEntity> getCouponsList();

    /**
     * 查询当天优惠券活动的领取量
     *
     * @param activityId 活动ID
     * @param date       日期
     * @return 领取数量
     * @author xuzhch
     * @date 2020年9月17日
     */
    //@DataSource("activity")
    Integer getPersonNum(@Param("activityId") Long activityId,
                         @Param("date") String date);


    /**
     * 查询出满减活动
     *
     * @return
     */
    //@DataSource("activity")
    List<ActivityStatisEntity> getReduceActivity();

    /**
     * 查询出优惠券活动订单信息
     *
     * @param date       日期
     * @param activityId 活动ID
     * @return 活动统计结果
     * @author xuzhch
     * @date 2020年9月17日
     */
    //@DataSource("order")
    ActivityStatisEntity getCouponsActivityList(@Param("activityId") Long activityId,
                                                @Param("date") String date);

    /**
     * 查询出满减活动订单信息
     *
     * @param date       日期
     * @param activityId 活动ID
     * @return 满减活动统计结果
     * @author xuzhch
     * @date 2020年9月17日
     */
    //@DataSource("order")
    ActivityStatisEntity getReduceActivityList(@Param("activityId") Long activityId,
                                               @Param("date") String date);

    /**
     * 查询出活动下单商品信息
     *
     * @param date       日期
     * @param activityId 活动ID
     * @return 统计结果
     * @author xuzhch
     * @date 2020年9月17日
     */
    //@DataSource("order")
    List<ActivityDetailStatisEntity> getActivityDetail(@Param("activityId") Long activityId,
                                                       @Param("date") String date);
}
