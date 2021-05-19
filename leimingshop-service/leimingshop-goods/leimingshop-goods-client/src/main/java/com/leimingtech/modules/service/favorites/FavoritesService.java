/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.favorites;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.favorites.FavoritesDTO;
import com.leimingtech.modules.dto.favorites.FavoritesPageDTO;
import com.leimingtech.modules.dto.favorites.FavoritesQueryDTO;
import com.leimingtech.modules.vo.FavResulltVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户收藏
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */

public interface FavoritesService {

    /**
     * 收藏商品
     *
     * @param dto 收藏参数
     * @return void
     * @Description 收藏商品
     * @Author huangkeyuan
     * @Date 16:46 2019-05-15
     */

    Map<String, Object> saveGoodsFav(@RequestBody FavoritesDTO dto);

    /**
     * 查询分页信息
     *
     * @param params 查询参数
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goodsclass.GoodsClassDTO>
     * @Description 查询分页信息
     * @Author huangkeyuan
     * @Date 16:55 2019-05-28
     */

    PageData<FavoritesPageDTO> favPage(@RequestParam Map<String, Object> params);

    /**
     * 删除商品
     *
     * @param memberId 用户id
     * @param ids      商品id
     * @return com.leimingtech.commons.tools.utils.Result
     * @Description 删除商品
     * @Author huangkeyuan
     * @Date 15:59 2019-05-23
     */

    void delete(@RequestBody Long[] ids, @RequestParam("memberId") Long memberId);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */

    FavoritesQueryDTO get(Long id);

    /**
     * 购物车批量移入收藏
     *
     * @param memberId 用户id
     * @return void
     * @Description 购物车批量移入收藏
     * @Author huangkeyuan
     * @Date 15:49 2019-07-24
     */

    void cartFavoriteGoods(@RequestParam("memberId") Long memberId);

    /**
     * 根据用户id获取商品收藏数量
     *
     * @param id
     * @return
     */

    Integer countByMemberId(Long id);

    /**
     * 查询商品是否收藏
     *
     * @param specId   商品规格ID
     * @param memberId 用户id
     * @return 返回是否收藏数量
     */

    Integer findIsFavorites(@RequestParam("specId") Long specId, @RequestParam("memberId") Long memberId);

    /**
     * 用户收藏商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */

    List<Long> getFavoriteGoodIds(@RequestParam("memberId") Long memberId);


    /**
     * 购物车单个商品移入收藏夹
     *
     * @param cartId
     */

    void cartOneFavorites(@RequestParam("cartId") Long cartId);

    /**
     * 查询用户是否收藏店铺和商品ID
     *
     * @param memberId 用户ID
     * @param storeId  店铺ID
     * @param specId   规格ID
     * @return
     */

    FavResulltVO isFavGoodsStore(@RequestParam(value = "memberId") Long memberId,
                                 @RequestParam(value = "storeId", required = false) Long storeId,
                                 @RequestParam(value = "specId", required = false) Long specId);

    /**
     * 查询当前商品收藏数量
     *
     * @param goodsId 规格ID
     * @return
     */

    Integer goodsTotalFav(@RequestParam("goodsId") Long goodsId);
}
