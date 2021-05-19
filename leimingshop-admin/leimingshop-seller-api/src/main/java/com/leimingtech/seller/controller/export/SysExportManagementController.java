/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.export;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.service.sys.SysExportManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 导入导出管理
 *
 * @author 刘远杰
 * @since v1.0.0 2019-11-14
 */
@RestController
@RequestMapping("sysexportmanagement")
@Api(tags = "导入导出管理")
public class SysExportManagementController {

    @Autowired
    private SysExportManagementService sysExportManagementService;

    /**
     * Description: 分页查询SysExportManagement
     *
     * @param params
     * @return Result<PageData<SysExportManagementVO>>
     * @author 刘远杰
     * @date 2019-11-14
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "assignmentName", value = "任务名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "operationStatus", value = "状态", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<SysExportManagementVO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (null == sellerDetail || null == sellerDetail.getId()) {
            return new Result<PageData<SysExportManagementVO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        PageData<SysExportManagementVO> page = sysExportManagementService.page(params);

        return new Result<PageData<SysExportManagementVO>>().ok(page);
    }

    /**
     * Description: 删除SysExportManagement
     *
     * @param ids
     * @return Result
     * @author 刘远杰
     * @date 2019-11-14
     */
    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        sysExportManagementService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

}
