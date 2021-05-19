/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.activity;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.activity.goods.AddSeckillGoodsPageDTO;
import com.leimingtech.modules.dto.activity.goods.AddSeckillSkuListDTO;
import com.leimingtech.modules.dto.activity.goods.AdminSeckillGoodsDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 活动商品操作ＡＰＩ
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */
@RestController
@RequestMapping("activity/goods")
@Api(tags = "活动商品操作API ")
public class ActivityGoodsController {


    @Autowired
    private ActivityGoodsService activityGoodsService;


    @GetMapping("flash/sale/already")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "query", dataType = "long")
    })
    @ApiOperation("已选择活动商品列表查询")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_GOODS_LIST_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_GOODS_LIST_SUCCESS_MESSAGE)
    public Result<PageData<AdminSeckillGoodsDTO>> flashSaleGoodsPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AdminSeckillGoodsDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());
        params.put("activityType", ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
        // 活动已选择商品列表查询
        PageData<AdminSeckillGoodsDTO> page = activityGoodsService.alreadyActivityGoodsList(params);
        return new Result<PageData<AdminSeckillGoodsDTO>>().ok(page, "查询成功");
    }

    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsShow", value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "gcId", value = "三级分类id", paramType = "query", dataType = "long")
    })
    @ApiOperation("未选择活动商品分页查询")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ADD_GOODS_PAGE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ADD_GOODS_PAGE_SUCCESS_MESSAGE)
    public Result<PageData<AddSeckillGoodsPageDTO>> canAddActiveGoods(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AddSeckillGoodsPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());

        // 查询商品分页
        PageData<AddSeckillGoodsPageDTO> page = activityGoodsService.canAddActiveGoods(params);
        return new Result<PageData<AddSeckillGoodsPageDTO>>().ok(page, "查询成功");
    }

    @GetMapping("goods/sku/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "goodsId", value = "商品spu id", required = true, paramType = "query", dataType = "long")
    })
    @ApiOperation("活动商品sku数据查询")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_SKU_LIST_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_SKU_LIST_SUCCESS_MESSAGE)
    public Result<List<AddSeckillSkuListDTO>> getAddSeckillSkuList(@RequestParam("activityId") Long activityId,
                                                                   @RequestParam("goodsId") Long goodsId,
                                                                   @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AddSeckillSkuListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询活动sku列表
        List<AddSeckillSkuListDTO> addSeckillSkuList = activityGoodsService.getAddSeckillSkuList(
                activityId, goodsId, storeId);
        return new Result<List<AddSeckillSkuListDTO>>().ok(addSeckillSkuList, "查询成功");
    }
}
