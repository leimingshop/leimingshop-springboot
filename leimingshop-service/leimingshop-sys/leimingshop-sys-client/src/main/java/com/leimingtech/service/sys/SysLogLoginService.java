/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.SysLogLoginDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 登录日志
 *
 * @since 1.0.0
 */

public interface SysLogLoginService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<SysLogLoginDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 导出日志信息
     *
     * @param params
     * @return
     */

    List<SysLogLoginDTO> list(@RequestParam Map<String, Object> params);

}