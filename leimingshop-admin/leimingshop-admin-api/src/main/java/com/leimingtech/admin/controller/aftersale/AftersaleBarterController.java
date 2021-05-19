/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.aftersale;

import com.leimingtech.admin.excel.aftersale.AftersaleBarterExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.aftersale.dto.AftersaleBarterDetailDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO;
import com.leimingtech.modules.aftersale.service.AftersaleBarterService;
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
    public Result<PageData<AftersaleReturnPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<AftersaleReturnPageDTO> page = aftersaleBarterService.pageData(params, null);

        return new Result<PageData<AftersaleReturnPageDTO>>().ok(page);
    }

    @GetMapping("detail/{aftersaleSn}")
    @ApiOperation("订单退货详情")
    public Result<AftersaleBarterDetailDTO> detail(@PathVariable("aftersaleSn") Long aftersaleSn) {
        AftersaleBarterDetailDTO data = aftersaleBarterService.detail(aftersaleSn);

        return new Result<AftersaleBarterDetailDTO>().ok(data);
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
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<AftersaleReturnPageDTO> list = aftersaleBarterService.findListExport(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, AftersaleBarterExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}
