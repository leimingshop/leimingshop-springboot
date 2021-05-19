/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.admin.excel.SysUserExcel;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.enums.SuperAdminEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.PasswordDTO;
import com.leimingtech.dto.sys.SysUserDTO;
import com.leimingtech.dto.sys.SysUserDetailDTO;
import com.leimingtech.service.sys.SysResourceService;
import com.leimingtech.service.sys.SysRoleDataScopeService;
import com.leimingtech.service.sys.SysRoleUserService;
import com.leimingtech.service.sys.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 用户管理Controller
 * @Date :2019/5/28 14:07
 * @Version V1.0
 **/
@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
@Slf4j
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    private SysResourceService sysResourceService;

    /**
     * @Author: weixianchun
     * @Description: 分页并根据用户名查询
     * @Date :2019/5/28 12:59
     * @Param params
     * @Version V1.0
     **/
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "Long")
    })
    public Result<PageData<SysUserDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SysUserDTO> page = sysUserService.page(params);

        return new Result<PageData<SysUserDTO>>().ok(page);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询用户信息（不返回用户名）
     * @Date :2019/5/28 12:59
     * @Param id
     * @Version V1.0
     **/
    @GetMapping("{id}")
    @ApiOperation("根据id查询用户信息")
    public Result<SysUserDetailDTO> get(@PathVariable("id") Long id) {
        SysUserDTO data = sysUserService.get(id);

        SysUserDetailDTO sysUserDetailDTO = new SysUserDetailDTO();
        BeanCopier.create(data.getClass(), sysUserDetailDTO.getClass(), false)
                .copy(data, sysUserDetailDTO, null);

        //用户角色列表
        // lx 用户与角色关系修改为一对一，考虑不能需求，此处按照以下方式处理
        List<Long> roleIdList = sysRoleUserService.getRoleIdList(id);

        if (CollectionUtils.isNotEmpty(roleIdList)) {
            sysUserDetailDTO.setRoleId(roleIdList.get(0));
        }

        return new Result<SysUserDetailDTO>().ok(sysUserDetailDTO);
    }

    /**
     * @Author: weixianchun
     * @Description: 查看登录用户信息
     * @Date :2019/5/28 13:00
     * @Param user
     * @Version V1.0
     **/
    @GetMapping("info")
    @ApiOperation("登录用户信息")
    public Result<SysUserDTO> info(@ApiIgnore UserDetail user) {
        SysUserDTO data = ConvertUtils.sourceToTarget(user, SysUserDTO.class);
        return new Result<SysUserDTO>().ok(data);
    }

    /**
     * @Author: weixianchun
     * @Description: 保存用户信息
     * @Date :2019/5/28 13:00
     * @Param dto
     * @Version V1.0
     **/
    @PostMapping
    @ApiOperation("保存用户信息")
    @LogOperation("保存用户信息")
    public Result save(@RequestBody SysUserDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        // 密码解密
        // 使用RSA私钥进行解密
        try {
            dto.setPassword(RSACoder.decryptByPrivateKey(dto.getPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", dto.getUsername(), e);
            return new Result().error("密码解析失败，请重新输入");
        }

        // 增加账号和密码校验
        if (!dto.getUsername().matches(RegexConstant.SAVE_UPDATE_ACCOUNT_REGEX)) {
            return new Result().error("用户名必须为6-20位字符，数字，下划线");
        }
        if (!dto.getPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符，数字，特殊字符");
        }

        SysUserDTO sysUserDTO = sysUserService.getByUsername(dto.getUsername());
        if (sysUserDTO != null) {
            return new Result().error("用户名已存在");
        }

        sysUserService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 修改用户信息
     * @Date :2019/5/28 13:02
     * @Param dto
     * @Version V1.0
     **/
    @PutMapping
    @ApiOperation("修改用户信息")
    @LogOperation("修改用户信息")
    public Result update(@RequestBody SysUserDetailDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        SysUserDTO sysUserDTO = sysUserService.getByUsername(dto.getUsername());
        if (sysUserDTO != null && !sysUserDTO.getId().equals(dto.getId())) {
            return new Result().error("用户名已存在");
        }
        if (!dto.getUsername().matches(RegexConstant.SAVE_UPDATE_ACCOUNT_REGEX)) {
            return new Result().error("用户名必须为6-20位字符，数字，下划线");
        }

        sysUserService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 修改密码
     * @Date :2019/5/28 13:03
     * @Param dto
     * @Version V1.0
     **/
    @PutMapping("password")
    @ApiOperation("修改密码")
    @LogOperation("修改密码")
    public Result password(@RequestBody PasswordDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto);

        UserDetail user = SecurityUser.getUser();

        // 密码解密
        // 使用RSA私钥进行解密
        try {
            dto.setNewPassword(RSACoder.decryptByPrivateKey(dto.getNewPassword()));
            dto.setPassword(RSACoder.decryptByPrivateKey(dto.getPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", user.getUsername(), e);
            return new Result().error("密码解析失败，请重新输入");
        }
        //原密码不正确
        if (!PasswordUtils.matches(dto.getPassword(), user.getPassword())) {
            return new Result().error(ErrorCode.PASSWORD_ERROR);
        }
        if (!dto.getNewPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }
        sysUserService.updatePassword(user.getId(), dto.getNewPassword());

        return new Result().ok(null, "修改成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id删除用户
     * @Date :2019/5/28 13:04
     * @Param ids
     * @Version V1.0
     **/
    @DeleteMapping
    @ApiOperation("根据id删除用户")
    @LogOperation("根据id删除用户")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        // 校验删除用户是否是超级管理员
        for (Long id : ids) {
            SysUserDTO sysUserDTO = sysUserService.get(id);
            if (sysUserDTO.getSuperAdmin().equals(SuperAdminEnum.YES.value())) {
                return new Result().error("超级管理员不允许删除");
            }
        }

        sysUserService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 导出用户信息
     * @Date :2019/5/28 13:05
     * @Param params
     * @Param response
     * @Version V1.0
     **/
    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<SysUserDTO> list = sysUserService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, SysUserExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

    /**
     * @Author: weixianchun
     * @Description: 根据用户名，获取用户信息
     * @Date :2019/5/28 13:06
     * @Param username 用户名
     * @Version V1.0
     **/
    @GetMapping("username")
    public Result<UserDetail> getByUsername(String username) {
        SysUserDTO user = sysUserService.getByUsername(username);

        UserDetail userDetail = ConvertUtils.sourceToTarget(user, UserDetail.class);
        //初始化用户数据
        initUserData(userDetail);

        return new Result<UserDetail>().ok(userDetail);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据用户ID，获取用户信息
     * @Date :2019/5/28 13:06
     * @Param id 用户id
     * @Version V1.0
     **/
    @GetMapping("user/{id}")
    public Result<UserDetail> getById(@PathVariable Long id) {
        SysUserDTO user = sysUserService.get(id);

        UserDetail userDetail = ConvertUtils.sourceToTarget(user, UserDetail.class);
        //初始化用户数据
        initUserData(userDetail);


        return new Result<UserDetail>().ok(userDetail);
    }

    /**
     * 重置密码
     *
     * @param id: 管理员ID
     * @return 重置结果
     * @date 2019/8/8 15:27
     * @author lixiang
     **/
    @ApiOperation("重置管理员密码")
    @LogOperation("重置管理员密码")
    @PutMapping("reset/password")
    public Result restPassword(@RequestParam("id") Long id) {
        boolean result = sysUserService.resetPassword(id);
        return result ? new Result().ok(null, "密码重置成功，新密码为123456") : new Result().error("密码重置失败，请重试");
    }

    /**
     * @Author: weixianchun
     * @Description: 初始化用户数据
     * @Date :2019/5/28 13:09
     * @Param userDetail
     * @Version V1.0
     **/
    private void initUserData(UserDetail userDetail) {
        if (userDetail == null) {
            return;
        }
        //用户部门数据权限
        List<Long> deptIdList = sysRoleDataScopeService.getDataScopeList(userDetail.getId());
        userDetail.setDeptIdList(deptIdList);

        //用户资源列表
        List<ResourceBO> resourceList = sysResourceService.getUserResourceList(userDetail.getId());
        userDetail.setResourceList(resourceList);

    }


}