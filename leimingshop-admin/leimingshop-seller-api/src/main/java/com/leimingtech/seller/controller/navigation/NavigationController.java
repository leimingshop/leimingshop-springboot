/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.navigation;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.navigation.AddNavigationDTO;
import com.leimingtech.modules.dto.navigation.NavigationDTO;
import com.leimingtech.modules.service.navigation.NavigationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 首页导航设置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-06-11
 */
@RestController
@RequestMapping("navigation")
@Api(tags = "首页导航设置")
public class NavigationController {

    @Autowired
    private NavigationService navigationService;

    /**
     * 导航分页
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<NavigationDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        params.put("storeId", sellerDetail.getStoreId().toString());
        PageData<NavigationDTO> page = navigationService.page(params);

        return new Result<PageData<NavigationDTO>>().ok(page);
    }

    /**
     * 导航详情
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<NavigationDTO> findById(@PathVariable("id") Long id) {
        NavigationDTO data = navigationService.get(id);

        return new Result<NavigationDTO>().ok(data);
    }

    /**
     * 保存导航
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody AddNavigationDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        dto.setStoreId(sellerDetail.getStoreId());
        navigationService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改导航配置
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody NavigationDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        navigationService.update(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 删除导航配置
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "请选择删除的导航");

        navigationService.delete(ids);

        return new Result().ok(null, "删除成功");
    }
}