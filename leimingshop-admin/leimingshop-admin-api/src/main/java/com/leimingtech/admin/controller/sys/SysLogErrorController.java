/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.admin.excel.SysLogErrorExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.SysLogErrorDTO;
import com.leimingtech.service.sys.SysLogErrorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 异常日志
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("log/error")
@Api(tags = "异常日志")
@Slf4j
public class SysLogErrorController {
    @Autowired
    private SysLogErrorService sysLogErrorService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "module", value = "模块名称，如：sys", paramType = "query", dataType = "String")
    })
    public Result<PageData<SysLogErrorDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysLogErrorDTO> page = sysLogErrorService.page(params);

        return new Result<PageData<SysLogErrorDTO>>().ok(page);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("Export Log Error")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<SysLogErrorDTO> list = sysLogErrorService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, SysLogErrorExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}