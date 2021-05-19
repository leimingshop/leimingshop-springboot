/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.message.config;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AliyunGroup;
import com.leimingtech.commons.tools.validator.group.QcloudGroup;
import com.leimingtech.message.dto.SysSmsDTO;
import com.leimingtech.message.enums.PlatformEnum;
import com.leimingtech.message.service.SysSmsService;
import com.leimingtech.message.sms.SmsConfig;
import com.leimingtech.message.utils.ModuleConstant;
import com.leimingtech.remote.ParamsRemoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 短信服务
 */
@RestController
@RequestMapping("sms")
@Api(tags = "短信服务")
public class SmsController {
    private static final String KEY = ModuleConstant.SMS_CONFIG_KEY;
    @Autowired
    private SysSmsService sysSmsService;
    @Autowired
    private ParamsRemoteService paramsRemoteService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "mobile", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status", paramType = "query", dataType = "String")
    })
    public Result<PageData<SysSmsDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysSmsDTO> page = sysSmsService.page(params);

        return new Result<PageData<SysSmsDTO>>().ok(page);
    }

    @GetMapping("config")
    @ApiOperation("获取配置短信")
    public Result<SmsConfig> config() {
        SmsConfig config = paramsRemoteService.getValueObject(KEY, SmsConfig.class);

        return new Result<SmsConfig>().ok(config);
    }

    @PostMapping("saveConfig")
    @ApiOperation("保存配置短信")
    @LogOperation("保存配置短信")
    public Result saveConfig(@RequestBody SmsConfig config) {
        //校验类型
        ValidatorUtils.validateEntity(config);

        if (config.getPlatform() == PlatformEnum.ALIYUN.value()) {
            //校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getPlatform() == PlatformEnum.QCLOUD.value()) {
            //校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }

        paramsRemoteService.updateValueByCode(KEY, JSON.toJSONString(config));

        return new Result();
    }

    @PostMapping("send")
    @ApiOperation("发送短信")
    @LogOperation("发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机好号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "params", value = "参数", paramType = "query", required = true, dataType = "String")
    })
    public Result send(String mobile, String params) {
        sysSmsService.send(mobile, params);

        return new Result();
    }

    @PostMapping("sendByCode")
    @ApiOperation("根据模板发送短信")
    @LogOperation("根据模板发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机好号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "params", value = "参数", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "模板code", paramType = "query", required = true, dataType = "String")
    })
    public Result send(String mobile, String params, String code) {
        sysSmsService.send(mobile, params, code);

        return new Result();
    }

    @PostMapping("sendBatchSms")
    @ApiOperation("根据模板批量发送短信")
    @LogOperation("根据模板批量发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileJson", value = "手机号数组", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "paramsJson", value = "参数json", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "signNameJson", value = "签名数组", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "模板code", paramType = "query", required = true, dataType = "String")
    })
    public Result sendBatchSms(String mobileJson, String paramsJson, String signNameJson, String code) {
        sysSmsService.sendBatchSms(mobileJson, paramsJson, signNameJson, code);

        return new Result();
    }


    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        sysSmsService.deleteBatchIds(ids);
        return new Result();
    }

}