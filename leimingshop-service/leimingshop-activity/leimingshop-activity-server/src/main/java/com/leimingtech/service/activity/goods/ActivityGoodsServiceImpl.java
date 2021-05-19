/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.activity.goods;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.EsConflictException;
import com.leimingtech.modules.constant.ActivityMongoConstants;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.activity.goods.ActivityGoodsDao;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.activity.goods.*;
import com.leimingtech.modules.dto.activity.goods.group.ActivityReminderDTO;
import com.leimingtech.modules.dto.coupons.CouponsGoodsDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityDTO;
import com.leimingtech.modules.dto.goods.ActivitityGoodsInfoDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsSpecDetailDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.group.GroupDTO;
import com.leimingtech.modules.dto.group.GroupGoodsDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.dto.seckill.SeckillActivityDTO;
import com.leimingtech.modules.dto.seckill.SeckillSessionDTO;
import com.leimingtech.modules.entity.activity.goods.ActivityGoodsEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.flashsale.FlashSaleActivityEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.flashsale.FlashSaleActivityService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.service.seckill.SeckillActivityService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动商品表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)

public class ActivityGoodsServiceImpl extends BaseServiceImpl<ActivityGoodsDao, ActivityGoodsEntity> implements ActivityGoodsService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private CouponsActivityService couponsActivityService;
    @Autowired
    private ReduceActivityService reduceActivityService;
    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private GroupService groupService;
    @Autowired
    private FlashSaleActivityService flashSaleActivityService;

    @Autowired
    private EsDataUtils esDataUtils;
    @Autowired
    private NosqlService nosqlService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MemberService memberService;
    @Autowired
    private SysMessageService sysMessageService;

    @Override

    public PageData<ActivityGoodsDTO> page(@RequestParam Map<String, Object> params) {
        IPage<ActivityGoodsEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ActivityGoodsDTO.class);
    }

    @Override

    public List<ActivityGoodsDTO> list(@RequestParam Map<String, Object> params) {
        List<ActivityGoodsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ActivityGoodsDTO.class);
    }

    private QueryWrapper<ActivityGoodsEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<ActivityGoodsEntity> wrapper = new QueryWrapper<>();

        if (params.get("id") != null) {
            wrapper.eq("id", params.get("id").toString());
        }
        if (params.get("activityId") != null) {
            wrapper.eq("activity_id", params.get("activityId").toString());
        }
        if (params.get("activityType") != null) {
            wrapper.eq("activity_type", params.get("activityType").toString());
        }
        if (params.get("specId") != null) {
            wrapper.eq("spec_id", params.get("specId").toString());
        }

        return wrapper;
    }

    @Override

    public ActivityGoodsDTO get(Long id) {
        ActivityGoodsEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ActivityGoodsDTO.class);
    }

    /**
     * 功能描述：
     * <根据活动id 活动类型查询活动商品数量>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @return
     * @date 2020/3/18 15:29
     * @author 刘远杰
     **/
    @Override

    public Integer countByActivityId(@RequestParam("activityId") Long activityId, @RequestParam("activityType") Integer activityType) {
        QueryWrapper<ActivityGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId).eq("activity_type", activityType).groupBy("goods_id");
        return baseDao.selectCount(wrapper);
    }

    /**
     * 功能描述：
     * <批量查询活动商品>
     *
     * @param activityIds  活动id集合
     * @param activityType 活动类型
     * @return
     * @date 2020/3/12 11:37
     * @author 刘远杰
     **/
    @Override

    public List<ActivityGoodsDTO> getByActivityIds(@RequestBody List<Long> activityIds, @RequestParam("activityType") Integer activityType) {
        QueryWrapper<ActivityGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_type", activityType).in("activity_id", activityIds);
        List<ActivityGoodsEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, ActivityGoodsDTO.class);
    }

    @Override

    public Boolean save(@RequestBody ActivityGoodsDTO dto) {
        ActivityGoodsEntity entity = ConvertUtils.sourceToTarget(dto, ActivityGoodsEntity.class);

        return insert(entity);
    }

    /**
     * 功能描述：
     * <活动商品批量保存>
     *
     * @param dtoList 活动商品集合
     * @return
     * @date 2020/3/10 18:00
     * @author 刘远杰
     **/
    @Override

    public Boolean saveBatch(@RequestBody List<ActivityGoodsDTO> dtoList) {
        List<ActivityGoodsEntity> entityList = ConvertUtils.sourceToTarget(dtoList, ActivityGoodsEntity.class);
        return super.insertBatch(entityList);
    }

    /**
     * 批量更新活动商品
     *
     * @param dtoList
     * @return
     * @date 2020-08-25 16:32
     * @author huangkeyuan@leimingtech.com
     */

    @Override
    public Boolean updateBatch(List<ActivityGoodsDTO> dtoList) {
        List<ActivityGoodsEntity> entityList = ConvertUtils.sourceToTarget(dtoList, ActivityGoodsEntity.class);
        return super.updateBatchById(entityList);
    }

    /**
     * 功能描述：
     * <活动商品批量保存>
     *
     * @param dtoList      活动商品集合
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsId      商品id
     * @return
     * @date 2020/3/10 18:00
     * @author 刘远杰
     **/
    @Override

    public Boolean saveBatch(@RequestBody List<ActivityGoodsDTO> dtoList,
                             @RequestParam("activityId") Long activityId,
                             @RequestParam("activityType") Integer activityType,
                             @RequestParam("goodsId") Long goodsId) {
        // 逻辑删除商品数据
        this.deleteByActivityAndGoodsId(activityId, activityType, Collections.singletonList(goodsId));

        List<ActivityGoodsEntity> entityList = ConvertUtils.sourceToTarget(dtoList, ActivityGoodsEntity.class);

        // 锁定商品活动库存
        this.reduceGoodsSpecStorage(entityList);

        // 保存活动商品
        boolean flag = super.insertBatch(entityList);

        if (flag) {
            // 更新es活动信息
            updateEsSpecActivity(activityId, activityType, dtoList);
            // 更新购物车es活动信息
            updateCartEsSpecActivity(activityId, activityType, dtoList);
        }
        return flag;
    }

    @Override

    public Boolean update(@RequestBody ActivityGoodsDTO dto) {
        ActivityGoodsEntity entity = ConvertUtils.sourceToTarget(dto, ActivityGoodsEntity.class);

        return updateById(entity);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, ActivityGoodsEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述：
     * <物理删除指定商品id活动数据>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsId      商品id
     * @return
     * @date 2020/3/10 18:29
     * @author 刘远杰
     **/
    @Override

    public void noLogicDeleteByActivityAndGoodsId(@RequestParam("activityId") Long activityId,
                                                  @RequestParam("activityType") Integer activityType,
                                                  @RequestParam("goodsId") Long goodsId) {
        // 更新商品es
        this.deleteEsSpecActivity(activityId, activityType, Collections.singletonList(goodsId));
        // 更新购物车es
        this.deleteCartEsSpecActivity(activityId, activityType, Collections.singletonList(goodsId));

        // 物理删除指定商品id活动数据
        baseDao.noLogicDeleteByActivityAndGoodsId(activityId, activityType, goodsId);
    }

    /**
     * 功能描述：
     * <删除指定商品id活动数据>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsIds     商品id集合
     * @return
     * @date 2020/3/11 15:27
     * @author 刘远杰
     **/
    @Override

    public void deleteByActivityAndGoodsId(@RequestParam("activityId") Long activityId,
                                           @RequestParam("activityType") Integer activityType,
                                           @RequestBody List<Long> goodsIds) {
        // 释放商品活动库存 - 修改商品es - 修改购物车es - 删除活动商品
        UpdateWrapper<ActivityGoodsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activity_id", activityId)
                .eq("activity_type", activityType)
                .in("goods_id", goodsIds);

        // 释放活动库存
        if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == activityType) {
            // 释放秒杀库存
            SeckillActivityDTO seckillActivityDTO = seckillActivityService.get(activityId);
            if (seckillActivityDTO != null) {
                // 秒杀未结束 - 释放活动库存
                if (SeckillActivityEnum.ACTIVITY_STATE_END.value() != seckillActivityDTO.getActivityState()) {
                    // 查询秒杀商品集合
                    List<ActivityGoodsEntity> entityList = baseDao.selectList(updateWrapper);
                    // 增加商品规格库存
                    this.addGoodsSpecStorage(entityList);
                }
            }
        } else if (ActivityTypeEnum.GROUP_ACTIVITY.value() == activityType) {
            // 释放拼团库存
            GroupDTO groupDTO = groupService.get(activityId);
            if (groupDTO != null) {
                // 拼团未结束 - 释放活动库存
                if (GroupsEnum.ACTIVITY_STATUS_END.value() != groupDTO.getActivityStatus()) {
                    // 查询拼团商品集合
                    List<ActivityGoodsEntity> entityList = baseDao.selectList(updateWrapper);
                    // 增加商品规格库存
                    this.addGoodsSpecStorage(entityList);
                }
            }
        } else if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == activityType) {
            // 释放限时购库存
            FlashSaleActivityDTO activityDTO = flashSaleActivityService.get(activityId);
            if (activityDTO != null) {
                // 拼团未结束 - 释放活动库存
                if (GroupsEnum.ACTIVITY_STATUS_END.value() != activityDTO.getActivityState()) {
                    // 查询拼团商品集合
                    List<ActivityGoodsEntity> entityList = baseDao.selectList(updateWrapper);
                    // 增加商品规格库存
                    this.addGoodsSpecStorage(entityList);
                }
            }
        }

        // 删除es索引活动
        deleteEsSpecActivity(activityId, activityType, goodsIds);
        // 更新购物车es
        this.deleteCartEsSpecActivity(activityId, activityType, goodsIds);

        // 删除活动商品
        baseDao.delete(updateWrapper);
    }

    /**
     * 功能描述：
     * <删除指定活动id数据>
     *
     * @param activityIds  活动id集合
     * @param activityType 活动类型
     * @return
     * @date 2020/3/11 15:27
     * @author 刘远杰
     **/
    @Override

    public void deleteByActivityIds(@RequestBody List<Long> activityIds,
                                    @RequestParam("activityType") Integer activityType) {
        UpdateWrapper<ActivityGoodsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("activity_id", activityIds)
                .eq("activity_type", activityType);
        baseDao.delete(updateWrapper);
    }

    /**
     * 功能描述：
     * <添加秒杀活动商品分页查询>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/10 11:03
     * @author 刘远杰
     **/
    @Override

    public PageData<AddSeckillGoodsPageDTO> canAddActiveGoods(@RequestParam Map<String, Object> params) {
        Long storeId = Long.parseLong(params.get("storeId").toString());
        // 1.查询分页商品数据
        PageData<ActivitityGoodsInfoDTO> goodsPage = goodsService.selectGoodsAndStoragePage(params);

        // 2.已参加活动信息查询
        List<AddSeckillGoodsPageDTO> goodsPageList = ConvertUtils.sourceToTarget(goodsPage.getList(), AddSeckillGoodsPageDTO.class);
        if (CollectionUtils.isNotEmpty(goodsPageList)) {
            // 封装商品优惠券数据
            fillGoodsCouponsActivity(storeId, goodsPageList);
            // 满减
            fillGoodsReduceActivity(storeId, goodsPageList);
            //todo xuzhch 添加活动可优化 2020年10月20日
            // 秒杀
            fillGoodsSeckillActivity(storeId, goodsPageList);
            // 拼团
            fillGoodsGroupActivity(storeId, goodsPageList);
            // 限时抢购
            fillGoodsFlashSaleActivity(storeId, goodsPageList);

            // TODO: 2020/3/10 其他活动处理
        }

        return new PageData<>(goodsPageList, goodsPage.getTotal());
    }

    /**
     * 功能描述：
     * <封装商品限时抢购活动数据>
     *
     * @param storeId       店铺id
     * @param goodsPageList 商品集合
     * @return
     * @date 2020/3/10 13:39
     * @author 刘远杰
     **/
    private void fillGoodsFlashSaleActivity(Long storeId, List<AddSeckillGoodsPageDTO> goodsPageList) {
        // 1.查询所有限时抢购商品
        List<ActivityGoodsDTO> activityGoodsDTOList = baseDao.selectFlashSaleAllGoods(storeId, null);
        // 判断商品是否存在限时抢购活动
        for (AddSeckillGoodsPageDTO goods : goodsPageList) {
            for (ActivityGoodsDTO activityGoodsDTO : activityGoodsDTOList) {
                if (activityGoodsDTO.getGoodsId().equals(goods.getId())) {
                    // 匹配活动商品id与商品id一致 封装活动类型 操作设置为已添加
                    goods.getActivityTypeList().add(ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                    goods.setOperationType(1);
                    goods.setActivityId(activityGoodsDTO.getActivityId());
                    break;
                }
            }
        }
    }

    /**
     * 添加拼团活动商品的分页查询
     *
     * @param params 查询条件
     * @return
     * @date 2020-04-08 14:58
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public PageData<AddSeckillGoodsPageDTO> getAddGroupGoodsList(@RequestParam Map<String, Object> params) {
        Long storeId = Long.parseLong(params.get("storeId").toString());
        // 1.查询分页商品数据
        PageData<ActivitityGoodsInfoDTO> goodsPage = goodsService.selectGoodsAndStoragePage(params);

        // 2.已参加活动信息查询
        List<AddSeckillGoodsPageDTO> goodsPageList = ConvertUtils.sourceToTarget(goodsPage.getList(), AddSeckillGoodsPageDTO.class);
        if (CollectionUtils.isNotEmpty(goodsPageList)) {
            // 封装商品优惠券数据
            fillGoodsCouponsActivity(storeId, goodsPageList);
            // 满减
            fillGoodsReduceActivity(storeId, goodsPageList);
            // 秒杀
            fillGoodsSeckillActivity(storeId, goodsPageList);
            // 拼团
            fillGoodsGroupActivity(storeId, goodsPageList);
            // TODO: 2020/3/10 其他活动处理
            // 限时抢购
            fillGoodsFlashSaleActivity(storeId, goodsPageList);
        }

        return new PageData<>(goodsPageList, goodsPage.getTotal());
    }

    /**
     * 功能描述：
     * <查询秒杀已选择商品列表>
     *
     * @param params 查询参数
     * @return
     * @date 2020/3/11 11:49
     * @author 刘远杰
     **/
    @Override

    public PageData<AdminSeckillGoodsDTO> adminSeckillGoodsPage(@RequestParam Map<String, Object> params) {
        // 查询秒杀商品 - 查询秒杀商品对应商品数据 - 匹配对应商品封装页面展示数据
        // 1.查询活动商品
        IPage page = getPage(params, null, false);
        List<AdminSeckillGoodsDTO> seckillGoodsList = baseDao.alreadyAddActivityGoodsPage(params, page);

        Long storeId = null;
        if (params.get("storeId") != null) {
            storeId = Long.parseLong(params.get("storeId").toString());
        }

        if (CollectionUtils.isNotEmpty(seckillGoodsList)) {
            // 查询商品数据
            List<Long> goodsIds = seckillGoodsList.stream().map(AdminSeckillGoodsDTO::getGoodsId).collect(Collectors.toList());
            List<GoodsDTO> goodsList = goodsService.getByGoodsIds(goodsIds, storeId);

            if (CollectionUtils.isNotEmpty(goodsList)) {
                // 匹配商品数据 封装商品名称销售价
                for (GoodsDTO goods : goodsList) {
                    for (AdminSeckillGoodsDTO seckillGoods : seckillGoodsList) {
                        if (goods.getId().equals(seckillGoods.getGoodsId())) {
                            seckillGoods.setGoodsName(goods.getGoodsName());
                            seckillGoods.setSpecSellPrice(goods.getSpecSellPrice());
                            seckillGoods.setGoodsMainPicture(goods.getGoodsMainPicture());
                            break;
                        }
                    }
                }
            }
        }
        return new PageData<AdminSeckillGoodsDTO>(seckillGoodsList, page.getTotal());
    }

    /**
     * 功能描述：
     * <查询所有秒杀商品>
     *
     * @param storeId 店铺id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 15:06
     * @author 刘远杰
     **/
    @Override

    public List<ActivityGoodsDTO> getAllSeckillGoods(@RequestParam("storeId") Long storeId,
                                                     @RequestParam(value = "goodsId", required = false) Long goodsId) {
        return baseDao.selectAllSeckillGoods(storeId, goodsId);
    }

    /**
     * 功能描述：
     * <查询所有拼团商品>
     *
     * @param storeId 店铺id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 15:06
     * @author 刘远杰
     **/
    @Override

    public List<ActivityGoodsDTO> getAllGroupGoods(@RequestParam("storeId") Long storeId,
                                                   @RequestParam(value = "goodsId", required = false) Long goodsId) {
        return baseDao.selectAllGroupGoods(storeId, goodsId);
    }

    /**
     * 功能描述：
     * <保存或者修改秒杀商品sku列表数据查询>
     *
     * @param activityId 活动id
     * @param goodsId    商品spu id
     * @param storeId    店铺id
     * @return
     * @date 2020/3/10 15:53
     * @author 刘远杰
     **/
    @Override

    public List<AddSeckillSkuListDTO> getAddSeckillSkuList(@RequestParam("activityId") Long activityId,
                                                           @RequestParam("goodsId") Long goodsId,
                                                           @RequestParam("storeId") Long storeId) {
        List<AddSeckillSkuListDTO> list = new ArrayList<>();

        // 1.查询该商品活动设置
        LambdaQueryWrapper<ActivityGoodsEntity> warpper = Wrappers.<ActivityGoodsEntity>lambdaQuery()
                .eq(ActivityGoodsEntity::getActivityId, activityId)
                .eq(ActivityGoodsEntity::getGoodsId, goodsId);
        List<ActivityGoodsEntity> entityList = baseDao.selectList(warpper);

        // 2.查询该商品规格列表
        List<GoodsSpecDetailDTO> goodsSpecDetailList = goodsSpecService.selectByGoodsId(goodsId);

        // 3.封装列表回显数据
        if (CollectionUtils.isNotEmpty(goodsSpecDetailList)) {
            goodsSpecDetailList.forEach(goodsSpecDetail -> {
                AddSeckillSkuListDTO addSeckillSku = new AddSeckillSkuListDTO();
                BeanCopier.create(GoodsSpecDetailDTO.class, AddSeckillSkuListDTO.class, false)
                        .copy(goodsSpecDetail, addSeckillSku, null);
                addSeckillSku.setSpecId(goodsSpecDetail.getId());

                // 匹配活动商品，封装活活动金额以及活动库存 - 增加商品sku库存
                if (CollectionUtils.isNotEmpty(entityList)) {
                    for (ActivityGoodsEntity entity : entityList) {
                        if (entity.getSpecId().equals(addSeckillSku.getSpecId())) {
                            addSeckillSku.setActivityStorage(entity.getActivityStorage());
                            addSeckillSku.setActivityPrice(entity.getActivityPrice());
                            // 商品实际库存 = 普通商品库存 + 活动剩余库存
                            addSeckillSku.setSpecStorage(addSeckillSku.getSpecStorage() + entity.getActivitySurplusStorage());
                            break;
                        }
                    }
                }
                list.add(addSeckillSku);
            });
        }

        return list;
    }

    /**
     * 拼团活动添加或管理商品获取商品列表
     *
     * @param activityId 拼团活动id
     * @param goodsId    商品spu id
     * @param storeId    店铺id
     * @return
     * @date 2020-03-10 17:53
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public List<AddSeckillSkuListDTO> getAddGropGoodsList(Long activityId, Long goodsId, Long storeId) {
        List<AddSeckillSkuListDTO> list = new ArrayList<>();

        // 1.查询该商品拼团活动列表
        QueryWrapper<ActivityGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId).eq("goods_id", goodsId)
                .eq("activity_type", ActivityTypeEnum.GROUP_ACTIVITY.value());
        List<ActivityGoodsEntity> entityList = baseDao.selectList(queryWrapper);

        // 2.查询该商品规格列表
        List<GoodsSpecDetailDTO> goodsSpecDetailList = goodsSpecService.selectByGoodsId(goodsId);

        // 3.封装列表回显数据
        goodsSpecDetailList.forEach(goodsSpecDetail -> {
            AddSeckillSkuListDTO addSeckillSku = new AddSeckillSkuListDTO();
            BeanCopier.create(GoodsSpecDetailDTO.class, AddSeckillSkuListDTO.class, false)
                    .copy(goodsSpecDetail, addSeckillSku, null);
            addSeckillSku.setSpecId(goodsSpecDetail.getId());

            // 匹配活动商品，封装活活动金额以及活动库存
            if (CollectionUtils.isNotEmpty(entityList)) {
                for (ActivityGoodsEntity entity : entityList) {
                    if (entity.getSpecId().equals(addSeckillSku.getSpecId())) {
                        addSeckillSku.setActivityStorage(entity.getActivityStorage());
                        addSeckillSku.setActivityPrice(entity.getActivityPrice());
                        // 商品实际库存 = 普通商品库存 + 活动剩余库存
                        addSeckillSku.setSpecStorage(addSeckillSku.getSpecStorage() + entity.getActivitySurplusStorage());
                        break;
                    }
                }
            }
            list.add(addSeckillSku);
        });

        return list;
    }

    /**
     * 功能描述：
     * <封装商品优惠券数据>
     *
     * @param storeId       店铺id
     * @param goodsPageList 商品集合
     * @return
     * @date 2020/3/10 13:39
     * @author 刘远杰
     **/
    private void fillGoodsCouponsActivity(Long storeId, List<AddSeckillGoodsPageDTO> goodsPageList) {
        // 查询所有优惠券关联商品 - 按优惠券商品类型分组 - 匹配判断是否为优惠券活动商品
        // 1.查询所有优惠券商品
        List<CouponsGoodsDTO> allCouponsGoods = couponsActivityService.selectAllCouponsGoodsByStoreId(storeId);
        // 2.商品关联类型分组 关联类型->优惠券商品集合
        Map<Integer, List<CouponsGoodsDTO>> map = allCouponsGoods.stream().collect(Collectors.groupingBy(CouponsGoodsDTO::getActivityGoodsScope));

        if (map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value()) != null) {
            // 存在店铺优惠券 - 全部商品为优惠券商品 - 设置为优惠券商品
            goodsPageList.forEach(goods -> {
                // 封装活动类型 操作设置为添加
                goods.getActivityTypeList().add(ActivityTypeEnum.COUPONS_ACTIVITY.value());
                goods.setOperationType(0);
            });
        } else {
            // 不存在店铺优惠券 - 获取品牌、商品优惠券关联 - 匹配判断是否为优惠券活动商品
            if (map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value()) != null || map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value()) != null) {
                // 获取存在活动的品牌和商品
                List<CouponsGoodsDTO> couponsGoodsBrandList = Optional.ofNullable(map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value())).orElse(new ArrayList<>());
                List<CouponsGoodsDTO> couponsGoodsList = Optional.ofNullable(map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value())).orElse(new ArrayList<>());
                for (AddSeckillGoodsPageDTO goods : goodsPageList) {
                    for (CouponsGoodsDTO couponsGoods : couponsGoodsBrandList) {
                        if (couponsGoods.getRelationId().equals(goods.getBrandId())) {
                            // 匹配活动商品品牌与商品品牌一致 - 封装活动类型 操作设置为添加
                            goods.getActivityTypeList().add(ActivityTypeEnum.COUPONS_ACTIVITY.value());
                            goods.setOperationType(0);
                            break;
                        }
                    }
                    if (!goods.getActivityTypeList().contains(ActivityTypeEnum.COUPONS_ACTIVITY.value())) {
                        // 品牌未匹配执行商品id匹配操作
                        for (CouponsGoodsDTO couponsGoods : couponsGoodsList) {
                            // 商品存在优惠券活动
                            if (couponsGoods.getRelationId().equals(goods.getId())) {
                                // 匹配活动商品id与商品id一致 - 封装活动类型 操作设置为添加
                                goods.getActivityTypeList().add(ActivityTypeEnum.COUPONS_ACTIVITY.value());
                                goods.setOperationType(0);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装商品优惠券数据(与秒杀不同，拼团和优惠券活动互斥，参加了优惠券活动则不能参加拼团；operationType为1)
     *
     * @param storeId       店铺id
     * @param goodsPageList 商品集合
     * @return
     * @date 2020-04-08 15:03
     * @author huangkeyuan@leimingtech.com
     **/
    private void fillGroupGoodsCouponsActivity(Long storeId, List<AddSeckillGoodsPageDTO> goodsPageList) {
        // 查询所有优惠券关联商品 - 按优惠券商品类型分组 - 匹配判断是否为优惠券活动商品
        // 1.查询所有优惠券商品
        List<CouponsGoodsDTO> allCouponsGoods = couponsActivityService.selectAllCouponsGoodsByStoreId(storeId);
        // 2.商品关联类型分组 关联类型->优惠券商品集合
        Map<Integer, List<CouponsGoodsDTO>> map = allCouponsGoods.stream().collect(Collectors.groupingBy(CouponsGoodsDTO::getActivityGoodsScope));

        if (map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value()) != null) {
            // 存在店铺优惠券 - 全部商品为优惠券商品 - 设置为优惠券商品
            goodsPageList.forEach(goods -> {
                // 封装活动类型 操作设置为已添加
                goods.getActivityTypeList().add(ActivityTypeEnum.COUPONS_ACTIVITY.value());
                goods.setOperationType(1);
            });
        } else {
            // 不存在店铺优惠券 - 获取品牌、商品优惠券关联 - 匹配判断是否为优惠券活动商品
            if (map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value()) != null || map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value()) != null) {
                // 获取存在活动的品牌和商品
                List<CouponsGoodsDTO> couponsGoodsBrandList = Optional.ofNullable(map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value())).orElse(new ArrayList<>());
                List<CouponsGoodsDTO> couponsGoodsList = Optional.ofNullable(map.get(CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value())).orElse(new ArrayList<>());
                for (AddSeckillGoodsPageDTO goods : goodsPageList) {
                    for (CouponsGoodsDTO couponsGoods : couponsGoodsBrandList) {
                        if (couponsGoods.getRelationId().equals(goods.getBrandId())) {
                            // 匹配活动商品品牌与商品品牌一致 - 封装活动类型 操作设置为已添加
                            goods.getActivityTypeList().add(ActivityTypeEnum.COUPONS_ACTIVITY.value());
                            goods.setOperationType(1);
                            break;
                        }
                    }
                    if (!goods.getActivityTypeList().contains(ActivityTypeEnum.COUPONS_ACTIVITY.value())) {
                        // 品牌未匹配执行商品id匹配操作
                        for (CouponsGoodsDTO couponsGoods : couponsGoodsList) {
                            // 商品存在优惠券活动
                            if (couponsGoods.getRelationId().equals(goods.getId())) {
                                // 匹配活动商品id与商品id一致 - 封装活动类型 操作设置为已添加
                                goods.getActivityTypeList().add(ActivityTypeEnum.COUPONS_ACTIVITY.value());
                                goods.setOperationType(1);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 功能描述：
     * <封装商品满减活动数据>
     *
     * @param storeId       店铺id
     * @param goodsPageList 商品集合
     * @return
     * @date 2020/3/10 13:39
     * @author 刘远杰
     **/
    private void fillGoodsReduceActivity(Long storeId, List<AddSeckillGoodsPageDTO> goodsPageList) {
        // 查询所有满减关联商品 - 按满减商品类型分组 - 匹配判断是否为满减活动商品
        // 1.查询所有满减商品
        List<ReduceGoodsDTO> allReduceGoods = reduceActivityService.selectAllReduceGoodsByStoreId(storeId);
        // 2.商品关联类型分组 关联类型->满减商品集合
        Map<Integer, List<ReduceGoodsDTO>> map = allReduceGoods.stream().collect(Collectors.groupingBy(ReduceGoodsDTO::getActivityGoodsScope));

        if (map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_ALL.value()) != null) {
            // 存在店铺满减 - 全部商品为满减商品 - 设置为满减商品
            goodsPageList.forEach(goods -> {
                // 封装活动类型 操作设置为添加
                goods.getActivityTypeList().add(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                goods.setOperationType(0);
            });
        } else {
            // 不存在店铺满减 - 获取品牌、商品满减关联 - 匹配判断是否为满减活动商品
            if (map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value()) != null || map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value()) != null) {
                // 获取存在活动的品牌和商品
                List<ReduceGoodsDTO> reduceGoodsBrandList = Optional.ofNullable(map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value())).orElse(new ArrayList<ReduceGoodsDTO>());
                List<ReduceGoodsDTO> reduceGoodsList = map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value());
                for (AddSeckillGoodsPageDTO goods : goodsPageList) {
                    for (ReduceGoodsDTO reduceGoods : reduceGoodsBrandList) {
                        // 匹配活动商品品牌与商品品牌一致 - 封装活动类型 操作设置为添加
                        if (reduceGoods.getRelationId().equals(goods.getBrandId())) {
                            // 封装活动类型 操作设置为已添加
                            goods.getActivityTypeList().add(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                            goods.setOperationType(0);
                            break;
                        }
                    }
                    if (!goods.getActivityTypeList().contains(ActivityTypeEnum.REDUCE_ACTIVITY.value())) {
                        if (CollectionUtils.isNotEmpty(reduceGoodsList)) {
                            // 品牌未匹配执行商品id匹配操作
                            for (ReduceGoodsDTO reduceGoods : reduceGoodsList) {
                                if (reduceGoods.getRelationId().equals(goods.getId())) {
                                    // 匹配活动商品id与商品id一致 - 封装活动类型 操作设置为添加
                                    goods.getActivityTypeList().add(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                                    goods.setOperationType(0);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装商品满减数据(与秒杀不同，拼团和满减活动互斥，参加了满减活动则不能参加拼团；operationType为1)
     *
     * @param storeId
     * @param goodsPageList
     * @return
     * @date 2020-04-08 15:07
     * @author huangkeyuan@leimingtech.com
     **/
    private void fillGroupGoodsReduceActivity(Long storeId, List<AddSeckillGoodsPageDTO> goodsPageList) {
        // 查询所有满减关联商品 - 按满减商品类型分组 - 匹配判断是否为满减活动商品
        // 1.查询所有满减商品
        List<ReduceGoodsDTO> allReduceGoods = reduceActivityService.selectAllReduceGoodsByStoreId(storeId);
        // 2.商品关联类型分组 关联类型->满减商品集合
        Map<Integer, List<ReduceGoodsDTO>> map = allReduceGoods.stream().collect(Collectors.groupingBy(ReduceGoodsDTO::getActivityGoodsScope));

        if (map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_ALL.value()) != null) {
            // 存在店铺满减 - 全部商品为满减商品 - 设置为满减商品
            goodsPageList.forEach(goods -> {
                // 封装活动类型 操作设置为不可添加
                goods.getActivityTypeList().add(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                goods.setOperationType(1);
            });
        } else {
            // 不存在店铺满减 - 获取品牌、商品满减关联 - 匹配判断是否为满减活动商品
            if (map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value()) != null || map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value()) != null) {
                // 获取存在活动的品牌和商品
                List<ReduceGoodsDTO> reduceGoodsBrandList = Optional.ofNullable(map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value())).orElse(new ArrayList<ReduceGoodsDTO>());
                List<ReduceGoodsDTO> reduceGoodsList = map.get(ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value());
                for (AddSeckillGoodsPageDTO goods : goodsPageList) {
                    for (ReduceGoodsDTO reduceGoods : reduceGoodsBrandList) {
                        // 匹配活动商品品牌与商品品牌一致 - 封装活动类型 操作设置为添加
                        if (reduceGoods.getRelationId().equals(goods.getBrandId())) {
                            // 封装活动类型 操作设置为不可添加
                            goods.getActivityTypeList().add(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                            goods.setOperationType(1);
                            break;
                        }
                    }
                    if (!goods.getActivityTypeList().contains(ActivityTypeEnum.REDUCE_ACTIVITY.value())) {
                        // 品牌未匹配执行商品id匹配操作
                        for (ReduceGoodsDTO reduceGoods : reduceGoodsList) {
                            if (reduceGoods.getRelationId().equals(goods.getId())) {
                                // 匹配活动商品id与商品id一致 - 封装活动类型 操作设置为不可添加
                                goods.getActivityTypeList().add(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                                goods.setOperationType(1);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 功能描述：
     * <封装商品秒杀活动数据>
     *
     * @param storeId       店铺id
     * @param goodsPageList 商品集合
     * @return
     * @date 2020/3/10 13:39
     * @author 刘远杰
     **/
    private void fillGoodsSeckillActivity(Long storeId, List<AddSeckillGoodsPageDTO> goodsPageList) {
        // 1.查询所有秒杀商品
        List<ActivityGoodsDTO> allSeckillGoods = baseDao.selectAllSeckillGoods(storeId, null);

        // 判断商品是否存在秒杀活动
        for (AddSeckillGoodsPageDTO goods : goodsPageList) {
            for (ActivityGoodsDTO seckillGoods : allSeckillGoods) {
                if (seckillGoods.getGoodsId().equals(goods.getId())) {
                    // 匹配活动商品id与商品id一致 封装活动类型 操作设置为已添加
                    goods.getActivityTypeList().add(ActivityTypeEnum.SECKILL_ACTIVITY.value());
                    goods.setOperationType(1);
                    goods.setActivityId(seckillGoods.getActivityId());
                    break;
                }
            }
        }
    }

    /**
     * 功能描述：
     * <封装商品拼团活动数据>
     *
     * @param storeId       店铺id
     * @param goodsPageList 商品集合
     * @return
     * @date 2020/3/10 13:39
     * @author 刘远杰
     **/
    private void fillGoodsGroupActivity(Long storeId, List<AddSeckillGoodsPageDTO> goodsPageList) {
        // 1.查询所有拼团商品
        List<ActivityGoodsDTO> allGroupGoods = baseDao.selectAllGroupGoods(storeId, null);

        // 判断商品是否存在拼团活动
        for (AddSeckillGoodsPageDTO goods : goodsPageList) {
            for (ActivityGoodsDTO groupGoods : allGroupGoods) {
                if (groupGoods.getGoodsId().equals(goods.getId())) {
                    // 匹配活动商品id与商品id一致 封装活动类型 操作设置为已添加
                    goods.getActivityTypeList().add(ActivityTypeEnum.GROUP_ACTIVITY.value());
                    goods.setOperationType(1);
                    goods.setActivityId(groupGoods.getActivityId());
                    break;
                }
            }
        }
    }

    /**
     * 功能描述：
     * <删除商品es活动信息>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsIds     商品id集合
     * @return
     * @date 2020/3/12 11:12
     * @author 刘远杰
     **/
    @Retryable(value = EsConflictException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void deleteEsSpecActivity(Long activityId, Integer activityType, List<Long> goodsIds) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();

        // 指定goodsId 活动id
        inSearchCondition.put("goodsId", goodsIds);
        Map<String, Object> subEqualsParams = new HashMap<>();
        subEqualsParams.put("activityType", activityType);
        subEqualsParams.put("activityId", activityId);
        subEqualsSearchCondition.put("specActivityList", subEqualsParams);

        // 查询活动商品es
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
        List<String> jsonRsList = pageModelDTO.getJsonRsList();

        List<Map<String, Object>> updateDataList = new ArrayList<>();
        // 遍历活动商品：获取活动集合 - 删除该活动 - 更新商品es
        jsonRsList.forEach(jsonRs -> {
            JSONObject specJson = JSONObject.parseObject(jsonRs);
            SpecActivityVO specActivity = new SpecActivityVO();
            // 封装活动id
            specActivity.setActivityId(activityId);
            // es更新对象封装
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("id", specJson.get("id").toString());
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
            log.info("删除商品es活动数据成功，activityId:{}，activityType:{}，goodsIds:{}", activityId, activityType, goodsIds);
        }
    }

    /**
     * 功能描述：
     * <更新es秒杀活动信息>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param dtoList      活动商品集合
     * @return
     * @date 2020/3/12 11:16
     * @author 刘远杰
     **/
    @Retryable(value = EsConflictException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void updateEsSpecActivity(Long activityId, Integer activityType, List<ActivityGoodsDTO> dtoList) {
        List<Map<String, Object>> updateDataList = new ArrayList<>();

        // 查询活动 - 遍历活动商品 - 封装更新后es活动数据 - 移除原活动 - 查询添加 - 更新es
        if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == activityType) {
            // 查询秒杀活动
            SeckillActivityDTO seckillActivity = seckillActivityService.get(activityId);
            dtoList.forEach(dto -> {
                // 封装spec索引活动
                SpecActivityVO specActivity = new SpecActivityVO();
                // 活动数据封装
                BeanCopier.create(SeckillActivityDTO.class, SpecActivityVO.class, false)
                        .copy(seckillActivity, specActivity, null);
                // 活动商品数据封装
                BeanCopier.create(ActivityGoodsDTO.class, SpecActivityVO.class, false)
                        .copy(dto, specActivity, null);
                specActivity.setId(dto.getId());

                // es更新对象封装
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("id", dto.getSpecId().toString());
                updateData.put("specActivityList", specActivity);
                updateDataList.add(updateData);
            });
        } else if (ActivityTypeEnum.GROUP_ACTIVITY.value() == activityType) {
            // 查询拼团活动
            GroupDTO groupDTO = groupService.get(activityId);
            dtoList.forEach(dto -> {
                // 封装spec索引活动
                SpecActivityVO specActivity = new SpecActivityVO();
                // 活动数据封装
                BeanCopier.create(GroupDTO.class, SpecActivityVO.class, false)
                        .copy(groupDTO, specActivity, null);
                // 活动商品数据封装
                BeanCopier.create(ActivityGoodsDTO.class, SpecActivityVO.class, false)
                        .copy(dto, specActivity, null);
                specActivity.setId(dto.getId());
                specActivity.setRestrictionQuantity(dto.getOnceBuyLimit());
                specActivity.setActivityStartDate(groupDTO.getStartTime());
                specActivity.setActivityEndDate(groupDTO.getEndTime());
                specActivity.setActivityState(groupDTO.getActivityStatus());

                // es更新对象封装
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("id", dto.getSpecId().toString());
                updateData.put("specActivityList", specActivity);
                updateDataList.add(updateData);
            });
        } else if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == activityType) {
            // 查询秒杀活动
            FlashSaleActivityDTO flashSaleActivityDTO = flashSaleActivityService.get(activityId);
            dtoList.forEach(dto -> {
                // 封装spec索引活动
                SpecActivityVO specActivity = new SpecActivityVO();
                // 活动数据封装
                BeanCopier.create(FlashSaleActivityDTO.class, SpecActivityVO.class, false)
                        .copy(flashSaleActivityDTO, specActivity, null);
                // 活动商品数据封装
                BeanCopier.create(ActivityGoodsDTO.class, SpecActivityVO.class, false)
                        .copy(dto, specActivity, null);
                specActivity.setId(dto.getId());

                // es更新对象封装
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("id", dto.getSpecId().toString());
                updateData.put("specActivityList", specActivity);
                updateDataList.add(updateData);
            });
        }

        if (CollectionUtils.isNotEmpty(updateDataList)) {
            // 更新商品es
            boolean flag = esDataUtils.saveSubListBatchById(ElasticSearchConstant.GOODS_SPEC_INDEX, JSONArray.toJSONString(updateDataList),
                    "activityId", "specActivityList");
            if (!flag) {
                // 更新es异常
                throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
            }
            log.info("更新商品es活动数据成功，activityId:{}，activityType:{}", activityId, activityType);
        }
    }

    @Recover
    public void recover(EsConflictException e) {
        log.error("编辑活动商品异常：" + e.getMessage());
        throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
    }

    /**
     * 功能描述：
     * <删除购物车es活动信息>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param goodsIds     商品id集合
     * @return
     * @date 2020/3/12 11:12
     * @author 刘远杰
     **/
    private void deleteCartEsSpecActivity(Long activityId, Integer activityType, List<Long> goodsIds) {
        // 查询活动商品 - 遍历活动商品 - 查询商品原价 - 查询该商品购物车 - 移除活动、覆盖价格 - 更新购物车es - 删除redis活动价格库存
        goodsIds.forEach(goodsId -> {
            // 查询活动商品
            QueryWrapper<ActivityGoodsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("goods_id", goodsId)
                    .eq("activity_id", activityId)
                    .eq("activity_type", activityType);
            List<ActivityGoodsEntity> activityGoodsList = baseDao.selectList(queryWrapper);

            activityGoodsList.forEach(activityGoods -> {
                // 查询商品原价
                GoodsSpecDTO goodsSpec = goodsSpecService.get(activityGoods.getSpecId());
                if (goodsSpec == null) {
                    throw new ServiceException(ActivityStatusCode.NOTFUND_GOODS_SKU_EXCEPTION);
                }

                // 更新条件 指定规格购物车
                PageModelDTO pageModelDTO = new PageModelDTO();
                Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                equalsSearchCondition.put("specId", activityGoods.getSpecId());

                // 更新活动类型和价格
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("activityType", ActivityTypeEnum.NO_ACTIVITY.value());
                updateData.put("specSellPrice", goodsSpec.getSpecSellPrice());

                // 更新购物车es
                esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                log.info("秒杀活动结束删除购物车活动成功，activityId：{}，specId：{}", activityId, activityGoods.getSpecId());

                // 删除redis活动库存
                String storageKey = "";
                if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == activityType) {
                    storageKey = ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                } else if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == activityType) {
                    storageKey = ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                }
                redisUtils.delete(storageKey);
                log.info("活动结束删除redis活动库存成功，storageKey：{}", storageKey);

                // 删除redis活动库存
                String priceKey = "";
                if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == activityType) {
                    priceKey = ActivityRedisConstants.SECKILL_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                } else if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == activityType) {
                    priceKey = ActivityRedisConstants.FLASH_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                }
                redisUtils.delete(priceKey);
                log.info("活动结束删除redis活动价格成功，priceKey：{}", priceKey);
            });
        });
    }

    /**
     * 功能描述：
     * <更新购物车es秒杀活动信息>
     *
     * @param activityId   活动id
     * @param activityType 活动类型
     * @param dtoList      活动商品集合
     * @return
     * @date 2020/3/12 11:16
     * @author 刘远杰
     **/
    private void updateCartEsSpecActivity(Long activityId, Integer activityType, List<ActivityGoodsDTO> dtoList) {
        // 查询活动 - 遍历活动商品 - 设置购物车更新条件（指定商品specId） - 设置更新购物车结果 - 更新购物车 - 维护redis库存价格
        if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == activityType) {
            // 查询秒杀活动
            SeckillActivityDTO seckillActivity = seckillActivityService.get(activityId);
            if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == seckillActivity.getActivityState()) {
                // 进行中更新购物车、redis
                dtoList.forEach(activityGoods -> {
                    GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(activityGoods.getSpecId());
                    // 更新条件 指定规格购物车
                    PageModelDTO pageModelDTO = new PageModelDTO();
                    Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                    equalsSearchCondition.put("specId", activityGoods.getSpecId());

                    // 更新活动类型和价格
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("activityId", activityId);
                    updateData.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                    updateData.put("activitySurplusStorage", activityGoods.getActivitySurplusStorage());
                    updateData.put("specStorage", goodsSpecDTO == null ? 0 : goodsSpecDTO.getSpecStorage());
                    updateData.put("activityEndDate", seckillActivity.getActivityEndDate());
                    updateData.put("restrictionQuantity", seckillActivity.getRestrictionQuantity());
                    updateData.put("specSellPrice", activityGoods.getActivityPrice());

                    // 更新购物车es
                    esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                    log.info("更新购物车商品秒杀活动成功，activityId：{}，specId：{}", activityId, activityGoods.getSpecId());

                    // 保存redis活动库存
                    String storageKey = ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(storageKey, activityGoods.getActivitySurplusStorage(), -1L);
                    log.info("保存redis秒杀活动库存成功，storageKey：{}", storageKey);

                    // 保存redis活动价格
                    String priceKey = ActivityRedisConstants.SECKILL_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(priceKey, activityGoods.getActivityPrice().toString(), -1L);
                    log.info("保存redis秒杀活动价格成功，storageKey：{}", priceKey);
                });
            }
        } else if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == activityType) {
            // 查询秒杀活动
            FlashSaleActivityDTO activityDTO = flashSaleActivityService.get(activityId);
            if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == activityDTO.getActivityState()) {
                // 进行中更新购物车、redis
                dtoList.forEach(activityGoods -> {
                    GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(activityGoods.getSpecId());
                    // 更新条件 指定规格购物车
                    PageModelDTO pageModelDTO = new PageModelDTO();
                    Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
                    equalsSearchCondition.put("specId", activityGoods.getSpecId());

                    // 更新活动类型和价格
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("activityId", activityId);
                    updateData.put("activityType", ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                    updateData.put("activitySurplusStorage", activityGoods.getActivitySurplusStorage());
                    updateData.put("specStorage", goodsSpecDTO == null ? 0 : goodsSpecDTO.getSpecStorage());
                    updateData.put("activityEndDate", activityDTO.getActivityEndDate());
                    updateData.put("restrictionQuantity", activityDTO.getRestrictionQuantity());
                    updateData.put("specSellPrice", activityGoods.getActivityPrice());

                    // 更新购物车es
                    esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                    log.info("更新购物车商品秒杀活动成功，activityId：{}，specId：{}", activityId, activityGoods.getSpecId());

                    // 保存redis活动库存
                    String storageKey = ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(storageKey, activityGoods.getActivitySurplusStorage(), -1L);
                    log.info("保存redis秒杀活动库存成功，storageKey：{}", storageKey);

                    // 保存redis活动价格
                    String priceKey = ActivityRedisConstants.SECKILL_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
                    redisUtils.set(priceKey, activityGoods.getActivityPrice().toString(), -1L);
                    log.info("保存redis秒杀活动价格成功，storageKey：{}", priceKey);
                });
            }
        }
    }

    /**
     * 拼团商品列表
     *
     * @return
     * @date 2020-03-11 16:35
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public PageData<GroupGoodsDTO> groupGoodsList(@RequestParam Map<String, Object> params) {

        IPage page = getPage(params, null, false);

        // 1.查询活动商品
        List<GroupGoodsDTO> groupGoodsList = baseDao.groupGoodsPage(params, page);

        Long storeId = null;
        if (params.get("storeId") != null) {
            storeId = Long.parseLong(params.get("storeId").toString());
        }

        if (CollectionUtils.isNotEmpty(groupGoodsList)) {
            // 查询商品数据
            List<Long> goodsIds = groupGoodsList.stream().map(GroupGoodsDTO::getGoodsId).collect(Collectors.toList());
            List<GoodsDTO> goodsList = goodsService.getByGoodsIds(goodsIds, storeId);

            // 匹配商品数据 封装商品名称销售价
            for (GoodsDTO goods : goodsList) {
                for (GroupGoodsDTO groupGoodsDTO : groupGoodsList) {
                    if (goods.getId().equals(groupGoodsDTO.getGoodsId())) {
                        groupGoodsDTO.setGoodsName(goods.getGoodsName());
                        groupGoodsDTO.setSpecSellPrice(goods.getSpecSellPrice());
                        groupGoodsDTO.setGoodsMainPicture(goods.getGoodsMainPicture());
                        break;
                    }
                }
            }
        }
        return new PageData<GroupGoodsDTO>(groupGoodsList, page.getTotal());

    }

    /**
     * 功能描述：
     * <保存活动商品浏览记录>
     *
     * @param goodsId 商品id
     * @param ip      ip
     * @return
     * @date 2020/3/16 14:25
     * @author 刘远杰
     **/
    @Override

    public void saveActivityGoodsBrowser(@RequestParam("goodsId") Long goodsId,
                                         @RequestParam("ip") String ip) {
        // 1.获取商品参与的秒杀活动集合
        List<ActivityGoodsDTO> activityGoodsDTOList = baseDao.selectAllSeckillGoods(null, goodsId);
        List<ActivityGoodsDTO> flashSaleAllGoods = baseDao.selectFlashSaleAllGoods(null, goodsId);
        activityGoodsDTOList.addAll(flashSaleAllGoods);

        if (CollectionUtils.isNotEmpty(activityGoodsDTOList)) {
            List<Long> seckillIds = new ArrayList<>();
            List<Long> flashSaleIds = new ArrayList<>();
            // 遍历活动商品
            activityGoodsDTOList.forEach(activityGoods -> {
                // 2.查询mongo 活动商品浏览记录
                Map<String, Object> queryParams = new HashMap<>();
                queryParams.put("activityId", activityGoods.getActivityId());
                queryParams.put("goodsId", goodsId);
                if (activityGoods.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()) {
                    queryParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());

                } else if (activityGoods.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()) {
                    queryParams.put("activityType", ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                }
                List<String> browserJsonList = nosqlService.queryData("activity_goods_browser", queryParams);

                // 3.判断该ip是否存在，存在不记录，不存在+1
                if (CollectionUtils.isNotEmpty(browserJsonList)) {
                    JSONObject browser = JSONObject.parseObject(browserJsonList.get(0));
                    List<String> ipList = JSONArray.parseArray(browser.get("ip").toString(), String.class);
                    if (!ipList.contains(ip)) {
                        Map<String, Object> data = new HashMap<>();
                        if (activityGoods.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()) {
                            data.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                            seckillIds.add(activityGoods.getActivityId());
                        } else if (activityGoods.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()) {
                            data.put("activityType", ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                            flashSaleIds.add(activityGoods.getActivityId());
                        }
                        data.put("id", browser.get("id"));
                        data.put("activityId", activityGoods.getActivityId());
                        data.put("goodsId", goodsId);
                        int count = Integer.parseInt(browser.get("count").toString());
                        data.put("count", count + 1);
                        ipList.add(ip);
                        data.put("ip", ipList);
                        // 更新mongo活动商品浏览记录
                        nosqlService.updateData("activity_goods_browser", "id", data);
                    }
                } else {
                    Map<String, Object> data = new HashMap<>();
                    if (activityGoods.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()) {
                        data.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                        // 更新秒杀活动总浏览记录
                        seckillIds.add(activityGoods.getActivityId());
                    } else if (activityGoods.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()) {
                        FlashSaleActivityDTO activityDTO = Optional.of(flashSaleActivityService.get(activityGoods.getId()))
                                .orElse(new FlashSaleActivityDTO());
                        if (activityDTO.getActivityState() == FlashSaleActivityEnum.ACTIVITY_STATE_START.value()) {
                            data.put("activityType", ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                            flashSaleIds.add(activityGoods.getActivityId());
                        }
                    }
                    data.put("id", IdGenerator.defaultSnowflakeId());
                    data.put("activityId", activityGoods.getActivityId());
                    data.put("goodsId", goodsId);
                    data.put("count", 1);
                    data.put("ip", Collections.singleton(ip));
                    // 更新mongo活动商品浏览记录
                    nosqlService.saveData(ActivityMongoConstants.ACTIVITY_GOODS_BROWSER, data);
                }
            });
            if (CollectionUtils.isNotEmpty(seckillIds)) {
                // 更新秒杀活动总浏览记录
                seckillActivityService.increaseBrowserNum(seckillIds);
            }
            if (CollectionUtils.isNotEmpty(flashSaleIds)) {
                // 更新秒杀活动总浏览记录
                flashSaleActivityService.increaseBrowserNum(flashSaleIds);
            }
        }
    }

    /**
     * 功能描述：
     * <修改秒杀商品库存、更新下单数量>
     *
     * @param updateActivitySurplusStorageList 秒杀库存更新实体集合
     * @param type                             0扣减库存操作 1增加库存操作（规格删除不操做）
     * @return
     * @date 2020/3/30 11:14
     * @author 刘远杰
     **/
    @Override

    public Boolean updateStorageAndIncreaseOrderNum(@RequestBody List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList,
                                                    @RequestParam("type") Integer type) {
        // 1.更新秒杀库存
        boolean flag = this.updateStorage(updateActivitySurplusStorageList, type);

        // 2.更新活动下单数
        //秒杀ID集合
        List<Long> seckillIds = updateActivitySurplusStorageList.stream().filter(list ->
                list.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value())
                .map(UpdateActivitySurplusStorageDTO::getActivityId).distinct().collect(Collectors.toList());

        //限时抢购ID集合
        List<Long> flashSaleIds = updateActivitySurplusStorageList.stream().filter(list ->
                list.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value())
                .map(UpdateActivitySurplusStorageDTO::getActivityId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(seckillIds)) {
            seckillActivityService.increaseOrderNum(seckillIds);
        }
        if (CollectionUtils.isNotEmpty(flashSaleIds)) {
            flashSaleActivityService.increaseOrderNum(flashSaleIds);
        }

        // 3.更新活动商品下单数
        this.increaseOrderNum(updateActivitySurplusStorageList);
        return flag;
    }

    /**
     * 功能描述：
     * <设置秒杀提醒>
     *
     * @param setRemingDTO 是指秒杀提醒实体
     * @param memberId     会员id
     * @return
     * @date 2020/3/16 17:43
     * @author 刘远杰
     **/
    @Override

    public String remindSetting(@RequestBody SetRemingDTO setRemingDTO, @RequestParam("memberId") Long memberId) {
        // 1.获取秒杀设置
        Object obj = redisUtils.get(ActivityRedisConstants.SECKILL_SESSION);
        if (obj == null) {
            log.info("设置提醒失败");
            return "设置提醒失败";
        }
        List<SeckillSessionDTO> sessionList = JSONArray.parseArray(obj.toString(), SeckillSessionDTO.class);

        // 匹配当前秒杀场次
        SeckillSessionDTO seckillSession = null;
        for (SeckillSessionDTO session : sessionList) {
            if (session.getId().equals(setRemingDTO.getSessionId())) {
                seckillSession = session;
                break;
            }
        }
        if (seckillSession == null) {
            log.info("设置提醒失败");
            return "设置提醒失败";
        }

        // 3..查询mongo 商品提醒设置
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("activityId", setRemingDTO.getActivityId());
        queryParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
        queryParams.put("goodsId", setRemingDTO.getGoodsId());
        queryParams.put("memberId", memberId);
        List<String> remindJsonList = nosqlService.queryData(ActivityMongoConstants.ACTIVITY_GOODS_REMIND, queryParams);
        if (CollectionUtils.isNotEmpty(remindJsonList)) {
            if (setRemingDTO.getRemingFlag() != null && setRemingDTO.getRemingFlag() == 1) {
                nosqlService.deleteData(ActivityMongoConstants.ACTIVITY_GOODS_REMIND, queryParams);
                return "";
            }
            log.info("设置提醒失败，已设置提醒");
            return "设置提醒失败，已设置提醒";
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("id", IdGenerator.defaultSnowflakeId());
            data.put("activityId", setRemingDTO.getActivityId());
            data.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
            data.put("goodsId", setRemingDTO.getGoodsId());
            data.put("memberId", memberId);
            // 0未设置 1已设置未发送 2已设置已发送
            data.put("remindFlag", 1);
            data.put("remindDate", DateUtils.addDateMinutes(seckillSession.getActivityStartDate(), -seckillSession.getReminderTime()));
            nosqlService.saveData(ActivityMongoConstants.ACTIVITY_GOODS_REMIND, data);
            return "";
        }
    }

    /**
     * 功能描述:
     * 〈更新活动商品剩余库存〉
     *
     * @param updateActivitySurplusStorageList 秒杀库存更新实体集合
     * @param type                             0扣减库存操作 1增加库存操作（规格删除不操做）
     * @return : void
     * @author : 刘远杰
     */
    @Override

    public boolean updateStorage(@RequestBody List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList, @RequestParam("type") Integer type) {
        log.info("ActivityGoodsServiceImpl.updateStorage({},{})", updateActivitySurplusStorageList, type);

        List<UpdateActivitySurplusStorageDTO> specEsUpdateList = new ArrayList<>();
        // 遍历要修改的商品 - 查询redis秒杀库存 - 同步数据库秒杀库存 - 更新商品es活动库存
        for (UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO : updateActivitySurplusStorageList) {
            // 查询redis秒杀活动库存
            String key = "";
            // 区分秒杀和拼团
            if (updateActivitySurplusStorageDTO.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()) {
                key = ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + updateActivitySurplusStorageDTO.getActivityId() + "_" + updateActivitySurplusStorageDTO.getSpecId();
            } else if (updateActivitySurplusStorageDTO.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value()) {
                key = ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + updateActivitySurplusStorageDTO.getActivityId() + "_" + updateActivitySurplusStorageDTO.getSpecId();
            } else if (updateActivitySurplusStorageDTO.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()) {
                key = ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + updateActivitySurplusStorageDTO.getActivityId() + "_" + updateActivitySurplusStorageDTO.getSpecId();
            }

            Object obj = redisUtils.get(key);
            if (type == 1 && obj == null) {
                // 添加秒杀库存操作 redis查询无库存 则秒杀结束 放弃增加秒杀库存
                continue;
            }
            Integer activitySurplusStorage = Integer.parseInt(obj.toString());

            // 设置同步后数据库活动库存 更新数据库活动剩余库存
            updateActivitySurplusStorageDTO.setActivitySurplusStorage(activitySurplusStorage);
            baseDao.updateActivitySurplusStorage(updateActivitySurplusStorageDTO);

            specEsUpdateList.add(updateActivitySurplusStorageDTO);
        }

        if (CollectionUtils.isNotEmpty(specEsUpdateList)) {
            // 发送mq修改商品es活动库存
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_UPDATE_ES_GOODS_ACTIVITY_SURPLUS_STORAGE_QUEUE,
                    JSONArray.toJSONString(specEsUpdateList));
            //更新购物车活动库存
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS,
                    JSONArray.toJSONString(specEsUpdateList));
        }
        return true;
    }

    /**
     * 功能描述：
     * <活动订单+1>
     *
     * @param updateActivitySurplusStorageList
     * @return
     * @date 2020/3/30 10:59
     * @author 刘远杰
     **/
    @Override

    public void increaseOrderNum(@RequestBody List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList) {
        baseDao.increaseOrderNum(updateActivitySurplusStorageList);

        List<UpdateActivitySurplusStorageDTO> dtoList = new ArrayList<>();
        for (UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO : updateActivitySurplusStorageList) {
            if (updateActivitySurplusStorageDTO.getGoodsId() != null) {
                UpdateActivitySurplusStorageDTO dto = new UpdateActivitySurplusStorageDTO();
                dto.setGoodsId(updateActivitySurplusStorageDTO.getGoodsId());
                dto.setActivityId(updateActivitySurplusStorageDTO.getActivityId());
                dto.setActivityType(updateActivitySurplusStorageDTO.getActivityType());

                if (!dtoList.contains(dto)) {
                    dtoList.add(dto);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(dtoList)) {
            baseDao.increaseSpuOrderNum(dtoList);
        }
    }

    /**
     * 查询具体商品信息
     *
     * @param activityId   活动id
     * @param specId       活动商品规格id
     * @param activityType 活动类型
     * @return
     * @date 2020-03-23 18:05
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public ActivityGoodsDTO goodsAndSpec(@RequestParam("activityId") Long activityId, @RequestParam("specId") Long specId, @RequestParam("activityType") Integer activityType) {
        QueryWrapper<ActivityGoodsEntity> queryWrapper = new QueryWrapper<>();
        // 根据满减ID,店铺ID查询信息
        queryWrapper.eq("activity_id", activityId).eq("spec_id", specId).eq("activity_type", activityType);
        ActivityGoodsEntity entity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(entity, ActivityGoodsDTO.class);
    }

    /**
     * 功能描述：
     * <查询所有秒杀活动商品>
     *
     * @param
     * @return
     * @date 2020/3/26 17:31
     * @author 刘远杰
     **/
    @Override

    public List<SpecActivityEsDTO> getAllSeckillSpecActivity() {
        return baseDao.selectAllSeckillSpecActivity();
    }

    @Override

    public List<SpecActivityEsDTO> getAllFlashSaleSpecActivity() {
        return baseDao.selectAllFlashSaleSpecActivity();
    }

    /**
     * 功能描述：
     * <校验商品是否参见促销活动>
     *
     * @param goodsIds 商品id集合
     * @return
     * @date 2020/4/1 17:24
     * @author 刘远杰
     **/
    @Override

    public UpdateGoodsCheckActivityDTO checkGoodsActivity(@RequestBody List<Long> goodsIds) {
        UpdateGoodsCheckActivityDTO updateGoodsCheckActivityDTO = new UpdateGoodsCheckActivityDTO();
        updateGoodsCheckActivityDTO.setActivityFlag(0);
        updateGoodsCheckActivityDTO.setOperationMsg("");
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            // 查询商品参加的秒杀活动
            List<SpecActivityEsDTO> activityGoodsList = Optional.of(baseDao.selectSeckillActivityByGoodsIds(goodsIds))
                    .orElse(new ArrayList<>());
            List<SpecActivityEsDTO> groupActivityGoodsList = baseDao.selectGroupActivityByGoodsIds(goodsIds);
            List<SpecActivityEsDTO> flashSaleActivityGoods = baseDao.selectFlashSaleActivityGoodsByGoodsIds(goodsIds);
            activityGoodsList.addAll(groupActivityGoodsList);
            activityGoodsList.addAll(flashSaleActivityGoods);
            //拼团众筹活动状态 0：未开始 1：活动中 2：活动结束(默认未开始为0)
            //限时抢购活动状态 (默认:0未开始，1进行中，2已结束)
            //秒杀活动状态    0未开始 1进行中 2已结束
            if (CollectionUtils.isNotEmpty(activityGoodsList)) {
                for (SpecActivityEsDTO specActivityEsDTO : activityGoodsList) {
                    if (specActivityEsDTO.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value()) {
                        // 即将开始覆盖无活动
                        updateGoodsCheckActivityDTO.setActivityFlag(1);
                        updateGoodsCheckActivityDTO.setOperationMsg("存在选择商品已参加促销活动，编辑后该商品会从促销活动移除");
                    } else {
                        // 进行中覆盖无活动、即将开始
                        updateGoodsCheckActivityDTO.setActivityFlag(2);
                        updateGoodsCheckActivityDTO.setOperationMsg("存在选择商品已参加促销活动，从促销活动移除后可编辑");
                        break;
                    }
                }
            }
        }
        return updateGoodsCheckActivityDTO;
    }

    /**
     * 功能描述：
     * <编辑商品更新商品活动 促销未开始：删除活动商品 促销进行中：编辑失败>
     *
     * @param goodsIds 商品id集合
     * @return
     * @date 2020/4/1 17:24
     * @author 刘远杰
     **/
    @Override
    public Boolean updateGoodsActivityHandle(@RequestBody List<Long> goodsIds) {
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            // 需要删除的活动商品集合
            Map<String, List<SpecActivityEsDTO>> params = new HashMap<>();

            // 秒杀活动 促销未开始：删除活动商品 促销进行中：编辑失败
            List<SpecActivityEsDTO> seckillActivityGoodsList = baseDao.selectSeckillActivityGoodsByGoodsIds(goodsIds);
            if (CollectionUtils.isNotEmpty(seckillActivityGoodsList)) {
                for (SpecActivityEsDTO seckillGoods : seckillActivityGoodsList) {
                    if (seckillGoods.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value()) {
                        // 修改活动商品集合按活动分组
                        groupActivityGoods(params, seckillGoods);
                    } else {
                        // 进行中商品不可编辑
                        return false;
                    }
                }
            }
            // 拼团活动 促销未开始：删除活动商品 促销进行中：编辑失败
            List<SpecActivityEsDTO> groupActivityGoodsList = baseDao.selectGroupActivityGoodsByGoodsIds(goodsIds);
            if (CollectionUtils.isNotEmpty(groupActivityGoodsList)) {
                for (SpecActivityEsDTO groupGoods : groupActivityGoodsList) {
                    if (groupGoods.getActivityState() == GroupsEnum.ACTIVITY_STATUS_NO.value()) {
                        // 修改活动商品集合按活动分组
                        groupActivityGoods(params, groupGoods);
                    } else {
                        // 进行中商品不可编辑
                        return false;
                    }
                }
            }


            // 限时抢购活动 促销未开始：  删除活动商品 促销进行中：编辑失败
            List<SpecActivityEsDTO> flashSaleActivityGoods = baseDao.selectFlashSaleActivityGoodsByGoodsIds(goodsIds);
            if (CollectionUtils.isNotEmpty(flashSaleActivityGoods)) {
                for (SpecActivityEsDTO flashGoods : flashSaleActivityGoods) {
                    if (flashGoods.getActivityState() == FlashSaleActivityEnum.ACTIVITY_STATE_PREPARE.value()) {
                        // 修改活动商品集合按活动分组
                        groupActivityGoods(params, flashGoods);
                    } else {
                        // 进行中商品不可编辑
                        return false;
                    }
                }
            }
            // 删除活动商品
            if (CollectionUtils.isNotEmpty(params.keySet())) {
                for (String key : params.keySet()) {
                    List<SpecActivityEsDTO> goodsByActivity = params.get(key);
                    Long activityId = Long.parseLong(key.split("_")[0]);
                    Integer activityType = Integer.parseInt(key.split("_")[1]);
                    // 删除活动商品
                    List<ActivityGoodsEntity> entityList = ConvertUtils.sourceToTarget(goodsByActivity, ActivityGoodsEntity.class);
                    // 回退库存
                    this.addGoodsSpecStorage(entityList);

                    List<Long> goodsIdsByActivity = entityList.stream().map(ActivityGoodsEntity::getGoodsId).distinct().collect(Collectors.toList());
                    List<Long> idsByActivity = entityList.stream().map(ActivityGoodsEntity::getId).collect(Collectors.toList());

                    // 删除es索引活动
                    deleteEsSpecActivity(activityId, activityType, goodsIdsByActivity);
                    // 更新购物车es
                    this.deleteCartEsSpecActivity(activityId, activityType, goodsIdsByActivity);

                    // 删除活动商品
                    baseDao.deleteBatchIds(idsByActivity);
                }
            }

        }
        return true;
    }

    /**
     * 功能描述：
     * <计算商品活动剩余总库存>
     *
     * @param goodsId 商品spu id
     * @return
     * @date 2020/4/7 10:57
     * @author 刘远杰
     **/
    @Override

    public Map<Long, Integer> getActivityGoodsSurplusStorage(@RequestParam("goodsId") Long goodsId) {
        Map<Long, Integer> storageMap = new HashMap<>();
        // 秒杀商品查询
        List<SpecActivityEsDTO> seckillActivityGoodsList = Optional.of(
                baseDao.selectSeckillActivityGoodsByGoodsIds(Collections.singletonList(goodsId))).orElse(new ArrayList<>());
        // 秒杀商品剩余库存
        getStorageMap(storageMap, seckillActivityGoodsList);

        // 拼团商品查询
        List<SpecActivityEsDTO> groupActivityGoodsList = baseDao.selectGroupActivityGoodsByGoodsIds(Collections.singletonList(goodsId));
        getStorageMap(storageMap, groupActivityGoodsList);
        // 限时抢购商品查询
        List<SpecActivityEsDTO> flashActivityGoodsList = baseDao.selectFlashSaleActivityGoodsByGoodsIds(Collections.singletonList(goodsId));
        getStorageMap(storageMap, flashActivityGoodsList);
        return storageMap;
    }

    private void getStorageMap(Map<Long, Integer> storageMap, List<SpecActivityEsDTO> activityGoodsList) {
        if (CollectionUtils.isNotEmpty(activityGoodsList)) {
            for (SpecActivityEsDTO activityGoods : activityGoodsList) {
                if (storageMap.containsKey(activityGoods.getSpecId())) {
                    Integer surplusStorage = storageMap.get(activityGoods.getSpecId());
                    surplusStorage += activityGoods.getActivitySurplusStorage();
                    storageMap.put(activityGoods.getSpecId(), surplusStorage);
                } else {
                    storageMap.put(activityGoods.getSpecId(), activityGoods.getActivitySurplusStorage());
                }
            }
        }
    }

    /**
     * 功能描述：
     * <修改活动商品集合按活动分组>
     *
     * @param params        分组结果
     * @param activityGoods 活动商品
     * @return
     * @date 2020/4/2 17:56
     * @author 刘远杰
     **/
    private void groupActivityGoods(Map<String, List<SpecActivityEsDTO>> params, SpecActivityEsDTO activityGoods) {
        String key = activityGoods.getActivityId() + "_" + activityGoods.getActivityType();
        if (params.containsKey(key)) {
            List<SpecActivityEsDTO> goodsByActivity = params.get(key);
            goodsByActivity.add(activityGoods);
        } else {
            List<SpecActivityEsDTO> list = new ArrayList<>();
            list.add(activityGoods);
            params.put(key, list);
        }
    }

    /**
     * 查询所有拼团活动商品
     *
     * @return
     * @date 2020-04-01 17:15
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public List<SpecActivityEsDTO> getAllGroupSpecActivity() {
        return baseDao.selectAllGroupSpecActivity();
    }

//    public UpdateGoodsCheckActivityDTO checkGoodsActivity(List<Long> goodsIds) {
//        if (CollectionUtils.isNotEmpty(goodsIds)) {
//            for (Long goodsId : goodsIds) {
//                // 促销商品秒杀活动
//                List<ActivityGoodsDTO> allSeckillGoods = this.getAllSeckillGoods(null, goodsId);
//            }
//
//        }
//
//    }

    /**
     * 功能描述：
     * <增加商品规格库存>
     *
     * @param entityList 活动商品集合
     * @return
     * @date 2020/4/1 10:46
     * @author 刘远杰
     **/
    private void addGoodsSpecStorage(List<ActivityGoodsEntity> entityList) {
        Map<String, String> params = new HashMap<>();
        // 遍历活动商品 - 封装商品库存增加参数
        if (CollectionUtils.isNotEmpty(entityList)) {
            entityList.forEach(entity -> params.put(entity.getSpecId().toString(), String.valueOf(-entity.getActivitySurplusStorage())));
            if (params.size() > 0) {
                // 增加商品库存
                goodsSpecService.updateStorage(params, 1);
            }
        }
    }

    /**
     * 功能描述：
     * <扣减商品规格库存>
     *
     * @param entityList 活动商品集合
     * @return
     * @date 2020/4/1 10:46
     * @author 刘远杰
     **/
    private void reduceGoodsSpecStorage(List<ActivityGoodsEntity> entityList) {
        Map<String, String> params = new HashMap<>();
        // 遍历活动商品 - 封装商品库存扣减参数
        if (CollectionUtils.isNotEmpty(entityList)) {
            entityList.forEach(entity -> params.put(entity.getSpecId().toString(), String.valueOf(entity.getActivityStorage())));
            if (params.size() > 0) {
                // 增加商品库存
                goodsSpecService.updateStorage(params, 0);
            }
        }
    }

    /**
     * 查询活动提醒集合并发送消息推送
     *
     * @date 2020/4/23 9:07
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public void activityReminder(@RequestParam("tiem") Long time) {

        // 1.查询待发送活动通知
        Date now = new Date(time);
        // 查询条件: 推送时间<=当前时间 未推送 秒杀
        Query query = Query.query(Criteria.where("remindFlag").is(1)
                .and("activityType").is(ActivityTypeEnum.SECKILL_ACTIVITY.value())
                .and("remindDate").lte(now));
        List<ActivityReminderDTO> reminderList = mongoTemplate.find(query, ActivityReminderDTO.class, ActivityMongoConstants.ACTIVITY_GOODS_REMIND);

        reminderList.forEach(ar -> {
            // 查询会员
            MemberDTO memberDTO = Optional.ofNullable(memberService.getById(ar.getMemberId())).orElse(new MemberDTO());
            // 查询商品
            GoodsDTO goodsDTO = Optional.ofNullable(goodsService.get(ar.getGoodsId())).orElse(new GoodsDTO());

            Map<String, Object> map = new HashMap<>(16);

            Map<Long, String> memberMap = new HashMap<>(5);
            memberMap.put(memberDTO.getId(), memberDTO.getMemberName());
            map.put("memberMap", memberMap);

            // 消息参数封装
            Map<String, String> paramsMap = new HashMap<>(5);
            paramsMap.put("Username", replaceStr(memberDTO.getNickName()));
            paramsMap.put("sellerName", replaceStr(goodsDTO.getStoreName()));
            paramsMap.put("goodsName", replaceStr(goodsDTO.getGoodsName()));
            paramsMap.put("goodsId", String.valueOf(ar.getGoodsId()));
            paramsMap.put("specId", String.valueOf(goodsDTO.getSpecId()));
            map.put("paramMap", paramsMap);

            map.put("mobile", memberDTO.getMemberMobile());

            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageCode(MessageCodeEnum.SECKLL_PRESTART_SMS.value());
            messageDTO.setSendName("系统");
            messageDTO.setParamJson(JSON.toJSONString(map));
            messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
            sysMessageService.save(messageDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));

            // 更新推送状态
            Update update = new Update();
            update.set("remindFlag", 2);
            mongoTemplate.upsert(query, update, ActivityMongoConstants.ACTIVITY_GOODS_REMIND);
        });
    }


    @Override
    public PageData<AdminSeckillGoodsDTO> alreadyActivityGoodsList(@RequestParam Map<String, Object> params) {
        // 查询秒杀商品 - 查询秒杀商品对应商品数据 - 匹配对应商品封装页面展示数据
        // 1.查询活动商品
        IPage page = getPage(params, null, false);
        List<AdminSeckillGoodsDTO> seckillGoodsList = baseDao.alreadyAddActivityGoodsPage(params, page);

        Long storeId = null;
        if (params.get("storeId") != null) {
            storeId = Long.parseLong(params.get("storeId").toString());
        }

        if (CollectionUtils.isNotEmpty(seckillGoodsList)) {
            // 查询商品数据
            List<Long> goodsIds = seckillGoodsList.stream().map(AdminSeckillGoodsDTO::getGoodsId).collect(Collectors.toList());
            List<GoodsDTO> goodsList = goodsService.getByGoodsIds(goodsIds, storeId);

            if (CollectionUtils.isNotEmpty(goodsList)) {
                // 匹配商品数据 封装商品名称销售价
                for (GoodsDTO goods : goodsList) {
                    for (AdminSeckillGoodsDTO seckillGoods : seckillGoodsList) {
                        if (goods.getId().equals(seckillGoods.getGoodsId())) {
                            seckillGoods.setGoodsName(goods.getGoodsName());
                            seckillGoods.setSpecSellPrice(goods.getSpecSellPrice());
                            seckillGoods.setGoodsMainPicture(goods.getGoodsMainPicture());
                            break;
                        }
                    }
                }
            }
        }
        return new PageData<AdminSeckillGoodsDTO>(seckillGoodsList, page.getTotal());
    }


    @Override
    public Boolean updateSpecIndexByActivityId(@RequestBody List<Long> activityIds, @RequestParam("activityType") Integer activityType) {
        if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == activityType) {
            this.syncFlashSalseGoodsIndex(activityIds);
        }
        return true;
    }

    /**
     * 同步限时抢购活动商品索引
     *
     * @param activityIds 活动ID集合
     * @author xuzhch
     * @date 2020年10月20日
     */
    private void syncFlashSalseGoodsIndex(List<Long> activityIds) {
        //获取活动信息集合
        List<FlashSaleActivityDTO> activityDTOList = flashSaleActivityService.getInfoListByActivityIds(activityIds);
        //遍历活动集合更新活动商品索引
        activityDTOList.forEach(activityDTO -> {
            List<ActivityGoodsDTO> activityGoodsList = this.getByActivityIds(Collections.singletonList(activityDTO.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
            if (FlashSaleActivityEnum.ACTIVITY_STATE_END.value() == activityDTO.getActivityState()) {
                //删除限时抢购活动商品索引
                this.deleteFlashSaleGoodsIndex(activityDTO, activityGoodsList);
            } else {
                //创建或者更新限时抢购活动商品索引
                this.createFlashSaleGoodsIndex(activityDTO, activityGoodsList);
            }
        });
    }

    /**
     * 更新限时抢购活动商品索引
     *
     * @param activityDTO       活动信息
     * @param activityGoodsList 活动商品信息
     * @author xuzhch
     * @date 2020年10月20日
     */
    private void createFlashSaleGoodsIndex(FlashSaleActivityDTO activityDTO, List<ActivityGoodsDTO> activityGoodsList) {
        List<Map<String, Object>> updateDataList = new ArrayList<>();
        activityGoodsList.forEach(activityGoods -> {
            // 封装spec索引活动
            SpecActivityVO specActivity = new SpecActivityVO();
            // 活动数据封装
            BeanCopier.create(FlashSaleActivityDTO.class, SpecActivityVO.class, false)
                    .copy(activityDTO, specActivity, null);
            // 活动商品数据封装
            BeanCopier.create(ActivityGoodsDTO.class, SpecActivityVO.class, false)
                    .copy(activityGoods, specActivity, null);
            specActivity.setId(activityGoods.getId());
            specActivity.setActivityState(activityDTO.getActivityState());

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
            log.info("秒杀活动开始、即将开始更新商品es成功，activityId:{},updateDataList:{}", activityDTO.getId(), updateDataList);
        }
    }

    /**
     * 删除限时抢购活动商品索引
     *
     * @param activityDTO       活动信息
     * @param activityGoodsList 活动商品信息
     * @author xuzhch
     * @date 2020年10月20日
     */
    private void deleteFlashSaleGoodsIndex(FlashSaleActivityDTO activityDTO, List<ActivityGoodsDTO> activityGoodsList) {
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
            log.info("秒杀活动结束更新商品es成功，activityId:{},updateDataList:{}", activityDTO.getId(), updateDataList);
        }
    }

    /**
     * 更新活动购物车ES 根据活动ID集合
     *
     * @param activityIds  活动ID集合
     * @param activityType 活动类型
     * @return the boolean
     * @author xuzhch
     * @date 2020年10月20日
     */

    @Override
    public Boolean updateCartIndexByActivityId(@RequestBody List<Long> activityIds, @RequestParam("activityType") Integer activityType) {
        //限时抢购活动
        if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == activityType) {
            this.syncFlashSaleCartIndex(activityIds);
        }
        return true;
    }

    /**
     * 同步限时抢购活动商品索引
     *
     * @param activityIds 活动ID集合
     * @author xuzhch
     * @date 2020年10月20日
     */
    private void syncFlashSaleCartIndex(List<Long> activityIds) {
        //获取限时抢购活动集合
        List<FlashSaleActivityDTO> activityDTOList = flashSaleActivityService.getInfoListByActivityIds(activityIds);
        //遍历活动集合 更新活动购物车索引
        activityDTOList.forEach(activityDTO -> {
            List<ActivityGoodsDTO> activityGoodsList = this.getByActivityIds(Collections.singletonList(activityDTO.getId()), ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
            if (FlashSaleActivityEnum.ACTIVITY_STATE_END.value() == activityDTO.getActivityState()) {
                //删除活动购物车索引
                this.deleteFlashSaleCartIndex(activityGoodsList);
            } else {
                //更新活动购物车索引
                this.createFlashSaleCartIndex(activityDTO, activityGoodsList);
            }
        });
    }

    /**
     * 更新限时抢购活动购物车索引
     *
     * @param activityDTO       活动信息
     * @param activityGoodsList 活动商品信息
     * @author xuzhch
     * @date 2020年10月20日
     */
    private void createFlashSaleCartIndex(FlashSaleActivityDTO activityDTO, List<ActivityGoodsDTO> activityGoodsList) {
        activityGoodsList.forEach(activityGoods -> {
            // 更新条件 指定规格购物车
            PageModelDTO pageModelDTO = new PageModelDTO();
            Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
            equalsSearchCondition.put("specId", activityGoods.getSpecId());

            // 更新活动类型和价格
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("activityId", activityDTO.getId());
            updateData.put("status", CartEnum.CART_STATUS_NORMAL.value());
            updateData.put("activityType", ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
            updateData.put("activitySurplusStorage", activityGoods.getActivitySurplusStorage());
            updateData.put("activityEndDate", activityDTO.getActivityEndDate());
            updateData.put("restrictionQuantity", activityDTO.getRestrictionQuantity());
            updateData.put("specSellPrice", activityGoods.getActivityPrice());

            // 更新购物车es
            esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
            log.info("更新限时抢购活动购物车，activityId:{},specId:{}", activityGoods.getActivityId(), activityGoods.getSpecId());

            // 保存redis活动库存
            String storageKey = ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
            redisUtils.set(storageKey, activityGoods.getActivitySurplusStorage(), -1L);
            log.info("更新限时抢购活动库存成功，storageKey:{}，value:{}", storageKey, activityGoods.getActivitySurplusStorage());

            // 保存redis活动价格
            String priceKey = ActivityRedisConstants.FLASH_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
            redisUtils.set(priceKey, activityGoods.getActivityPrice().toString(), -1L);
            log.info("更新限时抢购开始保存redis活动价格成功，priceKey:{}，value:{}", storageKey, activityGoods.getActivityPrice());
        });
    }

    /**
     * 删除限时抢购活动购物车索引
     *
     * @param activityGoodsList 活动商品信息
     * @author xuzhch
     * @date 2020年10月20日
     */
    private void deleteFlashSaleCartIndex(List<ActivityGoodsDTO> activityGoodsList) {
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
            log.info("限时抢购活动结束更新购物车成功，activityId:{},specId:{}", activityGoods.getActivityId(), activityGoods.getSpecId());

            // 删除redis活动库存
            String storageKey = ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
            redisUtils.delete(storageKey);
            log.info("限时抢购活动结束删除redis活动库存成功，storageKey:{}", storageKey);

            // 删除reids活动商品金额
            String priceKey = ActivityRedisConstants.FLASH_GOODS_PRICE + activityGoods.getActivityId() + "_" + activityGoods.getSpecId();
            redisUtils.delete(priceKey);
            log.info("限时抢购活动结束删除redis活动价格成功，priceKey:{}", priceKey);
        });
    }

    /**
     * 字段提取  短信发送只能发送20个汉字
     *
     * @param str: 提取字段
     * @return 结果
     */
    private String replaceStr(String str) {
        if (StringUtils.isNotBlank(str) && str.length() > 15) {
            str = str.substring(0, 12) + "...";
        }
        return str;
    }

}
