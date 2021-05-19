/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.traffic;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.common.GetCondition;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.CollectionName;
import com.leimingtech.modules.constants.StatisticModuleConstant;
import com.leimingtech.modules.dao.activity.ActivityStatisDao;
import com.leimingtech.modules.dto.activity.ActivityPvDTO;
import com.leimingtech.modules.dto.traffic.PageSourceStatisticDTO;
import com.leimingtech.modules.dto.traffic.PvDetailDTO;
import com.leimingtech.modules.dto.traffic.PvStatisticShowDTO;
import com.leimingtech.modules.dto.traffic.SourceStatisticDTO;
import com.leimingtech.modules.entity.traffic.PvDetailEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @ClassName TrafficStatisService
 * @Description 流量统计接口实现类
 * @Author xuzhch
 * @Date 2019年12月6日
 * @Version 1.0
 **/

@Slf4j
@Service

public class PvDetailStatisServiceImpl implements PvDetailStatisService {

    /**
     * 访问来源标记 0:全站
     */
    public static final Integer ALL_SITE = 0;
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(PvDetailStatisServiceImpl.class);
    @Autowired
    private NosqlService nosqlService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ActivityStatisDao activityStatisDao;


    /**
     * 保存流量统计记录
     *
     * @param pvDetail 浏览量保存对象
     * @return
     * @date 2019/12/31/031 14:57
     * @author xuzhch
     **/
    @Override

    public void savePageview(@RequestBody PvDetailDTO pvDetail) {
        //设置浏览时间  （按月、天、时 区分）
        Date createTime = pvDetail.getCreateTime();
        DateFormat timesFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = timesFormat.format(createTime);
        Date hour = DateUtils.parse(times, "yyyy-MM-dd HH");
        Date day = DateUtils.parse(times, "yyyy-MM-dd");
        Date month = DateUtils.parse(times, "yyyy-MM");
        pvDetail.setCreateHourTime(hour);
        pvDetail.setCreateDayTime(day);
        pvDetail.setCreateMonthTime(month);
        //设置ID
        pvDetail.setId(IdWorker.getId());
        nosqlService.saveObj(pvDetail, CollectionName.PV_DETAIL);

        // 判断商品详情页是否有活动
        if (pvDetail.getActivityType() != null) {
            saveActivityView(pvDetail);
        }
    }

    /**
     * 统计活动访问量
     *
     * @param pvDetail
     */
    private void saveActivityView(PvDetailDTO pvDetail) {

        ActivityPvDTO activityPvDTO = ConvertUtils.sourceToTarget(pvDetail, ActivityPvDTO.class);

        //  满减--商品详情页默认满减活动
        if (StatisticModuleConstant.REDUCE_TYPE.equals(pvDetail.getActivityType())) {
            //查询出当前活动的店铺
            nosqlService.saveObj(activityPvDTO, CollectionName.ACTIVITY_PV_DETAIL);
        }
    }


    @Override

    /**
     * 查询浏览量统计数据
     *
     * @param params {
     *          (name = "timeType", value = "时间类型(1:时,2:日,3:月)", paramType = "query", required = true, dataType = "int"),
     *          (name = "startDate", value = "查询开始时间", paramType = "query", required = true, dataType = "Date"),
     *          (name = "endDate", value = "查询结束时间", paramType = "query", required = true, dataType = "Date"),
     *          (name = "statisticType", value = "统计页面(1:全站、2:首页、3:商品分类页、4:购物车、5:商品详情页)", paramType = "query", required = true, defaultValue = "1", dataType = "int")
     * }
     * @return Map  key为时间节点   value是浏览量数据
     * @date 2019/12/31/031 14:58
     * @author xuzhch
     **/
    public Map<String, PvStatisticShowDTO> flowStatistic(@RequestParam Map params) throws ParseException {
        SimpleDateFormat dateFormat = null;
        //获取日期条件
        Criteria dateWhere = GetCondition.getCriteria(params, dateFormat);
//        统计页面(0:全站、1:首页、2:商品分类页、3:购物车、4:商品详情页)
        String statisticType = params.get("statisticType").toString();
        MatchOperation match = null;
        if (ALL_SITE.toString().equals(statisticType)) {
            match = Aggregation.match(dateWhere);
        } else {
            match = Aggregation.match(dateWhere.andOperator(Criteria.where("pageType").is(Integer.valueOf(statisticType))));
        }

        //获取查询时间类型  (1:时,2:日,3:月)
        String timeType = params.get("timeType").toString();
        //封装PV查询条件，并执行查询
        Aggregation pvCondition = Aggregation.newAggregation(match, Aggregation.group(dateWhere.getKey()).count().as("pvCount"), Aggregation.project("pvCount").and(dateWhere.getKey()).previousOperation());
        AggregationResults<Object> pvRes = mongoTemplate.aggregate(pvCondition, CollectionName.PV_DETAIL, Object.class);

        //封装UV查询条件，并执行查询
        Aggregation uvCondtion = Aggregation.newAggregation(match, Aggregation.group(dateWhere.getKey(), "ipDetail"),
                Aggregation.project().and(dateWhere.getKey()).as(dateWhere.getKey()).andInclude(dateWhere.getKey())
                        .and("ipDetail").as("ipDetail").andInclude("ipDetail"));
        AggregationResults<PvDetailEntity> uvRes = mongoTemplate.aggregate(uvCondtion, CollectionName.PV_DETAIL, PvDetailEntity.class);
        List<PvDetailEntity> mappedResults = uvRes.getMappedResults();
        Map createTime = (Map) dateWhere.getCriteriaObject().get(dateWhere.getKey());
        Date start = (Date) createTime.get("$gte");
        Date end = (Date) createTime.get("$lte");
        Map<Date, Long> collect = new HashMap<>(3);
        List<String> keyList = new ArrayList<String>();
        if (StatisticModuleConstant.TIME_TYPE_HOURS.equals(timeType)) {
            keyList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_HOUR_PATTERN);
            dateFormat = new SimpleDateFormat(DateUtils.DATE_HOUR_PATTERN);
            collect = mappedResults.stream().collect(Collectors.groupingBy(PvDetailEntity::getCreateHourTime, Collectors.counting()));

        }
        if (StatisticModuleConstant.TIME_TYPE_DAY.equals(timeType)) {
            keyList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_PATTERN);
            dateFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN);
            collect = mappedResults.stream().collect(Collectors.groupingBy(PvDetailEntity::getCreateDayTime, Collectors.counting()));

        }
        if (StatisticModuleConstant.TIME_TYPE_MONTH.equals(timeType)) {
            keyList = DateUtils.getSpecifyDate(start, end, DateUtils.DATE_MONTH_PATTERN);
            dateFormat = new SimpleDateFormat(DateUtils.DATE_MONTH_PATTERN);
            collect = mappedResults.stream().collect(Collectors.groupingBy(PvDetailEntity::getCreateMonthTime, Collectors.counting()));
        }
        Map<String, PvStatisticShowDTO> resultMap = new TreeMap<>();
        for (String s : keyList) {
            PvStatisticShowDTO pvStatisticShowDTO = new PvStatisticShowDTO(0, 0);
            resultMap.put(s, pvStatisticShowDTO);
        }
        for (Object mappedResult : pvRes.getMappedResults()) {
            Map mapPvResult = (Map) mappedResult;
            Date date = (Date) mapPvResult.get(dateWhere.getKey());
            PvStatisticShowDTO pvStatisticShowDTO = resultMap.get(dateFormat.format(date));
            Integer pvCount = (Integer) mapPvResult.get("pvCount");
            pvStatisticShowDTO.setPv(pvCount);
        }
        for (Date date : collect.keySet()) {
            PvStatisticShowDTO pvStatisticShowDTO = resultMap.get(dateFormat.format(date));
            Long uvL = collect.get(date);
            pvStatisticShowDTO.setUv(uvL.intValue());
        }
        return resultMap;

    }


    /**
     * 页面流量来源统计
     *
     * @param params
     * @return
     * @date 2020/3/14/014 19:30
     * @author xuzhch
     **/

    @Override
    public PageSourceStatisticDTO pageStatisticSource(@RequestParam Map params) throws ParseException {
        PageSourceStatisticDTO result = PageSourceStatisticDTO.newPageSourceStatisticDTO();
        //获取日期条件
        Criteria dateWhere = GetCondition.getDateWhereCriteria(params);
        MatchOperation match = Aggregation.match(dateWhere);
        Aggregation pvCondi = Aggregation.newAggregation(match,
                Aggregation.group("browsePlatforms").count().as("pvCount"),
                Aggregation.project("pvCount").and("browsePlatforms").previousOperation());
        List<SourceStatisticDTO> pvRes = mongoTemplate.aggregate(pvCondi, CollectionName.PV_DETAIL, SourceStatisticDTO.class).getMappedResults();
        pvRes.stream().forEach(sourceStatisticDTO -> {
            if (Integer.valueOf(StatisticModuleConstant.PC).equals(sourceStatisticDTO.getBrowsePlatforms())) {
                result.setPvPcCount(sourceStatisticDTO.getPvCount());
            }
            if (Integer.valueOf(StatisticModuleConstant.H5).equals(sourceStatisticDTO.getBrowsePlatforms())) {
                result.setPvOtherCount(sourceStatisticDTO.getPvCount());
            }
            if (Integer.valueOf(StatisticModuleConstant.ANDROID).equals(sourceStatisticDTO.getBrowsePlatforms())) {
                result.setPvAndroidCount(sourceStatisticDTO.getPvCount());
            }
            if (Integer.valueOf(StatisticModuleConstant.IOS).equals(sourceStatisticDTO.getBrowsePlatforms())) {
                result.setPvIosCount(sourceStatisticDTO.getPvCount());
            }
            if (Integer.valueOf(StatisticModuleConstant.WECHAT).equals(sourceStatisticDTO.getBrowsePlatforms())) {
                result.setPvWeChatCount(sourceStatisticDTO.getPvCount());
            }
            if (Integer.valueOf(StatisticModuleConstant.APPLETS).equals(sourceStatisticDTO.getBrowsePlatforms())) {
                result.setPvAppletsCount(sourceStatisticDTO.getPvCount());
            }
            result.setPvAllCount(result.getPvAllCount() + sourceStatisticDTO.getPvCount());
        });

        Aggregation aggregation = Aggregation.newAggregation(match,
                Aggregation.group("browsePlatforms", "ipDetail"),
                Aggregation.project().and("browsePlatforms").as("browsePlatforms").andInclude("browsePlatforms").and("ipDetail").as("ipDetail").andInclude("ipDetail"));
        List<SourceStatisticDTO> uvRes = mongoTemplate.aggregate(aggregation, CollectionName.PV_DETAIL, SourceStatisticDTO.class).getMappedResults();
        Map<Integer, List<SourceStatisticDTO>> uvMap = uvRes.stream().collect(Collectors.groupingBy(SourceStatisticDTO::getBrowsePlatforms));
        AtomicReference<Integer> uvAllCount = new AtomicReference<>(0);
        uvMap.keySet().stream().forEach(k -> {
            List<SourceStatisticDTO> sourceStatisticDTOList = uvMap.get(k);
            //用户来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
            if (Integer.valueOf(StatisticModuleConstant.PC).equals(k)) {
                result.setUvPcCount(sourceStatisticDTOList.size());
            }
            if (Integer.valueOf(StatisticModuleConstant.H5).equals(k)) {
                result.setUvOtherCount(sourceStatisticDTOList.size());
            }
            if (Integer.valueOf(StatisticModuleConstant.ANDROID).equals(k)) {
                result.setUvAndroidCount(sourceStatisticDTOList.size());
            }
            if (Integer.valueOf(StatisticModuleConstant.IOS).equals(k)) {
                result.setUvIosCount(sourceStatisticDTOList.size());
            }
            if (Integer.valueOf(StatisticModuleConstant.WECHAT).equals(k)) {
                result.setUvWeChatCount(sourceStatisticDTOList.size());
            }
            if (Integer.valueOf(StatisticModuleConstant.APPLETS).equals(k)) {
                result.setUvAppletsCount(sourceStatisticDTOList.size());
            }
            result.setUvAllCount(sourceStatisticDTOList.size() + result.getUvAllCount());
        });

        if (0 != result.getPvAllCount()) {
            result.setPvPcProportion(NumberUtils.divisionTwoDecimal(result.getPvPcCount(), result.getPvAllCount()));
            result.setPvWeChatProportion(NumberUtils.divisionTwoDecimal(result.getPvWeChatCount(), result.getPvAllCount()));
            result.setPvAndroidProportion(NumberUtils.divisionTwoDecimal(result.getPvAndroidCount(), result.getPvAllCount()));
            result.setPvIosProportion(NumberUtils.divisionTwoDecimal(result.getPvIosCount(), result.getPvAllCount()));
            result.setPvOtherProportion(NumberUtils.divisionTwoDecimal(result.getPvOtherCount(), result.getPvAllCount()));
            result.setPvAppletsProportion(NumberUtils.divisionTwoDecimal(result.getPvAppletsCount(), result.getPvAllCount()));
        }
        if (0 != result.getUvAllCount()) {
            result.setUvPcProportion(NumberUtils.divisionTwoDecimal(result.getUvPcCount(), result.getUvAllCount()));
            result.setUvWeChatProportion(NumberUtils.divisionTwoDecimal(result.getUvWeChatCount(), result.getUvAllCount()));
            result.setUvAndroidProportion(NumberUtils.divisionTwoDecimal(result.getUvAndroidCount(), result.getUvAllCount()));
            result.setUvIosProportion(NumberUtils.divisionTwoDecimal(result.getUvIosCount(), result.getUvAllCount()));
            result.setUvOtherProportion(NumberUtils.divisionTwoDecimal(result.getUvOtherCount(), result.getUvAllCount()));
            result.setUvAppletsProportion(NumberUtils.divisionTwoDecimal(result.getUvAppletsCount(), result.getUvAllCount()));
        }
        return result;

    }


}
