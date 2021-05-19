/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.seckill;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.seckill.*;

import java.util.List;
import java.util.Map;

/**
 * <秒杀活动搜索管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/13
 */

public interface SeckillActivitySearchService {

    /**
     * 功能描述：
     * <正在进行的秒杀场次信息>
     *
     * @param
     * @return
     * @date 2020/3/13 11:06
     * @author 刘远杰
     **/

    StartSeckillSessionDTO startSeckillSession();

    /**
     * 功能描述：
     * <即将开始的秒杀场次信息>
     *
     * @param
     * @return
     * @date 2020/3/13 11:06
     * @author 刘远杰
     **/

    List<PrestartSeckillSessionDTO> prestartSeckillSessionList();

    /**
     * 功能描述：
     * <秒杀更多预告天数列表>
     *
     * @param
     * @return
     * @date 2020/3/15 19:53
     * @author 刘远杰
     **/

    List<MoreDaysSeckillDTO> morePrestartSeckillSessionList();

    /**
     * 功能描述：
     * <正在进行的秒杀场次分页>
     *
     * @param
     * @return
     * @date 2020/3/13 11:06
     * @author 刘远杰
     **/

    PageData<SeckillGoodsPageDTO> startSeckillGoodsPage(Map<String, Object> params);

    /**
     * 功能描述：
     * <即将售罄秒杀商品列表>
     *
     * @param params
     * @return
     * @date 2020/3/16 10:51
     * @author 刘远杰
     **/

    PageData<SeckillGoodsPageDTO> preendSeckillGoodsPage(Map<String, Object> params);

    /**
     * 功能描述：
     * <即将开始的秒杀场次商品分页>
     *
     * @param params   查询条件
     * @param memberId 会员id
     * @return
     * @date 2020/3/15 20:29
     * @author 刘远杰
     **/

    PageData<PrestartSeckillGoodsPageDTO> seckillGoodsPage(Map<String, Object> params,
                                                           Long memberId);

}
