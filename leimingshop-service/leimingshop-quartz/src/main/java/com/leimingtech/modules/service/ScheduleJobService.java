/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.ScheduleJobDTO;

import java.util.Map;

/**
 * 定时任务
 */
public interface ScheduleJobService {

    PageData<ScheduleJobDTO> page(Map<String, Object> params);

    ScheduleJobDTO get(Long id);

    /**
     * 保存定时任务
     */
    void save(ScheduleJobDTO dto);

    /**
     * 更新定时任务
     */
    void update(ScheduleJobDTO dto);

    /**
     * 批量删除定时任务
     */
    void deleteBatch(Long[] ids);

    /**
     * 批量更新定时任务状态
     */
    int updateBatch(Long[] ids, int status);

    /**
     * 立即执行
     */
    void run(Long[] ids);

    /**
     * 暂停运行
     */
    void pause(Long[] ids);

    /**
     * 恢复运行
     */
    void resume(Long[] ids);
}
