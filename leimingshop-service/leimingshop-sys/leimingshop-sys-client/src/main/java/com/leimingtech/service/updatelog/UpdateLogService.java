/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.updatelog;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.updatelog.UpdateLogDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 版本更新日志
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-09
 */

public interface UpdateLogService {


    PageData<UpdateLogDTO> page(@RequestParam Map<String, Object> params);


    List<UpdateLogDTO> list(@RequestParam Map<String, Object> params);


    UpdateLogDTO get(Long id);


    void save(@RequestBody UpdateLogDTO dto);


    void update(@RequestBody UpdateLogDTO dto);


    void delete(@RequestBody Long[] ids);
}