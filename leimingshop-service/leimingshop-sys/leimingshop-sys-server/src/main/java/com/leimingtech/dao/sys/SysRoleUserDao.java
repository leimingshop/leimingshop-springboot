/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.sys;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.sys.SysRoleUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 角色用户关系
 * @Date :2019/5/28 15:09
 * @Version V1.0
 **/
@Mapper
public interface SysRoleUserDao extends BaseDao<SysRoleUserEntity> {

    /**
     * 根据角色ids，删除角色用户关系
     *
     * @param roleIds 角色ids
     */
    void deleteByRoleIds(Long[] roleIds);

    /**
     * 根据用户id，删除角色用户关系
     *
     * @param userId 用户id
     */
    void deleteByUserId(Long userId);

    /**
     * 查询角色ID列表
     *
     * @param userId 用户ID
     * @return
     */
    List<Long> getRoleIdList(Long userId);

    /**
     * 查询角色下是否关联用户
     *
     * @param ids 角色ID
     * @return
     */
    Integer findUserCount(@Param("ids") Long[] ids);
}