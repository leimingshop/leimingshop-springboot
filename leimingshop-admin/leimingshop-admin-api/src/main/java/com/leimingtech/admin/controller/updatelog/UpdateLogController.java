/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.updatelog;

import com.leimingtech.admin.excel.updatelog.UpdateLogExcel;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.updatelog.UpdateLogDTO;
import com.leimingtech.service.updatelog.UpdateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 版本更新日志
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@RestController
@RequestMapping("updatelog")
@Api(tags = "版本更新日志")
public class UpdateLogController {

    @Autowired
    private UpdateLogService updateLogService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "logType", value = "更新日志类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "logTitle", value = "更新日志标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startCreateDate", value = "创建开始时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endCreateDate", value = "创建结束时间", paramType = "query", dataType = "Date"),
    })
    public Result<PageData<UpdateLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<UpdateLogDTO> page = updateLogService.page(params);

        return new Result<PageData<UpdateLogDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<UpdateLogDTO> get(@PathVariable("id") Long id) {
        UpdateLogDTO data = updateLogService.get(id);

        return new Result<UpdateLogDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody @Valid UpdateLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        updateLogService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody @Valid UpdateLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        updateLogService.update(dto);

        return new Result().ok(null, "编辑成功");
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        updateLogService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    @GetMapping("export")
    @ApiOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<UpdateLogDTO> list = updateLogService.list(params);

        EasyExcelUtils.exportExcelToTarget(response, null, list, UpdateLogExcel.class);
    }

}