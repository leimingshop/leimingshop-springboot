/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.GoodsInfoDTO;
import com.leimingtech.modules.entity.goods.GoodsInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品附加信息表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Mapper
public interface GoodsInfoDao extends BaseDao<GoodsInfoEntity> {

    /**
     * 功能描述:
     * 〈批量修改销量〉
     *
     * @param goodsInfoDTOList
     * @return 返回修改数量
     * @author : 刘远杰
     */
    Integer updateSaleList(@Param("goodsInfoDTOList") List<GoodsInfoDTO> goodsInfoDTOList);

    /**
     * 修改评论次数
     *
     * @param goodsInfoDTO
     * @return
     */
    Integer updateEvalusteNum(GoodsInfoDTO goodsInfoDTO);

    /**
     * 批量保存
     *
     * @param goodsInfoEntities 保存参数
     */
    void insertBatch(@Param("goodsInfoEntities") List<GoodsInfoDTO> goodsInfoEntities);
}