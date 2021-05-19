/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.index;


import com.leimingtech.dto.index.IndexBaseDataDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


public interface IndexService {

    /**
     * 管理端基础概况
     *
     * @param params
     * @return
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/

    IndexBaseDataDTO baseManage(@RequestParam Map<String, Object> params);
}