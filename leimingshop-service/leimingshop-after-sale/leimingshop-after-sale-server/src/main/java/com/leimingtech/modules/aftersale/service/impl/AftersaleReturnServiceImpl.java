/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.config.AddressPrefixProperties;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.dto.sys.SysUserDTO;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.aftersale.dao.AftersaleReturnDao;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.aftersale.enmus.AfterSaleStateEnum;
import com.leimingtech.modules.aftersale.entity.AftersaleReturnEntity;
import com.leimingtech.modules.aftersale.service.*;
import com.leimingtech.modules.dto.log.point.PointLogPackDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.payment.H5WxRefundBaseDTO;
import com.leimingtech.modules.dto.payment.OrderPayDTO;
import com.leimingtech.modules.dto.payment.PaymentDTO;
import com.leimingtech.modules.enums.order.VirtualFlagEnum;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.modules.service.payment.PaymentService;
import com.leimingtech.modules.service.payment.RefundService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单退货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AftersaleReturnServiceImpl extends CrudServiceImpl<AftersaleReturnDao, AftersaleReturnEntity, AftersaleReturnDTO> implements AftersaleReturnService {


    private static final String MAP_PARAMS_CODE = "code";

    @Resource
    private AftersaleReturnDao aftersaleReturnDao;

    @Autowired
    private AftersaleApplyService aftersaleApplyService;

    @Autowired

    private AftersaleAuditLogService aftersaleAuditLogService;

    @Autowired

    private AftersaleGoodsService aftersaleGoodsService;

    @Autowired

    private AftersaleLogService aftersaleLogService;

    @Autowired

    private AftersaleRefundRecordsService aftersaleRefundRecordsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private PointLogService pointLogService;

    @Autowired
    private ArbitrationService arbitrationService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public QueryWrapper<AftersaleReturnEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<AftersaleReturnEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(null != params.get("id"), "aftersale_sn", params.get("id"));
        wrapper.eq(null != params.get("aftersaleSn"), "aftersale_sn", params.get("aftersaleSn"));
        return wrapper;
    }

    /**
     * 保存订单退货
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody AftersaleReturnSaveDTO dto) {
        AftersaleReturnEntity reasonDescriptionEntity = ConvertUtils.sourceToTarget(dto, AftersaleReturnEntity.class);
        aftersaleReturnDao.insert(reasonDescriptionEntity);
    }

    /**
     * 修改订单退货
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody AftersaleReturnDTO dto) {
        super.update(dto);
    }

    /**
     * 删除订单退货
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据ID查询订单退货
     *
     * @param id
     * @return
     */
    @Override

    public AftersaleReturnDTO get(Long id) {
        QueryWrapper<AftersaleReturnEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "aftersale_sn", id);
        AftersaleReturnEntity entity = baseDao.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(entity, AftersaleReturnDTO.class);

    }

    /**
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleReturnDTO>
     * @Description 查询所有的订单退货列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */
    @Override

    public List<AftersaleReturnDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleReturnDTO>
     * @Description 分页查询所有的订单退货列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 13:40 2019-06-11
     */
    @Override

    public PageData<AftersaleReturnPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId) {
        if (storeId != null) {
            params.put("storeId", storeId);
        }
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        // 分页
        IPage<AftersaleReturnEntity> page = getPage(params, "lar.create_date", false);
        // 查询
        List<AftersaleReturnPageDTO> list = aftersaleReturnDao.findPage(params, page);
        return new PageData<>(list, page.getTotal());
    }

    /***
     * @Description 退货详情
     * @Param  * @param aftersaleSn:
     * @Author huangkeyuan
     * @Date 11:16 2019-06-17
     * @return com.leimingtech.modules.aftersale.dto.AftersaleReturnDetailDTO
     */
    @Override

    public AftersaleReturnDetailDTO detail(Long aftersaleSn) {
        AftersaleReturnDetailDTO aftersaleReturnDetailDTO = new AftersaleReturnDetailDTO();
        // 获取基本的退货信息
        AftersaleReturnDTO aftersaleReturnDTO = this.get(aftersaleSn);
        aftersaleReturnDetailDTO.setAftersaleReturnDTO(aftersaleReturnDTO);

        //获取仲裁信息
        ArbitrationDTO arbitrationDTO = arbitrationService.getDataByAfterSn(aftersaleSn);
        if (!BeanUtil.isEmpty(arbitrationDTO)) {
            aftersaleReturnDetailDTO.setArbitrationDTO(arbitrationDTO);
        }
        // 获取退货原因
        AftersaleApplyDTO aftersaleApplyDTO = aftersaleApplyService.getApply(aftersaleSn);
        aftersaleReturnDetailDTO.setAftersaleApplyDTO(aftersaleApplyDTO);

        // 获取商家退货审核
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", aftersaleSn);
        List<AftersaleAuditLogDTO> auditList = aftersaleAuditLogService.list(map);
        aftersaleReturnDetailDTO.setAftersaleAuditLogDTOList(auditList);

        // 获取退货商品数据
        List<AftersaleGoodsDTO> goodsDTOList = aftersaleGoodsService.list(map);
        aftersaleReturnDetailDTO.setAftersaleGoodsDTOList(goodsDTOList);

        // 获取日志列表
        List<AftersaleLogListDTO> logDTOList = aftersaleLogService.listLog(aftersaleSn);
//        List<AftersaleLogListDTO> logAuditDTOList = aftersaleAuditLogService.listLog(aftersaleSn);
//        logDTOList.addAll(logAuditDTOList);
        Collections.sort(logDTOList, new TradeNoComparatorUp());

        aftersaleReturnDetailDTO.setAftersaleLogListDTOList(logDTOList);

        return aftersaleReturnDetailDTO;

    }

    /**
     * 退货商家确认收货接口
     *
     * @param
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmGoods(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("aftersaleStatus") Integer aftersaleStatus) {
        Map<String, Object> resultMap = new HashMap<>(2);
        // 首先查询状态是否是商家确认收货
        QueryWrapper<AftersaleReturnEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(aftersaleSn != null, "aftersale_sn", aftersaleSn);
        AftersaleReturnEntity aftersaleReturn = aftersaleReturnDao.selectOne(wrapper);
        if (null != aftersaleReturn && aftersaleReturn.getAftersaleStatus().equals(AfterSaleStateEnum.AFTER_STATUS_RECEIVE_RETURNIN.value()) && aftersaleReturn.getLogisticsStatus().equals(AfterSaleStateEnum.LOGISTICS_STATUS_SELLERIN.value())) {
            // 如果查询出来退货记录不为空，而且售后状态为确认收货 物流状态为卖家已收货
            // 则说明已提交过 直接返回
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "已提交请勿重复提交");
            return resultMap;

        }
        //确认收货
        aftersaleReturnDao.confirmGoods(aftersaleSn, aftersaleStatus, AfterSaleStateEnum.LOGISTICS_STATUS_SELLERIN.value());
        OrderDTO orderDTO = orderService.get(aftersaleReturn.getOrderId());
        if (orderDTO.getVirtualFlag().equals(VirtualFlagEnum.NO.value())) {
            //发送mq消息异步保存售后日志记录
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setMessage("退货-商家确认收货");
            if (StringUtils.isNotBlank(SecurityUser.getUserName())) {
                aftersaleLogSaveDTO.setCreator("店铺小二(" + SecurityUser.getUserName() + ")");
                aftersaleLogSaveDTO.setUpdater("店铺小二(" + SecurityUser.getUserName() + ")");
            } else {
                aftersaleLogSaveDTO.setCreator("/");
                aftersaleLogSaveDTO.setUpdater("/");
            }

            aftersaleLogSaveDTO.setAftersaleSn(aftersaleSn);
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_RECEIVE_RETURNIN.value());
            aftersaleLogSaveDTO.setType(AfterSaleStateEnum.TYPE_RETURN.value());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_IN.value());
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
        }


        // 保存退款记录表，新增待退款信息
        AftersaleRefundRecordsDTO aftersaleRefundRecordsDTO = new AftersaleRefundRecordsDTO();
        Long refundId = IdGenerator.defaultSnowflakeId();
        aftersaleRefundRecordsDTO.setId(refundId);
        aftersaleRefundRecordsDTO.setAftersaleSn(aftersaleSn);
        aftersaleRefundRecordsDTO.setRefundStatus(AfterSaleStateEnum.REFUND_STATE_PAY_PREPARE.value());
        boolean result = aftersaleRefundRecordsService.saveRecords(aftersaleRefundRecordsDTO);
        if (result) {
            resultMap.put("code", ResultCodeEnum.SUCCESS.value());
            resultMap.put("msg", "保存退款记录成功");
            resultMap.put("refundId", refundId);
            // 商家针对退货商品点击确认收货后，扣减用户该订单的积分和成长值
            this.updateMemberPoint(aftersaleSn, aftersaleReturn);
            this.wxRefund(refundId);
        } else {
            resultMap.put("code", ResultCodeEnum.ERROR.value());
            resultMap.put("msg", "保存退款记录失败");
        }

        return resultMap;
    }

    /**
     * 售后退货--确认入库修改用户积分
     *
     * @param aftersaleSn     售后单号
     * @param aftersaleReturn 退货信息
     */
    private void updateMemberPoint(@RequestParam("aftersaleSn") Long aftersaleSn, AftersaleReturnEntity aftersaleReturn) {
        AftersaleReturnDetailDTO aftersaleReturnDetailDTO = detail(aftersaleSn);

        // 查询本订单对应的积分和成长值
        Map<String, Object> queryMap = new HashMap<>(1);
        queryMap.put("id", aftersaleReturnDetailDTO.getAftersaleReturnDTO().getOrderId());
        OrderDTO order = orderService.getOrderByMap(queryMap);

        // 退款金额和订单支付总金额
        BigDecimal refundAmount = aftersaleReturn.getRefundAmount();
        BigDecimal orderAmount = order.getOrderAmount();
        BigDecimal orderPointsCount = new BigDecimal(order.getOrderPointsCount().toString());
        BigDecimal orderGrowthCount = new BigDecimal(order.getOrderGrowthCount().toString());
        BigDecimal finalPoint = refundAmount.multiply(orderPointsCount).divide(orderAmount, 0, BigDecimal.ROUND_HALF_UP);
        BigDecimal finalGrowth = refundAmount.multiply(orderGrowthCount).divide(orderAmount, 0, BigDecimal.ROUND_HALF_UP);

        // 保存积分扣减日志记录
        PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                order.getBuyerId(),
                order.getBuyerName(),
                -finalPoint.intValue(),
                -finalGrowth.intValue(),
                MemberPointLogEnum.REFUND_ORDER.value(),
                MemberPointLogEnum.REFUND_ORDER.code(),
                MemberPointLogEnum.INSERT_ALL.code());

        pointLogService.packPointLog(pointLogPackDTO);
    }

    /**
     * @param id:退款单号
     * @Author: SWH ab4856812@163.com
     * @Description:微信退款
     * @Date: 2019/6/24 17:42
     * @Version: V1.0
     */

    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public Map<String, Object> wxRefund(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>(2);
        AftersaleRefundRecordsDTO aftersaleRefundRecordsDTO = aftersaleRefundRecordsService.get(id);
        // 封装数据dto
        if (null == aftersaleRefundRecordsDTO) {
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "查询退款记录异常");
            return resultMap;
        }
        Long aftersaleSn = aftersaleRefundRecordsDTO.getAftersaleSn();
        // 将退款记录状态改为退款中
        aftersaleRefundRecordsDTO.setId(aftersaleSn);
        aftersaleRefundRecordsDTO.setRefundStatus(AfterSaleStateEnum.REFUND_STATE_PAYING.value());
        aftersaleRefundRecordsService.update(aftersaleRefundRecordsDTO);

        AftersaleReturnDTO aftersaleReturnDTO = this.get(aftersaleSn);
        if (null == aftersaleReturnDTO) {
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "查询退货记录异常");
            return resultMap;
        }
        H5WxRefundBaseDTO h5WxRefundBaseDTO = new H5WxRefundBaseDTO();
        h5WxRefundBaseDTO.setOutrefundno(String.valueOf(aftersaleSn));

        h5WxRefundBaseDTO.setRefundfee((aftersaleReturnDTO.getRefundAmount().multiply(new BigDecimal(100))).intValue());
        // 查询订单
        Map<String, Object> queryMap = new HashMap<>(1);
        queryMap.put("id", aftersaleReturnDTO.getOrderId());
        OrderDTO order = orderService.getOrderByMap(queryMap);
        if (null == order) {
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "查询订单记录异常");
            return resultMap;
        }
        // 子订单也会保存父订单的支付单号所以直接赋值就行
        h5WxRefundBaseDTO.setOuttradeno(String.valueOf(order.getPaySn()));
        h5WxRefundBaseDTO.setOrdersn(order.getOrderSn().toString());
        h5WxRefundBaseDTO.setOrderId(aftersaleReturnDTO.getOrderId());
        OrderPayDTO payDTO = orderPayService.getByPaySn(order.getPaySn());
        h5WxRefundBaseDTO.setTotalfee((payDTO.getPayAmount().multiply(new BigDecimal(100))).intValue());

        // 给id赋值
        h5WxRefundBaseDTO.setId(id);
        h5WxRefundBaseDTO.setBuyerId(aftersaleReturnDTO.getMemberId());
        h5WxRefundBaseDTO.setBuyerName(aftersaleReturnDTO.getMemberName());
        h5WxRefundBaseDTO.setStoreId(aftersaleReturnDTO.getStoreId());
        h5WxRefundBaseDTO.setStoreName(aftersaleReturnDTO.getStoreName());
        h5WxRefundBaseDTO.setPayType(aftersaleReturnDTO.getPayType().intValue());
        h5WxRefundBaseDTO.setBalanceRefundAmount(aftersaleReturnDTO.getBalanceRefundAmount());
        String refundMessage = JSONObject.toJSONString(h5WxRefundBaseDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_WEIXIN_REFUND_QUEUE, refundMessage);
        AftersaleLogDTO aftersaleLogDTO = aftersaleLogService.findlogByStatus(aftersaleReturnDTO.getAftersaleSn(), AfterSaleStateEnum.AFTER_STATUS_PAYING.value());
        if (null == aftersaleLogDTO) {
            // 发送异步消息保存售后日志
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setAftersaleSn(aftersaleReturnDTO.getAftersaleSn());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setMessage("您的服务单的商品已收到，退款中");
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_PAYING.value());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_PAY.value());
            aftersaleLogSaveDTO.setType(AfterSaleStateEnum.TYPE_RETURN.value());
            if (StringUtils.isNotBlank(SecurityUser.getUserName())) {
                SysUserDTO byUsername = sysUserService.getByUsername(SecurityUser.getUserName());
                if (BeanUtil.isEmpty(byUsername)) {
                    aftersaleLogSaveDTO.setCreator("店铺小二(" + SecurityUser.getUserName() + ")");
                    aftersaleLogSaveDTO.setUpdater("店铺小二(" + SecurityUser.getUserName() + ")");
                } else {
                    aftersaleLogSaveDTO.setCreator("平台运营(" + SecurityUser.getUserName() + ")");
                    aftersaleLogSaveDTO.setUpdater("平台运营(" + SecurityUser.getUserName() + ")");
                }
            } else {
                aftersaleLogSaveDTO.setCreator("/");
                aftersaleLogSaveDTO.setUpdater("/");
            }


            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
        }


        resultMap.put("code", ResultCodeEnum.SUCCESS.value());
        resultMap.put("msg", "您的服务单的商品已收到，退款中");
        return resultMap;
    }

    /**
     * @param id:退款记录id
     * @param aftersaleSn:退款编号
     * @Author: SWH ab4856812@163.com
     * @Description:微信退款后修改状态
     * @Date: 2019/6/24 17:42
     * @Version: V1.0
     */

    @Override

    public Map<String, Object> changeStateAfterRefund(@RequestParam("id") Long id, @RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("status") Integer status) {
        Map<String, Object> map = new HashMap<>(3);
        // 修改退款记录表状态值
        AftersaleRefundRecordsDTO aftersaleRefundRecordsDTO = new AftersaleRefundRecordsDTO();
        aftersaleRefundRecordsDTO.setId(id);
        if (status.compareTo(0) == 0) {
            aftersaleRefundRecordsDTO.setRefundStatus(AfterSaleStateEnum.REFUND_STATE_PAY_SUCCESS.value());
        } else {
            aftersaleRefundRecordsDTO.setRefundStatus(AfterSaleStateEnum.REFUND_STATE_PAY_FAIL.value());
        }

        aftersaleRefundRecordsService.update(aftersaleRefundRecordsDTO);

        if (status.compareTo(0) == 0) {
            baseDao.updateStatus(aftersaleSn, AfterSaleStateEnum.AFTER_STATUS_PAYSUCESS.value());
            // 增加退货日志
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setAftersaleSn(aftersaleSn);
            aftersaleLogSaveDTO.setType(AfterSaleStateEnum.TYPE_RETURN.value());
            aftersaleLogSaveDTO.setMessage("您的服务单财务已退款，请您注意查收");
            aftersaleLogSaveDTO.setCreator("平台运营");
            aftersaleLogSaveDTO.setUpdater("平台运营");
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_PAYSUCESS.value());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_END.value());
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
        } else {
            baseDao.updateStatus(aftersaleSn, AfterSaleStateEnum.AFTER_STATUS_PAYFAIL.value());
        }


        map.put("code", com.leimingtech.commons.tools.enums.ResultCodeEnum.SUCCESS);
        map.put("msg", "退款修改状态成功");
        return map;
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:
     * @Date: 2019/6/24 17:42
     * @Version: V1.0
     */
    @Override

    public List<String> findSellerInOutTimeList(@RequestParam("days") String days, @RequestParam("logisticsStatus") String logisticsStatus) {
        return baseDao.findSellerInOutTimeList(days, logisticsStatus);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:定时批量取消
     * @Date: 2019/6/25 20:58
     * @Version: V1.0
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void batchCancel(@RequestBody List<String> aftersaleReturnList) {
        // 批量取消
        baseDao.batchCancel(aftersaleReturnList);
        // 发送日志
        for (String afterSaleSn : aftersaleReturnList) {
            if (StringUtils.isBlank(afterSaleSn)) {
                continue;
            }
            // 增加退货日志
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setAftersaleSn(Long.valueOf(afterSaleSn));
            aftersaleLogSaveDTO.setType(AfterSaleStateEnum.TYPE_RETURN.value());
            aftersaleLogSaveDTO.setMessage("您的申请单已被取消");
            aftersaleLogSaveDTO.setCreator("平台运营");
            aftersaleLogSaveDTO.setUpdater("平台运营");
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_CANCEL.value());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_CANCEL.value());
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
        }

    }

    /**
     * @param aftersaleSn: 售后单号
     * @param memberId:    用户id
     * @Author: SWH ab4856812@163.com
     * @Description: 根据售后单号和用户id查询退货信息
     * @Date: 2019/6/28 21:20
     * @Version: V1.0
     */

    @Override
    public AftersaleReturnDTO getDetail(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId) {
        AftersaleReturnDTO aftersaleReturnDTO = new AftersaleReturnDTO();
        QueryWrapper<AftersaleReturnEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(aftersaleSn != null, "aftersale_sn", aftersaleSn);
        wrapper.eq(memberId != null, "member_id", memberId);
        AftersaleReturnEntity entity = baseDao.selectOne(wrapper);
        if (null != entity) {
            BeanCopier.create(AftersaleReturnEntity.class, AftersaleReturnDTO.class, false).copy(entity, aftersaleReturnDTO, null);
        } else {
            return null;
        }

        return aftersaleReturnDTO;
    }

    /**
     * @Author weixianchun
     * @Description 导出列表查询
     * @Param params
     * @Date 2019/12/5 10:22
     * @Return java.util.List<com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO>
     * @version 1.0
     */

    @Override
    public List<AftersaleReturnPageDTO> findListExport(@RequestParam Map<String, Object> params) {
        List<AftersaleReturnPageDTO> listExport = baseDao.findListExport(params);
        listExport.stream().forEach(aftersaleReturnPageDTO -> {
            aftersaleReturnPageDTO.setRefundAmount(aftersaleReturnPageDTO.getRefundAmount().add(aftersaleReturnPageDTO.getBalanceRefundAmount()));
        });
        return listExport;
    }

    /**
     * 查询退换货数量
     *
     * @param storeId
     * @param type    1 换货 2 退货
     * @return
     */

    @Override
    public Integer findCount(@RequestParam("storeId") Long storeId, @RequestParam("type") Integer type) {
        return baseDao.findCount(storeId, type);
    }

    /**
     * 售后退款
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void aftersaleReturn(@RequestParam("h5WxRefundBaseStr") String h5WxRefundBaseStr) {
        //解析消息
        JSONObject jsonObject = JSON.parseObject(h5WxRefundBaseStr);
        if (jsonObject == null) {
            return;
        }
        H5WxRefundBaseDTO h5WxRefundBaseDTO = JSON.toJavaObject(jsonObject, H5WxRefundBaseDTO.class);
        //如果id不存在,为seller端取消订单退款操作,直接保存退款记录为退款中进行退款
        if (h5WxRefundBaseDTO.getId() == null) {
            AftersaleRefundRecordsDTO aftersaleRefundRecordsDTO = new AftersaleRefundRecordsDTO();
            aftersaleRefundRecordsDTO.setRefundStatus(AfterSaleStateEnum.AFTER_STATUS_PAYING.value());
            boolean result = aftersaleRefundRecordsService.saveRecords(aftersaleRefundRecordsDTO);
            if (!result) {
                return;
            }
            h5WxRefundBaseDTO.setId(aftersaleRefundRecordsDTO.getId());
        }

        if (null != h5WxRefundBaseDTO.getRefundfee() && h5WxRefundBaseDTO.getRefundfee() == 0 && h5WxRefundBaseDTO.getBalanceRefundAmount().compareTo(BigDecimal.ZERO) == 0) {

            log.info("退款金额{}", h5WxRefundBaseDTO.getRefundfee());
            this.changeStateAfterRefund(h5WxRefundBaseDTO.getId(), Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()), 0);
            return;
        }
        Map<String, Object> result = refundService.wechatRefund(h5WxRefundBaseDTO);
        log.info("退款返回结果{}", result);
        if (null != result && result.get(MAP_PARAMS_CODE).equals(com.leimingtech.commons.tools.enums.ResultCodeEnum.SUCCESS.value())) {
            Map<String, Object> map = this.changeStateAfterRefund(h5WxRefundBaseDTO.getId(), Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()), 0);
            log.info("修改状态结果{}", map);
            this.sendMessage(h5WxRefundBaseDTO);
        } else if (null != result && result.get(MAP_PARAMS_CODE).equals(com.leimingtech.commons.tools.enums.ResultCodeEnum.RETRY.value())) {
            log.info("等待定时重试");
        } else {
            log.error("调用退款方法异常，返回结果：{}", result);
            this.changeStateAfterRefund(h5WxRefundBaseDTO.getId(), Long.valueOf(h5WxRefundBaseDTO.getOutrefundno()), 1);
        }
    }

    /**
     * 给用户推送信息
     *
     * @param h5WxRefundBaseDTO
     */
    private void sendMessage(H5WxRefundBaseDTO h5WxRefundBaseDTO) {
        Map<String, Object> map = new HashMap<>(16);
        // 获取用户信息
        MemberDTO memberDTO = memberService.getById(h5WxRefundBaseDTO.getBuyerId());

        // 创建发送站内信用户信息集合
        Map<Long, String> memberMap = new HashMap<>(5);
        memberMap.put(memberDTO.getId(), memberDTO.getMemberName());
        map.put("memberMap", memberMap);

        //设置用户手机号、站内信数据
        map.put("mobile", memberDTO.getMemberMobile());
        map.put("wechatOpenId", memberDTO.getWechatOpenid());

        // 创建短信所需参数Map
        //尊敬的客户，订单${orderSn}已完成退款：${paymentName}支付${payPrice}元预计${time}前原路返还
        // （请以银行实际到账时间为准），请注意查收，退款信息可在售后服务申请记录查询。
        Map<String, String> paramsMap = new HashMap<>(16);
        paramsMap.put("afterSaleSn", String.valueOf(h5WxRefundBaseDTO.getOrdersn()));
        //订单号
        paramsMap.put("orderSn", String.valueOf(h5WxRefundBaseDTO.getOrdersn()));
        //支付方式
        PaymentDTO paymentDTO = Optional.ofNullable(paymentService.getByPaymentCode(
                String.valueOf(h5WxRefundBaseDTO.getPayType()))).orElse(new PaymentDTO());
        paramsMap.put("paymentName", paymentDTO.getPaymentName());
        //退款金额
        paramsMap.put("payPrice", (h5WxRefundBaseDTO.getRefundfee() * 1.0 / 100) + "");
        Date date = new Date();
        String dateStr = DateUtils.format(date, DateUtils.DATE_TIME_PATTERN);
        //到账时间
        paramsMap.put("time", dateStr);
        map.put("paramMap", paramsMap);

        // 创建微信公众号推送信息所需参数Map
        Map<String, String> wechatParamsMap = new HashMap<>(16);
        //提醒
        wechatParamsMap.put("first", "您的订单已成功退款");
        //订单号
        wechatParamsMap.put("keyword1", String.valueOf(h5WxRefundBaseDTO.getOrdersn()));
        //店铺名称
        wechatParamsMap.put("keyword2", h5WxRefundBaseDTO.getStoreName());
        //支付方式
        String orderSn = h5WxRefundBaseDTO.getOrdersn();
        OrderDTO orderDto = orderService.findOrderByOrderSn(orderSn);
        wechatParamsMap.put("keyword3", orderDto.getPaymentName());
        //退款金额
        wechatParamsMap.put("keyword4", (h5WxRefundBaseDTO.getRefundfee() * 1.0 / 100) + "");
        //退款时间
        wechatParamsMap.put("keyword5", DateUtils.getDateStr(DateUtils.DATE_TIME_PATTERN));
        //备注
        wechatParamsMap.put("remark", "感谢您使用我们的产品");

        map.put("wechatParamsJson", JSON.toJSONString(wechatParamsMap));
        map.put("wehcatClickUrl", AddressPrefixProperties.adddressPrefix + "#/pagesD/order/orderDet?paySn=" + h5WxRefundBaseDTO.getOuttradeno() + "%26id=" + h5WxRefundBaseDTO.getOrderId());
        // 创建消息实体
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(MessageCodeEnum.REFUNDS_MESSAGE.value());
        messageDTO.setSendName(h5WxRefundBaseDTO.getStoreName());
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
    }

    /**
     * 根据时间排序---降序
     */
    static class TradeNoComparatorUp implements Comparator {
        @Override
        public int compare(Object object1, Object object2) {
            // 强制转换
            AftersaleLogListDTO aftersaleLogListDTO = (AftersaleLogListDTO) object1;
            AftersaleLogListDTO logListDTO = (AftersaleLogListDTO) object2;
            return logListDTO.getCreateDate().compareTo(aftersaleLogListDTO.getCreateDate());
        }
    }
}
