/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.store;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.store.StoreGoodsInvoiceSettingDTO;
import com.leimingtech.modules.dto.store.StoreInvoiceDTO;
import com.leimingtech.modules.service.store.StoreInvoiceService;
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
 * 店铺发票设置表
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-27
 */
@RestController
@RequestMapping("storeinvoice")
@Api(tags = "店铺发票设置表")
public class StoreInvoiceController {

    @Autowired
    private StoreInvoiceService storeInvoiceService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<StoreInvoiceDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<StoreInvoiceDTO> page = storeInvoiceService.page(params);

        return new Result<PageData<StoreInvoiceDTO>>().ok(page);
    }

    @GetMapping()
    @ApiOperation("信息")
    public Result<StoreInvoiceDTO> get(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<StoreInvoiceDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long id = sellerDetail.getStoreId();
        StoreInvoiceDTO data = storeInvoiceService.get(id);
        return new Result<StoreInvoiceDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@ApiIgnore SellerDetail sellerDetail, @RequestBody StoreInvoiceDTO dto) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<StoreInvoiceDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        storeInvoiceService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@ApiIgnore SellerDetail sellerDetail, @RequestBody StoreInvoiceDTO dto) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<
                    >().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        storeInvoiceService.update(dto);

        return new Result();
    }

    @GetMapping("detail")
    @ApiOperation("信息")
    public Result<StoreInvoiceDTO> getInvoiceDetail(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<StoreInvoiceDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        StoreInvoiceDTO data = storeInvoiceService.getInvoiceDetail(storeId);
        return new Result<StoreInvoiceDTO>().ok(data);
    }

    @GetMapping("setting")
    @ApiOperation("店铺开票设置信息")
    public Result<StoreGoodsInvoiceSettingDTO> getInvoiceSetting(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<StoreGoodsInvoiceSettingDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        StoreGoodsInvoiceSettingDTO data = storeInvoiceService.getInvoiceSetting(storeId);
        return new Result<StoreGoodsInvoiceSettingDTO>().ok(data);
    }

    @PostMapping("/change/setting")
    @ApiOperation("保存(修改)店铺发票设置")
    public Result savaAndEdit(@ApiIgnore SellerDetail sellerDetail, @RequestBody StoreInvoiceDTO dto) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<StoreInvoiceDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        StoreInvoiceDTO data = storeInvoiceService.getInvoiceDetail(storeId);
        //校验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        if (data == null) {
            dto.setStoreId(storeId);
            storeInvoiceService.save(dto);
        } else {
            dto.setId(data.getId());
            dto.setStoreId(data.getStoreId());
            storeInvoiceService.update(dto);
        }
        return new Result().ok(null, "保存成功");
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        storeInvoiceService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<StoreInvoiceDTO> list = storeInvoiceService.list(params);

//        ExcelUtils.exportExcelToTarget(response, null, list, StoreInvoiceExcel.class);
    }

}
