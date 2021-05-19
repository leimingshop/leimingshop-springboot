/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.constant.RegexConstant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DataMaskingUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.dto.setting.reward.RewardDTO;
import com.leimingtech.dto.setting.reward.RewardSettingDTO;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.EmailEnum;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.constant.MemberRedisConstants;
import com.leimingtech.modules.dao.member.MemberDao;
import com.leimingtech.modules.dao.member.MemberInfoDao;
import com.leimingtech.modules.dto.WxOauth2ResDTO;
import com.leimingtech.modules.dto.WxUserDto;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.dto.coupons.FrontMyCouponsPageDTO;
import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.dto.grade.MemberGradeSaveDTO;
import com.leimingtech.modules.dto.log.point.PointLogDTO;
import com.leimingtech.modules.dto.log.point.PointLogPackDTO;
import com.leimingtech.modules.dto.member.*;
import com.leimingtech.modules.dto.order.LastestOrderLogisticsDTO;
import com.leimingtech.modules.dto.order.MemberOrderCountDTO;
import com.leimingtech.modules.entity.member.MemberEntity;
import com.leimingtech.modules.entity.member.MemberInfoEntity;
import com.leimingtech.modules.enums.ResultEnum;
import com.leimingtech.modules.enums.member.LoginEnum;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.WechatMpService;
import com.leimingtech.modules.service.address.MemberAddressService;
import com.leimingtech.modules.service.browse.GoodsBrowseService;
import com.leimingtech.modules.service.coupons.CouponsActivitySearchService;
import com.leimingtech.modules.service.favorites.FavoritesService;
import com.leimingtech.modules.service.grade.MemberGradeService;
import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderLogisticsLogService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.utils.PasswdUtil;
import com.leimingtech.modules.utils.member.MemberUtil;
import com.leimingtech.modules.vo.member.MemberVo;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.message.MessageReceiverService;
import com.leimingtech.service.message.MessageTextService;
import com.leimingtech.service.setting.PointSettingService;
import com.leimingtech.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 会员表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Slf4j
@Service

public class MemberServiceImpl extends BaseServiceImpl<MemberDao, MemberEntity> implements MemberService {

    private static MonitorLogger mlogger =
            MonitorLoggerFactory.getMonitorLogger(MemberServiceImpl.class);
    private final String MEMBER_NAME = "0";
    private final String MEMBER_EMAIL = "2";
    private final String NICK_NAME = "3";
    private final String PARAMS_NAME_REPEAT = "repeat";
    private final String PARAMS_NAME_CODE = "code";
    private final String PARAMS_NAME_MEMBER_ID = "memberId";
    private final String EMAIL_ALREADY_BIND = "1";
    private final int INIT_GROWTH_VALUE = 0;


    @Autowired
    private MemberInfoDao memberInfoDao;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired

    private MemberGradeService memberGradeService;
    @Autowired

    private MemberInfoService memberInfoService;
    @Autowired

    private MemberAddressService memberAddressService;

    @Autowired
    private MessageReceiverService messageReceiverService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsBrowseService goodsBrowseService;
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private OrderLogisticsLogService orderLogisticsLogService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private MessageTextService messageTextService;
    @Autowired
    private PointSettingService pointSettingService;
    @Autowired
    private PointLogService pointLogService;
    @Autowired
    private CouponsActivitySearchService couponsActivitySearchService;
    @Autowired
    private SysMessageService sysMessageService;
    @Autowired
    private WechatMpService wechatMpService;

    @Autowired
    private UploadService uploadService;


    /**
     * 分页查询会员管理列表
     *
     * @param params
     * @return DY 2019.5.10
     */
    @Override

    public PageData<MemberListDTO> page(@RequestParam Map<String, Object> params) {
        String gradeId = (String) params.get("gradeId");
        if (StringUtils.isNotBlank(gradeId)) {
            MemberGradeSaveDTO gradeSaveDTO = memberGradeService.getMember(Long.valueOf(gradeId));
            params.put("min", gradeSaveDTO.getIntegration());
            params.put("max", gradeSaveDTO.getMaxIntegration());
        }

        //分页
        IPage<MemberEntity> page = getPage(params, "m.create_date", false);
        //查询
        List<MemberVo> list = baseDao.getPage(page, params);
        List<MemberListDTO> memberVoDTOList = new ArrayList<>();
        for (MemberVo memberVo : list) {
            MemberListDTO memberListDTO = ConvertUtils.sourceToTarget(memberVo, MemberListDTO.class);
            MemberGradeDTO memberGradeDTO = memberGradeService.selectByMemberId(memberVo.getMemberInfoEntity().getGradePoint());
            if (!BeanUtil.isEmpty(memberVo.getMemberInfoEntity())) {
                memberListDTO.setAvailableBalance(null != memberVo.getMemberInfoEntity().getAvailableBalance()
                        ? memberVo.getMemberInfoEntity().getAvailableBalance() : BigDecimal.ZERO);
            }
            if (memberGradeDTO == null) {
                memberListDTO.setGradeName("暂无等级");
            } else {
                memberListDTO.setGradeName(memberGradeDTO.getGradeName());
            }
            memberVoDTOList.add(memberListDTO);
        }
        return getPageData(memberVoDTOList, page.getTotal(), MemberListDTO.class);
    }

    /**
     * 功能描述:
     * <导出查询>
     *
     * @param params
     * @date 2020/1/13 21:03
     * @author weixianchun
     **/

    @Override
    public PageData<EasyMemberExcelDTO> export(@RequestParam Map<String, Object> params) {
//分页参数
        Long curPage = 1L;
        Long limit = 100L;
        if (params.get(Constant.PAGE) != null && !"".equals(params.get(Constant.PAGE))) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null && !"".equals(params.get(Constant.LIMIT))) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }
        //分页
        Page<EasyMemberExcelDTO> page = new Page<>(curPage, limit);
        List<EasyMemberExcelDTO> list = baseDao.findListExport(page, params);
        return new PageData(list, page.getTotal());
    }

    /**
     * 功能描述:
     * 〈站内信列表会员查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<MemberDTO> pageMessage(@RequestParam Map<String, Object> params) {
        //查询
        String type = (String) params.get("type");
        String memberName = (String) params.get("memberName");
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(type)) {
//            查询类型 默认会员:0,邮箱:2,姓名:3
            if (MEMBER_NAME.equals(type)) {
                queryWrapper.like(StringUtils.isNotBlank(memberName), "member_name", memberName);
            } else if (MEMBER_EMAIL.equals(type)) {
                queryWrapper.like(StringUtils.isNotBlank(memberName), "member_email", memberName);
            } else if (NICK_NAME.equals(type)) {
                queryWrapper.like(StringUtils.isNotBlank(memberName), "nick_name", memberName);
            }
        }
        IPage<MemberEntity> page = baseDao.selectPage(getPage(params, "create_date", false), queryWrapper);

        return getPageData(page, MemberDTO.class);

    }

    /**
     * 根据id查询当前用户信息
     *
     * @param id
     * @return DY 2019.5.10
     */
    @Override

    public MemberVoDTO selectMemberById(Long id) {

        //todo 逻辑待改
        MemberVo memberVo = baseDao.selectMemberById(id);
        MemberVoDTO memberVoDTO = ConvertUtils.sourceToTarget(memberVo, MemberVoDTO.class);
        if (null != memberVo) {
            MemberInfoDTO memberInfoDTO = ConvertUtils.sourceToTarget(memberVo.getMemberInfoEntity(), MemberInfoDTO.class);
            memberVoDTO.setMemberInfoDTO(memberInfoDTO);
            if (null != memberInfoDTO.getGradePoint()) {
                MemberGradeDTO memberGradeDTO = memberGradeService.selectByMemberId(memberVo.getMemberInfoEntity().getGradePoint());
                memberInfoDTO.setGradeName(memberGradeDTO.getGradeName() == null ? "" : memberGradeDTO.getGradeName());
            }
        }
        return memberVoDTO;
    }

    /**
     * 根据id查询会员信息
     *
     * @param id
     * @return
     */
    @Override

    public MemberDTO getById(Long id) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return ConvertUtils.sourceToTarget(baseDao.selectOne(queryWrapper), MemberDTO.class);
    }

    /**
     * 功能描述:
     * 〈获得会员密码〉
     *
     * @param id 会员id
     * @author : 刘远杰
     */
    @Override

    public MemberDTO getMemberPassword(Long id) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "member_passwd").eq("id", id);
        return ConvertUtils.sourceToTarget(baseDao.selectOne(queryWrapper), MemberDTO.class);
    }


    /**
     * 根据username查询认证用户信息
     *
     * @param username
     * @return
     */
    @Override

    public SecurityDTO selectSecurityUserInfo(@RequestParam("username") String username) {
        SecurityDTO securityDTO = baseDao.selectSecurityUserInfo(username);
        if (securityDTO != null) {
            //clientid为本应用使用，不应该放在用户表里
            securityDTO.setClientId("client");
        }
        return securityDTO;
    }

    /**
     * 根据username查询当前用户信息
     *
     * @param username 用户名
     * @return
     */

    @Override
    public MemberDTO selectMemberByUserName(@RequestParam("username") String username) {
        MemberEntity memberEntity = baseDao.selectMemberByUserName(username);
        return ConvertUtils.sourceToTarget(memberEntity, MemberDTO.class);
    }


    /**
     * 查询列表
     *
     * @param params 查询条件
     * @return list 会员列表信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public List<MemberVoDTO> list(@RequestParam Map<String, Object> params) {
        List<MemberVo> entityList = baseDao.getList(params);
        return ConvertUtils.sourceToTarget(entityList, MemberVoDTO.class);
    }

    /**
     * 保存用户信息
     *
     * @param dto 会员注册信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public void saveMember(@RequestBody MemberRegisterDTO dto) {
        //转换
        MemberEntity memberEntity = ConvertUtils.sourceToTarget(dto, MemberEntity.class);
        MemberInfoEntity memberInfoEntity = ConvertUtils.sourceToTarget(dto, MemberInfoEntity.class);

        //memberEntity数据填充
        MemberUtil.saveMemberFill(memberEntity);

        int success = baseDao.insert(memberEntity);

        if (success == 1) {

            //memberInfoEntity数据填充
            //关联id
            memberInfoEntity.setMemberId(memberEntity.getId());
            //todo 成功注册等级积分,消费积分
            memberInfoDao.insert(memberInfoEntity);

            Map<String, Object> params = new HashMap<>(5);
            params.put("memberId", memberEntity.getId());
            params.put("memberName", memberEntity.getMemberName());
            params.put("sourceType", MemberPointLogEnum.WELCOME_SOURCE_TYPE.code());
            params.put("pointDesc", MemberPointLogEnum.WELCOME_SOURCE_TYPE.value());
            this.savePoint(params);
        }


    }

    /**
     * 查询用户手机号是否已经注册
     *
     * @param memberMobile 用户手机号
     * @return boolean 查询结果
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public Boolean selectMemberByUsermaneOrMobile(@RequestParam("memberMobile") String memberMobile) {

        return baseDao.selectMemberByUsermaneOrMobile(memberMobile);
    }

    /**
     * 邮箱是否已经注册
     *
     * @param memberEmail 用户手机邮箱
     * @return boolean 查询结果
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public Boolean selectMemberByMemberEmail(@RequestParam("memberEmail") String memberEmail) {
        return baseDao.selectMemberByMemberEmail(memberEmail);
    }


    /**
     * 用户注册/保存
     *
     * @param params
     */
    @Override

    public Map<String, Object> saveMemberLongin(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>(4);
        MemberEntity memberEntity = new MemberEntity();

        String memberMobile = (String) params.get("memberMobile");
        String memberName = (String) params.get("memberName");
        String memberEmail = (String) params.get("memberEmail");
        int loginType = Integer.parseInt((String) params.get("loginType"));
        int memberSource = Integer.parseInt((String) params.get("memberSource"));
        memberEntity.setMemberSource(memberSource);

        if (null != memberName) {
            memberEntity.setMemberName(memberName);
        }

        //验证码校验
        String captcha = "";

        if (loginType == LoginEnum.LOGIN_EMAIL.value()) {
            // 邮箱注册
            if (StringUtils.isNotBlank(memberEmail)) {
                captcha = (String) redisUtils.get(memberEmail);
                //邮箱设置
                memberEntity.setMemberEmail(memberEmail);
                memberEntity.setNickName(memberEmail);
            }
        } else {
            // 手机注册
            if (StringUtils.isNotBlank(memberMobile)) {
                String key = MemberRedisConstants.REGISTER_MOBILE_CODE_PREFIX + memberMobile;
                log.info("手机验证码的key为{}", key);
                captcha = (String) redisUtils.get(key);
                log.info("手机验证码的key为{}，值为{}", key, captcha);
                //手机设置
                memberEntity.setMemberMobile(memberMobile);
                memberEntity.setNickName(memberMobile);
            }
        }
        //判断验证码是否已过期
        if (StringUtils.isBlank(captcha)) {
            result.put("code", MemberErrorEnum.E_CAPTCHA_PASS.code());
            result.put("msg", MemberErrorEnum.E_CAPTCHA_PASS.value());
            return result;
        }
        String checkValidCode = (String) params.get("checkValidCode");
        if (!StringUtils.equals(captcha, checkValidCode)) {
            result.put("code", MemberErrorEnum.E_CAPTCHA_ERROR.code());
            result.put("msg", MemberErrorEnum.E_CAPTCHA_ERROR.value());
            return result;
        }

        String memberPasswd = (String) params.get("memberPasswd");
        // 密码解密
        // 使用RSA私钥进行解密
        try {
            memberPasswd = memberPasswd.replace(" ", "+");
            memberPasswd = (RSACoder.decryptByPrivateKey(memberPasswd));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", memberPasswd, e);
            throw new ServiceException("密码解析失败，请重新输入");
        }

        if (!memberPasswd.matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            throw new ServiceException("密码必须为6-20位字符,数字,特殊字符");
        }
        String encodePasswd = PasswordUtils.encode(memberPasswd);
        log.debug("加密后密码为{}", encodePasswd);
        memberEntity.setMemberPasswd(encodePasswd);
        //设置用户默认头像,昵称(给已注册用户返回时用),手机注册使用手机号作为用户名,邮箱注册用邮箱作为用户名
        MemberUtil.saveMemberFill(memberEntity);
        //会员保存
        int success = baseDao.insert(memberEntity);
        MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
        memberInfoDTO.setMemberId(memberEntity.getId());
        //保存会员信息
        memberInfoService.save(memberInfoDTO);

        // 1.如果用户注册成功，则先查询是否有积分设置 2.如果有积分设置则按照积分的规则增加积分记录
        if (success == 1) {
            Map<String, Object> memberParams = new HashMap<>(4);
            memberParams.put("memberId", memberEntity.getId());
            memberParams.put("memberName", memberEntity.getMemberName());
            memberParams.put("sourceType", MemberPointLogEnum.WELCOME_SOURCE_TYPE.code());
            memberParams.put("pointDesc", MemberPointLogEnum.WELCOME_SOURCE_TYPE.value());
            this.savePoint(memberParams);
        }
        // 注册成功清除reids缓存
        redisUtils.delete(MemberRedisConstants.REGISTER_MOBILE_CODE_PREFIX + memberMobile);
        result.put("code", ErrorCode.SUCCESS);
        result.put("msg", "注册成功");
        return result;
    }

    /**
     * 登录
     *
     * @param login
     * @return DY 2019/5/13
     */
    @Override

    public AuthorizationDTO login(@RequestBody LoginDTO login) {

        return null;
    }

    /**
     * 忘记密码:根据手机号修改
     *
     * @param params
     * @return
     */
    @Override

    public void updatePasswordByMobile(@RequestParam Map<String, Object> params) {

        String mobile = params.get("mobile").toString();
        //根据手机号修改密码
        String passwd = params.get("newPwd").toString();
        String confirmPwd = params.get("ConfirmPwd").toString();
        // 密码解密
        // 使用RSA私钥进行解密
        try {
            passwd = passwd.replace(" ", "+");
            passwd = (RSACoder.decryptByPrivateKey(passwd));

            confirmPwd = confirmPwd.replace(" ", "+");
            confirmPwd = (RSACoder.decryptByPrivateKey(confirmPwd));

        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", passwd, e);
            throw new ServiceException("密码解析失败，请重新输入");
        }

        if (!passwd.equals(confirmPwd)) {
            throw new ServiceException(MemberStatusCode.ORDER_CHANGE_SHOW_STATUS_EXCERTION);
        }
        if (!passwd.matches(RegexConstant.SAVE_UPDATE_PWD_REGEX)) {
            throw new ServiceException("密码必须为6-20位字符,数字,特殊字符");
        }
        //加密新密码
        String encodePasswd = PasswordUtils.encode(passwd);
        baseDao.updatePasswordByMobile(mobile, encodePasswd);
    }


    @Override

    public List<MemberDTO> selectAllMember(@RequestParam(value = "roleType", required = false) Integer rolleType) {
        QueryWrapper<MemberEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", MemberEnum.DEL_FLAG_NOT.value());
        wrapper.eq(ObjectUtil.isNotNull(rolleType), "member_role", rolleType);
        List<MemberEntity> memberEntityList = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(memberEntityList, MemberDTO.class);
    }


    /**
     * 修改用户信息
     *
     * @param memberDTO 会员信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public void updateMember(@RequestBody MemberUpdateDTO memberDTO) {
        MemberEntity memberEntity = ConvertUtils.sourceToTarget(memberDTO, MemberEntity.class);
        baseDao.updateById(memberEntity);

        // todo 头像、昵称、完善个人信息

        //为null不做修改
        if (StringUtils.isNotBlank(memberDTO.getMemberAreaid())) {
            memberInfoService.updateByMemberId(memberDTO);
        }
    }

    /**
     * 会员编辑回显
     *
     * @param id 用户ID
     * @return 会员信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public MemberUpdateDTO selectMemberUpdateDTO(Long id) {
        return baseDao.selectMemberUpdateDTO(id);
    }

    /**
     * 删除用户信息
     *
     * @param ids 用户ID数组
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public void logicDelete(@RequestBody Long[] ids) {
        super.logicDelete(ids);
    }

    /**
     * 重置密码
     *
     * @param id     会员id
     * @param mobile 手机号
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public void resetPasswd(@RequestParam("id") Long id, @RequestParam("mobile") String mobile) {

        //查询用户获取手机号
        Map<String, Object> res = new HashMap<>(3);
        Object o = redisUtils.get(MemberRedisConstants.AVOID_REPEAT_SEND_PREFIX + mobile);
        if (!BeanUtil.isEmpty(o)) {
            throw new ServiceException(MemberStatusCode.MESSAGE_SEND_TIME_FAILED);
        }
        MemberEntity memberEntity = baseDao.selectById(id);

        if (ObjectUtil.isNotNull(memberEntity)) {
            //修改密码
            String passwd = PasswdUtil.getGeneratePassword();
            // 明文密码加密
            memberEntity.setMemberPasswd(PasswordUtils.encode(passwd));
            baseDao.updateById(memberEntity);

            this.sendMessage(mobile, passwd);
        } else {
            throw new ServiceException(MemberStatusCode.MEMBER_NOT_EXIST);
        }
    }

    /**
     * 封装发送短信参数并发送短信消息
     *
     * @param mobile: 手机号
     * @param passwd: 密码铭文
     * @date 2020/4/10 9:24
     * @author lixiangx@leimingtech.com
     **/
    private void sendMessage(String mobile, String passwd) {
        // 重置密码发送短信
        // 创建短信所需参数Map
        Map<String, String> paramsMap = new HashMap<>(5);
        paramsMap.put("passwd", passwd);

        Map<String, Object> map = new HashMap<>(16);
        map.put("mobile", mobile);
        map.put("paramJson", JSON.toJSONString(paramsMap));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(MessageCodeEnum.TEMPLATECODE_RESET_PWD_CODE.value());
        messageDTO.setSendName("系统");
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
        redisUtils.set(MemberRedisConstants.AVOID_REPEAT_SEND_PREFIX + mobile, passwd, MemberRedisConstants.AVOID_REPEAT_TIME);
    }


    /**
     * 修改用户状态
     *
     * @param id 用户id
     * @return 修改结果
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public Map<String, Object> updateState(Long id) {
        Map<String, Object> res = new HashMap<>(2);
        MemberEntity memberEntity = baseDao.selectById(id);
        if (ObjectUtil.isNotNull(memberEntity)) {
            if (memberEntity.getMemberState() == MemberEnum.MEMBER_STATE_NO.value()) {
                //正常
                memberEntity.setMemberState(MemberEnum.MEMBER_STATE_OFF.value());
                Object o = redisUtils.get(RedisKeys.getSecurityUserKey(memberEntity.getMemberName()));
                if (o != null) {
                    MemberDTO memberDTO = (MemberDTO) o;
                    redisUtils.delete(memberDTO.getToken());
                }
            } else {
                memberEntity.setMemberState(MemberEnum.MEMBER_STATE_NO.value());
            }
            baseDao.updateById(memberEntity);
        } else {
            res.put("code", MemberErrorEnum.E_IS_NULL.code());
            res.put("msg", MemberErrorEnum.E_IS_NULL.value());
        }
        return res;

    }

    /**
     * 根据id获取用户基本信息（PC端会员中心数据查询）
     *
     * @param id 用户ID
     * @return 会员信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public MemberPersonCenterDTO selectMember(Long id) {
        MemberPersonCenterDTO memberPersonCenterDTO = new MemberPersonCenterDTO();
        MemberVoDTO memberVoDTO = this.selectMemberById(id);
        BeanCopier.create(MemberVoDTO.class, MemberPersonCenterDTO.class, false)
                .copy(memberVoDTO, memberPersonCenterDTO, null);
        MemberAddressDTO memberAddressDTO = memberAddressService.findDefalutAddress(id);
        if (null != memberAddressDTO) {
            memberPersonCenterDTO.setAreaInfo(memberAddressDTO.getAreaInfo());
        }
        if (null != memberVoDTO.getMemberBirthday()) {
            String date = DateUtils.format(memberVoDTO.getMemberBirthday());
            memberPersonCenterDTO.setMemberBirthday(date);
        }
        //查询未读消息数量
        memberPersonCenterDTO.setMessageNum(messageReceiverService.selectByReceiveId(id));
        // 订单数量查询
        MemberOrderCountDTO memberOrderCountDTO = orderService.countMemberOrder(id);
        memberPersonCenterDTO.setPaymentNum(memberOrderCountDTO.getPaymentNum());
        memberPersonCenterDTO.setReceivingNum(memberOrderCountDTO.getReceivingNum());

        //待评价
        Map<String, Object> params = new HashMap<>(2);
        params.put("memberId", id);
        memberPersonCenterDTO.setEvaluateNum(orderGoodsService.countNotEvaluateGoods(params));

        // 未使用优惠券数量查询
        memberPersonCenterDTO.setCouponNum(0);
        params.put("couponsState", 0);
        List<FrontMyCouponsPageDTO> myCouponsList = couponsActivitySearchService.myCouponsList(params);
        if (CollectionUtils.isNotEmpty(myCouponsList)) {
            memberPersonCenterDTO.setCouponNum(myCouponsList.size());
        }

        //收藏数量
        memberPersonCenterDTO.setCollectNum(favoritesService.countByMemberId(id));
        //足迹
        memberPersonCenterDTO.setBrowseNum(goodsBrowseService.findNum(id));
        //物流消息
        LastestOrderLogisticsDTO newOrderLogisticsLog = orderLogisticsLogService.findNewOrderLogisticsLog(id);
        if (newOrderLogisticsLog != null) {
            memberPersonCenterDTO.setOrderId(newOrderLogisticsLog.getOrderId());
            memberPersonCenterDTO.setContext(newOrderLogisticsLog.getContext());
            memberPersonCenterDTO.setGoodsPicture(newOrderLogisticsLog.getGoodsPicture());
            memberPersonCenterDTO.setState(newOrderLogisticsLog.getState());
        }
        // 是否设置密码
        memberPersonCenterDTO.setPwdFlag(StringUtils.isNotBlank(memberVoDTO.getMemberPasswd()) ? MemberEnum.PWDFLAG_YES.value() : MemberEnum.PWDFLAG_NO.value());
        memberPersonCenterDTO.setPayPwdFlag(StringUtils.isNotBlank(memberVoDTO.getMemberInfoDTO().getPaymentPasswd()) ? MemberEnum.PWDFLAG_YES.value() : MemberEnum.PWDFLAG_NO.value());
        memberPersonCenterDTO.setAvailableBalance(null != memberVoDTO.getMemberInfoDTO().getAvailableBalance()
                ? memberVoDTO.getMemberInfoDTO().getAvailableBalance() : new BigDecimal(BigInteger.ZERO));
        memberPersonCenterDTO.setGrowthValue(memberVoDTO.getMemberInfoDTO().getGradePoint());
        memberPersonCenterDTO.setMemberGraderName(memberVoDTO.getMemberInfoDTO().getGradeName());
        memberPersonCenterDTO.setPointValue(memberVoDTO.getMemberInfoDTO().getAvailablePoint());

        // 访问个人中心时，增加每日登录积分
        // 每日登录增加积分之前先查询当天的积分是否增加
        Map<String, Object> memberParams = new HashMap<>(2);
        memberParams.put("memberId", id);
        memberParams.put("sourceType", MemberPointLogEnum.LOGIN_SOURCE_TYPE.code());

        // 获取当前日期的0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        String from = DateUtils.format(zero, DateUtils.DATE_TIME_PATTERN);
        memberParams.put("endTime", DateUtils.format(DateUtils.getToday(), DateUtils.DATE_TIME_PATTERN));
        memberParams.put("startTime", from);
        List<PointLogDTO> pointLogDTOList = pointLogService.queryByTime(memberParams);
        if (CollectionUtils.isEmpty(pointLogDTOList)) {
            Map<String, Object> timeParams = new HashMap<>(5);
            timeParams.put("memberId", id);
            timeParams.put("memberName", memberPersonCenterDTO.getMemberName());
            timeParams.put("sourceType", MemberPointLogEnum.LOGIN_SOURCE_TYPE.code());
            timeParams.put("pointDesc", MemberPointLogEnum.LOGIN_SOURCE_TYPE.value());
            timeParams.put("repeat", 1);
            this.savePoint(timeParams);
        }


        return memberPersonCenterDTO;
    }

    /**
     * 修改用户信息
     *
     * @param memberDTO 用户信息
     * @return map 修改结果
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public Map<String, Object> updatememberById(@RequestBody MemberDTO memberDTO) {
        Map<String, Object> map = new HashMap<>(2);
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        Integer soureType = 0;
        String pointDesc = "";
        if (StringUtils.isNotEmpty(memberDTO.getMemberAvatar())) {
            memberEntity.setMemberAvatar(memberDTO.getMemberAvatar());

            soureType = MemberPointLogEnum.AVATAR_SOURCE_TYPE.code();
            pointDesc = MemberPointLogEnum.AVATAR_SOURCE_TYPE.value();
        }
        if (null != memberDTO.getMemberSex()) {
            memberEntity.setMemberSex(memberDTO.getMemberSex());

            soureType = MemberPointLogEnum.MEMBER_INFORMATION_SOURCE_TYPE.code();
            pointDesc = MemberPointLogEnum.MEMBER_INFORMATION_SOURCE_TYPE.value();
        }
        if (null != memberDTO.getMemberBirthday()) {
            memberEntity.setMemberBirthday(memberDTO.getMemberBirthday());

            soureType = MemberPointLogEnum.MEMBER_INFORMATION_SOURCE_TYPE.code();
            pointDesc = MemberPointLogEnum.MEMBER_INFORMATION_SOURCE_TYPE.value();
        }
        if (StringUtils.isNotEmpty(memberDTO.getNickName())) {
            memberEntity.setNickName(memberDTO.getNickName());

            soureType = MemberPointLogEnum.NICKNAME_SOURCE_TYPE.code();
            pointDesc = MemberPointLogEnum.NICKNAME_SOURCE_TYPE.value();
        }
        int result = baseDao.updateById(memberEntity);
        if (result == ResultEnum.RESULT_COUNT.value()) {
            map.put("code", 400);
            map.put("msg", "保存失败");
            return map;
        } else {
            Map<String, Object> memberParams = new HashMap<>(4);
            memberParams.put("memberId", memberEntity.getId());
            memberParams.put("memberName", memberEntity.getMemberName());
            memberParams.put("sourceType", soureType);
            memberParams.put("pointDesc", pointDesc);
            this.savePoint(memberParams);
        }
        map.put("code", 200);
        map.put("msg", "保存成功");
        return map;
    }

    /**
     * 会员基础信息修改
     *
     * @param memberDTO 会员信息
     * @author lixiangx@leimingtech.com
     * @date 2019 /11/11 10:25
     */
    @Override

    public void updateBase(@RequestBody MemberDTO memberDTO) {
        MemberEntity memberEntity = ConvertUtils.sourceToTarget(memberDTO, MemberEntity.class);
        baseDao.updateById(memberEntity);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param memberMobile 手机号
     * @return 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public MemberPhoneDTO selectByMobile(@RequestParam("memberMobile") String memberMobile) {
        return baseDao.selectByMobile(memberMobile);
    }

    /**
     * 根据手机号查询用户详细信息
     *
     * @param mobile :
     * @return MemberVoDTO 用户详细信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public MemberVoDTO getByMobile(@RequestParam("mobile") String mobile) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_mobile", mobile).last("limit 1");
        MemberEntity memberEntity = baseDao.selectOne(queryWrapper);
        if (null != memberEntity) {
            return ConvertUtils.sourceToTarget(memberEntity, MemberVoDTO.class);
        } else {
            return null;
        }

    }

    /**
     * 更新用户信息
     *
     * @param dto :
     * @return Boolean 更新结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public Boolean updateById(@RequestBody MemberDTO dto) {
        MemberEntity memberEntity = ConvertUtils.sourceToTarget(dto, MemberEntity.class);
        return super.updateById(memberEntity);
    }

    /**
     * 根据对应的类型添加用户积分和成长值
     *
     * @param params 参数条件
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public void savePoint(@RequestParam Map<String, Object> params) {

        Long memberId = Long.parseLong(params.get("memberId").toString());
        String memberName = (String) params.get("memberName");
        int sourceType = Integer.parseInt(params.get("sourceType").toString());
        String pointDesc = (String) params.get("pointDesc");

        // repeat=2表示积分或者成长值是永远只增加一次；repeat=1表示积分或成长值可以重复增加；
        int repeat = 2;
        if (params.get(PARAMS_NAME_REPEAT) != null) {
            repeat = Integer.parseInt(params.get(PARAMS_NAME_REPEAT).toString());
        }

        String pointSetting = pointSettingService.findFromRedis("reward");
        RewardSettingDTO rewardSettingDTO = JSONObject.parseObject(pointSetting, RewardSettingDTO.class);

        if (rewardSettingDTO == null) {
            return;
        }
        RewardDTO rewardDTO = new RewardDTO();


        switch (sourceType) {
            case 1:
                // 新手欢迎奖励
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getWelcome();
                break;
            case 2:
                // 设置头像
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getAvatar();
                break;
            case 3:
                // 设置昵称
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getNikename();
                break;
            case 4:
                // 完善个人信息
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getInformation();
                break;
            case 5:
                // 首次关注店铺
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getAttentionStore();
                break;
            case 6:
                // 首次分享商品
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getSharegoods();
                break;
            case 7:
                // 首次收藏商品
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getFavoritesgoods();
                break;
            case 8:
                // 首次购物成功
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getFirstOrder();
                break;
            case 9:
                // 首次完成评价
                rewardDTO = rewardSettingDTO.getNoviceTaskSetting().getEvaluateOrder();
                break;
            case 10:
                // 每日登录
                rewardDTO = rewardSettingDTO.getDailyTaskSetting().getLogin();
                break;
            default:
                return;
        }

        if (null == rewardDTO.getGrowthValue() || null == rewardDTO.getPointValue()) {
            return;
        }
        if (repeat == 1) {

            if (rewardDTO.getGrowthValue() != 0 && rewardDTO.getPointValue() != 0) {

                PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                        memberId,
                        memberName,
                        rewardDTO.getPointValue(),
                        rewardDTO.getGrowthValue(),
                        pointDesc,
                        sourceType,
                        MemberPointLogEnum.INSERT_ALL.code());

                pointLogService.packPointLog(pointLogPackDTO);

            } else if (rewardDTO.getGrowthValue() != 0 && rewardDTO.getPointValue() == 0) {

                PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                        memberId,
                        memberName,
                        rewardDTO.getPointValue(),
                        rewardDTO.getGrowthValue(),
                        pointDesc,
                        sourceType,
                        MemberPointLogEnum.INSERT_GROWTH.code());

                pointLogService.packPointLog(pointLogPackDTO);

            } else if (rewardDTO.getGrowthValue() == 0 && rewardDTO.getPointValue() != 0) {

                PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                        memberId,
                        memberName,
                        rewardDTO.getPointValue(),
                        rewardDTO.getGrowthValue(),
                        pointDesc,
                        sourceType,
                        MemberPointLogEnum.INSERT_POINT.code());

                pointLogService.packPointLog(pointLogPackDTO);

            }
        } else {

            if (rewardDTO.getGrowthValue() != 0 && rewardDTO.getPointValue() != 0) {

                // 查询项目中的新手奖励是否存在
                Map<String, Object> newTask = new HashMap<String, Object>(2) {{
                    put("memberId", memberId);
                    put("sourceType", sourceType);
                }};

                List<PointLogDTO> pointLogDTOList = pointLogService.queryWithMemberId(newTask);

                if (CollectionUtils.isEmpty(pointLogDTOList)) {
                    PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                            memberId,
                            memberName,
                            rewardDTO.getPointValue(),
                            rewardDTO.getGrowthValue(),
                            pointDesc,
                            sourceType,
                            MemberPointLogEnum.INSERT_ALL.code());

                    pointLogService.packPointLog(pointLogPackDTO);
                }

            } else if (rewardDTO.getGrowthValue() != 0 && rewardDTO.getPointValue() == 0) {
                // 查询项目中的新手奖励是否存在
                Map<String, Object> newTask = new HashMap<String, Object>(2) {{
                    put("memberId", memberId);
                    put("sourceType", sourceType);
                }};

                List<PointLogDTO> pointLogDTOList = pointLogService.queryWithMemberId(newTask);

                if (CollectionUtils.isEmpty(pointLogDTOList)) {
                    PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                            memberId,
                            memberName,
                            rewardDTO.getPointValue(),
                            rewardDTO.getGrowthValue(),
                            pointDesc,
                            sourceType,
                            MemberPointLogEnum.INSERT_GROWTH.code());

                    pointLogService.packPointLog(pointLogPackDTO);
                }
            } else if (rewardDTO.getGrowthValue() == 0 && rewardDTO.getPointValue() != 0) {
                // 查询项目中的新手奖励是否存在
                Map<String, Object> newTask = new HashMap<String, Object>(2) {{
                    put("memberId", memberId);
                    put("sourceType", sourceType);
                }};

                List<PointLogDTO> pointLogDTOList = pointLogService.queryWithMemberId(newTask);

                if (CollectionUtils.isEmpty(pointLogDTOList)) {
                    PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                            memberId,
                            memberName,
                            rewardDTO.getPointValue(),
                            rewardDTO.getGrowthValue(),
                            pointDesc,
                            sourceType,
                            MemberPointLogEnum.INSERT_POINT.code());

                    pointLogService.packPointLog(pointLogPackDTO);
                }
            }
        }


    }

    /**
     * 批量查询用户手机号
     *
     * @param memberList 用户ID集合
     * @return 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public List<MemberDTO> selectPhoneListById(@RequestBody List<Long> memberList) {
        return baseDao.selectPhoneListById(memberList);
    }

    /**
     * 首页>基础概况>会员数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexMemberDataDTO 首页用户数据
     * @date 2020/4/7/007 12:02
     * @author xuzhch
     */

    @Override
    public IndexMemberDataDTO indexMemberData(@RequestParam("startDateStr") String startDateStr, @RequestParam("endDateStr") String endDateStr) {
        Date startDate = DateUtils.parse(startDateStr, DateUtils.DATE_TIME_PATTERN);
        Date endDate = DateUtils.parse(endDateStr, DateUtils.DATE_TIME_PATTERN);
        return baseDao.selectIndexMemberData(startDate, endDate);
    }

    /**
     * 根据用户名模糊匹配查询用户信息
     *
     * @param params nickName 用户名
     * @return 分页结果
     * @author pixiaoyong cms新增方法
     * @date 2019/7/23 17:34
     */

    @Override
    public PageData<MemberDTO> selectByNickName(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<MemberDTO> page = new Page<>(pageNum, limit);
        List<MemberEntity> memberEntities = baseDao.selectMemberList(page, params);
        List<MemberDTO> list = ConvertUtils.sourceToTarget(memberEntities, MemberDTO.class);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 根据会员ID集合查询友盟token信息
     *
     * @param memberIds 会员ID集合
     * @return list 友盟token信息集合
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public List<MemberUmengTokenInfo> selectUmengTokenByIds(@RequestBody List<Long> memberIds) {
        return baseDao.selectUmengTokenByIds(memberIds);
    }


    @Override
    public Integer getCountByUnionId(String unionid) {
        return baseDao.selectCount(Wrappers.<MemberEntity>lambdaQuery().eq(MemberEntity::getWechatUnionid, unionid));
    }

    /**
     * PC端个人中心基本信息
     *
     * @param memberId 会员ID
     * @return MemberPersonCenterPcDTO 个人中心基本信息
     * @author xuzhch
     * @date 2020年5月19日11:36:03
     */

    @Override
    public MemberPersonCenterPcDTO selectPcMemberDetail(Long memberId) {
        MemberPersonCenterPcDTO centerPcDTO = new MemberPersonCenterPcDTO();
        MemberVo memberVo = baseDao.selectMemberById(memberId);
        centerPcDTO.setPointValue(memberVo.getMemberInfoEntity() == null ? INIT_GROWTH_VALUE : memberVo.getMemberInfoEntity().getAvailablePoint());
        centerPcDTO.setGrowthValue(memberVo.getMemberInfoEntity() == null ? INIT_GROWTH_VALUE : memberVo.getMemberInfoEntity().getGradePoint());

        if (null != memberVo.getMemberInfoEntity().getGradePoint()) {
            MemberGradeDTO memberGradeDTO = memberGradeService.selectByMemberId(memberVo.getMemberInfoEntity().getGradePoint());
            centerPcDTO.setMemberGraderName(memberGradeDTO.getGradeName() == null ? "" : memberGradeDTO.getGradeName());
        }
        //先copy用户详情的数据，再copy用户信息。否则Id字段会被覆盖
        MemberInfoEntity memberInfoEntity = memberVo.getMemberInfoEntity();
        BeanCopier.create(MemberInfoEntity.class, MemberPersonCenterPcDTO.class, false)
                .copy(memberInfoEntity, centerPcDTO, null);

        BeanCopier.create(MemberVo.class, MemberPersonCenterPcDTO.class, false)
                .copy(memberVo, centerPcDTO, null);

        centerPcDTO.setMemberMobile(DataMaskingUtils.mobileEncrypt(centerPcDTO.getMemberMobile()));
        Map<String, Object> params = new HashMap<>(2);
        params.put("memberId", memberId);

        // 未使用优惠券数量查询
        centerPcDTO.setCouponNum(0);
        params.put("couponsState", 0);
        List<FrontMyCouponsPageDTO> myCouponsList = couponsActivitySearchService.myCouponsList(params);
        if (CollectionUtils.isNotEmpty(myCouponsList)) {
            centerPcDTO.setCouponNum(myCouponsList.size());
        }
        //收藏数量
        centerPcDTO.setCollectNum(favoritesService.countByMemberId(memberId));
        //足迹数量
        centerPcDTO.setBrowseNum(goodsBrowseService.findNum(memberId));

        // 访问个人中心时，增加每日登录积分
        // 每日登录增加积分之前先查询当天的积分是否增加
        Map<String, Object> memberParams = new HashMap<>(2);
        memberParams.put("memberId", memberId);
        memberParams.put("sourceType", MemberPointLogEnum.LOGIN_SOURCE_TYPE.code());

        // 获取当前日期的0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        String from = DateUtils.format(zero, DateUtils.DATE_TIME_PATTERN);
        memberParams.put("endTime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        memberParams.put("startTime", from);
        List<PointLogDTO> pointLogDTOList = pointLogService.queryByTime(memberParams);
        if (CollectionUtils.isEmpty(pointLogDTOList)) {
            Map<String, Object> timeParams = new HashMap<>(5);
            timeParams.put("memberId", memberId);
            timeParams.put("memberName", centerPcDTO.getMemberName());
            timeParams.put("sourceType", MemberPointLogEnum.LOGIN_SOURCE_TYPE.code());
            timeParams.put("pointDesc", MemberPointLogEnum.LOGIN_SOURCE_TYPE.value());
            timeParams.put("repeat", 1);
            this.savePoint(timeParams);
        }

        return centerPcDTO;
    }

    /**
     * 修改邮箱验证码发送
     *
     * @param email    邮箱
     * @param memberId 会员ID
     * @param codeType 验证码类型
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public void changeEmailSendCaptcha(@RequestParam("email") String email, @RequestParam("memberId") Long memberId, @RequestParam("codeType") String codeType) {
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        Map<String, String> params = new HashMap<>(1);
        params.put("code", code);
        redisUtils.set(MemberRedisConstants.MEMBER_EMAIL_CODE + memberId + ":" + codeType, code, MemberRedisConstants.EMAIL_CACHE_TIME);
        Map<String, String> sendParams = new HashMap<>(1);
        sendParams.put("template", EmailEnum.FORGET_PWD_EMAIL_TEMPLATE.value());
        sendParams.put("mailTo", email);
        sendParams.put("mailCc", "");
        sendParams.put("params", JSON.toJSONString(params));
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_EMAIL_QUEUE, JSON.toJSONString(sendParams));
//        sysMailTemplateService.sendMail(Long.valueOf(EmailEnum.FORGET_PWD_EMAIL_TEMPLATE.value()), email, "", JSON.toJSONString(params));
    }


    @Override
    public void changeEmail(@RequestParam Map<String, String> params) {
        String memberIdStr = params.get("memberId");
        String codeType = params.get("codeType");
        String email = params.get("email");
        String redisKey = MemberRedisConstants.MEMBER_EMAIL_CODE + memberIdStr + ":" + codeType;
        Object o = redisUtils.get(redisKey);
        if (BeanUtil.isEmpty(o)) {
            throw new ServiceException(MemberStatusCode.EMAIL_VERIFY_FAIL);
        }
        if (params.get(PARAMS_NAME_CODE).equals(String.valueOf(o))) {
            //查询邮箱是否已绑定
            if (EMAIL_ALREADY_BIND.equals(codeType)) {
                MemberEntity memberEntity = baseDao.selectOne(Wrappers.<MemberEntity>lambdaQuery()
                        .eq(MemberEntity::getMemberEmail, email)
                        .eq(MemberEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
                if (!BeanUtil.isEmpty(memberEntity)) {
                    throw new ServiceException(MemberStatusCode.EMAIL_REPEAT_BINDING_FAIL);
                }
                String memberName = params.get("memberName");
                Object memberObj = redisUtils.get(RedisKeys.getSecurityUserKey(memberName));
                if (null != memberObj) {
                    MemberDTO memberDTO = (MemberDTO) memberObj;
                    memberDTO.setMemberEmail(email);
                    redisUtils.set(RedisKeys.getSecurityUserKey(memberName), memberDTO, RedisUtils.NOT_EXPIRE);
                }
                MemberEntity updateEntity = new MemberEntity();
                updateEntity.setId(Long.valueOf(memberIdStr));
                updateEntity.setMemberEmail(email);
                updateEntity.setEmailValidateState(MemberEnum.EMAILVALIDATE_STATE_ON.value());
                baseDao.updateById(updateEntity);
            }
        }
    }


    @Override
    public void logoutDeviceToken(String deviceToken) {
        super.update(Wrappers.<MemberEntity>lambdaUpdate().eq(MemberEntity::getDeviceToken, deviceToken).set(MemberEntity::getDeviceToken, null));
    }


    @Override
    public void logout(Long memberId) {
        super.update(Wrappers.<MemberEntity>lambdaUpdate().eq(MemberEntity::getId, memberId).set(MemberEntity::getDeviceToken, null));
    }


    @Override
    public MemberBaseInfoDTO selectPcMemberBaseInfo(Long memberId) {
        MemberVo memberVo = baseDao.selectMemberById(memberId);
        MemberBaseInfoDTO memberBaseInfoDTO = new MemberBaseInfoDTO();
        //先copy用户详情的数据，再copy用户信息。否则Id字段会被覆盖
        MemberInfoEntity memberInfoEntity = memberVo.getMemberInfoEntity();
        BeanCopier.create(MemberInfoEntity.class, MemberBaseInfoDTO.class, false)
                .copy(memberInfoEntity, memberBaseInfoDTO, null);

        BeanCopier.create(MemberVo.class, MemberBaseInfoDTO.class, false)
                .copy(memberVo, memberBaseInfoDTO, null);

        return memberBaseInfoDTO;

    }

    /**
     * PC 端微信登录
     *
     * @param code
     * @return
     */

    @Override
    public Map<String, Object> wechatLogin(@RequestParam("code") String code) {
        Map<String, Object> result = new HashMap<>(4);
        WxOauth2ResDTO wxOauth2ResDTO = wechatMpService.wechatLogin(code);
        if (!BeanUtil.isEmpty(wxOauth2ResDTO)) {
            log.debug("获取微信登录返回信息", JSON.toJSONString(wxOauth2ResDTO));
            String accessToken = wxOauth2ResDTO.getAccessToken();
            String openId = wxOauth2ResDTO.getOpenId();
            String unionid = wxOauth2ResDTO.getUnionId();
            // 小程序不存在accessToken，所以&&条件更改为||
            if (StringUtils.isNotBlank(accessToken) || StringUtils.isNotBlank(openId)) {
                //获取微信用户信息
                MemberEntity memberEntity = baseDao.selectByUnionid(unionid);
                // 封装授权数据实体
                if (memberEntity == null || StringUtils.isBlank(memberEntity.getMemberMobile())) {
                    // 4.不存在该用户，进入填写手机号页面，绑定手机号
                    result.put("code", MemberErrorEnum.E_WECHAT_MOBILE_UNBIND.code());
                    result.put("msg", MemberErrorEnum.E_WECHAT_MOBILE_UNBIND.value());
                    result.put("data", JSON.toJSONString(wxOauth2ResDTO));
                    redisUtils.set(RedisKeys.getWechatLoginKey(code), wxOauth2ResDTO, 60 * 5);
                    return result;
                } else {
                    // 5.已绑定手机号，放行登陆操作
                    result.put("code", ErrorCode.SUCCESS);
                    result.put("msg", "登录成功");
                    result.put("unionid", unionid);
                    result.put("mobile", memberEntity.getMemberMobile());
                    return result;
                }
            }
        }
        return result;
    }


    @Override
    public Map<String, Object> wechatBind(@RequestParam(value = "code") String code,
                                          @RequestParam(value = "mobile") String mobile,
                                          @RequestParam(value = "validCode") String validCode) {
        Map<String, Object> result = new HashMap<>(4);
        // 1.获取验证码
        Object obj = redisUtils.get(RedisKeys.getMobileBindCaptchaKey(mobile));
        if (obj == null) {
            result.put("code", MemberErrorEnum.E_CAPTCHA_ERROR.code());
            result.put("msg", MemberErrorEnum.E_CAPTCHA_ERROR.value());
            return result;
        }
        String validCodeReids = obj.toString();
        // 2.判断验证码是否正确
        if (!StringUtils.equals(validCode, validCodeReids)) {
            result.put("code", MemberErrorEnum.E_CAPTCHA_ERROR.code());
            result.put("msg", MemberErrorEnum.E_CAPTCHA_ERROR.value());
            return result;
        }
        return this.wechatBindMobile(code, mobile);
    }

    private synchronized Map<String, Object> wechatBindMobile(@RequestParam(value = "code") String code, @RequestParam(value = "mobile") String mobile) {
        Map result = new HashMap<String, Object>(2);
        // 1.获取redis微信授权信息
        Object obj = redisUtils.get(RedisKeys.getWechatLoginKey(code));
        log.info("获取key:{},微信授权信息缓存成功，result：{}", RedisKeys.getWechatLoginKey(code), obj);
        if (obj == null) {
            // redis不存在该用户微信授权信息
            result.put("code", MemberErrorEnum.E_WECHAT_BIND_ACCESS_EXCERTION.code());
            result.put("msg", MemberErrorEnum.E_WECHAT_BIND_ACCESS_EXCERTION.value());
        } else {
            // 存在用户微信授权信息
            WxOauth2ResDTO wxOauth2ResDTO = (WxOauth2ResDTO) obj;
            // 获取用户信息，进行新用户保存或者原有用户更新微信绑定关系
            Integer status = this.getUserMessage(wxOauth2ResDTO, mobile);
            if (status == 0) {
                result.put("code", ErrorCode.SUCCESS);
                result.put("msg", "绑定手机号成功");
                result.put("mobile", mobile);
                // 删除redis数据
                redisUtils.delete(RedisKeys.getWechatLoginKey(code));
                log.info("删除登录缓存成功，key：{}", RedisKeys.getWechatLoginKey(code));
                redisUtils.delete(RedisKeys.getMobileBindCaptchaKey(mobile));
                log.info("删除微信授权信息缓存成功，key：{}", RedisKeys.getMobileBindCaptchaKey(mobile));
            } else if (status == MemberErrorEnum.E_WECHAT_MOBILE_FAIL.code()) {
                result.put("code", MemberErrorEnum.E_WECHAT_BIND_ACCESS_EXCERTION.code());
                result.put("msg", MemberErrorEnum.E_WECHAT_BIND_ACCESS_EXCERTION.value());
            } else if (status == MemberErrorEnum.E_WECHAT_BINDED_MOBILE.code()) {
                result.put("code", MemberErrorEnum.E_WECHAT_BINDED_MOBILE.code());
                result.put("msg", MemberErrorEnum.E_WECHAT_BINDED_MOBILE.value());
            }
        }
        return result;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * get请求:https协议
     * <p>
     * 通过access_token和openid拉去用户信息
     * <p>
     * 返回: openid,nickname(昵称),sex,province(省份),city,country,headimgurl,privilege(特权),unionid
     *
     * @return
     */
    private Integer getUserMessage(WxOauth2ResDTO wxOauth2ResDTO, String mobile) {
        // 1.查询手机号是否被注册
        MemberVoDTO member = this.getByMobile(mobile);
        // 2.获取微信用户信息
        WxUserDto wxUserDto = wechatMpService.getUserMessage(wxOauth2ResDTO);

        if (BeanUtil.isEmpty(wxUserDto)) {
            // 获取微信用户信息错误
            return MemberErrorEnum.E_WECHAT_MOBILE_FAIL.code();
        }
        if (member != null) {
            if (StringUtils.isNotBlank(member.getWechatUnionid())) {
                // 已绑定微信
                return MemberErrorEnum.E_WECHAT_BINDED_MOBILE.code();
            }
            if (MemberEnum.MEMBER_STATE_OFF.value() == member.getMemberState()) {
                // 手机账号被禁用
                return 3;
            }
            // 3.手机号已被注册，修改该用户微信openid，unionid，微信昵称
            member.setWechatOpenid(wxUserDto.getOpenId());
            member.setWechatUnionid(wxUserDto.getUnionId());
            MemberDTO memberDTO = ConvertUtils.sourceToTarget(member, MemberDTO.class);
            this.updateById(memberDTO);
        } else {
            // 4.手机号未被注册，使用微信信息新增用户信息
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setNickName(wxUserDto.getNickname());
            memberEntity.setMemberMobile(mobile);
            memberEntity.setWechatUnionid(wxUserDto.getUnionId());
            memberEntity.setWechatOpenid(wxUserDto.getOpenId());
            // 微信接口1男,2女  数据库配置 1女 2男
            memberEntity.setMemberSex(wxUserDto.getSex() == 1 ?
                    MemberEnum.MEMBER_SEX_MAN.value() : MemberEnum.MEMBER_SEX_WOMAN.value());
            //上传头像
            String headimgUrl = uploadService.downloadToUrl(wxUserDto.getHeadImgUrl());
            memberEntity.setMemberAvatar(headimgUrl);
            // 填充用户名称
            MemberUtil.saveMemberFill(memberEntity);
            //转换
            MemberRegisterDTO memberRegisterDTO = ConvertUtils.sourceToTarget(memberEntity, MemberRegisterDTO.class);
            this.saveMember(memberRegisterDTO);
        }
        return 0;
    }


    @Override
    public void bindEmail(@RequestParam Map<String, String> params) {
        String code = params.get("code");
        String memberIdStr = params.get("memberId");
        Object o = redisUtils.get(MemberRedisConstants.MEMBER_BIND_EMAIL + code);
        redisUtils.delete(MemberRedisConstants.MEMBER_BIND_EMAIL + code);
        if (BeanUtil.isEmpty(o)) {
            throw new ServiceException(MemberStatusCode.EMAIL_BIND_VLIDATE_FAIL);
        }
        Map<String, String> memberParams = (Map) o;
        if (StringUtils.isBlank(memberParams.get(PARAMS_NAME_MEMBER_ID)) || !memberParams.get(PARAMS_NAME_MEMBER_ID).equals(memberIdStr)) {
            throw new ServiceException(MemberStatusCode.EMAIL_BIND_VLIDATE_FAIL);
        }
        String email = memberParams.get("email");
        String memberName = memberParams.get("memberName");
        Long memberId = Long.valueOf(memberParams.get(PARAMS_NAME_MEMBER_ID));
        Object obj = redisUtils.get(RedisKeys.getSecurityUserKey(memberName));
        if (null != obj) {
            MemberDTO memberCache = (MemberDTO) obj;
            memberCache.setMemberEmail(email);
            redisUtils.set(RedisKeys.getSecurityUserKey(memberName), memberCache, RedisUtils.NOT_EXPIRE);
        } else {
            throw new ServiceException(MemberStatusCode.EMAIL_BIND_VLIDATE_FAIL);
        }
        MemberEntity entity = new MemberEntity();
        entity.setId(memberId);
        entity.setMemberEmail(email);
        entity.setEmailValidateState(MemberEnum.EMAILVALIDATE_STATE_ON.value());
        baseDao.updateById(entity);
    }


    @Override
    public void updateMemberBaseInfo(@RequestBody MemberUpdateDTO memberUpdateDTO) {
        super.update(Wrappers.<MemberEntity>lambdaUpdate()
                .set(MemberEntity::getNickName, memberUpdateDTO.getNickName())
                .set(MemberEntity::getMemberBirthday, memberUpdateDTO.getMemberBirthday())
                .set(StringUtils.isNotBlank(memberUpdateDTO.getMemberAvatar()), MemberEntity::getMemberAvatar, memberUpdateDTO.getMemberAvatar())
                .set(StringUtils.isNotBlank(memberUpdateDTO.getMemberRealName()), MemberEntity::getMemberRealName, memberUpdateDTO.getMemberRealName())
                .set(null != memberUpdateDTO.getMemberSex(), MemberEntity::getMemberSex, memberUpdateDTO.getMemberSex())
                .eq(MemberEntity::getId, memberUpdateDTO.getId())
        );

        Object o = redisUtils.get(RedisKeys.getSecurityUserKey(memberUpdateDTO.getMemberName()));
        if (!ObjectUtil.isEmpty(o)) {
            MemberDTO memberDTO = (MemberDTO) o;
            memberDTO.setNickName(memberUpdateDTO.getNickName());
            memberDTO.setMemberBirthday(memberUpdateDTO.getMemberBirthday());
            if (StringUtils.isNotBlank(memberUpdateDTO.getMemberAvatar())) {
                memberDTO.setMemberAvatar(memberUpdateDTO.getMemberAvatar());
            }
            if (StringUtils.isNotBlank(memberUpdateDTO.getMemberRealName())) {
                memberDTO.setMemberRealName(memberUpdateDTO.getMemberRealName());
            }
            if (null != memberUpdateDTO.getMemberSex()) {
                memberDTO.setMemberSex(memberUpdateDTO.getMemberSex());
            }
            redisUtils.set(RedisKeys.getSecurityUserKey(memberUpdateDTO.getMemberName()), memberDTO, RedisUtils.NOT_EXPIRE);
        }
        memberInfoService.updateMemberBaseInfo(memberUpdateDTO);
    }

    @Override

    public MemberLabelDTO getUserInfoByUser(Long memberId) {
        MemberLabelDTO memberLabelDTO = baseDao.getUserInfoById(memberId);
        return memberLabelDTO;
    }
}

