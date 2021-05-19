/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.satatis;

import com.leimingtech.admin.controller.satatis.validate.ValiDateParams;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.traffic.PageSourceStatisticDTO;
import com.leimingtech.modules.dto.traffic.PvStatisticShowDTO;
import com.leimingtech.modules.service.traffic.PvDetailStatisService;
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

@RestController
@RequestMapping("statis/page")
@Slf4j
@Api(tags = "流量统计")
public class PvStatisController {

    @Autowired
    private PvDetailStatisService pvDetailStatisService;

    @GetMapping("pv")
    @ApiOperation("页面流量统计展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeType", value = "时间类型(1:时,2:日,3:月)", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "statisticType", value = "统计页面(0:全站、1:首页、2:商品分类页、3:购物车、4:商品详情页)", paramType = "query", required = true, defaultValue = "1", dataType = "int")
    })
    @LogContext(code = StatisticStatusCode.PAGE_VIEW_STATISTIC_CODE, note = StatisticStatusCode.PAGE_VIEW_STATISTIC_MASSAGE)
    public Result<Map> flowStatistic(@ApiIgnore @RequestParam Map params) {
        try {
            String s = ValiDateParams.checkTime(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
            Map<String, PvStatisticShowDTO> resultMap = pvDetailStatisService.flowStatistic(params);
            return new Result().ok(resultMap);
        } catch (ParseException e) {
            log.error("日期转换错误{}", e.getMessage());
            return new Result().error(ErrorCode.FORBIDDEN, "日期转换错误");
        }
    }

    @GetMapping("source")
    @ApiOperation("页面流量统计展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date")})
    @LogContext(code = StatisticStatusCode.PAGE_VIEW_STATISTIC_CODE, note = StatisticStatusCode.PAGE_VIEW_STATISTIC_MASSAGE)
    public Result<PageSourceStatisticDTO> pageStatisticSource(@ApiIgnore @RequestParam Map params) {
        try {
            String s = ValiDateParams.checkDate(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
            PageSourceStatisticDTO pageSourceStatisticDTO = pvDetailStatisService.pageStatisticSource(params);
            return new Result().ok(pageSourceStatisticDTO);
        } catch (ParseException e) {
            log.error("日期转换错误{}", e.getMessage());
            return new Result().error(ErrorCode.FORBIDDEN, "日期转换错误");
        }
    }
}
