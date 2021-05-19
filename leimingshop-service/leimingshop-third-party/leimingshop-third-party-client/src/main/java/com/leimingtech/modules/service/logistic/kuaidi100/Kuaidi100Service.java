/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.logistic.kuaidi100;

import com.leimingtech.modules.dto.logistic.kuaidi100.RealTimeQueryDTO;
import com.leimingtech.modules.dto.logistic.kuaidi100.RealTimeQueryResponseDTO;
import com.leimingtech.modules.dto.logistic.kuaidi100.SubsrcibeRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


public interface Kuaidi100Service {

    /**
     * @param realTimeQueryDTO: 参数实体
     * @Author: SWH ab4856812@163.com
     * @Description: 快递100实时查询快递信息接口
     * @Date: 2019/7/31 10:12
     * @Version: V1.0
     */

    @ApiOperation("实时查询快递信息")
    RealTimeQueryResponseDTO realtime(@RequestBody RealTimeQueryDTO realTimeQueryDTO);

    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 快递100实订阅快递信息
     * @Date: 2019/7/31 15:59
     * @Version: V1.0
     */

    @ApiOperation("订阅快递信息")
    Map<String, Object> subsrcibe(@RequestBody SubsrcibeRequestDTO subsrcibeRequestDTO);
}
