/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment;


import com.leimingtech.modules.dto.payment.H5WxRefundBaseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:退款服务
 * @Date: 2019/6/23 15:51
 * @Version: V1.0
 */

@Api
public interface RefundService {

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:微信退款
     * @Date: 2019/6/23 15:52
     * @Version: V1.0
     */
    @ApiOperation("微信退款")
    Map<String, Object> wechatRefund(@RequestBody H5WxRefundBaseDTO H5WxRefundBaseDTO);
}
