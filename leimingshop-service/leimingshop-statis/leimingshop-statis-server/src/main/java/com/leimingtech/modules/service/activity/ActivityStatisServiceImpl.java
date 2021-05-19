/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.activity;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.common.GetCondition;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.CollectionName;
import com.leimingtech.modules.dao.activity.ActivityStatisDao;
import com.leimingtech.modules.dto.activity.*;
import com.leimingtech.modules.entity.activity.ActivityDetailStatisEntity;
import com.leimingtech.modules.entity.activity.ActivityStatisEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ActivityStatisService
 * @Description 活动信息统计
 * @Author chengqian
 * @Date 2020-3-27
 * @Version 1.0
 **/

@Slf4j
@Service

public class ActivityStatisServiceImpl implements ActivityStatisService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(ActivityStatisServiceImpl.class);

    @Autowired
    private ActivityStatisDao activityStatisDao;

    @Autowired
    private NosqlService nosqlService;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 定时统计营销信息
     */
    @Override

    public void timeActivityStatis() throws ParseException {

        // 查询出所有优惠券活动信息
        List<ActivityStatisEntity> list = activityStatisDao.getCouponsList();
        list.stream().forEach(a -> {
            a.setActivityType(1);
        });
        // 查询出满减活动信息
        List<ActivityStatisEntity> reduceActivity = activityStatisDao.getReduceActivity();
        reduceActivity.stream().forEach(r -> {
            r.setActivityType(2);
        });
        list.addAll(reduceActivity);

        DateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        String date = day.format(DateUtil.yesterday());
        Date createDayTime = day.parse(date);
        list.stream().forEach(a -> {
            // 查询当天优惠券的领取量
            Integer personNum = activityStatisDao.getPersonNum(a.getActivityId(), date);
            a.setPersonNum(personNum);
            // 查询出活动订单信息
            ActivityStatisEntity activityStatisEntity;
            if (a.getActivityType() == 1) {
                activityStatisEntity = activityStatisDao.getCouponsActivityList(a.getActivityId(), date);
            } else {
                activityStatisEntity = activityStatisDao.getReduceActivityList(a.getActivityId(), date);
            }
            a.setActivityReduceAmount(activityStatisEntity.getActivityReduceAmount());
            a.setDownOrderNum(activityStatisEntity.getDownOrderNum());
            a.setPayAmount(activityStatisEntity.getPayAmount());
            a.setOrderAmount(activityStatisEntity.getOrderAmount());
            a.setPayOrderNum(activityStatisEntity.getPayOrderNum());
            a.setUserOrderNum(activityStatisEntity.getDownOrderNum());
            a.setRealityAmount(activityStatisEntity.getRealityAmount());
            a.setPayUseNum(activityStatisEntity.getPayOrderNum());
            a.setPayPerson(activityStatisEntity.getPayPerson());
            a.setDownOrderPerson(activityStatisEntity.getDownOrderPerson());

            a.setId(IdWorker.getId());
            a.setCreateTime(new Date());
            a.setCreateDayTime(createDayTime);
            //统计营销明细
            saveActivityDetail(a.getActivityId(), date, createDayTime);
        });

        nosqlService.saveBatch(list, CollectionName.ACTIVITY_SUMMARIZE_STATISTICS);

    }

    /**
     * 统计营销明细
     *
     * @param activityId
     * @param date
     * @param createDayTime
     */
    public void saveActivityDetail(Long activityId, String date, Date createDayTime) {

        List<ActivityDetailStatisEntity> activityDetailStatisEntities = new ArrayList();
        // 查询当前活动当天下单的商品信息
        List<ActivityDetailStatisEntity> detail = activityStatisDao.getActivityDetail(activityId, date);
        for (ActivityDetailStatisEntity detailStatisEntity : detail) {
            detailStatisEntity.setId(IdWorker.getId());
            detailStatisEntity.setCreateDayTime(createDayTime);
            detailStatisEntity.setCreateTime(new Date());
            BigDecimal subtract = detailStatisEntity.getRealityAmount().subtract(detailStatisEntity.getActivityReduceAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            detailStatisEntity.setPayAmount(subtract);
        }

        activityDetailStatisEntities.addAll(detail);

        nosqlService.saveBatch(activityDetailStatisEntities, CollectionName.ACTIVITY_DETAIL_STATISTICS);
    }

    /**
     * 营销统计展示
     *
     * @param map
     * @return
     */

    @Override
    public ActivityStatisFindDTO findActivityStatistic(@RequestParam Map<String, Object> map) {
        ActivityStatisFindDTO activityStatisFindDTO = new ActivityStatisFindDTO();

        ActivityStatisDTO activityStatisDTO = ActivityStatisDTO.newActivityStatisDTO();

        MatchOperation defMatchOperation = where(map, null);

        Aggregation aggregation = Aggregation.newAggregation(defMatchOperation,
                Aggregation
                        .group("activityType").first("activityType").as("activityType")
                        .sum("personNum").as("personNum")
                        .sum("downOrderNum").as("downOrderNum")
                        .sum("userOrderNum").as("userOrderNum")
                        .sum("payOrderNum").as("payOrderNum")
                        .sum("payUseNum").as("payUseNum")
                        .sum("orderAmount").as("orderAmount")
                        .sum("realityAmount").as("realityAmount")
                        .sum("payAmount").as("payAmount")
                        .sum("activityReduceAmount").as("activityReduceAmount")
        );
        // 领取量，下单订单数，下单使用量，下单金额，支付订单数，支付使用量，应收金额，实收金额，活动减免金额
        List<ActivityStatisDTO> mappedResults = mongoTemplate.aggregate(aggregation, CollectionName.ACTIVITY_SUMMARIZE_STATISTICS, ActivityStatisDTO.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(mappedResults)) {
            activityStatisDTO = mappedResults.get(0);
        }
        // 查询浏览量
        Aggregation pageViewAggr = Aggregation.newAggregation(defMatchOperation);
        List<ActivityPvDTO> pvDTOList = mongoTemplate.aggregate(pageViewAggr, CollectionName.ACTIVITY_PV_DETAIL, ActivityPvDTO.class).getMappedResults();
        activityStatisDTO.setPageView(pvDTOList.size());
        //访客量
        ArrayList<ActivityPvDTO> visitorsNumber = pvDTOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(ActivityPvDTO::getIpDetail))), ArrayList::new));
        activityStatisDTO.setVisitorsNumber(visitorsNumber.size());
        // 查询营销趋势
        packaging(activityStatisFindDTO, pvDTOList, map, pageViewAggr);
        // 下单人数
        List<ActivityDetailStatisEntity> list = mongoTemplate.aggregate(pageViewAggr, CollectionName.ACTIVITY_DETAIL_STATISTICS, ActivityDetailStatisEntity.class).getMappedResults();
        ArrayList<ActivityDetailStatisEntity> collect = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(ActivityDetailStatisEntity::getMemberId))), ArrayList::new));
        //支付人数
        List<ActivityDetailStatisEntity> payList = list.stream().filter(a -> (a.getPaymentStatus() == 1)).collect(Collectors.toList());
        ArrayList<ActivityDetailStatisEntity> payMember = payList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(ActivityDetailStatisEntity::getMemberId))), ArrayList::new));
        activityStatisDTO.setDownOrderPerson(collect.size());
        activityStatisDTO.setPayPerson(payMember.size());
        // 转化率：支付使用的优惠券数量/领取的优惠券数量*100%
        if (activityStatisDTO.getPayUseNum() != 0 && activityStatisDTO.getPersonNum() != 0) {
            Double aDouble = NumberUtils.divisionTwoDecimal(activityStatisDTO.getPayUseNum(), activityStatisDTO.getPersonNum());
            activityStatisDTO.setConversion(aDouble);
        } else {
            activityStatisDTO.setConversion(0.00);
        }
        activityStatisFindDTO.setActivityStatisDTO(activityStatisDTO);
        // 营销明细
        ActivityStatisGoodsPage activityStatisGoodsPage = activityGoodsStatis(map);
        activityStatisFindDTO.setActivityStatisGoodsPage(activityStatisGoodsPage);
        return activityStatisFindDTO;
    }

    /**
     * 封装营销趋势数据
     *
     * @param activityStatisFindDTO 返回结果集
     * @param pvDTOList             pv 结果集
     * @param pageViewAggr
     */
    public void packaging(ActivityStatisFindDTO activityStatisFindDTO, List<ActivityPvDTO> pvDTOList
            , Map<String, Object> params, Aggregation pageViewAggr) {
        List<ActivityStatisEntity> activityStatis = mongoTemplate.aggregate(pageViewAggr, CollectionName.ACTIVITY_SUMMARIZE_STATISTICS, ActivityStatisEntity.class).getMappedResults();
        Map<String, ActivityStatisDTO> map = GetCondition.getStartDateAndEndDate(params);

        Map<Date, List<ActivityPvDTO>> pvMap = pvDTOList.stream().collect(Collectors.groupingBy(ActivityPvDTO::getCreateDayTime));


        Map<Date, List<ActivityStatisEntity>> mapList = activityStatis.stream().collect(Collectors.groupingBy(ActivityStatisEntity::getCreateDayTime));

        for (Map.Entry<String, ActivityStatisDTO> entryUser : map.entrySet()) {

            Date parse = DateUtils.parse(entryUser.getKey() + " 00:00:00", DateUtils.DATE_TIME_PATTERN);

            ActivityStatisDTO dto = ActivityStatisDTO.newActivityStatisDTO();
            // pv ，uv
            List<ActivityPvDTO> pvDatas = pvMap.get(parse);

            if (CollectionUtils.isNotEmpty(pvDatas)) {
                ArrayList<ActivityPvDTO> visitorsNumber = pvDatas.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(ActivityPvDTO::getIpDetail))), ArrayList::new));
                dto.setPageView(pvDatas.size());
                dto.setVisitorsNumber(visitorsNumber.size());
            }

            List<ActivityStatisEntity> statisEntities = mapList.get(parse);
            // 求和数据
            if (CollectionUtils.isNotEmpty(statisEntities)) {
                dataSum(statisEntities, dto);
            }
            entryUser.setValue(dto);
        }
        activityStatisFindDTO.setMap(map);
    }

    /**
     * 数据求和
     *
     * @param activityStatisEntities
     * @param dto
     */
    public void dataSum(List<ActivityStatisEntity> activityStatisEntities, ActivityStatisDTO dto) {
        Integer personNum = activityStatisEntities.stream().mapToInt(ActivityStatisEntity::getPersonNum).sum();
        Integer payPerson = activityStatisEntities.stream().mapToInt(ActivityStatisEntity::getPayPerson).sum();
        Integer downOrderPerson = activityStatisEntities.stream().mapToInt(ActivityStatisEntity::getDownOrderPerson).sum();
        Integer downOrderNum = activityStatisEntities.stream().mapToInt(ActivityStatisEntity::getDownOrderNum).sum();
        Integer userOrderNum = activityStatisEntities.stream().mapToInt(ActivityStatisEntity::getUserOrderNum).sum();
        BigDecimal orderAmount = activityStatisEntities.stream().map(ActivityStatisEntity::getOrderAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer payOrderNum = activityStatisEntities.stream().mapToInt(ActivityStatisEntity::getPayOrderNum).sum();
        Integer payUseNum = activityStatisEntities.stream().mapToInt(ActivityStatisEntity::getPayUseNum).sum();
        BigDecimal realityAmount = activityStatisEntities.stream().map(ActivityStatisEntity::getRealityAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal payAmount = activityStatisEntities.stream().map(ActivityStatisEntity::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal activityReduceAmount = activityStatisEntities.stream().map(ActivityStatisEntity::getActivityReduceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setPayPerson(payPerson);
        dto.setDownOrderPerson(downOrderPerson);
        dto.setPersonNum(personNum);
        dto.setDownOrderNum(downOrderNum);
        dto.setUserOrderNum(userOrderNum);
        dto.setOrderAmount(orderAmount);
        dto.setPayOrderNum(payOrderNum);
        dto.setPayUseNum(payUseNum);
        dto.setActivityReduceAmount(activityReduceAmount);
        dto.setPayAmount(payAmount);
        dto.setRealityAmount(realityAmount);
        dto.setActivityType(activityStatisEntities.get(0).getActivityType());
        // 转化率：支付使用的优惠券数量/领取的优惠券数量*100%
        if (dto.getPayUseNum() != 0 && dto.getPersonNum() != 0) {
            Double aDouble = NumberUtils.divisionTwoDecimal(dto.getPayUseNum(), dto.getPersonNum());
            dto.setConversion(aDouble);
        } else {
            dto.setConversion(0.00);
        }
    }

    /**
     * 营销商品明细分页
     *
     * @param map
     * @return
     */

    @Override
    public ActivityStatisGoodsPage activityGoodsStatis(@RequestParam Map<String, Object> map) {
        ActivityStatisGoodsPage activityStatisGoodsPage = new ActivityStatisGoodsPage();

        MatchOperation defMatchOperation = where(map, null);
        // 查询营销明细,分页查询出当前时间内的分页商品信息
        List<ActivityDetailStatisDTO> dtoList = new ArrayList<>();

        String page = (String) map.get("page");
        String limit = (String) map.get("limit");
        if (StringUtils.isBlank(page)) {
            page = "1";
        }

        if (StringUtils.isBlank(limit)) {
            limit = "10";
        }
        Aggregation activityDetail = Aggregation.newAggregation(
                defMatchOperation, Aggregation.group("goodsId"),
                Aggregation.project().and("goodsId").previousOperation(),
                Aggregation.skip(Long.valueOf(page) > 1 ? (Long.valueOf(page) - 1) * Long.valueOf(limit) : 0),
                Aggregation.limit(Integer.valueOf(limit))
        );
        List<GoodsIdListDTO> goodsIdList = mongoTemplate.aggregate(activityDetail, CollectionName.ACTIVITY_DETAIL_STATISTICS, GoodsIdListDTO.class).getMappedResults();
        for (GoodsIdListDTO goodsIdListDTO : goodsIdList) {
            ActivityDetailStatisDTO activityDetailStatisDTO = new ActivityDetailStatisDTO();

            MatchOperation goodsInfoMatch = where(map, goodsIdListDTO.getGoodsId());
            Aggregation aggregation = Aggregation.newAggregation(goodsInfoMatch);

            List<ActivityDetailStatisEntity> detailStatisEntities = mongoTemplate.aggregate(aggregation, CollectionName.ACTIVITY_DETAIL_STATISTICS, ActivityDetailStatisEntity.class).getMappedResults();
            // 下单人数
            ArrayList<ActivityDetailStatisEntity> collect = detailStatisEntities.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                    () -> new TreeSet<>(Comparator.comparing(ActivityDetailStatisEntity::getMemberId))), ArrayList::new));
            activityDetailStatisDTO.setGoodsId(goodsIdListDTO.getGoodsId());
            activityDetailStatisDTO.setSpecId(detailStatisEntities.get(0).getSpecId());
            activityDetailStatisDTO.setDownOrderPerson(collect.size());
            // 查询支付人数
            List<ActivityDetailStatisEntity> payList = detailStatisEntities.stream().filter(a -> (a.getPaymentStatus() == 1)).collect(Collectors.toList());
            ArrayList<ActivityDetailStatisEntity> payMember = payList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                    () -> new TreeSet<>(Comparator.comparing(ActivityDetailStatisEntity::getMemberId))), ArrayList::new));
            activityDetailStatisDTO.setPayPerson(payMember.size());
            // 支付件数
            int paySum = payList.stream().mapToInt(ActivityDetailStatisEntity::getGoodsNum).sum();
            activityDetailStatisDTO.setPayOrderGoodsNum(paySum);
            //下单件数
            int sum = detailStatisEntities.stream().mapToInt(ActivityDetailStatisEntity::getGoodsNum).sum();
            activityDetailStatisDTO.setOrderGoodsNum(sum);
            //活动减免金额
            BigDecimal activityReduceAmount = detailStatisEntities.stream().map(ActivityDetailStatisEntity::getActivityReduceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            activityDetailStatisDTO.setActivityReduceAmount(activityReduceAmount);
            // 下单金额
            BigDecimal downOrder = detailStatisEntities.stream().map(ActivityDetailStatisEntity::getRealityAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            activityDetailStatisDTO.setRealityAmount(downOrder);
            // 实付金额
            BigDecimal payAmount = detailStatisEntities.stream().map(ActivityDetailStatisEntity::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            activityDetailStatisDTO.setPayAmount(payAmount);
            activityDetailStatisDTO.setGoodsName(detailStatisEntities.get(0).getGoodsName());
            activityDetailStatisDTO.setGoodsImage(detailStatisEntities.get(0).getGoodsImage());
            activityDetailStatisDTO.setActivityType(detailStatisEntities.get(0).getActivityType());
            dtoList.add(activityDetailStatisDTO);
        }
        activityStatisGoodsPage.setDetailStatisDTOList(dtoList);
        activityStatisGoodsPage.setLimit(Integer.valueOf(limit));
        activityStatisGoodsPage.setPage(Integer.valueOf(page));
        // 查询时间内的商品总数量
        Aggregation count = Aggregation.newAggregation(
                defMatchOperation, Aggregation.group("goodsId"),
                Aggregation.project("goodsId").and("goodsId").previousOperation());

        List<Object> total = mongoTemplate.aggregate(count, CollectionName.ACTIVITY_DETAIL_STATISTICS, Object.class).getMappedResults();
        activityStatisGoodsPage.setTotal(total.size());
        return activityStatisGoodsPage;
    }


    /**
     * 封装查询条件
     *
     * @return
     */
    public MatchOperation where(Map<String, Object> map, Long goodsId) {
        //日期搜索条件
        Criteria dateWhereCriteria = GetCondition.getDateWhereCriteria(map);
        //店铺ID搜索条件
        Criteria storeIdCriteria = GetCondition.getStoreIdCriteria(map);
        // 活动类型
        Criteria storeTypeCriteria = GetCondition.getActivityTypeCriteria(map);
        //活动ID
        Criteria activityIdCriteria = GetCondition.getActivityIdCriteria(map);

        MatchOperation defMatchOperation = null;
        if (activityIdCriteria != null) {
            if (goodsId != null) {
                defMatchOperation = Aggregation.match(dateWhereCriteria
                        .andOperator(storeIdCriteria, storeTypeCriteria, activityIdCriteria, Criteria.where("goodsId").is(goodsId)));
            } else {
                defMatchOperation = Aggregation.match(dateWhereCriteria
                        .andOperator(storeIdCriteria, storeTypeCriteria, activityIdCriteria));
            }

        } else {
            if (goodsId != null) {
                defMatchOperation = Aggregation.match(dateWhereCriteria
                        .andOperator(storeIdCriteria, storeTypeCriteria, Criteria.where("goodsId").is(goodsId)));
            } else {
                defMatchOperation = Aggregation.match(dateWhereCriteria
                        .andOperator(storeIdCriteria, storeTypeCriteria));
            }
        }
        return defMatchOperation;
    }

}
