/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 */
@Mapper
public interface ScheduleJobLogDao extends BaseDao<ScheduleJobLogEntity> {

}
