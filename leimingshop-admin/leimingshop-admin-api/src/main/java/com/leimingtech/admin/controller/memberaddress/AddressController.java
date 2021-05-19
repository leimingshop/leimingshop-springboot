/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.memberaddress;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.service.address.MemberAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 会员地址表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@RestController
@RequestMapping("address")
@Api(tags = "会员地址表")
public class AddressController {
    @Autowired
    private MemberAddressService memberAddressService;

    @GetMapping("page")
    @ApiOperation("会员地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "会员id", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<MemberAddressDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MemberAddressDTO> page = memberAddressService.page(params);

        return new Result<PageData<MemberAddressDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("会员地址详情")
    public Result<MemberAddressDTO> get(@PathVariable("id") Long id) {
        MemberAddressDTO data = memberAddressService.get(id);

        return new Result<MemberAddressDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("会员地址保存")
    @LogOperation("会员地址保存")
    public Result save(@RequestBody MemberAddressDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        memberAddressService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("会员地址修改")
    @LogOperation("会员地址修改")
    public Result update(@RequestBody MemberAddressDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        memberAddressService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    @DeleteMapping
    @ApiOperation("会员地址删除")
    @LogOperation("会员地址删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        // LX 17839193044@162.com 使用mybatisplus 逻辑删除  单个删除 .deleteById(id) 集合删除.delete(id)  并移除pictureService中的deleteBatchByIds方法
        memberAddressService.logicDelete(ids);

        return new Result().ok(null, "删除成功");
    }

    @PutMapping("default/flag")
    @ApiOperation("会员地址默认状态修改")
    @LogOperation("会员地址默认状态修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址主键", paramType = "query",
                    required = true, dataType = "Long"),
            @ApiImplicitParam(name = "defaultFlag", value = "是否默认（ 默认为0:非默认，1:已默认）",
                    paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "会员id", paramType = "query",
                    required = true, dataType = "Long")
    })
    public Result updateDefaultFlag(@ApiIgnore @RequestParam Map<String, Object> params) {

        Long id = Long.valueOf(params.get("id").toString());
        Integer defaultFlag = Integer.valueOf(params.get("defaultFlag").toString());
        Long memberId = Long.valueOf(params.get("memberId").toString());
        memberAddressService.updateDefaultFlag(id, defaultFlag, memberId);
        return new Result().ok(null, "修改成功");
    }
}