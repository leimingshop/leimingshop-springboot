/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.aftersale;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.aftersale.dto.AftersaleAuditDetailDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleAuditLogPageDTO;
import com.leimingtech.modules.aftersale.service.AftersaleAuditLogService;
import com.leimingtech.seller.excel.aftersale.AftersaleAuditExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @Author: SWH ab4856812@163.com
 * @Description: 售后-审核
 * @Date: 2019/8/16 13:56
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("aftersale/audit")
@Api(tags = "售后-审核")
public class AftersaleAuditController {

    @Autowired
    private AftersaleAuditLogService aftersaleAuditLogService;

    @GetMapping("page")
    @ApiOperation("售后审核分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "申请单号", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "aftersaleSn", value = "退货单号", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specSerial", value = "商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "商户名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aftersaleType", value = "售后方式（0:退货,1:换货,2:仅退款）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "process", value = "审核流程（1:商家审核,2:平台审核）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "result", value = "审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "申请开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "申请结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditStartTime", value = "审核开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditEndTime", value = "审核结束时间", paramType = "query", dataType = "String")
    })
    public Result<PageData<AftersaleAuditLogPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result<PageData<AftersaleAuditLogPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        PageData<AftersaleAuditLogPageDTO> page = aftersaleAuditLogService.pageData(params, sellerDetail.getStoreId());

        return new Result<PageData<AftersaleAuditLogPageDTO>>().ok(page);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("售后审核详情")
    public Result<AftersaleAuditDetailDTO> detail(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result<AftersaleAuditDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        AftersaleAuditDetailDTO data = aftersaleAuditLogService.detail(id);

        return new Result<AftersaleAuditDetailDTO>().ok(data);
    }

    /**
     * 导出记录
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("export")
    @ApiOperation("导出记录")
    @LogOperation("导出记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aftersaleSn", value = "申请单号", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specSerial", value = "商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "会员名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "商户名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aftersaleType", value = "售后方式（0:退货,1:换货,2:仅退款）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "process", value = "审核流程（1:商家审核,2:平台审核）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "result", value = "审核结果（1:审核通过,2:审核不通过,3:审核中,4:用户取消）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "申请开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "申请结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditStartTime", value = "审核开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditEndTime", value = "审核结束时间", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<AftersaleAuditLogPageDTO> list = aftersaleAuditLogService.findListExport(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, AftersaleAuditExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}
