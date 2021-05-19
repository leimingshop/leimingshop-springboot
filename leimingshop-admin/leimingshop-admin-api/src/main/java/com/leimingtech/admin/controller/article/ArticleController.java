/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.article;

import com.leimingtech.admin.excel.ArticleExcel;
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
import com.leimingtech.dto.article.ArticleDTO;
import com.leimingtech.dto.article.ArticleSaveDTO;
import com.leimingtech.dto.article.ArticleUpdateDTO;
import com.leimingtech.service.article.ArticleService;
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
 * 文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@RestController
@RequestMapping("article")
@Api(tags = "文章管理")
@Slf4j
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "acId", value = "文章分类", paramType = "query", dataType = "String")
    })
    public Result<PageData<ArticleDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<ArticleDTO> page = articleService.page(params);
        return new Result<PageData<ArticleDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("根据ID查看文章详情")
    public Result<ArticleDTO> get(@PathVariable("id") Long id) {
        ArticleDTO data = articleService.get(id);
        return new Result<ArticleDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存文章信息")
    @LogOperation("保存文章信息")
    @LogContext(code = SysStatusCode.ARTICLE_SAVE_OPERATION_CODE, note = SysStatusCode.ARTICLE_SAVE_OPERATION_MESSAGE)
    public Result save(@RequestBody ArticleSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        articleService.save(dto);
        return new Result().ok(null, "保存文章成功");
    }

    @PutMapping
    @ApiOperation("修改文章信息")
    @LogOperation("修改文章信息")
    @LogContext(code = SysStatusCode.ARTICLE_UPDATE_OPERATION_CODE, note = SysStatusCode.ARTICLE_UPDATE_OPERATION_MESSAGE)

    public Result update(@RequestBody ArticleUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        articleService.update(dto);
        return new Result().ok(null, "修改文章成功");
    }

    @DeleteMapping
    @ApiOperation("删除文章信息")
    @LogOperation("删除文章信息")
    @LogContext(code = SysStatusCode.ARTICLE_DELETE_OPERATION_CODE, note = SysStatusCode.ARTICLE_DELETE_OPERATION_MESSAGE)

    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        articleService.delete(ids);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出文章信息")
    @LogOperation("导出文章信息")
    @LogContext(code = SysStatusCode.ARTICLE_EXPORT_OPERATION_CODE, note = SysStatusCode.ARTICLE_EXPORT_OPERATION_MESSAGE)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<ArticleDTO> list = articleService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, ArticleExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

}