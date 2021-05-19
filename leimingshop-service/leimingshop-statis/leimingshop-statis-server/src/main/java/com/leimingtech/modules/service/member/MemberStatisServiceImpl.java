/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.common.GetCondition;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.CollectionName;
import com.leimingtech.modules.constants.StatisticModuleConstant;
import com.leimingtech.modules.dao.member.MemberStatisDao;
import com.leimingtech.modules.dto.member.MemberGradeNameDTO;
import com.leimingtech.modules.dto.member.MemberGradeShowDTO;
import com.leimingtech.modules.dto.member.MemberStatisDTO;
import com.leimingtech.modules.entity.member.MemberGradeStatisEntity;
import com.leimingtech.modules.entity.member.MemberStatisEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.statuscode.StatisticStatusCode;
import com.leimingtech.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName MemberStatisService
 * @Description 会员统计接口实现类
 * @Author xuzhch
 * @Date 2019年12月6日
 * @Version 1.0
 **/
@Slf4j
@Service

public class MemberStatisServiceImpl implements MemberStatisService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(MemberStatisServiceImpl.class);


    @Autowired
    private MemberStatisDao memberStatisDao;

    @Autowired
    private NosqlService nosqlService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存会员信息统计
     *
     * @param params url?params=yyyy-MM-dd HH:mm:ss
     * @throws ParseException 日期转化异常
     */
    @Override

    //@DataSource("member")
    public void saveMemberStatis(@RequestParam(required = false) String params) throws ParseException {
        //初始化查询数据
        String date = null;
        DateFormat hour = new SimpleDateFormat("yyyy-MM-dd HH");
        DateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat month = new SimpleDateFormat("yyyy-MM");
        if (StringUtils.isNotBlank(params)) {
            Date reqTime = hour.parse(params);
            date = hour.format(reqTime);
        } else {
            Date nowTime = DateUtil.offsetHour(new Date(), -1);
            date = hour.format(nowTime);
        }
        //判断MongoDB是否存在数据
        Date parse = DateUtils.parse(date, "yyyy-MM-dd HH");
        Query query = Query.query(Criteria.where("createHourTime").is(parse));
        List<MemberStatisEntity> selectRes = mongoTemplate.find(query, MemberStatisEntity.class);
        //初始化日志保存数据
        Map<String, String> logMap = new HashMap<>(1);
        logMap.put("保存日期", date);
        if (selectRes != null && selectRes.size() > 0) {
            mlogger.info(StatisticStatusCode.MEMBER_STATISTIC_DATA_REPEAT_SAVE_CODE, StatisticStatusCode.MEMBER_STATISTIC_DATA_REPEAT_SAVE_MASSAGE, logMap);
            return;
        }

        //查询数据库 获取数据
        List<Map<String, Object>> findRes = memberStatisDao.selectMemberAddCount(date);
        //封装数据
        MemberStatisEntity memberStatis = new MemberStatisEntity();
        memberStatis.setId(IdWorker.getId());
        memberStatis.setCreateHourTime(hour.parse(date));
        memberStatis.setCreateDayTime(day.parse(date));
        memberStatis.setCreateMonthTime(month.parse(date));
        int sumCount = 0;
        //用户来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
        for (Map<String, Object> findRe : findRes) {
            Object source = findRe.get("source");
            Object num = findRe.get("num");
            //0:pc
            if (null != source && num != null && StatisticModuleConstant.PC.equals(source.toString())) {
                memberStatis.setPcIncreaseNumber(Integer.valueOf(num.toString()));
                sumCount = sumCount + Integer.valueOf(num.toString());
            }
            //1:h5
            if (null != source && num != null && StatisticModuleConstant.H5.equals(source.toString())) {
                memberStatis.setOtherIncreaseNumber(Integer.valueOf(num.toString()));
                sumCount = sumCount + Integer.valueOf(num.toString());
            }
            //2:android
            if (null != source && num != null && StatisticModuleConstant.ANDROID.equals(source.toString())) {
                memberStatis.setAndroidIncreaseNumber(Integer.valueOf(num.toString()));
                sumCount = sumCount + Integer.valueOf(num.toString());
            }
            //3:ios
            if (null != source && num != null && StatisticModuleConstant.IOS.equals(source.toString())) {
                memberStatis.setIosIncreaseNumber(Integer.valueOf(num.toString()));
                sumCount = sumCount + Integer.valueOf(num.toString());
            }
            //4:微信
            if (null != source && num != null && StatisticModuleConstant.WECHAT.equals(source.toString())) {
                memberStatis.setWechatIncreaseNumber(Integer.valueOf(num.toString()));
                sumCount = sumCount + Integer.valueOf(num.toString());
            }
            //5:小程序
            if (null != source && num != null && StatisticModuleConstant.APPLETS.equals(source.toString())) {
                memberStatis.setAppletsIncreaseNumber(Integer.valueOf(num.toString()));
                sumCount = sumCount + Integer.valueOf(num.toString());
            }
        }
        if (sumCount > 0) {
            memberStatis.setMemberIncreaseNumber(sumCount);
            //批量保存到MongoDB
            mongoTemplate.save(memberStatis, CollectionName.MEMBER_STATISTICS);
            mlogger.info(StatisticStatusCode.MEMBER_STATISTIC_DATA_SAVE_CODE, StatisticStatusCode.MEMBER_STATISTIC_DATA_SAVE_MASSAGE, logMap);
        } else {
            mlogger.info(StatisticStatusCode.MEMBER_STATISTIC_DATA_SAVE_CODE, StatisticStatusCode.MEMBER_STATISTIC_DATA_NOT_SAVE_MASSAGE, logMap);
        }


    }

    /**
     * 会员等级数据统计
     *
     * @param params
     * @throws ParseException 日期转换异常
     * @Author xuzhch
     */
    @Override

    //@DataSource("member")
    public void saveMemberGradeStatis(@RequestParam(required = false) String params) throws ParseException {
        String date = null;
        DateFormat hour = new SimpleDateFormat("yyyy-MM-dd HH");
        DateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat month = new SimpleDateFormat("yyyy-MM");
        if (StringUtils.isNotBlank(params)) {
            Date reqTime = hour.parse(params);
            date = hour.format(reqTime);
        } else {
            Date nowTime = DateUtil.offsetHour(new Date(), -1);
            date = hour.format(nowTime);
        }

        //判断MongoDB是否存在数据
        Date parse = DateUtils.parse(date, "yyyy-MM-dd HH");
        Query query = Query.query(Criteria.where("createHourTime").is(parse));
        List<MemberGradeStatisEntity> selectRes = mongoTemplate.find(query, MemberGradeStatisEntity.class);
        //初始化日志保存数据
        Map<String, String> logMap = new HashMap<>(1);
        logMap.put("保存日期", date);
        if (selectRes != null && selectRes.size() > 0) {
            mlogger.info(StatisticStatusCode.MEMBER_GRADE_RATIO_STATISTIC_DATA_REPEAT_SAVE_CODE, StatisticStatusCode.MEMBER_GRADE_RATIO_STATISTIC_DATA_REPEAT_SAVE_MASSAGE, logMap);
            return;
        }

        List<MemberGradeNameDTO> gradeNameList = memberStatisDao.selectMemberGrade();
        for (int i = 0; i < gradeNameList.size(); i++) {
            gradeNameList.get(i).setStart(i);
            gradeNameList.get(i).setEnd(i + 1);
        }
        List<MemberGradeStatisEntity> memberGradeStatisList = memberStatisDao.selectMemberGradeStatis(gradeNameList);
        for (MemberGradeStatisEntity memberGradeStatis : memberGradeStatisList) {
            memberGradeStatis.setId(IdWorker.getId());
            memberGradeStatis.setCreateHourTime(hour.parse(date));
            memberGradeStatis.setCreateDayTime(day.parse(date));
            memberGradeStatis.setCreateMonthTime(month.parse(date));
        }
        nosqlService.saveBatch(memberGradeStatisList, CollectionName.MEMBER_GRADER_STATISTICS);
        mlogger.info(StatisticStatusCode.MEMBER_GRADE_RATIO_STATISTIC_DATA_SAVE_CODE, StatisticStatusCode.MEMBER_GRADE_RATIO_STATISTIC_DATA_SAVE_MASSAGE, logMap);

    }

    /**
     * 会员增长统计展示
     *
     * @param params Map {
     *               (name = "timeType", value = "时间类型(1:小时,2:日,3:月，4:年)", paramType = "query",defaultValue = "1",dataType = "int" ),
     *               (name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date" ),
     *               (name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date" )
     *               }
     */

    @Override
    public List<MemberStatisDTO> memberGrowStatis(@RequestParam Map params) throws ParseException {
        SimpleDateFormat dateFormat = null;

        Criteria dateWhere = GetCondition.getCriteria(params, dateFormat);
        MatchOperation matchOperation = Aggregation.match(dateWhere);
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group(dateWhere.getKey())
                        .sum("memberIncreaseNumber").as("memberIncreaseNumber"),
                Aggregation.project("memberIncreaseNumber").and(dateWhere.getKey()).previousOperation()
        );

        AggregationResults<MemberStatisEntity> aggregate = mongoTemplate.aggregate(aggregation, MemberStatisEntity.class, MemberStatisEntity.class);
        List<MemberStatisEntity> mappedResults = aggregate.getMappedResults();

//        Map<String, MemberStatisDTO> result = getMemberGrowInit(params,mappedResults);
        List<MemberStatisDTO> result = getMemberGrowInit(params, mappedResults);
        return result;
    }

    /**
     * 封装用户统计结果
     *
     * @param params        日期参数
     * @param mappedResults 查询结果
     * @return 统计结果
     * @throws ParseException 日期转换异常
     * @author xuzhch
     * @date 2020年9月17日
     */
    private List<MemberStatisDTO> getMemberGrowInit(Map params, List<MemberStatisEntity> mappedResults) throws ParseException {
        SimpleDateFormat dateFormat = null;
        String timeType = params.get("timeType").toString();
        String startDateStr = (String) params.get("startDate");
        String endDateStr = (String) params.get("endDate");
        Date start = null;
        Date end = null;
        List<String> dateList = new ArrayList<>();
        Map<String, MemberStatisDTO> resultMap = new HashMap<>(3);
        if (StatisticModuleConstant.TIME_TYPE_HOURS.equals(timeType)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            start = dateFormat.parse(startDateStr);
            end = dateFormat.parse(endDateStr);
            dateList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_HOUR_PATTERN);
        }
        if (StatisticModuleConstant.TIME_TYPE_DAY.equals(timeType)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            start = dateFormat.parse(startDateStr);
            end = dateFormat.parse(endDateStr);
            dateList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_PATTERN);
        }
        if (StatisticModuleConstant.TIME_TYPE_MONTH.equals(timeType)) {
            dateFormat = new SimpleDateFormat("yyyy-MM");
            start = dateFormat.parse(startDateStr);
            end = dateFormat.parse(endDateStr);
            dateList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_MONTH_PATTERN);
        }
        for (String s : dateList) {
            MemberStatisDTO memberStatisDTO = MemberStatisDTO.newMemberStatisDTO();
            if (StatisticModuleConstant.TIME_TYPE_HOURS.equals(timeType)) {
                memberStatisDTO.setCreateHourTime(dateFormat.parse(s));
                for (MemberStatisEntity mappedResult : mappedResults) {
                    if (dateFormat.format(mappedResult.getCreateHourTime()).equals(s)) {
                        memberStatisDTO = ConvertUtils.sourceToTarget(mappedResult, MemberStatisDTO.class);
                    }
                }
            }
            if (StatisticModuleConstant.TIME_TYPE_DAY.equals(timeType)) {
                memberStatisDTO.setCreateDayTime(dateFormat.parse(s));
                for (MemberStatisEntity mappedResult : mappedResults) {
                    if (dateFormat.format(mappedResult.getCreateDayTime()).equals(s)) {
                        memberStatisDTO = ConvertUtils.sourceToTarget(mappedResult, MemberStatisDTO.class);
                    }
                }

            }
            if (StatisticModuleConstant.TIME_TYPE_MONTH.equals(timeType)) {
                memberStatisDTO.setCreateMonthTime(dateFormat.parse(s));
                for (MemberStatisEntity mappedResult : mappedResults) {
                    if (dateFormat.format(mappedResult.getCreateMonthTime()).equals(s)) {
                        memberStatisDTO = ConvertUtils.sourceToTarget(mappedResult, MemberStatisDTO.class);
                    }
                }

            }
            resultMap.put(s, memberStatisDTO);
        }
        List<MemberStatisDTO> memberStatisData = null;
        if (StatisticModuleConstant.TIME_TYPE_HOURS.equals(timeType)) {
            resultMap.values().stream().sorted(Comparator.comparing(MemberStatisDTO::getCreateHourTime)).collect(Collectors.toList());
            memberStatisData = resultMap.values().stream().sorted(Comparator.comparing(MemberStatisDTO::getCreateHourTime)).collect(Collectors.toList());
        }
        if (StatisticModuleConstant.TIME_TYPE_DAY.equals(timeType)) {
            resultMap.values().stream().sorted(Comparator.comparing(MemberStatisDTO::getCreateDayTime)).collect(Collectors.toList());
            memberStatisData = resultMap.values().stream().sorted(Comparator.comparing(MemberStatisDTO::getCreateDayTime)).collect(Collectors.toList());
        }
        if (StatisticModuleConstant.TIME_TYPE_MONTH.equals(timeType)) {
            memberStatisData = resultMap.values().stream().sorted(Comparator.comparing(MemberStatisDTO::getCreateMonthTime)).collect(Collectors.toList());
        }
//        return resultMap;
        return memberStatisData;
    }


    /**
     * 会员等级占比
     *
     * @param params
     * @return
     * @throws ParseException 日期转换异常
     */
    @Override

    public Map<String, List<MemberGradeShowDTO>> memberGradeProportion(@RequestParam Map params) throws ParseException {
        SimpleDateFormat dateFormat = null;
        String timeType = params.get("timeType").toString();
        //获取查询条件
        Criteria dateWhere = GetCondition.getCriteria(params, dateFormat);
        //设置查询条件
        MatchOperation matchOperation = Aggregation.match(dateWhere);
        //设置聚合参数 和返回数据
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group(dateWhere.getKey(), "gradeId", "memberGraderName")
                        .sum("memberNumber").as("memberNumber"),
                Aggregation.project("memberNumber")
                        .and("memberGraderName").as("memberGraderName").andInclude("memberGraderName")
                        .and(dateWhere.getKey()).as(dateWhere.getKey()).andInclude(dateWhere.getKey())
                        .and("gradeId").as("gradeId").andInclude("gradeId")
        );
        //执行查询
        AggregationResults<MemberGradeStatisEntity> aggregate = mongoTemplate.aggregate(aggregation, MemberGradeStatisEntity.class, MemberGradeStatisEntity.class);
        Map createTime = (Map) dateWhere.getCriteriaObject().get(dateWhere.getKey());
        Date start = (Date) createTime.get("$gte");
        Date end = (Date) createTime.get("$lte");
        List<String> dateStrList = new ArrayList<>();
        if (StatisticModuleConstant.TIME_TYPE_HOURS.equals(timeType)) {
            dateStrList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_HOUR_PATTERN);
            dateFormat = new SimpleDateFormat(DateUtils.DATE_HOUR_PATTERN);

        }
        if (StatisticModuleConstant.TIME_TYPE_DAY.equals(timeType)) {
            dateStrList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_PATTERN);
            dateFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN);

        }
        if (StatisticModuleConstant.TIME_TYPE_MONTH.equals(timeType)) {
            dateStrList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_MONTH_PATTERN);
            dateFormat = new SimpleDateFormat(DateUtils.DATE_MONTH_PATTERN);
        }
        //封装返回数据

        //设置 map 的Value
        Map<String, List<MemberGradeShowDTO>> result = new TreeMap<>();
        for (String s : dateStrList) {
            List<MemberGradeShowDTO> memberGradeShowDTOList = new ArrayList<>();
            result.put(s, memberGradeShowDTOList);

        }
        for (MemberGradeStatisEntity memberGradeStatisEntity : aggregate) {
            MemberGradeShowDTO memberGradeShowDTO = new MemberGradeShowDTO();
            if (StatisticModuleConstant.TIME_TYPE_HOURS.equals(timeType)) {
                String dateStr = dateFormat.format(memberGradeStatisEntity.getCreateHourTime());
                if (result.containsKey(dateStr)) {
                    BeanCopier.create(MemberGradeStatisEntity.class, MemberGradeShowDTO.class, false)
                            .copy(memberGradeStatisEntity, memberGradeShowDTO, null);
                    List<MemberGradeShowDTO> memberGradeShowDTOList = result.get(dateStr);
                    memberGradeShowDTOList.add(memberGradeShowDTO);
                    result.put(dateStr, memberGradeShowDTOList);

                }
            }
            if (StatisticModuleConstant.TIME_TYPE_DAY.equals(timeType)) {
                String dateStr = dateFormat.format(memberGradeStatisEntity.getCreateDayTime());
                if (result.containsKey(dateStr)) {
                    BeanCopier.create(MemberGradeStatisEntity.class, MemberGradeShowDTO.class, false)
                            .copy(memberGradeStatisEntity, memberGradeShowDTO, null);
                    List<MemberGradeShowDTO> memberGradeShowDTOList = result.get(dateStr);
                    memberGradeShowDTOList.add(memberGradeShowDTO);
                    result.put(dateStr, memberGradeShowDTOList);
                }
            }
            if (StatisticModuleConstant.TIME_TYPE_MONTH.equals(timeType)) {
                String dateStr = dateFormat.format(memberGradeStatisEntity.getCreateMonthTime());
                if (result.containsKey(dateStr)) {
                    BeanCopier.create(MemberGradeStatisEntity.class, MemberGradeShowDTO.class, false)
                            .copy(memberGradeStatisEntity, memberGradeShowDTO, null);
                    List<MemberGradeShowDTO> memberGradeShowDTOList = result.get(dateStr);
                    memberGradeShowDTOList.add(memberGradeShowDTO);
                    result.put(dateStr, memberGradeShowDTOList);
                }
            }
        }
        //设置会员等级占比
        for (String s : dateStrList) {
            List<MemberGradeShowDTO> memberGradeShowDTOList = result.get(s);
            if (CollectionUtils.isNotEmpty(memberGradeShowDTOList)) {
                int sum = memberGradeShowDTOList.stream().mapToInt(MemberGradeShowDTO::getMemberNumber).sum();
                for (MemberGradeShowDTO statisDTO : memberGradeShowDTOList) {
                    Double aDouble = NumberUtils.divisionTwoDecimal(statisDTO.getMemberNumber(), sum);
                    statisDTO.setGradeConversion((aDouble * 100) + "%");
                }
                result.put(s, memberGradeShowDTOList);
            }
        }
        return result;
    }


    @Override
    public List<MemberStatisDTO> memberSource(@RequestParam Map params) throws ParseException {
        //小时维度
        SimpleDateFormat dateFormat = null;

        Criteria dateWhere = GetCondition.getCriteria(params, dateFormat);
        MatchOperation matchOperation = Aggregation.match(dateWhere);
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group(dateWhere.getKey())
                        .sum("memberIncreaseNumber").as("memberIncreaseNumber")
                        .sum("androidIncreaseNumber").as("androidIncreaseNumber")
                        .sum("iosIncreaseNumber").as("iosIncreaseNumber")
                        .sum("pcIncreaseNumber").as("pcIncreaseNumber")
                        .sum("otherIncreaseNumber").as("otherIncreaseNumber")
                        .sum("wechatIncreaseNumber").as("wechatIncreaseNumber")
                        .sum("appletsIncreaseNumber").as("appletsIncreaseNumber"),
                Aggregation.project("memberIncreaseNumber", "androidIncreaseNumber", "iosIncreaseNumber", "pcIncreaseNumber", "otherIncreaseNumber", "wechatIncreaseNumber", "appletsIncreaseNumber")
                        .and(dateWhere.getKey()).previousOperation()
        );

        AggregationResults<MemberStatisEntity> aggregate = mongoTemplate.aggregate(aggregation, MemberStatisEntity.class, MemberStatisEntity.class);
        List<MemberStatisEntity> mappedResults = aggregate.getMappedResults();

//        Map<String, MemberStatisDTO> result = getMemberGrowInit(params,mappedResults);
        List<MemberStatisDTO> result = getMemberGrowInit(params, mappedResults);
        return result;
    }

//    public static void main(String[] args) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
//        Date date = new Date();
//        for (int i = 1; i <= 120 * 24; i++) {
//            Date reqDate = DateUtil.offsetHour(date, -i);
//            String format = dateFormat.format(reqDate);
//            String url = "http://localhost:30216/statis/member/member";
////            String url = "curl -H \\\"Content-Type: application/json\\\"  http://localhost:30216/statis/member/member -X POST -d '{\\\"params\\\":\\\"" + format + "\\\"}'";
//
//
////            String s = HttpUtil.get(url);
//            Map<String, Object> map = new HashMap<>();
//            map.put("params",format);
//            String s = HttpUtil.post(url, map);
//            System.out.println(url);
//
//        }
//    }
}
