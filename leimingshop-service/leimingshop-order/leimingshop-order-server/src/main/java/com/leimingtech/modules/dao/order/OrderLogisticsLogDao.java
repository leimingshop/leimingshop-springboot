/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.OrderLogisticsLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单物流消息记录管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Mapper
public interface OrderLogisticsLogDao extends BaseDao<OrderLogisticsLogEntity> {

}
