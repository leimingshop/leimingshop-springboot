/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.information;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleAddRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleRecommendSaveDTO;
import com.leimingtech.modules.service.articlerecommend.CmsArticleRecommendService;
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
 * CMS资讯相关推荐管理
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@RestController
@RequestMapping("cms/informationrecommend")
@Api(tags = "CMS资讯相关推荐管理")
@Slf4j
public class CmsInformationRecommendController {

    @Autowired
    private CmsArticleRecommendService cmsArticleRecommendService;

    /**
     * 资讯相关推荐
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("资讯相关推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "articleTitle", value = "标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "articleBelong", value = "文章所属", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "author", value = "文章作者", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "acId", value = "分类ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态标识（0：停用 1：启用（默认））", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "commentFlag", value = "评论标识（0：停用 1：启用（默认））", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "audit", value = "审核标识（0：未审核（默认），1：审核通过，2：审核驳回）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "id", value = "文章ID", paramType = "query", dataType = "Long")
    })
    public Result<PageData<CmsArticleRecommendDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CmsArticleRecommendDTO> page = cmsArticleRecommendService.page(params);
        return new Result<PageData<CmsArticleRecommendDTO>>().ok(page);
    }

    /**
     * 资讯添加推荐列表
     *
     * @param params
     * @return
     */
    @GetMapping("addRecommendList")
    @ApiOperation("资讯添加推荐列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "articleTitle", value = "标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "articleBelong", value = "文章所属", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "author", value = "文章作者", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "acId", value = "分类ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "commentFlag", value = "评论标识（0：停用 1：启用（默认））", paramType = "query", dataType = "int"),
    })
    public Result<PageData<CmsArticleAddRecommendDTO>> addRecommendList(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CmsArticleAddRecommendDTO> page = cmsArticleRecommendService.addRecommendList(params);
        return new Result<PageData<CmsArticleAddRecommendDTO>>().ok(page);
    }

    @PostMapping("save")
    @ApiOperation("新增相关推荐")
    public Result save(@RequestBody CmsArticleRecommendSaveDTO dto) {
        //效验数据
        AssertUtils.isArrayEmpty(dto.getRecommendArticleIds(), "id");
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        cmsArticleRecommendService.save(dto);
        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        cmsArticleRecommendService.delete(ids);
        return new Result();
    }

//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<CmsArticleRecommendDTO> list = cmsArticleRecommendService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, CmsArticleRecommendExcel.class);
//    }

}
