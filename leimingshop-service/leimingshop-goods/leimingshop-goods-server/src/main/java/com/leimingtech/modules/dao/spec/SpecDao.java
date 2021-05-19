/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.spec.SpecClassDTO;
import com.leimingtech.modules.dto.spec.SpecIdAndNameDTO;
import com.leimingtech.modules.entity.spec.SpecEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 规格管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Mapper
public interface SpecDao extends BaseDao<SpecEntity> {

    /**
     * 功能描述:
     * 〈规格条件查询〉
     *
     * @param params 查询条件
     * @return 返回规格信息
     * @author : 刘远杰
     */
    List<SpecEntity> findSpecList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈规格条件查询,可输入分组查询〉
     *
     * @param params 查询条件
     * @return 返回规格信息
     * @author : 刘远杰
     */
    List<SpecEntity> findSpecListByGroupName(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈查询规格关联后台分类数量〉
     *
     * @param ids 规格id
     * @return 返回规格信息
     * @author : 刘远杰
     */
    List<SpecClassDTO> countClass(Long[] ids);

    /**
     * 功能描述:
     * 〈批量查询规格名称〉
     *
     * @param ids 查询的规格ids
     * @return 返回规格名称
     * @author : 刘远杰
     */
    List<SpecIdAndNameDTO> selectNameBatchByids(Long[] ids);

}
