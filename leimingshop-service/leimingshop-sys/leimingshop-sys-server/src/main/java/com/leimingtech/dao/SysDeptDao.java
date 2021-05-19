/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @since 1.0.0
 */
@Mapper
public interface SysDeptDao extends BaseDao<SysDeptEntity> {

    List<SysDeptEntity> getList(Map<String, Object> params);

    SysDeptEntity getById(Long id);

    /**
     * 获取所有部门的id、pid列表
     */
    List<SysDeptEntity> getIdAndPidList();

    /**
     * 根据部门ID，获取所有子部门ID列表
     *
     * @param id 部门ID
     */
    List<Long> getSubDeptIdList(String id);
}