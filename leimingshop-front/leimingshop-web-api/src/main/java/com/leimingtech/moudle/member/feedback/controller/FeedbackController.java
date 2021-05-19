/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.feedback.controller;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.dto.feedback.FeedbackDTO;
import com.leimingtech.dto.feedback.SaveFeedbackDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.enums.complain.OrderComplainEnum;
import com.leimingtech.moudle.member.feedback.vo.FeedbackVO;
import com.leimingtech.security.SecurityCurrentUser;
import com.leimingtech.service.feedback.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 意见反馈信息表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@RestController
@RequestMapping("feedback")
@Api(tags = "意见反馈信息表")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 反馈分页
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<FeedbackVO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<PageData<FeedbackVO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        MemberDTO memberDTO = SecurityCurrentUser.getUserDetail();
        params.put("memberId", memberDTO.getId());
        params.put("showFlag", OrderComplainEnum.SHOW_FLAG_YES.value());
        PageData<FeedbackDTO> page = feedbackService.page(params);

        PageData<FeedbackVO> pageData = new PageData<>();
        pageData.setTotal(page.getTotal());
        List<FeedbackVO> feedbackVOList = ConvertUtils.sourceToTarget(page.getList(), FeedbackVO.class);
        pageData.setList(feedbackVOList);
        return new Result<PageData<FeedbackVO>>().ok(pageData);
    }

    /**
     * 反馈详情
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<FeedbackVO> find(@PathVariable("id") Long id) {
        FeedbackDTO data = feedbackService.get(id);

        return new Result<FeedbackVO>().ok(ConvertUtils.sourceToTarget(data, FeedbackVO.class));
    }

    /**
     * 保存反馈信息
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public Result<Long> save(@RequestBody SaveFeedbackDTO dto, HttpServletRequest request) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<Long>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        String memberSource = request.getHeader("memberSource");
        MemberDTO memberDTO = SecurityCurrentUser.getUserDetail();
        dto.setMemberId(memberDTO.getId());
        dto.setMemberName(memberDTO.getMemberName());
        dto.setFeedbackSource(Integer.valueOf(memberSource));
        Long id = feedbackService.save(dto);

        return new Result().ok(id, "保存成功");
    }

    /**
     * 删除反馈记录
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除反馈记录")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id不能为空");

        feedbackService.delete(ids);

        return new Result().ok("删除成功");
    }


}
