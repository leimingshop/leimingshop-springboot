/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.ZxingUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.exception.ServiceStatusCode;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.PaymentCodeConstants;
import com.leimingtech.modules.constants.wxpay.PaymentResultCodeConstants;
import com.leimingtech.modules.dao.payment.PaymentDao;
import com.leimingtech.modules.dto.alipay.*;
import com.leimingtech.modules.dto.balance.MemberBalanceLogDTO;
import com.leimingtech.modules.dto.member.MemberBalanceInfoDTO;
import com.leimingtech.modules.dto.member.MemberUpdateDTO;
import com.leimingtech.modules.dto.order.UpdateOrderDTO;
import com.leimingtech.modules.dto.payment.*;
import com.leimingtech.modules.dto.payment.alipay.*;
import com.leimingtech.modules.dto.payment.apple.ApplePayDTO;
import com.leimingtech.modules.dto.payment.apple.AppleRechargeDTO;
import com.leimingtech.modules.dto.payment.wechat.*;
import com.leimingtech.modules.dto.wxpay.AppWXPayDTO;
import com.leimingtech.modules.dto.wxpay.AppWXPayResponseDTO;
import com.leimingtech.modules.dto.wxpay.H5WXPayDTO;
import com.leimingtech.modules.dto.wxpay.WXPayResponseDTO;
import com.leimingtech.modules.entity.payment.PaymentEntity;
import com.leimingtech.modules.enums.balance.BalanceEnum;
import com.leimingtech.modules.enums.order.PayStateEnum;
import com.leimingtech.modules.enums.payment.PaymentCodeEnum;
import com.leimingtech.modules.enums.payment.PaymentTallyEnum;
import com.leimingtech.modules.enums.payment.PaymentTypeCodeEnum;
import com.leimingtech.modules.service.alipay.AliPayService;
import com.leimingtech.modules.service.balance.MemberBalanceLogService;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.modules.service.payment.PaymentService;
import com.leimingtech.modules.service.payment.PaymentTallyService;
import com.leimingtech.modules.service.wxpay.WeChatAppPayService;
import com.leimingtech.modules.service.wxpay.WeChatPayservice;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import com.leimingtech.modules.utils.HttpUtil;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: weixianchun
 * @Description: 支付方式PaymentServiceImpl
 * @Date :2019/5/20 14:46
 * @Version V1.0
 **/
@Service

@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PaymentServiceImpl extends CrudServiceImpl<PaymentDao, PaymentEntity, PaymentDTO> implements PaymentService {

    /**
     * 提交此JSON对象作为HTTP POST请求的有效内容。
     * 在测试环境中，https://sandbox.itunes.apple.com/verifyReceipt用作URL。
     * 在生产中，https://buy.itunes.apple.com/verifyReceipt用作URL。
     */
    private static final String APP_STORE_PRIVATE_RECEIPT = "https://sandbox.itunes.apple.com/verifyReceipt"; // 苹果的测试服
    private static final String APP_STORE_PUBLIC_RECEIPT = "https://buy.itunes.apple.com/verifyReceipt"; // 苹果的生产服
    private static final String BUNDLE_ID = "com.leimingtech.shop.b2b2c";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(PaymentServiceImpl.class);
    @Autowired
    private WeChatPayservice weChatPayservice;
    @Autowired
    private WeChatAppPayService weChatAppPayService;
    @Autowired

    private PaymentTallyService paymentTallyService;
    @Autowired

    private OrderPayService orderPayService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private MemberInfoService memberInfoService;
    @Autowired
    private MemberBalanceLogService memberBalanceLogService;

    /**
     * @Author: weixianchun
     * @Description: 条件构造器
     * @Date :2019/6/24 15:07
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override
    public QueryWrapper<PaymentEntity> getWrapper(Map<String, Object> params) {

        String id = (String) params.get("id");

        QueryWrapper<PaymentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * @Author: weixianchun
     * @Description: 修改支付方式信息
     * @Date :2019/5/29 14:47
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public void update(@RequestBody PaymentDTO dto) {
        super.update(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询支付方式
     * @Date :2019/5/29 14:47
     * @Param id 支付方式id
     * @Version V1.0
     **/

    @Override
    public PaymentDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈根据支付方式代码查询支付方式〉
     *
     * @param paymentCode 支付方式代码
     * @author : 刘远杰
     */

    @Override
    public PaymentDTO getByPaymentCode(String paymentCode) {
        log.info("根据支付方式代码查询支付方式,paymentCode:{}", paymentCode);
        QueryWrapper<PaymentEntity> wrapper = new QueryWrapper<>();

        if (paymentCode.equals(PaymentCodeConstants.WECHATAPP)) {
            wrapper.eq(StringUtils.isNotBlank(paymentCode), "payment_code", PaymentCodeConstants.WECHAT);
            wrapper.eq("client_type", PaymentTypeCodeEnum.APP);
        } else if (paymentCode.equals(PaymentCodeConstants.ALIPAYAPP)) {
            wrapper.eq(StringUtils.isNotBlank(paymentCode), "payment_code", PaymentCodeConstants.ALIPAY);
            wrapper.eq("client_type", PaymentTypeCodeEnum.APP);
        } else if (paymentCode.equals(PaymentCodeConstants.BALANCE)) {
            log.info("余额支付成功，进入余额支付的回调");
            wrapper.eq(StringUtils.isNotBlank(paymentCode), "payment_code", paymentCode);
        } else {
            wrapper.eq("client_type", PaymentTypeCodeEnum.WAP);
            wrapper.eq(StringUtils.isNotBlank(paymentCode), "payment_code", paymentCode);
        }

        PaymentEntity paymentEntity = baseDao.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(paymentEntity, PaymentDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有支付方式信息
     * @Date :2019/5/29 14:50
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public List<PaymentDTO> list(@RequestParam Map<String, Object> params) {
        String clientType = (String) params.get("clientType");
        String type = (String) params.get("type");
        // memberSource对应的值 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序
        String memberSource = (String) params.get("memberSource");
        QueryWrapper<PaymentEntity> queryWrapper = new QueryWrapper<>();
        QueryWrapper<PaymentEntity> wrapper = queryWrapper.select("id", "payment_code", "payment_name",
                "payment_state", "payment_logo", "remark").orderByDesc("payment_state");

        // app、小程序、h5、pc端获取支付列表走这里
        if (StringUtils.isNotBlank(clientType)) {
            // memberSource不为空，且memberSource等于4或者5的时候表示在微信浏览器内或者微信小程序内，就不显示支付宝
            if (StringUtils.isNotBlank(memberSource) && ("4".equals(memberSource) || "5".equals(memberSource))) {
                wrapper.ne("payment_code", "ZFB");
            }

            wrapper.eq("client_type", clientType);
            wrapper.eq("payment_state", 1);
        }

        // admin端，seller端查询列表列表时获取支付方式走这里
        if (StringUtils.isNotBlank(type)) {
            wrapper.groupBy("payment_code");
        }

        List<PaymentEntity> paymentEntityList = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(paymentEntityList, PaymentDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 修改支付方式信息(页面保存)
     * @Date :2019/6/12 23:14
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public void updateConfig(@RequestBody PaymentConfigUpdateDTO dto) {

        //根据id查询
        PaymentDTO paymentDTO = super.get(dto.getId());
        //josn转为object
        PaymentConfigUpdateDTO configUpdateDTO = JSON.parseObject(paymentDTO.getPaymentConfig(), PaymentConfigUpdateDTO.class);
        //修改开启关闭状态
        paymentDTO.setPaymentState(dto.getPaymentState());
        //修改商户号
        configUpdateDTO.setPaymentAccount(dto.getPaymentAccount());
        //修改APPID
        configUpdateDTO.setAppId(dto.getAppId());
        //该字段不为空修改
        if (StringUtils.isNotBlank(dto.getSecretKey())) {
            configUpdateDTO.setSecretKey(dto.getSecretKey());
        }
        //该字段不为空修改
        if (StringUtils.isNotBlank(dto.getSecretKeyApi())) {
            configUpdateDTO.setSecretKeyApi(dto.getSecretKeyApi());
        }
        //序列化
        String jsonString = JSON.toJSONString(configUpdateDTO);
        //将转换后的json字符串放入PaymentConfig对象中
        paymentDTO.setPaymentConfig(jsonString);
        //修改(保存)支付方式信息
        super.update(paymentDTO);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询页面支付方式信息
     * @Date :2019/5/29 14:47
     * @Param id 支付方式id
     * @Version V1.0
     **/

    @Override
    public PaymentFindDTO findById(Long id) {

        PaymentDTO paymentDTO = super.get(id);
        //转换dto
        PaymentFindDTO paymentFindDTO = ConvertUtils.sourceToTarget(paymentDTO, PaymentFindDTO.class);
        //获取PaymentConfig对象
        String paymentConfig = paymentFindDTO.getPaymentConfig();

        PaymentConfigUpdateDTO configUpdateDTO = JSON.parseObject(paymentConfig, PaymentConfigUpdateDTO.class);
        //将密钥值设为空
        configUpdateDTO.setSecretKey("");
        configUpdateDTO.setSecretKeyApi("");
        //转换为json字符串
        String strConfigUpdateDTO = JSON.toJSONString(configUpdateDTO);
        //将转换后的json字符串放入paymentDTO对象中
        paymentFindDTO.setPaymentConfig(strConfigUpdateDTO);
        return paymentFindDTO;
    }

    /**
     * 功能描述:
     * 微信统一下单接口（H5和小程序）
     *
     * @param
     * @author : 刘远杰
     */

    @Override
    public ApiWXPayResponseDTO wechatPay(@RequestBody ApiWXPayDTO apiWXPayDTO) {

        WXPayResponseDTO WXPayResponseDTO = null;

        // 1.查询支付单号对应的订单支付信息
        Long paySn = Long.parseLong(apiWXPayDTO.getPaySn());
        OrderPayDTO orderPayDTO = apiWXPayDTO.getOrderPayDTO();
        if (null == orderPayDTO) {
            orderPayDTO = orderPayService.getByPaySn(paySn);
        }

        Optional.ofNullable(orderPayDTO).orElseThrow(() -> new ServiceException(PaymentStatusCode.WECHAT_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION));

        // 2.判断支付状态
        if (orderPayDTO.getPayState() != PayStateEnum.NO.value()) {
            log.error("微信预支付失败，订单支付状态异常");
            throw new ServiceException(PaymentStatusCode.WECHAT_PRE_PAY_PAYSN_STATUS_EXCEPTION);
        }

        // 3.封装微信支付参数
        // 微信支付金额单位为分，支付金额*100，去除小数点后的数字
        String totalFeeStr = orderPayDTO.getPayAmount().multiply(BigDecimal.valueOf(100)).setScale(0).toString();
        apiWXPayDTO.setTotalFee(totalFeeStr);
        apiWXPayDTO.setBody(orderPayDTO.getGoodsName());

        // 4.调用微信支付接口预支付,获得预计支付返回信息
        H5WXPayDTO h5WXPayDTO = ConvertUtils.sourceToTarget(apiWXPayDTO, H5WXPayDTO.class);
        try {
            log.info("调用thrid-party支付服务，入参为:{}", apiWXPayDTO);
            WXPayResponseDTO = weChatPayservice.wechatPay(h5WXPayDTO);
            log.info("调用thrid-party支付服务返回结果:{}", WXPayResponseDTO);
        } catch (Exception e) {
            log.error("支付单号:[{}]错误信息为：{}", apiWXPayDTO.getPaySn(), e);
            throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
        }
        return ConvertUtils.sourceToTarget(WXPayResponseDTO, ApiWXPayResponseDTO.class);
    }

    @Override
    public ApiWXPayResponseDTO wechatRecharge(@RequestBody WechatRechargeDTO wechatRechargeDTO) {
        // 保存orderPay数据到orderPay表
        OrderPayDTO orderPayDTO = ConvertUtils.sourceToTarget(wechatRechargeDTO, OrderPayDTO.class);
        orderPayDTO.setPayState(PayStateEnum.NO.value());
        orderPayDTO.setPaySn(Long.valueOf(wechatRechargeDTO.getPaySn()));
        orderPayDTO.setPayTotalAmount(wechatRechargeDTO.getPayAmount());
        orderPayService.saveOrderPay(orderPayDTO);
        if ("MWEB".equalsIgnoreCase(wechatRechargeDTO.getTradeType()) || "JSAPI".equalsIgnoreCase(wechatRechargeDTO.getTradeType()) || "wxMiniProgram".equalsIgnoreCase(wechatRechargeDTO.getTradeType())) {
            ApiWXPayDTO apiWXPayDTO = new ApiWXPayDTO();
            apiWXPayDTO.setPaySn(wechatRechargeDTO.getPaySn());
            apiWXPayDTO.setIp(wechatRechargeDTO.getIp());
            apiWXPayDTO.setOpenId(wechatRechargeDTO.getOpenId());
            apiWXPayDTO.setCode(wechatRechargeDTO.getCode());
            // 微信JSAPI支付或者h5支付
            apiWXPayDTO.setTradeType(wechatRechargeDTO.getTradeType());
            apiWXPayDTO.setOrderPayDTO(orderPayDTO);
            return this.wechatPay(apiWXPayDTO);
        }
        if ("APP".equalsIgnoreCase(wechatRechargeDTO.getTradeType())) {
            AppApiWXPayDTO wxPayDTO = new AppApiWXPayDTO();
            wxPayDTO.setPaySn(wechatRechargeDTO.getPaySn());
            wxPayDTO.setIp(wechatRechargeDTO.getIp());
            // 微信支付金额单位为分，支付金额*100，去除小数点后的数字
            String totalFeeStr = orderPayDTO.getPayAmount().multiply(BigDecimal.valueOf(100)).setScale(0).toString();
            wxPayDTO.setTotalFee(totalFeeStr);
            wxPayDTO.setBody(orderPayDTO.getGoodsName());
            AppApiWXPayResponseDTO responseDTO = this.wechatAppPay(wxPayDTO);
            return ConvertUtils.sourceToTarget(responseDTO, ApiWXPayResponseDTO.class);
        }
        return null;
    }

    /**
     * PC端微信支付
     *
     * @param apiWXPayDTO: 支付参数
     * @return pc支付返回参数
     * @date 2020/5/19 21:57
     * @author lixiangx@leimingtech.com
     **/

    @Override
    public PCPayResponseDTO pcWechatPay(@RequestBody ApiWXPayDTO apiWXPayDTO) {

        PCPayResponseDTO pcPayResponseDTO = new PCPayResponseDTO();
        WXPayResponseDTO wxPayResponseDTO = null;

        // 1.查询支付单号对应的订单支付信息
        Long paySn = Long.parseLong(apiWXPayDTO.getPaySn());
        OrderPayDTO orderPayDTO = orderPayService.getByPaySn(paySn);

        Optional.ofNullable(orderPayDTO).orElseThrow(() -> new ServiceException(PaymentStatusCode.WECHAT_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION));

        pcPayResponseDTO.setOrderSn(orderPayDTO.getOrderSn());
        pcPayResponseDTO.setOrderPrice(orderPayDTO.getPayAmount().toString());
        pcPayResponseDTO.setPayState(orderPayDTO.getPayState());
        // 2.判断支付状态
        if (orderPayDTO.getPayState() != PayStateEnum.NO.value()) {
            log.error("微信预支付失败，订单支付状态异常");
            return pcPayResponseDTO;
        }

        // 3.封装微信支付参数
        // 微信支付金额单位为分，支付金额*100，去除小数点后的数字
        String totalFeeStr = orderPayDTO.getPayAmount().multiply(BigDecimal.valueOf(100)).setScale(0).toString();
        apiWXPayDTO.setTotalFee(totalFeeStr);
        apiWXPayDTO.setBody(orderPayDTO.getGoodsName());

        // 4.调用微信支付接口预支付,获得预计支付返回信息
        H5WXPayDTO h5WXPayDTO = ConvertUtils.sourceToTarget(apiWXPayDTO, H5WXPayDTO.class);

        try {
            log.info("调用thrid-party支付服务，入参为:{}", apiWXPayDTO);
            wxPayResponseDTO = weChatPayservice.wechatPay(h5WXPayDTO);
            pcPayResponseDTO.setPayCodeBase64(ZxingUtils.generateQrCodeBase64(wxPayResponseDTO.getCodeUrl()));
            log.info("调用thrid-party支付服务返回结果:{}", wxPayResponseDTO);
        } catch (Exception e) {
            log.error("支付单号:[{}]错误信息为：{}", apiWXPayDTO.getPaySn(), e);
            throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
        }
        return pcPayResponseDTO;
    }

    /**
     * 功能描述:
     * 〈微信统一下单接口，生成微信预支付订单〉
     *
     * @param
     * @author : 刘远杰
     */

    @Override
    public AppApiWXPayResponseDTO wechatAppPay(@RequestBody AppApiWXPayDTO appWXPayDTO) {

        log.info("调用微信app预支付接口，参数：{}", appWXPayDTO);
        AppApiWXPayResponseDTO apiWXPayResponseDTO = null;

        Map<String, String> logMap = new HashMap<>();
        logMap.put("appWXPayDTO", appWXPayDTO.toString());

        // 1.查询支付单号对应的订单支付信息
        Long paySn = Long.parseLong(appWXPayDTO.getPaySn());
        OrderPayDTO orderPayDTO = orderPayService.getByPaySn(paySn);
        log.info("微信app预支付获取订单确认表信息，paySn:{}", paySn);

        if (orderPayDTO != null) {
            // 2.判断支付状态，只有
            if (orderPayDTO.getPayState() != PayStateEnum.NO.value()) {
                log.info("微信app预支付失败，订单支付状态异常");
                throw new ServiceException(PaymentStatusCode.WECHAT_PRE_PAY_PAYSN_STATUS_EXCEPTION);
            }

            // 3.封装微信支付参数
            // 微信支付金额单位为分，支付金额*100，去除小数点后的数字
            String totalFeeStr = orderPayDTO.getPayAmount().multiply(BigDecimal.valueOf(100)).setScale(0).toString();
            appWXPayDTO.setTotalFee(totalFeeStr);
            appWXPayDTO.setBody(orderPayDTO.getGoodsName());

            // 4.调用微信支付接口预支付,获得预计支付返回信息
            log.info("调用微信app预支付接口，appWXPayDTO:{}", appWXPayDTO);


            try {
                AppWXPayDTO appWXPayDTO1 = ConvertUtils.sourceToTarget(appWXPayDTO, AppWXPayDTO.class);
                AppWXPayResponseDTO appWXPayResponseDTO = weChatAppPayService.payWX(appWXPayDTO1);
                apiWXPayResponseDTO = ConvertUtils.sourceToTarget(appWXPayResponseDTO, AppApiWXPayResponseDTO.class);
            } catch (Exception e) {
                log.error("错误信息为" + e);
                throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
            }
            log.info("调用微信app预支付接口返回结果，apiWXPayResponseDTO:{}", apiWXPayResponseDTO);

            return apiWXPayResponseDTO;
        } else {
            mlogger.info(PaymentStatusCode.WECHAT_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getCode(),
                    PaymentStatusCode.WECHAT_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getMessage(), logMap);
            log.info("微信app预支付接口获取orderpay失败，支付单号不存在");
            throw new ServiceException(PaymentStatusCode.WECHAT_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION);
        }

    }

    /**
     * 功能描述:
     * 〈保存支付流水〉
     *
     * @param orderPayDTO 订单支付表
     * @param paymentDTO  支付方式信息
     * @return : void
     * @author : 刘远杰
     */
    private PaymentTallyDTO savePaymentTally(OrderPayDTO orderPayDTO, H5WXPayNotifyDTO h5WXPayNotifyDTO, PaymentDTO paymentDTO) {

        // 获取子订单保存支付流水
        PaymentTallyDTO paymentTallyDTO = new PaymentTallyDTO();

        paymentTallyDTO.setId(IdGenerator.defaultSnowflakeId());
        if (paymentDTO != null) {
            paymentTallyDTO.setPaymentCode(paymentDTO.getPaymentCode());
            paymentTallyDTO.setPaymentName(paymentDTO.getPaymentName());
        }
        // 参数封装
        paymentTallyDTO.setTransactionStatus(PaymentCodeEnum.TRANSACTION_STATUS_OUT.value());
        paymentTallyDTO.setPaymentStatus(PaymentTallyEnum.PAYMENTTALLY_STATE_SUCCESS.value());
        // 区分普通支付和充值
        if (h5WXPayNotifyDTO.getPaySn().startsWith(PaymentResultCodeConstants.RECHARGEPAY)) {
            paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_RECHANGE.value());

            // 充值修改用户余额和余额变更记录
            this.rechargeBalance(orderPayDTO);
        } else {
            paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_ORDER.value());
        }
        paymentTallyDTO.setTradeSn(h5WXPayNotifyDTO.getTransactionId());
        paymentTallyDTO.setPaymentSn(Long.parseLong(h5WXPayNotifyDTO.getPaySn()));
        paymentTallyDTO.setPaymentAmount(orderPayDTO.getPayTotalAmount());
        paymentTallyDTO.setBalanceAmount(orderPayDTO.getBalanceAmount());
        paymentTallyDTO.setOrderSn(orderPayDTO.getOrderSn());
        paymentTallyDTO.setBuyerId(orderPayDTO.getBuyerId());
        paymentTallyDTO.setBuyerName(orderPayDTO.getBuyerName());
        paymentTallyDTO.setIsSplit(orderPayDTO.getIsSplit());
        log.info("支付流水保存参数：paymentTallyDTO:{}", paymentTallyDTO);
        paymentTallyService.save(paymentTallyDTO);

        return paymentTallyDTO;
    }

    /**
     * 功能描述:
     * 〈微信支付回调，修改订单支付状态〉
     *
     * @author : 刘远杰
     */

    @Override
    public H5WXPayNotifyDTO updateorderstate(@RequestParam Map<String, String> params) {
        log.info("微信支付回调方法调用参数：{}", params);
        H5WXPayNotifyDTO h5WXPayNotifyDTO = new H5WXPayNotifyDTO();
        String result = "";

        Map<String, String> logMap = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            logMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        try {
            // 1.根据反过来支付信息修改订单状态
            String resultCode = (String) params.get("result_code");
            h5WXPayNotifyDTO.setResultCode(resultCode);
            if (StringUtils.equals("SUCCESS", resultCode)) {
                // 签名正确，封装结果数据
                String totalFee = params.get("total_fee");
                String sn = params.get("out_trade_no");
                String transactionId = params.get("transaction_id");
                h5WXPayNotifyDTO.setTotalFee(totalFee);
                h5WXPayNotifyDTO.setPaySn(sn);
                h5WXPayNotifyDTO.setTransactionId(transactionId);
                // 根据trade_type判断支付来自app还是H5,trade_type有JSAPI、NATIVE、APP
                log.info("微信支付回调返回的appId为:{}", params.get("appid"));
                if (params.get("trade_type") != null && "APP".equals(params.get("trade_type"))) {
                    this.finishPay(h5WXPayNotifyDTO, transactionId, PaymentCodeConstants.WECHATAPP);
                } else {
                    this.finishPay(h5WXPayNotifyDTO, transactionId, PaymentCodeConstants.WECHAT);
                }
                mlogger.info(PaymentStatusCode.WECHAT_PAY_NOTIFY_SUCCESS_CODE, PaymentStatusCode.WECHAT_PAY_NOTIFY_SUCCESS_MESSAGE, logMap);
                // 修改订单状态成功
                log.info("微信支付回调修改订单、支付状态成功，结果码：" + resultCode + ",支付单号：" + sn + "微信支付单号：" + transactionId);
                // 给微信系统发送成功信息，微信系统收到此结果后不再进行后续通知
                result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            } else {
                logMap.put("resultCode", resultCode);
                mlogger.info(PaymentStatusCode.WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_CODE,
                        PaymentStatusCode.WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_MESSAGE, logMap);
                result = "<xml><return_code><![CDATA[FAIL ]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            }
            h5WXPayNotifyDTO.setResult(result);
            return h5WXPayNotifyDTO;
        } catch (Exception e) {
            mlogger.info(PaymentStatusCode.WECHAT_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE,
                    PaymentStatusCode.WECHAT_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE, logMap, e);
            log.error("修改订单支付状态异常，支付单号{}" + params.get("out_trade_no"));
            log.error("系统异常，{}" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (StringUtils.isNotBlank(result)) {
                h5WXPayNotifyDTO.setResult("<xml><return_code><![CDATA[FAIL ]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            }
            return h5WXPayNotifyDTO;
        }
    }

    /**
     * h5支付时，前端调用该接口查询支付结果
     *
     * @param paySn 支付单号
     * @return
     * @date 2020-01-02 15:05
     * @author
     **/

    @Override
    public H5WXQueryOrderResponseDTO orderquery(Long paySn) throws Exception {

        H5WXQueryOrderResponseDTO h5WXQueryOrderResponseDTO = new H5WXQueryOrderResponseDTO();

        String paySnString = paySn.toString();

        log.info("构造微信查询订单接口参数成功,paySnString:{}", paySnString);

        // 2.调用微信查询订单接口
        String resultXml = weChatPayservice.orderquery(paySnString);
        Map<String, String> result = WXPayUtil.xmlToMap(resultXml);
        String resultCode = "";
        // 发出的请求正确与否
        if ("SUCCESS".equals(result.get("return_code"))) {
            resultCode = result.get("trade_state");
            log.info("获取微信订单查询结果成功：prepay_id:{}", resultCode);

            // 查询的业务结果正确与否
            if ("SUCCESS".equals(result.get("result_code"))) {
                // 返回结果封装
                h5WXQueryOrderResponseDTO.setResultCode(resultCode);
                h5WXQueryOrderResponseDTO.setResultMsg(result.get("trade_state_desc"));
            } else {
                // 如果错误返回对应错误码和描述
                h5WXQueryOrderResponseDTO.setResultCode(result.get("err_code"));
                h5WXQueryOrderResponseDTO.setResultMsg(result.get("err_code_des"));
            }

        } else {
            log.info("获取信订单查询结果失败：prepay_id:{}", resultCode);
            mlogger.info(PaymentStatusCode.WECHAT_ORDER_QUERY_FAIL.getCode(),
                    PaymentStatusCode.WECHAT_ORDER_QUERY_FAIL.getMessage());
            throw new ServiceException(PaymentStatusCode.WECHAT_ORDER_QUERY_FAIL);
        }

        return h5WXQueryOrderResponseDTO;
    }

    /**
     * app支付时，前端调用该接口查询支付结果
     *
     * @param paySn 支付单号
     * @return
     * @date 2020-01-02 15:05
     * @author
     **/

    @Override
    public AppWXQueryOrderResponseDTO appOrderQuery(Long paySn) throws Exception {

        AppWXQueryOrderResponseDTO appWXQueryOrderResponseDTO = new AppWXQueryOrderResponseDTO();

        String paySnString = paySn.toString();

        log.info("构造微信查询订单接口参数成功,paySnString:{}", paySnString);

        // 2.调用微信查询订单接口
        String resultXml = weChatAppPayService.orderquery(paySnString);
        Map<String, String> result = WXPayUtil.xmlToMap(resultXml);
        String resultCode = "";
        // 发出的请求正确与否
        if ("SUCCESS".equals(result.get("return_code"))) {
            resultCode = result.get("trade_state");
            log.info("获取微信订单查询结果成功：prepay_id:{}", resultCode);

            // 查询的业务结果正确与否
            if ("SUCCESS".equals(result.get("result_code"))) {
                // 返回结果封装
                appWXQueryOrderResponseDTO.setResultCode(resultCode);
                appWXQueryOrderResponseDTO.setResultMsg(result.get("trade_state_desc"));
            } else {
                // 如果错误返回对应错误码和描述
                appWXQueryOrderResponseDTO.setResultCode(result.get("err_code"));
                appWXQueryOrderResponseDTO.setResultMsg(result.get("err_code_des"));
            }

        } else {
            log.info("获取信订单查询结果失败：prepay_id:{}", resultCode);
            mlogger.info(PaymentStatusCode.WECHAT_ORDER_QUERY_FAIL.getCode(),
                    PaymentStatusCode.WECHAT_ORDER_QUERY_FAIL.getMessage());
            throw new ServiceException(PaymentStatusCode.WECHAT_ORDER_QUERY_FAIL);
        }

        return appWXQueryOrderResponseDTO;
    }

    /**
     * @param aliPayH5RequestDTO : 支付单号和类型
     * @return java.lang.String
     * @Description 支付宝支付统一下单接口(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:31 2019-12-09
     */

    @Override
    public AliPayH5ResponseDTO aliPayCertUnifiedOrder(@RequestBody AliPayH5RequestDTO aliPayH5RequestDTO) {
        log.info("调用支付宝预支付接口，参数：{}", aliPayH5RequestDTO);
        AliPayH5ResponseDTO aliPayH5ResponseDTO = new AliPayH5ResponseDTO();

        Map<String, String> logMap = new HashMap<>();
        logMap.put("h5AliPayRequestDTO", aliPayH5RequestDTO.toString());

        // 1.查询支付单号对应的订单支付信息
        Long paySn = Long.parseLong(aliPayH5RequestDTO.getPaySn());
        // 普通支付没有orderPay,则根据paySn获取
        OrderPayDTO orderPayDTO = aliPayH5RequestDTO.getOrderPayDTO();
        if (null == orderPayDTO) {
            orderPayDTO = orderPayService.getByPaySn(paySn);
        }
        log.info("支付宝支付获取订单确认表信息，paySn:{}", paySn);

        if (orderPayDTO != null) {
            // 2.判断支付状态，只有
            if (orderPayDTO.getPayState() != PayStateEnum.NO.value()) {
                log.info("支付宝预支付失败，订单支付状态异常");
                throw new ServiceException(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_STATUS_EXCEPTION);
            }

            // 3.封装支付宝支付参数
            // 支付宝支付金额单位为元，支付金额匹配支付宝的单位，小数点后两位数字最小0.01元；
            BigDecimal totalFee = orderPayDTO.getPayAmount();
            H5AliPayRequestDTO h5AliPayRequestDTO = new H5AliPayRequestDTO();
            h5AliPayRequestDTO.setBody(orderPayDTO.getGoodsName());
            h5AliPayRequestDTO.setSubject(orderPayDTO.getGoodsName());
            h5AliPayRequestDTO.setOutTradeNo(aliPayH5RequestDTO.getPaySn());
            h5AliPayRequestDTO.setTotalAmount(totalFee);
            // h5支付时传QUICK_WAP_PAY，APP支付是传QUICK_MSECURITY_PAY，后期做APP支付根据传递的type值判断是h5或者APP支付这里更改对应的值
            String payType = aliPayH5RequestDTO.getTradeType();
            if ("h5".equals(payType)) {
                h5AliPayRequestDTO.setProductCode("QUICK_WAP_PAY");
            } else if ("app".equals(payType)) {
                h5AliPayRequestDTO.setProductCode("QUICK_MSECURITY_PAY");
            } else {
                h5AliPayRequestDTO.setProductCode("QUICK_WAP_PAY");
            }

            String paySnString = aliPayH5RequestDTO.getPaySn();
            Integer isSplit = orderPayDTO.getIsSplit();
            String orderID = aliPayH5RequestDTO.getOrderId();
            String payPrice = totalFee.toString();
            String returnUrl = "?paySn=" + paySnString + "&orderId=" + orderID + "&isSplit=" + isSplit + "&payPrice=" + payPrice + "&payType=alipay";
            h5AliPayRequestDTO.setReturnUrl(returnUrl);
            log.info("支付宝支付h5的成功回调地址,returnUrl：{}", returnUrl);
            H5AlipayResponseDTO h5AlipayResponseDTO = new H5AlipayResponseDTO();
            // 4.调用支付宝支付接口预支付,获得预计支付返回信息
            log.info("调用支付宝预支付接口，h5AliPayRequestDTO:{}", h5AliPayRequestDTO);
            try {
                h5AlipayResponseDTO = aliPayService.aliPayCertUnifiedOrder(h5AliPayRequestDTO);
            } catch (Exception e) {
                log.error("错误信息为" + e);
                throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
            }
            log.info("调用支付宝预支付接口返回结果，h5AlipayResponseDTO:{}", h5AlipayResponseDTO);
            if (h5AlipayResponseDTO != null) {
                aliPayH5ResponseDTO.setOrderString(h5AlipayResponseDTO.getOrderString());
                aliPayH5ResponseDTO.setCode(200);
                aliPayH5ResponseDTO.setMsg("支付成功");
            }
            return aliPayH5ResponseDTO;
        } else {
            mlogger.info(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getCode(),
                    PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getMessage(), logMap);
            log.info("支付宝预支付接口失败，支付单号不存在");
            throw new ServiceException(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION);
        }

    }

    /**
     * @param aliPayAppReqestDTO : 支付单号和类型
     * @return java.lang.String
     * @Description PC支付宝支付统一下单接口(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:31 2019-12-09
     */

    @Override
    public AliPayAppResponseDTO aliPayPcCertUnifiedOrder(@RequestBody AliPayAppReqestDTO aliPayAppReqestDTO) {
        log.info("调用支付宝预支付接口，参数：{}", aliPayAppReqestDTO);
        AliPayAppResponseDTO aliPayAppResponseDTO = new AliPayAppResponseDTO();

        Map<String, String> logMap = new HashMap<>();
        logMap.put("h5AliPayRequestDTO", aliPayAppReqestDTO.toString());

        // 1.查询支付单号对应的订单支付信息
        Long paySn = Long.parseLong(aliPayAppReqestDTO.getPaySn());
        OrderPayDTO orderPayDTO = orderPayService.getByPaySn(paySn);
        log.info("支付宝支付获取订单确认表信息，paySn:{}", paySn);

        if (orderPayDTO != null) {
            // 2.判断支付状态，只有
            if (orderPayDTO.getPayState() != PayStateEnum.NO.value()) {
                log.info("支付宝预支付失败，订单支付状态异常");
                throw new ServiceException(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_STATUS_EXCEPTION);
            }

            // 3.封装支付宝支付参数
            // 支付宝支付金额单位为元，支付金额匹配支付宝的单位，小数点后两位数字最小0.01元；
            BigDecimal totalFee = orderPayDTO.getPayAmount();
            PcAliPayRequestDTO pcAliPayRequestDTO = new PcAliPayRequestDTO();
            pcAliPayRequestDTO.setBody(orderPayDTO.getGoodsName());
            pcAliPayRequestDTO.setSubject(orderPayDTO.getGoodsName());
            pcAliPayRequestDTO.setOutTradeNo(aliPayAppReqestDTO.getPaySn());
            pcAliPayRequestDTO.setTotalAmount(totalFee);

            String orderID = aliPayAppReqestDTO.getOrderId();
            String payPrice = totalFee.toString();
            String returnUrl = "?orderId=" + orderID + "&price=" + payPrice + "&payType=1";
            pcAliPayRequestDTO.setReturnUrl(returnUrl);
            log.info("支付宝支付pc的成功回调地址,returnUrl：{}", returnUrl);

            PcAlipayResponseDTO pcAlipayResponseDTO = new PcAlipayResponseDTO();
            // 4.调用支付宝支付接口预支付,获得预计支付返回信息
            log.info("调用支付宝预支付接口，pcAliPayRequestDTO:{}", pcAliPayRequestDTO);
            try {
                pcAlipayResponseDTO = aliPayService.aliPayPagePay(pcAliPayRequestDTO);
            } catch (Exception e) {
                log.error("错误信息为" + e);
                throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
            }
            log.info("调用支付宝预支付接口返回结果，pcAlipayResponseDTO:{}", pcAlipayResponseDTO);
            if (pcAlipayResponseDTO != null) {
                aliPayAppResponseDTO.setOrderString(pcAlipayResponseDTO.getOrderString());
                aliPayAppResponseDTO.setCode(200);
                aliPayAppResponseDTO.setMsg("支付成功");
            }
            return aliPayAppResponseDTO;
        } else {
            mlogger.info(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getCode(),
                    PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getMessage(), logMap);
            log.info("支付宝预支付接口失败，支付单号不存在");
            throw new ServiceException(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION);
        }

    }

    /**
     * @param requestParams : 回调参数
     * @return java.lang.String
     * @Description 支付宝回调(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:34 2019-12-09
     */

    @Override
    public String aliPayCertNotify(@RequestParam Map<String, String> requestParams) {

        log.info(">>>支付宝回调参数：" + requestParams.toString());

        Map<String, String> logMap = new HashMap<>();

        try {
            // 获取支付宝的支付key
            boolean flag = aliPayService.checkSignature(requestParams);

            if (flag) {
                log.info(">>>支付宝回调签名认证成功");
                //商户订单号
                String out_trade_no = requestParams.get("out_trade_no");
                //交易状态
                String trade_status = requestParams.get("trade_status");
                //交易金额
                String amount = requestParams.get("total_amount");

                //支付宝交易号
                String trade_no = requestParams.get("trade_no");

                if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
                    log.info("支付宝回调支付成功，trade_status:{}", trade_status);
                    /**
                     * 自己的业务处理
                     */
                    H5WXPayNotifyDTO h5WXPayNotifyDTO = new H5WXPayNotifyDTO();
                    h5WXPayNotifyDTO.setResultCode(trade_status);
                    h5WXPayNotifyDTO.setTotalFee(amount);
                    h5WXPayNotifyDTO.setPaySn(out_trade_no);
                    h5WXPayNotifyDTO.setTransactionId(trade_no);

                    // 根据paySn查询状态如果已经是已付款状态则直接返回Sucess
                    OrderPayDTO orderPayDTO = orderPayService.getByPaySn(Long.parseLong(out_trade_no));
                    if (null != orderPayDTO && orderPayDTO.getPayState().equals(PayStateEnum.Yes.value())) {
                        return "success";
                    }

                    // 执行支付成功后的商城业务逻辑比如更新订单支付信息成功，保存交易流水，修改订单状态
                    this.finishPay(h5WXPayNotifyDTO, trade_no, PaymentCodeConstants.ALIPAY);

                    mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SUCCESS_CODE, PaymentStatusCode.ALI_PAY_NOTIFY_SUCCESS_MESSAGE, logMap);
                    // 修改订单状态成功
                    log.info("支付宝支付回调修改订单、支付状态成功，结果码：" + trade_status + ",支付单号：" + out_trade_no + "支付宝支付单号：" + trade_no);

                } else {
                    log.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}", trade_status, requestParams);
                    mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_CODE,
                            PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_MESSAGE, logMap);
                }
            } else {
                log.info("支付宝回调签名认证失败，signVerified=false, params:{}", requestParams);
                mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE,
                        PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE, logMap);

                return "failure";
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("支付宝回调签名认证失败，signVerified=false, params:{}", requestParams);
            mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE,
                    PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE, logMap, e);
            return "failure";
        }
        return "success";//请不要修改或删除
    }

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

    @Override
    public void finishPay(@RequestBody H5WXPayNotifyDTO h5WXPayNotifyDTO, @RequestParam("transactionId") String transactionId, @RequestParam("paymentCode") String paymentCode) {
        // 2.根据交易单号获取订单信息
        Long paySn = Long.parseLong(h5WXPayNotifyDTO.getPaySn());
        OrderPayDTO orderPayDTO = orderPayService.getByPaySn(paySn);
        log.info("支付回调获取订单支付信息，paySn:{}，paymentCode:{}", paySn, paymentCode);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("payment_code", paymentCode);
        PaymentDTO paymentDTO = this.getByPaymentCode(paymentCode);
        log.info("支付回调paymentDTO:{}", paymentDTO);

        // 3.更新订单支付信息
        orderPayService.updatePayStateByPaySn(paySn, transactionId);
        log.info("更新订单支付信息成功，paySn:{},transactionId:{}", paySn, transactionId);

        // 4.保存交易流水
        log.info("支付回调，保存订单支付流水，h5WXPayNotifyDTO:{},{},{}", orderPayDTO, h5WXPayNotifyDTO, paymentDTO);
        this.savePaymentTally(orderPayDTO, h5WXPayNotifyDTO, paymentDTO);

        if (h5WXPayNotifyDTO.getPaySn().startsWith(PaymentResultCodeConstants.NORMALPAY)) {
            // 5.如果使用了余额支付，则更新用户余额相关信息
            if (orderPayDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
                this.saveBalance(orderPayDTO);
            }

            // 6.支付成功，修改订单状态
            log.info("支付回调，修改订单状态，paySn:{},paymentId:{},paymentName:{},transactionId:{}",
                    paySn, paymentDTO.getId(), paymentDTO.getPaymentName(), h5WXPayNotifyDTO.getTransactionId());
            //发送MQ消息修改订单状态
            UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
            updateOrderDTO.setPaymentId(paymentDTO.getId());
            updateOrderDTO.setPaymentName(paymentDTO.getPaymentName());
            updateOrderDTO.setPaySn(paySn);
            updateOrderDTO.setPaymentCode(paymentDTO.getPaymentCode());
            updateOrderDTO.setTransactionId(transactionId);
            String message = JSONObject.toJSONString(updateOrderDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_NOTIFY_UPDATE_ORDER_QUEUE, message);
        }
    }

    /**
     * 支付宝h5充值
     *
     * @param aliPayRechargeDTO
     * @return
     * @date 2020-10-26 15:24
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public AliPayH5ResponseDTO aliPayRecharge(@RequestBody AliPayRechargeDTO aliPayRechargeDTO) {
        // 保存orderPay数据到orderPay表
        OrderPayDTO orderPayDTO = ConvertUtils.sourceToTarget(aliPayRechargeDTO, OrderPayDTO.class);
        orderPayDTO.setPayState(PayStateEnum.NO.value());
        orderPayDTO.setPayTotalAmount(aliPayRechargeDTO.getPayAmount());
        orderPayService.saveOrderPay(orderPayDTO);
        // 充值和支付流程共用支付宝支付流程
        AliPayH5RequestDTO aliPayH5RequestDTO = new AliPayH5RequestDTO();
        aliPayH5RequestDTO.setPaySn(aliPayRechargeDTO.getPaySn().toString());
        aliPayH5RequestDTO.setTradeType("h5");
        aliPayH5RequestDTO.setOrderPayDTO(orderPayDTO);
        return this.aliPayCertUnifiedOrder(aliPayH5RequestDTO);
    }

    @Override
    public AliPayAppResponseDTO appAliPayRecharge(@RequestBody AliPayRechargeDTO aliPayRechargeDTO) {
        // 保存orderPay数据到orderPay表
        OrderPayDTO orderPayDTO = ConvertUtils.sourceToTarget(aliPayRechargeDTO, OrderPayDTO.class);
        orderPayDTO.setPayState(PayStateEnum.NO.value());
        orderPayDTO.setPayTotalAmount(aliPayRechargeDTO.getPayAmount());
        orderPayService.saveOrderPay(orderPayDTO);
        // 充值和支付流程共用支付宝支付流程
        AliPayAppReqestDTO aliPayAppReqestDTO = new AliPayAppReqestDTO();
        aliPayAppReqestDTO.setPaySn(aliPayRechargeDTO.getPaySn().toString());
        return this.aliPayAPPCertUnifiedOrder(aliPayAppReqestDTO);
    }

    /**
     * 更新用户余额相关信息
     *
     * @param orderPayDTO 订单支付信息
     * @return
     * @date 2020-07-08 19:03
     * @author huangkeyuan@leimingtech.com
     **/
    private void saveBalance(OrderPayDTO orderPayDTO) {
        // 查询当前的用户余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(orderPayDTO.getBuyerId());

        // 更新memberinfo冻结金额和用户余额明细
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        memberUpdateDTO.setBlockedBalance(orderPayDTO.getBalanceAmount().negate());
        memberUpdateDTO.setId(orderPayDTO.getBuyerId());
        // 将用户冻结余额释放
        memberInfoService.updateByMemberId(memberUpdateDTO);

        // 更新用户余额明细
        MemberBalanceLogDTO memberBalanceLogDTO = new MemberBalanceLogDTO();
        memberBalanceLogDTO.setBalanceType(BalanceEnum.MEMBER_ORDER.value());
        memberBalanceLogDTO.setBeforeBalance(memberBalanceInfoDTO.getAvailableBalance().add(orderPayDTO.getBalanceAmount()));
        memberBalanceLogDTO.setChangeBalance(orderPayDTO.getBalanceAmount().negate());
        memberBalanceLogDTO.setCurrentBalance(memberBalanceInfoDTO.getAvailableBalance());
        memberBalanceLogDTO.setMemberId(orderPayDTO.getBuyerId());
        memberBalanceLogDTO.setMemberName(orderPayDTO.getBuyerName());
        memberBalanceLogService.save(memberBalanceLogDTO);
        log.info("更新用户余额明细成功，memberBalanceLogDTO：{}", memberBalanceLogDTO);
    }

    private void rechargeBalance(OrderPayDTO orderPayDTO) {
        // 查询当前的用户余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(orderPayDTO.getBuyerId());

        // 更新memberinfo冻结金额和用户余额明细
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
//        memberUpdateDTO.setBlockedBalance(orderPayDTO.getPayAmount().negate());
        memberUpdateDTO.setAvailableBalance(orderPayDTO.getPayAmount());
//        if (String.valueOf(orderPayDTO.getPaySn()).startsWith(PaymentResultCodeConstants.RECHARGEPAY)) {
//            memberUpdateDTO.setAvailableWithdrawal(orderPayDTO.getPayAmount());
//        }
        memberUpdateDTO.setId(orderPayDTO.getBuyerId());
        // 将用户冻结余额释放
        memberInfoService.updateByMemberId(memberUpdateDTO);

        // 更新用户余额明细
        MemberBalanceLogDTO memberBalanceLogDTO = new MemberBalanceLogDTO();
        memberBalanceLogDTO.setBalanceType(BalanceEnum.MEMBER_RECHARGE.value());
        memberBalanceLogDTO.setBeforeBalance(memberBalanceInfoDTO.getAvailableBalance());
        memberBalanceLogDTO.setChangeBalance(orderPayDTO.getPayAmount());
        memberBalanceLogDTO.setCurrentBalance(memberBalanceInfoDTO.getAvailableBalance().add(orderPayDTO.getPayAmount()));
        memberBalanceLogDTO.setMemberId(orderPayDTO.getBuyerId());
        memberBalanceLogDTO.setMemberName(orderPayDTO.getBuyerName());
        memberBalanceLogService.save(memberBalanceLogDTO);
        log.info("充值，更新用户余额明细成功，memberBalanceLogDTO：{}", memberBalanceLogDTO);
    }

    /**
     * 功能描述: 支付宝app支付下单接口(公钥证书方式)
     *
     * @param: [aliPayAppReqestDTO]
     * @return: com.leimingtech.modules.dto.payment.alipay.AliPayAppResponseDTO
     * @auther: zhangtai
     * @date: 2020/4/3 18:45
     */

    @Override
    public AliPayAppResponseDTO aliPayAPPCertUnifiedOrder(@RequestBody AliPayAppReqestDTO aliPayAppReqestDTO) {
        log.info("调用app支付支付宝预支付接口，参数：{}", aliPayAppReqestDTO);
        AliPayAppResponseDTO aliPayAppResponseDTO = new AliPayAppResponseDTO();
        Map<String, String> logMap = new HashMap<>();
        logMap.put("aliPayAppReqestDTO", aliPayAppReqestDTO.toString());

        // 1.查询支付单号对应的订单支付信息
        Long paySn = Long.parseLong(aliPayAppReqestDTO.getPaySn());
        OrderPayDTO orderPayDTO = orderPayService.getByPaySn(paySn);
        log.info("支付宝app支付获取订单确认表信息，paySn:{}", paySn);

        if (orderPayDTO != null) {
            // 2.判断支付状态，只有
            if (orderPayDTO.getPayState() != PayStateEnum.NO.value()) {
                log.info("支付宝预支付失败，订单支付状态异常");
                throw new ServiceException(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_STATUS_EXCEPTION);
            }

            // 3.封装支付宝支付参数
            // 支付宝支付金额单位为元，支付金额匹配支付宝的单位，小数点后两位数字最小0.01元；
            BigDecimal totalFee = orderPayDTO.getPayAmount();
            AppAlipayRequestDTO appAlipayRequestDTO = new AppAlipayRequestDTO();
            appAlipayRequestDTO.setBody(orderPayDTO.getGoodsName());
            appAlipayRequestDTO.setSubject(orderPayDTO.getGoodsName());
            appAlipayRequestDTO.setOutTradeNo(aliPayAppReqestDTO.getPaySn());
            appAlipayRequestDTO.setTotalAmount(totalFee);

            AppAlipayResponseDTO appAlipayResponseDTO = null;
            // 4.调用支付宝支付接口预支付,获得预计支付返回信息
            log.info("调用支付宝app支付接口，appAlipayRequestDTO:{}", appAlipayRequestDTO);
            try {
                appAlipayResponseDTO = aliPayService.aliPayAppPay(appAlipayRequestDTO);
            } catch (Exception e) {
                log.error("错误信息为" + e);
                throw new ServiceException(ServiceStatusCode.SERVER_INTERNAL_ERROR);
            }
            log.info("调用支付宝app支付接口返回结果，appAlipayResponseDTO:{}", appAlipayResponseDTO);
            if (appAlipayResponseDTO != null) {
                aliPayAppResponseDTO.setOrderString(appAlipayResponseDTO.getOrderString());
                aliPayAppResponseDTO.setCode(200);
                aliPayAppResponseDTO.setMsg("支付成功");
            }
            return aliPayAppResponseDTO;
        } else {
            mlogger.info(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getCode(),
                    PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION.getMessage(), logMap);
            log.info("支付宝预支付接口失败，支付单号不存在");
            throw new ServiceException(PaymentStatusCode.ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION);
        }

    }

    /**
     * @param requestParams : 回调参数
     * @return java.lang.String
     * @Description app支付支付宝回调(公钥证书方式)
     * @Author huangkeyuan
     * @Date 16:34 2019-12-09
     */

    @Override
    public String appAliPayCertNotify(@RequestParam Map<String, String> requestParams) {

        log.info(">>>app支付支付宝回调参数：" + requestParams.toString());

        Map<String, String> logMap = new HashMap<>();

        try {
            // 获取支付宝的支付key
            boolean flag = aliPayService.appCheckSignature(requestParams);

            if (flag) {
                log.info(">>>app支付支付宝回调签名认证成功");
                //商户订单号
                String out_trade_no = requestParams.get("out_trade_no");
                //交易状态
                String trade_status = requestParams.get("trade_status");
                //交易金额
                String amount = requestParams.get("total_amount");

                //支付宝交易号
                String trade_no = requestParams.get("trade_no");

                if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
                    log.info("APP支付支付宝回调支付成功，trade_status:{}", trade_status);
                    /**
                     * 自己的业务处理
                     */
                    H5WXPayNotifyDTO h5WXPayNotifyDTO = new H5WXPayNotifyDTO();
                    h5WXPayNotifyDTO.setResultCode(trade_status);
                    h5WXPayNotifyDTO.setTotalFee(amount);
                    h5WXPayNotifyDTO.setPaySn(out_trade_no);
                    h5WXPayNotifyDTO.setTransactionId(trade_no);

                    // 执行支付成功后的商城业务逻辑比如更新订单支付信息成功，保存交易流水，修改订单状态
                    log.info("支付宝app支付参数h5WXPayNotifyDTO:{},{}", h5WXPayNotifyDTO, PaymentCodeConstants.ALIPAYAPP);
                    this.finishPay(h5WXPayNotifyDTO, trade_no, PaymentCodeConstants.ALIPAYAPP);

                    mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SUCCESS_CODE, PaymentStatusCode.ALI_PAY_NOTIFY_SUCCESS_MESSAGE, logMap);
                    // 修改订单状态成功
                    log.info("支付宝app支付回调修改订单、支付状态成功，结果码：" + trade_status + ",支付单号：" + out_trade_no + "支付宝支付单号：" + trade_no);

                } else {
                    log.error("没有处理app支付宝回调业务，支付宝交易状态：{},params:{}", trade_status, requestParams);
                    mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_CODE,
                            PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_MESSAGE, logMap);
                }
            } else {
                log.info("支付宝回调签名认证失败，signVerified=false, params:{}", requestParams);
                mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE,
                        PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE, logMap);

                return "failure";
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("支付宝回调签名认证失败，signVerified=false, params:{}", requestParams);
            mlogger.info(PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE,
                    PaymentStatusCode.ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE, logMap, e);
            return "failure";
        }
        return "success";//请不要修改或删除
    }

    /**
     * 苹果内购充值
     *
     * @param appleRechargeDTO
     * @return paysn
     * @date 2020-11-23 15:24
     * @author kuangweiguo@leimingtech.com
     **/

    @Override
    public String applePayRecharge(@RequestBody AppleRechargeDTO appleRechargeDTO) {

        OrderPayDTO orderPayDTO = new OrderPayDTO();
        // 保存orderPay数据到orderPay表
        orderPayDTO.setPayState(PayStateEnum.NO.value());
        orderPayDTO.setPaySn(Long.valueOf(appleRechargeDTO.getPaySn()));
        orderPayDTO.setBuyerId(appleRechargeDTO.getBuyerId());
        orderPayDTO.setBuyerName(appleRechargeDTO.getBuyerName());
        orderPayDTO.setGoodsName(appleRechargeDTO.getGoodsName());
        orderPayDTO.setPayAmount(appleRechargeDTO.getPayAmount());
        orderPayDTO.setPayTotalAmount(appleRechargeDTO.getPayAmount());
        orderPayDTO.setBalanceAmount(new BigDecimal("0"));
        orderPayService.saveOrderPay(orderPayDTO);
        return orderPayDTO.getPaySn() + "";
    }

    /**
     * 苹果内购充值
     *
     * @param applePayDTO
     * @return ApplePayResponseDTO
     * @date 2020-11-23 15:24
     * @author kuangweiguo@leimingtech.com
     **/

    @Override
    public Boolean applePayVerify(@RequestBody ApplePayDTO applePayDTO) {

        String sendPost = HttpUtil.sendPost(APP_STORE_PUBLIC_RECEIPT, "{\"receipt-data\":\"" + applePayDTO.getReceipt() + "\"}");

        log.info("苹果内购订单返回值,reciveMsg,{}", sendPost.toString());

        JSONObject object = JSON.parseObject(sendPost);
        int status = (int) object.get("status");
        if (status == 0) {
            String response = object.getString("receipt");
            JSONObject obj = JSON.parseObject(response);
            String bundle_id = (String) obj.getString("bundle_id");

            if (!BUNDLE_ID.equals(bundle_id)) {
                throw new ServiceException(PaymentStatusCode.BUNDLE_ID_WRONG);
            }

            JSONArray jsonArray = obj.getJSONArray("in_app");
            if (jsonArray.size() == 0) {
                throw new ServiceException(PaymentStatusCode.PARAM_WRONG);
            }
            JSONObject inAppObj = jsonArray.getJSONObject(0);

            String transactionId = inAppObj.getString("transaction_id");
            String originalOrderId = inAppObj.getString("original_transaction_id");
            String product_id = inAppObj.getString("product_id");

            if (StringUtils.isEmpty(transactionId)) {
                throw new ServiceException(PaymentStatusCode.PARAM_WRONG);
            }
            OrderPayDTO orderPayDTO = orderPayService.getByTransactionId(transactionId);
            //判断交易事物id是否使用过
            if (orderPayDTO == null) {
                orderPayDTO = orderPayService.getByPaySn(applePayDTO.getPaySn());
                if (orderPayDTO.getPayState() == 1) {
                    throw new ServiceException(PaymentStatusCode.ORDER_REPEAT_PAY);
                } else {
                    int result = orderPayService.updatePayStateByPaySn(applePayDTO.getPaySn(), transactionId);
                    if (result == 0) {
                        //todo 修改订单支付表状态失败，需要记录日志
                        return false;
                    } else {
                        // 获取子订单保存支付流水
                        PaymentTallyDTO paymentTallyDTO = new PaymentTallyDTO();
                        paymentTallyDTO.setId(IdGenerator.defaultSnowflakeId());
                        paymentTallyDTO.setPaymentCode("applePay");
                        paymentTallyDTO.setPaymentName("苹果内购支付");
                        // 参数封装
                        paymentTallyDTO.setTransactionStatus(PaymentCodeEnum.TRANSACTION_STATUS_OUT.value());
                        paymentTallyDTO.setPaymentStatus(PaymentTallyEnum.PAYMENTTALLY_STATE_SUCCESS.value());
                        // 区分普通支付和充值
                        paymentTallyDTO.setTradeType(PaymentTallyEnum.TRADE_TYPE_RECHANGE.value());
                        // 充值修改用户余额和余额变更记录
                        this.rechargeBalance(orderPayDTO);

                        paymentTallyDTO.setTradeSn(transactionId);
                        paymentTallyDTO.setPaymentSn(applePayDTO.getPaySn());
                        paymentTallyDTO.setPaymentAmount(orderPayDTO.getPayTotalAmount());
                        paymentTallyDTO.setBalanceAmount(orderPayDTO.getBalanceAmount());
                        paymentTallyDTO.setOrderSn(orderPayDTO.getOrderSn());
                        paymentTallyDTO.setBuyerId(orderPayDTO.getBuyerId());
                        paymentTallyDTO.setBuyerName(orderPayDTO.getBuyerName());
                        paymentTallyDTO.setIsSplit(orderPayDTO.getIsSplit());
                        log.info("支付流水保存参数：paymentTallyDTO:{}", paymentTallyDTO);
                        paymentTallyService.save(paymentTallyDTO);
                        return true;

                    }

                }
            } else {
                throw new ServiceException(PaymentStatusCode.TRANSACTION_ID_IS_USED);
            }

        } else if (status == 21007) {
            throw new ServiceException(PaymentStatusCode.ENV_WRONG);
        } else {
            throw new ServiceException(PaymentStatusCode.PARAM_WRONG);
        }

    }


}
