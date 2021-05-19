/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.excel.ImportErrorExcel;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 导入导出管理
 *
 * @author 刘远杰
 * @since v1.0.0 2019-11-14
 */

public interface SysExportManagementService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     * @author 刘远杰
     * @date 2019-11-14
     */

    PageData<SysExportManagementVO> page(@RequestParam Map<String, Object> params);

    /**
     * 获取结果集
     *
     * @param params
     * @return
     * @author 刘远杰
     * @date 2019-11-14
     */

    List<SysExportManagementVO> list(@RequestParam Map<String, Object> params);

    /**
     * 获取实体信息
     *
     * @param id
     * @return
     * @author 刘远杰
     * @date 2019-11-14
     */

    SysExportManagementVO get(Long id);

    /**
     * 保存
     *
     * @param dto
     * @author 刘远杰
     * @date 2019-11-14
     */

    SysExportManagementVO save(@RequestBody SysExportManagementDTO dto);

    /**
     * 批量插入
     *
     * @param dtos
     * @author 刘远杰
     * @date 2019-11-14
     */

    void saveBach(@RequestBody List<SysExportManagementDTO> dtos);

    /**
     * 修改
     *
     * @param dto
     * @author 刘远杰
     * @date 2019-11-14
     */

    Boolean update(@RequestBody SysExportManagementDTO dto);

    /**
     * 批量修改
     *
     * @param dtos
     * @author 刘远杰
     * @date 2019-11-14
     */

    void updateBach(@RequestBody List<SysExportManagementDTO> dtos);

    /**
     * 删除
     *
     * @param ids
     * @author 刘远杰
     * @date 2019-11-14
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 查询导入失败数据
     *
     * @param sysManagerId 导入导出管理id
     * @return
     */

    List<ImportErrorExcel> errorExportList(@RequestParam(value = "sysManagerId") Long sysManagerId);
}
