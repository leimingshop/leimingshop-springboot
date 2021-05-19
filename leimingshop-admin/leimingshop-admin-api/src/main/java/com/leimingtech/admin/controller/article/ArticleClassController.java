/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.article;

import com.leimingtech.admin.excel.ArticleClassExcel;
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
import com.leimingtech.dto.article.ArticleClassDTO;
import com.leimingtech.dto.article.ArticleClassSaveDTO;
import com.leimingtech.dto.article.ArticleClassUpdateDTO;
import com.leimingtech.service.article.ArticleClassService;
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
 * 文章分类表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@RestController
@RequestMapping("article/class")
@Api(tags = "文章分类管理")
@Slf4j
public class ArticleClassController {
    @Autowired
    private ArticleClassService articleClassService;

    @GetMapping("page")
    @ApiOperation("文章分类管理分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "acParentId", value = "分类id", paramType = "query", dataType = "Long")
    })
    public Result<PageData<ArticleClassDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<ArticleClassDTO> page = articleClassService.page(params);

        return new Result<PageData<ArticleClassDTO>>().ok(page);
    }

    @GetMapping("child/{id}")
    @ApiOperation("根据ID查询子级分类")
    public Result<List<ArticleClassDTO>> child(@PathVariable("id") Long id) {
        List<ArticleClassDTO> data = articleClassService.selectListByParentId(id);
        return new Result<List<ArticleClassDTO>>().ok(data);
    }


    @GetMapping("name/{id}")
    @ApiOperation("根据ID获取名称(新增下级使用)")
    public Result<String> addChild(@PathVariable("id") Long id) {
        String acName = articleClassService.selectNameById(id);
        return new Result<String>().ok(acName);
    }

    @GetMapping("{id}")
    @ApiOperation("根据ID查询文章分类详情")
    public Result<ArticleClassDTO> get(@PathVariable("id") Long id) {
        ArticleClassDTO data = articleClassService.get(id);
        return new Result<ArticleClassDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存文章分类信息")
    @LogOperation("保存文章分类信息")
    @LogContext(code = SysStatusCode.ARTICLE_CLASS_SAVE_OPERATION_CODE, note = SysStatusCode.ARTICLE_CLASS_SAVE_OPERATION_MESSAGE)
    public Result save(@RequestBody ArticleClassSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        articleClassService.save(dto);
        return new Result().ok(null, "保存文章分类信息成功");
    }

    @PutMapping
    @ApiOperation("修改文章分类信息")
    @LogOperation("修改文章分类信息")
    @LogContext(code = SysStatusCode.ARTICLE_CLASS_UPDATE_OPERATION_CODE, note = SysStatusCode.ARTICLE_CLASS_UPDATE_OPERATION_MESSAGE)
    public Result update(@RequestBody ArticleClassUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        articleClassService.update(dto);
        return new Result().ok(null, "修改文章分类信息成功");
    }

    @DeleteMapping
    @ApiOperation("删除文章分类信息")
    @LogOperation("删除文章分类信息")
    @LogContext(code = SysStatusCode.ARTICLE_CLASS_DELETE_OPERATION_CODE, note = SysStatusCode.ARTICLE_CLASS_DELETE_OPERATION_MESSAGE)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        articleClassService.delete(ids);
        return new Result().ok(null, "删除文章分类成功");
    }

    @GetMapping("export")
    @ApiOperation("导出文章分类信息")
    @LogOperation("导出文章分类信息")
    @LogContext(code = SysStatusCode.ARTICLE_CLASS_EXPORT_OPERATION_CODE, note = SysStatusCode.ARTICLE_CLASS_EXPORT_OPERATION_MESSAGE)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<ArticleClassDTO> list = articleClassService.list(params);
        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, ArticleClassExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}