/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.circle;

import cn.hutool.core.collection.CollectionUtil;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.articleclass.*;
import com.leimingtech.modules.enums.articleclass.CmsEnum;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 圈子分类管理
 *
 * @author pixiaoyong pixiaoyong@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@RestController
@RequestMapping("cms/circleclass")
@Api(tags = "CMS圈子分类管理")
@Slf4j
public class CmsCircleClassController {


    @Autowired
    private CmsCircleService cmsCircleService;

    @Autowired
    private CmsClassService cmsClassService;

    @GetMapping("page")
    @ApiOperation("圈子分类列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "acName", value = "分类名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态标识（0：停用 1：启用（默认））", paramType = "query", dataType = "int")
    })
    public Result<PageData<CmsClassPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        //标识为圈子分类
        params.put("acCode", CmsEnum.QZCODE.value());
        PageData<CmsClassPageDTO> page = cmsClassService.page(params);
        return new Result<PageData<CmsClassPageDTO>>().ok(page);
    }

    /**
     * 圈子分类列表
     *
     * @param
     * @return
     */
    @GetMapping("circleClassList")
    @ApiOperation("圈子分类列表")
    public Result<List<CmsClassListDTO>> circleClassList() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("acCode", CmsEnum.QZCODE.value());
        List<CmsClassListDTO> list = cmsClassService.classList(params);

        return new Result<List<CmsClassListDTO>>().ok(list);
    }


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
        cmsClassService.circleClassSave(dto);

        return new Result();
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
        Map<String, Object> res = cmsClassService.deleteQzClassIds(ids);
        if (CollectionUtil.isEmpty(res)) {
            return new Result().ok(null, "圈子分类信息删除成功");
        }
        return new Result().error(Integer.valueOf(res.get("code").toString()), res.get("msg").toString());
    }

    @GetMapping("/getTreeClass")
    @ApiOperation("获取树形分类列表")
    public Result<List<CmsClassTreeListDTO>> getTreeClassList(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("acCode", CmsEnum.ZXCODE.value());
        List<CmsClassTreeListDTO> treeList = cmsClassService.getTree(params);

        return new Result<List<CmsClassTreeListDTO>>().ok(treeList);
    }

}
