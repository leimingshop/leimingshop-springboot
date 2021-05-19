/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.alipay;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.dto.alipay.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 功能描述：
 * <微信支付>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/5/30 16:29
 **/

public interface AliPayService {

    /**
     * @param h5AliPayRequestDTO: 支付宝支付所需参数
     * @return com.leimingtech.modules.dto.alipay.H5AlipayResponseDTO
     * @Description 创建订单并支付接口
     * @Author huangkeyuan
     * @Date 10:21 2019-12-10
     */

    H5AlipayResponseDTO aliPayCertUnifiedOrder(@RequestBody H5AliPayRequestDTO h5AliPayRequestDTO);


    /**
     * @param pcAliPayRequestDTO: 支付宝支付所需参数
     * @return com.leimingtech.modules.dto.alipay.PcAlipayResponseDTO
     * @Description 创建订单并支付接口
     * @Author huangkeyuan
     * @Date 10:21 2020-5-20
     */

    PcAlipayResponseDTO aliPayPagePay(@RequestBody PcAliPayRequestDTO pcAliPayRequestDTO);

    /**
     * @param requestParams:
     * @return boolean
     * @Description 收到支付宝的成功回调之后验证签名是否正确
     * @Author huangkeyuan
     * @Date 17:01 2019-12-12
     */

    boolean checkSignature(@RequestParam Map<String, String> requestParams);

    /**
     * @param alipayRefundRequestDTO:
     * @return java.lang.String
     * @Description 支付宝退款接口
     * @Author huangkeyuan
     * @Date 20:24 2019-12-12
     */

    String alipayRefund(@RequestBody AlipayRefundRequestDTO alipayRefundRequestDTO);

    /**
     * 功能描述: app创建订单并支付的接口
     *
     * @param: [appAlipayRequestDTO]
     * @return: com.leimingtech.modules.dto.alipay.AppAlipayResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/3 15:56
     */

    AppAlipayResponseDTO aliPayAppPay(@RequestBody AppAlipayRequestDTO appAlipayRequestDTO);

    /**
     * @return boolean
     * @Description app支付收到支付宝的成功回调之后验证签名是否正确
     * @Author huangkeyuan
     * @Date 17:01 2019-12-12
     */

    boolean appCheckSignature(@RequestParam Map<String, String> requestParams);

    /**
     * 功能描述: 支付宝app支付的退款
     *
     * @param: [alipayRefundRequestDTO]
     * @return: java.lang.String
     * @auther: zhangtai
     * @date: 2020/4/3 17:19
     */

    String appAlipayRefund(@RequestBody AppAlipayRefundRequestDTO appAlipayRefundRequestDTO);

    /**
     * 根据银行卡号查询所属银行
     *
     * @param bankAccount 银行卡号
     * @return
     * @date 2020-10-21 15:56
     * @author huangkeyuan@leimingtech.com
     **/

    JSONObject getBankName(@RequestParam("bankAccount") String bankAccount);

}
