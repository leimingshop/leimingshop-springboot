/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.goods.label;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.goodslable.GoodsLabelDTO;
import com.leimingtech.modules.service.goodslabel.GoodsLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: weixianchun
 * @Description: 商品标签管理
 * @Date :2019/5/20 14:14
 * @Version V1.0
 **/
@RestController
@RequestMapping("label")
@Api(tags = "商品标签管理")
public class GoodsLabelController {

    @Autowired
    private GoodsLabelService goodsLabelService;

    @GetMapping("list")
    @ApiOperation("标签列表查询")
    @ApiImplicitParam(name = "labelName", value = "标签名称", paramType = "query", dataType = "String")
    public Result<List<GoodsLabelDTO>> findList(@RequestParam(value = "labelName", required = false) String labelName) {
        Map<String, Object> params = new HashMap<>(16);

        // 查询标签列表
        params.put(Constant.ORDER_FIELD, "createDate");
        params.put(Constant.ORDER, "desc");
        if (StringUtils.isNotBlank(labelName)) {
            params.put("labelName", labelName);
        }
        List<GoodsLabelDTO> list = goodsLabelService.list(params);

        if (CollectionUtils.isNotEmpty(list)) {
            return new Result<List<GoodsLabelDTO>>().ok(list, "查询标签数据成功");
        } else {
            return new Result<List<GoodsLabelDTO>>().ok(null, "暂无标签");
        }
    }

}
