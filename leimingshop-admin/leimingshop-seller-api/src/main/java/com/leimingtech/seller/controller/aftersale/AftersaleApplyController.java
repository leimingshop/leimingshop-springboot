/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.aftersale;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.aftersale.dto.AftersaleApplyDetailDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleApplyPageDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleAuditLogSaveDTO;
import com.leimingtech.modules.aftersale.service.AftersaleApplyService;
import com.leimingtech.seller.statusCode.AftersaleApplystatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;


/**
 * 售后申请
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@RestController
@RequestMapping("aftersale/apply")
@Api(tags = "售后-申请")
public class AftersaleApplyController {

    MonitorLogger monitorLogger = MonitorLoggerFactory.getMonitorLogger(AftersaleApplyController.class);

    @Autowired
    private AftersaleApplyService aftersaleApplyService;

    @GetMapping("page")
    @ApiOperation("售后申请分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aftersaleSn", value = "退货单号", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specSerial", value = "商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "用户昵称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "商户名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "adminApplyStatus", value = "平台审核情况（0:待审核、1:审核通过、2:审核拒绝）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "aftersaleType", value = "售后方式（0:退货,1:换货,2:仅退款）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态（1:商家审核中,2:平台审核中,3:已完成,4:已取消）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "auditResult", value = "审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditStartTime", value = "审核开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditEndTime", value = "审核结束时间", paramType = "query", dataType = "String"),
    })
    public Result<PageData<AftersaleApplyPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result<PageData<AftersaleApplyPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        PageData<AftersaleApplyPageDTO> page = aftersaleApplyService.pageData(params, sellerDetail.getStoreId());
        Map<String, String> logMap = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            logMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        monitorLogger.info(AftersaleApplystatusCode.AFTERSALE_APPLY_PAGE_SUCCESS.getCode(), AftersaleApplystatusCode.AFTERSALE_APPLY_PAGE_SUCCESS.getMessage(), logMap);
        return new Result<PageData<AftersaleApplyPageDTO>>().ok(page);
    }

    @GetMapping("detail/{aftersaleSn}")
    @ApiOperation("退货/换货审核申请详情")
    public Result<AftersaleApplyDetailDTO> detail(@PathVariable("aftersaleSn") Long aftersaleSn, @ApiIgnore SellerDetail sellerDetail) {
        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result<AftersaleApplyDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        AftersaleApplyDetailDTO data = aftersaleApplyService.detail(aftersaleSn);
        return new Result<AftersaleApplyDetailDTO>().ok(data);
    }

    @PostMapping("reason")
    @ApiOperation("保存审核理由")
    @LogOperation("保存审核理由")
    public Map<String, Object> saveReason(@RequestBody AftersaleAuditLogSaveDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        Map<String, Object> resultMap = new HashMap<>(16);
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        if (null == sellerDetail || null == sellerDetail.getId()) {
            resultMap.put("code", ErrorCode.UNAUTHORIZED);
            resultMap.put("msg", "用户未登录");
            return resultMap;
        }
        //开始保存数据
        resultMap = aftersaleApplyService.saveApplyResult(dto);

        return resultMap;
    }
}
