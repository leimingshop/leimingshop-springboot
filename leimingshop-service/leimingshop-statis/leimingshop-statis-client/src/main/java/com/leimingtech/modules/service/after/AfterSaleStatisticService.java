/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.after;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

/**
 * 售后统计接口
 *
 * @author xuzhch
 * @date 2020年9月17日
 */

public interface AfterSaleStatisticService {

    /**
     * 保存退款统计
     *
     * @param storeIds 店铺ID集合
     * @param date     日期
     * @throws ParseException
     */

    void saveAfterReturnAmountByStore(@RequestBody List<Long> storeIds, @RequestParam(value = "date") String date) throws ParseException;

}
