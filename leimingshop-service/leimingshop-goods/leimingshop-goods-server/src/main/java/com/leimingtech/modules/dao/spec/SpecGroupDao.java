/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.spec.SpecGroupEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 规格分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Mapper
public interface SpecGroupDao extends BaseDao<SpecGroupEntity> {

    /**
     * 功能描述:
     * 〈规格展示列表查询〉
     *
     * @param params 查询条件
     * @return 返回规格分组信息
     * @author : 刘远杰
     */
    List<SpecGroupEntity> findSpecGroupShowList(Map<String, Object> params);

}
