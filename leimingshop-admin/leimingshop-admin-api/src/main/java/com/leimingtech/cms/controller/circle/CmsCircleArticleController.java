/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.circle;


import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.article.*;
import com.leimingtech.modules.service.article.CmsArticleService;
import com.leimingtech.modules.service.articleclass.CmsClassService;
import com.leimingtech.modules.service.circle.CmsCircleService;
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
 * 圈子分类管理
 *
 * @author pixiaoyong pixiaoyong@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@RestController
@RequestMapping("cms/circlearticle")
@Api(tags = "CMS圈子帖子管理")
@Slf4j
public class CmsCircleArticleController {
    @Autowired
    private CmsCircleService cmsCircleService;

    @Autowired
    private CmsClassService cmsClassService;

    @Autowired
    private CmsArticleService cmsArticleService;

    @GetMapping("page")
    @ApiOperation("帖子列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "articleTitle", value = "帖子标题/内容", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "creator", value = "创建人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "articleFlag", value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "acId", value = "圈子分类ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "topicName", value = "圈子话题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态标识（0：停用 1：启用（默认））", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "essenceFlag", value = "精华帖标识（0：非精华帖（默认），1：精华帖）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "audit", value = "审核标识（0：未审核（默认），1：审核通过，2：审核驳回）", paramType = "query", dataType = "int")
    })
    public Result<PageData<CmsArticleQzListDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CmsArticleQzListDTO> page = cmsArticleService.pageQz(params);
        return new Result<PageData<CmsArticleQzListDTO>>().ok(page);
    }


    @PostMapping("save")
    @ApiOperation("保存")
    public Result save(@RequestBody CmsArticleQzSaveDTO dto) {
        dto.setAcCode(1);
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        cmsArticleService.saveQz(dto);
        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    public Result update(@RequestBody CmsArticleQzUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        cmsArticleService.updateQz(dto);
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

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<CmsArticleQzAdminInfoDTO> get(@PathVariable("id") Long id) {
        CmsArticleQzAdminInfoDTO data = cmsArticleService.getQzArticleAdmin(id);
        return new Result<CmsArticleQzAdminInfoDTO>().ok(data);
    }

    @PutMapping("statusUpdate")
    @ApiOperation("文章状态修改")
    public Result statusUpdate(@RequestBody CmsArticleStatusUpdateDTO dto) {
        cmsArticleService.statusUpdate(dto);
        return new Result();
    }


}
