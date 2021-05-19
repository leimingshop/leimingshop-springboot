/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.coupons.FrontCouponsActivityPageDTO;
import com.leimingtech.modules.dto.coupons.FrontMyCouponsPageDTO;
import com.leimingtech.modules.dto.coupons.GoodsDetailCouponsListDTO;
import com.leimingtech.modules.service.coupons.CouponsActivitySearchService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.moudle.activity.code.PcActivityCode;
import com.leimingtech.moudle.activity.vo.coupon.CouponCenterVO;
import com.leimingtech.moudle.activity.vo.coupon.GoodsDetailCouponsListVO;
import com.leimingtech.moudle.activity.vo.coupon.MyCouponsVO;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 优惠券活动API接口
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/16 14:35
 **/
@Slf4j
@RestController
@RequestMapping("activity/coupon")
@Api(tags = "优惠券活动")
public class CouponActivityController {

    @Resource
    private CouponsActivitySearchService couponsActivitySearchService;

    @Resource
    private CouponsActivityService couponsActivityService;

    @GetMapping("center")
    @ApiOperation("领劵中心")
    @LogContext(code = PcActivityCode.HOME_COUPON_CENTER_CODE, note = PcActivityCode.HOME_COUPON_CENTER_DESC)
    public Result<List<CouponCenterVO>> getCouponList() {
        //获取用户信息
        Long memberId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        }
        // 查询优惠券信息
        List<FrontCouponsActivityPageDTO> frontCouponsActivityPageList = couponsActivitySearchService.recommendCouponsList(memberId);
        return new Result<List<CouponCenterVO>>().ok(ConvertUtils.sourceToTarget(frontCouponsActivityPageList, CouponCenterVO.class));
    }

    @PostMapping("receive/{couponId}")
    @ApiOperation("优惠券领取")
    @ApiImplicitParam(name = "couponId", value = "优惠券ID", required = true, paramType = "path", dataType = "long")
    @LogContext(code = PcActivityCode.HOME_RECEIVE_COUPON_CODE, note = PcActivityCode.HOME_RECEIVE_COUPON_DESC)
    public Result<Object> receviedCoupons(@PathVariable("couponId") Long couponId) {
        //获取用户信息
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "您未登录，无法进行此操作");
        }
        Long memberId = SecurityCurrentUser.getUserDetail().getId();

        // 领取操作
        if (couponsActivityService.receivedCoupons(couponId, memberId)) {
            return new Result<>().ok(null, "领取成功");
        } else {
            return new Result<>().error("领取失败");
        }
    }

    @GetMapping("goods/detail")
    @ApiOperation("商品详情页-优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品spuID", required = true, paramType = "query", dataType = "long"),
    })
    @LogContext(code = PcActivityCode.GOODS_DETAIL_COUPON_LIST_CODE, note = PcActivityCode.GOODS_DETAIL_COUPON_LIST_DESC)
    public Result<GoodsDetailCouponsListVO> goodsDetailCouponsList(@ApiIgnore @RequestParam Map<String, Object> params) {
        //获取用户信息
        if (SecurityCurrentUser.userIsLogin()) {
            Long memberId = SecurityCurrentUser.getUserDetail().getId();
            params.put("memberId", memberId.toString());
        }
        GoodsDetailCouponsListDTO goodsDetailCouponsListDTO = couponsActivitySearchService.goodsDetailCouponsList(params);
        GoodsDetailCouponsListVO couponsListVO = ConvertUtils.sourceToTarget(goodsDetailCouponsListDTO,
                GoodsDetailCouponsListVO.class);
        return new Result<GoodsDetailCouponsListVO>().ok(couponsListVO, "查询成功");
    }


    @GetMapping("center/list")
    @ApiOperation("个人中心-优惠卷列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponsState", value = "优惠券状态 0未使用 1已使用 2已过期", required = true, paramType = "query", dataType = "int"),
    })
    @LogContext(code = PcActivityCode.CENTER_COUPON_LIST_CODE, note = PcActivityCode.CENTER_COUPON_LIST_DESC)
    public Result<List<MyCouponsVO>> myCouponsList(@ApiIgnore @RequestParam Map<String, Object> params) {
        //获取用户信息
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<List<MyCouponsVO>>().error(ErrorCode.UNAUTHORIZED, "您未登录，无法进行此操作");
        }
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        params.put("memberId", memberId.toString());
        List<FrontMyCouponsPageDTO> myCouponsPageDTOList = couponsActivitySearchService.myCouponsList(params);
        return new Result<List<MyCouponsVO>>().ok(ConvertUtils.sourceToTarget(myCouponsPageDTOList, MyCouponsVO.class), "查询成功");
    }

}
