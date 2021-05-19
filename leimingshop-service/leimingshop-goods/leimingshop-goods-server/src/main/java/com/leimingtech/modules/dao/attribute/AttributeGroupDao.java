/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.attribute;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.attribute.AttributeGroupEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 属性分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Mapper
public interface AttributeGroupDao extends BaseDao<AttributeGroupEntity> {

}
