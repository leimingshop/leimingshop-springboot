/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.goods.check;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.goods.check.GoodsCheckDTO;
import com.leimingtech.modules.service.goods.check.GoodsCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品审核管理
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:53
 **/
@RestController
@RequestMapping("goods/check")
@Api(tags = "商品审核管理")
public class GoodsCheckController {
    @Autowired
    private GoodsCheckService goodsCheckService;

    @GetMapping("/list/{goodsId}")
    @ApiOperation("商品审核操作流水列表")
    public Result<List<GoodsCheckDTO>> page(@PathVariable("goodsId") Long goodsId) {

        List<GoodsCheckDTO> goodsCheckDTOS = goodsCheckService.selectByGoodsIdTocheck(goodsId);
        return new Result<List<GoodsCheckDTO>>().ok(goodsCheckDTOS, "查询成功");
    }

}
