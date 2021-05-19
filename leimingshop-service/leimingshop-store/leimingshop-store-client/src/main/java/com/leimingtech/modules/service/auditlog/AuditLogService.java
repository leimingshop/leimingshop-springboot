/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.auditlog;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.auditlog.AuditLogDTO;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 审核记录表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-23
 */

public interface AuditLogService {
    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    PageData<AuditLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有
     *
     * @param params
     * @return
     */

    List<AuditLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */

    AuditLogDTO get(Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody AuditLogDTO dto);

    /**
     * 更新
     *
     * @param dto
     */

    void update(@RequestBody AuditLogDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 保存审核记录
     *
     * @param updateStoreStatusDTO
     */

    void saveLog(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO);

}