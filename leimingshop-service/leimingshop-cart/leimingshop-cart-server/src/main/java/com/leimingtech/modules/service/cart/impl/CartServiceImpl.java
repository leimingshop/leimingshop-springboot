/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.cart.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.leimingtech.commons.lock.annotation.Lock4j;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.RangConditionDTO;
import com.leimingtech.modules.dto.activity.goods.SpecActivityDTO;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.dto.cart.*;
import com.leimingtech.modules.dto.coupons.*;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityDTO;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplateDetailDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateRuleDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.GoodsLabDTO;
import com.leimingtech.modules.dto.goods.PartGoodsSpecDTO;
import com.leimingtech.modules.dto.goods.ValidateGoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.group.GroupDTO;
import com.leimingtech.modules.dto.group.GroupRecordDTO;
import com.leimingtech.modules.dto.member.MemberBalanceInfoDTO;
import com.leimingtech.modules.dto.member.MemberUmengTokenInfo;
import com.leimingtech.modules.dto.order.freight.FreightRuleDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.entity.cart.CartEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.evaluate.GoodsEnum;
import com.leimingtech.modules.enums.flashsale.FlashSaleActivityEnum;
import com.leimingtech.modules.enums.freight.template.FreightTemplateEnum;
import com.leimingtech.modules.enums.goods.GoodsSpecStatusEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.enums.order.FreightRuleEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.service.address.MemberAddressService;
import com.leimingtech.modules.service.cart.CartService;
import com.leimingtech.modules.service.coupons.CouponsActivitySearchService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.flashsale.FlashSaleActivityService;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.group.GroupRecordService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.freight.FreightRuleService;
import com.leimingtech.modules.service.reduce.ReduceActivitySearchService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.service.reduce.ReduceGoodsService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.statuscode.CartStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.modules.vo.DisabledCartVO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Slf4j
@Service

public class CartServiceImpl implements CartService {

    private static final String SPEC_ACTIVITY_LIST = "specActivityList";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(CartServiceImpl.class);
    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberInfoService memberInfoService;
    @Autowired
    private CouponsActivitySearchService couponsActivitySearchService;
    @Autowired
    private CouponsActivityService couponsActivityService;
    @Autowired
    private ReduceActivitySearchService reduceActivitySearchService;
    @Autowired
    private ReduceActivityService reduceActivityService;
    @Autowired
    private ReduceGoodsService reduceGoodsService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRecordService groupRecordService;
    @Autowired
    private EsDataUtils esDataUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private SysMessageService sysMessageService;
    @Autowired
    private MemberAddressService memberAddressService;
    @Autowired
    private FreightRuleService freightRuleService;
    @Autowired
    private FreightTemplateService freightTemplateService;
    @Autowired
    private FlashSaleActivityService flashSaleActivityService;

    /**
     * 分页查询购物车列表
     *
     * @param memberId 用户ID
     * @return
     */
    @Override

    public List<CartDTO> getList(@RequestParam("memberId") Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        // 领取结束时间正序排序
        sortFileds.put("updateDate", "desc");

        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        // 查询已开始活动
        equalsSearchCondition.put("memberId", memberId);

        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<String> resultJson = pageModelDTO.getJsonRsList();
        List<CartDTO> cartDTOList = resultJson.stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
        return cartDTOList;
    }

    /**
     * 根据ID 获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public CartDTO get(Long id) {


        return JSONObject.parseObject(esDataUtils.getDateById(ElasticSearchConstant.CART_INDEX, String.valueOf(id)),
                CartDTO.class);
    }

    /**
     * 加入购物车
     *
     * @param dto      参数
     * @param memberId 用户ID
     */
    @Override

    @Lock4j(keys = "#memberId")
    public void save(@RequestBody SaveCartDTO dto, @RequestParam("memberId") Long memberId) {
        // 获取es商品信息
        String specEsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, dto.getSpecId().toString());
        PartGoodsSpecDTO goodsSpecDTO = JSONObject.parseObject(specEsJson, PartGoodsSpecDTO.class);

        // 数据校验：最大购买数量、商品上下架状态
        int check = check(dto, goodsSpecDTO);
        if (check != CartEnum.SUCCESS_CODE.value()) {
            throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
        }

        //  判断当前商品在购物车中是否有相同信息的商品
        CartDTO cartEs = findCartGoodsNum(dto.getSpecId(), memberId);
        if (cartEs != null) {
            // 购物车存在该商品  替换购物车数量/增加购物车数量
            if (dto.getCartId() == null) {

                cartEs.setGoodsNum(cartEs.getGoodsNum() + dto.getGoodsNum());
                // 增加购物车数量：获取购物车中得商品数量 + 当前的数量是否大于200
                if (cartEs.getGoodsNum() + dto.getGoodsNum() > CartEnum.MAX_CART_NUM.value()) {
                    cartEs.setGoodsNum(CartEnum.MAX_CART_NUM.value());
                }
                cartEs.setIsSelect(CartEnum.IS_SELECT_YES.value());
            } else {
                cartEs.setGoodsNum(dto.getGoodsNum());
            }

            // 初始设置购物车商品原销售价格及实际销售价格（秒杀销售价）--均设置为商品原销售价，获取活动价格后替换
            cartEs.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
            cartEs.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());

            // 封装购物车秒杀活动信息
            boolean addActivityGoodsFlag = false;
            if (CollectionUtils.isNotEmpty(goodsSpecDTO.getSpecActivityList())) {
                addActivityGoodsFlag = fillActivityIfno(cartEs, goodsSpecDTO);
            }

            if (!addActivityGoodsFlag) {
                // 非秒杀商品：校验商品库存、获取商品默认满减活动
                // 校验商品库存
                if (cartEs.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                    throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
                }
                // 校验活动数据是否合法,设置购物车选择满减活动
                vaildActivity(dto, cartEs, cartEs.getStoreId(), cartEs.getGoodsId(), cartEs.getBrandId());
            }

            // 更新购物车
            updateCart(cartEs);
        } else {
            //查询购物车内商品数量，不能超过999件
            cartCount(memberId);

            // 查询出商品信息
            GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
            CartDTO cartDTO = new CartDTO();
            setCartGoodsLabel(cartDTO, goodsDTO.getId());
            cartDTO.setId(IdGenerator.defaultSnowflakeId());
            cartDTO.setGoodsId(goodsSpecDTO.getGoodsId());
            cartDTO.setSpecWeight(goodsSpecDTO.getSpecWeight());
            cartDTO.setGoodsNum(dto.getGoodsNum());
            cartDTO.setFirstGcId(goodsDTO.getFirstGcId());
            cartDTO.setBrandId(goodsDTO.getBrandId());
            cartDTO.setSpecId(goodsSpecDTO.getId());
            cartDTO.setSpecName(goodsSpecDTO.getSpecName());
            cartDTO.setStoreId(goodsDTO.getStoreId());
            cartDTO.setStoreName(goodsDTO.getStoreName());
            cartDTO.setSpecStorage(goodsSpecDTO.getSpecStorage());
            cartDTO.setSpecSerial(goodsSpecDTO.getSpecSerial());
            cartDTO.setSpecMainPicture(goodsSpecDTO.getSpecMainPicture());
            cartDTO.setSpecInfo(goodsSpecDTO.getSpecAttrValueName());
            cartDTO.setIsSelect(CartEnum.IS_SELECT_YES.value());
            cartDTO.setStoreId(goodsDTO.getStoreId());
            cartDTO.setSecondStoreClassId(goodsDTO.getSecondStoreGoodsGcId());
            cartDTO.setMemberId(memberId);
            cartDTO.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
            cartDTO.setUpdateDate(System.currentTimeMillis());
            cartDTO.setStatus(0);
            // 校验活动数据是否合法,设置购物车选择满减活动

            cartDTO.setVirtualFlag(goodsDTO.getVirtualFlag());
            cartDTO.setDevlierType(goodsDTO.getExpressFlag());
            cartDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
            cartDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());

            // 封装到购物车秒杀活动信息
            boolean addActivityGoodsFlag = false;
            if (CollectionUtils.isNotEmpty(goodsSpecDTO.getSpecActivityList())) {
                addActivityGoodsFlag = fillActivityIfno(cartDTO, goodsSpecDTO);
            } else {
                cartDTO.setSpecSavePrice(goodsSpecDTO.getSpecSellPrice());
            }

            // 非秒杀商品：校验商品库存、获取商品默认满减活动
            if (!addActivityGoodsFlag) {
                // 校验商品库存
                if (cartDTO.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                    throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
                }
                // 校验活动数据是否合法,设置购物车选择满减活动
                this.vaildActivity(dto, cartDTO, goodsDTO.getStoreId(), goodsDTO.getId(), goodsDTO.getBrandId());
            }

            esDataUtils.saveData(ElasticSearchConstant.CART_INDEX, String.valueOf(cartDTO.getId()),
                    JSON.toJSONString(cartDTO), CartEntity.class);
        }
    }

    /**
     * 查询购物车内商品数量
     *
     * @param memberId
     */
    private void cartCount(Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        // 领取结束时间正序排序
        sortFileds.put("updateDate", "desc");

        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        // 查询已开始活动
        equalsSearchCondition.put("memberId", memberId);

        Long countData = esDataUtils.countData(pageModelDTO, ElasticSearchConstant.CART_INDEX, CartEntity.class);

        if (countData != null && countData.intValue() > CartEnum.CART_GOODS_NUM.value()) {
            throw new ServiceException(CartStatusCode.SAVE_CART_GOODS_NUM_ERROR);
        }
    }

    /**
     * 功能描述：
     * <加入购物车填充活动数据>
     *
     * @param cartDTO      加入购物车参数
     * @param goodsSpecDTO 购物车修改新增实体
     * @return boolean 加入结果
     * @date 2020/3/17 15:25
     * @author 刘远杰
     **/
    private boolean fillActivityIfno(CartDTO cartDTO, PartGoodsSpecDTO goodsSpecDTO) {
        if (CollectionUtils.isNotEmpty(goodsSpecDTO.getSpecActivityList())) {
            for (com.leimingtech.modules.dto.goods.SpecActivityDTO specActivityDTO : goodsSpecDTO.getSpecActivityList()) {
                // 获取秒杀活动,更新购物车保存实体（秒杀进行中）
                if (specActivityDTO.getActivityType().equals(ActivityTypeEnum.SECKILL_ACTIVITY.value())
                        && SeckillActivityEnum.ACTIVITY_STATE_START.value() == specActivityDTO.getActivityState()) {
                    // 超出活动限购数量
                    if (specActivityDTO.getRestrictionQuantity() != null && specActivityDTO.getRestrictionQuantity() != 0
                            && cartDTO.getGoodsNum() > specActivityDTO.getRestrictionQuantity()) {
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_ADDCART_ERROR);
                    }
                    // 校验秒杀活动商品库存
                    if (cartDTO.getGoodsNum() > specActivityDTO.getActivitySurplusStorage()) {
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }
                    // 替换商品销售价格
                    cartDTO.setSpecSellPrice(specActivityDTO.getActivityPrice());
                    if (cartDTO.getSpecSavePrice() == null) {
                        cartDTO.setSpecSavePrice(specActivityDTO.getActivityPrice());
                    }
                    // 设置购物车活动信息
                    cartDTO.setActivityId(specActivityDTO.getActivityId());
                    cartDTO.setActivityType(specActivityDTO.getActivityType());
                    cartDTO.setActivityEndDate(specActivityDTO.getActivityEndDate());
                    cartDTO.setActivitySurplusStorage(specActivityDTO.getActivitySurplusStorage());
                    cartDTO.setRestrictionQuantity(specActivityDTO.getRestrictionQuantity());
                    cartDTO.setSpecSellPrice(specActivityDTO.getActivityPrice());
                    return true;
                } else if (specActivityDTO.getActivityType().equals(ActivityTypeEnum.FLASH_SALE_ACTIVITY.value())
                        && FlashSaleActivityEnum.ACTIVITY_STATE_START.value() == specActivityDTO.getActivityState()) {
                    // 超出活动限购数量
                    if (specActivityDTO.getRestrictionQuantity() != null && specActivityDTO.getRestrictionQuantity() != 0
                            && cartDTO.getGoodsNum() > specActivityDTO.getRestrictionQuantity()) {
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_ADDCART_ERROR);
                    }
                    // 校验活动商品库存
                    if (cartDTO.getGoodsNum() > specActivityDTO.getActivitySurplusStorage()) {
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }
                    // 替换商品销售价格
                    cartDTO.setSpecSellPrice(specActivityDTO.getActivityPrice());
                    if (cartDTO.getSpecSavePrice() == null) {
                        cartDTO.setSpecSavePrice(specActivityDTO.getActivityPrice());
                    }
                    // 设置购物车活动信息
                    cartDTO.setActivityId(specActivityDTO.getActivityId());
                    cartDTO.setActivityType(specActivityDTO.getActivityType());
                    cartDTO.setActivityEndDate(specActivityDTO.getActivityEndDate());
                    cartDTO.setActivitySurplusStorage(specActivityDTO.getActivitySurplusStorage());
                    cartDTO.setRestrictionQuantity(specActivityDTO.getRestrictionQuantity());
                    cartDTO.setSpecSellPrice(specActivityDTO.getActivityPrice());
                    return true;
                }
            }
        }

        if (cartDTO.getSpecSavePrice() == null) {
            // 第一次加入且不是活动商品
            cartDTO.setSpecSavePrice(goodsSpecDTO.getSpecSellPrice());
        }
        return false;
    }

    /**
     * 修改购物车数据
     *
     * @param dto 参数实体
     */
    @Override

    public void update(@RequestBody CartDTO dto) {
        dto.setUpdateDate(System.currentTimeMillis());
        esDataUtils.updateData(ElasticSearchConstant.CART_INDEX, String.valueOf(dto.getId()), JSON.toJSONString(dto));
    }

    /**
     * 根据ID删除购物车数据
     *
     * @param ids 主键ID
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        esDataUtils.bulkDelete(ElasticSearchConstant.CART_INDEX, Arrays.asList(ids));
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 去结算服务层方法
     * @Date: 2019/6/14 16:37
     * @Version: V1.0
     */
    @Override

    public SettlementDTO settlement(@RequestParam("memberId") Long memberId) {
        // 1.查询用户购物车全部选中订单
        List<CartDTO> cartDTOList = this.findListBySelect(memberId);
        if (CollectionUtils.isEmpty(cartDTOList)) {
            // 用户没有勾选的商品 直接返回错误信息
            throw new ServiceException(CartStatusCode.SETTLEMENT_NO_SELECT_GOODS_ERROR);
        }

        // 上下架校验
        for (CartDTO cartDTO : cartDTOList) {
            if (CartEnum.CART_STATUS_UNDER.value() == cartDTO.getStatus()) {
                throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
            }
        }

        // 2.虚拟商品与实体商品同结算，请分开结算
        Integer virtualNumber = 0;
        for (CartDTO cartDTO : cartDTOList) {
            int templeFlag = 0;
            if (null == cartDTO.getVirtualFlag()) {
                templeFlag = 0;
            } else {
                templeFlag = cartDTO.getVirtualFlag();
            }
            virtualNumber = templeFlag + virtualNumber;
        }

        if (virtualNumber != 0 && virtualNumber != cartDTOList.size()) {
            throw new ServiceException(CartStatusCode.SETTLEMENT_VIRTUAL_GOODS_ERROR);
        }

        Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(10);
        List<GoodsConverOrderDTO> goodsConverOrderDTOList = Lists.newArrayList();
        Integer totalNum = this.groupByStoreId(cartDTOList, storeMap, goodsConverOrderDTOList);


        // 计算满减优惠金额
        countReduceAmount(memberId, storeMap);
        // 订单结算返回前台实体定义
        SettlementDTO settlementDTO = new SettlementDTO();

        // 优惠券计算 获得用户优惠券集合--对应使用商品--组合
        createSettlementCouponsList(memberId, storeMap, 1, null, settlementDTO);

        // 获取去结算金额
        settlementDTO.setCartToOrderList(goodsConverOrderDTOList);

        // 获取用户默认地址
        MemberAddressDTO memberAddressDTO = memberAddressService.findDefalutAddress(memberId);
        if (BeanUtil.isEmpty(memberAddressDTO)) {
            // 获取上次选择的地址
            memberAddressDTO = memberAddressService.getLastSelected(memberId);
        }
        // 计算运费金额,封装下单地址
        if (memberAddressDTO != null) {
            this.countFreightAmount(memberAddressDTO, storeMap);
            settlementDTO.setHasDefaultAddress(true);
            settlementDTO.setMemberAddress(memberAddressDTO);
        }


        BigDecimal goodsTotalFreight = BigDecimal.ZERO;
        BigDecimal totalprice = BigDecimal.ZERO;
        BigDecimal orderprice = BigDecimal.ZERO;
        BigDecimal couponsPrice = BigDecimal.ZERO;
        BigDecimal reducePrice = BigDecimal.ZERO;
        for (GoodsConverOrderDTO goodsConverOrderDTO : goodsConverOrderDTOList) {
            // 店铺结算商品
            goodsTotalFreight = goodsTotalFreight.add(goodsConverOrderDTO.getGoodsTotalFreight()).setScale(2, BigDecimal.ROUND_DOWN);
            totalprice = totalprice.add(goodsConverOrderDTO.getGoodsTotalPrice()).setScale(2, BigDecimal.ROUND_DOWN);
            orderprice = orderprice.add(goodsConverOrderDTO.getOrderPrice()).setScale(2, BigDecimal.ROUND_DOWN);
            couponsPrice = couponsPrice.add(goodsConverOrderDTO.getCouponsPrice()).setScale(2, BigDecimal.ROUND_DOWN);
            reducePrice = reducePrice.add(goodsConverOrderDTO.getReducePrice()).setScale(2, BigDecimal.ROUND_DOWN);
            if (reducePrice.compareTo(BigDecimal.ZERO) > 0) {
                goodsConverOrderDTO.setReduceFlag(ReduceActivityEnum.SELECT_FLAG_YES.value());
            }
        }
        settlementDTO.setGoodsTotalFreight(goodsTotalFreight);
        settlementDTO.setOrderPrice(totalprice);
        settlementDTO.setPayPrice(orderprice);
        //修改当前系统的优惠金额为满减金额
        settlementDTO.setActivityPrice(totalprice.subtract(orderprice).subtract(couponsPrice));
        settlementDTO.setCouponsPrice(couponsPrice);
        settlementDTO.setReducePrice(reducePrice);
        settlementDTO.setTotalNum(totalNum);
        // 判断是否是虚拟商品
        settlementDTO.setVirtualFlag(cartDTOList.get(0).getVirtualFlag());

        // 查询用户可用余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(memberId);
        settlementDTO.setAvailableBalance(memberBalanceInfoDTO.getAvailableBalance());
        settlementDTO.setPayPwdFlag(memberBalanceInfoDTO.getPayPwdFlag());
        settlementDTO.setThirdPayPrice(settlementDTO.getPayPrice());
        return settlementDTO;

    }

    /**
     * 购物车结算切换活动
     *
     * @param storeCouponsMap 店铺会员优惠券
     * @param memberId        会员id
     * @param addressId       会员地址id
     * @param useBalance      是否使用了余额
     * @return
     * @date
     * @author 刘远杰
     **/

    @Override
    public SettlementDTO recountSettlementPrice(@RequestBody Map<String, Long> storeCouponsMap,
                                                @RequestParam("memberId") Long memberId,
                                                @RequestParam(value = "addressId", required = false) Long addressId,
                                                @RequestParam(value = "useBalance", required = false) Integer useBalance) {
        // 1.查询用户购物车全部选中订单
        List<CartDTO> cartDTOList = this.findListBySelect(memberId);
        if (CollectionUtils.isEmpty(cartDTOList)) {
            // 用户没有勾选的商品 直接返回错误信息
            throw new ServiceException(CartStatusCode.SETTLEMENT_NO_SELECT_GOODS_ERROR);
        }

        // 上下架校验
        for (CartDTO cartDTO : cartDTOList) {
            if (CartEnum.CART_STATUS_UNDER.value() == cartDTO.getStatus()) {
                throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
            }
        }

        Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(10);
        List<GoodsConverOrderDTO> goodsConverOrderDTOList = Lists.newArrayList();
        Integer totalNum = groupByStoreId(cartDTOList, storeMap, goodsConverOrderDTOList);

        // 订单结算返回前台实体定义
        SettlementDTO settlementDTO = new SettlementDTO();
        // 优惠券计算 获得用户优惠券集合--对应使用商品--组合
        createSettlementCouponsList(memberId, storeMap, 0, storeCouponsMap, settlementDTO);

        // 计算满减优惠金额
        countReduceAmount(memberId, storeMap);

        // 获取去结算金额
        settlementDTO.setCartToOrderList(goodsConverOrderDTOList);

        MemberAddressDTO memberAddressDTO;
        if (addressId == null) {
            // 获取用户默认地址
            memberAddressDTO = memberAddressService.findDefalutAddress(memberId);
        } else {
            // 查询选择的地址信息
            memberAddressDTO = memberAddressService.get(addressId);
        }
        if (BeanUtil.isEmpty(memberAddressDTO)) {
            // 获取上次选择的地址
            memberAddressDTO = memberAddressService.getLastSelected(memberId);
        }

        // 计算运费金额,封装下单地址
        if (memberAddressDTO != null) {
            this.countFreightAmount(memberAddressDTO, storeMap);
            settlementDTO.setHasDefaultAddress(true);
            settlementDTO.setMemberAddress(memberAddressDTO);
        }

        // 获取去结算金额
        BigDecimal goodsTotalFreight = BigDecimal.ZERO;
        BigDecimal totalprice = BigDecimal.ZERO;
        BigDecimal orderprice = BigDecimal.ZERO;
        BigDecimal couponsPrice = BigDecimal.ZERO;
        BigDecimal reducePrice = BigDecimal.ZERO;
        for (GoodsConverOrderDTO goodsConverOrderDTO : goodsConverOrderDTOList) {
            // 店铺结算商品
            goodsTotalFreight = goodsTotalFreight.add(goodsConverOrderDTO.getGoodsTotalFreight()).setScale(2, BigDecimal.ROUND_DOWN);
            totalprice = totalprice.add(goodsConverOrderDTO.getGoodsTotalPrice());
            orderprice = orderprice.add(goodsConverOrderDTO.getOrderPrice()).setScale(2, BigDecimal.ROUND_DOWN);
            couponsPrice = couponsPrice.add(goodsConverOrderDTO.getCouponsPrice()).setScale(2, BigDecimal.ROUND_DOWN);
            reducePrice = reducePrice.add(goodsConverOrderDTO.getReducePrice()).setScale(2, BigDecimal.ROUND_DOWN);
            if (reducePrice.compareTo(BigDecimal.ZERO) > 0) {
                goodsConverOrderDTO.setReduceFlag(ReduceActivityEnum.SELECT_FLAG_YES.value());
            }
        }
        settlementDTO.setGoodsTotalFreight(goodsTotalFreight);
        settlementDTO.setOrderPrice(totalprice);
        settlementDTO.setPayPrice(orderprice);
        // 2020-07-07 修改活动金额,keyuan 修改当前系统的优惠金额为满减金额
        settlementDTO.setActivityPrice(totalprice.subtract(orderprice).subtract(couponsPrice));
        settlementDTO.setCouponsPrice(couponsPrice);
        settlementDTO.setReducePrice(reducePrice);
        settlementDTO.setTotalNum(totalNum);

        // 查询用户可用余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(memberId);
        settlementDTO.setAvailableBalance(memberBalanceInfoDTO.getAvailableBalance());
        settlementDTO.setPayPwdFlag(memberBalanceInfoDTO.getPayPwdFlag());
        settlementDTO.setThirdPayPrice(settlementDTO.getPayPrice());
        // 使用余额结算
        if (CartEnum.SETTMENT_USE_BALANCE.value() == useBalance) {
            BigDecimal difference = settlementDTO.getPayPrice().subtract(memberBalanceInfoDTO.getAvailableBalance());

            // 余额足够支付订单
            if (difference.compareTo(BigDecimal.ZERO) < 1) {
                BigDecimal number = BigDecimal.ZERO;
                settlementDTO.setThirdPayPrice(number.setScale(2, BigDecimal.ROUND_DOWN));
            } else {
                settlementDTO.setThirdPayPrice(difference);
            }
        }

        // 判断是否是虚拟商品
        settlementDTO.setVirtualFlag(cartDTOList.get(0).getVirtualFlag());

        return settlementDTO;
    }

    /**
     * 功能描述：
     * <根据店铺id拆分购物车>
     *
     * @param cartDTOList             购物车结合
     * @param storeMap                拆分后分组
     * @param goodsConverOrderDTOList 店铺购物车
     * @return 商品总数
     * @date 2020/3/20 11:57
     * @author 刘远杰
     **/
    private Integer groupByStoreId(List<CartDTO> cartDTOList, Map<Long, GoodsConverOrderDTO> storeMap, List<GoodsConverOrderDTO> goodsConverOrderDTOList) {
        Integer totalNum = 0;
        // 遍历购物车，封装去结算参数
        for (CartDTO cartDTO : cartDTOList) {
            StoreDTO storeDTO;
            GoodsConverOrderDTO goodsConverOrderDTO = new GoodsConverOrderDTO();
            if (!storeMap.containsKey(cartDTO.getStoreId())) {
                // 第一次出现店铺商品，查询该店铺信息，设置店铺名称及结算初始值
                storeDTO = storeService.get(cartDTO.getStoreId());
                if (storeDTO == null) {
                    // 店铺不存在
                    throw new ServiceException(CartStatusCode.SETTLEMENT_STORE_NOTFOUND_ERROR);
                }
                // 设置初始值
                goodsConverOrderDTO.setStoreId(cartDTO.getStoreId());
                goodsConverOrderDTO.setStoreLogo(storeDTO.getStoreLogo());
                goodsConverOrderDTO.setStoreName(storeDTO.getStoreName());
                goodsConverOrderDTO.setGoodsTotalPrice(BigDecimal.ZERO);
                goodsConverOrderDTO.setCartGoodsDTOList(new ArrayList<CartGoodsDTO>());
                goodsConverOrderDTO.setGoodsTotalFreight(BigDecimal.ZERO);
                goodsConverOrderDTO.setOrderPrice(BigDecimal.ZERO);
                goodsConverOrderDTO.setActivityPrice(BigDecimal.ZERO);
                goodsConverOrderDTO.setReducePrice(BigDecimal.ZERO);
                goodsConverOrderDTO.setCouponsPrice(BigDecimal.ZERO);
                goodsConverOrderDTO.setReduceFlag(ReduceActivityEnum.SELECT_FLAG_NO.value());
                // 地址引用，共用同一个对象，直接修改goodsConverOrderDTO对象
                storeMap.put(cartDTO.getStoreId(), goodsConverOrderDTO);
                goodsConverOrderDTOList.add(goodsConverOrderDTO);
            } else {
                // 存在该店铺，取出storeMap的店铺信息修改结算信息
                goodsConverOrderDTO = storeMap.get(cartDTO.getStoreId());
            }

            CartGoodsDTO cartGoodsDTO = new CartGoodsDTO();
            BeanCopier.create(cartDTO.getClass(), CartGoodsDTO.class, false)
                    .copy(cartDTO, cartGoodsDTO, null);

            String goodsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, cartDTO.getGoodsId().toString());
            JSONObject jsonObject = JSONObject.parseObject(goodsJson);
            if (jsonObject.get("brandId") != null && StringUtils.isNotBlank(jsonObject.get("brandId").toString())) {
                cartGoodsDTO.setBrandId(Long.parseLong(jsonObject.get("brandId").toString()));
                cartGoodsDTO.setBrandName(jsonObject.get("brandName").toString());
            }
            if (jsonObject.get("secondStoreGoodsGcId") != null && StringUtils.isNotBlank(jsonObject.get("secondStoreGoodsGcId").toString())) {
                cartGoodsDTO.setSecondStoreGoodsGcId(Long.parseLong(jsonObject.get("secondStoreGoodsGcId").toString()));
            }

            if (cartDTO.getSpecWeight() == null) {
                cartGoodsDTO.setSpecWeight(0.00);
            } else {
                cartGoodsDTO.setSpecWeight(cartDTO.getSpecWeight());
            }

            if (jsonObject.get("expressFlag") != null
                    && GoodsEnum.EXPRESS_FLAG_YES.value() == Integer.parseInt(jsonObject.get("expressFlag").toString())
                    && null != jsonObject.get("freightTemplateId")) {
                cartGoodsDTO.setFreightTemplateId(Long.parseLong(jsonObject.get("freightTemplateId").toString()));
            }
            cartGoodsDTO.setFreightStatus(0);
            // 活动商品才设置实际销售价格
            if (cartDTO.getActivityType() != ActivityTypeEnum.NO_ACTIVITY.value()) {
                cartGoodsDTO.setSpecSellPrice(cartDTO.getSpecSellPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                cartGoodsDTO.setSpecPrice(cartDTO.getSpecPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                cartGoodsDTO.setSpecPrice(cartDTO.getSpecSellPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            //设置是否可以开票信息
            if (jsonObject.get("invoiceFlag") != null
                    && null != jsonObject.get("invoiceType")
                    && null != jsonObject.get("invoiceContent")) {

                cartGoodsDTO.setInvoiceFlag(Integer.parseInt(jsonObject.get("invoiceFlag").toString()));
                cartGoodsDTO.setInvoiceType(jsonObject.get("invoiceType").toString());
                cartGoodsDTO.setInvoiceContent(jsonObject.get("invoiceContent").toString());
            }
            // 增加购物车商品、计算价格、商品数量
            goodsConverOrderDTO.getCartGoodsDTOList().add(cartGoodsDTO);
            if (cartDTO.getActivityType() != ActivityTypeEnum.NO_ACTIVITY.value()) {
                goodsConverOrderDTO.setGoodsTotalPrice(goodsConverOrderDTO.getGoodsTotalPrice().add(cartDTO.getSpecPrice().
                        multiply(new BigDecimal(cartDTO.getGoodsNum()))).setScale(2, BigDecimal.ROUND_HALF_UP));
                goodsConverOrderDTO.setOrderPrice(goodsConverOrderDTO.getOrderPrice().add(cartDTO.getSpecSellPrice().
                        multiply(new BigDecimal(cartDTO.getGoodsNum()))).setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                goodsConverOrderDTO.setOrderPrice(goodsConverOrderDTO.getOrderPrice().add(cartDTO.getSpecSellPrice().
                        multiply(new BigDecimal(cartDTO.getGoodsNum()))).setScale(2, BigDecimal.ROUND_HALF_UP));
                goodsConverOrderDTO.setGoodsTotalPrice(goodsConverOrderDTO.getGoodsTotalPrice().add(cartDTO.getSpecSellPrice().
                        multiply(new BigDecimal(cartDTO.getGoodsNum()))).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            totalNum += cartDTO.getGoodsNum();
        }
        return totalNum;
    }

    /**
     * 功能描述:
     * 〈构建结算页店铺优惠券列表数据，不选择优惠券，获取默认优惠券数据〉
     *
     * @param memberId        会员id
     * @param storeMap        店铺结算商品信息
     * @param type            是否获取默认优惠券 0否 1是
     * @param storeCouponsMap 店铺优惠券分组
     * @param settlementDTO   去结算与提交订单返回DTO
     * @author : 刘远杰
     */
    private void createSettlementCouponsList(Long memberId, Map<Long, GoodsConverOrderDTO> storeMap,
                                             int type, Map<String, Long> storeCouponsMap, SettlementDTO settlementDTO) {
        for (Long storeId : storeMap.keySet()) {
            // 店铺结算商品
            GoodsConverOrderDTO goodsConverOrderDTO = storeMap.get(storeId);

            if (goodsConverOrderDTO.getCanList() == null) {
                goodsConverOrderDTO.setCanList(new ArrayList<>());
            }

            if (goodsConverOrderDTO.getCanntList() == null) {
                goodsConverOrderDTO.setCanntList(new ArrayList<>());
            }
            // 获取所有店铺领取优惠券
            Map<String, Object> params = new HashMap<>(10);
            params.put("storeId", storeId);
            params.put("memberId", memberId);
            params.put("couponsState", 0);

            // 用户领取的该店铺、未使用优惠券集合
            List<FrontMyCouponsPageDTO> frontMyCouponsPageDTOList = couponsActivitySearchService.myCouponsList(params);

            // 优惠券按面额倒序排序
            frontMyCouponsPageDTOList.sort((FrontMyCouponsPageDTO o1, FrontMyCouponsPageDTO o2) -> o2.getFaceValue().compareTo(o1.getFaceValue()));

            // 获取每个优惠券活动下的商品
            Map<Long, CouponsActivityGoodsGroupDTO> couponsActivityGroupMap = new HashMap<>(10);
            for (FrontMyCouponsPageDTO frontMyCouponsPageDTO : frontMyCouponsPageDTOList) {
                // 设置初始活动商品分组信息
                CouponsActivityGoodsGroupDTO couponsActivityGoodsGroupDTO = new CouponsActivityGoodsGroupDTO();
                couponsActivityGoodsGroupDTO.setCartGoodsDTOList(new ArrayList<>());
                couponsActivityGoodsGroupDTO.setActivityId(frontMyCouponsPageDTO.getActivityId());
                couponsActivityGoodsGroupDTO.setStoreId(storeId);
                couponsActivityGoodsGroupDTO.setGoodsTotalPrice(BigDecimal.ZERO);
                couponsActivityGroupMap.put(frontMyCouponsPageDTO.getActivityId(), couponsActivityGoodsGroupDTO);
                // 匹配活动分组下的商品
                Integer activityGoodsScope = frontMyCouponsPageDTO.getActivityGoodsScope();

                List<CouponsGoodsDTO> relationList = frontMyCouponsPageDTO.getGoodsList();
                //遍历购物车商品
                for (CartGoodsDTO cartGoodsDTO : goodsConverOrderDTO.getCartGoodsDTOList()) {
                    //首先排除秒杀和拼团商品
                    if (cartGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()
                            && cartGoodsDTO.getActivityType() != ActivityTypeEnum.GROUP_ACTIVITY.value()) {
                        //查看限时抢购商品活动信息
                        FlashSaleActivityDTO flashSaleActivityDTO = new FlashSaleActivityDTO();
                        if (cartGoodsDTO.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()) {
                            flashSaleActivityDTO = flashSaleActivityService.get(cartGoodsDTO.getActivityId());
                            settlementDTO.setCouponsFlag(flashSaleActivityDTO.getCouponsFlag());
                            settlementDTO.setBalanceFlag(flashSaleActivityDTO.getBalanceFlag());
                            settlementDTO.setPointFlag(flashSaleActivityDTO.getPointFlag());
                            settlementDTO.setReduceFlag(flashSaleActivityDTO.getReduceFlag());
                        }
                        //支持优惠券的限时抢购商品和其他优惠券可用商品
                        if (cartGoodsDTO.getActivityType() != ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()
                                ||
                                (cartGoodsDTO.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()
                                        && flashSaleActivityDTO.getCouponsFlag() == FlashSaleActivityEnum.COUPONS_FLAG_CAN.value())) {
                            for (CouponsGoodsDTO relation : relationList) {
                                /**
                                 * 优惠券使用要求
                                 */
                                boolean allGoods = CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == activityGoodsScope;
                                //指定店铺分类
                                boolean classFlag = cartGoodsDTO.getSecondStoreGoodsGcId() != null && cartGoodsDTO.getSecondStoreGoodsGcId().equals(relation.getRelationId());
                                //指定商品分类
                                boolean goodsFlag = cartGoodsDTO.getGoodsId().equals(relation.getRelationId());
                                //指定品牌分类
                                boolean brandFlag = cartGoodsDTO.getBrandId() != null && cartGoodsDTO.getBrandId().equals(relation.getRelationId());
                                if (allGoods || classFlag || goodsFlag || brandFlag) {
                                    couponsActivityGoodsGroupDTO.getCartGoodsDTOList().add(cartGoodsDTO);
                                    BigDecimal cartTotalPrice = cartGoodsDTO.getSpecSellPrice().multiply(new BigDecimal(cartGoodsDTO.getGoodsNum()));
                                    couponsActivityGoodsGroupDTO.setGoodsTotalPrice(couponsActivityGoodsGroupDTO.getGoodsTotalPrice().add(cartTotalPrice));
                                }

                            }
                        }
                    }
                }
                // 判断优惠券活动是否可用，加入结算优惠券活动列表
                addCartCouponsList(goodsConverOrderDTO, frontMyCouponsPageDTO, couponsActivityGoodsGroupDTO.getGoodsTotalPrice());
            }
            if (goodsConverOrderDTO.getReducePrice().compareTo(BigDecimal.ZERO) == 0) {
                // 未使用满减活动
                if (CollectionUtils.isNotEmpty(goodsConverOrderDTO.getCanList())) {
                    if (type == 1) {
                        // 可用优惠券不为空，获取最优优惠券活动计算价格,设置为默认选择状态
                        goodsConverOrderDTO.getCanList().get(0).setSelectFlag(CouponsEnum.SELECT_FLAG_YES.value());
                        goodsConverOrderDTO.setCouponsPrice(goodsConverOrderDTO.getCanList().get(0).getFaceValue());
                        goodsConverOrderDTO.setOrderPrice(goodsConverOrderDTO.getOrderPrice().subtract(goodsConverOrderDTO.getCanList().get(0).getFaceValue()));
                    } else if (type == 0) {
                        if (null != storeCouponsMap) {
                            Long memberCouponsId = storeCouponsMap.get(storeId.toString());
                            if (memberCouponsId != null) {
                                for (SettlementCouponsPageDTO settlementCouponsPageDTO : goodsConverOrderDTO.getCanList()) {
                                    if (memberCouponsId.equals(settlementCouponsPageDTO.getId())) {
                                        settlementCouponsPageDTO.setSelectFlag(CouponsEnum.SELECT_FLAG_YES.value());
                                        goodsConverOrderDTO.setCouponsPrice(settlementCouponsPageDTO.getFaceValue());
                                        goodsConverOrderDTO.setOrderPrice(goodsConverOrderDTO.getOrderPrice().subtract(settlementCouponsPageDTO.getFaceValue()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 功能描述:
     * 〈计算满减活动金额〉
     *
     * @param memberId 会员id
     * @param storeMap 店铺结算商品信息
     * @author : 刘远杰
     */
    private void countReduceAmount(@RequestParam("memberId") Long memberId, Map<Long, GoodsConverOrderDTO> storeMap) {
        for (Long storeId : storeMap.keySet()) {
            // 店铺结算商品
            GoodsConverOrderDTO goodsConverOrderDTO = storeMap.get(storeId);

            // 未使用优惠券，计算满减活动
            if (goodsConverOrderDTO.getCouponsPrice().compareTo(BigDecimal.ZERO) == 0) {
                // 计算购物车选中满减是否达到使用条件
                Map<String, CouponsActivityGoodsGroupDTO> reduceActivityGroupMap = new HashMap<>(10);

                // 根据活动id分组
                // 计算每个满减活动分组商品总金额
                for (CartGoodsDTO cartGoodsDTO : goodsConverOrderDTO.getCartGoodsDTOList()) {
                    if (cartGoodsDTO.getActivityId() != null && ActivityTypeEnum.REDUCE_ACTIVITY.value() == cartGoodsDTO.getActivityType()) {
                        String activityKey = storeId + "." + cartGoodsDTO.getActivityId();
                        if (!reduceActivityGroupMap.containsKey(activityKey)) {
                            CouponsActivityGoodsGroupDTO couponsActivityGoodsGroupDTO = new CouponsActivityGoodsGroupDTO();
                            couponsActivityGoodsGroupDTO.setCartGoodsDTOList(new ArrayList<>());
                            couponsActivityGoodsGroupDTO.setActivityId(cartGoodsDTO.getActivityId());
                            couponsActivityGoodsGroupDTO.setStoreId(storeId);
                            // 购物车商品价格
                            BigDecimal cartGoodsAmount = cartGoodsDTO.getSpecSellPrice().multiply(new BigDecimal(cartGoodsDTO.getGoodsNum()));
                            couponsActivityGoodsGroupDTO.setGoodsTotalPrice(cartGoodsAmount);
                            couponsActivityGoodsGroupDTO.getCartGoodsDTOList().add(cartGoodsDTO);
                            reduceActivityGroupMap.put(activityKey, couponsActivityGoodsGroupDTO);
                        } else {
                            CouponsActivityGoodsGroupDTO couponsActivityGoodsGroupDTO = reduceActivityGroupMap.get(activityKey);
                            // 购物车商品价格
                            BigDecimal cartGoodsAmount = cartGoodsDTO.getSpecSellPrice().multiply(new BigDecimal(cartGoodsDTO.getGoodsNum()));
                            couponsActivityGoodsGroupDTO.setGoodsTotalPrice(couponsActivityGoodsGroupDTO.getGoodsTotalPrice().add(cartGoodsAmount));
                            if (CollectionUtils.isEmpty(couponsActivityGoodsGroupDTO.getCartGoodsDTOList())) {
                                couponsActivityGoodsGroupDTO.setCartGoodsDTOList(new ArrayList<>());
                            }
                            couponsActivityGoodsGroupDTO.getCartGoodsDTOList().add(cartGoodsDTO);
                        }
                    }
                }

                for (String activityKey : reduceActivityGroupMap.keySet()) {
                    // 查询满减活动详情
                    CouponsActivityGoodsGroupDTO couponsActivityGoodsGroupDTO = reduceActivityGroupMap.get(activityKey);
                    ReduceActivityIndexDTO reduceActivityIndexDTO = reduceActivitySearchService.activityDetail(couponsActivityGoodsGroupDTO.getActivityId());

                    if (reduceActivityIndexDTO == null) {
                        log.info("购物车商品去结算异常，满减活动不存在，活动id：{}", couponsActivityGoodsGroupDTO.getActivityId());
                        throw new ServiceException(CartStatusCode.ACTIVITY_NOT_FOUNT);
                    }

                    // 匹配规则
                    // 指定单条活动计算购物车金额
                    isReduceFlag(goodsConverOrderDTO, couponsActivityGoodsGroupDTO, reduceActivityIndexDTO, 1);
                }
                goodsConverOrderDTO.setOrderPrice(goodsConverOrderDTO.getOrderPrice().subtract(goodsConverOrderDTO.getReducePrice()));
            }
        }
    }

    /**
     * 功能描述:
     * 〈优惠券活动加入到结算页对应的优惠券列表〉
     *
     * @param goodsConverOrderDTO   结算页店铺结算信息
     * @param frontMyCouponsPageDTO 优惠券活动信息
     * @param goodsTotalPrice       结算的活动商品价格
     * @author : 刘远杰
     */
    private void addCartCouponsList(GoodsConverOrderDTO goodsConverOrderDTO, FrontMyCouponsPageDTO frontMyCouponsPageDTO, BigDecimal goodsTotalPrice) {
        if (CouponsEnum.COUPON_CANNT_USE.value() == frontMyCouponsPageDTO.getCouponsState()) {
            // 未到使用时间
            SettlementCouponsPageDTO settlementCouponsPageDTO = ConvertUtils.sourceToTarget(frontMyCouponsPageDTO, SettlementCouponsPageDTO.class);
            settlementCouponsPageDTO.setAddFlag(CouponsEnum.ADD_FLAG_TIME.value());
            settlementCouponsPageDTO.setSelectFlag(CouponsEnum.SELECT_FLAG_NO.value());
            goodsConverOrderDTO.getCanntList().add(settlementCouponsPageDTO);
        } else if (goodsTotalPrice.compareTo(BigDecimal.ZERO) == 0) {
            // 未选择改活动商品
            SettlementCouponsPageDTO settlementCouponsPageDTO = ConvertUtils.sourceToTarget(frontMyCouponsPageDTO, SettlementCouponsPageDTO.class);
            settlementCouponsPageDTO.setAddFlag(CouponsEnum.ADD_FLAG_SCOPE.value());
            settlementCouponsPageDTO.setSelectFlag(CouponsEnum.SELECT_FLAG_NO.value());
            goodsConverOrderDTO.getCanntList().add(settlementCouponsPageDTO);
        } else if (goodsTotalPrice.compareTo(frontMyCouponsPageDTO.getLimitAmount()) < 0) {
            // 可凑单
            SettlementCouponsPageDTO settlementCouponsPageDTO = ConvertUtils.sourceToTarget(frontMyCouponsPageDTO, SettlementCouponsPageDTO.class);
            settlementCouponsPageDTO.setAddFlag(CouponsEnum.ADD_FLAG_YES.value());
            settlementCouponsPageDTO.setSelectFlag(CouponsEnum.SELECT_FLAG_NO.value());
            goodsConverOrderDTO.getCanntList().add(settlementCouponsPageDTO);
        } else if (goodsTotalPrice.compareTo(frontMyCouponsPageDTO.getLimitAmount()) >= 0) {
            // 可使用
            SettlementCouponsPageDTO settlementCouponsPageDTO = ConvertUtils.sourceToTarget(frontMyCouponsPageDTO, SettlementCouponsPageDTO.class);
            settlementCouponsPageDTO.setSelectFlag(CouponsEnum.SELECT_FLAG_NO.value());
            goodsConverOrderDTO.getCanList().add(settlementCouponsPageDTO);
        }
    }

    /**
     * @param memberId: 用户ID
     * @Author: LX 17839193044@162.com
     * @Description: 查询用户ID下选中的购物车数据
     * @Date: 2019/6/14 17:27
     * @Version: V1.0
     */

    @Override
    public List<CartDTO> findListBySelect(@RequestParam("memberId") Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> filterSearchCondition = pageModelDTO.getEqualsFilterSearchCondition();
        filterSearchCondition.put("isSelect", CartEnum.IS_SELECT_YES.value());
        filterSearchCondition.put("memberId", memberId);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<String> resultJson = pageModelDTO.getJsonRsList();
        List<CartDTO> cartDTOList = resultJson.stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
        return cartDTOList;
    }

    /**
     * 立即购买
     *
     * @param specId:   规格ID
     * @param memberId: 用户ID
     * @param number:   规格数量
     * @return code:200成功 msg:错误信息提示 data:成功返回数据
     * @date 2019/6/27 20:47
     * @author lixiang
     **/
    @Override

    public SettlementDTO buyNow(@RequestParam(value = "memberId") Long memberId,
                                @RequestParam("specId") Long specId,
                                @RequestParam("number") int number) {
//        memberId = 1242275400738975746L;
        // 获取商品spec es
        String specJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, specId.toString());
        GoodsSpecDTO goodsSpecDTO = JSONObject.parseObject(specJson, GoodsSpecDTO.class);

        // 校验规格
        if (goodsSpecDTO == null
                || goodsSpecDTO.getSpecShow() == GoodsSpecStatusEnum.GOODS_SPEC_SHOW_DOWN.value()) {
            // 商品不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
        }

        // 校验秒杀活动库存及限购数量
        Integer activityType = ActivityTypeEnum.NO_ACTIVITY.value();
        SpecActivityDTO specActivityDTO = null;
        JSONObject specObj = JSONObject.parseObject(specJson);
        if (specObj.get(SPEC_ACTIVITY_LIST) != null) {
            List<SpecActivityDTO> specActivityList = JSONArray.parseArray(specObj.get(SPEC_ACTIVITY_LIST).toString(), SpecActivityDTO.class);
            if (CollectionUtils.isNotEmpty(specActivityList)) {
                // TODO: 2020/3/20 不考虑多活动
                //秒杀活动
                if (specActivityList.get(0).getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()
                        && SeckillActivityEnum.ACTIVITY_STATE_START.value() == specActivityList.get(0).getActivityState()) {
                    if (specActivityList.get(0).getRestrictionQuantity() != null && specActivityList.get(0).getRestrictionQuantity() != 0
                            && number > specActivityList.get(0).getRestrictionQuantity()) {
                        // 超出限购
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_RESTICTION_ERROR);
                    } else if (number > specActivityList.get(0).getActivitySurplusStorage()) {
                        // 活动库存不足
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }
                    activityType = specActivityList.get(0).getActivityType();
                    specActivityDTO = specActivityList.get(0);
                } else if (specActivityList.get(0).getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()
                        && FlashSaleActivityEnum.ACTIVITY_STATE_START.value() == specActivityList.get(0).getActivityState()) {
                    //限时抢购活动
                    if (specActivityList.get(0).getRestrictionQuantity() != null && specActivityList.get(0).getRestrictionQuantity() != 0
                            && number > specActivityList.get(0).getRestrictionQuantity()) {
                        // 超出限购
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_RESTICTION_ERROR);
                    } else if (number > specActivityList.get(0).getActivitySurplusStorage()) {
                        // 活动库存不足
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }
                    activityType = specActivityList.get(0).getActivityType();
                    specActivityDTO = specActivityList.get(0);
                }
            }
        } else {
            if (goodsSpecDTO.getSpecStorage() == 0) {
                // 商品售罄
                throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_STOREAGE_NULL_ERROR);
            }
            if (number > goodsSpecDTO.getSpecStorage()) {
                throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
            }
        }

        GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
        if (goodsDTO == null
                || goodsDTO.getGoodsStatus() != GoodsStatusEnum.GOODS_AUDIT_PASS.value()
                || goodsDTO.getGoodsShow() != GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            // 商品不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
        }
        StoreDTO storeDTO = storeService.get(goodsDTO.getStoreId());
        if (storeDTO == null) {
            // 店铺不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_STORE_NOTFOUND_ERROR);
        }

        SettlementDTO settlementDTO = new SettlementDTO();

        List<GoodsConverOrderDTO> goodsConverOrderDTOList = Lists.newArrayList();
        //计算购物车结算价格
        GoodsConverOrderDTO goodsConverOrderDTO = this.getGoodsConverOrderDTO(specId, number, goodsSpecDTO, goodsDTO, storeDTO, specActivityDTO);


        if (activityType == ActivityTypeEnum.NO_ACTIVITY.value()) {
            // 获取默认满减活动数据
            this.createReduceActivity(goodsConverOrderDTO, goodsDTO.getId());

            if (null != goodsConverOrderDTO.getReduceActivityId()) {
                goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityId(goodsConverOrderDTO.getReduceActivityId());
                goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
            }

            // 封装优惠券数据
            Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(10);
            storeMap.put(storeDTO.getId(), goodsConverOrderDTO);
            createSettlementCouponsList(memberId, storeMap, 1, null, settlementDTO);
        } else if (specActivityDTO != null && activityType.equals(ActivityTypeEnum.FLASH_SALE_ACTIVITY.value())) {
            FlashSaleActivityDTO flashSaleActivityDTO = Optional.of(flashSaleActivityService.get(specActivityDTO.getActivityId())).orElse(new FlashSaleActivityDTO());
            //设置是否可用余额、积分、优惠券、满减
            settlementDTO.setBalanceFlag(specActivityDTO.getBalanceFlag());
            settlementDTO.setPointFlag(specActivityDTO.getPointFlag());
            settlementDTO.setCouponsFlag(specActivityDTO.getCouponsFlag());
            settlementDTO.setReduceFlag(specActivityDTO.getReduceFlag());
            //满减
            if (flashSaleActivityDTO.getReduceFlag() == FlashSaleActivityEnum.REDUCE_FLAG_CAN.value()) {
                this.createReduceActivity(goodsConverOrderDTO, goodsDTO.getId());
                if (null != goodsConverOrderDTO.getReduceActivityId()) {
                    goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityId(goodsConverOrderDTO.getReduceActivityId());
                    goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                }
            }
            //优惠券
            if (flashSaleActivityDTO.getCouponsFlag() == FlashSaleActivityEnum.COUPONS_FLAG_CAN.value()) {
                // 封装优惠券数据
                Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(10);
                storeMap.put(storeDTO.getId(), goodsConverOrderDTO);
                createSettlementCouponsList(memberId, storeMap, 1, null, settlementDTO);
            }
        }
        settlementDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
        settlementDTO.setCartToOrderList(goodsConverOrderDTOList);
        goodsConverOrderDTOList.add(goodsConverOrderDTO);
        settlementDTO.setCartToOrderList(goodsConverOrderDTOList);

        // 获取用户默认地址
        MemberAddressDTO memberAddressDTO = memberAddressService.findDefalutAddress(memberId);
        if (BeanUtil.isEmpty(memberAddressDTO)) {
            // 获取上次选择的地址
            memberAddressDTO = memberAddressService.getLastSelected(memberId);
        }
        // 计算运费金额,封装下单地址
        if (memberAddressDTO != null) {
            Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(1);
            storeMap.put(goodsDTO.getStoreId(), goodsConverOrderDTO);
            this.countFreightAmount(memberAddressDTO, storeMap);
            settlementDTO.setHasDefaultAddress(true);
            settlementDTO.setMemberAddress(memberAddressDTO);
        }

        // 获取去结算金额
        settlementDTO.setGoodsTotalFreight(goodsConverOrderDTO.getGoodsTotalFreight().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setOrderPrice(goodsConverOrderDTO.getGoodsTotalPrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setPayPrice(goodsConverOrderDTO.getOrderPrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setActivityPrice(goodsConverOrderDTO.getGoodsTotalPrice().subtract(goodsConverOrderDTO.getCouponsPrice()).subtract(goodsConverOrderDTO.getOrderPrice()).setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setReducePrice(goodsConverOrderDTO.getReducePrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setCouponsPrice(goodsConverOrderDTO.getCouponsPrice().setScale(2, BigDecimal.ROUND_DOWN));
        if (settlementDTO.getReducePrice().compareTo(BigDecimal.ZERO) > 0) {
            goodsConverOrderDTO.setReduceFlag(ReduceActivityEnum.SELECT_FLAG_YES.value());
        }
        settlementDTO.setTotalNum(number);

        // 判断是否是虚拟商品
        settlementDTO.setVirtualFlag(goodsDTO.getVirtualFlag());

        // 查询用户可用余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(memberId);
        settlementDTO.setAvailableBalance(memberBalanceInfoDTO.getAvailableBalance());
        settlementDTO.setPayPwdFlag(memberBalanceInfoDTO.getPayPwdFlag());
        settlementDTO.setThirdPayPrice(settlementDTO.getPayPrice());
        return settlementDTO;

    }

    /**
     * 立即购买切换活动
     *
     * @param dto
     * @return
     * @date
     * @author 刘远杰
     **/

    @Override
    public SettlementDTO recountSettlementPrice(@RequestBody NowSettlementActivtyDTO dto) {
        // 获取商品spec es
        String specJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, dto.getSpecId().toString());
        GoodsSpecDTO goodsSpecDTO = JSONObject.parseObject(specJson, GoodsSpecDTO.class);

        // 校验规格
        if (goodsSpecDTO == null
                || goodsSpecDTO.getSpecShow() == GoodsSpecStatusEnum.GOODS_SPEC_SHOW_DOWN.value()) {
            // 商品不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
        }

        GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
        if (goodsDTO == null
                || goodsDTO.getGoodsStatus() != GoodsStatusEnum.GOODS_AUDIT_PASS.value()
                || goodsDTO.getGoodsShow() != GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            // 商品不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
        }
        StoreDTO storeDTO = storeService.get(goodsDTO.getStoreId());
        if (storeDTO == null) {
            // 店铺不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_STORE_NOTFOUND_ERROR);
        }

        // 校验秒杀活动库存及限购数量
        Integer activityType = ActivityTypeEnum.NO_ACTIVITY.value();
        SpecActivityDTO specActivityDTO = null;
        JSONObject specObj = JSONObject.parseObject(specJson);
        if (specObj.get(SPEC_ACTIVITY_LIST) != null) {
            List<SpecActivityDTO> specActivityList = JSONArray.parseArray(specObj.get(SPEC_ACTIVITY_LIST).toString(), SpecActivityDTO.class);
            if (CollectionUtils.isNotEmpty(specActivityList)) {
                // TODO: 2020/3/20 不考虑多活动
                if (specActivityList.get(0).getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()
                        && SeckillActivityEnum.ACTIVITY_STATE_START.value() == specActivityList.get(0).getActivityState()) {
                    if (specActivityList.get(0).getRestrictionQuantity() != null && specActivityList.get(0).getRestrictionQuantity() != 0
                            && dto.getNumber() > specActivityList.get(0).getRestrictionQuantity()) {
                        // 超出限购
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_RESTICTION_ERROR);
                    } else if (dto.getNumber() > specActivityList.get(0).getActivitySurplusStorage()) {
                        // 活动库存不足
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }
                    activityType = specActivityList.get(0).getActivityType();
                    specActivityDTO = specActivityList.get(0);

                } else if (specActivityList.get(0).getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()
                        && FlashSaleActivityEnum.ACTIVITY_STATE_START.value() == specActivityList.get(0).getActivityState()) {
                    //限时抢购活动
                    if (specActivityList.get(0).getRestrictionQuantity() != null && specActivityList.get(0).getRestrictionQuantity() != 0
                            && dto.getNumber() > specActivityList.get(0).getRestrictionQuantity()) {
                        // 超出限购
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_RESTICTION_ERROR);
                    } else if (dto.getNumber() > specActivityList.get(0).getActivitySurplusStorage()) {
                        // 活动库存不足
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }
                    activityType = specActivityList.get(0).getActivityType();
                    specActivityDTO = specActivityList.get(0);
                } else if (specActivityList.get(0).getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value()
                        && GroupsEnum.ACTIVITY_STATUS_ONGOING.value() == specActivityList.get(0).getActivityState()) {
                    // 拼团活动的切换地址或者优惠
                    Boolean activityFlag = null != specActivityList.get(0).getRestrictionQuantity() && specActivityList.get(0).getRestrictionQuantity() != 0
                            && dto.getNumber() > specActivityList.get(0).getRestrictionQuantity();
                    Boolean numFlag = dto.getNumber() > specActivityList.get(0).getOnceBuyLimit() && specActivityList.get(0).getOnceBuyLimit() != 0;
                    if (activityFlag || numFlag) {
                        // 超出限购
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_RESTICTION_ERROR);
                    } else if (dto.getNumber() > specActivityList.get(0).getActivitySurplusStorage()) {
                        // 活动库存不足
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }

                    if (null != dto.getGroupId()) {
                        // 查询用户的参加拼团记录
                        List<GroupRecordDTO> groupRecordDTOList = groupRecordService.getMemberRecord(dto.getMemberId(), dto.getGroupId(), goodsSpecDTO.getGoodsId());

                        if (CollectionUtils.isNotEmpty(groupRecordDTOList)) {
                            // 校验用户参加拼团活动次数
                            if ((dto.getNumber() + groupRecordDTOList.size() > specActivityList.get(0).getJoinLimit()) && specActivityList.get(0).getJoinLimit() > 0) {
                                throw new ServiceException(CartStatusCode.GROUP_EXCEED_LIMIT_ERROR);
                            }
                        }
                    }

                    specActivityDTO = specActivityList.get(0);
                }
            }
        } else {
            if (goodsSpecDTO.getSpecStorage() == 0) {
                // 商品售罄
                throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_STOREAGE_NULL_ERROR);
            }
            if (dto.getNumber() > goodsSpecDTO.getSpecStorage()) {
                throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
            }
        }

        SettlementDTO settlementDTO = new SettlementDTO();

        List<GoodsConverOrderDTO> goodsConverOrderDTOList = Lists.newArrayList();
        GoodsConverOrderDTO goodsConverOrderDTO = getGoodsConverOrderDTO(dto.getSpecId(), dto.getNumber(), goodsSpecDTO, goodsDTO, storeDTO, specActivityDTO);

        settlementDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());

        if (activityType == ActivityTypeEnum.NO_ACTIVITY.value()) {
            // 封装优惠券数据
            Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(10);
            storeMap.put(storeDTO.getId(), goodsConverOrderDTO);
            if (ActivityTypeEnum.COUPONS_ACTIVITY.value() == dto.getActivityType() && dto.getId() != null) {
                Map<String, Long> storeCouponsMap = new HashMap<>(10);
                storeCouponsMap.put(storeDTO.getId().toString(), dto.getId());
                createSettlementCouponsList(dto.getMemberId(), storeMap, 0, storeCouponsMap, settlementDTO);
            } else {
                createSettlementCouponsList(dto.getMemberId(), storeMap, 0, null, settlementDTO);
            }

            // 获取默认满减活动数据
            this.createReduceActivity(goodsConverOrderDTO, goodsSpecDTO.getGoodsId());

            if (null != goodsConverOrderDTO.getReduceActivityId()) {
                goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityId(goodsConverOrderDTO.getReduceActivityId());
                goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
            }

        } else if (specActivityDTO != null && activityType.equals(ActivityTypeEnum.FLASH_SALE_ACTIVITY.value())) {
            FlashSaleActivityDTO flashSaleActivityDTO = Optional.of(flashSaleActivityService.get(specActivityDTO.getActivityId())).orElse(new FlashSaleActivityDTO());
            //设置是否可用余额、积分、优惠券、满减
            settlementDTO.setBalanceFlag(specActivityDTO.getBalanceFlag());
            settlementDTO.setPointFlag(specActivityDTO.getPointFlag());
            settlementDTO.setCouponsFlag(specActivityDTO.getCouponsFlag());
            settlementDTO.setReduceFlag(specActivityDTO.getReduceFlag());
            //满减
            if (flashSaleActivityDTO.getReduceFlag() == FlashSaleActivityEnum.REDUCE_FLAG_CAN.value()) {
                this.createReduceActivity(goodsConverOrderDTO, goodsDTO.getId());
                if (null != goodsConverOrderDTO.getReduceActivityId()) {
                    goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityId(goodsConverOrderDTO.getReduceActivityId());
                    goodsConverOrderDTO.getCartGoodsDTOList().get(0).setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                }
            }
            //优惠券
            if (flashSaleActivityDTO.getCouponsFlag() == FlashSaleActivityEnum.COUPONS_FLAG_CAN.value() && dto.getActivityType() == 1) {
                // 封装优惠券数据
                Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(10);
                storeMap.put(storeDTO.getId(), goodsConverOrderDTO);
                this.createSettlementCouponsList(dto.getMemberId(), storeMap, 1, null, settlementDTO);
            }
        }

        goodsConverOrderDTOList.add(goodsConverOrderDTO);
        settlementDTO.setCartToOrderList(goodsConverOrderDTOList);

        MemberAddressDTO memberAddressDTO;
        if (dto.getAddressId() == null) {
            // 获取用户默认地址
            memberAddressDTO = memberAddressService.findDefalutAddress(dto.getMemberId());
            if (BeanUtil.isEmpty(memberAddressDTO)) {
                // 获取上次选择的地址
                memberAddressDTO = memberAddressService.getLastSelected(dto.getMemberId());
            }
        } else {
            // 获取已选择的地址信息
            memberAddressDTO = memberAddressService.get(dto.getAddressId());
            //修改已选择地址为上次选择
            memberAddressService.updateAddressLastSelected(dto.getAddressId(), dto.getMemberId());
        }
        // 计算运费金额,封装下单地址
        if (memberAddressDTO != null) {
            Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(1);
            storeMap.put(goodsDTO.getStoreId(), goodsConverOrderDTO);
            this.countFreightAmount(memberAddressDTO, storeMap);
            settlementDTO.setHasDefaultAddress(true);
            settlementDTO.setMemberAddress(memberAddressDTO);
        }


        // 获取去结算金额
        settlementDTO.setGoodsTotalFreight(goodsConverOrderDTO.getGoodsTotalFreight().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setOrderPrice(goodsConverOrderDTO.getGoodsTotalPrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setPayPrice(goodsConverOrderDTO.getOrderPrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setActivityPrice(goodsConverOrderDTO.getGoodsTotalPrice().subtract(goodsConverOrderDTO.getCouponsPrice()).subtract(goodsConverOrderDTO.getOrderPrice()).setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setReducePrice(goodsConverOrderDTO.getReducePrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setCouponsPrice(goodsConverOrderDTO.getCouponsPrice().setScale(2, BigDecimal.ROUND_DOWN));
        if (settlementDTO.getReducePrice().compareTo(BigDecimal.ZERO) > 0) {
            goodsConverOrderDTO.setReduceFlag(ReduceActivityEnum.SELECT_FLAG_YES.value());
        }
        settlementDTO.setTotalNum(dto.getNumber());

        // 设置虚拟商品标识
        settlementDTO.setVirtualFlag(goodsDTO.getVirtualFlag());

        // 查询用户可用余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(dto.getMemberId());
        settlementDTO.setAvailableBalance(memberBalanceInfoDTO.getAvailableBalance());
        settlementDTO.setPayPwdFlag(memberBalanceInfoDTO.getPayPwdFlag());
        settlementDTO.setThirdPayPrice(settlementDTO.getPayPrice());
        if (null != dto.getUseBalance()) {
            // 使用余额结算
            if (CartEnum.SETTMENT_USE_BALANCE.value() == dto.getUseBalance()) {
                BigDecimal balance = memberBalanceInfoDTO.getAvailableBalance();
                BigDecimal payPrice = settlementDTO.getPayPrice();
                BigDecimal difference = payPrice.subtract(balance);

                // 余额足够支付订单
                if (difference.compareTo(BigDecimal.ZERO) < 1) {
                    BigDecimal number = BigDecimal.ZERO;
                    settlementDTO.setThirdPayPrice(number.setScale(2, BigDecimal.ROUND_DOWN));
                } else {
                    settlementDTO.setThirdPayPrice(difference);
                }
            }
        }

        return settlementDTO;
    }

    /**
     * 构建购物车结算实体(秒杀活动)
     *
     * @param specId          规格id
     * @param number          数量
     * @param goodsSpecDTO    商品sku
     * @param goodsDTO        商品spu
     * @param storeDTO        店铺id
     * @param specActivityDTO 活动
     * @return
     * @date
     * @author 刘远杰
     **/
    private GoodsConverOrderDTO getGoodsConverOrderDTO(Long specId, int number, GoodsSpecDTO goodsSpecDTO, GoodsDTO goodsDTO, StoreDTO storeDTO, SpecActivityDTO specActivityDTO) {
        GoodsConverOrderDTO goodsConverOrderDTO = new GoodsConverOrderDTO();
        goodsConverOrderDTO.setCanList(new ArrayList<>());
        goodsConverOrderDTO.setCanntList(new ArrayList<>());
        // 封装 商品转化订单 实体
        goodsConverOrderDTO.setStoreId(storeDTO.getId());
        goodsConverOrderDTO.setStoreLogo(storeDTO.getStoreLogo());
        goodsConverOrderDTO.setStoreName(storeDTO.getStoreName());
        goodsConverOrderDTO.setGoodsTotalPrice(new BigDecimal(number)
                .multiply(goodsSpecDTO.getSpecSellPrice()));
        goodsConverOrderDTO.setOrderPrice(new BigDecimal(number)
                .multiply(goodsSpecDTO.getSpecSellPrice()));
        goodsConverOrderDTO.setActivityPrice(BigDecimal.ZERO);
        goodsConverOrderDTO.setReducePrice(BigDecimal.ZERO);
        goodsConverOrderDTO.setCouponsPrice(BigDecimal.ZERO);
        goodsConverOrderDTO.setReduceFlag(ReduceActivityEnum.SELECT_FLAG_NO.value());
        goodsConverOrderDTO.setGoodsTotalFreight(BigDecimal.ZERO);

        List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
        // 封装立即购买和去结算商品信息实体
        CartGoodsDTO cartGoodsDTO = new CartGoodsDTO();
        cartGoodsDTO.setSpecWeight(goodsSpecDTO.getSpecWeight() == null ? 0.00 : goodsSpecDTO.getSpecWeight());
        cartGoodsDTO.setGoodsId(goodsSpecDTO.getGoodsId());
        cartGoodsDTO.setGoodsNum(number);
        cartGoodsDTO.setSecondStoreGoodsGcId(goodsDTO.getSecondStoreGoodsGcId());
        cartGoodsDTO.setSecondStoreGoodsGcName(goodsDTO.getSecondStoreGoodsGcName());
        cartGoodsDTO.setSpecId(specId);
        cartGoodsDTO.setSpecInfo(goodsSpecDTO.getSpecAttrValueName());
        cartGoodsDTO.setSpecMainPicture(goodsSpecDTO.getSpecMainPicture());
        cartGoodsDTO.setSpecSerial(goodsSpecDTO.getSpecSerial());
        cartGoodsDTO.setSpecStorage(goodsSpecDTO.getSpecStorage());
        cartGoodsDTO.setSpecName(goodsSpecDTO.getSpecName());
        cartGoodsDTO.setBrandId(goodsDTO.getBrandId());
        cartGoodsDTO.setBrandName(goodsDTO.getBrandName());
        cartGoodsDTO.setInvoiceFlag(goodsDTO.getInvoiceFlag());
        cartGoodsDTO.setInvoiceType(goodsDTO.getInvoiceType());
        cartGoodsDTO.setInvoiceContent(goodsDTO.getInvoiceContent());
        cartGoodsDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
        cartGoodsDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
        cartGoodsDTO.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
        if (GoodsEnum.EXPRESS_FLAG_YES.value() == goodsDTO.getExpressFlag() && goodsDTO.getFreightTemplateId() != null) {
            cartGoodsDTO.setFreightTemplateId(goodsDTO.getFreightTemplateId());
        }
        cartGoodsDTO.setFreightStatus(0);
        if (specActivityDTO != null) {
            goodsConverOrderDTO.setOrderPrice(new BigDecimal(number)
                    .multiply(specActivityDTO.getActivityPrice()));
            cartGoodsDTO.setSpecSellPrice(specActivityDTO.getActivityPrice());
            cartGoodsDTO.setActivityId(specActivityDTO.getActivityId());
            cartGoodsDTO.setActivityType(specActivityDTO.getActivityType());
            cartGoodsDTO.setActivityEndDate(specActivityDTO.getActivityEndDate());
        }
        goodsConverOrderDTO.setActivityPrice(cartGoodsDTO.getSpecPrice().subtract(cartGoodsDTO.getSpecSellPrice()));

        cartGoodsDTOList.add(cartGoodsDTO);
        goodsConverOrderDTO.setCartGoodsDTOList(cartGoodsDTOList);
        return goodsConverOrderDTO;
    }

    /**
     * 立即购买封装默认满减选择
     *
     * @param goodsConverOrderDTO 购物车商品
     * @param goodsId             商品id
     * @return
     * @date
     * @author 刘远杰
     **/
    private void createReduceActivity(GoodsConverOrderDTO goodsConverOrderDTO, Long goodsId) {
        if (goodsConverOrderDTO.getCouponsPrice().compareTo(BigDecimal.ZERO) == 0) {
            // 未使用满减活动
            Map<String, Object> params = new HashMap<>(10);
            params.put("goodsId", goodsId);
            List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = reduceActivitySearchService.goodsReduceList(params);
            if (CollectionUtils.isNotEmpty(reduceActivityIndexDTOList)) {
                // 满减活动规则不为空
                for (ReduceActivityIndexDTO reduceActivityIndexDTO : reduceActivityIndexDTOList) {
                    isReduceFlag(goodsConverOrderDTO, reduceActivityIndexDTO, 0);
                }
            }
            // 计算支付金额
            goodsConverOrderDTO.setOrderPrice(goodsConverOrderDTO.getOrderPrice().subtract(goodsConverOrderDTO.getReducePrice()));
        }
    }

    /**
     * 根据<店铺购物车>和<满减活动>计算店铺购物车满减金额
     * cartType 0标识立即购买 下一优惠活动可用且优惠大于已设置优惠，用新活动替换旧活动
     * cartType 1标识购物车购买 如果活动可用，在旧活动基础上扣减活动金额
     *
     * @param goodsConverOrderDTO    购物车商品
     * @param reduceActivityIndexDTO 满减活动实体
     * @param cartType               结算类型 0立即购买 1购物车结算
     * @return
     * @date
     * @author 刘远杰
     **/
    private void isReduceFlag(GoodsConverOrderDTO goodsConverOrderDTO, ReduceActivityIndexDTO reduceActivityIndexDTO, Integer cartType) {
        if (CollectionUtils.isNotEmpty(reduceActivityIndexDTO.getRuleList())) {
            for (int i = reduceActivityIndexDTO.getRuleList().size() - 1; i >= 0; i--) {
                if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == reduceActivityIndexDTO.getRuleList().get(i).getRuleType()
                        || ReduceActivityEnum.RULE_TYPE_LADDER.value() == reduceActivityIndexDTO.getRuleList().get(i).getRuleType()) {
                    // 普通满减及阶梯满减规则金额计算
                    if (cartType == 0) {
                        // 立即购买
                        if (goodsConverOrderDTO.getGoodsTotalPrice().compareTo(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount()) >= 0
                                && reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount().compareTo(goodsConverOrderDTO.getReducePrice()) > 0) {
                            // 达到当前满减门槛并且满减金额大于上一个活动设置金额，替换原活动,获取到最优活动规则，结束方法
                            goodsConverOrderDTO.setReducePrice(reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount());
                            // 设置满减的活动id
                            goodsConverOrderDTO.setReduceActivityId(reduceActivityIndexDTO.getRuleList().get(i).getActivityId());
                            return;
                        }
                    } else if (cartType == 1) {
                        if (goodsConverOrderDTO.getGoodsTotalPrice().compareTo(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount()) >= 0) {
                            // 达到当前满减门槛,叠加满减活动优惠金额;获取到 最优活动规则，结束方法
                            goodsConverOrderDTO.setReducePrice(reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount().add(goodsConverOrderDTO.getReducePrice()));
                            return;
                        }
                    }
                } else if (ReduceActivityEnum.RULE_TYPE_AVG.value() == reduceActivityIndexDTO.getRuleList().get(i).getRuleType()) {
                    // 每满减活动
                    if (goodsConverOrderDTO.getGoodsTotalPrice().compareTo(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount()) >= 0) {
                        // 得到阶梯等级
                        BigDecimal ladder = goodsConverOrderDTO.getGoodsTotalPrice().divide(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount(), 0, BigDecimal.ROUND_DOWN);
                        BigDecimal limitAmount = reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount().multiply(ladder);
                        BigDecimal reduceAmount = reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount().multiply(ladder);
                        if (cartType == 0) {
                            if (goodsConverOrderDTO.getGoodsTotalPrice().compareTo(limitAmount) >= 0
                                    && reduceAmount.compareTo(goodsConverOrderDTO.getReducePrice()) > 0) {
                                // 达到当前满减门槛并且满减金额大于上一个活动设置金额,替换活动数据
                                goodsConverOrderDTO.setReducePrice(reduceAmount);
                                // 设置满减的活动id
                                goodsConverOrderDTO.setReduceActivityId(reduceActivityIndexDTO.getRuleList().get(i).getActivityId());
                            }

                        } else { // 设置叠加满减活动

                            goodsConverOrderDTO.setReducePrice(reduceAmount.add(goodsConverOrderDTO.getReducePrice()));
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * 根据<店铺购物车>和<满减活动>计算店铺购物车满减金额
     * cartType 0标识立即购买 下一优惠活动可用且优惠大于已设置优惠，用新活动替换旧活动
     * cartType 1标识购物车购买 如果活动可用，在旧活动基础上扣减活动金额
     *
     * @param goodsConverOrderDTO    购物车商品
     * @param reduceActivityIndexDTO 满减活动实体
     * @param cartType               结算类型 0立即购买 1购物车结算
     * @return
     * @date
     * @author 刘远杰
     **/
    private void isReduceFlag(GoodsConverOrderDTO goodsConverOrderDTO, CouponsActivityGoodsGroupDTO couponsActivityGoodsGroupDTO, ReduceActivityIndexDTO reduceActivityIndexDTO, Integer cartType) {
        if (CollectionUtils.isNotEmpty(reduceActivityIndexDTO.getRuleList())) {
            for (int i = reduceActivityIndexDTO.getRuleList().size() - 1; i >= 0; i--) {
                if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == reduceActivityIndexDTO.getRuleList().get(i).getRuleType()
                        || ReduceActivityEnum.RULE_TYPE_LADDER.value() == reduceActivityIndexDTO.getRuleList().get(i).getRuleType()) {
                    // 普通满减及阶梯满减规则金额计算
                    if (cartType == 0) {
                        // 立即购买
                        if (goodsConverOrderDTO.getGoodsTotalPrice().compareTo(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount()) >= 0
                                && reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount().compareTo(goodsConverOrderDTO.getReducePrice()) > 0) {
                            // 达到当前满减门槛并且满减金额大于上一个活动设置金额，替换原活动;获取到 最优活动规则，结束方法
                            goodsConverOrderDTO.setReducePrice(reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount());
                            return;
                        }
                    } else if (cartType == 1) {
                        if (couponsActivityGoodsGroupDTO.getGoodsTotalPrice().compareTo(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount()) >= 0) {
                            // 达到当前满减门槛,叠加满减活动优惠金额;获取到 最优活动规则，结束方法
                            goodsConverOrderDTO.setReducePrice(reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount().add(goodsConverOrderDTO.getReducePrice()));
                            return;
                        }
                    }
                } else if (ReduceActivityEnum.RULE_TYPE_AVG.value() == reduceActivityIndexDTO.getRuleList().get(i).getRuleType()) {
                    // 每满减活动
                    if (cartType == 0) {
                        // 立即购买
                        if (couponsActivityGoodsGroupDTO.getGoodsTotalPrice().compareTo(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount()) >= 0) {
                            // 得到阶梯等级
                            BigDecimal ladder = couponsActivityGoodsGroupDTO.getGoodsTotalPrice().divide(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount(), 0, BigDecimal.ROUND_DOWN);
                            BigDecimal reduceAmount = reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount().multiply(ladder);
                            // 达到当前满减门槛并且满减金额大于上一个活动设置金额,替换活动数据
                            goodsConverOrderDTO.setReducePrice(reduceAmount);
                        }
                    } else if (cartType == 1) {
                        if (couponsActivityGoodsGroupDTO.getGoodsTotalPrice().compareTo(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount()) >= 0) {
                            // 得到阶梯等级
                            BigDecimal ladder = couponsActivityGoodsGroupDTO.getGoodsTotalPrice().divide(reduceActivityIndexDTO.getRuleList().get(i).getLimitAmount(), 0, BigDecimal.ROUND_DOWN);
                            BigDecimal reduceAmount = reduceActivityIndexDTO.getRuleList().get(i).getReduceAmount().multiply(ladder);
                            // 设置叠加满减活动
                            goodsConverOrderDTO.setReducePrice(reduceAmount.add(goodsConverOrderDTO.getReducePrice()));
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * 封装购物车信息
     *
     * @param cartDTOList 购物车集合
     * @param type        0 查询全部购物车 1 查询购物车内商品正常的购物车
     * @return
     */
    @Override

    public ResultCartDTO getRedisCart(@RequestBody List<CartDTO> cartDTOList,
                                      @RequestParam(value = "type", required = false) Integer type) {

        BigDecimal totalPrice = BigDecimal.ZERO;
        ResultCartDTO resultCartDTO = new ResultCartDTO();
        resultCartDTO.setTotalPrice(BigDecimal.ZERO);
        resultCartDTO.setPayPrice(BigDecimal.ZERO);
        resultCartDTO.setDiscountAmount(BigDecimal.ZERO);

        List<FindCartDTO> findCartDTOList = new ArrayList<>();
        int cartNum = 0;

        // 按照店铺购物车分组
        Map<Long, FindCartDTO> storeCartMap = new HashMap<>(10);
        // 购物车按活动id分组
        Map<String, ActivityCartGroupDTO> activityCartGroupMap = new HashMap<>(10);
        for (CartDTO cartDTO : cartDTOList) {

            // 计算出选中的所有商品价格和是否全选
            if (cartDTO.getIsSelect() == CartEnum.IS_SELECT_YES.value()) {
                BigDecimal goodsPrice = cartDTO.getSpecSellPrice().multiply(new BigDecimal(cartDTO.getGoodsNum()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                totalPrice = totalPrice.add(goodsPrice);
                cartNum++;
            }
            // pc端查询购物车，查询购物车商品的优惠活动
            findPcActivity(cartDTO, type);

            if (!storeCartMap.containsKey(cartDTO.getStoreId())) {
                FindCartDTO findCartDTO = new FindCartDTO();
                findCartDTO.setActivityCartGroupDTOList(new ArrayList<>());
                findCartDTO.setIsSelectAll(CartEnum.IS_SELECT_NO.value());
                // 第一次循环到店铺购物车，初始化参数
                findCartDTO.setStoreId(cartDTO.getStoreId());
                findCartDTO.setStoreName(cartDTO.getStoreName());
                if (cartDTO.getStatus() == CartEnum.CART_STATUS_NORMAL.value()) {
                    findCartDTO.setIsSelectAll(cartDTO.getIsSelect());
                }

                findCartDTO.setUpdateDate(cartDTO.getUpdateDate());
                // H5查询购物车获取优惠券活动信息
                if (type == null) {
                    Map<String, Object> params = new HashMap<>(3);
                    params.put("storeId", findCartDTO.getStoreId());
                    params.put("memberId", cartDTOList.get(0).getMemberId());
                    GoodsDetailCouponsListDTO goodsDetailCouponsListDTO = couponsActivitySearchService.cartStoreCouponsList(params);

                    if (CollectionUtils.isNotEmpty(goodsDetailCouponsListDTO.getRecedList())
                            || CollectionUtils.isNotEmpty(goodsDetailCouponsListDTO.getCanRecList())) {
                        // 查询到店铺优惠券信息，设置为可领券状态
                        findCartDTO.setCouponsFlag(CartEnum.CAN_RECEIVED_COUPONS.value());
                    } else {
                        findCartDTO.setCouponsFlag(CartEnum.CANNT_RECEIVED_COUPONS.value());
                    }
                }

                // 按活动对购物车分组
                cartGroupByActivity(activityCartGroupMap, cartDTO, findCartDTO);
                storeCartMap.put(cartDTO.getStoreId(), findCartDTO);
                findCartDTOList.add(findCartDTO);
            } else {
                // 循环到店铺购物车，重新计算数据
                FindCartDTO findCartDTO = storeCartMap.get(cartDTO.getStoreId());
                if (cartDTO.getIsSelect() == CartEnum.IS_SELECT_NO.value() && cartDTO.getStatus() == CartEnum.CART_STATUS_NORMAL.value()) {
                    findCartDTO.setIsSelectAll(CartEnum.IS_SELECT_NO.value());
                }

                // 按活动对购物车分组
                cartGroupByActivity(activityCartGroupMap, cartDTO, findCartDTO);
            }
            if (ActivityTypeEnum.SECKILL_ACTIVITY.value() != cartDTO.getActivityType() && type == null) {
                // 获取满减活动信息，封装商品满减活动数量
                Map<String, Object> params = new HashMap<>(3);
                params.put("goodsId", cartDTO.getGoodsId());
                params.put("activityId", cartDTO.getActivityId());
                params.put("activityType", cartDTO.getActivityType());
                List<FrontReduceActivityPageDTO> frontReduceActivityPageDTOList = reduceActivitySearchService.goodsDetailCouponsList(params);
                //移除已选择的活动
                frontReduceActivityPageDTOList.removeIf(
                        frontReduceActivityPageDTO -> ReduceActivityEnum.SELECT_FLAG_YES.value() == frontReduceActivityPageDTO.getSelectFlag()
                );
                if (CollectionUtils.isNotEmpty(frontReduceActivityPageDTOList)) {
                    cartDTO.setActivityNum(frontReduceActivityPageDTOList.size());
                    cartDTO.setActivityDescription(frontReduceActivityPageDTOList.get(0).getActivityDescription());
                } else {
                    cartDTO.setActivityNum(0);
                    cartDTO.setActivityDescription("");
                }
            } else {
                cartDTO.setActivityNum(0);
                cartDTO.setActivityDescription("");
            }
        }
        Integer size = cartDTOList.stream().filter(c -> c.getStatus() == CartEnum.CART_STATUS_NORMAL.value()).collect(Collectors.toList()).size();
        if (size != 0 && size == cartNum) {
            resultCartDTO.setIsSelectAll(CartEnum.IS_SELECT_YES.value());
        } else {
            resultCartDTO.setIsSelectAll(CartEnum.IS_SELECT_NO.value());
        }

        resultCartDTO.setTotalPrice(totalPrice);
        resultCartDTO.setPayPrice(totalPrice);

        // 封装活动购物车分组描述信息
        createActivityCartDescription(activityCartGroupMap, resultCartDTO);

        findCartDTOList.sort(FindCartDTO::compare);
        resultCartDTO.setSelectNum(cartNum);
        resultCartDTO.setTotalNum(cartDTOList.size());
        resultCartDTO.setFindCartDTO(findCartDTOList);
        return resultCartDTO;
    }

    /**
     * pc端购物车列表把活动信息封装到购物车内
     *
     * @param cartDTO
     * @param type
     */
    private void findPcActivity(CartDTO cartDTO, Integer type) {
        List<Long> activitySet = new ArrayList<>();
        if (type != null && type == 1) {
            // 查询用户已经使用或者已经过期的优惠券
            if (cartDTO.getMemberId() != null) {
                List<MemberCouponsDTO> coupons = couponsActivityService.getDisableActivity(cartDTO.getMemberId());
                if (CollectionUtils.isNotEmpty(coupons)) {
                    Map<Long, List<MemberCouponsDTO>> collect = coupons.stream().collect(Collectors.groupingBy(c -> c.getActivityId()));
                    for (Long activityId : collect.keySet()) {
                        List<MemberCouponsDTO> memberCouponsDTOList = collect.get(activityId);
                        String dateById = esDataUtils.getDateById(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, activityId.toString());
                        CouponsActivityDTO couponsActivityDTO = JSONObject.parseObject(dateById, CouponsActivityDTO.class);
                        if (couponsActivityDTO != null && couponsActivityDTO.getPersonLimit() > 0 && couponsActivityDTO.getPersonLimit() <= memberCouponsDTOList.size()) {
                            activitySet.add(activityId);
                        }
                    }
                }
            }
            //查询可领取优惠券数量
            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            if (CollectionUtils.isNotEmpty(activitySet)) {
                Map<String, List> notInSearchCondition = pageModelDTO.getNotInSearchCondition();
                notInSearchCondition.put("id", activitySet);
            }

            Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
            equalsSearchCondition.put("activityState", CouponsEnum.ACTIVITY_STATE_START.value());
            // 1-3.查询剩余优惠券数量大于0 或者 优惠券总数为0（不限量）
            List<Map<String, Object>> orSearchConditionList = pageModelDTO.getOrSearchConditionList();
            Map<String, Object> mapCondition = new HashMap<>(10);
            mapCondition.put("totalNum", 0);
            orSearchConditionList.add(mapCondition);
            RangConditionDTO rangConditionDTO = new RangConditionDTO();
            rangConditionDTO.setBeginValue("1");
            Map<String, Object> rangConditionMap = new HashMap<>(10);
            rangConditionMap.put("surplusNum", rangConditionDTO);
            orSearchConditionList.add(rangConditionMap);

            // 优惠券类别
            Map<String, Map<String, Object>> subInFilterSearchCondition = pageModelDTO.getSubInFilterSearchCondition();
            Map<String, Object> map = new HashMap<>(10);
            map.put("activityGoodsScope", Arrays.asList(CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value(), CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value(), CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value(), CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value()));
            // 关联值
            List<Long> relationIdList = new ArrayList<>();
            relationIdList.add(cartDTO.getGoodsId());
            relationIdList.add(cartDTO.getStoreId());
            //品牌优惠券
            if (cartDTO.getBrandId() != null) {
                relationIdList.add(cartDTO.getBrandId());
            }
            if (cartDTO.getSecondStoreClassId() != null) {
                relationIdList.add(cartDTO.getSecondStoreClassId());
            }
            map.put("relationId", relationIdList);
            subInFilterSearchCondition.put("goodsList", map);
            Long countData = esDataUtils.countData(pageModelDTO, ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, null);
            if (countData != null && countData.intValue() > 0) {
                cartDTO.setCouponsFlag(1);
                return;
            }
        }
    }


    /**
     * 功能描述:
     * 〈封装活动购物车分组描述信息，修改〉
     *
     * @param activityCartGroupMap 活动购物车分组
     * @param resultCartDTO        购物车列表
     * @author : 刘远杰
     */
    private void createActivityCartDescription(Map<String, ActivityCartGroupDTO> activityCartGroupMap, ResultCartDTO resultCartDTO) {
        for (String key : activityCartGroupMap.keySet()) {
            ActivityCartGroupDTO activityCartGroupDTO = activityCartGroupMap.get(key);
            List<ActivityRuleDTO> activityRuleList = activityCartGroupDTO.getActivityRuleList();
            if (ActivityTypeEnum.REDUCE_ACTIVITY.value() == activityCartGroupDTO.getActivityType()) {
                // 满减活动，修改活动分组描述
                if (CollectionUtils.isNotEmpty(activityRuleList)) {
                    if (activityRuleList.size() == 1) {
                        // 单规则满减活动显示描述
                        ActivityRuleDTO activityRuleDTO = activityRuleList.get(0);
                        if (activityCartGroupDTO.getGoodsAmount().compareTo(BigDecimal.ZERO) == 0) {
                            // 无勾选商品
                            setNoGoodsDescription(activityCartGroupDTO, activityRuleDTO);
                        } else if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == activityRuleDTO.getRuleType() || ReduceActivityEnum.RULE_TYPE_LADDER.value() == activityRuleDTO.getRuleType()) {
                            // 普通满减活动，单规则阶梯满减
                            if (activityCartGroupDTO.getGoodsAmount().compareTo(activityRuleDTO.getLimitAmount()) >= 0) {
                                // 满足活动条件
                                setAllDescription(activityCartGroupDTO, activityRuleDTO);
                                resultCartDTO.setDiscountAmount(resultCartDTO.getDiscountAmount().add(activityRuleDTO.getReduceAmount()));
                                resultCartDTO.setPayPrice(resultCartDTO.getPayPrice().subtract(activityRuleDTO.getReduceAmount()));
                            } else {
                                // 不满足活动条件
                                setLackDescription(activityCartGroupDTO, activityRuleDTO);
                            }
                        } else if (ReduceActivityEnum.RULE_TYPE_AVG.value() == activityRuleDTO.getRuleType()) {
                            // 每满减
                            if (activityCartGroupDTO.getGoodsAmount().compareTo(activityRuleDTO.getLimitAmount()) >= 0) {
                                // 满足活动条件,计算每满减次数
                                BigDecimal avgLevel = activityCartGroupDTO.getGoodsAmount().divide(activityRuleDTO.getLimitAmount(), 0, BigDecimal.ROUND_DOWN);
                                // 满减门槛条件 = 每满减门槛条件 * 每满减次数
                                BigDecimal limitAmount = activityRuleDTO.getLimitAmount().multiply(avgLevel);
                                // 满减优惠金额 = 每满减优惠金额 * 每满减次数
                                BigDecimal reduceAmount = activityRuleDTO.getReduceAmount().multiply(avgLevel);
                                BigDecimal lackAmount = limitAmount.add(activityRuleDTO.getLimitAmount()).subtract(activityCartGroupDTO.getGoodsAmount());
                                StringBuffer activityDescription = new StringBuffer();
                                activityDescription.append("已满").append(limitAmount.stripTrailingZeros().toPlainString())
                                        .append("元已减").append(reduceAmount.stripTrailingZeros().toPlainString()).append("元，还差")
                                        .append(lackAmount.stripTrailingZeros().toPlainString()).append("元可减")
                                        .append(reduceAmount.add(activityRuleDTO.getReduceAmount()).stripTrailingZeros().toPlainString()).append("元");
                                activityCartGroupDTO.setActivityDescription(activityDescription.toString());
                                activityCartGroupDTO.setActivityOperation(0);
                                resultCartDTO.setDiscountAmount(resultCartDTO.getDiscountAmount().add(reduceAmount));
                                resultCartDTO.setPayPrice(resultCartDTO.getPayPrice().subtract(reduceAmount));
                            } else {
                                // 不满足活动条件
                                setLackDescription(activityCartGroupDTO, activityRuleDTO);
                            }
                        }
                    } else {
                        // 多规则满减活动显示描述
                        ActivityRuleDTO firstActivityRuleDTO = activityRuleList.get(0);
                        ActivityRuleDTO lastActivityRuleDTO = activityRuleList.get(activityRuleList.size() - 1);
                        if (activityCartGroupDTO.getGoodsAmount().compareTo(BigDecimal.ZERO) == 0) {
                            // 无勾选商品
                            setNoGoodsDescription(activityCartGroupDTO, firstActivityRuleDTO);
                        } else if (activityCartGroupDTO.getGoodsAmount().compareTo(firstActivityRuleDTO.getLimitAmount()) < 0) {
                            // 未满足最低满减
                            setLackDescription(activityCartGroupDTO, firstActivityRuleDTO);
                        } else if (activityCartGroupDTO.getGoodsAmount().compareTo(lastActivityRuleDTO.getLimitAmount()) >= 0) {
                            // 满足最高满减
                            setAllDescription(activityCartGroupDTO, lastActivityRuleDTO);
                            resultCartDTO.setDiscountAmount(resultCartDTO.getDiscountAmount().add(lastActivityRuleDTO.getReduceAmount()));
                            resultCartDTO.setPayPrice(resultCartDTO.getPayPrice().subtract(lastActivityRuleDTO.getReduceAmount()));
                        } else {
                            for (int i = activityRuleList.size() - 1; i >= 0; i--) {
                                if (activityCartGroupDTO.getGoodsAmount().compareTo(activityRuleList.get(i).getLimitAmount()) >= 0) {
                                    // 满足当前满减
                                    StringBuffer activityDescription = new StringBuffer();
                                    BigDecimal lackAmount = activityRuleList.get(i + 1).getLimitAmount().subtract(activityCartGroupDTO.getGoodsAmount());
                                    activityDescription.append("已满").append(activityRuleList.get(i).getLimitAmount().stripTrailingZeros().toPlainString())
                                            .append("元已减").append(activityRuleList.get(i).getReduceAmount().stripTrailingZeros().toPlainString()).append("元，还差")
                                            .append(lackAmount.stripTrailingZeros().toPlainString()).append("元可减")
                                            .append(activityRuleList.get(i + 1).getReduceAmount().stripTrailingZeros().toPlainString()).append("元");
                                    activityCartGroupDTO.setActivityDescription(activityDescription.toString());
                                    activityCartGroupDTO.setActivityOperation(1);
                                    resultCartDTO.setDiscountAmount(resultCartDTO.getDiscountAmount().add(activityRuleList.get(i).getReduceAmount()));
                                    resultCartDTO.setPayPrice(resultCartDTO.getPayPrice().subtract(activityRuleList.get(i).getReduceAmount()));
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
     * 功能描述：
     * <设置满减活动满足最优规则活动描述>
     *
     * @param activityCartGroupDTO 购物车按活动分组
     * @param lastActivityRuleDTO  活动规则
     * @return
     * @date 2020/1/3 14:12
     * @author 刘远杰
     **/
    private void setAllDescription(ActivityCartGroupDTO activityCartGroupDTO, ActivityRuleDTO lastActivityRuleDTO) {
        StringBuffer activityDescription = new StringBuffer();
        activityDescription.append("已满").append(lastActivityRuleDTO.getLimitAmount().stripTrailingZeros().toPlainString())
                .append("元已减").append(lastActivityRuleDTO.getReduceAmount().stripTrailingZeros().toPlainString()).append("元");
        activityCartGroupDTO.setActivityDescription(activityDescription.toString());
        activityCartGroupDTO.setActivityOperation(0);
    }

    /**
     * 功能描述:
     * 〈设置未勾选商品购物车活动描述〉
     *
     * @param activityCartGroupDTO 购物车按活动分组
     * @param activityRuleDTO      活动规则
     * @author : 刘远杰
     */
    private void setNoGoodsDescription(ActivityCartGroupDTO activityCartGroupDTO, ActivityRuleDTO activityRuleDTO) {
        StringBuffer activityDescription = new StringBuffer();
        activityDescription.append("满").append(activityRuleDTO.getLimitAmount().stripTrailingZeros().toPlainString())
                .append("元减").append(activityRuleDTO.getReduceAmount().stripTrailingZeros().toPlainString()).append("元");
        activityCartGroupDTO.setActivityDescription(activityDescription.toString());
        activityCartGroupDTO.setActivityOperation(3);
    }

    /**
     * 功能描述:
     * 〈设置凑单活动描述〉
     *
     * @param activityCartGroupDTO 购物车按活动分组
     * @param activityRuleDTO      活动规则
     * @author : 刘远杰
     */
    private void setLackDescription(ActivityCartGroupDTO activityCartGroupDTO, ActivityRuleDTO activityRuleDTO) {
        StringBuffer activityDescription = new StringBuffer();
        BigDecimal lackAmount = activityRuleDTO.getLimitAmount().subtract(activityCartGroupDTO.getGoodsAmount());
        activityDescription.append("满").append(activityRuleDTO.getLimitAmount().stripTrailingZeros().toPlainString())
                .append("元减").append(activityRuleDTO.getReduceAmount().stripTrailingZeros().toPlainString()).append("元，还差")
                .append(lackAmount.stripTrailingZeros().toPlainString()).append("元");
        activityCartGroupDTO.setActivityDescription(activityDescription.toString());
        activityCartGroupDTO.setActivityOperation(2);
    }

    /**
     * 功能描述:
     * 〈按活动对购物车分组〉
     *
     * @param activityCartGroupMap 活动分组map
     * @param cartDTO              当前购物车数据
     * @param findCartDTO          当前店铺购物车
     * @author : 刘远杰
     */
    private void cartGroupByActivity(Map<String, ActivityCartGroupDTO> activityCartGroupMap, CartDTO cartDTO, FindCartDTO findCartDTO) {
        String activityKey = cartDTO.getStoreId() + "." + cartDTO.getActivityId() + "." + cartDTO.getActivityType();
        if (!activityCartGroupMap.containsKey(activityKey)) {
            // 第一次循环到该活动下的购物车，初始化参数
            ActivityCartGroupDTO activityCartGroupDTO = new ActivityCartGroupDTO();
            activityCartGroupDTO.setCartDTOList(new ArrayList<>());
            activityCartGroupDTO.setActivityRuleList(new ArrayList<>());
            activityCartGroupDTO.setStoreId(cartDTO.getStoreId());
            activityCartGroupDTO.setUpdateDate(cartDTO.getUpdateDate());
            activityCartGroupDTO.setActivityId(cartDTO.getActivityId());
            activityCartGroupDTO.setActivityType(cartDTO.getActivityType());
            if (ActivityTypeEnum.REDUCE_ACTIVITY.value() == cartDTO.getActivityType()) {
                // 获取满减活动规则
                ReduceActivityIndexDTO reduceActivityIndexDTO = reduceActivitySearchService.activityDetail(activityCartGroupDTO.getActivityId());
                if (reduceActivityIndexDTO != null) {
                    List<ActivityRuleDTO> activityRuleDTOList = ConvertUtils.sourceToTarget(reduceActivityIndexDTO.getRuleList(), ActivityRuleDTO.class);
                    activityCartGroupDTO.setActivityRuleList(activityRuleDTOList);
                }
            }
            if (cartDTO.getIsSelect() == 1) {
                activityCartGroupDTO.setGoodsAmount(cartDTO.getSpecSellPrice().multiply(new BigDecimal(cartDTO.getGoodsNum())));
            } else {
                activityCartGroupDTO.setGoodsAmount(BigDecimal.ZERO);
            }

            activityCartGroupDTO.getCartDTOList().add(cartDTO);

            // 放入map
            activityCartGroupMap.put(activityKey, activityCartGroupDTO);
            // 放入店铺分组购物车中
            if (ActivityTypeEnum.NO_ACTIVITY.value() == cartDTO.getActivityType()) {
                // 无活动置顶
                findCartDTO.getActivityCartGroupDTOList().add(0, activityCartGroupDTO);
            } else {
                findCartDTO.getActivityCartGroupDTOList().add(activityCartGroupDTO);
            }
        } else {
            ActivityCartGroupDTO activityCartGroupDTO = activityCartGroupMap.get(activityKey);
            // 活动商品价格相加
            if (cartDTO.getIsSelect() == 1) {
                activityCartGroupDTO.setGoodsAmount(cartDTO.getSpecSellPrice().multiply(new BigDecimal(cartDTO.getGoodsNum()))
                        .add(activityCartGroupDTO.getGoodsAmount()));
            }

            activityCartGroupDTO.getCartDTOList().add(cartDTO);
        }
    }

    /**
     * 获取选中购物车的价钱
     *
     * @return
     */
    @Override

    public BigDecimal findAllSelect(@RequestParam("memberId") Long memberId) {

        // 封装PageModelDTO 查询对象
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("memberId", memberId);
        equalsSearchCondition.put("isSelect", CartEnum.IS_SELECT_YES.value());

        // 查询用户已勾选的购物车信息
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<String> resultJson = pageModelDTO.getJsonRsList();

        // 对结果进行解析
        DoubleSummaryStatistics summaryStatistics = resultJson.stream().collect(Collectors.summarizingDouble(p -> {
            CartDTO cartDTO = JSONObject.parseObject(p, CartDTO.class);
            // 单种商品价格总和=商品总数*商品总价格
            return new BigDecimal(cartDTO.getGoodsNum()).multiply(cartDTO.getSpecSellPrice()).doubleValue();
        }));
        return BigDecimal.valueOf(summaryStatistics.getSum());
    }

    /**
     * 同步购物车
     *
     * @param cartKey:  用户未登录情况下购物车key
     * @param memberId: 用户ID
     */
    @Override

    public void synchCart(@RequestParam("cartKey") String cartKey,
                          @RequestParam("memberId") Long memberId) {

        // 从Redis中获取用户未登录下购物车数据
        Map<String, Object> map = redisUtils.hGetAll(cartKey);

        List<CartDTO> cartDTOList = new ArrayList(map.values());

        List<CartDTO> updateList = Lists.newArrayList();
        List<CartDTO> saveList = Lists.newArrayList();

        // 循环购物车信息进行同步
        cartDTOList.forEach(cartDTO -> {
            // 根据用户ID 和商品规格ID去查询当前用户购物车中是否有相同的商品
            cartDTO.setMemberId(memberId);

            // 根据用户ID和规格ID查询已经登陆的用户是否存在同种商品  存在及合并，不存在则保存
            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
            equalsSearchCondition.put("memberId", memberId);
            equalsSearchCondition.put("specId", cartDTO.getSpecId());
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);

            if (CollectionUtils.isNotEmpty(pageModelDTO.getJsonRsList())) {
                // 存在相同数据
                // 取出第一条也是唯一一条数据
                CartDTO cartLoginDTO = JSONObject.parseObject(pageModelDTO.getJsonRsList().get(0), CartDTO.class);
                // 判断总的数量是否大于库存数量,如果大于库存就设为库存的最大值
                if (cartDTO.getGoodsNum() + cartLoginDTO.getGoodsNum() > CartEnum.MAX_CART_NUM.value()) {
                    //如果大于就设为最大值
                    cartLoginDTO.setGoodsNum(CartEnum.MAX_CART_NUM.value());
                } else {
                    cartLoginDTO.setGoodsNum(cartDTO.getGoodsNum() + cartLoginDTO.getGoodsNum());
                }
                if (cartLoginDTO.getStatus() == CartEnum.CART_STATUS_NORMAL.value()) {
                    cartLoginDTO.setIsSelect(CartEnum.IS_SELECT_YES.value());
                } else {
                    cartLoginDTO.setIsSelect(CartEnum.IS_SELECT_NO.value());
                }

                updateList.add(cartLoginDTO);
            } else {
                // 没有相同数据就直接插入
                cartDTO.setIsSelect(CartEnum.IS_SELECT_YES.value());
                saveList.add(cartDTO);
            }
        });
        if (CollectionUtils.isNotEmpty(saveList)) {
            esDataUtils.saveDataBatch(ElasticSearchConstant.CART_INDEX, "id",
                    JSON.toJSONString(saveList), CartEntity.class);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            esDataUtils.updateDataBatch(ElasticSearchConstant.CART_INDEX, "id",
                    JSON.toJSONString(updateList));
        }
    }

    /**
     * @param goodsConverOrderList: 用户选中购物车集合
     * @Author: LX 17839193044@162.com
     * @Description: 对购物车商品进行规格、库存、价格校验。后期会增加活动等校验
     * @Date: 2019/6/20 14:25
     * @Version: V1.0
     */
    @Override

    public Map<String, List<CartGoodsDTO>> cartValidation(@RequestBody List<GoodsConverOrderDTO> goodsConverOrderList) {
        Map<String, List<CartGoodsDTO>> map = new HashMap<>(10);
        //商品下架集合
        List<CartGoodsDTO> goodsShowList = Lists.newArrayList();
        //商品规格异常集合
        List<CartGoodsDTO> specList = Lists.newArrayList();
        //库存不足商品集合
        List<CartGoodsDTO> stockList = Lists.newArrayList();
        //价格变动商品集合
        List<CartGoodsDTO> priceList = Lists.newArrayList();
        // 满减活动不存在集合
        List<CartGoodsDTO> reduceNotExistList = Lists.newArrayList();
        // 满减活动未开始集合
        List<CartGoodsDTO> reducePreList = Lists.newArrayList();
        // 满减活动已结束集合
        List<CartGoodsDTO> reduceStopList = Lists.newArrayList();

        //TODO lixiang 后期需要增加店铺优惠、赠品等校验
        for (GoodsConverOrderDTO goodsConverOrderDTO : goodsConverOrderList) {
            for (CartGoodsDTO cartGoodsDTO : goodsConverOrderDTO.getCartGoodsDTOList()) {

                // 查询商品信息
                ValidateGoodsDTO validateGoodsDTO = goodsService.findValidationGoods(cartGoodsDTO.getGoodsId(), cartGoodsDTO.getSpecId());
                if (validateGoodsDTO != null
                        && validateGoodsDTO.getGoodsStatus().equals(GoodsStatusEnum.GOODS_AUDIT_PASS.value())
                        && validateGoodsDTO.getGoodsShow().equals(GoodsStatusEnum.GOODS_SHOW_ON.value())
                        && validateGoodsDTO.getSpecShow().equals(GoodsSpecStatusEnum.GOODS_SPEC_SHOW_UP.value())) {

                    // 设置运费模板id
                    if (GoodsEnum.EXPRESS_FLAG_YES.value() == validateGoodsDTO.getExpressFlag() && validateGoodsDTO.getFreightTemplateId() != null) {
                        cartGoodsDTO.setFreightTemplateId(validateGoodsDTO.getFreightTemplateId());
                    }

                    if (cartGoodsDTO.getGoodsNum() > validateGoodsDTO.getSpecStorage()) {
                        // 规格库存不足,更新购物车购买数量
                        stockList.add(cartGoodsDTO);
                    }

                    if (cartGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()
                            && cartGoodsDTO.getSpecSellPrice().compareTo(validateGoodsDTO.getSpecSellPrice()) != 0) {
                        // 商品价格变动，更新购物车销售价格
                        cartGoodsDTO.setSpecSellPrice(validateGoodsDTO.getSpecSellPrice());
                        priceList.add(cartGoodsDTO);
                    }
                } else {
                    // 规格不存在
                    if (ActivityTypeEnum.SECKILL_ACTIVITY.value() != cartGoodsDTO.getActivityType() && cartGoodsDTO.getSpecSellPrice().compareTo(validateGoodsDTO.getSpecSellPrice()) != 0) {
                        // 商品价格变动，更新购物车销售价格
                        cartGoodsDTO.setSpecSellPrice(validateGoodsDTO.getSpecSellPrice());
                        priceList.add(cartGoodsDTO);
                    } else {
                        // 规格不存在
                        specList.add(cartGoodsDTO);
                    }
                }

                // 满减活动校验
                if (null != cartGoodsDTO.getActivityId() && ActivityTypeEnum.REDUCE_ACTIVITY.value() == cartGoodsDTO.getActivityType()) {
                    ReduceActivityDTO reduceActivityDTO = reduceActivityService.get(cartGoodsDTO.getActivityId());
                    if (null == reduceActivityDTO) {
                        reduceNotExistList.add(cartGoodsDTO);
                    } else if (reduceActivityDTO.getActivityState().equals(ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value())) {
                        reducePreList.add(cartGoodsDTO);
                    } else if (reduceActivityDTO.getActivityState().equals(ReduceActivityEnum.ACTIVITY_STATE_END.value())) {
                        reduceStopList.add(cartGoodsDTO);
                    }
                }
            }
        }

        // 错误信息封装
        map.put("understock", stockList);
        map.put("pricechange", priceList);
        map.put("specnotfund", specList);
        map.put("goodsshow", goodsShowList);
        map.put("reducenotexist", reduceNotExistList);
        map.put("reducepre", reducePreList);
        map.put("reducestop", reduceStopList);
        return map;
    }

    /**
     * 删除用户选中的购物车
     *
     * @param memberId 用户ID
     */

    @Override
    public void deleteIsSelectCart(@RequestParam("memberId") Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("memberId", memberId);
        equalsSearchCondition.put("isSelect", CartEnum.IS_SELECT_YES.value());
        esDataUtils.deleteByQuery(pageModelDTO, ElasticSearchConstant.CART_INDEX);
    }

    /**
     * @param cartDTOList: 用户选中的购物车集合
     * @Author: LX 17839193044@162.com
     * @Description: 根据店铺拆分购物车（方便后期增加店铺活动校验等操作）
     * @Date: 2019/6/20 14:31
     * @Version: V1.0
     */
    @Override

    public List<GoodsConverOrderDTO> findVoListByCartIds(@RequestBody List<CartDTO> cartDTOList) {
        List<GoodsConverOrderDTO> goodsConverOrderDTOList = Lists.newArrayList();

        Map<Long, String> storeMap = new HashMap<>(10);
        for (CartDTO cart : cartDTOList) {
            if (cart != null) {
                storeMap.put(cart.getStoreId(), cart.getStoreName());
            }
        }

        //循环店铺ID 封装店铺ID下所有购物车商品数据
        for (Long storeId : storeMap.keySet()) {

            //封装购物车转化为订单对象
            GoodsConverOrderDTO goodsConverOrderDTO = new GoodsConverOrderDTO();
            // 封装店铺信息
            goodsConverOrderDTO.setStoreId(storeId);
            goodsConverOrderDTO.setStoreName(storeMap.get(storeId));

            // 店铺下商品总价格
            BigDecimal goodsTotalPrice = BigDecimal.ZERO;
            List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();

            // 循环购物车数据
            for (CartDTO cartDTO : cartDTOList) {
                if (storeId.equals(cartDTO.getStoreId())) {
                    CartGoodsDTO cartGoodsDTO = new CartGoodsDTO();
                    BeanCopier.create(CartDTO.class, CartGoodsDTO.class, false)
                            .copy(cartDTO, cartGoodsDTO, null);
                    cartGoodsDTOList.add(cartGoodsDTO);
                    goodsTotalPrice = goodsTotalPrice.add(cartDTO.getSpecSellPrice().
                            multiply(new BigDecimal(cartDTO.getGoodsNum())));
                }
            }

            //TODO LX 17839193044@162.com 后期需要查询店铺下所有的优惠信息

            goodsConverOrderDTO.setCartGoodsDTOList(cartGoodsDTOList);
            goodsConverOrderDTO.setGoodsTotalPrice(goodsTotalPrice);
            goodsConverOrderDTOList.add(goodsConverOrderDTO);
        }

        return goodsConverOrderDTOList;
    }

    /**
     * 查询购物车中是否有相同的商品
     *
     * @param memberId 用户ID
     * @param specId   规格ID
     * @return
     */
    @Override

    public CartDTO findCartGoodsNum(@RequestParam("specId") Long specId, @RequestParam("memberId") Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsFilterSearchCondition = pageModelDTO.getEqualsFilterSearchCondition();
        equalsFilterSearchCondition.put("specId", specId);
        equalsFilterSearchCondition.put("memberId", memberId);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        if (CollectionUtils.isNotEmpty(pageModelDTO.getJsonRsList())) {
            return JSONObject.parseObject(pageModelDTO.getJsonRsList().get(0), CartDTO.class);
        }
        return null;
    }

    /**
     * 全选或者取消全选
     *
     * @param memberId 用户ID
     * @param storeId  店铺ID
     * @param state    选中状态
     */
    @Override

    public void updateIsSelect(@RequestParam("memberId") Long memberId,
                               @RequestParam(value = "storeId", required = false) Long storeId,
                               @RequestParam("state") Integer state) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> condition = pageModelDTO.getEqualsSearchCondition();
        condition.put("storeId", storeId);
        condition.put("memberId", memberId);
        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        inSearchCondition.put("status", Arrays.asList(CartEnum.CART_STATUS_NORMAL.value()));
        Map<String, Object> map = new HashMap<>(10);
        map.put("isSelect", state);
        esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSON.toJSONString(map));
    }


    /**
     * 修改购物车商品状态
     *
     * @param updateCartStatusDTO 参数
     */

    @Override
    public void updateCartStatus(@RequestBody List<UpdateCartStatusDTO> updateCartStatusDTO) {
        updateCartStatusDTO.stream().forEach(p -> {
            GoodsSpecDTO goodsSpecDTO = new GoodsSpecDTO();
            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            Map<String, Object> condition = pageModelDTO.getEqualsSearchCondition();
            if (p.getGoodsId() != null) {
                condition.put("goodsId", p.getGoodsId());
            }
            if (p.getSpecId() != null) {
                goodsSpecDTO = goodsSpecService.get(p.getSpecId());
                // 查询活动价格
                BigDecimal activityPrice = getActivityPrice(p.getSpecId());
                if (goodsSpecDTO != null) {
                    if (p.getSpecSellPrice() == null) {
                        p.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
                    }

                    if (activityPrice != null) {
                        p.setSpecSellPrice(activityPrice);
                    }

                    if (p.getSpecStorage() == null) {
                        p.setSpecStorage(goodsSpecDTO.getSpecStorage());
                    }

                    if (p.getStatus() == null) {
                        p.setStatus(goodsSpecDTO.getSpecShow());
                    }
                }
                condition.put("specId", p.getSpecId());
            }
            Map<String, Object> map = new HashMap<>(10);
            if (p.getStatus() != null && p.getStatus() == GoodsStatusEnum.GOODS_SHOW_DOWN.value()) {
                map.put("status", CartEnum.CART_STATUS_UNDER.value());
                map.put("isSelect", CartEnum.IS_SELECT_NO.value());
            }
            if (p.getStatus() != null && p.getStatus() == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
                map.put("status", CartEnum.CART_STATUS_NORMAL.value());
                map.put("specSellPrice", p.getSpecSellPrice());
                map.put("specStorage", p.getSpecStorage());
            }

            if (p.getStatus() != null && p.getStatus() == CartEnum.CART_STATUS_DEL.value()) {
                map.put("status", p.getStatus());
                map.put("isSelect", CartEnum.IS_SELECT_NO.value());
            }

            if (p.getActivitySurplusStorage() != null) {
                map.put("activitySurplusStorage", p.getActivitySurplusStorage());
            }

            if (p.getActivitySurplusStorage() != null && p.getActivitySurplusStorage() == 0) {
                map.put("status", CartEnum.CART_STATUS_STOCK.value());
                map.put("isSelect", CartEnum.IS_SELECT_NO.value());
            }
            if (p.getSpecStorage() != null && p.getSpecStorage() == 0) {
                map.put("status", CartEnum.CART_STATUS_STOCK.value());
                map.put("isSelect", CartEnum.IS_SELECT_NO.value());
            }

            if (StringUtils.isNotBlank(p.getUpdateName())) {
                map.put("updater", p.getUpdateName());
            }
            //查询商品是否降价,如果降价给用户发送通知
            goodsReduceRemind(p);

            esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSON.toJSONString(map));
        });
    }

    private BigDecimal getActivityPrice(Long specId) {
        String specEsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, specId.toString());
        JSONObject specEs = JSONObject.parseObject(specEsJson);
        if (specEs != null && specEs.get("specActivityList") != null) {
            List<SpecActivityDTO> specActivityList = JSONArray.parseArray(specEs.get("specActivityList").toString(), SpecActivityDTO.class);
            if (CollectionUtils.isNotEmpty(specActivityList)) {
                for (SpecActivityDTO specActivityDTO : specActivityList) {
                    if (specActivityDTO.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()
                            && specActivityDTO.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()
                            && specActivityDTO.getActivityState() == 1) {
                        return specActivityDTO.getActivityPrice();
                    }
                }
            }
        }
        return null;
    }

    /***
     *    //查询商品是否降价,如果降价给用户发送通知
     * @param updateCartStatusDTO
     */
    private void goodsReduceRemind(UpdateCartStatusDTO updateCartStatusDTO) {

        if (updateCartStatusDTO.getStatus() == null || updateCartStatusDTO.getStatus() != 1) {
            return;
        }
        updateCartStatusDTO.setSpecSellPrice(updateCartStatusDTO.getSpecSellPrice());
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("specId", updateCartStatusDTO.getSpecId());
        pageModelDTO.setCollapseField("memberId");
        String[] strings = new String[3];
        strings[0] = "specSellPrice";
        strings[1] = "memberId";
        strings[2] = "specName";
        pageModelDTO.setFetchSourceIncludes(strings);

        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<String> jsonRsList = pageModelDTO.getJsonRsList();
        List<CartDTO> collect = jsonRsList.stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return;
        }
        BigDecimal specSellPrice = collect.get(0).getSpecSellPrice();
        if (specSellPrice.compareTo(updateCartStatusDTO.getSpecSellPrice()) == 1) {
            Map<String, Object> map = new HashMap<>(16);
            String specName = collect.get(0).getSpecName();
            //获取用户信息Map
            List<Long> memberList = collect.stream().map(c -> c.getMemberId()).collect(Collectors.toList());
            List<MemberUmengTokenInfo> tokenInfos = memberService.selectUmengTokenByIds(memberList);
            Map<Long, String> memberMap = new HashMap(10);
            tokenInfos.stream().forEach(memberUmengTokenInfo -> {
                memberMap.put(memberUmengTokenInfo.getId(), memberUmengTokenInfo.getMemberName());
            });
            map.put("memberMap", memberMap);

            //站内信、短信参数
            Map<String, String> paramMap = new HashMap<>(16);
            paramMap.put("goodsName", specName);
            paramMap.put("goodsId", String.valueOf(collect.get(0).getGoodsId()));
            paramMap.put("specId", String.valueOf(collect.get(0).getSpecId()));
            map.put("paramMap", paramMap);
            // TODO: 2020/4/24/024  xuzhch  手机号待处理
            map.put("mobile", "");
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setSendName(updateCartStatusDTO.getUpdateName());
            messageDTO.setMessageCode(MessageCodeEnum.CART_GOODS_REDUCE_REMIND.value());
            messageDTO.setParamJson(JSON.toJSONString(map));
            messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
            sysMessageService.save(messageDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
        }
    }

    /**
     * 获取购物车中选中的商品id
     *
     * @param memberId
     * @return
     */
    @Override

    public List<FavoriteCartDTO> findGoodsIdsByMemberId(@RequestParam("memberId") Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("memberId", memberId);
        equalsSearchCondition.put("isSelect", CartEnum.IS_SELECT_YES.value());
        pageModelDTO.getNoEqualsSearchConditioin().put("status", CartEnum.CART_STATUS_UNDER.value());
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        return pageModelDTO.getJsonRsList().stream().map(p -> JSONObject.parseObject(p, FavoriteCartDTO.class)).collect(Collectors.toList());
    }

    /**
     * 查询用户购物车内商品数量
     *
     * @param memberId 用户ID
     * @return
     */

    @Override
    public Integer findCartNum(@RequestParam("memberId") Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("memberId", memberId);
        Long result = esDataUtils.countData(pageModelDTO, ElasticSearchConstant.CART_INDEX, CartEntity.class);
        return result.intValue();
    }

    /**
     * 更新购物车商品
     *
     * @param dto 参数
     */

    @Override
    public void updateCartGoods(@RequestBody SaveCartDTO dto) {
        GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(dto.getSpecId());
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(dto.getCartId());
        cartDTO.setSpecId(dto.getSpecId());
        cartDTO.setGoodsNum(dto.getGoodsNum());
        cartDTO.setSpecInfo(goodsSpecDTO.getSpecAttrValueName());
        cartDTO.setSpecMainPicture(goodsSpecDTO.getSpecMainPicture());
        cartDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
        cartDTO.setSpecSavePrice(goodsSpecDTO.getSpecSellPrice());
        cartDTO.setSpecName(goodsSpecDTO.getSpecName());
        cartDTO.setSpecSerial(goodsSpecDTO.getSpecSerial());
        cartDTO.setSpecStorage(goodsSpecDTO.getSpecStorage());
        esDataUtils.updateData(ElasticSearchConstant.CART_INDEX, String.valueOf(dto.getCartId()), JSON.toJSONString(cartDTO));
    }


    /**
     * 更新购物车，不更新更新时间
     *
     * @param cartDTO
     */

    @Override
    public void updateCart(@RequestBody CartDTO cartDTO) {
        cartDTO.setUpdateDate(null);
        esDataUtils.updateData(ElasticSearchConstant.CART_INDEX, String.valueOf(cartDTO.getId()), JSON.toJSONString(cartDTO));
    }


    /**
     * 根据店铺ID修改购物车商品信息
     *
     * @param storeId
     */

    @Override
    public void updateCartGoodsStatusByStoreId(@RequestParam("storeId") String storeId) {
        StoreDTO storeDTO = storeService.get(Long.valueOf(storeId));
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        pageModelDTO.getEqualsSearchCondition().put("storeId", storeId);
        Map<String, Object> map = new HashMap<>(10);
        if (storeDTO != null && storeDTO.getIsEnable() == 1) {
            map.put("status", CartEnum.CART_STATUS_UNDER.value());
        }
        if (storeDTO != null && StringUtils.isNotBlank(storeDTO.getStoreName())) {
            map.put("storeName", storeDTO.getStoreName());
        }

        esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSON.toJSONString(map));
    }

    /**
     * 活动结束删除购物车活动信息
     *
     * @param activityIds  活动id
     * @param activityType 活动类型
     * @return
     * @date
     * @author 刘远杰
     **/

    @Override
    public void deleteCartActivity(@RequestBody List<Long> activityIds, @RequestParam("activityType") Integer activityType) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        pageModelDTO.getEqualsSearchCondition().put("activityType", activityType);
        pageModelDTO.getInSearchCondition().put("activityId", activityIds);
        Map<String, Object> map = new HashMap<>(10);
        map.put("activityId", null);
        map.put("activityType", ActivityTypeEnum.NO_ACTIVITY.value());
        esDataUtils.updateByQueryDate(pageModelDTO, ElasticSearchConstant.CART_INDEX, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
    }


    /**
     * 功能描述:
     * 〈加入购物车校验活动信息是否合法，合法设置满减活动，如未选择满减活动，则选择最优满减活动保存购物车〉
     *
     * @param dto     加入购物车实体
     * @param cartDTO 购物车
     * @param storeId 店铺id
     * @param goodsId 商品spu id
     * @param brandId 品牌id
     * @author : 刘远杰
     */
    private void vaildActivity(SaveCartDTO dto, CartDTO cartDTO, Long storeId, Long goodsId, Long brandId) {
        if (null == dto.getCartId() && ActivityTypeEnum.NO_ACTIVITY.value() == cartDTO.getActivityType()) {
            // 未选择满减活动，购物车列表操作，不修改活动信息，其他页面加购，商品未选择优惠活动，获取最优活动
            ReduceActivityDTO goodsBestActivity = reduceActivityService.getGoodsBestActivity(cartDTO.getGoodsId(), cartDTO.getStoreId(), cartDTO.getBrandId(), cartDTO.getSecondStoreClassId());
            if (null != goodsBestActivity && null != goodsBestActivity.getId()) {
                cartDTO.setActivityId(goodsBestActivity.getId());
                cartDTO.setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
            }
        } else if (null != dto.getActivityId() && ActivityTypeEnum.REDUCE_ACTIVITY.value() == dto.getActivityType()) {
            // 加入购物车用户已选择满减活动，校验活动数据，通过则购物车设置该活动
            cartDTO.setActivityId(dto.getActivityId());
            cartDTO.setActivityType(dto.getActivityType());
            // 满减活动,获取活动数据、活动规则
            ReduceActivityDTO reduceActivityDTO = reduceActivityService.get(dto.getActivityId());
            List<ReduceGoodsDTO> reduceGoodsDTOList;
            if (reduceActivityDTO == null) {
                // 未查询到活动，输出错误信息
                throw new ServiceException(CartStatusCode.ACTIVITY_NOT_FOUNT);
            } else if (ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value() == reduceActivityDTO.getActivityState()) {
                // 活动未开始，输出错误信息
                throw new ServiceException(CartStatusCode.ACTIVITY_NOT_START);
            } else if (ReduceActivityEnum.ACTIVITY_STATE_END.value() == reduceActivityDTO.getActivityState()) {
                // 活动已结束，输出错误信息
                throw new ServiceException(CartStatusCode.ACTIVITY_HAS_END);
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == reduceActivityDTO.getActivityGoodsScope()) {
                // 店铺活动商品，判断商品店铺id是否正确
                if (!reduceActivityDTO.getStoreId().equals(storeId)) {
                    // 商品不是活动店铺下的商品，输出错误信息
                    throw new ServiceException(CartStatusCode.GOODS_NOT_JOINED_CHOICE_ACTIVITY);
                }
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == reduceActivityDTO.getActivityGoodsScope()) {
                // 指定店铺分类
                reduceGoodsDTOList = reduceGoodsService.getByActivityId(dto.getActivityId());
                boolean result = reduceGoodsDTOList.stream().anyMatch(p -> p.getRelationId().equals(cartDTO.getSecondStoreClassId()));
                if (!result) {
                    throw new ServiceException(CartStatusCode.GOODS_NOT_JOINED_CHOICE_ACTIVITY);
                }

            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == reduceActivityDTO.getActivityGoodsScope()) {
                reduceGoodsDTOList = reduceGoodsService.getByActivityId(dto.getActivityId());
                // 指定商品活动商品
                boolean result = reduceGoodsDTOList.stream().anyMatch(p -> p.getRelationId().equals(goodsId));
                if (!result) {
                    // 商品不是活动选择的商品，输出错误信息
                    throw new ServiceException(CartStatusCode.GOODS_NOT_JOINED_CHOICE_ACTIVITY);
                }
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == reduceActivityDTO.getActivityGoodsScope()) {
                reduceGoodsDTOList = reduceGoodsService.getByActivityId(dto.getActivityId());
                // 指定商品活动商品
                boolean result = reduceGoodsDTOList.stream().anyMatch(p -> p.getRelationId().equals(brandId));
                if (!result) {
                    // 商品品牌不是活动选择的商品品牌，输出错误信息
                    throw new ServiceException(CartStatusCode.GOODS_NOT_JOINED_CHOICE_ACTIVITY);
                }
            } else {
                // TODO: 2019/12/27 不支持店铺分类满减
                throw new ServiceException(CartStatusCode.ACTIVITY_MESSAGE_ERROR);
            }
        }
    }

    /**
     * 未登录加入购物车
     *
     * @param dto
     * @param redisKey
     */

    @Override
    @Lock4j(keys = "#redisKey")
    public void savaRedisCart(@RequestBody SaveCartDTO dto, @RequestParam("redisKey") String redisKey) {
        String specEsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, dto.getSpecId().toString());
        PartGoodsSpecDTO goodsSpecDTO = JSONObject.parseObject(specEsJson, PartGoodsSpecDTO.class);
        int check = check(dto, goodsSpecDTO);
        if (check != CartEnum.SUCCESS_CODE.value()) {
            throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
        }
        // 验证redis中有没有相同的数据
        CartDTO cartDTO = (CartDTO) redisUtils.hGet(redisKey, String.valueOf(dto.getSpecId()));
        if (cartDTO == null) {
            // 如果等于null，就新增
            GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
            cartDTO = new CartDTO();
            setCartGoodsLabel(cartDTO, goodsDTO.getId());
            cartDTO.setGoodsId(goodsSpecDTO.getGoodsId());
            cartDTO.setGoodsNum(dto.getGoodsNum());
            cartDTO.setFirstGcId(goodsDTO.getFirstGcId());
            cartDTO.setBrandId(goodsDTO.getBrandId());
            cartDTO.setSpecId(goodsSpecDTO.getId());
            cartDTO.setSpecName(goodsSpecDTO.getSpecName());
            cartDTO.setStoreId(goodsDTO.getStoreId());
            cartDTO.setStoreName(goodsDTO.getStoreName());
            cartDTO.setSpecStorage(goodsSpecDTO.getSpecStorage());
            cartDTO.setSpecSerial(goodsSpecDTO.getSpecSerial());
            cartDTO.setSpecMainPicture(goodsSpecDTO.getSpecMainPicture());
            cartDTO.setSpecInfo(goodsSpecDTO.getSpecAttrValueName());
            cartDTO.setIsSelect(CartEnum.IS_SELECT_YES.value());
            cartDTO.setStoreId(goodsDTO.getStoreId());
            cartDTO.setUpdateDate(System.currentTimeMillis());
            cartDTO.setId(IdGenerator.defaultSnowflakeId());
            cartDTO.setSpecWeight(goodsSpecDTO.getSpecWeight());
            cartDTO.setStatus(0);
            cartDTO.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
            cartDTO.setSecondStoreClassId(goodsDTO.getSecondStoreGoodsGcId());
            cartDTO.setVirtualFlag(goodsDTO.getVirtualFlag());
            cartDTO.setDevlierType(goodsDTO.getExpressFlag());
            cartDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
            cartDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
            cartDTO.setSecondStoreClassId(goodsDTO.getSecondStoreGoodsGcId());

            // 封装到购物车秒杀活动信息
            boolean addActivityGoodsFlag = false;
            if (CollectionUtils.isNotEmpty(goodsSpecDTO.getSpecActivityList())) {
                addActivityGoodsFlag = fillActivityIfno(cartDTO, goodsSpecDTO);
            } else {
                cartDTO.setSpecSavePrice(goodsSpecDTO.getSpecSellPrice());
            }

            if (!addActivityGoodsFlag) {
                // 校验商品库存
                if (cartDTO.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                    throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
                }
                // 校验活动数据是否合法
                vaildActivity(dto, cartDTO, goodsDTO.getStoreId(), goodsDTO.getId(), goodsDTO.getBrandId());
            }

            redisUtils.hSet(redisKey, String.valueOf(dto.getSpecId()), cartDTO, RedisUtils.HOUR_TWO_EXPIRE);
        } else {
            //有数据
            if (dto.getCartId() == null) {
                cartDTO.setGoodsNum(cartDTO.getGoodsNum() + dto.getGoodsNum());
                cartDTO.setUpdateDate(System.currentTimeMillis());
            } else {
                // 存在ID 修改数量调用
                if (dto.getCartId().equals(cartDTO.getId())) {
                    cartDTO.setGoodsNum(dto.getGoodsNum());
                }
            }

            cartDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
            cartDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());

            // 封装到购物车秒杀活动信息
            boolean addActivityGoodsFlag = false;
            if (CollectionUtils.isNotEmpty(goodsSpecDTO.getSpecActivityList())) {
                addActivityGoodsFlag = fillActivityIfno(cartDTO, goodsSpecDTO);
            }

            if (!addActivityGoodsFlag) {
                // 校验商品库存
                if (cartDTO.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                    throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
                }
                // 校验活动数据是否合法
                vaildActivity(dto, cartDTO, cartDTO.getStoreId(), cartDTO.getGoodsId(), cartDTO.getBrandId());
            }

            redisUtils.hSet(redisKey, String.valueOf(dto.getSpecId()), cartDTO, RedisUtils.HOUR_TWO_EXPIRE);
        }
    }

    private void setCartGoodsLabel(CartDTO cartDTO, Long goodsId) {
        // 查询商品标签和店铺类型
        String goodsLab = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, goodsId.toString());
        if (StringUtils.isBlank(goodsLab)) {
            return;
        }
        GoodsLabDTO labels = JSONObject.parseObject(goodsLab, GoodsLabDTO.class);
        if (labels != null) {
            cartDTO.setStoreType(labels.getStoreType());
            if (CollectionUtils.isNotEmpty(labels.getGoodsLabels())) {
                cartDTO.setLabelId(labels.getGoodsLabels().get(0) != null ? labels.getGoodsLabels().get(0).getLabelId() : null);
                cartDTO.setLabelName(labels.getGoodsLabels().get(0) != null ? labels.getGoodsLabels().get(0).getLabelName() : null);
            }
        }
    }

    /**
     * 未登录购物车切换规格
     *
     * @param dto
     * @param redisKey
     */

    @Override
    public void updateRedisCartSpec(@RequestBody SaveCartDTO dto, @RequestParam("redisKey") String redisKey) {
        PartGoodsSpecDTO goodsSpecDTO = goodsSpecService.findGoodsSpecById(dto.getSpecId());
        int check = check(dto, goodsSpecDTO);

        // 验证redis中有没有相同的数据
        CartDTO cartDTO = (CartDTO) redisUtils.hGet(redisKey, String.valueOf(dto.getSpecId()));
        if (cartDTO != null) {
            // 如果有数据就更新
            if (!dto.getCartId().equals(cartDTO.getId())) {
                if (cartDTO.getGoodsNum() + dto.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                    cartDTO.setGoodsNum(1);
                } else {
                    cartDTO.setGoodsNum(cartDTO.getGoodsNum() + dto.getGoodsNum());
                }
                cartDTO.setUpdateDate(System.currentTimeMillis());
                // 封装到购物车秒杀活动信息
                cartDTO.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
                updateCartActivity(dto, goodsSpecDTO, cartDTO);
                redisUtils.hSet(redisKey, String.valueOf(cartDTO.getSpecId()), cartDTO);
                // 删除上一个购物车
                Map<String, Object> map = redisUtils.hGetAll(redisKey);
                List<CartDTO> cartDTOList = new ArrayList(map.values());
                Long specId = null;
                for (CartDTO cart : cartDTOList) {
                    if (cart.getId().equals(dto.getCartId())) {
                        specId = cart.getSpecId();
                        break;
                    }
                }
                redisUtils.hDel(redisKey, String.valueOf(specId));
            }
        } else {
            Map<String, Object> map = redisUtils.hGetAll(redisKey);
            List<CartDTO> cartDTOList = new ArrayList(map.values());
            for (CartDTO carts : cartDTOList) {
                if (carts.getId().equals(dto.getCartId())) {
                    GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
                    redisUtils.hDel(redisKey, String.valueOf(carts.getSpecId()));
                    carts.setSpecId(dto.getSpecId());
                    carts.setSpecInfo(goodsSpecDTO.getSpecAttrValueName());
                    carts.setSpecMainPicture(goodsSpecDTO.getSpecMainPicture());
                    carts.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
                    carts.setSpecName(goodsSpecDTO.getSpecName());
                    carts.setSpecSerial(goodsSpecDTO.getSpecSerial());
                    carts.setSpecWeight(goodsSpecDTO.getSpecWeight());
                    carts.setSpecStorage(goodsSpecDTO.getSpecStorage());
                    carts.setBrandId(goodsDTO.getBrandId());
                    carts.setStoreId(goodsDTO.getStoreId());
                    carts.setGoodsId(goodsSpecDTO.getGoodsId());
                    setCartGoodsLabel(carts, goodsSpecDTO.getGoodsId());
                    if (check != Integer.parseInt(CartStatusCode.CART_STORAGE_IS_NOT.getCode())) {
                        carts.setGoodsNum(dto.getGoodsNum());
                    } else {
                        carts.setGoodsNum(1);
                    }

                    // 封装到购物车秒杀活动信息
                    carts.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
                    updateCartActivity(dto, goodsSpecDTO, carts);
                    if (carts.getSpecSavePrice() == null) {
                        carts.setSpecSavePrice(goodsSpecDTO.getSpecSellPrice());
                    }

                    redisUtils.hSet(redisKey, String.valueOf(dto.getSpecId()), carts, RedisUtils.HOUR_TWO_EXPIRE);
                    break;
                }
            }
        }
    }

    /**
     * 已登录切换规格
     *
     * @param dto
     * @param memberId
     */

    @Override
    public void updateEsCartSpec(@RequestBody SaveCartDTO dto, @RequestParam("memberId") Long memberId) {
        String specEsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, dto.getSpecId().toString());
        PartGoodsSpecDTO goodsSpecDTO = JSONObject.parseObject(specEsJson, PartGoodsSpecDTO.class);

        int check = check(dto, goodsSpecDTO);

        // 查询购物车中是否有当前规格商品
        CartDTO cart = findCartGoodsNum(dto.getSpecId(), memberId);

        // 修改购物车规格
        if (cart != null) {

            // 获取购物车中得商品数量家当当前的数量是否大于200
            if (cart.getGoodsNum() + dto.getGoodsNum() > CartEnum.MAX_CART_NUM.value()) {
                throw new ServiceException(CartStatusCode.CART_NUM_ERROR.getCode());
            }
            if (!dto.getCartId().equals(cart.getId())) {
                if (cart.getGoodsNum() + dto.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                    cart.setGoodsNum(1);
                } else {
                    cart.setGoodsNum(cart.getGoodsNum() + dto.getGoodsNum());
                }
                // 封装到购物车秒杀活动信息
                cart.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
                updateCartActivity(dto, goodsSpecDTO, cart);
                update(cart);
                delete(new Long[]{dto.getCartId()});
            } else {
                if (check == Integer.parseInt(CartStatusCode.CART_STORAGE_IS_NOT.getCode())) {
                    dto.setGoodsNum(1);
                }
                cart.setGoodsNum(dto.getGoodsNum());
                // 封装到购物车秒杀活动信息
                cart.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
                updateCartActivity(dto, goodsSpecDTO, cart);
                updateCart(cart);
            }
        } else {
            GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
            CartDTO cartDTO = new CartDTO();
            setCartGoodsLabel(cartDTO, goodsSpecDTO.getGoodsId());
            cartDTO.setId(dto.getCartId());
            cartDTO.setSpecId(dto.getSpecId());
            cartDTO.setGoodsNum(dto.getGoodsNum());
            cartDTO.setSpecInfo(goodsSpecDTO.getSpecAttrValueName());
            cartDTO.setSpecMainPicture(goodsSpecDTO.getSpecMainPicture());
            cartDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
            cartDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
            cartDTO.setSpecName(goodsSpecDTO.getSpecName());
            cartDTO.setSpecWeight(goodsSpecDTO.getSpecWeight());
            cartDTO.setSpecSerial(goodsSpecDTO.getSpecSerial());
            cartDTO.setSpecStorage(goodsSpecDTO.getSpecStorage());
            cartDTO.setBrandId(goodsDTO.getBrandId());
            cartDTO.setStoreId(goodsDTO.getStoreId());
            cartDTO.setGoodsId(goodsSpecDTO.getGoodsId());
            if (check == Integer.parseInt(CartStatusCode.CART_STORAGE_IS_NOT.getCode())) {
                cartDTO.setGoodsNum(1);
            }

            cartDTO.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
            updateCartActivity(dto, goodsSpecDTO, cartDTO);
            if (cartDTO.getSpecSavePrice() == null) {
                cartDTO.setSpecSavePrice(goodsSpecDTO.getSpecSellPrice());
            }
            esDataUtils.updateData(ElasticSearchConstant.CART_INDEX, String.valueOf(dto.getCartId()), JSON.toJSONString(cartDTO));

        }
    }

    /**
     * 功能描述：
     * <更新购物侧活动数据校验封装>
     *
     * @param dto
     * @param goodsSpecDTO
     * @param cartDTO
     * @return
     * @date 2020/4/7 17:07
     * @author 刘远杰
     **/
    private void updateCartActivity(@RequestBody SaveCartDTO dto, PartGoodsSpecDTO goodsSpecDTO, CartDTO cartDTO) {
        // 封装到购物车秒杀活动信息
        boolean addActivityGoodsFlag = false;
        if (CollectionUtils.isNotEmpty(goodsSpecDTO.getSpecActivityList())) {
            addActivityGoodsFlag = fillActivityIfno(cartDTO, goodsSpecDTO);
        }

        if (!addActivityGoodsFlag) {
            // 校验商品库存
            if (cartDTO.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                throw new ServiceException(CartStatusCode.CART_STORAGE_IS_NOT);
            }
            // 校验活动数据是否合法,设置购物车选择满减活动
            // 未选择满减活动，购物车列表操作，不修改活动信息，其他页面加购，商品未选择优惠活动，获取最优活动
            ReduceActivityDTO goodsBestActivity = reduceActivityService.getGoodsBestActivity(cartDTO.getGoodsId(), cartDTO.getStoreId(), cartDTO.getBrandId(), cartDTO.getSecondStoreClassId());
            if (goodsBestActivity != null && goodsBestActivity.getId() != null) {
                cartDTO.setActivityId(goodsBestActivity.getId());
                cartDTO.setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
            }
        }
    }

    /**
     * 购物车列表
     *
     * @param memberId
     * @param type     0 查询全部购物车 1 查询购物车内商品正常的购物车
     * @return
     */
    @Override

    public ResultCartDTO getCartList(@RequestParam("memberId") Long memberId,
                                     @RequestParam(value = "type", required = false) Integer type) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        // 根据更新时间倒序
        sortFileds.put("updateDate", "desc");
        Map<String, Object> filterSearchCondition = pageModelDTO.getEqualsFilterSearchCondition();
        // 查询已开始活动
        filterSearchCondition.put("memberId", memberId);
        // H5 查询所有购物车， pc  正常购物车和失效购物车分别查询
        if (type != null && type == 1) {
            Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
            inSearchCondition.put("status", Arrays.asList(CartEnum.CART_STATUS_NORMAL.value()));
        }
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<CartDTO> cartDTOList = pageModelDTO.getJsonRsList().stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
        return this.getRedisCart(cartDTOList, type);
    }

    /**
     * 校验数据
     */
    private int check(SaveCartDTO dto, PartGoodsSpecDTO goodsSpecDTO) {
        int status = CartEnum.SUCCESS_CODE.value();

        // 加入购物车前验证
        // 判断是否有当前商品
        if (goodsSpecDTO == null) {
            throw new ServiceException(CartStatusCode.GOODS_SPEC_NOT_FOUND);
        }

        //判断当前规格是否下架
        if (goodsSpecDTO.getSpecShow() != GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            throw new ServiceException(CartStatusCode.GOODS_NOT_FOUND);
        }

        if (dto.getGoodsNum() > CartEnum.MAX_CART_NUM.value()) {
            throw new ServiceException(CartStatusCode.CART_NUM_ERROR);
        }
        return status;
    }

    /**
     * 发起拼团
     *
     * @param memberId  会员id
     * @param specId    商品规格id
     * @param number    购买数量
     * @param groupId   拼团活动id
     * @param addressId 会员地址
     * @return
     * @date 2020-03-20 11:38
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public SettlementDTO groupBuyNow(@RequestParam("memberId") Long memberId,
                                     @RequestParam("specId") Long specId,
                                     @RequestParam("number") int number,
                                     @RequestParam("groupId") Long groupId,
                                     @RequestParam(value = "addressId", required = false) Long addressId) {
        //1.查询拼团活动是否存在 2.拼团活动是否进行中
        GroupDTO groupDTO = groupService.get(groupId);
        if (groupDTO == null || groupDTO.getActivityStatus() != GroupsEnum.ACTIVITY_STATUS_ONGOING.value()) {
            // 拼团活动不存在活拼团活动不是进行中
            throw new ServiceException(CartStatusCode.SETTLEMENT_GROUP_NOTFOUND_ERROR);
        }

        // 获取商品spec es
        String specJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, specId.toString());
        GoodsSpecDTO goodsSpecDTO = JSONObject.parseObject(specJson, GoodsSpecDTO.class);

        // 校验规格
        if (goodsSpecDTO == null
                || goodsSpecDTO.getSpecShow() == GoodsSpecStatusEnum.GOODS_SPEC_SHOW_DOWN.value()) {
            // 商品不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
        }

        // 校验拼团活动库存及限购数量
        SpecActivityDTO specActivityDTO = null;
        JSONObject specObj = JSONObject.parseObject(specJson);
        if (specObj.get(SPEC_ACTIVITY_LIST) != null) {
            List<SpecActivityDTO> specActivityList = JSONArray.parseArray(specObj.get(SPEC_ACTIVITY_LIST).toString(), SpecActivityDTO.class);
            if (CollectionUtils.isNotEmpty(specActivityList)) {

                if (specActivityList.get(0).getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value()
                        && GroupsEnum.ACTIVITY_STATUS_ONGOING.value() == specActivityList.get(0).getActivityState()) {
                    Boolean activityFlag = null != specActivityList.get(0).getRestrictionQuantity() && specActivityList.get(0).getRestrictionQuantity() != 0
                            && number > specActivityList.get(0).getRestrictionQuantity();
                    Boolean numFlag = number > specActivityList.get(0).getOnceBuyLimit() && specActivityList.get(0).getOnceBuyLimit() != 0;
                    if (activityFlag || numFlag) {
                        // 超出限购
                        throw new ServiceException(CartStatusCode.OVER_ACTIVITY_RESTICTION_ERROR);
                    } else if (number > specActivityList.get(0).getActivitySurplusStorage()) {
                        // 活动库存不足
                        throw new ServiceException(CartStatusCode.ACTIVITY_STORAGE_LACK_ERROR);
                    }

                    // 查询用户的参加拼团记录
                    List<GroupRecordDTO> groupRecordDTOList = groupRecordService.getMemberRecord(memberId, groupId, goodsSpecDTO.getGoodsId());

                    if (CollectionUtils.isNotEmpty(groupRecordDTOList)) {
                        // 校验用户参加拼团活动次数
                        if ((number + groupRecordDTOList.size() > specActivityList.get(0).getJoinLimit()) && specActivityList.get(0).getJoinLimit() > 0) {
                            throw new ServiceException(CartStatusCode.GROUP_EXCEED_LIMIT_ERROR);
                        }
                    }
                    specActivityDTO = specActivityList.get(0);
                }
            }
        }


        GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
        if (goodsDTO == null
                || goodsDTO.getGoodsStatus() != GoodsStatusEnum.GOODS_AUDIT_PASS.value()
                || goodsDTO.getGoodsShow() != GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            // 商品不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
        }

        StoreDTO storeDTO = storeService.get(goodsDTO.getStoreId());
        if (storeDTO == null) {
            // 店铺不存在
            throw new ServiceException(CartStatusCode.SETTLEMENT_STORE_NOTFOUND_ERROR);
        }

        SettlementDTO settlementDTO = new SettlementDTO();

        List<GoodsConverOrderDTO> goodsConverOrderDTOList = Lists.newArrayList();

        GoodsConverOrderDTO goodsConverOrderDTO = getGoodsConverOrderDTO(specId, number, goodsSpecDTO, goodsDTO, storeDTO, specActivityDTO);
        goodsConverOrderDTOList.add(goodsConverOrderDTO);

        settlementDTO.setSpecSellPrice(goodsSpecDTO.getSpecSellPrice());
        settlementDTO.setCartToOrderList(goodsConverOrderDTOList);

        // 获取去结算金额
        settlementDTO.setCartToOrderList(goodsConverOrderDTOList);

        MemberAddressDTO memberAddressDTO;
        if (memberId != null) {
            // 获取用户默认地址
            memberAddressDTO = memberAddressService.findDefalutAddress(memberId);
        } else {
            // 获取用户选择的地址
            memberAddressDTO = memberAddressService.get(addressId);
        }
        // 计算运费金额,封装下单地址
        if (memberAddressDTO != null) {
            Map<Long, GoodsConverOrderDTO> storeMap = new HashMap<>(1);
            storeMap.put(goodsDTO.getStoreId(), goodsConverOrderDTO);
            this.countFreightAmount(memberAddressDTO, storeMap);
            settlementDTO.setHasDefaultAddress(true);
            settlementDTO.setMemberAddress(memberAddressDTO);
        }

        settlementDTO.setGoodsTotalFreight(goodsConverOrderDTO.getGoodsTotalFreight().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setOrderPrice(goodsConverOrderDTO.getGoodsTotalPrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setPayPrice(goodsConverOrderDTO.getOrderPrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setActivityPrice(goodsConverOrderDTO.getGoodsTotalPrice().subtract(goodsConverOrderDTO.getOrderPrice()).setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setReducePrice(goodsConverOrderDTO.getReducePrice().setScale(2, BigDecimal.ROUND_DOWN));
        settlementDTO.setCouponsPrice(goodsConverOrderDTO.getCouponsPrice().setScale(2, BigDecimal.ROUND_DOWN));
        if (settlementDTO.getReducePrice().compareTo(BigDecimal.ZERO) > 0) {
            goodsConverOrderDTO.setReduceFlag(ReduceActivityEnum.SELECT_FLAG_YES.value());
        }
        settlementDTO.setTotalNum(number);
        // 判断是否是虚拟商品
        settlementDTO.setVirtualFlag(goodsDTO.getVirtualFlag());

        // 查询用户可用余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(memberId);
        settlementDTO.setAvailableBalance(memberBalanceInfoDTO.getAvailableBalance());
        settlementDTO.setPayPwdFlag(memberBalanceInfoDTO.getPayPwdFlag());
        settlementDTO.setThirdPayPrice(settlementDTO.getPayPrice());
        return settlementDTO;

    }

    /**
     * 用户购物车商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */
    @Override

    public List<CartCmsDTO> getCartGoodIds(@RequestParam("memberId") Long memberId) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        // 领取结束时间正序排序
        sortFileds.put("updateDate", "desc");
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("memberId", memberId);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<String> resultJson = pageModelDTO.getJsonRsList();
        List<CartCmsDTO> cartDTOList = resultJson.stream().map(p -> JSONObject.parseObject(p, CartCmsDTO.class)).collect(Collectors.toList());
        return cartDTOList;

    }

    /**
     * 功能描述：
     * <运费计算>
     *
     * @param memberAddressDTO 会员地址
     * @param storeMap         结算实体
     * @return
     * @date 2020/5/7 14:39
     * @author 刘远杰
     **/
    private void countFreightAmount(MemberAddressDTO memberAddressDTO, Map<Long, GoodsConverOrderDTO> storeMap) {
        for (Long storeId : storeMap.keySet()) {
            List<BigDecimal> freightAmountList = new ArrayList<>();
            GoodsConverOrderDTO goodsConverOrderDTO = storeMap.get(storeId);

            // 运费计算规则
            FreightRuleDTO storeFreightRule = freightRuleService.getStoreFreightRule(storeId);
            Integer ruleType = storeFreightRule.getRuleType();

            // 根据运费模板id分组
            Map<Long, List<CartGoodsDTO>> freightMap = goodsConverOrderDTO.getCartGoodsDTOList().stream().filter(o -> o.getFreightTemplateId() != null).collect(Collectors.groupingBy(CartGoodsDTO::getFreightTemplateId));

            if (storeFreightRule == null) {
                throw new ServiceException(CartStatusCode.FREIGHT_TEMPLATE_RULE_ERROR);
            }

            for (Long freightTemplateId : freightMap.keySet()) {
                // 查询运费模板
                AdminFreightTemplateDetailDTO detailDTO = freightTemplateService.adminDetail(freightTemplateId, storeId);
                if (detailDTO == null) {
                    // 运费模板不存在
                    throw new ServiceException(CartStatusCode.FREIGHT_TEMPLATE_NOTFOUND_ERROR);
                }

                // 累加商品数量
                Integer number = freightMap.get(freightTemplateId).stream().mapToInt(CartGoodsDTO::getGoodsNum).sum();


                // 匹配下单的地区
                List<FreightTemplateRuleDTO> ruleList = detailDTO.getFreightTemplateRuleList();
                BigDecimal goodsTotalFreight = BigDecimal.ZERO;

                //运费规则中是否有所有地区的规则，在叠加运费的时候如果所有地区的运费和单项运费有冲突就取最少的运费叠加上
                BigDecimal superposition = BigDecimal.ZERO;
                Boolean moreRule = false;

                // 按件计算
                // 该区域是否可配送标识
                boolean flag = true;
                for (int i = ruleList.size() - 1; i >= 0; i--) {
                    FreightTemplateRuleDTO freightTemplateRuleDTO = ruleList.get(i);
                    if (freightTemplateRuleDTO.getAreaIdsArr().contains(0L)) {

                        // 计算某规则运费金额
                        goodsTotalFreight = simplePieceFreightAmount(number, freightTemplateRuleDTO, detailDTO, freightTemplateId, freightMap);
                        freightAmountList.add(goodsTotalFreight);
                        superposition = goodsTotalFreight;
                        flag = false;
                        break;
                    } else {
                        Boolean a = memberAddressDTO.getProvinceId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getProvinceId());
                        Boolean b = memberAddressDTO.getCityId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getCityId());
                        Boolean c = memberAddressDTO.getAreaId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getAreaId());
                        if (a || b || c) {
                            // 计算某规则运费金额
                            goodsTotalFreight = simplePieceFreightAmount(number, freightTemplateRuleDTO, detailDTO, freightTemplateId, freightMap);
                            freightAmountList.add(goodsTotalFreight);
                            flag = false;
                            moreRule = true;
                            break;
                        }
                    }
                }

                if (flag) {
                    // 下单区域该模板不可配送
                    freightMap.get(freightTemplateId).forEach(o -> o.setFreightStatus(1));
                }

                // 证明是叠加运费的算法
                if (FreightRuleEnum.RULE_TYPE_ADD.value() == ruleType) {
                    if (moreRule && superposition.compareTo(BigDecimal.ZERO) == 1) {
                        freightAmountList.remove(superposition);
                    }
                }

            }

            // 运费
            BigDecimal goodsTotalFreight = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(freightAmountList)) {

                if (FreightRuleEnum.RULE_TYPE_CEIL.value() == ruleType) {
                    // 最高运费
                    goodsTotalFreight = freightAmountList.stream().max(BigDecimal::compareTo).get();
                } else if (FreightRuleEnum.RULE_TYPE_FLOOR.value() == ruleType) {
                    // 最低运费
                    goodsTotalFreight = freightAmountList.stream().min(BigDecimal::compareTo).get();
                } else if (FreightRuleEnum.RULE_TYPE_ADD.value() == ruleType) {
                    // 叠加运费
                    goodsTotalFreight = freightAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                } else if (FreightRuleEnum.RULE_TYPE_MIND.value() == ruleType) {
                    // 智能运费
                    goodsTotalFreight = freightAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                }
            }
            goodsConverOrderDTO.setGoodsTotalFreight(goodsTotalFreight);
            goodsConverOrderDTO.setGoodsTotalPrice(goodsConverOrderDTO.getGoodsTotalPrice().add(goodsTotalFreight));
            goodsConverOrderDTO.setOrderPrice(goodsConverOrderDTO.getOrderPrice().add(goodsTotalFreight));
        }

    }

    /**
     * 功能描述：
     * <计算某规则运费金额>
     *
     * @param number                 商品数量
     * @param freightTemplateRuleDTO 运费模板规则
     * @return
     * @date 2020/5/7 10:07
     * @author 刘远杰
     **/
    private BigDecimal simplePieceFreightAmount(Integer number, FreightTemplateRuleDTO freightTemplateRuleDTO,
                                                AdminFreightTemplateDetailDTO detailDTO, Long freightTemplateId, Map<Long, List<CartGoodsDTO>> freightMap) {
        BigDecimal goodsTotalFreight = BigDecimal.ZERO;
        if (FreightTemplateEnum.TEMPLATE_TYPE_PIECE.value() == detailDTO.getTemplateType()) {
            // 计算件数运费
            if (Integer.parseInt(freightTemplateRuleDTO.getFirstFee()) >= number) {
                // 首件范围内运费计算 - 首件运费为当前运费金额
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount();
            } else {
                // 超出首件范围 首件运费 + 超出部分运费
                // 超出首件商品数量 = 商品总数量 - 首件数量
                BigDecimal additionalNumber = new BigDecimal(number - Integer.parseInt(freightTemplateRuleDTO.getFirstFee()));
                BigDecimal additionalFee = new BigDecimal(freightTemplateRuleDTO.getAdditionalFee());
                // 超出续件倍数 = 超出数量 / 续件数量 向上取证
                BigDecimal ladder = additionalNumber.divide(additionalFee, 0, BigDecimal.ROUND_UP);
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount().add(freightTemplateRuleDTO.getAdditionalAmount().multiply(ladder));
            }

        } else {
            // 累加商品重量
            BigDecimal totalWeight = BigDecimal.ZERO;
            List<CartGoodsDTO> cartGoodsDTOS = freightMap.get(freightTemplateId);
            for (CartGoodsDTO cartGoodsDTO : cartGoodsDTOS) {
                BigDecimal numDec = new BigDecimal(cartGoodsDTO.getGoodsNum());
                BigDecimal weightDec = new BigDecimal(cartGoodsDTO.getSpecWeight());
                BigDecimal goodsWeight = weightDec.multiply(numDec).setScale(2, BigDecimal.ROUND_UP);
                totalWeight = totalWeight.add(goodsWeight);
            }
            log.info("商品重量:{}", totalWeight);
            // 计算重量运费
            if (totalWeight == null || totalWeight.compareTo(new BigDecimal(freightTemplateRuleDTO.getFirstFee())) == -1) {
                // 不超过首重作为当前商品运费
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount();
                log.info("初始运费:{}", goodsTotalFreight);
            } else {
                // 超出重量
                BigDecimal addWeight = totalWeight.subtract(new BigDecimal(freightTemplateRuleDTO.getFirstFee()));
                log.info("超出重量:{}", addWeight);
                BigDecimal additionalFee = new BigDecimal(freightTemplateRuleDTO.getAdditionalFee());
                log.info("续重kg:{}", additionalFee);
//                BigDecimal ladder = addWeight.divide(additionalFee, 0, BigDecimal.ROUND_DOWN);
                BigDecimal[] ladder = addWeight.divideAndRemainder(additionalFee);
                log.info("ladder:{}", JSON.toJSONString(ladder));
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount().add(freightTemplateRuleDTO.getAdditionalAmount().multiply(ladder[0]));
                if (ladder[1].compareTo(BigDecimal.ZERO) == 1) {
                    goodsTotalFreight = goodsTotalFreight.add(freightTemplateRuleDTO.getAdditionalAmount());
                }
                log.info("超出运费:{}", goodsTotalFreight);
            }
        }
        return goodsTotalFreight;
    }

    /**
     * pc首页购物车弹窗列表
     *
     * @param cartDTOList
     * @return
     */

    @Override
    public PcIndexCartDTO pcIndexCart(@RequestBody List<CartDTO> cartDTOList,
                                      @RequestParam(value = "memberId", required = false) Long memberId) {

        PcIndexCartDTO pcIndexCartDTO = new PcIndexCartDTO();
        if (memberId != null) {
            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            Map<String, String> sortFileds = pageModelDTO.getSortFileds();
            // 根据更新时间倒序
            sortFileds.put("updateDate", "desc");
            Map<String, Object> filterSearchCondition = pageModelDTO.getEqualsFilterSearchCondition();
            filterSearchCondition.put("memberId", memberId);
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
            cartDTOList = pageModelDTO.getJsonRsList().stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
        }
        cartDTOList.sort(CartDTO::compare);
        pcIndexCartDTO.setList(cartDTOList);
        pcIndexCartDTO.setNum(cartDTOList.size());
        BigDecimal total = BigDecimal.ZERO;
        for (CartDTO c : cartDTOList) {
            total = total.add(c.getSpecSellPrice().multiply(new BigDecimal(c.getGoodsNum())));
        }
        pcIndexCartDTO.setGoodsTotal(total);
        return pcIndexCartDTO;
    }

    /**
     * 查询失效购物车
     *
     * @param memberId
     * @return
     */

    @Override
    public ResultCartDTO disabledCart(@RequestParam("memberId") Long memberId) {

        // 查询正常购物车
        ResultCartDTO cartList = this.getCartList(memberId, CartEnum.PC_CART_LIST.value());

        // 查询失效的购物车
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();
        // 根据更新时间倒序
        sortFileds.put("updateDate", "desc");
        Map<String, Object> filterSearchCondition = pageModelDTO.getEqualsFilterSearchCondition();
        filterSearchCondition.put("memberId", memberId);

        Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
        inSearchCondition.put("status", Arrays.asList(CartEnum.CART_STATUS_UNDER.value(), CartEnum.CART_STATUS_DEL.value(), CartEnum.CART_STATUS_STOCK.value()));
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);

        List<CartDTO> cartDTOList = pageModelDTO.getJsonRsList().stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
        Map<Long, List<CartDTO>> groupCart = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getStoreId));
        List<DisabledCartVO> disabledCartVOList = new ArrayList<>();

        for (Map.Entry<Long, List<CartDTO>> entry : groupCart.entrySet()) {
            DisabledCartVO disabledCartVO = new DisabledCartVO();
            disabledCartVO.setStoreId(entry.getKey());
            disabledCartVO.setStoreName(entry.getValue().get(0).getStoreName());
            disabledCartVO.setList(entry.getValue());
            disabledCartVOList.add(disabledCartVO);
        }
        cartList.setCartVOList(disabledCartVOList);
        cartList.setTotalNum(cartList.getTotalNum() + cartDTOList.size());
        int isSelectNum = cartDTOList.stream().filter(c -> c.getIsSelect() == 1).collect(Collectors.toList()).size();
        cartList.setSelectNum(cartList.getSelectNum() + isSelectNum);
        return cartList;
    }
}
