/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.article;

import com.leimingtech.admin.excel.DocumentExcel;
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
import com.leimingtech.dto.article.DocumentDTO;
import com.leimingtech.dto.article.DocumentSaveDTO;
import com.leimingtech.dto.article.DocumentUpdateDTO;
import com.leimingtech.service.article.DocumentService;
import com.leimingtech.statuscode.SysStatusCode;
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
 * 系统文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@RestController
@RequestMapping("document")
@Api(tags = "系统文章管理")
@Slf4j
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "docTitle", value = "文章标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "docCode", value = "文章标识码", paramType = "query", dataType = "String")
    })
    public Result<PageData<DocumentDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<DocumentDTO> page = documentService.page(params);

        return new Result<PageData<DocumentDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("根据ID查询系统文章详情")
    public Result<DocumentDTO> get(@PathVariable("id") Long id) {
        DocumentDTO data = documentService.get(id);
        return new Result<DocumentDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存系统文章信息")
    @LogOperation("保存系统文章信息")
    @LogContext(code = SysStatusCode.DOCUMENT_SAVE_OPERATION_CODE, note = SysStatusCode.DOCUMENT_SAVE_OPERATION_MESSAGE)
    public Result save(@RequestBody DocumentSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        documentService.save(dto);

        return new Result().ok(null, "系统文章保存成功");
    }

    @PutMapping
    @ApiOperation("修改系统文章信息")
    @LogOperation("修改系统文章信息")
    @LogContext(code = SysStatusCode.DOCUMENT_UPDATE_OPERATION_CODE, note = SysStatusCode.DOCUMENT_UPDATE_OPERATION_MESSAGE)
    public Result update(@RequestBody DocumentUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        documentService.update(dto);
        return new Result().ok(null, "系统文章修改成功");
    }

    @DeleteMapping
    @ApiOperation("删除系统文章信息")
    @LogOperation("删除系统文章信息")
    @LogContext(code = SysStatusCode.DOCUMENT_DELETE_OPERATION_CODE, note = SysStatusCode.DOCUMENT_DELETE_OPERATION_MESSAGE)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        documentService.delete(ids);
        return new Result().ok(null, "系统文章删除成功");
    }

    @GetMapping("export")
    @ApiOperation("导出系统文章信息")
    @LogOperation("导出系统文章信息")
    @LogContext(code = SysStatusCode.DOCUMENT_EXPORT_OPERATION_CODE, note = SysStatusCode.DOCUMENT_EXPORT_OPERATION_MESSAGE)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<DocumentDTO> list = documentService.list(params);
        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, DocumentExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}