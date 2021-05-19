/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.message;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.dto.message.MessageReceiverDTO;
import com.leimingtech.service.message.MessageReceiverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 短消息接收人表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@RestController
@RequestMapping("message/receiver")
@Api(tags = "短消息接收人管理")
public class MessageReceiverController {
    @Autowired
    private MessageReceiverService messageReceiverService;


    @GetMapping("page")
    @ApiOperation("短消息接收人分页,根据站内信编号查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "messageId", value = "站内信编号", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "receiveName", value = "接收者名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "查看状态 0未读 1已读", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "strTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<MessageReceiverDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MessageReceiverDTO> page = messageReceiverService.pageMessag(params);
        return new Result<PageData<MessageReceiverDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("短消息接收人详情")
    public Result<MessageReceiverDTO> get(@PathVariable("id") Long id) {
        MessageReceiverDTO data = messageReceiverService.get(id);

        return new Result<MessageReceiverDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("短消息接收人保存")
    @LogOperation("短消息接收人保存")
    public Result save(@RequestBody MessageReceiverDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        messageReceiverService.save(dto);

        return new Result().ok(null, "保存成功");
    }


    @DeleteMapping
    @ApiOperation("短消息接收人删除")
    @LogOperation("短消息接收人删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "至少选中一个id");

        messageReceiverService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    @GetMapping("count/{id}")
    public Integer findByReceiveId(@PathVariable("id") Long id) {

        return messageReceiverService.selectByReceiveId(id);
    }

    /**
     * 查询接收人信息列表
     *
     * @param params
     * @return
     */
    @PostMapping("page/receiveid")
    public PageData<MessageReceiverDTO> findMessageByReceiveId(@ApiIgnore @RequestParam Map<String, Object> params) {
        return messageReceiverService.pageMessag(params);
    }

//    /**
//     * 修改接收人状态为已读
//     *
//     * @param params
//     */
//    @PutMapping("status/{id}")
//    public void updateByReceiveId(@ApiIgnore @RequestParam Map<String, Object> params) {
//        messageReceiverService.updateStatus(params);
//    }


}