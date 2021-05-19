/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.address.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.enums.member.MemberAddressEnum;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.address.MemberAddressService;
import com.leimingtech.modules.utils.MobileUtil;
import com.leimingtech.moudle.member.address.code.AddressCode;
import com.leimingtech.moudle.member.address.vo.PcMemberAddressVo;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * pc端地址相关接口
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-05-18 11:43
 **/
@RestController
@RequestMapping("member/address")
@Api(tags = "会员地址")
public class AddressController {
    @Autowired
    private MemberAddressService memberAddressService;

    @GetMapping("page")
    @ApiOperation("pc会员地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @LogContext(code = AddressCode.ADDRESS_LIST_CODE, note = AddressCode.ADDRESS_LIST_DESC)
    public Result<PageData<MemberAddressDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        Long buyerId = null;
        try {
            // 获取用户信息
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<PageData<MemberAddressDTO>>().error("请先登录");
        }
        params.put("memberId", buyerId);
        PageData<MemberAddressDTO> dtoPageData = memberAddressService.page(params);

        return new Result<PageData<MemberAddressDTO>>().ok(dtoPageData, "查询成功");
    }

    @GetMapping("{id}")
    @ApiOperation("会员地址详情")
    @LogContext(code = AddressCode.ADDRESS_DETAIL_CODE, note = AddressCode.ADDRESS_DETAIL_DESC)
    public Result<PcMemberAddressVo> get(@PathVariable("id") Long id) {
        MemberAddressDTO memberAddressDTO = memberAddressService.get(id);

        return new Result<PcMemberAddressVo>().ok(ConvertUtils.sourceToTarget(memberAddressDTO, PcMemberAddressVo.class), "查询成功");
    }

    @PostMapping
    @ApiOperation("会员地址保存")
    @LogContext(code = AddressCode.ADDRESS_SAVE_CODE, note = AddressCode.ADDRESS_SAVE_DESC)
    public Result save(@RequestBody MemberAddressDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error("请先登录");
        }
        dto.setMemberId(memberId);

        if (null != dto.getMobPhone()) {
            if (StringUtils.isBlank(dto.getMobPhone()) || !MobileUtil.isMobile(dto.getMobPhone())) {
                return new Result<>().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
            }
        }

        memberAddressService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("会员地址修改")
    @LogContext(code = AddressCode.ADDRESS_EDIT_CODE, note = AddressCode.ADDRESS_EDIT_DESC)
    public Result update(@RequestBody MemberAddressDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error("请先登录");
        }

        if (null != dto.getMobPhone()) {
            if (StringUtils.isBlank(dto.getMobPhone()) || !MobileUtil.isMobile(dto.getMobPhone())) {
                return new Result<>().error(MemberErrorEnum.E_MOBILE_ERROR.code(), MemberErrorEnum.E_MOBILE_ERROR.value());
            }
        }

        //校验用户是否属于该用户
        MemberAddressDTO memberAddressDTO = memberAddressService.get(dto.getId());
        if (!memberId.equals(memberAddressDTO.getMemberId())) {
            return new Result().error("地址不存在");
        }
        memberAddressService.update(dto);
        //设为用户默认地址
        if (dto.getDefaultFlag() == MemberAddressEnum.IS_DEFAULT.value()) {
            memberAddressService.updateDefaultFlag(dto.getId(), MemberAddressEnum.IS_DEFAULT.value(), memberId);
        }
        return new Result().ok(null, "修改成功");
    }

    @DeleteMapping
    @ApiOperation("会员地址删除")
    @LogContext(code = AddressCode.ADDRESS_DELETE_CODE, note = AddressCode.ADDRESS_DELETE_DESC)
    public Result delete(@RequestParam("id") Long id) {

        //效验数据
        AssertUtils.isNull(id, "id不可为空");
        Long buyerId = null;
        try {
            // 获取用户信息
            buyerId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error("请先登录");
        }
        //校验用户是否属于该用户
        MemberAddressDTO addressDTO = memberAddressService.get(id);
        if (addressDTO == null) {
            return new Result().error("地址不存在");
        }
        if (!buyerId.equals(addressDTO.getMemberId())) {
            return new Result().error("地址不存在");
        }
        memberAddressService.deleteById(id);


        return new Result().ok(null, "删除成功");
    }

    @PutMapping("default")
    @ApiOperation("根据地址id设置用户默认地址")
    public Result updateDefaultAddress(@RequestParam("id") Long id) {

        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error("请先登录");
        }
        //校验用户是否属于该用户
        MemberAddressDTO memberAddressDTO = memberAddressService.get(id);
        if (!memberId.equals(memberAddressDTO.getMemberId())) {
            return new Result().error("地址不存在");
        }
        if (memberAddressDTO.getDefaultFlag() == MemberAddressEnum.IS_NOT_DEFAULT.value()) {
            memberAddressService.updateDefaultFlag(id, MemberAddressEnum.IS_DEFAULT.value(), memberId);
        }
        return new Result().ok(null, "修改成功");
    }

}
