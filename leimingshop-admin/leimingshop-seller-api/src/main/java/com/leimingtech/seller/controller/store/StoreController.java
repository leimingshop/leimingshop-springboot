/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.store;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.auth.SettingStoreAuthDTO;
import com.leimingtech.modules.dto.goods.GoodsLiveDTO;
import com.leimingtech.modules.dto.index.IndexDTO;
import com.leimingtech.modules.dto.order.OrderLiveDTO;
import com.leimingtech.modules.dto.store.*;
import com.leimingtech.modules.dto.user.UserInfoDTO;
import com.leimingtech.modules.enums.store.StoreEnum;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.role.StoreRoleService;
import com.leimingtech.modules.service.store.StoreAuditService;
import com.leimingtech.modules.service.store.StoreAuthAuditService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.statuscode.StoreStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Description
 * @Data: 2019/6/19 9:27
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("setting")
@Api(tags = "店铺设置")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    @Autowired
    private StoreAuditService storeAuditService;

    @Autowired
    private StoreAuthAuditService storeAuthAuditService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreRoleService storeRoleService;

    /**
     * 获取用户下所有店铺
     *
     * @return
     */
    @GetMapping("user/all")
    @ApiOperation("获取用户下所有店铺")
    @LogOperation("获取用户下所有店铺")
    public Result<List<UserAllStoreDTO>> allStore(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<List<UserAllStoreDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        List<UserAllStoreDTO> byUserId = storeService.findByUserId(sellerDetail.getId());

        return new Result<List<UserAllStoreDTO>>().ok(byUserId);
    }

    /**
     * 修改店铺信息
     */
    @PutMapping("update")
    @ApiOperation("修改店铺信息")
    @LogOperation("修改店铺信息")
    @LogContext(code = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS, note = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS_MESSAGE)
    public Result updateStore(@RequestBody SettingStoreAuthDTO settingStoreAuthDTO) {
        ValidatorUtils.validateEntity(settingStoreAuthDTO, AddGroup.class, DefaultGroup.class);
        // 校验店铺名称是否重复
        Integer count = storeService.findStoreName(settingStoreAuthDTO.getStoreName(), settingStoreAuthDTO.getStoreId());
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }

        storeService.updateSetting(settingStoreAuthDTO);
        goodsSyncIndexService.goodsIndexSyncByStoreId(settingStoreAuthDTO.getStoreId());
        return new Result().ok(null, "修改成功");
    }

    /**
     * 根据用户ID 获取用户和店铺管理信息
     */
    @GetMapping("find/store")
    @ApiOperation("获取用户和店铺管理信息")
    @LogOperation("获取用户和店铺管理信息")
    public Result<UserInfoDTO> findStore(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result<UserInfoDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        UserInfoDTO userInfoDTO = ConvertUtils.sourceToTarget(sellerDetail, UserInfoDTO.class);
        if (sellerDetail.getStoreId() != null) {
            StoreDTO storeDTO = storeService.get(sellerDetail.getStoreId());
            userInfoDTO.setLogo(storeDTO.getStoreLogo());
//            userInfoDTO.setQrCode(storeDTO.getQrCode());
            userInfoDTO.setStoreId(sellerDetail.getStoreId());
            Integer count = storeRoleService.getIndexMenu(sellerDetail.getRoleId());
            if (sellerDetail.getSuperAdmin() == 1 || count > 0) {
                userInfoDTO.setIndexMenu("yes");
            }
        }
        return new Result<UserInfoDTO>().ok(userInfoDTO);
    }

    /**
     * 创建店铺
     */
    @PostMapping("save")
    @ApiOperation("创建店铺")
    @LogOperation("创建店铺")
    public Result createStore(@RequestBody CreateStoreDTO createStoreDTO, @ApiIgnore SellerDetail sellerDetail) {
        ValidatorUtils.validateEntity(createStoreDTO, AddGroup.class, DefaultGroup.class);
        // 校验店铺名称是否重复
        Integer count = storeService.findStoreName(createStoreDTO.getStoreName(), null);
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }
        createStoreDTO.setUserId(sellerDetail.getId());
        storeService.createStore(createStoreDTO, null);
        return new Result().ok(null, "创建成功");
    }

    /**
     * 店铺详情
     *
     * @return
     */
    @GetMapping("info")
    @ApiOperation("店铺详情")
    @LogOperation("店铺详情")
    public Result<RegisterStoreInfoDTO> info(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<RegisterStoreInfoDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        RegisterStoreInfoDTO settingStoreDTO = storeService.storeInfoByUserId(sellerDetail.getId(), null);

        return new Result<RegisterStoreInfoDTO>().ok(settingStoreDTO);
    }

    /**
     * 修改入住信息
     */
    @PutMapping("update/store")
    @ApiOperation("修改入住信息")
    @LogOperation("修改入住信息")
    @LogContext(code = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS, note = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS_MESSAGE)
    public Result updateStore(@RequestBody UpdateRegisterStoreDTO updateRegisterStoreDTO) {
        ValidatorUtils.validateEntity(updateRegisterStoreDTO, UpdateGroup.class, DefaultGroup.class);
        // 校验店铺名称是否重复
        Integer count = storeService.findStoreName(updateRegisterStoreDTO.getStoreName(), updateRegisterStoreDTO.getId());
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }

        storeService.updateRegisterStore(updateRegisterStoreDTO, null);
        return new Result().ok(null, "修改成功");
    }

    /**
     * 提交审核
     */
    @PutMapping("update/status")
    @ApiOperation("提交审核")
    @LogOperation("提交审核")
    public Result updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getAccount() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        updateStoreStatusDTO.setOperator(sellerDetail.getAccount());
        storeService.updateStoreStatus(updateStoreStatusDTO);
        return new Result().ok(null, "提交成功");
    }

    /**
     * 修改店铺基础信息
     */
    @PutMapping("update/info")
    @ApiOperation("修改店铺基础信息")
    @LogOperation("修改店铺基础信息")
    @LogContext(code = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS, note = StoreStatusCode.SELLER_STORE_UPDATE_SUCCESS_MESSAGE)
    public Result updateStoreInfo(@RequestBody UpdateStoreBasicDTO updateRegisterStoreDTO) {
        ValidatorUtils.validateEntity(updateRegisterStoreDTO, UpdateGroup.class, DefaultGroup.class);
        // 校验店铺名称是否重复
        Integer count = storeService.findStoreName(updateRegisterStoreDTO.getStoreName(), updateRegisterStoreDTO.getStoreId());
        if (count > 0) {
            return new Result().error("店铺名称已经存在");
        }
        storeAuditService.save(updateRegisterStoreDTO);

        return new Result().ok(null, "提交成功");
    }

    /**
     * 修改店铺公司信息
     */
    @PutMapping("update/auth")
    @ApiOperation("修改店铺公司信息")
    @LogOperation("修改店铺公司信息")
    public Result updateStoreCompany(@RequestBody UpdateStoreCompanyDTO updateRegisterStoreDTO) {
        ValidatorUtils.validateEntity(updateRegisterStoreDTO, UpdateGroup.class, DefaultGroup.class);
        StoreAuthAuditDTO storeAuthAuditDTO = ConvertUtils.sourceToTarget(updateRegisterStoreDTO, StoreAuthAuditDTO.class);

        storeAuthAuditDTO.setAuthAuditStatus(StoreEnum.AUDIT_STATUS.value());
        storeAuthAuditService.save(storeAuthAuditDTO);

        return new Result().ok(null, "提交成功");
    }

    /**
     * 店铺基础信息回显
     *
     * @param sellerDetail
     * @return
     */
    @GetMapping("basic/info")
    @ApiOperation("店铺基础信息回显")
    @LogOperation("店铺基础信息回显")
    public Result<StoreAuditDTO> basicInfo(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<StoreAuditDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        StoreAuditDTO storeAuditDTO = storeAuditService.getInfoByStoreId(sellerDetail.getStoreId(), null);

        return new Result().ok(storeAuditDTO);
    }

    /**
     * 店铺公司信息回显
     *
     * @param sellerDetail
     * @return
     */
    @GetMapping("company/info")
    @ApiOperation("店铺公司信息回显")
    @LogOperation("店铺公司信息回显")
    public Result<StoreAuthAuditDTO> companyInfo(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<StoreAuthAuditDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        StoreAuthAuditDTO storeAuditDTO = storeAuthAuditService.getInfoByStoreId(sellerDetail.getStoreId(), null);

        return new Result().ok(storeAuditDTO);
    }

    /**
     * 店铺列表
     *
     * @param sellerDetail
     * @return
     */
    @GetMapping("store/list")
    @ApiOperation("店铺列表")
    @LogOperation("店铺列表")
    public Result<PageData<StoreDTO>> storeList(@ApiIgnore SellerDetail sellerDetail) {
        PageData<StoreDTO> storeDTOS = storeService.storeList(sellerDetail.getId());
        return new Result<PageData<StoreDTO>>().ok(storeDTOS);
    }

    /**
     * 店铺首页
     *
     * @return
     */
    @GetMapping("index")
    @ApiOperation("店铺首页")
    @LogOperation("店铺首页")
    public Result<IndexDTO> storeIndex(@ApiIgnore SellerDetail sellerDetail) {
        IndexDTO index = storeService.index(sellerDetail);

        return new Result<IndexDTO>().ok(index);

    }

    /**
     * 查询商品实况
     *
     * @param type         是否刷新页面 1 刷新
     * @param sellerDetail
     * @return
     */
    @GetMapping("goods")
    @ApiOperation("商品实况")
    @LogOperation("商品实况")
    public Result<GoodsLiveDTO> goodsLive(@RequestParam(value = "type", required = false) Integer type, @ApiIgnore SellerDetail sellerDetail) {
        GoodsLiveDTO goodsLiveDTO = goodsService.goodsLive(sellerDetail.getStoreId(), type);

        return new Result<GoodsLiveDTO>().ok(goodsLiveDTO);

    }

    /**
     * 订单实况
     *
     * @param type         是否刷新页面 1 刷新
     * @param sellerDetail
     * @return
     */
    @GetMapping("orderlive")
    @ApiOperation("订单实况")
    @LogOperation("订单实况")
    public Result<OrderLiveDTO> orderLive(@RequestParam(value = "type", required = false) Integer type, @ApiIgnore SellerDetail sellerDetail) {
        OrderLiveDTO orderLiveDTO = orderService.orderLive(sellerDetail.getStoreId(), type);

        return new Result<OrderLiveDTO>().ok(orderLiveDTO);

    }
}
