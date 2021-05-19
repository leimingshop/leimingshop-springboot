/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.ImageCheckUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.utils.file.Base64Util;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.dto.reason.ReasonDescriptionDTO;
import com.leimingtech.enums.picture.ErrorCodeEnum;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.order.RedisConstants;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsDTO;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplateDetailDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateRuleDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.invoice.OrderInvoiceSaveDTO;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.dto.reduce.ReduceActivityDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.evaluate.GoodsEnum;
import com.leimingtech.modules.enums.freight.template.FreightTemplateEnum;
import com.leimingtech.modules.enums.goods.GoodsSpecStatusEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.member.MemberAddressEnum;
import com.leimingtech.modules.enums.order.DevlierTypeEnum;
import com.leimingtech.modules.enums.order.FreightRuleEnum;
import com.leimingtech.modules.enums.order.SubmitOrderErrorEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.service.address.MemberAddressService;
import com.leimingtech.modules.service.coupons.MemberCouponsService;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderLogisticsLogService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.statuscode.OrderStatusCode;
import com.leimingtech.modules.vo.order.PCOrderPageVO;
import com.leimingtech.modules.vo.order.PcOrderDetailVO;
import com.leimingtech.moudle.order.after.vo.AvailableAfterSaleOrderGoodsVo;
import com.leimingtech.moudle.order.code.PcOrderCode;
import com.leimingtech.moudle.order.vo.ReasonDescriptionVO;
import com.leimingtech.security.SecurityCurrentUser;
import com.leimingtech.service.reason.ReasonDescriptionService;
import com.leimingtech.upload.dto.Base64FileDTO;
import com.leimingtech.upload.dto.Base64PictureDTO;
import com.leimingtech.upload.dto.UploadDTO;
import com.leimingtech.upload.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.*;

/**
 * 订单模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("order")
@Api(tags = "订单")
public class OrderController {

    private static final String MAP_PARANS_CODE_STR = "code";
    private static final String MAP_PARANS_URL_STR = "url";
    private static final String MAP_PARANS_SIZE_STR = "size";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(OrderController.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLogisticsLogService orderLogisticsLogService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MemberCouponsService memberCouponsService;

    @Autowired
    private FreightTemplateService freightTemplateService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ReasonDescriptionService reasonDescriptionService;

    @Autowired
    private ReduceActivityService reduceActivityService;

    @GetMapping("page")
    @ApiOperation("pc端订单分页(取值规则：1.普通订单正常取值 2.父子订单的话，appStatus(0,10)正常取，其他情况取)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "keyWord", value = "搜索条件 商品名称/商品编号/订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "appStatus", value = "前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成", paramType = "query", dataType = "int")
    })
    @LogContext(code = PcOrderCode.ORDER_LIST_CODE, note = PcOrderCode.ORDER_LIST_DESC)
    public Result<PageData<PCOrderPageVO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<PageData<PCOrderPageVO>>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
//        Long buyerId = 1234408545600933889L;
        PageData<PCOrderPageVO> page = orderService.pagePC(params, buyerId);

        Map<String, String> logMap = new HashMap<>(2);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            logMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        if (buyerId != null) {
            logMap.put("buyerId", buyerId.toString());
        }
        mlogger.info(OrderStatusCode.H5_ORDER_PAGE_SUCCESS_CODE, OrderStatusCode.H5_ORDER_PAGE_SUCCESS_MESSAGE);

        return new Result<PageData<PCOrderPageVO>>().ok(page, "查询成功");
    }

    /**
     * 查询订单详情
     *
     * @param orderId: 订单ID
     * @return H5页面展示的订单信息
     * @date 2019/11/6 17:43
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("{id}")
    @ApiOperation("pc订单详情")
    @LogContext(code = PcOrderCode.ORDER_DETAIL_CODE, note = PcOrderCode.ORDER_DELETE_DESC)
    public Result<PcOrderDetailVO> get(@PathVariable("id") Long orderId) {
        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<PcOrderDetailVO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        OrderDTO orderDetail = orderService.findOrderDetail(orderId, buyerId);
        if (orderDetail == null) {
            return new Result<PcOrderDetailVO>().error("抱歉，订单信息不存在");
        }

        PcOrderDetailVO pcOrderDetailVO = orderService.findPCOrderDetail(orderId, buyerId);
        pcOrderDetailVO.setCurrentTime(System.currentTimeMillis());

        return new Result<PcOrderDetailVO>().ok(pcOrderDetailVO, "查询详情成功");
    }

    /**
     * 确认收货
     *
     * @param id 订单id
     *           dy
     */
    @PutMapping("/confirm/receipt/{id}")
    @ApiOperation("确认收货")
    @LogContext(code = PcOrderCode.ORDER_CONFIRM_RECEIPT_CODE, note = PcOrderCode.ORDER_CONFIRM_RECEIPT_DESC)
    public Result confirmReceipt(@PathVariable("id") Long id) {

        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("orderId", id.toString());
        logMap.put("buyerId", buyerId.toString());

        Map<String, Object> res = orderService.confirmReceipt(id, buyerId);

        if (CollectionUtil.isNotEmpty(res)) {
            return new Result().error(Integer.valueOf(res.get("code").toString()), res.get("msg").toString());
        }

        mlogger.info(OrderStatusCode.H5_ORDER_COMFIRM_RECEIPT_SUCCESS_CODE, OrderStatusCode.H5_ORDER_COMFIRM_RECEIPT_SUCCESS_MESSAGE, logMap);
        return new Result().ok(null, "确认收货成功");
    }

    /**
     * 取消订单
     *
     * @param dto 取消订单dto
     *            dy
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    @LogContext(code = PcOrderCode.ORDER_CANCEL_CODE, note = PcOrderCode.ORDER_CANCEL_DESC)
    public Result cancelOrder(@RequestBody OrderCancelDTO dto) {

        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("dto", dto.toString());
        logMap.put("buyerId", buyerId.toString());

        orderService.cancelOrder(dto, buyerId);

        mlogger.info(OrderStatusCode.H5_ORDER_CANCEL_SUCCESS_CODE, OrderStatusCode.H5_ORDER_CANCEL_SUCCESS_MESSAGE, logMap);
        return new Result().ok(null, "订单取消成功");
    }

    /**
     * 订单删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ApiOperation("删除订单")
    @LogContext(code = PcOrderCode.ORDER_DETAIL_CODE, note = PcOrderCode.ORDER_DELETE_DESC)
    public Result delOrder(@PathVariable("id") Long id) {

        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        orderService.delOrder(id, buyerId);

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("orderId", id.toString());

        mlogger.info(OrderStatusCode.H5_ORDER_DELETE_SUCCESS_CODE, OrderStatusCode.H5_ORDER_DELETE_SUCCESS_MESSAGE, logMap);
        return new Result().ok(null, "删除成功");
    }

    @GetMapping("logistics/{orderId}")
    @ApiOperation("根据订单id查询物流详细信息")
    public Result<OrderLogisticsDTO> logistics(@PathVariable("orderId") Long orderId) {

        AssertUtils.isNull(orderId, "订单ID不能为空");

        Map<String, Object> orderMap = new HashMap<>(2);
        orderMap.put("id", orderId);
        OrderDTO orderDTO = orderService.getOrderByMap(orderMap);
        if (!DevlierTypeEnum.EXPRESS.value().equals(orderDTO.getDevlierType())) {
            return new Result<OrderLogisticsDTO>().error(ErrorCode.FORBIDDEN, "无物流信息！");
        }

        OrderLogisticsDTO logistics = orderLogisticsLogService.findLogistics(String.valueOf(orderId));
        if (null == logistics) {
            return new Result<OrderLogisticsDTO>().error(400, "订单不存在");
        }
        return new Result<OrderLogisticsDTO>().ok(logistics);
    }


    /**
     * @param :订单编号
     * @Author: SWH ab4856812@163.com
     * @Description:订单可申请售后服务列表
     * @Date: 2019/6/17 19:35
     * @Version: V1.0
     */
    @GetMapping("/available/aftersale")
    @ApiOperation("订单可申请售后服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buyTime", value = "下单时间查询（0:全部，1:一个月内，2:一个月至六个月，3:六个月至一年,4:一年以上，5:自定义时间）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "createTime", value = "下单开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "下单结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<AvailableAfterSaleOrderDTO>> availAfterSaleOrder(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取buyerId
        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<PageData<AvailableAfterSaleOrderDTO>>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        if (null != buyerId) {
            params.put("buyerId", String.valueOf(buyerId));
        }
        String createTime = (String) params.get("createTime");
        if (StringUtils.isNotBlank(createTime)) {
            createTime = createTime + " 00:00:00";
            params.put("createTime", createTime);
        }
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
            params.put("endTime", endTime);
        }
        PageData<AvailableAfterSaleOrderDTO> pagelist = orderService.findAvailAfterList(params);

        Map<String, String> logMap = new HashMap<>(2);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            logMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        if (buyerId != null) {
            logMap.put("buyerId", buyerId.toString());
        }
        mlogger.info(OrderStatusCode.H5_ORDER_AVAILABLE_AFTERSALE_PAGE_SUCCESS_CODE,
                OrderStatusCode.H5_ORDER_AVAILABLE_AFTERSALE_PAGE_SUCCESS_MESSAGE, logMap);

        return new Result<PageData<AvailableAfterSaleOrderDTO>>().ok(pagelist, "查询成功");
    }

    /**
     * @param :订单项目编号
     * @Author: SWH ab4856812@163.com
     * @Description:售后可申请订单商品详情接口
     * @Date: 2019/6/17 19:35
     * @Version: V1.0
     */
    @GetMapping("/available/aftersale/{id}")
    @ApiOperation("订单中可申请售后商品详情")
    public Result<AvailableAfterSaleOrderGoodsVo> getAvailGoodsDetail(@PathVariable("id") Long id) {
        AvailableAfterSaleOrderGoodsDTO availGoodsDetail = orderGoodsService.getAvailGoodsDetail(id);
        if (null == availGoodsDetail) {
            return new Result<AvailableAfterSaleOrderGoodsVo>().error(500, "查询详情数据为空");
        }

        Map<String, String> logMap = new HashMap<>(1);
        logMap.put("orderStatus", id.toString());
        AvailableAfterSaleOrderGoodsVo afterSaleOrderGoodsVo = ConvertUtils.sourceToTarget(availGoodsDetail, AvailableAfterSaleOrderGoodsVo.class);
        mlogger.info(OrderStatusCode.H5_ORDER_AVAILABLE_AFTERSALE_GOODS_SUCCESS_CODE,
                OrderStatusCode.H5_ORDER_AVAILABLE_AFTERSALE_GOODS_SUCCESS_MESSAGE, logMap);
        return new Result<AvailableAfterSaleOrderGoodsVo>().ok(afterSaleOrderGoodsVo, "查询详情成功");
    }


    @PostMapping("buynow")
    @ApiOperation("立即购买提交订单")
    @LogContext(code = PcOrderCode.ORDER_BUY_NOW_CODE, note = PcOrderCode.ORDER_BUY_NOW_DESC)
    public Result<OrderSaveResultDTO> saveOrderNow(@RequestBody InsertNowOrderDTO orderDTO) {

        // 效验数据
        ValidatorUtils.validateEntity(orderDTO, AddGroup.class);

        // 转化提交订单实体，用于封住商品数据，会员数据，避免重复查询
        if (orderDTO.getActivityType() == null) {
            orderDTO.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
        }
        InsertNowOrderVoDTO insertNowOrderVoDTO = ConvertUtils.sourceToTarget(orderDTO, InsertNowOrderVoDTO.class);

//        Long buyerId = 1192326496660873218L;
        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<OrderSaveResultDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

//        Long buyerId = 1234408545600933889L;

        // 新建订单保存结果实体
        OrderSaveResultDTO orderSaveResultDTO = new OrderSaveResultDTO();
        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("orderDTO", orderDTO.toString());
        if (buyerId != null) {
            logMap.put("buyerId", String.valueOf(buyerId));
        }

        // 2.查询商品规格信息
        GoodsSpecDTO goodsSpecDTO = Optional.ofNullable(goodsSpecService.get(orderDTO.getSpecId())).orElse(new GoodsSpecDTO());
        GoodsDTO goodsDTO = Optional.ofNullable(goodsService.findGoodsBySpecId(goodsSpecDTO.getId())).orElse(new GoodsDTO());

        log.debug("立即购买提交订单商品规格信息，specId：{}，goodsSpecDTO:{}", orderDTO.getSpecId(), goodsSpecDTO);

        List<ErrorOrderMsgDTO> errorOrderMsgDTOList = new ArrayList<>();

        // 3.校验商品和规格信息
        if (goodsSpecDTO.getIsEnable() == GoodsSpecStatusEnum.GOODS_SPEC_ENABLE.value()
                && goodsSpecDTO.getSpecShow() == GoodsSpecStatusEnum.GOODS_SPEC_SHOW_UP.value()) {
            // 规格状态正常
            if (orderDTO.getGoodsNum() > goodsSpecDTO.getSpecStorage() && orderDTO.getActivityType() == ActivityTypeEnum.NO_ACTIVITY.value()) {
                // 规格库存不足
                mlogger.info(OrderStatusCode.BUYNOW_GOODS_NO_STORAGE.getCode(),
                        OrderStatusCode.BUYNOW_GOODS_NO_STORAGE.getMessage(), logMap);

                // 封装错误提示信息
                createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品库存不足");
                return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
            } else if (orderDTO.getActivityType() != ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() && orderDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value() && orderDTO.getSpecSellPrice().compareTo(goodsSpecDTO.getSpecSellPrice()) != 0) {
                // 规格价格变化 不校验秒杀商品
                mlogger.info(OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE.getCode(),
                        OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE.getMessage(), logMap);

                // 封装错误提示信息
                createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品价格已发生变化");
                return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
            } else {
                if (goodsDTO.getGoodsStatus() != GoodsStatusEnum.GOODS_AUDIT_PASS.value() || goodsDTO.getGoodsShow() != GoodsStatusEnum.GOODS_SHOW_ON.value()) {
                    mlogger.info(OrderStatusCode.BUYNOW_GOODS_NO_EXIST.getCode(),
                            OrderStatusCode.BUYNOW_GOODS_NO_EXIST.getMessage(), logMap);

                    // 封装错误提示信息
                    createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                            SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品已下架");
                    return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                }
                //立即购买提交订单
                //如果是否开票的标记不为0(不开票),则该订单需要开发票，校验该订单商品是否可以开票
                if (null != insertNowOrderVoDTO.getInvoiceType() && !insertNowOrderVoDTO.getInvoiceType().equals(0)) {
                    //该商品不能开发票
                    if (goodsDTO.getInvoiceFlag() == 0) {
                        createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，该商品不能开发票");
                        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                    }
                    if (!goodsDTO.getInvoiceContent().contains(insertNowOrderVoDTO.getInvoiceContent().toString()) ||
                            !goodsDTO.getInvoiceType().contains(insertNowOrderVoDTO.getInvoiceType().toString())) {
                        createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，该商品不能开发票");
                        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                    }
                } else {
                    insertNowOrderVoDTO.setInvoiceType(0);
                }

                // 校验满减活动信息
                if (null != orderDTO.getActivityType() && orderDTO.getActivityType() == ActivityTypeEnum.REDUCE_ACTIVITY.value()) {
                    ReduceActivityDTO reduceActivityDTO = reduceActivityService.get(orderDTO.getActivityId());
                    if (null == reduceActivityDTO) {
                        createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，该满减活动不存在，请重新提交订单！");
                        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                    } else if (reduceActivityDTO.getActivityState().equals(ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value())) {
                        createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，该满减活动未开始，请重新提交订单！");
                        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                    } else if (reduceActivityDTO.getActivityState().equals(ReduceActivityEnum.ACTIVITY_STATE_END.value())) {
                        createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，该满减活动已结束，请重新提交订单！");
                        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                    }
                }

                // 商品参数信息
                insertNowOrderVoDTO.setGoodsId(goodsDTO.getId());
                insertNowOrderVoDTO.setSpecMainPicture(goodsSpecDTO.getSpecMainPicture());
                insertNowOrderVoDTO.setSpecName(goodsSpecDTO.getSpecName());
                insertNowOrderVoDTO.setBrandId(goodsDTO.getBrandId());
                insertNowOrderVoDTO.setBrandName(goodsDTO.getBrandName());
                insertNowOrderVoDTO.setSpecCostPrice(goodsSpecDTO.getSpecCostPrice());
                insertNowOrderVoDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
                insertNowOrderVoDTO.setStoreId(goodsDTO.getStoreId());
                insertNowOrderVoDTO.setStoreName(goodsDTO.getStoreName());
                insertNowOrderVoDTO.setVirtualFlag(goodsDTO.getVirtualFlag());
                insertNowOrderVoDTO.setDevlierType(goodsDTO.getExpressFlag());
                insertNowOrderVoDTO.setShippingFee(BigDecimal.ZERO);
            }
        } else {
            mlogger.info(OrderStatusCode.BUYNOW_GOODS_NO_EXIST.getCode(),
                    OrderStatusCode.BUYNOW_GOODS_NO_EXIST.getMessage(), logMap);
            // 封装错误提示信息
            createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品已下架");

            return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
        }

        // 虚拟订单处理用户地址
        if (StringUtils.isNotBlank(orderDTO.getMemberName()) && StringUtils.isNotBlank(orderDTO.getMemberMobile())) {
            MemberAddressDTO memberAddressDTO = new MemberAddressDTO();
            memberAddressDTO.setMemberId(buyerId);
            memberAddressDTO.setMobPhone(orderDTO.getMemberMobile());
            memberAddressDTO.setTrueName(orderDTO.getMemberName());
            Long addressId = IdGenerator.defaultSnowflakeId();
            memberAddressDTO.setId(addressId);
            memberAddressDTO.setDefaultFlag(MemberAddressEnum.IS_NOT_DEFAULT.value());
            memberAddressService.save(memberAddressDTO);
            orderDTO.setAddressId(addressId);
            insertNowOrderVoDTO.setAddressId(addressId);
        }

        // 查询会员地址
        MemberAddressDTO memberAddressDTO = memberAddressService.get(orderDTO.getAddressId());
        log.debug("立即购买提交订单会员订单地址信息，addressId:{}", orderDTO.getAddressId());
        if (memberAddressDTO == null) {
            mlogger.info(OrderStatusCode.BUYNOW_ADDRESS_NO_EXIST.getCode(),
                    OrderStatusCode.BUYNOW_ADDRESS_NO_EXIST.getMessage(), logMap);
            orderSaveResultDTO.setMsg("用户地址不存在");
            orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.MEMBER_CHECK_FAILED.value());
            return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
        }

        // 校验运费模板
        if (GoodsEnum.EXPRESS_FLAG_YES.value() == goodsDTO.getExpressFlag() && goodsDTO.getFreightTemplateId() != null) {
            insertNowOrderVoDTO.setFreightTemplateId(goodsDTO.getFreightTemplateId());
            // 查询运费模板
            AdminFreightTemplateDetailDTO detailDTO = freightTemplateService.adminDetail(goodsDTO.getFreightTemplateId(), goodsDTO.getStoreId());

            if (detailDTO == null) {
                // 封装错误提示信息
                createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品运费模板不存在");
                return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
            }

            Integer ruleType = detailDTO.getTemplateType();

            // 匹配下单的地区
            List<FreightTemplateRuleDTO> ruleList = detailDTO.getFreightTemplateRuleList();
            List<BigDecimal> freightAmountList = new ArrayList<>();
            // 该区域是否可配送标识
            boolean flag = true;
            for (int i = ruleList.size() - 1; i >= 0; i--) {
                FreightTemplateRuleDTO freightTemplateRuleDTO = ruleList.get(i);
                if (freightTemplateRuleDTO.getAreaIdsArr().contains(0L)) {
                    // 计算某规则运费金额
                    BigDecimal goodsTotalFreight = this.simplePieceFreightAmount(insertNowOrderVoDTO.getGoodsNum(), freightTemplateRuleDTO, ruleType, goodsSpecDTO.getSpecWeight());
                    freightAmountList.add(goodsTotalFreight);
                    insertNowOrderVoDTO.setShippingFee(goodsTotalFreight);
                    flag = false;
                    break;
                } else {
                    boolean provinceFlag = memberAddressDTO.getProvinceId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getProvinceId());
                    boolean cityFlag = memberAddressDTO.getCityId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getCityId());
                    boolean areaFlag = memberAddressDTO.getAreaId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getAreaId());
                    if (provinceFlag || cityFlag || areaFlag) {
                        // 计算某规则运费金额
                        BigDecimal goodsTotalFreight = this.simplePieceFreightAmount(insertNowOrderVoDTO.getGoodsNum(), freightTemplateRuleDTO, ruleType, goodsSpecDTO.getSpecWeight());
                        freightAmountList.add(goodsTotalFreight);
                        insertNowOrderVoDTO.setShippingFee(goodsTotalFreight);
                        flag = false;
                        break;
                    }
                }
            }

            if (flag) {
                // 封装错误提示信息
                createBuynowGoodsErrorMsg(orderDTO, orderSaveResultDTO, goodsSpecDTO, goodsDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品超出配送范围");
                return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
            }

            // 运费
            BigDecimal goodsTotalFreight = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(freightAmountList)) {
                if (FreightRuleEnum.RULE_TYPE_CEIL.value() == ruleType) {
                    // 最高运费
                    goodsTotalFreight = freightAmountList.stream().max(BigDecimal::compareTo).get();
                } else {
                    // 最低运费
                    goodsTotalFreight = freightAmountList.stream().min(BigDecimal::compareTo).get();
                }
                insertNowOrderVoDTO.setShippingFee(goodsTotalFreight);
            }
        }

        // 校验使用的优惠券
        if (insertNowOrderVoDTO.getMemberCouponsId() != null) {
            MemberCouponsDTO memberCouponsDTO = memberCouponsService.get(insertNowOrderVoDTO.getMemberCouponsId());
            if (memberCouponsDTO == null || !memberCouponsDTO.getMemberId().equals(buyerId)) {
                // 未查询到会员优惠券
                mlogger.info(OrderStatusCode.SAVE_ORDER_NO_COUPONS.getCode(),
                        OrderStatusCode.SAVE_ORDER_NO_COUPONS.getMessage(), logMap);
                orderSaveResultDTO.setMsg(OrderStatusCode.SAVE_ORDER_NO_COUPONS.getMessage());
                orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.OTHER_ACTIVITY_CHECK_FAILED.value());
                return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
            } else if (CouponsEnum.COUPON_CAN_USE.value() != memberCouponsDTO.getCouponsState()) {
                // 优惠券不可用
                mlogger.info(OrderStatusCode.SAVE_ORDER_COUPONS_CANNT_USE.getCode(),
                        OrderStatusCode.SAVE_ORDER_COUPONS_CANNT_USE.getMessage(), logMap);
                orderSaveResultDTO.setMsg(OrderStatusCode.SAVE_ORDER_COUPONS_CANNT_USE.getMessage());
                orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.OTHER_ACTIVITY_CHECK_FAILED.value());
                return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
            }
        }

        // 提交订单操作
        orderSaveResultDTO = orderService.saveOrderNow(insertNowOrderVoDTO, buyerId);
        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
    }

    /**
     * 功能描述：
     * <计算某规则运费金额>
     *
     * @param number                 商品数量
     * @param freightTemplateRuleDTO 运费模板规则
     * @return
     * @date 2020/5/7 10:07
     * @author 刘远杰
     **/
    private BigDecimal simplePieceFreightAmount(Integer number, FreightTemplateRuleDTO freightTemplateRuleDTO, Integer ruleType, Double specWeight) {
        // 计算运费
        BigDecimal goodsTotalFreight = BigDecimal.ZERO;
        if (FreightTemplateEnum.TEMPLATE_TYPE_PIECE.value() == ruleType) {
            if (Integer.parseInt(freightTemplateRuleDTO.getFirstFee()) >= number) {
                // 首件范围内运费计算 - 首件运费为当前运费金额
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount();
            } else {
                // 超出首件范围 首件运费 + 超出部分运费
                // 超出首件商品数量 = 商品总数量 - 首件数量
                BigDecimal additionalNumber = new BigDecimal(number - Integer.parseInt(freightTemplateRuleDTO.getFirstFee()));
                BigDecimal additionalFee = new BigDecimal(freightTemplateRuleDTO.getAdditionalFee());
                // 超出续件倍数 = 超出数量 / 续件数量 向上取证
                BigDecimal ladder = additionalNumber.divide(additionalFee, 0, BigDecimal.ROUND_UP);
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount().add(freightTemplateRuleDTO.getAdditionalAmount().multiply(ladder));
            }
        } else {
            // 累加商品重量
            BigDecimal numDec = new BigDecimal(number);
            BigDecimal weightDec = new BigDecimal(specWeight);
            BigDecimal totalWeight = weightDec.multiply(numDec).setScale(2, BigDecimal.ROUND_UP);
            log.info("商品重量:{}", totalWeight);
            // 计算重量运费
            if (totalWeight.compareTo(new BigDecimal(freightTemplateRuleDTO.getFirstFee())) == -1) {
                // 不超过首重作为当前商品运费
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount();
                log.info("初始运费:{}", goodsTotalFreight);
            } else {
                // 超出重量
                BigDecimal addWeight = totalWeight.subtract(new BigDecimal(freightTemplateRuleDTO.getFirstFee()));
                log.info("超出重量:{}", addWeight);
                BigDecimal additionalFee = new BigDecimal(freightTemplateRuleDTO.getAdditionalFee());
                log.info("续重kg:{}", additionalFee);
//                BigDecimal ladder = addWeight.divide(additionalFee, 0, BigDecimal.ROUND_DOWN);
                BigDecimal[] ladder = addWeight.divideAndRemainder(additionalFee);
                log.info("ladder:{}", JSON.toJSONString(ladder));
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount().add(freightTemplateRuleDTO.getAdditionalAmount().multiply(ladder[0]));
                if (ladder[1].compareTo(BigDecimal.ZERO) == 1) {
                    goodsTotalFreight = goodsTotalFreight.add(freightTemplateRuleDTO.getAdditionalAmount());
                }
                log.info("超出运费:{}", goodsTotalFreight);
            }
        }
        return goodsTotalFreight;
    }


    /**
     * 购物车保存订单API
     *
     * @return 订单确认id
     * @date 2019/6/25 12:49
     * @author lixiang
     **/
    @PostMapping("cart")
    @ApiOperation("购物车保存订单")
    @LogContext(code = PcOrderCode.ORDER_CART_SAVE_ORDER_CODE, note = PcOrderCode.ORDER_CART_SAVE_ORDER_DESC)
    public Result<OrderSaveResultDTO> saveOrderFromCart(@RequestBody InsertCartOrderDTO insertCartOrderDTO) {
        // 效验数据
        ValidatorUtils.validateEntity(insertCartOrderDTO, AddGroup.class);
        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<OrderSaveResultDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        // 虚拟订单处理用户地址
        if (StringUtils.isNotBlank(insertCartOrderDTO.getMemberName()) && StringUtils.isNotBlank(insertCartOrderDTO.getMemberMobile())) {
            MemberAddressDTO memberAddressDTO = new MemberAddressDTO();
            memberAddressDTO.setMemberId(buyerId);
            memberAddressDTO.setMobPhone(insertCartOrderDTO.getMemberMobile());
            memberAddressDTO.setTrueName(insertCartOrderDTO.getMemberName());
            Long addressId = IdGenerator.defaultSnowflakeId();
            memberAddressDTO.setId(addressId);
            memberAddressDTO.setDefaultFlag(MemberAddressEnum.IS_NOT_DEFAULT.value());
            memberAddressService.save(memberAddressDTO);
            insertCartOrderDTO.setAddressId(addressId);
        }
        Long addressId = insertCartOrderDTO.getAddressId();
        String orderMessage = insertCartOrderDTO.getOrderMessage();
        OrderSaveResultDTO orderSaveResultDTO = new OrderSaveResultDTO();
        // 校验用户地址
        if (addressId == null) {
            orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.MEMBER_CHECK_FAILED.value());
            orderSaveResultDTO.setMsg("用户地址信息有误，请重新选择！");
            return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
        }
        log.info("购物车提交订单会员订单地址id，addressId:{}", addressId);
        // 校验会员优惠券
        Map<String, Long> memberCouponsId = new HashMap<>(8);
        if (StringUtils.isNotBlank(insertCartOrderDTO.getMemberCouponsId())) {
            JSONObject jsonObject = JSONObject.parseObject(insertCartOrderDTO.getMemberCouponsId());
            for (String key : jsonObject.keySet()) {
                Object o = jsonObject.get(key);
                if (o != null) {
                    memberCouponsId.put(key, Long.parseLong(o.toString()));
                    MemberCouponsDTO memberCouponsDTO = memberCouponsService.get(Long.parseLong(o.toString()));
                    if (memberCouponsDTO == null || !memberCouponsDTO.getMemberId().equals(buyerId)) {
                        // 未查询到会员优惠券
                        mlogger.info(OrderStatusCode.SAVE_ORDER_NO_COUPONS.getCode(),
                                OrderStatusCode.SAVE_ORDER_NO_COUPONS.getMessage());
                        orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.OTHER_ACTIVITY_CHECK_FAILED.value());
                        orderSaveResultDTO.setMsg(OrderStatusCode.SAVE_ORDER_NO_COUPONS.getMessage());
                        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                    } else if (CouponsEnum.COUPON_CAN_USE.value() != memberCouponsDTO.getCouponsState()) {
                        // 优惠券不可用
                        mlogger.info(OrderStatusCode.SAVE_ORDER_COUPONS_CANNT_USE.getCode(),
                                OrderStatusCode.SAVE_ORDER_COUPONS_CANNT_USE.getMessage());
                        orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.OTHER_ACTIVITY_CHECK_FAILED.value());
                        orderSaveResultDTO.setMsg(OrderStatusCode.SAVE_ORDER_COUPONS_CANNT_USE.getMessage());
                        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
                    }
                }
            }
        }
        List<OrderInvoiceSaveDTO> invoiceSaveDTOList;
        if (CollectionUtils.isEmpty(insertCartOrderDTO.getOrderInvoiceSaveDTOS())) {
            invoiceSaveDTOList = new ArrayList<>();
        } else {
            invoiceSaveDTOList = insertCartOrderDTO.getOrderInvoiceSaveDTOS();
        }
        //提交订单
        log.info("购物车提交订单参数，buyerId:{}, addressId:{}, orderMessage:{},memberCoupons:{},orderPlatForm:{}orderInvoiceSaveDTOS:{}",
                buyerId, addressId, orderMessage, insertCartOrderDTO.getMemberCouponsId(), insertCartOrderDTO.getOrderPlatform(), invoiceSaveDTOList);
        InsertCartSaveOrderVoDTO insertCartSaveOrderVoDTO = new InsertCartSaveOrderVoDTO();
        insertCartSaveOrderVoDTO.setAddressId(addressId);
        insertCartSaveOrderVoDTO.setBuyerId(buyerId);
        insertCartSaveOrderVoDTO.setOrderMessage(orderMessage);
        insertCartSaveOrderVoDTO.setMemberCouponsId(insertCartOrderDTO.getMemberCouponsId());
        insertCartSaveOrderVoDTO.setOrderInvoiceSaveDTOS(invoiceSaveDTOList);
        insertCartSaveOrderVoDTO.setOrderPlatform(insertCartOrderDTO.getOrderPlatform());
        if (null != insertCartOrderDTO.getUseBalance()) {
            insertCartSaveOrderVoDTO.setUseBalance(insertCartOrderDTO.getUseBalance());
        } else {
            insertCartSaveOrderVoDTO.setUseBalance(0);
        }
        orderSaveResultDTO = orderService.saveOrderToCart(insertCartSaveOrderVoDTO);

        return new Result<OrderSaveResultDTO>().ok(orderSaveResultDTO);
    }

    /**
     * 查询订单保存结果接口
     *
     * @param orderId: 订单Id
     * @return 订单支付信息
     * @date 2019/6/28 15:02
     * @author lixiang
     **/
    @GetMapping("check/{orderId}")
    @ApiOperation("查询订单保存是否成功")
    public Result<SaveOrderRedisDTO> checkOrderSave(@PathVariable("orderId") Long orderId) {

        AssertUtils.isNull(orderId, "订单ID不能为空");

        // 1.从缓存中获取订单保存结果
        Object obj = redisUtils.get(RedisConstants.SAVE_ORDER_PREFIX + orderId);
        log.info("获取缓存订单保存结果，orderId:{}", orderId);
        if (obj != null) {
            SaveOrderRedisDTO saveOrderRedisDTO = (SaveOrderRedisDTO) obj;
            if (RedisConstants.SUSSESS == saveOrderRedisDTO.getResultCode()) {
                // 保存成功
                return new Result<SaveOrderRedisDTO>().ok(saveOrderRedisDTO, "订单提交成功！请尽快完成支付。");
            } else if (RedisConstants.WAITING == saveOrderRedisDTO.getResultCode()) {
                return new Result<SaveOrderRedisDTO>().error(ErrorCode.METHOD_EXCUTEING, "订单保存中");
            } else {
                return new Result<SaveOrderRedisDTO>().error(ErrorCode.INTERNAL_SERVER_ERROR, "订单保存失败");
            }
        } else {
            return new Result<SaveOrderRedisDTO>().error(ErrorCode.INTERNAL_SERVER_ERROR, "订单号不存在或已经支付完成");
        }
    }

    /**
     * 功能描述:
     * 〈base64图片上传〉
     *
     * @param id               分组id
     * @param base64PictureDTO base64文件实体
     * @author : 刘远杰
     */
    @PostMapping("base64")
    @ApiOperation("保存base64图片")
    public Result<UploadDTO> save(
            @RequestParam(required = false, value = "id") Long id,
            @RequestBody Base64PictureDTO base64PictureDTO) throws Exception {
        Long buyerId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } else {
            return new Result<UploadDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        Result<UploadDTO> result = new Result<>();
        // 限制图片格式
        String imageTypes = "gif,jpg,jpeg,png,bmp,PNG,JEPG,JPG";
        //效验数据
        ValidatorUtils.validateEntity(base64PictureDTO, AddGroup.class, DefaultGroup.class);
        // 图片大小、格式校验
        Map<String, Object> imageMap = ImageCheckUtils.isImage(base64PictureDTO.getImgStr());

        // 获得图片格式
        String imageType = String.valueOf(imageMap.get("imageType"));
        // 获取图片大小
        Long picSize = Long.valueOf(String.valueOf(imageMap.get("imageSize")));

        // 将图片base64字符串转化为byte数组
        byte[] base64ByteArr = Base64Util.imgBase64ToByte(base64PictureDTO.getImgStr());

        // 构建文件上传实体
        Base64FileDTO base64FileDTO = new Base64FileDTO();
        if (StringUtils.isNotBlank(base64PictureDTO.getPictureName())) {
            base64FileDTO.setFileRealName(base64PictureDTO.getPictureName());
        } else {
            base64FileDTO.setFileRealName(System.currentTimeMillis() + "." + imageType);
        }
        base64FileDTO.setFileSize(picSize);
        base64FileDTO.setFileSuffix(imageType);
        base64FileDTO.setBase64ByteArr(base64ByteArr);
        Map<String, Object> resultMap = uploadService.uploadPicBase64(base64FileDTO);
        if (ErrorCode.SUCCESS == (int) resultMap.get(MAP_PARANS_CODE_STR)) {
            UploadDTO uploadDTO = (UploadDTO) resultMap.get("data");
            if (uploadDTO.getUrl() == null || uploadDTO.getSize() == null) {
                result = new Result<UploadDTO>().error(ErrorCodeEnum.ILLEGAL_IMAGES.value(), "无法获取图片路径和大小");
            } else {
                result = new Result<UploadDTO>().ok(uploadDTO, "上传成功");
            }
        } else {
            result = new Result<UploadDTO>().error((int) resultMap.get("code"), (String) resultMap.get("msg"));
        }

        return result;

    }

    /**
     * 功能描述：
     * <封装提交订单校验错误数据>
     *
     * @param orderDTO             立即购买提交订单参数
     * @param orderSaveResultDTO   立即购买返回结果
     * @param goodsSpecDTO         商品sku
     * @param goodsDTO             商品spu
     * @param errorOrderMsgDTOList 校验不通过商品集合
     * @param checkStatus          校验错误码
     * @param errorMsgTitle        校验错误标题
     * @return
     * @date 2020/4/14 9:47
     * @author 刘远杰
     **/
    private void createBuynowGoodsErrorMsg(InsertNowOrderDTO orderDTO, OrderSaveResultDTO orderSaveResultDTO,
                                           GoodsSpecDTO goodsSpecDTO, GoodsDTO goodsDTO,
                                           List<ErrorOrderMsgDTO> errorOrderMsgDTOList,
                                           int checkStatus, String errorMsgTitle) {
        // 封装错误提示信息
        ErrorGoodsMsgDTO errorGoodsMsgDTO = new ErrorGoodsMsgDTO();
        errorGoodsMsgDTO.setSpuId(goodsDTO.getId());
        errorGoodsMsgDTO.setSkuId(goodsSpecDTO.getId());
        errorGoodsMsgDTO.setGoodsImage(goodsSpecDTO.getSpecMainPicture());
        errorGoodsMsgDTO.setSpecName(goodsSpecDTO.getSpecName());
        errorGoodsMsgDTO.setSpecAttrValueName(goodsSpecDTO.getSpecAttrValueName());
        errorGoodsMsgDTO.setBrandId(goodsDTO.getBrandId());
        errorGoodsMsgDTO.setBrandName(goodsDTO.getBrandName());
        errorGoodsMsgDTO.setStoreId(goodsDTO.getStoreId());
        errorGoodsMsgDTO.setStoreName(goodsDTO.getStoreName());
        errorGoodsMsgDTO.setGoodsNum(orderDTO.getGoodsNum());
        errorGoodsMsgDTO.setPrice(orderDTO.getSpecSellPrice());
        ErrorOrderMsgDTO errorOrderMsgDTO = new ErrorOrderMsgDTO();
        errorOrderMsgDTO.setErrorMsgTitle(errorMsgTitle);
        errorOrderMsgDTO.setErrorGoodsMsgDTOList(Collections.singletonList(errorGoodsMsgDTO));
        errorOrderMsgDTOList.add(errorOrderMsgDTO);

        orderSaveResultDTO.setCheckStatus(checkStatus);
        orderSaveResultDTO.setErrorGoodsList(errorOrderMsgDTOList);
    }

    /**
     * @param params:原因类型（0：退货，1：换货，2：申请退款 ，3：取消订单）
     * @Author: SWH ab4856812@163.com
     * @Description:H5端售后原因接口
     * @Date: 2019/6/20 22:51
     * @Version: V1.0
     */
    @GetMapping("/apply/reason")
    @ApiOperation("H5端售后原因接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（0：退货，1：换货，2：申请退款 ，3：取消订单）", paramType = "query", dataType = "String")
    })
    public Result<List<ReasonDescriptionVO>> aftersaleReason(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<ReasonDescriptionDTO> resultList = reasonDescriptionService.list(params);
        if (CollectionUtils.isEmpty(resultList)) {
            return new Result().error(ResultCodeEnum.WARN.value(), "查询结果为空");
        }
        return new Result<List<ReasonDescriptionVO>>().ok(ConvertUtils.sourceToTarget(resultList, ReasonDescriptionVO.class), "查询成功");
    }


}
