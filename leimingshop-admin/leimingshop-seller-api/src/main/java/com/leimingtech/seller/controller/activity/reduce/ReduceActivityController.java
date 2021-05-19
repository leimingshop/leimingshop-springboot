/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.activity.reduce;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.reduce.*;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
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
 * 店铺满减活动管理
 *
 * @author 魏显春
 * @since v1.0.0 2019-12-05
 */
@RestController
@RequestMapping("reduce")
@Api(tags = "店铺满减活动管理")
public class ReduceActivityController {

    @Autowired
    private ReduceActivityService reduceActivityService;

    @Autowired
    private StoreService storeService;

    // private Long storeId = 1196266068939710466L;

    @GetMapping("activity/page")
    @ApiOperation("满减活动分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityName", value = "满减活动名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ruleType", value = "活动类型 0普通满减 1每满减 2阶梯满减", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "activityState", value = "活动状态 0未开始 1进行中 2已结束", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "活动开始日期", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "活动结束日期", paramType = "query", dataType = "Date")
    })
    @LogContext(code = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_PAGE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_PAGE_SUCCESS_MESSAGE)
    public Result<PageData<ReduceActivityPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<ReduceActivityPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());

        // 后台满减活动分页
        PageData<ReduceActivityPageDTO> page = reduceActivityService.reduceActivityPage(params);

        return new Result<PageData<ReduceActivityPageDTO>>().ok(page, "查询成功");
    }

    @GetMapping("activity/{id}")
    @ApiOperation("店铺满减活动详情")
    @LogContext(code = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_DETAIL_SUCCESS_CODE, note = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_DETAIL_SUCCESS_MESSAGE)
    public Result<ReduceActivityDeatilDTO> detail(@PathVariable("id") Long id,
                                                  @RequestParam(value = "type", required = false) String type,
                                                  @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<ReduceActivityDeatilDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 后台满减活动详情查询
        ReduceActivityDeatilDTO deatilDTO = reduceActivityService.reduceActivityDeatil(id, storeId);

        if (null == deatilDTO) {
            return new Result<ReduceActivityDeatilDTO>().error("该数据不存在");
        }

        // 编辑操作需校验活动状态
        if (StringUtils.equals("update", type)) {
            if (ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value() != deatilDTO.getActivityState()) {
                return new Result<ReduceActivityDeatilDTO>().error("删除失败，活动状态不是未开始");
            }
        }

        return new Result<ReduceActivityDeatilDTO>().ok(deatilDTO, "查询成功");
    }

    @DeleteMapping("activity/{id}")
    @ApiOperation("满减活动删除")
    @LogContext(code = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_DELETE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_DELETE_SUCCESS_MESSAGE)
    public Result delete(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 校验活动状态，只有未开始的满减活动可以删除
        ReduceActivityDTO reduceActivityDTO = reduceActivityService.findByIdAndStoreId(id, storeId);
        if (reduceActivityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "删除失败，未查询到活动");
        }
        if (ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value() != reduceActivityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "删除失败，活动状态不是未开始");
        }

        // 满减活动删除
        Boolean flag = reduceActivityService.deleteByActivityId(id);
        if (flag) {
            return new Result<>().ok(null, "删除成功");
        } else {
            return new Result<>().error("删除失败");
        }

    }

    @PutMapping("activity/stop/{id}")
    @ApiOperation("停止满减活动")
    @LogContext(code = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_STOP_SUCCESS_CODE, note = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_STOP_SUCCESS_MESSAGE)
    public Result stop(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 校验活动状态，只有进行中的活动可以停止
        ReduceActivityDTO reduceActivityDTO = reduceActivityService.findByIdAndStoreId(id, storeId);
        if (reduceActivityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "停止失败，未查询到活动");
        }
        if (ReduceActivityEnum.ACTIVITY_STATE_START.value() != reduceActivityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "停止失败，活动状态不是开始状态");
        }

        // 修改活动状态
        Boolean flag = reduceActivityService.stop(id);
        if (flag) {
            return new Result<>().ok(null, "停止成功");
        } else {
            return new Result<>().error("停止失败");
        }
    }

    @PostMapping("activity")
    @ApiOperation("保存满减活动")
    @LogContext(code = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_SAVE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_SAVE_SUCCESS_MESSAGE)
    public Result save(@RequestBody ReduceActivitySaveDTO dto, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);

        ReduceAndGoodsDTO reduceAndGoodsDTO = ConvertUtils.sourceToTarget(dto, ReduceAndGoodsDTO.class);

        // 活动保存参数校验 时间，关联商品,规则
        String errorMsg = this.vaildReduceActivity(reduceAndGoodsDTO, dto.getRelationIds());
        if (StringUtils.isNotBlank(errorMsg)) {
            return new Result().error(ErrorCode.FORBIDDEN, errorMsg);
        }

        reduceAndGoodsDTO.setId(IdGenerator.defaultSnowflakeId());
        // 店铺及满减活动默认设置
        reduceAndGoodsDTO.setStoreId(sellerDetail.getStoreId());
        StoreDTO storeDTO = storeService.get(sellerDetail.getStoreId());
        if (storeDTO == null) {
            return new Result().error("店铺不存在");
        }
        reduceAndGoodsDTO.setStoreName(storeDTO.getStoreName());
        reduceAndGoodsDTO.setStoreLogo(storeDTO.getStoreLogo());
        this.activityDefault(reduceAndGoodsDTO);

        // 获得满减关联商品集合
        this.getReducesGoodsList(reduceAndGoodsDTO, dto.getRelationIds());

        //满减规则集合信息
        dto.setReduceRuleSaveDTOList(dto.getReduceRuleSaveDTOList());

        // 满减活动,关联商品信息保存
        Boolean flag = reduceActivityService.saveReduceActivity(reduceAndGoodsDTO);

        if (flag) {
            // 满减活动es委会
            Date now = new Date();
            if (now.after(reduceAndGoodsDTO.getEndDate())) {
                reduceActivityService.stopActivityTiming(now.getTime());
            } else if (!now.before(reduceAndGoodsDTO.getStartDate())) {
                reduceActivityService.startActivityTiming(now.getTime());
            }
            return new Result<>().ok(null, "保存成功");
        }
        return new Result<>().error("保存失败");
    }

    /**
     * @Author weixianchun
     * @Description 活动保存参数校验 时间，关联商品,规则(封装)
     * @Param reduceAndGoodsDTO
     * @Param relationIds
     * @Date 2019/12/26 16:56
     * @Return java.lang.String
     * @version 1.0
     */
    private String vaildReduceActivity(ReduceAndGoodsDTO reduceAndGoodsDTO, List<Long> relationIds) {
        String errorMsg = "";
        // 活动时间校验
        if (reduceAndGoodsDTO.getStartDate().after(reduceAndGoodsDTO.getEndDate())) {
            // 开始时间晚于结束时间
            errorMsg = "满减开始时间不能晚于结束时间";
        }

        // 活动规则校验
        List<ReduceRuleSaveDTO> ruleList = reduceAndGoodsDTO.getReduceRuleSaveDTOList();
        if (CollectionUtils.isEmpty(ruleList)) {
            errorMsg = "满减规则不能为空";
        } else {
            for (ReduceRuleSaveDTO rule : ruleList) {
                if (rule.getLimitAmount().compareTo(rule.getReduceAmount()) <= 0) {
                    errorMsg = "活动限制金额必须大于优惠金额";
                    break;
                }
            }
        }

        // 满减关联商品非空校验
        if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_ALL.value() != reduceAndGoodsDTO.getActivityGoodsScope()) {
            if (CollectionUtils.isEmpty(relationIds)) {
                errorMsg = "满减可用范围不能为空";
            }
        }
        return errorMsg;
    }

    /**
     * @Author weixianchun
     * @Description 满减活动默认设置
     * @Param reduceAndGoodsDTO
     * @Date 2019/12/26 17:00
     * @Return
     * @version 1.0
     */
    private void activityDefault(ReduceAndGoodsDTO reduceAndGoodsDTO) {

        // 其他默认值 活动范围、审核状态、活动状态,是否可用优惠券
        reduceAndGoodsDTO.setActivityScope(ReduceActivityEnum.ACTIVITY_SCOPE_STORE.value());
        reduceAndGoodsDTO.setAuditState(ReduceActivityEnum.AUDIT_STATE_PASS.value());
        reduceAndGoodsDTO.setActivityState(ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value());
        reduceAndGoodsDTO.setCouponsFlag(ReduceActivityEnum.UNABLE_COUPONS_FLAG.value());
    }


    /**
     * @Author weixianchun
     * @Description 满减关联商品集合信息封装
     * @Param reduceAndGoodsDTO
     * @Param relationIds
     * @Date 2019/12/26 17:49
     * @Return void
     * @version 1.0
     */
    private void getReducesGoodsList(ReduceAndGoodsDTO reduceAndGoodsDTO, List<Long> relationIds) {

        List<ReduceGoodsDTO> reduceGoodsDTOList = new ArrayList<>();
        Integer activityGoodsScope = reduceAndGoodsDTO.getActivityGoodsScope();
        if (CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == activityGoodsScope) {
            // 店铺通用满减，设置关联信息为店铺信息
            ReduceGoodsDTO reduceGoodsDTO = new ReduceGoodsDTO();
            reduceGoodsDTO.setActivityId(reduceAndGoodsDTO.getId());
            reduceGoodsDTO.setRelationId(reduceAndGoodsDTO.getStoreId());
            reduceGoodsDTO.setActivityGoodsScope(activityGoodsScope);
            reduceGoodsDTOList.add(reduceGoodsDTO);
            reduceAndGoodsDTO.setReduceGoodsDTOList(reduceGoodsDTOList);
        } else {
            // 不为店铺级别满减
            if (CollectionUtils.isNotEmpty(relationIds)) {
                relationIds.forEach(relationId -> {
                    ReduceGoodsDTO reduceGoodsDTO = new ReduceGoodsDTO();
                    reduceGoodsDTO.setActivityId(reduceAndGoodsDTO.getId());
                    reduceGoodsDTO.setRelationId(relationId);
                    reduceGoodsDTO.setActivityGoodsScope(activityGoodsScope);
                    reduceGoodsDTOList.add(reduceGoodsDTO);
                });
            }
            reduceAndGoodsDTO.setReduceGoodsDTOList(reduceGoodsDTOList);
        }
    }


    @PutMapping("activity")
    @ApiOperation("编辑满减活动")
    @LogContext(code = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_UPDATE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_REDUCE_ACTIVITY_UPDATE_SUCCESS_MESSAGE)
    public Result editActivity(@RequestBody ReduceActivityEditDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);

        ReduceAndGoodsDTO reduceAndGoodsDTO = ConvertUtils.sourceToTarget(dto, ReduceAndGoodsDTO.class);

        // 活动保存参数校验 时间，关联商品,规则
        String errorMsg = this.vaildReduceActivity(reduceAndGoodsDTO, dto.getRelationIds());
        if (StringUtils.isNotBlank(errorMsg)) {
            return new Result().error(ErrorCode.FORBIDDEN, errorMsg);
        }

        //校验活动状态
        ReduceActivityDTO reduceActivityDTO = reduceActivityService.get(dto.getId());
        if (reduceActivityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "编辑失败，未查询到活动");
        }
        if (ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value() != reduceActivityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "编辑失败，活动状态不是未开始");
        }

        // 店铺及满减活动默认设置
        reduceAndGoodsDTO.setStoreId(storeId);
        StoreDTO storeDTO = storeService.get(storeId);
        if (storeDTO == null) {
            return new Result().error("店铺不存在");
        }
        reduceAndGoodsDTO.setStoreName(storeDTO.getStoreName());
        reduceAndGoodsDTO.setStoreLogo(storeDTO.getStoreLogo());
        this.activityDefault(reduceAndGoodsDTO);

        // 获得满减关联商品集合
        this.getReducesGoodsList(reduceAndGoodsDTO, dto.getRelationIds());

        //满减规则集合信息
        dto.setReduceRuleSaveDTOList(dto.getReduceRuleSaveDTOList());

        // 满减活动,关联商品信息编辑
        Boolean flag = reduceActivityService.editReduceActivity(reduceAndGoodsDTO);

        if (flag) {
            // 满减活动es维护
            Date now = new Date();
            if (now.after(reduceAndGoodsDTO.getEndDate())) {
                reduceActivityService.stopActivityTiming(now.getTime());
            } else if (!now.before(reduceAndGoodsDTO.getStartDate())) {
                reduceActivityService.startActivityTiming(now.getTime());
            }
            return new Result<>().ok(null, "编辑成功");
        } else {
            return new Result<>().error("编辑失败");
        }
    }
}
