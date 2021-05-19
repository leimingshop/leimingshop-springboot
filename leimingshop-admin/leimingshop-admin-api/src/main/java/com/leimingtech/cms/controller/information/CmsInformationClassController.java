/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.information;

import cn.hutool.core.collection.CollectionUtil;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.articleclass.*;
import com.leimingtech.modules.service.articleclass.CmsClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 资讯分类管理
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@RestController
@RequestMapping("cms/informationclass")
@Api(tags = "CMS资讯分类管理")
@Slf4j
public class CmsInformationClassController {

    @Autowired
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
            return new Result().ok(null, "资讯分类创建成功");
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
            return new Result().ok(null, "资讯分类信息删除成功");
        }
        return new Result().error(Integer.valueOf(res.get("code").toString()), res.get("msg").toString());
    }


    @GetMapping("zxClassSecondLevel")
    @ApiOperation("资讯栏目分类(二级)")
    public Result<List<CmsZxClassListDto>> selectZxClassSecondLevel() {
        List<CmsZxClassListDto> listDtos = cmsClassService.selectZxClassSecondLevel();
        return new Result<List<CmsZxClassListDto>>().ok(listDtos, "查询成功");
    }

    @GetMapping("zxClassList")
    @ApiOperation("资讯栏目分类(分层级)")
    public Result<List<CmsZxClassListDto>> selectZxClassList() {
        List<CmsZxClassListDto> listDtos = cmsClassService.selectZxClassList();
        return new Result<List<CmsZxClassListDto>>().ok(listDtos, "查询成功");
    }

    @GetMapping("zxAllClassList")
    @ApiOperation("资讯栏目分类(分层级)")
    public Result<List<CmsClassListDTO>> selectZxAllClassList() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("acCode", 2);
        List<CmsClassListDTO> listDtos = cmsClassService.classList(map);
        return new Result<List<CmsClassListDTO>>().ok(listDtos, "查询成功");
    }


    @GetMapping("zxChildClassList/{parentId}")
    @ApiOperation("资讯子分类列表")
    public Result<List<CmsChildClassListDto>> zxChildClassList(@PathVariable("parentId") Long id) {
        List<CmsChildClassListDto> listDtos = cmsClassService.zxChildClassList(id);
        return new Result<List<CmsChildClassListDto>>().ok(listDtos, "查询成功");
    }


//
//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<CmsArticleDTO> list = cmsArticleService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, CmsArticleExcel.class);
//    }


}
