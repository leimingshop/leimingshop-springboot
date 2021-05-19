/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.circle;


import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dto.report.CmsArticleReportAdminInfoDTO;
import com.leimingtech.modules.dto.report.CmsReportPageDto;
import com.leimingtech.modules.service.report.CmsReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 举报管理
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:47
 **/
@RestController
@RequestMapping("cms/circleReport")
@Api(tags = "举报管理")
@Slf4j
public class CmsCircleReportController {

    @Autowired
    private CmsReportService cmsReportService;

    @GetMapping("page")
    @ApiOperation("举报列表分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "reportStatus", value = "状态(1：待处理，2：通过，3未通过)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reportContent", value = "举报信息", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reportFlagName", value = "标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<CmsReportPageDto>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CmsReportPageDto> page = cmsReportService.page(params);
        return new Result<PageData<CmsReportPageDto>>().ok(page);
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        cmsReportService.deleteList(ids);
        return new Result();
    }

    @PutMapping("statusUpdate")
    @ApiOperation("举报状态修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "举报id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "reportStatus", value = "状态(1：待处理，2：通过，3未通过)", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "reportResult", value = "举报反馈", paramType = "query", required = true, dataType = "String")
    })
    public Result statusUpdate(@ApiIgnore @RequestBody Map<String, Object> params) {
        cmsReportService.statusUpdate(params);
        return new Result();
    }


    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<CmsArticleReportAdminInfoDTO> get(@PathVariable("id") Long id) {
        CmsArticleReportAdminInfoDTO data = cmsReportService.getArticleReportAdmin(id);
        return new Result<CmsArticleReportAdminInfoDTO>().ok(data);
    }
}

