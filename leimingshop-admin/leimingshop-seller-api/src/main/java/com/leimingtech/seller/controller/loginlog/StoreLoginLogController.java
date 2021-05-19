/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.loginlog;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dto.loginlog.StoreLoginLogDTO;
import com.leimingtech.modules.dto.loginlog.StoreLoginLogExcelDTO;
import com.leimingtech.modules.service.loginlog.StoreLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @Author: weixianchun
 * @Description: 店铺登录日志
 * @Date :2019/6/28 10:55
 * @Version V1.0
 **/
@RestController
@RequestMapping("storeloginlog")
@Api(tags = "店铺登录日志")
@Slf4j
public class StoreLoginLogController {

    @Autowired
    private StoreLoginLogService storeLoginLogService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "creator", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态  0：失败    1：成功    2：账号已锁定", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "operation", value = "用户操作   0：用户登录   1：用户退出", paramType = "query", dataType = "int")
    })
    public Result<PageData<StoreLoginLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null) {
            return new Result<PageData<StoreLoginLogDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());

        PageData<StoreLoginLogDTO> page = storeLoginLogService.page(params);

        return new Result<PageData<StoreLoginLogDTO>>().ok(page, "查询成功");
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "creator", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态  0：失败    1：成功    2：账号已锁定", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "operation", value = "用户操作   0：用户登录   1：用户退出", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<StoreLoginLogDTO> list = storeLoginLogService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, StoreLoginLogExcelDTO.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}
