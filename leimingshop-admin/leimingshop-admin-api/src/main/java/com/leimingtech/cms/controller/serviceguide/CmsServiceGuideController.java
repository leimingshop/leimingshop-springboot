/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.serviceguide;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.article.CmsArticleFwznListDTO;
import com.leimingtech.modules.dto.article.CmsArticleFwznSaveDTO;
import com.leimingtech.modules.dto.article.CmsArticleFwznUpdateDTO;
import com.leimingtech.modules.service.article.CmsArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * CMS服务指南文章管理
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@RestController
@RequestMapping("cms/serviceguide")
@Api(tags = "CMS服务指南文章管理")
@Slf4j
public class CmsServiceGuideController {

    @Autowired
    private CmsArticleService cmsArticleService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "articleTitle", value = "分类名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "acId", value = "分类ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态标识（0：停用 1：启用（默认））", paramType = "query", dataType = "int")
    })
    public Result<PageData<CmsArticleFwznListDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CmsArticleFwznListDTO> page = cmsArticleService.pageFwzn(params);
        return new Result<PageData<CmsArticleFwznListDTO>>().ok(page, "查询服务指南成功");
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<CmsArticleFwznSaveDTO> get(@PathVariable("id") Long id) {
        CmsArticleFwznSaveDTO data = cmsArticleService.getFwznArticleAdmin(id);
        return new Result<CmsArticleFwznSaveDTO>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    public Result save(@RequestBody CmsArticleFwznSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        cmsArticleService.saveFwzn(dto);
        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    public Result update(@RequestBody CmsArticleFwznUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        cmsArticleService.updateFwzn(dto);
        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        cmsArticleService.delete(ids);
        return new Result();
    }

//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<CmsArticleDTO> list = cmsArticleService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, CmsArticleExcel.class);
//    }

}