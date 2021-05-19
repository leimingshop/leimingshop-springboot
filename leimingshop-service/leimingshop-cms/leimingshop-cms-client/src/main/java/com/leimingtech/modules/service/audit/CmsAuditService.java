/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.audit;


import com.leimingtech.modules.dto.audit.CmsAuditDTO;
import com.leimingtech.modules.dto.audit.CmsAuditSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 审核记录管理 CmsAuditService
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2020-03-24
 */

public interface CmsAuditService {
    /**
     * 根据id查询详情
     *
     * @param id 主键id
     * @return 返回信息
     */

    CmsAuditDTO get(Long id);

    /**
     * 保存信息
     *
     * @param dto 保存参数
     */

    void save(@RequestBody CmsAuditSaveDTO dto);

}