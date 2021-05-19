/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.FetchCodeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 电子提货码
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */
@Mapper
public interface FetchCodeDao extends BaseDao<FetchCodeEntity> {

}
