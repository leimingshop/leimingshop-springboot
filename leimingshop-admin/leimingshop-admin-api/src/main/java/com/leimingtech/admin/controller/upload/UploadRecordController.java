/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.upload;

import com.leimingtech.admin.excel.uploadrecord.UploadRecordExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.upload.SaveUploadRecordDTO;
import com.leimingtech.dto.upload.UploadRecordDTO;
import com.leimingtech.service.upload.UploadRecordService;
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
 * 文件上传记录管理
 *
 * @author chengqian
 * @since 7.0 2019-05-24
 */
@RestController
@RequestMapping("uploadRecord")
@Api(tags = "文件上传记录管理")
@Slf4j
public class UploadRecordController {
    @Autowired
    private UploadRecordService uploadRecordService;

    /**
     * 分页查询记录
     *
     * @param params 查询条件
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<UploadRecordDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<UploadRecordDTO> page = uploadRecordService.page(params);

        return new Result<PageData<UploadRecordDTO>>().ok(page);
    }

    /**
     * 根据id查询单条信息
     *
     * @param id 主键ID
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据id查询单条信息")
    @LogOperation("根据id查询单条信息")
    public Result<UploadRecordDTO> findById(@PathVariable("id") Long id) {
        UploadRecordDTO data = uploadRecordService.get(id);

        return new Result<UploadRecordDTO>().ok(data);
    }

    /**
     * 新增上传记录
     *
     * @param dto 上传记录实体
     * @return
     */
    @PostMapping
    @ApiOperation("新增上传记录")
    @LogOperation("新增上传记录")
    public Result save(@RequestBody SaveUploadRecordDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        UploadRecordDTO uploadRecordDTO = ConvertUtils.sourceToTarget(dto, UploadRecordDTO.class);
        uploadRecordService.save(uploadRecordDTO);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改上传记录
     *
     * @param dto 上传记录实体
     */
    @PutMapping
    @ApiOperation("修改上传记录")
    @LogOperation("修改上传记录")
    public Result update(@RequestBody UploadRecordDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        uploadRecordService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 删除上传记录
     *
     * @param ids 主键ID
     */
    @DeleteMapping
    @ApiOperation("删除上传记录")
    @LogOperation("删除上传记录")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id不能为空");

        uploadRecordService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 导出上传记录
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<UploadRecordDTO> list = uploadRecordService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, UploadRecordExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}