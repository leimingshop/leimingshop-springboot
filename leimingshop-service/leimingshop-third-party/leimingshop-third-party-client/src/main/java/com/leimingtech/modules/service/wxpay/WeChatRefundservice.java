/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wxpay;

import com.leimingtech.modules.dto.wxpay.H5WxRefundDTO;
import com.leimingtech.modules.dto.wxpay.H5WxRefundResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:微信退款服务
 * @Date: 2019/6/22 15:02
 * @Version: V1.0
 */

public interface WeChatRefundservice {


    @ApiOperation("微信退款")
    H5WxRefundResponseDTO refundWX(@RequestBody H5WxRefundDTO h5WxRefundDTO);

}
