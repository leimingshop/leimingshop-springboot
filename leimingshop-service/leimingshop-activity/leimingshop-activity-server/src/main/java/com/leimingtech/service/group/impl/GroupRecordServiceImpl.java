/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.group.impl;

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
import com.leimingtech.dto.reason.ReasonDescriptionDTO;
import com.leimingtech.dto.setting.SettingDefaultAvatarsDTO;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.group.GroupRecordDao;
import com.leimingtech.modules.dto.activity.goods.ActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.SpecActivityDTO;
import com.leimingtech.modules.dto.group.*;
import com.leimingtech.modules.dto.order.GroupOrderDetailDTO;
import com.leimingtech.modules.dto.order.OrderCancelDTO;
import com.leimingtech.modules.entity.group.GroupRecordEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.enums.order.OrderGroupStatusEnum;
import com.leimingtech.modules.enums.order.OrderStatusEnum;
import com.leimingtech.modules.enums.order.PaymentStatusEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.group.GroupMemberService;
import com.leimingtech.modules.service.group.GroupRecordService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.service.reason.ReasonDescriptionService;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 拼团记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Service
@Transactional
@Slf4j
public class GroupRecordServiceImpl extends BaseServiceImpl<GroupRecordDao, GroupRecordEntity> implements GroupRecordService {


    @Autowired
    private OrderService orderService;

    @Resource
    private GroupService groupService;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private ReasonDescriptionService reasonDescriptionService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private SettingService settingService;

    @Override

    public PageData<GroupRecordDTO> page(@RequestParam Map<String, Object> params) {
        IPage<GroupRecordEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );
        PageData<GroupRecordDTO> pageData = getPageData(page, GroupRecordDTO.class);
        Date date = new Date();
        pageData.getList().stream().forEach(groupRecordDTO -> {
            groupRecordDTO.setCurrentTime(date);
        });
        return pageData;
    }

    @Override

    public List<GroupRecordDTO> list(Map<String, Object> params) {
        List<GroupRecordEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, GroupRecordDTO.class);
    }

    private QueryWrapper<GroupRecordEntity> getWrapper(Map<String, Object> params) {
        String storeId = (String) params.get("storeId");
        String activityRecordId = (String) params.get("activityRecordId");
        String groupId = "";
        if (null != params.get("groupId")) {
            groupId = params.get("groupId").toString();
        }

        String activityName = (String) params.get("activityName");
        String memberId = (String) params.get("memberId");
        String groupStatus = (String) params.get("groupStatus");
        String goodsId = (String) params.get("goodsId");


        QueryWrapper<GroupRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        wrapper.eq(StringUtils.isNotBlank(activityRecordId), "id", activityRecordId);
        wrapper.eq(StringUtils.isNotBlank(groupId), "group_id", groupId);
        wrapper.like(StringUtils.isNotBlank(activityName), "group_name", activityName);
        wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);
        wrapper.eq(StringUtils.isNotBlank(groupStatus), "group_status", groupStatus);
        wrapper.eq(StringUtils.isNotBlank(goodsId), "goods_id", goodsId);

        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public GroupRecordDTO get(Long id) {
        GroupRecordEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, GroupRecordDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody GroupRecordDTO dto) {
        GroupRecordEntity entity = ConvertUtils.sourceToTarget(dto, GroupRecordEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody GroupRecordDTO dto) {
        GroupRecordEntity entity = ConvertUtils.sourceToTarget(dto, GroupRecordEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, GroupRecordEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * (seller端)查询拼团记录详情
     *
     * @param storeId       店铺id
     * @param groupRecordId 拼团活动记录id
     * @return
     * @date 2020-03-17 11:09
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public GroupRecordOrderDTO getGroupOrderDetail(@RequestParam("storeId") Long storeId, @RequestParam("groupRecordId") Long groupRecordId) {

        GroupRecordOrderDTO groupRecordOrderDTO = new GroupRecordOrderDTO();
        // 查询拼团记录的基本详情信息
        GroupRecordDTO groupRecordDTO = this.get(groupRecordId);
        // 数据转换处理
        BeanCopier.create(GroupRecordDTO.class, GroupRecordOrderDTO.class, false)
                .copy(groupRecordDTO, groupRecordOrderDTO, null);

        List<GroupOrderListDTO> orderList = new ArrayList<>();
        // 查询该记录关联的订单数据(过滤掉未支付的订单，只有支付完成才能算参与拼团活动)
        Map<String, Object> params = new HashMap<>();
        params.put("storeId", storeId);
        params.put("groupRecordId", groupRecordId);
        params.put("paymentStatus", PaymentStatusEnum.YES.value());
        List<GroupOrderDetailDTO> groupOrderDetailDTOList = orderService.findGroupOrderList(params);

        if (CollectionUtils.isNotEmpty(groupOrderDetailDTOList)) {
            groupOrderDetailDTOList.forEach(groupOrderDetailDTO -> {
                GroupOrderListDTO groupOrderListDTO = new GroupOrderListDTO();
                // 活动商品数据封装
                BeanCopier.create(GroupOrderDetailDTO.class, GroupOrderListDTO.class, false)
                        .copy(groupOrderDetailDTO, groupOrderListDTO, null);
                orderList.add(groupOrderListDTO);
            });
        }
        groupRecordOrderDTO.setGroupOrderListDTOList(orderList);
        return groupRecordOrderDTO;
    }

    /**
     * (拼团中)快速成团
     *
     * @param storeId       店铺id
     * @param groupRecordId 拼团记录id
     * @return
     * @date 2020-03-17 17:13
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public Boolean groupComposition(@RequestParam("storeId") Long storeId, @RequestParam("groupRecordId") Long groupRecordId) {
        // 1.查询当前拼团记录状态是否是进行中，只有进行中可以快速成团
        GroupRecordDTO groupRecordDTO = this.get(groupRecordId);
        Integer groupStatus = groupRecordDTO.getGroupStatus();
        if (null != groupStatus && GroupsEnum.GROUP_STATUS_ONGOING.value() == groupStatus) {
            // 2.修改此拼团记录关联订单状态的拼团状态为拼团成功
            // 查询该记录关联的待收货订单数据
            Map<String, Object> params = new HashMap<>();
            params.put("storeId", storeId);
            params.put("groupRecordId", groupRecordId);
            params.put("orderStatus", OrderStatusEnum.WAITTING_SHIPPED.value());
            List<GroupOrderDetailDTO> groupOrderDetailDTOList = orderService.findGroupOrderList(params);

            if (CollectionUtils.isNotEmpty(groupOrderDetailDTOList)) {
                // 取出此记录的所有待收货订单id
                List<Long> orderIds = groupOrderDetailDTOList.stream().map(GroupOrderDetailDTO::getId).collect(Collectors.toList());
                // 批量修改订单的拼团状态为拼团成功
                orderService.updateGroupStatus(orderIds, OrderGroupStatusEnum.GROUP_SUCCESS.value());
            }

            // 3.修改当前拼团记录为拼团成功
            groupRecordDTO.setGroupStatus(GroupsEnum.GROUP_STATUS_SUCCESS.value());
            groupRecordDTO.setActualTime(new Date());
            groupRecordDTO.setNeedNum(0);
            this.update(groupRecordDTO);

            // 4.修改拼团活动的成团数
            GroupDTO groupDTO = groupService.get(groupRecordDTO.getGroupId());
            groupDTO.setGroupNum(groupDTO.getGroupNum() + 1);
            groupService.updateByPayEnd(groupDTO);

            return true;
        } else {
            return false;
        }

    }

    /**
     * 拼团成团超时定时任务
     *
     * @param time
     * @return
     * @date 2020-03-30 15:48
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void overTimeGroupActivityTiming(@RequestParam("time") Long time) {
        String key = "group_overtime_timing";
        if (redisUtils.tryLock(key)) {
            try {
                Date now = new Date(time);
                // 未删除,拼团进行中，超时时间小于等于当前时间
                UpdateWrapper<GroupRecordEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                        .le("over_time", now)
                        .eq("group_status", GroupsEnum.GROUP_STATUS_ONGOING.value());

                this.updateGroupList(updateWrapper);

            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("定时超时拼团异常回滚尝试：{0}", e);
            }
            redisUtils.releaseLock(key);
        }


    }

    /**
     * 取消订单
     *
     * @param storeId       店铺id
     * @param groupRecordId 拼团记录id
     * @param reasonId      取消订单原因id
     * @return
     * @date 2020-03-18 15:48
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public Boolean cancelOrder(@RequestParam("storeId") Long storeId, @RequestParam("groupRecordId") Long groupRecordId, @RequestParam("reasonId") Long reasonId) {
        // 1.查询当前拼团记录状态是否是进行中，只有进行中可以取消订单
        GroupRecordDTO groupRecordDTO = this.get(groupRecordId);
        log.info("取消拼团订单记录，groupRecordDTO：{}", groupRecordDTO);

        // 查询该记录关联的待收货、未支付订单数据
        Map<String, Object> params = new HashMap<>();
        params.put("storeId", storeId);
        params.put("groupRecordId", groupRecordId);
        List<GroupOrderDetailDTO> groupOrderDetailDTOList = orderService.findGroupOrderList(params);

        if (CollectionUtils.isNotEmpty(groupOrderDetailDTOList)) {
            // 取出此记录的所有订单id
            List<Long> orderIds = groupOrderDetailDTOList.stream().map(GroupOrderDetailDTO::getId).collect(Collectors.toList());

            // 批量取消订单
            orderIds.forEach(orderId -> {
                OrderCancelDTO orderCancelDTO = new OrderCancelDTO();
                orderCancelDTO.setId(orderId);
                orderCancelDTO.setReasonId(reasonId);
                orderService.cancelOrderSeller(orderCancelDTO, storeId);

            });

            // 批量修改订单的拼团状态为拼团失败
            orderService.updateGroupStatus(orderIds, OrderGroupStatusEnum.GROUP_FAIL.value());
        }

        // 3.修改当前拼团记录为拼团失败
        groupRecordDTO.setGroupStatus(GroupsEnum.GROUP_STATUS_FAIL.value());
        groupRecordDTO.setNeedNum(0);
        this.update(groupRecordDTO);
        return true;
    }

    /**
     * 查询用户参加拼团活动的次数
     *
     * @param memberId 会员id
     * @param groupId  拼团活动id
     * @return
     * @date 2020-03-20 15:03
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public List<GroupRecordDTO> getMemberRecord(@RequestParam("memberId") Long memberId, @RequestParam("groupId") Long groupId, @RequestParam("goodsId") Long goodsId) {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId.toString());
        params.put("groupId", groupId.toString());
        params.put("goodsId", goodsId.toString());
        List<GroupRecordEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, GroupRecordDTO.class);
    }

    /**
     * h5查询拼团记录详情
     *
     * @param groupRecordId 拼团活动记录id
     * @param memberId      用户id
     * @return
     * @date 2020-03-17 11:09
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public H5GroupDetailDTO getH5GroupOrderDetail(@RequestParam("groupRecordId") Long groupRecordId, @RequestParam("memberId") Long memberId) {

        H5GroupDetailDTO h5GroupDetailDTO = new H5GroupDetailDTO();
        // 查询拼团记录数据
        GroupRecordDTO groupRecordDTO = this.get(groupRecordId);

        if (null == groupRecordDTO) {
            return null;
        }

        // 数据转化
        BeanCopier.create(GroupRecordDTO.class, H5GroupDetailDTO.class, false).copy(groupRecordDTO, h5GroupDetailDTO, null);

        h5GroupDetailDTO.setCurrentTime(new Date());

        // 查询对应商品的正常价格和活动价格
        ActivityGoodsDTO activityGoodsDTO = activityGoodsService.goodsAndSpec(groupRecordDTO.getGroupId(), groupRecordDTO.getSpecId(), ActivityTypeEnum.GROUP_ACTIVITY.value());
        if (null == activityGoodsDTO) {
            // 活动商品不存在
            return null;
        }

        // 查询商品es 获得商品活动
        String specEsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, groupRecordDTO.getSpecId().toString());
        JSONObject specEs = JSONObject.parseObject(specEsJson);
        if (specEs.get("specActivityList") != null) {
            List<SpecActivityDTO> specActivityList = JSONArray.parseArray(specEs.get("specActivityList").toString(), SpecActivityDTO.class);
            if (CollectionUtils.isNotEmpty(specActivityList)) {
                for (SpecActivityDTO specActivityDTO : specActivityList) {
                    // 获取拼团活动
                    if (specActivityDTO.getActivityType().equals(ActivityTypeEnum.GROUP_ACTIVITY.value())) {
                        h5GroupDetailDTO.setActivityPrice(specActivityDTO.getActivityPrice());
                        h5GroupDetailDTO.setGoodsPrice(new BigDecimal(specEs.get("specSellPrice").toString()));
                        if (null != specEs.get("specAttrName")) {
                            h5GroupDetailDTO.setSpecName(specEs.get("specAttrName").toString());
                        }
                        h5GroupDetailDTO.setNeedPeople(specActivityDTO.getRegimentNum());
                        break;
                    }
                }
            } else {
                h5GroupDetailDTO.setGoodsPrice(new BigDecimal(specEs.get("specSellPrice").toString()));
                h5GroupDetailDTO.setActivityPrice(activityGoodsDTO.getActivityPrice());
                if (null != specEs.get("specAttrName")) {
                    h5GroupDetailDTO.setSpecName(specEs.get("specAttrName").toString());
                }
                h5GroupDetailDTO.setNeedPeople(activityGoodsDTO.getRegimentNum());
            }
        } else {
            h5GroupDetailDTO.setGoodsPrice(new BigDecimal(specEs.get("specSellPrice").toString()));
            h5GroupDetailDTO.setActivityPrice(activityGoodsDTO.getActivityPrice());
            if (null != specEs.get("specAttrName")) {
                h5GroupDetailDTO.setSpecName(specEs.get("specAttrName").toString());
            }
            h5GroupDetailDTO.setNeedPeople(activityGoodsDTO.getRegimentNum());
        }

        List<GroupMemberDTO> groupMemberDTOList = groupMemberService.recordList(groupRecordId);

        if (CollectionUtils.isNotEmpty(groupMemberDTOList)) {

            // 只有拼团成功或者失败需要填充默认头像
            if (GroupsEnum.GROUP_STATUS_ONGOING.value() != groupRecordDTO.getGroupStatus()) {
                // 判断填充默认头像
                if (activityGoodsDTO.getRegimentNum() > groupMemberDTOList.size()) {
                    SettingDefaultAvatarsDTO defaultAvatarsSet = settingService.getDefaultAvatarsSet();
                    if (null == defaultAvatarsSet) {
                        log.error("查询用户默认头像设置异常");
                    }
                    String defaultAvatars = String.valueOf(defaultAvatarsSet.getAvatar());
                    Integer changeFlags = activityGoodsDTO.getRegimentNum() - groupMemberDTOList.size();
                    for (int j = 0; j < changeFlags; j++) {
                        GroupMemberDTO groupMemberDTO = new GroupMemberDTO();
                        groupMemberDTO.setMemberImage(defaultAvatars);
                        groupMemberDTOList.add(groupMemberDTO);
                    }
                }
            }

            h5GroupDetailDTO.setGroupMemberDTOList(groupMemberDTOList);
        }


        Integer joinFlag = 0;
        for (GroupMemberDTO groupMemberDTO : groupMemberDTOList) {
            if (memberId.equals(groupMemberDTO.getMemberId())) {
                joinFlag = 1;
                break;
            }
        }

        h5GroupDetailDTO.setJoinFlag(joinFlag);

        return h5GroupDetailDTO;

    }


    @Override
    public List<GroupRecordDTO> groupList(@RequestParam Map<String, Object> params) {
        List<GroupRecordDTO> entityList = baseDao.selectH5GroupRecord(params);
        if (CollectionUtils.isNotEmpty(entityList)) {
            if (null != params.get("memberId")) {
                Long memberId = Long.valueOf(params.get("memberId").toString());
                // 过滤当前用户发起的拼团
                entityList = entityList.stream().filter(s -> !s.getMemberId().equals(memberId)).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(entityList)) {
                    // 过滤当前用户参与的拼团
                    Iterator<GroupRecordDTO> iterator = entityList.iterator();
                    while (iterator.hasNext()) {
                        Map<String, Object> queryMap = new HashMap<>();
                        GroupRecordDTO groupRecordDTO = iterator.next();
                        queryMap.put("storeId", groupRecordDTO.getStoreId());
                        queryMap.put("groupRecordId", groupRecordDTO.getId());
                        queryMap.put("orderStatus", OrderStatusEnum.WAITTING_SHIPPED.value());
                        queryMap.put("memberId", memberId);
                        List<GroupOrderDetailDTO> groupOrderDetailDTOList = orderService.findGroupOrderList(queryMap);
                        if (CollectionUtils.isNotEmpty(groupOrderDetailDTOList)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }

        return entityList;
    }

    /**
     * 更新涉及的拼团记录
     *
     * @param groupIds 需要更新的拼团id集合
     * @return
     * @date 2020-04-07 15:33
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void updateRecord(@RequestParam("groupIds") List<Long> groupIds) {

        UpdateWrapper<GroupRecordEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                .eq("group_status", GroupsEnum.GROUP_STATUS_ONGOING.value())
                .in("group_id", groupIds);
        this.updateGroupList(updateWrapper);
    }

    private void updateGroupList(UpdateWrapper<GroupRecordEntity> updateWrapper) {

        // 1.查询当前已到拼团活动结束时间且拼团仍在进行中的数据
        List<GroupRecordEntity> groupRecordEntityList = baseDao.selectList(updateWrapper);

        if (CollectionUtils.isNotEmpty(groupRecordEntityList)) {
            log.info("已到拼团活动结束时间且拼团仍在进行，groupRecordEntityList：{}", groupRecordEntityList);
            // 2.查询对应的拼团是否为模拟成团
            groupRecordEntityList.forEach(groupRecordEy -> {
                GroupDTO groupDTO = groupService.get(groupRecordEy.getGroupId());

                if (null != groupDTO) {
                    // 3.如果是模拟成团则进行修改拼团状态为拼团成功
                    if (groupDTO.getSimulateFlag() == 0) {
                        groupRecordEy.setGroupStatus(GroupsEnum.GROUP_STATUS_SUCCESS.value());
                        groupRecordEy.setNeedNum(0);
                        groupRecordEy.setActualTime(new Date());
                        // 修改当前拼团记录为拼团成功
                        this.updateById(groupRecordEy);

                        // 修改该拼团记录对应的订单的拼团状态为拼团成功
                        Map<String, Object> params = new HashMap<>();
                        params.put("storeId", groupRecordEy.getStoreId());
                        params.put("groupRecordId", groupRecordEy.getId());
                        params.put("orderStatus", OrderStatusEnum.WAITTING_SHIPPED.value());
                        List<GroupOrderDetailDTO> groupOrderDetailDTOList = orderService.findGroupOrderList(params);
                        log.info("模拟成团，groupOrderDetailDTOList：{}", groupOrderDetailDTOList);
                        if (CollectionUtils.isNotEmpty(groupOrderDetailDTOList)) {
                            // 取出此记录的所有待收货订单id
                            List<Long> orderIds = groupOrderDetailDTOList.stream().map(GroupOrderDetailDTO::getId).collect(Collectors.toList());
                            // 批量修改订单的拼团状态为拼团成功
                            orderService.updateGroupStatus(orderIds, OrderGroupStatusEnum.GROUP_SUCCESS.value());
                        }

                        // 4.如果不是模拟成团，则修改为拼团失败，且进行取消订单退款
                    } else {
                        // 查询取消订单的原因id
                        Map<String, Object> params = new HashMap<>();
                        // 取消订单
                        params.put("type", "3");
                        params.put("role", "1");
                        List<ReasonDescriptionDTO> resultList = reasonDescriptionService.list(params);
                        if (CollectionUtils.isEmpty(resultList)) {
                            log.info("查询取消原因失败");
                        }
                        log.info("成团失败，取消订单，groupRecordEy：{}", groupRecordEy);
                        // 取消当前拼团记录关联的订单
                        this.cancelOrder(groupRecordEy.getStoreId(), groupRecordEy.getId(), resultList.get(0).getId());

                    }
                }

            });
        }
    }
}
