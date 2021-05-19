/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.after;

import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.CollectionName;
import com.leimingtech.modules.dao.order.OrderStatisDao;
import com.leimingtech.modules.entity.order.TransactionStatisEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.statuscode.StatisticStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 售后统计实现类
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Slf4j
@Service

public class AfterSaleStatisticServiceImpl implements AfterSaleStatisticService {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(AfterSaleStatisticServiceImpl.class);

    @Autowired
    private OrderStatisDao orderStatisDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NosqlService nosqlService;


    @Override
    public void saveAfterReturnAmountByStore(@RequestBody List<Long> storeIds, @RequestParam(value = "date") String date) throws ParseException {
        DateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat month = new SimpleDateFormat("yyyy-MM");
        //转换日期
        Date date1 = day.parse(date);
        //设置查询条件。判断数据库是否有记录，有的话返回
        Query query = Query.query(Criteria.where("createDayTime").is(date1));
        List<TransactionStatisEntity> selectRes = mongoTemplate.find(query, TransactionStatisEntity.class);
        //初始化日志保存数据
        Map<String, String> logMap = new HashMap<>(1);
        logMap.put("保存日期", date);
        if (selectRes != null && selectRes.size() > 0) {
            mlogger.info(StatisticStatusCode.TRANSACTION_STATISTIC_DATA_REPEAT_SAVE_CODE, StatisticStatusCode.TRANSACTION_STATISTIC_DATA_REPEAT_SAVE_MASSAGE, logMap);
            return;
        }
        //查询MySql获取统计数据保存
        List<TransactionStatisEntity> transactionStatisEntities = orderStatisDao.transactionData(storeIds, date);
        nosqlService.saveBatch(transactionStatisEntities, CollectionName.TRANSACTION_STATISTICS);
        mlogger.info(StatisticStatusCode.TRANSACTION_STATISTIC_DATA_SAVE_CODE, StatisticStatusCode.TRANSACTION_STATISTIC_DATA_SAVE_MASSAGE, logMap);
    }

}
