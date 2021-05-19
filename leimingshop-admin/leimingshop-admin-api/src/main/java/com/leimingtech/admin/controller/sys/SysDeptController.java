/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.SysDeptDTO;
import com.leimingtech.service.sys.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 部门管理
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("dept")
@Api(tags = "部门管理")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 查询部门列表
     *
     * @return
     */
    @GetMapping("list")
    @ApiOperation("列表")
    public Result<List<SysDeptDTO>> list() {

        List<SysDeptDTO> list = sysDeptService.list(new HashMap<>(1));

        return new Result<List<SysDeptDTO>>().ok(list);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    @GetMapping("{id}")
    @ApiOperation("根据ID获取信息")
    public Result<SysDeptDTO> findById(@PathVariable("id") Long id) {
        SysDeptDTO data = sysDeptService.get(id);

        return new Result<SysDeptDTO>().ok(data);
    }

    /**
     * 新增部门信息
     *
     * @param dto 部门实体类
     * @return
     */
    @PostMapping
    @ApiOperation("新增部门信息")
    @LogOperation("新增部门信息")
    public Result save(@RequestBody SysDeptDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysDeptService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改部门信息
     *
     * @param dto 部门实体类
     * @return
     */
    @PutMapping
    @ApiOperation("修改部门信息")
    @LogOperation("修改部门信息")
    public Result update(@RequestBody SysDeptDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysDeptService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 根据ID删除信息
     *
     * @param id 主键ID
     * @return
     */
    @DeleteMapping("{id}")
    @ApiOperation("删除")
    @LogOperation("Delete Dept")
    public Result delete(@PathVariable("id") Long id) {
        //效验数据
        AssertUtils.isNull(id, "id");

        sysDeptService.delete(id);

        return new Result().ok(null, "删除成功");
    }
}