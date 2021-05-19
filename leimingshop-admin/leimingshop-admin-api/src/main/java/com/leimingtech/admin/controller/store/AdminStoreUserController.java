/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.store;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.store.RegisterStoreDTO;
import com.leimingtech.modules.dto.user.FindStoreUserDTO;
import com.leimingtech.modules.dto.user.StoreUserDTO;
import com.leimingtech.modules.dto.user.StoreUserListDTO;
import com.leimingtech.modules.dto.user.UpdateStoreUserDTO;
import com.leimingtech.modules.service.user.StoreUserService;
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
 * @Description
 * @Data: 2020-3-9 20:22
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("store/user")
@Api(tags = "admin端用户管理")
public class AdminStoreUserController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(AdminStoreUserController.class);

    @Autowired
    private StoreUserService storeUserService;

    /**
     * 分页查询当前店铺用户信息
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询所有用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobilePhone", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isEnable", value = "状(0 启用，1 禁用)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String")

    })
    public Result<PageData<StoreUserDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<StoreUserDTO> page = storeUserService.adminStoreUserPage(params);

        return new Result<PageData<StoreUserDTO>>().ok(page);
    }

    /**
     * 新增用户
     *
     * @param dto 新增参数
     * @return
     */
    @PostMapping
    @ApiOperation("新增用户")
    @LogOperation("新增用户")
    public Result save(@RequestBody RegisterStoreDTO dto) {

        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        //rsa解密
        try {
            dto.setPassword(RSACoder.decryptByPrivateKey(dto.getPassword()));
            dto.setAccount(RSACoder.decryptByPrivateKey(dto.getAccount()));
            dto.setConfirmPassword(RSACoder.decryptByPrivateKey(dto.getConfirmPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", dto.getAccount(), e);
            return new Result().error("密码解析失败，请重新输入");
        }

        Integer verify = storeUserService.verify(dto.getAccount(), null, null);
        if (verify > 0) {
            return new Result().error("该登录名已存在，请重新输入");
        }

        if (!dto.getAccount().matches(RegexConstant.SAVE_UPDATE_ACCOUNT_REGEX)) {
            return new Result().error("账号必须为6-20位字符，数字，下划线");
        }
        if (!dto.getPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }
        // 验证新密码
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return new Result().error("两次输入密码不一致");
        }

        Integer phoneCount = storeUserService.verifyMobile(dto.getMobilePhone(), null);
        if (phoneCount > 0) {
            return new Result().error("该手机号已注册");
        }

        // 校验邮箱
        Integer count = storeUserService.verifyEmail(dto.getEmail(), null);
        if (count > 0) {
            return new Result().error("邮箱已经注册");
        }


        storeUserService.storeRegister(dto);

        return new Result().ok(null, "保存成功");
    }


    /**
     * 删除用户
     *
     * @param ids 用户ID 数组
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除用户")
    @LogOperation("删除用户")
    public Result delete(@RequestBody Long[] ids) {

        // 验证用户是否是超级管理员
        Integer count = storeUserService.findUserMark(ids);
        if (count > 0) {
            return new Result().error("该商户账号有在营业店铺，无法删除");
        }

        storeUserService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据ID获取信息")
    public Result<UpdateStoreUserDTO> findById(@PathVariable("id") Long id) {

        // 根据店铺ID 查询用户信息
        UpdateStoreUserDTO byId = storeUserService.findById(id);
        return new Result<UpdateStoreUserDTO>().ok(byId);
    }


    /**
     * 修改用户信息
     *
     * @param dto 修改参数
     * @return
     */
    @PutMapping
    @ApiOperation("修改用户信息")
    @LogOperation("修改用户信息")
    public Result update(@RequestBody UpdateStoreUserDTO dto) {

        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        Integer verify = storeUserService.verify(dto.getAccount(), null, dto.getId());
        if (verify > 0) {
            return new Result().error("该登录名已存在，请重新输入");
        }

        if (!dto.getAccount().matches(RegexConstant.SAVE_UPDATE_ACCOUNT_REGEX)) {
            return new Result().error("账号必须为6-20位字符，数字，下划线");
        }

        Integer phoneCount = storeUserService.verifyMobile(dto.getMobilePhone(), dto.getId());
        if (phoneCount > 0) {
            return new Result().error("该手机号已注册");
        }

        // 校验邮箱
        Integer count = storeUserService.verifyEmail(dto.getEmail(), dto.getId());
        if (count > 0) {
            return new Result().error("邮箱已经注册");
        }

        storeUserService.updateSellerUser(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 启用禁用用户
     *
     * @param userId   用户id
     * @param isEnable 0 启用 1 禁用
     * @return
     */
    @PutMapping("isenable")
    @ApiOperation("启用禁用用户")
    @LogOperation("启用禁用用户")
    public Result isEnable(@RequestParam("userId") Long userId, @RequestParam("type") Integer isEnable) {
        StoreUserDTO storeUserDTO = new StoreUserDTO();
        storeUserDTO.setId(userId);
        storeUserDTO.setIsEnable(isEnable);
        storeUserService.update(storeUserDTO);
        return new Result().ok(null, isEnable == 1 ? "禁用成功" : "启用成功");
    }

    /**
     * 查询店铺下所有管理员
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("user/page")
    @ApiOperation("分页查询店铺下用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺ID", paramType = "query", dataType = "Long")

    })
    public Result<PageData<FindStoreUserDTO>> userPage(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<FindStoreUserDTO> page = storeUserService.sellerUserPage(params);

        return new Result<PageData<FindStoreUserDTO>>().ok(page);
    }

    /**
     * 查询未关联店铺的用户
     *
     * @return
     */
    @GetMapping("not/store/user")
    @ApiOperation("查询未关联店铺的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobilePhone", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isEnable", value = "状(0 启用，1 禁用)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String")

    })
    public Result<PageData<StoreUserListDTO>> notStoreUser(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<StoreUserListDTO> page = storeUserService.findNotStoreUser(params);


        return new Result<PageData<StoreUserListDTO>>().ok(page);
    }

    /**
     * 重置管理员密码
     *
     * @param id
     * @return
     */
    @ApiOperation("重置管理员密码")
    @LogOperation("重置管理员密码")
    @PutMapping("reset/password")
    public Result restPassword(@RequestParam("id") Long id) {
        boolean result = storeUserService.resetPassword(id);
        return result ? new Result().ok(null, "密码重置成功") : new Result().error("密码重置失败，请重试");
    }
}
