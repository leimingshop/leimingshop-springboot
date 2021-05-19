/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.mobile.indexmenu;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.mobile.indexmenu.MobileIndexMenuResultConstant;
import com.leimingtech.modules.dto.mobile.indexmenu.InsertMobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.MobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.UpdateMobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.UpdateShowDTO;
import com.leimingtech.modules.enums.mobile.indexmenu.LinkTypeEnum;
import com.leimingtech.modules.service.mobile.indexmenu.MobileIndexMenuService;
import com.leimingtech.modules.statuscode.OperationStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 移动首页菜单
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */
@RestController
@RequestMapping("mobile/indexmenu")
@Api(tags = "移动首页菜单管理")
public class MobileIndexMenuController {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(MobileIndexMenuController.class);

    @Autowired
    private MobileIndexMenuService mobileIndexMenuService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "menuName", value = "移动首页菜单名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "showFlag", value = "是否显示 0不显示 1显示", paramType = "query", dataType = "int")
    })
    @LogContext(code = "")
    public Result<PageData<MobileIndexMenuDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MobileIndexMenuDTO> page = mobileIndexMenuService.page(params);

        Map<String, String> logMap = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            logMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        if (CollectionUtils.isNotEmpty(page.getList())) {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_PAGE_SUCCESS_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_PAGE_SUCCESS_MESSAGE, logMap);
            return new Result<PageData<MobileIndexMenuDTO>>().ok(page, "查询移动首页菜单列表成功");
        } else {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_PAGE_RESPONSE_VALIDATION_ERROR_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_PAGE_RESPONSE_VALIDATION_ERROR_MESSAGE, logMap);
            return new Result<PageData<MobileIndexMenuDTO>>().ok(page, "暂无数据");
        }


    }

    @GetMapping("{id}")
    @ApiOperation("查询移动首页菜单详情")
    public Result<MobileIndexMenuDTO> get(@PathVariable("id") Long id) {
        MobileIndexMenuDTO data = mobileIndexMenuService.get(id);

        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("id", id.toString());

        if (data != null) {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_DETAIL_SUCCESS_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_DETAIL_SUCCESS_CODE, logMap);
            return new Result<MobileIndexMenuDTO>().ok(data, "查询移动首页菜单详情成功");
        } else {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_DETAIL_RESPONSE_VALIDATION_ERROR_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_DETAIL_RESPONSE_VALIDATION_ERROR_MESSAGE, logMap);
            return new Result<MobileIndexMenuDTO>().ok(data, "移动首页菜单不存在");
        }

    }

    @PostMapping
    @ApiOperation("保存移动首页菜单")
    @LogOperation("保存移动首页菜单")
    public Result save(@RequestBody InsertMobileIndexMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("dto", dto.toString());
        // 保存url类型首页菜单，校验url是否为空,类型
        if (StringUtils.equals(LinkTypeEnum.URL.value(), dto.getLinkType())
                && StringUtils.isBlank(dto.getUrl())) {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_SAVE_CLIENT_VALIDATION_ERROR_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_SAVE_CLIENT_VALIDATION_ERROR_MESSAGE, logMap);
            return new Result().error(MobileIndexMenuResultConstant.ERR_INV_PARAMS, "url不能为空");

        }

        Map<String, Object> map = mobileIndexMenuService.saveMobileIndexMenu(dto);

        mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_SAVE_SUCCESS_CODE,
                OperationStatusCode.MOBILE_INDEX_MENU_SAVE_SUCCESS_MESSAGE, logMap);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @PutMapping
    @ApiOperation("修改移动首页菜单")
    @LogOperation("修改移动首页菜单")
    public Result update(@RequestBody UpdateMobileIndexMenuDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("dto", dto.toString());
        // 保存url类型首页菜单，校验url是否为空
        if (StringUtils.equals(LinkTypeEnum.URL.value(), dto.getLinkType()) && StringUtils.isBlank(dto.getUrl())) {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_UPDATE_CLIENT_VALIDATION_ERROR_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_UPDATE_CLIENT_VALIDATION_ERROR_MESSAGE, logMap);
            return new Result().error(MobileIndexMenuResultConstant.ERR_INV_PARAMS, "url不能为空");

        }

        Map<String, Object> map = mobileIndexMenuService.updateMobileIndexMenu(dto);

        mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_UPDATE_SUCCESS_CODE,
                OperationStatusCode.MOBILE_INDEX_MENU_UPDATE_SUCCESS_MESSAGE, logMap);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));

    }

    @PutMapping("is/show")
    @ApiOperation("启用禁用")
    @LogOperation("启用禁用")
    public Result isShow(@RequestBody UpdateShowDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        mobileIndexMenuService.isShow(dto);

        return new Result().ok(null, "操作成功");
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除移动首页菜单")
    @LogOperation("删除移动首页菜单")
    public Result delete(@PathVariable("id") Long id) {

        int count = mobileIndexMenuService.deleteById(id);

        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("id", id.toString());

        if (count > 0) {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_DELETE_SUCCESS_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_DELETE_SUCCESS_MESSAGE, logMap);
            return new Result<>().ok(null, "删除成功");
        } else {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_DELETE_RESPONSE_VALIDATION_ERROR_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_DELETE_RESPONSE_VALIDATION_ERROR_MESSAGE, logMap);
            return new Result<>().error(MobileIndexMenuResultConstant.ERR_INV_PARAMS, "删除失败");
        }

    }

    @DeleteMapping("batch")
    @ApiOperation("批量删除移动首页菜单")
    @LogOperation("批量删除移动首页菜单")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("ids", Arrays.toString(ids));

        try {
            mobileIndexMenuService.delete(ids);
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_BATCH_DELETE_SUCCESS_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_BATCH_DELETE_SUCCESS_MESSAGE, logMap);
            return new Result<>().ok(null, "删除成功");
        } catch (Exception e) {
            mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_BATCH_DELETE_SERVER_INTERNAL_ERROR_CODE,
                    OperationStatusCode.MOBILE_INDEX_MENU_BATCH_DELETE_SERVER_INTERNAL_ERROR_MESSAGE, logMap, e);
            return new Result().error(MobileIndexMenuResultConstant.ERR_BADCODE, "删除失败");
        }

    }

    @GetMapping("check/name/add")
    @ApiOperation("新增移动首页菜单校验规格菜单名称重复性")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "menuName", value = "移动首页菜单名称", required = true, paramType = "query", dataType = "String")
    )
    public Result checkMenuNameWhenAdd(@RequestParam("menuName") String menuName) {
        Map<String, Object> map = mobileIndexMenuService.checkMenuNameWhenAdd(menuName);

        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("menuName", menuName);
        mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_CHECK_NAME_SAVE_SUCCESS_CODE,
                OperationStatusCode.MOBILE_INDEX_MENU_CHECK_NAME_SAVE_SUCCESS_MESSAGE, logMap);

        return new Result<>().error((int) map.get("code"), (String) map.get("message"));

    }

    @GetMapping("check/name/update")
    @ApiOperation("修改移动首页菜单校验规格菜单名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuName", value = "移动首页菜单名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "移动首页菜单id", required = true, paramType = "query", dataType = "Long")
    })
    public Result checkMenuNameWhenUpdate(@RequestParam("menuName") String menuName, @RequestParam("id") Long id) {
        Map<String, Object> map = mobileIndexMenuService.checkMenuNameWhenUpdate(menuName, id);

        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("menuName", menuName);
        mlogger.info(OperationStatusCode.MOBILE_INDEX_MENU_CHECK_NAME_UPDATE_SUCCESS_CODE,
                OperationStatusCode.MOBILE_INDEX_MENU_CHECK_NAME_SAVE_SUCCESS_MESSAGE, logMap);

        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

}
