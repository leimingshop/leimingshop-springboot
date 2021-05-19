/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.SysDictDTO;
import com.leimingtech.service.sys.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("dict")
@Api(tags = "数据字典")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    /***
     * 分页查询
     * @param params 分页参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("字典分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dictType", value = "字典类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dictName", value = "字典名称", paramType = "query", dataType = "String")
    })
    public Result<PageData<SysDictDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        //字典分类
        PageData<SysDictDTO> page = sysDictService.page(params);

        return new Result<PageData<SysDictDTO>>().ok(page);
    }

    @GetMapping("list")
    @ApiOperation("字典分类数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictName", value = "字典名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dictValue", value = "字典值", paramType = "query", dataType = "String")
    })
    public Result<List<SysDictDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
        //字典分类数据
        List<SysDictDTO> list = sysDictService.list(params);

        return new Result<List<SysDictDTO>>().ok(list);
    }

    /**
     * 根据ID查询信息
     *
     * @param id 主键ID
     * @return
     */

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<SysDictDTO> findById(@PathVariable("id") Long id) {
        SysDictDTO data = sysDictService.get(id);

        return new Result<SysDictDTO>().ok(data);
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     * @return
     */
    @PostMapping
    @ApiOperation("新增数据")
    @LogOperation("新增数据")
    public Result save(@RequestBody SysDictDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysDictService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改数据字典
     *
     * @param dto 参数实体
     * @return
     */

    @PutMapping
    @ApiOperation("修改数据字典")
    @LogOperation("修改数据字典")
    public Result update(@RequestBody SysDictDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysDictService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     * @return
     */

    @DeleteMapping
    @ApiOperation("根据ID删除")
    @LogOperation("根据ID删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        sysDictService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

}