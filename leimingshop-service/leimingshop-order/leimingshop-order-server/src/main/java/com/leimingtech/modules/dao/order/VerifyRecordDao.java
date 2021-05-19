/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.VerifyRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 核销记录表
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Mapper
public interface VerifyRecordDao extends BaseDao<VerifyRecordEntity> {

}
