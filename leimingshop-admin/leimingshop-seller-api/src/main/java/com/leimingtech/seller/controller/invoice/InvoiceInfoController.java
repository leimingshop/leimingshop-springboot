/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.invoice;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.invoice.InvoiceInfoDTO;
import com.leimingtech.modules.service.invoice.InvoiceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 发票信息表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@RestController
@RequestMapping("invoiceinfo")
@Api(tags = "发票信息表")
public class InvoiceInfoController {

    @Autowired
    private InvoiceInfoService invoiceInfoService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<InvoiceInfoDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<InvoiceInfoDTO> page = invoiceInfoService.page(params);

        return new Result<PageData<InvoiceInfoDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<InvoiceInfoDTO> get(@PathVariable("id") Long id) {
        InvoiceInfoDTO data = invoiceInfoService.get(id);

        return new Result<InvoiceInfoDTO>().ok(data);
    }

    @GetMapping("detail/{invoiceId}")
    @ApiOperation("信息")
    public Result<List<InvoiceInfoDTO>> getDetailInvoiceID(@PathVariable("invoiceId") Long invoiceId) {
        List<InvoiceInfoDTO> data = invoiceInfoService.getDetailInvoiceID(invoiceId);

        return new Result<List<InvoiceInfoDTO>>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody InvoiceInfoDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        invoiceInfoService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody InvoiceInfoDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        invoiceInfoService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        invoiceInfoService.delete(ids);

        return new Result();
    }

//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<InvoiceInfoDTO> list = invoiceInfoService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, InvoiceInfoExcel.class);
//    }

}