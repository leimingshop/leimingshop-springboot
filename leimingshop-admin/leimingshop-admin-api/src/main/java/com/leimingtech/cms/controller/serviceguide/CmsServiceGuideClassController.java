/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.serviceguide;

import cn.hutool.core.collection.CollectionUtil;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.articleclass.CmsClassDTO;
import com.leimingtech.modules.dto.articleclass.CmsClassSaveDTO;
import com.leimingtech.modules.dto.articleclass.CmsClassTreeListDTO;
import com.leimingtech.modules.enums.articleclass.CmsEnum;
import com.leimingtech.modules.service.articleclass.CmsClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CMS服务指南分类管理
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@RestController
@RequestMapping("cms/serviceguideclass")
@Api(tags = "CMS服务指南分类管理")
@Slf4j
public class CmsServiceGuideClassController {

    @Resource
    private CmsClassService cmsClassService;

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<CmsClassDTO> get(@PathVariable("id") Long id) {
        CmsClassDTO data = cmsClassService.get(id);
        return new Result<CmsClassDTO>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    public Result save(@RequestBody CmsClassSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        Map<String, Object> res = cmsClassService.articleClassSave(dto);
        if (CollectionUtil.isEmpty(res)) {
            return new Result().ok(null, "服务指南分类创建成功");
        }
        return new Result().error(Integer.valueOf(res.get("code").toString()), res.get("msg").toString());
    }

    @PutMapping("update")
    @ApiOperation("修改")
    public Result update(@RequestBody CmsClassDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        cmsClassService.update(dto);
        return new Result();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        Map<String, Object> res = cmsClassService.deleteClassIds(ids);
        if (CollectionUtil.isEmpty(res)) {
            return new Result().ok(null, "服务指南分类信息删除成功");
        }
        return new Result().error(Integer.valueOf(res.get("code").toString()), res.get("msg").toString());
    }

//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<CmsArticleDTO> list = cmsArticleService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, CmsArticleExcel.class);
//    }

    @GetMapping("classTreeList")
    @ApiOperation("文章分类树集合")
    public Result<List<CmsClassTreeListDTO>> classAllList() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("acCode", CmsEnum.FWZNCODE.value());
        List<CmsClassTreeListDTO> data = cmsClassService.getTree(params);
        return new Result<List<CmsClassTreeListDTO>>().ok(data);
    }

    @GetMapping("firstClassList")
    @ApiOperation("一级分类集合")
    public Result<List<CmsClassTreeListDTO>> firstClassList() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("acCode", CmsEnum.FWZNCODE.value());
        List<CmsClassTreeListDTO> data = cmsClassService.firstClassList(params);
        return new Result<List<CmsClassTreeListDTO>>().ok(data);
    }

}
