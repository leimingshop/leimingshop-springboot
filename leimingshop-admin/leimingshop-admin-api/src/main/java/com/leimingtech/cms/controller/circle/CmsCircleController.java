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
import com.leimingtech.modules.dto.circle.CmsCircleTopicDTO;
import com.leimingtech.modules.dto.circle.CmsCircleTopicPageDTO;
import com.leimingtech.modules.dto.circle.CmsCircleTopicSaveDTO;
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
 * 圈子话题管理
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2020-01-07
 */
@RestController
@RequestMapping("cms/circle")
@Api(tags = "CMS圈子话题管理")
@Slf4j
public class CmsCircleController {

    @Autowired
    private CmsCircleService cmsCircleService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "topicName", value = "话题名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "acId", value = "圈子分类", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态标识（0：停用 1：启用（默认））", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "superTopicFlag", value = "超话标识（0：普话（默认），1：超话）", paramType = "query", dataType = "int")
    })
    public Result<PageData<CmsCircleTopicPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CmsCircleTopicPageDTO> page = cmsCircleService.page(params);
        return new Result<PageData<CmsCircleTopicPageDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<CmsCircleTopicDTO> get(@PathVariable("id") Long id) {
        CmsCircleTopicDTO data = cmsCircleService.get(id);
        return new Result<CmsCircleTopicDTO>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    public Result save(@RequestBody CmsCircleTopicSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        cmsCircleService.save(dto);
        return new Result();
    }

    @PutMapping("update")
    @ApiOperation("修改")
    public Result update(@RequestBody CmsCircleTopicDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        cmsCircleService.update(dto);
        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        cmsCircleService.delete(ids);
        return new Result().ok("", "删除成功");
    }

//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<CmsCircleDTO> list = cmsCircleService.list(params);
//        ExcelUtils.exportExcelToTarget(response, null, list, CmsCircleExcel.class);
//    }

}
