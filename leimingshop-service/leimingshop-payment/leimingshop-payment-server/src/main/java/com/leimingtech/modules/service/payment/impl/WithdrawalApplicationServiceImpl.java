/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.dao.payment.WithdrawalApplicationDao;
import com.leimingtech.modules.dto.balance.MemberBalanceLogDTO;
import com.leimingtech.modules.dto.member.MemberBalanceInfoDTO;
import com.leimingtech.modules.dto.member.MemberBankAccountDTO;
import com.leimingtech.modules.dto.member.MemberUpdateDTO;
import com.leimingtech.modules.dto.payment.WithdrawalApplicationDTO;
import com.leimingtech.modules.dto.payment.WithdrawalApplicationLogDTO;
import com.leimingtech.modules.dto.payment.WithdrawalAuditDTO;
import com.leimingtech.modules.dto.payment.WithdrawalDetailDTO;
import com.leimingtech.modules.entity.payment.WithdrawalApplicationEntity;
import com.leimingtech.modules.enums.balance.BalanceEnum;
import com.leimingtech.modules.enums.payment.WithdrawalLogEnum;
import com.leimingtech.modules.service.balance.MemberBalanceLogService;
import com.leimingtech.modules.service.member.MemberBankAccountService;
import com.leimingtech.modules.service.member.MemberInfoService;
import com.leimingtech.modules.service.payment.WithdrawalApplicationLogService;
import com.leimingtech.modules.service.payment.WithdrawalApplicationService;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

/**
 * 提现申请
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Service
@Transactional

@Slf4j
public class WithdrawalApplicationServiceImpl extends BaseServiceImpl<WithdrawalApplicationDao, WithdrawalApplicationEntity> implements WithdrawalApplicationService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(PaymentServiceImpl.class);

    @Autowired
    WithdrawalApplicationLogService withdrawalApplicationLogService;

    @Autowired
    MemberInfoService memberInfoService;

    @Autowired
    MemberBalanceLogService memberBalanceLogService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    MemberBankAccountService memberBankAccountService;

    @Override

    public PageData<WithdrawalApplicationDTO> page(@RequestParam Map<String, Object> params) {
        IPage<WithdrawalApplicationEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, WithdrawalApplicationDTO.class);
    }

    @Override

    public List<WithdrawalApplicationDTO> list(Map<String, Object> params) {
        List<WithdrawalApplicationEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, WithdrawalApplicationDTO.class);
    }

    private QueryWrapper<WithdrawalApplicationEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        //会员名称
        String memberName = (String) params.get("memberName");
        //收款人姓名
        String bankPeople = (String) params.get("bankPeople");
        //提现申请进度
        String auditStatus = (String) params.get("auditStatus");
        //申请开始时间
        String startDate = (String) params.get("startDate");
        //申请结束时间
        String endDate = (String) params.get("endDate");
        //用户id
        String memberId = (String) params.get("memberId");

        QueryWrapper<WithdrawalApplicationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.and(StringUtils.isNotBlank(memberName), queryWrapper -> queryWrapper.like("buyer_name", memberName).or().like("member_id", memberName));
        wrapper.like(StringUtils.isNotBlank(bankPeople), "bank_people", bankPeople);
        if (null == auditStatus || StringUtils.isBlank(auditStatus)) {
            wrapper.in("audit_status", WithdrawalLogEnum.SUBMIT.value(), WithdrawalLogEnum.CANCEL.value(), WithdrawalLogEnum.REFUSE_AUDITING.value(), WithdrawalLogEnum.PASS_ISSUEING.value());
        } else {
            wrapper.eq(StringUtils.isNotBlank(auditStatus), "audit_status", auditStatus);
        }
        wrapper.ge(StringUtils.isNotBlank(startDate), "create_date", startDate + " 00:00:00");
        wrapper.le(StringUtils.isNotBlank(endDate), "create_date", endDate + " 23:59:59");
        wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);
        return wrapper;
    }

    @Override

    public WithdrawalApplicationDTO get(Long id) {
        WithdrawalApplicationEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, WithdrawalApplicationDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody WithdrawalApplicationDTO dto) {
        WithdrawalApplicationEntity entity = ConvertUtils.sourceToTarget(dto, WithdrawalApplicationEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody WithdrawalApplicationDTO dto) {
        WithdrawalApplicationEntity entity = ConvertUtils.sourceToTarget(dto, WithdrawalApplicationEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, WithdrawalApplicationEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 提现申请审核
     *
     * @param dto
     * @return
     * @date 2020-10-20 16:25
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void audit(@RequestBody WithdrawalAuditDTO dto) {
        List<Long> applyIds = dto.getIds();
        if (CollectionUtils.isNotEmpty(applyIds)) {
            applyIds.forEach(applyId -> {
                WithdrawalApplicationDTO withdrawalApplicationDTO = this.get(applyId);
                if (null == withdrawalApplicationDTO) {
                    log.info("提现申请不存在：id:{}", applyId);
                    mlogger.info(PaymentStatusCode.APPLY_NOTEXITC_FAIL.getCode(),
                            PaymentStatusCode.APPLY_NOTEXITC_FAIL.getMessage());
                    throw new ServiceException(PaymentStatusCode.APPLY_NOTEXITC_FAIL);
                }

                WithdrawalApplicationEntity entity = ConvertUtils.sourceToTarget(dto, WithdrawalApplicationEntity.class);
                entity.setId(applyId);

                if (dto.getAuditStatus() == WithdrawalLogEnum.REFUSE_AUDITING.value()) {
                    // 审核驳回，取消冻结金额
                    this.changeBalance(withdrawalApplicationDTO.getWithdrawalAmount(), withdrawalApplicationDTO.getMemberId());
                }

                //添加日志更新
                WithdrawalApplicationLogDTO withdrawalApplicationLogDTO = new WithdrawalApplicationLogDTO();

                updateById(entity);

                withdrawalApplicationLogDTO.setApplyId(applyId);
                withdrawalApplicationLogDTO.setMemberId(withdrawalApplicationDTO.getMemberId());
                withdrawalApplicationLogDTO.setAuditStatus(dto.getAuditStatus());
                withdrawalApplicationLogService.save(withdrawalApplicationLogDTO);
            });
        }
    }

    /**
     * 提现发放审核列表
     *
     * @param params
     * @return
     * @date 2020-10-20 17:56
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public PageData<WithdrawalApplicationDTO> issuePage(@RequestParam Map<String, Object> params) {
        IPage<WithdrawalApplicationEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getIssueWrapper(params)
        );

        return getPageData(page, WithdrawalApplicationDTO.class);
    }

    private QueryWrapper<WithdrawalApplicationEntity> getIssueWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        //会员名称
        String memberName = (String) params.get("memberName");
        //收款人姓名
        String bankPeople = (String) params.get("bankPeople");
        //申请开始时间
        String startDate = (String) params.get("startDate");
        //申请结束时间
        String endDate = (String) params.get("endDate");
        //提现发放申请进度
        String issueStatus = (String) params.get("issueStatus");

        QueryWrapper<WithdrawalApplicationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        // 查询提现发放分页时，分页审核状态值不能为空
        if (StringUtils.isBlank(issueStatus)) {
            wrapper.in("audit_status", WithdrawalLogEnum.PASS_ISSUEING.value(), WithdrawalLogEnum.REFUSE_ISSUE.value(), WithdrawalLogEnum.FINISH.value());
        } else {
            wrapper.eq("audit_status", issueStatus);
        }
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.and(StringUtils.isNotBlank(memberName), queryWrapper -> queryWrapper.like("buyer_name", memberName).or().like("member_id", memberName));
        wrapper.like(StringUtils.isNotBlank(bankPeople), "bank_people", bankPeople);
        wrapper.ge(StringUtils.isNotBlank(startDate), "create_date", startDate + " 00:00:00");
        wrapper.le(StringUtils.isNotBlank(endDate), "create_date", endDate + " 23:59:59");

        return wrapper;
    }

    /**
     * 提现发放审核
     *
     * @param dto
     * @return
     * @date 2020-10-20 16:25
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public void issue(@RequestBody WithdrawalAuditDTO dto) {

        List<Long> applyIds = dto.getIds();
        if (CollectionUtils.isNotEmpty(applyIds)) {
            applyIds.forEach(applyId -> {
                WithdrawalApplicationDTO withdrawalApplicationDTO = this.get(applyId);
                if (null == withdrawalApplicationDTO) {
                    log.info("提现申请不存在：id:{}", applyId);
                    mlogger.info(PaymentStatusCode.APPLY_NOTEXITC_FAIL.getCode(),
                            PaymentStatusCode.APPLY_NOTEXITC_FAIL.getMessage());
                    throw new ServiceException(PaymentStatusCode.APPLY_NOTEXITC_FAIL);
                }

                WithdrawalApplicationEntity entity = ConvertUtils.sourceToTarget(dto, WithdrawalApplicationEntity.class);

                //添加日志更新
                WithdrawalApplicationLogDTO withdrawalApplicationLogDTO = new WithdrawalApplicationLogDTO();

                // 如果审核申请状态为已审核通过，那么提现发放审核状态为审核中
                if (dto.getAuditStatus() == WithdrawalLogEnum.FINISH.value()) {
                    withdrawalApplicationLogDTO.setAuditStatus(WithdrawalLogEnum.FINISH.value());
                    // 发放的时候，设置支付时间
                    entity.setPayTime(new Date());

                    // 完成提现修改冻结金额
                    this.releaseBlockedBalance(withdrawalApplicationDTO);

                } else {
                    withdrawalApplicationLogDTO.setAuditStatus(WithdrawalLogEnum.REFUSE_ISSUE.value());
                    // 审核驳回，取消冻结金额
                    this.changeBalance(withdrawalApplicationDTO.getWithdrawalAmount(), withdrawalApplicationDTO.getMemberId());
                }
                entity.setId(applyId);
                updateById(entity);

                withdrawalApplicationLogDTO.setApplyId(applyId);
                withdrawalApplicationLogDTO.setAuditStatus(dto.getAuditStatus());
                withdrawalApplicationLogDTO.setMemberId(withdrawalApplicationDTO.getMemberId());
                withdrawalApplicationLogService.save(withdrawalApplicationLogDTO);
            });
        }

    }

    /**
     * 提现成功，释放用户的冻结金额
     *
     * @param withdrawalApplicationDTO
     * @return
     * @date 2020-10-28 17:18
     * @author huangkeyuan@leimingtech.com
     **/
    private void releaseBlockedBalance(WithdrawalApplicationDTO withdrawalApplicationDTO) {


        // 查询当前的用户余额
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(withdrawalApplicationDTO.getMemberId());

        // 更新memberinfo冻结金额和用户余额明细
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        memberUpdateDTO.setBlockedBalance(withdrawalApplicationDTO.getWithdrawalAmount().negate());
        memberUpdateDTO.setId(withdrawalApplicationDTO.getMemberId());
        // 将用户冻结余额释放
        memberInfoService.updateByMemberId(memberUpdateDTO);

        // 更新用户余额明细
        MemberBalanceLogDTO memberBalanceLogDTO = new MemberBalanceLogDTO();
        memberBalanceLogDTO.setBalanceType(BalanceEnum.MEMBER_WITHDRAW.value());
        memberBalanceLogDTO.setBeforeBalance(memberBalanceInfoDTO.getAvailableBalance().add(withdrawalApplicationDTO.getWithdrawalAmount()));
        memberBalanceLogDTO.setChangeBalance(withdrawalApplicationDTO.getWithdrawalAmount().negate());
        memberBalanceLogDTO.setCurrentBalance(memberBalanceInfoDTO.getAvailableBalance());
        memberBalanceLogDTO.setMemberId(withdrawalApplicationDTO.getMemberId());
        memberBalanceLogDTO.setMemberName(withdrawalApplicationDTO.getBuyerName());
        memberBalanceLogService.save(memberBalanceLogDTO);
        log.info("更新用户余额明细成功，memberBalanceLogDTO：{}", memberBalanceLogDTO);

    }


    /**
     * 提现记录列表
     *
     * @param params
     * @return
     * @date 2020-10-20 17:56
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public PageData<WithdrawalApplicationDTO> recordPage(@RequestParam Map<String, Object> params) {
        IPage<WithdrawalApplicationEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getRecordWrapper(params)
        );

        return getPageData(page, WithdrawalApplicationDTO.class);
    }

    private QueryWrapper<WithdrawalApplicationEntity> getRecordWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        //会员名称
        String memberName = (String) params.get("memberName");
        //收款人姓名
        String bankPeople = (String) params.get("bankPeople");
        //收款账号
        String bankAccount = (String) params.get("bankAccount");
        //申请开始时间
        String startDate = (String) params.get("startDate");
        //申请结束时间
        String endDate = (String) params.get("endDate");
        //提现发放申请进度
        String issueStatus = (String) params.get("issueStatus");

        QueryWrapper<WithdrawalApplicationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        if (StringUtils.isBlank(issueStatus)) {
            wrapper.in("audit_status", WithdrawalLogEnum.REFUSE_AUDITING.value(), WithdrawalLogEnum.FINISH.value());
        } else {
            wrapper.eq("audit_status", issueStatus);
        }
        wrapper.eq(StringUtils.isNotBlank(bankAccount), "bank_account", bankAccount);
        wrapper.and(StringUtils.isNotBlank(memberName), queryWrapper -> queryWrapper.like("buyer_name", memberName).or().like("member_id", memberName));
        wrapper.like(StringUtils.isNotBlank(bankPeople), "bank_people", bankPeople);
        wrapper.ge(StringUtils.isNotBlank(startDate), "pay_time", startDate + " 00:00:00");
        wrapper.le(StringUtils.isNotBlank(endDate), "pay_time", endDate + " 23:59:59");

        return wrapper;
    }

    /**
     * 获取用户提现明细列表
     *
     * @param params
     * @return
     * @date 2020-10-22 16:38
     * @author huangkeyuan@leimingtech.com
     **/
    @Override

    public PageData<WithdrawalApplicationDTO> memberList(@RequestParam Map<String, Object> params) {
        IPage<WithdrawalApplicationEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getMemberWrapper(params)
        );

        return getPageData(page, WithdrawalApplicationDTO.class);
    }

    private QueryWrapper<WithdrawalApplicationEntity> getMemberWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        //用户id
        String memberId = (String) params.get("memberId");

        QueryWrapper<WithdrawalApplicationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);
        return wrapper;
    }

    /**
     * 根据申请id和用户id查询申请详情
     *
     * @param params
     * @return
     * @date 2020-10-22 17:01
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public WithdrawalDetailDTO detail(@RequestParam Map<String, Object> params) {
        Object memberId = params.get("memberId");
        Object applyId = params.get("applyId");
//        params.put("id", applyId);

        WithdrawalApplicationEntity withdrawalApplicationEntity = baseDao.selectOne(Wrappers.<WithdrawalApplicationEntity>lambdaQuery()
                .eq(WithdrawalApplicationEntity::getMemberId, memberId)
                .eq(WithdrawalApplicationEntity::getId, applyId)
                .eq(WithdrawalApplicationEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        WithdrawalDetailDTO withdrawalDetailDTO = ConvertUtils.sourceToTarget(withdrawalApplicationEntity, WithdrawalDetailDTO.class);

        // 查询提现记录日志
        List<WithdrawalApplicationLogDTO> list = withdrawalApplicationLogService.list(params);
        if (null != list && list.size() > 0) {
            withdrawalDetailDTO.setWithdrawalApplicationLogDTOList(list);
        }
        return withdrawalDetailDTO;

    }

    /**
     * 取消提现申请
     *
     * @param params
     * @return
     * @date 2020-10-22 17:32
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public String cancel(@RequestParam Map<String, Object> params) {
        Object memberId = params.get("memberId");
        Object applyId = params.get("applyId");

        WithdrawalApplicationEntity withdrawalApplicationEntity = baseDao.selectOne(Wrappers.<WithdrawalApplicationEntity>lambdaQuery()
                .eq(WithdrawalApplicationEntity::getMemberId, memberId)
                .eq(WithdrawalApplicationEntity::getId, applyId)
                .eq(WithdrawalApplicationEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        if (withdrawalApplicationEntity.getAuditStatus() != WithdrawalLogEnum.SUBMIT.value()) {
            return "fail";
        }

        withdrawalApplicationEntity.setAuditStatus(WithdrawalLogEnum.CANCEL.value());
        updateById(withdrawalApplicationEntity);

        this.changeBalance(withdrawalApplicationEntity.getWithdrawalAmount(), withdrawalApplicationEntity.getMemberId());

        // 添加提现日志
        WithdrawalApplicationLogDTO withdrawalApplicationLogDTO = new WithdrawalApplicationLogDTO();
        withdrawalApplicationLogDTO.setAuditStatus(WithdrawalLogEnum.CANCEL.value());
        withdrawalApplicationLogDTO.setApplyId(withdrawalApplicationEntity.getId());
        withdrawalApplicationLogDTO.setMemberId(withdrawalApplicationEntity.getMemberId());
        withdrawalApplicationLogService.save(withdrawalApplicationLogDTO);
        return "success";
    }

    /**
     * 增加用户余额，增加用户可提现余额，减少用户冻结金额
     *
     * @param amount 金额
     * @return
     * @date 2020-10-28 17:51
     * @author huangkeyuan@leimingtech.com
     **/
    private void changeBalance(BigDecimal amount, Long memberId) {
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        memberUpdateDTO.setId(memberId);
        memberUpdateDTO.setAvailableBalance(amount);
        memberUpdateDTO.setAvailableWithdrawal(amount);
        memberUpdateDTO.setBlockedBalance(amount.negate());
        memberInfoService.updateByMemberId(memberUpdateDTO);
    }

    /**
     * 提现申请
     *
     * @param params
     * @return
     * @date 2020-10-22 10:53
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public Map<String, Object> withdrawalApply(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>(3);
        // 判断提现余额是否大于当前可提现余额
        Object amountObject = params.get("withdrawalAmount");
        BigDecimal withdrawalAmount = new BigDecimal(amountObject.toString());
        Object memberObject = params.get("memberId");
        MemberBalanceInfoDTO memberBalanceInfoDTO = memberInfoService.getMemberBalanceInfo(Long.valueOf(String.valueOf(memberObject)));
        if (null == memberBalanceInfoDTO) {
            result.put("code", ErrorCode.BAD_REQUEST);
            result.put("msg", "用户不存在");
            return result;
        }

        BigDecimal memberWithdrawal = memberBalanceInfoDTO.getAvailableWithdrawal();

        if (withdrawalAmount.compareTo(memberWithdrawal) > 0) {
            result.put("code", ErrorCode.BAD_REQUEST);
            result.put("msg", "申请金额大于可提现金额");
            return result;
        }

        // 减去申请金额，且申请金额添加到冻结金额中

        // 减少用户余额，减少用户可提现余额，增加用户冻结金额
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        memberUpdateDTO.setAvailableWithdrawal(withdrawalAmount.negate());
        memberUpdateDTO.setId(memberBalanceInfoDTO.getMemberId());
        memberUpdateDTO.setBlockedBalance(withdrawalAmount);
        memberUpdateDTO.setAvailableBalance(withdrawalAmount.negate());
        memberInfoService.updateByMemberId(memberUpdateDTO);

        MemberBankAccountDTO memberBankAccountDTO = memberBankAccountService.get(Long.valueOf(params.get("bankId").toString()));

        // 新增提现申请记录
        WithdrawalApplicationDTO withdrawalApplicationDTO = new WithdrawalApplicationDTO();
        withdrawalApplicationDTO.setId(IdGenerator.defaultSnowflakeId());
        withdrawalApplicationDTO.setMemberId(memberBalanceInfoDTO.getMemberId());
        withdrawalApplicationDTO.setBuyerName(params.get("memberName").toString());
        withdrawalApplicationDTO.setWithdrawalAmount(withdrawalAmount);
        withdrawalApplicationDTO.setBankAccount(memberBankAccountDTO.getBankAccount());
        withdrawalApplicationDTO.setBankName(memberBankAccountDTO.getBankName());
        withdrawalApplicationDTO.setBankPeople(memberBankAccountDTO.getBankPeople());
        withdrawalApplicationDTO.setBankPhone(memberBankAccountDTO.getBankPhone());
        withdrawalApplicationDTO.setAuditStatus(WithdrawalLogEnum.SUBMIT.value());
        this.save(withdrawalApplicationDTO);

        // 新增提现申请记录日志
        WithdrawalApplicationLogDTO withdrawalApplicationLogDTO = new WithdrawalApplicationLogDTO();
        withdrawalApplicationLogDTO.setMemberId(memberBalanceInfoDTO.getMemberId());
        withdrawalApplicationLogDTO.setAuditStatus(WithdrawalLogEnum.SUBMIT.value());
        withdrawalApplicationLogDTO.setApplyId(withdrawalApplicationDTO.getId());
        withdrawalApplicationLogDTO.setId(IdGenerator.defaultSnowflakeId());
        withdrawalApplicationLogService.save(withdrawalApplicationLogDTO);

        // 查询redis中是否存在
        String applyMoneyKey = RedisKeys.getApplyKey(memberBalanceInfoDTO.getMemberId());
        Object object = redisUtils.get(applyMoneyKey);
        if (object == null) {
            // 获取当前时间，计算当前时间到0点的时间差为redis的过期时间
            Long expire = this.getSecondsTobeforedawn();
            redisUtils.set(applyMoneyKey, MemberRedisConstants.APPLY_MONEY, expire);
        }

        result.put("code", ErrorCode.SUCCESS);
        result.put("msg", "申请成功");
        return result;
    }

    /**
     * 计算当前时间到0点的时间差并返回秒
     *
     * @param
     * @return
     * @date 2020-10-26 23:04
     * @author huangkeyuan@leimingtech.com
     **/
    private Long getSecondsTobeforedawn() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

}
