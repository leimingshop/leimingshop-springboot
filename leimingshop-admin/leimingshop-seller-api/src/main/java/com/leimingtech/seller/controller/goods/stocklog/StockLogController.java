/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.goods.stocklog;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dto.stock.SaveStockLogDTO;
import com.leimingtech.modules.dto.stock.StockLogDTO;
import com.leimingtech.modules.dto.stock.UpdataStockLogDTO;
import com.leimingtech.modules.service.stock.StockLogService;
import com.leimingtech.seller.excel.stock.StockLogExcel;
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
 * 库存修改记录管理
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Slf4j
@RestController
@RequestMapping("stock/log")
@Api(tags = "库存修改记录管理")
public class StockLogController {
    @Autowired
    private StockLogService stockLogService;

    /**
     * 分页查询
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sku", value = "sku", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<StockLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<StockLogDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        PageData<StockLogDTO> page = stockLogService.page(params);

        return new Result<PageData<StockLogDTO>>().ok(page);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据ID获取信息")
    public Result<StockLogDTO> findById(@PathVariable("id") Long id) {
        StockLogDTO data = stockLogService.get(id);

        return new Result<StockLogDTO>().ok(data);
    }

    /**
     * 保存库存修改记录
     *
     * @param dto 参数实体
     * @return
     */
    @PostMapping
    @ApiOperation("保存库存修改记录")
    @LogOperation("保存库存修改记录")
    public Result save(@RequestBody SaveStockLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        stockLogService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改库存记录
     *
     * @param dto 参数实体
     * @return
     */
    @PutMapping
    @ApiOperation("修改库存记录")
    @LogOperation("修改库存记录")
    public Result update(@RequestBody UpdataStockLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        stockLogService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 根据ID删除修改记录
     *
     * @param ids 主键ID
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据ID删除修改记录")
    @LogOperation("根据ID删除修改记录")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        stockLogService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 导出记录
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("export")
    @ApiOperation("导出记录")
    @LogOperation("导出记录")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<StockLogDTO> list = stockLogService.selectLogExport(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, StockLogExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }
}
