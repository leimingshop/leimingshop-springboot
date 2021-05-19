/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.label;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelSaveDTO;
import com.leimingtech.modules.entity.label.GoodsLabelRelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品与标签关联表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2019-12-13
 */
@Mapper
public interface GoodsLabelRelDao extends BaseDao<GoodsLabelRelEntity> {

    /**
     * 查询标签关联的商品数量, 店铺数量
     *
     * @param goodsId 商品id
     * @param labelId 标签id
     * @return 返回标签关联的商品数量, 店铺数量
     * @Author weixianchun
     * @Description 查询标签关联的商品数量, 店铺数量
     * @Date 2019/12/13 17:11
     * @Return com.leimingtech.modules.dto.goodslable.GoodsLabelRelDTO
     * @version 1.0
     */
    GoodsLabelRelDTO findNumGoodsAndStore(@Param("goodsId") Long goodsId, @Param("labelId") Long labelId);

    /**
     * 、
     * 根据商品ID删除数据
     *
     * @param goodsId 商品id
     * @return 返回删除成功数量
     * @Author weixianchun
     * @Description 根据商品ID删除数据
     * @Date 2019/12/12 10:52
     * @Return int
     * @version 1.0
     */
    int deleteByGoodsId(Long goodsId);

    /**
     * 根据标签ID查询商品列表
     *
     * @param labelId 标签id
     * @return 返回商品信息
     * @Author weixianchun
     * @Description 根据标签ID查询商品列表
     * @Date 2019/12/17 16:56
     * @version 1.0
     */
    List<GoodsDTO> findGoodsByLabelId(@Param(("labelId")) Long labelId);

    /**
     * 保存商品标签
     *
     * @param goodsLabelRelEntityList 保存参数
     */
    void saveBatch(@Param("goodsLabelRelEntityList") List<GoodsLabelRelSaveDTO> goodsLabelRelEntityList);
}
