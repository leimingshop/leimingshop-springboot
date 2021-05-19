/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.weibo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.dao.member.MemberDao;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.member.MemberRegisterDTO;
import com.leimingtech.modules.dto.weibo.WeiboAccessTokenDTO;
import com.leimingtech.modules.entity.member.MemberEntity;
import com.leimingtech.modules.entity.weibo.WeiboInfoEntity;
import com.leimingtech.modules.enums.WeiboEnum;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.utils.member.MemberUtil;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import com.leimingtech.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangtai
 * @date 2020/4/9 18:04
 * @Description:
 */
@Slf4j
@Service

public class WeiboServiceImpl implements WeiboService {

    private static final String PARAM_ERROR_CODE_STR = "errcode";
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(WeiboServiceImpl.class);
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private WeiboLoginService weiboLoginService;
    @Autowired

    private MemberService memberService;
    @Autowired
    private UploadService uploadService;

    /**
     * 功能描述: 微博登录
     *
     * @param: [code]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @auther: zhangtai
     * @date: 2020/4/9 18:07
     */

    @Override
    public Map<String, Object> weiboLogin(String code) {
        Map<String, Object> result = new HashMap<>(4);
        if (StringUtils.isNotBlank(code)) {
            log.debug("------------------收到微博code:{}------------------", code);
            JSONObject jsonObject = weiboLoginService.getAccessToken(code);
            // 打印调取微博后的日志
            Map<String, String> logMap = new HashMap<>(1);
            logMap.put("jsonObject", jsonObject.toString());
            mlogger.info(MemberStatusCode.MEMBER_WEIBO_SUCCESS_CODE, MemberStatusCode.MEMBER_WEIBO_SUCCESS_MSG, logMap);

            String accessToken = "";
            String uid = "";
            String expiresIn = "";
            if (jsonObject != null) {
                accessToken = jsonObject.getString(WeiboEnum.WEIBO_ACCESS_TOKEN.value());
                uid = jsonObject.getString(WeiboEnum.WEIBO_UID.value());

                log.debug("---------uid:{}----accessToken:{}---------", uid, accessToken);
                if (StringUtils.isNotBlank(accessToken) && StringUtils.isNotBlank(uid)) {
                    //获取微博用户信息
                    MemberEntity memberEntity = memberDao.selectByWeiboUid(uid);
                    // 封装授权数据实体
                    WeiboAccessTokenDTO weiboAccessTokenDTO = new WeiboAccessTokenDTO();
                    weiboAccessTokenDTO.setAccessToken(accessToken);
                    weiboAccessTokenDTO.setUid(uid);
                    weiboAccessTokenDTO.setExpiresIn(expiresIn);
                    if (memberEntity == null) {
                        // 4.不存在该用户，进入填写手机号页面，绑定手机号
                        result.put("code", MemberErrorEnum.E_WEIBO_MOBILE_UNBIND.code());
                        result.put("msg", MemberErrorEnum.E_WEIBO_MOBILE_UNBIND.value());
                        result.put("data", weiboAccessTokenDTO);
                    } else {
                        // 4.存在微博用户，判断是否绑定手机号
                        if (StringUtils.isBlank(memberEntity.getMemberMobile())) {
                            // 5.未绑定手机号，进入填写手机号页面，绑定手机号
                            result.put("code", MemberErrorEnum.E_WEIBO_MOBILE_UNBIND.code());
                            result.put("msg", MemberErrorEnum.E_WEIBO_MOBILE_UNBIND.value());
                            result.put("data", weiboAccessTokenDTO);
                        } else {
                            // 5.已绑定手机号，放行登陆操作
                            result.put("code", ErrorCode.SUCCESS);
                            result.put("msg", "登录成功");
                            result.put("weiboUid", uid);
                            result.put("mobile", memberEntity.getMemberMobile());
                        }
                    }
                } else {
                    result.put("code", MemberErrorEnum.E_WEIBO_ACCESSTOKEN_ERROR.code());
                    result.put("msg", MemberErrorEnum.E_WEIBO_ACCESSTOKEN_ERROR.value());
                }
            } else {
                result.put("code", MemberErrorEnum.E_WEIBO_ACCESSTOKEN_ERROR.code());
                result.put("msg", MemberErrorEnum.E_WEIBO_ACCESSTOKEN_ERROR.value());
            }
        } else {
            result.put("code", MemberErrorEnum.E_WEIBO_ACCESSTOKEN_ERROR.code());
            result.put("msg", MemberErrorEnum.E_WEIBO_ACCESSTOKEN_ERROR.value());
        }
        return result;
    }

    /**
     * 功能描述: 用户手机号绑定微博登录信息
     *
     * @param: [code, mobile]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @auther: zhangtai
     * @date: 2020/4/10 10:41
     */

    @Override
    public Map<String, Object> weiboBindMobile(String code, String mobile) {
        Map result = new HashMap<String, Object>(3);
        // 1.获取redis微信授权信息
        Object obj = redisUtils.get(RedisKeys.getWeiboLoginKey(code));
        log.info("获取key:{},微博授权信息缓存成功，result：{}", RedisKeys.getWeiboLoginKey(code), obj);
        if (obj == null) {
            // redis不存在该用户微博授权信息
            result.put("code", MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.code());
            result.put("msg", MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.value());
        } else {
            // 存在用户微信授权信息
            WeiboAccessTokenDTO weiboAccessTokenDTO = (WeiboAccessTokenDTO) obj;
            // 获取用户信息，进行新用户保存或者原有用户更新微信绑定关系
            Integer status = getUserMessage(weiboAccessTokenDTO.getAccessToken(), weiboAccessTokenDTO.getUid(), mobile);
            if (status == 0) {
                result.put("code", ErrorCode.SUCCESS);
                result.put("msg", "绑定手机号成功");
                result.put("mobile", mobile);
                // 删除redis数据
                redisUtils.delete(RedisKeys.getWeiboLoginKey(code));
                log.info("删除登录缓存成功，key：{}", RedisKeys.getWeiboLoginKey(code));
                redisUtils.delete(RedisKeys.getMobileBindCaptchaKey(mobile));
                log.info("删除微博授权信息缓存成功，key：{}", RedisKeys.getMobileBindCaptchaKey(mobile));
            } else if (status == MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.code()) {
                result.put("code", MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.code());
                result.put("msg", MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.value());
            } else if (status == MemberErrorEnum.E_WEIBO_BINDED_MOBILE.code()) {
                result.put("code", MemberErrorEnum.E_WEIBO_BINDED_MOBILE.code());
                result.put("msg", MemberErrorEnum.E_WEIBO_BINDED_MOBILE.value());
            }
        }
        return result;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * get请求:https协议
     * <p>
     * 通过access_token和uid拉去用户信息
     * <p>
     * 返回: openid,nickname(昵称),sex,province(省份),city,country,headimgurl,privilege(特权),unionid
     *
     * @return
     */
    private Integer getUserMessage(String accessToken, String uid, String mobile) {
        // 1.查询手机号是否被注册
        MemberVoDTO member = memberService.getByMobile(mobile);
        // 2.获取微博用户信息
        JSONObject userMessage;
        try {
            userMessage = weiboLoginService.getUserInfo(accessToken, uid);
        } catch (Exception e) {
            return MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.code();
        }
        if (userMessage == null || userMessage.get(PARAM_ERROR_CODE_STR) != null) {
            // 获取微博用户信息错误
            return MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.code();
        }
        WeiboInfoEntity weiboInfoEntity = JSON.parseObject(userMessage.toJSONString(), WeiboInfoEntity.class);
        if (weiboInfoEntity == null) {
            // 获取微博用户信息错误
            return MemberErrorEnum.E_WEIBO_BIND_ACCESS_EXCERTION.code();
        }
        if (member != null) {
            if (StringUtils.isNotBlank(member.getWechatUnionid())) {
                // 已绑定微博
                return MemberErrorEnum.E_WEIBO_BINDED_MOBILE.code();
            }
            if (MemberEnum.MEMBER_STATE_OFF.value() == member.getMemberState()) {
                // 手机账号被禁用
                return 3;
            }
            // 3.手机号已被注册，修改该用户微博uid
            member.setWeiboUid(weiboInfoEntity.getUid());
            MemberDTO memberDTO = ConvertUtils.sourceToTarget(member, MemberDTO.class);
            memberService.updateById(memberDTO);
        } else {
            // 4.手机号未被注册，使用微信信息新增用户信息
            MemberEntity memberPo = new MemberEntity();
            memberPo.setNickName(weiboInfoEntity.getNicname());
            memberPo.setMemberMobile(mobile);
            memberPo.setWeiboUid(weiboInfoEntity.getUid());
            // 微博接口1男,2女,0未知  数据库配置 1女 2男
            if (String.valueOf(MemberEnum.WEI_BO_SEX_MAN).equals(weiboInfoEntity.getSex())) {
                memberPo.setMemberSex(MemberEnum.MEMBER_SEX_MAN.value());
            } else if (String.valueOf(MemberEnum.WEI_BOS_EX_WOMAN).equals(weiboInfoEntity.getSex())) {
                memberPo.setMemberSex(MemberEnum.MEMBER_SEX_WOMAN.value());
            }

            //上传头像
            String headimgUrl = uploadService.downloadToUrl(weiboInfoEntity.getHeadimgurl());
            memberPo.setMemberAvatar(headimgUrl);
            // 填充用户名称
            MemberUtil.saveMemberFill(memberPo);

            //转换
            MemberRegisterDTO memberRegisterDTO = ConvertUtils.sourceToTarget(memberPo, MemberRegisterDTO.class);
            memberService.saveMember(memberRegisterDTO);

        }
        return 0;
    }
}
