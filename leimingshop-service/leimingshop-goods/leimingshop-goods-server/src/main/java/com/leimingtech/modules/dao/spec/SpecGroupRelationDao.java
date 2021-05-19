/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.spec.SpecGroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规格与规格分组关联管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Mapper
public interface SpecGroupRelationDao extends BaseDao<SpecGroupRelationEntity> {

    /**
     * 功能描述:
     * 〈根据分组id物理删除〉
     *
     * @param groupId 分组id
     * @return : int 删除数量
     * @author : 刘远杰
     */
    int deleteByGroupId(Long groupId);

}
