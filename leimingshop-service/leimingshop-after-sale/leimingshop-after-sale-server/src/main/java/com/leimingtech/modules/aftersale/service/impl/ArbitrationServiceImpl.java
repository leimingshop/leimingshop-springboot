/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.dto.setting.SettingAftersaleDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.modules.aftersale.dao.ArbitrationDao;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.aftersale.enmus.AfterSaleEnum;
import com.leimingtech.modules.aftersale.enmus.AfterSaleStateEnum;
import com.leimingtech.modules.aftersale.entity.ArbitrationEntity;
import com.leimingtech.modules.aftersale.service.*;
import com.leimingtech.modules.aftersale.statuscode.AftersaleStatusCode;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

/**
 * 仲裁表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */
@Service
@Transactional(rollbackFor = Exception.class)

@Slf4j
public class ArbitrationServiceImpl extends BaseServiceImpl<ArbitrationDao, ArbitrationEntity> implements ArbitrationService {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(ArbitrationServiceImpl.class);
    @Autowired
    private AftersaleReturnService returnService;
    @Autowired
    private AftersaleBarterService barterService;
    @Autowired
    private AftersaleApplyService aftersaleApplyService;
    @Autowired
    private AftersaleAuditLogService aftersaleAuditLogService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private AftersaleReturnService aftersaleReturnService;
    @Autowired
    private AftersaleBarterService aftersaleBarterService;
    @Autowired
    private MemberService memberService;

    /**
     * 查询仲裁分页列表
     *
     * @param params 查询条件
     * @return 分页数据 data
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public PageData<ArbitrationPageDTO> page(@RequestParam Map<String, Object> params) {
        IPage<ArbitrationEntity> page = getPage(params, null, false);
        List<ArbitrationPageDTO> arbitrationPageData = baseDao.selectPageList(params, page);
        return new PageData<>(arbitrationPageData, page.getTotal());
    }

    @Override

    public List<ArbitrationDTO> list(@RequestParam Map<String, Object> params) {
        List<ArbitrationEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ArbitrationDTO.class);
    }

    private QueryWrapper<ArbitrationEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<ArbitrationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        return wrapper;
    }

    @Override

    public ArbitrationDTO get(Long id) {
        ArbitrationEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ArbitrationDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody ArbitrationDTO dto) {
        ArbitrationEntity entity = ConvertUtils.sourceToTarget(dto, ArbitrationEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody ArbitrationDTO dto) {
        ArbitrationEntity entity = ConvertUtils.sourceToTarget(dto, ArbitrationEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, ArbitrationEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * H5用户申请仲裁
     *
     * @param dto ApplyArbitrationDTO 申请仲裁对象
     * @date 2020/4/8/008 16:30
     * @author xuzhch
     */
    @Transactional

    @Override
    public void applyArbitration(@RequestBody ApplyArbitrationDTO dto) {
        //保存仲裁记录
        ArbitrationEntity arbitrationEntity = new ArbitrationEntity();
        BeanCopier.create(ApplyArbitrationDTO.class, ArbitrationEntity.class, false).copy(dto, arbitrationEntity, null);
        arbitrationEntity.setArbitrationApplyDate(new Date());
        arbitrationEntity.setArbitrationStatus(3);

        AftersaleApplyDTO aftersaleApplyDTO = aftersaleApplyService.getApply(arbitrationEntity.getAftersaleSn());
        arbitrationEntity.setStoreId(aftersaleApplyDTO.getStoreId());
        int count = baseDao.insert(arbitrationEntity);
        if (count > 0) {
            //保存仲裁申请记录
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setMessage("您的仲裁信息已申请成功，待售后审核中");
            aftersaleLogSaveDTO.setCreator("平台运营");
            aftersaleLogSaveDTO.setUpdater("平台运营");
            aftersaleLogSaveDTO.setAftersaleSn(dto.getAftersaleSn());
            aftersaleLogSaveDTO.setStatus(AfterSaleEnum.NOAUDIT.value());
            aftersaleLogSaveDTO.setType(dto.getArbitrationType());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_ARBITRATION.value());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            //发送mq消息异步保存售后日志记录
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);

            // 保存售后审核表信息
            AftersaleAuditLogSaveDTO aftersaleAuditLogSaveDTO = new AftersaleAuditLogSaveDTO();
            aftersaleAuditLogSaveDTO.setId(IdGenerator.defaultSnowflakeId());
            aftersaleAuditLogSaveDTO.setAftersaleSn(dto.getAftersaleSn());
            aftersaleAuditLogSaveDTO.setProcess(Long.valueOf(AfterSaleEnum.ARBITRATION_AUDIT.value()));
            aftersaleAuditLogSaveDTO.setResult(AfterSaleEnum.RESULT_REVIEW.value());
            aftersaleAuditLogSaveDTO.setAftersaleType(dto.getArbitrationType());
            aftersaleAuditLogSaveDTO.setReason("提交仲裁申请");
            aftersaleAuditLogSaveDTO.setCreateDate(new Date());
            aftersaleAuditLogSaveDTO.setUpdateDate(null);
            aftersaleAuditLogService.save(aftersaleAuditLogSaveDTO);
        }
    }

    /**
     * H5查看仲裁进度
     *
     * @param aftersaleSn 售后单号
     * @return UserDetailArbitrationDTO
     * @date 2020/4/8/008 16:48
     * @author xuzhch
     */

    @Override
    public UserDetailArbitrationDTO userArbitrationDetail(Long aftersaleSn) {

        UserDetailArbitrationDTO dto = new UserDetailArbitrationDTO();
        ArbitrationEntity arbitrationEntity = baseDao.selectOne(Wrappers.<ArbitrationEntity>lambdaQuery().eq(ArbitrationEntity::getAftersaleSn, aftersaleSn).eq(ArbitrationEntity::getDelFlag, 0));
        if (BeanUtil.isEmpty(arbitrationEntity)) {
            return null;
        }
        BeanCopier.create(ArbitrationEntity.class, UserDetailArbitrationDTO.class, false).copy(arbitrationEntity, dto, null);
        return dto;
    }

    /**
     * 查看仲裁详情
     *
     * @param id 仲裁信息ID
     * @return ArbitrationDetailDTO 返回详情
     * @date 2020/4/9/009 10:26
     * @author xuzhch
     */

    @Override
    public ArbitrationDetailDTO arbitrationDetail(Long id) {
        ArbitrationEntity arbitrationEntity = baseDao.selectById(id);
        ArbitrationDetailDTO arbitrationDetailDTO = new ArbitrationDetailDTO();
        BeanCopier.create(ArbitrationEntity.class, ArbitrationDetailDTO.class, false).copy(arbitrationEntity, arbitrationDetailDTO, null);
        if (arbitrationDetailDTO.getArbitrationType().equals(0)) {
            AftersaleReturnDetailDTO detail = returnService.detail(arbitrationDetailDTO.getAftersaleSn());
            arbitrationDetailDTO.setReturnDetailDTO(detail);
        } else {
            AftersaleBarterDetailDTO detail = barterService.detail(arbitrationDetailDTO.getAftersaleSn());
            arbitrationDetailDTO.setBarterDetailDTO(detail);
        }
        return arbitrationDetailDTO;

    }

    /**
     * 保存平台仲裁信息
     *
     * @param auditArbitrationDTO 保存审核对象
     * @date 2020/4/9/009 10:25
     * @author xuzhch
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void audit(@RequestBody AuditArbitrationDTO auditArbitrationDTO) {
        ArbitrationEntity entity = new ArbitrationEntity();
        BeanCopier.create(AuditArbitrationDTO.class, ArbitrationEntity.class, false).copy(auditArbitrationDTO, entity, null);
        entity.setArbitrationAuditDate(new Date());
        baseDao.updateById(entity);
        ArbitrationEntity arbitrationEntity = baseDao.selectById(entity.getId());

        //仲裁拒绝
        if (AfterSaleEnum.RESULT_REFUSE.value().equals(auditArbitrationDTO.getArbitrationStatus())) {
            //保存仲裁审核记录
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            //设置信息
            aftersaleLogSaveDTO.setMessage("平台仲裁审核不通过：" + auditArbitrationDTO.getAuditReason());
            aftersaleLogSaveDTO.setCreator("平台运营(" + auditArbitrationDTO.getAuditor() + ")");
            aftersaleLogSaveDTO.setUpdater("平台运营(" + auditArbitrationDTO.getAuditor() + ")");
            aftersaleLogSaveDTO.setAftersaleSn(arbitrationEntity.getAftersaleSn());
            aftersaleLogSaveDTO.setStatus(AfterSaleEnum.NOAUDIT.value());
            aftersaleLogSaveDTO.setType(arbitrationEntity.getArbitrationType());
            //设置进度
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_ARBITRATION_REFUASE.value());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            //发送mq消息异步保存售后日志记录
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);

            /***********************************************************/
            // 修改审核日志
            AftersaleAuditLogDTO aftersaleAuditLogDTO = new AftersaleAuditLogDTO();
            aftersaleAuditLogDTO.setId(auditArbitrationDTO.getAuditLogId());
            aftersaleAuditLogDTO.setAftersaleSn(arbitrationEntity.getAftersaleSn());
            aftersaleAuditLogDTO.setResult(AfterSaleEnum.RESULT_REFUSE.value());
            aftersaleAuditLogDTO.setProcess(Long.valueOf(AfterSaleEnum.ARBITRATION_AUDIT.value()));
            aftersaleAuditLogDTO.setReason("仲裁审核不通过：" + auditArbitrationDTO.getAuditReason());
            aftersaleAuditLogDTO.setCreateDate(new Date());
            aftersaleAuditLogDTO.setUpdateDate(new Date());
            aftersaleAuditLogDTO.setCreator("平台运营(" + SecurityUser.getUserName() + ")");
            aftersaleAuditLogService.update(aftersaleAuditLogDTO);
        } else {
            this.saveReturnBarter(arbitrationEntity, auditArbitrationDTO.getAuditLogId(), AfterSaleEnum.ARBITRATION_AUDIT.value(), auditArbitrationDTO.getAuditReason());
        }


    }


    @Override
    public ArbitrationDTO getDataByAfterSn(Long aftersaleSn) {
        ArbitrationEntity arbitrationEntity = baseDao.selectOne(Wrappers.<ArbitrationEntity>lambdaQuery().eq(ArbitrationEntity::getAftersaleSn, aftersaleSn));
        if (BeanUtil.isEmpty(arbitrationEntity)) {
            return null;
        }
        ArbitrationDTO arbitrationDTO = new ArbitrationDTO();
        BeanCopier.create(ArbitrationEntity.class, ArbitrationDTO.class, false).copy(arbitrationEntity, arbitrationDTO, null);
        return arbitrationDTO;
    }

    /**
     * @param arbitrationEntity: 售后申请实体
     * @Author: SWH ab4856812@163.com
     * @Description:根据传来的售后单号，保存对应的退货表和换货表记录（此时会设置此申请单已完成），并增加日志
     * @Description:如果关闭是否必填物流单号按钮，平台审核后直接商家直接可以点击确认收货按钮
     * @Date: 2019/6/21 11:09
     * @Version: V1.0
     */
    private Map<String, Object> saveReturnBarter(ArbitrationEntity arbitrationEntity, Long auditLogId, Integer process, String reason) {

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("process", process.toString());
        logMap.put("reason", reason);

        Map<String, Object> resultMap = new HashMap(3);

        AftersaleApplyDTO detail = aftersaleApplyService.getApply(arbitrationEntity.getAftersaleSn());
        // 保存退货单或者换货单
        // 支付方式
        Map<String, Object> orderMap = new HashMap<>(1);
        orderMap.put("id", detail.getOrderId());
        OrderDTO orderDTO = orderService.getOrderByMap(orderMap);
        if (null == orderDTO) {
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "查询订单信息异常");
            return resultMap;
        }
        // 查询售后设置如果售后设置不填物流单号，则售后物流状态变为卖家待收货，并修改日志
        String queryRedisByName = settingService.queryRedisByName(SettingsEnum.AFTERSALE.value());
        SettingAftersaleDTO aftersaleDTO = JSON.parseObject(queryRedisByName, SettingAftersaleDTO.class);
        if (null == aftersaleDTO) {
            log.info("审核查询售后设置异常，请稍后再试");
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "审核查询售后设置异常，请稍后再试");
            return resultMap;
        }
        if (arbitrationEntity.getArbitrationType().equals(AfterSaleEnum.TYPERETURN.value())) {
            AftersaleReturnSaveDTO aftersaleReturnSaveDTO = new AftersaleReturnSaveDTO();
            BeanCopier.create(AftersaleApplyDTO.class, AftersaleReturnSaveDTO.class, false).copy(detail, aftersaleReturnSaveDTO, null);
            //TODO SWH 后期后面更换生成方式

            // 开始区分余额支付金额和第三方支付金额
            // 判断本次退款的金额，优先退余额支付的金额
            if (orderDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {

                // 计算订单可退余额金额
                BigDecimal lastBalance = orderDTO.getBalanceAmount().subtract(orderDTO.getBalanceRefundAmount());

                // 还有可退余额
                if (lastBalance.compareTo(BigDecimal.ZERO) > 0) {
                    // 如果可退余额大于等于本次退款金额那么全部从余额的金额退款，不走第三方退款
                    if (lastBalance.compareTo(detail.getRefundAmount()) > -1) {
                        aftersaleReturnSaveDTO.setBalanceRefundAmount(detail.getRefundAmount());
                        aftersaleReturnSaveDTO.setRefundAmount(BigDecimal.ZERO);
                    } else {
                        // 可退余额小于本次退款金额，则需要混合退款或者第三方退款
                        BigDecimal thirdRefund = detail.getRefundAmount().subtract(lastBalance);
                        aftersaleReturnSaveDTO.setRefundAmount(thirdRefund);
                        aftersaleReturnSaveDTO.setBalanceRefundAmount(lastBalance);
                    }
                }

            }

            aftersaleReturnSaveDTO.setServiceSn(IdGenerator.defaultSnowflakeId());
            aftersaleReturnSaveDTO.setAftersaleStatus(AfterSaleStateEnum.AFTER_STATUS_RETURNIN.value());

            aftersaleReturnSaveDTO.setLogisticsStatus(AfterSaleStateEnum.LOGISTICS_STATUS_BUYEROUT.value());
            if ((String.valueOf(AfterSaleEnum.DEFAULT)).equals(aftersaleDTO.getAftersaleExpressSn()) && AfterSaleEnum.SELLER_AUDIT.value().equals(process)) {
                // 变为卖家待收货
                aftersaleReturnSaveDTO.setLogisticsStatus(AfterSaleStateEnum.LOGISTICS_STATUS_SELLERWAITIN.value());
            }
            aftersaleReturnSaveDTO.setPayType(orderDTO.getPaymentId());
            aftersaleReturnService.save(aftersaleReturnSaveDTO);

            // 售后申请通过 发送短信
            this.sendMessage(arbitrationEntity.getAftersaleSn(), MessageEnum.SEND_MODE_SMS_INNER.value(), MessageCodeEnum.AFTER_AUDIT_YES.value(), true);

            log.info("保存退货表成功");
            mlogger.info(AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_SAVE_RETURN_SUCCESS_CODE, AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_SAVE_RETURN_SUCCESS_MESSAGE, logMap);
        } else if (arbitrationEntity.getArbitrationType().equals(AfterSaleEnum.TYPEBARTER.value())) {
            AftersaleBarterSaveDTO aftersaleBarterSaveDTO = new AftersaleBarterSaveDTO();
            BeanCopier.create(AftersaleApplyDTO.class, AftersaleBarterSaveDTO.class, false).copy(detail, aftersaleBarterSaveDTO, null);
            aftersaleBarterSaveDTO.setServiceSn(IdGenerator.defaultSnowflakeId());
            aftersaleBarterSaveDTO.setPayType(orderDTO.getPaymentId());
            aftersaleBarterSaveDTO.setAftersaleStatus(AfterSaleStateEnum.AFTER_STATUS_BARTERIN.value());
            aftersaleBarterSaveDTO.setLogisticsStatus(AfterSaleStateEnum.LOGISTICS_STATUS_BUYEROUT.value());
            if ((String.valueOf(AfterSaleEnum.DEFAULT)).equals(aftersaleDTO.getAftersaleExpressSn()) && AfterSaleEnum.SELLER_AUDIT.value().equals(process)) {
                // 变为卖家待收货
                aftersaleBarterSaveDTO.setLogisticsStatus(AfterSaleStateEnum.LOGISTICS_STATUS_SELLERWAITIN.value());
            }
            aftersaleBarterService.save(aftersaleBarterSaveDTO);
            log.info("保存换货表成功");
            mlogger.info(AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_SAVE_BATER_SUCCESS_CODE, AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_SAVE_BATER_SUCCESS_MESSAGE, logMap);

        }
        // 修改审核日志
        AftersaleAuditLogDTO aftersaleAuditLogDTO = new AftersaleAuditLogDTO();
        aftersaleAuditLogDTO.setId(auditLogId);
        aftersaleAuditLogDTO.setAftersaleSn(arbitrationEntity.getAftersaleSn());
        aftersaleAuditLogDTO.setResult(AfterSaleEnum.RESULT_PROCESS.value());
        aftersaleAuditLogDTO.setProcess(Long.valueOf(AfterSaleEnum.ARBITRATION_AUDIT.value()));
        aftersaleAuditLogDTO.setReason(reason);
        aftersaleAuditLogDTO.setCreateDate(new Date());
        aftersaleAuditLogDTO.setUpdateDate(new Date());
        aftersaleAuditLogDTO.setCreator("平台运营(" + SecurityUser.getUserName() + ")");
        aftersaleAuditLogService.update(aftersaleAuditLogDTO);
        // 保存日志
        AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
        if (AfterSaleEnum.TYPERETURN.value().equals(arbitrationEntity.getArbitrationType())) {
            aftersaleLogSaveDTO.setMessage("您的服务单退款申请业务已确认，请将商品寄回");
        } else {
            aftersaleLogSaveDTO.setMessage("您的服务单换货申请业务已确认，请将商品寄回");
        }
        aftersaleLogSaveDTO.setAftersaleSn(arbitrationEntity.getAftersaleSn());
        aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_RETURNIN.value());
        aftersaleLogSaveDTO.setType(arbitrationEntity.getArbitrationType());
        aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_IN.value());
        aftersaleLogSaveDTO.setCreator("平台运营");
        aftersaleLogSaveDTO.setCreateDate(new Date());
        aftersaleLogSaveDTO.setUpdater(SecurityUser.getUserName());
        // 发送mq消息异步保存售后日志记录
        String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);

        resultMap.put("code", ResultCodeEnum.SUCCESS.value());
        resultMap.put("msg", "审核成功");
        return resultMap;
    }

    /**
     * 封装发送短信参数并发送短信消息
     *
     * @param aftersaleSn: 售后单号
     * @param type:        发送类型  0 站内信 1 短信 4 站内信、短信、微信消息推送
     * @param code:        消息类型
     * @param flag:        审核是否通过  true通过，false未通过
     * @date 2020/4/10 9:24
     * @author lixiangx@leimingtech.com
     **/
    private void sendMessage(Long aftersaleSn, int type, String code, boolean flag) {
        // 售后申请通过 发送短信
        AftersaleReturnDetailDTO detail = aftersaleReturnService.detail(aftersaleSn);
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", type);
        map.put("code", code);

        // 创建短信所需参数Map
        Map<String, String> paramsMap = new HashMap<>(16);
        paramsMap.put("sellerName", detail.getAftersaleApplyDTO().getStoreName());
        paramsMap.put("orderSn", String.valueOf(detail.getAftersaleApplyDTO().getOrderSn()));
        paramsMap.put("cause", detail.getAftersaleApplyDTO().getAftersaleReason());
        map.put("paramsMap", paramsMap);

        // 创建微信公众号推送信息所需参数Map
        Map<String, String> wechatParamsMap = new HashMap<>(16);
        wechatParamsMap.put("first", flag ? "售后审核通过" : "售后审核未通过");
        wechatParamsMap.put("keyword1", String.valueOf(detail.getAftersaleApplyDTO().getOrderSn()));
        wechatParamsMap.put("keyword2", detail.getAftersaleGoodsDTOList().get(0).getGoodsName());
        wechatParamsMap.put("keyword3", detail.getAftersaleApplyDTO().getAftersaleReason());
        wechatParamsMap.put("keyword4", DateUtils.getDateStr(DateUtils.DATE_TIME_PATTERN));
        wechatParamsMap.put("remark", "感谢您使用我们的产品");
        map.put("wechatParamsJson", wechatParamsMap);

        MemberDTO memberDTO = memberService.getById(detail.getAftersaleApplyDTO().getMemberId());
        map.put("mobile", detail.getAftersaleApplyDTO().getContactsPhone());
        map.put("memberId", detail.getAftersaleApplyDTO().getMemberId());
        map.put("nikeName", detail.getAftersaleApplyDTO().getMemberName());
        map.put("sendName", detail.getAftersaleApplyDTO().getStoreName());
        map.put("wechatOpenId", memberDTO.getWechatOpenid());
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(map));
    }

    /**
     * 仲裁记录导出
     *
     * @param params 导出条件
     * @return List<ArbitrationPageDTO> 查询结果
     */

    @Override
    public List<ArbitrationPageDTO> exportList(@RequestParam Map<String, Object> params) {
        return baseDao.selectPageList(params, null);
    }

}
