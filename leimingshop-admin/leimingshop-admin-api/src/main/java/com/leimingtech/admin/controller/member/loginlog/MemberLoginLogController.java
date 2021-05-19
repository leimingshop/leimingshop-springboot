/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.member.loginlog;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.log.MemberLoginLogDTO;
import com.leimingtech.modules.dto.log.MemberLoginLogExcelDTO;
import com.leimingtech.modules.service.log.MemberLoginLogService;
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
 * 用户登录日志表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-30
 */
@RestController
@RequestMapping("memberloginlog")
@Api(tags = "用户登录日志表")
@Slf4j
public class MemberLoginLogController {
    @Autowired
    private MemberLoginLogService memberLoginLogService;

    @GetMapping("page")
    @ApiOperation("用户登录日志分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "用户id/名称/手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "starTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "source", value = "登录方式   0:PC登录  1:手机  2:其他", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<MemberLoginLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MemberLoginLogDTO> page = memberLoginLogService.page(params);

        return new Result<PageData<MemberLoginLogDTO>>().ok(page);
    }

    /**
     * 根据memberid查询列表
     *
     * @param
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("查询用户日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "用户id/昵称/手机号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "starTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "source", value = "登录方式   0:PC登录  1:手机  2:其他", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<MemberLoginLogDTO>> get(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MemberLoginLogDTO> page = memberLoginLogService.page(params);

        return new Result<PageData<MemberLoginLogDTO>>().ok(page);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "用户id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "starTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "source", value = "登录方式   0:PC登录  1:手机  2:其他", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "phoneNumber", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "用户名", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<MemberLoginLogExcelDTO> list = memberLoginLogService.export(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, MemberLoginLogExcelDTO.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}
