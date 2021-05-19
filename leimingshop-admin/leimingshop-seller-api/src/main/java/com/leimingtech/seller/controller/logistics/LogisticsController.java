/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.logistics;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.transport.TransportCompanyDTO;
import com.leimingtech.modules.service.transport.TransportCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/8/13 20:01
 * @Version: V1.0
 */
@RestController
@RequestMapping("logistics")
@Api(tags = "物流管理")
@Slf4j
public class LogisticsController {

    @Autowired
    private TransportCompanyService transportCompanyService;

    @GetMapping("company")
    @ApiOperation("物流公司信息")
    public Result<List<TransportCompanyDTO>> company() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("id", "");
        List<TransportCompanyDTO> list = transportCompanyService.list(params);
        return new Result<List<TransportCompanyDTO>>().ok(list);
    }

}
