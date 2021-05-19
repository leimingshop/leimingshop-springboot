/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.payment;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.payment.WithdrawalApplicationLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提现日志表
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Mapper
public interface WithdrawalApplicationLogDao extends BaseDao<WithdrawalApplicationLogEntity> {

}
