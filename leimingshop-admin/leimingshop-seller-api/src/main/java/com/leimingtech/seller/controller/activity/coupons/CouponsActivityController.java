/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.activity.coupons;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.coupons.*;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.coupons.MemberCouponsService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 店铺优惠券活动管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@RestController
@RequestMapping("con/coupons")
@Api(tags = "店铺优惠券活动管理")
public class CouponsActivityController {

    @Autowired
    private CouponsActivityService couponsActivityService;

    @Autowired
    private MemberCouponsService memberCouponsService;
    @Autowired
    private StoreService storeService;

//    private Long storeId = 1196266068939710466L;
//    private String storeName = "牛家铺子";

    @GetMapping("activity/page")
    @ApiOperation("优惠券活动分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityName", value = "优惠券活动名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "couponsType", value = "优惠券类型 0满减券 1满折券", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "activityState", value = "活动状态 0未开始 1进行中 2已结束", paramType = "query", dataType = "int")
    })
    @LogContext(code = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_PAGE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_PAGE_SUCCESS_MESSAGE)
    public Result<PageData<AdminCouponsActivityPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AdminCouponsActivityPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());
        // 后台优惠券活动分页
        PageData<AdminCouponsActivityPageDTO> page = couponsActivityService.adminPage(params);

        return new Result<PageData<AdminCouponsActivityPageDTO>>().ok(page, "查询成功");
    }

    @GetMapping("activity/{id}")
    @ApiOperation("店铺优惠券活动详情")
    @LogContext(code = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_DETAIL_SUCCESS_CODE, note = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_DETAIL_SUCCESS_MESSAGE)
    public Result<AdminCouponsActivityDetailDTO> detail(@PathVariable("id") Long id,
                                                        @RequestParam(value = "type", required = false) String type,
                                                        @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<AdminCouponsActivityDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        // 后台优惠券活动详情查询
        AdminCouponsActivityDetailDTO adminCouponsActivityDetailDTO = couponsActivityService.adminDetail(id, storeId);

        if (null == adminCouponsActivityDetailDTO) {
            return new Result<AdminCouponsActivityDetailDTO>().error("该数据不存在");
        }

        // 编辑操作需校验活动状态
        if (org.apache.commons.lang3.StringUtils.equals("update", type)) {
            if (CouponsEnum.ACTIVITY_STATE_PREPARE.value() != adminCouponsActivityDetailDTO.getActivityState()) {
                return new Result<AdminCouponsActivityDetailDTO>().error("删除失败，活动状态不是未开始");
            }
        }

        return new Result<AdminCouponsActivityDetailDTO>().ok(adminCouponsActivityDetailDTO, "查询成功");
    }

    @PostMapping("activity")
    @ApiOperation("保存优惠券活动")
    @LogContext(code = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_SAVE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_SAVE_SUCCESS_MESSAGE)
    public Result save(@RequestBody InsertStoreCouponsActivityDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);

        CouponsActivityAndGoodsDTO couponsActivityAndGoodsDTO = ConvertUtils.sourceToTarget(dto, CouponsActivityAndGoodsDTO.class);

        // 活动保存参数校验 时间，优惠券面额，关联商品
        String errorMsg = this.vaildCouponActivity(couponsActivityAndGoodsDTO, dto.getRelationIds());
        if (StringUtils.isNotBlank(errorMsg)) {
            return new Result().error(ErrorCode.FORBIDDEN, errorMsg);
        }

        couponsActivityAndGoodsDTO.setId(IdGenerator.defaultSnowflakeId());
        // 店铺及优惠券默认设置
        couponsActivityAndGoodsDTO.setStoreId(sellerDetail.getStoreId());
        StoreDTO storeDTO = storeService.get(sellerDetail.getStoreId());
        if (storeDTO == null) {
            return new Result().error("店铺不存在");
        }
        couponsActivityAndGoodsDTO.setStoreName(storeDTO.getStoreName());
        couponsActivityAndGoodsDTO.setStoreLogo(storeDTO.getStoreLogo());
        getCouponActivity(couponsActivityAndGoodsDTO);

        // 获得优惠券关联商品集合
        getCouponsGoodsList(couponsActivityAndGoodsDTO, dto.getRelationIds());

        // 优惠券活动及关联商品保存
        Boolean flag = couponsActivityService.saveCouponsActivity(couponsActivityAndGoodsDTO);

        if (flag) {
            Date now = new Date();
            if (now.after(couponsActivityAndGoodsDTO.getGetEndDate())) {
                couponsActivityService.stopActivityTiming(now.getTime());
            } else if (!now.before(couponsActivityAndGoodsDTO.getGetStartDate())) {
                couponsActivityService.startActivityTiming(now.getTime());
            }
            return new Result<>().ok(null, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }

    }

    @PutMapping("activity")
    @ApiOperation("编辑优惠券活动")
    @LogContext(code = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_UPDATE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_UPDATE_SUCCESS_MESSAGE)
    public Result update(@RequestBody EditStoreCouponsActivityDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);

        CouponsActivityAndGoodsDTO couponsActivityAndGoodsDTO = ConvertUtils.sourceToTarget(dto, CouponsActivityAndGoodsDTO.class);

        // 活动保存参数校验 时间，优惠券面额，关联商品
        String errorMsg = this.vaildCouponActivity(couponsActivityAndGoodsDTO, dto.getRelationIds());
        if (StringUtils.isNotBlank(errorMsg)) {
            return new Result().error(ErrorCode.FORBIDDEN, errorMsg);
        }

        // 校验活动状态
        CouponsActivityDTO couponsActivityDTO = couponsActivityService.getByIdAndStoreId(dto.getId(), storeId);
        if (couponsActivityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "编辑失败，未查询到活动");
        }
        if (CouponsEnum.ACTIVITY_STATE_PREPARE.value() != couponsActivityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "编辑失败，活动状态不是未开始");
        }

        // 店铺及优惠券默认设置
        couponsActivityAndGoodsDTO.setStoreId(sellerDetail.getStoreId());
        StoreDTO storeDTO = storeService.get(sellerDetail.getStoreId());
        if (storeDTO == null) {
            return new Result().error("店铺不存在");
        }
        couponsActivityAndGoodsDTO.setStoreName(storeDTO.getStoreName());
        couponsActivityAndGoodsDTO.setStoreLogo(storeDTO.getStoreLogo());
        getCouponActivity(couponsActivityAndGoodsDTO);

        // 获得优惠券关联商品集合
        getCouponsGoodsList(couponsActivityAndGoodsDTO, dto.getRelationIds());

        // 优惠券活动及关联商品编辑
        Boolean flag = couponsActivityService.editCouponsActivity(couponsActivityAndGoodsDTO);

        if (flag) {
            // 优惠券活动es维护
            Date now = new Date();
            if (now.after(couponsActivityAndGoodsDTO.getGetEndDate())) {
                couponsActivityService.stopActivityTiming(now.getTime());
            } else if (!now.before(couponsActivityAndGoodsDTO.getGetStartDate())) {
                couponsActivityService.startActivityTiming(now.getTime());
            }
            return new Result<>().ok(null, "编辑成功");
        } else {
            return new Result<>().error("编辑失败");
        }
    }

    @DeleteMapping("activity/{id}")
    @ApiOperation("优惠券活动删除")
    @LogContext(code = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_DELETE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_DELETE_SUCCESS_MESSAGE)
    public Result delete(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        // 校验活动状态，只有未开始活动可以删除
        CouponsActivityDTO couponsActivityDTO = couponsActivityService.getByIdAndStoreId(id, storeId);
        if (couponsActivityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "删除失败，未查询到活动");
        }
        if (CouponsEnum.ACTIVITY_STATE_PREPARE.value() != couponsActivityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "删除失败，活动状态不是未开始");
        }

        // 优惠券活动删除
        Boolean flag = couponsActivityService.deleteCouponsActivityById(id);
        if (flag) {
            return new Result<>().ok(null, "删除成功");
        } else {
            return new Result<>().error("删除失败");
        }

    }

    @PutMapping("activity/stop/{id}")
    @ApiOperation("停止优惠券活动")
    @LogContext(code = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_STOP_SUCCESS_CODE, note = ActivityStatusCode.SELLER_COUPONS_ACTIVITY_STOP_SUCCESS_MESSAGE)
    public Result stop(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        // 校验活动状态，只有进行中的活动可以停止
        CouponsActivityDTO couponsActivityDTO = couponsActivityService.getByIdAndStoreId(id, storeId);
        if (couponsActivityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "停止失败，未查询到活动");
        }
        if (CouponsEnum.ACTIVITY_STATE_START.value() != couponsActivityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "停止失败，活动状态不是开始状态");
        }

        // 修改活动状态
        Boolean flag = couponsActivityService.stop(id);

        if (flag) {
            return new Result<>().ok(null, "停止成功");
        } else {
            return new Result<>().error("停止失敗");
        }
    }

    @GetMapping("member/page")
    @ApiOperation("优惠券领取明细分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityId", value = "活动id", paramType = "query", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "memberName", value = "领取会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "couponsState", value = "优惠券状态 0未使用 2已使用 3已过期", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "Long")
    })
    @LogContext(code = ActivityStatusCode.SELLER_COUPONS_RECEIVE_DETAIL_SUCCESS_CODE, note = ActivityStatusCode.SELLER_COUPONS_RECEIVE_DETAIL_SUCCESS_MESSAGE)
    public Result<PageData<AdminMemberCouponsPageDTO>> memberCouponsPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AdminMemberCouponsPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());
        // 后台会员优惠券分页
        PageData<AdminMemberCouponsPageDTO> page = memberCouponsService.adminPage(params);

        return new Result<PageData<AdminMemberCouponsPageDTO>>().ok(page, "查询成功");
    }

    /**
     * 功能描述:
     * 〈校验普通优惠券活动实体〉
     *
     * @param couponsActivityAndGoodsDTO 优惠券实体
     * @param relationIds                关联活动内容
     * @author : 刘远杰
     */
    private String vaildCouponActivity(CouponsActivityAndGoodsDTO couponsActivityAndGoodsDTO, List<Long> relationIds) {
        String errorMsg = "";
        // 优惠券活动时间校验
        if (couponsActivityAndGoodsDTO.getGetStartDate().after(couponsActivityAndGoodsDTO.getGetEndDate())) {
            // 领取开始时间晚于里领取结束时间
            errorMsg = "优惠券领取开始时间不能晚于领取结束时间";
        }
        if (CouponsEnum.VALIDITY_TYPE_RANGE.value() == couponsActivityAndGoodsDTO.getValidityType()) {
            // 有效日期类型为范围天数
            couponsActivityAndGoodsDTO.setValidityDays(0);
            if (couponsActivityAndGoodsDTO.getUseStartDate() == null || couponsActivityAndGoodsDTO.getUseEndDate() == null) {
                // 可用开始时间，结束时间不能为空
                errorMsg = "优惠券可用开始时间、结束时间不能为空";
            } else {
                if (couponsActivityAndGoodsDTO.getUseStartDate().after(couponsActivityAndGoodsDTO.getUseEndDate())) {
                    // 可用开始时间晚于可用结束时间
                    errorMsg = "优惠券可用开始时间不能晚于可用结束时间";
                }
                if (couponsActivityAndGoodsDTO.getGetEndDate().after(DateUtils.addDateSeconds(DateUtils.addDateDays(couponsActivityAndGoodsDTO.getUseEndDate(), 1), -1))) {
                    // 领取结束时间晚于可用结束时间
                    errorMsg = "优惠券领取结束时间不能晚于可用结束时间";
                }
            }
        } else if (CouponsEnum.VALIDITY_TYPE_DAYS.value() == couponsActivityAndGoodsDTO.getValidityType()) {
            // 有效日期类型为固定天数
            couponsActivityAndGoodsDTO.setUseStartDate(null);
            couponsActivityAndGoodsDTO.setUseEndDate(null);
            if (couponsActivityAndGoodsDTO.getValidityDays() == null) {
                errorMsg = "优惠券有效天数不能为空";
            } else if (couponsActivityAndGoodsDTO.getValidityDays() <= 0) {
                errorMsg = "优惠券有效天数不能小于1天";
            }
        }
        // 优惠券金额校验
        if (couponsActivityAndGoodsDTO.getLimitAmount().compareTo(couponsActivityAndGoodsDTO.getFaceValue()) <= 0) {
            // 使用条件小于等于面额
            errorMsg = "优惠券面额必须小于使用条件";
        }
        // 优惠券关联商品非空校验
        if (CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() != couponsActivityAndGoodsDTO.getActivityGoodsScope()) {
            if (CollectionUtils.isEmpty(relationIds)) {
                errorMsg = "优惠券可用范围不能为空";
            }
        }
        return errorMsg;
    }

    /**
     * 功能描述:
     * 〈店铺及优惠券默认设置〉
     *
     * @param couponsActivityAndGoodsDTO 优惠券及商品实体
     * @author : 刘远杰
     */
    private void getCouponActivity(CouponsActivityAndGoodsDTO couponsActivityAndGoodsDTO) {
        // 设置剩余数量为优惠券总数
        couponsActivityAndGoodsDTO.setSurplusNum(couponsActivityAndGoodsDTO.getTotalNum());

        // 其他默认值 活动范围、活动状态、审核状态、优惠券类型、活动共享
        couponsActivityAndGoodsDTO.setActivityScene(CouponsEnum.ACTIVITY_SCENE_NORMAL.value());
        couponsActivityAndGoodsDTO.setActivityScope(CouponsEnum.ACTIVITY_SCOPE_STORE.value());
        couponsActivityAndGoodsDTO.setAuditState(CouponsEnum.AUDIT_STATE_PASS.value());
        couponsActivityAndGoodsDTO.setActivityState(CouponsEnum.ACTIVITY_STATE_PREPARE.value());
        couponsActivityAndGoodsDTO.setCouponsType(CouponsEnum.COUPONS_TYPE_REDUCE.value());
        couponsActivityAndGoodsDTO.setShareFlag(CouponsEnum.SHARE_FLAG_NO.value());
    }

    /**
     * 功能描述:
     * 〈获得优惠券关联商品集合〉
     *
     * @param couponsActivityAndGoodsDTO 优惠券及商品实体
     * @param relationIds                关联商品id
     * @author : 刘远杰
     */
    private void getCouponsGoodsList(CouponsActivityAndGoodsDTO couponsActivityAndGoodsDTO, List<Long> relationIds) {
        List<CouponsGoodsDTO> couponsGoodsDTOList = new ArrayList<>();
        Integer activityGoodsScope = couponsActivityAndGoodsDTO.getActivityGoodsScope();
        if (CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == activityGoodsScope) {
            // 店铺通用优惠券，设置关联信息为店铺信息
            CouponsGoodsDTO couponsGoodsDTO = new CouponsGoodsDTO();
            couponsGoodsDTO.setActivityId(couponsActivityAndGoodsDTO.getId());
            couponsGoodsDTO.setRelationId(couponsActivityAndGoodsDTO.getStoreId());
            couponsGoodsDTO.setActivityGoodsScope(activityGoodsScope);
            couponsGoodsDTOList.add(couponsGoodsDTO);
            couponsActivityAndGoodsDTO.setCouponsGoodsDTOList(couponsGoodsDTOList);
        } else {
            // 不为店铺级别优惠券
            if (CollectionUtils.isNotEmpty(relationIds)) {
                relationIds.forEach(relationId -> {
                    CouponsGoodsDTO couponsGoodsDTO = new CouponsGoodsDTO();
                    couponsGoodsDTO.setActivityId(couponsActivityAndGoodsDTO.getId());
                    couponsGoodsDTO.setRelationId(relationId);
                    couponsGoodsDTO.setActivityGoodsScope(activityGoodsScope);
                    couponsGoodsDTOList.add(couponsGoodsDTO);
                });
            }
            couponsActivityAndGoodsDTO.setCouponsGoodsDTOList(couponsGoodsDTOList);
        }
    }

}
