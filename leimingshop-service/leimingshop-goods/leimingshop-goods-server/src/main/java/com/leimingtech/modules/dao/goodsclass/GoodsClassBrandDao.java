/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goodsclass;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.goodsclass.GoodsClassBrandEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类和品牌关联表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2019-06-25
 */
@Mapper
public interface GoodsClassBrandDao extends BaseDao<GoodsClassBrandEntity> {

}