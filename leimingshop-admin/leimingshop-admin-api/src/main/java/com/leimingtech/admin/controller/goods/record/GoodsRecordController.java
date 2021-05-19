/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.goods.record;

import com.leimingtech.admin.excel.goods.record.GoodsRecordExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.goods.record.GoodsRecordDTO;
import com.leimingtech.modules.service.goods.record.GoodsRecordService;
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
 * @Author: weixianchun
 * @Description: 商品上下架状态记录
 * @Date :2019/6/4 18:55
 * @Version V1.0
 **/
@RestController
@RequestMapping("goods/record")
@Api(tags = "商品上下架状态记录")
@Slf4j
public class GoodsRecordController {

    @Autowired
    private GoodsRecordService goodsRecordService;

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/6/4 19:05
     * @Param params 可变参数
     * @Version V1.0
     **/
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<GoodsRecordDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<GoodsRecordDTO> page = goodsRecordService.page(params);

        return new Result<PageData<GoodsRecordDTO>>().ok(page, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询商品上下架状态记录
     * @Date :2019/6/4 19:08
     * @Param id 商品上下架状态记录id
     * @Version V1.0
     **/
    @GetMapping("{id}")
    @ApiOperation("根据id查询商品上下架状态记录")
    public Result<GoodsRecordDTO> get(@PathVariable("id") Long id) {
        GoodsRecordDTO data = goodsRecordService.get(id);

        return new Result<GoodsRecordDTO>().ok(data, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 修改商品上下架状态记录
     * @Date :2019/6/4 19:07
     * @Param dto 实体
     * @Version V1.0
     **/
    @PutMapping
    @ApiOperation("修改商品上下架状态记录")
    @LogOperation("修改商品上下架状态记录")
    public Result update(@RequestBody GoodsRecordDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        goodsRecordService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 根据商品上下架状态记录id批量删除
     * @Date :2019/6/4 19:07
     * @Param ids 商品上下架状态记录id
     * @Version V1.0
     **/
    @DeleteMapping
    @ApiOperation("根据商品上下架状态记录id批量删除")
    @LogOperation("根据商品上下架状态记录id批量删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        goodsRecordService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 导出商品上下架状态记录
     * @Date :2019/6/4 19:07
     * @Param params 查询条件
     * @Param response
     * @Version V1.0
     **/
    @GetMapping("export")
    @ApiOperation("导出商品上下架状态记录")
    @LogOperation("导出商品上下架状态记录")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<GoodsRecordDTO> list = goodsRecordService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, GoodsRecordExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}