/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.common;

import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.modules.constants.DateTimeConstants;
import com.leimingtech.modules.constants.StatisticModuleConstant;
import com.leimingtech.modules.dto.activity.ActivityStatisDTO;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 获取查询条件
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
public class GetCondition {

    /**
     * 按照时间获取查询条件
     *
     * @param params
     * @param dateFormat
     * @return
     * @throws ParseException
     */
    public static Criteria getCriteria(Map params, SimpleDateFormat dateFormat) throws ParseException {
        //条件初始化
        Criteria dateWhere = null;
        String timeType = params.get("timeType").toString();
        String startDateStr = (String) params.get("startDate");
        String endDateStr = (String) params.get("endDate");
        if (StatisticModuleConstant.TIME_TYPE_HOURS.equals(timeType)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            dateWhere = Criteria.where("createHourTime").gte(dateFormat.parse(startDateStr)).lte(dateFormat.parse(endDateStr));
        }
        if (StatisticModuleConstant.TIME_TYPE_DAY.equals(timeType)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateWhere = Criteria.where("createDayTime").gte(dateFormat.parse(startDateStr)).lte(dateFormat.parse(endDateStr));
        }
        if (StatisticModuleConstant.TIME_TYPE_MONTH.equals(timeType)) {
            dateFormat = new SimpleDateFormat("yyyy-MM");
            dateWhere = Criteria.where("createMonthTime").gte(dateFormat.parse(startDateStr)).lte(dateFormat.parse(endDateStr));
        }
        return dateWhere;
    }

    /**
     * 按照查询类别获取查询条件
     *
     * @param params
     * @return
     */
    public static MatchOperation getMatchOperation(Map params) {
        Integer queryType = Integer.valueOf(params.get("queryType").toString());
        Object storeIdObj = params.get("storeId");
        //日期初始化
        Date date = new Date();
        Date start = null;
        Date end = null;
        //条件初始化
        MatchOperation matchOperation = null;
        Aggregation aggregation = null;

        Criteria dateWhere = null;
        Criteria storeIdWhere = null;
        //昨天
        if (queryType == DateTimeConstants.YESTERDAY) {
            start = DateUtils.addDateDays(date, -1);
            dateWhere = Criteria.where("createDayTime").is(start);
        }
        //近七天
        if (queryType == DateTimeConstants.SEVEN_DAY) {
            start = DateUtils.addDateWeeks(date, -2);
            end = DateUtils.addDateDays(date, -1);
            dateWhere = Criteria.where("createDayTime").gte(start).lte(end);
        }
        //近30天
        if (queryType == DateTimeConstants.THIRTY_DAY) {
            start = DateUtils.addDateDays(date, -31);
            end = DateUtils.addDateDays(date, -1);
            dateWhere = Criteria.where("createDayTime").gte(start).lte(end);
        }
        //自定义日期
        if (queryType == DateTimeConstants.CUSTOMIZE_TIME) {
            String startDate = params.get("startDate").toString();
            String endDate = params.get("endDate").toString();
//            //时间类型(1:年,2:月,3:日)
            start = DateUtils.parse(startDate, "yyyy-MM-dd");
            end = DateUtils.parse(endDate, "yyyy-MM-dd");
            dateWhere = Criteria.where("createDayTime").gte(start).lte(end);
        }
        /**************************************************************************************/

        if (null != storeIdObj) {
            storeIdWhere = Criteria.where("storeId").is(Long.valueOf(storeIdObj.toString()));
            matchOperation = Aggregation.match(dateWhere.andOperator(storeIdWhere));
        } else {
            matchOperation = Aggregation.match(dateWhere);
        }
        return matchOperation;
    }

    public static Criteria getStoreIdCriteria(Map params) {
        Object storeIdObj = params.get("storeId");
        if (null != storeIdObj) {
            Criteria storeIdWhere = Criteria.where("storeId").is(Long.valueOf(storeIdObj.toString()));
            return storeIdWhere;
        }
        return null;
    }

    public static Criteria getActivityIdCriteria(Map params) {
        Object activityIdObj = params.get("activityId");
        if (null != activityIdObj) {
            Criteria activityIdWhere = Criteria.where("activityId").is(Long.valueOf(activityIdObj.toString()));
            return activityIdWhere;
        }
        return null;
    }

    public static Criteria getActivityTypeCriteria(Map params) {
        Object activityTypeObj = params.get("activityType");
        if (null != activityTypeObj) {
            Criteria activityIdWhere = Criteria.where("activityType").is(Long.valueOf(activityTypeObj.toString()));
            return activityIdWhere;
        }
        return null;
    }

    public static Criteria getDateWhereCriteria(Map params) {
        Integer queryType = Integer.valueOf(params.get("queryType").toString());
        //日期初始化
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);
        date = DateUtils.parse(dateStr, "yyyy-MM-dd");
        Date start = null;
        Date end = null;
        //条件初始化
        Criteria dateWhere = null;
        //昨天
        if (queryType == DateTimeConstants.YESTERDAY) {
            start = DateUtils.addDateDays(date, -1);
            end = start;
            dateWhere = Criteria.where("createDayTime").is(start);
        }
        //近七天
        if (queryType == DateTimeConstants.SEVEN_DAY) {
            start = DateUtils.addDateWeeks(date, -1);
            end = DateUtils.addDateDays(date, -1);
            dateWhere = Criteria.where("createDayTime").gte(start).lte(end);
        }
        //近30天
        if (queryType == DateTimeConstants.THIRTY_DAY) {
            start = DateUtils.addDateDays(date, -31);
            end = DateUtils.addDateDays(date, -1);
            dateWhere = Criteria.where("createDayTime").gte(start).lte(end);
        }
        //自定义日期
        if (queryType == DateTimeConstants.CUSTOMIZE_TIME) {
            String startDate = params.get("startDate").toString();
            String endDate = params.get("endDate").toString();
            start = DateUtils.parse(startDate, "yyyy-MM-dd");
            end = DateUtils.parse(endDate, "yyyy-MM-dd");
            dateWhere = Criteria.where("createDayTime").gte(start).lte(end);
        }
        return dateWhere;
    }

    /**
     * 获取营销趋势时间范围
     *
     * @param params
     * @return
     */
    public static Map<String, ActivityStatisDTO> getStartDateAndEndDate(Map params) {
        Integer queryType = Integer.valueOf(params.get("queryType").toString());
        //日期初始化
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);
        date = DateUtils.parse(dateStr, "yyyy-MM-dd");
        Date start = null;
        Date end = null;

        //昨天
        if (queryType == DateTimeConstants.YESTERDAY) {
            start = DateUtils.addDateDays(date, -1);
            end = start;
        }
        //近七天
        if (queryType == DateTimeConstants.SEVEN_DAY) {
            start = DateUtils.addDateWeeks(date, -1);
            end = DateUtils.addDateDays(date, -1);
        }
        //近30天
        if (queryType == DateTimeConstants.THIRTY_DAY) {
            start = DateUtils.addDateDays(date, -31);
            end = DateUtils.addDateDays(date, -1);
        }
        //自定义日期
        if (queryType == DateTimeConstants.CUSTOMIZE_TIME) {
            String startDate = params.get("startDate").toString();
            String endDate = params.get("endDate").toString();
            start = DateUtils.parse(startDate, "yyyy-MM-dd");
            end = DateUtils.parse(endDate, "yyyy-MM-dd");
        }

        Map<String, ActivityStatisDTO> treeMap = new TreeMap<>();
        java.time.LocalDate startLocal = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        java.time.LocalDate endLocal = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //计算两个日期之间相差的日期并转成list集合
        List<String> collect = Stream.iterate(startLocal, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startLocal, endLocal) + 1)
                .map(java.time.LocalDate::toString)
                .collect(Collectors.toList());
        //遍历集合 设置初始化数据
        for (String s : collect) {
            ActivityStatisDTO activityStatisDTO = ActivityStatisDTO.newActivityStatisDTO();
            treeMap.put(s, activityStatisDTO);
        }
        return treeMap;
    }
}
