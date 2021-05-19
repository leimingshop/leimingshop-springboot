/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.aftersale;

import cn.hutool.core.bean.BeanUtil;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.aftersale.dto.ArbitrationDTO;
import com.leimingtech.modules.aftersale.dto.ArbitrationDetailDTO;
import com.leimingtech.modules.aftersale.dto.ArbitrationPageDTO;
import com.leimingtech.modules.aftersale.service.ArbitrationService;
import com.leimingtech.seller.excel.aftersale.ArbitrationExcel;
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
 * 仲裁表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */
@RestController
@RequestMapping("arbitration")
@Api(tags = "仲裁管理")
@Slf4j
public class ArbitrationController {

    @Autowired
    private ArbitrationService arbitrationService;

    @GetMapping("page")
    @ApiOperation("仲裁列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "会员账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "afterSn", value = "原申请单号", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "arbitrationType", value = "仲裁类型（0：售后-退货、1：售后-换货）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "arbitrationStatus", value = "仲裁状态（默认：1:审核通过、2:审核不通过、3:待审核）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specSerial", value = "商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "applyStartDate", value = "申请开始时间", paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "applyEndDate", value = "申请结束时间", paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "auditStartDate", value = "仲裁审核开始时间", paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "auditEndDate", value = "仲裁审核结束时间", paramType = "query", dataType = "date"),
    })
    public Result<PageData<ArbitrationPageDTO>> page(@ApiIgnore SellerDetail sellerDetail, @ApiIgnore @RequestParam Map<String, Object> params) {
        if (BeanUtil.isEmpty(sellerDetail)) {
            return new Result<PageData<ArbitrationPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());
        PageData<ArbitrationPageDTO> page = arbitrationService.page(params);

        return new Result<PageData<ArbitrationPageDTO>>().ok(page);
    }

    /**
     * 查看仲裁详情
     *
     * @param id 仲裁信息ID
     * @return ArbitrationDetailDTO 返回详情
     * @date 2020/4/9/009 10:26
     * @author xuzhch
     */
    @GetMapping("detail/{id}")
    @ApiOperation("查看仲裁详情")
    public Result<ArbitrationDetailDTO> arbitrationDetail(@ApiIgnore SellerDetail sellerDetail, @PathVariable("id") Long id) {
        if (BeanUtil.isEmpty(sellerDetail)) {
            return new Result<ArbitrationDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        ArbitrationDetailDTO arbitrationDetailDTO = arbitrationService.arbitrationDetail(id);
        return new Result().ok(arbitrationDetailDTO);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<ArbitrationDTO> get(@PathVariable("id") Long id) {
        ArbitrationDTO data = arbitrationService.get(id);

        return new Result<ArbitrationDTO>().ok(data);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出仲裁记录")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<ArbitrationPageDTO> list = arbitrationService.exportList(params);
        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, ArbitrationExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}
