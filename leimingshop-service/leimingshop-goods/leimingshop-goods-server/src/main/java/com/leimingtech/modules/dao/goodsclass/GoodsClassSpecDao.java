/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goodsclass;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.goodsclass.GoodsClassSpecEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类和规格关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-03
 */
@Mapper
public interface GoodsClassSpecDao extends BaseDao<GoodsClassSpecEntity> {
    /**
     * 根据分类id删除信息+
     *
     * @param id 分类id
     */
    void deleteByGcClassId(Long id);
}