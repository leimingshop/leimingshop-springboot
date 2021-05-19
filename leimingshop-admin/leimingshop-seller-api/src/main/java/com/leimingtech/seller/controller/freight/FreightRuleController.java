/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.freight;

import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.order.freight.EditFreightRuleDTO;
import com.leimingtech.modules.dto.order.freight.FreightRuleDTO;
import com.leimingtech.modules.service.order.freight.FreightRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <运费规则管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/4/21
 */
@RestController
@RequestMapping("freight/rule")
@Api(tags = "运费规则管理")
public class FreightRuleController {

//    private Long storeId = 1196266068939710466L;

    @Autowired
    private FreightRuleService freightRuleService;

    @GetMapping
    @ApiOperation("查询运费规则")
    public Result<FreightRuleDTO> adminDetail(@ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<FreightRuleDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        FreightRuleDTO storeFreightRule = freightRuleService.getStoreFreightRule(storeId);

        return new Result<FreightRuleDTO>().ok(storeFreightRule, "查询成功");

    }

    @PutMapping("edit")
    @ApiOperation("修改运费模板")
    public Result save(@RequestBody EditFreightRuleDTO editDto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 模板数据校验
        ValidatorUtils.validateEntity(editDto, UpdateGroup.class);

        FreightRuleDTO freightRuleDTO = new FreightRuleDTO();
        freightRuleDTO.setStoreId(storeId);
        freightRuleDTO.setRuleType(editDto.getRuleType());

        freightRuleService.updateFreightRule(freightRuleDTO);

        return new Result().ok(null, "修改成功");
    }

}
