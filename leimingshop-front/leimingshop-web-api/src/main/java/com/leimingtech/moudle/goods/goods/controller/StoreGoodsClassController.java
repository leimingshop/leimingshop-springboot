/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goods.controller;


import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassListDTO;
import com.leimingtech.modules.enums.adv.AdvStateEnum;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import com.leimingtech.moudle.goods.goods.code.PcGoodsCode;
import com.leimingtech.moudle.goods.goods.vo.StoreGoodsClassListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 商品自定义分类表
 *
 * @author chenqgian
 * @since 1.0.0 2020-5-13
 */
@RestController
@RequestMapping("goods/store/class")
@Api(tags = "店铺商品分类管理")
public class StoreGoodsClassController {
    @Autowired
    private StoreGoodsClassService storeGoodsClassService;

    /**
     * 查询所有店铺商品分类带广告
     *
     * @return
     */
    @GetMapping("all")
    @ApiOperation("店铺商品分类")
    @LogContext(code = PcGoodsCode.STORE_GOODS_CLASS_CODE, note = PcGoodsCode.STORE_GOODS_CLASS_DESC)
    public Result<List<StoreGoodsClassListVO>> selectAllCustom(@RequestParam("storeId") Long storeId) {
        List<StoreGoodsClassListDTO> frontStoreGoodsClass = storeGoodsClassService.findFrontStoreGoodsClass(storeId, AdvStateEnum.TYPE.value());
        return new Result<List<StoreGoodsClassListVO>>().ok(ConvertUtils.sourceToTarget(frontStoreGoodsClass, StoreGoodsClassListVO.class));
    }

}