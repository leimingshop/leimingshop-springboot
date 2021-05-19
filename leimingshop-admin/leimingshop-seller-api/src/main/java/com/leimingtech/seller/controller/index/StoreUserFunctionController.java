/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.index;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import com.leimingtech.modules.service.index.StoreUserFunctionService;
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
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-03-16
 */
@RestController
@RequestMapping("storeuserfunction")
@Api(tags = "首页菜单按钮控制")
public class StoreUserFunctionController {

    @Autowired
    private StoreUserFunctionService storeUserFunctionService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<StoreUserFunctionDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<StoreUserFunctionDTO> page = storeUserFunctionService.page(params);

        return new Result<PageData<StoreUserFunctionDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<StoreUserFunctionDTO> get(@PathVariable("id") Long id) {
        StoreUserFunctionDTO data = storeUserFunctionService.get(id);

        return new Result<StoreUserFunctionDTO>().ok(data);
    }

    /**
     * 保存首页店铺按钮设置
     *
     * @param
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public Result save(@ApiIgnore SellerDetail userDetail, @RequestBody List<Long> menuIds) {


        storeUserFunctionService.saveBatch(userDetail.getId(), menuIds);

        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody StoreUserFunctionDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        storeUserFunctionService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        storeUserFunctionService.delete(ids);

        return new Result();
    }

}