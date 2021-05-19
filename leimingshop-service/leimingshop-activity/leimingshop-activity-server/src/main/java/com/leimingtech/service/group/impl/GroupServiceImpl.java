/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.group.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import com.leimingtech.modules.dao.group.GroupDao;
import com.leimingtech.modules.dto.activity.goods.ActivityGoodsDTO;
import com.leimingtech.modules.dto.group.GroupDTO;
import com.leimingtech.modules.entity.group.GroupEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.group.GroupRecordService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
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
import java.util.stream.Collectors;

/**
 * 拼团
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Service
@Transactional
@Slf4j
public class GroupServiceImpl extends BaseServiceImpl<GroupDao, GroupEntity> implements GroupService {

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GroupRecordService groupRecordService;

    @Override

    public PageData<GroupDTO> page(@RequestParam Map<String, Object> params) {
        IPage<GroupEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, GroupDTO.class);
    }

    /**
     * 分页查询拼团活动列表
     *
     * @param params 查询参数
     * @return
     */
    @Override

    public PageData<GroupDTO> sellerPage(@RequestParam Map<String, Object> params) {
        IPage<GroupEntity> page = getPage(params, null, false);
        //查询
        List<GroupDTO> list = baseDao.sellerPage(page, params);

        return new PageData<>(list, page.getTotal());
    }

    @Override

    public List<GroupDTO> list(Map<String, Object> params) {
        List<GroupEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, GroupDTO.class);
    }

    private QueryWrapper<GroupEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String storeId = (String) params.get("storeId");
        String activityName = (String) params.get("activityName");
        String activityState = (String) params.get("activityState");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");

        QueryWrapper<GroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        wrapper.like(StringUtils.isNotBlank(activityName), "group_name", activityName);
        wrapper.eq(StringUtils.isNotBlank(activityState), "activity_status", activityState);
        wrapper.gt(StringUtils.isNotBlank(startTime), "start_time", startTime);
        wrapper.lt(StringUtils.isNotBlank(endTime), "end_time", endTime);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    /**
     * 查询拼团活动
     *
     * @return
     * @date 2020-03-09 18:34
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public GroupDTO get(Long id) {
        GroupEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, GroupDTO.class);
    }

    /**
     * 新增拼团活动
     *
     * @return
     * @date 2020-03-09 18:34
     * @author huangkeyuan@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Boolean save(@RequestBody GroupDTO dto) {
        GroupEntity entity = ConvertUtils.sourceToTarget(dto, GroupEntity.class);
        // 默认设置审核状态为30，已通过审核；等后续增加审核流程再添加对应的逻辑；本期没有审核流程；
        entity.setAuditStatus(GroupsEnum.AUDIT_STATUS_PASS.value());
        return insert(entity);
    }

    /**
     * 修改拼团活动
     *
     * @return
     * @date 2020-03-09 18:34
     * @author huangkeyuan@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Boolean update(@RequestBody GroupDTO dto) {
        GroupEntity entity = ConvertUtils.sourceToTarget(dto, GroupEntity.class);
        // 维护商品es活动
        this.updateEsSpecActivity(Collections.singletonList(entity), GroupsEnum.ACTIVITY_STATUS_NO.value());
        return updateById(entity);
    }


    /**
     * 支付完成后修改拼团数据
     *
     * @param dto: 修改的拼团信息
     * @date 2020/4/8 15:41
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public void updateByPayEnd(@RequestBody GroupDTO dto) {
        GroupEntity entity = ConvertUtils.sourceToTarget(dto, GroupEntity.class);
        updateById(entity);
    }


    /**
     * 删除拼团活动
     *
     * @return
     * @date 2020-03-09 18:34
     * @author huangkeyuan@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Boolean delete(@RequestBody Long[] ids) {

        // 查询即将删除活动
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);

        List<GroupEntity> groupEntityList = baseDao.selectList(queryWrapper);

        // 释放商品库存
        this.addGoodsSpecStorage(groupEntityList);

        // 更新商品es活动
        updateEsSpecActivity(groupEntityList, GroupsEnum.ACTIVITY_STATUS_END.value());

        // 删除拼团商品
        activityGoodsService.deleteByActivityIds(Arrays.asList(ids), ActivityTypeEnum.GROUP_ACTIVITY.value());
        // 删除拼团活动
        //物理删除
        int flag = baseDao.deleteBatchIds(Arrays.asList(ids));

        return flag == 1 ? true : false;
    }

    /**
     * 拼团活动定时开始
     *
     * @param time 时间
     * @return
     * @date 2020-03-12 10:30
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void startGroupActivityTiming(@RequestParam("time") Long time) {
        String key = "group_start_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date(time);
                // 未开始，已审核，未删除，开始时间小于等于当前时间，结束时间大于当前时间
                UpdateWrapper<GroupEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                        .le("start_time", now).gt("end_time", now)
                        .eq("activity_status", GroupsEnum.ACTIVITY_STATUS_NO.value())
                        .eq("audit_status", GroupsEnum.AUDIT_STATUS_PASS.value());
                GroupEntity entity = new GroupEntity();
                entity.setActivityStatus(GroupsEnum.ACTIVITY_STATUS_ONGOING.value());


                // 1.查询修改的活动数据
                List<GroupEntity> groupEntityList = baseDao.selectList(updateWrapper);

                // 2.修改活动状态
                baseDao.update(entity, updateWrapper);

                if (CollectionUtils.isNotEmpty(groupEntityList)) {
                    // 3.更新商品es活动状态
                    updateEsSpecActivity(groupEntityList, GroupsEnum.ACTIVITY_STATUS_ONGOING.value());
                }
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("定时开始拼团异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }


    }

    /**
     * 拼团活动定时结束
     *
     * @param time 时间
     * @return
     * @date 2020-03-12 10:42
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void stopGroupActivityTiming(@RequestParam("time") Long time) {
        String key = "group_stop_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date(time);
                // 未开始或已开始，已审核，未删除，结束时间小于等于当前时间
                UpdateWrapper<GroupEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value()).le("end_time", now)
                        .in("activity_status", GroupsEnum.ACTIVITY_STATUS_NO.value(), GroupsEnum.ACTIVITY_STATUS_ONGOING.value())
                        .eq("audit_status", GroupsEnum.AUDIT_STATUS_PASS.value());
                GroupEntity groupEntity = new GroupEntity();
                groupEntity.setActivityStatus(GroupsEnum.ACTIVITY_STATUS_END.value());

                // 查询修改的活动数据
                List<GroupEntity> groupEntityList = baseDao.selectList(updateWrapper);

                // 释放商品库存
                this.addGoodsSpecStorage(groupEntityList);

                // 修改活动状态
                baseDao.update(groupEntity, updateWrapper);


                if (CollectionUtils.isNotEmpty(groupEntityList)) {
                    // 更新商品es活动状态
                    updateEsSpecActivity(groupEntityList, GroupsEnum.ACTIVITY_STATUS_END.value());

                    // 更新拼团记录中未完成拼团的数据为拼团失败，并取消订单
                    List<Long> groupIds = groupEntityList.stream().map(GroupEntity::getId).collect(Collectors.toList());
                    log.info("活动终止，更新拼团记录中的信息，groupIds：{}", groupIds);
                    groupRecordService.updateRecord(groupIds);
                }
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("定时停止拼团异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }


    }

    /**
     * 拼团活动定时预热开始
     *
     * @param time 时间
     * @return
     * @date 2020-03-16 10:30
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void preheatGroupActivityTiming(@RequestParam("time") Long time) {
        String key = "group_preheat_timing";
        if (redisUtils.tryLock(key)) {
            try {
                // 1.查询修改的活动数据
                List<GroupEntity> groupEntityList = baseDao.selectPreheatList();

                if (CollectionUtils.isNotEmpty(groupEntityList)) {
                    // 2.更新商品es活动状态
                    updateEsSpecActivity(groupEntityList, GroupsEnum.ACTIVITY_STATUS_NO.value());
                }
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("定时预热拼团异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }


    }


    /**
     * 根据活动id和店铺id进行查询
     *
     * @param activityId: 活动ID
     * @param storeId:    店铺ID
     * @return 拼团活动实体
     * @date 2020-03-16 10:30
     * @author huangkeyuan@leimingtech.com
     */
    @Override
    public GroupDTO findByIdAndStoreId(Long activityId, Long storeId) {
        GroupEntity entity = baseDao.selectOne(Wrappers.<GroupEntity>lambdaQuery()
                .eq(activityId != null, GroupEntity::getId, activityId)
                .eq(storeId != null, GroupEntity::getStoreId, storeId));
        return ConvertUtils.sourceToTarget(entity, GroupDTO.class);
    }

    /**
     * 停止拼团活动
     *
     * @param id      拼团id
     * @param storeId 店铺id
     * @return
     * @date 2020-04-07 17:01
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public Boolean stop(@RequestParam(value = "id") Long id,
                        @RequestParam(value = "storeId", required = false) Long storeId) {
        // 构造条件
        UpdateWrapper<GroupEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).eq(storeId != null, "store_id", storeId);
        // 修改参数
        GroupEntity entity = new GroupEntity();
        entity.setActivityStatus(GroupsEnum.ACTIVITY_STATUS_END.value());

        GroupEntity groupEntity = baseDao.selectOne(updateWrapper);
        if (groupEntity != null) {
            // 释放商品库存
            this.addGoodsSpecStorage(Collections.singletonList(groupEntity));
            // 1.修改活动状态
            int count = baseDao.update(entity, updateWrapper);

            if (count > 0) {
                // 更新商品es活动状态
                this.updateEsSpecActivity(Collections.singletonList(groupEntity), SeckillActivityEnum.ACTIVITY_STATE_END.value());

                // 更新拼团记录中未完成拼团的数据为拼团失败，并取消订单
                List<Long> groupIds = new ArrayList<>();
                groupIds.add(groupEntity.getId());
                groupRecordService.updateRecord(groupIds);
            }
            return count > 0;
        } else {
            return false;
        }
    }


    /**
     * 更新商品es活动状态
     *
     * @param groupEntityList 拼团活动集合
     * @param activityState   活动修改后的状态
     * @return
     * @date 2020-03-13 15:54
     * @author huangkeyuan@leimingtech.com
     **/
    @Retryable(value = EsConflictException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void updateEsSpecActivity(List<GroupEntity> groupEntityList, Integer activityState) {

        // 遍历活动，查询活动商品,封装spec es更新数据
        groupEntityList.forEach(groupEntity -> {
            // 查询活动商品
            List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(groupEntity.getId()), ActivityTypeEnum.GROUP_ACTIVITY.value());

            // 拼团活动已结束
            if (activityState == GroupsEnum.ACTIVITY_STATUS_END.value()) {
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
                    log.info("拼团活动结束更新商品es成功，activityId:{},updateDataList:{}", groupEntity.getId(), updateDataList);

                }

                for (ActivityGoodsDTO activityGoodsDTO : activityGoodsList) {
                    // 删除redis活动库存
                    String key = ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + activityGoodsDTO.getActivityId() + "_" + activityGoodsDTO.getSpecId();
                    redisUtils.delete(key);

                    // 删除reids活动商品金额
                    String priceKey = ActivityRedisConstants.GROUP_GOODS_PRICE + activityGoodsDTO.getActivityId() + "_" + activityGoodsDTO.getSpecId();
                    redisUtils.delete(priceKey);
                }

            } else {
                List<Map<String, Object>> updateDataList = new ArrayList<>();
                activityGoodsList.forEach(activityGoods -> {
                    // 封装spec索引活动
                    SpecActivityVO specActivity = new SpecActivityVO();
                    // 活动数据封装
                    BeanCopier.create(GroupEntity.class, SpecActivityVO.class, false).copy(groupEntity, specActivity, null);
                    // 活动商品数据封装
                    BeanCopier.create(ActivityGoodsDTO.class, SpecActivityVO.class, false)
                            .copy(activityGoods, specActivity, null);

                    specActivity.setId(activityGoods.getId());
                    specActivity.setActivityState(activityState);
                    specActivity.setActivityStartDate(groupEntity.getStartTime());
                    specActivity.setActivityEndDate(groupEntity.getEndTime());
                    specActivity.setRestrictionQuantity(activityGoods.getOnceBuyLimit());

                    // es更新对象封装
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("id", activityGoods.getSpecId().toString());
                    updateData.put("specActivityList", specActivity);
                    updateDataList.add(updateData);

                    // 保存拼团活动商品库存
                    // 保存redis活动库存
                    String storageKey = ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(storageKey, activityGoods.getActivitySurplusStorage(), -1);

                    // 保存redis活动价格
                    String priceKey = ActivityRedisConstants.GROUP_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(priceKey, activityGoods.getActivityPrice().toString(), -1L);
                });

                if (CollectionUtils.isNotEmpty(updateDataList)) {
                    // 更新商品es
                    boolean flag = esDataUtils.saveSubListBatchById(ElasticSearchConstant.GOODS_SPEC_INDEX, JSONArray.toJSONString(updateDataList),
                            "activityId", "specActivityList");
                    if (!flag) {
                        // 更新es异常
                        throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
                    }
                    log.info("拼团活动开始、即将开始更新商品es成功，activityId:{}", groupEntity.getId());

                }
            }
        });
    }

    @Recover
    public void recover(EsConflictException e) {
        log.error("修改拼团商品es异常：" + e.getMessage());
        throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
    }

    /**
     * 增加商品规格库存
     *
     * @param groupEntityList 拼团活动集合
     * @return
     * @date 2020-04-01 14:12
     * @author huangkeyuan@leimingtech.com
     **/
    private void addGoodsSpecStorage(List<GroupEntity> groupEntityList) {

        // 遍历拼团活动 - 获取活动商品 - 封装增加商品库存参数 - 修改库存
        Map<String, String> params = new HashMap<>();
        groupEntityList.forEach(groupEntity -> {
            // 查询拼团商品
            if (GroupsEnum.ACTIVITY_STATUS_END.value() != groupEntity.getActivityStatus()) {
                // 已结束不释放库存
                List<ActivityGoodsDTO> activityGoodsList = activityGoodsService.getByActivityIds(Collections.singletonList(groupEntity.getId()), ActivityTypeEnum.GROUP_ACTIVITY.value());

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
