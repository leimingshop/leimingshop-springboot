/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.ScheduleJobLogDTO;

import java.util.Map;

/**
 * 定时任务日志
 */
public interface ScheduleJobLogService {

    PageData<ScheduleJobLogDTO> page(Map<String, Object> params);

    ScheduleJobLogDTO get(Long id);

    void insert(ScheduleJobLogDTO scheduleJobLogDTO);
}
