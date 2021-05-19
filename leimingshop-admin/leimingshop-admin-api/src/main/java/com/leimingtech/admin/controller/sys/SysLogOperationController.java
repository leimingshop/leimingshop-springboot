/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.SysLogOperationDTO;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.sys.SysExportManagementService;
import com.leimingtech.service.sys.SysLogOperationService;
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
import java.util.Map;


/**
 * 操作日志
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("log/operation")
@Api(tags = "操作日志")
@Slf4j
public class SysLogOperationController {
    @Autowired
    private SysLogOperationService sysLogOperationService;
    @Autowired
    private SysExportManagementService sysExportManagementService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "module", value = "模块名称，如：sys", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态  0：失败    1：成功", paramType = "query", dataType = "int")
    })
    public Result<PageData<SysLogOperationDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysLogOperationDTO> page = sysLogOperationService.page(params);

        return new Result<PageData<SysLogOperationDTO>>().ok(page);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("Export Log Operation")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        // 保存导出记录表 状态为导出中
        String assignmentName = ExcelEnum.OPERATION_LOG_EXPORT.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(0L);
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);
        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 1000);
        params.put("managementId", sysExportManagementVO.getId());

        // 发送异步消息将参数传递过去，消费者进行导出处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_LOG_EXPORT_QUEUE, JSON.toJSONString(params));
        log.info("日志导出结束");
    }
}