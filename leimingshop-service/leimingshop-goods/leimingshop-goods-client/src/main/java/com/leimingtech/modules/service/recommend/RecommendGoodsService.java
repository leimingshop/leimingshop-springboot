/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.recommend;


import com.leimingtech.modules.dto.goods.GoodsRecommendDTO;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 推荐商品查询
 * @Date: 2019/7/12 17:35
 * @Version: V1.0
 */

public interface RecommendGoodsService {
    /**
     * 查询推荐商品列表数据
     *
     * @return 返回推荐商品
     * @Author: SWH ab4856812@163.com
     * @Description: 查询推荐商品列表数据
     * @Date: 2019/7/12 17:38
     * @Version: V1.0
     */
    @ApiOperation("推荐商品")
    List<GoodsRecommendDTO> findRecommendList();
}