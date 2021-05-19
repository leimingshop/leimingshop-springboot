/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.AfterSaleProcessDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleLogDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleLogListDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleLogSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 售后日志
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */

public interface AftersaleLogService {
    /**
     * 保存售后日志记录
     *
     * @param dto
     */

    void save(@RequestBody AftersaleLogSaveDTO dto);

    /**
     * 修改售后日志记录
     *
     * @param dto
     */

    void update(@RequestBody AftersaleLogDTO dto);

    /**
     * 删除售后日志记录
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID查询售后日志记录
     *
     * @param serviceSn
     * @return
     */

    AftersaleLogDTO get(Long serviceSn);

    /**
     * 查询所有的售后日志记录列表
     *
     * @param params 查询条件
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleLogDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询所有的售后日志记录列表
     *
     * @param aftersaleSn 售后单号
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleLogDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleLogListDTO> listLog(Long aftersaleSn);

    /**
     * 分页查询所有的售后日志记录列表
     *
     * @param params 查询条件
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleLogDTO>   data
     * @author huangkeyuan
     * @date 2020年09月21日
     */

    PageData<AftersaleLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * H5端售后进度
     *
     * @param aftersaleSn :售后单号
     * @param sort        :排序
     * @return H5售后进度
     * @author xuzhch
     * @date 2020年09月21日
     */

    AfterSaleProcessDTO findProcess(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("sort") String sort);

    /**
     * 查询日志状态
     *
     * @param aftersaleSn :售后单号
     * @param value       当前售后状态
     * @return 售后日志
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleLogDTO findlogByStatus(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("value") Integer value);
}
