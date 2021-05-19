/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.satatis;

import com.leimingtech.admin.controller.satatis.validate.ValiDateParams;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.exception.ErrorCode;
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
import java.util.Map;

/**
 * 订单统计
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 17:23
 **/
@RestController
@RequestMapping("statis/order")
@Api(tags = "订单统计")
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
    public Result<Map> sevenDayStatis() {
        Map<String, SevenTransactionStatisShowDTO> stringObjectMap = orderStatisService.sevenDayStatis(null);
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
    public Result<TransactionStatisShowDTO> transactionStatisByParams(@ApiIgnore @RequestParam Map params) {
        try {
            String s = ValiDateParams.checkDate(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
        } catch (ParseException e) {
            log.error("日期转换错误{}", e.getMessage());
            return new Result().error(ErrorCode.FORBIDDEN, "日期转换错误");
        }
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
    public Result<Map<Integer, OrderSourceStatisticDTO>> selectOrderSource(@ApiIgnore @RequestParam Map params) {
        if (Integer.valueOf(params.get("queryType").toString()) == 3) {
            AssertUtils.isNull(params.get("date"), "查询月份");
        }
        Map<Integer, OrderSourceStatisticDTO> resultMap = orderStatisService.selectOrderSource(params);
        return new Result<Map<Integer, OrderSourceStatisticDTO>>().ok(resultMap);
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
    public Result<Map> singleConsumerStatis(@ApiIgnore @RequestParam Map params) {
        try {
            String s = ValiDateParams.checkDate(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
        } catch (ParseException e) {
            log.error("日期转换错误{}", e.getMessage());
            return new Result().error(ErrorCode.FORBIDDEN, "日期转换错误");
        }
        Map<String, Integer> stringIntegerMap = orderStatisService.singleConsumerStatis(params);
        return new Result<Map>().ok(stringIntegerMap);
    }
}
