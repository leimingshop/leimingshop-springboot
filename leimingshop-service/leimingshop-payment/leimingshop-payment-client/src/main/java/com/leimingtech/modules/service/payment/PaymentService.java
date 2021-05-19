/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment;


import com.leimingtech.modules.dto.payment.PaymentConfigUpdateDTO;
import com.leimingtech.modules.dto.payment.PaymentDTO;
import com.leimingtech.modules.dto.payment.PaymentFindDTO;
import com.leimingtech.modules.dto.payment.alipay.*;
import com.leimingtech.modules.dto.payment.apple.ApplePayDTO;
import com.leimingtech.modules.dto.payment.apple.AppleRechargeDTO;
import com.leimingtech.modules.dto.payment.wechat.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 支付方式Service
 * @Date :2019/5/20 14:44
 * @Version V1.0
 **/

public interface PaymentService {

    /**
     * @Author: weixianchun
     * @Description: 修改支付方式信息
     * @Date :2019/5/29 14:47
     * @Param dto 实体
     * @Version V1.0
     **/

    void update(@RequestBody PaymentDTO dto);

    /**
     * @Author: weixianchun
     * @Description: 根据id查询支付方式
     * @Date :2019/5/29 14:47
     * @Param id 支付方式id
     * @Version V1.0
     **/

    PaymentDTO get(Long id);

    /**
     * 功能描述:
     * 〈根据支付方式代码查询支付方式〉
     *
     * @param paymentCode 支付方式代码
     * @author : 刘远杰
     */

    PaymentDTO getByPaymentCode(String paymentCode);

    /**
     * @Author: weixianchun
     * @Description: 查询所有支付方式信息
     * @Date :2019/5/29 14:50
     * @Param params 查询条件
     * @Version V1.0
     **/

    List<PaymentDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈h5支付方式列表数据〉
     *
     * @param
     * @author : 刘远杰
     */
//
//    List<PaymentDTO> listH5();

    /**
     * @Author: weixianchun
     * @Description: 修改支付方式信息(页面保存)
     * @Date :2019/6/12 23:14
     * @Param dto 实体
     * @Version V1.0
     **/

    void updateConfig(@RequestBody PaymentConfigUpdateDTO dto);

    /**
     * @Author: weixianchun
     * @Description: 根据id查询页面支付方式信息
     * @Date :2019/5/29 14:47
     * @Param id 支付方式id
     * @Version V1.0
     **/

    PaymentFindDTO findById(Long id);

    /**
     * 功能描述:
     * 〈微信统一下单接口，生成微信预支付订单〉
     *
     * @param
     * @author : 刘远杰
     */

    ApiWXPayResponseDTO wechatPay(@RequestBody ApiWXPayDTO apiWXPayDTO);

    /**
     * PC端微信支付
     *
     * @param apiWXPayDTO: 支付参数
     * @return: pc支付返回参数
     * @date 2020/5/19 21:57
     * @author lixiangx@leimingtech.com
     **/

    PCPayResponseDTO pcWechatPay(@RequestBody ApiWXPayDTO apiWXPayDTO);

    /**
     * 功能描述:
     * 〈微信统一下单接口，生成微信预支付订单〉
     *
     * @param
     * @author : 刘远杰
     */

    AppApiWXPayResponseDTO wechatAppPay(@RequestBody AppApiWXPayDTO appWXPayDTO);

    /**
     * 功能描述:
     * 〈微信支付回调，修改订单支付状态〉
     *
     * @author : 刘远杰
     */

    H5WXPayNotifyDTO updateorderstate(@RequestParam Map<String, String> params);

    /**
     * @param paySn:
     * @return com.leimingtech.modules.dto.payment.wechat.H5WXQueryOrderResponseDTO
     * @Description 查询微信支付结果
     * @Author huangkeyuan
     * @Date 20:12 2019-12-05
     */

    H5WXQueryOrderResponseDTO orderquery(Long paySn) throws Exception;

    /**
     * app支付时，前端调用该接口查询支付结果
     *
     * @param paySn 支付单号
     * @return
     * @date 2020-01-02 15:05
     * @author
     **/

    AppWXQueryOrderResponseDTO appOrderQuery(Long paySn) throws Exception;

    /**
     * @param aliPayH5RequestDTO: 支付单号和类型
     * @return java.lang.String
     * @Description 支付宝支付统一下单接口(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:31 2019-12-09
     */

    AliPayH5ResponseDTO aliPayCertUnifiedOrder(@RequestBody AliPayH5RequestDTO aliPayH5RequestDTO);

    /**
     * @param aliPayAppReqestDTO : 支付单号和类型
     * @return java.lang.String
     * @Description PC支付宝支付统一下单接口(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:31 2019-12-09
     */

    AliPayAppResponseDTO aliPayPcCertUnifiedOrder(@RequestBody AliPayAppReqestDTO aliPayAppReqestDTO);

    /**
     * @param requestParams: 回调参数
     * @return java.lang.String
     * @Description 支付宝回调(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:34 2019-12-09
     */

    String aliPayCertNotify(@RequestParam Map<String, String> requestParams);

    /**
     * 功能描述: 支付宝app支付下单接口(公钥证书方式)
     *
     * @param: [aliPayAppReqestDTO]
     * @return: com.leimingtech.modules.dto.payment.alipay.AliPayAppResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/3 18:45
     */

    public AliPayAppResponseDTO aliPayAPPCertUnifiedOrder(@RequestBody AliPayAppReqestDTO aliPayAppReqestDTO);


    /**
     * @param requestParams : 回调参数
     * @return java.lang.String
     * @Description app支付支付宝回调(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:34 2019-12-09
     */

    public String appAliPayCertNotify(@RequestParam Map<String, String> requestParams);

    /**
     * 完成支付
     *
     * @param h5WXPayNotifyDTO 支付参数
     * @param transactionId    交易id
     * @param paymentCode      支付码
     * @return
     * @date 2020-07-08 10:15
     * @author huangkeyuan@leimingtech.com
     **/

    void finishPay(@RequestBody() H5WXPayNotifyDTO h5WXPayNotifyDTO, @RequestParam("transactionId") String transactionId, @RequestParam("paymentCode") String paymentCode);

    /**
     * 支付宝h5充值
     *
     * @param aliPayRechargeDTO
     * @return
     * @date 2020-10-26 15:24
     * @author huangkeyuan@leimingtech.com
     **/

    AliPayH5ResponseDTO aliPayRecharge(@RequestBody AliPayRechargeDTO aliPayRechargeDTO);

    /**
     * APP支付宝充值
     *
     * @param aliPayRechargeDTO
     * @return
     * @date 2020-10-26 15:24
     * @author huangkeyuan@leimingtech.com
     **/

    AliPayAppResponseDTO appAliPayRecharge(@RequestBody AliPayRechargeDTO aliPayRechargeDTO);

    /**
     * 微信h5充值
     *
     * @param wechatRechargeDTO
     * @return
     * @date 2020-10-27 13:46
     * @author huangkeyuan@leimingtech.com
     **/

    ApiWXPayResponseDTO wechatRecharge(@RequestBody WechatRechargeDTO wechatRechargeDTO);


    /**
     * 苹果内购充值
     *
     * @param appleRechargeDTO
     * @return paySn
     * @date 2020-11-23 15:24
     * @author kuangweiguo@leimingtech.com
     **/

    String applePayRecharge(@RequestBody AppleRechargeDTO appleRechargeDTO);

    /**
     * 苹果内购充值
     *
     * @param applePayDTO
     * @return ApplePayResponseDTO
     * @date 2020-11-23 15:24
     * @author kuangweiguo@leimingtech.com
     **/

    Boolean applePayVerify(@RequestBody ApplePayDTO applePayDTO);

}
