/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.attribute;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.attribute.AttrClassDTO;
import com.leimingtech.modules.dto.attribute.AttributeDTO;
import com.leimingtech.modules.dto.attribute.AttributeIdAndNameDTO;
import com.leimingtech.modules.entity.attribute.AttributeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 属性管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Mapper
public interface AttributeDao extends BaseDao<AttributeEntity> {

    /**
     * 功能描述:
     * 〈批量查询属性名称〉
     *
     * @param ids 查询的属性ids
     * @return 返回属性名称
     * @author : 刘远杰
     */
    List<AttributeIdAndNameDTO> selectNameBatchByids(Long[] ids);

    /**
     * 功能描述:
     * 〈属性条件查询,可输入分组查询〉
     *
     * @param params 查询条件
     * @return 返回属性
     * @author : 刘远杰
     */
    List<AttributeDTO> findAttrListByGroupName(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈查询属性关联后台分类数量〉
     *
     * @param ids 属性id
     * @return 返回属性关联后台分类数量
     * @author : 刘远杰
     */
    List<AttrClassDTO> countClass(Long[] ids);

}
