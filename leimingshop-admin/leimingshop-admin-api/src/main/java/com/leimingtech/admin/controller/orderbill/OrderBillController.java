/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.orderbill;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.settle.*;
import com.leimingtech.modules.excel.settle.OrderBillExcel;
import com.leimingtech.modules.excel.settle.ReturnBillExcel;
import com.leimingtech.modules.service.settle.BillLogService;
import com.leimingtech.modules.service.settle.BillTotalService;
import com.leimingtech.modules.service.settle.OrderBillService;
import com.leimingtech.modules.service.settle.ReturnBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 订单结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@RestController
@RequestMapping("orderbill")
@Api(tags = "订单结算表")
public class OrderBillController {

    @Autowired
    private BillTotalService billTotalService;

    @Autowired
    private OrderBillService orderBillService;
    @Autowired
    private ReturnBillService returnBillService;

    @Autowired
    private BillLogService billLogService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "billTotalSn", value = "账单编号", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "storeType", value = "店铺类型1 自营商户 2 普通账户", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "confirmStatus", value = "确认状态 0 未确认 1 已确认", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "billState", value = "结算状态 0 未结算 1 已结算", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "出账日期开始时间", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "出账日期结束时间", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<BillTotalDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<BillTotalDTO> page = billTotalService.page(params);

        return new Result<PageData<BillTotalDTO>>().ok(page);
    }

    /**
     * 订单分页
     *
     * @param params
     * @return
     */
    @GetMapping("order/page")
    @ApiOperation("订单分页")
    public Result<PageData<OrderBillDTO>> orderPage(@RequestParam Map<String, Object> params) {
        PageData<OrderBillDTO> page = orderBillService.page(params);
        return new Result<PageData<OrderBillDTO>>().ok(page);
    }

    /**
     * 退款订单分页
     *
     * @param params
     * @return
     */
    @GetMapping("return/page")
    @ApiOperation("退款订单分页")
    public Result<PageData<ReturnBillDTO>> returnPage(@RequestParam Map<String, Object> params) {
        PageData<ReturnBillDTO> page = returnBillService.page(params);
        return new Result<PageData<ReturnBillDTO>>().ok(page);
    }

    /**
     * 根据ID获取信息
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<BillTotalDTO> get(@PathVariable("id") Long id) {
        BillTotalDTO data = billTotalService.get(id);

        return new Result<BillTotalDTO>().ok(data);
    }

    /**
     * 对账记录
     *
     * @return
     */
    @GetMapping("log")
    @ApiOperation("对账记录")
    public Result<List<BillLogDTO>> info(@RequestParam Map<String, Object> params) {
        List<BillLogDTO> list = billLogService.list(params);

        return new Result<List<BillLogDTO>>().ok(list);
    }

    /**
     * 结算
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("结算")
    public Result update(@RequestBody UpdateBillTotalDTO dto) {
        BillTotalDTO billTotalDTO = ConvertUtils.sourceToTarget(dto, BillTotalDTO.class);
        billTotalDTO.setBillState(1);
        billTotalService.update(billTotalDTO);

        return new Result().ok(null, "结算成功");
    }


    @GetMapping("export")
    @ApiOperation("导出对账详情-订单列表/退货列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "listType", value = "1.订单列表 2.退货列表", paramType = "query", required = true, dataType = "String"), @ApiImplicitParam(name = "billTotalId", value = "结算单编号", paramType = "query", required = true, dataType = "String")
    })
    public void exportOrderBill(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        String listType = (String) params.get("listType");
        //1导出订单列表 2导出退货列表
        if ("1".equals(listType)) {
            List<OrderBillDTO> list = orderBillService.list(params);
            EasyExcelUtils.exportExcelToTarget(response, null, list, OrderBillExcel.class);
        } else {
            List<ReturnBillDTO> list = returnBillService.list(params);

            EasyExcelUtils.exportExcelToTarget(response, null, list, ReturnBillExcel.class);
        }

    }
}
