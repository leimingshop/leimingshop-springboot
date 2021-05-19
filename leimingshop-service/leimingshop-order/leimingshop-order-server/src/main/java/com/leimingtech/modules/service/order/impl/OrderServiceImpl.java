/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.leimingtech.commons.lock.annotation.Lock4j;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.config.AddressPrefixProperties;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.ShareCodeUtils;
import com.leimingtech.constants.IndexRedisConstants;
import com.leimingtech.dto.reason.ReasonDescriptionDTO;
import com.leimingtech.dto.setting.SettingAftersaleDTO;
import com.leimingtech.dto.setting.SettingDefaultAvatarsDTO;
import com.leimingtech.dto.setting.SettingSeckillDTO;
import com.leimingtech.dto.setting.SettingSeniorDTO;
import com.leimingtech.dto.setting.reward.rule.GrowthRuleSettingDTO;
import com.leimingtech.dto.setting.reward.rule.MoreRuleSettingDTO;
import com.leimingtech.dto.setting.reward.rule.PointRuleSettingDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.enums.MessageSourceEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.aftersale.service.AftersaleApplyService;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.constant.order.OrderDefaultConstants;
import com.leimingtech.modules.constant.order.OrderTimeConstants;
import com.leimingtech.modules.constant.order.RedisConstants;
import com.leimingtech.modules.constants.PaymentCodeConstants;
import com.leimingtech.modules.dao.order.OrderDao;
import com.leimingtech.modules.dto.activity.goods.ActivityGoodsDTO;
import com.leimingtech.modules.dto.activity.goods.UpdateActivitySurplusStorageDTO;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.dto.cart.CartDTO;
import com.leimingtech.modules.dto.cart.CartGoodsDTO;
import com.leimingtech.modules.dto.cart.GoodsConverOrderDTO;
import com.leimingtech.modules.dto.complain.OrderComplainDTO;
import com.leimingtech.modules.dto.coupons.CouponsActivityDTO;
import com.leimingtech.modules.dto.coupons.CouponsGoodsDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsIndexDTO;
import com.leimingtech.modules.dto.flashsale.FlashSaleActivityDTO;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplateDetailDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateRuleDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.ValidateGoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecVersionDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassDTO;
import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.dto.group.GroupDTO;
import com.leimingtech.modules.dto.group.GroupMemberDTO;
import com.leimingtech.modules.dto.group.GroupRecordDTO;
import com.leimingtech.modules.dto.invoice.InvoiceInfoDTO;
import com.leimingtech.modules.dto.invoice.MemberInvoiceDTO;
import com.leimingtech.modules.dto.invoice.OrderInvoiceDTO;
import com.leimingtech.modules.dto.log.point.PointLogPackDTO;
import com.leimingtech.modules.dto.member.MemberBalanceInfoDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.member.MemberUpdateDTO;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.dto.order.freight.FreightRuleDTO;
import com.leimingtech.modules.dto.payment.H5WxRefundBaseDTO;
import com.leimingtech.modules.dto.payment.OrderPayDTO;
import com.leimingtech.modules.dto.payment.wechat.H5WXPayNotifyDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.dto.reduce.ReduceRuleDTO;
import com.leimingtech.modules.dto.seckill.SeckillActivityDTO;
import com.leimingtech.modules.dto.transport.TransportMessageDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDTO;
import com.leimingtech.modules.entity.order.OrderEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.complain.OrderComplainEnum;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.enums.evaluate.GoodsEnum;
import com.leimingtech.modules.enums.flashsale.FlashSaleActivityEnum;
import com.leimingtech.modules.enums.freight.template.FreightTemplateEnum;
import com.leimingtech.modules.enums.goods.GoodsSpecStatusEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.enums.invoice.OrderInvoiceEnum;
import com.leimingtech.modules.enums.order.*;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.address.MemberAddressService;
import com.leimingtech.modules.service.cart.CartService;
import com.leimingtech.modules.service.complain.OrderComplainService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.coupons.MemberCouponsService;
import com.leimingtech.modules.service.flashsale.FlashSaleActivityService;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.service.grade.MemberGradeService;
import com.leimingtech.modules.service.group.GroupMemberService;
import com.leimingtech.modules.service.group.GroupRecordService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.service.invoice.InvoiceInfoService;
import com.leimingtech.modules.service.invoice.MemberInvoiceService;
import com.leimingtech.modules.service.invoice.OrderInvoiceService;
import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.*;
import com.leimingtech.modules.service.order.freight.FreightRuleService;
import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.modules.service.payment.PaymentService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.service.reduce.ReduceRuleService;
import com.leimingtech.modules.service.seckill.SeckillActivityService;
import com.leimingtech.modules.service.transport.TransportMessageService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.statuscode.CartStatusCode;
import com.leimingtech.modules.statuscode.OrderStatusCode;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import com.leimingtech.modules.vo.order.PCOrderInvoicePageVO;
import com.leimingtech.modules.vo.order.PCOrderPageVO;
import com.leimingtech.modules.vo.order.PcOrderDetailVO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.reason.ReasonDescriptionService;
import com.leimingtech.service.setting.PointSettingService;
import com.leimingtech.service.setting.SettingService;
import com.leimingtech.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends CrudServiceImpl<OrderDao, OrderEntity, OrderDTO> implements OrderService {

    // 平台订单店铺id，名称
    private static final Long PLATFORM_ORDER_STORE_ID = 0L;
    private static final String PLATFORM_ORDER_STORE_NAME = "雷铭商城";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(OrderServiceImpl.class);
    @Autowired

    private OrderGoodsService orderGoodsService;

    @Autowired

    private OrderLogisticsLogService orderLogisticsLogService;

    @Autowired

    private OrderAddressService orderAddressService;

    @Autowired

    private OrderLogService orderLogService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired

    private OrderConfirmService orderConfirmService;

    @Autowired

    private OrderGoodsConfirmService orderGoodsConfirmService;

    @Autowired
    private CartService cartService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ReasonDescriptionService reasonDescriptionService;

    @Autowired
    private TransportMessageService transportMessageService;

    @Autowired
    private MemberGradeService memberGradeService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsClassService goodsClassService;

    @Autowired
    private MemberCouponsService memberCouponsService;

    @Autowired
    private CouponsActivityService couponsActivityService;

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private PointSettingService pointSettingService;

    @Autowired
    private PointLogService pointLogService;

    @Autowired
    private ReduceActivityService reduceActivityService;
    @Autowired
    private ReduceRuleService reduceRuleService;
    @Autowired
    private OrderActivityService orderActivityService;

    @Autowired
    private FlashSaleActivityService flashSaleActivityService;

    @Autowired

    private OrderInvoiceService orderInvoiceService;

    @Autowired

    private InvoiceInfoService invoiceInfoService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRecordService groupRecordService;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired

    private FetchCodeService fetchCodeService;

    @Autowired
    private UploadService uploadService;
    @Autowired
    private MemberInvoiceService memberInvoiceService;

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private FreightTemplateService freightTemplateService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private FreightRuleService freightRuleService;


    @Autowired
    private OrderComplainService orderComplainService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private PaymentService paymentService;


    @Autowired
    private AftersaleApplyService aftersaleApplyService;

    /**
     * 功能描述:
     * 〈admin/seller订单分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<OrderDTO> page(@RequestParam Map<String, Object> params,
                                   @RequestParam(value = "storeId", required = false) Long storeId) {
        if (storeId != null) {
            params.put("storeId", storeId);
        }
        IPage<OrderEntity> page = getPage(params, Constant.CREATE_DATE, false);
        List<OrderEntity> orderList = baseDao.findOrderList(params);

        return getPageData(orderList, page.getTotal(), OrderDTO.class);
    }

    /**
     * 拼团订单列表
     *
     * @return
     * @date 2020-03-25 16:50
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public PageData<OrderDTO> groupPage(@RequestParam Map<String, Object> params,
                                        @RequestParam(value = "storeId", required = false) Long storeId) {
        if (storeId != null) {
            params.put("storeId", storeId);
        }
        IPage<OrderEntity> page = getPage(params, Constant.CREATE_DATE, false);
        List<OrderEntity> orderList = baseDao.findPageGroupOrderList(params);

        return getPageData(orderList, page.getTotal(), OrderDTO.class);
    }

    /**
     * 功能描述:
     * 〈h5订单分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<H5OrderPageDTO> pageH5(@RequestParam Map<String, Object> params,
                                           @RequestParam("buyerId") Long buyerId) {
        params.put("buyerId", buyerId);
        IPage<OrderEntity> page = getPage(params, Constant.CREATE_DATE, false);
        List<H5OrderPageDTO> orderList = baseDao.findH5OrderList(params, page);
        orderList.forEach(order -> {
            order.setNowTime(System.currentTimeMillis());
        });
        return new PageData<>(orderList, page.getTotal());
    }

    /**
     * pc订单分页查询
     *
     * @return
     * @date 2020-05-18 14:52
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public PageData<PCOrderPageVO> pagePC(@RequestParam Map<String, Object> params,
                                          @RequestParam("buyerId") Long buyerId) {
        params.put("buyerId", buyerId);
        IPage<OrderEntity> page = getPage(params, Constant.CREATE_DATE, false);
        List<PCOrderPageVO> orderList = baseDao.findPcOrderList(params, page);

        List<PCOrderPageVO> newlist = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            PCOrderPageVO pcOrderPageVO = orderList.get(i);
            // 没有父订单的情况
            if (null == pcOrderPageVO.getParentOrderSn()) {
                newlist.add(pcOrderPageVO);
                // 有父订单的情况
            } else {
                PCOrderPageVO parentOrder = new PCOrderPageVO();
                parentOrder.setOrderSn(pcOrderPageVO.getParentOrderSn());
                parentOrder.setCreateDate(pcOrderPageVO.getCreateDate());
                parentOrder.setOrderStatus(pcOrderPageVO.getOrderStatus());
                parentOrder.setIsSplit(1);
                parentOrder.setPaymentName(pcOrderPageVO.getPaymentName());
                if (CollectionUtils.isNotEmpty(newlist)) {

                    PCOrderPageVO senOrder = newlist.get(newlist.size() - 1);
                    // 判断已有数组中是否存在父订单
                    if (pcOrderPageVO.getParentOrderSn().equals(senOrder.getOrderSn())) {
                        senOrder.getOrderChildrenList().add(pcOrderPageVO);
                    } else {
                        List<PCOrderPageVO> orderChildrenList = new ArrayList<>();
                        orderChildrenList.add(pcOrderPageVO);
                        parentOrder.setOrderChildrenList(orderChildrenList);
                        newlist.add(parentOrder);
                    }
                } else {
                    List<PCOrderPageVO> orderChildrenList = new ArrayList<>();
                    orderChildrenList.add(pcOrderPageVO);
                    parentOrder.setOrderChildrenList(orderChildrenList);
                    newlist.add(parentOrder);
                }
            }
        }

        newlist.forEach(order -> {

            if (order.getOrderStatus() == 10) {
                OrderPayDTO orderPayDTO = orderPayService.getByPaySn(order.getPaySn());
                if (orderPayDTO != null) {
                    // 设置支付取消时间
                    order.setCancalDate(orderPayDTO.getCancalDate());
                    order.setCurrentTime(System.currentTimeMillis());
                }
            }
        });
        return new PageData<>(newlist, page.getTotal());
    }

    /**
     * @Author weixianchun
     * @Description 导出订单列表
     * @Param params
     * @Date 2019/12/4 21:24
     * @Return java.util.List<com.leimingtech.modules.dto.order.OrderDTO>
     * @version 1.0
     */

    @Override
    public List<OrderDTO> findOrderListExport(@RequestParam Map<String, Object> params) {
        List<OrderEntity> orderList = baseDao.findOrderList(params);
        return ConvertUtils.sourceToTarget(orderList, OrderDTO.class);
    }


    /**
     * 功能描述:
     * 〈根据订单id查询订单详情〉
     *
     * @param id      订单id
     * @param buyerId 买家id
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    @Override
    public AdminOrderDetailDTO getAdminOrderDetail(Long id,
                                                   @RequestParam(value = "buyerId", required = false) Long buyerId,
                                                   @RequestParam(value = "storeId", required = false) Long storeId) {
        // 订单主表
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        wrapper.eq(storeId != null, "store_id", storeId);
        OrderEntity orderEntity = baseDao.selectOne(wrapper);

        return getAdminOrderDetailDTOByOrderEntity(orderEntity);

    }


    /**
     * H5查询订单详情
     *
     * @param orderId:  订单ID
     * @param memberId: 会员ID
     * @return 订单详情数据（基础、地址、商品数据）
     * @date 2019/11/6 17:48
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public H5OrderDetailDTO findH5OrderDetail(@RequestParam("orderId") Long orderId,
                                              @RequestParam("memberId") Long memberId) {

        H5OrderDetailDTO h5OrderDetailDTO = new H5OrderDetailDTO();

        OrderDTO orderDTO = this.findOrderDetail(orderId, memberId);

        BeanCopier.create(orderDTO.getClass(), h5OrderDetailDTO.getClass(), false)
                .copy(orderDTO, h5OrderDetailDTO, null);
        //订单发票信息
        if (null != h5OrderDetailDTO.getInvoiceFlag() && !h5OrderDetailDTO.getInvoiceFlag().equals(0)) {
            OrderInvoiceDTO orderInvoiceDTO = orderInvoiceService.getOrderInvoiceDTO(orderDTO.getId());
            if (orderInvoiceDTO.getCompanyType().equals(OrderInvoiceEnum.INVOICE_PERSONAL.value())) {
                h5OrderDetailDTO.setCompany(StringUtils.isNotBlank(orderInvoiceDTO.getPersonalName())
                        ? orderInvoiceDTO.getPersonalName() : orderInvoiceDTO.getAddresseeName());
            } else {
                h5OrderDetailDTO.setCompany(orderInvoiceDTO.getCompany());
            }
            if (null != orderInvoiceDTO.getStoreInvoiceContent() && null != orderInvoiceDTO.getStoreInvoiceType()) {
                h5OrderDetailDTO.setInvoiceType(orderInvoiceDTO.getStoreInvoiceType());
                h5OrderDetailDTO.setInvoiceContent(orderInvoiceDTO.getStoreInvoiceContent());
            } else {
                h5OrderDetailDTO.setInvoiceType(orderInvoiceDTO.getMemberInvoiceType());
                h5OrderDetailDTO.setInvoiceContent(orderInvoiceDTO.getMemberInvoiceContent());
            }
        }
        OrderPayDTO orderPayDTO = orderPayService.getByPaySn(orderDTO.getPaySn());
        if (orderPayDTO != null) {
            // 设置支付取消时间
            h5OrderDetailDTO.setCancelDate(orderPayDTO.getCancalDate());
        }

        // 如果订单待付款并且存在拆单情况  需要根据子订单ID查询订单商品等信息
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();
        if ((orderDTO.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()
                || orderDTO.getOrderStatus() == OrderStatusEnum.CANCELED.value())
                && orderDTO.getIsSplit() == OrderSplitEnum.YES.value()) {
            // 订单商品
            orderGoodsDTOList = orderGoodsService.findOrderGoodsByParentOrderSn(orderDTO.getOrderSn());
            // 订单留言
            List<OrderDTO> orderDTOList = baseDao.findOrderMessageByParentOrderSn(orderDTO.getOrderSn());
            List<H5OrderDetailGoodsListDTO> orderDetailGoodsListDTOS = packOrderDetailGoodsList(orderGoodsDTOList,
                    orderDTOList);
            h5OrderDetailDTO.setOrderDetailGoodsListDTOList(orderDetailGoodsListDTOS);
        } else {
            // 订单商品
            orderGoodsDTOList = orderGoodsService.getByOrderId(orderDTO.getId(), null, null);
            ArrayList<OrderDTO> orderDTOList = new ArrayList<>();
            orderDTOList.add(orderDTO);
            List<H5OrderDetailGoodsListDTO> orderDetailGoodsListDTOS = packOrderDetailGoodsList(orderGoodsDTOList,
                    orderDTOList);
            h5OrderDetailDTO.setOrderDetailGoodsListDTOList(orderDetailGoodsListDTOS);
        }

        // 订单物流信息
        OrderLogisticsDTO logistics = orderLogisticsLogService.findLogistics(String.valueOf(orderId));
        h5OrderDetailDTO.setOrderLogisticsDTO(logistics);

        // 收货地址
        OrderAddressDTO orderAddressDTO = orderAddressService.get(orderDTO.getAddressId());
        h5OrderDetailDTO.setOrderAddressDTO(orderAddressDTO);

        // 判断是否是电子提货码方式
        if (null != h5OrderDetailDTO.getDevlierType() && h5OrderDetailDTO.getDevlierType().equals(DevlierTypeEnum.FETCH_CODE.value())) {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("storeId", String.valueOf(orderDTO.getStoreId()));
            queryParams.put("orderId", String.valueOf(orderDTO.getId()));
            queryParams.put("memberId", String.valueOf(orderDTO.getBuyerId()));
            List<FetchCodeDTO> list = fetchCodeService.list(queryParams);
            h5OrderDetailDTO.setFetchCodeDTOList(list);
        }

        h5OrderDetailDTO.setIsAfterSale(AfterSettingEnum.FALSE.value());
        if (OrderStatusEnum.COMPLETE.value() == orderDTO.getOrderStatus()) {
            // 订单已结束
            //获取setting表中的是否开启售后按钮SettingAftersaleDTO
            String queryRedisByName = settingService.queryRedisByName(SettingsEnum.AFTERSALE.value());
            SettingAftersaleDTO aftersaleDTO = JSON.parseObject(queryRedisByName, SettingAftersaleDTO.class);
            //获取setting表中的可申请售后时间
            String setting = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
            SettingSeniorDTO senior = JSON.parseObject(setting, SettingSeniorDTO.class);
            if (aftersaleDTO != null && senior != null) {
                Date completeTime = orderDTO.getCompleteTime();
                AftersaleSettingDTO aftersaleSettingDTO = new AftersaleSettingDTO();
                aftersaleSettingDTO.setCannotReturn(senior.getCannotReturn());
                aftersaleSettingDTO.setCannotBarter(senior.getCannotBarter());
                aftersaleSettingDTO.setGoodsReturn(aftersaleDTO.getGoodsReturn());
                aftersaleSettingDTO.setGoodsBarter(aftersaleDTO.getGoodsBarter());
                aftersaleSettingDTO.setCompleteTime(completeTime);
                AftersaleAvailTypeDTO aftersaleAvailTypeDTO = orderGoodsService.findAvailType(aftersaleSettingDTO);
                if (aftersaleAvailTypeDTO != null && (AfterSettingEnum.TRUE.value() == aftersaleAvailTypeDTO.getIsBarter()
                        || AfterSettingEnum.TRUE.value() == aftersaleAvailTypeDTO.getIsReturn())) {
                    // 在售后时间内
                    for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                        // 存在可售后商品
                        if (orderGoodsDTO.getAftersaleQuantity() > 0) {
                            h5OrderDetailDTO.setIsAfterSale(AfterSettingEnum.TRUE.value());
                            break;
                        }
                    }
                }
            }

            if (orderDTO.getVirtualFlag() == VirtualFlagEnum.YES.value()) {
                Long goodsId = orderGoodsDTOList.get(0).getGoodsId();
                GoodsDTO goodsDTO = goodsService.get(goodsId);
                // 提货码过期退款（0不支持，1支持）
                if (goodsDTO.getCodeRefundFlag() == 0) {
                    h5OrderDetailDTO.setIsAfterSale(AfterSettingEnum.FALSE.value());
                }
            }

        }

        // 拼团记录头像添加
        if (null != orderDTO.getGroupRecordId()) {
            // 拼团用户头像信息
            List<GroupMemberDTO> groupMemberDTOList =
                    Optional.ofNullable(groupMemberService.recordList(orderDTO.getGroupRecordId())).orElse(Lists.newArrayList());

            if (CollectionUtils.isNotEmpty(groupMemberDTOList)) {

                List<String> memberImages = new ArrayList<>();
                groupMemberDTOList.forEach(groupMemberDTO -> {
                    memberImages.add(groupMemberDTO.getMemberImage());
                });

                // 只有拼团成功或者失败需要填充默认头像
                if (orderDTO.getGroupStatus() != GroupsEnum.GROUP_STATUS_ONGOING.value()) {
                    // 判断填充默认头像
                    if (orderDTO.getGroupNeedNum() >= memberImages.size()) {
                        SettingDefaultAvatarsDTO defaultAvatarsSet = settingService.getDefaultAvatarsSet();
                        if (null == defaultAvatarsSet) {
                            log.error("查询用户默认头像设置异常");
                        }
                        String defaultAvatars = String.valueOf(defaultAvatarsSet.getAvatar());
                        for (int j = 0; j < orderDTO.getGroupNeedNum(); j++) {
                            memberImages.add(defaultAvatars);
                        }
                    }
                }

                h5OrderDetailDTO.setGroupMemberDTOList(memberImages);
            }
        }
        // 查询当前订单是否投诉
        OrderComplainDTO orderComplainDTO = orderComplainService.orderComInfo(h5OrderDetailDTO.getId(),
                h5OrderDetailDTO.getBuyerId());
        h5OrderDetailDTO.setComplainFlag(orderComplainDTO == null ? OrderComplainEnum.COMPLAIN_FLAG_NO.value() :
                OrderComplainEnum.COMPLAIN_FLAG_YES.value());


        return h5OrderDetailDTO;
    }


    /**
     * 查询pc端订单详情
     *
     * @return
     * @date 2020-05-21 11:47
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public PcOrderDetailVO findPCOrderDetail(@RequestParam("orderId") Long orderId,
                                             @RequestParam("memberId") Long memberId) {

        PcOrderDetailVO pcOrderDetailVO = new PcOrderDetailVO();

        OrderDTO orderDTO = this.findOrderDetail(orderId, memberId);

        BeanCopier.create(orderDTO.getClass(), pcOrderDetailVO.getClass(), false)
                .copy(orderDTO, pcOrderDetailVO, null);
        //订单发票信息
        if (null != pcOrderDetailVO.getInvoiceFlag() && !pcOrderDetailVO.getInvoiceFlag().equals(0)) {
            OrderInvoiceDTO orderInvoiceDTO = orderInvoiceService.getOrderInvoiceDTO(orderDTO.getId());
            if (orderInvoiceDTO.getCompanyType().equals(OrderInvoiceEnum.INVOICE_PERSONAL.value())) {
                pcOrderDetailVO.setCompany(StringUtils.isNotBlank(orderInvoiceDTO.getPersonalName())
                        ? orderInvoiceDTO.getPersonalName() : orderInvoiceDTO.getAddresseeName());
            } else {
                pcOrderDetailVO.setCompany(orderInvoiceDTO.getCompany());
            }
            if (null != orderInvoiceDTO.getStoreInvoiceContent() && null != orderInvoiceDTO.getStoreInvoiceType()) {
                pcOrderDetailVO.setInvoiceType(orderInvoiceDTO.getStoreInvoiceType());
                pcOrderDetailVO.setInvoiceContent(orderInvoiceDTO.getStoreInvoiceContent());
            } else {
                pcOrderDetailVO.setInvoiceType(orderInvoiceDTO.getMemberInvoiceType());
                pcOrderDetailVO.setInvoiceContent(orderInvoiceDTO.getMemberInvoiceContent());
            }
        }
        OrderPayDTO orderPayDTO = orderPayService.getByPaySn(orderDTO.getPaySn());
        if (orderPayDTO != null) {
            // 设置支付取消时间
            pcOrderDetailVO.setCancelDate(orderPayDTO.getCancalDate());
        }

        // 如果订单待付款并且存在拆单情况  需要根据子订单ID查询订单商品等信息
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();
        if ((orderDTO.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()
                || orderDTO.getOrderStatus() == OrderStatusEnum.CANCELED.value() || orderDTO.getOrderStatus() == OrderStatusEnum.WAITTING_SHIPPED.value())
                && orderDTO.getIsSplit() == OrderSplitEnum.YES.value()) {
            // 订单商品
            orderGoodsDTOList = orderGoodsService.findOrderGoodsByParentOrderSn(orderDTO.getOrderSn());
            // 订单留言
            List<OrderDTO> orderDTOList = baseDao.findOrderMessageByParentOrderSn(orderDTO.getOrderSn());
            List<H5OrderDetailGoodsListDTO> orderDetailGoodsListDTOS = packOrderDetailGoodsList(orderGoodsDTOList,
                    orderDTOList);
            pcOrderDetailVO.setOrderDetailGoodsListDTOList(orderDetailGoodsListDTOS);
        } else {
            // 订单商品
            orderGoodsDTOList = orderGoodsService.getByOrderId(orderDTO.getId(), null, null);
            ArrayList<OrderDTO> orderDTOList = new ArrayList<>();
            orderDTOList.add(orderDTO);
            List<H5OrderDetailGoodsListDTO> orderDetailGoodsListDTOS = packOrderDetailGoodsList(orderGoodsDTOList,
                    orderDTOList);
            pcOrderDetailVO.setOrderDetailGoodsListDTOList(orderDetailGoodsListDTOS);
        }

        // 订单物流信息
        OrderLogisticsDTO logistics = orderLogisticsLogService.findLogistics(String.valueOf(orderId));

        if (null != logistics) {
            // 格式化物流信息处理
            List<LogisticsProcessDTO> logisticsProcessDTOList = logistics.getData();

            if (CollectionUtils.isNotEmpty(logisticsProcessDTOList)) {
                Map<String, List<LogisticsProcessDTO>> listLinkedHashMap = new LinkedHashMap<>();
                for (LogisticsProcessDTO logisticsProcessDTO : logisticsProcessDTOList) {
                    String time = logisticsProcessDTO.getFtime();
                    String weekname = this.getWeek(time);
                    String finalDate = time.substring(0, 10);
                    finalDate = finalDate + " " + weekname;
                    if (listLinkedHashMap.containsKey(finalDate)) {
                        listLinkedHashMap.get(finalDate).add(logisticsProcessDTO);
                    } else {
                        List<LogisticsProcessDTO> logisticsProcessDTOList1 = new ArrayList<>();
                        logisticsProcessDTOList1.add(logisticsProcessDTO);
                        listLinkedHashMap.put(finalDate, logisticsProcessDTOList1);
                    }

                }

                pcOrderDetailVO.setLogisticsMap(listLinkedHashMap);
                logistics.setData(null);
                pcOrderDetailVO.setOrderLogisticsDTO(logistics);
            }
        }

        // 收货地址
        OrderAddressDTO orderAddressDTO = orderAddressService.get(orderDTO.getAddressId());
        pcOrderDetailVO.setOrderAddressDTO(orderAddressDTO);

        // 判断是否是电子提货码方式
        if (null != pcOrderDetailVO.getDevlierType() && pcOrderDetailVO.getDevlierType().equals(DevlierTypeEnum.FETCH_CODE.value())) {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("storeId", String.valueOf(orderDTO.getStoreId()));
            queryParams.put("orderId", String.valueOf(orderDTO.getId()));
            queryParams.put("memberId", String.valueOf(orderDTO.getBuyerId()));
            List<FetchCodeDTO> list = fetchCodeService.list(queryParams);
            pcOrderDetailVO.setFetchCodeDTOList(list);
        }

        pcOrderDetailVO.setIsAfterSale(AfterSettingEnum.FALSE.value());
        if (OrderStatusEnum.COMPLETE.value() == orderDTO.getOrderStatus()) {
            // 订单已结束
            //获取setting表中的是否开启售后按钮SettingAftersaleDTO
            String queryRedisByName = settingService.queryRedisByName(SettingsEnum.AFTERSALE.value());
            SettingAftersaleDTO aftersaleDTO = JSON.parseObject(queryRedisByName, SettingAftersaleDTO.class);
            //获取setting表中的可申请售后时间
            String setting = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
            SettingSeniorDTO senior = JSON.parseObject(setting, SettingSeniorDTO.class);
            if (aftersaleDTO != null && senior != null) {
                Date completeTime = orderDTO.getCompleteTime();
                AftersaleSettingDTO aftersaleSettingDTO = new AftersaleSettingDTO();
                aftersaleSettingDTO.setCannotReturn(senior.getCannotReturn());
                aftersaleSettingDTO.setCannotBarter(senior.getCannotBarter());
                aftersaleSettingDTO.setGoodsReturn(aftersaleDTO.getGoodsReturn());
                aftersaleSettingDTO.setGoodsBarter(aftersaleDTO.getGoodsBarter());
                aftersaleSettingDTO.setCompleteTime(completeTime);
                AftersaleAvailTypeDTO aftersaleAvailTypeDTO = orderGoodsService.findAvailType(aftersaleSettingDTO);
                if (aftersaleAvailTypeDTO != null && (AfterSettingEnum.TRUE.value() == aftersaleAvailTypeDTO.getIsBarter()
                        || AfterSettingEnum.TRUE.value() == aftersaleAvailTypeDTO.getIsReturn())) {
                    // 在售后时间内
                    for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                        // 存在可售后商品
                        if (orderGoodsDTO.getAftersaleQuantity() > 0) {
                            pcOrderDetailVO.setIsAfterSale(AfterSettingEnum.TRUE.value());
                            break;
                        }
                    }
                }
            }

            if (orderDTO.getVirtualFlag() == VirtualFlagEnum.YES.value()) {
                Long goodsId = orderGoodsDTOList.get(0).getGoodsId();
                GoodsDTO goodsDTO = goodsService.get(goodsId);
                // 提货码过期退款（0不支持，1支持）
                if (goodsDTO.getCodeRefundFlag() == 0) {
                    pcOrderDetailVO.setIsAfterSale(AfterSettingEnum.FALSE.value());
                }
            }

        }

        // 拼团记录头像添加
        if (null != orderDTO.getGroupRecordId()) {
            // 拼团用户头像信息
            List<GroupMemberDTO> groupMemberDTOList =
                    Optional.ofNullable(groupMemberService.recordList(orderDTO.getGroupRecordId())).orElse(Lists.newArrayList());

            if (CollectionUtils.isNotEmpty(groupMemberDTOList)) {

                List<String> memberImages = new ArrayList<>();
                groupMemberDTOList.forEach(groupMemberDTO -> {
                    memberImages.add(groupMemberDTO.getMemberImage());
                });

                // 只有拼团成功或者失败需要填充默认头像
                if (orderDTO.getGroupStatus() != GroupsEnum.GROUP_STATUS_ONGOING.value()) {
                    // 判断填充默认头像
                    if (orderDTO.getGroupNeedNum() >= memberImages.size()) {
                        SettingDefaultAvatarsDTO defaultAvatarsSet = settingService.getDefaultAvatarsSet();
                        if (null == defaultAvatarsSet) {
                            log.error("查询用户默认头像设置异常");
                        }
                        String defaultAvatars = String.valueOf(defaultAvatarsSet.getAvatar());
                        for (int j = 0; j < orderDTO.getGroupNeedNum(); j++) {
                            memberImages.add(defaultAvatars);
                        }
                    }
                }

                pcOrderDetailVO.setGroupMemberDTOList(memberImages);
            }
        }

        OrderEntity orderEntity = ConvertUtils.sourceToTarget(orderDTO, OrderEntity.class);
        // 操作时间
        List<AdminOrderTimeDTO> orderTimeList = this.createOrderTimeList(orderEntity);

        pcOrderDetailVO.setOrderTimeDTOList(orderTimeList);

        return pcOrderDetailVO;
    }

    /**
     * 根据日期计算当前日期是星期几？
     *
     * @param dates 字符串的日期
     * @return
     * @date 2020-06-05 13:52
     * @author huangkeyuan@leimingtech.com
     **/
    private String getWeek(String dates) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = f.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;

        String week = null;
        switch (w) {
            case 0:
                week = "周日";
                break;
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;

            default:
                break;
        }
        return week;

    }

    /**
     * 封装订单详情页商品列表实体(按照店铺拆分)
     *
     * @param orderGoodsDTOList: 订单商品实体
     * @return 订单详情页商品列表实体
     * @date 2019/11/7 13:53
     * @author lixiangx@leimingtech.com
     **/
    private List<H5OrderDetailGoodsListDTO> packOrderDetailGoodsList(List<OrderGoodsDTO> orderGoodsDTOList,
                                                                     List<OrderDTO> orderDTOList) {

        List<H5OrderDetailGoodsListDTO> orderDetailGoodsListDTOList = new ArrayList<>();

        Map<Long, List<OrderGoodsDTO>> result = new HashMap<>();
        orderGoodsDTOList.forEach(orderGoodsDTO -> {
            List<OrderGoodsDTO> orderGoodsDTOS = result.get(orderGoodsDTO.getStoreId());
            if (CollectionUtils.isEmpty(orderGoodsDTOS)) {
                orderGoodsDTOS = new ArrayList<>();
            }
            orderGoodsDTOS.add(orderGoodsDTO);
            result.put(orderGoodsDTO.getStoreId(), orderGoodsDTOS);
        });

        result.keySet().forEach(storeId -> {
            H5OrderDetailGoodsListDTO h5OrderDetailGoodsListDTO = new H5OrderDetailGoodsListDTO();
            h5OrderDetailGoodsListDTO.setStoreId(storeId);
            h5OrderDetailGoodsListDTO.setStoreName(result.get(storeId).get(0).getStoreName());
            h5OrderDetailGoodsListDTO.setOrderGoodsDTOList(result.get(storeId));
            for (OrderDTO orderDTO : orderDTOList) {
                if (storeId.equals(orderDTO.getStoreId())) {
                    h5OrderDetailGoodsListDTO.setOrderMessage(orderDTO.getOrderMessage());
                    break;
                }
            }
            orderDetailGoodsListDTOList.add(h5OrderDetailGoodsListDTO);
        });

        return orderDetailGoodsListDTOList;
    }


    /**
     * 根据用户信息和订单信息查询订单
     *
     * @param orderId:  订单id
     * @param memberId: 会员ID
     * @return 订单基础信息
     * @date 2019/11/6 17:58
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public OrderDTO findOrderDetail(@RequestParam("orderId") Long orderId,
                                    @RequestParam("memberId") Long memberId) {
        QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        queryWrapper.eq("buyer_id", memberId);
        OrderEntity orderEntity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(orderEntity, OrderDTO.class);
    }

    /**
     * 根据订单编号获取订单信息
     *
     * @param orderSn: 订单编号
     * @return 订单基础信息
     * @date 2020/4/23 16:59
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public OrderDTO findOrderByOrderSn(@RequestParam("orderSn") String orderSn) {
        OrderEntity orderEntity =
                Optional.ofNullable(baseDao.selectOne(Wrappers.<OrderEntity>lambdaQuery().eq(OrderEntity::getOrderSn,
                        orderSn))).orElse(new OrderEntity());
        return ConvertUtils.sourceToTarget(orderEntity, OrderDTO.class);
    }

    /**
     * 功能描述:
     * 〈根据父订单号查询子订单集合〉
     *
     * @param parentOrderSn 父订单编号
     * @author : 刘远杰
     */

    @Override
    public List<OrderDTO> childOrderList(Long parentOrderSn) {
        return baseDao.findChildOrderList(parentOrderSn);
    }

    /**
     * 功能描述:
     * 〈根据订单编号查询订单详情〉
     *
     * @param orderSn 订单编号
     * @param buyerId 买家id
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    @Override
    public AdminOrderDetailDTO getAdminOrderDetailByOrderSn(Long orderSn,
                                                            @RequestParam(value = "buyerId", required = false) Long buyerId,
                                                            @RequestParam(value = "storeId", required = false) Long storeId) {
        // 订单主表
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(orderSn != null, "order_sn", orderSn);
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        wrapper.eq(storeId != null, "store_id", storeId);
        OrderEntity orderEntity = baseDao.selectOne(wrapper);

        return getAdminOrderDetailDTOByOrderEntity(orderEntity);

    }

    /**
     * 功能描述：
     * <根据订单编号查询订单详情及订单活动信息>
     *
     * @param orderSn 订单编号
     * @param storeId 店铺id
     * @return
     * @date 2020/2/24 10:51
     * @author 刘远杰
     **/
    @Override

    public AdminOrderActivityDetailDTO getOrderActivityDetail(Long orderSn,
                                                              @RequestParam(value = "storeId", required = false) Long storeId) {
        // 订单主表
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(orderSn != null, "order_sn", orderSn);
        wrapper.eq(storeId != null, "store_id", storeId);
        OrderEntity orderEntity = baseDao.selectOne(wrapper);

        return this.getOrderActivityDetail(orderEntity);
    }

    /**
     * 功能描述:
     * 〈根据订单实体获得订单详情实体〉
     *
     * @param orderEntity 订单实体
     * @author : 刘远杰
     */
    private AdminOrderDetailDTO getAdminOrderDetailDTOByOrderEntity(OrderEntity orderEntity) {
        AdminOrderDetailDTO adminOrderDetailDTO = new AdminOrderDetailDTO();

        if (orderEntity != null) {
            adminOrderDetailDTO = ConvertUtils.sourceToTarget(orderEntity, AdminOrderDetailDTO.class);
            OrderPayDTO orderPayDTO = orderPayService.getByPaySn(adminOrderDetailDTO.getPaySn());
            if (orderPayDTO != null) {
                // 设置支付取消时间
                adminOrderDetailDTO.setCancelDate(orderPayDTO.getCancalDate());
            }
            // 订单商品
            // 如果订单待付款并且存在拆单情况  需要根据子订单ID查询订单商品等信息
            if ((orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()
                    || orderEntity.getOrderStatus() == OrderStatusEnum.CANCELED.value())
                    && orderEntity.getIsSplit() == OrderSplitEnum.YES.value()) {
                // 订单商品
                List<OrderGoodsDTO> orderGoodsDTOList =
                        orderGoodsService.findOrderGoodsByParentOrderSn(orderEntity.getOrderSn());
                adminOrderDetailDTO.setOrderGoodsDTOList(orderGoodsDTOList);
            } else {
                // 订单商品
                List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsService.getByOrderId(orderEntity.getId(), null, null);
                adminOrderDetailDTO.setOrderGoodsDTOList(orderGoodsDTOList);
            }
            // 订单物流信息
            OrderLogisticsDTO logistics = orderLogisticsLogService.findLogistics(String.valueOf(orderEntity.getId()));
            adminOrderDetailDTO.setOrderLogisticsDTO(logistics);
            // 收货地址
            OrderAddressDTO orderAddressDTO = orderAddressService.get(adminOrderDetailDTO.getAddressId());
            adminOrderDetailDTO.setOrderAddressDTO(orderAddressDTO);
            // 订单操作记录
            List<OrderLogDTO> orderLogDTOList = orderLogService.getByOrderId(adminOrderDetailDTO.getId());
            adminOrderDetailDTO.setOrderLogDTOList(orderLogDTOList);
            // 操作时间
            List<AdminOrderTimeDTO> orderTimeList = this.createOrderTimeList(orderEntity);
            adminOrderDetailDTO.setOrderTimeDTOList(orderTimeList);
            //发票信息
            OrderInvoiceDTO orderInvoiceDTO = orderInvoiceService.getOrderInvoiceDTO(adminOrderDetailDTO.getId());
            adminOrderDetailDTO.setOrderInvoiceDTO(orderInvoiceDTO);
            // todo 配送信息
            // 虚拟订单设置电子提货码
            if (orderEntity.getVirtualFlag() == VirtualFlagEnum.YES.value() && orderEntity.getDevlierType().equals(DevlierTypeEnum.FETCH_CODE.value())) {
                Map<String, Object> queryParams = new HashMap<>();
                queryParams.put("storeId", String.valueOf(orderEntity.getStoreId()));
                queryParams.put("orderId", String.valueOf(orderEntity.getId()));
                List<FetchCodeDTO> list = fetchCodeService.list(queryParams);
                adminOrderDetailDTO.setFetchCodeDTOList(list);
            }

            // todo 配送信息，发票信息

        }
        return adminOrderDetailDTO;
    }

    /**
     * 功能描述:
     * 〈根据订单实体获得订单及订单商品信息〉
     *
     * @param orderEntity 订单实体
     * @author : 刘远杰
     */
    private AdminOrderActivityDetailDTO getOrderActivityDetail(OrderEntity orderEntity) {
        AdminOrderActivityDetailDTO adminOrderActivityDetail = new AdminOrderActivityDetailDTO();

        if (orderEntity != null) {
            // 订单商品
            adminOrderActivityDetail = ConvertUtils.sourceToTarget(orderEntity, AdminOrderActivityDetailDTO.class);
            if ((orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()
                    || orderEntity.getOrderStatus() == OrderStatusEnum.CANCELED.value())
                    && orderEntity.getIsSplit() == OrderSplitEnum.YES.value()) {
                // 订单为待付款或取消状态并且存在拆单情况，根据父订单编号查询订单商品等信息
                List<OrderGoodsDTO> orderGoodsDTOList =
                        orderGoodsService.findOrderGoodsByParentOrderSn(orderEntity.getOrderSn());
                adminOrderActivityDetail.setOrderGoodsList(orderGoodsDTOList);
            } else {
                // 子订单根据订单编号查询订单商品等信息
                List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsService.getByOrderId(orderEntity.getId(), null, null);
                adminOrderActivityDetail.setOrderGoodsList(orderGoodsDTOList);
            }

            // 订单优惠券活动
            if (StringUtils.isNotBlank(orderEntity.getMemberCouponsId())) {
                // 1.获取订单表保存的会员优惠券id
                List<Long> memberCouponsIds = new ArrayList<>();
                JSONObject jsonObject = JSONObject.parseObject(orderEntity.getMemberCouponsId());
                jsonObject.values().stream().filter(Objects::nonNull).forEach(o -> memberCouponsIds.add(Long.parseLong(o.toString())));
                if (CollectionUtils.isNotEmpty(memberCouponsIds)) {
                    // 2.会员优惠券不为空，查询会员优惠券
                    List<MemberCouponsDTO> memberCouponsList = memberCouponsService.getByIds(memberCouponsIds);
                    // 3.对会员优惠券根据活动id去重
                    List<Long> activityIds =
                            memberCouponsList.stream().map(MemberCouponsDTO::getActivityId).distinct().collect(Collectors.toList());
                    // 4.根据优惠券活动id查询活动
                    List<CouponsActivityDTO> couponsActivityList = couponsActivityService.getByIds(activityIds);
                    // 5.转化为前台数据封装
                    List<AdminOrderCouponsDTO> adminOrderCouponsList =
                            ConvertUtils.sourceToTarget(couponsActivityList, AdminOrderCouponsDTO.class);
                    adminOrderActivityDetail.setAdminOrderCouponsList(adminOrderCouponsList);
                }
            }

            // 订单促销活动集合
            List<AdminOrderActivityDTO> adminOrderActivityList = new ArrayList<>();
            // 1.查询订单活动集合
            List<OrderActivityDTO> orderActivityList = new ArrayList<>();
            if ((orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()
                    || orderEntity.getOrderStatus() == OrderStatusEnum.CANCELED.value())
                    && orderEntity.getIsSplit() == OrderSplitEnum.YES.value()) {
                // 父订单查询子订单
                QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("parent_order_sn", orderEntity.getOrderSn());
                List<OrderEntity> entityList = baseDao.selectList(queryWrapper);
                List<Long> orderIds = entityList.stream().map(OrderEntity::getId).collect(Collectors.toList());
                // 子订单查询订单活动集合
                orderActivityList = orderActivityService.getByOrderIds(orderIds);
            } else {
                // 子订单查询订单活动集合
                orderActivityList = orderActivityService.getByOrderId(orderEntity.getId());
            }

            // 2.根据活动id以及活动类型去重
            ArrayList<OrderActivityDTO> collect = orderActivityList.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getActivityId() + ";" + o.getActivityType()))),
                            ArrayList::new));
            // 遍历去重集合，查询活动数据
            collect.forEach(orderActivity -> {
                // TODO: 2020/2/24 目前只存在满减活动，增加活动新增条件
                if (ActivityTypeEnum.REDUCE_ACTIVITY.value() == orderActivity.getActivityType()) {
                    // 3.查询满减活动
                    ReduceActivityDTO reduceActivity = reduceActivityService.get(orderActivity.getActivityId());
                    ReduceRuleDTO reduceRule = reduceRuleService.get(orderActivity.getRuleId());
                    // 4.封装前台活动数据
                    AdminOrderActivityDTO adminOrderActivity = new AdminOrderActivityDTO();
                    BeanCopier.create(ReduceActivityDTO.class, AdminOrderActivityDTO.class, false).copy(reduceActivity, adminOrderActivity, null);
                    adminOrderActivity.setLimitAmount(reduceRule.getLimitAmount());
                    adminOrderActivity.setReduceAmount(reduceRule.getReduceAmount());
                    adminOrderActivity.setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                    adminOrderActivityList.add(adminOrderActivity);
                }
            });
            adminOrderActivityDetail.setAdminOrderActivityList(adminOrderActivityList);
        }

        return adminOrderActivityDetail;
    }

    /**
     * 功能描述:
     * 〈封装操作时间集合〉
     *
     * @param orderEntity 订单信息
     * @author : 刘远杰
     */
    private List<AdminOrderTimeDTO> createOrderTimeList(OrderEntity orderEntity) {
        List<AdminOrderTimeDTO> orderTimeDTOList = new ArrayList<>();
        if (OrderStatusEnum.CANCELED.value() == orderEntity.getOrderStatus()) {
            // 已取消订单
            if (PaymentStatusEnum.NO.value() == orderEntity.getPaymentStatus() || orderEntity.getPaymentStatus() == null) {
                // 未支付取消的订单
                AdminOrderTimeDTO orderTimeDTO = new AdminOrderTimeDTO();
                orderTimeDTO.setOrderOperation("提交订单");
                orderTimeDTO.setOperationDate(orderEntity.getCreateDate());
                orderTimeDTOList.add(orderTimeDTO);

                AdminOrderTimeDTO orderTimeDTO1 = new AdminOrderTimeDTO();
                orderTimeDTO1.setOrderOperation("用户取消");
                orderTimeDTO1.setOperationDate(orderEntity.getCancelDate());
                orderTimeDTOList.add(orderTimeDTO1);
            } else {
                // 已支付取消的订单
                AdminOrderTimeDTO orderTimeDTO = new AdminOrderTimeDTO();
                orderTimeDTO.setOrderOperation("提交订单");
                orderTimeDTO.setOperationDate(orderEntity.getCreateDate());
                orderTimeDTOList.add(orderTimeDTO);

                AdminOrderTimeDTO orderTimeDTO1 = new AdminOrderTimeDTO();
                orderTimeDTO1.setOrderOperation("付款成功");
                orderTimeDTO1.setOperationDate(orderEntity.getPaymentTime());
                orderTimeDTOList.add(orderTimeDTO1);

                AdminOrderTimeDTO orderTimeDTO2 = new AdminOrderTimeDTO();
                orderTimeDTO2.setOrderOperation("商家取消");
                orderTimeDTO2.setOperationDate(orderEntity.getCancelDate());
                orderTimeDTOList.add(orderTimeDTO2);
            }
        } else {
            // 未取消订单
            if (DevlierTypeEnum.EXPRESS.value().equals(orderEntity.getDevlierType()) || orderEntity.getDevlierType() == null) {
                // 快递
                AdminOrderTimeDTO orderTimeDTO = new AdminOrderTimeDTO();
                orderTimeDTO.setOrderOperation("提交订单");
                orderTimeDTO.setOperationDate(orderEntity.getCreateDate());
                orderTimeDTOList.add(orderTimeDTO);

                AdminOrderTimeDTO orderTimeDTO1 = new AdminOrderTimeDTO();
                orderTimeDTO1.setOrderOperation("付款成功");
                orderTimeDTO1.setOperationDate(orderEntity.getPaymentTime());
                orderTimeDTOList.add(orderTimeDTO1);

                AdminOrderTimeDTO orderTimeDTO2 = new AdminOrderTimeDTO();
                orderTimeDTO2.setOrderOperation("商品出库");
                orderTimeDTO2.setOperationDate(orderEntity.getTransportTime());
                orderTimeDTOList.add(orderTimeDTO2);

                AdminOrderTimeDTO orderTimeDTO3 = new AdminOrderTimeDTO();
                orderTimeDTO3.setOrderOperation("待收货");
                orderTimeDTOList.add(orderTimeDTO3);

                AdminOrderTimeDTO orderTimeDTO4 = new AdminOrderTimeDTO();
                orderTimeDTO4.setOrderOperation("完成");
                orderTimeDTO4.setOperationDate(orderEntity.getCompleteTime());
                orderTimeDTOList.add(orderTimeDTO4);
            } else if (DevlierTypeEnum.SELF_MENTION.value().equals(orderEntity.getDevlierType())
                    || DevlierTypeEnum.NO_LOGISTICS.value().equals(orderEntity.getDevlierType())) {
                // 自提
                AdminOrderTimeDTO orderTimeDTO = new AdminOrderTimeDTO();
                orderTimeDTO.setOrderOperation("提交订单");
                orderTimeDTO.setOperationDate(orderEntity.getCreateDate());
                orderTimeDTOList.add(orderTimeDTO);

                AdminOrderTimeDTO orderTimeDTO1 = new AdminOrderTimeDTO();
                orderTimeDTO1.setOrderOperation("付款成功");
                orderTimeDTO1.setOperationDate(orderEntity.getPaymentTime());
                orderTimeDTOList.add(orderTimeDTO1);

                AdminOrderTimeDTO orderTimeDTO2 = new AdminOrderTimeDTO();
                orderTimeDTO2.setOrderOperation("待自提");
                orderTimeDTOList.add(orderTimeDTO2);

                AdminOrderTimeDTO orderTimeDTO3 = new AdminOrderTimeDTO();
                orderTimeDTO3.setOrderOperation("完成");
                orderTimeDTO3.setOperationDate(orderEntity.getCompleteTime());
                orderTimeDTOList.add(orderTimeDTO3);
            } else if (DevlierTypeEnum.FETCH_CODE.value().equals(orderEntity.getDevlierType())) {
                // 电子提货码
                AdminOrderTimeDTO orderTimeDTO = new AdminOrderTimeDTO();
                orderTimeDTO.setOrderOperation("提交订单");
                orderTimeDTO.setOperationDate(orderEntity.getCreateDate());
                orderTimeDTOList.add(orderTimeDTO);

                AdminOrderTimeDTO orderTimeDTO1 = new AdminOrderTimeDTO();
                orderTimeDTO1.setOrderOperation("付款成功");
                orderTimeDTO1.setOperationDate(orderEntity.getPaymentTime());
                orderTimeDTOList.add(orderTimeDTO1);

                AdminOrderTimeDTO orderTimeDTO2 = new AdminOrderTimeDTO();
                orderTimeDTO2.setOrderOperation("生成电子提货码");
                orderTimeDTOList.add(orderTimeDTO2);

                AdminOrderTimeDTO orderTimeDTO3 = new AdminOrderTimeDTO();
                orderTimeDTO3.setOrderOperation("完成");
                orderTimeDTO3.setOperationDate(orderEntity.getCompleteTime());
                orderTimeDTOList.add(orderTimeDTO3);
            }
        }
        return orderTimeDTOList;
    }

    /**
     * 功能描述:
     * 〈保存订单〉
     *
     * @param dto 订单实体
     * @author : 刘远杰
     */

    @Override
    public void save(@RequestBody OrderDTO dto) {
        super.save(dto);
    }

    /**
     * 功能描述:
     * 〈查询某支付状态订单数量〉
     *
     * @param buyerId     会员id
     * @param orderStatus 订单状态
     * @author : 刘远杰
     */

    @Override
    public int countOrder(Integer orderStatus, @RequestParam("buyerId") Long buyerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("buyerId", buyerId);
        params.put("orderStatus", orderStatus);
        return baseDao.countOrder(params);
    }

    /**
     * 功能描述:
     * 〈会员中心订单数量查询〉
     *
     * @param buyerId 会员id
     * @author : 刘远杰
     */

    @Override
    public MemberOrderCountDTO countMemberOrder(@RequestParam("buyerId") Long buyerId) {
        return baseDao.countMemberOrder(buyerId);
    }


    /**
     * 功能描述:
     * 〈立即购买提交订单〉
     * 不支持整体事务，保存方法单独进行事务控制，解决确认表保存完成事务未结束mq执行消息查不到确认表数据
     *
     * @param dto 订单保存实体
     * @author : 刘远杰
     */
    @Override

    @Transactional(propagation = Propagation.SUPPORTS)
    //@GlobalTransactional(rollbackFor = Exception.class)
    @Lock4j(keys = "#buyerId", timeout = 10)
    public OrderSaveResultDTO saveOrderNow(@RequestBody InsertNowOrderVoDTO dto,
                                           @RequestParam("buyerId") Long buyerId) {
        log.info("立即购买提交订单参数，orderDTO:{}， buyerId;{}", dto, buyerId);
        OrderSaveResultDTO orderSaveResultDTO = new OrderSaveResultDTO();
        List<ErrorOrderMsgDTO> errorOrderMsgDTOList = new ArrayList<>();

        Map<String, String> logMap = new HashMap<>();
        logMap.put("orderDTO", dto.toString());
        logMap.put("buyerId", buyerId.toString());

        // 会员等级限制
        MemberVoDTO memberVo = memberService.selectMemberById(buyerId);
        dto.setMemberVoDTO(memberVo);

        MemberGradeDTO memberGradeDTO =
                memberGradeService.selectByMemberId(memberVo.getMemberInfoDTO().getGradePoint());
        dto.setMemberGradeDTO(memberGradeDTO);

        // 校验活动状态、商品库存
        if (null != dto.getActivityId() && ActivityTypeEnum.NO_ACTIVITY.value() != dto.getActivityType()) {
            Boolean rheckResult = this.checkActivityStatusAndGoods(dto, buyerId, orderSaveResultDTO, errorOrderMsgDTOList, logMap);
            if (!rheckResult) {
                //活动信息校验是失败
                return orderSaveResultDTO;
            }
        }
        // 封装并保存订单确认信息，订单商品确认信息
        OrderConfirmDTO orderConfirmDTO = this.saveNowOrderConfirm(dto, buyerId);

        if (orderConfirmDTO != null) {
            // redis记录订单保存
            String key = RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId();
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.WAITING);
            saveOrderRedisDTO.setResultMsg("订单保存中");
            redisUtils.set(key, saveOrderRedisDTO, RedisConstants.JEDIS_EXPIRE);

            // 发送消息到mq执行订单保存操作
            log.info("发送mq消息保存订单，订单id:{}", orderConfirmDTO.getId());
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CONFIRM_ORDER_QUEUE, orderConfirmDTO.getId().toString());

            mlogger.info(OrderStatusCode.ORDER_BUYNOW_SUCCESS_CODE, OrderStatusCode.ORDER_BUYNOW_SUCCESS_MESSAGE,
                    logMap);

            orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.ALL_CHECK_SUCCESS.value());
            orderSaveResultDTO.setOrderId(orderConfirmDTO.getId());
            return orderSaveResultDTO;
        } else {
            mlogger.info(OrderStatusCode.BUYNOW_CONFIRM_SAVE_FAIL.getCode(),
                    OrderStatusCode.BUYNOW_CONFIRM_SAVE_FAIL.getMessage(), logMap);
            log.info("立即购买提交订单失败，保存订单确认表及商品确认表错误");
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_UNKNOWN_ERROR);
        }
    }

    /**
     * 校验活动状态和库存信息
     *
     * @param dto                  订单保存对象
     * @param buyerId              购买人ID
     * @param orderSaveResultDTO   订单保存结果
     * @param errorOrderMsgDTOList 异常订单提示
     * @param logMap               日志参数
     * @return 执行状态
     */
    private Boolean checkActivityStatusAndGoods(InsertNowOrderVoDTO dto, Long buyerId, OrderSaveResultDTO orderSaveResultDTO, List<ErrorOrderMsgDTO> errorOrderMsgDTOList, Map<String, String> logMap) {
        //校验秒杀活动状态和库存信息
        if (dto.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value()) {
            return this.getSeckillActivity(dto, buyerId, orderSaveResultDTO, errorOrderMsgDTOList, logMap);
        }
        //校验限时抢购活动状态和库存信息
        if (dto.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()) {
            return this.getFlashSaleActivity(dto, buyerId, orderSaveResultDTO, errorOrderMsgDTOList, logMap);
        }
        return true;
    }

    /**
     * 校验限时抢购活动状态和库存信息
     *
     * @param dto                  订单保存对象
     * @param buyerId              购买人ID
     * @param orderSaveResultDTO   订单保存结果
     * @param errorOrderMsgDTOList 异常订单提示
     * @param logMap               日志参数
     * @return 执行状态
     */
    private Boolean getFlashSaleActivity(InsertNowOrderVoDTO dto, Long buyerId, OrderSaveResultDTO orderSaveResultDTO, List<ErrorOrderMsgDTO> errorOrderMsgDTOList, Map<String, String> logMap) {
        MemberVoDTO memberVo = dto.getMemberVoDTO();
        // 查询限时购活动
        FlashSaleActivityDTO flashSaleActivityDTO = flashSaleActivityService.get(dto.getActivityId());

        //限时购活动校验
        if (flashSaleActivityDTO == null) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_NO_FLASH_SALE_ACTIVITY.getCode(),
                    OrderStatusCode.SAVE_ORDER_NO_FLASH_SALE_ACTIVITY.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品限时抢购活动不存在");
            return false;
        } else if (flashSaleActivityDTO.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value()) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_FLASH_SALE_ACTIVITY_PREPARE.getCode(),
                    OrderStatusCode.SAVE_ORDER_FLASH_SALE_ACTIVITY_PREPARE.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品限时抢购未开始");
            return false;
        } else if (flashSaleActivityDTO.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_END.value()) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_FLASH_SALE_ACTIVITY_END.getCode(),
                    OrderStatusCode.SAVE_ORDER_FLASH_SALE_ACTIVITY_END.getMessage(), logMap);
            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品限时抢购活动已结束");
            return false;
        }

        // 校验限时购商品价格
        String priceKey = ActivityRedisConstants.FLASH_GOODS_PRICE + dto.getActivityId() + "_" + dto.getSpecId();
        Object priceObj = redisUtils.get(priceKey);
        if (priceObj == null) {
            // 获取活动库存异常
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_PRICE_ERROR.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_PRICE_ERROR.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_PRICE_ERROR);
        }

        BigDecimal activityPrice = new BigDecimal(priceObj.toString());
        if (activityPrice.compareTo(dto.getSpecSellPrice()) != 0) {
            mlogger.info(OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE.getCode(),
                    OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品价格已发生变化");
            return false;
        }

        // 会员等级限制
        if (flashSaleActivityDTO.getMemberGradeLimit() != null) {
            if (memberVo.getMemberInfoDTO().getGradePoint() < flashSaleActivityDTO.getMemberGradeLimit()) {
                mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_LESS_MEMBER_GRADE.getCode(),
                        OrderStatusCode.SAVE_ORDER_ACTIVITY_LESS_MEMBER_GRADE.getMessage(), logMap);

                // 封装错误提示信息
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品未满足会员参与等级");
                return false;
            }
        }

        // 活动限购
        Integer buyNum = this.countFlashSaleOrderGoodsNum(dto.getActivityId(), dto.getSpecId(), buyerId);
        if (flashSaleActivityDTO.getRestrictionQuantity() != null && flashSaleActivityDTO.getRestrictionQuantity() != 0
                && buyNum + dto.getGoodsNum() > flashSaleActivityDTO.getRestrictionQuantity()) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_LIMIT_OVER.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_LIMIT_OVER.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品已超出活动限购数量");
            return false;
        }

        //查询限时购活动库存
        String storageKey =
                ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + dto.getActivityId() + "_" + dto.getSpecId();
        Object storageObj = redisUtils.get(storageKey);
        if (storageObj == null) {
            // 获取活动库存异常
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR);
        }

        Integer activitySurplusStorage = Integer.parseInt(storageObj.toString());
        if (dto.getGoodsNum() > activitySurplusStorage) {
            // 活动库存不足
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_LACK.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_LACK.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品活动库存不足");
            return false;
        }

        try {
            // 扣减限时购库存
            redisUtils.decrement(storageKey, dto.getGoodsNum());
        } catch (Exception e) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR.getCode(),
                    OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR);
        }
        return true;
    }

    /**
     * 校验秒杀活动状态和库存信息
     *
     * @param dto                  订单保存对象
     * @param buyerId              购买人ID
     * @param orderSaveResultDTO   订单保存结果
     * @param errorOrderMsgDTOList 异常订单提示
     * @param logMap               日志参数
     * @return 执行状态
     */
    private Boolean getSeckillActivity(InsertNowOrderVoDTO dto, Long buyerId, OrderSaveResultDTO orderSaveResultDTO, List<ErrorOrderMsgDTO> errorOrderMsgDTOList, Map<String, String> logMap) {
        MemberVoDTO memberVo = dto.getMemberVoDTO();
        // 查询秒杀活动
        SeckillActivityDTO seckillActivityDTO = seckillActivityService.get(dto.getActivityId());

        //秒杀活动校验
        if (seckillActivityDTO == null) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_NO_SECKILL_ACTIVITY.getCode(),
                    OrderStatusCode.SAVE_ORDER_NO_SECKILL_ACTIVITY.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品秒杀活动不存在");
            return false;
        } else if (seckillActivityDTO.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value()) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_PREPARE.getCode(),
                    OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_PREPARE.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品秒杀活动未开始");
            return false;
        } else if (seckillActivityDTO.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_END.value()) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_END.getCode(),
                    OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_END.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品秒杀活动已结束");
            return false;
        }

        // 校验秒杀商品价格
        String priceKey = ActivityRedisConstants.SECKILL_GOODS_PRICE + dto.getActivityId() + "_" + dto.getSpecId();
        Object priceObj = redisUtils.get(priceKey);
        if (priceObj == null) {
            // 获取活动库存异常
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_PRICE_ERROR.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_PRICE_ERROR.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_PRICE_ERROR);
        }

        BigDecimal activityPrice = new BigDecimal(priceObj.toString());
        if (activityPrice.compareTo(dto.getSpecSellPrice()) != 0) {
            mlogger.info(OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE.getCode(),
                    OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品价格已发生变化");
            return false;
        }

        // 会员等级限制
        if (seckillActivityDTO.getMemberGradeLimit() != null) {
            if (memberVo.getMemberInfoDTO().getGradePoint() < seckillActivityDTO.getMemberGradeLimit()) {
                mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_LESS_MEMBER_GRADE.getCode(),
                        OrderStatusCode.SAVE_ORDER_ACTIVITY_LESS_MEMBER_GRADE.getMessage(), logMap);

                // 封装错误提示信息
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品未满足会员参与等级");
                return false;
            }
        }

        // 活动限购
        Integer buyNum = this.countSeckillOrderGoodsNum(dto.getActivityId(), dto.getSpecId(), buyerId);
        if (seckillActivityDTO.getRestrictionQuantity() != null && seckillActivityDTO.getRestrictionQuantity() != 0
                && buyNum + dto.getGoodsNum() > seckillActivityDTO.getRestrictionQuantity()) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_LIMIT_OVER.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_LIMIT_OVER.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品已超出活动限购数量");
            return false;
        }

        //查询秒杀活动库存
        String storageKey =
                ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + dto.getActivityId() + "_" + dto.getSpecId();
        Object storageObj = redisUtils.get(storageKey);
        if (storageObj == null) {
            // 获取活动库存异常
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR);
        }

        Integer activitySurplusStorage = Integer.parseInt(storageObj.toString());
        if (dto.getGoodsNum() > activitySurplusStorage) {
            // 活动库存不足
            mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_LACK.getCode(),
                    OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_LACK.getMessage(), logMap);

            // 封装错误提示信息
            this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                    SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品活动库存不足");
            return false;
        }

        try {
            // 扣减秒杀库存
            redisUtils.decrement(storageKey, dto.getGoodsNum());
        } catch (Exception e) {
            mlogger.info(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR.getCode(),
                    OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR);
        }
        return true;
    }

    /**
     * 拼团保存订单
     *
     * @param dto     提交订单实体
     * @param buyerId 会员id
     * @return
     * @date 2020-03-23 10:27
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    @Transactional(propagation = Propagation.SUPPORTS)
    //@GlobalTransactional(rollbackFor = Exception.class)
    public OrderSaveResultDTO saveGroupOrderNow(@RequestBody InsertNowOrderVoDTO dto,
                                                @RequestParam("buyerId") Long buyerId) {
        log.info("拼团立即购买提交订单参数，orderDTO:{}， buyerId;{}", dto, buyerId);

        OrderSaveResultDTO orderSaveResultDTO = new OrderSaveResultDTO();
        List<ErrorOrderMsgDTO> errorOrderMsgDTOList = new ArrayList<>();

        Map<String, String> logMap = new HashMap<>();
        logMap.put("orderDTO", dto.toString());
        logMap.put("buyerId", buyerId.toString());

        // 校验拼团活动状态、商品库存
        if (dto.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value() && dto.getActivityId() != null) {
            // 查询拼团活动
            GroupDTO groupDTO = groupService.get(dto.getActivityId());

            //拼团活动校验
            if (groupDTO == null) {
                mlogger.info(OrderStatusCode.SAVE_ORDER_NO_GROUP_ACTIVITY.getCode(),
                        OrderStatusCode.SAVE_ORDER_NO_GROUP_ACTIVITY.getMessage(), logMap);

                // 封装错误提示信息
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品拼团活动不存在");
                return orderSaveResultDTO;
            } else if (groupDTO.getActivityStatus() == GroupsEnum.ACTIVITY_STATUS_NO.value()) {
                mlogger.info(OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_PREPARE.getCode(),
                        OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_PREPARE.getMessage(), logMap);

                // 封装错误提示信息
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品拼团活动未开始");
                return orderSaveResultDTO;
            } else if (groupDTO.getActivityStatus() == GroupsEnum.ACTIVITY_STATUS_END.value()) {
                mlogger.info(OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_END.getCode(),
                        OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_END.getMessage(), logMap);

                // 封装错误提示信息
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品拼团活动已结束");
                return orderSaveResultDTO;
            }

            if (null != dto.getGroupRecordId()) {
                // 校验拼团记录的成团人数
                GroupRecordDTO groupRecordDTO = groupRecordService.get(dto.getGroupRecordId());
                if (null == groupRecordDTO) {
                    mlogger.info(OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_RECORD_ERROR.getCode(),
                            OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_RECORD_ERROR.getMessage(), logMap);

                    // 封装错误提示信息
                    this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                            SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品拼团记录不存在");
                    return orderSaveResultDTO;
                } else {
                    // 校验拼团记录状态是否为失败
                    if (groupRecordDTO.getGroupStatus() == GroupsEnum.GROUP_STATUS_FAIL.value()) {
                        mlogger.info(OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_RECORD_FAIL.getCode(),
                                OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_RECORD_FAIL.getMessage(), logMap);

                        // 封装错误提示信息
                        this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品拼团已失败");
                        return orderSaveResultDTO;
                    }

                    // 需要拼团人数为0则拼团已满员
                    if (groupRecordDTO.getNeedNum() == 0) {
                        mlogger.info(OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_PEOPLE_OVER.getCode(),
                                OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_PEOPLE_OVER.getMessage(), logMap);

                        // 封装错误提示信息
                        this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品拼团人数已满");
                        return orderSaveResultDTO;
                    }

                    // 校验成团超时时间
                    if (groupRecordDTO.getOverTime().compareTo(new Date()) == -1) {
                        mlogger.info(OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_OVER_TIME.getCode(),
                                OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_OVER_TIME.getMessage(), logMap);

                        // 封装错误提示信息
                        this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品超出拼团成团时间");
                        return orderSaveResultDTO;
                    }

                    // 查询用户是否参加过本次拼团
                    Map<String, Object> queryMap = new HashMap<>();
                    queryMap.put("storeId", groupRecordDTO.getStoreId());
                    queryMap.put("groupRecordId", groupRecordDTO.getId());
                    queryMap.put("memberId", buyerId);
                    List<GroupOrderDetailDTO> groupOrderDetailDTOList = this.findGroupOrderList(queryMap);
                    if (groupOrderDetailDTOList.size() > 0) {
                        mlogger.info(OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_OVER_JOIN.getCode(),
                                OrderStatusCode.SAVE_ORDER_GROUP_ACTIVITY_OVER_JOIN.getMessage(), logMap);
                        // 封装错误提示信息
                        this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                                SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您已参加过此拼团");
                        return orderSaveResultDTO;
                    }
                }

            }

            // 查询此拼团商品的数据
            ActivityGoodsDTO activityGoodsDTO = activityGoodsService.goodsAndSpec(dto.getActivityId(),
                    dto.getSpecId(), dto.getActivityType());

            // 校验规格
            if (activityGoodsDTO == null) {
                // 商品不存在
                throw new ServiceException(CartStatusCode.SETTLEMENT_GOODS_NOTFOUND_ERROR);
            }
            if (activityGoodsDTO.getActivitySurplusStorage() == 0) {
                // 商品售罄
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品暂无库存");
                return orderSaveResultDTO;
            }

            // 查询该拼团商品的单次购买数量限制
            if ((dto.getGoodsNum() > activityGoodsDTO.getOnceBuyLimit()) && activityGoodsDTO.getOnceBuyLimit() > 0) {
                // 封装错误提示信息
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品已超出活动限购数俩");
                return orderSaveResultDTO;
            }

            // 查询用户的参加拼团记录
            List<GroupRecordDTO> groupRecordDTOList = groupRecordService.getMemberRecord(buyerId, dto.getActivityId()
                    , activityGoodsDTO.getGoodsId());

            if (CollectionUtils.isNotEmpty(groupRecordDTOList)) {
                // 校验用户参加拼团活动次数
                if ((dto.getGoodsNum() + groupRecordDTOList.size() > activityGoodsDTO.getJoinLimit()) && activityGoodsDTO.getJoinLimit() > 0) {
                    this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                            SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品已超出拼团活动参团次数");
                    return orderSaveResultDTO;
                }
            }

            //查询拼团活动库存
            String storageKey =
                    ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + dto.getActivityId() + "_" + dto.getSpecId();
            Object storageObj = redisUtils.get(storageKey);

            if (storageObj == null) {
                // 获取活动库存异常
                mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR.getCode(),
                        OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR.getMessage(), logMap);
                throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR);
            }

            Integer activitySurplusStorage = Integer.parseInt(storageObj.toString());
            if (dto.getGoodsNum() > activitySurplusStorage) {
                // 活动库存不足
                mlogger.info(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_LACK.getCode(),
                        OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_LACK.getMessage(), logMap);

                // 封装错误提示信息
                this.createBuynowGoodsErrorMsg(dto, orderSaveResultDTO, errorOrderMsgDTOList,
                        SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value(), "抱歉，您购买的以下商品活动库存不足");
                return orderSaveResultDTO;
            }

            try {
                // 扣减拼团库存
                redisUtils.decrement(storageKey, dto.getGoodsNum());
            } catch (Exception e) {
                mlogger.error(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR.getCode(),
                        OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR.getMessage(), e);
                throw new ServiceException(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR);
            }

        }

        MemberVoDTO memberVo = memberService.selectMemberById(buyerId);
        dto.setMemberVoDTO(memberVo);

        MemberGradeDTO memberGradeDTO =
                memberGradeService.selectByMemberId(memberVo.getMemberInfoDTO().getGradePoint());
        dto.setMemberGradeDTO(memberGradeDTO);

        // 封装并保存订单确认信息，订单商品确认信息(抽取和普通订单走一个方法20200507)
        OrderConfirmDTO orderConfirmDTO = this.saveNowOrderConfirm(dto, buyerId);

        // redis记录订单保存
        String key = RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId();
        SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
        saveOrderRedisDTO.setResultCode(RedisConstants.WAITING);
        saveOrderRedisDTO.setResultMsg("订单保存中");
        redisUtils.set(key, saveOrderRedisDTO, RedisConstants.JEDIS_EXPIRE);

        // 发送消息到mq执行订单保存操作
        log.info("发送mq消息保存订单，订单id:{}", orderConfirmDTO.getId());
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CONFIRM_GROUP_ORDER_QUEUE,
                orderConfirmDTO.getId().toString());

        mlogger.info(OrderStatusCode.ORDER_BUYNOW_SUCCESS_CODE, OrderStatusCode.ORDER_BUYNOW_SUCCESS_MESSAGE, logMap);

        orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.ALL_CHECK_SUCCESS.value());
        orderSaveResultDTO.setOrderId(orderConfirmDTO.getId());
        return orderSaveResultDTO;
    }

    /**
     * @Author weixianchun
     * @Description 保存商家备注信息
     * @Param dto
     * @Param buyerId
     * @Date 2019/11/11 10:07
     * @Return java.lang.Long
     * @version 1.0
     */

    @Override
    public void updateSellerRemark(@RequestBody OrderSellerRemarkDTO dto) {
        OrderDTO orderDTO = ConvertUtils.sourceToTarget(dto, OrderDTO.class);
        super.update(orderDTO);
    }

    /**
     * 根据orderSN更新订单余额信息
     *
     * @param orderDTO 订单信息
     * @date 2020-07-09 20:11
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void putOrderDTO(@RequestBody OrderDTO orderDTO) {
        OrderEntity orderEntity = baseDao.selectOne(Wrappers.<OrderEntity>lambdaQuery().eq(OrderEntity::getOrderSn,
                orderDTO.getOrderSn()));
        if (!BeanUtil.isEmpty(orderEntity)) {
//            OrderEntity entity = ConvertUtils.sourceToTarget(orderDTO, OrderEntity.class);
//            entity.setId(orderEntity.getId());
//            baseDao.updateById(entity);
            BigDecimal refundAmount = orderEntity.getBalanceRefundAmount().add(orderDTO.getBalanceRefundAmount());
            super.update(Wrappers.<OrderEntity>lambdaUpdate()
                    .set(OrderEntity::getBalanceRefundAmount, refundAmount)
                    .eq(OrderEntity::getId, orderEntity.getId()));
            log.info("订单信息{},修改信息:{}", orderEntity, refundAmount);
            return;
        }
        log.error("订单信息未更新，入参：{}，查询订单结果：{}", orderDTO, orderEntity);

    }


    @Override
    public void updateByInvoiceFlag(@RequestBody OrderDTO dto) {
        baseDao.update(null, Wrappers.<OrderEntity>lambdaUpdate().set(OrderEntity::getInvoiceFlag,
                dto.getInvoiceFlag()).eq(OrderEntity::getId, dto.getId()));
    }


    @Override
    public QueryWrapper<OrderEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * @param params:查询参数
     * @Author: SWH ab4856812@163.com
     * @Description:订单可申请售后服务列表
     * @Date: 2019/6/17 19:39
     * @Version: V1.0
     */

    @Override
    public PageData<AvailableAfterSaleOrderDTO> findAvailAfterList(@RequestParam Map<String, Object> params) {
        //根据条件查询对应的列表数据
        IPage page = getPage(params, Constant.CREATE_DATE, false);
        List<AvailableAfterSaleOrderDTO> orderList = baseDao.findAvailAfterList(page, params);

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
        if (CollectionUtils.isNotEmpty(orderList)) {
            for (AvailableAfterSaleOrderDTO availableAfterSaleOrderDTO : orderList) {
                Date completeTime = availableAfterSaleOrderDTO.getCompleteTime();
                AftersaleSettingDTO aftersaleSettingDTO = new AftersaleSettingDTO();
                aftersaleSettingDTO.setCannotReturn(senior.getCannotReturn());
                aftersaleSettingDTO.setCannotBarter(senior.getCannotBarter());
                aftersaleSettingDTO.setGoodsReturn(aftersaleDTO.getGoodsReturn());
                aftersaleSettingDTO.setGoodsBarter(aftersaleDTO.getGoodsBarter());
                aftersaleSettingDTO.setCompleteTime(completeTime);
                if (CollectionUtils.isNotEmpty(availableAfterSaleOrderDTO.getOrderGoodsList())) {
                    AvailableAfterSaleOrderGoodsDTO dto = availableAfterSaleOrderDTO.getOrderGoodsList().get(0);
                    GoodsDTO goodsDTO = goodsService.get(dto.getGoodsId());
                    // 提货码过期退款（0不支持，1支持）
                    if (!BeanUtil.isEmpty(goodsDTO) || null == goodsDTO.getCodeRefundFlag() || goodsDTO.getCodeRefundFlag() == 0) {
//                        log.info("提货码过期不支持退款");
                        availableAfterSaleOrderDTO.setIsOutTime(AfterSaleEnum.DEFAULTONE.value());
                    }
                }
                AftersaleAvailTypeDTO aftersaleAvailTypeDTO = orderGoodsService.findAvailType(aftersaleSettingDTO);
                if (null == aftersaleAvailTypeDTO) {
                    return null;
                }
                if (aftersaleAvailTypeDTO.getIsBarter().equals(AfterSaleEnum.DEFAULT.value())
                        && aftersaleAvailTypeDTO.getIsReturn().equals(AfterSaleEnum.DEFAULT.value())) {
                    availableAfterSaleOrderDTO.setIsOutTime(AfterSaleEnum.DEFAULTONE.value());
                } else {
                    availableAfterSaleOrderDTO.setIsOutTime(AfterSaleEnum.DEFAULT.value());
                }
            }
        }
        return new PageData<>(orderList, page.getTotal());
    }

    /**
     * @param orderId:订单id
     * @Author: SWH ab4856812@163.com
     * @Description:根据订单号查询订单完成时间
     * @Date: 2019/6/17 19:39
     * @Version: V1.0
     */

    @Override
    public String findCompleteTimeById(@RequestParam("orderId") Long orderId) {
        return baseDao.findCompleteTimeById(orderId);
    }

    /**
     * 功能描述:
     * 〈保存立即购买订单确认表，订单商品确认表〉
     *
     * @param dto     立即购买提交订单参数实体
     * @param buyerId 买家id
     * @return : void
     * @author : 刘远杰
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public OrderConfirmDTO saveNowOrderConfirm(@RequestBody InsertNowOrderVoDTO dto,
                                               @RequestParam("buyerId") Long buyerId) {
        OrderConfirmDTO orderConfirmDTO = new OrderConfirmDTO();
        OrderGoodsConfirmDTO orderGoodsConfirmDTO = new OrderGoodsConfirmDTO();

        // 用户信息
        MemberVoDTO memberVo = dto.getMemberVoDTO();
        MemberGradeDTO memberGradeDTO = dto.getMemberGradeDTO();

        // 封装订单确认表数据
        orderConfirmDTO.setId(IdGenerator.defaultSnowflakeId());
        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put(dto.getStoreId() + "", dto.getOrderMessage());
        // map转字符串
        List<Map<String, Object>> orderMessageList = new ArrayList<>();
        orderMessageList.add(orderMessage);
        String orderMessageStr = JSON.toJSONString(orderMessageList);
        orderConfirmDTO.setOrderMessage(orderMessageStr);
        orderConfirmDTO.setAddressId(dto.getAddressId());
        orderConfirmDTO.setStoreId(dto.getStoreId());
        orderConfirmDTO.setStoreName(dto.getStoreName());
        orderConfirmDTO.setOrderPlatform(dto.getOrderPlatform());
        orderConfirmDTO.setBuyerId(buyerId);
        orderConfirmDTO.setBuyerName(memberVo.getMemberName());
        orderConfirmDTO.setBuyerEmail(memberVo.getMemberEmail());
        orderConfirmDTO.setVirtualFlag(dto.getVirtualFlag());
        orderConfirmDTO.setVirtualCustomer(dto.getMemberName());
        orderConfirmDTO.setVirtualPhone(dto.getMemberMobile());

        // 设置用户等级信息
        if (memberGradeDTO != null) {
            orderConfirmDTO.setBuyerGraderId(memberGradeDTO.getId());
            orderConfirmDTO.setBuyerGraderName(memberGradeDTO.getGradeName());
        }

        // 运费
        Map<Long, BigDecimal> shippingAmount = new HashMap<>(1);
        shippingAmount.put(dto.getStoreId(), dto.getShippingFee());
        orderConfirmDTO.setShippingFee(dto.getShippingFee());
        orderConfirmDTO.setShippingAmount(JSONObject.toJSONString(shippingAmount));

        //计算订单商品总价
        orderConfirmDTO.setPreferentialPrice(BigDecimal.ZERO);

        // 商品价格=商品单价*购买数量
        BigDecimal goodsAmount = dto.getSpecSellPrice().multiply(BigDecimal.valueOf(dto.getGoodsNum()));
        // 订单总价=商品价格-优惠金额+运费
        BigDecimal orderAmount =
                goodsAmount.subtract(orderConfirmDTO.getPreferentialPrice()).add(orderConfirmDTO.getShippingFee());
        BigDecimal maxOrderAmount = new BigDecimal(10000000);
        if (orderAmount.compareTo(maxOrderAmount) == 1) {
            throw new ServiceException(OrderStatusCode.ORDER_AMOUNT_OUT_MAX_PRICE);
        }

        orderConfirmDTO.setGoodsAmount(goodsAmount);
        orderConfirmDTO.setOrderAmount(orderAmount);

        // 判断使用余额支付的话
        if (null != dto.getUseBalance()) {
            if (CartEnum.SETTMENT_USE_BALANCE.value() == dto.getUseBalance()) {
                // 查询用户可用余额
                MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(buyerId);

                if (null != memberBalanceInfoDTO) {
                    BigDecimal difference = orderAmount.subtract(memberBalanceInfoDTO.getAvailableBalance());

                    // 余额足够支付订单,则订单确认表的余额支付金额为订单的支付总金额，否则为用户的可用余额
                    if (difference.compareTo(BigDecimal.ZERO) < 1) {
                        orderConfirmDTO.setBalanceAmount(orderAmount);
                    } else {
                        orderConfirmDTO.setBalanceAmount(memberBalanceInfoDTO.getAvailableBalance());
                    }
                }

            }
        }

        if (dto.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value() && dto.getActivityId() != null) {
            // 拼团
            orderGoodsConfirmDTO.setActivityId(dto.getActivityId());
            orderGoodsConfirmDTO.setActivityType(dto.getActivityType());
            orderGoodsConfirmDTO.setActivityRecordId(dto.getGroupRecordId());
        } else if (dto.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value() && dto.getActivityId() != null) {
            // 秒杀
            orderGoodsConfirmDTO.setActivityId(dto.getActivityId());
            orderGoodsConfirmDTO.setActivityType(dto.getActivityType());
        } else if (dto.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() && dto.getActivityId() != null) {
            // 限时购
            orderGoodsConfirmDTO.setActivityId(dto.getActivityId());
            orderGoodsConfirmDTO.setActivityType(dto.getActivityType());
            FlashSaleActivityDTO flashSaleActivityDTO = Optional.of(flashSaleActivityService.get(dto.getActivityId())).orElse(new FlashSaleActivityDTO());
            //优惠券
            if (flashSaleActivityDTO.getCouponsFlag() == FlashSaleActivityEnum.COUPONS_FLAG_CAN.value()) {
                Map<String, Long> memberCouponsMap = new HashMap<>();
                memberCouponsMap.put(dto.getStoreId().toString(), dto.getMemberCouponsId());
                log.info("提交订单选择优惠券：{}", memberCouponsMap);
                orderConfirmDTO.setMemberCouponsId(JSONObject.toJSONString(memberCouponsMap));
//                orderGoodsConfirmDTO.setActivityType(ActivityTypeEnum.COUPONS_ACTIVITY.value());
            }

        } else {
            // 非秒杀商品
            if (dto.getMemberCouponsId() != null) {
                // 用户选择使用优惠券，设置订单优惠券信息
                Map<String, Long> memberCouponsMap = new HashMap<>();
                memberCouponsMap.put(dto.getStoreId().toString(), dto.getMemberCouponsId());
                log.info("提交订单选择优惠券：{}", memberCouponsMap);
                orderConfirmDTO.setMemberCouponsId(JSONObject.toJSONString(memberCouponsMap));
                orderGoodsConfirmDTO.setActivityType(ActivityTypeEnum.COUPONS_ACTIVITY.value());
            } else {
                // 未选择优惠券，立即购买使用最优满减活动
                Long reduceActivityId = getDeaultReduceActivityId(dto.getGoodsId(), dto.getStoreId(),
                        dto.getBrandId(), goodsAmount, dto.getSecondStoreClassId());
//                orderGoodsConfirmDTO.setReduceActivityId(reduceActivityId);
                if (reduceActivityId != null) {
                    orderGoodsConfirmDTO.setActivityId(reduceActivityId);
                    orderGoodsConfirmDTO.setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                }
            }
        }

        // 保存订单确认表
        orderConfirmService.save(orderConfirmDTO);
        log.info("立即购买保存订单确认表信息成功");
        if (null != dto.getInvoiceType() && dto.getInvoiceType() != 0) {
            //保存订单发票信息
            OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();
            BeanCopier.create(InsertNowOrderVoDTO.class, OrderInvoiceDTO.class, false).copy(dto, orderInvoiceDTO, null);
            //说明用户开的是企业发票
            if (null != dto && null != dto.getCompanyType() && dto.getCompanyType() == 2) {
                MemberInvoiceDTO memberInvoiceDTO = dto.getMemberInvoiceDTO();
                BeanCopier.create(MemberInvoiceDTO.class, OrderInvoiceDTO.class, false).copy(memberInvoiceDTO,
                        orderInvoiceDTO, null);
                //TODO xuzhch 2020年5月7日18:56:00 修改为MQ保存发票信息
//            memberInvoiceService.saveOrUpdate(memberInvoiceDTO);
            }
            orderInvoiceDTO.setCreateOrderDate(new Date());
            if (!BeanUtil.isEmpty(orderInvoiceDTO.getCompanyType()) && !orderInvoiceDTO.getCompanyType().equals(0)) {
                orderInvoiceDTO.setApplyDate(new Date());
                orderInvoiceDTO.setInvoiceEvolve(1);
            }
            orderInvoiceDTO.setMemberName(memberVo.getMemberName());
            orderInvoiceDTO.setMemberInvoiceType(dto.getInvoiceType());
            orderInvoiceDTO.setMemberInvoiceContent(dto.getInvoiceContent());
            orderInvoiceDTO.setOrderId(orderConfirmDTO.getId());
            orderInvoiceDTO.setId(null);
            orderInvoiceService.save(orderInvoiceDTO);
        }
        // 封装订单商品确认表信息
        orderGoodsConfirmDTO.setOrderConfirmId(orderConfirmDTO.getId());
        orderGoodsConfirmDTO.setGoodsId(dto.getGoodsId());
        orderGoodsConfirmDTO.setSpecId(dto.getSpecId());
        orderGoodsConfirmDTO.setGoodsNum(dto.getGoodsNum());
        orderGoodsConfirmDTO.setGoodsImage(dto.getSpecMainPicture());
        orderGoodsConfirmDTO.setStoreId(dto.getStoreId());
        orderGoodsConfirmDTO.setStoreName(dto.getStoreName());
        orderGoodsConfirmDTO.setGoodsPayPrice(dto.getSpecSellPrice());
        orderGoodsConfirmDTO.setBuyerId(buyerId);
        orderGoodsConfirmDTO.setIsGift(IsGiftEnum.NO.value());
        orderGoodsConfirmDTO.setSpecCostPrice(dto.getSpecCostPrice());
        orderGoodsConfirmDTO.setSpecPrice(dto.getSpecPrice());
        orderGoodsConfirmDTO.setVirtualFlag(dto.getVirtualFlag());
        orderGoodsConfirmDTO.setDevlierType(dto.getDevlierType());

        // 保存订单商品确认表
        orderGoodsConfirmService.save(orderGoodsConfirmDTO);
        log.info("立即购买保存订单商品确认表信息成功");

        return orderConfirmDTO;
    }

    /**
     * 功能描述：
     * <获得默认满减活动id>
     *
     * @param goodsId     spu id
     * @param storeId     店铺id
     * @param brandId     品牌id
     * @param goodsAmount 商品总金额
     * @return
     * @date 2020/1/3 14:55
     * @author 刘远杰
     **/
    private Long getDeaultReduceActivityId(Long goodsId, Long storeId, Long brandId, BigDecimal goodsAmount, Long secondStoreClassId) {
        log.info("获取下单默认选择满减活动，goodsId：{}，storeId：{}，brandId：{}，商品总金额：{}", goodsId, storeId, brandId, goodsAmount);
        BigDecimal reduceAmount = BigDecimal.ZERO;
        Long reduceActivityId = null;
        // 1.查询商品所有满减活动
        List<ReduceActivityIndexDTO> goodsAllActivity = reduceActivityService.getGoodsAllActivity(goodsId, storeId,
                brandId, secondStoreClassId);
        log.info(goodsAllActivity + "");
        // 2.遍历活动，遍历规则，计算每个规则优惠金额
        if (CollectionUtils.isNotEmpty(goodsAllActivity)) {
            // 遍历所有满减规则，下单商品金额满足规则门槛，确定为默认活动，计算满减优惠金额
            // 再次匹配上满减规则，确定优惠金额是否大于上一条满减优惠设置金额，更加优惠替换默认满减，否则不做任何操作
            for (ReduceActivityIndexDTO reduceActivityIndexDTO : goodsAllActivity) {
                if (CollectionUtils.isNotEmpty(reduceActivityIndexDTO.getRuleList())) {
                    for (ReduceRuleDTO reduceRuleDTO : reduceActivityIndexDTO.getRuleList()) {
                        if (reduceRuleDTO.getRuleType() == ReduceActivityEnum.RULE_TYPE_NORMAL.value()
                                || reduceRuleDTO.getRuleType() == ReduceActivityEnum.RULE_TYPE_LADDER.value()) {
                            // 普通满减及阶梯满减规则金额计算
                            if (goodsAmount.compareTo(reduceRuleDTO.getLimitAmount()) >= 0
                                    && reduceRuleDTO.getReduceAmount().compareTo(reduceAmount) > 0) {
                                // 达到当前满减门槛并且满减金额大于上一个活动设置金额，替换原活动
                                reduceAmount = reduceRuleDTO.getReduceAmount();
                                reduceActivityId = reduceActivityIndexDTO.getId();
                            }
                        } else if (ReduceActivityEnum.RULE_TYPE_AVG.value() == reduceRuleDTO.getRuleType()) {
                            // 每满减活动
                            if (goodsAmount.compareTo(reduceRuleDTO.getLimitAmount()) >= 0) {
                                // 计算每满减优惠次数 = 下单商品总金额 / 规则门槛 （向下取整）
                                BigDecimal ladder = goodsAmount.divide(reduceRuleDTO.getLimitAmount(), 0,
                                        BigDecimal.ROUND_DOWN);
                                BigDecimal thisLimitAmount = reduceRuleDTO.getLimitAmount().multiply(ladder);
                                BigDecimal thisReduceAmount = reduceRuleDTO.getReduceAmount().multiply(ladder);
                                if (goodsAmount.compareTo(thisLimitAmount) >= 0
                                        && thisReduceAmount.compareTo(reduceAmount) > 0) {
                                    // 达到当前满减门槛并且满减金额大于上一个活动设置金额,替换活动数据
                                    reduceAmount = thisReduceAmount;
                                    reduceActivityId = reduceActivityIndexDTO.getId();
                                }
                            }
                        }
                    }
                }
            }
        }
        log.info("获取下单默认选择满减活动，reduceActivityId：{}, reduceAmount：{}", reduceActivityId, reduceAmount);
        return reduceActivityId;
    }

    /**
     * 功能描述:
     * 〈获得保存订单日志实体〉
     *
     * @param orderId      订单id
     * @param memberName   会员昵称
     * @param opreatorType 用户类型
     * @param orderStatus  订单当前状态
     * @param changeStatus 订单下一步状态
     * @param message      操作信息
     * @param now          操作时间
     * @return : void
     * @author : 刘远杰
     */
    private OrderLogDTO getOrderLog(Long orderId, String memberName, String opreatorType,
                                    Integer orderStatus, Integer changeStatus, String message, Date now) {
        OrderLogDTO orderLogDTO = new OrderLogDTO();
        orderLogDTO.setOrderId(orderId);
        orderLogDTO.setOrderStatus(orderStatus);
        orderLogDTO.setChangeStatus(changeStatus);
        orderLogDTO.setStatusInfo(message);
        orderLogDTO.setCreator(memberName);
        orderLogDTO.setCreateDate(now);
        return orderLogDTO;

    }

    /**
     * 功能描述:
     * 〈商家订单发货〉
     *
     * @param orderDeliverDTO 订单发货实体
     * @param storeId         店铺id
     * @author : 刘远杰
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void shippment(@RequestBody OrderDeliverDTO orderDeliverDTO,
                          @RequestParam("storeId") Long storeId) {
        Map<String, String> logMap = new HashMap<>();
        logMap.put("orderDeliverDTO", orderDeliverDTO.toString());
        logMap.put("storeId", storeId.toString());

        // 查询商家订单信息
        OrderEntity orderEntity = baseDao.selectOne(Wrappers.<OrderEntity>lambdaQuery()
                .eq(orderDeliverDTO.getId() != null, OrderEntity::getId, orderDeliverDTO.getId())
                .eq(OrderEntity::getStoreId, storeId));
        if (orderEntity == null) {
            mlogger.info(OrderStatusCode.ORDER_SHIPPMENT_FAILED.getCode(),
                    OrderStatusCode.ORDER_SHIPPMENT_FAILED.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.ORDER_SHIPPMENT_FAILED);
        }


        // 存在，判断订单状态
        Date now = new Date();
        if (orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_SHIPPED.value()) {
            // 修改订单状态
            orderEntity.setOrderStatus(OrderStatusEnum.WAITTING_RECEIVED.value());
            orderEntity.setAppStatus(OrderStatusEnum.WAITTING_RECEIVED.value());

            orderEntity.setTransportTime(now);
            orderEntity.setDevlierType(orderDeliverDTO.getDevlierType());
            if (DevlierTypeEnum.EXPRESS.value().equals(orderDeliverDTO.getDevlierType())) {

                orderEntity.setTransportCompanyName(orderDeliverDTO.getTransportCompanyName());
                orderEntity.setTransportCode(orderDeliverDTO.getTransportCode());
                orderEntity.setTrandportExpressCode(orderDeliverDTO.getTransportCompanyId());
                orderEntity.setTransportCompanyPhone(StringUtils.isBlank(orderDeliverDTO.getTransportCompanyPhone())
                        ? null : orderDeliverDTO.getTransportCompanyPhone());
            }
            // 修改订单记录
            this.updateById(orderEntity);

            // 查询此订单的父订单并修改订单状态为已发货
            if (null != orderEntity.getParentOrderSn()) {
                OrderDTO orderDTO = this.findOrderByOrderSn(orderEntity.getParentOrderSn().toString());
                if (null != orderDTO) {
                    OrderEntity orderEntity1 = new OrderEntity();
                    BeanCopier.create(OrderDTO.class, OrderEntity.class, false).copy(orderDTO, orderEntity1, null);
                    orderEntity1.setOrderStatus(OrderStatusEnum.WAITTING_RECEIVED.value());
                    orderEntity1.setAppStatus(OrderStatusEnum.WAITTING_RECEIVED.value());
                    this.updateById(orderEntity1);
                }
            }

            // 增加物流信息表保存
            if (DevlierTypeEnum.EXPRESS.value().equals(orderDeliverDTO.getDevlierType())) {
                TransportMessageDTO transportMessageDTO = new TransportMessageDTO();
                transportMessageDTO.setNu(orderDeliverDTO.getTransportCode());
                transportMessageDTO.setCom(orderDeliverDTO.getTransportCompanyId());
                transportMessageDTO.setState(10);
                transportMessageDTO.setComanyName(orderDeliverDTO.getTransportCompanyName());
                transportMessageDTO.setComanyPhone(orderDeliverDTO.getTransportCompanyPhone());
                transportMessageService.save(transportMessageDTO);
            }

            // 发货日志记录
            OrderLogDTO orderLogDTO = this.getOrderLog(orderEntity.getId(), "商家", OperatorTypeEnum.SELLER.value(),
                    orderEntity.getOrderStatus(), OrderStatusEnum.COMPLETE.value(), "商家发货", now);
            // 发送MQ消息保存订单日志
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE,
                    JSONObject.toJSONString(orderLogDTO));

            // 封装发送短信参数并发送短信消息
            this.sendMessage(orderDeliverDTO, orderEntity, now);

            mlogger.info(OrderStatusCode.ORDER_SHIPPMENT_SUCCESS_CODE,
                    OrderStatusCode.ORDER_SHIPPMENT_SUCCESS_MESSAGE, logMap);
        } else {
            mlogger.info(OrderStatusCode.ORDER_SHIPPMENT_FAILED_STATUS.getCode(),
                    OrderStatusCode.ORDER_SHIPPMENT_FAILED_STATUS.getMessage(), logMap);
            throw new ServiceException(OrderStatusCode.ORDER_SHIPPMENT_FAILED_STATUS);
        }
    }

    /**
     * 封装发送短信参数并发送短信消息
     *
     * @param orderDeliverDTO: 物流公司信息
     * @param orderEntity:     订单实体
     * @param now:             发货时间
     * @date 2020/4/10 10:24
     * @author lixiangx@leimingtech.com
     **/
    private void sendMessage(OrderDeliverDTO orderDeliverDTO, OrderEntity orderEntity, Date now) {
//        // 订单发货发送消息提醒
//        Map<String, Object> map = new HashMap<>(16);
//        map.put("type", MessageEnum.SEND_MODE_SMS_INNER.value());
//        map.put("code", MessageTemlateEnum.ORDER_DELIVER_SMS.value());
//
//        // 创建短信所需参数Map
//        Map<String, String> paramsMap = new HashMap<>(16);
//        paramsMap.put("sellerName", orderEntity.getStoreName());
//        paramsMap.put("orderSn", String.valueOf(orderEntity.getOrderSn()));
//        map.put("paramsMap", paramsMap);
//
//        // 创建微信公众号推送信息所需参数Map
//        Map<String, String> wechatParamsMap = new HashMap<>(16);
//        wechatParamsMap.put("first", "您购买的订单已经发货啦，正快马加鞭向您飞奔而去。");
//        // keyword1 订单编号
//        wechatParamsMap.put("keyword1", String.valueOf(orderEntity.getOrderSn()));
//        // keyword2 发货时间
//        wechatParamsMap.put("keyword2", DateUtils.format(now, DateUtils.DATE_TIME_PATTERN));
//        // keyword3 物流公司
//        wechatParamsMap.put("keyword3", orderDeliverDTO.getTransportCompanyName());
//        // keyword4 快递单号
//        wechatParamsMap.put("keyword4", orderDeliverDTO.getTransportCode());
//
//        OrderAddressDTO orderAddress = Optional.ofNullable(orderAddressService.get(orderEntity.getAddressId()))
//        .orElseGet(OrderAddressDTO::new);
//        // keyword5 收货地址信息
//        StringBuilder sb = new StringBuilder();
//        sb.append(orderAddress.getTrueName());
//        sb.append(orderAddress.getMobPhone());
//        sb.append(orderAddress.getAddress());
//        sb.append(orderAddress.getAreaInfo());
//        wechatParamsMap.put("keyword5", sb.toString());
//        wechatParamsMap.put("remark", "请保持收件手机畅通！您可以在雷铭电商平台我的订单中查看物流信息");
//        map.put("wechatParamsMap", wechatParamsMap);
//
//        MemberDTO memberDTO = memberService.getById(orderEntity.getBuyerId());
//        map.put("mobile", memberDTO.getMemberMobile());
//        map.put("memberId", orderEntity.getBuyerId());
//        map.put("nikeName", orderEntity.getBuyerName());
//        map.put("sendName", orderEntity.getStoreName());
//        map.put("wechatOpenId", memberDTO.getWechatOpenid());
//
//        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(map));

        Map<String, Object> map = new HashMap<>(16);

        // 获取用户信息
        MemberDTO memberDTO = memberService.getById(orderEntity.getBuyerId());

        // 创建发送站内信用户信息集合
        Map<Long, String> memberMap = new HashMap<>(5);
        memberMap.put(orderEntity.getBuyerId(), orderEntity.getBuyerName());
        map.put("memberMap", memberMap);

        //设置用户手机号、站内信数据
        map.put("mobile", memberDTO.getMemberMobile());
        map.put("wechatOpenId", memberDTO.getWechatOpenid());

        // 创建短信所需参数Map
        Map<String, String> paramsMap = new HashMap<>(5);
        paramsMap.put("sellerName", orderEntity.getStoreName());
        paramsMap.put("orderSn", String.valueOf(orderEntity.getOrderSn()));
        paramsMap.put("orderId", String.valueOf(orderEntity.getId()));
        map.put("paramMap", paramsMap);

        // 创建微信公众号推送信息所需参数Map
        Map<String, String> wechatParamsMap = new HashMap<>(16);
        wechatParamsMap.put("first", "您购买的订单已经发货啦，正快马加鞭向您飞奔而去。");
        // keyword1 订单编号
        wechatParamsMap.put("keyword1", String.valueOf(orderEntity.getOrderSn()));
        // keyword2 发货时间
        wechatParamsMap.put("keyword2", DateUtils.format(now, DateUtils.DATE_TIME_PATTERN));
        if (orderDeliverDTO.getDevlierType() == 1) {
            // keyword3 物流公司
            wechatParamsMap.put("keyword3", orderDeliverDTO.getTransportCompanyName());
            // keyword4 快递单号
            wechatParamsMap.put("keyword4", orderDeliverDTO.getTransportCode());
        } else if (orderDeliverDTO.getDevlierType() == 2) {
            wechatParamsMap.put("keyword3", "自提");
            wechatParamsMap.put("keyword4", "自提");
        } else {
            wechatParamsMap.put("keyword3", "无需物流");
            wechatParamsMap.put("keyword4", "无需物流");
        }


        OrderAddressDTO orderAddress =
                Optional.ofNullable(orderAddressService.get(orderEntity.getAddressId())).orElseGet(OrderAddressDTO::new);
        // keyword5 收货地址信息
        StringBuilder sb = new StringBuilder();
        sb.append(orderAddress.getTrueName());
        sb.append(orderAddress.getMobPhone());
        sb.append(orderAddress.getAddress());
        sb.append(orderAddress.getAreaInfo());
        wechatParamsMap.put("keyword5", sb.toString());
        String redisByName = settingService.queryRedisByName(SettingsEnum.SITE.value());
        JSONObject jsonObject = JSONObject.parseObject(redisByName);
        wechatParamsMap.put("remark", "请保持收件手机畅通！您可以在" + jsonObject.get("name") + "我的订单中查看物流信息");

        map.put("wechatParamsJson", JSON.toJSONString(wechatParamsMap));
        map.put("wehcatClickUrl",
                AddressPrefixProperties.adddressPrefix + "#/pagesD/order/orderDet?paySn=" + orderEntity.getPaySn() +
                        "%26id" +
                        "=" + orderEntity.getId());

        // 创建消息实体
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(MessageCodeEnum.ORDER_DELIVER_SMS.value());
        messageDTO.setSendName(orderEntity.getStoreName());
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);

        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
    }

    /**
     * 功能描述：
     * <封装发送短信参数并发送支付成功短信消息>
     *
     * @param orderDTO          订单
     * @param orderGoodsDTOList 订单商品集合
     * @return
     * @date 2020/4/16 17:29
     * @author 刘远杰
     **/
    private void sendPayMessage(OrderDTO orderDTO, List<OrderGoodsDTO> orderGoodsDTOList) {
        Map<String, Object> map = new HashMap<>(16);

        // 获取用户信息
        MemberDTO memberDTO = memberService.getById(orderDTO.getBuyerId());
        // 创建发送站内信用户信息集合
        Map<Long, String> memberMap = new HashMap<>(5);
        memberMap.put(orderDTO.getBuyerId(), orderDTO.getBuyerName());
        map.put("memberMap", memberMap);

        //设置用户手机号、站内信数据
        map.put("mobile", memberDTO.getMemberMobile());
        map.put("wechatOpenId", memberDTO.getWechatOpenid());

        if (orderDTO.getVirtualFlag().equals(0)) {
            this.orderPayNotice(orderDTO, orderGoodsDTOList, map);
        } else {
            this.virtualOrderPayNotice(orderDTO, orderGoodsDTOList, map);
        }

    }

    /**
     * 虚拟商品发送消息参数封装
     *
     * @param orderDTO
     * @param orderGoodsDTOList
     * @param map
     */
    private void virtualOrderPayNotice(OrderDTO orderDTO, List<OrderGoodsDTO> orderGoodsDTOList,
                                       Map<String, Object> map) {
        // 创建短信所需参数Map
        Map<String, String> paramsMap = new HashMap<>(1);
        // 创建微信公众号推送信息所需参数Map0
        Map<String, String> wechatParamsMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                Integer goodsNum = orderGoodsDTO.getGoodsNum();
                String goodsName = orderGoodsDTO.getGoodsName();
                paramsMap.put("goodsName", goodsName);
                paramsMap.put("orderSn", orderDTO.getOrderSn().toString());
                paramsMap.put("number", goodsNum + "");
                paramsMap.put("code", orderGoodsDTO.getFetchCode());
                map.put("paramMap", paramsMap);
                wechatParamsMap.put("first", "您购买的商品已支付成功");
                // keyword1 订单商品名称
                wechatParamsMap.put("keyword1", goodsName);
                // keyword2 订单编号
                wechatParamsMap.put("keyword2", String.valueOf(orderDTO.getOrderSn()));
                // keyword3 订单金额
                wechatParamsMap.put("keyword3", String.valueOf(orderDTO.getOrderAmount()));
                // keyword4 支付时间
                wechatParamsMap.put("keyword4", DateUtils.format(orderDTO.getPaymentTime(),
                        DateUtils.DATE_TIME_PATTERN));
                wechatParamsMap.put("keyword5", "该订单无需物流");
                wechatParamsMap.put("remark", "电子提货码为【" + orderGoodsDTO.getFetchCode() + "】请凭电子提货码到店自提。");
                map.put("wechatParamsJson", JSON.toJSONString(wechatParamsMap));
                map.put("wehcatClickUrl", "");
                // 创建消息实体
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setMessageCode(MessageCodeEnum.VIRTUAL_ORDER_PAY_SUCCESS_REMIND.value());
                messageDTO.setSendTime(new Date());
                messageDTO.setMessageSource(MessageSourceEnum.LEIMING_SOURCE.value());
                messageDTO.setSendName(orderDTO.getStoreName());
                messageDTO.setParamJson(JSON.toJSONString(map));
                messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
                sysMessageService.save(messageDTO);
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
            }
        }
    }

    /**
     * 非虚拟商品发送消息参数封装
     *
     * @param orderDTO
     * @param orderGoodsDTOList
     * @param map
     */
    private void orderPayNotice(OrderDTO orderDTO, List<OrderGoodsDTO> orderGoodsDTOList, Map<String, Object> map) {
        // 创建短信所需参数Map
        Map<String, String> paramsMap = new HashMap<>(1);
        String firstGoodsName = "";
        Integer goodsNum = 0;
        if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
            firstGoodsName = String.valueOf(orderGoodsDTOList.get(0).getGoodsName());
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                goodsNum += orderGoodsDTO.getGoodsNum();
            }
        }

        paramsMap.put("goodsName", goodsNum > 1 ? firstGoodsName + "等，共" + goodsNum + "件" :
                firstGoodsName + "，共" + goodsNum + "件");
        paramsMap.put("orderSn", orderDTO.getOrderSn().toString());
        map.put("paramMap", paramsMap);

        // 创建微信公众号推送信息所需参数Map0
        Map<String, String> wechatParamsMap = new HashMap<>(16);
        wechatParamsMap.put("first", "订单支付成功，请您注意物流信息，及时收取货物");
        // keyword1 订单商品名称
        wechatParamsMap.put("keyword1", firstGoodsName);
        // keyword2 订单编号
        wechatParamsMap.put("keyword2", String.valueOf(orderDTO.getOrderSn()));
        // keyword3 订单金额
        wechatParamsMap.put("keyword3", String.valueOf(orderDTO.getOrderAmount()));
        // keyword4 支付时间
        wechatParamsMap.put("keyword4", DateUtils.format(orderDTO.getPaymentTime(), DateUtils.DATE_TIME_PATTERN));

        OrderAddressDTO orderAddress =
                Optional.ofNullable(orderAddressService.get(orderDTO.getAddressId())).orElseGet(OrderAddressDTO::new);
        // keyword5 收货地址信息
        StringBuilder sb = new StringBuilder();
        sb.append(orderAddress.getTrueName());
        sb.append(orderAddress.getMobPhone());
        sb.append(orderAddress.getAddress());
        sb.append(orderAddress.getAreaInfo());
        wechatParamsMap.put("keyword5", sb.toString());
        wechatParamsMap.put("remark", "请保持收件手机畅通！您可以在雷铭电商平台我的订单中查看物流信息");

        map.put("wechatParamsJson", JSON.toJSONString(wechatParamsMap));
        map.put("wehcatClickUrl",
                AddressPrefixProperties.adddressPrefix + "#/pagesD/order/orderDet?paySn=" + orderDTO.getPaySn() +
                        "%26id=" + orderDTO.getId());

        // 创建消息实体
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(MessageCodeEnum.ORDER_PAY_SUCCESS_REMIND.value());
        messageDTO.setSendName(orderDTO.getStoreName());
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
    }

    /**
     * 确认收货
     *
     * @param id
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmReceipt(Long id,
                                              @RequestParam(value = "buyerId", required = false) Long buyerId) {
        Map<String, String> logMap = new HashMap<>();
        logMap.put("orderId", id.toString());
        logMap.put("buyerId", buyerId.toString());
        mlogger.info(OrderStatusCode.ORDER_CONFIRM_RECEIPT_H5, OrderStatusCode.ORDER_CONFIRM_RECEIPT_H5_MESSAGE,
                logMap);

        Map<String, Object> res = new HashMap<>();
        // 查询商家订单信息
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        OrderEntity orderEntity = baseDao.selectOne(wrapper);

        if (orderEntity != null) {
            Date now = new Date();
            log.info("确认收货的订单数据，orderEntity:{}", orderEntity);
            if (orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_RECEIVED.value()) {
                orderEntity.setOrderStatus(OrderStatusEnum.COMPLETE.value());
                orderEntity.setAppStatus(OrderAppStatusEnum.APP_COMPLETE.value());
                orderEntity.setCompleteTime(now);

                // 首次下单奖励积分和成长值
                Map<String, Object> memberParams = new HashMap<>();
                memberParams.put("memberId", orderEntity.getBuyerId());
                memberParams.put("memberName", orderEntity.getBuyerName());
                memberParams.put("sourceType", MemberPointLogEnum.FIRST_ORDER_SOURCE_TYPE.code());
                memberParams.put("pointDesc", MemberPointLogEnum.FIRST_ORDER_SOURCE_TYPE.value());
                memberService.savePoint(memberParams);

                // 更多规则 订单支付金额，运费，单价商品优惠后的价格在order——goods中

                // 根据订单查询对应的订单商品列表为了后续的积分计算
                List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsService.getByOrderId(orderEntity.getId(), null,
                        orderEntity.getStoreId());

                // 保存积分
                Map<String, Object> pointAndGrowth = this.savePoint(orderEntity.getOrderAmount(), orderGoodsDTOList,
                        orderEntity.getBuyerId(), orderEntity.getBuyerName());

                orderEntity.setOrderGrowthCount((Integer) (pointAndGrowth.get("finalGrowth")));
                orderEntity.setOrderPointsCount((Integer) (pointAndGrowth.get("finalPont")));
                baseDao.updateById(orderEntity);

                Map<String, String> params = new HashMap<>();
                orderGoodsDTOList.forEach(orderGoodsDTO -> {
                    params.put(orderGoodsDTO.getSpecId().toString(), String.valueOf(orderGoodsDTO.getGoodsNum()));
                });
                // 更新销量
                goodsSpecService.updateSale(params, 1);

                // 修改父订单状态：只有子订单全部已完成，父订单才是已完成
                if (null != orderEntity.getParentOrderSn()) {
                    List<OrderDTO> childOrderList = this.childOrderList(orderEntity.getParentOrderSn());
                    if (CollectionUtils.isNotEmpty(childOrderList)) {
                        Integer completeFlag = 0;
                        for (OrderDTO orderDTO : childOrderList) {
                            if (orderDTO.getAppStatus() == OrderStatusEnum.COMPLETE.value()) {
                                completeFlag++;
                            }
                        }

                        // 说明子订单全部是已完成
                        if (completeFlag == childOrderList.size()) {
                            OrderDTO orderDTO = this.findOrderByOrderSn(orderEntity.getParentOrderSn().toString());
                            if (null != orderDTO) {
                                OrderEntity orderEntity1 = new OrderEntity();
                                BeanCopier.create(OrderDTO.class, OrderEntity.class, false).copy(orderDTO,
                                        orderEntity1, null);
                                orderEntity1.setOrderStatus(OrderStatusEnum.COMPLETE.value());
                                orderEntity1.setAppStatus(OrderAppStatusEnum.APP_COMPLETE.value());
                                baseDao.updateById(orderEntity1);
                            }
                        }
                    }

                }

                //保存日志
                if (buyerId != null) {
                    OrderLogDTO orderLog = this.getOrderLog(orderEntity.getId(), orderEntity.getBuyerName(),
                            OperatorTypeEnum.BUYER.value(), OrderStatusEnum.COMPLETE.value(), null, "确认收货", now);
                    //发送MQ消息保存订单日志
                    String message = JSONObject.toJSONString(orderLog);
                    rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE, message);

                } else {
                    OrderLogDTO orderLog = this.getOrderLog(orderEntity.getId(), null,
                            OperatorTypeEnum.ADMIN.value(), OrderStatusEnum.COMPLETE.value(), null, "自动确认收货", now);
                    //发送MQ消息保存订单日志
                    String message = JSONObject.toJSONString(orderLog);
                    rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE, message);
                }
            } else {
                res.put("code", OrderErrorEnum.OPERATION_ERROR.code());
                res.put("msg", OrderErrorEnum.OPERATION_ERROR.msg());
            }
        } else {
            res.put("code", OrderErrorEnum.IS_NULL.code());
            res.put("msg", OrderErrorEnum.IS_NULL.msg());
        }
        return res;
    }

    /**
     * 定时确认收货
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void orderReceivingTiming() {


        //获取当前时间
        Date nowDate = new Date();
        //获取自动收货配置
        String setting = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
        SettingSeniorDTO seniorSet = JSON.parseObject(setting, SettingSeniorDTO.class);
        String day = seniorSet.getAutoCompleteOrder();
        Integer receivingOrder = Integer.valueOf(day);
        int cancelOrderLong = OrderTimeConstants.SECOND_DAY * receivingOrder * -1;
        //获取可取消订单的时间
        Date authDate = DateUtils.addDateSeconds(nowDate, cancelOrderLong);
        // 当前时间时间戳
        //查询到时间确认收货订单
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper();
        wrapper.le("transport_time", authDate);
        wrapper.eq("order_status", OrderStatusEnum.WAITTING_RECEIVED.value());
        wrapper.eq("is_split", OrderSplitEnum.NO.value());
        List<OrderEntity> orderEntities = baseDao.selectList(wrapper);
        orderEntities.forEach(orderEntity -> confirmReceipt(orderEntity.getId(), orderEntity.getBuyerId()));
    }

    /**
     * 取消订单（H5）
     *
     * @param dto
     * @param buyerId 买家id
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void cancelOrder(@RequestBody OrderCancelDTO dto,
                            @RequestParam(value = "buyerId", required = false) Long buyerId) {

        // 查询商家订单信息
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(dto.getId() != null, "id", dto.getId());
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        OrderEntity orderEntity = baseDao.selectOne(wrapper);

        // 日志更新集合
        List<OrderEntity> logs = new ArrayList<>();

        if (orderEntity != null) {
            if (orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()) {
                Date now = new Date();
                //取消父订单，修改支付状态
                cancelOrder(orderEntity, dto.getReasonId(), now);
                // 查询所有子订单
                if (orderEntity.getIsSplit() == OrderSplitEnum.YES.value()) {
                    // 父订单，查询子订单，修改日志
                    List<OrderEntity> list = baseDao.selectList(new QueryWrapper<OrderEntity>().eq("parent_order_sn",
                            orderEntity.getOrderSn()));
                    if (CollectionUtils.isNotEmpty(list)) {
                        // 添加日志更新对象
                        logs.addAll(list);
                    }
                }
                // 添加日志更新对象
                logs.add(orderEntity);
                //保存日志
                logs.forEach(entity -> saveOrderLog(buyerId, null, entity, now));
            } else {
                throw new ServiceException(OrderStatusCode.CANCEL_ORDER_STATUS_EXCPTION);
            }
        } else {
            throw new ServiceException(OrderStatusCode.CANCEL_ORDER_NO_EXIST_ORDER);
        }
    }


    /**
     * 取消订单(seller)
     *
     * @param dto
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void cancelOrderSeller(@RequestBody OrderCancelDTO dto,
                                  @RequestParam(value = "storeId", required = false) Long storeId) {
        // 查询商家订单信息
        OrderEntity orderEntity = baseDao.selectOne(Wrappers.<OrderEntity>lambdaQuery()
                .eq(dto.getId() != null, OrderEntity::getId, dto.getId())
                .eq(storeId != null, OrderEntity::getStoreId, storeId));

        Optional.ofNullable(orderEntity).orElseThrow(() -> new ServiceException(OrderStatusCode.CANCEL_ORDER_NO_EXIST_ORDER));

        // 日志更新集合
        List<OrderEntity> orderEntityList = new ArrayList<>();

        Date now = new Date();

        OrderEntity parentOrderEntity = new OrderEntity();
        if (orderEntity.getParentOrderSn() != null) {
            // 存在父订单号则获取父订单信息
            parentOrderEntity = baseDao.selectOne(Wrappers.<OrderEntity>lambdaQuery()
                    .eq(OrderEntity::getOrderSn, orderEntity.getParentOrderSn())
                    .eq(OrderEntity::getIsSplit, OrderSplitEnum.YES.value()));
            Optional.ofNullable(parentOrderEntity).orElseThrow(() -> new ServiceException(OrderStatusCode.GET_PARENT_ORDER_FAILED));
        }

        if (orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()) {
            if (orderEntity.getParentOrderSn() == null) {
                // 待付款状态下不存在父订单 直接取消订单
                cancelOrder(orderEntity, dto.getReasonId(), now);
            } else {
                // 待付款状态下存在父订单 取消父订单下所有子订单
                cancelOrder(parentOrderEntity, dto.getReasonId(), now);
            }
        } else if (orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_SHIPPED.value()) {
            // 待发货状态下取消子订单
            cancelOrder(orderEntity, dto.getReasonId(), now);

            // 待发货并且已支付状态,微信退款
            if (orderEntity.getPaymentStatus() == PaymentStatusEnum.YES.value()) {
                this.asynWechatRefund(orderEntity, parentOrderEntity.getOrderAmount());
            }
        } else {
            throw new ServiceException(OrderStatusCode.CANCEL_ORDER_STATUS_EXCPTION);
        }

        // 添加日志更新对象
        orderEntityList.add(orderEntity);
        // 查询所有子订单
        if (orderEntity.getIsSplit() == OrderSplitEnum.YES.value()) {
            // 父订单，查询子订单，修改日志
            List<OrderEntity> list = Optional.ofNullable(baseDao.selectList(new QueryWrapper<OrderEntity>()
                    .eq("parent_order_sn", orderEntity.getOrderSn()))).orElse(Lists.newArrayList());
            // 添加日志更新对象
            orderEntityList.addAll(list);
        }

        //保存日志
        orderEntityList.forEach(entity -> this.saveOrderLog(null, storeId, entity, now));
    }

    /**
     * 取消订单 异步进行微信退款
     *
     * @param orderEntity:       订单实体
     * @param parentOrderAmount: 父订单金额
     * @date 2020/4/11 17:01
     * @author lixiangx@leimingtech.com
     **/
    private void asynWechatRefund(OrderEntity orderEntity, BigDecimal parentOrderAmount) {
        H5WxRefundBaseDTO h5WxRefundBaseDTO = new H5WxRefundBaseDTO();
        h5WxRefundBaseDTO.setOutrefundno(IdGenerator.defaultSnowflakeId().toString());
        h5WxRefundBaseDTO.setOrdersn(orderEntity.getOrderSn().toString());
        h5WxRefundBaseDTO.setOrderId(orderEntity.getId());
        h5WxRefundBaseDTO.setBuyerId(orderEntity.getBuyerId());
        h5WxRefundBaseDTO.setBuyerName(orderEntity.getBuyerName());
        h5WxRefundBaseDTO.setStoreId(orderEntity.getStoreId());
        h5WxRefundBaseDTO.setOuttradeno(String.valueOf(orderEntity.getPaySn()));
        h5WxRefundBaseDTO.setStoreName(orderEntity.getStoreName());
        h5WxRefundBaseDTO.setPayType(orderEntity.getPaymentId().intValue());
        h5WxRefundBaseDTO.setRefundfee((orderEntity.getOrderAmount().multiply(new BigDecimal(100))).intValue());
        h5WxRefundBaseDTO.setBalanceRefundAmount(orderEntity.getBalanceAmount());
        OrderPayDTO payDTO = orderPayService.getByPaySn(orderEntity.getPaySn());
        h5WxRefundBaseDTO.setTotalfee((payDTO.getPayAmount().multiply(new BigDecimal(100))).intValue());

        // 判断是否余额支付
        if (orderEntity.getPaymentCode().equals(PaymentCodeConstants.BALANCE)) {
            // 更新用户订单已退款余额金额
            OrderEntity orderEntity1 = new OrderEntity();
            orderEntity1.setId(orderEntity.getId());
            orderEntity1.setBalanceRefundAmount(orderEntity.getBalanceAmount());
            baseDao.updateById(orderEntity1);

        } else {
            // 混合支付下处理优先退余额
            if (orderEntity.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {

                // 退款金额等于总的支付金额减去余额支付金额
                BigDecimal thirdRefund = orderEntity.getOrderAmount().subtract(orderEntity.getBalanceAmount());

                h5WxRefundBaseDTO.setRefundfee((thirdRefund.multiply(new BigDecimal(100))).intValue());

                // 判断余额支付金额是否大于余额已退款金额。大于则说明还可以进行余额退款,需要先余额退款并修改第三方退款金额，否则不进行余额退款
                if (orderEntity.getBalanceAmount().compareTo(orderEntity.getBalanceRefundAmount()) > 0) {
                    BigDecimal refundBalance =
                            orderEntity.getBalanceAmount().subtract(orderEntity.getBalanceRefundAmount());

                    // 更新订单已退款余额
                    OrderEntity orderEntity1 = new OrderEntity();
                    orderEntity1.setId(orderEntity.getId());
                    orderEntity1.setBalanceRefundAmount(orderEntity.getBalanceRefundAmount().add(refundBalance));
                    baseDao.updateById(orderEntity1);

                    // 变更用户余额和新增用户余额变更明细表
//                    MemberChangeBalanceDTO memberChangeBalanceDTO = new MemberChangeBalanceDTO();
//                    memberChangeBalanceDTO.setMemberId(orderEntity.getBuyerId());
//                    memberChangeBalanceDTO.setChangeBalance(refundBalance);
//                    memberChangeBalanceDTO.setBalanceType(BalanceEnum.ORDER_REFUND.value());
//                    log.info("余额退款数据，memberChangeBalanceDTO{}", memberChangeBalanceDTO);
//                    memberInfoService.changeMemberBalance(memberChangeBalanceDTO);

                }

            }
        }

        String refundMessage = JSONObject.toJSONString(h5WxRefundBaseDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_WEIXIN_REFUND_QUEUE, refundMessage);
    }

    /**
     * 取消订单
     *
     * @param orderEntity 订单实体
     * @param reasonId    原因id
     * @param now         取消时间
     */
    private void cancelOrder(OrderEntity orderEntity, Long reasonId, Date now) {
        log.info("取消订单信息,orderEntity:{}", orderEntity);
        OrderEntity entity = new OrderEntity();
        entity.setOrderSn(orderEntity.getOrderSn());
        // 待付款/商家操作
        entity.setOrderStatus(OrderStatusEnum.CANCELED.value());
        entity.setAppStatus(OrderAppStatusEnum.APP_CANCELED.value());
        //获取订单原因描述
        ReasonDescriptionDTO reasonDescriptionDTO = reasonDescriptionService.get(reasonId);
        if (reasonDescriptionDTO != null) {
            entity.setCauseId(reasonDescriptionDTO.getId());
            entity.setCancelCause(reasonDescriptionDTO.getContent());
        } else {
            entity.setCancelCause("订单未支付超时，自动取消订单");
        }
        entity.setCancelDate(now);
        baseDao.updateOrderByOrderSnOrParent(entity);

        // 订单取消修改商品库存/销量
        List<OrderGoodsDTO> orderGoodsDTOList;
        if (OrderSplitEnum.YES.value() == orderEntity.getIsSplit()) {
            // 父订单查询订单商品
            orderGoodsDTOList = orderGoodsService.findOrderGoodsByParentOrderSn(orderEntity.getOrderSn());
        } else {
            // 子订单查询订单商品
            orderGoodsDTOList = orderGoodsService.getByOrderId(orderEntity.getId(), null, orderEntity.getStoreId());
        }

        // 如果没有商品信息直接返回，不进行下面的更新商品库存、销量逻辑
        if (CollectionUtils.isEmpty(orderGoodsDTOList)) {
            return;
        }

        // 创建Map key规格ID  value为数量
        Map<String, String> params = new HashMap<>(20);
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            String goodsNum = String.valueOf(-orderGoodsDTO.getGoodsNum());
            params.put(orderGoodsDTO.getSpecId().toString(), goodsNum);
        }


        boolean flag = true;
        // 判断当前订单是否是拼团订单，如果是的话，则不更新普通商品的库存；如果拼团活动结束，则更新普通商品库存；
        if (null == orderEntity.getGroupRecordId()) {
            // 更新库存,规格删除放弃增加库存
            flag = goodsSpecService.updateStorage(params, 1);
        } else {
            // 更新拼团订单记录相关信息
            this.cancelGroupOrderRecord(orderEntity, flag, params);
        }

        if (!flag) {
            log.error("取消订单失败，更新库存失败");
            throw new ServiceException(OrderStatusCode.CANCEL_ORDER_UPDATE_STROAGR_SALE_FAIL);
        }

        // 与产品确认订单完成后更新销量
//        if (orderEntity.getPaymentStatus() == PaymentStatusEnum.YES.value()) {
//            // 已支付订单取消更新销量,已删除规格放弃更新销量操作
//            flag = goodsSpecService.updateSale(params, type);
//            if (!flag) {
//                log.info("取消订单失败，更新销量失败");
//                throw new ServiceException(OrderStatusCode.CANCEL_ORDER_UPDATE_STROAGR_SALE_FAIL);
//            }
//        }

        // 取消订单返回活动库存
        orderCancelReturnSeckillStorage(orderGoodsDTOList);

        // 取消订单返回优惠券
        this.returnCoupons(orderEntity);

        // 修改父订单状态：只有子订单全部取消，父订单才是已取消
        if (null != orderEntity.getParentOrderSn()) {
            List<OrderDTO> childOrderList = this.childOrderList(orderEntity.getParentOrderSn());
            if (CollectionUtils.isNotEmpty(childOrderList)) {
                Integer completeFlag = 0;
                for (OrderDTO orderDTO : childOrderList) {
                    if (orderDTO.getAppStatus() == OrderStatusEnum.CANCELED.value()) {
                        completeFlag++;
                    }
                }

                // 说明子订单全部是已取消
                if (completeFlag == childOrderList.size()) {
                    OrderDTO orderDTO = this.findOrderByOrderSn(orderEntity.getParentOrderSn().toString());
                    if (null != orderDTO) {
                        OrderEntity orderEntity1 = new OrderEntity();
                        BeanCopier.create(OrderDTO.class, OrderEntity.class, false).copy(orderDTO, orderEntity1, null);
                        orderEntity1.setOrderStatus(OrderStatusEnum.CANCELED.value());
                        orderEntity1.setAppStatus(OrderAppStatusEnum.APP_CANCELED.value());
                        baseDao.updateById(orderEntity1);
                    }
                }
            }

        }

        // 未支付订单修改支付状态为已取消，防止取消订单定时任务扫描
        if (orderEntity.getPaymentStatus() == PaymentStatusEnum.NO.value()) {
            // 修改支付状态为取消
            Boolean payFlag = orderPayService.cancelOrderPay(orderEntity.getPaySn());
            if (!payFlag) {
                log.info("取消订单失败，修改支付状态失败");
                throw new ServiceException(OrderStatusCode.CANCEL_ORDER_UPDATE_PAY_FAIL);
            }
        }

        // 余额支付取消订单
        if (orderEntity.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
            this.balanceCancelOrder(orderEntity);
        }

    }

    /**
     * 支付未完成，取消订单，解冻余额
     *
     * @return
     * @date 2020-07-09 14:36
     * @author huangkeyuan@leimingtech.com
     **/
    private void balanceCancelOrder(OrderEntity orderEntity) {
        // 处理未支付的余额支付退款，已支付的余额退款在asynWechatRefund方法中处理
        if (orderEntity.getPaymentStatus() == PaymentStatusEnum.NO.value()) {
            log.info("余额支付取消订单，orderEntity{}", orderEntity);
            // 将冻结余额放回到可用余额中
            MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
            memberUpdateDTO.setAvailableBalance(orderEntity.getBalanceAmount());
            memberUpdateDTO.setBlockedBalance(orderEntity.getBalanceAmount().negate());
            memberUpdateDTO.setId(orderEntity.getBuyerId());
            log.info("余额支付取消订单，将冻结余额放回到可用余额中,memberUpdateDTO{}", memberUpdateDTO);
            memberInfoService.updateByMemberId(memberUpdateDTO);

            // 更新用户订单已退款余额金额
            OrderEntity orderEntity1 = new OrderEntity();
            orderEntity1.setId(orderEntity.getId());
            orderEntity1.setBalanceRefundAmount(orderEntity.getBalanceAmount());
            baseDao.updateById(orderEntity1);

        }
    }


    /**
     * 取消拼团订单和记录
     *
     * @param orderEntity
     * @param flag
     * @param params
     * @return
     * @date 2020-07-09 10:16
     * @author huangkeyuan@leimingtech.com
     **/
    private void cancelGroupOrderRecord(OrderEntity orderEntity, boolean flag, Map<String, String> params) {

        GroupRecordDTO groupRecordDTO = groupRecordService.get(orderEntity.getGroupRecordId());
        if (null != groupRecordDTO) {
            log.info("取消拼团订单，groupRecordDTO:{}", groupRecordDTO);
            GroupDTO groupDTO = groupService.get(groupRecordDTO.getGroupId());

            // 如果拼团活动结束，则更新普通商品库存；或者拼团活动被删除
            if (null == groupDTO || groupDTO.getActivityStatus() == GroupsEnum.ACTIVITY_STATUS_END.value()) {
                // 更新库存,规格删除放弃增加库存
                flag = goodsSpecService.updateStorage(params, 1);
            }

            // 判断当前订单是拼团中还是已经成团；已经成团则不修改拼团记录信息；
            if (orderEntity.getGroupStatus() == GroupsEnum.GROUP_STATUS_ONGOING.value() && orderEntity.getPaymentStatus() == PaymentStatusEnum.YES.value()) {
                // 如果是拼团订单取消，则需要更新拼团记录和拼团订单数据

                // 修改订单的拼团状态为拼团失败
                List<Long> orderIds = new ArrayList<>();
                orderIds.add(orderEntity.getId());
                log.info("更新拼团订单为失败，orderIds:{}", orderIds);
                this.updateGroupStatus(orderIds, OrderGroupStatusEnum.GROUP_FAIL.value());

                // 更新拼团记录
                Long groupMember = groupRecordDTO.getMemberId();
                Integer groupOrderPeople = 0;
                // 1.判断当前用户是否是拼团的发起人
                if (groupMember.equals(orderEntity.getBuyerId())) {
                    // 2.查询当前拼团记录下是否还有其他订单
                    Map<String, Object> groupMap = new HashMap<>();
                    groupMap.put("groupRecordId", groupRecordDTO.getId());
                    groupMap.put("storeId", groupRecordDTO.getStoreId());
                    groupMap.put("orderStatus", OrderStatusEnum.WAITTING_SHIPPED.value());
                    log.info("当前记录是否有其他订单，groupMap:{}", groupMap);
                    List<GroupOrderDetailDTO> groupOrderList = this.findGroupOrderList(groupMap);
                    log.info("当前记录是否有其他订单结果，groupOrderList:{}", groupOrderList);
                    // 3.如果不为空，则更新拼团记录人数+1
                    if (CollectionUtils.isNotEmpty(groupOrderList)) {
                        groupOrderPeople = groupRecordDTO.getNeedNum() + 1;
                        groupRecordDTO.setNeedNum(groupOrderPeople);
                        log.info("发起人更新拼团记录人数，groupRecordDTO:{}", groupRecordDTO);
                        groupRecordService.update(groupRecordDTO);
                        // 4.如果为空，则修改拼团记录状态为失败
                    } else {
                        groupRecordDTO.setNeedNum(0);
                        groupRecordDTO.setGroupStatus(GroupsEnum.GROUP_STATUS_FAIL.value());
                        log.info("发起人更新拼团记录为失败，groupRecordDTO:{}", groupRecordDTO);
                        groupRecordService.update(groupRecordDTO);
                    }
                    // 5.更新拼团记录人数+1
                } else {
                    groupOrderPeople = groupRecordDTO.getNeedNum() + 1;
                    groupRecordDTO.setNeedNum(groupOrderPeople);
                    log.info("参与人更新拼团记录人数，groupRecordDTO:{}", groupRecordDTO);
                    groupRecordService.update(groupRecordDTO);
                }

                OrderEntity groupCancelEntity = new OrderEntity();
                groupCancelEntity.setGroupNeedNum(groupOrderPeople);
                UpdateWrapper<OrderEntity> wrapper = new UpdateWrapper<>();
                wrapper.eq("group_record_id", orderEntity.getGroupRecordId());
                log.info("订单取消更新其他订单所需人数，groupCancelEntity{}", groupCancelEntity);
                this.update(groupCancelEntity, wrapper);

                // 取消订单移除用户的头像
                log.info("取消订单移除头像，memberid:{},{}", orderEntity.getBuyerId(), orderEntity.getGroupRecordId());
                groupMemberService.deleteMember(orderEntity.getBuyerId(), orderEntity.getGroupRecordId());

            }

        }
    }

    /**
     * 功能描述:
     * 〈返回已使用优惠券〉
     *
     * @param orderEntity 订单实体
     * @author : 刘远杰
     */
    private void returnCoupons(OrderEntity orderEntity) {
        if (StringUtils.isNotBlank(orderEntity.getMemberCouponsId())) {
            List<Long> memberCouponsIds = new ArrayList<>();

            // 获取订单保存的会员优惠券信息
            JSONObject jsonObject = JSONObject.parseObject(orderEntity.getMemberCouponsId());
            jsonObject.values().stream().filter(Objects::nonNull).forEach(o -> memberCouponsIds.add(Long.parseLong(o.toString())));

            if (CollectionUtils.isNotEmpty(memberCouponsIds)) {
                // 修改会员会优惠券状态
                Boolean flag = memberCouponsService.returnMemberCoupons(memberCouponsIds, orderEntity.getBuyerId());
                if (!flag) {
                    log.info("返回已使用优惠券异常");
                    throw new ServiceException(ActivityStatusCode.RETURN_MEMBER_COUPONS_EXCEPTION);
                }
            }
        }
    }

    /**
     * 保存日志
     *
     * @param buyerId     买家id
     * @param storeId     店铺id
     * @param orderEntity 订单实体
     * @param now         操作时间
     */
    private void saveOrderLog(Long buyerId, Long storeId, OrderEntity orderEntity, Date now) {
        // 定义订单日志实体
        OrderLogDTO orderLog = null;

        // 封装订单日志信息
        if (buyerId != null) {
            orderLog = this.getOrderLog(orderEntity.getId(), orderEntity.getBuyerName(),
                    OperatorTypeEnum.BUYER.value(), OrderStatusEnum.CANCELED.value(), null, "用户取消订单", now);
        } else if (storeId != null) {
            orderLog = this.getOrderLog(orderEntity.getId(), orderEntity.getStoreName(),
                    OperatorTypeEnum.SELLER.value(), OrderStatusEnum.CANCELED.value(), null, "商家取消订单", now);
        } else {
            orderLog = this.getOrderLog(orderEntity.getId(), "平台",
                    OperatorTypeEnum.ADMIN.value(), OrderStatusEnum.CANCELED.value(), null, "支付超时自动取消订单", now);
        }

        //发送MQ消息保存订单日志
        String message = JSONObject.toJSONString(orderLog);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE, message);
    }

    /**
     * 给用户推送信息
     *
     * @param orderEntity
     */
    private void cancelOrderSendMessage(OrderEntity orderEntity) {
        /**
         * 超时自动取消订单（微信  aHooatWUHAklsnYHkW3XKYuk_50b7btDAi0ONtpdg-U）：
         * {{first.DATA}}
         * 订单金额：{{orderProductPrice.DATA}}
         * 商品详情：{{orderProductName.DATA}}
         * 收货信息：{{orderAddress.DATA}}
         * 订单编号：{{orderName.DATA}}
         * {{remark.DATA}}
         *
         * 您的京东订单已取消
         * 订单金额：44.20元
         * 商品详情：七匹狼真皮鞋包七匹狼真皮鞋包
         * 收货信息：深圳市宝安区海秀路23号
         * 订单编号：8976541236598771
         * 点击“详情”查看详细处理信息，如有疑问可直接回复此公众号联系京东客服。
         * 详情 >
         *
         * 超时自动取消订单（短信 SMS_193231745）
         * 尊敬的客户，您的订单${orderSn}已经超出支付时间，系统自动取消订单。给您带来不便敬请谅解！
         *
         * 超时自动取消订单（站内信、APP）
         * 您的订单${orderSn}已经超出支付时间，系统自动取消订单。
         */
        Map<String, Object> map = new HashMap<>(16);
        // 获取用户信息
        MemberDTO memberDTO = memberService.getById(orderEntity.getBuyerId());

        // 创建发送站内信用户信息集合
        Map<Long, String> memberMap = new HashMap<>(5);
        memberMap.put(memberDTO.getId(), memberDTO.getMemberName());
        map.put("memberMap", memberMap);

        //设置用户手机号、站内信数据
        map.put("mobile", memberDTO.getMemberMobile());
        map.put("wechatOpenId", memberDTO.getWechatOpenid());

        // 创建短信所需参数Map
        //您的订单${orderSn}已经超出支付时间，系统自动取消订单。
        //尊敬的客户，您的订单${orderSn}已经超出支付时间，系统自动取消订单。给您带来不便敬请谅解！
        Map<String, String> paramsMap = new HashMap<>(16);
        paramsMap.put("orderSn", String.valueOf(orderEntity.getOrderSn()));
        map.put("paramMap", paramsMap);

        // 创建微信公众号推送信息所需参数Map
        /**
         *          * 超时自动取消订单（微信  aHooatWUHAklsnYHkW3XKYuk_50b7btDAi0ONtpdg-U）：
         *          * {{first.DATA}}
         *          * 订单金额：{{orderProductPrice.DATA}}
         *          * 商品详情：{{orderProductName.DATA}}
         *          * 收货信息：{{orderAddress.DATA}}
         *          * 订单编号：{{orderName.DATA}}
         *          * {{remark.DATA}}
         */
        Map<String, String> wechatParamsMap = new HashMap<>(16);
        //提醒
        wechatParamsMap.put("first", "您的订单已超时取消");
        //订单金额
        wechatParamsMap.put("orderProductPrice", String.valueOf(orderEntity.getOrderAmount()));

        //商品详情
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();
        if ((orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()
                || orderEntity.getOrderStatus() == OrderStatusEnum.CANCELED.value())
                && orderEntity.getIsSplit() == OrderSplitEnum.YES.value()) {
            // 订单商品
            orderGoodsDTOList = orderGoodsService.findOrderGoodsByParentOrderSn(orderEntity.getOrderSn());
        } else {
            // 订单商品
            orderGoodsDTOList = orderGoodsService.getByOrderId(orderEntity.getId(), null, null);
        }
        if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
            String firstGoodsName = String.valueOf(orderGoodsDTOList.get(0).getGoodsName());
            wechatParamsMap.put("orderProductName", firstGoodsName);
        }
        //收货信息
        OrderAddressDTO orderAddress =
                Optional.ofNullable(orderAddressService.get(orderEntity.getAddressId())).orElseGet(OrderAddressDTO::new);
        StringBuilder sb = new StringBuilder();
        sb.append(orderAddress.getTrueName());
        sb.append(orderAddress.getMobPhone());
        sb.append(orderAddress.getAddress());
        sb.append(orderAddress.getAreaInfo());
        wechatParamsMap.put("orderAddress", sb.toString());
        //订单编号
        wechatParamsMap.put("orderName", String.valueOf(orderEntity.getOrderSn()));
        //退款时间
//        wechatParamsMap.put("keyword5", DateUtils.getDateStr(DateUtils.DATE_TIME_PATTERN));
        //备注
        wechatParamsMap.put("remark", "点击“查看详情”查看详细处理信息，如有疑问可直接回复此公众号联系雷铭客服。 ");

        map.put("wechatParamsJson", JSON.toJSONString(wechatParamsMap));
        map.put("wehcatClickUrl",
                AddressPrefixProperties.adddressPrefix + "#/pagesD/order/orderDet?paySn=" + orderEntity.getPaySn() +
                        "%26id" +
                        "=" + orderEntity.getId());
        // 创建消息实体
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(MessageCodeEnum.ORDER_TIME_OUT_CANCEL.value());
        messageDTO.setSendName("系统");
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
    }

    /**
     * 定时取消订单
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public Boolean orderCanceledTiming(@RequestBody List<Long> orderSnList) {
        // 1.根据paysn查询订单
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("payment_status", PaymentStatusEnum.NO.value());
        wrapper.in(CollectionUtils.isNotEmpty(orderSnList), "order_sn", orderSnList);
        List<OrderEntity> orderEntities = baseDao.selectList(wrapper);

        Date now = new Date();
        // 定义日志更新集合
        List<OrderEntity> logs = new ArrayList<>();

        // 2.获取所有订单及子订单日志保存对象
        for (OrderEntity orderEntity : orderEntities) {
//             TODO: 2020/6/16/016  xuzhch 超时取消订单
            this.cancelOrderSendMessage(orderEntity);
            // 添加日志更新对象
            logs.add(orderEntity);

            // 订单存在拆单
            if (orderEntity.getOrderStatus() == OrderStatusEnum.WAITTING_PAYMENT.value()
                    && orderEntity.getIsSplit() == OrderSplitEnum.YES.value()) {
                // 查询全部子订单
                QueryWrapper<OrderEntity> wrapperTemp = new QueryWrapper<>();
                wrapperTemp.eq("parent_order_sn", orderEntity.getOrderSn());
                List<OrderEntity> orderDetail = baseDao.selectList(wrapperTemp);

                for (OrderEntity entity : orderDetail) {
                    // 添加日志更新对象
                    logs.add(entity);
                    orderSnList.add(entity.getOrderSn());
                }
            }

            // 3.取消订单
            baseDao.updateOrderStatusByPaySn(orderSnList, OrderStatusEnum.CANCELED.value(),
                    OrderAppStatusEnum.APP_CANCELED.value(), "超时自动取消订单", now);

            // 4.取消订单返回优惠券
            this.returnCoupons(orderEntity);

            // 5.订单取消修改商品库存
            List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();
            if (OrderSplitEnum.YES.value() == orderEntity.getIsSplit()) {
                // 父订单查询订单商品
                orderGoodsDTOList = orderGoodsService.findOrderGoodsByParentOrderSn(orderEntity.getOrderSn());
            } else {
                // 子订单查询订单商品
                orderGoodsDTOList = orderGoodsService.getByOrderId(orderEntity.getId(), null, orderEntity.getStoreId());
            }
            if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
                Map<String, String> params = new HashMap<>();
                for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                    //库存
                    String num = String.valueOf(-orderGoodsDTO.getGoodsNum());
                    params.put(orderGoodsDTO.getSpecId().toString(), num);
                }
                // 更新库存,规格删除放弃增加库存
                Integer type = 1;
                boolean flag = true;
                // 判断当前订单是否是拼团订单，如果是的话，则不更新普通商品的库存
                if (null == orderEntity.getGroupRecordId()) {
                    // 更新库存,规格删除放弃增加库存
                    flag = goodsSpecService.updateStorage(params, type);
                }
                if (!flag) {
                    log.info("取消订单失败，更新库存失败");
                    throw new ServiceException(OrderStatusCode.CANCEL_ORDER_UPDATE_STROAGR_SALE_FAIL);
                }
                // 取消订单返回活动库存
                orderCancelReturnSeckillStorage(orderGoodsDTOList);
            }
        }
        // 保存订单取消日志
        logs.forEach(entity -> saveOrderLog(null, null, entity, now));
        return true;
    }

    /**
     * 根据用户id 订单状态查询订单数量
     *
     * @param id
     * @param orderStatus
     * @return
     */

    @Override
    public Integer findOrderNum(Long id, @RequestParam("orderStatus") Integer orderStatus,
                                @RequestParam(value = "evaluateStatus", required = false) Integer evaluateStatus) {
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper();
        wrapper.eq("buyer_id", id);
        if (orderStatus != null) {
            wrapper.eq("order_status", orderStatus);
        }
        if (evaluateStatus != null) {
            wrapper.eq("evaluate_status", evaluateStatus);
        }
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        return baseDao.selectCount(wrapper);
    }


    /**
     * 获取所有未付款可取消的订单
     *
     * @param
     * @return
     */

    @Override
    public List<OrderDTO> orderCanceledList(@RequestParam("delDate") Date delDate) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("order_status", OrderStatusEnum.WAITTING_PAYMENT);
        wrapper.ge("create_date", delDate);
        return baseDao.selectList(wrapper);
    }

    /**
     * 删除订单
     *
     * @param id
     */

    @Override
    public void delOrder(Long id, @RequestParam("buyerId") Long buyerId) {
        // 查询用户订单信息
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        wrapper.eq(buyerId != null, "buyer_id", buyerId);
        OrderEntity orderEntity = baseDao.selectOne(wrapper);
        if (null == orderEntity) {
            throw new ServiceException("订单不存在");
        }
        if (orderEntity.getOrderStatus() == OrderStatusEnum.COMPLETE.value() || orderEntity.getOrderStatus() == OrderStatusEnum.CANCELED.value()) {
            //已完成或已取消
            OrderEntity updateEntity = new OrderEntity();
            updateEntity.setId(orderEntity.getId());
            updateEntity.setShowFlag(OrderShowFlagEnum.YES.value());
            baseDao.updateById(updateEntity);
        } else {
            throw new ServiceException(OrderStatusCode.ORDER_CHANGE_SHOW_STATUS_EXCERTION);
        }
    }

    /**
     * 功能描述:
     * 〈根据支付单号查询订单〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    @Override
    public List<OrderDTO> getByPaySn(@RequestParam("paySn") Long paySn) {

        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(paySn != null, "pay_sn", paySn);
        List<OrderEntity> orderEntities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(orderEntities, OrderDTO.class);


    }

    /**
     * 功能描述:
     * 〈支付异步修改订单状态〉
     *
     * @param updateOrderDTO 修改订单状态
     * @author : 刘远杰
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public boolean updateOrderStatePayFinish(UpdateOrderDTO updateOrderDTO) {

        Map<String, String> logMap = new HashMap<>();
        logMap.put("paySn", updateOrderDTO.getPaySn().toString());
        logMap.put("paymentId", updateOrderDTO.getPaymentId().toString());
        logMap.put("paymentName", updateOrderDTO.getPaymentName().toString());
        logMap.put("transactionId", updateOrderDTO.getTransactionId());

        // 1.根据支付单号查询订单
        List<OrderDTO> orderDTOList = getByPaySn(updateOrderDTO.getPaySn());

        log.info("根据支付单号查询订单orderDTOList:{}", orderDTOList);

        if (CollectionUtils.isEmpty(orderDTOList)) {
            mlogger.info(OrderStatusCode.ORDER_STATE_PAY_UPDATE_SERVER_RESPONSE_VALIDATION_ERRORA_CODE,
                    OrderStatusCode.ORDER_STATE_PAY_UPDATE_SERVER_RESPONSE_VALIDATION_ERRORA_MESSAGE, logMap);
            log.info("修改订单状态失败，订单不存在");
            return false;
        }

        // 3.修改订单状态
        // 3.1 新建修改订单实体
        Date now = new Date();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPaymentId(updateOrderDTO.getPaymentId());
        orderEntity.setPaymentName(updateOrderDTO.getPaymentName());
        orderEntity.setPaymentCode(updateOrderDTO.getPaymentCode());
        orderEntity.setPaymentStatus(PaymentStatusEnum.YES.value());
        orderEntity.setPaymentTime(now);
        orderEntity.setTradeSn(updateOrderDTO.getTransactionId());


        List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsService.getByPaySn(updateOrderDTO.getPaySn());

        log.info("根据paySn查询订单商品集合orderGoodsDTOList:{}", orderGoodsDTOList);

        // 拼团成功取消标识
        Integer groupCancelFlag = 0;

        if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
            Map<String, String> params = new HashMap<>();
            OrderGoodsDTO orderGoodsDTO = orderGoodsDTOList.get(0);
            params.put(orderGoodsDTO.getSpecId().toString(), String.valueOf(orderGoodsDTO.getGoodsNum()));

            // 判断订单是不是拼团订单，如果是的话需要更新拼团记录信息，和拼团会员信息
            if (orderGoodsDTO.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value()) {
                // 拼团记录相关处理
                this.groupRecord(orderDTOList, orderGoodsDTO, orderEntity, groupCancelFlag);
            }
            // 与产品确认订单完成后更新销量
//            // 更新销量,已删除规格抛出异常
//            Integer type = 0;
//            boolean flag = goodsSpecService.updateSale(params, type);
//            if (!flag) {
//                log.info("异步修改订单状态失败，更新销量失败");
//                throw new ServiceException(OrderStatusCode.CANCEL_ORDER_UPDATE_STROAGR_SALE_FAIL);
//            }
        }

        // 3.2 构造修改条件


        // 判断当前订单是不是电子提货码配送方式，如果是的话，支付完成后订单状态就变成已完成；
        for (OrderDTO orderDTO : orderDTOList) {
            if (orderDTO.getVirtualFlag() == VirtualFlagEnum.YES.value()) {
                // 处理电子提货码
                this.saveFetchCode(orderDTO, orderEntity, updateOrderDTO, orderGoodsDTOList);
            } else {
                orderEntity.setOrderStatus(OrderStatusEnum.WAITTING_SHIPPED.value());
                orderEntity.setAppStatus(OrderAppStatusEnum.APP_WAITTING_SHIPPED.value());
                UpdateWrapper<OrderEntity> wrapper = new UpdateWrapper<>();
                wrapper.eq("pay_sn", updateOrderDTO.getPaySn());
                log.info("保存的订单实体，orderEntity{}", orderEntity);
                this.update(orderEntity, wrapper);
            }
        }


        logMap.put("orderEntity", orderEntity.toString());
        log.info("异步修改订单状态成功,orderEntity:{},paySn:{}", orderEntity, updateOrderDTO.getPaySn());

        // 拼团成功取消其他未支付订单
        if (groupCancelFlag == 1) {
            log.info("拼团成功取消其他未支付订单groupCancelFlag");
            this.cancleGroupOrder(orderDTOList.get(0).getStoreId(), orderDTOList.get(0).getGroupRecordId());
        }

        for (OrderDTO orderDTO : orderDTOList) {
            //新建一个订单日志
            OrderLogDTO orderLogDTO = this.getOrderLog(orderDTO.getId(), orderDTO.getBuyerName(),
                    OperatorTypeEnum.BUYER.value(),
                    OrderStatusEnum.WAITTING_SHIPPED.value(), OrderStatusEnum.WAITTING_RECEIVED.value(), "付款成功", now);
            //发送MQ消息保存订单日志
            String message = JSONObject.toJSONString(orderLogDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE, message);

        }

        // 发送支付成功通知
        if (orderDTOList.size() == 1) {
            orderDTOList.get(0).setPaymentTime(now);
            this.sendPayMessage(orderDTOList.get(0), orderGoodsDTOList);
        }
        if (orderDTOList.size() > 1) {
            for (OrderDTO orderDTO : orderDTOList) {
                if (OrderSplitEnum.YES.value() == orderDTO.getIsSplit()) {
                    orderDTO.setPaymentTime(now);
                    this.sendPayMessage(orderDTO, orderGoodsDTOList);
                    break;
                }
            }
        }

        return true;
    }

    /**
     * 处理虚拟订单中的电子提货码数据
     *
     * @param orderDTO
     * @param orderEntity
     * @param updateOrderDTO
     * @param orderGoodsDTOList
     * @return
     * @date 2020-07-08 19:56
     * @author huangkeyuan@leimingtech.com
     **/
    private void saveFetchCode(OrderDTO orderDTO, OrderEntity orderEntity, UpdateOrderDTO updateOrderDTO,
                               List<OrderGoodsDTO> orderGoodsDTOList) {

        if (null != orderDTO.getDevlierType() && orderDTO.getDevlierType().equals(DevlierTypeEnum.FETCH_CODE.value())) {
            // 电子提货码付款成功后，订单状态变成已完成
            orderEntity.setOrderStatus(OrderStatusEnum.WAITTING_RECEIVED.value());
            orderEntity.setAppStatus(OrderAppStatusEnum.APP_WAITTING_RECEIVED.value());
            UpdateWrapper<OrderEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("pay_sn", updateOrderDTO.getPaySn());
            wrapper.eq("devlier_type", DevlierTypeEnum.FETCH_CODE.value());
            wrapper.eq("id", orderDTO.getId());
            this.update(orderEntity, wrapper);
            log.info("电子提货码虚拟订单自动进行确认收货，orderEntity{}", orderEntity);

            this.confirmReceipt(orderDTO.getId(), orderDTO.getBuyerId());
            // 根据商品维度生成电子提货码
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                if (orderGoodsDTO.getOrderId().equals(orderDTO.getId())) {
                    FetchCodeDTO fetchCodeDTO = new FetchCodeDTO();
                    BeanCopier.create(OrderGoodsDTO.class, FetchCodeDTO.class, false).copy(orderGoodsDTO,
                            fetchCodeDTO, null);
                    fetchCodeDTO.setId(IdGenerator.defaultSnowflakeId());
                    fetchCodeDTO.setOrderGoodsId(orderGoodsDTO.getId());
                    fetchCodeDTO.setMemberId(orderGoodsDTO.getBuyerId());
                    fetchCodeDTO.setMemberName(orderDTO.getBuyerName());
                    fetchCodeDTO.setGoodsSerial(orderGoodsDTO.getSpecId().toString());
                    fetchCodeDTO.setGoodsPrice(orderGoodsDTO.getSpecPrice());
                    fetchCodeDTO.setCodeStatus(CodeStatusEnum.WATTING_CHECK.value());
                    fetchCodeDTO.setExchangeNum(0);
                    fetchCodeDTO.setSpecId(orderGoodsDTO.getSpecId());

                    // 获取商品过期天数
                    GoodsDTO goodsDTO = goodsService.get(orderGoodsDTO.getGoodsId());
                    fetchCodeDTO.setValidDay(goodsDTO.getCodeValidDay());
                    Integer validDay = goodsDTO.getCodeValidDay();
                    if (validDay != -1) {
                        Date nowDate = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(nowDate);
                        calendar.add(Calendar.DAY_OF_YEAR, validDay);
                        Date expiryDate = calendar.getTime();
                        fetchCodeDTO.setExpiryDate(expiryDate);
                    }

                    String codeNumber = ShareCodeUtils.idToCode(IdGenerator.defaultSnowflakeId());
                    fetchCodeDTO.setFetchCode(codeNumber);

                    // 设置订单商品的电子提货码，方便发送短信通知
                    orderGoodsDTO.setFetchCode(codeNumber);
                    String codeImage = uploadService.uploadQrCode(codeNumber);
                    fetchCodeDTO.setFetchCodeImage(codeImage);
                    log.info("生成电子提货码记录，fetchCodeDTO{}", fetchCodeDTO);
                    fetchCodeService.save(fetchCodeDTO);
                }
            }
        } else {
            orderEntity.setOrderStatus(OrderStatusEnum.WAITTING_SHIPPED.value());
            orderEntity.setAppStatus(OrderAppStatusEnum.APP_WAITTING_SHIPPED.value());
            UpdateWrapper<OrderEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("pay_sn", updateOrderDTO.getPaySn());
            wrapper.eq("id", orderDTO.getId());
            log.info("保存的订单实体，orderEntity{}", orderEntity);
            this.update(orderEntity, wrapper);
        }
    }

    private void groupRecord(List<OrderDTO> orderDTOList, OrderGoodsDTO orderGoodsDTO, OrderEntity orderEntity,
                             Integer groupCancelFlag) {
        Long groupRecordId = orderDTOList.get(0).getGroupRecordId();

        // 查询拼团的基本信息
        GroupDTO groupDTO = groupService.get(orderGoodsDTO.getActivityId());

        GroupRecordDTO groupRecordDTO = groupRecordService.get(groupRecordId);

        log.info("获取到的拼团记录实体，groupRecordDTO{}", groupRecordDTO);
        // 发起拼团
        if (null == groupRecordDTO) {
            groupRecordDTO = new GroupRecordDTO();
            groupRecordDTO.setGroupName(groupDTO.getGroupName());
            groupRecordDTO.setGroupId(groupDTO.getId());
            groupRecordDTO.setStoreId(orderGoodsDTO.getStoreId());
            groupRecordDTO.setId(groupRecordId);
            groupRecordDTO.setMemberId(orderGoodsDTO.getBuyerId());
            groupRecordDTO.setMemberName(orderDTOList.get(0).getBuyerName());
            groupRecordDTO.setGoodsName(orderGoodsDTO.getSpuName());
            groupRecordDTO.setGoodsNum(orderGoodsDTO.getGoodsNum());
            groupRecordDTO.setSpecId(orderGoodsDTO.getSpecId());
            groupRecordDTO.setGoodsId(orderGoodsDTO.getGoodsId());
            groupRecordDTO.setStartTime(new Date());
            groupRecordDTO.setGoodsMainPicture(orderGoodsDTO.getGoodsImage());

            // 拼团超时时间
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.HOUR_OF_DAY, groupDTO.getValidTime());
            groupRecordDTO.setOverTime(cal.getTime());

            // 拼团成团人数
            ActivityGoodsDTO activityGoodsDTO = activityGoodsService.goodsAndSpec(orderGoodsDTO.getActivityId(),
                    orderGoodsDTO.getSpecId(), ActivityTypeEnum.GROUP_ACTIVITY.value());

            Integer needNum = activityGoodsDTO.getRegimentNum() - 1;
            groupRecordDTO.setNeedNum(needNum);
            groupRecordDTO.setGroupStatus(GroupsEnum.GROUP_STATUS_ONGOING.value());

            // 设置订单表的拼团超时时间
            orderEntity.setGroupOverTime(cal.getTime());
            orderEntity.setGroupNeedNum(needNum);
            groupRecordService.save(groupRecordDTO);

            log.info("剩余拼团参与人数{}", needNum);
            log.info("保存的发起拼团订单实体，orderEntity{}", orderEntity);

        } else {

            orderEntity.setGroupOverTime(groupRecordDTO.getOverTime());

            log.info("参与拼团的记录dto{}", groupRecordDTO);

            Integer joinNum = groupRecordDTO.getNeedNum() - 1;
            // 拼团已存在，属于加入拼团
            groupRecordDTO.setNeedNum(joinNum);
            OrderEntity groupActivityEntity = new OrderEntity();

            log.info("剩余拼团参与人数{}", joinNum);

            // 拼团所需成团人数为0 说明拼团成功
            if (joinNum <= 0) {
                groupRecordDTO.setGroupStatus(GroupsEnum.GROUP_STATUS_SUCCESS.value());
                // 设置实际成团时间
                groupRecordDTO.setActualTime(new Date());
                // 拼团成功，成团个数+1
                groupDTO.setGroupNum(groupDTO.getGroupNum() + 1);

                // 更新该拼团记录下的所有订单拼团状态为拼团成功
                groupActivityEntity.setGroupNeedNum(0);
                groupActivityEntity.setGroupStatus(GroupsEnum.GROUP_STATUS_SUCCESS.value());

                groupCancelFlag = 1;


            } else if (joinNum > 0) {
                // 更新该拼团记录下的所有订单的拼团所需人数
                groupActivityEntity.setGroupNeedNum(joinNum);
            }

            UpdateWrapper<OrderEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("group_record_id", groupRecordId);

            log.info("保存的已参加订单实体，groupActivityEntity{}", groupActivityEntity);

            this.update(groupActivityEntity, wrapper);

            log.info("更新拼团记录dto，groupRecordDTO{}", groupRecordDTO);

            groupRecordService.update(groupRecordDTO);


        }
        // 拼团支付订单数
        groupDTO.setPaymentNum(groupDTO.getPaymentNum() + 1);

        groupService.updateByPayEnd(groupDTO);

        MemberVoDTO memberVo = memberService.selectMemberById(orderGoodsDTO.getBuyerId());
        log.info("获取到的会员信息,memberVo: {}", memberVo);
        // 保存拼团会员记录
        GroupMemberDTO groupMemberDTO = new GroupMemberDTO();
        groupMemberDTO.setGroupId(groupDTO.getId());
        groupMemberDTO.setGroupRecordId(groupRecordId);
        groupMemberDTO.setMemberId(orderGoodsDTO.getBuyerId());
        groupMemberDTO.setMemberName(orderDTOList.get(0).getBuyerName());
        groupMemberDTO.setMemberImage(memberVo.getMemberAvatar());
        log.info("保存拼团会员记录,groupMemberDTO: {}", groupMemberDTO);
        groupMemberService.save(groupMemberDTO);

    }

    /**
     * 拼团成功，取消该团下的其他未支付订单
     *
     * @param storeId       店铺id
     * @param groupRecordId 拼团记录id
     * @return
     * @date 2020-04-16 14:58
     * @author huangkeyuan@leimingtech.com
     **/
    private void cancleGroupOrder(Long storeId, Long groupRecordId) {
        // 拼团成功，取消该团下的未支付订单
        // 查询该记录关联的待收货订单数据
        Map<String, Object> findParams = new HashMap<>();
        findParams.put("storeId", storeId);
        findParams.put("groupRecordId", groupRecordId);
        findParams.put("orderStatus", OrderStatusEnum.WAITTING_PAYMENT.value());
        log.info("拼团成功，取消未支付的订单，findParams：{}", findParams);
        List<GroupOrderDetailDTO> groupOrderDetailDTOList = this.findGroupOrderList(findParams);
        log.info("未支付的订单列表，groupOrderDetailDTOList：{}", groupOrderDetailDTOList);

        if (CollectionUtils.isNotEmpty(groupOrderDetailDTOList)) {
            // 取出此记录的所有待收货订单id
            List<Long> orderIds =
                    groupOrderDetailDTOList.stream().map(GroupOrderDetailDTO::getId).collect(Collectors.toList());
            Long recordStoreId = storeId;
            log.info("需要取消的订单ids：{}", orderIds);
            // 批量取消订单
            orderIds.forEach(orderId -> {
                OrderCancelDTO orderCancelDTO = new OrderCancelDTO();
                orderCancelDTO.setId(orderId);
                orderCancelDTO.setReasonId(0L);
                log.info("取消的订单orderCancelDTO：{}", orderCancelDTO);
                this.cancelOrderSeller(orderCancelDTO, recordStoreId);

            });

            // 批量修改订单的拼团状态为拼团失败
            log.info("批量修改订单的拼团状态为拼团失败orderIds：{}", orderIds);
            this.updateGroupStatus(orderIds, OrderGroupStatusEnum.GROUP_FAIL.value());
        }
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:根据自定义参数查询订单信息
     * @Date: 2019/6/21 16:30
     * @Version: V1.0
     */
    @Override

    public OrderDTO getOrderByMap(@RequestParam Map<String, Object> map) {
        OrderDTO orderDTO = new OrderDTO();
        String id = "";
        String parentOrderSn = "";
        String orderSn = "";
        if (null != map.get("id")) {
            id = map.get("id").toString();
        }
        if (null != map.get("parentOrderSn")) {
            parentOrderSn = map.get("parentOrderSn").toString();
        }
        if (null != map.get("orderSn")) {
            orderSn = map.get("orderSn").toString();
        }
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(parentOrderSn), "parent_order_sn", parentOrderSn);
        wrapper.eq(StringUtils.isNotBlank(orderSn), "order_sn", orderSn);
        OrderEntity orderEntity = baseDao.selectOne(wrapper);
        if (null != orderEntity) {
            BeanCopier.create(OrderEntity.class, OrderDTO.class, false).copy(orderEntity, orderDTO, null);
        }
        return orderDTO;
    }


    /**
     * 购物车提交订单
     *
     * @param insertCartSaveOrderVoDTO
     * @return 封装返回信息 code:状态码 msg:错误信息描述 data:数据  (代码待优化)
     * @date 2019/6/22 14:16
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    // 不支持整体事务，保存方法单独进行事务控制，解决确认表保存完成事务未结束mq执行消息查不到确认表数据
    @Transactional(propagation = Propagation.SUPPORTS)
    @Lock4j(keys = "#insertCartSaveOrderVoDTO.buyerId")
    public OrderSaveResultDTO saveOrderToCart(@RequestBody InsertCartSaveOrderVoDTO insertCartSaveOrderVoDTO) {
        // 转化会员优惠券格式： json -> map(key:店铺id,value:会员优惠券id)
        Map<String, Long> memberCouponsId = new HashMap<>();
        if (StringUtils.isNotBlank(insertCartSaveOrderVoDTO.getMemberCouponsId())) {
            JSONObject jsonObject = JSONObject.parseObject(insertCartSaveOrderVoDTO.getMemberCouponsId());
            jsonObject.keySet().stream().filter(key -> jsonObject.get(key) != null).forEach(key -> memberCouponsId.put(key,
                    Long.parseLong(jsonObject.get(key).toString())));
        }

        // 获取用户选中购物车商品
        log.info("购物车提交订单获取用户购物车数据，buyerId;{}", insertCartSaveOrderVoDTO.getBuyerId());
        List<CartDTO> cartDTOList = cartService.findListBySelect(insertCartSaveOrderVoDTO.getBuyerId());

        if (CollectionUtils.isEmpty(cartDTOList)) {
            throw new ServiceException(OrderStatusCode.BUY_CART_NO_CHECKED);
        }

        try {
            // 购物车分单，根据店铺拆分购物车  后期校验店铺优惠信息
            List<GoodsConverOrderDTO> goodsConverOrderList = cartService.findVoListByCartIds(cartDTOList);

            // 校验库存，价格是否变动，规格是否存在，商品是否上架  后期需要校验活动信息
            Map<String, List<CartGoodsDTO>> validationMap = this.cartValidation(goodsConverOrderList);

            // 会员查询
            MemberVoDTO memberVo = memberService.selectMemberById(insertCartSaveOrderVoDTO.getBuyerId());
            MemberGradeDTO memberGradeDTO =
                    memberGradeService.selectByMemberId(memberVo.getMemberInfoDTO().getGradePoint());

            // 秒杀活动校验
            this.cartSeckillValidation(cartDTOList, insertCartSaveOrderVoDTO.getBuyerId(), memberVo, validationMap);

            //限时抢购活动校验
            this.cartFlashSaleValidation(cartDTOList, insertCartSaveOrderVoDTO.getBuyerId(), memberVo, validationMap);

            Map<Long, BigDecimal> shippingAmount = null;
            if (insertCartSaveOrderVoDTO.getAddressId() != 0L) {
                MemberAddressDTO memberAddressDTO =
                        Optional.ofNullable(memberAddressService.get(insertCartSaveOrderVoDTO.getAddressId())).orElse(new MemberAddressDTO());
                if (!memberAddressDTO.getMemberId().equals(insertCartSaveOrderVoDTO.getBuyerId())) {
                    OrderSaveResultDTO orderSaveResultDTO = new OrderSaveResultDTO();
                    orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.MEMBER_CHECK_FAILED.value());
                    orderSaveResultDTO.setMsg("用户地址信息有误，请重新选择！");
                    log.info("购物车提交订单用户地址信息有误");
                    return orderSaveResultDTO;
                }

                // 计算运费
                shippingAmount = this.countFreightAmount(memberAddressDTO, goodsConverOrderList, validationMap);
            }

            // 错误信息封装
            OrderSaveResultDTO orderSaveResultDTO = this.validation(validationMap);
            // 如果存在错误信息直接返回
            if (Optional.ofNullable(orderSaveResultDTO).isPresent()) {
                return orderSaveResultDTO;
            }

            orderSaveResultDTO = new OrderSaveResultDTO();

            Map<Long, String> storeMap = new HashMap<>();
            List<CartDTO> cartDTOS = ConvertUtils.sourceToTarget(cartDTOList, CartDTO.class);
            cartDTOS.stream().filter(Objects::nonNull).forEach(cart -> storeMap.put(cart.getStoreId(),
                    cart.getStoreName()));

            OrderConfirmDTO orderConfirmDTO = new OrderConfirmDTO();

            // 设置用户等级信息
            if (memberGradeDTO != null) {
                orderConfirmDTO.setBuyerGraderId(memberGradeDTO.getId());
                orderConfirmDTO.setBuyerGraderName(memberGradeDTO.getGradeName());
            }
            //封装用户信息
            orderConfirmDTO.setId(IdGenerator.defaultSnowflakeId());
            orderConfirmDTO.setBuyerId(insertCartSaveOrderVoDTO.getBuyerId());
            orderConfirmDTO.setBuyerName(memberVo.getMemberName());
            orderConfirmDTO.setBuyerEmail(memberVo.getMemberEmail());
            orderConfirmDTO.setAddressId(insertCartSaveOrderVoDTO.getAddressId());
            orderConfirmDTO.setOrderPlatform(insertCartSaveOrderVoDTO.getOrderPlatform());
            if (null != insertCartSaveOrderVoDTO.getMemberMobile()) {
                orderConfirmDTO.setVirtualPhone(insertCartSaveOrderVoDTO.getMemberMobile());
                orderConfirmDTO.setVirtualCustomer(insertCartSaveOrderVoDTO.getMemberName());
            }

            //如果只包含一个店铺  设置订单确认表的店铺ID  否则不进行设置
            if (storeMap.keySet().size() == 1) {
                Long storeId = storeMap.keySet().iterator().next();
                orderConfirmDTO.setStoreId(storeId);
                orderConfirmDTO.setStoreName(storeMap.get(storeId));
            } else if (storeMap.keySet().size() > 1) {
                orderConfirmDTO.setStoreId(PLATFORM_ORDER_STORE_ID);
                orderConfirmDTO.setStoreName(PLATFORM_ORDER_STORE_NAME);
            }

            // 订单留言信息 (消费者中解析订单留言)
            orderConfirmDTO.setOrderMessage(insertCartSaveOrderVoDTO.getOrderMessage());

            // 订单优惠券信息
            if (memberCouponsId.size() > 0) {
                orderConfirmDTO.setMemberCouponsId(JSONObject.toJSONString(memberCouponsId));
                log.info("提交订单选择优惠券：{}", memberCouponsId);
            }

            OrderConfirmPriceDTO orderConfirmPriceDTO = this.computePrice(cartDTOList, storeMap, shippingAmount);
            BigDecimal maxOrderAmount = new BigDecimal(10000000);
            if (orderConfirmPriceDTO.getOrderAmount().compareTo(maxOrderAmount) == 1) {
                orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.ORDER_ORTHER_FAILED.value());
                orderSaveResultDTO.setMsg("单笔订单最多不可超过1000万");
                return orderSaveResultDTO;
            }
            orderConfirmDTO.setOrderAmount(orderConfirmPriceDTO.getOrderAmount());
            orderConfirmDTO.setGoodsAmount(orderConfirmPriceDTO.getGoodsAmout());
            orderConfirmDTO.setPreferentialPrice(orderConfirmPriceDTO.getPreferentialPrice());
            orderConfirmDTO.setShippingFee(orderConfirmPriceDTO.getShippingFee());
            if (null != shippingAmount) {
                orderConfirmDTO.setShippingAmount(JSONObject.toJSONString(shippingAmount));
            }
            CartDTO cartDTO = cartDTOList.get(0);
            if (null == cartDTO.getVirtualFlag()) {
                orderConfirmDTO.setVirtualFlag(VirtualFlagEnum.NO.value());
            } else {
                orderConfirmDTO.setVirtualFlag(cartDTO.getVirtualFlag());
            }


            // 判断使用余额支付的话 todo 进行余额冻结
            if (null != insertCartSaveOrderVoDTO.getUseBalance()) {
                if (CartEnum.SETTMENT_USE_BALANCE.value() == insertCartSaveOrderVoDTO.getUseBalance()) {
                    // 查询用户可用余额
                    MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(insertCartSaveOrderVoDTO.getBuyerId());

                    if (null != memberBalanceInfoDTO) {
                        BigDecimal difference = orderConfirmPriceDTO.getOrderAmount().subtract(memberBalanceInfoDTO.getAvailableBalance());

                        // 余额足够支付订单,则订单确认表的余额支付金额为订单的支付总金额，否则为用户的可用余额
                        if (difference.compareTo(BigDecimal.ZERO) < 1) {
                            orderConfirmDTO.setBalanceAmount(orderConfirmPriceDTO.getOrderAmount());
                        } else {
                            orderConfirmDTO.setBalanceAmount(memberBalanceInfoDTO.getAvailableBalance());
                        }
                    }

                }
            }

            //保存订单确认信息
            orderConfirmService.save(orderConfirmDTO);

            //封装并保存订单确认信息
            insertCartSaveOrderVoDTO.getOrderInvoiceSaveDTOS().forEach(o -> {
                o.setOrderId(orderConfirmDTO.getId());
            });
            log.info("购物车购买保存订单确认表成功");

            if (CollectionUtils.isNotEmpty(insertCartSaveOrderVoDTO.getOrderInvoiceSaveDTOS())) {
                orderInvoiceService.saveBatch(insertCartSaveOrderVoDTO.getOrderInvoiceSaveDTOS());
            }
            //保存商品信息
            this.saveOrderGoodsConfirm(orderConfirmDTO.getId(), cartDTOList, insertCartSaveOrderVoDTO.getBuyerId(),
                    memberCouponsId);

            // 删除购物车内商品
            log.info("购物车购买提交订单，删除购物车选中商品，buyerId:{}", insertCartSaveOrderVoDTO.getBuyerId());
            cartService.deleteIsSelectCart(insertCartSaveOrderVoDTO.getBuyerId());
            log.info("购物车购买提交订单，删除购物车选中商品成功");

            // redis记录订单保存
            String key = RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId();
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.WAITING);
            saveOrderRedisDTO.setResultMsg("订单保存中");
            redisUtils.set(key, saveOrderRedisDTO, RedisConstants.JEDIS_EXPIRE);

            //发送MQ消息进行异步处理订单拆单等信息
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CONFIRM_ORDER_QUEUE, orderConfirmDTO.getId().toString());

            // 返回订单确认表的ID给前台，前台根据ID定时查询接口，获取支付信息
            orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.ALL_CHECK_SUCCESS.value());
            orderSaveResultDTO.setOrderId(orderConfirmDTO.getId());
            return orderSaveResultDTO;
        } catch (Exception e) {
            log.error("用户Id:{}执行购物车保存订单未知异常:{}", insertCartSaveOrderVoDTO.getBuyerId(), e);
            throw new ServiceException(OrderStatusCode.SAVE_ORDER_UNKNOWN_ERROR);
        }
    }

    private void cartFlashSaleValidation(List<CartDTO> cartDTOList, Long buyerId, MemberVoDTO memberVo, Map<String, List<CartGoodsDTO>> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        // 活动不存在集合
        List<CartGoodsDTO> activityNotExistList = Lists.newArrayList();
        // 活动未开始集合
        List<CartGoodsDTO> activityPreList = Lists.newArrayList();
        // 活动已结束集合
        List<CartGoodsDTO> activityStopList = Lists.newArrayList();
        // 库存不足商品集合
        List<CartGoodsDTO> activityStorageLackList = Lists.newArrayList();
        // 价格变动商品集合
        List<CartGoodsDTO> activityPriceList = Lists.newArrayList();
        // 会员等级不足商品集合
        List<CartGoodsDTO> activityMemberGradeLackList = Lists.newArrayList();
        // 超出限购商品集合
        List<CartGoodsDTO> activityOverLimitList = Lists.newArrayList();
        //判断是否扣减库存
        List<CartGoodsDTO> decrementList = Lists.newArrayList();

        List<CartGoodsDTO> cartGoodsDTOList = ConvertUtils.sourceToTarget(cartDTOList, CartGoodsDTO.class);
        for (CartGoodsDTO cartGoodsDTO : cartGoodsDTOList) {
            // 校验限时抢购活动状态、商品库存
            if (cartGoodsDTO.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() && cartGoodsDTO.getActivityId() != null) {
                // 查询限时抢购活动
                FlashSaleActivityDTO flashSaleActivityDTO = flashSaleActivityService.get(cartGoodsDTO.getActivityId());
                //限时限时抢购活动校验
                if (flashSaleActivityDTO == null) {
                    activityNotExistList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
                } else if (flashSaleActivityDTO.getActivityState() == FlashSaleActivityEnum.ACTIVITY_STATE_PREPARE.value()) {
                    activityPreList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
                } else if (flashSaleActivityDTO.getActivityState() == FlashSaleActivityEnum.ACTIVITY_STATE_END.value()) {
                    activityStopList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
                }

                // 校验限时抢购活动商品价格
                String priceKey =
                        ActivityRedisConstants.FLASH_GOODS_PRICE + cartGoodsDTO.getActivityId() + "_" + cartGoodsDTO.getSpecId();
                Object priceObj = redisUtils.get(priceKey);
                if (priceObj == null) {
                    // 获取活动库存异常
                    throw new ServiceException(OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE);
                }

                BigDecimal activityPrice = new BigDecimal(priceObj.toString());
                if (activityPrice.compareTo(cartGoodsDTO.getSpecSellPrice()) != 0) {
                    activityPriceList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
                }

                // 会员等级限制
                if (flashSaleActivityDTO != null && flashSaleActivityDTO.getMemberGradeLimit() != null) {
                    if (memberVo.getMemberInfoDTO().getGradePoint() < flashSaleActivityDTO.getMemberGradeLimit()) {
                        activityMemberGradeLackList.add(cartGoodsDTO);
                        decrementList.add(cartGoodsDTO);
                    }
                }

                // 活动限购
                Integer buyNum = this.countFlashSaleOrderGoodsNum(cartGoodsDTO.getActivityId(),
                        cartGoodsDTO.getSpecId(), buyerId);
                if (flashSaleActivityDTO != null && flashSaleActivityDTO.getRestrictionQuantity() != null && flashSaleActivityDTO.getRestrictionQuantity() != 0
                        && buyNum + cartGoodsDTO.getGoodsNum() > flashSaleActivityDTO.getRestrictionQuantity()) {
                    activityOverLimitList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
                }

                //查询限时抢购活动库存
                String storageKey =
                        ActivityRedisConstants.FLASH_GOODS_SURPLUS_STORAGE + cartGoodsDTO.getActivityId() + "_" + cartGoodsDTO.getSpecId();
                Object storageObj = redisUtils.get(storageKey);

                if (storageObj == null) {
                    // 获取活动库存异常
                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR);
                }

                Integer activitySurplusStorage = Integer.parseInt(storageObj.toString());
                if (cartGoodsDTO.getGoodsNum() > activitySurplusStorage) {
                    // 活动库存不足
                    activityStorageLackList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
                }

                try {
                    if (CollectionUtils.isEmpty(decrementList)) {
                        // 扣减限时抢购库存
                        redisUtils.decrement(storageKey, cartGoodsDTO.getGoodsNum());
                    }
                } catch (Exception e) {
                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR);
                }
            }
        }

        // 错误信息封装
        map.put("flashSaleNotExist", activityNotExistList);
        map.put("flashSalePre", activityPreList);
        map.put("flashSaleStop", activityStopList);
        map.put("flashSaleStorageLack", activityStorageLackList);
        map.put("flashSalePrice", activityPriceList);
        map.put("flashSaleMemberGradeLack", activityMemberGradeLackList);
        map.put("flashSaleOverLimit", activityOverLimitList);
    }

    /**
     * 功能描述：
     * <购物车提交订单秒杀活动校验>
     *
     * @param cartDTOList
     * @param buyerId
     * @param memberVo
     * @param map
     * @return
     * @date 2020/4/8 11:43
     * @author 刘远杰
     **/
    private Map<String, List<CartGoodsDTO>> cartSeckillValidation(List<CartDTO> cartDTOList, Long buyerId,
                                                                  MemberVoDTO memberVo, Map<String,
            List<CartGoodsDTO>> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        // 秒杀活动不存在集合
        List<CartGoodsDTO> seckillNotExistList = Lists.newArrayList();
        // 秒杀活动未开始集合
        List<CartGoodsDTO> seckillPreList = Lists.newArrayList();
        // 秒杀活动已结束集合
        List<CartGoodsDTO> seckillStopList = Lists.newArrayList();
        // 秒杀库存不足商品集合
        List<CartGoodsDTO> seckillStorageLackList = Lists.newArrayList();
        // 秒杀价格变动商品集合
        List<CartGoodsDTO> seckillPriceList = Lists.newArrayList();
        // 秒杀会员等级不足商品集合
        List<CartGoodsDTO> seckillMemberGradeLackList = Lists.newArrayList();
        // 秒杀超出限购商品集合
        List<CartGoodsDTO> seckillOverLimitList = Lists.newArrayList();
        //判断是否扣减库存
        List<CartGoodsDTO> decrementList = Lists.newArrayList();

        List<CartGoodsDTO> cartGoodsDTOList = ConvertUtils.sourceToTarget(cartDTOList, CartGoodsDTO.class);
        for (CartGoodsDTO cartGoodsDTO : cartGoodsDTOList) {
            // 校验秒杀活动状态、商品库存
            if (cartGoodsDTO.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value() && cartGoodsDTO.getActivityId() != null) {
                // 查询秒杀活动
                SeckillActivityDTO seckillActivityDTO = seckillActivityService.get(cartGoodsDTO.getActivityId());

                //秒杀活动校验
                if (seckillActivityDTO == null) {
                    seckillNotExistList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
//                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_NO_SECKILL_ACTIVITY);
                } else if (seckillActivityDTO.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value()) {
                    seckillPreList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
//                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_PREPARE);
                } else if (seckillActivityDTO.getActivityState() == SeckillActivityEnum.ACTIVITY_STATE_END.value()) {
                    seckillStopList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
//                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_END);
                }

                // 校验秒杀商品价格
                String priceKey =
                        ActivityRedisConstants.SECKILL_GOODS_PRICE + cartGoodsDTO.getActivityId() + "_" + cartGoodsDTO.getSpecId();
                Object priceObj = redisUtils.get(priceKey);
                if (priceObj == null) {
                    // 获取活动库存异常
                    throw new ServiceException(OrderStatusCode.BUYNOW_GOODS_PRICE_CHANGE);
                }

                BigDecimal activityPrice = new BigDecimal(priceObj.toString());
                if (activityPrice.compareTo(cartGoodsDTO.getSpecSellPrice()) != 0) {
                    seckillPriceList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
//                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_PRICE_ERROR);
                }

                // 会员等级限制
                if (seckillActivityDTO.getMemberGradeLimit() != null) {
                    if (memberVo.getMemberInfoDTO().getGradePoint() < seckillActivityDTO.getMemberGradeLimit()) {
                        seckillMemberGradeLackList.add(cartGoodsDTO);
                        decrementList.add(cartGoodsDTO);
//                        throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_LESS_MEMBER_GRADE);
                    }
                }

                // 活动限购
                Integer buyNum = this.countSeckillOrderGoodsNum(cartGoodsDTO.getActivityId(),
                        cartGoodsDTO.getSpecId(), buyerId);
                if (seckillActivityDTO.getRestrictionQuantity() != null && seckillActivityDTO.getRestrictionQuantity() != 0
                        && buyNum + cartGoodsDTO.getGoodsNum() > seckillActivityDTO.getRestrictionQuantity()) {
                    seckillOverLimitList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
//                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_LIMIT_OVER);
                }

                //查询秒杀活动库存
                String storageKey =
                        ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + cartGoodsDTO.getActivityId() + "_" + cartGoodsDTO.getSpecId();
                Object storageObj = redisUtils.get(storageKey);

                if (storageObj == null) {
                    // 获取活动库存异常
                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_ERROR);
                }

                Integer activitySurplusStorage = Integer.parseInt(storageObj.toString());
                if (cartGoodsDTO.getGoodsNum() > activitySurplusStorage) {
                    // 活动库存不足
                    seckillStorageLackList.add(cartGoodsDTO);
                    decrementList.add(cartGoodsDTO);
//                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_ACTIVITY_STORAGE_LACK);
                }

                try {
                    if (CollectionUtils.isEmpty(decrementList)) {
                        // 扣减秒杀库存
                        redisUtils.decrement(storageKey, cartGoodsDTO.getGoodsNum());
                    }
                } catch (Exception e) {
                    throw new ServiceException(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR);
                }
            }
        }

        // 错误信息封装
        map.put("seckillNotExist", seckillNotExistList);
        map.put("seckillPre", seckillPreList);
        map.put("seckillStop", seckillStopList);
        map.put("seckillStorageLack", seckillStorageLackList);
        map.put("seckillPrice", seckillPriceList);
        map.put("seckillMemberGradeLack", seckillMemberGradeLackList);
        map.put("seckillOverLimit", seckillOverLimitList);
        return map;
    }

    /**
     * 封装并保存订单确认信息数据
     *
     * @param buyerId:         会员Id
     * @param addressId:       地址ID
     * @param orderMessage:    订单留言json格式:[{店铺id:买家留言}]
     * @param cartDTOList:     用户勾选购物车信息集合
     * @param memberCouponsId: 店铺id-会员优惠券id
     * @date 2019/6/22 14:45
     * @author LX lixiangx@leimingtech.com
     **/
//
//    @Override
//    public OrderConfirmDTO saveOrderConfirm(@RequestParam("buyerId") Long buyerId, @RequestParam("addressId") Long
//    addressId,
//                                            @RequestParam("orderMessage") String orderMessage, @RequestBody List
//                                            cartDTOList,
//                                            @RequestParam Map<String, Long> memberCouponsId,
//                                            @RequestParam(value = "orderPlatform", required = false) Integer
    //                                            orderPlatform) {
//
//        Map<Long, String> storeMap = new HashMap<>();
//        List<CartDTO> cartDTOS = ConvertUtils.sourceToTarget(cartDTOList, CartDTO.class);
//        cartDTOS.stream().filter(Objects::nonNull).forEach(cart -> storeMap.put(cart.getStoreId(), cart
//        .getStoreName()));
//
//        OrderConfirmDTO orderConfirmDTO = new OrderConfirmDTO();
//
//        // 查询用户信息
//        log.info("购物车购买获取会员信息，buyerId:{}", buyerId);
//        MemberVoDTO memberVo = memberService.selectMemberById(buyerId);
//        MemberGradeDTO memberGradeDTO = memberGradeService.selectByMemberId(memberVo.getMemberInfoDTO()
//        .getGradePoint());
//        // 设置用户等级信息
//        if (memberGradeDTO != null) {
//            orderConfirmDTO.setBuyerGraderId(memberGradeDTO.getId());
//            orderConfirmDTO.setBuyerGraderName(memberGradeDTO.getGradeName());
//        }
//        //封装用户信息
//        orderConfirmDTO.setId(IdGenerator.defaultSnowflakeId());
//        orderConfirmDTO.setBuyerId(buyerId);
//        orderConfirmDTO.setBuyerName(memberVo.getMemberName());
//        orderConfirmDTO.setBuyerEmail(memberVo.getMemberEmail());
//        orderConfirmDTO.setAddressId(addressId);
//        orderConfirmDTO.setOrderPlatform(orderPlatform);
//
//        //如果只包含一个店铺  设置订单确认表的店铺ID  否则不进行设置
//        if (storeMap.keySet().size() == 1) {
//            Long storeId = storeMap.keySet().iterator().next();
//            orderConfirmDTO.setStoreId(storeId);
//            orderConfirmDTO.setStoreName(storeMap.get(storeId));
//        } else if (storeMap.keySet().size() > 1) {
//            orderConfirmDTO.setStoreId(PLATFORM_ORDER_STORE_ID);
//            orderConfirmDTO.setStoreName(PLATFORM_ORDER_STORE_NAME);
//        }
//
//        // 订单留言信息 (消费者中解析订单留言)
//        orderConfirmDTO.setOrderMessage(orderMessage);
//
//        // 订单优惠券信息
//        if (memberCouponsId != null && memberCouponsId.size() > 0) {
//            orderConfirmDTO.setMemberCouponsId(JSONObject.toJSONString(memberCouponsId));
//            log.info("提交订单选择优惠券：{}", memberCouponsId);
//        }
//
//        //计算运费价格、商品总价格、支付总金额、优惠总金额
//        OrderConfirmPriceDTO orderConfirmPriceDTO = this.computePrice(cartDTOList, storeMap);
//        orderConfirmDTO.setOrderAmount(orderConfirmPriceDTO.getOrderAmount());
//        orderConfirmDTO.setGoodsAmount(orderConfirmPriceDTO.getGoodsAmout());
//        orderConfirmDTO.setPreferentialPrice(orderConfirmPriceDTO.getPreferentialPrice());
//        orderConfirmDTO.setShippingFee(orderConfirmPriceDTO.getShippingFee());
//
//        //保存订单确认信息
//        orderConfirmService.save(orderConfirmDTO);
//        log.info("购物车购买保存订单确认表成功");
//
//        //保存商品信息
//        this.saveOrderGoodsConfirm(orderConfirmDTO.getId(), cartDTOList, buyerId, memberCouponsId);
//
//        return orderConfirmDTO;
//    }

    /**
     * 封装并保存订单商品确认信息
     *
     * @param orderConfirmId: 订单确认表ID
     * @param cartDTOList:    用户勾选购物车信息
     * @date 2019/6/22 16:07
     * @author LX lixiangx@leimingtech.com
     **/
    private void saveOrderGoodsConfirm(Long orderConfirmId, List<CartDTO> cartDTOList, Long buyerId, Map<String,
            Long> memberCouponsId) {
        List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList = Lists.newArrayList();

        // 数据封装
        cartDTOList.forEach(cartDTO -> {
            // 封装订单商品确认数据
            OrderGoodsConfirmDTO orderGoodsConfirmDTO = new OrderGoodsConfirmDTO();
            orderGoodsConfirmDTO.setGoodsId(cartDTO.getGoodsId());
            orderGoodsConfirmDTO.setSpecId(cartDTO.getSpecId());
            orderGoodsConfirmDTO.setGoodsNum(cartDTO.getGoodsNum());

            log.info("购物车购买获取商品规格信息，specId:{}", cartDTO.getSpecId());
            GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(cartDTO.getSpecId());
            orderGoodsConfirmDTO.setGoodsImage(goodsSpecDTO.getSpecMainPicture());
            orderGoodsConfirmDTO.setStoreId(cartDTO.getStoreId());
            orderGoodsConfirmDTO.setStoreName(cartDTO.getStoreName());
            orderGoodsConfirmDTO.setBuyerId(buyerId);
            orderGoodsConfirmDTO.setIsGift(OrderGoodsEnum.NO_GIFT.value());
            orderGoodsConfirmDTO.setSpecCostPrice(goodsSpecDTO.getSpecCostPrice());
            orderGoodsConfirmDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
            orderGoodsConfirmDTO.setGoodsPayPrice(cartDTO.getSpecSellPrice());
            orderGoodsConfirmDTO.setOrderConfirmId(orderConfirmId);
            if (null == cartDTO.getVirtualFlag()) {
                orderGoodsConfirmDTO.setVirtualFlag(VirtualFlagEnum.NO.value());
            } else {
                orderGoodsConfirmDTO.setVirtualFlag(cartDTO.getVirtualFlag());
            }
            orderGoodsConfirmDTO.setDevlierType(cartDTO.getDevlierType());

            if (cartDTO.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value() && cartDTO.getActivityId() != null) {
                // 秒杀
                orderGoodsConfirmDTO.setActivityId(cartDTO.getActivityId());
                orderGoodsConfirmDTO.setActivityType(cartDTO.getActivityType());
            } else if (cartDTO.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() && cartDTO.getActivityId() != null) {
                // 限时购
                orderGoodsConfirmDTO.setActivityId(cartDTO.getActivityId());
                orderGoodsConfirmDTO.setActivityType(cartDTO.getActivityType());
            } else {
                // 店铺未使用优惠券则使用购物车勾选的满减活动
                if (!memberCouponsId.containsKey(cartDTO.getStoreId().toString()) && ActivityTypeEnum.REDUCE_ACTIVITY.value() == cartDTO.getActivityType()) {
//                    orderGoodsConfirmDTO.setReduceActivityId(cartDTO.getActivityId());
                    orderGoodsConfirmDTO.setActivityId(cartDTO.getActivityId());
                    orderGoodsConfirmDTO.setActivityType(cartDTO.getActivityType());
                }
            }
            orderGoodsConfirmDTOList.add(orderGoodsConfirmDTO);
        });

        // 批处理保存订单商品确认表数据
        orderGoodsConfirmService.saveList(orderGoodsConfirmDTOList);
        log.info("购物车购买保存订单商品确认表成功");
    }


    /**
     * 计算订单确认金额
     *
     * @param cartDTOList: 用户勾选购物车信息
     * @param storeMap:    店铺ID Map集合
     * @return 订单确认金额信息
     * @date 2019/6/22 15:39
     * @author LX lixiangx@leimingtech.com
     **/
    private OrderConfirmPriceDTO computePrice(List<CartDTO> cartDTOList, Map<Long, String> storeMap, Map<Long,
            BigDecimal> shippingAmount) {

        OrderConfirmPriceDTO orderConfirmPriceDTO = new OrderConfirmPriceDTO();

        //商品总金额
        BigDecimal goodsAmout = BigDecimal.ZERO;
        //优惠总金额
        BigDecimal preferentialPrice = BigDecimal.ZERO;
        //运费
        BigDecimal shippingFee = BigDecimal.ZERO;
        if (shippingAmount != null && shippingAmount.size() > 0) {
            for (Long key : shippingAmount.keySet()) {
                shippingFee = shippingFee.add(shippingAmount.get(key));
            }
        }

        //循环店铺ID 封装店铺ID下所有购物车商品数据
        for (Long storeId : storeMap.keySet()) {
            // 循环购物车数据
            for (CartDTO cartDTO : cartDTOList) {
                if (storeId.equals(cartDTO.getStoreId())) {
                    //计算商品总价
                    goodsAmout = goodsAmout.add(cartDTO.getSpecSellPrice().
                            multiply(new BigDecimal(cartDTO.getGoodsNum())));
                }
            }
        }

        //订单应付金额=商品总额-优惠总额+运费
        orderConfirmPriceDTO.setOrderAmount(goodsAmout.subtract(preferentialPrice).add(shippingFee));
        orderConfirmPriceDTO.setGoodsAmout(goodsAmout);
        orderConfirmPriceDTO.setPreferentialPrice(preferentialPrice);
        orderConfirmPriceDTO.setShippingFee(shippingFee);
        return orderConfirmPriceDTO;
    }


    /**
     * 解析提交订单出现的错误信息
     *
     * @param validationMap:
     * @return 包含 商品（goodsshow）、规格（specnotfund）、库存（understock）、价格（pricechange）不合法的信息map
     * @date 2019/6/22 14:20
     * @author LX lixiangx@leimingtech.com
     **/
    private OrderSaveResultDTO validation(Map<String, List<CartGoodsDTO>> validationMap) {

        OrderSaveResultDTO orderSaveResultDTO = new OrderSaveResultDTO();
        List<ErrorOrderMsgDTO> errorOrderMsgDTOList = new ArrayList<>();
        //判断商品规格异常
        List<CartGoodsDTO> list = validationMap.get("specnotfund");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品不存在"));
            });
        }

        //判断商品的状态
        list = validationMap.get("goodsshow");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品已下架"));
            });
        }

        //判断商品的库存
        list = validationMap.get("understock");
        if (CollectionUtils.isNotEmpty(list)) {

            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品库存不足"));
            });
        }

        //判断商品价格变动
        list = validationMap.get("pricechange");
        if (CollectionUtils.isNotEmpty(list)) {

            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品价格已发生变化"));
            });
        }

        // 判断满减活动不存在
        list = validationMap.get("reduceNotExist");
        if (CollectionUtils.isNotEmpty(list)) {

            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的满减活动不存在"));
            });
        }

        // 判断满减活动未开始
        list = validationMap.get("reducePre");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的满减活动未开始"));
            });
        }
        // 判断满减活动已结束
        list = validationMap.get("reduceStop");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的满减活动已结束"));
            });
        }

        // 判断秒杀活动不存在
        list = validationMap.get("seckillPre");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的秒杀活动不存在"));
            });
        }

        // 判断秒杀活动未开始
        list = validationMap.get("seckillNotExist");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的秒杀活动未开始"));
            });
        }

        // 判断秒杀活动已结束
        list = validationMap.get("seckillStop");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的秒杀活动已结束"));
            });
        }

        // 判断秒杀活动商品库存
        list = validationMap.get("seckillStorageLack");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的秒杀活动商品库存不足"));
            });
        }

        // 判断秒杀活动商品价格
        list = validationMap.get("seckillPrice");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的秒杀活动商品价格发生变化"));
            });
        }

        // 判断秒杀活动会员等级
        list = validationMap.get("seckillMemberGradeLack");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的秒杀活动商品未满足会员参与等级"));
            });
        }

        // 判断秒杀活动商品限购数量
        list = validationMap.get("seckillOverLimit");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的秒杀活动商品已超出商品限购数量"));
            });
        }
        // 判断限时抢购活动商品不存在
        list = validationMap.get("flashSaleNotExist");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的限时抢购活动不存在"));
            });
        }
        // 判断限时抢购活动商品未开始
        list = validationMap.get("flashSalePre");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的限时抢购活动未开始"));
            });
        }
        // 判断限时抢购活动商品已结束
        list = validationMap.get("flashSaleStop");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的限时抢购活动已结束"));
            });
        }
        // 判断限时抢购活动商品库存
        list = validationMap.get("flashSaleStorageLack");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的限时抢购活动商品库存不足"));
            });
        }
        // 判断限时抢购活动商品价格
        list = validationMap.get("flashSalePrice");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的限时抢购活动商品价格发生变化"));
            });
        }
        // 判断限时抢购活动商品会员参与等级
        list = validationMap.get("flashSaleMemberGradeLack");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的限时抢购活动商品未满足会员参与等级"));
            });
        }
        // 判断限时抢购活动商品限购数量
        list = validationMap.get("flashSaleOverLimit");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您选择的限时抢购活动商品已超出商品限购数量"));
            });
        }
        // 判断运费模板是否存在
        list = validationMap.get("freighttemplatenofound");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品运费模板不存在"));
            });
        }

        // 判断运费模板是否超出配送范围
        list = validationMap.get("freighttemplateoverarea");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品超出配送区域"));
            });
        }

        // 判断运费模板类型是否支持
        list = validationMap.get("freighttemplatetypeerror");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                // 封装错误提示信息
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品运费模板类型暂不支持"));
            });
        }

        // 判断运费模板类型是否支持
        list = validationMap.get("freighttemplateruleerror");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(cartGoodsDTO -> {
                errorOrderMsgDTOList.add(this.packageErrorOrderMsgDTO(cartGoodsDTO, "抱歉，您购买的以下商品运费规则不可用"));
            });
        }

        orderSaveResultDTO.setCheckStatus(SubmitOrderErrorEnum.GOODS_CHECK_FAILED.value());
        orderSaveResultDTO.setErrorGoodsList(errorOrderMsgDTOList);

        if (CollectionUtils.isNotEmpty(errorOrderMsgDTOList)) {
            return orderSaveResultDTO;
        }
        return null;
    }

    /**
     * 封装处理后的ErrorOrderMsgDTO
     *
     * @param cartGoodsDTO  购物车商品数据
     * @param errorMsgTitle 错误提示信息
     * @return
     * @date 2020-06-11 13:54
     * @author huangkeyuan@leimingtech.com
     **/
    private ErrorOrderMsgDTO packageErrorOrderMsgDTO(CartGoodsDTO cartGoodsDTO, String errorMsgTitle) {
        // 封装错误提示信息
        ErrorGoodsMsgDTO errorGoodsMsgDTO = new ErrorGoodsMsgDTO();
        errorGoodsMsgDTO.setSpuId(cartGoodsDTO.getGoodsId());
        errorGoodsMsgDTO.setSkuId(cartGoodsDTO.getSpecId());
        errorGoodsMsgDTO.setGoodsImage(cartGoodsDTO.getSpecMainPicture());
        errorGoodsMsgDTO.setSpecName(cartGoodsDTO.getSpecName());
        errorGoodsMsgDTO.setSpecAttrValueName(cartGoodsDTO.getSpecInfo());
        errorGoodsMsgDTO.setBrandId(cartGoodsDTO.getBrandId());
        errorGoodsMsgDTO.setBrandName(cartGoodsDTO.getBrandName());
        errorGoodsMsgDTO.setStoreId(cartGoodsDTO.getStoreId());
        errorGoodsMsgDTO.setStoreName(cartGoodsDTO.getStoreName());
        errorGoodsMsgDTO.setGoodsNum(cartGoodsDTO.getGoodsNum());
        errorGoodsMsgDTO.setPrice(cartGoodsDTO.getSpecSellPrice());
        ErrorOrderMsgDTO errorOrderMsgDTO = new ErrorOrderMsgDTO();
        errorOrderMsgDTO.setErrorMsgTitle(errorMsgTitle);
        errorOrderMsgDTO.setErrorGoodsMsgDTOList(Collections.singletonList(errorGoodsMsgDTO));
        return errorOrderMsgDTO;
    }

    /**
     * CartConfirmOrderConsumer消费者中订单处理 (内部模块使用)
     *
     * @param orderConfirmId: 订单确认表ID
     * @throws Exception 抛出订单保存失败异常
     * @date 2019/6/25 14:32
     * @author lixiang
     **/
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void packOrderFromConsumer(@RequestParam("orderConfirmId") Long orderConfirmId) {
        Map<String, String> logMap = new HashMap<>();
        logMap.put("orderConfirmId", orderConfirmId.toString());
        // 1.查询订单确认表信息
        OrderConfirmDTO orderConfirmDTO = orderConfirmService.findById(orderConfirmId);
        // 2.查询订单确认商品信息
        List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList = orderGoodsConfirmService.
                findListByOrderConfirmId(orderConfirmId);

        Map<Long, MemberCouponsIndexDTO> storeCouponsMap = new HashMap<>();
        // 3.异步保存订单数据校验：商品库存、优惠券、满减、秒杀 todo 数据校验提前到orderConfirm保存前
        boolean validFlag = mqSaveOrderValid(orderConfirmDTO, orderGoodsConfirmDTOList, storeCouponsMap);
        if (!validFlag) {
            // 校验不通过，结束方法
            return;
        }

        // 4.保存订单地址信息,区分虚拟订单和普通订单
        Long orderAddressId = 0L;
        if (orderConfirmDTO.getVirtualFlag() == VirtualFlagEnum.YES.value()) {
            orderAddressId = orderAddressService.saveOrderAddress(orderConfirmDTO.getAddressId(),
                    orderConfirmDTO.getBuyerId(), orderConfirmDTO.getVirtualCustomer(),
                    orderConfirmDTO.getVirtualPhone());
        } else {
            orderAddressId = orderAddressService.saveOrderAddress(orderConfirmDTO.getAddressId(),
                    orderConfirmDTO.getBuyerId(), orderConfirmDTO.getBuyerName(), null);
        }

        mlogger.info(OrderStatusCode.ORDER_ADDRESS_SVAE_SUCCESS_CODE,
                OrderStatusCode.ORDER_ADDRESS_SAVE_SUCCESS_MESSAGE, logMap);

        // 5.封装订单支付保存信息
        OrderPayDTO orderPayDTO = getOrderPay(orderConfirmDTO);

        boolean balancePay = false;
        try {
            // 6.保存订单信息
            balancePay = this.saveOrderInfo(orderPayDTO, orderConfirmDTO, orderGoodsConfirmDTOList, orderAddressId,
                    storeCouponsMap);
        } catch (Exception e) {
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
            saveOrderRedisDTO.setResultMsg("订单保存失败");
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                    RedisConstants.JEDIS_EXPIRE);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("异常回滚尝试：{0}", e);
            return;
        }

        // 如果balancePay为true说明是纯余额支付，不用跳转第三方微信或支付宝支付
        if (balancePay) {
            SaveOrderRedisDTO saveOrderRedisDTO1 = new SaveOrderRedisDTO();
            saveOrderRedisDTO1.setResultCode(RedisConstants.BALANCESUSSESS);
            saveOrderRedisDTO1.setResultMsg("保存余额支付订单成功");
            saveOrderRedisDTO1.setPaySn(orderPayDTO.getPaySn());
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO1,
                    RedisConstants.JEDIS_EXPIRE);
            log.info("保存余额支付订单信息成功");

//            H5WXPayNotifyDTO h5WXPayNotifyDTO = new H5WXPayNotifyDTO();
//            h5WXPayNotifyDTO.setPaySn(orderPayDTO.getPaySn().toString());
//            h5WXPayNotifyDTO.setTransactionId(IdGenerator.defaultSnowflakeId().toString());
//            h5WXPayNotifyDTO.setResultCode("SUCCESS");
//            log.info("余额支付成功,支付流水处理");
//            paymentService.finishPay(h5WXPayNotifyDTO, h5WXPayNotifyDTO.getTransactionId(), PaymentCodeConstants
//            .BALANCE);
        } else {
            // redis保存订单成功信息
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.SUSSESS);
            saveOrderRedisDTO.setResultMsg("保存订单成功");
            saveOrderRedisDTO.setPaySn(orderPayDTO.getPaySn());
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                    RedisConstants.JEDIS_EXPIRE);
            log.info("保存订单信息成功");
        }


    }

    /**
     * 余额支付完成后调用
     *
     * @param paySn 支付单号
     * @return
     * @date 2020-07-10 14:11
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void balanceFinsh(@RequestParam("paySn") Long paySn) {

        H5WXPayNotifyDTO h5WXPayNotifyDTO = new H5WXPayNotifyDTO();
        h5WXPayNotifyDTO.setPaySn(paySn.toString());
        h5WXPayNotifyDTO.setTransactionId(IdGenerator.defaultSnowflakeId().toString());
        h5WXPayNotifyDTO.setResultCode("SUCCESS");
        log.info("余额支付成功711，h5WXPayNotifyDTO：{}", h5WXPayNotifyDTO);
        paymentService.finishPay(h5WXPayNotifyDTO, h5WXPayNotifyDTO.getTransactionId(), PaymentCodeConstants.BALANCE);
    }

    /**
     * 功能描述：
     * <异步保存订单数据校验：商品库存、优惠券、满减>
     *
     * @param orderConfirmDTO          订单确认表数据
     * @param orderGoodsConfirmDTOList 订单是确认表数据
     * @param storeCouponsMap          店铺优惠券
     * @return
     * @date 2020/1/3 18:03
     * @author 刘远杰
     **/
    private boolean mqSaveOrderValid(OrderConfirmDTO orderConfirmDTO,
                                     List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList,
                                     Map<Long, MemberCouponsIndexDTO> storeCouponsMap) {
        // 再次校验库存信息
        if (!this.stockValidation(orderGoodsConfirmDTOList)) {
            log.error("校验库存失败,订单确认表主键:{}", orderConfirmDTO.getId());
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
            saveOrderRedisDTO.setResultMsg("保存订单失败，库存不足");
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                    RedisConstants.JEDIS_EXPIRE);
            return false;
        }

        // 校验优惠券 优惠券状态是否为可用状态、优惠券是否存在
        if (StringUtils.isNotBlank(orderConfirmDTO.getMemberCouponsId())) {
            JSONObject jsonObject = JSONObject.parseObject(orderConfirmDTO.getMemberCouponsId());
            for (String key : jsonObject.keySet()) {
                Object o = jsonObject.get(key);
                if (o != null) {
                    MemberCouponsIndexDTO memberCouponsIndexDTO =
                            memberCouponsService.selectMemberCouponsAndGoodsList(Long.parseLong(o.toString()));
                    storeCouponsMap.put(memberCouponsIndexDTO.getStoreId(), memberCouponsIndexDTO);
                    if (memberCouponsIndexDTO == null || !memberCouponsIndexDTO.getMemberId().equals(orderConfirmDTO.getBuyerId())) {
                        // 未查询到会员优惠券
                        log.error("保存订单失败，未查询到会员优惠券:{}", o.toString());
                        SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                        saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                        saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_NO_COUPONS.getMessage());
                        redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                                RedisConstants.JEDIS_EXPIRE);
                        return false;
                    } else if (CouponsEnum.COUPON_CAN_USE.value() != memberCouponsIndexDTO.getCouponsState()) {
                        // 优惠券不可用
                        log.error("保存订单失败，优惠券不可用:{}", o.toString());
                        SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                        saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                        saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_COUPONS_CANNT_USE.getMessage());
                        redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                                RedisConstants.JEDIS_EXPIRE);
                        return false;
                    }
                }
            }
        }

        // 校验满减活动 满减活动是否为开始状态、是否存在
        List<Long> reduceIds = new ArrayList<>();
        for (OrderGoodsConfirmDTO orderGoodsConfirmDTO : orderGoodsConfirmDTOList) {
            if (orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.COUPONS_ACTIVITY.value()
                    && orderGoodsConfirmDTO.getActivityId() != null && !reduceIds.contains(orderGoodsConfirmDTO.getActivityId())) {
                reduceIds.add(orderGoodsConfirmDTO.getActivityId());
                ReduceActivityDTO reduceActivityDTO = reduceActivityService.get(orderGoodsConfirmDTO.getActivityId());
                if (reduceActivityDTO == null) {
                    log.error("保存订单失败，未查询到满减活动:{}", orderGoodsConfirmDTO.getActivityId());
                    SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_NO_REDUCE_ACTIVITY.getMessage());
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    return false;
                } else if (ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value() == reduceActivityDTO.getActivityState()) {
                    log.error("保存订单失败，满减活动未开始:{}", orderGoodsConfirmDTO.getActivityId());
                    SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_PREPARE.getMessage());
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    return false;
                } else if (ReduceActivityEnum.ACTIVITY_STATE_END.value() == reduceActivityDTO.getActivityState()) {
                    log.error("保存订单失败，满减活动已结束:{}", orderGoodsConfirmDTO.getActivityId());
                    SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_REDUCE_ACTIVITY_END.getMessage());
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    return false;
                }
            }
        }

        // 校验秒杀活动 秒杀活动是否为开始状态、是否存在
        List<Long> seckillIds = new ArrayList<>();
        for (OrderGoodsConfirmDTO orderGoodsConfirmDTO : orderGoodsConfirmDTOList) {
            if (orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value() && orderGoodsConfirmDTO.getActivityId() != null && !seckillIds.contains(orderGoodsConfirmDTO.getActivityId())) {
                seckillIds.add(orderGoodsConfirmDTO.getActivityId());
                SeckillActivityDTO seckillActivity = seckillActivityService.get(orderGoodsConfirmDTO.getActivityId());
                if (seckillActivity == null) {
                    log.error("保存订单失败，未查询到秒杀活动:{}", orderGoodsConfirmDTO.getActivityId());
                    SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_NO_SECKILL_ACTIVITY.getMessage());
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    return false;
                } else if (SeckillActivityEnum.ACTIVITY_STATE_PREPARE.value() == seckillActivity.getActivityState()) {
                    log.error("保存订单失败，秒杀活动未开始:{}", orderGoodsConfirmDTO.getActivityId());
                    SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_PREPARE.getMessage());
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    return false;
                } else if (SeckillActivityEnum.ACTIVITY_STATE_END.value() == seckillActivity.getActivityState()) {
                    log.error("保存订单失败，秒杀活动已结束:{}", orderGoodsConfirmDTO.getActivityId());
                    SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_SECKILL_ACTIVITY_END.getMessage());
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    return false;
                }
            }
        }


        return true;
    }

    /**
     * ConfirmGroupOrderConsumer消费者中订单处理
     *
     * @param orderConfirmId 确认订单id
     * @return
     * @date 2020-03-24 10:31
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void packGroupOrderFromConsumer(@RequestParam("orderConfirmId") Long orderConfirmId) {
        Map<String, String> logMap = new HashMap<>();
        logMap.put("orderConfirmId", orderConfirmId.toString());

        // 1.查询订单确认表信息
        OrderConfirmDTO orderConfirmDTO = orderConfirmService.findById(orderConfirmId);
        // 2.查询订单确认商品信息
        List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList = orderGoodsConfirmService.
                findListByOrderConfirmId(orderConfirmId);

        Map<Long, MemberCouponsIndexDTO> storeCouponsMap = new HashMap<>();
        // 3.异步保存订单数据校验：商品库存、拼团活动
        boolean validFlag = mqGroupSaveOrderValid(orderConfirmDTO, orderGoodsConfirmDTOList, storeCouponsMap);
        if (!validFlag) {
            // 校验不通过，结束方法
            return;
        }

        // 4.保存订单地址信息
        Long orderAddressId = 0L;
        if (orderConfirmDTO.getVirtualFlag() == VirtualFlagEnum.YES.value()) {
            orderAddressId = orderAddressService.saveOrderAddress(orderConfirmDTO.getAddressId(),
                    orderConfirmDTO.getBuyerId(), orderConfirmDTO.getVirtualCustomer(),
                    orderConfirmDTO.getVirtualPhone());
        } else {
            orderAddressId = orderAddressService.saveOrderAddress(orderConfirmDTO.getAddressId(),
                    orderConfirmDTO.getBuyerId(), orderConfirmDTO.getBuyerName(), null);
        }

        mlogger.info(OrderStatusCode.ORDER_ADDRESS_SVAE_SUCCESS_CODE,
                OrderStatusCode.ORDER_ADDRESS_SAVE_SUCCESS_MESSAGE, logMap);

        // 5.封装订单支付保存信息
        OrderPayDTO orderPayDTO = getGroupOrderPay(orderConfirmDTO, orderGoodsConfirmDTOList);
        boolean balancePay = false;
        try {
            // 6.保存订单信息
            balancePay = this.saveOrderInfo(orderPayDTO, orderConfirmDTO, orderGoodsConfirmDTOList, orderAddressId,
                    storeCouponsMap);
        } catch (Exception e) {
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
            saveOrderRedisDTO.setResultMsg("订单保存失败");
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                    RedisConstants.JEDIS_EXPIRE);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("异常回滚尝试：{0}", e);
            return;
        }

        if (balancePay) {
            // redis保存订单成功信息
            SaveOrderRedisDTO saveOrderRedisDTO1 = new SaveOrderRedisDTO();
            saveOrderRedisDTO1.setResultCode(RedisConstants.BALANCESUSSESS);
            saveOrderRedisDTO1.setResultMsg("保存余额支付订单成功");
            saveOrderRedisDTO1.setPaySn(orderPayDTO.getPaySn());
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO1,
                    RedisConstants.JEDIS_EXPIRE);
            log.info("保存余额支付订单信息成功");

//            H5WXPayNotifyDTO h5WXPayNotifyDTO = new H5WXPayNotifyDTO();
//            h5WXPayNotifyDTO.setPaySn(orderPayDTO.getPaySn().toString());
//            h5WXPayNotifyDTO.setTransactionId(IdGenerator.defaultSnowflakeId().toString());
//            h5WXPayNotifyDTO.setResultCode("SUCCESS");
//            paymentService.finishPay(h5WXPayNotifyDTO, h5WXPayNotifyDTO.getTransactionId(), PaymentCodeConstants
//            .BALANCE);
        } else {
            // redis保存支付成功信息
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.SUSSESS);
            saveOrderRedisDTO.setResultMsg("保存订单成功");
            saveOrderRedisDTO.setPaySn(orderPayDTO.getPaySn());
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                    RedisConstants.JEDIS_EXPIRE);
            log.info("保存订单支付信息成功");
        }
    }

    private boolean mqGroupSaveOrderValid(OrderConfirmDTO orderConfirmDTO,
                                          List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList,
                                          Map<Long, MemberCouponsIndexDTO> storeCouponsMap) {
        // 校验拼团活动的商品库存
        if (!this.groupStockValidation(orderGoodsConfirmDTOList)) {
            log.error("校验库存失败,订单确认表主键:{}", orderConfirmDTO.getId());
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
            saveOrderRedisDTO.setResultMsg("保存订单失败，库存不足");
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                    RedisConstants.JEDIS_EXPIRE);
            return false;
        }

        // 校验拼团活动 拼团活动是否为开始状态、是否存在
        List<Long> groupIds = new ArrayList<>();
        for (OrderGoodsConfirmDTO orderGoodsConfirmDTO : orderGoodsConfirmDTOList) {
            if (orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value() && orderGoodsConfirmDTO.getActivityId() != null && !groupIds.contains(orderGoodsConfirmDTO.getActivityId())) {
                groupIds.add(orderGoodsConfirmDTO.getActivityId());
                GroupDTO groupDTO = groupService.get(orderGoodsConfirmDTO.getActivityId());
                if (groupDTO == null) {
                    log.error("保存订单失败，未查询到拼团活动:{}", orderGoodsConfirmDTO.getActivityId());
                    SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg(OrderStatusCode.SAVE_ORDER_NO_GROUP_ACTIVITY.getMessage());
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 功能描述:
     * 〈构造订单保存orderPay〉
     *
     * @param orderConfirmDTO 订单确认实体
     * @author : 刘远杰
     */
    private OrderPayDTO getOrderPay(OrderConfirmDTO orderConfirmDTO) {
        OrderPayDTO orderPayDTO = new OrderPayDTO();
        orderPayDTO.setBuyerId(orderConfirmDTO.getBuyerId());
        orderPayDTO.setBuyerName(orderConfirmDTO.getBuyerName());
        orderPayDTO.setOrderSn(IdGenerator.defaultSnowflakeId());
        orderPayDTO.setPaySn(IdGenerator.defaultSnowflakeId());
        orderPayDTO.setPayState(PayStateEnum.NO.value());
        orderPayDTO.setId(IdGenerator.defaultSnowflakeId());

        String goodsName = OrderDefaultConstants.GOODS_NAME;
        orderPayDTO.setGoodsName(goodsName);

        // 获取取消订单时间设置
        String setting = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
        SettingSeniorDTO senior = JSON.parseObject(setting, SettingSeniorDTO.class);
        Long cancelOrder = null;
        if (null != senior) {
            String cancelOrderStr = senior.getCancelOrder();
            cancelOrder = Long.valueOf(cancelOrderStr) * OrderTimeConstants.MINUTE;
        } else {
            // 设置默认取消时间为30分钟发
            cancelOrder = 30L * OrderTimeConstants.MINUTE;
        }
        Date cancelDate = new Date(System.currentTimeMillis() + cancelOrder);
        orderPayDTO.setCancalDate(cancelDate);

        return orderPayDTO;
    }

    /**
     * 获取拼团活动订单支付时间
     *
     * @param orderConfirmDTO
     * @return
     */
    private OrderPayDTO getGroupOrderPay(OrderConfirmDTO orderConfirmDTO,
                                         List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList) {
        OrderPayDTO orderPayDTO = new OrderPayDTO();
        orderPayDTO.setBuyerId(orderConfirmDTO.getBuyerId());
        orderPayDTO.setBuyerName(orderConfirmDTO.getBuyerName());
        orderPayDTO.setOrderSn(IdGenerator.defaultSnowflakeId());
        orderPayDTO.setPaySn(IdGenerator.defaultSnowflakeId());
        orderPayDTO.setPayState(PayStateEnum.NO.value());
        orderPayDTO.setId(IdGenerator.defaultSnowflakeId());

        String goodsName = OrderDefaultConstants.GOODS_NAME;
        orderPayDTO.setGoodsName(goodsName);

        // 查询当前拼团活动的订单支付有效期
        OrderGoodsConfirmDTO orderGoodsConfirmDTO = orderGoodsConfirmDTOList.get(0);

        // 获取取消订单时间设置
        GroupDTO groupDTO = groupService.get(orderGoodsConfirmDTO.getActivityId());

        Long cancelOrder = null;
        if (null != groupDTO) {
            cancelOrder = Long.valueOf(groupDTO.getPayEndTime()) * OrderTimeConstants.MINUTE;
        } else {
            // 设置默认取消时间为30分钟发
            cancelOrder = 30L * OrderTimeConstants.MINUTE;
        }
        Date cancelDate = new Date(System.currentTimeMillis() + cancelOrder);
        orderPayDTO.setCancalDate(cancelDate);

        return orderPayDTO;
    }


    /**
     * 校验库存
     *
     * @param orderGoodsConfirmDTOList: 订单商品确认列表
     * @date 2019/6/22 20:28
     * @author LX lixiangx@leimingtech.com
     **/
    private boolean stockValidation(List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList) {

        //循环校验商品库存
        for (OrderGoodsConfirmDTO orderGoodsConfirmDTO : orderGoodsConfirmDTOList) {
            if (!orderGoodsConfirmDTO.getActivityType().equals(ActivityTypeEnum.SECKILL_ACTIVITY.value()) &&
                    !orderGoodsConfirmDTO.getActivityType().equals(ActivityTypeEnum.FLASH_SALE_ACTIVITY.value())) {
                GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(orderGoodsConfirmDTO.getSpecId());
                if (orderGoodsConfirmDTO.getGoodsNum() > goodsSpecDTO.getSpecStorage()) {
                    log.error("商品规格：{}，规格ID：{}。库存不足", goodsSpecDTO.getSpecName(), goodsSpecDTO.getId());
                    //库存不足情况
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 校验拼团商品的活动库存
     *
     * @return
     * @date 2020-03-24 11:26
     * @author huangkeyuan@leimingtech.com
     **/
    private boolean groupStockValidation(List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList) {

        //循环校验拼团商品库存
        for (OrderGoodsConfirmDTO orderGoodsConfirmDTO : orderGoodsConfirmDTOList) {
            ActivityGoodsDTO activityGoodsDTO =
                    activityGoodsService.goodsAndSpec(orderGoodsConfirmDTO.getActivityId(),
                            orderGoodsConfirmDTO.getSpecId()
                            , orderGoodsConfirmDTO.getActivityType());
            if (orderGoodsConfirmDTO.getGoodsNum() > activityGoodsDTO.getActivitySurplusStorage()) {
                log.error("活动id：{}，规格ID：{}。库存不足", activityGoodsDTO.getActivityId(), activityGoodsDTO.getSpecId());
                //库存不足情况
                return false;
            }
        }
        return true;
    }

    /**
     * 功能描述：
     * <订单数据保存（保存订单、订单商品、订单活动、订单日志，扣减库存，用券）>
     *
     * @param orderPayDTO              订单支付DTO
     * @param orderConfirmDTO          订单确认信息
     * @param orderGoodsConfirmDTOList 订单商品确认信息
     * @param orderAddressId           订单地址ID
     * @param storeCouponsMap          下单使用优惠券集合
     * @return balancePay              是否是纯余额支付
     * @date 2020/1/17 11:20
     * @author 刘远杰
     **/
    private boolean saveOrderInfo(OrderPayDTO orderPayDTO, OrderConfirmDTO orderConfirmDTO,
                                  List<OrderGoodsConfirmDTO> orderGoodsConfirmDTOList, Long orderAddressId,
                                  Map<Long, MemberCouponsIndexDTO> storeCouponsMap) {
        Map<String, String> logMap = new HashMap<>();
        Long orderConfirmId = orderConfirmDTO.getId();
        logMap.put("orderConfirmId", orderConfirmId.toString());
        logMap.put("orderConfirmId", orderConfirmDTO.getId().toString());

        // 是否是纯余额支付
        boolean balancePay = false;

        //获取订单发票数据
        List<OrderInvoiceDTO> orderInvoiceDTOS = orderInvoiceService.selectOrderInvoiceList(orderConfirmId);
        Map<Long, OrderInvoiceDTO> invoiceDTOMap =
                orderInvoiceDTOS.stream().collect(Collectors.toMap(OrderInvoiceDTO::getStoreId,
                        OrderInvoiceDTO -> OrderInvoiceDTO));

        // 订单保存时间
        Date now = new Date();
        // 拆分订单留言
        List<Map<Long, String>> messageList = splitOrderMessage(orderConfirmDTO);

        // 定义订单异步保存redis支付状态信息，订单保存列表，订单商品保存列表，订单店铺拆分map，库存修改方法参数
        SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
        List<OrderDTO> orderDTOList = new ArrayList<>();
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();
        // 订单优惠保存实体集合
        List<OrderActivityDTO> orderActivityDTOList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList = new ArrayList<>();
        Map<Long, OrderDTO> orderVoMap = new HashMap<>();
        Map<Integer, OrderDTO> devlierTypeMap = new HashMap<>();
        Map<Long, List<OrderGoodsDTO>> orderGoodsMap = new HashMap<>();


        // 1.遍历订单商品确认表，封装订单商品保存信息；首次遍历到店铺商品，创建子订单；如果店铺已遍历到，原子订单叠加计算订单价格
        orderGoodsConfirmDTOList.forEach(orderGoodsConfirmDTO -> {
            OrderDTO orderDTO;
            // 虚拟订单先根据店铺拆单，再根据配送方式拆单
            if (orderGoodsConfirmDTO.getVirtualFlag() == VirtualFlagEnum.YES.value()) {
                log.info("虚拟订单拆单");
                if (!orderVoMap.containsKey(orderGoodsConfirmDTO.getStoreId())) {
                    // 第一次循环店铺下的商品，创建子订单，初始订单数据
                    orderDTO = createInitOrderDTO(orderPayDTO, orderConfirmDTO, orderAddressId, now,
                            orderGoodsConfirmDTO);
                    log.info("虚拟订单店铺拆单,storeId:{},orderId:{}", orderGoodsConfirmDTO.getStoreId(), orderDTO);
                    // 设置订单留言
                    setOrderMessage(messageList, orderDTO);

                    // 订单集合、订单店铺map保存订单实体，下一次发现同店铺商品取出集合实体修改价格/发票信息
                    orderDTOList.add(orderDTO);
                    orderVoMap.put(orderDTO.getStoreId(), orderDTO);
                    devlierTypeMap.put(orderDTO.getDevlierType(), orderDTO);
                    orderGoodsMap.put(orderDTO.getStoreId(), new ArrayList<>());
                } else {

                    // 同一店铺根据不同配送方式拆单
                    if (!devlierTypeMap.containsKey(orderGoodsConfirmDTO.getDevlierType())) {
                        orderDTO = createInitOrderDTO(orderPayDTO, orderConfirmDTO, orderAddressId, now,
                                orderGoodsConfirmDTO);
                        log.info("虚拟订单配送方式拆单,storeId:{},devlierType:{},orderDTO:{}",
                                orderGoodsConfirmDTO.getStoreId(), orderGoodsConfirmDTO.getDevlierType(), orderDTO);
                        // 设置订单留言
                        setOrderMessage(messageList, orderDTO);

                        // 订单集合、订单店铺map保存订单实体，下一次发现同店铺商品取出集合实体修改价格/发票信息
                        orderDTOList.add(orderDTO);
                        orderVoMap.put(orderDTO.getStoreId(), orderDTO);
                        devlierTypeMap.put(orderDTO.getDevlierType(), orderDTO);
                        orderGoodsMap.put(orderDTO.getStoreId(), new ArrayList<>());
                    } else {
                        // 配送方式已遍历到，原子订单叠加计算订单价格
                        orderDTO = devlierTypeMap.get(orderGoodsConfirmDTO.getDevlierType());
                        log.info("虚拟订单配送方式已拆单orderDTO{}", orderDTO);
                        reCountOrder(orderGoodsConfirmDTO, orderDTO);
                    }

                }
                // 封装订单商品信息，获取库存修改接口参数
                OrderGoodsDTO orderGoodsDTO = this.createOrderGoodsDTO(orderGoodsConfirmDTO, orderDTO, params,
                        updateActivitySurplusStorageList);
                orderGoodsDTOList.add(orderGoodsDTO);
                orderGoodsMap.get(orderDTO.getStoreId()).add(orderGoodsDTO);
            } else {
                if (!orderVoMap.containsKey(orderGoodsConfirmDTO.getStoreId())) {
                    // 第一次循环店铺下的商品，创建子订单，初始订单数据
                    orderDTO = createInitOrderDTO(orderPayDTO, orderConfirmDTO, orderAddressId, now,
                            orderGoodsConfirmDTO);
                    // 设置订单留言
                    setOrderMessage(messageList, orderDTO);

                    // 订单集合、订单店铺map保存订单实体，下一次发现同店铺商品取出集合实体修改价格/发票信息
                    orderDTOList.add(orderDTO);
                    orderVoMap.put(orderDTO.getStoreId(), orderDTO);
                    orderGoodsMap.put(orderDTO.getStoreId(), new ArrayList<>());
                } else {
                    // 店铺已遍历到，原子订单叠加计算订单价格
                    orderDTO = orderVoMap.get(orderGoodsConfirmDTO.getStoreId());
                    reCountOrder(orderGoodsConfirmDTO, orderDTO);
                }
                // 封装订单商品信息，获取库存修改接口参数
                OrderGoodsDTO orderGoodsDTO = this.createOrderGoodsDTO(orderGoodsConfirmDTO, orderDTO, params,
                        updateActivitySurplusStorageList);
                orderGoodsDTOList.add(orderGoodsDTO);
                orderGoodsMap.get(orderDTO.getStoreId()).add(orderGoodsDTO);
            }

        });

        // 拼团订单活动
        this.createGroupActivity(orderActivityDTOList, orderVoMap, orderGoodsMap, orderPayDTO);

        // 2.封装秒杀订单活动 重置订单取消时间
        this.createSeckillActivity(orderActivityDTOList, orderVoMap, orderGoodsMap, orderPayDTO);
        // TODO xuzhch  2020/10/21 设置限时购活动信息 重置订单取消时间
        this.createFlashSaleActivity(orderActivityDTOList, orderVoMap, orderGoodsMap, orderPayDTO);
        // 设置限时购活动信息 重置订单取消时间
        // 3.设置订单优惠券信息，计算订单优惠券金额，订单商品比例扣减优惠券金额，封装订单活动保存数据
        this.countCouponsPrice(orderActivityDTOList, orderVoMap, orderGoodsMap, storeCouponsMap);

        // 4.计算满减优惠金额，计算订单优惠券金额，订单商品比例扣减优惠券金额，封装订单活动保存数据
        this.countReducePrice(orderActivityDTOList, orderVoMap, orderGoodsMap);

        MemberBalanceInfoDTO memberBalanceInfoDTO =
                memberInfoService.getMemberBalanceInfo(orderConfirmDTO.getBuyerId());
        // 5.构造父订单，封装redis，支付表拆单信息
        Date createDate = new Date();

        if (orderDTOList.size() > 1) {
            // 创建父订单
            OrderDTO orderDTO = getParentOrderDTO(orderPayDTO, orderConfirmDTO, orderAddressId, now);
            // 子订单设置父订单编号,累加计算订单金额、优惠券金额
            orderDTOList.forEach(order -> {
                // 子订单设置父订单订单编号
                order.setParentOrderSn(orderDTO.getOrderSn());
                order.setIsSplit(OrderSplitEnum.NO.value());
                order.setCreateDate(createDate);
                //发票信息
                this.fillOrderInvoice(orderConfirmDTO, invoiceDTOMap, order);
                // 父订单商品金额、订单金额、优惠券金额、优惠金额为子订单累加之和
                orderDTO.setGoodsAmount(orderDTO.getGoodsAmount().add(order.getGoodsAmount()));
                orderDTO.setOrderAmount(orderDTO.getOrderAmount().add(order.getOrderAmount()));
                orderDTO.setCouponAmount(orderDTO.getCouponAmount().add(order.getCouponAmount()));
                orderDTO.setReduceAmount(orderDTO.getReduceAmount().add(order.getReduceAmount()));
                orderDTO.setPreferentialPrice(orderDTO.getPreferentialPrice().add(order.getPreferentialPrice()));
                orderDTO.setShippingFee(orderDTO.getShippingFee().add(order.getShippingFee()));
            });
            // 保存支付金额
            orderPayDTO.setPayAmount(orderDTO.getOrderAmount());
            // 余额支付设置order_pay表的值
            orderPayDTO.setPayTotalAmount(orderPayDTO.getPayAmount());
            // 计算总的余额支付金额
            if (orderConfirmDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
                log.info("进入余额支付");
                // 如果支付总金额等于余额支付金额，则余额的钱购付。否则需要进行混合支付

                BigDecimal userBalance = memberBalanceInfoDTO.getAvailableBalance();
                if (userBalance.compareTo(orderPayDTO.getPayAmount()) > -1) {
                    log.info("进入纯余额支付");
                    orderPayDTO.setBalanceAmount(orderPayDTO.getPayTotalAmount());
                    orderPayDTO.setPayAmount(BigDecimal.ZERO);
                    balancePay = true;
                } else {

                    log.info("进入混合支付");
                    orderPayDTO.setBalanceAmount(userBalance);
                    // 混合支付设置第三方支付金额为总支付金额减去余额支付金额
                    orderPayDTO.setPayAmount(orderPayDTO.getPayTotalAmount().subtract(orderPayDTO.getBalanceAmount()));
                }


                BigDecimal balanceAmount = orderPayDTO.getBalanceAmount();

                // 计算订单支付总金额
                BigDecimal payTotalAmount = orderPayDTO.getPayTotalAmount();

                // 进行订单金额比例的余额拆分；
                // 传入余额支付金额，循环遍历订单，按照订单的金额分摊余额支付金额
                BigDecimal removeEndOrderAmount = BigDecimal.ZERO;
                for (int i = 0; i < orderDTOList.size(); i++) {
                    OrderDTO orderDTO1 = orderDTOList.get(i);
                    if (i < orderDTOList.size() - 1) {
                        // 余额乘以订单金额除以总支付金额等于分摊每个订单的余额
                        BigDecimal orderBalanceAmount =
                                balanceAmount.multiply(orderDTO1.getOrderAmount()).divide(payTotalAmount, 2,
                                        BigDecimal.ROUND_DOWN);
                        orderDTO1.setBalanceAmount(orderBalanceAmount);
                        removeEndOrderAmount = removeEndOrderAmount.add(orderBalanceAmount);
                    } else if (i == orderDTOList.size() - 1) {
                        BigDecimal lastBalance = balanceAmount.subtract(removeEndOrderAmount);
                        orderDTO1.setBalanceAmount(lastBalance);
                    }
                }

            }
            orderDTOList.add(orderDTO);

            orderPayDTO.setIsSplit(OrderSplitEnum.YES.value());
            saveOrderRedisDTO.setIsSplit(OrderSplitEnum.YES.value());
        } else {
            // 当前订单即父订单
            orderPayDTO.setIsSplit(OrderSplitEnum.NO.value());
            // 保存支付金额
            orderPayDTO.setPayAmount(orderDTOList.get(0).getOrderAmount());

            saveOrderRedisDTO.setIsSplit(OrderSplitEnum.NO.value());
            // 父订单保存订单日志，设置订单id为确认表id
            orderDTOList.forEach(order -> {
                order.setCreateDate(createDate);
                // 重新设置订单号,订单编号
                order.setId(orderConfirmDTO.getId());
                order.setOrderSn(orderPayDTO.getOrderSn());
                order.setIsSplit(OrderSplitEnum.NO.value());

                if (orderConfirmDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal userBalance = memberBalanceInfoDTO.getAvailableBalance();
                    if (userBalance.compareTo(orderPayDTO.getPayAmount()) > -1) {
                        log.info("进入纯余额支付");
                        order.setBalanceAmount(orderPayDTO.getPayAmount());
                    } else {
                        log.info("进入混合支付");
                        order.setBalanceAmount(userBalance);
                    }
                }


                //发票信息
                this.fillOrderInvoice(orderConfirmDTO, invoiceDTOMap, order);
            });

            // 余额支付设置order_pay表的值
            orderPayDTO.setPayTotalAmount(orderPayDTO.getPayAmount());
            // 判断余额支付是否大于0，大于0则说明选择了余额支付，否则不是
            if (orderConfirmDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
                log.info("进入余额支付");
                // 如果支付总金额等于余额支付金额，则余额的钱购付。否则需要进行混合支付
                BigDecimal userBalance = memberBalanceInfoDTO.getAvailableBalance();
                if (userBalance.compareTo(orderPayDTO.getPayAmount()) > -1) {
                    log.info("进入纯余额支付");
                    orderPayDTO.setBalanceAmount(orderPayDTO.getPayTotalAmount());
                    orderPayDTO.setPayAmount(BigDecimal.ZERO);
                    balancePay = true;
                } else {

                    log.info("进入混合支付");
                    orderPayDTO.setBalanceAmount(userBalance);
                    // 混合支付设置第三方支付金额为总支付金额减去余额支付金额
                    orderPayDTO.setPayAmount(orderPayDTO.getPayTotalAmount().subtract(orderPayDTO.getBalanceAmount()));
                }
            }

            orderGoodsDTOList.forEach(orderGoods -> {
                // 重新设置订单号,订单编号
                orderGoods.setOrderId(orderConfirmDTO.getId());
                orderGoods.setOrderSn(orderPayDTO.getOrderSn());
            });
            orderActivityDTOList.forEach(orderActivityDTO -> {
                // 重新设置订单号
                orderActivityDTO.setOrderId(orderConfirmDTO.getId());
            });
        }


        // 余额支付金额大于0，则需要计算分摊订单金额
        if (orderConfirmDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 进行订单金额比例的余额拆分；
            this.countBalance(orderDTOList, orderPayDTO.getBalanceAmount());
        }


        if (CollectionUtils.isNotEmpty(orderInvoiceDTOS)) {
            //保存订单发票信息
            orderInvoiceService.updateBatch(orderInvoiceDTOS);
            //保存发票详情信息
            this.saveInvoiceInfoData(orderInvoiceDTOS, orderGoodsDTOList);
        }
        // 保存订单
        this.insertBatch(ConvertUtils.sourceToTarget(orderDTOList, OrderEntity.class));
        mlogger.info(OrderStatusCode.ORDER_SAVE_SUCCESS_CODE, OrderStatusCode.ORDER_SAVE_SUCCESS_MESSAGE, logMap);

        // 保存订单商品
        orderGoodsService.saveBatch(orderGoodsDTOList);
        mlogger.info(OrderStatusCode.ORDER_GOODS_SAVE_SUCCESS_CODE, OrderStatusCode.ORDER_GOODS_SAVE_SUCCESS_MESSAGE,
                logMap);
        // 保存订单活动
        orderActivityService.saveBatch(orderActivityDTOList);
        mlogger.info(OrderStatusCode.ORDER_ACTIVITY_SAVE_SUCCESS_CODE,
                OrderStatusCode.ORDER_ACTIVITY_SAVE_SUCCESS_MESSAGE, logMap);

        // 判断是拼团活动还是其他活动，拼团活动则去更新拼团的商品活动库存，其他活动则直接更新商品库存
        Integer activityType = orderGoodsConfirmDTOList.get(0).getActivityType();
        if (activityType == ActivityTypeEnum.GROUP_ACTIVITY.value()) {
            // 更新库存,规格不存在异常
            Integer type = 0;

            // 更新拼团库存,规格不存在异常
            boolean groupStockFlag = activityGoodsService.updateStorage(updateActivitySurplusStorageList, type);
            if (!groupStockFlag) {
                // 增加redis拼团库存
                returnGroupStorage(orderGoodsDTOList);
                // redis保存支付失败信息
                saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                saveOrderRedisDTO.setResultMsg("保存订单失败，扣除活动商品剩余库存异常");
                redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                        RedisConstants.JEDIS_EXPIRE);
                log.info("保存订单失败，扣除活动商品剩余库存异常");
                throw new RuntimeException("保存订单失败，扣除活动商品剩余库存异常");
            }
        } else {
            // 更新库存,规格不存在异常
            Integer type = 0;
            boolean stockFlag = goodsSpecService.updateStorage(params, type);
            if (!stockFlag) {
                // 增加redis秒杀库存
                returnSeckillStorage(orderGoodsDTOList);
                // redis保存支付失败信息
                saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                saveOrderRedisDTO.setResultMsg("保存订单失败，扣除库存异常");
                redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                        RedisConstants.JEDIS_EXPIRE);
//                log.info("保存订单失败，更新库存失败");
//                throw new RuntimeException("保存订单失败，更新库存失败");
            }

            // 更新秒杀库存,规格不存在异常
            if (CollectionUtils.isNotEmpty(updateActivitySurplusStorageList)) {
                boolean seckillStockFlag =
                        activityGoodsService.updateStorageAndIncreaseOrderNum(updateActivitySurplusStorageList, type);
                if (!seckillStockFlag) {
                    // 增加redis秒杀库存
                    returnSeckillStorage(orderGoodsDTOList);
                    // redis保存支付失败信息
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg("保存订单失败，扣除活动商品剩余库存异常");
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    log.info("保存订单失败，扣除活动商品剩余库存异常");
                    throw new RuntimeException("保存订单失败，扣除活动商品剩余库存异常");
                }
            }

            // 获取订单使用的优惠券集合
            List<MemberCouponsDTO> memberCouponsDTOList = getOrderMemberCouponsList(orderDTOList);
            // 批量更新会员优惠券状态
            if (CollectionUtils.isNotEmpty(memberCouponsDTOList)) {
                Boolean couponsFlag = memberCouponsService.useMemberCoupons(memberCouponsDTOList);
                if (!couponsFlag) {
                    // 增加redis秒杀库存
                    returnSeckillStorage(orderGoodsDTOList);
                    // 更新优惠券状态失败，设置redis失败信息，异常回滚
                    saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
                    saveOrderRedisDTO.setResultMsg("保存订单失败，使用优惠券异常");
                    redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                            RedisConstants.JEDIS_EXPIRE);
                    log.info("保存订单失败，使用优惠券异常");
                    throw new RuntimeException("保存订单失败，使用优惠券异常");
                }
            }
        }
        // 保存订单支付信息
        Boolean payFlag = orderPayService.saveOrderPay(orderPayDTO);
        if (!payFlag) {
            // 增加redis秒杀库存
            returnSeckillStorage(orderGoodsDTOList);
            saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
            saveOrderRedisDTO.setResultMsg("订单保存失败，保存订单支付表异常");
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmDTO.getId(), saveOrderRedisDTO,
                    RedisConstants.JEDIS_EXPIRE);
            log.info("保存订单失败，保存订单支付表异常");
            throw new RuntimeException("保存订单失败，保存订单支付表异常");
        }

        //保存订单记录
        orderDTOList.forEach(this::saveOrderLog);

        return balancePay;
    }

    private void createFlashSaleActivity(List<OrderActivityDTO> orderActivityDTOList, Map<Long, OrderDTO> orderVoMap,
                                         Map<Long, List<OrderGoodsDTO>> orderGoodsMap, OrderPayDTO orderPayDTO) {
        List<Long> activicyIds = new ArrayList<>();
        for (Long storeId : orderVoMap.keySet()) {
            // 获得店铺订单
            OrderDTO orderDTO = orderVoMap.get(storeId);
            // 获得店铺下单商品
            List<OrderGoodsDTO> orderGoodsList = orderGoodsMap.get(storeId);
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsList) {
                if (ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() == orderGoodsDTO.getActivityType()) {
                    OrderActivityDTO orderActivityDTO = new OrderActivityDTO();
                    orderActivityDTO.setActivityId(orderGoodsDTO.getActivityId());
                    orderActivityDTO.setActivityType(ActivityTypeEnum.FLASH_SALE_ACTIVITY.value());
                    orderActivityDTO.setOrderId(orderDTO.getId());
                    activicyIds.add(orderGoodsDTO.getActivityId());
                    if (!orderActivityDTOList.contains(orderActivityDTO)) {
                        orderActivityDTOList.add(orderActivityDTO);
                    }

                    // 订单商品优惠总金额
                    BigDecimal multiply =
                            (orderGoodsDTO.getSpecPrice().subtract(orderGoodsDTO.getSpecPayPrice())).multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()));
                    orderGoodsDTO.setDiscountActivityAmount(orderGoodsDTO.getDiscountActivityAmount().add(multiply));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(activicyIds)) {
            // 获取秒杀设置
            Integer payEffectiveTime = flashSaleActivityService.getCancelTime(activicyIds);
            Integer orderCancelTime = 1;
            if (null == payEffectiveTime || payEffectiveTime < orderCancelTime) {
                return;
            }
            // 设置支付取消时间
            Long cancelOrder = Long.valueOf(payEffectiveTime) * OrderTimeConstants.MINUTE;
            Date cancelDate = new Date(System.currentTimeMillis() + cancelOrder);
            orderPayDTO.setCancalDate(cancelDate.compareTo(orderPayDTO.getCancalDate()) <= 0 ? cancelDate :
                    orderPayDTO.getCancalDate());
        }

    }

    /**
     * 功能描述：
     * <保存订单异常，返回秒杀库存>
     *
     * @param orderGoodsDTOList
     * @return
     * @date 2020/3/23 14:29
     * @author 刘远杰
     **/
    private void returnSeckillStorage(List<OrderGoodsDTO> orderGoodsDTOList) {
        orderGoodsDTOList.forEach(orderGoodsDTO -> {
            if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == orderGoodsDTO.getActivityType()) {
                // 回滚redis秒杀库存
                String storageKey =
                        ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + orderGoodsDTO.getActivityId() + "_" + orderGoodsDTO.getSpecId();
                try {
                    // 返回秒杀库存
                    redisUtils.increment(storageKey, orderGoodsDTO.getGoodsNum());
                } catch (Exception e) {
                    log.error("保存订单异常回滚秒杀库存异常");
                }
            }
        });
    }

    /**
     * 保存订单异常，返回拼团库存
     *
     * @param orderGoodsDTOList
     * @return
     * @date 2020-03-24 17:12
     * @author huangkeyuan@leimingtech.com
     **/
    private void returnGroupStorage(List<OrderGoodsDTO> orderGoodsDTOList) {
        orderGoodsDTOList.forEach(orderGoodsDTO -> {
            if (ActivityTypeEnum.GROUP_ACTIVITY.value() == orderGoodsDTO.getActivityType()) {
                // 回滚redis拼团库存
                String storageKey =
                        ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + orderGoodsDTO.getActivityId() + "_" + orderGoodsDTO.getSpecId();
                try {
                    // 返回拼团库存
                    redisUtils.increment(storageKey, orderGoodsDTO.getGoodsNum());
                } catch (Exception e) {
                    log.error("保存订单异常回滚拼团库存异常");
                }
            }
        });
    }

    /**
     * 功能描述：
     * <取消订单返回秒杀库存>
     *
     * @param orderGoodsDTOList 订单商品
     * @return
     * @date 2020/3/23 14:29
     * @author 刘远杰
     **/
    private void orderCancelReturnSeckillStorage(List<OrderGoodsDTO> orderGoodsDTOList) {
        List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageDTOList = new ArrayList<>();
        orderGoodsDTOList.forEach(orderGoodsDTO -> {
            if (ActivityTypeEnum.GROUP_ACTIVITY.value() == orderGoodsDTO.getActivityType()) {
                // 拼团商品回退活动库存
                String storageKey =
                        ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + orderGoodsDTO.getActivityId() + "_" + orderGoodsDTO.getSpecId();

                Object storageObj = redisUtils.get(storageKey);
                if (storageObj != null) {
                    // redis存在活动库存则活动未结束 执行活动库存增加操作
                    try {
                        // 增加redis库存
                        redisUtils.increment(storageKey, orderGoodsDTO.getGoodsNum());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO =
                            new UpdateActivitySurplusStorageDTO();
                    updateActivitySurplusStorageDTO.setSpecId(orderGoodsDTO.getSpecId());
                    updateActivitySurplusStorageDTO.setActivityId(orderGoodsDTO.getActivityId());
                    updateActivitySurplusStorageDTO.setActivityType(orderGoodsDTO.getActivityType());
                    updateActivitySurplusStorageDTOList.add(updateActivitySurplusStorageDTO);
                }
            }
        });

        // 更新数据库活动库存
        if (CollectionUtils.isNotEmpty(updateActivitySurplusStorageDTOList)) {
            boolean seckillStockFlag = activityGoodsService.updateStorage(updateActivitySurplusStorageDTOList, 1);
            if (!seckillStockFlag) {
                // 回滚redis秒杀库存
                reduceGroupStorage(orderGoodsDTOList);
                log.info("取消订单失败，更新秒杀库存失败");
                throw new ServiceException(OrderStatusCode.CANCEL_ORDER_UPDATE_SECKILL_STROAGR_EXCEPTION);
            }
        }

    }

    /**
     * 功能描述：
     * <取消订单订单异常，扣减redis拼团库存>
     *
     * @param orderGoodsDTOList
     * @return
     * @date 2020/3/23 14:29
     * @author 刘远杰
     **/
    private void reduceGroupStorage(List<OrderGoodsDTO> orderGoodsDTOList) {
        orderGoodsDTOList.forEach(orderGoodsDTO -> {
            if (ActivityTypeEnum.GROUP_ACTIVITY.value() == orderGoodsDTO.getActivityType()) {
                // 回滚redis秒杀库存
                String storageKey =
                        ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + orderGoodsDTO.getActivityId() + "_" + orderGoodsDTO.getSpecId();
                try {
                    // 返回秒杀库存
                    redisUtils.decrement(storageKey, orderGoodsDTO.getGoodsNum());
                } catch (Exception e) {
                    log.error("保存订单异常回滚拼团库存异常");
                }
            }
        });
    }

    /**
     * 功能描述：
     * <保存发票信息>
     *
     * @param orderInvoiceDTOS  订单发票集合
     * @param orderGoodsDTOList 订单商品集合
     * @return
     * @date 2020年3月31日11:29:16
     * @author xuzhch
     **/
    private void saveInvoiceInfoData(List<OrderInvoiceDTO> orderInvoiceDTOS, List<OrderGoodsDTO> orderGoodsDTOList) {
        //发票详情
        List<InvoiceInfoDTO> invoiceInfoDTOS = new ArrayList<InvoiceInfoDTO>();
        for (OrderInvoiceDTO orderInvoiceDTO : orderInvoiceDTOS) {
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                if (orderInvoiceDTO.getOrderSn().compareTo(orderGoodsDTO.getOrderSn()) == 0) {
                    InvoiceInfoDTO invoiceInfoDTO = new InvoiceInfoDTO();
                    invoiceInfoDTO.setInvoiceId(orderInvoiceDTO.getId());
                    invoiceInfoDTO.setGoodsId(orderGoodsDTO.getGoodsId());
                    invoiceInfoDTO.setSpu(orderGoodsDTO.getSpuSerial());
                    invoiceInfoDTO.setFirstGcId(orderGoodsDTO.getFirstGcId());
//                invoiceInfoDTO.setFirstGcName(orderGoodsDTO.get());
                    invoiceInfoDTO.setGoodsName(orderGoodsDTO.getSpuName());
                    invoiceInfoDTO.setSpecId(orderGoodsDTO.getSpecId());
                    invoiceInfoDTO.setSpecInfo(orderGoodsDTO.getSpecAttrValueName());
                    invoiceInfoDTO.setGoodsNum(orderGoodsDTO.getGoodsNum());
                    invoiceInfoDTO.setSpecTotalPrice(
                            orderGoodsDTO.getSpecPayPrice()
                                    .multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()))
                                    .add(orderGoodsDTO.getAvgPreferentialAmount()));
                    invoiceInfoDTO.setSpecPayPrice(orderGoodsDTO.getSpecPrice());
                    invoiceInfoDTOS.add(invoiceInfoDTO);
                }
            }
        }
        invoiceInfoService.saveBatch(invoiceInfoDTOS);
    }

    /**
     * 功能描述：
     * <封装订单发票数据>
     *
     * @param orderConfirmDTO 订单确认表数据
     * @param invoiceDTOMap   订单发票数据Map
     * @param order           订单数据
     * @return
     * @date 2020年3月31日11:29:16
     * @author xuzhch
     **/
    private void fillOrderInvoice(OrderConfirmDTO orderConfirmDTO, Map<Long, OrderInvoiceDTO> invoiceDTOMap,
                                  OrderDTO order) {
        order.setInvoiceFlag(0);
        if (BeanUtil.isEmpty(invoiceDTOMap) || invoiceDTOMap.size() == 0) {
            return;
        }
        //发票信息
        OrderInvoiceDTO orderInvoiceDTO = invoiceDTOMap.get(order.getStoreId());
        if (BeanUtil.isEmpty(orderInvoiceDTO)) {
            //不开票
            order.setInvoiceFlag(OrderInvoiceEnum.INVOICE_NO.value());
            return;
        }
        if (!BeanUtil.isEmpty(orderInvoiceDTO) && null != orderInvoiceDTO.getCompanyType()) {
            order.setInvoiceFlag(orderInvoiceDTO.getCompanyType());
        }
        orderInvoiceDTO.setCreateOrderDate(order.getCreateDate());
        orderInvoiceDTO.setApplyDate(order.getCreateDate());
        orderInvoiceDTO.setOrderSn(order.getOrderSn());
        orderInvoiceDTO.setInvoiceEvolve(1);
        orderInvoiceDTO.setOrderId(order.getId());
        orderInvoiceDTO.setMemberName(order.getBuyerName());
        orderInvoiceDTO.setPayAmount(order.getOrderAmount().add(order.getPreferentialPrice()));
    }

    /**
     * 计算余额，按照订单金额比例进行分摊
     *
     * @param orderDTOList 订单列表
     * @return
     * @date 2020-07-08 11:18
     * @author huangkeyuan@leimingtech.com
     **/
    private void countBalance(List<OrderDTO> orderDTOList, BigDecimal balanceAmount) {

        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        memberUpdateDTO.setAvailableBalance(balanceAmount.negate());
        memberUpdateDTO.setBlockedBalance(balanceAmount);
        memberUpdateDTO.setId(orderDTOList.get(0).getBuyerId());
        // 将用户余额进行冻结
        memberInfoService.updateByMemberId(memberUpdateDTO);
    }

    /**
     * 功能描述：
     * <获取订单使用的优惠券集合>
     *
     * @param orderDTOList 订单集合
     * @return
     * @date 2020/2/26 10:33
     * @author 刘远杰
     **/
    private List<MemberCouponsDTO> getOrderMemberCouponsList(List<OrderDTO> orderDTOList) {
        List<MemberCouponsDTO> memberCouponsDTOList = new ArrayList<>();
        orderDTOList.forEach(order -> {
            // 子订单设置父订单订单编号
            if (OrderSplitEnum.NO.value() == order.getIsSplit()) {
                // 修改优惠券状态
                String memberCouponsId = order.getMemberCouponsId();
                if (StringUtils.isNotBlank(memberCouponsId)) {
                    // 更新会员优惠券状态，设置订单信息
                    MemberCouponsDTO memberCouponsDTO = new MemberCouponsDTO();
                    JSONObject jsonObject = JSONObject.parseObject(memberCouponsId);
                    Object o = jsonObject.get(order.getStoreId());
                    memberCouponsDTO.setId(Long.parseLong(o.toString()));
                    memberCouponsDTO.setMemberId(order.getBuyerId());
                    memberCouponsDTO.setCouponsState(CouponsEnum.COUPONS_USED.value());
                    memberCouponsDTO.setUseDate(new Date());
                    memberCouponsDTO.setOrderId(order.getId());
                    memberCouponsDTO.setOrderSn(order.getOrderSn());
                    memberCouponsDTO.setOrderAmount(order.getOrderAmount());
                    memberCouponsDTO.setGoodsAmount(order.getGoodsAmount());
                    memberCouponsDTOList.add(memberCouponsDTO);
                }
            }
        });
        return memberCouponsDTOList;
    }

    /**
     * 功能描述:
     * 〈设置订单优惠券信息，计算订单优惠券金额，订单商品比例扣减优惠券金额〉
     *
     * @param orderActivityDTOList 订单活动
     * @param orderVoMap           店铺结算商品信息
     * @param orderGoodsMap        商品按店铺分组
     * @param storeCouponsMap      优惠券按店铺分组
     * @author : 刘远杰
     */
    private void countCouponsPrice(List<OrderActivityDTO> orderActivityDTOList, Map<Long, OrderDTO> orderVoMap,
                                   Map<Long, List<OrderGoodsDTO>> orderGoodsMap,
                                   Map<Long, MemberCouponsIndexDTO> storeCouponsMap) {
        for (Long storeId : orderVoMap.keySet()) {
            // 获得店铺订单
            OrderDTO orderDTO = orderVoMap.get(storeId);
            // 获得下单选择的店铺优惠券
            MemberCouponsIndexDTO memberCouponsIndexDTO = storeCouponsMap.get(orderDTO.getStoreId());
            if (memberCouponsIndexDTO != null) {
                // 获取当前优惠券活动的结算商品集合
                List<OrderGoodsDTO> couponsGoodsList = new ArrayList<>();
                Integer activityGoodsScope = memberCouponsIndexDTO.getActivityGoodsScope();
                // 当前优惠券活动的结算商品总金额
                BigDecimal activityGoodsPrice = BigDecimal.ZERO;
                if (CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == activityGoodsScope) {
                    // 店铺范围优惠券活动，购物车该店铺商品全部为活动商品，去除秒杀商品
                    for (OrderGoodsDTO orderGoodsDTO : orderGoodsMap.get(storeId)) {
                        if (orderGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()) {
                            // 活动商品总价格 = 当前活动商品价格 + 该商品价格
                            activityGoodsPrice =
                                    orderGoodsDTO.getSpecPrice().multiply(new BigDecimal(orderGoodsDTO.getGoodsNum())).add(activityGoodsPrice);
                            couponsGoodsList.add(orderGoodsDTO);
                        }
                    }
                } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == activityGoodsScope) {
                    // 指定店铺分类
                    List<CouponsGoodsDTO> relationList = memberCouponsIndexDTO.getGoodsList();
                    for (CouponsGoodsDTO relation : relationList) {
                        for (OrderGoodsDTO orderGoodsDTO : orderGoodsMap.get(storeId)) {
                            if (orderGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()
                                    && orderGoodsDTO.getSecondStoreGoodsGcId().equals(relation.getRelationId())) {
                                // 购物车商品店铺分类id等于活动关联商品品牌id,该商品加入当前优惠券活动商品集合，活动商品总价格 = 当前活动商品价格 + 该商品价格
                                activityGoodsPrice =
                                        orderGoodsDTO.getSpecPrice().multiply(new BigDecimal(orderGoodsDTO.getGoodsNum())).add(activityGoodsPrice);
                                couponsGoodsList.add(orderGoodsDTO);
                            }
                        }
                    }
                } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == activityGoodsScope) {
                    // 指定商品
                    List<CouponsGoodsDTO> relationList = memberCouponsIndexDTO.getGoodsList();
                    if (CollectionUtils.isNotEmpty(relationList)) {
                        for (CouponsGoodsDTO relation : relationList) {
                            for (OrderGoodsDTO orderGoodsDTO : orderGoodsMap.get(storeId)) {
                                if (orderGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()
                                        && orderGoodsDTO.getGoodsId().equals(relation.getRelationId())) {
                                    // 购物车商品id等于活动关联商品id,该商品加入该优惠券活动商品集合，活动商品总价格 = 当前活动商品价格 + 该商品价格
                                    activityGoodsPrice =
                                            orderGoodsDTO.getSpecPrice().multiply(new BigDecimal(orderGoodsDTO.getGoodsNum())).add(activityGoodsPrice);
                                    couponsGoodsList.add(orderGoodsDTO);
                                }
                            }
                        }
                    }
                } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == activityGoodsScope) {
                    // 指定品牌
                    List<CouponsGoodsDTO> relationList = memberCouponsIndexDTO.getGoodsList();
                    for (CouponsGoodsDTO relation : relationList) {
                        for (OrderGoodsDTO orderGoodsDTO : orderGoodsMap.get(storeId)) {
                            if (orderGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()
                                    && orderGoodsDTO.getBrandId().equals(relation.getRelationId())) {
                                // 购物车商品品牌id等于活动关联商品品牌id,该商品加入当前优惠券活动商品集合，活动商品总价格 = 当前活动商品价格 + 该商品价格
                                activityGoodsPrice =
                                        orderGoodsDTO.getSpecPrice().multiply(new BigDecimal(orderGoodsDTO.getGoodsNum())).add(activityGoodsPrice);
                                couponsGoodsList.add(orderGoodsDTO);
                            }
                        }
                    }
                }
                // 达到优惠券使用条件，订单设置优惠券信息，修改优惠金额，商品按比例均摊优惠券优惠金额
                countSimpleCouponsPrice(orderActivityDTOList, storeId, orderDTO, memberCouponsIndexDTO,
                        couponsGoodsList, activityGoodsPrice);
            }
        }
    }

    /**
     * 设置订单满减信息，计算订单优惠满减金额，订单商品比例扣减满减金额，封装订单活动保存数据
     *
     * @param orderActivityDTOList 订单活动实体（待封装数据）
     * @param orderVoMap           商品按店铺分组
     * @param orderGoodsMap        订单商品按店铺分组
     * @return
     * @date
     * @author 刘远杰
     **/
    private void countReducePrice(List<OrderActivityDTO> orderActivityDTOList, Map<Long, OrderDTO> orderVoMap,
                                  Map<Long, List<OrderGoodsDTO>> orderGoodsMap) {
        for (Long storeId : orderVoMap.keySet()) {
            // 商品按照满减活动分组
            Map<Long, List<OrderGoodsDTO>> reduceActivityGroupMap = new HashMap<>();
            // 获得店铺订单
            OrderDTO orderDTO = orderVoMap.get(storeId);
            if (orderDTO.getCouponAmount().compareTo(BigDecimal.ZERO) == 0) {
                // 未使用优惠券方可使用满减活动,对满减活动商品分组
                // 获得店铺下单商品
                List<OrderGoodsDTO> orderGoodsList = orderGoodsMap.get(storeId);
                for (OrderGoodsDTO orderGoodsDTO : orderGoodsList) {
                    // 遍历店铺下单商品，按满减活动id分组
                    if (orderGoodsDTO.getActivityType() == ActivityTypeEnum.REDUCE_ACTIVITY.value()
                            && orderGoodsDTO.getActivityId() != null) {
                        Long activityKey = orderGoodsDTO.getActivityId();
                        if (!reduceActivityGroupMap.containsKey(activityKey)) {
                            List<OrderGoodsDTO> reduceOrderGoodsList = new ArrayList<>();
                            reduceOrderGoodsList.add(orderGoodsDTO);
                            reduceActivityGroupMap.put(activityKey, reduceOrderGoodsList);
                        } else {
                            List<OrderGoodsDTO> reduceOrderGoodsList = reduceActivityGroupMap.get(activityKey);
                            reduceOrderGoodsList.add(orderGoodsDTO);
                        }
                    }
                }
            } else {
                // 使用优惠券，清空选择的满减
                List<OrderGoodsDTO> orderGoodsList = orderGoodsMap.get(storeId);
//                orderGoodsList.forEach(orderGoodsDTO -> orderGoodsDTO.setReduceActivityId(null));
                orderGoodsList.forEach(orderGoodsDTO -> {
                    if (orderGoodsDTO.getActivityType() == ActivityTypeEnum.REDUCE_ACTIVITY.value()) {
                        orderGoodsDTO.setActivityId(null);
                        orderGoodsDTO.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
                    }
                });
            }

            // 遍历每个满减活动对应的商品，计算商品总价格，使用对应的满减规则
            for (Long activityId : reduceActivityGroupMap.keySet()) {
                BigDecimal reduceGoodsPrice = BigDecimal.ZERO;
                // 计算满减商品总价格
                List<OrderGoodsDTO> reduceGoodsList = reduceActivityGroupMap.get(activityId);
                for (OrderGoodsDTO orderGoodsDTO : reduceGoodsList) {
                    reduceGoodsPrice =
                            reduceGoodsPrice.add(orderGoodsDTO.getSpecPrice().multiply(new BigDecimal(orderGoodsDTO.getGoodsNum())));
                }

                List<ReduceRuleDTO> reduceRuleDTOList = reduceRuleService.getByActivityId(activityId);
                // 订单满减金额计算、订单商品均摊活动金额、封装订单活动表保存数据
                countReducePrice(orderDTO, reduceRuleDTOList, reduceGoodsList, reduceGoodsPrice, orderActivityDTOList);
            }
        }
    }

    /**
     * 封装订单秒杀活动保存数据
     *
     * @param orderActivityDTOList 订单活动实体（待封装数据）
     * @param orderVoMap           商品按店铺分组
     * @param orderGoodsMap        订单商品按店铺分组
     * @param orderGoodsMap        订单商品按店铺分组
     * @return
     * @date
     * @author 刘远杰
     **/
    private void createSeckillActivity(List<OrderActivityDTO> orderActivityDTOList, Map<Long, OrderDTO> orderVoMap,
                                       Map<Long, List<OrderGoodsDTO>> orderGoodsMap, OrderPayDTO orderPayDTO) {
        boolean flag = false;
        for (Long storeId : orderVoMap.keySet()) {
            // 获得店铺订单
            OrderDTO orderDTO = orderVoMap.get(storeId);
            // 获得店铺下单商品
            List<OrderGoodsDTO> orderGoodsList = orderGoodsMap.get(storeId);
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsList) {
                if (ActivityTypeEnum.SECKILL_ACTIVITY.value() == orderGoodsDTO.getActivityType()) {
                    OrderActivityDTO orderActivityDTO = new OrderActivityDTO();
                    orderActivityDTO.setActivityId(orderGoodsDTO.getActivityId());
                    orderActivityDTO.setActivityType(ActivityTypeEnum.SECKILL_ACTIVITY.value());
                    orderActivityDTO.setOrderId(orderDTO.getId());
                    if (!orderActivityDTOList.contains(orderActivityDTO)) {
                        orderActivityDTOList.add(orderActivityDTO);
                        flag = true;
                    }

                    // 订单商品优惠总金额
                    BigDecimal multiply =
                            (orderGoodsDTO.getSpecPrice().subtract(orderGoodsDTO.getSpecPayPrice())).multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()));
                    orderGoodsDTO.setDiscountActivityAmount(orderGoodsDTO.getDiscountActivityAmount().add(multiply));
                }
            }
        }
        if (flag) {
            // 获取秒杀设置
            SettingSeckillDTO seckillSetting = settingService.getSeckillSetting();
            if (seckillSetting != null) {
                // 设置支付取消时间
                Integer payEffectiveTime = seckillSetting.getPayEffectiveTime();
                Long cancelOrder = Long.valueOf(payEffectiveTime) * OrderTimeConstants.MINUTE;
                Date cancelDate = new Date(System.currentTimeMillis() + cancelOrder);
                orderPayDTO.setCancalDate(cancelDate.compareTo(orderPayDTO.getCancalDate()) <= 0 ? cancelDate :
                        orderPayDTO.getCancalDate());
            }
        }
    }

    /**
     * 封装订单拼团活动保存数据
     *
     * @param orderActivityDTOList 订单活动实体（待封装数据）
     * @param orderVoMap           商品按店铺分组
     * @param orderGoodsMap        订单商品按店铺分组
     * @param orderGoodsMap        订单商品按店铺分组
     * @return
     * @date 2020-03-24 17:38
     * @author huangkeyuan@leimingtech.com
     **/
    private void createGroupActivity(List<OrderActivityDTO> orderActivityDTOList, Map<Long, OrderDTO> orderVoMap,
                                     Map<Long, List<OrderGoodsDTO>> orderGoodsMap, OrderPayDTO orderPayDTO) {

        for (Long storeId : orderVoMap.keySet()) {
            // 获得店铺订单
            OrderDTO orderDTO = orderVoMap.get(storeId);
            // 获得店铺下单商品
            List<OrderGoodsDTO> orderGoodsList = orderGoodsMap.get(storeId);
            for (OrderGoodsDTO orderGoodsDTO : orderGoodsList) {
                if (ActivityTypeEnum.GROUP_ACTIVITY.value() == orderGoodsDTO.getActivityType()) {
                    OrderActivityDTO orderActivityDTO = new OrderActivityDTO();
                    orderActivityDTO.setActivityId(orderGoodsDTO.getActivityId());
                    orderActivityDTO.setActivityType(ActivityTypeEnum.GROUP_ACTIVITY.value());
                    orderActivityDTO.setOrderId(orderDTO.getId());
                    orderActivityDTOList.add(orderActivityDTO);

                    // 订单商品优惠总金额
                    BigDecimal multiply =
                            (orderGoodsDTO.getSpecPrice().subtract(orderGoodsDTO.getSpecPayPrice())).multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()));
                    orderGoodsDTO.setDiscountActivityAmount(orderGoodsDTO.getDiscountActivityAmount().add(multiply));
                }
            }
        }

    }

    /**
     * 功能描述:
     * 〈设置订单满减信息，计算订单优惠满减金额，订单商品比例扣减满减金额，封装订单活动保存数据〉
     *
     * @param orderDTO             订单信息
     * @param reduceRuleDTOList    满减规则集合
     * @param reduceGoodsList      满减商品集合
     * @param reduceGoodsPrice     活动商品总金额
     * @param orderActivityDTOList 订单活动集合
     * @author : 刘远杰
     */
    private void countReducePrice(OrderDTO orderDTO, List<ReduceRuleDTO> reduceRuleDTOList,
                                  List<OrderGoodsDTO> reduceGoodsList,
                                  BigDecimal reduceGoodsPrice, List<OrderActivityDTO> orderActivityDTOList) {
        if (CollectionUtils.isNotEmpty(reduceRuleDTOList)) {
            for (ReduceRuleDTO reduceRuleDTO : reduceRuleDTOList) {
                // 达到规则门槛
                if (reduceGoodsPrice.compareTo(reduceRuleDTO.getLimitAmount()) >= 0) {
                    // 封装订单活动表数据
                    OrderActivityDTO orderActivityDTO = new OrderActivityDTO();
                    orderActivityDTO.setActivityId(reduceRuleDTO.getActivityId());
                    orderActivityDTO.setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                    orderActivityDTO.setOrderId(orderDTO.getId());
                    orderActivityDTO.setRuleId(reduceRuleDTO.getId());
                    orderActivityDTOList.add(orderActivityDTO);

                    // 订单商品封装活动
                    reduceGoodsList.forEach(reduceGoods -> {
                        reduceGoods.setActivityId(reduceRuleDTO.getActivityId());
                        reduceGoods.setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
                    });

                    BigDecimal reduceAmount = BigDecimal.ZERO;
                    if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == reduceRuleDTO.getRuleType()
                            || ReduceActivityEnum.RULE_TYPE_LADDER.value() == reduceRuleDTO.getRuleType()) {
                        // 满减活动商品总金额达到满减规则使用条件，设置订单满减信息，计算订单满减金额，订单商品比例扣减满减金额
                        reduceAmount = reduceRuleDTO.getReduceAmount();
                        // 订单满减金额 = 当前订单满减金额 + 该满减活动优惠金额
                        orderDTO.setReduceAmount(orderDTO.getReduceAmount().add(reduceAmount));
                    } else if (ReduceActivityEnum.RULE_TYPE_AVG.value() == reduceRuleDTO.getRuleType()) {
                        // 每满减次数 = 满减商品总金额 / 满减限制金额
                        BigDecimal ladder = reduceGoodsPrice.divide(reduceRuleDTO.getLimitAmount(), 0,
                                BigDecimal.ROUND_DOWN);
                        // 满减活动商品总金额达到满减规则使用条件，设置订单满减信息，计算订单满减金额，订单商品比例扣减满减金额
                        reduceAmount = reduceRuleDTO.getReduceAmount().multiply(ladder);
                        // 订单满减金额 = 当前订单满减金额 + 该满减活动优惠金额
                        orderDTO.setReduceAmount(orderDTO.getReduceAmount().add(reduceAmount));
                        // 订单满减金额 = 当前订单满减金额 + 该满减活动优惠金额
                    }
                    // 摊单个订单活动金额，设置订单优惠金额，分摊商品优惠金额
                    avgSimpleOrderActivityPrice(orderDTO, reduceGoodsList, reduceGoodsPrice, reduceAmount,
                            ActivityTypeEnum.REDUCE_ACTIVITY.value());
                    return;
                }
            }
        } else {
            // 未达到使用门槛，清除订单商品设置的满减活动id
            reduceGoodsList.forEach(reduceGoods -> reduceGoods.setReduceActivityId(null));
        }
    }

    /**
     * 分摊单个订单活动金额，设置订单优惠金额，分摊商品优惠金额
     *
     * @param orderDTO           订单实体
     * @param activityGoodsList  活动商品集合
     * @param activityGoodsPrice 商品商品总金额
     * @param activityAmount     活动优惠金额
     * @param activityType       活动类型 0无活动 1优惠券 2满减
     * @return
     * @date
     * @author 刘远杰
     **/
    private void avgSimpleOrderActivityPrice(OrderDTO orderDTO, List<OrderGoodsDTO> activityGoodsList,
                                             BigDecimal activityGoodsPrice, BigDecimal activityAmount,
                                             Integer activityType) {
        // 订单实付金额 = 当前订单实付金额 - 该满减活动优惠金额
        orderDTO.setOrderAmount(orderDTO.getOrderAmount().subtract(activityAmount));
        // 订单优惠金额 = 当前订单优惠金额 + 该满减活动优惠金额
        orderDTO.setPreferentialPrice(orderDTO.getPreferentialPrice().add(activityAmount));
        // 活动商品均摊优惠金额
        if (CollectionUtils.isNotEmpty(activityGoodsList)) {
            // 满减金额
            if (activityGoodsList.size() == 1) {
                // 活动只一种商品
                OrderGoodsDTO orderGoodsDTO = activityGoodsList.get(0);

                // 分摊单种商品优惠券金额
                avgSimpleOrderGoodsActivityPrice(orderGoodsDTO, activityAmount, activityType);
            } else {
                // 活动多种商品

                // 除去最后一件商品的满减金额
                BigDecimal removeEndGoodsCouponAmount = BigDecimal.ZERO;

                for (int i = 0; i < activityGoodsList.size(); i++) {
                    OrderGoodsDTO orderGoodsDTO = activityGoodsList.get(i);

                    // 商品数量
                    BigDecimal goodsNumber = new BigDecimal(orderGoodsDTO.getGoodsNum());
                    if (i < activityGoodsList.size() - 1) {
                        // 单种商品的分摊优惠券金额 = 该商品总价格 * 优惠总价格 / 商品总价格
                        BigDecimal avgReduceAmount =
                                orderGoodsDTO.getSpecPrice().multiply(goodsNumber).multiply(activityAmount).divide(activityGoodsPrice, 2,
                                        BigDecimal.ROUND_DOWN);
                        // 分摊单种商品优惠金额
                        avgSimpleOrderGoodsActivityPrice(orderGoodsDTO, avgReduceAmount, activityType);
                        // 除去最后一件商品的优惠金额 = 当前除去最后一件商品的优惠金额 + 该商品分摊优惠券金额
                        removeEndGoodsCouponAmount = removeEndGoodsCouponAmount.add(avgReduceAmount);
                    } else if (i == activityGoodsList.size() - 1) {
                        // 最后一件商品的分摊优惠券金额 = 优惠券总面额 - 除去最后一件商品的优惠券金额
                        BigDecimal avgReduceAmount = activityAmount.subtract(removeEndGoodsCouponAmount);
                        // 分摊单种商品优惠金额
                        avgSimpleOrderGoodsActivityPrice(orderGoodsDTO, avgReduceAmount, activityType);
                    }
                }
            }
        }
    }

    /**
     * 功能描述:
     * 〈设置订单优惠券信息，计算订单优惠券金额，订单商品比例扣减优惠券金额（单个优惠券处理）〉
     *
     * @param orderActivityDTOList  订单活动
     * @param storeId               店铺id
     * @param orderDTO              订单信息
     * @param memberCouponsIndexDTO 优惠券信息
     * @param couponsGoodsList      优惠券商品集合
     * @param activityGoodsPrice    活动商品总金额
     * @author : 刘远杰
     */
    private void countSimpleCouponsPrice(List<OrderActivityDTO> orderActivityDTOList, Long storeId, OrderDTO orderDTO,
                                         MemberCouponsIndexDTO memberCouponsIndexDTO,
                                         List<OrderGoodsDTO> couponsGoodsList,
                                         BigDecimal activityGoodsPrice) {
        if (activityGoodsPrice.compareTo(memberCouponsIndexDTO.getLimitAmount()) >= 0) {
            // 封装订单活动表数据
            OrderActivityDTO orderActivityDTO = new OrderActivityDTO();
            orderActivityDTO.setActivityId(memberCouponsIndexDTO.getActivityId());
            orderActivityDTO.setActivityType(ActivityTypeEnum.COUPONS_ACTIVITY.value());
            orderActivityDTO.setOrderId(orderDTO.getId());
            orderActivityDTOList.add(orderActivityDTO);

            // 订单商品封装活动
            couponsGoodsList.forEach(couponsGoods -> {
                couponsGoods.setActivityId(memberCouponsIndexDTO.getActivityId());
                couponsGoods.setActivityType(ActivityTypeEnum.COUPONS_ACTIVITY.value());
            });

            // 优惠券活动商品子那个金额达到优惠券使用条件，设置订单优惠券信息，计算订单优惠券金额，订单商品比例扣减优惠券金额
            BigDecimal faceValue = memberCouponsIndexDTO.getFaceValue();
            // 订单设置优惠券信息
            HashMap<Long, Long> memberCouponsId = new HashMap<>();
            memberCouponsId.put(storeId, memberCouponsIndexDTO.getId());
            orderDTO.setMemberCouponsId(JSONObject.toJSONString(memberCouponsId));
            // 订单优惠券金额 = 当前订单优惠券金额 + 该优惠券活动优惠金额
            orderDTO.setCouponAmount(orderDTO.getCouponAmount().add(faceValue));
            // 摊单个订单活动金额，设置订单优惠金额，分摊商品优惠金额
            avgSimpleOrderActivityPrice(orderDTO, couponsGoodsList, activityGoodsPrice, faceValue,
                    ActivityTypeEnum.COUPONS_ACTIVITY.value());
        }
    }

    /**
     * 功能描述:
     * 〈分摊单种商品优惠金额〉
     *
     * @param orderGoodsDTO  订单商品
     * @param activityAmount 单种商品的分摊优惠金额
     * @param activityType   活动类型 0无活动 1优惠券 2满减
     * @author : 刘远杰
     */
    private void avgSimpleOrderGoodsActivityPrice(OrderGoodsDTO orderGoodsDTO, BigDecimal activityAmount,
                                                  Integer activityType) {
        // 单件商品的分摊优惠金额 =（该商品分摊优惠券金额 / 商品购买个数）向上取整 保留两位小数
        BigDecimal perCouponAmount = activityAmount.divide(new BigDecimal(orderGoodsDTO.getGoodsNum()), 2,
                BigDecimal.ROUND_UP);
        // 每件商品的实付金额 = 商品当前实付金额 - 单件商品的优惠金额
        BigDecimal specPayPrice = orderGoodsDTO.getSpecPayPrice().subtract(perCouponAmount);
        // 均摊金额 = 该商品分摊优惠券金额 - （单件商品的优惠金额 * 商品数量）+ 当前均摊金额
        BigDecimal avgPreferentialAmount = orderGoodsDTO.getAvgPreferentialAmount();
        if (activityAmount.compareTo(perCouponAmount.multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()))) == 0) {
            avgPreferentialAmount =
                    activityAmount.subtract(perCouponAmount.multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()))).add(avgPreferentialAmount);
        } else {
            avgPreferentialAmount =
                    activityAmount.subtract((perCouponAmount.subtract(new BigDecimal(0.01D))).multiply(new BigDecimal(orderGoodsDTO.getGoodsNum()))).add(avgPreferentialAmount);
        }

        // 商品折扣金额 = 商品当前折扣金额 + 该商品分摊优惠券金额
        BigDecimal discountActivityAmount = orderGoodsDTO.getDiscountActivityAmount().add(activityAmount);

        // 设置优惠金额信息
        orderGoodsDTO.setAvgPreferentialAmount(avgPreferentialAmount);
        orderGoodsDTO.setDiscountActivityAmount(discountActivityAmount);
        if (ActivityTypeEnum.COUPONS_ACTIVITY.value() == activityType) {
            // 优惠券活动
            orderGoodsDTO.setActivityType(ActivityTypeEnum.COUPONS_ACTIVITY.value());
            orderGoodsDTO.setCouponAmount(activityAmount);
        } else if (ActivityTypeEnum.REDUCE_ACTIVITY.value() == activityType) {
            // 满减活动
            orderGoodsDTO.setActivityType(ActivityTypeEnum.REDUCE_ACTIVITY.value());
            orderGoodsDTO.setReduceAmount(activityAmount);
        }
        orderGoodsDTO.setSpecPayPrice(specPayPrice);
    }

    /**
     * 功能描述:
     * 〈创建父订单信息〉
     *
     * @param orderPayDTO     订单支付信息
     * @param orderConfirmDTO 订单确认信息
     * @param orderAddressId  订单地址信息
     * @param now             下单时间
     * @author : 刘远杰
     */
    private OrderDTO getParentOrderDTO(OrderPayDTO orderPayDTO, OrderConfirmDTO orderConfirmDTO, Long orderAddressId,
                                       Date now) {
        OrderDTO orderDTO = new OrderDTO();
        BeanCopier.create(orderConfirmDTO.getClass(), orderDTO.getClass(), false)
                .copy(orderConfirmDTO, orderDTO, null);
        // 将订单确认表ID和主订单表的ID设置为同一个值
        orderDTO.setId(orderConfirmDTO.getId());
        orderDTO.setOrderSn(orderPayDTO.getOrderSn());
        orderDTO.setOrderStatus(OrderStatusEnum.WAITTING_PAYMENT.value());
        orderDTO.setAppStatus(OrderAppStatusEnum.APP_WAITTING_PAYMENT.value());
        orderDTO.setPayId(orderPayDTO.getId());
        orderDTO.setPaySn(orderPayDTO.getPaySn());
        orderDTO.setPaymentStatus(PayStateEnum.NO.value());
        orderDTO.setEvaluateStatus(EvaluateStatusEnum.NO.value());
        orderDTO.setAddressId(orderAddressId);
        orderDTO.setCreateDate(now);
        orderDTO.setOrderPlatform(orderConfirmDTO.getOrderPlatform());
        orderDTO.setStoreId(orderConfirmDTO.getStoreId());
        orderDTO.setStoreName(orderConfirmDTO.getStoreName());
        orderDTO.setIsSplit(OrderSplitEnum.YES.value());
        // 直接设置优惠券id，子订单校验
        orderDTO.setMemberCouponsId(orderConfirmDTO.getMemberCouponsId());
        // 初始化活动价格
        orderDTO.setShippingFee(BigDecimal.ZERO);
        orderDTO.setGoodsAmount(BigDecimal.ZERO);
        orderDTO.setOrderAmount(BigDecimal.ZERO);
        orderDTO.setCouponAmount(BigDecimal.ZERO);
        orderDTO.setReduceAmount(BigDecimal.ZERO);
        orderDTO.setPreferentialPrice(BigDecimal.ZERO);
        orderDTO.setVirtualFlag(orderConfirmDTO.getVirtualFlag());
        return orderDTO;
    }

    /**
     * 功能描述:
     * 〈重新计算订单金额信息〉
     *
     * @param orderGoodsConfirmDTO 订单商品
     * @param orderDTO             订单信息
     * @author : 刘远杰
     */
    private void reCountOrder(OrderGoodsConfirmDTO orderGoodsConfirmDTO, OrderDTO orderDTO) {
        // 订单商品总金额 = 原订单商品总金额 + 该商品总金额
        orderDTO.setGoodsAmount(orderDTO.getGoodsAmount().add(orderGoodsConfirmDTO.getSpecPrice().
                multiply(BigDecimal.valueOf(orderGoodsConfirmDTO.getGoodsNum()))));
        orderDTO.setOrderAmount(orderDTO.getOrderAmount().add(orderGoodsConfirmDTO.getGoodsPayPrice().
                multiply(BigDecimal.valueOf(orderGoodsConfirmDTO.getGoodsNum()))));
        orderDTO.setPreferentialPrice(orderDTO.getGoodsAmount().subtract(orderDTO.getOrderAmount()).add(orderDTO.getShippingFee()));
        //TODO LX lixiangx@leimingtech.com 保存订单发票信息
    }

    /**
     * 功能描述:
     * 〈得到初始子订单信息〉
     *
     * @param orderPayDTO          订单支付信息
     * @param orderConfirmDTO      订单确认表
     * @param orderAddressId       订单地址id
     * @param now                  下单时间
     * @param orderGoodsConfirmDTO 订单商品实体
     * @author : 刘远杰
     */
    private OrderDTO createInitOrderDTO(OrderPayDTO orderPayDTO, OrderConfirmDTO orderConfirmDTO, Long orderAddressId
            , Date now,
                                        OrderGoodsConfirmDTO orderGoodsConfirmDTO) {
        OrderDTO orderDTO;
        orderDTO = new OrderDTO();
        // 封装订单固定参数  id/单号/买家/店铺/地址/支付/状态/配送方式
        orderDTO.setId(IdWorker.getId());
        orderDTO.setOrderSn(IdGenerator.defaultSnowflakeId());

        orderDTO.setBuyerId(orderConfirmDTO.getBuyerId());
        orderDTO.setBuyerEmail(orderConfirmDTO.getBuyerEmail());
        orderDTO.setBuyerName(orderConfirmDTO.getBuyerName());
        orderDTO.setBuyerGraderId(orderConfirmDTO.getBuyerGraderId());
        orderDTO.setBuyerGraderName(orderConfirmDTO.getBuyerGraderName());

        orderDTO.setStoreId(orderGoodsConfirmDTO.getStoreId());
        orderDTO.setStoreName(orderGoodsConfirmDTO.getStoreName());

        orderDTO.setAddressId(orderAddressId);
        orderDTO.setVirtualFlag(orderConfirmDTO.getVirtualFlag());
        // 虚拟订单
        if (orderGoodsConfirmDTO.getVirtualFlag() == VirtualFlagEnum.YES.value()) {
            // 设置配送方式
            orderDTO.setDevlierType(orderGoodsConfirmDTO.getDevlierType());
        }


        orderDTO.setPayId(orderPayDTO.getId());
        orderDTO.setPaySn(orderPayDTO.getPaySn());
        orderDTO.setOrderStatus(OrderStatusEnum.WAITTING_PAYMENT.value());
        orderDTO.setAppStatus(OrderAppStatusEnum.APP_WAITTING_PAYMENT.value());
        orderDTO.setEvaluateStatus(EvaluateStatusEnum.NO.value());
        orderDTO.setCreateDate(now);
        orderDTO.setOrderPlatform(orderConfirmDTO.getOrderPlatform());
        //TODO LX lixiangx@leimingtech.com 配送方式

        // 设置订单金额信息为该商品总金额
        orderDTO.setGoodsAmount(orderGoodsConfirmDTO.getSpecPrice().multiply(BigDecimal.valueOf(orderGoodsConfirmDTO.getGoodsNum())));
        // 运费
        orderDTO.setShippingFee(BigDecimal.ZERO);
        if (StringUtils.isNotBlank(orderConfirmDTO.getShippingAmount())) {
            JSONObject jsonObject = JSONObject.parseObject(orderConfirmDTO.getShippingAmount());
            String s = jsonObject.get(orderGoodsConfirmDTO.getStoreId()).toString();
            orderDTO.setShippingFee(new BigDecimal(s));
        }
        // 初始化优惠金额
        orderDTO.setCouponAmount(BigDecimal.ZERO);
        orderDTO.setReduceAmount(BigDecimal.ZERO);
        orderDTO.setOrderAmount(orderGoodsConfirmDTO.getGoodsPayPrice().multiply(BigDecimal.valueOf(orderGoodsConfirmDTO.getGoodsNum())).add(orderDTO.getShippingFee()));
        orderDTO.setPreferentialPrice(orderDTO.getGoodsAmount().subtract(orderDTO.getOrderAmount()).add(orderDTO.getShippingFee()));
        //TODO LX lixiangx@leimingtech.com 保存订单发票信息

        // 设置拼团活动的相关信息
        if (orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value()) {
            // 设置拼团状态为进行中
            orderDTO.setGroupStatus(GroupsEnum.ACTIVITY_STATUS_NO.value());
            // 存在groupRecordId说明是参与拼团，不存在则是发起拼团
            if (null == orderGoodsConfirmDTO.getActivityRecordId()) {
                orderDTO.setGroupRecordId(IdGenerator.defaultSnowflakeId());
            } else {
                orderDTO.setGroupRecordId(orderGoodsConfirmDTO.getActivityRecordId());
            }
        }


        return orderDTO;
    }

    /**
     * 功能描述:
     * 〈创建订单商品保存信息，修改库存修改接口参数〉
     *
     * @param orderGoodsConfirmDTO             订单商品确认信息
     * @param orderDTO                         订单信息
     * @param params                           库存修改
     * @param updateActivitySurplusStorageList 秒杀库存修改
     * @author : 刘远杰
     */
    private OrderGoodsDTO createOrderGoodsDTO(OrderGoodsConfirmDTO orderGoodsConfirmDTO, OrderDTO orderDTO,
                                              Map<String, String> params,
                                              List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList) {
        OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();

        // 查询商品及商品分类数据
        GoodsDTO goodsDTO = goodsService.get(orderGoodsConfirmDTO.getGoodsId());
        GoodsClassDTO goodsClassDTO = goodsClassService.findById(goodsDTO.getGcId());

        // 根据规格ID查询出携带乐观锁的订单规格数据
        GoodsSpecVersionDTO goodsSpecDTO = goodsSpecService.findVersionById(orderGoodsConfirmDTO.getSpecId());

        //订单商品数据封装
        orderGoodsDTO.setFirstGcId(goodsDTO.getFirstGcId());
        orderGoodsDTO.setSecondStoreGoodsGcId(goodsDTO.getSecondStoreGoodsGcId());
        orderGoodsDTO.setSecondStoreGoodsGcName(goodsDTO.getSecondStoreGoodsGcName());
        orderGoodsDTO.setSecondGcId(goodsDTO.getSecondGcId());
        orderGoodsDTO.setBrandId(goodsDTO.getBrandId());
        orderGoodsDTO.setGcId(goodsClassDTO.getId());
        orderGoodsDTO.setGoodsId(orderGoodsConfirmDTO.getGoodsId());
        orderGoodsDTO.setSpuSerial(goodsDTO.getGoodsSerial());
        orderGoodsDTO.setSpuName(goodsDTO.getGoodsName());
        orderGoodsDTO.setSpecId(orderGoodsConfirmDTO.getSpecId());
        orderGoodsDTO.setGoodsImage(orderGoodsConfirmDTO.getGoodsImage());
        orderGoodsDTO.setSpecSerial(goodsSpecDTO.getSpecSerial());
        orderGoodsDTO.setGoodsName(goodsSpecDTO.getSpecName());
        orderGoodsDTO.setSpecInfo(goodsSpecDTO.getSpecAttrName());
        orderGoodsDTO.setSpecAttrValueName(goodsSpecDTO.getSpecAttrValueName());

        orderGoodsDTO.setGoodsNum(orderGoodsConfirmDTO.getGoodsNum());
        orderGoodsDTO.setAftersaleQuantity(orderGoodsConfirmDTO.getGoodsNum());

        orderGoodsDTO.setOrderId(orderDTO.getId());
        orderGoodsDTO.setOrderSn(orderDTO.getOrderSn());

        orderGoodsDTO.setBuyerId(orderGoodsConfirmDTO.getBuyerId());
        orderGoodsDTO.setStoreId(orderGoodsConfirmDTO.getStoreId());
        orderGoodsDTO.setStoreName(orderGoodsConfirmDTO.getStoreName());

        // 保存使用的满减秒杀活动
        orderGoodsDTO.setActivityId(orderGoodsConfirmDTO.getActivityId());
        orderGoodsDTO.setActivityType(orderGoodsConfirmDTO.getActivityType());

        // 初始化优惠信息
        orderGoodsDTO.setAvgPreferentialAmount(BigDecimal.ZERO);
        orderGoodsDTO.setDiscountActivityAmount(BigDecimal.ZERO);
        orderGoodsDTO.setCouponAmount(BigDecimal.ZERO);
        orderGoodsDTO.setReduceAmount(BigDecimal.ZERO);
        orderGoodsDTO.setReturnPreferential(OrderGoodsEnum.RETURN_PREFERENTIAL_NO.value());

        orderGoodsDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
        orderGoodsDTO.setSpecPayPrice(orderGoodsConfirmDTO.getGoodsPayPrice());
        orderGoodsDTO.setSpecCostPrice(orderGoodsConfirmDTO.getSpecCostPrice());

        // 封装修改库存参数
        if (orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value() || orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.SECKILL_ACTIVITY.value() || orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()) {
            // 秒杀商品修改活动商品库存
            UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO = new UpdateActivitySurplusStorageDTO();

            updateActivitySurplusStorageDTO.setActivityId(orderGoodsDTO.getActivityId());
            updateActivitySurplusStorageDTO.setActivityType(orderGoodsDTO.getActivityType());
            updateActivitySurplusStorageDTO.setSpecId(orderGoodsDTO.getSpecId());
            updateActivitySurplusStorageDTO.setGoodsId(orderGoodsDTO.getGoodsId());
            updateActivitySurplusStorageList.add(updateActivitySurplusStorageDTO);
        } else {
            // 非秒杀商品扣减普通商品库存
            if (params.containsKey(goodsSpecDTO.getId().toString())) {
                Integer goodsNum =
                        orderGoodsConfirmDTO.getGoodsNum() + Integer.parseInt(params.get(goodsSpecDTO.getId().toString()));
                params.put(goodsSpecDTO.getId().toString(), goodsNum.toString());
            } else {
                params.put(goodsSpecDTO.getId().toString(), orderGoodsConfirmDTO.getGoodsNum().toString());
            }
        }

        return orderGoodsDTO;
    }

    /**
     * （拼团）创建订单商品保存信息，修改库存修改接口参数
     *
     * @param orderGoodsConfirmDTO
     * @param orderDTO
     * @param params
     * @param updateActivitySurplusStorageList
     * @return
     */
    private OrderGoodsDTO createGroupOrderGoodsDTO(OrderGoodsConfirmDTO orderGoodsConfirmDTO, OrderDTO orderDTO,
                                                   Map<String, String> params,
                                                   List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList) {
        OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();

        // 查询商品及商品分类数据
        GoodsDTO goodsDTO = goodsService.get(orderGoodsConfirmDTO.getGoodsId());
        GoodsClassDTO goodsClassDTO = goodsClassService.findById(goodsDTO.getGcId());

        // 根据规格ID查询出携带乐观锁的订单规格数据
        GoodsSpecVersionDTO goodsSpecDTO = goodsSpecService.findVersionById(orderGoodsConfirmDTO.getSpecId());

        //订单商品数据封装
        orderGoodsDTO.setFirstGcId(goodsDTO.getFirstGcId());
        orderGoodsDTO.setSecondGcId(goodsDTO.getSecondGcId());
        orderGoodsDTO.setBrandId(goodsDTO.getBrandId());
        orderGoodsDTO.setGcId(goodsClassDTO.getId());
        orderGoodsDTO.setGoodsId(orderGoodsConfirmDTO.getGoodsId());
        orderGoodsDTO.setSpuSerial(goodsDTO.getGoodsSerial());
        orderGoodsDTO.setSpuName(goodsDTO.getGoodsName());
        orderGoodsDTO.setSpecId(orderGoodsConfirmDTO.getSpecId());
        orderGoodsDTO.setGoodsImage(orderGoodsConfirmDTO.getGoodsImage());
        orderGoodsDTO.setSpecSerial(goodsSpecDTO.getSpecSerial());
        orderGoodsDTO.setGoodsName(goodsSpecDTO.getSpecName());
        orderGoodsDTO.setSpecInfo(goodsSpecDTO.getSpecAttrName());

        orderGoodsDTO.setGoodsNum(orderGoodsConfirmDTO.getGoodsNum());
        orderGoodsDTO.setAftersaleQuantity(orderGoodsConfirmDTO.getGoodsNum());

        orderGoodsDTO.setOrderId(orderDTO.getId());
        orderGoodsDTO.setOrderSn(orderDTO.getOrderSn());

        orderGoodsDTO.setBuyerId(orderGoodsConfirmDTO.getBuyerId());
        orderGoodsDTO.setStoreId(orderGoodsConfirmDTO.getStoreId());
        orderGoodsDTO.setStoreName(orderGoodsConfirmDTO.getStoreName());

        // 保存使用的满减活动
        orderGoodsDTO.setActivityId(orderGoodsConfirmDTO.getActivityId());
        orderGoodsDTO.setActivityType(orderGoodsConfirmDTO.getActivityType());

        // 初始化优惠信息
        orderGoodsDTO.setAvgPreferentialAmount(BigDecimal.ZERO);
        orderGoodsDTO.setDiscountActivityAmount(BigDecimal.ZERO);
        orderGoodsDTO.setCouponAmount(BigDecimal.ZERO);
        orderGoodsDTO.setReduceAmount(BigDecimal.ZERO);
        orderGoodsDTO.setReturnPreferential(OrderGoodsEnum.RETURN_PREFERENTIAL_NO.value());

        orderGoodsDTO.setSpecPrice(goodsSpecDTO.getSpecSellPrice());
        orderGoodsDTO.setSpecPayPrice(orderGoodsConfirmDTO.getGoodsPayPrice());
        orderGoodsDTO.setSpecCostPrice(orderGoodsConfirmDTO.getSpecCostPrice());

        // 封装修改拼团库存参数
        if (orderGoodsConfirmDTO.getActivityType() == ActivityTypeEnum.GROUP_ACTIVITY.value()) {
            UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO = new UpdateActivitySurplusStorageDTO();

            updateActivitySurplusStorageDTO.setActivityId(orderGoodsDTO.getActivityId());
            updateActivitySurplusStorageDTO.setActivityType(orderGoodsDTO.getActivityType());
            updateActivitySurplusStorageDTO.setSpecId(orderGoodsDTO.getSpecId());
            updateActivitySurplusStorageDTO.setGoodsId(orderGoodsDTO.getGoodsId());
            updateActivitySurplusStorageList.add(updateActivitySurplusStorageDTO);
        }

        if (params.containsKey(goodsSpecDTO.getId().toString())) {
            Integer goodsNum =
                    orderGoodsConfirmDTO.getGoodsNum() + Integer.parseInt(params.get(goodsSpecDTO.getId().toString()));
            params.put(goodsSpecDTO.getId().toString(), goodsNum.toString());
        } else {
            params.put(goodsSpecDTO.getId().toString(), orderGoodsConfirmDTO.getGoodsNum().toString());
        }

        return orderGoodsDTO;
    }

    /**
     * 拆分订单留言
     *
     * @param orderConfirmDTO: 订单确认实体
     * @return 订单留言集合 [{store:"留言内容"}]
     * @date 2019/6/25 11:07
     * @author lixiang
     **/
    private List<Map<Long, String>> splitOrderMessage(OrderConfirmDTO orderConfirmDTO) {
        // 拆分订单留言信息
        String orderMessage = orderConfirmDTO.getOrderMessage();
        List<Map<Long, String>> messageList = new ArrayList<>();
        if (StringUtils.isNotBlank(orderMessage)) {
            JSONArray message = JSONArray.parseArray(orderMessage);
            for (int i = 0; i < message.size(); i++) {
                Map<Long, String> msg = (Map<Long, String>) message.get(i);
                messageList.add(msg);
            }
        }
        return messageList;
    }

    /**
     * 功能描述:
     * 〈设置子订单留言〉
     *
     * @param messageList 留言集合
     * @param orderDTO    订单实体
     * @author : 刘远杰
     */
    private void setOrderMessage(List<Map<Long, String>> messageList, OrderDTO orderDTO) {
        if (CollectionUtils.isNotEmpty(messageList)) {
            for (Map temp : messageList) {
                if (temp.containsKey(orderDTO.getStoreId().toString())) {
                    orderDTO.setOrderMessage((String) temp.get(orderDTO.getStoreId().toString()));
                }
            }
        }
    }

    /**
     * 功能描述:
     * 〈保存订单日志〉
     *
     * @param order
     * @author : 刘远杰
     */
    private void saveOrderLog(OrderDTO order) {
        OrderLogDTO orderLog = this.getOrderLog(order.getId(), order.getBuyerName(),
                OperatorTypeEnum.BUYER.value(), OrderStatusEnum.WAITTING_PAYMENT.value(),
                OrderStatusEnum.WAITTING_SHIPPED.value(), "用户提交订单", order.getCreateDate());
        //发送MQ消息保存订单日志
        String message = JSONObject.toJSONString(orderLog);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE, message);
    }

    /**
     * 更新订单评价状态
     *
     * @param orderId
     */

    @Override
    public void updateEvaluateStatus(@RequestParam Long orderId) {
        baseDao.updateEvaluateStatus(orderId);


    }

    @Override

    public Long findLastNoComplete(Long id) {
        return baseDao.findLastNoComplete(id);
    }

    /**
     * @param orderAmount:       实际支付金额
     * @param orderGoodsDTOList: 订单对应的商品列表
     * @param memberId:          用户id
     * @param memberName:        用户名称
     * @return void
     * @Description 用户购物完成增加对应的积分和成长值
     * @Author huangkeyuan
     * @Date 14:52 2019-12-26
     */
    private Map<String, Object> savePoint(BigDecimal orderAmount, List<OrderGoodsDTO> orderGoodsDTOList,
                                          Long memberId, String memberName) {

        Map<String, String> logMap = new HashMap<>();
        logMap.put("orderAmount", orderAmount.toString());
        logMap.put("memberId", memberId.toString());
        logMap.put("orderGoodsDTOList", orderGoodsDTOList.toString());
        logMap.put("memberName", memberName);
        mlogger.info(OrderStatusCode.AFTER_ORDER_CONFIRM_RECEIPT_H5_COMPUTE_POINT,
                OrderStatusCode.AFTER_ORDER_CONFIRM_RECEIPT_H5_COMPUTE_POINT_MESSAGE, logMap);

        Map<String, Object> result = new HashMap<>();

        String pointSetting = pointSettingService.findFromRedis("morerule");
        MoreRuleSettingDTO moreRuleSettingDTO = JSONObject.parseObject(pointSetting, MoreRuleSettingDTO.class);

        GrowthRuleSettingDTO growthRuleSettingDTO = moreRuleSettingDTO.getGrowthRule();

        PointRuleSettingDTO pointRuleSettingDTO = moreRuleSettingDTO.getPointRule();

        //integer装换为bigDecimal
        BigDecimal growthMinPay = new BigDecimal(growthRuleSettingDTO.getMinOrderPay().toString());

        BigDecimal pointMinPay = new BigDecimal(pointRuleSettingDTO.getMinOrderPay().toString());

        // 积分购物消费值
        BigDecimal shoppingConsumption = new BigDecimal(pointRuleSettingDTO.getShoppingConsumption().toString());

        // 成长值购物消费值
        BigDecimal gShoppingConsumption = new BigDecimal(growthRuleSettingDTO.getShoppingConsumption().toString());

        // 单件商品最高获得积分值
        BigDecimal maxPiecesGoods = new BigDecimal(pointRuleSettingDTO.getMaxPiecesGoods().toString());

        // 单件商品最高获得成长值
        BigDecimal gMaxPiecesGoods = new BigDecimal(growthRuleSettingDTO.getMaxPiecesGoods().toString());

        BigDecimal finalPoint = BigDecimal.ZERO;

        BigDecimal finalGrowth = BigDecimal.ZERO;

        if (growthMinPay.compareTo(orderAmount) < 1 && pointMinPay.compareTo(orderAmount) < 1) {

            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {

                BigDecimal specPayPrice = orderGoodsDTO.getSpecPayPrice();


                BigDecimal resultNumber = specPayPrice.divide(shoppingConsumption);


                BigDecimal growthNumber = specPayPrice.divide(gShoppingConsumption);

                // 计算积分值
                if (resultNumber.compareTo(maxPiecesGoods) > 0) {

                    resultNumber = maxPiecesGoods;
                }

                finalPoint = resultNumber.add(finalPoint);

                // 计算成长值
                if (growthNumber.compareTo(gMaxPiecesGoods) > 0) {
                    growthNumber = gMaxPiecesGoods;
                }

                finalGrowth = growthNumber.add(finalGrowth);

            }


            // 针对计算后的积分进行四舍五入
            finalPoint = (finalPoint).setScale(0, BigDecimal.ROUND_HALF_UP);

            finalGrowth = (finalGrowth).setScale(0, BigDecimal.ROUND_HALF_UP);

            int saveFlag = finalPoint.compareTo(BigDecimal.ZERO);


            PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                    memberId,
                    memberName,
                    finalPoint.intValue(),
                    finalGrowth.intValue(),
                    MemberPointLogEnum.MORE_RULES_SHOPPING.value(),
                    MemberPointLogEnum.MORE_RULES_SHOPPING.code(),
                    MemberPointLogEnum.INSERT_ALL.code());

            logMap.clear();
            logMap.put("pointLogPackDTO", pointLogPackDTO.toString());
            mlogger.info(OrderStatusCode.ORDER_FINISH_H5_COMPUTE_POINT,
                    OrderStatusCode.ORDER_FINISH_H5_COMPUTE_POINT_MESSAGE, logMap);

            pointLogService.packPointLog(pointLogPackDTO);


        } else if (growthMinPay.compareTo(orderAmount) < 1 && pointMinPay.compareTo(orderAmount) >= 1) {

            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {

                BigDecimal specPayPrice = orderGoodsDTO.getSpecPayPrice();

                BigDecimal growthNumber = specPayPrice.divide(gShoppingConsumption);

                // 计算成长值
                if (growthNumber.compareTo(gMaxPiecesGoods) > 0) {
                    growthNumber = gMaxPiecesGoods;
                }

                finalGrowth = growthNumber.add(finalGrowth);

            }

            finalGrowth = (finalGrowth).setScale(0, BigDecimal.ROUND_HALF_UP);


            PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                    memberId,
                    memberName,
                    0,
                    finalGrowth.intValue(),
                    MemberPointLogEnum.MORE_RULES_SHOPPING.value(),
                    MemberPointLogEnum.MORE_RULES_SHOPPING.code(),
                    MemberPointLogEnum.INSERT_GROWTH.code());

            pointLogService.packPointLog(pointLogPackDTO);

        } else if (growthMinPay.compareTo(orderAmount) >= 1 && pointMinPay.compareTo(orderAmount) < 1) {

            for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {

                BigDecimal specPayPrice = orderGoodsDTO.getSpecPayPrice();


                BigDecimal resultNumber = specPayPrice.divide(shoppingConsumption);


                // 计算积分值
                if (resultNumber.compareTo(maxPiecesGoods) > 0) {

                    resultNumber = maxPiecesGoods;
                }

                finalPoint = resultNumber.add(finalPoint);


            }

            // 针对计算后的积分进行四舍五入
            finalPoint = (finalPoint).setScale(0, BigDecimal.ROUND_HALF_UP);

            int saveFlag = finalPoint.compareTo(BigDecimal.ZERO);

            // 如果saveFlag==1说明积分大于0
            if (saveFlag == 1) {

                PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                        memberId,
                        memberName,
                        finalPoint.intValue(),
                        0,
                        MemberPointLogEnum.MORE_RULES_SHOPPING.value(),
                        MemberPointLogEnum.MORE_RULES_SHOPPING.code(),
                        MemberPointLogEnum.INSERT_POINT.code());

                pointLogService.packPointLog(pointLogPackDTO);
            }

        }

        result.put("finalPont", finalPoint.intValue());
        result.put("finalGrowth", finalGrowth.intValue());
        return result;

    }

    /**
     * 查询周期内的订单
     *
     * @param storeId   店铺ID
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */

    @Override
    public List<OrderDTO> findBillOrderList(@RequestParam("storeId") Long storeId,
                                            @RequestParam(value = "startDate", required = false) String startDate,
                                            @RequestParam("endDate") String endDate) {

        return baseDao.findBillOrderList(storeId, startDate, endDate);
    }

    /**
     * 订单实况数据
     *
     * @param storeId
     * @param type    是否刷新数据 1 刷新
     * @return
     */

    @Override
    public OrderLiveDTO orderLive(@RequestParam("storeId") Long storeId,
                                  @RequestParam(value = "type", required = false) Integer type) {
        OrderLiveDTO orderLiveDTO = null;
        Long expire = 60L * 30;
        String goodsLiveKey = RedisKeys.getOrderLiveKey(storeId);

        Object object = redisUtils.get(goodsLiveKey);

        if (object == null || type == 1) {
            orderLiveDTO = baseDao.orderLive(storeId);
            Integer afterAuditCount = aftersaleApplyService.findAfterAuditCount(storeId);
            orderLiveDTO.setToAuditCount(afterAuditCount == null ? 0 : afterAuditCount);
            orderLiveDTO.setCreateDate(new Date());
            redisUtils.set(goodsLiveKey, orderLiveDTO, expire);
        } else {
            orderLiveDTO = (OrderLiveDTO) object;
        }

        return orderLiveDTO;
    }

    /**
     * 首页>基础概况>订单数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexOrderDataDTO
     * @date 2020/4/7/007 11:59
     * @author xuzhch
     */

    @Override
    public IndexOrderDataDTO indexOrderData(@RequestParam("startDateStr") String startDateStr, @RequestParam(
            "endDateStr") String endDateStr) {
        Date startDate = DateUtils.parse(startDateStr, DateUtils.DATE_TIME_PATTERN);
        Date endDate = DateUtils.parse(endDateStr, DateUtils.DATE_TIME_PATTERN);
        return baseDao.selectIndexOrderData(startDate, endDate);
    }

    /**
     * 商户销售排名
     *
     * @param params
     * @return
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/

    @Override
    public Map<String, Object> storeSellRanking(@RequestParam Map params) {
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
            redisUtils.deleteByPattern(IndexRedisConstants.INDEX_STORE_SELL + userId + ":*");
        } else {
            Object o = redisUtils.get(IndexRedisConstants.INDEX_STORE_SELL + userId + ":" + orderBy + ":" + dateType);
            if (o != null) {
                return (Map) o;
            }
        }

        Date startDate = DateUtils.getFixedDate(endDate, dateType);
        List<IndexStoreSellRakingDTO> indexStoreSellRakingDTOS = baseDao.selectStoreSellRanking(startDate, endDate,
                orderBy);
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", endDate);
        result.put("data", indexStoreSellRakingDTOS);
        redisUtils.set(IndexRedisConstants.INDEX_STORE_SELL + userId + ":" + orderBy + ":" + dateType, result,
                IndexRedisConstants.JEDIS_EXPIRE);
        return result;
    }

    /**
     * 拼团订单列表
     *
     * @return
     * @date 2020-03-17 10:16
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public List<GroupOrderDetailDTO> findGroupOrderList(@RequestParam Map<String, Object> params) {

        List<GroupOrderDetailDTO> list = baseDao.findGroupOrderList(params);
        return list;
    }

    /**
     * 批量修改拼团关联的订单状态
     *
     * @param orderIds 拼团订单id集合
     * @return
     * @date 2020-03-18 11:12
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void updateGroupStatus(@RequestParam("orderIds") List<Long> orderIds,
                                  @RequestParam("orderGroupStatus") Integer orderGroupStatus) {
        List<OrderEntity> orderEntityList = new ArrayList<>();
        orderIds.forEach(orderId -> {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(orderId);
            orderEntity.setGroupStatus(orderGroupStatus);
            orderEntityList.add(orderEntity);
        });
        log.info("更新拼团的订单，orderEntityList：{}", orderEntityList);
        updateBatchById(orderEntityList);

    }

    /**
     * 功能描述：
     * <计算用户已购买秒杀商品数量>
     *
     * @param activityId
     * @param specId
     * @return
     * @date 2020/3/23 14:53
     * @author 刘远杰
     **/
    private Integer countSeckillOrderGoodsNum(Long activityId, Long specId, Long buyerId) {
        // 计算用户已购买秒杀商品数量
        return baseDao.countSeckillOrderGoodsNum(activityId, specId, buyerId);
    }

    /**
     * 功能描述：
     * <统计用户限时抢购商品购买数量>
     *
     * @param activityId 活动id
     * @param specId     规格id
     * @param buyerId    会员id
     * @return 购买数量
     * @date 2020/3/23 14:55
     * @author 刘远杰
     **/
    private Integer countFlashSaleOrderGoodsNum(Long activityId, Long specId, Long buyerId) {
        // 计算用户已购买秒杀商品数量
        return baseDao.countFlashSaleOrderGoodsNum(activityId, specId, buyerId);
    }

    /**
     * 用户订单完成（已购买）商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */
    @Override

    public List<Long> getOrderGoodIds(@RequestParam("memberId") Long memberId) {
        List<Long> goodIds = baseDao.getOrderGoodIds(memberId);
        return goodIds;
    }

    /**
     * 功能描述：
     * <封装提交订单校验错误数据>
     *
     * @param insertNowOrderVoDTO  立即购买提交订单参数
     * @param orderSaveResultDTO   立即购买返回结果
     * @param errorOrderMsgDTOList 校验不通过商品集合
     * @param checkStatus          校验错误码
     * @param errorMsgTitle        校验错误标题
     * @return
     * @date 2020/4/14 9:47
     * @author 刘远杰
     **/
    private void createBuynowGoodsErrorMsg(InsertNowOrderVoDTO insertNowOrderVoDTO,
                                           OrderSaveResultDTO orderSaveResultDTO,
                                           List<ErrorOrderMsgDTO> errorOrderMsgDTOList,
                                           int checkStatus, String errorMsgTitle) {
        // 封装错误提示信息
        ErrorGoodsMsgDTO errorGoodsMsgDTO = new ErrorGoodsMsgDTO();
        errorGoodsMsgDTO.setSpuId(insertNowOrderVoDTO.getGoodsId());
        errorGoodsMsgDTO.setSkuId(insertNowOrderVoDTO.getSpecId());
        errorGoodsMsgDTO.setGoodsImage(insertNowOrderVoDTO.getSpecMainPicture());
        errorGoodsMsgDTO.setSpecName(insertNowOrderVoDTO.getSpecName());
        errorGoodsMsgDTO.setBrandId(insertNowOrderVoDTO.getBrandId());
        errorGoodsMsgDTO.setBrandName(insertNowOrderVoDTO.getBrandName());
        errorGoodsMsgDTO.setStoreId(insertNowOrderVoDTO.getStoreId());
        errorGoodsMsgDTO.setStoreName(insertNowOrderVoDTO.getStoreName());
        errorGoodsMsgDTO.setGoodsNum(insertNowOrderVoDTO.getGoodsNum());
        errorGoodsMsgDTO.setPrice(insertNowOrderVoDTO.getSpecSellPrice());
        ErrorOrderMsgDTO errorOrderMsgDTO = new ErrorOrderMsgDTO();
        errorOrderMsgDTO.setErrorMsgTitle(errorMsgTitle);
        errorOrderMsgDTO.setErrorGoodsMsgDTOList(Collections.singletonList(errorGoodsMsgDTO));
        errorOrderMsgDTOList.add(errorOrderMsgDTO);

        orderSaveResultDTO.setCheckStatus(checkStatus);
        orderSaveResultDTO.setErrorGoodsList(errorOrderMsgDTOList);
    }

    /**
     * 订单导出
     *
     * @param params
     * @return
     * @author xuzhch
     */

    @Override
    public PageData<EasyOrderExcelDTO> orderExport(@RequestParam Map<String, Object> params) {
        //分页参数
        Long curPage = null;
        Long limit = null;
        if (params.get(Constant.PAGE) != null && !"".equals(params.get(Constant.PAGE))) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null && !"".equals(params.get(Constant.LIMIT))) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }
        Page<EasyOrderExcelDTO> page = new Page<>(curPage, limit);
        List<EasyOrderExcelDTO> list = baseDao.orderExport(params);

        return new PageData(list, page.getTotal());
    }


    @Override
    public OrderDTO get(Long orderId) {
        return ConvertUtils.sourceToTarget(baseDao.selectById(orderId), OrderDTO.class);
    }


    @Override
    public void updateAfterFlag(@RequestBody List<Long> orderIds, @RequestParam("afterFlag") Integer afterFlag) {
        baseDao.update(null,
                Wrappers.<OrderEntity>lambdaUpdate().set(OrderEntity::getAfterFlag, afterFlag).in(OrderEntity::getId,
                        orderIds));
    }

    /**
     * @param goodsConverOrderList: 用户选中购物车集合
     * @Author: LX 17839193044@162.com
     * @Description: 对购物车商品进行规格、库存、价格校验。后期会增加活动等校验
     * @Date: 2019/6/20 14:25
     * @Version: V1.0
     */
    private Map<String, List<CartGoodsDTO>> cartValidation(List<GoodsConverOrderDTO> goodsConverOrderList) {
        Map<String, List<CartGoodsDTO>> map = new HashMap<>();
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
                ValidateGoodsDTO validateGoodsDTO = goodsService.findValidationGoods(cartGoodsDTO.getGoodsId(),
                        cartGoodsDTO.getSpecId());
                if (validateGoodsDTO != null
                        && validateGoodsDTO.getGoodsStatus().equals(GoodsStatusEnum.GOODS_AUDIT_PASS.value())
                        && validateGoodsDTO.getGoodsShow().equals(GoodsStatusEnum.GOODS_SHOW_ON.value())
                        && validateGoodsDTO.getSpecShow().equals(GoodsSpecStatusEnum.GOODS_SPEC_SHOW_UP.value())) {

                    // 设置运费模板id
                    if (GoodsEnum.EXPRESS_FLAG_YES.value() == validateGoodsDTO.getExpressFlag() && validateGoodsDTO.getFreightTemplateId() != null) {
                        cartGoodsDTO.setFreightTemplateId(validateGoodsDTO.getFreightTemplateId());
                    }

                    if (cartGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()
                            && cartGoodsDTO.getActivityType() != ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() &&
                            cartGoodsDTO.getGoodsNum() > validateGoodsDTO.getSpecStorage()) {
                        // 规格库存不足,更新购物车购买数量
//                        cartGoodsDTO.setGoodsNum(validateGoodsDTO.getSpecStorage());
                        stockList.add(cartGoodsDTO);
                    }

                    if (cartGoodsDTO.getActivityType() != ActivityTypeEnum.SECKILL_ACTIVITY.value()
                            && cartGoodsDTO.getActivityType() != ActivityTypeEnum.FLASH_SALE_ACTIVITY.value()
                            && cartGoodsDTO.getSpecSellPrice().compareTo(validateGoodsDTO.getSpecSellPrice()) != 0) {
                        // 商品价格变动，更新购物车销售价格
                        cartGoodsDTO.setSpecSellPrice(validateGoodsDTO.getSpecSellPrice());
                        priceList.add(cartGoodsDTO);
                    }
                } else {
                    // 规格不存在
                    if (ActivityTypeEnum.SECKILL_ACTIVITY.value() != cartGoodsDTO.getActivityType() && cartGoodsDTO.getActivityType() != ActivityTypeEnum.FLASH_SALE_ACTIVITY.value() && cartGoodsDTO.getSpecSellPrice().compareTo(validateGoodsDTO.getSpecSellPrice()) != 0) {
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
        map.put("reduceNotExist", reduceNotExistList);
        map.put("reducePre", reducePreList);
        map.put("reduceStop", reduceStopList);
        return map;
    }

    /**
     * 功能描述：
     * <运费计算>
     *
     * @param memberAddressDTO
     * @param goodsConverOrderList
     * @param validationMap
     * @return
     * @date 2020/5/7 18:15
     * @author 刘远杰
     **/
    private Map<Long, BigDecimal> countFreightAmount(MemberAddressDTO memberAddressDTO,
                                                     List<GoodsConverOrderDTO> goodsConverOrderList, Map<String,
            List<CartGoodsDTO>> validationMap) {
        Map<Long, BigDecimal> shippingAmount = new HashMap<>();

        if (validationMap == null) {
            validationMap = new HashMap<>();
        }

        // 运费模板不存在
        List<CartGoodsDTO> freighttemplatenofound = new ArrayList<>();
        // 超出配送区域
        List<CartGoodsDTO> freighttemplateoverarea = new ArrayList<>();
        // 运费模板类型错误
        List<CartGoodsDTO> freighttemplatetypeerror = new ArrayList<>();
        // 运费规则类型错误
        List<CartGoodsDTO> freighttemplateruleerror = new ArrayList<>();

        for (GoodsConverOrderDTO goodsConverOrderDTO : goodsConverOrderList) {
            List<BigDecimal> freightAmountList = new ArrayList<>();
            Long storeId = goodsConverOrderDTO.getStoreId();
            List<CartGoodsDTO> cartGoodsDTOList = goodsConverOrderDTO.getCartGoodsDTOList();

            // 根据运费模板id分组
            Map<Long, List<CartGoodsDTO>> freightMap =
                    cartGoodsDTOList.stream().filter(o -> o.getFreightTemplateId() != null).collect(Collectors.groupingBy(CartGoodsDTO::getFreightTemplateId));

            // 运费计算规则
            FreightRuleDTO storeFreightRule = freightRuleService.getStoreFreightRule(storeId);
            if (storeFreightRule == null) {
                // 下单区域该模板不可配送
                freighttemplateruleerror.addAll(cartGoodsDTOList);
            } else {
                Integer ruleType = storeFreightRule.getRuleType();
                for (Long freightTemplateId : freightMap.keySet()) {
                    // 查询运费模板ActivityGoodsServiceImpl
                    AdminFreightTemplateDetailDTO detailDTO =
                            freightTemplateService.adminDetail(freightTemplateId, storeId);
                    if (detailDTO == null) {
                        // 运费模板不存在
                        freighttemplatenofound.addAll(freightMap.get(freightTemplateId));
                    }

                    // 累加商品数量
                    Integer number =
                            freightMap.get(freightTemplateId).stream().mapToInt(CartGoodsDTO::getGoodsNum).sum();

                    // 匹配下单的地区
                    List<FreightTemplateRuleDTO> ruleList = detailDTO.getFreightTemplateRuleList();
                    //运费规则中是否有所有地区的规则，在叠加运费的时候如果所有地区的运费和单项运费有冲突就取最少的运费叠加上
                    BigDecimal superposition = BigDecimal.ZERO;
                    Boolean moreRule = false;
                    // 按件计算
                    BigDecimal goodsTotalFreight = BigDecimal.ZERO;
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
                            if ((memberAddressDTO.getProvinceId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getProvinceId()))
                                    || (memberAddressDTO.getCityId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getCityId()))
                                    || (memberAddressDTO.getAreaId() != null && freightTemplateRuleDTO.getAreaIdsArr().contains(memberAddressDTO.getAreaId()))) {
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
                        freighttemplateoverarea.addAll(freightMap.get(freightTemplateId));
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

                shippingAmount.put(storeId, goodsTotalFreight);
            }

        }

        // 错误信息封装
        validationMap.put("freighttemplatenofound", freighttemplatenofound);
        validationMap.put("freighttemplateoverarea", freighttemplateoverarea);
        validationMap.put("freighttemplatetypeerror", freighttemplatetypeerror);
        validationMap.put("freighttemplateruleerror", freighttemplateruleerror);
        return shippingAmount;
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
            log.info("商品重量", totalWeight);
            // 计算重量运费
            if (totalWeight == null || totalWeight.compareTo(new BigDecimal(freightTemplateRuleDTO.getFirstFee())) == -1) {
                // 不超过首重作为当前商品运费
                goodsTotalFreight = freightTemplateRuleDTO.getFirstAmount();
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
     * 查询未评价的订单
     *
     * @param params
     */

    @Override
    public PageData<EvaluateOrderPageDTO> findNotEvaOrder(@RequestParam Map<String, Object> params) {
        IPage<OrderEntity> page = getPage(params, "lo.update_date", false);
        List<EvaluateOrderPageDTO> orderList = baseDao.findNotEvaOrder(params);
        return new PageData<>(orderList, page.getTotal());
    }

    /**
     * pc查询已评价订单
     *
     * @param params
     * @return
     */

    @Override
    public PageData<EvaluateOrderPageDTO> pcEvaPage(@RequestParam Map<String, Object> params) {
        IPage<OrderEntity> page = getPage(params, "log.evaluation_time", false);
        List<EvaluateOrderPageDTO> list = baseDao.pcEvaPage(params);
        return new PageData<>(list, page.getTotal());
    }


    @Override
    public PageData<PCOrderInvoicePageVO> invoicePage(@RequestParam Map<String, Object> params) {
        IPage<OrderEntity> page = getPage(params, Constant.CREATE_DATE, false);
        List<PCOrderPageVO> orderList = baseDao.findPcOrderInvoicePage(params);
        List<PCOrderInvoicePageVO> pcOrderInvoicePageVOS = ConvertUtils.sourceToTarget(orderList,
                PCOrderInvoicePageVO.class);
        for (PCOrderInvoicePageVO pcOrderInvoicePageVO : pcOrderInvoicePageVOS) {
            OrderInvoiceDTO orderInvoiceDTO = orderInvoiceService.getOrderInvoiceDTO(pcOrderInvoicePageVO.getId());
//            pcOrderInvoicePageVO.setCompanyType(orderInvoiceDTO.getCompanyType());
            pcOrderInvoicePageVO.setMemberInvoiceType(orderInvoiceDTO.getMemberInvoiceType());
            pcOrderInvoicePageVO.setStoreInvoiceType(orderInvoiceDTO.getStoreInvoiceType());
            pcOrderInvoicePageVO.setInvoiceEvolve(orderInvoiceDTO.getInvoiceEvolve());
        }
        return new PageData<PCOrderInvoicePageVO>(pcOrderInvoicePageVOS, page.getTotal());
    }
}
