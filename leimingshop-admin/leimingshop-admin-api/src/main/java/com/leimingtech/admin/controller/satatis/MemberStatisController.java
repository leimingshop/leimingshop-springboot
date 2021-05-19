/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.satatis;


import com.leimingtech.admin.controller.satatis.validate.ValiDateParams;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.member.MemberGradeShowDTO;
import com.leimingtech.modules.dto.member.MemberStatisDTO;
import com.leimingtech.modules.service.member.MemberStatisService;
import com.leimingtech.modules.statuscode.StatisticStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 会员统计
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:58
 **/
@RestController
@RequestMapping("statis/member")
@Api(tags = "会员统计")
@Slf4j
public class MemberStatisController {
    @Autowired
    private MemberStatisService memberStatisService;


    @ApiOperation("会员来源统计展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeType", value = "时间类型(1:时,2:日,3:月)", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", required = true, dataType = "Date")
    })
    @GetMapping("source")
    @LogContext(code = StatisticStatusCode.MEMBER_GROW_STATISTIC_CODE, note = StatisticStatusCode.MEMBER_GROW_STATISTIC_MASSAGE)
    public Result<List<MemberStatisDTO>> memberSource(@ApiIgnore @RequestParam Map params) {
        try {
            String s = ValiDateParams.checkTime(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
            List<MemberStatisDTO> memberStatisDTOS = memberStatisService.memberSource(params);
            return new Result<List<MemberStatisDTO>>().ok(memberStatisDTOS);
        } catch (ParseException e) {
            log.info("日期转换异常", e);
            return new Result<List<MemberStatisDTO>>().error(ErrorCode.FORBIDDEN, "日期格式错误");
        }
    }

    /**
     * 会员增长统计展示
     *
     * @param params List<GoodsSaleStatisDTO></>
     * @Author xuzhch 2019年12月17日17:03
     */
    @ApiOperation("会员增长统计展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeType", value = "时间类型(1:时,2:日,3:月)", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", required = true, dataType = "Date")
    })
    @GetMapping("member/grow/statis")
    @LogContext(code = StatisticStatusCode.MEMBER_GROW_STATISTIC_CODE, note = StatisticStatusCode.MEMBER_GROW_STATISTIC_MASSAGE)
    public Result<List<MemberStatisDTO>> memberGrowStatis(@ApiIgnore @RequestParam Map params) {
        try {
            String s = ValiDateParams.checkTime(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
            List<MemberStatisDTO> memberStatisDTOS = memberStatisService.memberGrowStatis(params);
            return new Result<List<MemberStatisDTO>>().ok(memberStatisDTOS);
        } catch (ParseException e) {
            log.info("日期转换异常", e);
            return new Result<List<MemberStatisDTO>>().error(ErrorCode.FORBIDDEN, "日期格式错误");
        }
    }

    /**
     * 会员等级占比
     *
     * @param params List<GoodsSaleStatisDTO></>
     * @Author xuzhch 2019年12月17日17:03
     */
    @ApiOperation("会员等级占比")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeType", value = "时间类型(1:时,2:日,3:月)", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间", paramType = "query", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间", paramType = "query", required = true, dataType = "Date")
    })
    @GetMapping("member/grade/proportion")
    @LogContext(code = StatisticStatusCode.MEMBER_GRADE_RATIO_STATISTIC_CODE, note = StatisticStatusCode.MEMBER_GRADE_RATIO_STATISTIC_MASSAGE)
    public Result<Map<String, List<MemberGradeShowDTO>>> memberGradeProportion(@ApiIgnore @RequestParam Map params) {
        try {
            String s = ValiDateParams.checkTime(params);
            if (StringUtils.isNotBlank(s)) {
                return new Result().error(ErrorCode.FORBIDDEN, s);
            }
            Map<String, List<MemberGradeShowDTO>> stringListMap = memberStatisService.memberGradeProportion(params);
            return new Result<Map<String, List<MemberGradeShowDTO>>>().ok(stringListMap);
        } catch (ParseException e) {
            log.info("日期转换异常", e);
            return new Result<Map<String, List<MemberGradeShowDTO>>>().error(ErrorCode.FORBIDDEN, "日期格式错误");
        }
    }
}
