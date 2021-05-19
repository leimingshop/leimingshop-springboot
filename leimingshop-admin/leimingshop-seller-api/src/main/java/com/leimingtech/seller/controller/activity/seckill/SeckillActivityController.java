/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.activity.seckill;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.setting.SettingSeckillDTO;
import com.leimingtech.modules.dto.activity.goods.*;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.grade.MemberGradeSaveDTO;
import com.leimingtech.modules.dto.seckill.*;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.grade.MemberGradeService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.service.seckill.SeckillActivityService;
import com.leimingtech.modules.service.seckill.SeckillSessionService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.service.setting.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <店铺秒杀管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/9
 */
@Slf4j
@RestController
@RequestMapping("seckill")
@Api(tags = "店铺秒杀管理")
public class SeckillActivityController {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private SeckillSessionService seckillSessionService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private ReduceActivityService reduceActivityService;

    @Autowired
    private CouponsActivityService couponsActivityService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private MemberGradeService memberGradeService;

    @Autowired
    private SettingService settingService;

    @GetMapping("session/list")
    @ApiOperation("查询秒杀场次集合")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_SESSION_LIST_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_SESSION_LIST_SUCCESS_MESSAGE)
    public Result<List<SeckillSessionChoiceDTO>> sessionList() {
        // 查询未结束场次
        Map<String, Object> params = new HashMap<>(1);
        params.put("activityStartDateStart", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        List<SeckillSessionDTO> sessionList = seckillSessionService.list(params);

        // 实体转化
        List<SeckillSessionChoiceDTO> list = ConvertUtils.sourceToTarget(sessionList, SeckillSessionChoiceDTO.class);
        return new Result<List<SeckillSessionChoiceDTO>>().ok(list, "查询成功");
    }

    @GetMapping("activity/page")
    @ApiOperation("秒杀活动分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityName", value = "秒杀活动名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "activityState", value = "活动状态 0未开始 1进行中 2已结束", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "activityStartDate", value = "活动开始日期", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "activityEndDate", value = "活动结束日期", paramType = "query", dataType = "Date")
    })
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_PAGE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_PAGE_SUCCESS_MESSAGE)
    public Result<PageData<AdminSeckillActivityPageDTO>> adminPage(@ApiIgnore @RequestParam Map<String, Object> params,
                                                                   @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AdminSeckillActivityPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
//        Long storeId = 1196266068939710466L;
        params.put("storeId", storeId.toString());

        // 查询秒杀活动分页
        PageData<AdminSeckillActivityPageDTO> page = seckillActivityService.adminPage(params);
        return new Result<PageData<AdminSeckillActivityPageDTO>>().ok(page, "查询成功");
    }

    @GetMapping("activity/{id}")
    @ApiOperation("秒杀活动详情")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_DELETE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_DELETE_SUCCESS_MESSAGE)
    public Result<AdminSeckillActivityDetailDTO> adminDetail(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<AdminSeckillActivityDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询秒杀活动详情
        AdminSeckillActivityDetailDTO detail = seckillActivityService.adminDetail(id, storeId);
        return new Result<AdminSeckillActivityDetailDTO>().ok(detail, "查询成功");
    }

    @PostMapping("activity")
    @ApiOperation("保存秒杀活动")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_SAVE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_SAVE_SUCCESS_MESSAGE)
    public Result save(@RequestBody InsertStoreSeckillActivityDTO insertDto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        //效验数据
        ValidatorUtils.validateEntity(insertDto, AddGroup.class);

        SeckillActivityDTO dto = ConvertUtils.sourceToTarget(insertDto, SeckillActivityDTO.class);
        dto.setStoreId(storeId);

        // 数据校验
        String errorMsg = vaildSaveOrUpdate(dto);
        if (StringUtils.isNotBlank(errorMsg)) {
            return new Result().error(ErrorCode.FORBIDDEN, errorMsg);
        }

        // 默认数据填充
        fillDefaultData(dto);
        dto.setId(IdGenerator.defaultSnowflakeId());

        // 保存活动
        Boolean flag = seckillActivityService.save(dto);
        if (flag) {
            return new Result<>().ok(dto, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }
    }

    @PutMapping("activity")
    @ApiOperation("编辑秒杀活动")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_UPDATE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_UPDATE_SUCCESS_MESSAGE)
    public Result edit(@RequestBody EditStoreSeckillActivityDTO editDto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        //效验数据
        ValidatorUtils.validateEntity(editDto, UpdateGroup.class);

        SeckillActivityDTO dto = ConvertUtils.sourceToTarget(editDto, SeckillActivityDTO.class);
        dto.setStoreId(storeId);

        // 查询秒杀活动
        SeckillActivityDTO seckillActivity = seckillActivityService.getByIdOrStoreId(dto.getId(), dto.getStoreId());
        if (seckillActivity == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "秒杀活动不存在");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == seckillActivity.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "秒杀活动已开始");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_END.value() == seckillActivity.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "秒杀活动已结束");
        }

        // 数据校验
        String errorMsg = vaildSaveOrUpdate(dto);
        if (StringUtils.isNotBlank(errorMsg)) {
            return new Result().error(ErrorCode.FORBIDDEN, errorMsg);
        }

        // 默认数据填充
        fillDefaultData(dto);

        // 保存活动
        Boolean flag = seckillActivityService.editSeckillActivity(dto);
        if (flag) {
            return new Result<>().ok(null, "编辑成功");
        } else {
            return new Result<>().error("编辑失败");
        }
    }

    @DeleteMapping("activity")
    @ApiOperation("删除秒杀活动")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_DELETE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_DELETE_SUCCESS_MESSAGE)
    public Result delete(@RequestBody Long[] ids, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        // 查询秒杀活动状态
        for (Long id : ids) {
            SeckillActivityDTO seckillActivity = seckillActivityService.getByIdOrStoreId(id, storeId);
            if (seckillActivity == null) {
                return new Result().error(ErrorCode.FORBIDDEN, "秒杀活动不存在");
            } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == seckillActivity.getActivityState()) {
                return new Result().error(ErrorCode.FORBIDDEN, "秒杀活动已开始");
            }
        }

        // 删除秒杀活动
        seckillActivityService.delete(ids);

        return new Result<>().ok(null, "删除成功");
    }

    @PutMapping("activity/stop/{id}")
    @ApiOperation("停止秒杀活动")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_STOP_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ACTIVITY_STOP_SUCCESS_MESSAGE)
    public Result stop(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询秒杀活动状态
        SeckillActivityDTO seckillActivity = seckillActivityService.getByIdOrStoreId(id, storeId);
        if (seckillActivity == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "秒杀活动不存在");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() != seckillActivity.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "秒杀活动不是开始状态");
        }

        // 删除秒杀活动
        Boolean flag = seckillActivityService.stop(id, storeId);
        if (flag) {
            return new Result<>().ok(null, "停止成功");
        } else {
            return new Result<>().error("停止失败");
        }
    }

    @GetMapping("goods/add/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsShow", value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "gcId", value = "三级分类id", paramType = "query", dataType = "long")
    })
    @ApiOperation("秒杀活动添加商品分页查询")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_ADD_GOODS_PAGE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_ADD_GOODS_PAGE_SUCCESS_MESSAGE)
    public Result<PageData<AddSeckillGoodsPageDTO>> addSeckillGoodsPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AddSeckillGoodsPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());

        // 查询秒杀商品分页
        PageData<AddSeckillGoodsPageDTO> page = activityGoodsService.canAddActiveGoods(params);
        return new Result<PageData<AddSeckillGoodsPageDTO>>().ok(page, "查询成功");
    }

    @GetMapping("goods/sku/add/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "goodsId", value = "商品spu id", required = true, paramType = "query", dataType = "long")
    })
    @ApiOperation("保存或者修改秒杀商品sku列表数据查询")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_SKU_LIST_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_SKU_LIST_SUCCESS_MESSAGE)
    public Result<List<AddSeckillSkuListDTO>> getAddSeckillSkuList(@RequestParam("activityId") Long activityId,
                                                                   @RequestParam("goodsId") Long goodsId,
                                                                   @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AddSeckillSkuListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询秒杀sku列表
        List<AddSeckillSkuListDTO> addSeckillSkuList = activityGoodsService.getAddSeckillSkuList(activityId, goodsId, storeId);
        return new Result<List<AddSeckillSkuListDTO>>().ok(addSeckillSkuList, "查询成功");
    }

    @GetMapping("goods/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "query", dataType = "long")
    })
    @ApiOperation("秒杀活动已选择商品列表查询")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_GOODS_LIST_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_GOODS_LIST_SUCCESS_MESSAGE)
    public Result<PageData<AdminSeckillGoodsDTO>> seckillGoodsPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AdminSeckillGoodsDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());
        params.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());

        // 秒杀活动已选择商品列表查询
        PageData<AdminSeckillGoodsDTO> page = activityGoodsService.adminSeckillGoodsPage(params);
        return new Result<PageData<AdminSeckillGoodsDTO>>().ok(page, "查询成功");
    }

    @PutMapping("goods/add")
    @ApiOperation("保存或者修改秒杀商品sku")
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

        // 1.查询秒杀活动 - 校验活动合法性
        SeckillActivityDTO seckillActivity = seckillActivityService.getByIdOrStoreId(insertActivityGoodsDTO.getActivityId(), storeId);
        if (seckillActivity == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动不存在");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_END.value() == seckillActivity.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动已结束");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == seckillActivity.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动已开始");
        }

        // 2.查询添加的商品 - 校验商品合法性
        GoodsDTO goods = goodsService.get(insertActivityGoodsDTO.getGoodsId());
        if (goods == null || !goods.getStoreId().equals(storeId)) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品不存在");
        } else if (GoodsStatusEnum.GOODS_AUDIT_PASS.value() != goods.getGoodsStatus()) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品未通过审核");
        }

        // 3.查询活动商品规格 - 校验规格合法性 - 校验规格库存是否充足
        List<Long> specIds = skuList.stream().map(InsertSeckillSkuDTO::getSpecId).collect(Collectors.toList());
        List<GoodsSpecDTO> specList = goodsSpecService.getByIds(specIds);
        if (specList.size() != skuList.size()) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品规格不存在");
        }

        // 校验规格库存是否充足
        List<ActivityGoodsDTO> preActivityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(insertActivityGoodsDTO.getActivityId()), ActivityTypeEnum.SECKILL_ACTIVITY.value());
        for (InsertSeckillSkuDTO sku : skuList) {
            for (GoodsSpecDTO goodsSpec : specList) {
//                商品价格>活动价格 ？1 : -1
//                xs != ys ? ((xs > ys) ? 1 : -1) : 0;
                if (sku.getSpecId().equals(goodsSpec.getId()) && goodsSpec.getSpecSellPrice().compareTo(sku.getActivityPrice()) < 0) {
                    return new Result().error(ErrorCode.FORBIDDEN, "活动价格不能大于商品价格");
                }
                // 匹配活动商品
                ActivityGoodsDTO activityGoods = null;
                for (ActivityGoodsDTO activityGoodsDTO : preActivityGoodsList) {
                    if (activityGoodsDTO.getSpecId().equals(goodsSpec.getId())) {
                        activityGoods = activityGoodsDTO;
                    }
                }
                if (activityGoods == null) {
                    // 该商品未参加此活动，校验 活动库存 > 活动剩余库存
                    if (sku.getSpecId().equals(goodsSpec.getId()) && sku.getActivityStorage() > goodsSpec.getSpecStorage()) {
                        return new Result().error(ErrorCode.FORBIDDEN, "商品库存不足");
                    }
                } else {
                    // 该商品参加此活动，校验 活动库存 > 普通商品库存 + 活动剩余库存
                    if (sku.getSpecId().equals(goodsSpec.getId()) && sku.getActivityStorage() > (goodsSpec.getSpecStorage() + activityGoods.getActivitySurplusStorage())) {
                        return new Result().error(ErrorCode.FORBIDDEN, "商品库存不足");
                    }
                }
            }
        }

        // 4.查询商品所有优惠券活动
//        List<CouponsGoodsDTO> allCouponsGoods = couponsActivityService.getAllCouponsGoods(storeId, goods.getBrandId(), goods.getId());
//        if (CollectionUtils.isNotEmpty(allCouponsGoods)) {
//            return new Result().error(ErrorCode.FORBIDDEN, "商品已参加优惠券活动");
//        }

        // 5.查询商品所有满减活动
//        List<ReduceGoodsDTO> allReduceGoods = reduceActivityService.getAllReduceGoods(storeId, goods.getBrandId(), goods.getId());
//        if (CollectionUtils.isNotEmpty(allReduceGoods)) {
//            return new Result().error(ErrorCode.FORBIDDEN, "商品已参加满减活动");
//        }

        // spu下单数
        Integer spuOrderNum = 0;
        // 6.查询商品所有秒杀活动
        List<ActivityGoodsDTO> allSeckillGoods = activityGoodsService.getAllSeckillGoods(storeId, goods.getId());
        if (CollectionUtils.isNotEmpty(allSeckillGoods)) {
            if (allSeckillGoods.size() > 1 || (allSeckillGoods.size() == 1 && !allSeckillGoods.get(0).getActivityId().equals(insertActivityGoodsDTO.getActivityId()))) {
                // 商品参加了此活动以外的其他活动
                return new Result().error(ErrorCode.FORBIDDEN, "商品已参加秒杀活动");
            }

            // 原活动商品下单数获取 - 设置为该次数据保存的商品下单数
            for (ActivityGoodsDTO seckillGoods : allSeckillGoods) {
                if (seckillGoods.getActivityId().equals(insertActivityGoodsDTO.getActivityId())) {
                    spuOrderNum = seckillGoods.getSpuOrderNum();
                    break;
                }
            }
        }

        // 7.查询商品所有拼团活动
        List<ActivityGoodsDTO> allGroupGoods = activityGoodsService.getAllGroupGoods(storeId, goods.getId());
        if (CollectionUtils.isNotEmpty(allGroupGoods)) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品已参加拼团活动");
        }

        List<ActivityGoodsDTO> activityGoodsList = new ArrayList<>();
        // 8.保存商品数据封装
        for (InsertSeckillSkuDTO sku : skuList) {
            ActivityGoodsDTO activityGoods = new ActivityGoodsDTO();

            BeanCopier.create(InsertSeckillSkuDTO.class, ActivityGoodsDTO.class, false)
                    .copy(sku, activityGoods, null);
            activityGoods.setId(IdGenerator.defaultSnowflakeId());
            activityGoods.setActivityId(insertActivityGoodsDTO.getActivityId());
            activityGoods.setActivitySurplusStorage(activityGoods.getActivityStorage());
            activityGoods.setActivityType(ActivityTypeEnum.SECKILL_ACTIVITY.value());
            activityGoods.setGoodsId(insertActivityGoodsDTO.getGoodsId());
            activityGoods.setSessionId(seckillActivity.getSessionId());
            activityGoods.setSpuOrderNum(spuOrderNum);

            // 保存集合
            activityGoodsList.add(activityGoods);
        }

        // 9.保存活动商品
        Boolean flag = activityGoodsService.saveBatch(activityGoodsList, insertActivityGoodsDTO.getActivityId(), ActivityTypeEnum.SECKILL_ACTIVITY.value(), insertActivityGoodsDTO.getGoodsId());
        if (flag) {
            return new Result<>().ok(null, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }

    }

    @DeleteMapping("goods")
    @ApiOperation("删除秒杀商品sku")
    @LogContext(code = ActivityStatusCode.SELLER_SECKILL_GOODS_DELETE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_SECKILL_GOODS_DELETE_SUCCESS_MESSAGE)
    public Result deleteSeckillGoods(@RequestBody DeleteActivityGoodsDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AddSeckillSkuListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 数据校验
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        // 1.查询活动
        SeckillActivityDTO seckillActivity = seckillActivityService.getByIdOrStoreId(dto.getActivityId(), storeId);
        if (seckillActivity == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动不存在");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_END.value() == seckillActivity.getActivityState()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动已结束");
        }

        // 删除商品数据
        activityGoodsService.deleteByActivityAndGoodsId(dto.getActivityId(), ActivityTypeEnum.SECKILL_ACTIVITY.value(), dto.getGoodsIds());

        return new Result<>().ok(null, "删除成功");
    }

    @PostMapping("check/goods/activity")
    @ApiOperation("校验商品是否参加促销活动")
    public Result<UpdateGoodsCheckActivityDTO> checkGoodsActivity(@RequestBody List<Long> goodsIds) {
        UpdateGoodsCheckActivityDTO updateGoodsCheckActivityDTO = activityGoodsService.checkGoodsActivity(goodsIds);
        if (updateGoodsCheckActivityDTO != null) {
            return new Result<UpdateGoodsCheckActivityDTO>().ok(updateGoodsCheckActivityDTO, "查询成功");
        } else {
            return new Result<UpdateGoodsCheckActivityDTO>().error("查询失败");
        }
    }

    /**
     * 功能描述：
     * <保存编辑活动通用校验 场次 用户等级>
     *
     * @param dto 秒杀活动保存修改实体
     * @return
     * @date 2020/3/9 15:45
     * @author 刘远杰
     **/
    private String vaildSaveOrUpdate(SeckillActivityDTO dto) {
        // 查询秒杀场次
        SeckillSessionDTO session = seckillSessionService.get(dto.getSessionId());
        if (session == null) {
            return "秒杀场次不存在";
        } else if (session.getActivityEndDate().before(new Date())) {
            return "该场次已结束";
        }

        dto.setActivityStartDate(session.getActivityStartDate());
        dto.setActivityEndDate(DateUtils.addDateHours(session.getActivityStartDate(), dto.getActivityEffectiveTime()));

        // 查询用户等级
        if (dto.getMemberGradeId() != null) {
            MemberGradeSaveDTO memberGrade = memberGradeService.getMember(dto.getMemberGradeId());
            if (memberGrade == null) {
                return "用户等级不存在";
            }
            dto.setMemberGradeName(memberGrade.getGradeName());
            dto.setMemberGradeLimit(memberGrade.getIntegration());
        }
        return "";
    }

    /**
     * 功能描述：
     * <默认数据填充>
     *
     * @param dto 秒杀活动保存修改实体
     * @return
     * @date 2020/3/9 15:49
     * @author 刘远杰
     **/
    private void fillDefaultData(SeckillActivityDTO dto) {
        // 状态设置
        dto.setAuditState(SeckillActivityEnum.AUDIT_STATE_PASS.value());
        if (!dto.getActivityStartDate().after(new Date())) {
            dto.setActivityState(SeckillActivityEnum.ACTIVITY_STATE_START.value());
        } else {
            dto.setActivityState(SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value());
        }
        // 不与活动共享
        dto.setCouponsFlag(SeckillActivityEnum.SHARE_FLAG_NO.value());
        dto.setReduceFlag(SeckillActivityEnum.SHARE_FLAG_NO.value());
        dto.setPointFlag(SeckillActivityEnum.SHARE_FLAG_NO.value());
        dto.setBalanceFlag(SeckillActivityEnum.SHARE_FLAG_NO.value());
        dto.setFreeShippingFlag(SeckillActivityEnum.SHARE_FLAG_NO.value());

        dto.setBrowseNum(0);
        dto.setOrderNum(0);
    }

    @GetMapping("setting")
    @ApiOperation("查询秒杀设置")
    public Result<SettingSeckillDTO> getSeckillSetting() {
        // 查询秒杀设置
        SettingSeckillDTO dto = settingService.getSeckillSetting();
        return new Result<SettingSeckillDTO>().ok(dto, "查询秒杀设置成功");
    }

}
