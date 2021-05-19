/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.reason;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.reason.ReasonDescriptionDTO;
import com.leimingtech.dto.reason.ReasonDescriptionSaveDTO;
import com.leimingtech.service.reason.ReasonDescriptionService;
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
 * 原因描述表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-06-10
 */
@RestController
@RequestMapping("reasondescription")
@Api(tags = "原因描述")
public class ReasonDescriptionController {
    @Autowired
    private ReasonDescriptionService reasonDescriptionService;

    @GetMapping("page")
    @ApiOperation("获取原因描述信息分页")
    @LogOperation("获取原因描述信息分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（0：退货，1：换货，2：申请退款 ，3：取消订单）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "content", value = "描述内容", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "role", value = "适用角色（0：会员，1：商家，2：平台）", paramType = "query", dataType = "String"),
    })
    public Result<PageData<ReasonDescriptionDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<ReasonDescriptionDTO> page = reasonDescriptionService.page(params);

        return new Result<PageData<ReasonDescriptionDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("根据id获取原因描述信息")
    @LogOperation("根据id获取原因描述信息")
    public Result<ReasonDescriptionDTO> get(@PathVariable("id") Long id) {
        ReasonDescriptionDTO data = reasonDescriptionService.get(id);

        return new Result<ReasonDescriptionDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存原因描述信息")
    @LogOperation("保存原因描述信息")
    public Result save(@RequestBody ReasonDescriptionSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        reasonDescriptionService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改原因描述信息")
    @LogOperation("修改原因描述信息")
    public Result update(@RequestBody ReasonDescriptionDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        reasonDescriptionService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除原因描述信息")
    @LogOperation("删除原因描述信息")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        reasonDescriptionService.delete(ids);

        return new Result();
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有支付信息
     * @Date :2019/6/3 16:38
     * @Param params
     * @Version V1.0
     **/
    @GetMapping("list")
    @ApiOperation("查询所有原因描述信息")
    @LogOperation("查询所有原因描述信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（0：退货，1：换货，2：申请退款 ，3：取消订单）", required = true, paramType = "query", dataType = "String"),
    })
    public Result<List<ReasonDescriptionDTO>> test(@ApiIgnore @RequestParam Map<String, Object> params) {

        List<ReasonDescriptionDTO> list = reasonDescriptionService.list(params);

        return new Result<List<ReasonDescriptionDTO>>().ok(list, "查询成功");
    }


}
