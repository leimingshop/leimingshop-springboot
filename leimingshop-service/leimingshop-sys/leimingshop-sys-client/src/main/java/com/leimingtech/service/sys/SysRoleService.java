/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.SysRoleDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @since 1.0.0
 */

public interface SysRoleService {

    PageData<SysRoleDTO> page(@RequestParam Map<String, Object> params);


    List<SysRoleDTO> list(@RequestParam Map<String, Object> params);


    SysRoleDTO get(Long id);


    void save(@RequestBody SysRoleDTO dto);


    void update(@RequestBody SysRoleDTO dto);


    void delete(@RequestBody Long[] ids);


    /**
     * 校验角色名称是否唯一
     *
     * @param name 角色名称
     * @return 是或者否
     * @date 2019/7/4 15:48
     * @author lixiang
     **/

    Boolean checkName(@RequestParam("name") String name);


}