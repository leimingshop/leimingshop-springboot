/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.OrderGoodsConfirmEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 订单商品确认DAO
 * @Date: 2019/6/22 11:27
 * @Version: V1.0
 */
@Mapper
public interface OrderGoodsConfirmDao extends BaseDao<OrderGoodsConfirmEntity> {

}