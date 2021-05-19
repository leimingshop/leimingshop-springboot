/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.constants.IndexRedisConstants;
import com.leimingtech.dto.setting.SettingAftersaleDTO;
import com.leimingtech.dto.setting.SettingSeniorDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dao.order.OrderGoodsDao;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.entity.order.OrderGoodsEntity;
import com.leimingtech.modules.enums.order.AfterSaleEnum;
import com.leimingtech.modules.enums.order.AfterSettingEnum;
import com.leimingtech.modules.enums.order.VirtualFlagEnum;
import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.order.OrderAddressService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.statuscode.OrderStatusCode;
import com.leimingtech.modules.vo.order.PcOrderGoodsInfoVO;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单商品表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Service
@Transactional

@Slf4j
public class OrderGoodsServiceImpl extends CrudServiceImpl<OrderGoodsDao, OrderGoodsEntity, OrderGoodsDTO>
        implements OrderGoodsService {

    private static final int TRANSFORM_SECOND = 86400;

    @Autowired

    private OrderService orderService;

    @Autowired

    private OrderAddressService orderAddressService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private EvaluateGoodsService evaluateGoodsService;

    /**
     * 功能描述:
     * 〈保存订单商品〉
     *
     * @param dto 订单商品
     * @author : 刘远杰
     */

    @Override
    public void saveOrderGoods(@RequestBody OrderGoodsDTO dto) {
        super.save(dto);
    }

    /**
     * 功能描述:
     * 〈修改订单商品〉
     *
     * @param dto 订单商品
     * @author : 刘远杰
     */

    @Override
    public void updateOrderGoods(@RequestBody OrderGoodsDTO dto) {
        super.update(dto);
    }

    /**
     * 功能描述:
     * 〈根据id查询订单商品〉
     *
     * @param id 订单商品id
     * @author : 刘远杰
     */

    @Override
    public OrderGoodsDTO getById(Long id,
                                 @RequestParam(value = "buyerId", required = false) Long buyerId,
                                 @RequestParam(value = "storeId", required = false) Long storeId) {
        QueryWrapper<OrderGoodsEntity> wrapper = new QueryWrapper<>();
        getWrapper(buyerId, storeId, wrapper);
        wrapper.eq(id != null, "id", id);

        OrderGoodsEntity entity = baseDao.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(entity, OrderGoodsDTO.class);

    }

    /**
     * 功能描述:
     * 〈根据订单id查询订单商品集合〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    @Override
    public List<OrderGoodsDTO> getByOrderId(Long orderId,
                                            @RequestParam(value = "buyerId", required = false) Long buyerId,
                                            @RequestParam(value = "storeId", required = false) Long storeId) {
        QueryWrapper<OrderGoodsEntity> wrapper = new QueryWrapper<>();
        getWrapper(buyerId, storeId, wrapper);
        wrapper.eq(orderId != null, "order_id", orderId);
        List<OrderGoodsEntity> entities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entities, OrderGoodsDTO.class);
    }

    /**
     * 功能描述:
     * 〈根据paySn查询订单商品集合〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    @Override
    public List<OrderGoodsDTO> getByPaySn(Long paySn) {
        List<OrderGoodsEntity> orderGoodsEntityList = baseDao.findByPaySn(paySn);
        return ConvertUtils.sourceToTarget(orderGoodsEntityList, OrderGoodsDTO.class);
    }

    /**
     * 根据父订单号查询订单商品信息
     *
     * @param parentOrderSn: 父订单编号
     * @return 订单商品列表信息
     * @date 2019/11/6 18:16
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public List<OrderGoodsDTO> findOrderGoodsByParentOrderSn(@RequestParam(value = "parentOrderSn", required = false) Long parentOrderSn) {
        return baseDao.findOrderGoodsByParentOrderSn(parentOrderSn);
    }

    /**
     * 功能描述:
     * 〈根据id删除订单商品〉
     *
     * @param id 订单商品id
     * @author : 刘远杰
     */

    @Override
    public int deleteById(Long id,
                          @RequestParam(value = "buyerId", required = false) Long buyerId,
                          @RequestParam(value = "storeId", required = false) Long storeId) {
        QueryWrapper<OrderGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        getWrapper(buyerId, storeId, wrapper);
        return baseDao.delete(wrapper);
    }

    /**
     * 功能描述:
     * 〈根据orderId删除订单商品〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    @Override
    public int deleteByOrderId(Long orderId,
                               @RequestParam(value = "buyerId", required = false) Long buyerId,
                               @RequestParam(value = "storeId", required = false) Long storeId) {
        QueryWrapper<OrderGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(orderId != null, "order_id", orderId);
        getWrapper(buyerId, storeId, wrapper);
        return baseDao.delete(wrapper);
    }

    /**
     * 功能描述:
     * 〈查询商品购买数量〉
     *
     * @param buyerId 买家id
     * @param goodsId 商品id
     * @author : 刘远杰
     */

    @Override
    public int countByGoodsId(@RequestParam("buyerId") Long buyerId, @RequestParam("goodsId") Long goodsId) {
        return baseDao.countByGoodsId(buyerId, goodsId);
    }

    /**
     * 分页查未评价的商品
     *
     * @param params 分页参数
     * @author : 程前
     */
    @Override

    public PageData<EvaluateOrderGoodsDTO> findNotEvaluateGoods(@RequestParam Map<String, Object> params) {
        params.put(Constant.ORDER_FIELD, Constant.CREATE_DATE);
        params.put(Constant.ORDER, Constant.DESC);
        //分页
        IPage<OrderGoodsEntity> page = getPage(params, "create_date", false);
        //查询
        List<EvaluateOrderGoodsDTO> list = baseDao.findPage(params);
        return getPageData(list, page.getTotal(), EvaluateOrderGoodsDTO.class);
    }

    /**
     * 功能描述:
     * 〈查询未评价订单商品数量〉
     *
     * @param params 参数
     * @author : 刘远杰
     */
    @Override

    public Integer countNotEvaluateGoods(@RequestParam Map<String, Object> params) {
        return baseDao.countNotEvaluate(params);
    }

    /**
     * 根据订单ID 查询订单商品表中未评价的商品
     *
     * @param orderId 订单ID
     * @return
     */
    @Override

    public List<EvaluateOrderGoodsDTO> findOrderGoodsList(@RequestParam("orderId") Long orderId) {

        return baseDao.findOrderGoodsList(orderId);
    }

    private void getWrapper(Long buyerId, Long storeId, QueryWrapper<OrderGoodsEntity> wrapper) {
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        wrapper.eq(storeId != null, "store_id", storeId);
    }

    @Override
    public QueryWrapper<OrderGoodsEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * @param id:订单项id
     * @Author: SWH ab4856812@163.com
     * @Description:可申请售后订单中商品
     * @Date: 2019/6/17 22:27
     * @Version: V1.0
     */

    @Override
    public AvailableAfterSaleOrderGoodsDTO getAvailGoodsDetail(Long id) {
        //根据订单项id查询订单详情
        AvailableAfterSaleOrderGoodsDTO availableAfterSaleOrderGoodsDTO = new AvailableAfterSaleOrderGoodsDTO();
        QueryWrapper<OrderGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        OrderGoodsEntity entity = baseDao.selectOne(wrapper);
        if (entity.getAftersaleQuantity() == 0) {
            throw new ServiceException(OrderStatusCode.AFTER_GOODS_COUNT_IS_ZERO);
        }
        BeanCopier.create(OrderGoodsEntity.class, AvailableAfterSaleOrderGoodsDTO.class, false).copy(entity, availableAfterSaleOrderGoodsDTO, null);
        //设置可退货,可换货字段默认值
        availableAfterSaleOrderGoodsDTO.setIsReturn(AfterSettingEnum.FALSE.value());
        availableAfterSaleOrderGoodsDTO.setIsBarter(AfterSettingEnum.FALSE.value());
        availableAfterSaleOrderGoodsDTO.setSpecPayPrice(entity.getSpecPayPrice().add(entity.getAvgPreferentialAmount()));
        //判断当前时间和订单完成时间的差距
        if (null == availableAfterSaleOrderGoodsDTO.getOrderId()) {
            return null;
        }
        String completeTime = orderService.findCompleteTimeById(availableAfterSaleOrderGoodsDTO.getOrderId());
        if (StringUtils.isNotEmpty(completeTime)) {
            completeTime = completeTime.substring(0, completeTime.length() - 2);
            Date completeDate = DateUtils.stringToDate(completeTime, DateUtils.DATE_TIME_PATTERN);
            availableAfterSaleOrderGoodsDTO.setCompleteTime(completeDate);
            //获取setting表中的是否开启售后按钮
            String queryRedisByName = settingService.queryRedisByName(SettingsEnum.AFTERSALE.value());
            SettingAftersaleDTO aftersaleDTO = JSON.parseObject(queryRedisByName, SettingAftersaleDTO.class);
            if (null == aftersaleDTO) {
                return null;
            }
            //获取setting表中的可申请售后时间
            String setting = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
            SettingSeniorDTO senior = JSON.parseObject(setting, SettingSeniorDTO.class);
            if (null == senior) {
                return null;
            }


            AftersaleSettingDTO aftersaleSettingDTO = new AftersaleSettingDTO();
            aftersaleSettingDTO.setCannotReturn(senior.getCannotReturn());
            aftersaleSettingDTO.setCannotBarter(senior.getCannotBarter());
            aftersaleSettingDTO.setGoodsReturn(aftersaleDTO.getGoodsReturn());
            aftersaleSettingDTO.setGoodsBarter(aftersaleDTO.getGoodsBarter());
            aftersaleSettingDTO.setCompleteTime(completeDate);
            AftersaleAvailTypeDTO aftersaleAvailTypeDTO = this.findAvailType(aftersaleSettingDTO);

            Map<String, Object> map = new HashMap<>();
            map.put("id", availableAfterSaleOrderGoodsDTO.getOrderId());
            OrderDTO orderDTO = orderService.getOrderByMap(map);
            if (orderDTO.getVirtualFlag().equals(VirtualFlagEnum.YES.value())) {
                aftersaleAvailTypeDTO.setIsBarter(AfterSettingEnum.FALSE.value());
                aftersaleAvailTypeDTO.setNotBarterReson("虚拟商品不支持换货");
            }
            if (null == aftersaleAvailTypeDTO) {
                return null;
            }
            BeanCopier.create(AftersaleAvailTypeDTO.class, AvailableAfterSaleOrderGoodsDTO.class, false).copy(aftersaleAvailTypeDTO, availableAfterSaleOrderGoodsDTO, null);

            // 查询订单联系人信息
            if (null != orderDTO) {
                OrderAddressDTO orderAddressDTO = orderAddressService.get(orderDTO.getAddressId());
                if (null != orderAddressDTO) {
                    availableAfterSaleOrderGoodsDTO.setContacts(orderAddressDTO.getTrueName());
                    availableAfterSaleOrderGoodsDTO.setContactsPhone(orderAddressDTO.getMobPhone());
                    availableAfterSaleOrderGoodsDTO.setAreaId(orderAddressDTO.getAreaId());
                    availableAfterSaleOrderGoodsDTO.setProvinceId(orderAddressDTO.getProvinceId());
                    availableAfterSaleOrderGoodsDTO.setCityId(orderAddressDTO.getCityId());
                    availableAfterSaleOrderGoodsDTO.setStressId(orderAddressDTO.getStressId());
                    availableAfterSaleOrderGoodsDTO.setAddress(orderAddressDTO.getAddress());
                    availableAfterSaleOrderGoodsDTO.setAreaInfo(orderAddressDTO.getAreaInfo());
                }
            }


        }
        return availableAfterSaleOrderGoodsDTO;
    }

    @Override

    public AftersaleAvailTypeDTO findAvailType(@RequestBody AftersaleSettingDTO aftersaleSettingDTO) {
        AftersaleAvailTypeDTO aftersaleAvailTypeDTO = new AftersaleAvailTypeDTO();
        String finalDateStr = DateUtils.format(new Date());
        Date nowDate = DateUtils.stringToDate(finalDateStr, DateUtils.DATE_PATTERN);

        if ((aftersaleSettingDTO.getGoodsReturn()).equals(AfterSettingEnum.CANNOTRETURN.valueString())) {
            //不支持售后
            aftersaleAvailTypeDTO.setNotReturnReson("不支持退货");
            aftersaleAvailTypeDTO.setIsReturn(AfterSettingEnum.FALSE.value());
        } else if ((aftersaleSettingDTO.getGoodsReturn()).equals(AfterSettingEnum.CANRETURN.valueString()) && StringUtils.isNotEmpty(aftersaleSettingDTO.getCannotReturn())) {
            if (DateUtils.addDateSeconds(aftersaleSettingDTO.getCompleteTime(), Integer.valueOf(aftersaleSettingDTO.getCannotReturn()) * TRANSFORM_SECOND).before(new Date())) {
                //可申请退货截至时间小于当前时间，不允许退货，需设置原因
                aftersaleAvailTypeDTO.setNotReturnReson("已超过规定的退货时间");
                aftersaleAvailTypeDTO.setIsReturn(AfterSettingEnum.FALSE.value());
            } else {
                aftersaleAvailTypeDTO.setIsReturn(AfterSettingEnum.TRUE.value());
            }
        }
        if ((aftersaleSettingDTO.getGoodsBarter()).equals(AfterSettingEnum.CANNOTBARTER.valueString())) {
            //不支持售后
            aftersaleAvailTypeDTO.setNotBarterReson("不支持换货");
            aftersaleAvailTypeDTO.setIsBarter(AfterSettingEnum.FALSE.value());
        } else if ((aftersaleSettingDTO.getGoodsBarter()).equals(AfterSettingEnum.CANBARTER.valueString()) && StringUtils.isNotEmpty(aftersaleSettingDTO.getCannotBarter())) {
            if (DateUtils.addDateSeconds(aftersaleSettingDTO.getCompleteTime(), Integer.valueOf(aftersaleSettingDTO.getCannotBarter()) * TRANSFORM_SECOND).before(new Date())) {
                //可申请换货截止时间小于当前时间，不允许换货，需设置原因
                aftersaleAvailTypeDTO.setNotBarterReson("已超过规定的换货时间");
                aftersaleAvailTypeDTO.setIsBarter(AfterSettingEnum.FALSE.value());
            } else {
                aftersaleAvailTypeDTO.setIsBarter(AfterSettingEnum.TRUE.value());
            }
        }
        return aftersaleAvailTypeDTO;
    }

    /**
     * @param orderGoodsId:订单项id
     * @param applyNum:修改数量
     * @param updateType:修改类型（0：增加，1：减少）
     * @Author: SWH ab4856812@163.com
     * @Description:修改可售后数量
     * @Date: 2019/6/17 22:27
     * @Version: V1.0
     */
    @Override

    public void updateAftersaleQuantityById(@RequestParam("orderGoodsId") Long orderGoodsId, @RequestParam("applyNum") Integer applyNum, @RequestParam("updateType") Integer updateType) {
        if (updateType.equals(AfterSaleEnum.DEFAULT.value())) {
            baseDao.updateAftersaleQuantityByIdAdd(orderGoodsId, applyNum);
        } else if (updateType.equals(AfterSaleEnum.DEFAULTONE.value())) {
            baseDao.updateAftersaleQuantityById(orderGoodsId, applyNum);
        }
    }

    /**
     * 修改订单商品表是否已退分摊金额状态
     *
     * @param orderGoodsId
     * @param preferential
     * @author xuzhch
     */

    @Override
    public void updateReturnPreferential(@RequestParam("orderGoodsId") Long orderGoodsId, @RequestParam("preferential") int preferential) {
        super.update(Wrappers.<OrderGoodsEntity>lambdaUpdate().set(OrderGoodsEntity::getReturnPreferential, preferential).eq(OrderGoodsEntity::getId, orderGoodsId));
    }

    /**
     * 查询超时未评价的商品
     *
     * @param createTime
     * @return
     */

    @Override
    public List<OrderGoodsDTO> findOverTimeOrder(@RequestParam("createTime") String createTime) {
        return baseDao.findOverTimeOrder(createTime);
    }

    /**
     * 批量更新订单商品
     *
     * @param orderGoodsDTOS 参数
     */

    @Override
    public void updateBetch(@RequestBody List<OrderGoodsDTO> orderGoodsDTOS) {
        updateBatchById(ConvertUtils.sourceToTarget(orderGoodsDTOS, OrderGoodsEntity.class));
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOS) {
            updateOrderEvaluateStatus(orderGoodsDTO.getOrderId());
        }

    }

    /**
     * 更新订单表评价状态
     *
     * @param orderId 订单ID
     * @return
     */

    @Override
    public void updateOrderEvaluateStatus(@RequestParam Long orderId) {
        Integer evaluateNum = baseDao.findEvaluateNum(orderId);
        if (evaluateNum == 0) {
            orderService.updateEvaluateStatus(orderId);
        }
    }

    /**
     * 功能描述:
     * 〈批量保存订单商品〉
     *
     * @param dtoList 订单商品
     * @author : 刘远杰
     */

    @Override
    public Boolean saveBatch(@RequestBody List<OrderGoodsDTO> dtoList) {
        return super.insertBatch(ConvertUtils.sourceToTarget(dtoList, OrderGoodsEntity.class));
    }

    /**
     * 商品销售排名
     *
     * @param params
     * @return
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/

    @Override
    public Map<String, Object> goodsSellRanking(@RequestParam Map params) {
        Integer refresh = Integer.valueOf((String) params.get("refresh"));
        Object dateObj = params.get("date");
        Integer orderBy = Integer.valueOf((String) params.get("orderBy"));
        Integer dateType = Integer.valueOf((String) params.get("dateType"));
        Long userId = Long.valueOf(params.get("userId").toString());
        Date endDate = null;
        if (dateObj == null || refresh == 1) {
            endDate = new Date();
        } else {
            endDate = DateUtils.parse(dateObj.toString(), DateUtils.DATE_TIME_PATTERN);
        }
        if (refresh == 1) {
            redisUtils.deleteByPattern(IndexRedisConstants.INDEX_GOODS_SELL + userId + ":*");
        } else {
            Object o = redisUtils.get(IndexRedisConstants.INDEX_GOODS_SELL + userId + ":" + orderBy + ":" + dateType);
            if (o != null) {
                return (Map) o;
            }
        }
        Date startDate = DateUtils.getFixedDate(endDate, dateType);
        List<IndexGoodsSellRakingDTO> data = baseDao.selectGoodsSellRanking(startDate, endDate, orderBy);
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", endDate);
        result.put("data", data);
        redisUtils.set(IndexRedisConstants.INDEX_GOODS_SELL + userId + ":" + orderBy + ":" + dateType, result, IndexRedisConstants.JEDIS_EXPIRE);
        return result;

    }

    /**
     * 根据订单ID 查询订单商品项
     *
     * @param orderId
     */

    @Override
    public List<PcOrderGoodsInfoVO> findOrderGoodsInfo(@RequestParam("orderId") Long orderId,
                                                       @RequestParam(value = "orderGoodsId", required = false) Long orderGoodsId) {
        List<PcOrderGoodsInfoVO> list = baseDao.findOrderGoodsInfo(orderId, orderGoodsId);
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(a -> {
                Integer num = evaluateGoodsService.findEvaluateNum(a.getGoodsId());
                a.setNum(num);
                GoodsDTO goodsDTO = goodsService.get(a.getGoodsId());
                a.setGoodsSubTitle(goodsDTO.getGoodsSubTitle());
            });

        }
        return list;
    }
}
