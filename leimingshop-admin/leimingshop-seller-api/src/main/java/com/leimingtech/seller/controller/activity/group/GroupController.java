/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.activity.group;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.setting.SettingSeniorDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.modules.dto.activity.goods.ActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.AddSeckillGoodsPageDTO;
import com.leimingtech.modules.dto.activity.goods.AddSeckillSkuListDTO;
import com.leimingtech.modules.dto.activity.goods.DeleteActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.group.InsertGroupGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.group.InsertGroupSkuDTO;
import com.leimingtech.modules.dto.coupons.CouponsGoodsDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.group.*;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.group.GroupRecordService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.service.setting.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 拼团管理
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@RestController
@RequestMapping("group")
@Api(tags = "拼团管理")
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private GroupRecordService groupRecordService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private CouponsActivityService couponsActivityService;

    @Autowired
    private ReduceActivityService reduceActivityService;

    @GetMapping("page")
    @ApiOperation("拼团活动列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityName", value = "拼团活动名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "activityState", value = "拼团众筹活动状态 10：未生效 20：生效中 30：已失效", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "活动开始日期", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endTime", value = "活动结束日期", paramType = "query", dataType = "Date")
    })
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_ACTIVITY_PAGE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_ACTIVITY_PAGE_SUCCESS_MESSAGE)
    public Result<PageData<GroupDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<GroupDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
//        Long storeId = 1199222428123791362L;
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());

        PageData<GroupDTO> page = groupService.sellerPage(params);

        return new Result<PageData<GroupDTO>>().ok(page, "查询成功");
    }

    /**
     * 获取拼团活动详情
     *
     * @param
     * @return
     * @date 2020-03-09 17:42
     * @author huangkeyuan@leimingtech.com
     **/
    @GetMapping("{id}")
    @ApiOperation("拼团活动详情")
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_ACTIVITY_DETAIL_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_ACTIVITY_DETAIL_SUCCESS_MESSAGE)
    public Result<GroupDTO> get(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<GroupDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        GroupDTO data = groupService.get(id);

        return new Result<GroupDTO>().ok(data, "查询成功");
    }

    @PostMapping
    @ApiOperation("保存拼团活动")
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_ACTIVITY_SAVE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_ACTIVITY_SAVE_SUCCESS_MESSAGE)
    public Result save(@RequestBody InsertStoreGroupDTO dto, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);

        // 查询店铺是否存在
        StoreDTO storeDTO = storeService.get(sellerDetail.getStoreId());
        if (storeDTO == null) {
            return new Result().error("店铺不存在");
        }

        GroupDTO groupDTO = ConvertUtils.sourceToTarget(dto, GroupDTO.class);

        groupDTO.setStoreId(sellerDetail.getStoreId());
        groupDTO.setId(IdGenerator.defaultSnowflakeId());

        Boolean flag = groupService.save(groupDTO);

        if (flag) {
            return new Result<>().ok(groupDTO, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }

    }

    @PutMapping
    @ApiOperation("编辑拼团活动详情")
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_ACTIVITY_UPDATE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_ACTIVITY_UPDATE_SUCCESS_MESSAGE)
    public Result update(@RequestBody GroupDTO dto, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<GroupDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);

        // 校验活动状态
        GroupDTO groupDTO = groupService.get(dto.getId());
        if (groupDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "编辑失败，未查询到活动");
        }
        if (GroupsEnum.ACTIVITY_STATUS_NO.value() != groupDTO.getActivityStatus()) {
            return new Result().error(ErrorCode.FORBIDDEN, "编辑失败，活动状态不是未开始");
        }

        Boolean flag = groupService.update(dto);

        if (flag) {
            return new Result<>().ok(groupDTO, "编辑成功");
        } else {
            return new Result<>().error("编辑失败");
        }

    }

    @DeleteMapping
    @ApiOperation("删除拼团活动")
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_ACTIVITY_DELETE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_ACTIVITY_DELETE_SUCCESS_MESSAGE)
    public Result delete(@RequestBody Long[] ids, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 校验活动状态，只有未开始,活动可以删除
        for (Long id : ids) {
            GroupDTO groupDTO = groupService.get(id);
            if (groupDTO == null) {
                return new Result().error(ErrorCode.FORBIDDEN, "删除失败，未查询到活动");
            }
            if (GroupsEnum.ACTIVITY_STATUS_ONGOING.value() == groupDTO.getActivityStatus()) {
                return new Result().error(ErrorCode.FORBIDDEN, "删除失败，活动进行中不能删除");
            }
        }

        // 拼团活动删除
        Boolean flag = groupService.delete(ids);
        if (flag) {
            return new Result<>().ok(null, "删除成功");
        } else {
            return new Result<>().error("删除失败");
        }

    }

    /**
     * 拼团活动添加商品或者管理商品，商品列表数据回显
     *
     * @return
     * @date 2020-03-10 17:51
     * @author huangkeyuan@leimingtech.com
     **/
    @GetMapping("list/goods/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsShow", value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "gcId", value = "三级分类id", paramType = "query", dataType = "long")
    })
    @ApiOperation("拼团活动添加商品分页查询")
    public Result<PageData<AddSeckillGoodsPageDTO>> addSeckillGoodsPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {


        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AddSeckillGoodsPageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        params.put("storeId", storeId.toString());

        // 查询拼团商品分页
        PageData<AddSeckillGoodsPageDTO> page = activityGoodsService.getAddGroupGoodsList(params);
        return new Result<PageData<AddSeckillGoodsPageDTO>>().ok(page, "查询成功");
    }


    @GetMapping("list/sku/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "goodsId", value = "商品spu id", required = true, paramType = "query", dataType = "long")
    })
    @ApiOperation("保存或者修改拼团商品sku列表数据查询")
    public Result<List<AddSeckillSkuListDTO>> getAddSeckillSkuList(@RequestParam("activityId") Long activityId,
                                                                   @RequestParam("goodsId") Long goodsId,
                                                                   @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AddSeckillSkuListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询拼团sku列表
        List<AddSeckillSkuListDTO> addSeckillSkuList = activityGoodsService.getAddGropGoodsList(activityId, goodsId, storeId);
        return new Result<List<AddSeckillSkuListDTO>>().ok(addSeckillSkuList, "查询成功");
    }

    @GetMapping("selected/goods/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动id", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
    })
    @ApiOperation("拼团活动已选择商品列表查询")
    public Result<PageData<GroupGoodsDTO>> groupGoodsPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<GroupGoodsDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
//        Long storeId = 1199222428123791362L;
        params.put("storeId", storeId.toString());
        // 拼团活动已选择商品列表查询
        PageData<GroupGoodsDTO> groupGoodsList = activityGoodsService.groupGoodsList(params);
        return new Result<PageData<GroupGoodsDTO>>().ok(groupGoodsList, "查询成功");
    }

    @PutMapping("goods/add")
    @ApiOperation("保存或者修改拼团商品sku")
    public Result saveOrUpdateGroupGoods(@RequestBody InsertGroupGoodsDTO insertGroupGoodsDTO, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
//        Long storeId = 1199222428123791362L;
        // 数据校验
        ValidatorUtils.validateEntity(insertGroupGoodsDTO, AddGroup.class);
        List<InsertGroupSkuDTO> skuList = insertGroupGoodsDTO.getSkuList();

        skuList.forEach((sku) -> {
            ValidatorUtils.validateEntity(sku, AddGroup.class);
        });

        //判断活动库存为0的规格商品，并剔除
        Iterator<InsertGroupSkuDTO> iterator = skuList.iterator();
        while (iterator.hasNext()) {
            InsertGroupSkuDTO skuIterator = iterator.next();
            if (skuIterator.getActivityStorage() == 0 || skuIterator.getActivityStorage() == null) {
                iterator.remove();
            }
        }

        // 1.查询活动
        GroupDTO groupDTO = groupService.findByIdAndStoreId(insertGroupGoodsDTO.getActivityId(), storeId);
        if (groupDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动不存在");
        } else if (GroupsEnum.ACTIVITY_STATUS_END.value() == groupDTO.getActivityStatus()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动已结束");
        } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == groupDTO.getActivityStatus()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动已开始");
        }

        // 2.查询商品
        GoodsDTO goods = goodsService.get(insertGroupGoodsDTO.getGoodsId());
        if (goods == null || !goods.getStoreId().equals(storeId)) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品不存在");
        } else if (GoodsStatusEnum.GOODS_AUDIT_PASS.value() != goods.getGoodsStatus()) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品未通过审核");
        }

        // 3.查询活动商品规格
        List<Long> specIds = skuList.stream().map(InsertGroupSkuDTO::getSpecId).collect(Collectors.toList());
        List<GoodsSpecDTO> specList = goodsSpecService.getByIds(specIds);
        if (specList.size() != skuList.size()) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品规格不存在");
        }

        // 规格库存校验
        List<ActivityGoodsDTO> preActivityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(insertGroupGoodsDTO.getActivityId()), ActivityTypeEnum.GROUP_ACTIVITY.value());
        for (InsertGroupSkuDTO sku : skuList) {
            for (GoodsSpecDTO goodsSpec : specList) {
//                活动价格不能大于商品价格
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
        List<CouponsGoodsDTO> allCouponsGoods = couponsActivityService.getAllCouponsGoods(storeId, goods.getBrandId(), goods.getId());
        if (CollectionUtils.isNotEmpty(allCouponsGoods)) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品已参加优惠券活动");
        }

        // 5.查询商品所有满减活动
        List<ReduceGoodsDTO> allReduceGoods = reduceActivityService.getAllReduceGoods(storeId, goods.getBrandId(), goods.getId());
        if (CollectionUtils.isNotEmpty(allReduceGoods)) {
            return new Result().error(ErrorCode.FORBIDDEN, "商品已参加满减活动");
        }

        // spu下单数
        Integer spuOrderNum = 0;
        // 6.查询商品所有拼团活动
        List<ActivityGoodsDTO> allGroupGoods = activityGoodsService.getAllGroupGoods(storeId, goods.getId());
        if (CollectionUtils.isNotEmpty(allGroupGoods)) {
            if (allGroupGoods.size() > 1 || (allGroupGoods.size() == 1 && !allGroupGoods.get(0).getActivityId().equals(insertGroupGoodsDTO.getActivityId()))) {
                return new Result().error(ErrorCode.FORBIDDEN, "商品已参加拼团活动");
            }
            // 原活动商品下单数获取
            for (ActivityGoodsDTO groupGoods : allGroupGoods) {
                if (groupGoods.getActivityId().equals(insertGroupGoodsDTO.getActivityId())) {
                    spuOrderNum = groupGoods.getSpuOrderNum();
                    break;
                }
            }
        }

        List<ActivityGoodsDTO> activityGoodsList = new ArrayList<>();
        // 7.保存商品数据封装
        for (InsertGroupSkuDTO sku : skuList) {
            ActivityGoodsDTO activityGoods = new ActivityGoodsDTO();

            BeanCopier.create(InsertGroupSkuDTO.class, ActivityGoodsDTO.class, false)
                    .copy(sku, activityGoods, null);
            activityGoods.setId(IdGenerator.defaultSnowflakeId());
            activityGoods.setActivityId(insertGroupGoodsDTO.getActivityId());
            activityGoods.setActivitySurplusStorage(activityGoods.getActivityStorage());
            activityGoods.setActivityType(ActivityTypeEnum.GROUP_ACTIVITY.value());
            activityGoods.setGoodsId(insertGroupGoodsDTO.getGoodsId());
            activityGoods.setSort(insertGroupGoodsDTO.getSort());
            activityGoods.setRegimentNum(insertGroupGoodsDTO.getRegimentNum());
            activityGoods.setJoinLimit(insertGroupGoodsDTO.getJoinLimit());
            activityGoods.setOnceBuyLimit(insertGroupGoodsDTO.getOnceBuyLimit());
            activityGoods.setSpuOrderNum(spuOrderNum);
            // 保存集合
            activityGoodsList.add(activityGoods);
        }

        // 9.保存活动商品
        Boolean flag = activityGoodsService.saveBatch(activityGoodsList, insertGroupGoodsDTO.getActivityId(), ActivityTypeEnum.GROUP_ACTIVITY.value(), insertGroupGoodsDTO.getGoodsId());
        if (flag) {
            return new Result<>().ok(null, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }

    }


    @DeleteMapping("goods")
    @ApiOperation("删除拼团商品sku")
    public Result deleteGroupGoods(@RequestBody DeleteActivityGoodsDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID在·
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AddSeckillSkuListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        // 数据校验
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        // 1.查询活动
        GroupDTO groupDTO = groupService.get(dto.getActivityId());
        if (groupDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动不存在");
        } else if (GroupsEnum.ACTIVITY_STATUS_END.value() == groupDTO.getActivityStatus()) {
            return new Result().error(ErrorCode.FORBIDDEN, "活动已结束");
        }

        // 删除商品数据
        activityGoodsService.deleteByActivityAndGoodsId(dto.getActivityId(), ActivityTypeEnum.GROUP_ACTIVITY.value(), dto.getGoodsIds());

        return new Result<>().ok(null, "删除成功");
    }


    @GetMapping("record/page")
    @ApiOperation("拼团记录列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "activityRecordId", value = "拼团记录ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "groupId", value = "拼团活动ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "activityName", value = "拼团活动名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupStatus", value = "拼团活动状态0:拼团进行，1:拼团成功，2:拼团失败", paramType = "query", dataType = "int"),
    })
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_RECORD_PAGE_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_RECORD_PAGE_SUCCESS_MESSAGE)
    public Result<PageData<GroupRecordDTO>> recodePage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<GroupRecordDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());

        PageData<GroupRecordDTO> page = groupRecordService.page(params);
        return new Result<PageData<GroupRecordDTO>>().ok(page, "查询成功");

    }


    @GetMapping("record/detail/{id}")
    @ApiOperation("拼团记录详情")
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_RECORD_DETAIL_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_RECORD_DETAIL_SUCCESS_MESSAGE)
    public Result<GroupRecordOrderDTO> getGroupRecord(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<GroupRecordOrderDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        Long storeId = sellerDetail.getStoreId();

        GroupRecordOrderDTO groupRecordOrderDTO = groupRecordService.getGroupOrderDetail(storeId, id);

        return new Result<GroupRecordOrderDTO>().ok(groupRecordOrderDTO, "查询成功");
    }

    //拼团详情快速成团
    @PutMapping("composition/{groupRecordId}")
    @ApiOperation("快速成团")
    public Result composition(@PathVariable("groupRecordId") Long groupRecordId, @ApiIgnore SellerDetail sellerDetail) {

        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        // 查询店铺是否存在
        StoreDTO storeDTO = storeService.get(sellerDetail.getStoreId());
        if (storeDTO == null) {
            return new Result().error("店铺不存在");
        }
        Long storeId = sellerDetail.getStoreId();
//        Long storeId = 1191957407345991682L;

        Boolean flag = groupRecordService.groupComposition(storeId, groupRecordId);

        if (flag) {
            return new Result<>().ok(null, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }

    }

    //拼团详情取消订单
    @PutMapping("order/cancel/{groupRecordId}/{reasonId}")
    @ApiOperation("取消订单")
    public Result cancelGroupOrder(@PathVariable("groupRecordId") Long groupRecordId, @PathVariable("reasonId") Long reasonId, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        // 查询店铺是否存在
        StoreDTO storeDTO = storeService.get(sellerDetail.getStoreId());
        if (storeDTO == null) {
            return new Result().error("店铺不存在");
        }
        Long storeId = sellerDetail.getStoreId();

        Boolean flag = groupRecordService.cancelOrder(storeId, groupRecordId, reasonId);

        if (flag) {
            return new Result<>().ok(null, "取消成功");
        } else {
            return new Result<>().error("取消失败");
        }

    }

    @GetMapping("cancel/order/time")
    @ApiOperation("获取平台默认取消订单时间")
    public Result getDefaultTime() {
        // 获取setting表中的可申请售后时间
        String setting = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
        SettingSeniorDTO senior = JSON.parseObject(setting, SettingSeniorDTO.class);

        if (null == senior) {
            log.info("查询订单取消时间异常，请稍后再试");

            return new Result<>().error("查询订单取消时间异常，请稍后再试");
        } else {
            return new Result<>().ok(senior.getCancelOrder(), "获取成功");
        }
    }


    @GetMapping("order/change/detail")
    @ApiOperation("获取网站设置的高级设置订单设置")
    public Result<SettingSeniorDTO> getSeniorSet() {

        SettingSeniorDTO dto = settingService.getSeniorSet();

        return new Result<SettingSeniorDTO>().ok(dto);
    }

    @PutMapping("activity/stop/{id}")
    @ApiOperation("停止拼团活动")
    @LogContext(code = ActivityStatusCode.SELLER_GROUP_ACTIVITY_STOP_SUCCESS_CODE, note = ActivityStatusCode.SELLER_GROUP_ACTIVITY_STOP_SUCCESS_MESSAGE)
    public Result stop(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询拼团活动状态
        GroupDTO groupDTO = groupService.findByIdAndStoreId(id, storeId);
        if (groupDTO == null) {
            return new Result().error(ErrorCode.FORBIDDEN, "拼团活动不存在");
        } else if (GroupsEnum.ACTIVITY_STATUS_ONGOING.value() != groupDTO.getActivityStatus()) {
            return new Result().error(ErrorCode.FORBIDDEN, "拼团活动不是开始状态");
        }

        // 删除拼团活动
        Boolean flag = groupService.stop(id, storeId);
        if (flag) {
            return new Result<>().ok(null, "停止成功");
        } else {
            return new Result<>().error("停止失败");
        }
    }
}
