/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;


import com.leimingtech.modules.dto.goods.GoodsClassStatisDTO;
import com.leimingtech.modules.dto.goods.GoodsSaleStatisDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GoodsStatisService
 * @Description 商品统计接口
 * @Author xuzhch
 * @Date 2019年12月6日
 * @Version 1.0
 **/

public interface GoodsStatisService {

    /**
     * 定时同步商品统计信息
     *
     * @param params 日期 参数格式为  url?params=yyyy-MM-dd
     * @throws ParseException 日期转换异常
     * @author xuzhch 2019年12月17日17:02
     */

    void saveGoodsStatis(@RequestParam(required = false) String params) throws ParseException;


    /**
     * 商品分类信息统计展示
     *
     * @param params 查询条件
     * @return 商品分类信息统计结果
     * @author xuzhch
     * @date 2020 -09-18
     */

    List<GoodsClassStatisDTO> classGoodsShowStatis(@RequestParam Map params);


    /**
     * 商品销量统计信息展示
     *
     * @param params Map
     * @return 单品销量数据集合
     * @author xuzhch 2019年12月17日17:04
     */

    List<GoodsSaleStatisDTO> goodsSaleShowSattis(@RequestParam Map params);
}
