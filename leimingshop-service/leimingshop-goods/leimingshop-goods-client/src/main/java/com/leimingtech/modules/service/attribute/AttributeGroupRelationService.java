/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attribute;


import com.leimingtech.modules.dto.attribute.AttributeGroupRelationDTO;

import java.util.List;
import java.util.Map;

/**
 * 属性与属性分组关联表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */

public interface AttributeGroupRelationService {
    /**
     * 根据分组id物理删除
     *
     * @param groupId 分组id
     * @return 返回删除成功数量
     */
    int deleteByAttrId(Long groupId);

    /**
     * 、
     * 根据分组id逻辑删除
     *
     * @param groupId 属性分组id、
     * @return 返回删除成功数量
     * @author 刘远杰
     * @Description 根据分组id逻辑删除
     * @Date 2019/5/20 16:42
     */
    int logicDeleteByGroupId(Long groupId);

    /**
     * 查询所有
     *
     * @param params 条件查询
     * @return 返回属性信息
     */
    List<AttributeGroupRelationDTO> list(Map<String, Object> params);

    /**
     * 批量插入
     *
     * @param attributeGroupRelationDTOList 插入参数
     */
    void insertBatch(List<AttributeGroupRelationDTO> attributeGroupRelationDTOList);
}
