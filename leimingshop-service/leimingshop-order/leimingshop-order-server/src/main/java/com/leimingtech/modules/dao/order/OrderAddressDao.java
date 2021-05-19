/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.OrderAddressEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单地址信息管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Mapper
public interface OrderAddressDao extends BaseDao<OrderAddressEntity> {

    /**
     * 功能描述:
     * 〈编辑订单地址〉
     *
     * @param entity
     * @return : int
     * @author : 刘远杰
     */
    int editOrderAddress(OrderAddressEntity entity);

}
