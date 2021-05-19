/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.enums.setting.PointSettingEnum;
import com.leimingtech.modules.dto.log.point.PointLogDTO;
import com.leimingtech.modules.dto.member.MemberPersonCenterDTO;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.moudle.member.member.code.MemberCode;
import com.leimingtech.moudle.member.member.vo.point.PcPointLogDetailVo;
import com.leimingtech.moudle.member.member.vo.point.PcPointLogVo;
import com.leimingtech.security.SecurityCurrentUser;
import com.leimingtech.service.setting.PointSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;


/**
 * 积分日志
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */
@RestController
@RequestMapping("member/point/log")
@Api(tags = "积分日志")
@Slf4j
public class PointLogController {

    @Autowired
    private PointLogService pointLogService;

    @Autowired
    private PointSettingService pointSettingService;

    @Autowired
    private MemberService memberService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @LogContext(code = MemberCode.MEMBER_POINT_LIST_CODE, note = MemberCode.MEMBER_POINT_LIST_DESC)
    public Result<PageData<PcPointLogVo>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<PageData<PcPointLogVo>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("pointType", MemberPointLogEnum.POINT_TYPE.code());
        params.put("memberId", SecurityCurrentUser.getUserDetail().getId().toString());
        PageData<PointLogDTO> dtoPageData = pointLogService.page(params);

        PageData<PcPointLogVo> pageData = new PageData<>();

        BeanCopier.create(PageData.class, PageData.class, false)
                .copy(dtoPageData, pageData, null);
        return new Result<PageData<PcPointLogVo>>().ok(pageData);
    }


    /**
     * 获取积分说明
     *
     * @return 积分说明
     * @date 2020/1/19 10:19
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("desc")
    @ApiOperation(value = "用户中心-积分说明")
    public Result<String> growthDec() {
        // 从redis查询奖励设置信息
        String growthDesc = pointSettingService.findFromRedis(PointSettingEnum.REWARD_SETTING_NAME.value());

        // 解析积分说明
        String pointDescription = JSONObject.parseObject(growthDesc).getJSONObject("rewardDescription").getString("pointDescription");
        return new Result<String>().ok(pointDescription);
    }

    @GetMapping("detail")
    @ApiOperation(value = "用户中心-积分详情")
    @LogContext(code = MemberCode.MEMBER_POINT_DETAIL_CODE, note = MemberCode.MEMBER_POINT_DETAIL_DESC)
    public Result<PcPointLogDetailVo> pointDetail() {

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<PcPointLogDetailVo>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        MemberPersonCenterDTO memberPersonCenterDTO = memberService.selectMember(SecurityCurrentUser.getUserDetail().getId());

        PcPointLogDetailVo pcPointLogDetailVo = new PcPointLogDetailVo();
        pcPointLogDetailVo.setAvailablePoint(memberPersonCenterDTO.getPointValue());
        return new Result<PcPointLogDetailVo>().ok(pcPointLogDetailVo);
    }

    @GetMapping("recent")
    @ApiOperation(value = "用户中心-近期积分明细")
    @LogContext(code = MemberCode.MEMBER_POINT_DETAIL_CODE, note = MemberCode.MEMBER_POINT_DETAIL_DESC)
    public Result<List<PcPointLogVo>> reventPoint() {

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<List<PcPointLogVo>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Map<String, Object> memberParams = new HashMap<>(3);
        memberParams.put("memberId", SecurityCurrentUser.getUserDetail().getId().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        Date sevenDate = calendar.getTime();
        String from = DateUtils.format(sevenDate, DateUtils.DATE_TIME_PATTERN);

        memberParams.put("endTime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        memberParams.put("startTime", from);
        List<PointLogDTO> pointLogDTOList = pointLogService.queryByTime(memberParams);
        List<PcPointLogVo> pointLogVoList = ConvertUtils.sourceToTarget(pointLogDTOList, PcPointLogVo.class);
        return new Result<List<PcPointLogVo>>().ok(pointLogVoList);

    }

}
