/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.log;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.log.MemberLoginLogDTO;
import com.leimingtech.modules.dto.log.MemberLoginLogExcelDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户登录日志表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-30
 */

public interface MemberLoginLogService {

    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<MemberLoginLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询列表
     *
     * @param params
     * @return
     */

    List<MemberLoginLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */

    MemberLoginLogDTO get(Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody MemberLoginLogDTO dto);

    /**
     * 修改
     *
     * @param dto
     */

    void update(@RequestBody MemberLoginLogDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出用户登录日志
     *
     * @param params 查询条件
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberLoginLogExcelDTO> export(@RequestParam Map<String, Object> params);
}
