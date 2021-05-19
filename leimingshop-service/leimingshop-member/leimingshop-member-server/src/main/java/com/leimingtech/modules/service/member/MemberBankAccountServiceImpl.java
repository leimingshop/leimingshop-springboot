/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.dao.member.MemberBankAccountDao;
import com.leimingtech.modules.dto.member.MemberBankAccountDTO;
import com.leimingtech.modules.entity.member.MemberBankAccountEntity;
import com.leimingtech.modules.service.alipay.AliPayService;
import com.leimingtech.modules.service.payment.WithdrawalApplicationLogService;
import com.leimingtech.modules.service.payment.WithdrawalApplicationService;
import com.leimingtech.service.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 用户银行账户信息
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-21
 */
@Service
@Transactional

public class MemberBankAccountServiceImpl extends BaseServiceImpl<MemberBankAccountDao, MemberBankAccountEntity> implements MemberBankAccountService {

    @Autowired
    AliPayService aliPayService;

    @Autowired
    SettingService settingService;

    @Autowired
    WithdrawalApplicationService withdrawalApplicationService;

    @Autowired
    WithdrawalApplicationLogService withdrawalApplicationLogService;

    @Autowired
    RedisUtils redisUtils;

    @Override

    public PageData<MemberBankAccountDTO> page(@RequestParam Map<String, Object> params) {
        IPage<MemberBankAccountEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, MemberBankAccountDTO.class);
    }

    @Override

    public List<MemberBankAccountDTO> list(Map<String, Object> params) {
        List<MemberBankAccountEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, MemberBankAccountDTO.class);
    }

    private QueryWrapper<MemberBankAccountEntity> getWrapper(Map<String, Object> params) {
        Object id = params.get("id");
        Object memberId = params.get("memberId");

        QueryWrapper<MemberBankAccountEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(null != id, "id", id);
        wrapper.eq(null != memberId, "member_id", memberId);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public MemberBankAccountDTO get(Long id) {
        MemberBankAccountEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, MemberBankAccountDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody MemberBankAccountDTO dto) {
        MemberBankAccountEntity entity = ConvertUtils.sourceToTarget(dto, MemberBankAccountEntity.class);

        insert(entity);

        // 查询redis中是否存在
        String bankKey = RedisKeys.getBankKey(entity.getMemberId());
        Object object = redisUtils.get(bankKey);
        if (object == null) {
            // 获取当前时间，计算当前时间到0点的时间差为redis的过期时间
            Long expire = this.getSecondsTobeforedawn();
            redisUtils.set(bankKey, MemberRedisConstants.APPLY_MONEY, expire);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody MemberBankAccountDTO dto) {
        MemberBankAccountEntity entity = ConvertUtils.sourceToTarget(dto, MemberBankAccountEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, MemberBankAccountEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 根据银行卡号查询银行名称
     *
     * @param bankAccount 银行卡号
     * @return
     * @date 2020-10-21 16:21
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public String getBankName(String bankAccount) {
        String bankName = "";
        JSONObject baninfo = aliPayService.getBankName(bankAccount);
        if (null == baninfo) {
            bankName = "查询为空";
            return bankName;
        }
        String validated = baninfo.get("validated").toString();
        if (validated.equals("false")) {
            bankName = "查询为空";
            return bankName;
        }
        String bankCode = baninfo.get("bank").toString();

        // 查询银行名称列表
        String data = settingService.queryRedisByName(SettingsEnum.BANK_NAME_LIST.value());
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("bank_list");
        for (Object obj : jsonArray) {
            JSONObject dataObj = JSONObject.parseObject(obj.toString());
            if (bankCode.equals(dataObj.get("value"))) {
                bankName = dataObj.get("text").toString();
                break;
            }
        }
        return bankName;

    }

    /**
     * 查询用户已添加银行卡列表
     *
     * @param params
     * @return
     * @date 2020-10-22 10:25
     * @author huangkeyuan@leimingtech.com
     **/

    @Override
    public List<MemberBankAccountDTO> bankList(@RequestParam Map<String, Object> params) {
        List<MemberBankAccountEntity> entityList = baseDao.selectList(getWrapper(params));

        List<MemberBankAccountDTO> bankList = ConvertUtils.sourceToTarget(entityList, MemberBankAccountDTO.class);
        // 银行卡号加密
        bankList.forEach(memberBankAccountDTO -> {
            String bankAccount = memberBankAccountDTO.getBankAccount();
            int length = bankAccount.length();
            if (length > 4) {
                String endNum = bankAccount.substring(length - 4, length);
                bankAccount = "**** **** ****" + endNum;
            }
            memberBankAccountDTO.setBankAccount(bankAccount);
        });

        return bankList;
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
        // 改成这样就好了
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

}
