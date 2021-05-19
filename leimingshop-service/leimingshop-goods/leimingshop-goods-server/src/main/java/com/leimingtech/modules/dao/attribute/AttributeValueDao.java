/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.attribute;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.attribute.AttributeValueDTO;
import com.leimingtech.modules.entity.attribute.AttributeValueEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 属性值管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Mapper
public interface AttributeValueDao extends BaseDao<AttributeValueEntity> {
    /**
     * 删除属性值
     *
     * @param attrId 属性id
     * @return 返回删除成功数量
     */
    Integer deleteAttrValueByAttrId(Long attrId);

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
