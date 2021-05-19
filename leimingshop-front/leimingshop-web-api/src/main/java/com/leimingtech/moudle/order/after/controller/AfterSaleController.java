/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.after.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.aftersale.service.AftersaleApplyService;
import com.leimingtech.modules.aftersale.service.AftersaleLogService;
import com.leimingtech.modules.aftersale.statuscode.AftersaleStatusCode;
import com.leimingtech.modules.dto.cart.SettlementDTO;
import com.leimingtech.modules.dto.order.AvailableAfterSaleOrderDTO;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.moudle.order.after.vo.*;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 售后管理
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
@Slf4j
@RestController
@RequestMapping("/order/aftersale")
@Api(tags = "售后管理")
public class AfterSaleController {
    private static final String MAP_PARAMS_CODE_STR = "code";
    @Autowired
    private OrderService orderService;

    @Autowired
    private AftersaleApplyService aftersaleApplyService;

    @Autowired
    private AftersaleLogService aftersaleLogService;

    @GetMapping("/available/aftersale")
    @ApiOperation("订单可申请售后服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orderSn", value = "订单编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buyTime", value = "下单时间查询（0:全部，1:一个月内，2:一个月至六个月，3:六个月至一年,4:一年以上，5:自定义时间）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "createTime", value = "下单开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "下单结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<AvailableAfterSaleOrderVo>> availAfterSaleOrder(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取buyerId
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<PageData<AvailableAfterSaleOrderVo>>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }

        Long buyerId = SecurityCurrentUser.getUserDetail().getId();
        params.put("buyerId", String.valueOf(buyerId));
        String createTime = (String) params.get("createTime");
        if (StringUtils.isNotBlank(createTime)) {
            createTime = createTime + " 00:00:00";
            params.put("createTime", createTime);
        }
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
            params.put("endTime", endTime);
        }
        PageData<AvailableAfterSaleOrderDTO> page = orderService.findAvailAfterList(params);
        PageData<AvailableAfterSaleOrderVo> voPageData = new PageData<>();
        if (CollectionUtils.isNotEmpty(page.getList())) {
            List<AvailableAfterSaleOrderVo> pageList = ConvertUtils.sourceToTarget(page.getList(), AvailableAfterSaleOrderVo.class);
            voPageData.setList(pageList);
            voPageData.setTotal(page.getTotal());
        }
        return new Result<PageData<AvailableAfterSaleOrderVo>>().ok(voPageData, "查询成功");
    }


    /**
     * 售后进度
     *
     * @param aftersaleSn:售后单号
     * @author xuzhch
     * @date 2019/6/20 14:25
     * @version V1.0
     */
    @GetMapping("/apply/process/{aftersaleSn}")
    @ApiOperation("售后进度")
    public Result<AfterSaleProcessVo> frontAftersaleProcess(@PathVariable("aftersaleSn") Long aftersaleSn) {
        try {
            SecurityCurrentUser.getUserDetail();
        } catch (CustomException e) {
            return new Result<AfterSaleProcessVo>().error("请先登录");
        }
        AfterSaleProcessDTO afterSaleProcessDTO = aftersaleLogService.findProcess(aftersaleSn, "DESC");
        AfterSaleProcessVo processVo = ConvertUtils.sourceToTarget(afterSaleProcessDTO, AfterSaleProcessVo.class);
        return new Result<AfterSaleProcessVo>().ok(processVo, "查询成功");
    }

    /**
     * 售后买家确认收货接口
     *
     * @param aftersaleSn:售后单号
     * @author xuzhch
     * @date 2020年6月16日19:57:14
     */
    @PostMapping("/apply/confirm/receipt/{aftersaleSn}")
    @ApiOperation("售后买家确认收货接口")
    public Map<String, Object> confirmReceipt(@PathVariable("aftersaleSn") Long aftersaleSn) {
        Map<String, Object> resultMap = new HashMap<>(2);
        // 获取memberId
        Long memberId = null;
        try {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "请先登录");
            return resultMap;
        }
        resultMap = aftersaleApplyService.confirmReceipt(aftersaleSn, memberId);
        return resultMap;
    }


    /**
     * 买家上传物流信息
     *
     * @param deliveryVo:保存实体
     * @author xuzhch
     * @date 2019/6/20 14:25
     * @version V1.0
     */
    @PostMapping("seller/delivery/save")
    @ApiOperation("H5端售后买家上传物流信息")
    @LogContext(code = AftersaleStatusCode.H5_AFTERSALE_DELIVERY_SAVE_SUCCESS_CODE, note = AftersaleStatusCode.H5_AFTERSALE_DELIVERY_SAVE_SUCCESS_MESSAGE)
    public Result deliverySave(@RequestBody AftersaleDeliveryVo deliveryVo) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<SettlementDTO>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        // 获取memberId
        Long memberId = SecurityCurrentUser.getUserDetail().getId();

        AftersaleDeliveryDTO aftersaleDeliveryDTO = ConvertUtils.sourceToTarget(deliveryVo, AftersaleDeliveryDTO.class);
        Map<String, Object> resultMap = aftersaleApplyService.saveDelivery(aftersaleDeliveryDTO, memberId);
        if (!resultMap.get(MAP_PARAMS_CODE_STR).toString().equals(String.valueOf(ErrorCode.SUCCESS))) {
            return new Result().error(ErrorCode.NOT_NULL, resultMap.get("msg").toString());
        }
        return new Result().ok(null, "申请成功");
    }

    /**
     * 取消申请接口
     *
     * @param aftersaleSn:售后单号
     * @author xuzhch
     * @date 2019/6/20 14:25
     * @version V1.0
     */
    @PostMapping("/apply/cancel/{aftersaleSn}")
    @ApiOperation("取消售后申请")
    @LogContext(code = AftersaleStatusCode.H5_AFTERSALE_CANCEL_APPLY_SUCCESS_CODE, note = AftersaleStatusCode.H5_AFTERSALE_CANCEL_APPLY_SUCCESS_MESSAGE)
    public Result cancelApply(@PathVariable("aftersaleSn") Long aftersaleSn) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        // 获取memberId
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        Map<String, Object> resultMap = aftersaleApplyService.cancelApply(aftersaleSn, memberId);
        if (!resultMap.get(MAP_PARAMS_CODE_STR).toString().equals(String.valueOf(ErrorCode.SUCCESS))) {
            return new Result().error(ErrorCode.NOT_NULL, resultMap.get("msg").toString());
        }
        return new Result().ok(null, "取消成功");
    }

    /**
     * 售后详情
     *
     * @param params:查询条件
     * @author xuzhch
     * @date 2019/6/17 15:01
     * @version V1.0
     */
    @GetMapping("/apply/detail")
    @ApiOperation("售后详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aftersaleId", value = "售后编号", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "aftersaleType", value = "售后方式（0:退货,1:换货,2:仅退款）", paramType = "query", dataType = "int")
    })
    public Result<AfterSaleDetailVo> frontAftersaleDetail(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        // 获取memberId
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        AfterSaleDetailDTO afterSaleDetailDTO = aftersaleApplyService.findAftersaleDetail(params, memberId);
        AfterSaleDetailVo detailDTO = ConvertUtils.sourceToTarget(afterSaleDetailDTO, AfterSaleDetailVo.class);
        return new Result<AfterSaleDetailVo>().ok(detailDTO, "查询成功");
    }

    @PostMapping("apply/save")
    @ApiOperation("保存售后保存")
    @LogContext(code = AftersaleStatusCode.H5_AFTERSALE_APPLY_SAVE_SUCCESS_CODE, note = AftersaleStatusCode.H5_AFTERSALE_APPLY_SAVE_SUCCESS_MESSAGE)
    public Result applySave(@RequestBody AftersaleApplySaveVo applySaveVo) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        ValidatorUtils.validateEntity(applySaveVo, AddGroup.class, DefaultGroup.class);
        // 获取memberId
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        applySaveVo.setMemberId(memberId);
        applySaveVo.setMemberName(SecurityCurrentUser.getUserDetail().getMemberName());

        AftersaleApplyFrontSaveDTO aftersaleApplySaveDTO = ConvertUtils.sourceToTarget(applySaveVo, AftersaleApplyFrontSaveDTO.class);
        // 未来有可能有批量退货所以接口写成list
        List<AftersaleApplyFrontSaveDTO> saveDTOList = new ArrayList<>();
        saveDTOList.add(aftersaleApplySaveDTO);

        Map<String, Object> resultMap = aftersaleApplyService.saveAfterSaleApply(saveDTOList);
        if (ErrorCode.SUCCESS != (int) (resultMap.get(MAP_PARAMS_CODE_STR))) {

            return new Result().error((int) resultMap.get("code"), String.valueOf(resultMap.get("msg")));
        }
        return new Result().ok(resultMap.get("msg"), String.valueOf(resultMap.get("msg")));
    }

    /**
     * 售后服务申请记录列表接口
     *
     * @param params:查询参数
     * @author xuzhch
     * @date 2019/6/17 16:56
     * @version V1.0
     */
    @GetMapping("/apply/record")
    @ApiOperation("售后服务申请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "unionQuery", value = "查询条件（商品名称、订单编号、商品sku均是这个字段）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "applyTime", value = "申请时间查询（0:全部，1:一个月内，2:一个月至六个月，3:六个月至一年,4:一年以上，5:自定义时间）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "createTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<AftersaleApplyRecordVo>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        // 获取memberId
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        String createTime = (String) params.get("createTime");
        if (StringUtils.isNotBlank(createTime)) {
            createTime = createTime + " 00:00:00";
            params.put("createTime", createTime);
        }
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
            params.put("endTime", endTime);
        }
        PageData<AftersaleApplyRecordDTO> page = aftersaleApplyService.applyRecordPage(params, memberId);
        PageData<AftersaleApplyRecordVo> voPageData = new PageData<>();
        if (CollectionUtils.isNotEmpty(page.getList())) {
            List<AftersaleApplyRecordVo> aftersaleApplyRecordVos = ConvertUtils.sourceToTarget(page.getList(), AftersaleApplyRecordVo.class);
            voPageData.setTotal(page.getTotal());
            voPageData.setList(aftersaleApplyRecordVos);
        }
        return new Result<PageData<AftersaleApplyRecordVo>>().ok(voPageData, "查询成功");
    }

}

