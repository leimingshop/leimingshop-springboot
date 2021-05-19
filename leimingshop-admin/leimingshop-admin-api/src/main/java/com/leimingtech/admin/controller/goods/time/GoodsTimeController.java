/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.goods.time;

import com.leimingtech.admin.excel.goods.time.GoodsTimeExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.service.goods.time.GoodsTimeService;
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
 * @Description: 定时上架商品
 * @Date :2019/6/4 18:54
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("goods/time")
@Api(tags = "定时上架商品")
public class GoodsTimeController {

    @Autowired
    private GoodsTimeService goodsTimeService;

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/6/4 19:00
     * @Param params
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
    public Result<PageData<GoodsTimeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<GoodsTimeDTO> page = goodsTimeService.page(params);

        return new Result<PageData<GoodsTimeDTO>>().ok(page, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询定时上架商品
     * @Date :2019/6/4 19:00
     * @Param id
     * @Version V1.0
     **/
    @GetMapping("{id}")
    @ApiOperation("根据id查询定时上架商品")
    public Result<GoodsTimeDTO> get(@PathVariable("id") Long id) {
        GoodsTimeDTO data = goodsTimeService.get(id);

        return new Result<GoodsTimeDTO>().ok(data, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 删除定时上架商品
     * @Date :2019/6/4 19:01
     * @Param ids 定时上架商品id
     * @Version V1.0
     **/
    @DeleteMapping
    @ApiOperation("删除定时上架商品")
    @LogOperation("删除定时上架商品")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        goodsTimeService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 导出定时上架商品
     * @Date :2019/6/4 19:02
     * @Param params
     * @Param response
     * @Version V1.0
     **/
    @GetMapping("export")
    @ApiOperation("导出定时上架商品")
    @LogOperation("导出定时上架商品")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<GoodsTimeDTO> list = goodsTimeService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, GoodsTimeExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}