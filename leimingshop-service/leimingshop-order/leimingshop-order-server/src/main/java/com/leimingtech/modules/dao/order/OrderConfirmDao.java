/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.OrderConfirmEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 订单确定DAO
 * @Date: 2019/6/22 11:26
 * @Version: V1.0
 */
@Mapper
public interface OrderConfirmDao extends BaseDao<OrderConfirmEntity> {

}