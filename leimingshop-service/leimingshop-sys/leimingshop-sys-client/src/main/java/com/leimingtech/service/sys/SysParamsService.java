/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.SysParamsDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 参数管理
 *
 * @since 1.0.0
 */

public interface SysParamsService {

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据page查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */

    PageData<SysParamsDTO> page(@RequestParam Map<String, Object> params);

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据list查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */

    List<SysParamsDTO> list(@RequestParam Map<String, Object> params);

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据id查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */

    SysParamsDTO get(@RequestParam("id") Long id);

    /**
     * @return void
     * @Description 保存参数信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:05 2019-05-27
     */

    void save(@RequestBody SysParamsDTO dto);

    /**
     * @return void
     * @Description 修改参数信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:07 2019-05-27
     */

    void update(@RequestBody SysParamsDTO dto);

    /**
     * @return void
     * @Description 删除参数信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 15:09 2019-05-27
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据参数编码，获取参数的value值
     *
     * @param paramCode 参数编码
     */

    String getValue(String paramCode);

    /**
     * 根据参数编码，更新value
     *
     * @param paramCode  参数编码
     * @param paramValue 参数值
     */

    int updateValueByCode(String paramCode,
                          String paramValue);
}
