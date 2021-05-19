/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.dto.MenuResourceDTO;
import com.leimingtech.dto.SysMenuDTO;
import com.leimingtech.service.sys.SysMenuService;
import com.leimingtech.service.sys.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("menu")
@Api(tags = "菜单管理")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysResourceService sysResourceService;

    @GetMapping("nav")
    @ApiOperation("导航")
    public Result<List<SysMenuDTO>> nav(@ApiIgnore UserDetail userDetail) {
        List<SysMenuDTO> list = sysMenuService.getUserMenuNavList(userDetail);

        return new Result<List<SysMenuDTO>>().ok(list);
    }

    @GetMapping("permissions")
    @ApiOperation("权限标识")
    public Result<Set<String>> permissions(@ApiIgnore UserDetail userDetail) {
        Set<String> set = sysMenuService.getUserPermissions(userDetail.getSuperAdmin(), userDetail.getId());

        return new Result<Set<String>>().ok(set);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @ApiImplicitParam(name = "type", value = "菜单类型 0：菜单 1：按钮  null：全部", paramType = "query", dataType = "int")
    public Result<List<SysMenuDTO>> list(Integer type) {
        List<SysMenuDTO> list = sysMenuService.getMenuList(type);

        return new Result<List<SysMenuDTO>>().ok(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<SysMenuDTO> get(@PathVariable("id") Long id) {
        SysMenuDTO data = sysMenuService.get(id);

        //菜单资源列表
        List<MenuResourceDTO> resourceList = sysResourceService.getMenuResourceList(id);
        data.setResourceList(resourceList);

        return new Result<SysMenuDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("Save Menu")
    public Result save(@RequestBody SysMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysMenuService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("Update Menu")
    public Result update(@RequestBody SysMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysMenuService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    @LogOperation("Delete Menu")
    public Result delete(@PathVariable("id") Long id) {
        //效验数据
        AssertUtils.isNull(id, "id");
        Long pid = id;
        //判断是否有子菜单或按钮
        List<SysMenuDTO> list = sysMenuService.getListPid(pid);
        if (CollectionUtils.isNotEmpty(list)) {
            return new Result().error(ErrorCode.SUB_MENU_EXIST);
        }

        sysMenuService.delete(id);

        return new Result();
    }

    @GetMapping("select")
    @ApiOperation("角色菜单权限")
    public Result<List<SysMenuDTO>> select(@ApiIgnore UserDetail userDetail) {
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(userDetail.getSuperAdmin(), userDetail.getId(), null);

        return new Result<List<SysMenuDTO>>().ok(list);
    }

}
