/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.satatis;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dto.activity.ActivityStatisFindDTO;
import com.leimingtech.modules.dto.activity.ActivityStatisGoodsPage;
import com.leimingtech.modules.service.activity.ActivityStatisService;
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


@RestController
@RequestMapping("activity")
@Api(tags = "营销统计")
@Slf4j
/**
 * 营销统计
 *
 * @return
 * @date 2020-09-18 16:04
 * @author huangkeyuan@leimingtech.com
 **/
public class ActivityStatisController {

    private static final Integer QUERY_TYPE = 4;
    private static final int YEAR_DAYS = 365;
    @Autowired
    private ActivityStatisService activityStatisService;

    /**
     * 营销统计展示
     *
     * @return
     * @Author chengqian 2020年03月31日
     */
    @ApiOperation("营销统计展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "activityType", value = "活动类型1 优惠券 2 满减", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "activityId", value = "活动ID", paramType = "query", dataType = "Long")
    })
    @GetMapping
    @LogContext(code = StatisticStatusCode.TRANSACTION_STATISTIC_CODE, note = StatisticStatusCode.TRANSACTION_STATISTIC_MASSAGE)
    public Result<ActivityStatisFindDTO> activityStatis(@ApiIgnore @RequestParam Map params, @ApiIgnore SellerDetail sellerDetail) {
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
            return new Result<ActivityStatisFindDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        params.put("storeId", sellerDetail.getStoreId().toString());
        ActivityStatisFindDTO activityStatisFindDTO = activityStatisService.findActivityStatistic(params);
        return new Result<ActivityStatisFindDTO>().ok(activityStatisFindDTO);
    }


    /**
     * 营销明细展示
     *
     * @return
     * @Author chengqian 2020年03月31日
     */
    @ApiOperation("营销明细展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "activityType", value = "活动类型1 优惠券 2 满减", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "activityId", value = "活动ID", paramType = "query", dataType = "Long")
    })
    @GetMapping("detail")
    @LogContext(code = StatisticStatusCode.TRANSACTION_STATISTIC_CODE, note = StatisticStatusCode.TRANSACTION_STATISTIC_MASSAGE)
    public Result<ActivityStatisGoodsPage> activityDetailStatis(@ApiIgnore @RequestParam Map params, @ApiIgnore SellerDetail sellerDetail) {
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
            return new Result<ActivityStatisGoodsPage>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        params.put("storeId", sellerDetail.getStoreId().toString());
        ActivityStatisGoodsPage activityStatisFindDTO = activityStatisService.activityGoodsStatis(params);
        return new Result<ActivityStatisGoodsPage>().ok(activityStatisFindDTO);
    }

    /**
     * 参数校验
     *
     * @param params
     */
    private String checkParams(Map params) throws ParseException {
        if (Integer.valueOf(params.get("queryType").toString()).equals(QUERY_TYPE)) {
            AssertUtils.isNull(params.get("startDate"), "查询开始时间");
            AssertUtils.isNull(params.get("startDate"), "查询结束时间");
            String startStr = params.get("startDate").toString();
            String endStr = params.get("endDate").toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startStr);
            Date endDate = dateFormat.parse(endStr);
            long dayCount = DateUtil.between(startDate, endDate, DateUnit.DAY, true);
            if (YEAR_DAYS < (int) dayCount) {
                return "日期选择错误，最多只能选择365天";
            }
        }
        return null;
    }
}
