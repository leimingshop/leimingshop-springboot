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
import com.leimingtech.modules.aftersale.dto.AftersaleBarterDetailDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO;
import com.leimingtech.modules.aftersale.service.AftersaleBarterService;
import com.leimingtech.seller.excel.aftersale.AftersaleBarterExcel;
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
 * 订单换货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Slf4j
@RestController
@RequestMapping("aftersale/barter")
@Api(tags = "售后-订单换货")
public class AftersaleBarterController {
    @Autowired
    private AftersaleBarterService aftersaleBarterService;

    @GetMapping("page")
    @ApiOperation("订单换货分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aftersaleSn", value = "换货单号", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specSerial", value = "商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "用户昵称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "商户名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aftersaleStatus", value = "售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:退款中,5:退款成功,6:换货失败,7:待换货入库,8:换货出库中,9:换货成功）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
    })
    public Result<PageData<AftersaleReturnPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result<PageData<AftersaleReturnPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        PageData<AftersaleReturnPageDTO> page = aftersaleBarterService.pageData(params, sellerDetail.getStoreId());

        return new Result<PageData<AftersaleReturnPageDTO>>().ok(page);
    }

    @GetMapping("detail/{aftersaleSn}")
    @ApiOperation("订单退货详情")
    public Result<AftersaleBarterDetailDTO> detail(@PathVariable("aftersaleSn") Long aftersaleSn, @ApiIgnore SellerDetail sellerDetail) {
        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result<AftersaleBarterDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        AftersaleBarterDetailDTO data = aftersaleBarterService.detail(aftersaleSn);

        return new Result<AftersaleBarterDetailDTO>().ok(data);
    }

    /**
     * @param aftersaleStatus:确认收货的时候aftersaleStatus传4
     * @return com.leimingtech.commons.tools.utils.Result
     * @Description
     * @Param * @param aftersaleSn:
     * @Author huangkeyuan
     * @Date 16:27 2019-06-20
     */
    @PostMapping("confirm")
    @ApiOperation("确认收到换货商品")
    @LogOperation("确认收到换货商品")
    public Result confirm(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("aftersaleStatus") Integer aftersaleStatus, @ApiIgnore SellerDetail sellerDetail) {
        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        aftersaleBarterService.confirmGoods(aftersaleSn, aftersaleStatus);
        return new Result().ok("", "收货成功");
    }

    @PostMapping("express")
    @ApiOperation("商家提交换货物流信息")
    @LogOperation("商家提交换货物流信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aftersaleSn", value = "换货单号", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sellerDeliveryType", value = "商家配送方式（0:快递,1:自提,2:无需物流）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sellerLogisticsCompany", value = "商家发货物流公司", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sellerLogisticsNumber", value = "商家发货物流单号", paramType = "query", dataType = "String"),
    })
    public Result uploadExpress(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        aftersaleBarterService.uploadExpress(params);
        return new Result().ok("", "发货成功");
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
            @ApiImplicitParam(name = "aftersaleSn", value = "换货单号", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "specSerial", value = "商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberName", value = "用户昵称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "商户名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "aftersaleStatus", value = "售后单状态（1:用户取消,2:退款失败,3:待退货入库,4:退款中,5:退款成功,6:换货失败,7:待换货入库,8:换货出库中,9:换货成功）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<AftersaleReturnPageDTO> list = aftersaleBarterService.findListExport(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, AftersaleBarterExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }
}
