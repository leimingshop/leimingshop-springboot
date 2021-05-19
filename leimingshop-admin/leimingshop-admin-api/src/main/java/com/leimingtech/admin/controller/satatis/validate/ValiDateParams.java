/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.satatis.validate;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.leimingtech.commons.tools.validator.AssertUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author xuzhch
 * @className ValiDateParams
 * @description 参数校验
 * @date 2020/1/17/017
 */
public class ValiDateParams {
    public static String checkTime(Map params) throws ParseException {
        AssertUtils.isNull(params.get("timeType"), "查询时间类型");
        AssertUtils.isNull(params.get("startDate"), "查询开始时间");
        AssertUtils.isNull(params.get("endDate"), "查询结束时间");
        String startStr = params.get("startDate").toString();
        String endStr = params.get("endDate").toString();
        if ("1".equals(params.get("timeType").toString())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            Date startDate = dateFormat.parse(startStr);
            Date endDate = dateFormat.parse(endStr);
            long dayCount = DateUtil.between(startDate, endDate, DateUnit.HOUR, true);
            if (24 < (int) dayCount) {
                return "日期选择错误，最多只能选择24小时";
            }
        }
        if ("2".equals(params.get("timeType").toString())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startStr);
            Date endDate = dateFormat.parse(endStr);
            long dayCount = DateUtil.between(startDate, endDate, DateUnit.DAY, true);
            if (31 < (int) dayCount) {
                return "日期选择错误，最多只能选择31天";
            }
        }
        if ("3".equals(params.get("timeType").toString())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date startDate = dateFormat.parse(startStr);
            Date endDate = dateFormat.parse(endStr);
            long dayCount = DateUtil.betweenMonth(startDate, endDate, true);
            if (12 < (int) dayCount) {
                return "日期选择错误，最多只能选择12个月";
            }
        }
        return null;
    }

    /**
     * 参数校验
     *
     * @param params
     */
    public static String checkDate(Map params) throws ParseException {
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
