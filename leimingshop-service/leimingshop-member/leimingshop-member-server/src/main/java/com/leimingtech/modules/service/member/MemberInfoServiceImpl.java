/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.dao.member.MemberInfoDao;
import com.leimingtech.modules.dto.balance.BatchChangeBalanceDTO;
import com.leimingtech.modules.dto.balance.MemberBalanceLogDTO;
import com.leimingtech.modules.dto.balance.MemberChangeBalanceDTO;
import com.leimingtech.modules.dto.member.*;
import com.leimingtech.modules.entity.member.MemberInfoEntity;
import com.leimingtech.modules.enums.balance.BalanceApplyEnum;
import com.leimingtech.modules.enums.balance.BalanceEnum;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.modules.service.balance.MemberBalanceLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员详细信息表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Service

@Slf4j
public class MemberInfoServiceImpl extends CrudServiceImpl<MemberInfoDao, MemberInfoEntity, MemberInfoDTO> implements MemberInfoService {

    @Autowired
    private MemberBalanceLogService memberBalanceLogService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public QueryWrapper<MemberInfoEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<MemberInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据用户id修改用户详情
     *
     * @param memberUpdateDTO
     */
    @Override

    public void updateByMemberId(@RequestBody MemberUpdateDTO memberUpdateDTO) {

        baseDao.updateByMemberId(memberUpdateDTO);
    }

    /**
     * 根据id查询用户详情
     *
     * @param id
     * @return
     */
    @Override

    public MemberInfoDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 用户详情保存
     *
     * @param memberInfoDTO
     */
    @Override

    public void save(@RequestBody MemberInfoDTO memberInfoDTO) {
        super.save(memberInfoDTO);
    }

    /**
     * 修改用户详情
     *
     * @param memberInfoDTO
     */
    @Override

    public void update(@RequestBody MemberInfoDTO memberInfoDTO) {
        super.update(memberInfoDTO);
    }

    /**
     * 逻辑删除
     *
     * @param ids
     */
    @Override

    public void logicDelete(@RequestBody Long[] ids) {
        super.logicDelete(ids);
    }

    /**
     * 根据等级积分查询相应人数
     *
     * @param min 最小
     * @param max 最大
     * @return
     */

    @Override
    public Long selectMemberNum(@RequestParam("min") Integer min, @RequestParam("max") Integer max) {
        return baseDao.selectMemberNum(min, max);
    }


    /**
     * 修改会员成长值
     *
     * @param memberId:   会员ID
     * @param gradePoint: 会员等级积分（成长值）
     * @date 2019/12/26 10:34
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public void updateGradePoint(@RequestParam("memberId") Long memberId,
                                 @RequestParam("gradePoint") Integer gradePoint) {
        baseDao.updateGradePoint(memberId, gradePoint);
    }

    /**
     * 查询用户余额信息
     *
     * @param memberId 用户ID
     * @return MemberBalanceInfoDTO 用户余额信息
     * @date 2020年7月7日15:57:25
     * @author xuzhch@leimingtech.com
     **/

    @Override
    public MemberBalanceInfoDTO getMemberBalanceInfo(Long memberId) {
        MemberInfoEntity memberInfoEntity = baseDao.selectOne(Wrappers.<MemberInfoEntity>lambdaQuery()
                .eq(MemberInfoEntity::getMemberId, memberId)
                .eq(MemberInfoEntity::getDelFlag, DelFlagEnum.NORMAL.value()));

        MemberBalanceInfoDTO memberBalanceInfoDTO = ConvertUtils.sourceToTarget(memberInfoEntity, MemberBalanceInfoDTO.class);
        if (StringUtils.isNotBlank(memberInfoEntity.getPaymentPasswd())) {
            memberBalanceInfoDTO.setPayPwdFlag(MemberEnum.PWDFLAG_YES.value());
        } else {
            memberBalanceInfoDTO.setPayPwdFlag(MemberEnum.PWDFLAG_NO.value());
        }

        // 从redis中获取是否申请提现
        String applyMoneyKey = RedisKeys.getApplyKey(memberId);
        Object object = redisUtils.get(applyMoneyKey);
        if (object == null) {
            memberBalanceInfoDTO.setApplyFlag(BalanceApplyEnum.UNAPPLYED.value());
        } else {
            memberBalanceInfoDTO.setApplyFlag(BalanceApplyEnum.APPLYED.value());
        }

        // 从redis中获取是否绑定银行卡
        String bankKey = RedisKeys.getBankKey(memberId);
        Object redisObject = redisUtils.get(bankKey);
        if (redisObject == null) {
            memberBalanceInfoDTO.setBindFlag(BalanceApplyEnum.UNAPPLYED.value());
        } else {
            memberBalanceInfoDTO.setBindFlag(BalanceApplyEnum.APPLYED.value());
        }

        return memberBalanceInfoDTO;
    }

    /**
     * 查询用户余额信息
     *
     * @param params 用户密码信息
     * @date 2020年7月7日15:57:25
     * @author xuzhch@leimingtech.com
     **/

    @Override
    public void updatePayPassword(@RequestParam Map<String, Object> params) {
        String oldPwd = params.get("oldPwd").toString();
        String newPwd = params.get("newPwd").toString();
        String confirmPwd = params.get("confirmPwd").toString();
        Long memberId = Long.valueOf(params.get("memberId").toString());
        MemberInfoEntity memberInfoEntity = baseDao.selectOne(Wrappers.<MemberInfoEntity>lambdaQuery()
                .eq(MemberInfoEntity::getMemberId, memberId)
                .eq(MemberInfoEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        if (BeanUtil.isEmpty(memberInfoEntity)) {
            throw new ServiceException(MemberStatusCode.PASSWORD_ANALYZE_FAIL);
        }
        // 使用RSA私钥进行解密
        try {
            if (StringUtils.isNotBlank(oldPwd)) {
                oldPwd = oldPwd.replace(" ", "+");
                oldPwd = (RSACoder.decryptByPrivateKey(oldPwd));
            }
            newPwd = newPwd.replace(" ", "+");
            newPwd = (RSACoder.decryptByPrivateKey(newPwd));
            confirmPwd = confirmPwd.replace(" ", "+");
            confirmPwd = (RSACoder.decryptByPrivateKey(confirmPwd));
        } catch (Exception e) {
            log.error("入参:{},密码解析出现异常:{}", JSON.toJSONString(params), e);
            throw new ServiceException(MemberStatusCode.PASSWORD_ANALYZE_FAIL);
        }
        if (StringUtils.isNotBlank(memberInfoEntity.getPaymentPasswd())) {
            // 原密码不为空校验原密码是否正确
            if (StringUtils.isBlank(oldPwd)) {
                //旧密码不能为空
                throw new ServiceException(MemberStatusCode.OLD_PAY_PASSWORD_NOT_NULL);
            }
            if (!PasswordUtils.matches(oldPwd, memberInfoEntity.getPaymentPasswd())) {
                //原密码错误
                throw new ServiceException(MemberStatusCode.OLD_PAY_PASSWORD_INPUT_ERROR);
            }
        }
        //新密码对比
        if (!newPwd.equals(confirmPwd)) {
            throw new ServiceException(MemberStatusCode.PASSWORD_AND_CONFRIMPWD_MISMATCH);
        }
        if (!newPwd.matches(RegexConstant.PAY_PASSWORD_REGEX)) {
            throw new ServiceException(MemberStatusCode.PASSWORD_FORMAT_MUST_NUMBER);
        }
        //新密码加密
        String encodeNewPasswd = PasswordUtils.encode(newPwd);
        //修改密码
        boolean b = super.update(Wrappers.<MemberInfoEntity>lambdaUpdate()
                .set(MemberInfoEntity::getPaymentPasswd, encodeNewPasswd)
                .eq(MemberInfoEntity::getMemberId, memberId)
                .eq(MemberInfoEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        if (!b) {
            throw new ServiceException(MemberStatusCode.PASSWORD_UPDATE_FAIL);
        }
    }


    @Override
    public MemberInfoDTO selectInfoBymemberId(Long memberId) {

        MemberInfoEntity memberInfoEntity = baseDao.selectOne(Wrappers.<MemberInfoEntity>lambdaQuery()
                .eq(MemberInfoEntity::getMemberId, memberId)
                .eq(MemberInfoEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        return ConvertUtils.sourceToTarget(memberInfoEntity, MemberInfoDTO.class);
    }


    @Override
    public MemberPayPasswordInfoDTO checkPayPassword(@RequestParam Map<String, Object> params) {
        MemberPayPasswordInfoDTO memberPayPasswordInfoDTO = new MemberPayPasswordInfoDTO();
        Long memberId = Long.valueOf(params.get("memberId").toString());
        String payPassword = params.get("payPassword").toString();
        MemberInfoEntity memberInfoEntity = baseDao.selectOne(Wrappers.<MemberInfoEntity>lambdaQuery()
                .eq(MemberInfoEntity::getMemberId, memberId)
                .eq(MemberInfoEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        if (BeanUtil.isEmpty(memberInfoEntity) || StringUtils.isBlank(memberInfoEntity.getPaymentPasswd())) {
            memberPayPasswordInfoDTO.setPayPasswordResult(String.valueOf(ErrorCode.PASSWORD_ERROR));
        }
        try {
            payPassword = payPassword.replace(" ", "+");
            payPassword = (RSACoder.decryptByPrivateKey(payPassword));
        } catch (Exception e) {
            log.error("支付密码校验解析失败");
            throw new ServiceException(MemberStatusCode.PAY_PASSWORD_CHECK_FAIL);
        }
        if (PasswordUtils.matches(payPassword, memberInfoEntity.getPaymentPasswd())) {
            memberPayPasswordInfoDTO.setPayPasswordResult(String.valueOf(ErrorCode.SUCCESS));
        } else {
            memberPayPasswordInfoDTO.setPayPasswordResult(String.valueOf(ErrorCode.PASSWORD_ERROR));
        }

        // 获取用户余额信息
        MemberBalanceInfoDTO memberBalanceInfoDTO = this.getMemberBalanceInfo(memberId);
        memberPayPasswordInfoDTO.setMemberBalanceInfoDTO(memberBalanceInfoDTO);
        return memberPayPasswordInfoDTO;
    }


    @Override
    public List<MemberInfoDTO> selectList(@RequestBody List<Long> memberIds) {
        List<MemberInfoEntity> memberInfoEntities = baseDao.selectList(Wrappers.<MemberInfoEntity>lambdaQuery()
                .in(MemberInfoEntity::getMemberId, memberIds)
                .eq(MemberInfoEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        return Optional.ofNullable(ConvertUtils.sourceToTarget(memberInfoEntities, MemberInfoDTO.class)).orElse(new ArrayList<MemberInfoDTO>());
    }


    @Override
    public void changeMemberBalance(@RequestBody MemberChangeBalanceDTO memberChangeBalanceDTO) {
        Long memberId = memberChangeBalanceDTO.getMemberId();
        BigDecimal changeBalance = memberChangeBalanceDTO.getChangeBalance();
        MemberInfoDTO memberInfoDTO = this.selectInfoBymemberId(memberId);
        if (memberInfoDTO.getAvailableBalance().add(memberChangeBalanceDTO.getChangeBalance())
                .compareTo(new BigDecimal(BigInteger.ZERO)) == -1) {
            throw new ServiceException(MemberStatusCode.BALANCE_CANNOT_LESS_THAN_ZERO);
        }
        BigDecimal beforeBalance = memberInfoDTO.getAvailableBalance();
        //修改用户余额
        MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO();
        if (memberChangeBalanceDTO.getBalanceType() == 3 || memberChangeBalanceDTO.getBalanceType() == 4 || memberChangeBalanceDTO.getBalanceType() == 5) {
            memberUpdateDTO.setAvailableWithdrawal(changeBalance);
        }
        memberUpdateDTO.setAvailableBalance(changeBalance);
        memberUpdateDTO.setId(memberId);
        baseDao.updateByMemberId(memberUpdateDTO);
        //保存余额变更记录
        MemberBalanceLogDTO memberBalanceLogDTO = new MemberBalanceLogDTO();
        memberBalanceLogDTO.setBalanceType(memberChangeBalanceDTO.getBalanceType());
        memberBalanceLogDTO.setMemberId(memberId);
        memberBalanceLogDTO.setMemberName(memberChangeBalanceDTO.getMemberName());
        memberBalanceLogDTO.setBeforeBalance(beforeBalance);
        memberBalanceLogDTO.setChangeBalance(changeBalance);
        memberBalanceLogDTO.setCurrentBalance(beforeBalance.add(changeBalance));
        memberBalanceLogDTO.setRemark(memberChangeBalanceDTO.getRemark());
        memberBalanceLogService.save(memberBalanceLogDTO);
    }


    @Override
    public void batchChangeBalance(@RequestBody BatchChangeBalanceDTO batchChangeBalanceDTO) {
        List<MemberNameDTO> memberNameDTOList = batchChangeBalanceDTO.getMemberNameDTOList();
        if (CollectionUtils.isEmpty(memberNameDTOList)) {
            throw new ServiceException(MemberStatusCode.MEMBER_NOT_EXIST);
        }

        BigDecimal changeBalance = batchChangeBalanceDTO.getChangeBalance();
        //获取用户余额信息
        List<Long> memberIds = memberNameDTOList.stream().map(MemberNameDTO::getMemberId).collect(Collectors.toList());
        Map<Long, String> memberNames = memberNameDTOList.stream().collect(Collectors.toMap(MemberNameDTO::getMemberId, MemberNameDTO::getMemberName));
        List<MemberInfoDTO> memberInfoDTOList = this.selectList(memberIds);
        List<MemberInfoEntity> infoEntities = new ArrayList<>();
        List<MemberBalanceLogDTO> balanceLogDTOList = new ArrayList<>();
        Date date = new Date();
        //封装数据
        for (MemberInfoDTO memberInfoDTO : memberInfoDTOList) {
            MemberInfoEntity memberInfo = new MemberInfoEntity();
            MemberBalanceLogDTO memberBalanceLogDTO = new MemberBalanceLogDTO();
            //余额变动类型（0为增加或减少金额，1为调整后金额）
            if (batchChangeBalanceDTO.getChangeType() == 1) {
                memberInfo.setAvailableBalance(changeBalance);
                memberBalanceLogDTO.setCurrentBalance(changeBalance);
                memberBalanceLogDTO.setChangeBalance(BigDecimal.ZERO.subtract(memberInfoDTO.getAvailableBalance()).add(changeBalance));
            } else if (batchChangeBalanceDTO.getChangeType() == 0) {
                if (memberInfoDTO.getAvailableBalance().add(changeBalance)
                        .compareTo(new BigDecimal(BigInteger.ZERO)) == -1) {
                    throw new ServiceException(MemberStatusCode.BALANCE_CANNOT_LESS_THAN_ZERO);
                }
                memberInfo.setAvailableBalance(memberInfoDTO.getAvailableBalance().add(changeBalance));
                memberBalanceLogDTO.setCurrentBalance(memberInfoDTO.getAvailableBalance().add(changeBalance));
                memberBalanceLogDTO.setChangeBalance(changeBalance);
            } else {
                continue;
            }
            memberInfo.setId(memberInfoDTO.getId());
            infoEntities.add(memberInfo);
            memberBalanceLogDTO.setMemberName(memberNames.get(memberInfoDTO.getMemberId()));
            memberBalanceLogDTO.setMemberId(memberInfoDTO.getMemberId());
            memberBalanceLogDTO.setBeforeBalance(memberInfoDTO.getAvailableBalance());
            memberBalanceLogDTO.setRemark(batchChangeBalanceDTO.getRemark());
            memberBalanceLogDTO.setCreateDate(date);
            memberBalanceLogDTO.setBalanceType(BalanceEnum.ADMIN_CHANGE.value());
            balanceLogDTOList.add(memberBalanceLogDTO);
        }

        super.updateBatchById(infoEntities);
        memberBalanceLogService.updateBatch(balanceLogDTOList);
    }


    @Override
    public void updateMemberBaseInfo(@RequestBody MemberUpdateDTO memberUpdateDTO) {
        super.update(Wrappers.<MemberInfoEntity>lambdaUpdate()
                .set(MemberInfoEntity::getMemberAreainfo, memberUpdateDTO.getMemberAreainfo())
                .set(MemberInfoEntity::getMemberProvinceid, memberUpdateDTO.getMemberProvinceid())
                .set(MemberInfoEntity::getMemberCityid, memberUpdateDTO.getMemberCityid())
                .set(MemberInfoEntity::getMemberAreaid, memberUpdateDTO.getMemberAreaid())
                .set(MemberInfoEntity::getStressId, memberUpdateDTO.getStressId())
        );
    }

}
