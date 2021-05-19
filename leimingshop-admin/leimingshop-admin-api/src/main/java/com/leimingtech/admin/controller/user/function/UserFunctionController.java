/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.user.function;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.user.function.UserFunctionDTO;
import com.leimingtech.dto.user.function.UserFunctionInfoDTO;
import com.leimingtech.service.user.function.UserFunctionService;
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
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-16
 */
@RestController
@RequestMapping("userfunction")
@Api(tags = "admin首页权限配置")
public class UserFunctionController {

    @Autowired
    private UserFunctionService userFunctionService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<UserFunctionDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<UserFunctionDTO> page = userFunctionService.page(params);

        return new Result<PageData<UserFunctionDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<UserFunctionDTO> get(@PathVariable("id") Long id) {
        UserFunctionDTO data = userFunctionService.get(id);

        return new Result<UserFunctionDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody UserFunctionDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        userFunctionService.save(dto);

        return new Result();
    }

    @PostMapping("batch")
    @ApiOperation("批量保存用户首页权限")
    public Result saveFunctionAuthority(@ApiIgnore UserDetail userDetail, @RequestBody List<Long> menuIds) {

        userFunctionService.saveBatch(userDetail.getId(), menuIds);
        return new Result();
    }

    @PutMapping("batch")
    @ApiOperation("修改")
    public Result update(@RequestBody List<UserFunctionDTO> dtos) {
        //效验数据
        dtos.stream().forEach(dto -> {
            ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        });
        userFunctionService.updateBatch(dtos);
        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        userFunctionService.delete(ids);

        return new Result();
    }

    @GetMapping("info")
    @ApiOperation("获取用户配置的快捷方式")
    public Result<List<UserFunctionInfoDTO>> getUserFunctionByUserId(@PathVariable("userId") Long userId) {
        List<UserFunctionInfoDTO> userFunctionInfoDTOS = userFunctionService.getUserFunctionByUserId(userId);
        return new Result().ok(userFunctionInfoDTOS);
    }
//    @GetMapping("all/menu")
//    @ApiOperation("获取用户角色拥有的菜单权限")
//    public Result<List<UserFunctionInfoDTO>> getUserFunctionByUserId(@ApiIgnore UserDetail userDetail){
//        List<UserFunctionInfoDTO> userFunctionInfoDTOS = userFunctionService.getAllUserFunctionByUserId(userDetail);
//        return new Result().ok(userFunctionInfoDTOS);
//    }

//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<UserFunctionDTO> list = userFuctionService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, UserFuctionExcel.class);
//    }

}