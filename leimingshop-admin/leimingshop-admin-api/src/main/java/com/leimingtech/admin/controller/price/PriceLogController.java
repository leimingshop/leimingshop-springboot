/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.price;

import com.leimingtech.admin.excel.price.PriceLogExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.price.PriceLogDTO;
import com.leimingtech.modules.dto.price.SavePriceLogDTO;
import com.leimingtech.modules.dto.price.UpdatePriceLogDTO;
import com.leimingtech.modules.service.price.PriceLogService;
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
 * 价格修改记录管理
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@RestController
@RequestMapping("price/log")
@Api(tags = "价格修改记录管理")
@Slf4j
public class PriceLogController {
    @Autowired
    private PriceLogService priceLogService;

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
    public Result<PageData<PriceLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<PriceLogDTO> page = priceLogService.page(params);

        return new Result<PageData<PriceLogDTO>>().ok(page);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据ID获取信息")
    public Result<PriceLogDTO> findById(@PathVariable("id") Long id) {
        PriceLogDTO data = priceLogService.get(id);

        return new Result<PriceLogDTO>().ok(data);
    }

    /**
     * 保存价格修改记录
     *
     * @param dto 参数实体
     * @return
     */
    @PostMapping
    @ApiOperation("保存价格修改记录")
    @LogOperation("保存价格修改记录")
    public Result save(@RequestBody SavePriceLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        priceLogService.save(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 修改价格记录
     *
     * @param dto 参数实体
     * @return
     */
    @PutMapping
    @ApiOperation("修改价格记录")
    @LogOperation("修改价格记录")
    public Result update(@RequestBody UpdatePriceLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        priceLogService.update(dto);

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

        priceLogService.delete(ids);

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
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<PriceLogDTO> list = priceLogService.selectLogExport(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, PriceLogExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}
