/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.service;

import com.leimingtech.modules.dto.coupons.GoodsDetailCouponsListDTO;

/**
 * 优惠券搜索
 *
 * @author chengqian
 */
public interface CouponsSearchService {

    /**
     * 功能描述：
     * <p>
     * 商品详情页搜索（优惠券部分）
     *
     * @param storeId           店铺id
     * @param brandId           品牌id
     * @param goodsId           商品id
     * @param storeGoodsClassId 店铺商品分类id
     * @return 返回商品优惠券信息
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/12
     **/
    GoodsDetailCouponsListDTO goodsDetailCouponsList(Long goodsId, Long brandId, Long storeId, Long storeGoodsClassId);

}
