/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.attribute;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.attribute.AttributeGroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 属性与属性分组关联管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Mapper
public interface AttributeGroupRelationDao extends BaseDao<AttributeGroupRelationEntity> {

    /**
     * 根据分组id物理删除
     *
     * @param groupId 分组id
     * @return 返回删除成功数量
     * @author 刘远杰
     * @Description 根据分组id物理删除
     * @Date 2019/5/17 16:43
     */
    int deleteByAttrId(Long groupId);

}
