/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.reduce;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.reduce.ReduceGoodsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 满减活动商品dao
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Mapper
public interface ReduceGoodsDao extends BaseDao<ReduceGoodsEntity> {

}
