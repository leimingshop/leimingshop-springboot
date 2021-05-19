/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.spec;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.spec.SpecAndSpecValueDTO;
import com.leimingtech.modules.dto.spec.SpecDTO;
import com.leimingtech.modules.service.spec.SpecService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规格管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Slf4j
@RestController
@RequestMapping("spec")
@Api(tags = "规格管理")
public class SpecController {
    @Autowired
    private SpecService specService;

    @GetMapping("page")
    @ApiOperation("规格分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "specName", value = "规格名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupStatus", value = "规格分组状态（默认1:启用,2:禁用）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupName", value = "规格分组名称", paramType = "query", dataType = "String")
    })
    public Result<PageData<SpecDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SpecDTO> page = specService.findSpecPageList(params);

        if (CollectionUtils.isNotEmpty(page.getList())) {
            return new Result<PageData<SpecDTO>>().ok(page, "查询规格数据成功");
        } else {
            return new Result<PageData<SpecDTO>>().ok(null, "暂无规格");
        }
    }

    @GetMapping("list")
    @ApiOperation("规格列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specName", value = "规格名称", paramType = "query", dataType = "String")
    })
    public Result<List<SpecDTO>> findSpecList(@RequestParam(value = "specName", required = false) String specName) {
        Map<String, Object> params = new HashMap<>(16);

        // 查询规格
        params.put(Constant.ORDER_FIELD, "spec_sort");
        params.put(Constant.ORDER, "desc");
        if (StringUtils.isNotBlank(specName)) {
            params.put("specName", specName);
        }
        List<SpecDTO> specList = specService.findSpecList(params);

        if (CollectionUtils.isNotEmpty(specList)) {
            return new Result<List<SpecDTO>>().ok(specList, "查询规格数据成功");
        } else {
            return new Result<List<SpecDTO>>().ok(null, "暂无规格");
        }


    }

    @GetMapping("{id}")
    @ApiOperation("查询规格详情")
    public Result<SpecAndSpecValueDTO> get(@PathVariable("id") Long id) {
        // 查询规格及规格值
        SpecAndSpecValueDTO specAndSpecValueDTO = specService.findSpecAndSpecValueBySpecId(id);
        if (specAndSpecValueDTO == null) {
            return new Result<SpecAndSpecValueDTO>().ok(null, "暂无数据");
        } else {
            return new Result<SpecAndSpecValueDTO>().ok(specAndSpecValueDTO, "查询规格详情成功");
        }

    }

    @GetMapping("check/name/add")
    @ApiOperation("新增规格校验规格名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specName", value = "规格名称", required = true, paramType = "query", dataType = "String")
    })

    public Result checkSpecNameWhenAdd(@RequestParam("specName") String specName) {
        Map<String, Object> map = specService.checkSpecNameWhenAdd(specName);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

    @GetMapping("check/name/update")
    @ApiOperation("修改规格校验规格名称重复性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specName", value = "规格名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "规格id", required = true, paramType = "query", dataType = "Long")
    })
    public Result checkSpecName(@RequestParam("specName") String specName,
                                @RequestParam("id") Long id) {
        Map<String, Object> map = specService.checkSpecNameWhenUpdate(specName, id);
        return new Result<>().error((int) map.get("code"), (String) map.get("message"));
    }

}
