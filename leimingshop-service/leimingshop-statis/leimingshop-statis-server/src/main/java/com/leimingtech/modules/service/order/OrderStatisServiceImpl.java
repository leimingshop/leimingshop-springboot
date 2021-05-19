/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.leimingtech.common.GetCondition;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.AmountConstants;
import com.leimingtech.modules.constants.CollectionName;
import com.leimingtech.modules.dao.goods.GoodsStatisDao;
import com.leimingtech.modules.dao.order.OrderStatisDao;
import com.leimingtech.modules.dto.goods.GoodsStatisDTO;
import com.leimingtech.modules.dto.order.OrderSourceStatisticDTO;
import com.leimingtech.modules.dto.order.OrderStatisDTO;
import com.leimingtech.modules.dto.order.SevenTransactionStatisShowDTO;
import com.leimingtech.modules.dto.order.TransactionStatisShowDTO;
import com.leimingtech.modules.entity.order.OrderSourceEntity;
import com.leimingtech.modules.entity.order.OrderStatisEntity;
import com.leimingtech.modules.entity.order.OrderStatisticResult;
import com.leimingtech.modules.entity.order.TransactionStatisEntity;
import com.leimingtech.modules.entity.traffic.TrafficStatisticEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.after.AfterSaleStatisticService;
import com.leimingtech.modules.service.store.StoreStatisService;
import com.leimingtech.modules.statuscode.StatisticStatusCode;
import com.leimingtech.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 交易统计接口实现类
 *
 * @author xuzhch
 * @version 1.0
 * @date 2019年12月6日
 */
@Slf4j
@Service

public class OrderStatisServiceImpl implements OrderStatisService {
    public static final String DATE_STR = "date";
    /**
     * 1:本月
     */
    public static final String CURRENT_MONTH = "1";
    /**
     * 2:上月
     */
    public static final String LAST_MONTH = "2";
    /**
     * 3:自定义月份【yyyy-MM-dd】
     */
    public static final String CUSTOM_MONTH = "3";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(OrderStatisServiceImpl.class);
    @Autowired
    private StoreStatisService storeStatisService;

    @Resource
    private OrderStatisDao orderStatisDao;

    @Autowired
    private NosqlService nosqlService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AfterSaleStatisticService afterSaleStatisticService;

    @Autowired
    private GoodsStatisDao goodsStatisDao;

    /**
     * 订单来源统计
     *
     * @param params queryType  date
     * @return
     * @date 2020/3/12/012 18:58
     * @author xuzhch
     **/

    @Override
    public Map<Integer, OrderSourceStatisticDTO> selectOrderSource(@RequestParam Map params) {
        String queryType = params.get("queryType").toString();
        Date date = null;
        //店铺ID搜索条件
        Criteria storeIdCriteria = GetCondition.getStoreIdCriteria(params);
        //条件匹配对象
        if (CURRENT_MONTH.equals(queryType)) {
            Date date1 = new Date();
            date = DateUtils.longToDate(date1.getTime(), DateUtils.DATE_MONTH_PATTERN);
        }
        if (LAST_MONTH.equals(queryType)) {
            Date date1 = DateUtils.addDateMonths(new Date(), -1);
            date = DateUtils.longToDate(date1.getTime(), DateUtils.DATE_MONTH_PATTERN);
        }
        if (CUSTOM_MONTH.equals(queryType) && null != params.get(DATE_STR)) {
            String date1 = (String) params.get("date");
            date = DateUtils.parse(date1, DateUtils.DATE_MONTH_PATTERN);
        }
        GroupOperation as = Aggregation.group("orderSource", "memberId").sum("orderAmount").as("orderAmount");
        ProjectionOperation po = Aggregation.project("orderAmount").and("orderSource").as("orderSource").andInclude("orderSource").and("memberId").as("memberId").andInclude("memberId");
        Map<Integer, OrderSourceStatisticDTO> resultMap = new HashMap<>(6);
        resultMap.put(0, OrderSourceStatisticDTO.newOrderSourceStatisticDTO());
        resultMap.put(1, OrderSourceStatisticDTO.newOrderSourceStatisticDTO());
        resultMap.put(2, OrderSourceStatisticDTO.newOrderSourceStatisticDTO());
        resultMap.put(3, OrderSourceStatisticDTO.newOrderSourceStatisticDTO());
        resultMap.put(4, OrderSourceStatisticDTO.newOrderSourceStatisticDTO());
        resultMap.put(5, OrderSourceStatisticDTO.newOrderSourceStatisticDTO());

        //当前月
        MatchOperation currentMatch = Aggregation.match(Criteria.where("paymentStatus").is(1).andOperator(Criteria.where("createMonthTime").is(date), storeIdCriteria == null ? new Criteria() : storeIdCriteria));
        Aggregation currentAg = Aggregation.newAggregation(currentMatch, as, po);
        List<OrderSourceEntity> currentResult = mongoTemplate.aggregate(currentAg, CollectionName.ORDER_STATISTICS, OrderSourceEntity.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(currentResult)) {
            Map<Integer, List<OrderSourceEntity>> collect = currentResult.stream().collect(Collectors.groupingBy(OrderSourceEntity::getOrderSource));
            collect.keySet().stream().forEach(key -> {
                OrderSourceStatisticDTO orderSourceStatisticDTO = resultMap.get(key);
                List<OrderSourceEntity> orderSourceEntities = collect.get(key);
                BigDecimal bigDecimal = orderSourceEntities.stream().map(OrderSourceEntity::getOrderAmount).reduce(BigDecimal::add).get();
                List<Long> collect1 = orderSourceEntities.stream().map(OrderSourceEntity::getMemberId).distinct().collect(Collectors.toList());
                orderSourceStatisticDTO.setCurrentOrderAmount(bigDecimal);
                orderSourceStatisticDTO.setCurrentBuyerCount(collect1.size());
            });
        }
        //上月
        Date lastDate = DateUtils.addDateMonths(date, -1);
        MatchOperation lastMatch = Aggregation.match(Criteria.where("paymentStatus").is(1).andOperator(Criteria.where("createMonthTime").is(lastDate), storeIdCriteria == null ? new Criteria() : storeIdCriteria));
        Aggregation lastAg = Aggregation.newAggregation(lastMatch, as, po);
        List<OrderSourceEntity> lastResult = mongoTemplate.aggregate(lastAg, CollectionName.ORDER_STATISTICS, OrderSourceEntity.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(lastResult)) {
            Map<Integer, List<OrderSourceEntity>> collect = lastResult.stream().collect(Collectors.groupingBy(OrderSourceEntity::getOrderSource));
            collect.keySet().stream().forEach(key -> {
                OrderSourceStatisticDTO orderSourceStatisticDTO = resultMap.get(key);
                List<OrderSourceEntity> orderSourceEntities = collect.get(key);
                BigDecimal bigDecimal = orderSourceEntities.stream().map(OrderSourceEntity::getOrderAmount).reduce(BigDecimal::add).get();
                List<Long> collect1 = orderSourceEntities.stream().map(OrderSourceEntity::getMemberId).distinct().collect(Collectors.toList());
                orderSourceStatisticDTO.setLastMonthOrderAmount(bigDecimal);
                orderSourceStatisticDTO.setLastMonthBuyerCount(collect1.size());
            });
        }
        resultMap.keySet().stream().forEach(k -> {
            OrderSourceStatisticDTO statisticDTO = resultMap.get(k);
            statisticDTO.setOrderSource(k);
            if (null != statisticDTO.getLastMonthBuyerCount() && 0 != statisticDTO.getLastMonthBuyerCount()) {
                // (本月-上月)/上月=涨势
                Double v = NumberUtils.divisionTwoDecimal((statisticDTO.getCurrentBuyerCount() - statisticDTO.getLastMonthBuyerCount()), (statisticDTO.getLastMonthBuyerCount()));
                statisticDTO.setBuyerUptrend(v);
            }
            if (null != statisticDTO.getLastMonthOrderAmount() && !BigDecimal.ZERO.equals(statisticDTO.getLastMonthOrderAmount())) {
                // (本月-上月)/上月=涨势
                BigDecimal divide = (statisticDTO.getCurrentOrderAmount().subtract(statisticDTO.getLastMonthOrderAmount())).divide(statisticDTO.getLastMonthOrderAmount(), 2, BigDecimal.ROUND_HALF_UP);
                statisticDTO.setOrderAmountUptrend(divide.doubleValue());
            }
        });

        return resultMap;
    }

    /**
     * 交易订单信息统计（交易统计，订单统计保存）
     *
     * @param params 日期 参数格式为  url?params=yyyy-MM-dd
     * @return void
     * @date 2019/12/27/027 18:30
     * @author xuzhch
     **/
    @Override
    //@DataSource("order")

    public void saveTransactionStatis(@RequestParam(required = false) String params) throws ParseException {

        //获取日期
        String date = null;
        DateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat month = new SimpleDateFormat("yyyy-MM");
        if (StringUtils.isNotBlank((params))) {
            Date parse = day.parse(params);
            date = day.format(parse);
        } else {
            date = day.format(DateUtil.yesterday());
        }
        //获取所有店铺id
        List<Long> storeIds = selectStoreIds();
        // 保存交易统计
//        saveTransactionStatis(date, day, month, storeIds);
        afterSaleStatisticService.saveAfterReturnAmountByStore(storeIds, date);
        //保存订单统计
        saveOrderStatis(date, day, month, storeIds);
    }


    /**
     * 保存订单统计
     *
     * @param date     字符串日期
     * @param day      日期格式yyyy-MM-dd
     * @param month    日期格式yyyy-MM
     * @param storeIds 所有店铺ID集合
     * @return
     * @date 2019/12/30/030 10:27
     * @author xuzhch
     **/
    //@DataSource("order")
    private void saveOrderStatis(String date, DateFormat day, DateFormat month, List<Long> storeIds) throws ParseException {
        Date date1 = day.parse(date);
        Query query = Query.query(Criteria.where("createDayTime").is(date1));
        List<OrderStatisDTO> selectRes = mongoTemplate.find(query, OrderStatisDTO.class);
        //初始化日志保存数据
        Map<String, String> logMap = new HashMap<>(1);
        logMap.put("保存日期", date);
        if (selectRes != null && selectRes.size() > 0) {
            mlogger.info(StatisticStatusCode.ORDER_STATISTIC_DATA_REPEAT_SAVE_CODE, StatisticStatusCode.ORDER_STATISTIC_DATA_REPEAT_SAVE_MASSAGE, logMap);
            return;
        }
        List<OrderStatisDTO> orderStatisDTOList = orderStatisDao.selectOrderByDate(date);
        for (OrderStatisDTO orderStatisDTO : orderStatisDTOList) {
            String dateStr = DateUtils.format(orderStatisDTO.getCreateDayTime(), "yyyy-MM");
            Date monthDate = month.parse(dateStr);
            orderStatisDTO.setCreateMonthTime(monthDate);
        }
        nosqlService.saveBatch(orderStatisDTOList, CollectionName.ORDER_STATISTICS);
        mlogger.info(StatisticStatusCode.ORDER_STATISTIC_DATA_SAVE_CODE, StatisticStatusCode.ORDER_STATISTIC_DATA_SAVE_MASSAGE, logMap);

    }

    /**
     * 保存交易统计
     *
     * @param date     字符串日期
     * @param day      日期格式yyyy-MM-dd
     * @param month    日期格式yyyy-MM
     * @param storeIds 所有店铺ID集合
     * @return
     * @date 2019/12/30/030 10:28
     * @author xuzhch
     **/
//    private void saveTransactionStatis(String date, DateFormat day, DateFormat month, List<Long> storeIds) throws ParseException {
//        //转换日期
//        Date date1 = day.parse(date);
//        //设置查询条件。判断数据库是否有记录，有的话返回
//        Query query = Query.query(Criteria.where("createDayTime").is(date1));
//        List<TransactionStatisEntity> selectRes = mongoTemplate.find(query, TransactionStatisEntity.class);
//        //初始化日志保存数据
//        Map<String, String> logMap = new HashMap<>();
//        logMap.put("保存日期", date);
//        if (selectRes != null && selectRes.size() > 0) {
//            mlogger.info(StatisticStatusCode.TRANSACTION_STATISTIC_DATA_REPEAT_SAVE_CODE, StatisticStatusCode.TRANSACTION_STATISTIC_DATA_REPEAT_SAVE_MASSAGE, logMap);
//            return;
//        }
//        //查询MySql获取统计数据保存
//        List<TransactionStatisEntity> transactionStatisEntities = orderStatisDao.transactionData(storeIds, date);
//        nosqlService.saveBatch(transactionStatisEntities, CollectionName.TRANSACTION_STATISTICS);
//        mlogger.info(StatisticStatusCode.TRANSACTION_STATISTIC_DATA_SAVE_CODE, StatisticStatusCode.TRANSACTION_STATISTIC_DATA_SAVE_MASSAGE, logMap);
//
//    }

    /**
     * 查询所有店铺Id
     *
     * @return 返回店铺Id集合
     * @date 2019/12/30/030 10:29
     * @author xuzhch
     **/
    private List<Long> selectStoreIds() {
        return storeStatisService.selectList();
    }


    /**
     * 交易统计数据展示
     *
     * @param params Map {
     *               (name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间)"
     *               (name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date" ),
     *               (name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date" )
     *               }
     * @return
     * @Author xuzhch 2019年12月17日17:09
     */

    @Override
    public TransactionStatisShowDTO transactionStatisByParams(@RequestParam Map params) {
        //初始化返回数据
        TransactionStatisShowDTO statisShowDTO = TransactionStatisShowDTO.newTransactionStatisShowDTO();
        //日期搜索条件
        Criteria dateWhereCriteria = GetCondition.getDateWhereCriteria(params);
        Criteria dateWherePayCriteria = GetCondition.getDateWhereCriteria(params);
        //店铺ID搜索条件
        Criteria storeIdCriteria = GetCondition.getStoreIdCriteria(params);
        //条件匹配对象
        MatchOperation defMatchOperation = null;
        MatchOperation payMatchOperation = null;
        if (storeIdCriteria != null) {
            defMatchOperation = Aggregation.match(dateWhereCriteria.andOperator(storeIdCriteria));
            payMatchOperation = Aggregation.match(dateWherePayCriteria.andOperator(storeIdCriteria, Criteria.where("paymentStatus").is(1)));
        } else {
            defMatchOperation = Aggregation.match(dateWhereCriteria);
            payMatchOperation = Aggregation.match(dateWherePayCriteria.andOperator(Criteria.where("paymentStatus").is(1)));
        }
        //获取退款金额
        Aggregation refundAmountCondition = Aggregation.newAggregation(
                defMatchOperation,
                Aggregation.group().sum("refundAmount").as("refundAmount"),
                Aggregation.project("refundAmount")
        );
        List<TransactionStatisEntity> refundAmountRes = mongoTemplate.aggregate(refundAmountCondition, TransactionStatisEntity.class, TransactionStatisEntity.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(refundAmountRes)) {
            BigDecimal refundAmount = refundAmountRes.stream().map(TransactionStatisEntity::getRefundAmount).reduce(BigDecimal::add).get();
            statisShowDTO.setRefundAmount(refundAmount);
        }
        //下单人数
        Aggregation submitOrderNumberCondition = Aggregation.newAggregation(
                defMatchOperation,
                Aggregation.group("memberId").count().as("orderNumber"),
                Aggregation.project("orderNumber"));
        List<Object> submitOrderNumberList = mongoTemplate.aggregate(submitOrderNumberCondition, CollectionName.ORDER_STATISTICS, Object.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(submitOrderNumberList)) {
            statisShowDTO.setSubmitOrderNumber(submitOrderNumberList.size());
        }
        //获取订单数、下单件数、下单金额、
        Aggregation subOrderCondition = Aggregation.newAggregation(
                defMatchOperation,
                Aggregation.group("orderId").count().as("orderNumber")
                        .sum("goodsNum").as("orderGoodsNumber")
                        .sum("orderAmount").as("orderAmount"),
                Aggregation.project("orderNumber", "orderGoodsNumber", "orderAmount"));
        List<OrderStatisticResult> submitRes = mongoTemplate.aggregate(subOrderCondition, CollectionName.ORDER_STATISTICS, OrderStatisticResult.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(submitRes)) {
            Long orderNum = submitRes.stream().mapToInt(OrderStatisticResult::getOrderNumber).count();
            Integer orderGoodsNum = submitRes.stream().mapToInt(OrderStatisticResult::getOrderGoodsNumber).sum();
            BigDecimal orderAmount = submitRes.stream().map(OrderStatisticResult::getOrderAmount).reduce(BigDecimal::add).get();
            statisShowDTO.setOrderGoodsNumber(orderGoodsNum);
            statisShowDTO.setOrderNumber(orderNum.intValue());
            statisShowDTO.setSubmitOrderAmount(orderAmount);
        }
        //付款人数
        Aggregation payOrderNumberCondition = Aggregation.newAggregation(
                payMatchOperation,
                Aggregation.group("memberId").count().as("orderNumber"),
                Aggregation.project("orderNumber"));
        List<Object> payOrderNumberList = mongoTemplate.aggregate(payOrderNumberCondition, CollectionName.ORDER_STATISTICS, Object.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(payOrderNumberList)) {
            statisShowDTO.setPaymentNumber(payOrderNumberList.size());
        }
        //付款金额、付款件数、付款订单数
        Aggregation payOrderCondition = Aggregation.newAggregation(
                payMatchOperation,
                Aggregation.group("orderId").count().as("orderNumber")
                        .sum("goodsNum").as("orderGoodsNumber")
                        .sum("orderAmount").as("orderAmount"),
                Aggregation.project("orderNumber", "orderGoodsNumber", "orderAmount"));
        List<OrderStatisticResult> payRes = mongoTemplate.aggregate(payOrderCondition, CollectionName.ORDER_STATISTICS, OrderStatisticResult.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(payRes)) {
            Long orderNum = payRes.stream().mapToInt(OrderStatisticResult::getOrderNumber).count();
            Integer orderGoodsNum = payRes.stream().mapToInt(OrderStatisticResult::getOrderGoodsNumber).sum();
            BigDecimal orderAmount = payRes.stream().map(OrderStatisticResult::getOrderAmount).reduce(BigDecimal::add).get();
            statisShowDTO.setPaymentOrderNumber(orderNum.intValue());
            statisShowDTO.setPaymentGoodsNumber(orderGoodsNum);
            statisShowDTO.setPaymentAmount(orderAmount);
        }
        //获取浏览量
        Integer pvCount = getPvCount(defMatchOperation);
        //获取访问量
        int uvCount = getUvCount(defMatchOperation);
        //设置Pv
        statisShowDTO.setPageView(pvCount);
        //设置Uv
        statisShowDTO.setVisitorsNumber(uvCount);
        statisShowDTO.setPageView(pvCount);
        statisShowDTO.setVisitorsNumber(uvCount);
        //下单转化率 下单人数/浏览人数*100%
        //成交转化率 付款人数/浏览人数*100%
        if (uvCount != 0) {
            statisShowDTO.setSubmitConversion(NumberUtils.divisionTwoDecimal(statisShowDTO.getSubmitOrderNumber(), uvCount));
            statisShowDTO.setClosingConversion(NumberUtils.divisionTwoDecimal(statisShowDTO.getPaymentNumber(), uvCount));
        }
        //客单价：付款金额/付款人数=客单价
        if (null != statisShowDTO.getPaymentNumber() && 0 != statisShowDTO.getPaymentNumber()) {
            BigDecimal divide = statisShowDTO.getPaymentAmount().divide(new BigDecimal(statisShowDTO.getPaymentNumber()), 2, BigDecimal.ROUND_HALF_UP);
            statisShowDTO.setPerTicketSales(divide);
        } else {
            statisShowDTO.setPerTicketSales(BigDecimal.ZERO);
        }
        //付款转化率 付款人数/下单人数*100%
        if (null != statisShowDTO.getSubmitOrderNumber() && 0 != statisShowDTO.getSubmitOrderNumber()) {
            statisShowDTO.setPaymentConversion(NumberUtils.divisionTwoDecimal(statisShowDTO.getPaymentNumber(), statisShowDTO.getSubmitOrderNumber()));
        } else {
            statisShowDTO.setPaymentConversion((double) 0);
        }
        log.info(JSON.toJSONString(statisShowDTO));
        return statisShowDTO;
    }


    /**
     * 获取访问量
     *
     * @param matchOperation
     * @return
     */
    private int getUvCount(MatchOperation matchOperation) {
        //访问量
        Aggregation uvCountRes = Aggregation.newAggregation(matchOperation, Aggregation.group("ipDetail"), Aggregation.project("ipDetail"));
        AggregationResults<Object> uvCountData = mongoTemplate.aggregate(uvCountRes, CollectionName.PV_DETAIL, Object.class);
        //   判断空指针和 越界
        if (null != uvCountData.getMappedResults() && uvCountData.getMappedResults().size() > 0) {
            List<Object> mappedResults = uvCountData.getMappedResults();
            return mappedResults.size();
        }
        return 0;
    }

    /**
     * 获取浏览量
     *
     * @param matchOperation
     * @return
     */
    private Integer getPvCount(MatchOperation matchOperation) {
        //浏览量
        Aggregation pvCountRes = Aggregation.newAggregation(matchOperation, Aggregation.count().as("pvCountRes"), Aggregation.project("pvCountRes"));
        AggregationResults<Object> pvCountData = mongoTemplate.aggregate(pvCountRes, CollectionName.PV_DETAIL, Object.class);
        //  判断空指针 和 越界
        if (null != pvCountData.getMappedResults() && pvCountData.getMappedResults().size() > 0) {
            Map pvCountMap = (Map) pvCountData.getMappedResults().get(0);
            return (Integer) pvCountMap.get("pvCountRes");
        }
        return 0;
    }

    /**
     * 近七日交易数据统计
     *
     * @param storeId 店铺ID  非必传
     * @return 近七天的销售统计
     * @author xuzhch 2019年12月17日17:11
     */

    @Override
    public Map<String, SevenTransactionStatisShowDTO> sevenDayStatis(@RequestParam(required = false, name = "storeId") Long storeId) {
        //获取当前日期
        Date date = new Date();
        //获取昨天的日期
        Date end = DateUtil.offsetDay(date, -1);
        //获取一周前的日
        Date start = DateUtil.offsetWeek(date, -1);
        //初始化查询数据
        Map<String, SevenTransactionStatisShowDTO> result = getSevenDay(start, end);
        //设置查询匹配条件
        MatchOperation matchOperation = null;
        MatchOperation paymentMatchOperation = null;
        //如果店铺ID为空 就查询近七天所有数据
        //否则根据店铺ID 查询近7天数据
        if (storeId != null) {
            matchOperation = Aggregation.match(Criteria.where("createDayTime").gte(start).lte(end).andOperator(Criteria.where("storeId").is(storeId)));
            paymentMatchOperation = Aggregation.match(Criteria.where("createDayTime").gte(start).lte(end)
                    .andOperator(Criteria.where("storeId").is(storeId), Criteria.where("paymentStatus").is(1)));
        } else {
            matchOperation = Aggregation.match(Criteria.where("createDayTime").gte(start).lte(end));
            paymentMatchOperation = Aggregation.match(Criteria.where("createDayTime").gte(start).lte(end)
                    .andOperator(Criteria.where("paymentStatus").is(1)));
        }

        //下单人数、付款人数、付款金额、退款金额
        //获取退款金额
        Aggregation refundAmountCondition = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group("createDayTime").sum("refundAmount").as("refundAmount"),
                Aggregation.project("refundAmount").and("createDayTime").previousOperation()
        );
        List<TransactionStatisEntity> refundAmountRes = mongoTemplate.aggregate(refundAmountCondition, TransactionStatisEntity.class, TransactionStatisEntity.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(refundAmountRes)) {
            for (TransactionStatisEntity refundAmountRe : refundAmountRes) {
                if (refundAmountRe.getRefundAmount() != null) {
                    SevenTransactionStatisShowDTO sevenTransactionStatisShowDTO = result.get(DateUtils.format(refundAmountRe.getCreateDayTime(), DateUtils.DATE_PATTERN));
                    sevenTransactionStatisShowDTO.setRefundAmount(refundAmountRe.getRefundAmount());
                }
            }
        }

        //付款人数、付款金额
        Aggregation payOrderCondi = Aggregation.newAggregation(
                paymentMatchOperation,
                Aggregation.group("memberId", "createDayTime").sum("orderAmount").as("paymentAmount"),
                Aggregation.project("paymentAmount")
                        .and("memberId").as("memberId").andInclude("memberId")
                        .and("createDayTime").as("createDayTime").andInclude("createDayTime")
        );
        List<TransactionStatisEntity> payOrderRes = mongoTemplate.aggregate(payOrderCondi, CollectionName.ORDER_STATISTICS, TransactionStatisEntity.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(payOrderRes)) {
            Map<Date, Long> payCountMap = payOrderRes.stream().collect(Collectors.groupingBy(TransactionStatisEntity::getCreateDayTime, Collectors.counting()));
            //设置付款人数
            for (Date date1 : payCountMap.keySet()) {
                SevenTransactionStatisShowDTO sevenTransactionStatisShowDTO = result.get(DateUtils.format(date1, DateUtils.DATE_PATTERN));
                sevenTransactionStatisShowDTO.setPayOrderNumber(payCountMap.get(date1).intValue());
            }
            //设置付款金额
            Map<Date, List<TransactionStatisEntity>> dateListMap = payOrderRes.stream().collect(Collectors.groupingBy(TransactionStatisEntity::getCreateDayTime));
            for (Date date1 : dateListMap.keySet()) {
                List<TransactionStatisEntity> payOrderAmountList = dateListMap.get(date1);
                BigDecimal bigDecimal = payOrderAmountList.stream().map(TransactionStatisEntity::getPaymentAmount).reduce(BigDecimal::add).get();
                SevenTransactionStatisShowDTO sevenTransactionStatisShowDTO = result.get(DateUtils.format(date1, DateUtils.DATE_PATTERN));
                sevenTransactionStatisShowDTO.setPaymentAmount(bigDecimal);
            }
        }
        //下单人数
        Aggregation subOrderCondi = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group("memberId", "createDayTime"),
                Aggregation.project()
                        .and("memberId").as("memberId").andInclude("memberId")
                        .and("createDayTime").as("createDayTime").andInclude("createDayTime")
        );
        List<TransactionStatisEntity> subOrderRes = mongoTemplate.aggregate(subOrderCondi, CollectionName.ORDER_STATISTICS, TransactionStatisEntity.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(subOrderRes)) {
            Map<Date, Long> subOrderNumMap = subOrderRes.stream().collect(Collectors.groupingBy(TransactionStatisEntity::getCreateDayTime, Collectors.counting()));
            //设置下单人数
            for (Date date1 : subOrderNumMap.keySet()) {
                SevenTransactionStatisShowDTO sevenTransactionStatisShowDTO = result.get(DateUtils.format(date1, DateUtils.DATE_PATTERN));
                sevenTransactionStatisShowDTO.setSubOrderNumber(subOrderNumMap.get(date1).intValue());
            }
        }
        //获取UV
        Aggregation uvCountCondi = Aggregation.newAggregation(matchOperation,
                Aggregation.group("createDayTime", "ipDetail"),
                Aggregation.project()
                        .and("ipDetail").as("ipDetail").andInclude("ipDetail")
                        .and("createDayTime").as("createDayTime").andInclude("createDayTime"));
        List<TrafficStatisticEntity> uvConutRes = mongoTemplate.aggregate(uvCountCondi, CollectionName.PV_DETAIL, TrafficStatisticEntity.class).getMappedResults();
        Set<TrafficStatisticEntity> entities = uvConutRes.stream().collect(Collectors.toSet());
        Map<Date, Long> map = entities.stream().collect(Collectors.groupingBy(trafficStatisticEntity -> trafficStatisticEntity.getCreateDayTime(), Collectors.counting()));
        map.forEach((k, v) -> {
            SevenTransactionStatisShowDTO sevenTransactionStatisShowDTO = result.get(DateUtils.format(k, DateUtils.DATE_PATTERN));
            sevenTransactionStatisShowDTO.setUvCount(v.intValue());
        });

        Double initConversion = 0.00;
        //封装返回数据
        for (SevenTransactionStatisShowDTO sevenStatisShowDTO : result.values()) {
            /**
             * 下单转化率 下单人数/浏览人数*100%
             *
             * 付款转化率 付款人数/下单人数*100%
             *
             * 成交转化率 付款人数/浏览人数*100%
             */
            //付款人数
            Integer paymentNumber = sevenStatisShowDTO.getPayOrderNumber();
            //设置下单转换率和成交转换率
            if (0 != sevenStatisShowDTO.getUvCount()) {
                //浏览量
                sevenStatisShowDTO.setSubmitConversion(NumberUtils.divisionTwoDecimal(sevenStatisShowDTO.getSubOrderNumber(), sevenStatisShowDTO.getUvCount()));
                sevenStatisShowDTO.setClosingConversion(NumberUtils.divisionTwoDecimal(paymentNumber, sevenStatisShowDTO.getUvCount()));
            } else {
                sevenStatisShowDTO.setSubmitConversion(initConversion);
                sevenStatisShowDTO.setClosingConversion(initConversion);
            }
            //设置付款转换率
            if (null != sevenStatisShowDTO.getSubOrderNumber() && 0 != sevenStatisShowDTO.getSubOrderNumber()) {
                sevenStatisShowDTO.setPaymentConversion(NumberUtils.divisionTwoDecimal(paymentNumber, sevenStatisShowDTO.getSubOrderNumber()));
            } else {
                sevenStatisShowDTO.setPaymentConversion(initConversion);
            }
        }
        return result;
    }


    /**
     * Goods statis sales list.
     *
     * @param date the date
     * @return the list
     * @author xuzhch
     * @date 09.17.2020
     */

    //@DataSource("order")
    @Override
    public List<GoodsStatisDTO> goodsStatisSales(@RequestParam(name = "date") String date) {
        return Optional.ofNullable(goodsStatisDao.goodsStatisSales(date)).orElse(new ArrayList<>());
    }

    /**
     * 单人消费统计
     *
     * @param params Map {
     *               (name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间)
     *               (name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date" )
     *               (name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date" )
     *               }
     * @return
     * @date 2019/12/30/030 16:00
     * @author xuzhch
     **/

    @Override
    public Map<String, Integer> singleConsumerStatis(@RequestParam Map params) {
        //日期搜索条件
        Criteria dateWhereCriteria = GetCondition.getDateWhereCriteria(params);
        //店铺ID搜索条件
        Criteria storeIdCriteria = GetCondition.getStoreIdCriteria(params);
        //条件搜索匹配对象
        MatchOperation matchOperation = null;
        if (storeIdCriteria != null) {
            matchOperation = Aggregation.match(dateWhereCriteria.andOperator(storeIdCriteria, Criteria.where("paymentStatus").is(1)));
        } else {
            matchOperation = Aggregation.match(dateWhereCriteria.andOperator(Criteria.where("paymentStatus").is(1)));
        }
        //封装查询参数
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group("memberId")
                        .sum("orderAmount").as("orderAmount"),
                Aggregation.project("orderAmount").and("memberId").previousOperation());

        //执行查询
        AggregationResults<OrderStatisEntity> aggregate = mongoTemplate.aggregate(aggregation, OrderStatisEntity.class, OrderStatisEntity.class);

        //封装返回结果
        Map<String, Integer> result = new LinkedHashMap<>();
        Integer initCount = 0;
        result.put("0-50", initCount);
        result.put("50-100", initCount);
        result.put("100-200", initCount);
        result.put("200-500", initCount);
        result.put("500-1000", initCount);
        result.put("1000-2000", initCount);
        result.put("2000-5000", initCount);
        result.put("5000-10000", initCount);
        result.put("10000以上", initCount);
        for (OrderStatisEntity orderStatisEntity : aggregate) {
            BigDecimal orderAmount = orderStatisEntity.getOrderAmount();
            if ((orderAmount.compareTo(AmountConstants.FIFTY) < 1) && (orderAmount.compareTo(BigDecimal.ZERO) > -1)) {
                Integer integer = result.get("0-50");
                result.put("0-50", integer + 1);
            } else if ((orderAmount.compareTo(AmountConstants.ONE_HUNDRED) < 1) && orderAmount.compareTo(AmountConstants.FIFTY) > -1) {
                Integer integer = result.get("50-100");
                result.put("50-100", integer + 1);
            } else if ((orderAmount.compareTo(AmountConstants.TWO_HUNDRED) < 1) && orderAmount.compareTo(AmountConstants.ONE_HUNDRED) > -1) {
                Integer integer = result.get("100-200");
                result.put("100-200", integer + 1);
            } else if ((orderAmount.compareTo(AmountConstants.FIVE_HUNDRED) < 1 && orderAmount.compareTo(AmountConstants.TWO_HUNDRED) > -1)) {
                Integer integer = result.get("200-500");
                result.put("200-500", integer + 1);
            } else if ((orderAmount.compareTo(AmountConstants.ONE_THOUSAND) < 1) && orderAmount.compareTo(AmountConstants.FIVE_HUNDRED) > -1) {
                Integer integer = result.get("500-1000");
                result.put("500-1000", integer + 1);
            } else if ((orderAmount.compareTo(AmountConstants.TWO_THOUSAND) < 1) && orderAmount.compareTo(AmountConstants.ONE_THOUSAND) > -1) {
                Integer integer = result.get("1000-2000");
                result.put("1000-2000", integer + 1);
            } else if ((orderAmount.compareTo(AmountConstants.FIVE_THOUSAND) < 1) && orderAmount.compareTo(AmountConstants.TWO_THOUSAND) > -1) {
                Integer integer = result.get("2000-5000");
                result.put("2000-5000", integer + 1);
            } else if ((orderAmount.compareTo(AmountConstants.TEN_THOUSAND) < 1) && orderAmount.compareTo(AmountConstants.FIVE_THOUSAND) > -1) {
                Integer integer = result.get("5000-10000");
                result.put("5000-10000", integer + 1);
            } else if (orderAmount.compareTo(AmountConstants.TEN_THOUSAND) > 0) {
                Integer integer = result.get("10000以上");
                result.put("10000以上", integer + 1);
            }
        }
        return result;
    }

    /**
     * @param start:开始时间，end:结束时间
     * @return 初始化近七天交易统计
     * @date 2019/12/30/030 16:03
     * @author xuzhch
     **/
    private Map<String, SevenTransactionStatisShowDTO> getSevenDay(Date start, Date end) {
        Map<String, SevenTransactionStatisShowDTO> treeMap = new TreeMap<>();
        java.time.LocalDate startLocal = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        java.time.LocalDate endLocal = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //计算两个日期之间相差的日期并转成list集合
        List<String> collect = Stream.iterate(startLocal, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startLocal, endLocal) + 1)
                .map(java.time.LocalDate::toString)
                .collect(Collectors.toList());
        //遍历集合 设置初始化数据
        for (String s : collect) {
            SevenTransactionStatisShowDTO sevenTransactionStatisShowDTO = SevenTransactionStatisShowDTO.infoSevenTransactionStatisShowDTO();
            treeMap.put(s, sevenTransactionStatisShowDTO);
        }
        return treeMap;
    }


//    public static void main(String[] args) {
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        for (int i = 1; i < 150; i++) {
//            Date dateTime = DateUtil.offsetDay(date, -i);
//            String format = dateFormat.format(dateTime);
//            //例子： //http://url:port/statis/goods/goods?params=2020-02-28
//            //商品、交易统计以天做维度统计----> params=2019-12-20
//            //会员统计以小时做维度统计例子----> params=2019-12-20 14
//            //商品统计保存
//            //http://localhost:30216/statis/goods/goods?params=
//            //交易统计保存
//            //http://localhost:30216/statis/order/save/transaction?params=
//            //会员统计
//            //http://localhost:30216/statis/member/member?params=
////            String s = "curl http://localhost:30216/statis/goods/goods?params=" + format;
////            String s = "curl http://localhost:30216/statis/order/save/transaction?params=" + format;
//            String s = HttpUtil.get("http://localhost:30216/statis/order/save/transaction?params=" + format);
//            System.out.println(s);
//
//
//        }
//    }
}
