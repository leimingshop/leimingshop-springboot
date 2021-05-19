/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.common.GetCondition;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.CollectionName;
import com.leimingtech.modules.dao.goods.GoodsStatisDao;
import com.leimingtech.modules.dto.goods.GoodsClassNameDTO;
import com.leimingtech.modules.dto.goods.GoodsClassStatisDTO;
import com.leimingtech.modules.dto.goods.GoodsSaleStatisDTO;
import com.leimingtech.modules.dto.goods.GoodsStatisDTO;
import com.leimingtech.modules.entity.goods.GoodsPicEntity;
import com.leimingtech.modules.entity.goods.GoodsStatisEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.order.OrderStatisService;
import com.leimingtech.modules.statuscode.StatisticStatusCode;
import com.leimingtech.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName GoodsStatisService
 * @Description 商品统计接口实现类
 * @Author xuzhch
 * @Date 2019年12月6日
 * @Version 1.0
 **/

@Slf4j
@Service

public class GoodsStatisServiceImpl implements GoodsStatisService {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(GoodsStatisServiceImpl.class);

    @Autowired
    private GoodsStatisDao goodsStatisDao;

    @Autowired
    private OrderStatisService orderStatisService;

    @Autowired
    private NosqlService nosqlService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 定时同步商品统计信息
     *
     * @param params 日期 参数格式为  url?params=yyyy-MM-dd
     * @throws ParseException
     * @author xuzhch 2019年12月17日17:02
     */
    @Override
    //@DataSource("goods")

    public void saveGoodsStatis(@RequestParam(required = false) String params) throws ParseException {

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
        //判断MongoDB是否存在数据
        Date parse = DateUtils.parse(date, "yyyy-MM-dd");
        Query query = Query.query(Criteria.where("createDayTime").is(parse));
        List<GoodsStatisEntity> selectRes = mongoTemplate.find(query, GoodsStatisEntity.class);
        //初始化日志保存数据
        Map<String, String> logMap = new HashMap<>(1);
        logMap.put("保存日期", date);
        if (selectRes != null && selectRes.size() > 0) {
            mlogger.info(StatisticStatusCode.GOODS_STATISTIC_DATA_REPEAT_SAVE_CODE, StatisticStatusCode.GOODS_STATISTIC_DATA_REPEAT_SAVE_MASSAGE, logMap);
            return;
        }
        //查询Mysql 获取要保存的统计数据
        Map<Long, String> picMap = new HashMap<>(1);
        List<GoodsStatisDTO> goodsStatisSales = orderStatisService.goodsStatisSales(date);
        List<GoodsStatisEntity> goodsStatisEntities = ConvertUtils.sourceToTarget(goodsStatisSales, GoodsStatisEntity.class);
        List<Long> goodsIds = goodsStatisEntities.stream().map(GoodsStatisEntity::getGoodsId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            List<GoodsPicEntity> goodsMainPic = Optional.ofNullable(goodsStatisDao.selectGoodsMainPic(goodsIds)).orElse(new ArrayList<>());
            picMap = goodsMainPic.stream().collect(Collectors.toMap(GoodsPicEntity::getGoodsId, GoodsPicEntity::getGoodsMainPicture));
        }

        Integer initViewCount = 0;
        //保存商品的浏览量和访问量
        for (GoodsStatisEntity goodsStatisEntity : goodsStatisEntities) {
            //设置商品主图
            goodsStatisEntity.setGoodsMainPicture(picMap.get(goodsStatisEntity.getGoodsId()));
            //查询PV 并设置
            Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("createDayTime").is(parse).andOperator(Criteria.where("goodsId").is(goodsStatisEntity.getGoodsId()))),
                    Aggregation.group("id").count().as("pvCount"),
                    Aggregation.project("pvCount"));
            AggregationResults<Object> aggregate = mongoTemplate.aggregate(aggregation, CollectionName.PV_DETAIL, Object.class);
            List<Object> mappedResults = aggregate.getMappedResults();
            if (CollectionUtils.isNotEmpty(mappedResults)) {
                Map map = (Map) mappedResults.get(0);
                Integer pvCount = (Integer) map.get("pvCount");
                goodsStatisEntity.setPageView(pvCount);
            } else {
                goodsStatisEntity.setPageView(initViewCount);
            }

            //查询UV 并设置
            Aggregation uvCountRes = Aggregation.newAggregation(Aggregation.match(Criteria.where("createDayTime").is(parse).andOperator(Criteria.where("goodsId").is(goodsStatisEntity.getGoodsId()))),
                    Aggregation.group("ipDetail"), Aggregation.project("ipDetail"));
            AggregationResults<Object> uvCountData = mongoTemplate.aggregate(uvCountRes, CollectionName.PV_DETAIL, Object.class);
            List<Object> mappedResults1 = uvCountData.getMappedResults();
            if (null != mappedResults1 && mappedResults1.size() > 0) {
                goodsStatisEntity.setVisitorsNumber(mappedResults1.size());
            } else {
                goodsStatisEntity.setVisitorsNumber(initViewCount);
            }
            goodsStatisEntity.setId(IdWorker.getId());
            goodsStatisEntity.setCreateDayTime(day.parse(date));
            goodsStatisEntity.setCreateMonthTime(month.parse(date));
        }
        nosqlService.saveBatch(goodsStatisEntities, CollectionName.GOODS_STATISTICS);

        mlogger.info(StatisticStatusCode.GOODS_STATISTIC_DATA_SAVE_CODE, StatisticStatusCode.GOODS_STATISTIC_DATA_SAVE_MASSAGE, logMap);
    }


    /**
     * 商品分类信息统计展示
     *
     * @param params Map
     * @Author xuzhch 2019年12月17日17:03
     */
    @Override
    //@DataSource("goods")

    public List<GoodsClassStatisDTO> classGoodsShowStatis(@RequestParam Map params) {
        //拼装搜索条件
        Criteria dateWhereCriteria = GetCondition.getDateWhereCriteria(params);
        Criteria storeIdCriteria = GetCondition.getStoreIdCriteria(params);
        MatchOperation matchOperation = null;
        if (storeIdCriteria != null) {
            matchOperation = Aggregation.match(dateWhereCriteria.andOperator(storeIdCriteria));
        } else {
            matchOperation = Aggregation.match(dateWhereCriteria);
        }
        //聚合查询参数
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group("firstGcId")
                        .sum("salesNumber").as("salesNumber")
                        .sum("salesAmount").as("salesAmount"),
                Aggregation.project("firstGcId", "salesNumber", "salesAmount").and("firstGcId").previousOperation()
        );
        //执行查询 获取搜索结果
        AggregationResults<GoodsStatisEntity> aggregate = mongoTemplate.aggregate(aggregation, GoodsStatisEntity.class, GoodsStatisEntity.class);
        List<GoodsStatisEntity> mappedResults = aggregate.getMappedResults();
        //获取销量总数
        Integer sumSaleNumber = mappedResults.stream().mapToInt(GoodsStatisEntity::getSalesNumber).sum();
        //获取销售金额总数
        BigDecimal sumSaleAmount = mappedResults.stream().map(GoodsStatisEntity::getSalesAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //获取所有一级分类
        List<GoodsClassNameDTO> classNames = goodsStatisDao.selectClassName();
        //封装返回数据
        List<GoodsClassStatisDTO> goodsClassStatisData = new ArrayList<>();
        for (GoodsClassNameDTO classNameDTO : classNames) {
            GoodsClassStatisDTO goodsClassStatisDTO = GoodsClassStatisDTO.newGoodsClassStatisDTO();
            goodsClassStatisDTO.setClassName(classNameDTO.getClassName());
            goodsClassStatisDTO.setFirstGcId(classNameDTO.getClassId());
            for (GoodsStatisEntity goodsStatisEntity : aggregate) {
                if (null != goodsStatisEntity.getFirstGcId() && goodsStatisEntity.getFirstGcId().compareTo(classNameDTO.getClassId()) == 0) {
                    goodsClassStatisDTO.setSalesAmount(goodsStatisEntity.getSalesAmount());
                    goodsClassStatisDTO.setSalesNumber(goodsStatisEntity.getSalesNumber());
                    //销售数量比例
                    if (0 != sumSaleNumber) {
                        Double aDouble = NumberUtils.divisionTwoDecimal(goodsStatisEntity.getSalesNumber(), sumSaleNumber);
                        goodsClassStatisDTO.setSalesNumberProportion(aDouble);
                    }
                    //销售金额比例
                    if (null != sumSaleAmount && (sumSaleAmount.compareTo(BigDecimal.ZERO) == 1)) {
                        BigDecimal divide = goodsStatisEntity.getSalesAmount().divide(sumSaleAmount, 2, RoundingMode.HALF_UP);
                        goodsClassStatisDTO.setSalesAmountProportion(divide.doubleValue());
                    }
                }
            }
            goodsClassStatisData.add(goodsClassStatisDTO);
        }
        List<GoodsClassStatisDTO> classStatisResultData = goodsClassStatisData.stream().sorted(Comparator.comparing(GoodsClassStatisDTO::getSalesNumber).reversed()).collect(Collectors.toList());
        return classStatisResultData;
    }

    /**
     * 商品销量统计信息展示
     *
     * @param params Map
     * @return
     * @Author xuzhch 2019年12月17日17:04
     */

    @Override
    public List<GoodsSaleStatisDTO> goodsSaleShowSattis(@RequestParam Map params) {
        //获取搜索条件
        Criteria dateWhereCriteria = GetCondition.getDateWhereCriteria(params);
        Criteria storeIdCriteria = GetCondition.getStoreIdCriteria(params);
        MatchOperation matchOperation = null;
        if (storeIdCriteria != null) {
            matchOperation = Aggregation.match(dateWhereCriteria.andOperator(storeIdCriteria));
        } else {
            matchOperation = Aggregation.match(dateWhereCriteria);
        }
        //设置聚合参数
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group("goodsId", "goodsSerial", "goodsName", "buyerId", "goodsMainPicture", "specId")
                        .sum("pageView").as("pageView")
                        .sum("visitorsNumber").as("visitorsNumber")
                        .sum("salesNumber").as("salesNumber")
                        .sum("salesAmount").as("salesAmount"),
                Aggregation.sort(Sort.Direction.DESC, "salesNumber"),
                Aggregation.project("pageView", "visitorsNumber", "salesNumber", "salesAmount")
                        .and("goodsMainPicture").as("goodsMainPicture").andInclude("goodsMainPicture")
                        .and("goodsSerial").as("goodsSerial").andInclude("goodsSerial")
                        .and("goodsName").as("goodsName").andInclude("goodsSerial")
                        .and("goodsId").as("goodsId").andInclude("goodsId")
                        .and("specId").as("specId").andInclude("specId")
                        .and("buyerId").as("buyerId").andInclude("buyerId")
        );
        List<GoodsSaleStatisDTO> goodsSaleStatisData = new ArrayList<GoodsSaleStatisDTO>();

        //执行查询 并获取结果
        List<GoodsStatisEntity> goodsStatisEntities = mongoTemplate.aggregate(aggregation, GoodsStatisEntity.class, GoodsStatisEntity.class).getMappedResults();
        Map<Long, List<GoodsStatisEntity>> map = goodsStatisEntities.stream().collect(Collectors.groupingBy(GoodsStatisEntity::getGoodsId));
        for (List<GoodsStatisEntity> v : map.values()) {
            if (CollectionUtils.isNotEmpty(v)) {
                GoodsStatisEntity goodsStatisEntity = v.get(0);
                GoodsSaleStatisDTO goodsSaleStatisDTO = new GoodsSaleStatisDTO();
                BeanCopier.create(GoodsStatisEntity.class, GoodsSaleStatisDTO.class, false).copy(goodsStatisEntity, goodsSaleStatisDTO, null);
                Integer sum = v.stream().mapToInt(GoodsStatisEntity::getSalesNumber).sum();
                BigDecimal bigDecimal = v.stream().map(GoodsStatisEntity::getSalesAmount).reduce(BigDecimal::add).get();
                goodsSaleStatisDTO.setSalesNumber(sum);
                goodsSaleStatisDTO.setSalesAmount(bigDecimal);
                goodsSaleStatisDTO.setPaymentNumber(v.size());
                //单品转换率
                if (null != goodsSaleStatisDTO.getPageView() && null != goodsSaleStatisDTO.getPaymentNumber() && 0 != goodsSaleStatisDTO.getPageView()) {
                    Double aDouble = NumberUtils.divisionTwoDecimal(goodsSaleStatisDTO.getPaymentNumber(), goodsSaleStatisDTO.getPageView());
                    goodsSaleStatisDTO.setSingleConversion(aDouble);
                } else {
                    goodsSaleStatisDTO.setSingleConversion((double) 0);
                }
                goodsSaleStatisData.add(goodsSaleStatisDTO);
                if (goodsSaleStatisData.size() >= 10) {
                    break;
                }
            }
        }
        return goodsSaleStatisData.stream().sorted(Comparator.comparing(GoodsSaleStatisDTO::getSalesNumber).reversed()).limit(10).collect(Collectors.toList());
    }

}
