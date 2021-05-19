/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.message.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.dto.message.MessageTextDTO;
import com.leimingtech.dto.message.MessageTextListDTO;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.moudle.member.message.code.PcMessageCode;
import com.leimingtech.moudle.member.message.vo.MessageTextListVO;
import com.leimingtech.moudle.member.message.vo.MessageTextVO;
import com.leimingtech.security.SecurityCurrentUser;
import com.leimingtech.service.message.MessageReceiverService;
import com.leimingtech.service.message.MessageTextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 用户消息管理
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020年5月18日18:02:22
 **/
@Slf4j
@RestController
@RequestMapping("/member/message")
@Api(tags = "用户消息管理")
public class MessageController {

    @Autowired
    private MessageTextService messageTextService;
    @Autowired
    private MessageReceiverService messageReceiverService;

    @GetMapping("page")
    @ApiOperation("获取用户消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @LogContext(code = PcMessageCode.MEMBER_MESSAGE_PAGE_CODE, note = PcMessageCode.MEMBER_MESSAGE_PAGE_DESC)
    public Result<PageData<MessageTextListVO>> findMessageByReceiveId(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取memberId
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        params.put("receiveId", memberId);
        params.put("showFlag", MessageEnum.MESSAGE_SHOW_YES.value());
        PageData<MessageTextListDTO> pageData = messageTextService.pageByReceiveId(params);
        PageData<MessageTextListVO> dataVo = new PageData<>();
        if (CollectionUtils.isNotEmpty(pageData.getList())) {
            List<MessageTextListVO> messageTextListVOList = ConvertUtils.sourceToTarget(pageData.getList(), MessageTextListVO.class);
            dataVo.setList(messageTextListVOList);
            dataVo.setTotal(pageData.getTotal());
        }
        return new Result<PageData<MessageTextListVO>>().ok(dataVo, "查询成功");
    }

    @GetMapping("detail")
    @ApiOperation("站内信详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "站内信id", paramType = "query", required = true, dataType = "long")
    })
    @LogContext(code = PcMessageCode.MEMBER_MESSAGE_DETAIL_CODE, note = PcMessageCode.MEMBER_MESSAGE_DETAIL_DESC)
    public Result<MessageTextVO> getMessageTextDTO(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取memberId
        Long memberId = null;
        try {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<MessageTextVO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        params.put("id", memberId);
        String messageId = params.get("messageId").toString();
        MessageTextDTO messageTextDTO = messageTextService.get(Long.parseLong(messageId));
        MessageTextVO messageTextVO = ConvertUtils.sourceToTarget(messageTextDTO, MessageTextVO.class);
        //修改为已读状态
        messageReceiverService.updateStatus(params);
        return new Result<MessageTextVO>().ok(messageTextVO, "查询成功");
    }


    @GetMapping("count")
    @ApiOperation("获取未读消息总条数")
    public Result get() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<MessageTextDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        // 未读消息数量
        Integer count = messageReceiverService.selectByReceiveId(SecurityCurrentUser.getUserDetail().getId());
        return new Result().ok(count, "查询未读消息成功");
    }

    @GetMapping("read")
    @ApiOperation("一键已读")
    @LogContext(code = PcMessageCode.MEMBER_MESSAGE_READ_CODE, note = PcMessageCode.MEMBER_MESSAGE_READ_DESC)
    public Result read() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<MessageTextDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        messageReceiverService.read(SecurityCurrentUser.getUserDetail().getId());
        return new Result().ok(null, "已读成功");
    }

    @DeleteMapping
    @ApiOperation("站内信删除")
    @LogContext(code = PcMessageCode.MEMBER_MESSAGE_DELETE_CODE, note = PcMessageCode.MEMBER_MESSAGE_DELETE_DESC)
    public Result delete(@RequestBody Long[] ids) {
        AssertUtils.isArrayEmpty(ids, "请选择要删除的站内信");
        // 获取memberId
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<MessageTextDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        messageReceiverService.deleteShow(ids);
        return new Result<>().ok("删除成功");

    }
}
