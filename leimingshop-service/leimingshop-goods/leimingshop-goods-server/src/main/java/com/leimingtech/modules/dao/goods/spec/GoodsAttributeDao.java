/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeSaveDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsAttributeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Mapper
public interface GoodsAttributeDao extends BaseDao<GoodsAttributeEntity> {
    /**
     * 根据商品id删除信息
     *
     * @param goodsId 商品id
     */
    void deleteByGoodsId(Long goodsId);

    /**
     * 批量保存信息
     *
     * @param goodsAttributeEntities 保存参数
     */
    void saveBatch(@Param("goodsAttributeEntities") List<GoodsAttributeSaveDTO> goodsAttributeEntities);
}