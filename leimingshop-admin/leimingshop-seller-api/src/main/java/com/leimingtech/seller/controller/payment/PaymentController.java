/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.payment;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.payment.PaymentDTO;
import com.leimingtech.modules.service.payment.PaymentService;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 支付方式管理PaymentController
 * @Date :2019/5/20 14:38
 * @Version V1.0
 **/
@RestController
@RequestMapping("payment")
@Api(tags = "支付方式管理")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * @Author: weixianchun
     * @Description: 查询所有支付信息
     * @Date :2019/6/3 16:38
     * @Param params
     * @Version V1.0
     **/
    @GetMapping("list")
    @ApiOperation("查询所有支付分组")
    @LogOperation("查询所有支付分组")
    @LogContext(code = PaymentStatusCode.SELLER_PAYMENT_FIND_SUCCESS_CODE, note = PaymentStatusCode.SELLER_PAYMENT_FIND_SUCCESS_MESSAGE)
    public Result<List<PaymentDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("type", "seller");
        List<PaymentDTO> list = paymentService.list(params);

        return new Result<List<PaymentDTO>>().ok(list, "查询成功");
    }
}
