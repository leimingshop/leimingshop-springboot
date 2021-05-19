/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.sys;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.sys.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 用户管理DAO
 * @Date :2019/5/28 13:42
 * @Version V1.0
 **/
@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {

    /**
     * @Author: weixianchun
     * @Description: 查询所有用户
     * @Date :2019/5/28 13:43
     * @Param params
     * @Version V1.0
     **/
    List<SysUserEntity> getList(Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 根据id查询用户信息
     * @Date :2019/5/28 13:44
     * @Param id
     * @Version V1.0
     **/
    SysUserEntity getById(Long id);

    /**
     * @Author: weixianchun
     * @Description: 根据用户名查询用户信息
     * @Date :2019/5/28 13:44
     * @Param username
     * @Version V1.0
     **/
    SysUserEntity getByUsername(String username);

    /**
     * @Author: weixianchun
     * @Description: 修改密码
     * @Date :2019/5/28 13:45
     * @Param id
     * @Param newPassword
     * @Version V1.0
     **/
    int updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);

    /**
     * @Author: weixianchun
     * @Description: 根据部门ID，查询用户数
     * @Date :2019/5/28 13:45
     * @Param deptId
     * @Version V1.0
     **/
    int getCountByDeptId(Long deptId);

}