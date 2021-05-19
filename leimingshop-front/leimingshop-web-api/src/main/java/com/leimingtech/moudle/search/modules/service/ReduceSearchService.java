/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.service;

import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;

/**
 * 满减搜索
 *
 * @author chengqian
 */
public interface ReduceSearchService {

    /**
     * 功能描述：
     * <p>
     * 商品详情页搜索（满减活动部分）
     *
     * @param goodsId         商品id
     * @param brandId         品牌id
     * @param storeId         店铺id
     * @param storeGoodsClass 店铺二级分类id
     * @return 返回满减活动信息
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/14
     **/
    FrontReduceActivityPageDTO goodsDetailReduceList(Long goodsId, Long brandId, Long storeId, Long storeGoodsClass);

}
