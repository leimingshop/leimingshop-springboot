/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.detail.SpecAttributeDetailDTO;
import com.leimingtech.modules.dto.goods.price.SpecAttrNameDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeSaveDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecAttrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品规格属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Mapper
public interface SpecAttributeDao extends BaseDao<GoodsSpecAttrEntity> {
    /**
     * 根据商品删除信息
     *
     * @param goodsId 商品id
     */
    void deleteByGoodsId(Long goodsId);

    /**
     * 根据商品id查询规格信息
     *
     * @param goodsId 商品id
     * @return 返回规格属性信息
     */
    List<SpecAttributeDetailDTO> getByGoodsId(Long goodsId);

    /**
     * 根据商品id查询规格属性名称
     *
     * @param goodsId 商品id
     * @return 返回规格属性名称
     */
    List<SpecAttrNameDTO> getNameListByGoodsId(Long goodsId);

    /**
     * 功能描述 根据goodsId和specValue 查找goods选中的specAttr
     *
     * @param goodsId    商品id
     * @param specValues 规格属性值
     * @return 返回 规格属性
     * @author lishuo
     * @date 28/6/2020
     */
    List<SpecAttributeDTO> findSpecNameByGoodsId(@Param("goodsId") Long goodsId, @Param("specValues") List<String> specValues);

    /**
     * 保存信息
     *
     * @param goodsSpecAttrEntities 保存参数
     */
    void insertBatch(@Param("goodsSpecAttrEntities") List<SpecAttributeSaveDTO> goodsSpecAttrEntities);
}