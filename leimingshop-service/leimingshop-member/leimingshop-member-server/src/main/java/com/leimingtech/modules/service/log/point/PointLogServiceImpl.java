/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.log.point;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.enums.setting.PointSettingEnum;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.dao.log.point.PointLogDao;
import com.leimingtech.modules.dto.log.point.MemberGrowthValueDTO;
import com.leimingtech.modules.dto.log.point.PointLogDTO;
import com.leimingtech.modules.dto.log.point.PointLogPackDTO;
import com.leimingtech.modules.entity.log.point.PointLogEntity;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.setting.PointSettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;


/**
 * 积分日志 ServiceImpl
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class PointLogServiceImpl extends BaseServiceImpl<PointLogDao, PointLogEntity> implements PointLogService {

    private static final String PARAMS_NAME_MEMBER_ID = "memberId";
    private static final String PARAMS_NAME_SOURCE_TYPE = "sourceType";
    private static final String PARAMS_NAME_POINT_TYPE = "pointType";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(PointLogServiceImpl.class);
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private PointSettingService pointSettingService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisUtils redisUtils;

    @Override

    public PageData<PointLogDTO> page(@RequestParam Map<String, Object> params) {
        IPage<PointLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, PointLogDTO.class);
    }

    @Override

    public List<PointLogDTO> list(Map<String, Object> params) {
        List<PointLogEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, PointLogDTO.class);
    }

    private QueryWrapper<PointLogEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<PointLogEntity> wrapper = new QueryWrapper<>();
        if (params.get(PARAMS_NAME_MEMBER_ID) != null) {
            String memberId = String.valueOf(params.get(PARAMS_NAME_MEMBER_ID));
            wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);
        }
        if (params.get(PARAMS_NAME_SOURCE_TYPE) != null) {
            Object sourceType = params.get(PARAMS_NAME_SOURCE_TYPE);
            wrapper.eq(sourceType != null, "source_type", sourceType);
        }
        if (params.get(PARAMS_NAME_POINT_TYPE) != null) {
            Object pointType = params.get(PARAMS_NAME_POINT_TYPE);
            wrapper.eq(pointType != null, "point_type", pointType);
        }

        // 过滤掉积分为0的记录
        wrapper.ne("point_value", 0);
        return wrapper;
    }

    @Override

    public PointLogDTO get(Long id) {
        PointLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, PointLogDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public Boolean save(@RequestBody PointLogDTO dto) {
        PointLogEntity entity = ConvertUtils.sourceToTarget(dto, PointLogEntity.class);

        return insert(entity);
    }

    /**
     * 批量保存
     *
     * @param dto: 批量保存集合
     * @return 操作结果
     * @date 2019/12/24 14:44
     * @author lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Boolean saveBatch(@RequestBody List<PointLogDTO> dto) {
        List<PointLogEntity> entity = ConvertUtils.sourceToTarget(dto, PointLogEntity.class);
        return insertBatch(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody PointLogDTO dto) {
        PointLogEntity entity = ConvertUtils.sourceToTarget(dto, PointLogEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        logicDelete(ids, PointLogEntity.class);

        //物理删除
//        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


    /**
     * 封装积分/成长值 并发送MQ进行保存、修改用户可用积分数量|成长值
     *
     * @param pointLogPackDTO: 封装积分/成长值DTO
     * @date 2019/12/24 16:43
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public void packPointLog(@RequestBody PointLogPackDTO pointLogPackDTO) {

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("pointLogPackDTO", pointLogPackDTO.toString());
        mlogger.info(MemberStatusCode.COMPUTE_MEMBER_POINT_CODE, MemberStatusCode.COMPUTE_MEMBER_POINT_CODE_MSG, logMap);

        List<PointLogEntity> pointLogEntityList = Lists.newArrayList();
        MemberVoDTO memberVoDTO = memberService.selectMemberById(pointLogPackDTO.getMemberId());

        Integer status = pointLogPackDTO.getStatus();
        if (status == MemberPointLogEnum.INSERT_ALL.code()) {
            // 封装积分日志
            PointLogEntity pointLogEntity = new PointLogEntity();
            pointLogEntity.setMemberId(pointLogPackDTO.getMemberId());
            pointLogEntity.setMemberName(pointLogPackDTO.getMemberName());
            pointLogEntity.setPointValue(pointLogPackDTO.getPointValue());
            pointLogEntity.setPointDesc(pointLogPackDTO.getPointDesc());
            pointLogEntity.setSourceType(pointLogPackDTO.getSourceType());
            pointLogEntity.setPointType(MemberPointLogEnum.POINT_TYPE.code());
            Integer availablePoint = 0;
            if (null != memberVoDTO.getMemberInfoDTO().getAvailablePoint()) {
                availablePoint = memberVoDTO.getMemberInfoDTO().getAvailablePoint();
            }
            pointLogEntity.setCurrentValue(availablePoint + pointLogPackDTO.getPointValue());
            pointLogEntityList.add(pointLogEntity);
            logMap.clear();
            logMap.put("pointLogEntity", pointLogEntity.toString());
            mlogger.info(MemberStatusCode.FINAL_COMPUTE_MEMBER_POINT_CODE, MemberStatusCode.FINAL_COMPUTE_MEMBER_POINT_CODE_MSG, logMap);
            // 封装成长值日志
            PointLogEntity pointLogGrowthEntity = new PointLogEntity();
            pointLogGrowthEntity.setMemberId(pointLogPackDTO.getMemberId());
            pointLogGrowthEntity.setMemberName(pointLogPackDTO.getMemberName());
            pointLogGrowthEntity.setPointValue(pointLogPackDTO.getGrowthValue());
            pointLogGrowthEntity.setPointDesc(pointLogPackDTO.getPointDesc());
            pointLogGrowthEntity.setSourceType(pointLogPackDTO.getSourceType());
            pointLogGrowthEntity.setPointType(MemberPointLogEnum.GROWTH_TYPE.code());

            Integer gradePoint = 0;
            if (null != memberVoDTO.getMemberInfoDTO().getGradePoint()) {
                gradePoint = memberVoDTO.getMemberInfoDTO().getGradePoint();
            }

            pointLogGrowthEntity.setCurrentValue(gradePoint + pointLogPackDTO.getGrowthValue());
            pointLogEntityList.add(pointLogGrowthEntity);
            logMap.clear();
            logMap.put("pointLogGrowthEntity", pointLogGrowthEntity.toString());
            mlogger.info(MemberStatusCode.FINAL_COMPUTE_MEMBER_POINT_CODE, MemberStatusCode.FINAL_COMPUTE_MEMBER_POINT_CODE_MSG, logMap);
        } else if (status == MemberPointLogEnum.INSERT_POINT.code()) {
            // 封装积分日志
            PointLogEntity pointLogEntity = new PointLogEntity();
            pointLogEntity.setMemberId(pointLogPackDTO.getMemberId());
            pointLogEntity.setMemberName(pointLogPackDTO.getMemberName());
            pointLogEntity.setPointValue(pointLogPackDTO.getPointValue());
            pointLogEntity.setPointDesc(pointLogPackDTO.getPointDesc());
            pointLogEntity.setSourceType(pointLogPackDTO.getSourceType());
            pointLogEntity.setPointType(MemberPointLogEnum.POINT_TYPE.code());
            Integer availablePoint = 0;
            if (null != memberVoDTO.getMemberInfoDTO().getAvailablePoint()) {
                availablePoint = memberVoDTO.getMemberInfoDTO().getAvailablePoint();
            }
            pointLogEntity.setCurrentValue(availablePoint + pointLogPackDTO.getPointValue());
            pointLogEntityList.add(pointLogEntity);
        } else if (status == MemberPointLogEnum.INSERT_GROWTH.code()) {
            // 封装成长值日志
            PointLogEntity pointLogGrowthEntity = new PointLogEntity();
            pointLogGrowthEntity.setMemberId(pointLogPackDTO.getMemberId());
            pointLogGrowthEntity.setMemberName(pointLogPackDTO.getMemberName());
            pointLogGrowthEntity.setPointValue(pointLogPackDTO.getGrowthValue());
            pointLogGrowthEntity.setPointDesc(pointLogPackDTO.getPointDesc());
            pointLogGrowthEntity.setSourceType(pointLogPackDTO.getSourceType());
            pointLogGrowthEntity.setPointType(MemberPointLogEnum.GROWTH_TYPE.code());
            Integer gradePoint = 0;
            if (null != memberVoDTO.getMemberInfoDTO().getGradePoint()) {
                gradePoint = memberVoDTO.getMemberInfoDTO().getGradePoint();
            }
            pointLogGrowthEntity.setCurrentValue(gradePoint + pointLogPackDTO.getGrowthValue());
            pointLogEntityList.add(pointLogGrowthEntity);
        }

        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_POINT_LOG_QUEUE, JSON.toJSONString(pointLogEntityList));

    }


    /**
     * 清除用户成长值（定时任务调用）
     *
     * @return 操作结果
     * @date 2019/12/25 15:08
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public Boolean clearGrowth() {

        Object obj = redisUtils.get(RedisKeys.getClearGrowthKey());
        String moreRuleJson = "";
        if (obj == null) {
            // 首次执行清除成长值操作
            // 获取成长值相关配置
            moreRuleJson = pointSettingService.findFromRedis(PointSettingEnum.MORE_RULE_NAME.value());
        }
        if (StringUtils.isBlank(moreRuleJson)) {
            return false;
        }
        // 解析JSON
        JSONObject ruleJsonObj = JSONObject.parseObject(moreRuleJson);
        // 获取计算时间
        Integer calculationTime = ruleJsonObj.getJSONObject("growthRule").getInteger("calculationTime");

        // 获取当前是本月第几天
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (date == calculationTime) {
            // 获取计算周期
            Integer calculationCycle = ruleJsonObj.getJSONObject("growthRule").getInteger("calculationCycle");

            // 获取当前时间 yyyy-MM-dd
            Date currentDate = DateUtils.longToDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN);
            Date startTime = null;

            if (calculationCycle != 0) {
                // 设置固定的成长值清零周期
                startTime = DateUtils.addDateMonths(currentDate, calculationCycle - (calculationCycle * 2));
            }

            // 永久不清除成长值  将用户所有成长值增加在一起
            List<MemberGrowthValueDTO> memberGrowthValueDTOList = baseDao.findGrowthbyTime(startTime, currentDate);

            memberGrowthValueDTOList.forEach(memberGrowthValueDTO -> {
                // 修改用户的成长值
                memberInfoService.updateGradePoint(memberGrowthValueDTO.getMemberId(), memberGrowthValueDTO.getGrowthValue());
            });

            // 如果执行完成 清除更多规则设置Redis
            redisUtils.delete(RedisKeys.getClearGrowthKey());
        }

        return true;
    }


    /**
     * @param params: 用户id和积分类型
     * @return java.util.List<com.leimingtech.modules.dto.log.point.PointLogDTO>
     * @Description 根据用户id和积分类型查询
     * @Author huangkeyuan
     * @Date 14:31 2019-12-25
     */
    @Override

    public List<PointLogDTO> queryWithMemberId(@RequestParam Map<String, Object> params) {
        List<PointLogEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, PointLogDTO.class);
    }

    /**
     * 根据时间查询日志
     *
     * @param params 查询条件
     * @return
     * @date 2020-01-14 16:29
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public List<PointLogDTO> queryByTime(@RequestParam Map<String, Object> params) {
        List<PointLogDTO> list = baseDao.findByTime(params);
        return list;
    }
}
