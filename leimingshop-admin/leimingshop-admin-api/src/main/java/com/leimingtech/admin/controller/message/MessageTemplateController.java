/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.message;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.dto.message.MessageMongoDTO;
import com.leimingtech.dto.message.ShopMessageListVO;
import com.leimingtech.service.message.MessageTemplateService;
import com.leimingtech.service.message.MessageTextService;
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
 * 消息模板配置表
 *
 * @author *** tyl@leimingtech.com
 * @since v1.0.0 2019-08-22
 */
@RestController
@RequestMapping("template")
@Api(tags = "消息模板配置表")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private MessageTextService shopMessageTextService;

    @GetMapping("page")
    @ApiOperation("模板列表")
    public Result<List<ShopMessageListVO>> page() {
        List<ShopMessageListVO> page = messageTemplateService.listNoPage();
        return new Result<List<ShopMessageListVO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("模板标签信息")
    public Result<Map<String, Object>> get(@PathVariable("id") Long id) {
        if (null == id) {
            return new Result<Map<String, Object>>().error("id null");
        }
        Map<String, Object> data = messageTemplateService.getTemplate(id);
        return new Result<Map<String, Object>>().ok(data);
    }

    @PutMapping
    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "更新主键", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "isSendUmeng", value = "0 开启 ，1 关闭", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isSendSms", value = "0 开启 ，1 关闭", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isSendWechat", value = "0 开启 ，1 关闭", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isSendInner", value = "0 开启 ，1 关闭", dataType = "String")
    })
    public Result update(@RequestBody Map<String, Object> params) {
        messageTemplateService.updateIsSend(params);
        return new Result().ok("修改成功", "修改成功");
    }

    @GetMapping("message/page")
    @ApiOperation("消息分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "messageTitle", value = "消息标题", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "messageType", value = "信息类型(1:系统信息;0:私信)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sendMode", value = "发送方式(0:站内信;1友盟;2短信)", paramType = "query", dataType = "int"),
    })
    public Result<PageData<MessageMongoDTO>> messAgePage(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<MessageMongoDTO> page = shopMessageTextService.pageList(params);
        return new Result<PageData<MessageMongoDTO>>().ok(page);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("消息详情")
    public Result<MessageMongoDTO> messageDetails(@PathVariable("id") Long id) {
        MessageMongoDTO messageInfo = shopMessageTextService.getMessageInfo(id);
        return new Result<MessageMongoDTO>().ok(messageInfo);
    }

    @DeleteMapping
    @ApiOperation("消息列表删除")
    public Result delete(@RequestBody Long[] ids) {
        AssertUtils.isArrayEmpty(ids, "id");
        shopMessageTextService.delete(ids);
        return new Result().ok("删除成功", "删除成功");
    }
}
