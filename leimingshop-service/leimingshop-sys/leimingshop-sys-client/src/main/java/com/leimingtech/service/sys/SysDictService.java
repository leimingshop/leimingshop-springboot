/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.SysDictDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @since 1.0.0
 */

public interface SysDictService {
    /**
     * 分页查询
     *
     * @param params 分页参数也
     * @return
     */

    PageData<SysDictDTO> page(@RequestParam Map<String, Object> params);


    /**
     * 字典分类
     *
     * @param params 查询参数青蛙
     * @return
     */

    List<SysDictDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询信息
     *
     * @param id 主键ID
     * @return
     */

    SysDictDTO get(Long id);

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    void save(@RequestBody SysDictDTO dto);

    /**
     * 修改数据字典
     *
     * @param dto 参数实体
     */

    void update(@RequestBody SysDictDTO dto);

    /**
     * 根据ID删除数据
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);
}