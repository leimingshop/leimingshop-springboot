/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.ScheduleJobLogDao;
import com.leimingtech.modules.dto.ScheduleJobLogDTO;
import com.leimingtech.modules.entity.ScheduleJobLogEntity;
import com.leimingtech.modules.service.ScheduleJobLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScheduleJobLogServiceImpl extends BaseServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public PageData<ScheduleJobLogDTO> page(Map<String, Object> params) {
        IPage<ScheduleJobLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        return getPageData(page, ScheduleJobLogDTO.class);
    }

    private QueryWrapper<ScheduleJobLogEntity> getWrapper(Map<String, Object> params) {
        String jobId = (String) params.get("jobId");

        QueryWrapper<ScheduleJobLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(jobId), "job_id", jobId);

        return wrapper;
    }

    @Override
    public ScheduleJobLogDTO get(Long id) {
        ScheduleJobLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScheduleJobLogDTO.class);
    }

    @Override
    public void insert(ScheduleJobLogDTO scheduleJobLogDTO) {
        baseDao.insert(ConvertUtils.sourceToTarget(scheduleJobLogDTO, ScheduleJobLogEntity.class));
    }

}
