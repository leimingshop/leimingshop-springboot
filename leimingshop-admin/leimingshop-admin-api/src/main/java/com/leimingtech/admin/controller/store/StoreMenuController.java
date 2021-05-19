/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.store;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.menu.SaveStoreMenuDTO;
import com.leimingtech.modules.dto.menu.StoreMenuDTO;
import com.leimingtech.modules.service.menu.StoreMenuService;
import com.leimingtech.modules.service.resource.StoreResourceService;
import com.leimingtech.modules.service.usermanage.StoreUserManageService;
import com.leimingtech.modules.statuscode.StoreStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Data: 2019/6/24 17:13
 * @Author: chengqian
 * @Version: 1.0
 */
@RestController
@RequestMapping("store/menu")
@Api(tags = "商家端菜单管理")
public class StoreMenuController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(StoreMenuController.class);
    @Autowired
    private StoreUserManageService storeUserManageService;
    @Autowired
    private StoreMenuService storeMenuService;
    @Autowired
    private StoreResourceService storeResourceService;

    @GetMapping("find/menu")
    @ApiOperation("获取当前角色菜单")
    @LogOperation("获取当前角色菜单")
    public Result<List<StoreMenuDTOs>> getMenu(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<List<StoreMenuDTOs>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 查询当前角色菜单
        List<StoreMenuDTOs> menuDTOsList = storeUserManageService.findMenuByRoleId(sellerDetail.getRoleId());

        return new Result<List<StoreMenuDTOs>>().ok(menuDTOsList);
    }

    @GetMapping("permissions")
    @ApiOperation("权限标识")
    public Result<Set<String>> permissions(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getRoleId() == null) {
            return new Result<Set<String>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Set<String> strings = storeResourceService.getUserPermissions(sellerDetail.getRoleId());
        return new Result<Set<String>>().ok(strings);
    }

    /**
     * 修改菜单
     *
     * @param dto 参数
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜单")
    @LogOperation("修改菜单")
    public Result update(@RequestBody StoreMenuDTO dto) {
        storeMenuService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 保存菜单
     *
     * @param dto 参数
     * @return
     */
    @PostMapping
    @ApiOperation("保存菜单")
    @LogOperation("保存菜单")
    public Result save(@RequestBody SaveStoreMenuDTO dto) {
        StoreMenuDTO storeMenuDTO = ConvertUtils.sourceToTarget(dto, StoreMenuDTO.class);
        storeMenuService.save(storeMenuDTO);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 根据ID删除信息
     *
     * @param ids 主键ID
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据ID删除")
    @LogOperation("根据ID删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id不能为空");

        storeMenuService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 获取所有菜单
     *
     * @return
     */
    @GetMapping("find/all/menu")
    @ApiOperation("获取当前角色菜单")
    @LogOperation("获取当前角色菜单")
    public Result<List<StoreMenuDTOs>> getAllMenu() {

        // 查询当前角色菜单
        List<StoreMenuDTOs> menuDTOsList = storeUserManageService.findAllMenu();
        Map<String, String> logMap = new HashMap<>(16);
        if (menuDTOsList != null) {
            logMap.put("menuDTOsList", menuDTOsList.toString());
            mlogger.info(StoreStatusCode.SELLER_STORE_MENU_SUCCESS,
                    StoreStatusCode.SELLER_STORE_MENU_SUCCESS_MESSAGE, logMap);
        }

        return new Result<List<StoreMenuDTOs>>().ok(menuDTOsList);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<StoreMenuDTO> get(@PathVariable("id") String id) {
        StoreMenuDTO storeMenuDTO = storeMenuService.get(Long.valueOf(id));
        return new Result<StoreMenuDTO>().ok(storeMenuDTO);
    }


}
