/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.SysLogErrorDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 异常日志
 *
 * @since 1.0.0
 */

public interface SysLogErrorService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<SysLogErrorDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 导出日志信息
     *
     * @param params
     * @return
     */

    List<SysLogErrorDTO> list(@RequestParam Map<String, Object> params);


}