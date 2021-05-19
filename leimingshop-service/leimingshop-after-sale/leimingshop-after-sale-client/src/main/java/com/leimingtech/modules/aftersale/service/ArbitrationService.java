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
 * 仲裁表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */

public interface ArbitrationService {
    /**
     * 查询仲裁分页列表
     *
     * @param params 查询条件
     * @return 分页数据 data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<ArbitrationPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询仲裁列表
     *
     * @param params 查询条件
     * @return 分页数据 data
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<ArbitrationDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询仲裁信息
     *
     * @param id 仲裁ID
     * @return 分页数据 data
     * @author xuzhch
     * @date 2020年09月21日
     */

    ArbitrationDTO get(Long id);

    /**
     * 保存仲裁信息
     *
     * @param dto 仲裁信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void save(@RequestBody ArbitrationDTO dto);

    /**
     * 修改仲裁信息
     *
     * @param dto 仲裁信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void update(@RequestBody ArbitrationDTO dto);

    /**
     * 删除仲裁信息
     *
     * @param ids 仲裁记录ID数组
     * @author xuzhch
     * @date 2020年09月21日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * H5用户申请仲裁
     *
     * @param dto ApplyArbitrationDTO 申请仲裁对象
     * @date 2020/4/8/008 16:30
     * @author xuzhch
     */

    void applyArbitration(@RequestBody ApplyArbitrationDTO dto);

    /**
     * H5查看仲裁进度
     *
     * @param aftersaleSn 售后单号
     * @return UserDetailArbitrationDTO 仲裁信息
     * @author xuzhch
     * @date 2020 /4/8/008 16:48
     */

    UserDetailArbitrationDTO userArbitrationDetail(Long aftersaleSn);

    /**
     * 查看仲裁详情
     *
     * @param id 仲裁信息ID
     * @return ArbitrationDetailDTO 返回详情
     * @author xuzhch
     * @date 2020 /4/9/009 10:26
     */

    ArbitrationDetailDTO arbitrationDetail(Long id);

    /**
     * 保存平台仲裁信息
     *
     * @param auditArbitrationDTO 保存审核对象
     * @author xuzhch
     * @date 2020 /4/9/009 10:25
     */

    void audit(@RequestBody AuditArbitrationDTO auditArbitrationDTO);

    /**
     * 获取仲裁详情
     *
     * @param aftersaleSn 售后单号
     * @return 仲裁信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    ArbitrationDTO getDataByAfterSn(Long aftersaleSn);

    /**
     * 仲裁记录导出
     *
     * @param params 导出条件
     * @return List<ArbitrationPageDTO>  查询结果
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<ArbitrationPageDTO> exportList(@RequestParam Map<String, Object> params);
}
