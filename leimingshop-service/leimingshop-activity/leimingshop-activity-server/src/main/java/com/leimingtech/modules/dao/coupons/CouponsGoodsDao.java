/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.coupons;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.coupons.CouponsGoodsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券活动商品dao
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Mapper
public interface CouponsGoodsDao extends BaseDao<CouponsGoodsEntity> {

    /**
     * 功能描述:
     * 〈根据优惠券活动id物理删除优惠券商品〉
     *
     * @param
     * @author : 刘远杰
     */
    Integer noLogicDeleteByActivityId(@Param("activityId") Long activityId);

}
