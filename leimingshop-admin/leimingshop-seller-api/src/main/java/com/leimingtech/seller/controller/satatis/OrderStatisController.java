/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.satatis;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dto.order.OrderSourceStatisticDTO;
import com.leimingtech.modules.dto.order.SevenTransactionStatisShowDTO;
import com.leimingtech.modules.dto.order.TransactionStatisShowDTO;
import com.leimingtech.modules.service.order.OrderStatisService;
import com.leimingtech.modules.statuscode.StatisticStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * seller交易统计
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 17:24
 **/
@RestController
@RequestMapping("statis/order")
@Api(tags = "seller交易统计")
@Slf4j
public class OrderStatisController {

    @Autowired
    private OrderStatisService orderStatisService;

    /**
     * 近七日交易数据统计
     *
     * @return
     * @Author xuzhch 2019年12月18日14:09
     */
    @ApiOperation("近七日交易数据统计")
    @GetMapping("seven/day/statis")
    @LogContext(code = StatisticStatusCode.SEVEN_DAY_TRANSACTION_STATISTIC_CODE, note = StatisticStatusCode.SEVEN_DAY_TRANSACTION_STATISTIC_MASSAGE)
    public Result<Map> sevenDayStatis(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result<Map>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        Map<String, SevenTransactionStatisShowDTO> stringObjectMap = orderStatisService.sevenDayStatis(storeId);
        return new Result<Map>().ok(stringObjectMap);
    }

    /**
     * 交易统计数据展示
     *
     * @return
     * @Author xuzhch 2019年12月18日14:09
     */
    @ApiOperation("交易统计数据展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date")
    })
    @GetMapping("find/transaction")
    @LogContext(code = StatisticStatusCode.TRANSACTION_STATISTIC_CODE, note = StatisticStatusCode.TRANSACTION_STATISTIC_MASSAGE)
    public Result<TransactionStatisShowDTO> transactionStatisByParams(@ApiIgnore @RequestParam Map params, @ApiIgnore SellerDetail sellerDetail) {
        try {
            String s = checkParams(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
        } catch (ParseException e) {
            log.error("日期转换错误{}", e.getMessage());
            return new Result().error(ErrorCode.FORBIDDEN, "日期转换错误");
        }
        if (sellerDetail == null) {
            return new Result<TransactionStatisShowDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        TransactionStatisShowDTO transactionStatisShowDTO = orderStatisService.transactionStatisByParams(params);
        return new Result<TransactionStatisShowDTO>().ok(transactionStatisShowDTO);
    }

    /**
     * 订单来源数据展示
     *
     * @return
     * @Author xuzhch 2020年3月14日21:07:50
     */
    @ApiOperation("订单来源数据展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:本月,2:上月,3:自定义月份【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "date", value = "月份", paramType = "query", dataType = "Date"),
    })
    @GetMapping("source")
    @LogContext(code = StatisticStatusCode.ORDER_SOURCE_STATISTI_CODE, note = StatisticStatusCode.ORDER_SOURCE_STATISTIC_MASSAGE)
    public Result<Map<Integer, OrderSourceStatisticDTO>> selectOrderSource(@ApiIgnore @RequestParam Map params, @ApiIgnore SellerDetail sellerDetail) {
        if (Integer.valueOf(params.get("queryType").toString()) == 3) {
            AssertUtils.isNull(params.get("date"), "查询月份");
        }
        if (sellerDetail == null) {
            return new Result<Map<Integer, OrderSourceStatisticDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        Map<Integer, OrderSourceStatisticDTO> resultMap = orderStatisService.selectOrderSource(params);
        return new Result<Map<Integer, OrderSourceStatisticDTO>>().ok(resultMap);
    }

    /**
     * 参数校验
     *
     * @param params
     */
    private String checkParams(Map params) throws ParseException {
        if (Integer.valueOf(params.get("queryType").toString()) == 4) {
            AssertUtils.isNull(params.get("startDate"), "查询开始时间");
            AssertUtils.isNull(params.get("startDate"), "查询结束时间");
            String startStr = params.get("startDate").toString();
            String endStr = params.get("endDate").toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startStr);
            Date endDate = dateFormat.parse(endStr);
            long dayCount = DateUtil.between(startDate, endDate, DateUnit.DAY, true);
            if (365 < (int) dayCount) {
                return "日期选择错误，最多只能选择365天";
            }
        }
        return null;
    }

    /**
     * 单人消费数据统计展示
     *
     * @return
     * @Author xuzhch 2019年12月18日14:09
     */
    @ApiOperation("单人消费数据统计展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date")
    })
    @GetMapping("single/consumer")
    @LogContext(code = StatisticStatusCode.SINGLE_CONSUME_STATISTIC_CODE, note = StatisticStatusCode.SINGLE_CONSUME_STATISTIC_MASSAGE)
    public Result<Map> singleConsumerStatis(@ApiIgnore @RequestParam Map params, @ApiIgnore SellerDetail sellerDetail) {
        try {
            String s = checkParams(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
        } catch (ParseException e) {
            log.error("日期转换错误{}", e.getMessage());
            return new Result().error(ErrorCode.FORBIDDEN, "日期转换错误");
        }
        if (sellerDetail == null) {
            return new Result<Map>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        Map<String, Integer> stringIntegerMap = orderStatisService.singleConsumerStatis(params);
        return new Result<Map>().ok(stringIntegerMap);
    }
}
