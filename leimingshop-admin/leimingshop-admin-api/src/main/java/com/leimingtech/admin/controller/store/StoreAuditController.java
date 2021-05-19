/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.store;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.auditlog.AuditLogDTO;
import com.leimingtech.modules.dto.store.*;
import com.leimingtech.modules.service.auditlog.AuditLogService;
import com.leimingtech.modules.service.store.StoreAuditService;
import com.leimingtech.modules.service.store.StoreAuthAuditService;
import com.leimingtech.modules.service.store.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 店铺审核
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@RestController
@RequestMapping("audit")
@Api(tags = "店铺审核")
@Slf4j
public class StoreAuditController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreAuditService storeAuditService;

    @Autowired
    private StoreAuthAuditService storeAuthAuditService;

    @Autowired
    private AuditLogService auditLogService;

    /**
     * 店铺入住审核分页
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("店铺入住审核分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "店铺账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeType", value = "店铺类型（1:自营商户，2:普通商户）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "audit", value = "审核状态（10 待审核 20 审核通过 30 审核拒绝 ）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<PageStoreDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<PageStoreDTO> page = storeService.findAuditPage(params);

        return new Result<PageData<PageStoreDTO>>().ok(page);
    }

    /**
     * 店铺入住详情
     *
     * @return
     */
    @GetMapping("info")
    @ApiOperation("店铺入住详情")
    @LogOperation("店铺入住详情")
    public Result<RegisterStoreInfoDTO> info(@RequestParam Long storeId) {

        RegisterStoreInfoDTO settingStoreDTO = storeService.adminInfo(storeId);

        return new Result<RegisterStoreInfoDTO>().ok(settingStoreDTO);
    }


    /**
     * 审核
     */
    @PutMapping("update/status")
    @ApiOperation("审核")
    @LogOperation("审核")
    public Result updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO) {

        String userName = SecurityUser.getUserName();
        updateStoreStatusDTO.setOperator(userName);
        storeService.updateStoreStatus(updateStoreStatusDTO);
        return new Result().ok(null, "审核成功");
    }

    /**
     * 店铺普通信息审核列表
     *
     * @param params
     * @return
     */
    @GetMapping("basic/page")
    @ApiOperation("店铺普通信息审核列表")
    @LogOperation("店铺普通信息审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "店铺账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeType", value = "店铺类型（1:自营商户，2:普通商户）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "audit", value = "审核状态（10 待审核 20 审核通过 30 审核拒绝 ）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<StoreAuditPageDTO>> basicPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<StoreAuditPageDTO> page = storeAuditService.page(params);
        return new Result<PageData<StoreAuditPageDTO>>().ok(page);
    }

    /**
     * 店铺普通信息审核详情
     *
     * @param storeId
     * @param createDate
     * @return
     */
    @GetMapping("basic/info")
    @ApiOperation("店铺普通信息审核详情")
    @LogOperation("店铺普通信息审核详情")
    public Result<StoreAuditDTO> basicInfo(@RequestParam(value = "storeId", required = true) Long storeId,
                                           @RequestParam(value = "createDate", required = true) String createDate) {
        StoreAuditDTO infoByStoreId = storeAuditService.getInfoByStoreId(storeId, createDate);
        return new Result<StoreAuditDTO>().ok(infoByStoreId);

    }


    /**
     * 店铺公司信息审核列表
     *
     * @param params
     * @return
     */
    @GetMapping("company/page")
    @ApiOperation("店铺公司信息审核列表")
    @LogOperation("店铺公司信息审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "店铺账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "audit", value = "审核状态（10 待审核 20 审核通过 30 审核拒绝 ）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<StoreAuthAuditPageDTO>> companyPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<StoreAuthAuditPageDTO> page = storeAuthAuditService.page(params);
        return new Result<PageData<StoreAuthAuditPageDTO>>().ok(page);
    }

    /**
     * 店铺公司信息审核详情
     *
     * @param storeId
     * @param createDate
     * @return
     */
    @GetMapping("company/info")
    @ApiOperation("店铺公司信息审核详情")
    @LogOperation("店铺公司信息审核详情")
    public Result<StoreAuthAuditDTO> companyInfo(@RequestParam(value = "storeId", required = true) Long storeId,
                                                 @RequestParam(value = "createDate", required = true) String createDate) {
        StoreAuthAuditDTO storeAuthAuditDTO = storeAuthAuditService.getInfoByStoreId(storeId, createDate);
        return new Result<StoreAuthAuditDTO>().ok(storeAuthAuditDTO);

    }

    /**
     * 操作流水
     */
    @GetMapping("tally")
    @ApiOperation("操作流水")
    @LogOperation("操作流水")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contentId", value = "操作内容ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "auditType", value = "审核类型（1 店铺普通信息 2 店铺公司信息 3 店铺入住审核）", paramType = "query", dataType = "String")
    })
    public Result<List<AuditLogDTO>> tally(@ApiIgnore @RequestParam Map<String, Object> params) {

        List<AuditLogDTO> list = auditLogService.list(params);
        return new Result<List<AuditLogDTO>>().ok(list);
    }


}