/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.modules.dto.activity.goods.SetRemingDTO;
import com.leimingtech.modules.dto.seckill.PrestartSeckillGoodsPageDTO;
import com.leimingtech.modules.dto.seckill.PrestartSeckillSessionDTO;
import com.leimingtech.modules.dto.seckill.SeckillGoodsPageDTO;
import com.leimingtech.modules.dto.seckill.StartSeckillSessionDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.seckill.SeckillActivitySearchService;
import com.leimingtech.moudle.activity.code.PcActivityCode;
import com.leimingtech.moudle.activity.vo.seckill.*;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 活动API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("activity/seckill")
@Api(tags = "秒杀活动")
public class SecKillActivityController {

    @Resource
    private SeckillActivitySearchService seckillActivitySearchService;

    @Resource
    private ActivityGoodsService activityGoodsService;

    @GetMapping("/home")
    @ApiOperation("首页-秒杀专场")
    @LogContext(code = PcActivityCode.HOME_SECKILL_CODE, note = PcActivityCode.HOME_SECKILL_DESC)
    public Result<HomePageSeckillVO> getHomePageSeckill() {

        HomePageSeckillVO homePageSeckillVO = new HomePageSeckillVO();

        // 查询正在进行秒杀场次
        StartSeckillSessionDTO seckillData = Optional.ofNullable(seckillActivitySearchService.startSeckillSession())
                .orElse(new StartSeckillSessionDTO());
        homePageSeckillVO.setCurrentDateStr(DateUtils.format(seckillData.getActivityStartDate(), "HH:mm"));
        homePageSeckillVO.setActivityEndDate(seckillData.getActivityEndDate());

        // 查询即将秒杀场次 TODO lixiang 应提取单一的方法
        List<PrestartSeckillSessionDTO> preSeckillList = seckillActivitySearchService.prestartSeckillSessionList();
        if (CollectionUtils.isNotEmpty(preSeckillList)) {
            homePageSeckillVO.setNextDateStr(preSeckillList.get(0).getActivityStartDateStr());
        }

        // 查询秒杀商品信息  只取出前4条
        Map<String, Object> paramMap = new HashMap<>(3);
        paramMap.put(Constant.PAGE, 1);
        paramMap.put(Constant.LIMIT, 4);
        paramMap.put("sessionId", seckillData.getSessionId());
        PageData<SeckillGoodsPageDTO> pageData = seckillActivitySearchService.startSeckillGoodsPage(paramMap);
        homePageSeckillVO.setSeckillGoodsList(ConvertUtils.sourceToTarget(pageData.getList(), SeckillGoodsVO.class));

        return new Result<HomePageSeckillVO>().ok(homePageSeckillVO);
    }


    @GetMapping("/area/time")
    @ApiOperation("秒杀专区-时间段")
    @LogContext(code = PcActivityCode.HOME_SECKILL_AREA_CODE, note = PcActivityCode.HOME_SECKILL_AREA_DESC)
    public Result<SeckillTimeSoltVO> getSeckillArea() {
        SeckillTimeSoltVO seckillTimeSoltVO = new SeckillTimeSoltVO();

        // 查询正在进行秒杀场次
        StartSeckillSessionDTO seckillData = Optional.ofNullable(seckillActivitySearchService.startSeckillSession())
                .orElse(new StartSeckillSessionDTO());
        seckillTimeSoltVO.setActivityEndDate(seckillData.getActivityEndDate());
        seckillTimeSoltVO.setCurrentDateStr(DateUtils.format(seckillData.getActivityStartDate(), "HH:mm"));
        seckillTimeSoltVO.setSessionId(seckillData.getSessionId());

        // 查询即将开始的场次集合
        List<PrestartSeckillSessionDTO> seckillSessionDTOList = seckillActivitySearchService.prestartSeckillSessionList();
        seckillTimeSoltVO.setSoonTimeSoltList(ConvertUtils.sourceToTarget(seckillSessionDTOList, SeckillSoonTimeSoltVO.class));

        return new Result<SeckillTimeSoltVO>().ok(seckillTimeSoltVO);
    }

    @GetMapping("/current/goods")
    @ApiOperation("秒杀专区-正在秒杀商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "sessionId", value = "秒杀场次id", required = true, paramType = "query", dataType = "long")
    })
    @LogContext(code = PcActivityCode.HOME_SECKILL_CURRENT_GOODS_CODE, note = PcActivityCode.HOME_SECKILL_CURRENT_GOODS_DESC)
    public Result<PageData<SeckillGoodsVO>> getCurrentSeckillGoodsList(@ApiIgnore @RequestParam Map<String, Object> params) {

        // 查询正在进行秒杀场次商品分页
        PageData<SeckillGoodsPageDTO> page = seckillActivitySearchService.startSeckillGoodsPage(params);
        PageData<SeckillGoodsVO> pageData = new PageData<>();
        BeanCopier.create(PageData.class, PageData.class, false)
                .copy(page, pageData, null);
        return new Result<PageData<SeckillGoodsVO>>().ok(pageData);
    }

    @GetMapping("/soon/goods")
    @ApiOperation("秒杀专区-即将秒杀商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "sessionId", value = "秒杀场次id", required = true, paramType = "query", dataType = "long")
    })
    @LogContext(code = PcActivityCode.HOME_SECKILL_CURRENT_GOODS_CODE, note = PcActivityCode.HOME_SECKILL_CURRENT_GOODS_DESC)
    public Result<PageData<SoonSeckillGoodsVO>> getSoonSeckillGoodsList(@ApiIgnore @RequestParam Map<String, Object> params) {

        //获取用户信息
        Long memberId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        }
        // 查询正在进行秒杀场次商品分页
        PageData<PrestartSeckillGoodsPageDTO> page = seckillActivitySearchService.seckillGoodsPage(params, memberId);
        PageData<SoonSeckillGoodsVO> pageData = new PageData<>();
        BeanCopier.create(PageData.class, PageData.class, false)
                .copy(page, pageData, null);
        return new Result<PageData<SoonSeckillGoodsVO>>().ok(pageData);
    }

    @PostMapping("/remind")
    @ApiOperation("秒杀专区-设置提醒")
    @LogContext(code = PcActivityCode.HOME_SECKILL_REMIND_CODE, note = PcActivityCode.HOME_SECKILL_REMIND_DESC)
    public Result<Object> remindSeckill(@RequestBody SetRemingDTO setRemingDTO) {
        // 数据校验
        ValidatorUtils.validateEntity(setRemingDTO, AddGroup.class);
        setRemingDTO.setActivityType(ActivityTypeEnum.SECKILL_ACTIVITY.value());

        //获取用户信息
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "您未登录，无法进行此操作");
        }
        Long memberId = SecurityCurrentUser.getUserDetail().getId();

        // 设置秒杀提醒
        activityGoodsService.remindSetting(setRemingDTO, memberId);

        return new Result<>().ok(null, "设置成功");
    }


}
