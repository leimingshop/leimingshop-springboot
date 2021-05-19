/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.message;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.message.MessageTextDTO;
import com.leimingtech.dto.message.MessageTextListDTO;
import com.leimingtech.service.message.MessageTextService;
import com.leimingtech.statuscode.MessageStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 站内信内容表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@RestController
@RequestMapping("message/text")
@Api(tags = "站内信内容管理")
public class MessageTextController {
    @Autowired
    private MessageTextService messageTextService;


    @GetMapping("page")
    @ApiOperation("分页")
    @LogContext(code = MessageStatusCode.MESSAGE_PAGE_SUCCESS, note = MessageStatusCode.MESSAGE_PAGE_SUCCESS_MSG)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "messageTitle", value = "站内信标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "messageType", value = "信息类型(0:私信;1:系统信息)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "strTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<MessageTextListDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MessageTextListDTO> page = messageTextService.pageMessage(params);

        return new Result<PageData<MessageTextListDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("查看站内信详情")
    public Result<MessageTextDTO> get(@PathVariable("id") Long id) {
        MessageTextDTO data = messageTextService.get(id);

        return new Result<MessageTextDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("站内信保存")
    @LogOperation("站内信保存")
    @LogContext(code = MessageStatusCode.MESSAGE_SAVE_SUCCESS, note = MessageStatusCode.MESSAGE_SAVE_SUCCESS_MSG)
    public Result save(@RequestBody MessageTextDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        UserDetail user = SecurityUser.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setUserName(user.getUsername());
        }
        //保存并发送站内信
        messageTextService.saveMessage(dto);
        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("站内信修改")
    @LogOperation("站内信修改")
    @LogContext(code = MessageStatusCode.MESSAGE_UPDATE_SUCCESS, note = MessageStatusCode.MESSAGE_UPDATE_SUCCESS_MSG)
    public Result update(@RequestBody MessageTextDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        messageTextService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    @DeleteMapping
    @ApiOperation("站内信删除")
    @LogOperation("站内信删除")
    @LogContext(code = MessageStatusCode.MESSAGE_DEL_SUCCESS, note = MessageStatusCode.MESSAGE_DEL_SUCCESS_MSG)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据

        AssertUtils.isArrayEmpty(ids, "至少选中一个id");

        messageTextService.deleteInnerMessage(ids);

        return new Result().ok(null, "删除成功");
    }


}
