/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.invoice.controller;

import cn.hutool.core.bean.BeanUtil;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.MailUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.invoice.ApplyOrChangeInvoiceDTO;
import com.leimingtech.modules.dto.invoice.CanInvoiceDTO;
import com.leimingtech.modules.dto.invoice.H5InvoiceDetailDTO;
import com.leimingtech.modules.service.invoice.OrderInvoiceService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.vo.order.PCOrderInvoicePageVO;
import com.leimingtech.moudle.order.code.PcOrderCode;
import com.leimingtech.moudle.order.invoice.vo.ApplyOrChangeInvoiceVo;
import com.leimingtech.moudle.order.invoice.vo.InvoiceDetailVo;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 订单发票模块API
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020年6月15日09:46:04
 **/
@Slf4j
@RestController
@RequestMapping("order/invoice")
@Api(tags = "订单发票管理")
public class OrderInvoiceController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(OrderInvoiceController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderInvoiceService orderInvoiceService;


    @GetMapping("page")
    @ApiOperation("用户发票列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true,
                    dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true,
                    dataType = "int"),
            @ApiImplicitParam(name = "keyWord", value = "搜索条件 商品名称/商品编号/订单编号", paramType = "query", dataType =
                    "String"),
            @ApiImplicitParam(name = "appStatus", value = "前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成", paramType =
                    "query", dataType = "int")
    })
    @LogContext(code = PcOrderCode.ORDER_LIST_CODE, note = PcOrderCode.ORDER_LIST_DESC)
    public Result<PageData<PCOrderInvoicePageVO>> invoicePage(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long buyerId = SecurityCurrentUser.getUserDetail().getId();
//        Long buyerId = 1242275400738975746L;
        params.put("buyerId", buyerId);
        PageData<PCOrderInvoicePageVO> page = orderService.invoicePage(params);
        return new Result<PageData<PCOrderInvoicePageVO>>().ok(page, "查询成功");
    }

    @GetMapping("{orderId}")
    @ApiOperation("查看发票详情")
    public Result<InvoiceDetailVo> get(@PathVariable("orderId") Long orderId) {
        H5InvoiceDetailDTO h5InvoiceDetailDTO = orderInvoiceService.h5InvoiceDetail(orderId);
        if (BeanUtil.isEmpty(h5InvoiceDetailDTO)) {
            return new Result<InvoiceDetailVo>().ok(new InvoiceDetailVo());
        }
        InvoiceDetailVo invoiceDetailVo = ConvertUtils.sourceToTarget(h5InvoiceDetailDTO, InvoiceDetailVo.class);
        return new Result<InvoiceDetailVo>().ok(invoiceDetailVo);
    }

    @GetMapping("check/{orderId}")
    @ApiOperation("校验是否可以开票")
    public Result<CanInvoiceDTO> checkApplyInvoice(@PathVariable("orderId") Long orderId) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<CanInvoiceDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        //效验数据
        AssertUtils.isNull(orderId, "请选择订单后,申请开票");
        CanInvoiceDTO canInvoiceDTO = orderInvoiceService.checkApplyInvoice(orderId);
        return new Result().ok(canInvoiceDTO, "可以申请开票");
    }

    @PostMapping("apply")
    @ApiOperation("申请发票")
    public Result save(@RequestBody ApplyOrChangeInvoiceVo vo) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(vo, AddGroup.class, DefaultGroup.class);
        ApplyOrChangeInvoiceDTO dto = ConvertUtils.sourceToTarget(vo, ApplyOrChangeInvoiceDTO.class);

        dto.setMemberName(SecurityCurrentUser.getUserName());
        orderInvoiceService.applyInvoice(dto);

        return new Result().ok(null, "申请开票成功");
    }


    @PutMapping
    @ApiOperation("换开发票申请")
    public Result update(@RequestBody ApplyOrChangeInvoiceVo vo) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(vo, UpdateGroup.class, DefaultGroup.class);

        ApplyOrChangeInvoiceDTO dto = ConvertUtils.sourceToTarget(vo, ApplyOrChangeInvoiceDTO.class);
        orderInvoiceService.changeInvoice(dto);

        return new Result().ok(null, "发票换开申请成功");
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        orderInvoiceService.delete(ids);

        return new Result();
    }

    @PutMapping("send/mail")
    @ApiOperation("发送邮件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "发票Id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, paramType = "query", dataType = "String"),
    })
    public Result sendEmail(@ApiIgnore @RequestParam Map<String, String> params) {
        //效验数据
        AssertUtils.isMapEmpty(params, "邮箱不能为空");
        if (!MailUtils.isMail(params.get("email"))) {
            return new Result().error("邮箱输入有误");
        }
        orderInvoiceService.sendEmail(params);
        return new Result().ok(null, "发送成功");
    }


}
