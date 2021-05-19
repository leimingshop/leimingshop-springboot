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
import com.leimingtech.modules.dto.goods.GoodsClassStatisDTO;
import com.leimingtech.modules.dto.goods.GoodsSaleStatisDTO;
import com.leimingtech.modules.service.goods.GoodsStatisService;
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
import java.util.List;
import java.util.Map;

/**
 * 商品统计
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:54
 **/
@RestController
@RequestMapping("statis/goods")
@Api(tags = "商品统计")
@Slf4j
public class GoodsStatisController {

    @Autowired
    private GoodsStatisService goodsStatisService;

    /**
     * 商品分类信息统计展示
     *
     * @param params Map
     * @Author xuzhch 2019年12月17日17:03
     */
    @ApiOperation("商品分类信息统计展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date")
    })
    @GetMapping("class/goods/statis")
    @LogContext(code = StatisticStatusCode.GOODS_CLASS_SALES_STATISTIC_CODE, note = StatisticStatusCode.GOODS_CLASS_SALES_STATISTIC_MASSAGE)
    public Result<List<GoodsClassStatisDTO>> classGoodsShowStatis(@ApiIgnore @RequestParam Map params, @ApiIgnore SellerDetail sellerDetail) {
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
            return new Result<List<GoodsClassStatisDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<GoodsClassStatisDTO> goodsClassStatisDTOS = goodsStatisService.classGoodsShowStatis(params);
        return new Result<List<GoodsClassStatisDTO>>().ok(goodsClassStatisDTOS);
    }

    /**
     * 商品销量统计信息展示
     *
     * @param params Map
     * @Author xuzhch 2019年12月17日17:03
     */
    @ApiOperation("商品销量统计信息展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型(1:昨天,2:近七天,3:最近30天,4:自定义时间【yyyy-MM-dd】)", required = true, defaultValue = "1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date")
    })
    @GetMapping("goods/sale/statis")
    @LogContext(code = StatisticStatusCode.SINGLE_GOODS_SALES_STATISTIC_CODE, note = StatisticStatusCode.SINGLE_GOODS_SALES_STATISTIC_MASSAGE)
    public Result<List<GoodsSaleStatisDTO>> goodsSaleShowSattis(@ApiIgnore @RequestParam Map params, @ApiIgnore SellerDetail sellerDetail) {
        try {
            String s = checkParams(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
        } catch (ParseException e) {
            log.info("日期转换异常", e);
            return new Result().error(ErrorCode.FORBIDDEN, "日期格式错误");
        }
        if (sellerDetail == null) {
            return new Result<List<GoodsSaleStatisDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<GoodsSaleStatisDTO> goodsSaleStatisDTOS = goodsStatisService.goodsSaleShowSattis(params);
        return new Result<List<GoodsSaleStatisDTO>>().ok(goodsSaleStatisDTOS);
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
}
