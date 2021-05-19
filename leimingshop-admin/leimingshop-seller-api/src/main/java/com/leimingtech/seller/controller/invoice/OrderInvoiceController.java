/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.invoice;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.invoice.IssueOrderInvoiceDTO;
import com.leimingtech.modules.dto.invoice.OrderInvoiceDTO;
import com.leimingtech.modules.dto.invoice.OrderInvoiceDetailDTO;
import com.leimingtech.modules.service.invoice.OrderInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 订单发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@RestController
@RequestMapping("orderinvoice")
@Api(tags = "订单发票表")
public class OrderInvoiceController {

    @Autowired
    private OrderInvoiceService orderInvoiceService;


    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "memberName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberInvoiceType", value = "用户提交发票类型(1：电子、2：纸质、3：增值税)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "invoiceEvolve", value = "开票进度（1：待开票、2：已开票、3：待换开、4：已换开）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:已完成", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startInvoiceDate", value = "开票开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endInvoiceDate", value = "开票结束时间", paramType = "query", dataType = "Date"),

    })
    public Result<PageData<OrderInvoiceDTO>> page(@ApiIgnore SellerDetail sellerDetail, @ApiIgnore @RequestParam Map<String, Object> params) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<PageData<OrderInvoiceDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        PageData<OrderInvoiceDTO> page = orderInvoiceService.page(params);
        return new Result<PageData<OrderInvoiceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<OrderInvoiceDTO> get(@PathVariable("id") Long id) {
        OrderInvoiceDTO data = orderInvoiceService.get(id);

        return new Result<OrderInvoiceDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody OrderInvoiceDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        orderInvoiceService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody OrderInvoiceDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        orderInvoiceService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        orderInvoiceService.delete(ids);

        return new Result();
    }

    @PutMapping("make/invoice")
    @ApiOperation("开票操作")
    public Result makeInvoice(@RequestBody IssueOrderInvoiceDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        OrderInvoiceDTO orderInvoiceDTO = ConvertUtils.sourceToTarget(dto, OrderInvoiceDTO.class);
        orderInvoiceService.makeInvoice(orderInvoiceDTO);
        return new Result().ok(null, "开票成功");
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    public Result<OrderInvoiceDetailDTO> detail(@PathVariable("id") Long id) {
        OrderInvoiceDetailDTO data = orderInvoiceService.detail(id);
        return new Result<OrderInvoiceDetailDTO>().ok(data);
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
        orderInvoiceService.sendEmail(params);
        return new Result().ok(null, "发送成功");
    }


//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<OrderInvoiceDTO> list = orderInvoiceService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, OrderInvoiceExcel.class);
//    }

}