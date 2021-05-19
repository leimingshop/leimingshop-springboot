/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.favorites;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.favorites.FavoritesPageDTO;
import com.leimingtech.modules.entity.favorites.FavoritesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户收藏
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Mapper
public interface FavoritesDao extends BaseDao<FavoritesEntity> {
    /**
     * 查询用户的收藏列表
     *
     * @param params 查詢條件
     * @return java.util.List<com.leimingtech.modules.dto.favorites.FavoritesPageDTO>
     * @Description 查询用户的收藏列表
     * @Author huangkeyuan
     * @Date 21:29 2019-07-22
     */
    List<FavoritesPageDTO> favPage(Map<String, Object> params);

    /**
     * 查询商品是否收藏
     *
     * @param specId   商品规格ID
     * @param memberId 用户ID
     * @return
     */
    Integer findIsFavorites(@Param("specId") Long specId, @Param("memberId") Long memberId);

    /**
     * 根据用户查询收藏数量
     *
     * @param id: 会员ID
     * @return: 返回用户收藏的数量
     * @date 2019/8/16 10:45
     * @author lixiang
     **/
    Integer countByMemberId(Long id);

    /**
     * 用户收藏商品ID
     *
     * @param memberId 用户id
     * @return 返回商品id
     */
    List<Long> getFavoriteGoodIds(Long memberId);

    /**
     * 查询用户是否收藏店铺
     *
     * @param storeId
     * @param memberId
     * @return
     */
    Integer findIsFavoStore(@Param("storeId") Long storeId,
                            @Param("memberId") Long memberId);

    /**
     * 查询商品收藏的总数量
     *
     * @param goodsId
     * @return
     */
    Integer goodsTotalFav(Long goodsId);
}
