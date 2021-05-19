/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.SysRoleDTO;
import com.leimingtech.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @since 1.0.0
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {

    /**
     * @Author: weixianchun
     * @Description: 查询角色数量及信息, 并实现角色名称模糊查询
     * @Param params 查询条件
     * @Return: java.util.List<com.leimingtech.dto.SysRoleDTO>
     * @Date: 2019/8/13 12:57
     * @Version 1.0
     */
    List<SysRoleDTO> selectCountInfo(Map<String, Object> params);

}
