/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.SysLogOperationDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 操作日志
 *
 * @since 1.0.0
 */

public interface SysLogOperationService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<SysLogOperationDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 日志信息列表
     *
     * @param params
     * @return
     */

    List<SysLogOperationDTO> list(@RequestParam Map<String, Object> params);

}
