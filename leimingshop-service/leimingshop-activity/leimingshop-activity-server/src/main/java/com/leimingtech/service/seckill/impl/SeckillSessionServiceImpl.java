/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.seckill.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.dto.setting.SettingSeckillDTO;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.dao.seckill.SeckillSessionDao;
import com.leimingtech.modules.dto.seckill.SeckillSessionDTO;
import com.leimingtech.modules.entity.seckill.SeckillSessionEntity;
import com.leimingtech.modules.service.seckill.SeckillSessionService;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 秒杀场次管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)

public class SeckillSessionServiceImpl extends BaseServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SettingService settingService;

    @Override

    public PageData<SeckillSessionDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SeckillSessionDTO.class);
    }

    /**
     * 功能描述：
     * <秒杀场次条件查询>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/9 9:21
     * @author 刘远杰
     **/
    @Override

    public List<SeckillSessionDTO> list(@RequestParam Map<String, Object> params) {
        // 构造查询条件
        QueryWrapper<SeckillSessionEntity> queryWrapper = getWrapper(params);
        queryWrapper.orderByAsc("activity_start_date");

        // 查询秒杀场次
        List<SeckillSessionEntity> entityList = baseDao.selectList(queryWrapper);

        return ConvertUtils.sourceToTarget(entityList, SeckillSessionDTO.class);
    }

    private QueryWrapper<SeckillSessionEntity> getWrapper(Map<String, Object> params) {
        // 条件构造器
        QueryWrapper<SeckillSessionEntity> wrapper = new QueryWrapper<>();

        // id等值查询
        String id = (String) params.get("id");
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        // 场次开始时间区间查询 activityStartDateStart <= activity_start_date < activityStartDateEnd
        String activityStartDateStart = (String) params.get("activityStartDateStart");
        String activityStartDateEnd = (String) params.get("activityStartDateEnd");
        wrapper.ge(StringUtils.isNotBlank(activityStartDateStart), "activity_start_date", activityStartDateStart)
                .lt(StringUtils.isNotBlank(activityStartDateEnd), "activity_start_date", activityStartDateEnd);

        // 场次结束时间区间查询 activityEndDateStart <= activity_end_date < activityEndDateEnd
        String activityEndDateStart = (String) params.get("activityEndDateStart");
        String activityEndDateEnd = (String) params.get("activityEndDateEnd");
        wrapper.ge(StringUtils.isNotBlank(activityEndDateStart), "activity_end_date", activityEndDateStart)
                .lt(StringUtils.isNotBlank(activityEndDateEnd), "activity_end_date", activityEndDateEnd);

        return wrapper;
    }

    @Override

    public SeckillSessionDTO get(Long id) {
        SeckillSessionEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SeckillSessionDTO.class);
    }

    @Override

    public Boolean save(@RequestBody SeckillSessionDTO dto) {
        SeckillSessionEntity entity = ConvertUtils.sourceToTarget(dto, SeckillSessionEntity.class);

        return insert(entity);
    }

    /**
     * 功能描述：
     * <秒杀场次批量保存>
     *
     * @param dtoList 秒杀场次集合
     * @return
     * @date 2020/3/6 19:53
     * @author 刘远杰
     **/
    @Override

    public Boolean saveBatch(@RequestBody List<SeckillSessionDTO> dtoList) {
        List<SeckillSessionEntity> entityList = ConvertUtils.sourceToTarget(dtoList, SeckillSessionEntity.class);
        return super.insertBatch(entityList);
    }

    @Override

    public Boolean update(@RequestBody SeckillSessionDTO dto) {
        SeckillSessionEntity entity = ConvertUtils.sourceToTarget(dto, SeckillSessionEntity.class);

        return updateById(entity);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述：
     * <秒杀创次创建定时任务>
     *
     * @param time 定时任务执行时间
     * @return
     * @date 2020/3/9 10:09
     * @author 刘远杰
     **/
    @Override

    public void createSeckillSessionTiming(@RequestParam("time") Long time) {
        List<SeckillSessionDTO> sessionList = new ArrayList<>();
        // 当前定时任务时间
        Date now = new Date(time);

        // 1.获取秒杀设置，创建最后一天场次（之前的场次在修改设置时创建）
        SettingSeckillDTO seckillSetting = settingService.getSeckillSetting();
        if (seckillSetting == null) {
            log.info("查询秒杀设置失败，执行创建秒杀场次定时任务异常，now{}", now);
        } else {
            // 最后一天以及开始时间
            List<Date> activityStartDate = seckillSetting.getActivityStartDate();
            Integer presetDays = seckillSetting.getPresetDays();

            // 2.判断最后一天是否设置场次
            String startDay = DateUtils.format(DateUtils.addDateDays(now, presetDays), DateUtils.DATE_PATTERN);
            List<SeckillSessionDTO> listByStartDay = this.getSeckillSessionListByStartDay(startDay);
            if (CollectionUtils.isNotEmpty(listByStartDay)) {
                log.info("执行创建秒杀场次定时任务now:{}，当前日期{}场次已生成，无法继续创建", now, startDay);
            } else {
                // 3.遍历开始时间，封装最后一天场次，保存最后一天预设场次
                for (Date startDate : activityStartDate) {
                    //场次开始时间
                    Date date = new Date(DateUtils.addDateDays(DateUtils.parse(DateUtils.format(now, DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN), presetDays).getTime() + startDate.getTime() + 28800000);
                    if (!date.before(now)) {
                        SeckillSessionDTO session = createSeckillSession(seckillSetting, date);
                        // 添加到新增秒杀场次集合
                        sessionList.add(session);
                    }
                }
                this.saveBatch(sessionList);
            }
        }
        // 5.查询当前时间及以后结束的场次，redis更新进行中的场次及未开始的场次
        Map<String, Object> params = new HashMap<>(1);
        String activityStartDateStart = DateUtils.format(DateUtils.addDateDays(now, -3), DateUtils.DATE_TIME_PATTERN);
        params.put("activityStartDateStart", activityStartDateStart);
        List<SeckillSessionDTO> preSessionList = this.list(params);
        // redis维护未结束场次
        redisUtils.set(ActivityRedisConstants.SECKILL_SESSION, preSessionList, -1);
    }

    /**
     * 功能描述：
     * <查询某天开始的场次>
     *
     * @param startDay 场次开始时间
     * @return
     * @date 2020/3/9 11:20
     * @author 刘远杰
     **/
    @Override

    public List<SeckillSessionDTO> getSeckillSessionListByStartDay(@RequestParam("startDay") String startDay) {
        return baseDao.getSeckillSessionListByStartDay(startDay);
    }

    /**
     * 功能描述：
     * <创建秒杀场次>
     *
     * @param seckillSetting 秒杀设置
     * @param date           场次开始时间
     * @return
     * @date 2020/3/6 19:46
     * @author 刘远杰
     **/
    private SeckillSessionDTO createSeckillSession(SettingSeckillDTO seckillSetting, Date date) {
        // 封装秒杀场次数据
        SeckillSessionDTO session = new SeckillSessionDTO();
        session.setAuditSwitch(seckillSetting.getAuditSwitch());
        session.setSellPriceSwitch(seckillSetting.getSellPriceSwitch());
        session.setActivityStartDate(date);
        session.setActivityEndDate(DateUtils.addDateHours(session.getActivityStartDate(), seckillSetting.getActivityEffectiveTime()));
        session.setActivityEffectiveTime(seckillSetting.getActivityEffectiveTime());
        session.setPayEffectiveTime(seckillSetting.getPayEffectiveTime());
        session.setReminderTime(seckillSetting.getReminderTime());
        return session;
    }

}
