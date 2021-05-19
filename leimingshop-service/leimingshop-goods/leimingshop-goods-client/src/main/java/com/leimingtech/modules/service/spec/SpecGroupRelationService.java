/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec;


import com.leimingtech.modules.dto.spec.SpecGroupRelationDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 规格与规格分组关联管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */

public interface SpecGroupRelationService {

    /**
     * 功能描述:
     * 〈根据分组id物理删除〉
     *
     * @param groupId 规格分组id
     * @return : int 删除数量
     * @author : 刘远杰
     */

    int deleteByGroupId(Long groupId);

    /**
     * 功能描述:
     * 〈根据分组id逻辑删除〉
     *
     * @param groupId 规格分组id
     * @return : int 删除数量
     * @author : 刘远杰
     */

    int logicDeleteByGroupId(Long groupId);

    /**
     * 功能描述:
     * 〈批量保存〉
     *
     * @param dtoList
     * @return : boolean
     * @author : 刘远杰
     */

    boolean insertBatch(List<SpecGroupRelationDTO> dtoList);

    /**
     * 功能描述:
     * 〈规格关联条件查询〉
     *
     * @param params 查询条件
     * @return 返回规格关联信息
     * @author : 刘远杰
     */

    List<SpecGroupRelationDTO> list(@RequestParam Map<String, Object> params);

}
