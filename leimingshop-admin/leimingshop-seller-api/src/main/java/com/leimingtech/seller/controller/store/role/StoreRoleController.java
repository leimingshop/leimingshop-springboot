/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.store.role;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.enums.RoleEnum;
import com.leimingtech.modules.dto.role.FindStoreRoleDTO;
import com.leimingtech.modules.dto.role.SaveStoreRoleDTO;
import com.leimingtech.modules.dto.role.StoreRoleDTO;
import com.leimingtech.modules.dto.role.UpdateStoreRoleDTO;
import com.leimingtech.modules.service.role.StoreRoleService;
import com.leimingtech.modules.service.rolemenu.StoreRoleMenuService;
import com.leimingtech.modules.service.user.StoreUserService;
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
 * @Description
 * @Data: 2019/7/11 17:42
 * @Author: chengqian
 * @Version: 1.0
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色管理")
public class StoreRoleController {
    @Autowired
    private StoreRoleService storeRoleService;
    @Autowired
    private StoreRoleMenuService storeRoleMenuService;
    @Autowired
    private StoreUserService storeUserService;

    /**
     * 分页查询角色信息
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleMark", value = "是否是超级管理员", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleName", value = "角色名", paramType = "query", dataType = "String")
    })
    public Result<PageData<StoreRoleDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getRoleId() == null) {
            return new Result<PageData<StoreRoleDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId());
        PageData<StoreRoleDTO> page = storeRoleService.page(params);

        return new Result<PageData<StoreRoleDTO>>().ok(page);
    }

    /**
     * 新增角色
     *
     * @param dto 新增参数
     * @return
     */
    @PostMapping
    @ApiOperation("新增角色")
    @LogOperation("新增角色")
    public Result save(@RequestBody SaveStoreRoleDTO dto, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        // 查询角色名称数量
        int count = storeRoleService.checkRoleName(dto.getRoleName(), sellerDetail.getStoreId(), null);

        if (count > 0) {
            return new Result().error("角色名称已经存在");
        }
        dto.setStoreId(sellerDetail.getStoreId());
        storeRoleService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改角色
     *
     * @param dto 修改参数
     * @return
     */
    @PutMapping
    @ApiOperation("修改角色")
    @LogOperation("修改角色")
    public Result update(@RequestBody UpdateStoreRoleDTO dto, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        StoreRoleDTO storeRoleDTO = storeRoleService.get(dto.getId());
        // 查询角色名称数量
        int count = storeRoleService.checkRoleName(dto.getRoleName(), sellerDetail.getStoreId(), dto.getId());

        if (count > 0) {
            return new Result().error("角色名称已经存在");
        }

        if (storeRoleDTO != null && storeRoleDTO.getRoleMark() == RoleEnum.SUPER_ADMIN.value()) {
            return new Result().error("超级管理员角色不能修改");
        }

        storeRoleService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * s删除角色
     *
     * @param ids 角色ID 数组
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除角色")
    @LogOperation("删除角色")
    public Result delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            StoreRoleDTO storeRoleDTO = storeRoleService.get(id);
            if (storeRoleDTO != null && storeRoleDTO.getRoleMark() == RoleEnum.SUPER_ADMIN.value()) {
                return new Result().error("超级管理员角色不能删除");
            }
        }
        // 校验角色下面是否有关联账号
        Integer userCount = storeUserService.findUserCountByRole(ids);
        if (userCount > 0) {
            return new Result().error("该角色下已关联用户，请先解除关联");
        }

        storeRoleService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 校验角色名称是否重复
     */

    @GetMapping("check/rolename")
    @ApiOperation("校验角色名称是否重复")
    @LogOperation("校验角色名称是否重复")
    public Result checkName(@RequestParam String roleName, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Integer count = storeRoleService.checkRoleName(roleName, sellerDetail.getStoreId(), null);
        if (count > 0) {
            return new Result().error("名称重复");
        }
        return new Result().ok(null, "可以使用");

    }


    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据ID获取信息")
    public Result<FindStoreRoleDTO> findById(@PathVariable("id") Long id) {

        // 根据店铺ID 查询用户信息
        StoreRoleDTO storeRoleDTO = storeRoleService.get(id);
        FindStoreRoleDTO findStoreRoleDTO = ConvertUtils.sourceToTarget(storeRoleDTO, FindStoreRoleDTO.class);
        // 查询角色关联的菜单
        List<Long> storeRoleMenuDTOS = storeRoleMenuService.getListByRoleId(id);
        findStoreRoleDTO.setMenuIdList(storeRoleMenuDTOS);
        return new Result<FindStoreRoleDTO>().ok(findStoreRoleDTO);
    }


}
