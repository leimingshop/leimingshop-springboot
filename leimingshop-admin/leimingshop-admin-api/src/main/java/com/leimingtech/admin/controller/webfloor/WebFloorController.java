/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.webfloor;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.constants.webfloor.WebFloorResultCodeConstants;
import com.leimingtech.modules.dto.webfloor.InsertWebFloorAndLinkDTO;
import com.leimingtech.modules.dto.webfloor.UpdateWebFloorAndLinkDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorAndLinkDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorDTO;
import com.leimingtech.modules.enums.webfloor.IsShowEnum;
import com.leimingtech.modules.service.webfloor.WebFloorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * h5楼层设置
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Slf4j
@RestController
@RequestMapping("webfloor")
@Api(tags = "h5楼层管理")
public class WebFloorController {
    @Autowired
    private WebFloorService webFloorService;

    /**
     * 功能描述:
     * 〈楼层分页查询〉
     *
     * @param params 条件
     * @author : 刘远杰
     */
    @GetMapping("page")
    @ApiOperation("楼层管理分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "floorName", value = "楼层名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "floorType", value = "1 H5 楼层 2 PC楼层", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "isShow", value = "是否显示 0不显示 1显示", paramType = "query", dataType = "Long"),
    })
    public Result<PageData<WebFloorDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<WebFloorDTO> page = webFloorService.findShopWebFloorPagerList(params);

        return new Result<PageData<WebFloorDTO>>().ok(page, "查询楼层列表成功");
    }

    /**
     * 显示中的楼层
     *
     * @param floorType 1 h5 楼层 2 pc 楼层
     * @return
     */
    @GetMapping("list/enabled")
    @ApiOperation("显示中楼层列表")
    public Result<List<WebFloorDTO>> list(@RequestParam("floorType") Integer floorType) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("orderField", "sort");
        params.put("order", "desc");
        params.put("isShow", IsShowEnum.YES.value());
        params.put("floorType", floorType);
        List<WebFloorDTO> list = webFloorService.list(params);
        return new Result<List<WebFloorDTO>>().ok(list, "查询h5楼层成功");
    }


    /**
     * 功能描述:
     *
     * @param id 楼层id
     * @author : 刘远杰
     */
    @GetMapping("{id}")
    @ApiOperation("查询楼层及关联图片跳转链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "h5楼层id", required = true, paramType = "path", dataType = "Long")
    })
    public Result<WebFloorAndLinkDTO> get(@PathVariable("id") Long id) {

        // 根据id查询楼层及关联图片跳转链接
        WebFloorAndLinkDTO webFloorAndLinkDTO = webFloorService.findWebFloorLinkConfigById(id);

        if (webFloorAndLinkDTO == null) {
            return new Result<WebFloorAndLinkDTO>().error(WebFloorResultCodeConstants.ERR_INV_PARAMS, "查询楼层信息失败");
        } else {
            return new Result<WebFloorAndLinkDTO>().ok(webFloorAndLinkDTO, "查询楼层信息成功");
        }

    }

    /**
     * 功能描述:
     * 〈保存h5楼层设置〉
     *
     * @param dto
     * @author : 刘远杰
     */
    @PostMapping
    @ApiOperation("保存h5楼层设置")
    @LogOperation("保存h5楼层设置")
    public Result save(@Valid @RequestBody InsertWebFloorAndLinkDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        webFloorService.saveWebFloorAndLink(dto);
        return new Result<>().ok(null, "保存h5楼层设置成功");
    }

    @PutMapping
    @ApiOperation("修改h5楼层设置")
    @LogOperation("修改h5楼层设置")
    public Result update(@Valid @RequestBody UpdateWebFloorAndLinkDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        try {
            webFloorService.updateWebFloorAndLink(dto);
        } catch (Exception e) {
            log.error("修改楼层异常{}", e);
            return new Result().error("修改楼层失败");
        }
        return new Result<>().ok(null, "修改楼层成功");
    }

    @DeleteMapping()
    @ApiOperation("逻辑删除h5楼层设置")
    @LogOperation("逻辑删除h5楼层设置")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        try {
            webFloorService.deleteWebFloorAndLink(ids, null);
        } catch (Exception e) {
            log.error("删除楼层异常{}", e);
            return new Result().error("删除失败");
        }
        return new Result().ok(null, "删除楼层成功");
    }

}
