/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.SysRoleDTO;
import com.leimingtech.enums.RoleEnum;
import com.leimingtech.service.sys.SysRoleDataScopeService;
import com.leimingtech.service.sys.SysRoleMenuService;
import com.leimingtech.service.sys.SysRoleService;
import com.leimingtech.service.sys.SysRoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色管理")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDataScopeService sysRoleDataScopeService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "角色名", paramType = "query", dataType = "String")
    })
    public Result<PageData<SysRoleDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysRoleDTO> page = sysRoleService.page(params);

        return new Result<PageData<SysRoleDTO>>().ok(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public Result<List<SysRoleDTO>> list(UserDetail userDetail) {
        List<SysRoleDTO> data = sysRoleService.list(new HashMap<>(1));

        return new Result<List<SysRoleDTO>>().ok(data);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<SysRoleDTO> get(@PathVariable("id") Long id) {
        SysRoleDTO data = sysRoleService.get(id);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.getMenuIdList(id);
        data.setMenuIdList(menuIdList);

        //查询角色对应的数据权限
        List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(id);
        data.setDeptIdList(deptIdList);

        return new Result<SysRoleDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存角色")
    @LogOperation("保存角色")
    public Result save(@RequestBody SysRoleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        boolean result = sysRoleService.checkName(dto.getName());

        if (!result) {
            return new Result().error("角色名称已经存在");
        }

        sysRoleService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("修改角色")
    @LogOperation("修改角色")
    public Result update(@RequestBody SysRoleDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysRoleService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    @DeleteMapping
    @ApiOperation("删除角色")
    @LogOperation("删除角色")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        for (Long id : ids) {
            SysRoleDTO sysRoleDTO = sysRoleService.get(id);
            if (sysRoleDTO != null && sysRoleDTO.getRoleFlag() == RoleEnum.SUPER_ADMIN.value()) {
                return new Result().error("超级管理员角色不能删除");
            }
        }
        // 验证当前角色是否关联管理员
        Integer userCount = sysRoleUserService.findUserCount(ids);
        if (userCount > 0) {
            return new Result().error("当前角色下已关联用户，请先解除关联");
        }

        sysRoleService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    @PutMapping("user/{id}")
    @ApiOperation("保存")
    @LogOperation("Save UserRole")
    public Result saveUserRole(@PathVariable("id") Long userId, @RequestParam("roleId") Long roleId) {
        //效验数据
        //保存角色用户关系
        sysRoleUserService.saveOrUpdate(userId, roleId);

        return new Result().ok(null, "保存成功");
    }


    /**
     * 校验角色名称是否唯一
     *
     * @param name 角色名称
     * @date 2019/7/4 15:48
     * @author lixiang
     **/
    @GetMapping("check/{name}")
    @ApiOperation("校验角色名称唯一性")
    @LogOperation("校验角色名称唯一性")
    public Result<Boolean> checkRoleName(@PathVariable("name") String name) {

        AssertUtils.isNull(name, "名称不能为空");

        Boolean result = sysRoleService.checkName(name);

        if (result) {
            return new Result().ok("角色名称可以使用");
        } else {
            return new Result().error("角色名称已经存在");
        }
    }

}
