/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.order;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.order.AdminOrderActivityDetailDTO;
import com.leimingtech.modules.dto.order.AdminOrderDetailDTO;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDetailDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDetailDTO;
import com.leimingtech.modules.service.order.FetchCodeService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.order.VerifyRecordService;
import com.leimingtech.modules.statuscode.OrderStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.sys.SysExportManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
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
    private SysExportManagementService sysExportManagementService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private FetchCodeService fetchCodeService;

    @Autowired
    private VerifyRecordService verifyRecordService;

    @GetMapping("page")
    @ApiOperation("admin订单分页")
    @LogContext(code = OrderStatusCode.ADMIN_ORDER_PAGE_SUCCESS_CODE, note = OrderStatusCode.ADMIN_ORDER_PAGE_SUCCESS_MESSAGE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeIdOrName", value = "商户id/名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buyerId", value = "买家id", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "buyerName", value = "买家名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentCode", value = "支付方式", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "paymentStatus", value = "支付状态 0:未付款;1:已付款", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startCreateDate", value = "下单开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endCreateDate", value = "下单结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "startPaymentTime", value = "交易开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endPaymentTime", value = "交易结束时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "virtualFlag", value = "虚拟订单标记（0:否，1是）", paramType = "query", dataType = "int")
    })
    public Result<PageData<OrderDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<OrderDTO> page = orderService.page(params, null);
        return new Result<PageData<OrderDTO>>().ok(page, "查询成功");
    }

    /**
     * 功能描述:
     * 〈根据父订单号查询子订单集合〉
     *
     * @param parentOrderSn 父订单编号
     * @author : 刘远杰
     */
    @GetMapping("list/parentordersn/{parentOrderSn}")
    @ApiOperation("根据父订单号查询子订单集合")
    public Result<List<OrderDTO>> childOrderList(@PathVariable("parentOrderSn") Long parentOrderSn) {
        List<OrderDTO> childOrderList = orderService.childOrderList(parentOrderSn);
        return new Result<List<OrderDTO>>().ok(childOrderList, "查询成功");
    }

    @GetMapping("{id}")
    @LogContext(code = OrderStatusCode.ADMIN_ORDER_DETAIL_SUCCESS_CODE, note = OrderStatusCode.ADMIN_ORDER_DETAIL_SUCCESS_MESSAGE)
    @ApiOperation("admin根据订单id查询订单详情")
    public Result<AdminOrderDetailDTO> get(@PathVariable("id") Long id) {
        AdminOrderDetailDTO adminOrderDetail = orderService.getAdminOrderDetail(id, null, null);
        return new Result<AdminOrderDetailDTO>().ok(adminOrderDetail, "查询详情成功");
    }

    @GetMapping("activity/detail/orderSn/{orderSn}")
    @ApiOperation("订单活动详情查询")
    public Result<AdminOrderActivityDetailDTO> getOrderActivityDetail(@PathVariable("orderSn") Long orderSn) {
        AdminOrderActivityDetailDTO orderActivityDetail = orderService.getOrderActivityDetail(orderSn, null);
        return new Result<AdminOrderActivityDetailDTO>().ok(orderActivityDetail, "查询详情成功");
    }

    @GetMapping("orderSn/{orderSn}")
    @LogContext(code = OrderStatusCode.ADMIN_ORDER_DETAIL_SUCCESS_CODE, note = OrderStatusCode.ADMIN_ORDER_DETAIL_SUCCESS_MESSAGE)
    @ApiOperation("admin根据订单编号查询订单详情")
    public Result<AdminOrderDetailDTO> getByOrderSn(@PathVariable("orderSn") Long orderSn) {
        AdminOrderDetailDTO adminOrderDetail = orderService.getAdminOrderDetailByOrderSn(orderSn, null, null);
        return new Result<AdminOrderDetailDTO>().ok(adminOrderDetail, "查询详情成功");
    }

    @GetMapping("export")
    @ApiOperation("订单列表导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        // 保存导出记录表 状态为导出中
        String assignmentName = ExcelEnum.ORDER_EXPORT.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(0L);
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);

        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 10000);
        params.put("managementId", sysExportManagementVO.getId());

        // 发送异步消息将参数传递过去，消费者进行导出处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_ORDER_EXPORT_QUEUE, JSON.toJSONString(params));
//        List<OrderDTO> list = orderService.findOrderListExport(params);
//        try {
//            ExcelUtils.exportExcelToTarget(response, null, list, EasyOrderExcelDTO.class);
//        } catch (Exception e) {
//            log.error("导出订单excel异常,{}", e);
//        }
    }

    @GetMapping("fetchcode/page")
    @ApiOperation("电子提货码列表分页")
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
    public Result<PageData<FetchCodeDTO>> codePage(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<FetchCodeDTO> pageData = fetchCodeService.page(params);

        return new Result<PageData<FetchCodeDTO>>().ok(pageData, "查询成功");
    }

    @GetMapping("fetchcode/detail/{id}")
    @ApiOperation("电子提货码详情")
    public Result<FetchCodeDetailDTO> getFetchcodeDetail(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        FetchCodeDetailDTO fetchCodeDetailDTO = fetchCodeService.sellerDetail(id, null, null);
        return new Result<FetchCodeDetailDTO>().ok(fetchCodeDetailDTO, "查询成功");

    }

    @GetMapping("verify/page")
    @ApiOperation("核销记录分页")
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
    public Result<PageData<VerifyRecordDTO>> verifyPage(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<VerifyRecordDTO> pageData = verifyRecordService.page(params);

        return new Result<PageData<VerifyRecordDTO>>().ok(pageData, "查询成功");
    }

    @GetMapping("verify/detail/{id}")
    @ApiOperation("核销记录详情")
    public Result<VerifyRecordDetailDTO> getVerifyDetail(@PathVariable("id") Long id) {

        VerifyRecordDetailDTO verifyRecordDetailDTO = verifyRecordService.sellerDetail(id, null);
        return new Result<VerifyRecordDetailDTO>().ok(verifyRecordDetailDTO, "查询成功");

    }


}
