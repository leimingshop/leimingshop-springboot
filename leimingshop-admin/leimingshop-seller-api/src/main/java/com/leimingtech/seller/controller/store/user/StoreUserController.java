/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.store.user;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.enums.RoleEnum;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.user.*;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.modules.statuscode.OrderStatusCode;
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
 * @Data: 2019/7/11 20:22
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
public class StoreUserController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(StoreUserController.class);

    @Autowired
    private StoreUserService storeUserService;

    //private static Long id = 1192016327011786753L;

    /**
     * 分页查询当前店铺用户信息
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "storeId", value = "店铺ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String")

    })
    public Result<PageData<FindStoreUserDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params,
                                                   @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result<PageData<FindStoreUserDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("userId", sellerDetail.getId());
        PageData<FindStoreUserDTO> page = storeUserService.sellerUserPage(params);

        return new Result<PageData<FindStoreUserDTO>>().ok(page);
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
    public Result save(@RequestBody SaveStoreUserDTO dto, @ApiIgnore SellerDetail sellerDetail) {


        // 密码解密
        // 使用RSA私钥进行解密
        try {
            dto.setPassword(RSACoder.decryptByPrivateKey(dto.getPassword()));
            dto.setAccount(RSACoder.decryptByPrivateKey(dto.getAccount()));
            dto.setConfirmPassword(RSACoder.decryptByPrivateKey(dto.getConfirmPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", dto.getAccount(), e);
            return new Result().error("密码解析失败，请重新输入");
        }

        if (sellerDetail == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        int count = storeUserService.verify(dto.getAccount(), null, null);
        if (count > 0) {
            return new Result().error("用户名已经存在");
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
        Integer emailCount = storeUserService.verifyEmail(dto.getEmail(), null);
        if (emailCount > 0) {
            return new Result().error("邮箱已经注册");
        }

        dto.setPassword(PasswordUtils.encode(dto.getPassword()));
        storeUserService.saveUser(dto);

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
            return new Result().error("超级管理员不能删除");
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
        UpdateStoreUserDTO findStoreUserDTO = storeUserService.findById(dto.getId());
        if ((findStoreUserDTO != null && findStoreUserDTO.getRoleMark() != null) && findStoreUserDTO.getRoleMark() == RoleEnum.SUPER_ADMIN.value()) {
            return new Result().error("超级管理员不能修改");
        }
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
     * 重置密码
     *
     * @param id: 管理员ID
     **/
    @ApiOperation("重置管理员密码")
    @LogOperation("重置管理员密码")
    @PutMapping("reset/password")
    public Result restPassword(@RequestParam("id") Long id) {
        boolean result = storeUserService.resetPassword(id);
        return result ? new Result().ok(null, "密码重置成功") : new Result().error("密码重置失败，请重试");
    }

    /**
     * 功能描述:
     * <根据用户id查询商户个人中心信息>
     *
     * @param id
     * @return
     * @date 2020/3/5 15:41
     * @author weixianchun
     **/
    @GetMapping("person/info/id")
    @ApiOperation("根据用户id查询商户个人中心信息")
    public Result<FindStoreUserPersonInfoDTO> findPersonInfoById(@ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<FindStoreUserPersonInfoDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        FindStoreUserPersonInfoDTO info = storeUserService.findPersonInfoById(sellerDetail.getId());
        if (null == info) {
            return new Result<FindStoreUserPersonInfoDTO>().error("用户不存在");
        }
        info.setStoreId(sellerDetail.getStoreId());
        return new Result<FindStoreUserPersonInfoDTO>().ok(info, "查询成功");
    }


    /**
     * 功能描述:
     * <修改商户个人头像>
     *
     * @param logo 商户头像
     * @date 2020/3/5 16:19
     * @author weixianchun
     **/
    @PutMapping("person/info/logo")
    @ApiOperation("修改商户个人头像")
    @LogOperation("修改商户个人头像")
    public Result updatePersonInfoLogo(@ApiIgnore SellerDetail sellerDetail, @RequestBody StoreUserLogoDTO userLogoDTO) {

        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        //修改头像
        userLogoDTO.setId(sellerDetail.getId());
        storeUserService.update(ConvertUtils.sourceToTarget(userLogoDTO, StoreUserDTO.class));

        return new Result().ok(null, "修改头像成功");
    }

    /**
     * 功能描述:
     * <修改商户手机号>
     *
     * @param mobilePhone 商户手机号
     * @date 2020/3/5 16:19
     * @author weixianchun
     **/
    @PutMapping("person/info/phone")
    @ApiOperation("修改商户手机号")
    @LogOperation("修改商户手机号")
    public Result updatePersonInfoPhone(@ApiIgnore SellerDetail sellerDetail, @RequestBody StoreUserPhoneDTO userPhoneDTO) {

        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        //验证手机号是否被占用  使用RSA私钥进行解密
        try {
            if (!RSACoder.decryptByPrivateKey(userPhoneDTO.getMobilePhone()).matches(RegexConstant.SAVE_UPDATE_PHONE_REGEX)) {
                return new Result().error("手机号格式错误，请重新输入");
            }
            int count = storeUserService.verifyMobile(RSACoder.decryptByPrivateKey(userPhoneDTO.getMobilePhone()), sellerDetail.getId());
            if (count > 0) {
                return new Result().error("该手机号已被占用");
            }
            userPhoneDTO.setId(sellerDetail.getId());
            userPhoneDTO.setMobilePhone(RSACoder.decryptByPrivateKey(userPhoneDTO.getMobilePhone()));
            storeUserService.update(ConvertUtils.sourceToTarget(userPhoneDTO, StoreUserDTO.class));
        } catch (Exception e) {
            log.error("账户:{},手机号解析出现异常:{}", sellerDetail.getAccount(), e);
            return new Result().error("手机号解析失败，请重新输入");
        }


        return new Result().ok(null, "修改手机号成功");
    }

    /**
     * 功能描述:
     * <修改商户邮箱>
     *
     * @param email 商户邮箱
     * @date 2020/3/5 16:19
     * @author weixianchun
     **/
    @PutMapping("person/info/email")
    @ApiOperation("修改商户邮箱")
    @LogOperation("修改商户邮箱")
    public Result updatePersonInfoEmail(@ApiIgnore SellerDetail sellerDetail, @RequestBody StoreUserEmailDTO userEmailDTO) {

        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        try {
            //验证邮箱是否被占用 ,验证邮箱格式,使用RSA私钥进行解密
            if (!RSACoder.decryptByPrivateKey(userEmailDTO.getEmail()).matches(RegexConstant.EMAIL_REGEX)) {
                return new Result().error("邮箱格式错误，请重新输入");
            }
            Integer count = storeUserService.verifyEmail(RSACoder.decryptByPrivateKey(userEmailDTO.getEmail()), sellerDetail.getId());
            if (count > 0) {
                return new Result().error("该邮箱已被占用");
            }
            userEmailDTO.setId(sellerDetail.getId());
            userEmailDTO.setEmail(RSACoder.decryptByPrivateKey(userEmailDTO.getEmail()));
            storeUserService.update(ConvertUtils.sourceToTarget(userEmailDTO, StoreUserDTO.class));
        } catch (Exception e) {
            log.error("账户:{},邮箱解析出现异常:{}", sellerDetail.getAccount(), e);
            return new Result().error("邮箱解析失败，请重新输入");
        }

        return new Result().ok(null, "修改邮箱成功");
    }

    /**
     * 功能描述:
     * <修改商户密码>
     *
     * @param dto 修改密码实体
     * @date 2020/3/5 16:19
     * @author weixianchun
     **/
    @PutMapping("person/info/pwd")
    @ApiOperation("修改商户密码")
    @LogOperation("修改商户密码")
    public Result updatePersonInfoPwd(@ApiIgnore SellerDetail sellerDetail, @RequestBody StoreUpdateUserPassWordDTO dto) {

        if (sellerDetail == null || sellerDetail.getId() == null) {
            mlogger.info(OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getCode(), OrderStatusCode.CLIENT_UNAUTHORIZED_ERROR.getMessage());
            return new Result<PageData<OrderDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 使用RSA私钥进行解密
        try {
            dto.setNewPassword(RSACoder.decryptByPrivateKey(dto.getNewPassword()));
            dto.setConfirmPassword(RSACoder.decryptByPrivateKey(dto.getConfirmPassword()));
            dto.setPassword(RSACoder.decryptByPrivateKey(dto.getPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", sellerDetail.getAccount(), e);
            return new Result().error("密码解析失败，请重新输入");
        }
        //获取用户信息
        StoreUserDTO storeUserDTO = storeUserService.get(sellerDetail.getId());

        //原密码不正确
        if (!PasswordUtils.matches(dto.getPassword(), storeUserDTO.getPassword())) {
            return new Result().error(ErrorCode.PASSWORD_ERROR);
        }
        //校验密码格式
        if (!dto.getNewPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }
        // 验证新密码
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return new Result().error("两次输入密码不一致");
        }

        //修改密码 (将加密后的新密码保存到数据库)
        StoreUserDTO userDTO = new StoreUserDTO();
        userDTO.setId(sellerDetail.getId());
        userDTO.setPassword(PasswordUtils.encode(dto.getNewPassword()));
        storeUserService.update(userDTO);

        return new Result().ok(null, "修改密码成功");
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

}
