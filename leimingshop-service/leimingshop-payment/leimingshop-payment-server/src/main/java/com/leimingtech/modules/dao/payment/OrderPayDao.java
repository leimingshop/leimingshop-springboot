/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.payment;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.payment.OrderPayDTO;
import com.leimingtech.modules.entity.payment.OrderPayEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 订单支付管理
 * @Date :2019/6/18 10:25
 * @Version V1.0
 **/
@Mapper
public interface OrderPayDao extends BaseDao<OrderPayEntity> {

    /**
     * 根据订单id查询订单支付表数据
     *
     * @param paySn 支付单号
     * @return
     */
    OrderPayDTO getNoByPaySn(Long paySn);

    /**
     * 根据时间查询订单
     *
     * @return
     */
    List<Long> getOrderByDate(Date nowDate);

    /**
     * 功能描述:
     * 〈订单定时取消修改状态〉
     *
     * @param nowDate
     * @author : 刘远杰
     */
    Integer cancelOrderPayByTime(Date nowDate);

}