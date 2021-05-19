/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.store;

import com.leimingtech.admin.excel.store.StoreExcel;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.member.UpdateLoginDTO;
import com.leimingtech.modules.dto.store.*;
import com.leimingtech.modules.dto.user.StoreUserDTO;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.enums.store.StoreEnum;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.modules.service.usermanage.StoreUserManageService;
import com.leimingtech.modules.statuscode.StoreStatusCode;
import com.leimingtech.modules.utils.MobileUtil;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 店铺管理
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@RestController
@RequestMapping("store")
@Api(tags = "店铺管理")
@Slf4j
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreUserService storeUserService;
    @Autowired
    private StoreUserManageService storeUserManageService;

    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;
    //private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(StoreController.class);

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeId", value = "店铺ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "商家账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gradeId", value = "店铺等级", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeType", value = "店铺类型（1:自营商户，2:普通商户）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<PageStoreDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<PageStoreDTO> page = storeService.findPage(params);

        return new Result<PageData<PageStoreDTO>>().ok(page);
    }

    /**
     * 店铺详情
     *
     * @return
     */
    @GetMapping("info")
    @ApiOperation("店铺详情")
    @LogOperation("店铺详情")
    public Result<RegisterStoreInfoDTO> info(@RequestParam Long storeId) {

        RegisterStoreInfoDTO settingStoreDTO = storeService.storeInfoByUserId(null, storeId);

        return new Result<RegisterStoreInfoDTO>().ok(settingStoreDTO);
    }


    /**
     * 创建店铺
     */
    @PostMapping("save")
    @ApiOperation("创建店铺")
    @LogOperation("创建店铺")
    public Result createStore(@RequestBody AdminCreateStoreDTO adminCreateStoreDTO) {
        ValidatorUtils.validateEntity(adminCreateStoreDTO, AddGroup.class, DefaultGroup.class);
        // 校验店铺名称是否重复
        Integer count = storeService.findStoreName(adminCreateStoreDTO.getStoreName(), null);
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }

        storeService.adminCreateStore(adminCreateStoreDTO);

        return new Result().ok(null, "创建成功");
    }

    /**
     * 修改店铺信息
     */
    @PutMapping("update/store")
    @ApiOperation("修改店铺信息")
    @LogOperation("修改店铺信息")
    @LogContext(code = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS, note = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS_MESSAGE)
    public Result updateStore(@RequestBody UpdateRegisterStoreDTO updateRegisterStoreDTO) {
        ValidatorUtils.validateEntity(updateRegisterStoreDTO, UpdateGroup.class, DefaultGroup.class);
        // 校验店铺名称是否重复
        Integer count = storeService.findStoreName(updateRegisterStoreDTO.getStoreName(), updateRegisterStoreDTO.getId());
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }

        storeService.updateRegisterStore(updateRegisterStoreDTO, StoreEnum.ADMIN_TYPE.value());
        // 更新店铺索引
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(updateRegisterStoreDTO.getId()));
        // 同步商品ES
        goodsSyncIndexService.goodsIndexSyncByStoreId(updateRegisterStoreDTO.getId());
        //更新购物车
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_ID_CART_GOODS_STATUS, String.valueOf(updateRegisterStoreDTO.getId()));
        return new Result().ok(null, "修改成功");
    }


    /**
     * 店铺启用禁用
     *
     * @param storeId  店铺ID
     * @param isEnable 启用禁用   0 启用， 1 禁用
     * @return
     */
    @PutMapping("update/store/status")
    @ApiOperation("店铺启用禁用")
    @LogOperation("店铺启用禁用")
    @LogContext(code = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS, note = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS_MESSAGE)
    public Result updateStoreStatus(@RequestParam Long storeId, @RequestParam Integer isEnable) {
        storeService.updateStoreEnable(storeId, isEnable);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 根据ID 删除
     *
     * @param ids 店铺ID
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据ID 删除")
    @LogOperation("根据ID删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id不能为空");

        storeService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 导出数据
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("export")
    @ApiOperation("导出数据")
    @LogOperation("导出数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "店铺名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gradeId", value = "店铺等级", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeType", value = "店铺类型（1:自营商户，2:普通商户）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<ExportStoreDTO> list = storeService.exportList(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, StoreExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

    /**
     * 验证账号是否重复
     * account 账号
     */
    @GetMapping("verify")
    @ApiOperation("验证账号是否重复")
    @LogOperation("验证账号是否重复")
    public Result verify(@RequestParam String account) {
        int count = storeUserService.verify(account, null, null);
        if (count > 0) {
            return new Result().error("账号重复");
        }

        return new Result().ok(null, "可以使用");

    }

    /**
     * 验证店铺名称是否重复
     */
    @GetMapping("verify/name")
    @ApiOperation("验证店铺名称是否重复")
    @LogOperation("验证店铺名称是否重复")
    public Result verifyName(@RequestParam String storeName,
                             @RequestParam(value = "storeId", required = false) Long storeId) {
        Integer count = storeService.findStoreName(storeName, storeId);
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }

        return new Result().ok(null, "可以使用");
    }

    /**
     * 修改店铺密码
     */
    @PutMapping("update/password")
    @ApiOperation("修改店铺密码")
    @LogOperation("修改店铺密码")
    public Result updatePassword(@RequestBody UpdateLoginDTO updateLoginDTO) {
        // 获取用户信息
        SellerDetail sellerDetail = storeUserService.login(updateLoginDTO.getUsername());

        // 密码解密
        // 使用RSA私钥进行解密
        try {
            updateLoginDTO.setNewPassword(RSACoder.decryptByPrivateKey(updateLoginDTO.getNewPassword()));
            updateLoginDTO.setConfirmPassword(RSACoder.decryptByPrivateKey(updateLoginDTO.getConfirmPassword()));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", updateLoginDTO.getUsername(), e);
            return new Result().error("密码解析失败，请重新输入");
        }


        if (sellerDetail == null) {
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }

        // 验证新密码
        if (!updateLoginDTO.getNewPassword().equals(updateLoginDTO.getConfirmPassword())) {
            return new Result().error("两次输入密码不一致");
        }

        // 校验密码长度
        if (!updateLoginDTO.getNewPassword().matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            return new Result().error("密码必须为6-20位字符,数字,特殊字符");
        }

        sellerDetail.setPassword(PasswordUtils.encode(updateLoginDTO.getConfirmPassword()));
        StoreUserDTO storeUserDTO = ConvertUtils.sourceToTarget(sellerDetail, StoreUserDTO.class);
        storeUserService.update(storeUserDTO);


        return new Result().ok(null, "修改成功");

    }


    /**
     * 校验手机号邮箱是否注册
     *
     * @param mobileEmail
     * @param type        1 手机号 2 邮箱
     * @return
     */
    @GetMapping("verify/mobile")
    @ApiOperation("校验手机号邮箱是否注册")
    @LogOperation(value = "校验手机号邮箱是否注册")
    public Result verifyMobile(@RequestParam String mobileEmail, @RequestParam Integer type) {

        if (type == 1) {
            if (!MobileUtil.isMobile(mobileEmail)) {
                return new Result().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
            }
            Integer count = storeUserService.verifyMobile(mobileEmail, null);
            if (count > 0) {
                return new Result().error("该手机号已注册，请从新输入");
            }
        } else {
            // 校验邮箱
            Integer count = storeUserService.verifyEmail(mobileEmail, null);
            if (count > 0) {
                return new Result().error("邮箱已经注册");
            }
        }
        return new Result().ok(null, "可以使用");
    }
}