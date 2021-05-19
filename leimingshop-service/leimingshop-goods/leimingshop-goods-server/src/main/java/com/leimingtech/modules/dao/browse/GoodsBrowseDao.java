/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.browse;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.browse.FindGoodsBrowseDTO;
import com.leimingtech.modules.dto.browse.GoodsBrowseDTO;
import com.leimingtech.modules.entity.browse.GoodsBrowseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 足迹记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */
@Mapper
public interface GoodsBrowseDao extends BaseDao<GoodsBrowseEntity> {
    /**
     * 根据ID获取信息
     *
     * @param goodsBrowseDTO 参数实体
     * @return
     */
    List<GoodsBrowseDTO> findById(GoodsBrowseDTO goodsBrowseDTO);

    /**
     * 分页查询足迹
     *
     * @param params
     * @return
     */
    List<FindGoodsBrowseDTO> findPage(Map<String, Object> params);

    /**
     * 根据时间查询浏览记录
     *
     * @param browseTime 浏览时间
     * @param memberId   用户id
     * @return 返回浏览记录信息
     */
    List<GoodsBrowseDTO> findListByTime(String browseTime, Long memberId);

    /**
     * 查询当前商品是否有记录
     *
     * @param goodsId  商品ID
     * @param memberId 用户ID
     * @return
     */
    GoodsBrowseDTO findBrowse(@Param("memberId") Long memberId, @Param("goodsId") Long goodsId);

    /**
     * 查询用户足迹数量
     *
     * @param id: 用户ID
     * @return: 数量
     */
    Integer countByMemberId(Long id);


}