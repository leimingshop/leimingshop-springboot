/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.aftersale;

import com.leimingtech.admin.excel.aftersale.ArbitrationExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.aftersale.dto.ArbitrationDTO;
import com.leimingtech.modules.aftersale.dto.ArbitrationDetailDTO;
import com.leimingtech.modules.aftersale.dto.ArbitrationPageDTO;
import com.leimingtech.modules.aftersale.dto.AuditArbitrationDTO;
import com.leimingtech.modules.aftersale.service.ArbitrationService;
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
@Slf4j
@Api(tags = "仲裁管理")
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
            @ApiImplicitParam(name = "auditor", value = "审核人", paramType = "query", dataType = "String")
    })
    public Result<PageData<ArbitrationPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
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
    public Result<ArbitrationDetailDTO> arbitrationDetail(@PathVariable("id") Long id) {
        ArbitrationDetailDTO arbitrationDetailDTO = arbitrationService.arbitrationDetail(id);
        return new Result().ok(arbitrationDetailDTO);
    }

    /**
     * 保存平台仲裁信息
     *
     * @param auditArbitrationDTO 保存审核对象
     * @date 2020/4/9/009 10:25
     * @author xuzhch
     */
    @PutMapping("audit")
    @ApiOperation("保存平台仲裁信息")
    public Result audit(@ApiIgnore UserDetail userDetail, @RequestBody AuditArbitrationDTO auditArbitrationDTO) {
        auditArbitrationDTO.setAuditor(userDetail.getUsername());
        arbitrationService.audit(auditArbitrationDTO);
        return new Result().ok(null, "审核成功");
    }


    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<ArbitrationDTO> get(@PathVariable("id") Long id) {
        ArbitrationDTO data = arbitrationService.get(id);

        return new Result<ArbitrationDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody ArbitrationDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        arbitrationService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody ArbitrationDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        arbitrationService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        arbitrationService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出仲裁记录")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<ArbitrationPageDTO> list = arbitrationService.exportList(params);
        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, ArbitrationExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}
