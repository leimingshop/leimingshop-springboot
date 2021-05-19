/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.traffic;


import com.leimingtech.modules.dto.traffic.PageSourceStatisticDTO;
import com.leimingtech.modules.dto.traffic.PvDetailDTO;
import com.leimingtech.modules.dto.traffic.PvStatisticShowDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.Map;

/**
 * @ClassName TrafficStatisService
 * @Description 流量统计接口
 * @Author xuzhch
 * @Date 2019年12月6日
 * @Version 1.0
 **/


public interface PvDetailStatisService {

    /**
     * 保存流量统计记录
     *
     * @param pvDetail 浏览量保存对象
     * @return
     * @date 2019/12/31/031 15:00
     * @author xuzhch
     **/

    void savePageview(@RequestBody PvDetailDTO pvDetail);


    /**
     * 查询浏览量统计数据
     *
     * @param params { (name = "timeType", value = "时间类型(1:时,2:日,3:月)", paramType = "query", required = true, dataType = "int"),
     *               (name = "startDate", value = "查询开始时间", paramType = "query", required = true, dataType = "Date"),
     *               (name = "endDate", value = "查询结束时间", paramType = "query", required = true, dataType = "Date"),
     *               (name = "statisticType", value = "统计页面(1:全站、2:首页、3:商品分类页、4:购物车、5:商品详情页)", paramType = "query", required = true, defaultValue = "1", dataType = "int")
     *               }
     * @return Map key为时间节点   value是浏览量数据
     * @throws ParseException 日期转换异常
     * @author xuzhch
     * @date 2019 /12/31/031 14:58
     */

    Map<String, PvStatisticShowDTO> flowStatistic(@RequestParam Map params) throws ParseException;

    /**
     * 页面流量来源统计
     *
     * @param params 查询条件
     * @return PageSourceStatisticDTO 页面流量来源统计结果
     * @throws ParseException 日期转换异常
     * @author xuzhch
     * @date 2020年09月18日
     */

    PageSourceStatisticDTO pageStatisticSource(@RequestParam Map params) throws ParseException;
}
