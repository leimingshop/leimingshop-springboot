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
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.message.dto.SysMailTemplateDTO;
import com.leimingtech.message.email.EmailConfig;
import com.leimingtech.message.service.SysMailTemplateService;
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
 * 邮件模板
 */
@RestController
@RequestMapping("mailtemplate")
@Api(tags = "邮件模板")
public class MailTemplateController {
    private static final String KEY = ModuleConstant.MAIL_CONFIG_KEY;
    @Autowired
    private SysMailTemplateService sysMailTemplateService;
    @Autowired
    private ParamsRemoteService paramsRemoteService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name", paramType = "query", dataType = "String")
    })
    public Result<PageData<SysMailTemplateDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysMailTemplateDTO> page = sysMailTemplateService.page(params);

        return new Result<PageData<SysMailTemplateDTO>>().ok(page);
    }

    @GetMapping("config")
    @ApiOperation("获取配置信息")
    public Result<EmailConfig> config() {
        EmailConfig config = paramsRemoteService.getValueObject(KEY, EmailConfig.class);

        return new Result<EmailConfig>().ok(config);
    }

    @PostMapping("saveConfig")
    @ApiOperation("保存配置信息")
    @LogOperation("保存配置信息")
    public Result saveConfig(@RequestBody EmailConfig config) {
        //校验数据
        ValidatorUtils.validateEntity(config);

        paramsRemoteService.updateValueByCode(KEY, JSON.toJSONString(config));

        return new Result();
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<SysMailTemplateDTO> info(@PathVariable("id") Long id) {
        SysMailTemplateDTO sysMailTemplate = sysMailTemplateService.info(id);

        return new Result<SysMailTemplateDTO>().ok(sysMailTemplate);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public Result save(@RequestBody SysMailTemplateDTO dto) {
        //校验类型
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysMailTemplateService.saveMailTemplate(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public Result update(@RequestBody SysMailTemplateDTO dto) {
        //校验类型
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysMailTemplateService.updateMailTemplate(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        sysMailTemplateService.deleteMailTemplates(ids);

        return new Result();
    }

    @PostMapping("send")
    @ApiOperation("发送邮件")
    @LogOperation("发送邮件")
    public Result send(Long id, String mailTo, String mailCc, String params) throws Exception {
        boolean flag = sysMailTemplateService.sendMail(id, mailTo, mailCc, params);
        if (flag) {
            return new Result();
        }

        return new Result().error("邮件发送失败");
    }

}