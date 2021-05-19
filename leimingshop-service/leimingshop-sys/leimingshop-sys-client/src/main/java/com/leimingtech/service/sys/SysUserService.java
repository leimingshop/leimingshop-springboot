/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.sys.SysUserDTO;
import com.leimingtech.dto.sys.SysUserDetailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 用户管理service
 * @Date :2019/5/28 13:47
 * @Version V1.0
 **/

public interface SysUserService {

    /**
     * @Author: weixianchun
     * @Description: 根据条件查询并分页
     * @Date :2019/5/27 17:27
     * @Param params 可变参数
     * @Version V1.0
     **/

    PageData<SysUserDTO> page(@RequestParam Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 查询所有用户信息
     * @Date :2019/5/28 12:07
     * @Param params 可变参数
     * @Version V1.0
     **/

    List<SysUserDTO> list(@RequestParam Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 根据id查询信息
     * @Date :2019/5/28 11:59
     * @Param id
     * @Version V1.0
     **/

    SysUserDTO get(Long id);

    /**
     * @Author: weixianchun
     * @Description: 根据用户名查询信息
     * @Date :2019/5/28 11:59
     * @Param username
     * @Version V1.0
     **/

    SysUserDTO getByUsername(@RequestParam("username") String username);

    /**
     * @Author: weixianchun
     * @Description: 保存用户信息
     * @Date :2019/5/28 11:58
     * @Param dto
     * @Version V1.0
     **/

    void save(@RequestBody SysUserDTO dto);

    /**
     * @Author: weixianchun
     * @Description: 修改用户信息
     * @Date :2019/5/28 11:57
     * @Param dto
     * @Version V1.0
     **/

    void update(@RequestBody SysUserDetailDTO dto);

    /**
     * @Author: weixianchun
     * @Description: 删除用户信息(逻辑删除)
     * @Date :2019/5/28 11:56
     * @Param ids
     * @Version V1.0
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * @Author: weixianchun
     * @Description: 修改密码
     * @Date :2019/5/28 11:53
     * @Param id 主键
     * @Param newPassword 新密码
     * @Version V1.0
     **/

    void updatePassword(@RequestParam("id") Long id, @RequestParam("newPassword") String newPassword);

    /**
     * @Author: weixianchun
     * @Description: 根据部门ID，查询用户数
     * @Date :2019/5/28 11:54
     * @Param deptId 部门id
     * @Version V1.0
     **/

    int getCountByDeptId(Long deptId);

    /**
     * 重置密码
     *
     * @param id: 管理员ID
     * @return 重置结果
     * @date 2019/8/8 15:27
     * @author lixiang
     **/

    boolean resetPassword(@RequestParam("id") Long id);

}