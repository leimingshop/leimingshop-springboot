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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.config.AddressPrefixProperties;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.dto.setting.SettingAftersaleDTO;
import com.leimingtech.dto.setting.SettingGoodsAuditDTO;
import com.leimingtech.dto.setting.SettingSeniorDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.aftersale.dao.AftersaleApplyDao;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.aftersale.enmus.AfterSaleEnum;
import com.leimingtech.modules.aftersale.enmus.AfterSaleStateEnum;
import com.leimingtech.modules.aftersale.entity.AftersaleApplyEntity;
import com.leimingtech.modules.aftersale.service.*;
import com.leimingtech.modules.aftersale.statuscode.AftersaleStatusCode;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.order.OrderGoodsDTO;
import com.leimingtech.modules.dto.settle.ReturnBillDTO;
import com.leimingtech.modules.enums.order.AfterSettingEnum;
import com.leimingtech.modules.enums.order.OrderGoodsEnum;
import com.leimingtech.modules.enums.order.VirtualFlagEnum;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
 * 售后申请表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AftersaleApplyServiceImpl extends CrudServiceImpl<AftersaleApplyDao, AftersaleApplyEntity, AftersaleApplyDTO> implements AftersaleApplyService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(AftersaleApplyServiceImpl.class);

    @Resource
    private AftersaleApplyDao aftersaleApplyDao;

    @Autowired

    private AftersaleApplyService aftersaleApplyService;

    @Autowired

    private AftersaleAuditLogService aftersaleAuditLogService;

    @Autowired

    private AftersaleGoodsService aftersaleGoodsService;

    @Autowired

    private AftersaleLogService aftersaleLogService;

    @Autowired

    private AftersaleReturnService aftersaleReturnService;

    @Autowired

    private AftersaleBarterService aftersaleBarterService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ArbitrationService arbitrationService;
    @Autowired
    private SysMessageService sysMessageService;

    @Override
    public QueryWrapper<AftersaleApplyEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AftersaleApplyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "aftersale_sn", id);

        return wrapper;
    }

    /**
     * 保存售后申请
     *
     * @param dto 售后申请信息
     * @return integer
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public Integer save(@RequestBody AftersaleApplySaveDTO dto) {
        AftersaleApplyEntity reasonDescriptionEntity = ConvertUtils.sourceToTarget(dto, AftersaleApplyEntity.class);
        return aftersaleApplyDao.insert(reasonDescriptionEntity);
    }

    /**
     * 修改售后申请
     *
     * @param dto 售后申请信息
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public void update(@RequestBody AftersaleApplyDTO dto) {
        AftersaleApplyEntity aftersaleApplyEntity = ConvertUtils.sourceToTarget(dto, AftersaleApplyEntity.class);
        baseDao.update(aftersaleApplyEntity, new QueryWrapper<AftersaleApplyEntity>().eq("aftersale_sn", aftersaleApplyEntity.getAftersaleSn()));
    }

    /**
     * 删除售后申请
     *
     * @param ids 售后申请ID数组
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据ID查询售后申请
     *
     * @param id 售后申请ID
     * @return AftersaleApplyDTO 售后申请信息
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public AftersaleApplyDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 根据售后单号查询售后申请
     *
     * @param aftersaleSn 售后单号
     * @return AftersaleApplyDTO  售后申请信息
     * @author huangkeyuan @leimingtech.com
     * @date 2020 -09-08 14:46
     */
    @Override

    public AftersaleApplyDTO getApply(Long aftersaleSn) {
        QueryWrapper<AftersaleApplyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(aftersaleSn != null, "aftersale_sn", aftersaleSn);
        AftersaleApplyEntity entity = baseDao.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(entity, AftersaleApplyDTO.class);
    }

    /**
     * 根据售后单号和用户id查询退货信息
     *
     * @param aftersaleSn : 售后单号
     * @param memberId    : 用户id
     * @return the detail
     * @author xuzhch
     * @date 2020年09月21日
     */

    @Override
    public AftersaleApplyDTO getDetail(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId) {
        AftersaleApplyDTO aftersaleApplyDTO = new AftersaleApplyDTO();
        QueryWrapper<AftersaleApplyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(memberId != null, "member_id", memberId);
        wrapper.eq(aftersaleSn != null, "aftersale_sn", aftersaleSn);
        AftersaleApplyEntity entity = baseDao.selectOne(wrapper);
        BeanCopier.create(AftersaleApplyEntity.class, AftersaleApplyDTO.class, false).copy(entity, aftersaleApplyDTO, null);
        return aftersaleApplyDTO;
    }

    /**
     * 查询所有的售后申请列表
     *
     * @param params 查询条件
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleApplyDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public List<AftersaleApplyDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 分页查询所有的售后申请列表
     *
     * @param params  查询条件
     * @param storeId 店铺ID
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleApplyDTO>   data
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public PageData<AftersaleApplyPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId) {
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
        IPage<AftersaleApplyEntity> page = getPage(params, "lar.create_date", false);
        // 查询
        List<AftersaleApplyPageDTO> list = aftersaleApplyDao.findPage(params, page);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 售后申请详情
     *
     * @param aftersaleSn : 售后单号
     * @return com.leimingtech.modules.aftersale.dto.AftersaleReturnDetailDTO apply detail dto
     * @author xuzhch
     * @date 2020年09月21日
     * @Author huangkeyuan
     * @Date 11 :16 2019-06-17
     */
    @Override

    public AftersaleApplyDetailDTO detail(Long aftersaleSn) {


        AftersaleApplyDetailDTO aftersaleApplyDetailDTO = new AftersaleApplyDetailDTO();

        // 获取售后原因
        AftersaleApplyDTO aftersaleApplyDTO = aftersaleApplyService.getApply(aftersaleSn);
        aftersaleApplyDetailDTO.setAftersaleApplyDTO(aftersaleApplyDTO);

        // 获取商家售后审核
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", aftersaleSn);
        List<AftersaleAuditLogDTO> auditList = aftersaleAuditLogService.list(map);
        aftersaleApplyDetailDTO.setAftersaleAuditLogDTOList(auditList);

        // 获取售后商品数据
        List<AftersaleGoodsDTO> goodsDTOList = aftersaleGoodsService.list(map);
        aftersaleApplyDetailDTO.setAftersaleGoodsDTOList(goodsDTOList);

        // 获取日志列表
        List<AftersaleLogListDTO> logDTOList = aftersaleLogService.listLog(aftersaleSn);
        List<AftersaleLogListDTO> logAuditDTOList = aftersaleAuditLogService.listLog(aftersaleSn);
        logDTOList.addAll(logAuditDTOList);
        Collections.sort(logDTOList, new AftersaleApplyServiceImpl.TradeNoComparatorUp());

        aftersaleApplyDetailDTO.setAftersaleLogListDTOList(logDTOList);


        return aftersaleApplyDetailDTO;
    }

    /**
     * H5端售后申请记录列表
     *
     * @param params   :查询条件
     * @param memberId :用户Id
     * @return page data
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public PageData<AftersaleApplyRecordDTO> applyRecordPage(@RequestParam Map<String, Object> params, @RequestParam("memberId") Long memberId) {
        if (null != memberId) {
            params.put("memberId", memberId);
        }
        IPage page = getPage(params, Constant.CREATE_DATE, false);
        List<AftersaleApplyRecordDTO> applyRecordList = aftersaleApplyDao.applyRecordPage(page, params);
        for (AftersaleApplyRecordDTO aftersaleApplyRecordDTO : applyRecordList) {
            Long aftersaleSn = aftersaleApplyRecordDTO.getAftersaleSn();
            // 判断是退货还是换货
            AftersaleReturnDTO aftersaleReturnDTO = aftersaleReturnService.getDetail(aftersaleSn, memberId);
            if (aftersaleReturnDTO != null) {
                aftersaleApplyRecordDTO.setFinalStatus(aftersaleReturnDTO.getAftersaleStatus());
            }
            AftersaleBarterDTO aftersaleBarterDTO = aftersaleBarterService.getDetail(aftersaleSn, memberId);
            if (aftersaleBarterDTO != null) {
                aftersaleApplyRecordDTO.setFinalStatus(aftersaleBarterDTO.getAftersaleStatus());
            }
        }
        return new PageData<>(applyRecordList, page.getTotal());
    }

    /**
     * 售后申请审核结果
     *
     * @param aftersaleAuditLogSaveDTO 售后审核信息
     * @return map 保存结果
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public Map<String, Object> saveApplyResult(@RequestBody AftersaleAuditLogSaveDTO aftersaleAuditLogSaveDTO) {
        Map<String, Object> resultMap = new HashMap<>(3);

        AftersaleApplyDTO updateApplyDTO = new AftersaleApplyDTO();
        if (null == aftersaleAuditLogSaveDTO || null == aftersaleAuditLogSaveDTO.getAftersaleSn() || null == aftersaleAuditLogSaveDTO.getId()) {
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "售后单号不能为空或审核单号不能为空");
            log.info("售后单号不能为空或审核单号不能为空");
            return resultMap;
        }
        Long aftersaleSn = aftersaleAuditLogSaveDTO.getAftersaleSn();
        Integer result = aftersaleAuditLogSaveDTO.getResult();
        Long process = aftersaleAuditLogSaveDTO.getProcess();
        updateApplyDTO.setAftersaleSn(aftersaleSn);
        // 设置审核日志信息
        AftersaleAuditLogDTO aftersaleAuditLogDTO = new AftersaleAuditLogDTO();
        //设置操作日志信息
        AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
        // 保存售后日志
        aftersaleLogSaveDTO.setAftersaleSn(aftersaleSn);
        aftersaleLogSaveDTO.setStatus(0);
        aftersaleLogSaveDTO.setType(aftersaleAuditLogSaveDTO.getAftersaleType());
        aftersaleLogSaveDTO.setCreateDate(new Date());
        aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_AUDIT.value());
        if (process.intValue() == AfterSaleEnum.SELLER_AUDIT.value()) {
            aftersaleAuditLogDTO.setCreator("店铺小二(" + SecurityUser.getUserName() + ")");
            aftersaleLogSaveDTO.setCreator("店铺小二(" + SecurityUser.getUserName() + ")");
            aftersaleLogSaveDTO.setUpdater("店铺小二(" + SecurityUser.getUserName() + ")");
        } else {
            aftersaleAuditLogDTO.setCreator("平台运营(" + SecurityUser.getUserName() + ")");
            aftersaleLogSaveDTO.setCreator("平台运营(" + SecurityUser.getUserName() + ")");
            aftersaleLogSaveDTO.setUpdater("平台运营(" + SecurityUser.getUserName() + ")");
        }
        if (result.equals(AfterSaleEnum.RESULT_REFUSE.value())) {
            //修改订单表售后状态 为审核未通过
            if (aftersaleAuditLogSaveDTO.getAftersaleType().equals(AfterSaleEnum.TYPERETURN.value())) {
                AftersaleApplyEntity aftersaleApplyEntity = baseDao.selectOne(Wrappers.<AftersaleApplyEntity>lambdaQuery()
                        .eq(AftersaleApplyEntity::getAftersaleSn, aftersaleAuditLogSaveDTO.getAftersaleSn()));
                List<Long> orderIds = new ArrayList<>();
                orderIds.add(aftersaleApplyEntity.getOrderId());
                orderService.updateAfterFlag(orderIds, AfterSaleEnum.AFTER_APPLY_NO.value());
            }
            // 审核不通过
            updateApplyDTO.setAuditResult(result);
            updateApplyDTO.setAuditStatus(AfterSaleEnum.FINISH.value());
            aftersaleApplyService.update(updateApplyDTO);
            resultMap.put("code", ResultCodeEnum.SUCCESS.value());
            resultMap.put("msg", "保存审核申请成功，修改为不通过");
            // 更新
            BeanCopier.create(AftersaleAuditLogSaveDTO.class, AftersaleAuditLogDTO.class, false).copy(aftersaleAuditLogSaveDTO, aftersaleAuditLogDTO, null);
            aftersaleAuditLogDTO.setReason("审核不通过：" + aftersaleAuditLogDTO.getReason());
            aftersaleAuditLogDTO.setCreateDate(new Date());
            aftersaleAuditLogDTO.setUpdateDate(new Date());
            aftersaleAuditLogService.update(aftersaleAuditLogDTO);

            aftersaleLogSaveDTO.setMessage(aftersaleAuditLogDTO.getReason());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_REFUASE.value());
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
            // 售后申请未通过 发送短信
            this.sendMessage(aftersaleSn, MessageCodeEnum.AFTER_AUDIT_NO.value(), false);
            //TODO SWH 二期功能此处需要恢复order_goods表中的可售后数量
            return resultMap;
        }
        AftersaleApplyDTO aftersaleApplyDTO = this.getApply(aftersaleSn);
        if (result.equals(AfterSaleEnum.RESULT_PROCESS.value()) && process.equals(Long.valueOf(AfterSaleEnum.SELLER_AUDIT.value()))) {
            // 商家审核通过(平台如果关闭审核按钮 则商家通过后审核单直接设为已完成并保存对应退货换货表)
            // 获取setting表中的审核设置
            String queryRedisByName = settingService.queryRedisByName(SettingsEnum.GOODS_AUDIT.value());
            SettingGoodsAuditDTO goodsAuditSet = JSON.parseObject(queryRedisByName, SettingGoodsAuditDTO.class);
            if (null == goodsAuditSet) {
                resultMap.put("code", ResultCodeEnum.WARN.value());
                resultMap.put("msg", "查询审核设置异常");
                return resultMap;
            }
            // 更新原来的商家审核记录
            BeanCopier.create(AftersaleAuditLogSaveDTO.class, AftersaleAuditLogDTO.class, false).copy(aftersaleAuditLogSaveDTO, aftersaleAuditLogDTO, null);
            aftersaleAuditLogDTO.setReason("审核通过：" + aftersaleAuditLogDTO.getReason());
            aftersaleAuditLogDTO.setCreateDate(new Date());
            aftersaleAuditLogDTO.setUpdateDate(new Date());
            aftersaleAuditLogDTO.setCreator("店铺小二(" + SecurityUser.getUserName() + ")");
            aftersaleAuditLogService.update(aftersaleAuditLogDTO);
            aftersaleLogSaveDTO.setMessage(aftersaleAuditLogDTO.getReason());
            // 发送mq消息异步保存售后日志记录
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);


            boolean returnFlag = goodsAuditSet.getGoodsReturnOpen().equals(String.valueOf(AfterSaleEnum.DEFAULT.value())) && aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPERETURN.value());
            boolean exchangeFlag = goodsAuditSet.getGoodsExchangeOpen().equals(String.valueOf(AfterSaleEnum.DEFAULT.value())) && aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPEBARTER.value());
            if (returnFlag || exchangeFlag) {
                // 平台退货或换货审核按钮关闭
                return this.saveReturnBarter(aftersaleApplyDTO, AfterSaleEnum.SELLER_AUDIT.value(), aftersaleAuditLogSaveDTO.getReason());
            }
            updateApplyDTO.setAuditResult(AfterSaleEnum.RESULT_REVIEW.value());
            updateApplyDTO.setAuditStatus(AfterSaleEnum.ADMINAUDITING.value());
            aftersaleApplyService.update(updateApplyDTO);

            // 保存平台审核记录
            AftersaleAuditLogSaveDTO adminPrepareAudit = new AftersaleAuditLogSaveDTO();
            adminPrepareAudit.setId(IdGenerator.defaultSnowflakeId());
            adminPrepareAudit.setAftersaleType(aftersaleApplyDTO.getAftersaleType());
            adminPrepareAudit.setAftersaleSn(aftersaleSn);
            adminPrepareAudit.setUpdateDate(null);
            adminPrepareAudit.setProcess(Long.valueOf(AfterSaleEnum.ADMIN_AUDIT.value()));
            adminPrepareAudit.setResult(AfterSaleEnum.RESULT_REVIEW.value());
            aftersaleAuditLogService.save(adminPrepareAudit);

            resultMap.put("code", ResultCodeEnum.SUCCESS.value());
            resultMap.put("msg", "修改审核状态成功");


        } else if (result.equals(AfterSaleEnum.RESULT_PROCESS.value()) && process.equals(Long.valueOf(AfterSaleEnum.ADMIN_AUDIT.value()))) {
            // 更新平台审核记录信息
            BeanCopier.create(AftersaleAuditLogSaveDTO.class, AftersaleAuditLogDTO.class, false).copy(aftersaleAuditLogSaveDTO, aftersaleAuditLogDTO, null);
            aftersaleAuditLogDTO.setReason("审核通过：" + aftersaleAuditLogDTO.getReason());
            aftersaleAuditLogDTO.setCreateDate(new Date());
            aftersaleAuditLogDTO.setUpdateDate(new Date());
            aftersaleAuditLogDTO.setCreator("平台运营(" + SecurityUser.getUserName() + ")");
            aftersaleAuditLogService.update(aftersaleAuditLogDTO);
            // 平台审核通过
            aftersaleLogSaveDTO.setMessage(aftersaleAuditLogDTO.getReason());
            // 发送mq消息异步保存售后日志记录
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
            return this.saveReturnBarter(aftersaleApplyDTO, AfterSaleEnum.ADMIN_AUDIT.value(), aftersaleAuditLogSaveDTO.getReason());
        }


        return resultMap;
    }

    /**
     * @param aftersaleApplyDTO: 售后申请实体
     * @Author: SWH ab4856812@163.com
     * @Description:根据传来的售后单号，保存对应的退货表和换货表记录（此时会设置此申请单已完成），并增加日志
     * @Description:如果关闭是否必填物流单号按钮，平台审核后直接商家直接可以点击确认收货按钮
     * @Date: 2019/6/21 11:09
     * @Version: V1.0
     */
    private Map<String, Object> saveReturnBarter(AftersaleApplyDTO aftersaleApplyDTO, Integer process, String reason) {

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("process", process.toString());
        logMap.put("reason", reason);

        Map<String, Object> resultMap = new HashMap(3);
        // 修改售后申请表状态
        AftersaleApplyDTO updateApplyDTO = new AftersaleApplyDTO();
        updateApplyDTO.setAftersaleSn(aftersaleApplyDTO.getAftersaleSn());
        updateApplyDTO.setAuditResult(AfterSaleEnum.RESULT_PROCESS.value());
        updateApplyDTO.setAuditStatus(AfterSaleEnum.FINISH.value());
        aftersaleApplyService.update(updateApplyDTO);
        // 保存退货单或者换货单
        // 支付方式
        Map<String, Object> orderMap = new HashMap<>(1);
        orderMap.put("id", aftersaleApplyDTO.getOrderId());
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
        Map<String, Object> params = new HashMap<>(2);
        params.put("aftersaleSn", aftersaleApplyDTO.getAftersaleSn());
        if (aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPERETURN.value())) {
            List<AftersaleReturnDTO> list = aftersaleReturnService.list(params);
            if (CollectionUtils.isNotEmpty(list)) {
                throw new ServiceException(AftersaleStatusCode.AFTERSALE_ALREADY_CHECK);
            }
            AftersaleReturnSaveDTO aftersaleReturnSaveDTO = new AftersaleReturnSaveDTO();
            BeanCopier.create(AftersaleApplyDTO.class, AftersaleReturnSaveDTO.class, false).copy(aftersaleApplyDTO, aftersaleReturnSaveDTO, null);

            // 开始区分余额支付金额和第三方支付金额
            // 判断本次退款的金额，优先退余额支付的金额
            if (orderDTO.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {

                // 计算订单可退余额金额
                BigDecimal lastBalance = orderDTO.getBalanceAmount().subtract(orderDTO.getBalanceRefundAmount());

                // 还有可退余额
                if (lastBalance.compareTo(BigDecimal.ZERO) > 0) {
                    OrderDTO updateBalance = new OrderDTO();
                    updateBalance.setOrderSn(orderDTO.getOrderSn());
                    // 如果可退余额大于等于本次退款金额那么全部从余额的金额退款，不走第三方退款
                    if (lastBalance.compareTo(aftersaleApplyDTO.getRefundAmount()) > -1) {
                        aftersaleReturnSaveDTO.setBalanceRefundAmount(aftersaleApplyDTO.getRefundAmount());
                        aftersaleReturnSaveDTO.setRefundAmount(BigDecimal.ZERO);
                        updateBalance.setBalanceRefundAmount(aftersaleApplyDTO.getRefundAmount());
                    } else {
                        // 可退余额小于本次退款金额，则需要混合退款或者第三方退款
                        BigDecimal thirdRefund = aftersaleApplyDTO.getRefundAmount().subtract(lastBalance);
                        aftersaleReturnSaveDTO.setRefundAmount(thirdRefund);
                        aftersaleReturnSaveDTO.setBalanceRefundAmount(lastBalance);
                        updateBalance.setBalanceRefundAmount(lastBalance);
                    }
                    orderService.putOrderDTO(updateBalance);
                }

            }

            //TODO SWH 后期后面更换生成方式
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
            this.sendMessage(aftersaleApplyDTO.getAftersaleSn(), MessageCodeEnum.AFTER_AUDIT_YES.value(), true);
            //虚拟商品自动收货

            log.info("保存退货表成功");
            mlogger.info(AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_SAVE_RETURN_SUCCESS_CODE, AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_SAVE_RETURN_SUCCESS_MESSAGE, logMap);
            if (orderDTO.getVirtualFlag().equals(VirtualFlagEnum.YES.value())) {
                aftersaleReturnService.confirmGoods(aftersaleApplyDTO.getAftersaleSn(), AfterSaleStateEnum.AFTER_STATUS_RETURNIN.value());
            }
        } else if (aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPEBARTER.value())) {
            List<AftersaleBarterDTO> list = aftersaleBarterService.list(params);
            if (CollectionUtils.isNotEmpty(list)) {
                throw new ServiceException(AftersaleStatusCode.AFTERSALE_ALREADY_CHECK);
            }
            AftersaleBarterSaveDTO aftersaleBarterSaveDTO = new AftersaleBarterSaveDTO();
            BeanCopier.create(AftersaleApplyDTO.class, AftersaleBarterSaveDTO.class, false).copy(aftersaleApplyDTO, aftersaleBarterSaveDTO, null);
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
//        //更新平台审核记录

        if (orderDTO.getVirtualFlag().equals(VirtualFlagEnum.NO.value())) {
            // 保存日志
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            if (AfterSaleEnum.TYPERETURN.value().equals(aftersaleApplyDTO.getAftersaleType())) {
                aftersaleLogSaveDTO.setMessage("您的服务单退款申请业务已确认，请将商品寄回");
            } else {
                aftersaleLogSaveDTO.setMessage("您的服务单换货申请业务已确认，请将商品寄回");
            }
            aftersaleLogSaveDTO.setAftersaleSn(aftersaleApplyDTO.getAftersaleSn());
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_RETURNIN.value());
            aftersaleLogSaveDTO.setType(aftersaleApplyDTO.getAftersaleType());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_AUDIT.value());
            aftersaleLogSaveDTO.setCreator("平台运营");
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setUpdater("平台运营");
            // 发送mq消息异步保存售后日志记录
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
        }

        resultMap.put("code", ResultCodeEnum.SUCCESS.value());
        resultMap.put("msg", "审核成功");
        return resultMap;
    }

    /**
     * 封装发送短信参数并发送短信消息
     *
     * @param aftersaleSn: 售后单号
     * @param code:        消息类型
     * @param flag:        审核是否通过  true通过，false未通过
     * @date 2020/4/10 9:24
     * @author lixiangx@leimingtech.com
     **/
    private void sendMessage(Long aftersaleSn, String code, boolean flag) {
//        // 售后申请通过 发送短信

        Map<String, Object> map = new HashMap<>(16);

        AftersaleReturnDetailDTO detail = aftersaleReturnService.detail(aftersaleSn);
        // 获取用户信息
        MemberDTO memberDTO = memberService.getById(detail.getAftersaleApplyDTO().getMemberId());

        // 创建发送站内信用户信息集合
        Map<Long, String> memberMap = new HashMap<>(5);
        memberMap.put(detail.getAftersaleApplyDTO().getMemberId(), detail.getAftersaleApplyDTO().getMemberName());
        map.put("memberMap", memberMap);

        //设置用户手机号、站内信数据
        map.put("mobile", detail.getAftersaleApplyDTO().getContactsPhone());
        map.put("wechatOpenId", memberDTO.getWechatOpenid());

        // 创建短信所需参数Map
        Map<String, String> paramsMap = new HashMap<>(16);
        paramsMap.put("sellerName", detail.getAftersaleApplyDTO().getStoreName());
        paramsMap.put("orderSn", String.valueOf(detail.getAftersaleApplyDTO().getOrderSn()));
        paramsMap.put("cause", detail.getAftersaleApplyDTO().getAftersaleReason());
        paramsMap.put("aftersaleSn", String.valueOf(aftersaleSn));
        paramsMap.put("refundType", "1");
        map.put("paramMap", paramsMap);

        // 创建微信公众号推送信息所需参数Map
        // 创建微信公众号推送信息所需参数Map
        Map<String, String> wechatParamsMap = new HashMap<>(16);
        wechatParamsMap.put("first", flag ? "售后审核通过" : "售后审核未通过");
        wechatParamsMap.put("keyword1", String.valueOf(detail.getAftersaleApplyDTO().getOrderSn()));
        wechatParamsMap.put("keyword2", detail.getAftersaleGoodsDTOList().get(0).getGoodsName());
        wechatParamsMap.put("keyword3", detail.getAftersaleApplyDTO().getAftersaleReason());
        wechatParamsMap.put("keyword4", DateUtils.getDateStr(DateUtils.DATE_TIME_PATTERN));
        wechatParamsMap.put("remark", "感谢您使用我们的产品");

        map.put("wechatParamsJson", JSON.toJSONString(wechatParamsMap));
        map.put("wehcatClickUrl", AddressPrefixProperties.adddressPrefix + "#/pagesB/order/afterSaleDetail?aftersaleId=" + detail.getAftersaleApplyDTO().getAftersaleSn() + "%26aftersaleType=" + detail.getAftersaleApplyDTO().getAftersaleType());


        // 创建消息实体
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(code);
        messageDTO.setSendName(detail.getAftersaleApplyDTO().getStoreName());
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);

        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
    }

    /**
     * H5端售后详情
     *
     * @param params   :查询条件
     * @param memberId 用户ID
     * @return H5售后详情
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public AfterSaleDetailDTO findAftersaleDetail(@RequestParam Map<String, Object> params, @RequestParam("memberId") Long memberId) {
        AfterSaleDetailDTO afterSaleDetailDTO = new AfterSaleDetailDTO();
        // 查询售后申请表数据
        Long id = Long.valueOf(String.valueOf(params.get("aftersaleId")));
        Long aftersaleType = Long.valueOf(String.valueOf(params.get("aftersaleType")));
        QueryWrapper<AftersaleApplyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "aftersale_sn", id);
        wrapper.eq(memberId != null, "member_id", memberId);
        AftersaleApplyEntity aftersaleApplyEntity = baseDao.selectOne(wrapper);
        BeanCopier.create(AftersaleApplyEntity.class, AfterSaleDetailDTO.class, false).copy(aftersaleApplyEntity, afterSaleDetailDTO, null);
        Integer virtualFlag = VirtualFlagEnum.NO.value();
        OrderDTO orderDTO = Optional.ofNullable(orderService.get(aftersaleApplyEntity.getOrderId())).orElse(new OrderDTO());
        virtualFlag = orderDTO.getVirtualFlag();
        afterSaleDetailDTO.setVirtualFlag(virtualFlag);

        // 查询售后商品数量和图片
        Map<String, Object> goodsQueryMap = new HashMap<>(2);
        goodsQueryMap.put("id", id);
        goodsQueryMap.put("is_gift", 0L);
        AftersaleGoodsSaveDTO afterGoods = aftersaleGoodsService.getAfterGoods(goodsQueryMap);
        if (null != afterGoods) {
            afterSaleDetailDTO.setGoodsImage(afterGoods.getSpecMainPicture());
            afterSaleDetailDTO.setGoodsNum(afterGoods.getGoodsNum());
        }
        // 查询对应的审核消息
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        List<AftersaleAuditLogDTO> auditList = aftersaleAuditLogService.list(map);
        if (CollectionUtils.isNotEmpty(auditList)) {
            for (AftersaleAuditLogDTO aftersaleAuditLogDTO : auditList) {
                if (AfterSaleEnum.RESULT_PROCESS.value().equals(aftersaleAuditLogDTO.getResult()) || AfterSaleEnum.RESULT_REFUSE.value().equals(aftersaleAuditLogDTO.getResult())) {
                    if (AfterSaleEnum.SELLER_AUDIT.value().equals(aftersaleAuditLogDTO.getProcess().intValue())) {
                        // 商家审核
                        afterSaleDetailDTO.setSellerAudit(aftersaleAuditLogDTO.getReason());
                    }
                    if (AfterSaleEnum.ADMIN_AUDIT.value().equals(aftersaleAuditLogDTO.getProcess().intValue())) {
                        // 平台审核
                        afterSaleDetailDTO.setAdminAudit(aftersaleAuditLogDTO.getReason());
                    }
                }
            }
        }
        Map<String, Object> logMap = new HashMap<>(1);
        logMap.put("aftersaleSn", id);
        AfterSaleProcessDTO afterSaleProcessDTO = aftersaleLogService.findProcess(id, "ASC");
        if (null != afterSaleProcessDTO) {
            List<AftersaleLogDTO> logList = afterSaleProcessDTO.getLogList();
            List<AftersaleLogDetailDTO> detailDTOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(logList)) {
                logList.removeIf(logData -> logData.getProcess().equals(9));
                for (int i = 0; i < logList.size(); i++) {
                    AftersaleLogDetailDTO aftersaleLogDetailDTO = new AftersaleLogDetailDTO();
                    BeanCopier.create(AftersaleLogDTO.class, AftersaleLogDetailDTO.class, false).copy(logList.get(i), aftersaleLogDetailDTO, null);
                    if (i == logList.size() - 1) {
                        aftersaleLogDetailDTO.setCurrentState(AfterSaleEnum.DEFAULTONE.value());
                    }
                    if (AfterSaleStateEnum.LOG_START.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("提交申请");
                    } else if (AfterSaleStateEnum.LOG_AUDIT.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("申请审核中");
                        aftersaleLogDetailDTO.setCreateDate(null);
                    } else if (AfterSaleStateEnum.LOG_CANCEL.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("用户取消");
                    } else if (AfterSaleStateEnum.LOG_IN.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("售后收货");
                    } else if (AfterSaleStateEnum.LOG_BARTOUT.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("换货出库");
                        aftersaleLogDetailDTO.setCreateDate(null);
                    } else if (AfterSaleStateEnum.LOG_PAY.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("进行退款");
                        aftersaleLogDetailDTO.setCreateDate(null);
                    } else if (AfterSaleStateEnum.LOG_END.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("处理完成");
                    } else if (AfterSaleStateEnum.LOG_REFUASE.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("审核拒绝");
                    } else if (AfterSaleStateEnum.LOG_ARBITRATION.value().equals(logList.get(i).getProcess())) {
                        aftersaleLogDetailDTO.setProcessString("仲裁中");
                        aftersaleLogDetailDTO.setCreateDate(null);
                        UserDetailArbitrationDTO userDetailArbitrationDTO = arbitrationService.userArbitrationDetail(id);
                        if (!BeanUtil.isEmpty(userDetailArbitrationDTO)) {
                            afterSaleDetailDTO.setArbitrationDTO(userDetailArbitrationDTO);
//                            仲裁状态（1:审核通过、2:审核不通过、默认：3:待审核）
                            if (userDetailArbitrationDTO.getArbitrationStatus().equals(AfterSaleEnum.RESULT_REFUSE.value())) {
                                aftersaleLogDetailDTO.setProcessString("仲裁拒绝");
                                aftersaleLogDetailDTO.setCreateDate(userDetailArbitrationDTO.getArbitrationAuditDate());
                                aftersaleLogDetailDTO.setCurrentState(AfterSaleEnum.DEFAULTONE.value());
                            }
                            if (userDetailArbitrationDTO.getArbitrationStatus().equals(AfterSaleEnum.RESULT_PROCESS.value())) {
                                aftersaleLogDetailDTO.setProcessString("仲裁通过");
                                aftersaleLogDetailDTO.setCreateDate(userDetailArbitrationDTO.getArbitrationAuditDate());
                            }
                        }
                    }
                    detailDTOList.add(aftersaleLogDetailDTO);
                }
                // 处理如果状态是审核中单独增加日志
//                if (AfterSaleEnum.SELLER_AUDIT.value().equals(afterSaleDetailDTO.getAuditStatus())
//                        || AfterSaleEnum.ADMIN_AUDIT.value().equals(afterSaleDetailDTO.getAuditStatus())) {
//                    AftersaleLogDetailDTO aftersaleLogDetailDTO = new AftersaleLogDetailDTO();
//                    aftersaleLogDetailDTO.setProcessString("申请审核中");
//                    aftersaleLogDetailDTO.setCreateDate(null);
//                    detailDTOList.get(detailDTOList.size() - 1).setCurrentState(AfterSaleEnum.DEFAULT.value());
//                    aftersaleLogDetailDTO.setCurrentState(AfterSaleEnum.DEFAULTONE.value());
//                    detailDTOList.add(aftersaleLogDetailDTO);
//                }
                afterSaleDetailDTO.setDetailDTOList(detailDTOList);
            }
            afterSaleDetailDTO.setLastLog(afterSaleProcessDTO.getLastLog());
        }
        if (AfterSaleEnum.TYPERETURN.value().equals(aftersaleType.intValue())) {
            // 判断是退货,查询退货表状态
            AftersaleReturnDTO aftersaleReturnDTO = aftersaleReturnService.getDetail(id, memberId);
            if (null != aftersaleReturnDTO) {
                afterSaleDetailDTO.setAftersaleStatus(aftersaleReturnDTO.getAftersaleStatus());
                afterSaleDetailDTO.setLogisticsStatus(aftersaleReturnDTO.getLogisticsStatus());
                afterSaleDetailDTO.setLogisticsCompany(aftersaleReturnDTO.getLogisticsCompany());
                afterSaleDetailDTO.setLogisticsContactsPhone(aftersaleReturnDTO.getLogisticsContactsPhone());
                afterSaleDetailDTO.setLogisticsNumber(aftersaleReturnDTO.getLogisticsNumber());
            }

        } else if (AfterSaleEnum.TYPEBARTER.value().equals(aftersaleType.intValue())) {
            // 判断是换货,查询换货状态
            AftersaleBarterDTO aftersaleBarterDTO = aftersaleBarterService.getDetail(id, memberId);
            if (null != aftersaleBarterDTO) {
                afterSaleDetailDTO.setAftersaleStatus(aftersaleBarterDTO.getAftersaleStatus());
                afterSaleDetailDTO.setLogisticsStatus(aftersaleBarterDTO.getLogisticsStatus());
                afterSaleDetailDTO.setLogisticsCompany(aftersaleBarterDTO.getBuyerLogisticsCompany());
                afterSaleDetailDTO.setLogisticsNumber(aftersaleBarterDTO.getBuyerLogisticsNumber());
                afterSaleDetailDTO.setLogisticsContactsPhone(aftersaleBarterDTO.getBuyerLogisticsContactsPhone());
            }
        }
        return afterSaleDetailDTO;
    }

    /**
     * H5端售后买家上传物流信息
     *
     * @param aftersaleDeliveryDTO :保存实体
     * @param memberId             用户ID
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public Map<String, Object> saveDelivery(@RequestBody AftersaleDeliveryDTO aftersaleDeliveryDTO, @RequestParam("memberId") Long memberId) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("code", ResultCodeEnum.SUCCESS.value());
        // 判断aftersale_sn不能为空
        if (null == aftersaleDeliveryDTO.getAftersaleSn()) {
            result.put("code", ResultCodeEnum.WARN.value());
            result.put("msg", "售后单号不能为空");
            return result;
        }
        // 异步保存日志
        AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
        if (null != aftersaleDeliveryDTO.getAftersaleType() && AfterSaleEnum.TYPERETURN.value().equals(aftersaleDeliveryDTO.getAftersaleType())) {
            // 查询是否存在退货单
            AftersaleReturnDTO aftersaleReturnDTO = aftersaleReturnService.getDetail(aftersaleDeliveryDTO.getAftersaleSn(), memberId);
            if (null == aftersaleReturnDTO) {
                result.put("code", ResultCodeEnum.WARN.value());
                result.put("msg", "查询退货单服务异常，请稍后再试");
                return result;
            }
            if (StringUtils.isNotEmpty(aftersaleReturnDTO.getLogisticsNumber())) {
                result.put("code", ResultCodeEnum.WARN.value());
                result.put("msg", "已经上传物流，不可二次上传");
                return result;
            }
            AftersaleReturnDTO returnSaveDTO = new AftersaleReturnDTO();
            BeanCopier.create(AftersaleDeliveryDTO.class, AftersaleReturnDTO.class, false).copy(aftersaleDeliveryDTO, returnSaveDTO, null);
            returnSaveDTO.setId(aftersaleReturnDTO.getId());
            returnSaveDTO.setDeliveryNo(IdGenerator.defaultSnowflakeId());
            returnSaveDTO.setAftersaleStatus(AfterSaleStateEnum.AFTER_STATUS_RETURNIN.value());
            returnSaveDTO.setLogisticsStatus(AfterSaleStateEnum.LOGISTICS_STATUS_SELLERWAITIN.value());
            returnSaveDTO.setDeliveryTime(new Date());
            aftersaleReturnService.update(returnSaveDTO);
            aftersaleLogSaveDTO.setCreator(aftersaleReturnDTO.getMemberName());
            aftersaleLogSaveDTO.setUpdater(aftersaleReturnDTO.getMemberName());
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_RETURNIN.value());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setType(AfterSaleEnum.TYPERETURN.value());
            result.put("msg", "上传退货物流信息成功");
        } else if (null != aftersaleDeliveryDTO.getAftersaleType() && AfterSaleEnum.TYPEBARTER.value().equals(aftersaleDeliveryDTO.getAftersaleType())) {
            // 查询是否存在换货单
            AftersaleBarterDTO aftersaleBarterDTO = aftersaleBarterService.getDetail(aftersaleDeliveryDTO.getAftersaleSn(), memberId);
            if (null == aftersaleBarterDTO) {
                result.put("code", ResultCodeEnum.WARN.value());
                result.put("msg", "查询换货单服务异常，请稍后再试");
                return result;
            }
            if (StringUtils.isNotEmpty(aftersaleBarterDTO.getBuyerLogisticsContactsPhone())) {
                result.put("code", ResultCodeEnum.WARN.value());
                result.put("msg", "已经上传物流，不可二次上传");
                return result;
            }
            if (aftersaleBarterDTO.getLogisticsStatus().equals(AfterSaleStateEnum.LOGISTICS_STATUS_BUYERWAITIN.value())) {
                result.put("code", ResultCodeEnum.WARN.value());
                result.put("msg", "商家已收到您寄出的货物，请勿重复操作");
                return result;
            }
            AftersaleBarterDTO barterSaveDTO = new AftersaleBarterDTO();
            BeanCopier.create(AftersaleDeliveryDTO.class, AftersaleBarterDTO.class, false).copy(aftersaleDeliveryDTO, barterSaveDTO, null);
            barterSaveDTO.setId(aftersaleBarterDTO.getId());
            barterSaveDTO.setAftersaleStatus(AfterSaleStateEnum.AFTER_STATUS_BARTERIN.value());
            barterSaveDTO.setLogisticsStatus(AfterSaleStateEnum.LOGISTICS_STATUS_SELLERWAITIN.value());
            barterSaveDTO.setBuyerDeliveryTime(new Date());
            barterSaveDTO.setBuyerLogisticsCompany(aftersaleDeliveryDTO.getLogisticsCompany());
            barterSaveDTO.setBuyerLogisticsNumber(aftersaleDeliveryDTO.getLogisticsNumber());
            barterSaveDTO.setBuyerLogisticsContactsPhone(aftersaleDeliveryDTO.getLogisticsContactsPhone());
            aftersaleBarterService.update(barterSaveDTO);

            aftersaleLogSaveDTO.setCreator(aftersaleBarterDTO.getMemberName());
            aftersaleLogSaveDTO.setUpdater(aftersaleBarterDTO.getMemberName());
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_BARTERIN.value());
            aftersaleLogSaveDTO.setType(AfterSaleEnum.TYPEBARTER.value());
            result.put("msg", "上传物流换货信息成功");
        }
        aftersaleLogSaveDTO.setMessage("用户已发货");
        aftersaleLogSaveDTO.setAftersaleSn(aftersaleDeliveryDTO.getAftersaleSn());
        aftersaleLogSaveDTO.setCreator(SecurityUser.getUserName());
        aftersaleLogSaveDTO.setUpdater(SecurityUser.getUserName());
        // 发送mq消息异步保存售后日志记录
        String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);

        return result;
    }

    /**
     * H5端售后取消申请接口
     *
     * @param aftersaleSn :售后单号
     * @param memberId    用户ID
     * @return map 取消结果
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public Map<String, Object> cancelApply(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId) {
        Map<String, Object> result = new HashMap<>(3);
        AftersaleApplyDTO aftersaleApplyDTO = this.getDetail(aftersaleSn, memberId);
        if (null == aftersaleApplyDTO) {
            result.put("code", ResultCodeEnum.WARN.value());
            result.put("msg", "查询不到申请记录");
            return result;
        }
        log.info("进入取消售后单流程");

        Map<String, String> logMap = new HashMap<>(2);
        logMap.put("aftersaleSn", aftersaleSn.toString());
        logMap.put("memberId", memberId.toString());
        mlogger.info(AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_CANCEL_SUCCESS_CODE, AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_CANCEL_SUCCESS_MESSAGE, logMap);

        List<String> aftersaleList = new ArrayList<>();
        aftersaleList.add(aftersaleSn.toString());
        // 判断当前申请单状态是1:商家审核中,2:平台审核中才可以取消
        if (aftersaleApplyDTO.getAuditStatus().equals(AfterSaleEnum.SELLERAUDITING.value()) || aftersaleApplyDTO.getAuditStatus().equals(AfterSaleEnum.ADMINAUDITING.value())) {
            //todo 修改订单售后状态
            if (aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPERETURN.value())) {
                List<Long> orderIds = new ArrayList<>();
                orderIds.add(aftersaleApplyDTO.getOrderId());
                orderService.updateAfterFlag(orderIds, AfterSaleEnum.AFTER_APPLY_NO.value());
            }
            // 修改申请表状态
            AftersaleApplyEntity aftersaleApplyEntity = new AftersaleApplyEntity();
            aftersaleApplyEntity.setId(aftersaleApplyDTO.getId());
            aftersaleApplyEntity.setAftersaleSn(aftersaleApplyDTO.getAftersaleSn());
            aftersaleApplyEntity.setAuditStatus(AfterSaleEnum.CANCEL.value());
            aftersaleApplyEntity.setAuditResult(AfterSaleEnum.RESULT_CANCEL.value());
            this.updateById(aftersaleApplyEntity);
            // 修改审核信息状态为用户取消
            aftersaleAuditLogService.cancelAuditing(aftersaleSn);
            // 发送异步消息保存审核日志和售后日志
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setAftersaleSn(aftersaleApplyDTO.getAftersaleSn());
            aftersaleLogSaveDTO.setMessage("用户取消申请");
            aftersaleLogSaveDTO.setCreator(aftersaleApplyDTO.getMemberName());
            aftersaleLogSaveDTO.setUpdater(aftersaleApplyDTO.getMemberName());
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_CANCEL.value());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_CANCEL.value());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setType(aftersaleApplyDTO.getAftersaleType());
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
            result.put("code", ResultCodeEnum.SUCCESS.value());
            result.put("msg", "取消售后申请单成功");
            return result;
            //TODO SWH 二期功能：需要恢复ordergoods表中的可售后数量
        } else if (aftersaleApplyDTO.getAuditStatus().equals(AfterSaleEnum.FINISH.value())) {
            if (aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPERETURN.value())) {
                // 退货
                AftersaleReturnDTO detail = aftersaleReturnService.getDetail(aftersaleSn, memberId);
                if (StringUtils.isNotBlank(detail.getLogisticsContactsPhone())) {
                    result.put("code", ResultCodeEnum.WARN.value());
                    result.put("msg", "已提交物流信息不可取消");
                    return result;
                }
                List<Long> orderIds = new ArrayList<>();
                orderIds.add(aftersaleApplyDTO.getOrderId());
                orderService.updateAfterFlag(orderIds, AfterSaleEnum.AFTER_APPLY_NO.value());
                aftersaleReturnService.batchCancel(aftersaleList);
                result.put("code", ResultCodeEnum.SUCCESS.value());
                result.put("msg", "取消退货申请成功");
            } else if (aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPEBARTER.value())) {
                AftersaleBarterDTO detail = aftersaleBarterService.getDetail(aftersaleSn, memberId);
                if (StringUtils.isNotBlank(detail.getBuyerLogisticsContactsPhone())) {
                    result.put("code", ResultCodeEnum.WARN.value());
                    result.put("msg", "已提交物流信息不可取消");
                    return result;
                }
                aftersaleBarterService.batchCancel(aftersaleList);
                result.put("code", ResultCodeEnum.SUCCESS.value());
                result.put("msg", "取消换货申请成功");
            }
            // 修改申请表状态
            AftersaleApplyEntity aftersaleApplyEntity = new AftersaleApplyEntity();
            aftersaleApplyEntity.setId(aftersaleApplyDTO.getId());
            aftersaleApplyEntity.setAftersaleSn(aftersaleApplyDTO.getAftersaleSn());
            aftersaleApplyEntity.setAuditStatus(AfterSaleEnum.CANCEL.value());
            aftersaleApplyEntity.setAuditResult(AfterSaleEnum.RESULT_CANCEL.value());
            this.updateById(aftersaleApplyEntity);
            return result;
        } else {
            result.put("code", ResultCodeEnum.WARN.value());
            result.put("msg", "该申请单不能取消申请");
        }


        return result;
    }

    /**
     * H5端售后买家确认收货接口
     *
     * @param aftersaleSn :售后单号
     * @param memberId    用户ID
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */

    @Override
    public Map<String, Object> confirmReceipt(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId) {
        Map<String, Object> result = new HashMap<>(3);
        AftersaleApplyDTO aftersaleApplyDTO = this.getDetail(aftersaleSn, memberId);
        if (null == aftersaleApplyDTO) {
            result.put("code", ResultCodeEnum.WARN.value());
            result.put("msg", "确认收货查询不到申请记录");
            return result;
        }
        // 只有换货才有用户确认收货
        if (aftersaleApplyDTO.getAftersaleType().equals(AfterSaleEnum.TYPEBARTER.value())) {
            // 查询是否存在换货单
            AftersaleBarterDTO aftersaleBarterDTO = aftersaleBarterService.getDetail(aftersaleApplyDTO.getAftersaleSn(), memberId);
            if (null == aftersaleBarterDTO) {
                result.put("code", ResultCodeEnum.WARN.value());
                result.put("msg", "确认收货接口查询换货单服务异常，请稍后再试");
                return result;
            }
            AftersaleBarterDTO barterSaveDTO = new AftersaleBarterDTO();
            barterSaveDTO.setId(aftersaleBarterDTO.getId());
            barterSaveDTO.setAftersaleStatus(AfterSaleStateEnum.AFTER_STATUS_BARTERSUCESS.value());
            barterSaveDTO.setLogisticsStatus(AfterSaleStateEnum.LOGISTICS_STATUS_END.value());
            aftersaleBarterService.update(barterSaveDTO);
            // 异步保存售后日志
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setAftersaleSn(aftersaleApplyDTO.getAftersaleSn());
            aftersaleLogSaveDTO.setMessage("换货已签收");
            aftersaleLogSaveDTO.setCreator(aftersaleApplyDTO.getMemberName());
            aftersaleLogSaveDTO.setUpdater(aftersaleApplyDTO.getMemberName());
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_BARTERSUCESS.value());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_END.value());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            aftersaleLogSaveDTO.setType(aftersaleApplyDTO.getAftersaleType());
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
            //TODO SWH 二期功能：需要恢复ordergoods表中的可售后数量
            result.put("code", ResultCodeEnum.SUCCESS.value());
            result.put("msg", "买家确认收货成功");
        }
        return result;
    }

    /**
     * 保存售后申请记录
     *
     * @param dataList :售后申请实体
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public Map<String, Object> saveAfterSaleApply(@RequestBody List<AftersaleApplyFrontSaveDTO> dataList) {
        // 判断数据是否正常
        Map<String, Object> resultMap = new HashMap<>(3);
        if (CollectionUtils.isEmpty(dataList)) {
            resultMap.put("code", ResultCodeEnum.WARN.value());
            resultMap.put("msg", "传入集合数据为空");
        }
//        List<Long> orderIds = new ArrayList<Long>();
        Set<Long> orderIds = new HashSet<>();
        Boolean flag = true;
        for (AftersaleApplyFrontSaveDTO aftersaleApplyFrontSaveDTO : dataList) {
            if (aftersaleApplyFrontSaveDTO.getAftersaleType().equals(AfterSaleEnum.TYPERETURN.value())) {
                orderIds.add(aftersaleApplyFrontSaveDTO.getOrderId());
            }
            // 开始验证
            Map<String, Object> validateResult = this.validateApplyAfterSale(aftersaleApplyFrontSaveDTO);
            log.info("validateResult:{}", validateResult);
            if ((ResultCodeEnum.SUCCESS.value()) != (int) (validateResult.get("code"))) {
                resultMap.put("code", validateResult.get("code"));
                resultMap.put("msg", validateResult.get("msg"));
                return resultMap;
            }
            // 一个商品生成一个售后单
            for (int i = 1; i <= aftersaleApplyFrontSaveDTO.getApplyNum(); i++) {
                AftersaleApplySaveDTO aftersaleApplySaveDTO = new AftersaleApplySaveDTO();
                BeanCopier.create(AftersaleApplyFrontSaveDTO.class, AftersaleApplySaveDTO.class, false).copy(aftersaleApplyFrontSaveDTO, aftersaleApplySaveDTO, null);

                // 雪花算法生成id
                Long aftersaleSn = IdGenerator.defaultSnowflakeId();

                aftersaleApplySaveDTO.setAftersaleSn(aftersaleSn);
                aftersaleApplySaveDTO.setApplyNum(AfterSaleEnum.APPLYNUMDEFAULT.value());
                aftersaleApplySaveDTO.setAuditStatus(AfterSaleEnum.SELLERAUDITING.value());
                aftersaleApplySaveDTO.setAuditResult(AfterSaleEnum.RESULT_REVIEW.value());

                //todo  二期功能order_goods表需要添加商品和赠品之间的关联关系
                List<OrderGoodsDTO> orderGoodsList = new ArrayList<>();
                OrderGoodsDTO orderGoods = orderGoodsService.getById(aftersaleApplyFrontSaveDTO.getOrderGoodsId(), null, null);
                orderGoodsList.add(orderGoods);
                aftersaleApplySaveDTO.setStoreId(orderGoods.getStoreId());
                aftersaleApplySaveDTO.setStoreName(orderGoods.getStoreName());
                // 保存退款金额（以后有优惠则加分摊金额）
                // aftersaleApplyFrontSaveDTO.getApplyNum().equals(orderGoods.getGoodsNum())
                if (flag && orderGoods.getReturnPreferential().equals(OrderGoodsEnum.RETURN_PREFERENTIAL_NO.value()) && i == 1) {
                    aftersaleApplySaveDTO.setRefundAmount(orderGoods.getSpecPayPrice().multiply(new BigDecimal(1)).add(orderGoods.getAvgPreferentialAmount()));
                    flag = false;
                } else {
                    aftersaleApplySaveDTO.setRefundAmount(orderGoods.getSpecPayPrice().multiply(new BigDecimal(1)));
                }
                orderGoodsService.updateReturnPreferential(aftersaleApplyFrontSaveDTO.getOrderGoodsId(), OrderGoodsEnum.RETURN_PREFERENTIAL_YES.value());
                // 以下保存操作均不采用批量更新操作(如果保存失败手动回滚，将可售后数量恢复)
                Integer influenceNum = this.save(aftersaleApplySaveDTO);

                List<AftersaleGoodsSaveDTO> aftersaleGoodsSaveDTOList = new ArrayList<>();

                for (OrderGoodsDTO orderGoodsDTO : orderGoodsList) {
                    AftersaleGoodsSaveDTO aftersaleGoodsSaveDTO = new AftersaleGoodsSaveDTO();
                    BeanCopier.create(OrderGoodsDTO.class, AftersaleGoodsSaveDTO.class, false).copy(orderGoodsDTO, aftersaleGoodsSaveDTO, null);
                    aftersaleGoodsSaveDTO.setAftersaleSn(aftersaleSn);
                    aftersaleGoodsSaveDTO.setOrderGoodsId(orderGoodsDTO.getId());
                    aftersaleGoodsSaveDTO.setSpecName(orderGoodsDTO.getGoodsName());
                    aftersaleGoodsSaveDTO.setSpecAttrName(orderGoodsDTO.getSpecAttrValueName());
                    aftersaleGoodsSaveDTO.setSpecSellPrice(orderGoodsDTO.getSpecPrice());
                    aftersaleGoodsSaveDTO.setGoodsNum(AfterSaleEnum.DEFAULTONE.value());
                    aftersaleGoodsSaveDTO.setSpecSerial(orderGoodsDTO.getSpecSerial());
                    aftersaleGoodsSaveDTO.setSpecMainPicture(orderGoodsDTO.getGoodsImage());
                    aftersaleGoodsSaveDTOList.add(aftersaleGoodsSaveDTO);
                }

                // 保存售后商品表
                boolean saveAfterGoodsResult = aftersaleGoodsService.batchSave(aftersaleGoodsSaveDTOList);
                if (influenceNum.equals(AfterSaleEnum.DEFAULT.value()) || !saveAfterGoodsResult) {
                    // 手动回滚
//                    orderGoodsService.updateAftersaleQuantityById(aftersaleApplyFrontSaveDTO.getOrderGoodsId(), aftersaleApplyFrontSaveDTO.getApplyNum(), AfterSaleEnum.DEFAULT.value());
                    resultMap.put("code", ResultCodeEnum.WARN.value());
                    resultMap.put("msg", "保存售后申请异常，请稍后再试");
                    return resultMap;
                }
                AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
                aftersaleLogSaveDTO.setMessage("提交售后申请");
                aftersaleLogSaveDTO.setAftersaleSn(aftersaleSn);
                aftersaleLogSaveDTO.setStatus(AfterSaleEnum.NOAUDIT.value());
                aftersaleLogSaveDTO.setType(aftersaleApplySaveDTO.getAftersaleType());
                aftersaleLogSaveDTO.setProcess(AfterSaleEnum.DEFAULT.value());
                aftersaleLogSaveDTO.setCreateDate(new Date());
                aftersaleLogSaveDTO.setCreator(aftersaleApplySaveDTO.getMemberName());
                aftersaleLogSaveDTO.setUpdater(aftersaleApplySaveDTO.getMemberName());
                //发送mq消息异步保存售后日志记录
                String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);

//                // 保存售后审核表信息
                AftersaleAuditLogSaveDTO aftersaleAuditLogSaveDTO = new AftersaleAuditLogSaveDTO();
                aftersaleAuditLogSaveDTO.setId(IdGenerator.defaultSnowflakeId());
                aftersaleAuditLogSaveDTO.setAftersaleSn(aftersaleSn);
                aftersaleAuditLogSaveDTO.setProcess(Long.valueOf(AfterSaleEnum.SELLER_AUDIT.value()));
                aftersaleAuditLogSaveDTO.setResult(AfterSaleEnum.RESULT_REVIEW.value());
                aftersaleAuditLogSaveDTO.setAftersaleType(aftersaleApplySaveDTO.getAftersaleType());
                aftersaleAuditLogSaveDTO.setCreateDate(DateUtils.addDateSeconds(new Date(), 1));
                aftersaleAuditLogSaveDTO.setUpdateDate(null);
                aftersaleAuditLogService.save(aftersaleAuditLogSaveDTO);
            }
            // 更新订单项表,将对应的可售后数量减少
            orderGoodsService.updateAftersaleQuantityById(aftersaleApplyFrontSaveDTO.getOrderGoodsId(), aftersaleApplyFrontSaveDTO.getApplyNum(), AfterSaleEnum.DEFAULTONE.value());
        }
        List<Long> list = new ArrayList<>(orderIds);
        if (list.size() > 0) {
            orderService.updateAfterFlag(list, AfterSaleEnum.AFTER_APPLY_YES.value());
        }
        resultMap.put("code", ResultCodeEnum.SUCCESS.value());
        resultMap.put("msg", "申请成功");
        return resultMap;
    }

    /**
     * @param aftersaleApplyFrontSaveDTO:售后申请保存实体
     * @Author: SWH ab4856812@163.com
     * @Description:验明当前订单项是否可以申请售后
     * @Date: 2019/6/18 22:20
     * @Version: V1.0
     */
    private Map<String, Object> validateApplyAfterSale(AftersaleApplyFrontSaveDTO aftersaleApplyFrontSaveDTO) {

        Map<String, String> logMap = new HashMap<>(2);

        Map<String, Object> resultMap = new HashMap<>(3);
        // 此次申请的数量小于等于可申请售后数量
        Long id = aftersaleApplyFrontSaveDTO.getOrderGoodsId();
        OrderGoodsDTO orderGoodsDTO = orderGoodsService.getById(id, null, null);
        if (null != orderGoodsDTO && orderGoodsDTO.getAftersaleQuantity() >= aftersaleApplyFrontSaveDTO.getApplyNum()) {
            // 判断系统是否支持售后
            String queryRedisByName = settingService.queryRedisByName(SettingsEnum.AFTERSALE.value());
            SettingAftersaleDTO aftersaleDTO = JSON.parseObject(queryRedisByName, SettingAftersaleDTO.class);
            if (null == aftersaleDTO) {
                log.info("查询售后设置异常，请稍后再试");

                resultMap.put("code", ResultCodeEnum.WARN.value());
                resultMap.put("msg", "查询售后设置异常，请稍后再试");
                return resultMap;
            }

            // 获取setting表中的可申请售后时间
            String setting = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
            SettingSeniorDTO senior = JSON.parseObject(setting, SettingSeniorDTO.class);
            if (null == senior) {
                log.info("查询售后时间异常，请稍后再试");
                resultMap.put("code", ResultCodeEnum.WARN.value());
                resultMap.put("msg", "查询售后时间异常，请稍后再试");
                return resultMap;
            }
            String finalDateStr = DateUtils.format(new Date());
            String completeTime = orderService.findCompleteTimeById(aftersaleApplyFrontSaveDTO.getOrderId());
            if (StringUtils.isEmpty(completeTime)) {
                log.info("查询订单完成时间异常，请稍后再试");

                resultMap.put("code", ResultCodeEnum.WARN.value());
                resultMap.put("msg", "查询订单完成时间异常，请稍后再试");
                return resultMap;
            }
            completeTime = completeTime.substring(0, completeTime.length() - 2);
            Date completeDate = DateUtils.stringToDate(completeTime, DateUtils.DATE_TIME_PATTERN);
            Date nowDate = DateUtils.stringToDate(finalDateStr, DateUtils.DATE_PATTERN);
            if ((AfterSaleEnum.TYPERETURN.value()).equals(aftersaleApplyFrontSaveDTO.getAftersaleType())) {
                String cannotReturn = senior.getCannotReturn();
                if ((aftersaleDTO.getGoodsReturn()).equals(AfterSettingEnum.CANNOTRETURN.valueString())) {
                    // 不支持售后
                    resultMap.put("code", ResultCodeEnum.WARN.value());
                    resultMap.put("msg", "不支持退货");
                    return resultMap;
                }
                Date finalReturnDate = DateUtils.addDateDays(completeDate, Integer.valueOf(cannotReturn));
                if (finalReturnDate.before(nowDate)) {
                    // 可申请退货截至时间小于当前时间，不允许退货，需设置原因
                    resultMap.put("code", ResultCodeEnum.WARN.value());
                    resultMap.put("msg", "已超过规定的退货时间");
                    return resultMap;
                }
                resultMap.put("code", ResultCodeEnum.SUCCESS.value());
                resultMap.put("msg", "验证通过");
                return resultMap;
            } else if ((AfterSaleEnum.TYPEBARTER.value()).equals(aftersaleApplyFrontSaveDTO.getAftersaleType())) {
                String cannotBarter = senior.getCannotBarter();
                if ((aftersaleDTO.getGoodsBarter()).equals(AfterSettingEnum.CANNOTRETURN.valueString())) {
                    // 不支持售后
                    resultMap.put("code", ResultCodeEnum.WARN.value());
                    resultMap.put("msg", "不支持换货");
                    return resultMap;
                }
                Date finalBarterDate = DateUtils.addDateDays(completeDate, Integer.valueOf(cannotBarter));
                if (finalBarterDate.before(nowDate)) {
                    resultMap.put("code", ResultCodeEnum.WARN.value());
                    resultMap.put("msg", "已超过规定的换货时间");
                    return resultMap;
                }
                resultMap.put("code", ResultCodeEnum.SUCCESS.value());
                resultMap.put("msg", "验证通过");
                return resultMap;
            }
        } else {
            if (null != orderGoodsDTO && null != orderGoodsDTO.getGoodsNum()) {
                Integer goodsNum = orderGoodsDTO.getGoodsNum();
                log.info("此商品{}申请数量超过可售后数量", goodsNum);
                logMap.put("goodsNum", goodsNum.toString());
                mlogger.info(AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_GOODS_COUNT_SUCCESS_CODE, AftersaleStatusCode.SERVICE_AFTERSALE_APPLY_GOODS_COUNT_SUCCESS_MESSAGE, logMap);

                resultMap.put("code", ResultCodeEnum.WARN.value());
                resultMap.put("msg", "此商品申请数量+" + goodsNum + "超过可售后数量");
                return resultMap;
            } else {
                resultMap.put("code", ResultCodeEnum.WARN.value());
                resultMap.put("msg", "查询售后商品数量异常，请稍后再试");
                return resultMap;
            }
        }
        return resultMap;
    }

    /**
     * 查询申请记录
     *
     * @param aftersaleType 售后类型
     * @param agreeReturn   售后设置信息
     * @param auditStatus   审核人
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */
    @Override

    public List<AftersaleApplyDTO> findAutoApplyList(@RequestParam("aftersaleType") Integer aftersaleType, @RequestParam("agreeReturn") String agreeReturn, @RequestParam("auditStatus") Integer auditStatus) {
        return baseDao.findAutoApplyList(aftersaleType, agreeReturn, auditStatus);
    }

    /**
     * 查询退款订单
     *
     * @param storeId   店铺ID
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 退款订单列表
     * @author xuzhch
     * @date 2020年09月21日
     */

    @Override
    public List<ReturnBillDTO> findBillOrderList(@RequestParam("storeId") Long storeId,
                                                 @RequestParam(value = "startDate", required = false) String startDate,
                                                 @RequestParam("endDate") String endDate) {

        return baseDao.findBillOrderList(storeId, startDate, endDate);
    }

    /**
     * 查询待审核订单
     *
     * @param storeId 店铺ID
     * @return 待审核订单数量
     * @author xuzhch
     * @date 2020年09月21日
     */

    @Override
    public Integer findAfterAuditCount(@RequestParam("storeId") Long storeId) {
        return baseDao.findAfterAuditCount(storeId);
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
