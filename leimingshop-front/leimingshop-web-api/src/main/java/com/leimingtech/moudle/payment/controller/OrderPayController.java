/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.payment.controller;

import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.payment.OrderToPayDTO;
import com.leimingtech.modules.dto.payment.PaymentDTO;
import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.modules.service.payment.PaymentService;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <订单支付管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/14
 */
@Slf4j
@Api(tags = "订单支付管理")
@RestController
@RequestMapping("payment/pay")
public class OrderPayController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(OrderPayController.class);

    @Autowired
    private OrderPayService orderPayService;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("topay/{paySn}")
    @ApiOperation("待付款订单去支付")
    public Result<OrderToPayDTO> payOrder(@PathVariable("paySn") Long paySn) {
        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<OrderToPayDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        Map<String, String> logMap = new HashMap<>(3);
        logMap.put("paySn", paySn.toString());
        logMap.put("buyerId", buyerId.toString());
        log.info("获取待付款订单信息，paySn:{},buyerId:{}", paySn, buyerId);
        OrderToPayDTO orderToPayDTO = orderPayService.payOrder(paySn, buyerId);
        orderToPayDTO.setCurrentTime(System.currentTimeMillis());

        if (orderToPayDTO != null) {
            mlogger.info(PaymentStatusCode.H5_ORDER_PAY_SUCCESS_CODE, PaymentStatusCode.H5_ORDER_PAY_SUCCESS_MESSAGE, logMap);
            return new Result<OrderToPayDTO>().ok(orderToPayDTO, "获取支付信息成功");
        } else {
            return new Result<OrderToPayDTO>().error("服务器内部异常");
        }
    }

    /**
     * 查询支付方式
     *
     * @return
     */
    @GetMapping("type/{clientType}")
    @ApiOperation("查询支付方式 客户端类型（WEB、WAP、APP、MINI）")
    public Result<List<PaymentDTO>> list(@PathVariable("clientType") String clientType) {
        Map<String, Object> map = new HashMap<>(5);
        map.put("clientType", clientType);
        List<PaymentDTO> list = paymentService.list(map);
        return new Result<List<PaymentDTO>>().ok(list);
    }

}
