/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.activity;


import com.leimingtech.modules.dto.activity.ActivityStatisFindDTO;
import com.leimingtech.modules.dto.activity.ActivityStatisGoodsPage;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.Map;

/**
 * @ClassName ActivityStatisService
 * @Description 活动信息统计
 * @Author chengqian
 * @Date 2020-3-27
 * @Version 1.0
 **/

public interface ActivityStatisService {

    /**
     * 定时统计营销信息
     *
     * @throws ParseException 日期转换异常
     * @author xuzhch
     * @date 2020年9月17日
     */

    void timeActivityStatis() throws ParseException;


    /**
     * 营销统计展示
     *
     * @param map 查询条件
     * @return 统计结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    ActivityStatisFindDTO findActivityStatistic(@RequestParam Map<String, Object> map);

    /**
     * 营销商品明细分页
     *
     * @param map 查询条件
     * @return 统计结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    ActivityStatisGoodsPage activityGoodsStatis(@RequestParam Map<String, Object> map);


}
