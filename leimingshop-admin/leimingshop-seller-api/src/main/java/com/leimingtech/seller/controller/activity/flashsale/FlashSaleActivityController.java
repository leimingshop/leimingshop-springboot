/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.activity.flashsale;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.activity.goods.AddSeckillSkuListDTO;
import com.leimingtech.modules.dto.activity.goods.DeleteActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.InsertActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.InsertSeckillSkuDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityEditDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityPageDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivitySaveDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.flashsale.FlashSaleActivityService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
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
 * 限时抢购活动操作API接口
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */
@RestController
@RequestMapping("flash/sale")
@Api(tags = "限时抢购活动操作API接口")
public class FlashSaleActivityController {

    @Autowired
    private FlashSaleActivityService flashSaleActivityService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityName", value = "秒杀活动名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "activityState", value = "活动状态 0未开始 1进行中 2已结束", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "activityStartDate", value = "活动开始日期", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "activityEndDate", value = "活动结束日期", paramType = "query", dataType = "Date")
    })
    public Result<PageData<FlashSaleActivityPageDTO>> page(@ApiIgnore SellerDetail sellerDetail, @ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<FlashSaleActivityPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());
        PageData<FlashSaleActivityPageDTO> pageData = flashSaleActivityService.managePage(params);

        return new Result<PageData<FlashSaleActivityPageDTO>>().ok(pageData);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<FlashSaleActivityDTO> get(@ApiIgnore SellerDetail sellerDetail, @PathVariable("id") Long id) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<FlashSaleActivityDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        FlashSaleActivityDTO data = flashSaleActivityService.get(id);

        return new Result<FlashSaleActivityDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@ApiIgnore SellerDetail sellerDetail, @RequestBody FlashSaleActivitySaveDTO dto) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        dto.setStoreId(storeId);
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        dto.setId(IdGenerator.defaultSnowflakeId());
        flashSaleActivityService.saveFlashSaleActivity(dto);

        return new Result().ok(dto, "保存活动成功");
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@ApiIgnore SellerDetail sellerDetail, @RequestBody FlashSaleActivityEditDTO dto) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        dto.setStoreId(storeId);

        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        flashSaleActivityService.updateFlashSaleActivity(dto);

        return new Result().ok(null, "修改活动成功");
    }

    @PutMapping("/stop/{id}")
    @ApiOperation("停止限时抢购活动")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_STOP_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_STOP_SUCCESS_MESSAGE)
    public Result stop(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询活动状态
        FlashSaleActivityDTO activityDTO = flashSaleActivityService.getByIdOrStoreId(id, storeId);
        if (activityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动不存在");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() != activityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动不是开始状态");
        }

        // 删除活动
        Boolean flag = flashSaleActivityService.stop(id, storeId);
        if (flag) {
            return new Result<>().ok(null, "停止成功");
        } else {
            return new Result<>().error("停止失败");
        }
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@ApiIgnore SellerDetail sellerDetail, @RequestBody Long[] ids) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        Long storeId = sellerDetail.getStoreId();
        // 查询秒杀活动状态
        for (Long id : ids) {
            FlashSaleActivityDTO activityDTO = flashSaleActivityService.getByIdOrStoreId(id, storeId);
            if (activityDTO == null) {
                return new Result().error(ErrorCode.FORBIDDEN, "活动不存在");
            } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == activityDTO.getActivityState()) {
                return new Result().error(ErrorCode.FORBIDDEN, "活动已开始");
            }
        }

        flashSaleActivityService.delete(ids);

        return new Result().ok(null, "删除活动成功");
    }

    @PutMapping("goods/add")
    @ApiOperation("保存或者修改活动商品sku")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_GOODS_SAVE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_GOODS_SAVE_SUCCESS_MESSAGE)
    public Result saveOrUpdateSeckillGoods(@RequestBody InsertActivityGoodsDTO insertActivityGoodsDTO, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AddSeckillSkuListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        // 数据校验
        ValidatorUtils.validateEntity(insertActivityGoodsDTO, AddGroup.class);
        List<InsertSeckillSkuDTO> skuList = insertActivityGoodsDTO.getSkuList();
        skuList.forEach(sku -> ValidatorUtils.validateEntity(sku, AddGroup.class));
        Boolean flag = flashSaleActivityService.saveActivityGoods(insertActivityGoodsDTO, storeId);
        if (flag) {
            return new Result<>().ok(null, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }

    }

    @DeleteMapping("goods")
    @ApiOperation("删除限时购商品sku")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_GOODS_DELETE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_GOODS_DELETE_SUCCESS_MESSAGE)
    public Result deleteSeckillGoods(@RequestBody DeleteActivityGoodsDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AddSeckillSkuListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
//        Long storeId = 1191662196216594433L;

        // 数据校验
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        // 1.查询活动
        FlashSaleActivityDTO flashSaleActivityDTO = flashSaleActivityService.getByIdOrStoreId(dto.getActivityId(), storeId);
        if (flashSaleActivityDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动不存在");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_END.value() == flashSaleActivityDTO.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动已结束");
        }

        // 删除商品数据
        activityGoodsService.deleteByActivityAndGoodsId(dto.getActivityId(), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value(), dto.getGoodsIds());

        return new Result<>().ok(null, "删除成功");
    }


}
