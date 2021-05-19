/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 定时任务
 */
@Mapper
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {

    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);
}
