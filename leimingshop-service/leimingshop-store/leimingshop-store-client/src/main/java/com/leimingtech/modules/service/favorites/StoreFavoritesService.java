/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.favorites;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.favorites.StoreFavoritesByStoreIdDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesPageDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 用户店铺收藏
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */

public interface StoreFavoritesService {

    /**
     * 保存收藏店铺信息
     *
     * @param dto        保存实体
     * @param memberId   用户id
     * @param memberName 用户名称
     * @return void
     * @Description 收藏店铺
     * @Author huangkeyuan
     * @Date 16:46 2019-05-15
     */

    void saveStoreFav(@RequestBody StoreFavoritesSaveDTO dto, @RequestParam("memberId") Long memberId, @RequestParam("memberName") String memberName);

    /**
     * 删除收藏数据
     *
     * @param ids      主键id
     * @param memberId 用户id
     * @return void
     * @Description 删除
     * @Author huangkeyuan
     * @Date 11:48 2019-05-29
     */

    void delete(@RequestBody Long[] ids, @RequestParam("memberId") Long memberId);

    /**
     * 查询分页
     *
     * @param params 查询参数
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.favorites.StoreFavoritesDTO>
     * @Description 查询分页
     * @Author huangkeyuan
     * @Date 11:53 2019-05-29
     */

    PageData<StoreFavoritesPageDTO> storePage(@RequestParam Map<String, Object> params);

    /**
     * 校验用户是否关联该店铺
     *
     * @param userId
     * @param storeId
     * @return
     */

    StoreFavoritesDTO isFavorite(@RequestParam("userId") Long userId, @RequestParam("storeId") Long storeId);

    /**
     * 根据店铺Id查询关注人数
     *
     * @param storeId
     * @return
     */

    Integer selectFavoriteCount(Long storeId);

    /**
     * 根据店铺id查询信息
     *
     * @param userId  用户id
     * @param storeId 店铺id
     * @return 返回用户收藏数据
     * @Author: weixianchun
     * @Description: 根据店铺id查询信息
     * @Date :2019/7/29 16:03
     * @Version V1.0
     **/

    StoreFavoritesByStoreIdDTO selectByStoreId(@RequestParam("storeId") Long storeId, @RequestParam("userId") Long userId);
}
