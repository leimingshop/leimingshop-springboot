/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.favorities.code;

/**
 * 收藏入参日志码
 *
 * @author chengqian
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface PcFavoriteCode {

    /**
     * 访问用户商品收藏列表
     */
    String GOODS_FAVORITE_PAGE_CODE = "200650";
    String GOODS_FAVORITE_PAGE_DESC = "访问用户商品收藏列表";

    /**
     * 访问保存商品收藏
     */
    String SAVE_GOODS_FAVORITE_CODE = "200651";
    String SAVE_GOODS_FAVORITE_DESC = "访问保存商品收藏";

    /**
     * 访问取消商品收藏
     */
    String DELETE_GOODS_FAVORITE_CODE = "200652";
    String DELETE_GOODS_FAVORITE_DESC = "访问取消商品收藏";

    /**
     * 访问用户店铺收藏列表
     */
    String STORE_FAVORITE_PAGE_CODE = "200653";
    String STORE_FAVORITE_PAGE_DESC = "访问用户商品收藏列表";

    /**
     * 访问保存店铺收藏
     */
    String SAVE_STORE_FAVORITE_CODE = "200654";
    String SAVE_STORE_FAVORITE_DESC = "访问保存店铺收藏";

    /**
     * 访问取消店铺收藏
     */
    String DELETE_STORE_FAVORITE_CODE = "200655";
    String DELETE_STORE_FAVORITE_DESC = "访问取消店铺收藏";

}
