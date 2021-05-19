/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.transport;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.transport.TransportCompanyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物流公司表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */
@Mapper
public interface TransportCompanyDao extends BaseDao<TransportCompanyEntity> {

}