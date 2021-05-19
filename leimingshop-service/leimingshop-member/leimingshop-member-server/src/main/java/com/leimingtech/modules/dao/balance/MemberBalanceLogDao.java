/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.balance;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.balance.MemberBalanceLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户余额明细表
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */
@Mapper
public interface MemberBalanceLogDao extends BaseDao<MemberBalanceLogEntity> {

}