/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.payment;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.payment.PaymentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: weixianchun
 * @Description: 支付方式PaymentDao
 * @Date :2019/5/20 14:50
 * @Version V1.0
 **/
@Mapper
public interface PaymentDao extends BaseDao<PaymentEntity> {

}
