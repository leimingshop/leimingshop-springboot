/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.payment;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.payment.PaymentTallyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: weixianchun
 * @Description: 支付流水管理
 * @Date :2019/6/18 11:55
 * @Version V1.0
 **/
@Mapper
public interface PaymentTallyDao extends BaseDao<PaymentTallyEntity> {

}