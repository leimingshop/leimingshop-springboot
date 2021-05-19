/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.dto.SysDeptDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @since 1.0.0
 */

public interface SysDeptService {
    /**
     * 查询部门列表
     *
     * @param params 查询参数
     * @return
     */

    List<SysDeptDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据与ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    SysDeptDTO get(Long id);

    /**
     * 新增部门信息
     *
     * @param dto 部门实体
     */

    void save(@RequestBody SysDeptDTO dto);

    /**
     * 修改部门信息
     *
     * @param dto 部门实体类
     */

    void update(@RequestBody SysDeptDTO dto);

    /**
     * 删除部门信息
     *
     * @param id 主键ID
     */

    void delete(Long id);

    /**
     * 根据部门ID，获取本部门及子部门ID列表
     *
     * @param id 部门ID
     */

    List<Long> getSubDeptIdList(@RequestParam("id") Long id);
}