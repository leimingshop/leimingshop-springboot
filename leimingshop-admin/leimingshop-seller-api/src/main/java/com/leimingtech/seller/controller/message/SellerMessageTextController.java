/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.message;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.dto.message.MessageRemindDTO;
import com.leimingtech.dto.message.SellerMessageReceiverDTO;
import com.leimingtech.dto.message.SellerMessageTextDTO;
import com.leimingtech.service.message.MessageReceiverService;
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
 * @author chengqian
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-12-16
 */
@RestController
@RequestMapping("message/text")
@Api(tags = "seller站内信内容管理")
public class SellerMessageTextController {
    @Autowired
    private MessageTextService messageTextService;

    @Autowired
    private MessageReceiverService messageReceiverService;

    /**
     * 站内信列表
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("seller站内信列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "status", value = "查看状态 0未读 1已读", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "strTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "messageTitle", value = "站内信标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sendName", value = "发送人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "messageType", value = "信息类型(0:私信;1:系统信息)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<SellerMessageReceiverDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        params.put("receiveId", sellerDetail.getStoreId().toString());
        PageData<SellerMessageReceiverDTO> page = messageReceiverService.sellerMessagePage(params);

        return new Result<PageData<SellerMessageReceiverDTO>>().ok(page);
    }

    /**
     * 站内信详情
     *
     * @param receiveInfoId 站内信接收人表
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("查看站内信详情")
    public Result<SellerMessageTextDTO> get(@PathVariable("id") Long receiveInfoId) {
        SellerMessageTextDTO data = messageTextService.sellerMessageInfo(receiveInfoId);

        return new Result<SellerMessageTextDTO>().ok(data);
    }

    /**
     * 站内信删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("站内信删除")
    @LogOperation("站内信删除")
    @LogContext(code = MessageStatusCode.MESSAGE_DEL_SUCCESS, note = MessageStatusCode.MESSAGE_DEL_SUCCESS_MSG)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据

        AssertUtils.isArrayEmpty(ids, "至少选中一个id");

        messageReceiverService.deleteShow(ids);

        return new Result().ok(null, "删除成功");
    }

    /***
     * 批量已读
     * @param ids
     * @return
     */
    @PutMapping("batch/read")
    @ApiOperation("批量已读")
    @LogOperation("批量已读")
    public Result batchRead(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "至少选中一个id");

        messageReceiverService.batchRead(ids);

        return new Result().ok(null, "已读成功");
    }


    /**
     * 消息提醒列表
     *
     * @param sellerDetail
     * @return
     */
    @GetMapping("list")
    @ApiOperation("消息提醒列表")
    @LogOperation("消息提醒列表")
    @LogContext(code = MessageStatusCode.MESSAGE_DEL_SUCCESS, note = MessageStatusCode.MESSAGE_DEL_SUCCESS_MSG)
    public Result list(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error("用户未登录");
        }
        MessageRemindDTO remingList = messageTextService.getRemingList(sellerDetail.getStoreId());

        return new Result().ok(remingList);
    }


}
