/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.seckill.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.EsConflictException;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.seckill.SeckillActivityDao;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.activity.goods.ActivityGoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.seckill.AdminSeckillActivityDetailDTO;
import com.leimingtech.modules.dto.seckill.AdminSeckillActivityPageDTO;
import com.leimingtech.modules.dto.seckill.SeckillActivityDTO;
import com.leimingtech.modules.entity.seckill.SeckillActivityEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.seckill.SeckillActivityService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 秒杀活动表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)

public class SeckillActivityServiceImpl extends BaseServiceImpl<SeckillActivityDao, SeckillActivityEntity> implements SeckillActivityService {

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Override

    public PageData<SeckillActivityDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SeckillActivityEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SeckillActivityDTO.class);
    }

    /**
     * 功能描述：
     * <后台秒杀活动分页查询>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/9 16:44
     * @author 刘远杰
     **/
    @Override

    public PageData<AdminSeckillActivityPageDTO> adminPage(@RequestParam Map<String, Object> params) {
        IPage<SeckillActivityEntity> page = getPage(params, null, false);
        //查询
        List<AdminSeckillActivityPageDTO> list = baseDao.adminPage(page, params);

        return new PageData<>(list, page.getTotal());
    }

    @Override

    public List<SeckillActivityDTO> list(@RequestParam Map<String, Object> params) {
        List<SeckillActivityEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SeckillActivityDTO.class);
    }

    private QueryWrapper<SeckillActivityEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<SeckillActivityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public SeckillActivityDTO get(Long id) {
        SeckillActivityEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SeckillActivityDTO.class);
    }

    /**
     * 功能描述：
     * <后台秒杀活动详情>
     *
     * @param id      秒杀活动id
     * @param storeId 店铺id
     * @return
     * @date 2020/3/9 17:35
     * @author 刘远杰
     **/
    @Override

    public AdminSeckillActivityDetailDTO adminDetail(@RequestParam("id") Long id,
                                                     @RequestParam(value = "storeId", required = false) Long storeId) {
        // 条件构造
        QueryWrapper<SeckillActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq(storeId != null, "store_id", storeId);

        SeckillActivityEntity entity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(entity, AdminSeckillActivityDetailDTO.class);
    }

    /**
     * 功能描述：
     * <查询店铺指定id秒杀活动>
     *
     * @param id      秒杀活动id
     * @param storeId 店铺id
     * @return
     * @date 2020/3/9 15:30
     * @author 刘远杰
     **/
    @Override

    public SeckillActivityDTO getByIdOrStoreId(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "storeId", required = false) Long storeId) {
        // 条件构造
        QueryWrapper<SeckillActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq(storeId != null, "store_id", storeId);

        SeckillActivityEntity entity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(entity, SeckillActivityDTO.class);
    }

    @Override

    public Boolean save(@RequestBody SeckillActivityDTO dto) {
        SeckillActivityEntity entity = ConvertUtils.sourceToTarget(dto, SeckillActivityEntity.class);

        return insert(entity);
    }

    @Override

    public Boolean update(@RequestBody SeckillActivityDTO dto) {
        SeckillActivityEntity entity = ConvertUtils.sourceToTarget(dto, SeckillActivityEntity.class);

        return updateById(entity);
    }

    /**
     * 功能描述：
     * <秒杀活动浏览记录+1>
     *
     * @param ids
     * @return
     * @date 2020/3/16 16:09
     * @author 刘远杰
     **/
    @Override

    public void increaseBrowserNum(@RequestBody List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            baseDao.increaseBrowserNum(ids);
        }
    }

    /**
     * 功能描述：
     * <秒杀活动订单+1>
     *
     * @param ids
     * @return
     * @date 2020/3/30
     * @author 刘远杰
     **/
    @Override

    public void increaseOrderNum(@RequestBody List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            baseDao.increaseOrderNum(ids);
        }
    }

    /**
     * 功能描述：
     * <编辑秒杀活动>
     *
     * @param dto 编辑秒杀活动实体
     * @return
     * @date 2020/3/9 16:20
     * @author 刘远杰
     **/
    @Override

    public Boolean editSeckillActivity(@RequestBody SeckillActivityDTO dto) {
        SeckillActivityEntity entity = ConvertUtils.sourceToTarget(dto, SeckillActivityEntity.class);
        int count = baseDao.editSeckillActivity(entity);

        if (count > 0) {
            // 场次发生变化则需要更改秒杀商品对应的场次
            // 查询秒杀活动商品
            List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(entity.getId()), ActivityTypeEnum.SECKILL_ACTIVITY.value());
            if (CollectionUtils.isNotEmpty(activityGoodsList)) {
                // 如果编辑保存的时候，sessionId发生变化，则同步修改秒杀商品对应的SessionId
                if (!activityGoodsList.get(0).getSessionId().equals(entity.getSessionId())) {
                    activityGoodsList.forEach(activityGoodsDTO -> {
                        activityGoodsDTO.setSessionId(entity.getSessionId());
                    });

                    boolean flag = activityGoodsService.updateBatch(activityGoodsList);
                    if (!flag) {
                        log.info("秒杀商品更新异常！");
                        return false;
                    }
                }
            }

            // 维护商品es活动
            this.updateEsSpecActivity(Collections.singletonList(entity), entity.getActivityState());
            // 更新购物车es活动状态
            this.updateCartEsSpecActivity(Collections.singletonList(entity), entity.getActivityState());
        }
        return count > 0;
    }

    /**
     * 功能描述：
     * <秒杀活动删除 删除活动及活动商品 更新购物车商品es>
     *
     * @param ids 活动id集合
     * @return
     * @date 2020/3/25 14:32
     * @author 刘远杰
     **/
    @Override

    public void delete(@RequestBody Long[] ids) {
        // 1.查询即将删除秒杀活动
        QueryWrapper<SeckillActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<SeckillActivityEntity> seckillActivityList = baseDao.selectList(queryWrapper);

        // 2.释放活动库存
        this.addGoodsSpecStorage(seckillActivityList);

        // 3.更新秒杀商品es活动
        updateEsSpecActivity(seckillActivityList, SeckillActivityEnum.ACTIVITY_STATE_END.value());
        // 4.更新购物车es活动状态
        this.updateCartEsSpecActivity(seckillActivityList, SeckillActivityEnum.ACTIVITY_STATE_END.value());

        // 5.删除秒杀商品
        activityGoodsService.deleteByActivityIds(Arrays.asList(ids), ActivityTypeEnum.SECKILL_ACTIVITY.value());
        // 6.删除秒杀活动
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述：
     * <秒杀活动停止>
     *
     * @param id      秒杀活动id
     * @param storeId 店铺id
     * @return
     * @date 2020/3/9 17:50
     * @author 刘远杰
     **/
    @Override

    public Boolean stop(@RequestParam(value = "id") Long id,
                        @RequestParam(value = "storeId", required = false) Long storeId) {
        // 构造条件
        UpdateWrapper<SeckillActivityEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).eq(storeId != null, "store_id", storeId);
        // 修改参数
        SeckillActivityEntity entity = new SeckillActivityEntity();
        entity.setActivityState(SeckillActivityEnum.ACTIVITY_STATE_END.value());

        SeckillActivityEntity seckillActivityEntity = baseDao.selectOne(updateWrapper);
        if (seckillActivityEntity != null) {
            // 释放商品库存
            this.addGoodsSpecStorage(Collections.singletonList(seckillActivityEntity));
            // 1.修改活动状态
            int count = baseDao.update(entity, updateWrapper);

            if (count > 0) {
                // 更新商品es活动状态
                this.updateEsSpecActivity(Collections.singletonList(seckillActivityEntity), SeckillActivityEnum.ACTIVITY_STATE_END.value());
                // 更新购物车es活动状态
                this.updateCartEsSpecActivity(Collections.singletonList(seckillActivityEntity), SeckillActivityEnum.ACTIVITY_STATE_END.value());
            }
            return count > 0;
        } else {
            return false;
        }
    }

    /**
     * 功能描述：
     * <秒杀活动定时开始>
     *
     * @param time 定时执行时间
     * @return
     * @date 2020/3/11 17:07
     * @author 刘远杰
     **/

    @Override
    public void startSeckillActivityTiming(@RequestParam("time") Long time) {
        String key = "seckill_start_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date(time);
                // 未开始，已审核，未删除，开始时间小于等于当前时间，结束时间大于当前时间
                UpdateWrapper<SeckillActivityEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                        .le("activity_start_date", now).gt("activity_end_date", now)
                        .eq("activity_state", SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value())
                        .eq("audit_state", SeckillActivityEnum.AUDIT_STATE_PASS.value());
                SeckillActivityEntity entity = new SeckillActivityEntity();
                entity.setActivityState(SeckillActivityEnum.ACTIVITY_STATE_START.value());

                // 1.查询修改的活动数据
                List<SeckillActivityEntity> seckillActivityList = baseDao.selectList(updateWrapper);

                // 2.修改活动状态
                baseDao.update(entity, updateWrapper);

                // 3.更新商品es活动状态
                updateEsSpecActivity(seckillActivityList, SeckillActivityEnum.ACTIVITY_STATE_START.value());
                // 4.更新购物车es活动状态
                this.updateCartEsSpecActivity(seckillActivityList, SeckillActivityEnum.ACTIVITY_STATE_START.value());
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }
    }

    /**
     * 功能描述：
     * <秒杀活动定时结束>
     *
     * @param time 定时执行时间
     * @return
     * @date 2020/3/11 17:07
     * @author 刘远杰
     **/

    @Override
    public void stopSeckillActivityTiming(@RequestParam("time") Long time) {
        String key = "seckill_stop_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date(time);
                // 未开始或已开始，已审核，未删除，结束时间小于等于当前时间
                UpdateWrapper<SeckillActivityEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value()).le("activity_end_date", now)
                        .in("activity_state", CouponsEnum.ACTIVITY_STATE_PREPARE.value(), CouponsEnum.ACTIVITY_STATE_START.value())
                        .eq("audit_state", CouponsEnum.AUDIT_STATE_PASS.value());
                SeckillActivityEntity entity = new SeckillActivityEntity();
                entity.setActivityState(SeckillActivityEnum.ACTIVITY_STATE_END.value());

                // 1.查询修改的活动数据
                List<SeckillActivityEntity> seckillActivityList = baseDao.selectList(updateWrapper);

                // 2.释放商品库存
                this.addGoodsSpecStorage(seckillActivityList);

                // 3.修改活动状态
                baseDao.update(entity, updateWrapper);

                // 4.更新商品es活动状态
                updateEsSpecActivity(seckillActivityList, SeckillActivityEnum.ACTIVITY_STATE_END.value());
                // 5.更新购物车es活动状态
                this.updateCartEsSpecActivity(seckillActivityList, SeckillActivityEnum.ACTIVITY_STATE_END.value());
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("异常回滚尝试：{0}", e);
            }

            // 释放锁
            redisUtils.releaseLock(key);
        }

    }

    /**
     * 功能描述：
     * <更新商品es活动状态>
     *
     * @param seckillActivityList 秒杀活动集合
     * @param activityState       活动修改后状态
     * @return
     * @date 2020/3/12 11:58
     * @author 刘远杰
     **/
    @Retryable(value = EsConflictException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void updateEsSpecActivity(List<SeckillActivityEntity> seckillActivityList, Integer activityState) {
        // 遍历活动 更新商品es
        seckillActivityList.forEach(seckillActivity -> {
            // 查询秒杀活动商品
            List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(seckillActivity.getId()), ActivityTypeEnum.SECKILL_ACTIVITY.value());

            if (activityState == SeckillActivityEnum.ACTIVITY_STATE_END.value()) {
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
                    log.info("秒杀活动结束更新商品es成功，activityId:{},updateDataList:{}", seckillActivity.getId(), updateDataList);
                }
            } else {
                List<Map<String, Object>> updateDataList = new ArrayList<>();
                activityGoodsList.forEach(activityGoods -> {
                    // 封装spec索引活动
                    SpecActivityVO specActivity = new SpecActivityVO();
                    // 活动数据封装
                    BeanCopier.create(SeckillActivityEntity.class, SpecActivityVO.class, false)
                            .copy(seckillActivity, specActivity, null);
                    // 活动商品数据封装
                    BeanCopier.create(ActivityGoodsDTO.class, SpecActivityVO.class, false)
                            .copy(activityGoods, specActivity, null);
                    specActivity.setId(activityGoods.getId());
                    specActivity.setActivityState(activityState);
                    specActivity.setSessionId(seckillActivity.getSessionId());

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
                    log.info("秒杀活动开始、即将开始更新商品es成功，activityId:{},updateDataList:{}", seckillActivity.getId(), updateDataList);
                }
            }
        });
    }

    @Recover
    public void recover(EsConflictException e) {
        log.error("修改秒杀商品es异常：" + e.getMessage());
        throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
    }

    /**
     * 功能描述：
     * <更新购物车es活动状态>
     *
     * @param seckillActivityList 秒杀活动集合
     * @param activityState       活动修改后状态
     * @return
     * @date 2020/3/12 11:58
     * @author 刘远杰
     **/
    private void updateCartEsSpecActivity(List<SeckillActivityEntity> seckillActivityList, Integer activityState) {
        // 遍历正在更新的秒杀活动 更新购物车 redis活动库存价格
        seckillActivityList.forEach(seckillActivity -> {
            // 查询秒杀活动选择商品
            List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(seckillActivity.getId()), ActivityTypeEnum.SECKILL_ACTIVITY.value());

            if (activityState == SeckillActivityEnum.ACTIVITY_STATE_END.value()) {
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
                    log.info("秒杀活动结束更新购物车成功，activityId:{},specId:{}", activityGoods.getActivityId(), activityGoods.getSpecId());

                    // 删除redis活动库存
                    String storageKey = ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.delete(storageKey);
                    log.info("秒杀活动结束删除redis活动库存成功，storageKey:{}", storageKey);

                    // 删除reids活动商品金额
                    String priceKey = ActivityRedisConstants.SECKILL_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.delete(priceKey);
                    log.info("秒杀活动结束删除redis活动价格成功，priceKey:{}", priceKey);
                });
            } else if (activityState == SeckillActivityEnum.ACTIVITY_STATE_START.value()) {
                activityGoodsList.forEach(activityGoods -> {
                    // 更新条件 指定规格购物车
                    PageModelDTO pageModelDTO = new PageModelDTO();
                    Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                    equalsSearchCondition.put("specId", activityGoods.getSpecId());

                    // 更新活动类型和价格
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("activityId", seckillActivity.getId());
                    updateData.put("status", CartEnum.CART_STATUS_NORMAL.value());
                    updateData.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                    updateData.put("activitySurplusStorage", activityGoods.getActivitySurplusStorage());
                    updateData.put("activityEndDate", seckillActivity.getActivityEndDate());
                    updateData.put("restrictionQuantity", seckillActivity.getRestrictionQuantity());
                    updateData.put("specSellPrice", activityGoods.getActivityPrice());

                    // 更新购物车es
                    esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                    log.info("秒杀活动开始更新购物车成功，activityId:{},specId:{}", activityGoods.getActivityId(), activityGoods.getSpecId());

                    // 保存redis活动库存
                    String storageKey = ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(storageKey, activityGoods.getActivitySurplusStorage(), -1L);
                    log.info("秒杀活动开始保存redis活动库存成功，storageKey:{}，value:{}", storageKey, activityGoods.getActivitySurplusStorage());

                    // 保存redis活动价格
                    String priceKey = ActivityRedisConstants.SECKILL_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(priceKey, activityGoods.getActivityPrice().toString(), -1L);
                    log.info("秒杀活动开始保存redis活动价格成功，priceKey:{}，value:{}", storageKey, activityGoods.getActivityPrice());
                });
            }
        });
    }


    /**
     * 功能描述：
     * <增加商品规格库存>
     *
     * @param seckillActivityList 秒杀活动集合
     * @return
     * @date 2020/4/1 10:46
     * @author 刘远杰
     **/
    private void addGoodsSpecStorage(List<SeckillActivityEntity> seckillActivityList) {
        // 遍历秒杀活动 - 获取活动商品 - 封装增加商品库存参数 - 修改库存
        Map<String, String> params = new HashMap<>();
        seckillActivityList.forEach(seckillActivity -> {
            // 查询秒杀商品
            if (SeckillActivityEnum.ACTIVITY_STATE_END.value() != seckillActivity.getActivityState()) {
                // 已结束不释放库存
                List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(seckillActivity.getId()), ActivityTypeEnum.SECKILL_ACTIVITY.value());

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
}
