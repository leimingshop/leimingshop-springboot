/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.grade;

import com.leimingtech.admin.excel.grade.StoreGradeExcel;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.grade.StoreGradeDTO;
import com.leimingtech.modules.dto.grade.StoreGradeSaveDTO;
import com.leimingtech.modules.dto.grade.StoreGradeUpdateDTO;
import com.leimingtech.modules.dto.grade.StoreShowFlagUpdateDTO;
import com.leimingtech.modules.enums.ResultEnum;
import com.leimingtech.modules.enums.custom.ShowFlagEnum;
import com.leimingtech.modules.service.grade.StoreGradeService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.statuscode.StoreStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 店铺等级管理
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-30
 */
@RestController
@RequestMapping("store/grade")
@Api(tags = "店铺等级管理")
@Slf4j
public class StoreGradeController {
    @Resource
    private StoreGradeService storeGradeService;

    @Resource
    private StoreService storeService;

    @GetMapping("page")
    @ApiOperation("店铺等级分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "sgName", value = "等级名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<StoreGradeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<StoreGradeDTO> page = storeGradeService.page(params);

        return new Result<PageData<StoreGradeDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("根据ID查询店铺等级详情")
    public Result<StoreGradeDTO> get(@PathVariable("id") Long id) {
        StoreGradeDTO data = storeGradeService.get(id);

        return new Result<StoreGradeDTO>().ok(data);
    }

    @GetMapping("name/{id}")
    @ApiOperation("根据店铺等级ID查询店铺等级名称")
    public Result<String> getNameById(@PathVariable("id") Long id) {
        String gradeName = storeGradeService.getNameById(id);

        return new Result<String>().ok(gradeName);
    }

    @PostMapping
    @ApiOperation("保存店铺等级信息")
    @LogOperation("保存店铺等级信息")
    @LogContext(code = StoreStatusCode.STORE_GRADE_SAVE_OPERATION_CODE, note = StoreStatusCode.STORE_GRADE_SAVE_OPERATION_MESSAGE)
    public Result save(@RequestBody StoreGradeSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("sgName", dto.getSgName());
        Integer result = storeGradeService.checkGradeName(params);
        if (result != null && result != 0) {
            return new Result().error("等级名称重复");
        } else {
            storeGradeService.save(dto);
        }
        return new Result().ok(null, "店铺等级保存成功");
    }

    @PutMapping
    @ApiOperation("修改店铺等级信息")
    @LogOperation("修改店铺等级信息")
    @LogContext(code = StoreStatusCode.STORE_GRADE_UPDATE_OPERATION_CODE, note = StoreStatusCode.STORE_GRADE_UPDATE_OPERATION_MESSAGE)
    public Result update(@RequestBody StoreGradeUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("id", dto.getId());
        params.put("sgName", dto.getSgName());

        // 校验店铺等级,如果店铺等级下有关联店铺，就不让删除
        if (dto.getShowFlag() == 0) {
            Integer gradeCount = storeService.findGradeCount(dto.getId());
            if (gradeCount > 0) {
                return new Result().error("该等级已关联店铺");
            }
        }
        Integer result = storeGradeService.checkGradeName(params);
        if (result != null && result != ResultEnum.RESULT_COUNT.value()) {
            return new Result().error("等级名称重复");
        } else {
            storeGradeService.update(dto);
        }

        return new Result().ok(null, "店铺等级修改成功");
    }


    @PutMapping("show")
    @ApiOperation("启用/禁用店铺等级")
    @LogOperation("启用/禁用店铺等级")
    @LogContext(code = StoreStatusCode.STORE_GRADE_SHOW_OPERATION_CODE, note = StoreStatusCode.STORE_GRADE_SHOW_OPERATION_MESSAGE)
    public Result updateShowFlag(@RequestBody StoreShowFlagUpdateDTO storeShowFlagUpdateDTO) {
        storeGradeService.updateShowFlag(storeShowFlagUpdateDTO);
        return new Result().ok(null, "修改成功");
    }

    @DeleteMapping
    @ApiOperation("删除店铺等级信息")
    @LogOperation("删除店铺等级信息")
    @LogContext(code = StoreStatusCode.STORE_GRADE_DELETE_OPERATION_CODE, note = StoreStatusCode.STORE_GRADE_DELETE_OPERATION_MESSAGE)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        for (Long gradeId : ids) {
            Integer result = storeService.selectStoreByGrade(gradeId);
            if (result != null && result != ResultEnum.RESULT_COUNT.value()) {
                return new Result().error("选中的店铺等级下有店铺使用");
            }

        }
        storeGradeService.delete(ids);

        return new Result().ok(null, "店铺等级删除成功");
    }

    @GetMapping("export")
    @ApiOperation("导出店铺等级信息")
    @LogOperation("导出店铺等级信息")
    @LogContext(code = StoreStatusCode.STORE_GRADE_EXPORT_OPERATION_CODE, note = StoreStatusCode.STORE_GRADE_EXPORT_OPERATION_MESSAGE)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<StoreGradeDTO> list = storeGradeService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, StoreGradeExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

    /**
     * 查询店铺等级（不包括被禁用的）
     *
     * @return
     */
    @GetMapping("find/grade")
    @ApiOperation("查询店铺等级（不包括被禁用的）")
    @LogOperation("查询店铺等级（不包括被禁用的）")
    public Result<List<StoreGradeDTO>> findGrade() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("showFlag", ShowFlagEnum.YES.value());
        List<StoreGradeDTO> list = storeGradeService.list(params);

        return new Result<List<StoreGradeDTO>>().ok(list);
    }

}
