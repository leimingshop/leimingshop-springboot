/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.flashsale.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.EsConflictException;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.flashsale.FlashSaleActivityDao;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.activity.goods.ActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.InsertActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.InsertSeckillSkuDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityEditDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityPageDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivitySaveDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.grade.MemberGradeSaveDTO;
import com.leimingtech.modules.entity.flashsale.FlashSaleActivityEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.flashsale.FlashSaleActivityEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.flashsale.FlashSaleActivityService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.grade.MemberGradeService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 限时抢购活动表
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020-10-15
 */
@Service
@Slf4j

public class FlashSaleActivityServiceImpl extends BaseServiceImpl<FlashSaleActivityDao, FlashSaleActivityEntity> implements FlashSaleActivityService {


    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private ActivityGoodsService activityGoodsService;
    @Autowired
    private MemberGradeService memberGradeService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private EsDataUtils esDataUtils;

    @Override

    public PageData<FlashSaleActivityDTO> page(@RequestParam Map<String, Object> params) {
        IPage<FlashSaleActivityEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, FlashSaleActivityDTO.class);
    }

    @Override

    public List<FlashSaleActivityDTO> list(Map<String, Object> params) {
        List<FlashSaleActivityEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, FlashSaleActivityDTO.class);
    }

    private QueryWrapper<FlashSaleActivityEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<FlashSaleActivityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public FlashSaleActivityDTO get(Long id) {
        FlashSaleActivityEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, FlashSaleActivityDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody FlashSaleActivityDTO dto) {
        FlashSaleActivityEntity entity = ConvertUtils.sourceToTarget(dto, FlashSaleActivityEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody FlashSaleActivityDTO dto) {
        FlashSaleActivityEntity entity = ConvertUtils.sourceToTarget(dto, FlashSaleActivityEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        // 1.查询删除限时抢购活动信息
        List<FlashSaleActivityEntity> activityEntities = baseDao.selectList(Wrappers.<FlashSaleActivityEntity>lambdaQuery()
                .in(FlashSaleActivityEntity::getId, ids));
        // 2.释放活动库存
        this.addGoodsSpecStorage(activityEntities);

//        // 3.更新限时抢购商品es活动
//        activityGoodsService.updateSpecIndexByActivityId(Arrays.asList(ids), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
//        // 4.更新限时抢购购物车es活动状态
//        activityGoodsService.updateCartIndexByActivityId(Arrays.asList(ids), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());

        this.updateEsSpecActivity(activityEntities, FlashSaleActivityEnum.ACTIVITY_STATE_END.value());
        this.updateCartEsSpecActivity(activityEntities, FlashSaleActivityEnum.ACTIVITY_STATE_END.value());

        // 5.删除限时抢购商品
        activityGoodsService.deleteByActivityIds(Arrays.asList(ids), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
        // 6.删除限时抢购活动
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


    @Override
    public PageData<FlashSaleActivityPageDTO> managePage(@RequestParam Map<String, Object> params) {
        IPage<FlashSaleActivityEntity> page = getPage(params, null, false);

        List<FlashSaleActivityPageDTO> list = baseDao.managePage(params);


        return new PageData<FlashSaleActivityPageDTO>(list, page.getTotal());
    }


    @Override
    public void saveFlashSaleActivity(@RequestBody FlashSaleActivitySaveDTO dto) {
        FlashSaleActivityEntity entity = ConvertUtils.sourceToTarget(dto, FlashSaleActivityEntity.class);
        this.fillMemberGrade(entity);
        baseDao.insert(entity);
    }


    @Override
    public void updateFlashSaleActivity(@RequestBody FlashSaleActivityEditDTO dto) {
        FlashSaleActivityEntity entity = ConvertUtils.sourceToTarget(dto, FlashSaleActivityEntity.class);
        //设置用户等级
        this.fillMemberGrade(entity);
        baseDao.updateById(entity);
        if (entity.getMemberGradeId() == null) {
            super.update(Wrappers.<FlashSaleActivityEntity>lambdaUpdate()
                    .set(FlashSaleActivityEntity::getMemberGradeId, null)
                    .set(FlashSaleActivityEntity::getMemberGradeLimit, null)
                    .set(FlashSaleActivityEntity::getMemberGradeName, null)
                    .eq(FlashSaleActivityEntity::getId, entity.getId())
            );
        }
//        //更新活动商品索引
//        activityGoodsService.updateSpecIndexByActivityId(Collections.singletonList(entity.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
//        //更新活动购物车索引
//        activityGoodsService.updateCartIndexByActivityId(Collections.singletonList(entity.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
        this.updateEsSpecActivity(Collections.singletonList(entity), entity.getActivityState());
        this.updateCartEsSpecActivity(Collections.singletonList(entity), entity.getActivityState());
    }

    /**
     * 设置用户等级
     *
     * @param entity 活动信息
     * @author xuzhch
     * @date 2020年10月20日
     */
    private void fillMemberGrade(FlashSaleActivityEntity entity) {
        // 设置用户等级
        if (entity.getMemberGradeId() != null) {
            MemberGradeSaveDTO memberGrade = memberGradeService.getMember(entity.getMemberGradeId());
            if (memberGrade == null) {
                throw new ServiceException(MemberStatusCode.MEMBER_GRADE_NOT_EXIST);
            }
            entity.setMemberGradeName(memberGrade.getGradeName());
            entity.setMemberGradeLimit(memberGrade.getIntegration());
        } else {
            entity.setMemberGradeId(null);
            entity.setMemberGradeName(null);
            entity.setMemberGradeLimit(null);
        }
    }


    @Override
    public Boolean stop(@RequestParam(value = "id") Long id,
                        @RequestParam(value = "storeId", required = false) Long storeId) {
        LambdaQueryWrapper<FlashSaleActivityEntity> queryWrapper = Wrappers.<FlashSaleActivityEntity>lambdaQuery()
                .eq(FlashSaleActivityEntity::getId, id)
                .eq(storeId != null, FlashSaleActivityEntity::getStoreId, storeId);
        FlashSaleActivityEntity activityEntity = baseDao.selectOne(queryWrapper);

        if (activityEntity != null) {
            // 修改参数
            FlashSaleActivityEntity entity = new FlashSaleActivityEntity();
            entity.setActivityState(SeckillActivityEnum.ACTIVITY_STATE_END.value());
            // 释放商品库存
            this.addGoodsSpecStorage(Collections.singletonList(activityEntity));
            // 1.修改活动状态
            int count = baseDao.update(entity, queryWrapper);

            if (count > 0) {
//                // 更新商品es活动状态
                this.updateEsSpecActivity(Collections.singletonList(activityEntity), FlashSaleActivityEnum.ACTIVITY_STATE_END.value());
                // 更新购物车es活动状态
                this.updateCartEsSpecActivity(Collections.singletonList(activityEntity), FlashSaleActivityEnum.ACTIVITY_STATE_END.value());
                //更新活动商品索引
//                activityGoodsService.updateSpecIndexByActivityId(Collections.singletonList(entity.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
//                //更新活动购物车索引
//                activityGoodsService.updateCartIndexByActivityId(Collections.singletonList(entity.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
            }
            return count > 0;
        } else {
            return false;
        }
    }

    @Override

    public void increaseBrowserNum(@RequestBody List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            baseDao.increaseBrowserNum(ids);
        }
    }


    @Override
    public void increaseOrderNum(@RequestBody List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            baseDao.increaseOrderNum(ids);
        }
    }


    @Override
    public Boolean saveActivityGoods(@RequestBody InsertActivityGoodsDTO insertActivityGoodsDTO, @RequestParam("storeId") Long storeId) {
        FlashSaleActivityEntity activityEntity = baseDao.selectById(insertActivityGoodsDTO.getActivityId());
        //1、校验活动是否合法
        if (activityEntity == null) {
            throw new ServiceException(ActivityStatusCode.ACTIVITY_NOT_EXIST);
        } else if (SeckillActivityEnum.ACTIVITY_STATE_END.value() == activityEntity.getActivityState()) {
            throw new ServiceException(ActivityStatusCode.ACTIVITY_ALREADY_END);
        } else if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == activityEntity.getActivityState()) {
            throw new ServiceException(ActivityStatusCode.ACTIVITY_ALREADY_START);
        }
        // 2、查询添加的商品 - 校验商品合法性
        GoodsDTO goods = goodsService.get(insertActivityGoodsDTO.getGoodsId());
        if (goods == null || !goods.getStoreId().equals(storeId)) {
            throw new ServiceException(ActivityStatusCode.GOODS_NOT_EXIST);
        } else if (GoodsStatusEnum.GOODS_AUDIT_PASS.value() != goods.getGoodsStatus()) {
            throw new ServiceException(ActivityStatusCode.GOODS_NOT_VERIFY);
        }
        List<InsertSeckillSkuDTO> skuList = insertActivityGoodsDTO.getSkuList();
        // 3、查询活动商品规格 - 校验规格合法性 - 校验规格库存是否充足
        List<Long> specIds = skuList.stream().map(InsertSeckillSkuDTO::getSpecId).collect(Collectors.toList());
        List<GoodsSpecDTO> specList = goodsSpecService.getByIds(specIds);
        if (specList.size() != skuList.size()) {
            throw new ServiceException(ActivityStatusCode.GOODS_SPEC_NOT_EXIST);

        }

        // 4、校验规格库存是否充足 throw 库存不足
        List<ActivityGoodsDTO> preActivityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(insertActivityGoodsDTO.getActivityId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
        //todo xuzhch 2020年11月2日 逻辑待优化
        for (InsertSeckillSkuDTO sku : skuList) {
            for (GoodsSpecDTO goodsSpec : specList) {
                if (sku.getSpecId().equals(goodsSpec.getId()) && goodsSpec.getSpecSellPrice().compareTo(sku.getActivityPrice()) < 0) {
                    throw new ServiceException(ActivityStatusCode.ACTIVITY_GOODS_PRICE_GT_GOODS_PRICE);
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
                        throw new ServiceException(ActivityStatusCode.GOODS_INVENTORY_SHORTAGE);

                    }
                } else {
                    // 该商品参加此活动，校验 活动库存 > 普通商品库存 + 活动剩余库存
                    if (sku.getSpecId().equals(goodsSpec.getId()) && sku.getActivityStorage() > (goodsSpec.getSpecStorage() + activityGoods.getActivitySurplusStorage())) {
                        throw new ServiceException(ActivityStatusCode.GOODS_INVENTORY_SHORTAGE);
                    }
                }
            }
        }
//
        // spu下单数
        Integer spuOrderNum = 0;
        // 6.查询商品所有限时抢购活动
        List<ActivityGoodsDTO> allSeckillGoods = activityGoodsService.getAllSeckillGoods(storeId, goods.getId());
        if (CollectionUtils.isNotEmpty(allSeckillGoods)) {
            if (allSeckillGoods.size() > 1 || (allSeckillGoods.size() == 1 && !allSeckillGoods.get(0).getActivityId().equals(insertActivityGoodsDTO.getActivityId()))) {
                // 商品参加了此活动以外的其他活动
                throw new ServiceException(ActivityStatusCode.GOODS_ALREADY_PARTICIPATE_SECKILL_ACTIVITY);
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
            throw new ServiceException(ActivityStatusCode.GOODS_ALREADY_PARTICIPATE_GROUP_ACTIVITY);
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
            activityGoods.setActivityType(ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
            activityGoods.setGoodsId(insertActivityGoodsDTO.getGoodsId());
            activityGoods.setSpuOrderNum(spuOrderNum);

            // 保存集合
            activityGoodsList.add(activityGoods);
        }

        // 9.保存活动商品
        Boolean flag = activityGoodsService.saveBatch(activityGoodsList, insertActivityGoodsDTO.getActivityId(), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value(), insertActivityGoodsDTO.getGoodsId());

        return flag;


    }


    @Override
    public FlashSaleActivityDTO getByIdOrStoreId(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "storeId", required = false) Long storeId) {
        FlashSaleActivityEntity activityEntity = baseDao.selectOne(Wrappers.<FlashSaleActivityEntity>lambdaQuery()
                .eq(FlashSaleActivityEntity::getId, id)
                .eq(storeId != null, FlashSaleActivityEntity::getStoreId, storeId));
        return Optional.of(ConvertUtils.sourceToTarget(activityEntity, FlashSaleActivityDTO.class)).orElse(new FlashSaleActivityDTO());
    }


    @Override
    public List<FlashSaleActivityDTO> getInfoListByActivityIds(@RequestBody List<Long> activityIds) {
        List<FlashSaleActivityEntity> activityEntities = baseDao.selectList(Wrappers.<FlashSaleActivityEntity>lambdaQuery()
                .eq(FlashSaleActivityEntity::getDelFlag, DelFlagEnum.NORMAL.value())
                .in(FlashSaleActivityEntity::getId, activityIds));
        return Optional.of(ConvertUtils.sourceToTarget(activityEntities, FlashSaleActivityDTO.class)).orElse(new ArrayList<>());
    }


    @Override
    public Integer getCancelTime(@RequestParam("activicyIds") List<Long> activicyIds) {
        List<FlashSaleActivityEntity> entities = baseDao.selectList(Wrappers.<FlashSaleActivityEntity>lambdaQuery()
                .eq(FlashSaleActivityEntity::getDelFlag, DelFlagEnum.NORMAL.value())
                .in(FlashSaleActivityEntity::getId, activicyIds));
        return entities.stream().map(FlashSaleActivityEntity::getPayValidTime).distinct().min(Integer::compareTo).get();
    }

    /**
     * 限时购活动定时开始
     *
     * @param time 定时执行时间
     * @date 2020年10月20日
     * @author xuzhch
     */

    @Override
    public void startFlashSalelActivityTiming(@RequestParam("time") Long time) {
        String key = "flash_sale_start_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date(time);
                //构造查询条件
                LambdaQueryWrapper<FlashSaleActivityEntity> wrapper = Wrappers.<FlashSaleActivityEntity>lambdaQuery();
                wrapper.eq(FlashSaleActivityEntity::getDelFlag, DelFlagEnum.NORMAL.value());
                wrapper.le(FlashSaleActivityEntity::getActivityStartDate, now);
                wrapper.gt(FlashSaleActivityEntity::getActivityEndDate, now);
                wrapper.eq(FlashSaleActivityEntity::getActivityState, FlashSaleActivityEnum.ACTIVITY_STATE_PREPARE.value());
                // 1.查询修改的活动数据
                List<FlashSaleActivityEntity> activityEntities = baseDao.selectList(wrapper);
                List<Long> activityIds = activityEntities.stream().map(FlashSaleActivityEntity::getId).collect(Collectors.toList());
                // 2.修改活动状态
                FlashSaleActivityEntity entity = new FlashSaleActivityEntity();
                entity.setActivityState(FlashSaleActivityEnum.ACTIVITY_STATE_START.value());
                baseDao.update(entity, wrapper);
                // 3.更新商品es活动状态
                this.updateEsSpecActivity(activityEntities, FlashSaleActivityEnum.ACTIVITY_STATE_START.value());
                // 4.更新购物车es活动状态
                this.updateCartEsSpecActivity(activityEntities, FlashSaleActivityEnum.ACTIVITY_STATE_START.value());
//                if (CollectionUtils.isNotEmpty(activityIds)) {
//                    // 3.更新商品es活动状态
//                    activityGoodsService.updateSpecIndexByActivityId(activityIds, ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
//                    // 4.更新购物车商品es活动状态
//                    activityGoodsService.updateCartIndexByActivityId(activityIds, ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
//                }
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("定时开始拼团异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }
    }

    /**
     * 限时购活动定时结束
     *
     * @param time 定时执行时间
     * @date 2020年10月20日
     * @author xuzhch
     **/

    @Override
    public void stopFlashSaleActivityTiming(@RequestParam("time") Long time) {
        String key = "flash_sale_stop_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date(time);
                //构造查询条件
                LambdaQueryWrapper<FlashSaleActivityEntity> wrapper = Wrappers.<FlashSaleActivityEntity>lambdaQuery();
                wrapper.eq(FlashSaleActivityEntity::getDelFlag, DelFlagEnum.NORMAL.value());
                wrapper.le(FlashSaleActivityEntity::getActivityEndDate, now);
                wrapper.in(FlashSaleActivityEntity::getActivityState, FlashSaleActivityEnum.ACTIVITY_STATE_PREPARE.value(), FlashSaleActivityEnum.ACTIVITY_STATE_START.value());
                // 查询修改的活动数据
                List<FlashSaleActivityEntity> activityEntities = baseDao.selectList(wrapper);
                //构造修改条件
                FlashSaleActivityEntity entity = new FlashSaleActivityEntity();
                entity.setActivityState(FlashSaleActivityEnum.ACTIVITY_STATE_END.value());
                // 修改活动状态
                baseDao.update(entity, wrapper);
                // 释放商品库存
                this.addGoodsSpecStorage(activityEntities);
                if (CollectionUtils.isNotEmpty(activityEntities)) {
                    // 更新商品es活动状态
                    this.updateEsSpecActivity(activityEntities, FlashSaleActivityEnum.ACTIVITY_STATE_END.value());
                    this.updateCartEsSpecActivity(activityEntities, FlashSaleActivityEnum.ACTIVITY_STATE_END.value());
                }
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("定时停止拼团异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }
    }


    /**
     * 增加商品规格库存
     *
     * @param activityEntities 活动信息
     * @date 2020年10月20日
     * @author xuzhch
     **/
    private void addGoodsSpecStorage(List<FlashSaleActivityEntity> activityEntities) {
        // 遍历限时抢购活动 - 获取活动商品 - 封装增加商品库存参数 - 修改库存
        Map<String, String> params = new HashMap<>();
        activityEntities.forEach(activityEntity -> {
            // 查询限时抢购商品
            if (SeckillActivityEnum.ACTIVITY_STATE_END.value() != activityEntity.getActivityState()) {
                // 已结束不释放库存
                List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(activityEntity.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                // 遍历活动商品 - 封装商品库存增加参数
                if (CollectionUtils.isNotEmpty(activityGoodsList)) {
                    activityGoodsList.forEach(activityGoodsDTO -> params.put(activityGoodsDTO.getSpecId().toString(), String.valueOf(-activityGoodsDTO.getActivitySurplusStorage())));
                }
            }
        });
        if (params.size() > 0) {
            // 增加商品库存
            goodsSpecService.updateStorage(params, 1);
        }
    }

    /**
     * 功能描述：
     * <更新商品es活动状态>
     *
     * @param activityEntities 活动集合
     * @param activityState    活动修改后状态
     * @return
     * @date 2020/3/12 11:58
     * @author 刘远杰
     **/
    @Retryable(value = EsConflictException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void updateEsSpecActivity(List<FlashSaleActivityEntity> activityEntities, Integer activityState) {
        // 遍历活动 更新商品es
        activityEntities.forEach(activityEntity -> {
            // 查询限时抢购活动商品
            List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(activityEntity.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());

            if (activityState == FlashSaleActivityEnum.ACTIVITY_STATE_END.value()) {
                List<Map<String, Object>> updateDataList = new ArrayList<>();
                // 遍历活动商品
                activityGoodsList.forEach(activityGoods -> {
                    SpecActivityVO specActivity = new SpecActivityVO();
                    // 封装活动id
                    specActivity.setActivityId(activityGoods.getActivityId());
                    // es更新对象封装
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("id", activityGoods.getSpecId().toString());
                    updateData.put("specActivityList", specActivity);
                    updateDataList.add(updateData);
                });
                if (CollectionUtils.isNotEmpty(updateDataList)) {
                    // 更新商品es
                    boolean flag = esDataUtils.deleteSubListBatchById(ElasticSearchConstant.GOODS_SPEC_INDEX, JSONArray.toJSONString(updateDataList),
                            "activityId", "specActivityList");
                    if (!flag) {
                        // 更新es异常
                        throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
                    }
                    log.info("限时抢购活动结束更新商品es成功，activityId:{},updateDataList:{}", activityEntity.getId(), updateDataList);
                }
            } else {
                List<Map<String, Object>> updateDataList = new ArrayList<>();
                activityGoodsList.forEach(activityGoods -> {
                    // 封装spec索引活动
                    SpecActivityVO specActivity = new SpecActivityVO();
                    // 活动数据封装
                    BeanCopier.create(FlashSaleActivityEntity.class, SpecActivityVO.class, false)
                            .copy(activityEntity, specActivity, null);
                    // 活动商品数据封装
                    BeanCopier.create(ActivityGoodsDTO.class, SpecActivityVO.class, false)
                            .copy(activityGoods, specActivity, null);
                    specActivity.setId(activityGoods.getId());
                    specActivity.setActivityState(activityState);
                    // es更新对象封装
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("id", activityGoods.getSpecId().toString());
                    updateData.put("specActivityList", specActivity);
                    updateDataList.add(updateData);
                });
                if (CollectionUtils.isNotEmpty(updateDataList)) {
                    // 商品es新增活动数据：不存在插入、存在修改
                    boolean flag = esDataUtils.saveSubListBatchById(ElasticSearchConstant.GOODS_SPEC_INDEX, JSONArray.toJSONString(updateDataList),
                            "activityId", "specActivityList");
                    if (!flag) {
                        // 更新es异常
                        throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
                    }
                    log.info("限时抢购活动开始、即将开始更新商品es成功，activityId:{},updateDataList:{}", activityEntity.getId(), updateDataList);
                }
            }
        });
    }

    private void updateCartEsSpecActivity(List<FlashSaleActivityEntity> flashSaleActivityEntities, Integer activityState) {
        // 遍历正在更新的限时抢购活动 更新购物车 redis活动库存价格
        flashSaleActivityEntities.forEach(flashSaleActivityEntity -> {
            // 查询限时抢购活动选择商品
            List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(flashSaleActivityEntity.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());

            if (activityState == FlashSaleActivityEnum.ACTIVITY_STATE_END.value()) {
                // 结束活动：遍历活动商品 更新购物车 redis活动库存价格
                activityGoodsList.forEach(activityGoods -> {
                    // 查询商品获得商品原价
                    GoodsSpecDTO goodsSpec = goodsSpecService.get(activityGoods.getSpecId());
                    if (goodsSpec == null) {
                        throw new ServiceException(ActivityStatusCode.NOTFUND_GOODS_SKU_EXCEPTION);
                    }

                    // es更新条件：指定规格购物车 活动id
                    PageModelDTO pageModelDTO = new PageModelDTO();
                    Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                    equalsSearchCondition.put("specId", activityGoods.getSpecId());
                    equalsSearchCondition.put("activityId", activityGoods.getActivityId());
                    equalsSearchCondition.put("activityType", activityGoods.getActivityType());

                    // 更新活动类型 恢复商品价格
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("activityType", ActivityTypeEnum.NO_ACTIVITY.value());
                    updateData.put("specSellPrice", goodsSpec.getSpecSellPrice());

                    // 更新购物车es
                    esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                    log.info("限时购活动结束更新购物车成功，activityId:{},specId:{}", activityGoods.getActivityId(), activityGoods.getSpecId());

                    // 删除redis活动库存
                    String storageKey = ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.delete(storageKey);
                    log.info("限时抢购活动结束删除redis活动库存成功，storageKey:{}", storageKey);

                    // 删除reids活动商品金额
                    String priceKey = ActivityRedisConstants.FLASH_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.delete(priceKey);
                    log.info("限时抢购活动结束删除redis活动价格成功，priceKey:{}", priceKey);
                });
            } else if (activityState == FlashSaleActivityEnum.ACTIVITY_STATE_START.value()) {
                activityGoodsList.forEach(activityGoods -> {
                    // 更新条件 指定规格购物车
                    PageModelDTO pageModelDTO = new PageModelDTO();
                    Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                    equalsSearchCondition.put("specId", activityGoods.getSpecId());

                    // 更新活动类型和价格
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("activityId", flashSaleActivityEntity.getId());
                    updateData.put("status", CartEnum.CART_STATUS_NORMAL.value());
                    updateData.put("activityType", ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                    updateData.put("activitySurplusStorage", activityGoods.getActivitySurplusStorage());
                    updateData.put("activityEndDate", flashSaleActivityEntity.getActivityEndDate());
                    updateData.put("restrictionQuantity", flashSaleActivityEntity.getRestrictionQuantity());
                    updateData.put("specSellPrice", activityGoods.getActivityPrice());

                    // 更新购物车es
                    esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                    log.info("限时抢购活动开始更新购物车成功，activityId:{},specId:{}", activityGoods.getActivityId(), activityGoods.getSpecId());

                    // 保存redis活动库存
                    String storageKey = ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(storageKey, activityGoods.getActivitySurplusStorage(), -1L);
                    log.info("限时抢购活动开始保存redis活动库存成功，storageKey:{}，value:{}", storageKey, activityGoods.getActivitySurplusStorage());

                    // 保存redis活动价格
                    String priceKey = ActivityRedisConstants.FLASH_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(priceKey, activityGoods.getActivityPrice().toString(), -1L);
                    log.info("限时抢购活动开始保存redis活动价格成功，priceKey:{}，value:{}", storageKey, activityGoods.getActivityPrice());
                });
            }
        });
    }

}
