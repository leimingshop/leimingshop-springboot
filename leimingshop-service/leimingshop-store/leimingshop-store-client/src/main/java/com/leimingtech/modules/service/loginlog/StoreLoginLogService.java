/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.loginlog;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.loginlog.StoreLoginLogDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 店铺登录日志
 * @Date :2019/6/28 10:24
 * @Version V1.0
 **/

public interface StoreLoginLogService {
    /**
     * 查询登陆日志
     *
     * @param params 查询条件
     * @return 返回登陆日志信息
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/6/28 10:31
     * @Version V1.0
     **/

    PageData<StoreLoginLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 导出登陆日志
     *
     * @param params 查询条件
     * @return 返回日志数据
     * @Author: weixianchun
     * @Description: 导出日志
     * @Date :2019/6/28 10:31
     * @Version V1.0
     **/

    List<StoreLoginLogDTO> list(@RequestParam Map<String, Object> params);

}
