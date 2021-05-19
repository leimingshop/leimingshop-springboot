/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.auditlog;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.auditlog.AuditLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审核记录表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Mapper
public interface AuditLogDao extends BaseDao<AuditLogEntity> {

}