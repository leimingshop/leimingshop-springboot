/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.after.template;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.after.template.AfterTemplateDTO;
import com.leimingtech.modules.service.after.template.AfterTemplateService;
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
 * 售后模板
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-17
 */
@RestController
@RequestMapping("aftertemplate")
@Api(tags = "售后模板")
public class AfterTemplateController {

    @Autowired
    private AfterTemplateService afterTemplateService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<AfterTemplateDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<AfterTemplateDTO> page = afterTemplateService.page(params);

        return new Result<PageData<AfterTemplateDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询售后模板")
    public Result<AfterTemplateDTO> get(@PathVariable("id") Long id) {
        AfterTemplateDTO data = afterTemplateService.get(id);

        return new Result<AfterTemplateDTO>().ok(data);
    }


    @GetMapping("list")
    @ApiOperation("全部列表数据")
    @LogOperation("全部列表数据")
    public Result<List<AfterTemplateDTO>> export(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<AfterTemplateDTO> list = afterTemplateService.list(params);

        return new Result<List<AfterTemplateDTO>>().ok(list);
    }

}
