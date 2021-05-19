/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.service.sys.SysExportManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
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
     * @return Result<PageData < SysExportManagementVO>>
     * @author 刘远杰
     * @date 2019-11-14
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "assignmentName", value = "任务名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "operationStatus", value = "状态", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<SysExportManagementVO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 指定展示运营端
        params.put("storeId", "0");
        PageData<SysExportManagementVO> page = sysExportManagementService.page(params);

        return new Result<PageData<SysExportManagementVO>>().ok(page);
    }

    /**
     * Description: 根据id查询SysExportManagement
     *
     * @param id
     * @return Result<PageData < SysExportManagementVO>>
     * @author 刘远杰
     * @date 2019-11-14
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<SysExportManagementVO> get(@PathVariable("id") Long id) {
        SysExportManagementVO data = sysExportManagementService.get(id);

        return new Result<SysExportManagementVO>().ok(data);
    }


    /**
     * Description: 添加SysExportManagement
     *
     * @param dto
     * @return Result
     * @author 刘远杰
     * @date 2019-11-14
     */
    @PostMapping("add")
    @ApiOperation("保存")
    public Result save(@RequestBody SysExportManagementDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysExportManagementService.save(dto);

        return new Result();
    }


    /**
     * Description: 批量保存SysExportManagement
     *
     * @param dto
     * @return Result
     * @author 刘远杰
     * @date 2019-11-14
     */
    @PostMapping("addBach")
    @ApiOperation("批量保存")
    public Result saveBach(@RequestBody List<SysExportManagementDTO> dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysExportManagementService.saveBach(dto);

        return new Result();
    }

    /**
     * Description: 修改SysExportManagement
     *
     * @param dto
     * @return Result
     * @author 刘远杰
     * @date 2019-11-14
     */
    @PutMapping("update")
    @ApiOperation("修改")
    public Result update(@RequestBody SysExportManagementDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysExportManagementService.update(dto);

        return new Result();
    }

    /**
     * Description: 批量修改SysExportManagement
     *
     * @param dtos
     * @return Result
     * @author 刘远杰
     * @date 2019-11-14
     */
    @PutMapping("updateBach")
    @ApiOperation("批量修改")
    public Result updateBach(@RequestBody List<SysExportManagementDTO> dtos) {
        //效验数据
        ValidatorUtils.validateEntity(dtos, UpdateGroup.class, DefaultGroup.class);

        sysExportManagementService.updateBach(dtos);

        return new Result();
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
