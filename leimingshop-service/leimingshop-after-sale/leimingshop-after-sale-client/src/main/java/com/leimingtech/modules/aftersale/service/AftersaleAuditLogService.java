/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 售后审核记录
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */

public interface AftersaleAuditLogService {
    /**
     * 保存售后审核记录
     *
     * @param dto 售后审核记录
     * @author xuzhch
     * @date 2020年09月21日
     */

    void save(@RequestBody AftersaleAuditLogSaveDTO dto);

    /**
     * 修改售后审核记录
     *
     * @param dto 售后审核记录
     * @author xuzhch
     * @date 2020年09月21日
     */

    void update(@RequestBody AftersaleAuditLogDTO dto);

    /**
     * 删除售后审核记录
     *
     * @param ids 售后审核记录ID数组
     * @author xuzhch
     * @date 2020年09月21日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID查询售后审核记录
     *
     * @param id 售后审核记录ID
     * @return audit log dto
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleAuditLogDTO get(Long id);

    /**
     * 查询所有的售后审核记录列表
     *
     * @param params 查询条件
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleAuditLogDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleAuditLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询所有的售后审核记录列表
     *
     * @param aftersaleSn 售后单号
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleAuditLogDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleLogListDTO> listLog(Long aftersaleSn);

    /**
     * 分页查询所有的售后审核记录列表
     *
     * @param params  查询条件
     * @param storeId 店铺ID
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleAuditLogDTO> data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<AftersaleAuditLogPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 导出审核列表
     *
     * @param params 查询条件
     * @return list 售后审核日志分页列表
     * @author xuzhch
     * @date 2020年09月21日
     * @Author weixianchun
     */

    List<AftersaleAuditLogPageDTO> findListExport(@RequestParam Map<String, Object> params);

    /**
     * 查询售后审核详情
     *
     * @param id 售后审核记录ID
     * @return 售后审核详情
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleAuditDetailDTO detail(Long id);

    /**
     * 取消申请
     *
     * @param aftersaleSn 售后单号
     * @author xuzhch
     * @date 2020年09月21日
     */

    void cancelAuditing(@RequestParam("aftersaleSn") Long aftersaleSn);
}
