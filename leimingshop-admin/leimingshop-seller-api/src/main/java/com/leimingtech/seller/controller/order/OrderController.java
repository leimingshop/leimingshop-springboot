/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.order;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.reason.ReasonDescriptionDTO;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.dto.virtual.FetchCodeDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDetailDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDetailDTO;
import com.leimingtech.modules.enums.order.DevlierTypeEnum;
import com.leimingtech.modules.service.order.FetchCodeService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.order.VerifyRecordService;
import com.leimingtech.modules.statuscode.OrderStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.reason.ReasonDescriptionService;
import com.leimingtech.service.sys.SysExportManagementService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@RestController
@RequestMapping("order")
@Api(tags = "订单管理")
@Slf4j
public class OrderController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReasonDescriptionService reasonDescriptionService;
    @Autowired
    private SysExportManagementService sysExportManagementService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private FetchCodeService fetchCodeService;

    @Autowired
    private VerifyRecordService verifyRecordService;

    @GetMapping("page")
    @ApiOperation("seller订单分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buyerName", value = "买家名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentCode", value = "支付方式", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "paymentStatus", value = "支付状态 0:未付款;1:已付款", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startCreateDate", value = "下单开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endCreateDate", value = "下单结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "startPaymentTime", value = "交易开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endPaymentTime", value = "交易结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "virtualFlag", value = "虚拟订单标记（0:否，1是）", paramType = "query", dataType = "int")
    })
    public Result<PageData<OrderDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getId() == null || null == sellerDetail.getStoreId()) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Map<String, String> logMap = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            logMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        logMap.put("storeId", sellerDetail.getStoreId().toString());

        Long storeId = sellerDetail.getStoreId();

        // 拼接时间查询条件
        String startCreateDate = (String) params.get("startCreateDate");
        if (StringUtils.isNotBlank(startCreateDate)) {
            startCreateDate = startCreateDate + " 00:00:00";
            params.put("startCreateDate", startCreateDate);
        }
        String endCreateDate = (String) params.get("endCreateDate");
        if (StringUtils.isNotBlank(endCreateDate)) {
            endCreateDate = endCreateDate + " 23:59:59";
            params.put("endCreateDate", endCreateDate);
        }
        String startPaymentTime = (String) params.get("startPaymentTime");
        if (StringUtils.isNotBlank(startPaymentTime)) {
            startPaymentTime = startPaymentTime + " 00:00:00";
            params.put("startPaymentTime", startPaymentTime);
        }
        String endPaymentTime = (String) params.get("endPaymentTime");
        if (StringUtils.isNotBlank(endPaymentTime)) {
            endPaymentTime = endPaymentTime + " 23:59:59";
            params.put("endPaymentTime", endPaymentTime);
        }

        PageData<OrderDTO> page = orderService.page(params, storeId);
        mlogger.info(OrderStatusCode.SELLER_ORDER_PAGE_SUCCESS_CODE, OrderStatusCode.SELLER_ORDER_PAGE_SUCCESS_MESSAGE, logMap);

        return new Result<PageData<OrderDTO>>().ok(page, "查询成功");
    }

    @GetMapping("{id}")
    @ApiOperation("seller根据订单id查询订单详情")
    public Result<AdminOrderDetailDTO> get(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<AdminOrderDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();
        AdminOrderDetailDTO adminOrderDetail = orderService.getAdminOrderDetail(id, null, storeId);
        //判断商家是否备注 备注以后不可备注第二次
        if (null != adminOrderDetail.getSellerRemark() || null != adminOrderDetail.getSellerRemarkGrade()) {
            adminOrderDetail.setDisable(1);
        }
        return new Result<AdminOrderDetailDTO>().ok(adminOrderDetail, "查询详情成功");
    }

    @GetMapping("orderSn/{orderSn}")
    @LogContext(code = OrderStatusCode.SELLER_ORDER_DETAIL_SUCCESS_CODE, note = OrderStatusCode.SELLER_ORDER_DETAIL_SUCCESS_MESSAGE)
    @ApiOperation("seller根据订单编号查询订单详情")
    public Result<AdminOrderDetailDTO> getByOrderSn(@PathVariable("orderSn") Long orderSn, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<AdminOrderDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();
        AdminOrderDetailDTO adminOrderDetail = orderService.getAdminOrderDetailByOrderSn(orderSn, null, storeId);

        return new Result<AdminOrderDetailDTO>().ok(adminOrderDetail, "查询详情成功");
    }

    @GetMapping("activity/detail/orderSn/{orderSn}")
    @ApiOperation("订单活动详情查询")
    public Result<AdminOrderActivityDetailDTO> getOrderActivityDetail(@PathVariable("orderSn") Long orderSn, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<AdminOrderActivityDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();
        AdminOrderActivityDetailDTO orderActivityDetail = orderService.getOrderActivityDetail(orderSn, storeId);
        return new Result<AdminOrderActivityDetailDTO>().ok(orderActivityDetail, "查询详情成功");
    }

    @GetMapping("export")
    @ApiOperation("订单列表导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        // 保存导出记录表 状态为导出中
        String assignmentName = ExcelEnum.ORDER_EXPORT.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(sellerDetail.getStoreId());
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);

        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 10000);
        params.put("storeId", sellerDetail.getStoreId().toString());
        params.put("managementId", sysExportManagementVO.getId());

        // 发送异步消息将参数传递过去，消费者进行导出处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ORDER_EXPORT_QUEUE, JSON.toJSONString(params));

//        params.put("storeId", sellerDetail.getStoreId().toString());
//        List<OrderDTO> list = orderService.findOrderListExport(params);
//
//        try {
//            ExcelUtils.exportExcelToTarget(response, null, list, OrderExcelSellerDTO.class);
//        } catch (Exception e) {
//            log.error("导出订单excel异常,{}", e);
//        }
    }

    @PutMapping("shippment")
    @ApiOperation("订单发货")
    public Result shippment(@RequestBody OrderDeliverDTO orderDeliverDTO, @ApiIgnore SellerDetail sellerDetail) {

        // 校验数据
        ValidatorUtils.validateEntity(orderDeliverDTO, UpdateGroup.class);

        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        if (DevlierTypeEnum.EXPRESS.value().equals(orderDeliverDTO.getDevlierType())) {
            // 快递
            if (StringUtils.isBlank(orderDeliverDTO.getTransportCode())) {
                return new Result<PageData<OrderDTO>>().error("物流单号不能为空");
            }
            if (StringUtils.isBlank(orderDeliverDTO.getTransportCompanyName())) {
                return new Result<PageData<OrderDTO>>().error("物流公司名称不能为空");
            }
            if (StringUtils.isBlank(orderDeliverDTO.getTransportCompanyId())) {
                return new Result<PageData<OrderDTO>>().error("物流公司编码不能为空");
            }
        }
        Long storeId = sellerDetail.getStoreId();
        orderService.shippment(orderDeliverDTO, storeId);
        return new Result().ok(null, "发货成功");

    }

    /**
     * 取消订单
     *
     * @return
     */
    @PutMapping("cancel")
    @ApiOperation("取消订单")
    @LogOperation("取消订单")
    public Result cancelOrder(@RequestBody OrderCancelDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();
        orderService.cancelOrderSeller(dto, storeId);
        return new Result().ok(null, "取消订单成功");
    }

    /**
     * @param params:原因类型（0:退货,1:换货）
     * @Author: SWH ab4856812@163.com
     * @Description:原因接口
     * @Date: 2019/6/20 22:51
     * @Version: V1.0
     */
    @GetMapping("/apply/reason")
    @ApiOperation("原因接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（0：退货，1：换货，2：申请退款 ，3：取消订单）", paramType = "query", dataType = "String")
    })
    public Result<List<ReasonDescriptionDTO>> aftersaleReason(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("role", "1");
        List<ReasonDescriptionDTO> resultList = reasonDescriptionService.list(params);
        if (CollectionUtils.isEmpty(resultList)) {
            return new Result().error(ResultCodeEnum.WARN.value(), "查询结果为空");
        }
        return new Result<List<ReasonDescriptionDTO>>().ok(resultList, "查询成功");
    }


    @PutMapping("seller/remark")
    @ApiOperation("保存商家备注信息")
    public Result updateSellerRemark(@RequestBody OrderSellerRemarkDTO dto) {
        orderService.updateSellerRemark(dto);
        return new Result().ok(null, "保存成功");
    }

    @GetMapping("group/page")
    @ApiOperation("seller拼团订单分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buyerName", value = "买家名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentId", value = "支付方式", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "paymentStatus", value = "支付状态 0:未付款;1:已付款", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startCreateDate", value = "下单开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endCreateDate", value = "下单结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "startPaymentTime", value = "交易开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endPaymentTime", value = "交易结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupStatus", value = "拼团状态(0:进行中，1:成功，2:失败)",
                    paramType = "query", dataType = "int")
    })
    public Result<PageData<OrderDTO>> groupPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getId() == null || null == sellerDetail.getStoreId()) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Map<String, String> logMap = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            logMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        logMap.put("storeId", sellerDetail.getStoreId().toString());

        Long storeId = sellerDetail.getStoreId();

        // 拼接时间查询条件
        String startCreateDate = (String) params.get("startCreateDate");
        if (StringUtils.isNotBlank(startCreateDate)) {
            startCreateDate = startCreateDate + " 00:00:00";
            params.put("startCreateDate", startCreateDate);
        }
        String endCreateDate = (String) params.get("endCreateDate");
        if (StringUtils.isNotBlank(endCreateDate)) {
            endCreateDate = endCreateDate + " 23:59:59";
            params.put("endCreateDate", endCreateDate);
        }
        String startPaymentTime = (String) params.get("startPaymentTime");
        if (StringUtils.isNotBlank(startPaymentTime)) {
            startPaymentTime = startPaymentTime + " 00:00:00";
            params.put("startPaymentTime", startPaymentTime);
        }
        String endPaymentTime = (String) params.get("endPaymentTime");
        if (StringUtils.isNotBlank(endPaymentTime)) {
            endPaymentTime = endPaymentTime + " 23:59:59";
            params.put("endPaymentTime", endPaymentTime);
        }

        PageData<OrderDTO> page = orderService.groupPage(params, storeId);
        mlogger.info(OrderStatusCode.SELLER_ORDER_PAGE_SUCCESS_CODE, OrderStatusCode.SELLER_ORDER_PAGE_SUCCESS_MESSAGE, logMap);

        return new Result<PageData<OrderDTO>>().ok(page, "查询成功");
    }

    @GetMapping("fetchcode/page")
    @ApiOperation("seller电子提货码列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buyerName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsSerial", value = "商品编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "codeStatus", value = "提货码状态（默认 0待核销，1:部分核销，2:已核销，3:已过期）",
                    paramType = "query", dataType = "int")
    })
    public Result<PageData<FetchCodeDTO>> codePage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getId() == null || null == sellerDetail.getStoreId()) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<FetchCodeDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();

        params.put("storeId", storeId.toString());

        PageData<FetchCodeDTO> pageData = fetchCodeService.page(params);

        return new Result<PageData<FetchCodeDTO>>().ok(pageData, "查询成功");
    }

    @GetMapping("fetchcode/detail")
    @ApiOperation("seller电子提货码详情")
    public Result<FetchCodeDetailDTO> getFetchcodeDetail(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "fetchCode", required = false) String fetchCode, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null || null == sellerDetail.getStoreId()) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<FetchCodeDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();
//        Long storeId = 1191606940933849090L;
        FetchCodeDetailDTO fetchCodeDetailDTO = fetchCodeService.sellerDetail(id, fetchCode, storeId);
        return new Result<FetchCodeDetailDTO>().ok(fetchCodeDetailDTO, "查询成功");

    }

    @GetMapping("verify/page")
    @ApiOperation("seller核销记录分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buyerName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsSerial", value = "商品编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "Date"),
    })
    public Result<PageData<VerifyRecordDTO>> verifyPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getId() == null || null == sellerDetail.getStoreId()) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<VerifyRecordDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();

        params.put("storeId", storeId.toString());

        PageData<VerifyRecordDTO> pageData = verifyRecordService.page(params);

        return new Result<PageData<VerifyRecordDTO>>().ok(pageData, "查询成功");
    }

    @GetMapping("verify/detail/{id}")
    @ApiOperation("seller核销记录详情")
    public Result<VerifyRecordDetailDTO> getVerifyDetail(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null || null == sellerDetail.getStoreId()) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<VerifyRecordDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();
//        Long storeId = 1199222428123791362L;
        VerifyRecordDetailDTO verifyRecordDetailDTO = verifyRecordService.sellerDetail(id, storeId);
        return new Result<VerifyRecordDetailDTO>().ok(verifyRecordDetailDTO, "查询成功");

    }

    @PutMapping("verify")
    @ApiOperation("seller确认核销")
    public Result verifyCode(@RequestBody OrderVerifyCodeDTO orderVerifyCodeDTO, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null || null == sellerDetail.getStoreId()) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();
        fetchCodeService.verifyCode(orderVerifyCodeDTO);
        return new Result().ok(null, "核销成功");

    }


}
