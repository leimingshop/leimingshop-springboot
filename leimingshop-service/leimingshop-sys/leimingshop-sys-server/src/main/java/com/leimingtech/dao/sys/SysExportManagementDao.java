/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.sys;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.excel.ImportErrorExcel;
import com.leimingtech.entity.sys.SysExportManagementPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导入导出管理
 *
 * @author 刘远杰
 * @since v1.0.0 2019-11-14
 */
@Mapper
public interface SysExportManagementDao extends BaseDao<SysExportManagementPO> {

    /**
     * 批量插入
     *
     * @param list
     */

    void saveBach(@Param("list") List<SysExportManagementPO> list);

    /**
     * 批量修改
     *
     * @param list
     */
    void updateBach(@Param("list") List<SysExportManagementPO> list);

    /**
     * 查询导入失败数据
     *
     * @param sysManagerId 导入导出管理id
     * @return
     */
    List<ImportErrorExcel> errorExportListById(@Param("sysManagerId") Long sysManagerId);
}
