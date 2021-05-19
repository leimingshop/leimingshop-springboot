/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.feedback;

import com.leimingtech.admin.excel.feedback.FeedbackExcel;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.SysDictDTO;
import com.leimingtech.dto.feedback.FeedbackDTO;
import com.leimingtech.dto.feedback.UpdateFeedbackDTO;
import com.leimingtech.service.feedback.FeedbackService;
import com.leimingtech.service.sys.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
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
@Api(tags = "意见反馈信息")
public class FeedbackController {

    @Autowired
    private SysDictService sysDictService;

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
            @ApiImplicitParam(name = "memberName", value = "会员账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobileNumber", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "feedbackType", value = "反馈类型 1：功能异常、2：优化建议、3：其他反馈", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "feedbackSource", value = "反馈来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeStatus", value = "处理状态(默认0:未处理、1：已处理)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "decideType", value = "反馈判定（0:无效反馈、1:有效反馈、2:重点问题、)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeStartDate", value = "处理开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeEndDate", value = "处理结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createStartDate", value = "反馈开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createEndDate", value = "反馈结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "operator", value = "处理人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<FeedbackDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<FeedbackDTO> page = feedbackService.page(params);

        return new Result<PageData<FeedbackDTO>>().ok(page);
    }

    /**
     * 反馈详情
     */


    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<FeedbackDTO> get(@PathVariable("id") Long id) {
        FeedbackDTO data = feedbackService.get(id);

        return new Result<FeedbackDTO>().ok(data);
    }

    /**
     * 处理反馈信息
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("处理反馈信息")
    public Result update(@RequestBody UpdateFeedbackDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        FeedbackDTO feedbackDTO = ConvertUtils.sourceToTarget(dto, FeedbackDTO.class);
        feedbackDTO.setDisposeDate(new Date());
        feedbackDTO.setOperator(SecurityUser.getUserName());
        feedbackDTO.setDisposeStatus(1);
        feedbackService.update(feedbackDTO);

        return new Result().ok("处理成功");
    }

    /**
     * 获取反馈类型
     *
     * @throws Exception
     */
    @GetMapping("type")
    @ApiOperation("获取反馈类型")
    public Result<List<SysDictDTO>> feedbackType() {
        //TODO  暂时从数据字典中获取，后期优化此功能
        Map<String, Object> map = new HashMap<>(16);
        map.put("dictType", "feedback");
        map.put("pid", "1249610251246784513");
        List<SysDictDTO> list = sysDictService.list(map);
        return new Result<List<SysDictDTO>>().ok(list);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberName", value = "会员账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobileNumber", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "feedbackType", value = "反馈类型 1：功能异常、2：优化建议、3：其他反馈", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "feedbackSource", value = "反馈来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeStatus", value = "处理状态(默认0:未处理、1：已处理)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "decideType", value = "反馈判定（0:无效反馈、1:有效反馈、2:重点问题、)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeStartDate", value = "处理开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeEndDate", value = "处理结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createStartDate", value = "反馈开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createEndDate", value = "反馈结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "operator", value = "处理人", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<FeedbackDTO> list = feedbackService.list(params);

        EasyExcelUtils.exportExcelToTarget(response, null, list, FeedbackExcel.class);
    }

}
