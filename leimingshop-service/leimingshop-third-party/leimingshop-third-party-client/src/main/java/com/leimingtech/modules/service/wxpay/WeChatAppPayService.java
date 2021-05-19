/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wxpay;

import com.leimingtech.modules.dto.wxpay.AppWXPayDTO;
import com.leimingtech.modules.dto.wxpay.AppWXPayResponseDTO;
import com.leimingtech.modules.dto.wxpay.AppWxRefundDTO;
import com.leimingtech.modules.dto.wxpay.AppWxRefundResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangtai
 * @date 2020/4/7 11:29
 * @Description:
 */

public interface WeChatAppPayService {

    /**
     * 功能描述: 微信统一下单接口
     *
     * @param: [params]
     * @return: java.lang.String
     * @auther: zhangtai
     * @date: 2020/4/7 11:54
     */

    String unifiedOrder(@RequestParam("params") String params);

    /**
     * 功能描述: 微信app预支付
     *
     * @param: [appWXPayDTO]
     * @return: com.leimingtech.modules.dto.wxpay.AppWXPayResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/7 17:00
     */

    AppWXPayResponseDTO payWX(@RequestBody AppWXPayDTO appWXPayDTO) throws Exception;

    /**
     * @param params:
     * @return java.lang.String
     * @Description 查询微信支付结果
     * @Author huangkeyuan
     * @Date 20:24 2019-12-05
     */

    String orderquery(@RequestParam("params") String params) throws Exception;

    /**
     * 功能描述: app微信支付退款
     *
     * @param: [appWxRefundDTO]
     * @return: com.leimingtech.modules.dto.wxpay.AppWxRefundResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/8 11:00
     */

    AppWxRefundResponseDTO orderRefund(@RequestBody AppWxRefundDTO appWxRefundDTO);


    Boolean isAppPay(@RequestParam("appId") String appId);


}
