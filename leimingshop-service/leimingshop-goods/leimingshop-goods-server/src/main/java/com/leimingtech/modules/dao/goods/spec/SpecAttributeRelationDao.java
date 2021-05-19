/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeRelationDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecAttrRelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品规格属性与属性值关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Mapper
public interface SpecAttributeRelationDao extends BaseDao<GoodsSpecAttrRelEntity> {
    /**
     * 查询规格id
     *
     * @param list
     * @param num
     * @return
     */
    Long selectSpecId(@Param("list") List<Long> list, @Param("num") Integer num);

    /**
     * 保存信息
     *
     * @param goodsSpecAttrRelEntities 保存参数
     */
    void saveBatch(@Param("goodsSpecAttrRelEntities") List<SpecAttributeRelationDTO> goodsSpecAttrRelEntities);
}