/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.favorites;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.favorites.StoreFavoritesPageDTO;
import com.leimingtech.modules.entity.favorites.StoreFavoritesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户店铺收藏
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Mapper
public interface StoreFavoritesDao extends BaseDao<StoreFavoritesEntity> {
    /**
     * 查询用户收藏店铺列表
     *
     * @return java.util.List<com.leimingtech.modules.dto.favorites.StoreFavoritesPageDTO>
     * @Description 查询用户收藏店铺列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 15:36 2019-07-31
     */
    List<StoreFavoritesPageDTO> favPage(Map<String, Object> params);
}
