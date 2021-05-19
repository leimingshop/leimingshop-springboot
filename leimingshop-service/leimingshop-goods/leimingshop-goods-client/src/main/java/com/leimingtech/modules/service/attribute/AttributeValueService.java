/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attribute;


import com.leimingtech.modules.dto.attribute.AttributeValueDTO;

import java.util.List;
import java.util.Map;

/**
 * 属性值管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */

public interface AttributeValueService {

    /**
     * 返回删除成功数量
     *
     * @param attrId
     * @return 返回删除成功数量
     * @author 刘远杰
     * @Description 属性值物理删除
     * @Date 15:31
     */
    Integer deleteAttrValueByAttrId(Long attrId);

    /**
     * 删除属性
     *
     * @param attrId
     * @return 返回删除成功数量
     */
    Integer logicDeleteAttrValueByAttrId(Long attrId);

    /**
     * 查询属性
     *
     * @param params 查询条件
     * @return 返回属性信息
     */
    List<AttributeValueDTO> list(Map<String, Object> params);

    /**
     * 批量保存
     *
     * @param list 保存参数
     */
    void insertBatch(List<AttributeValueDTO> list);

    /**
     * 根据属性ID查询属性值集合
     *
     * @param id 主键id
     * @return 返回属性值
     * @Author: LX 17839193044@162.com
     * @Description: 根据属性ID查询属性值集合
     * @Date: 2019/6/13 16:44
     * @Version: V1.0
     */
    List<AttributeValueDTO> findListByAttrId(Long id);
}
