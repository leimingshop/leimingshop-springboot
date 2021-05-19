/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dto.WxOauth2ResDTO;
import com.leimingtech.modules.dto.WxUserDto;
import com.leimingtech.modules.service.WechatMpService;
import com.leimingtech.statuscode.WechatStatusCode;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 对接微信公众号服务实现层
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/1/19 11:50
 **/
@Slf4j

@Service
public class WechatMpServiceImpl implements WechatMpService {

    @Autowired
    private WxMpService wxService;

    @Value("${appidtype.website}")
    private String website;

    @Value("${appidtype.public}")
    private String publicNum;

    /**
     * 微信公众号-推送消息
     *
     * @param paramsJson: 参数JSON
     * @param templateId: 模板ID
     * @param openId:     微信用户的openId
     * @param url:        点击跳转地址
     * @return 消息ID
     * @date 2020/1/19 11:27
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public String sendTemplateMsg(@RequestParam("paramsJson") String paramsJson,
                                  @RequestParam("templateId") String templateId,
                                  @RequestParam("url") String url,
                                  @RequestParam("openId") String openId) {

        WxMpService wxMpService = wxService.switchoverTo(publicNum);
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId(templateId)
                .url(url)
                .build();

        // 封装发送消息的模板数据
        Map<String, Object> paramsMap = JSONObject.parseObject(paramsJson);
        paramsMap.keySet().forEach(key -> {
            templateMessage.addData(new WxMpTemplateData(key, String.valueOf(paramsMap.get(key)), "#000000"));
        });
        try {
            String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            log.info("发送消息成功：{}", msgId);
            // 保存发送记录
        } catch (WxErrorException e) {
            log.error("微信公众号发送消息异常,消息内容[{}],模板ID[{}],用户openId[{}],异常信息:{}", paramsJson, templateId, openId, e);
        }
        return null;
    }


    @Override
    public WxOauth2ResDTO wechatLogin(String code) {
        WxMpService wxMpService = wxService.switchoverTo(website);
        try {
            WxMpOAuth2AccessToken wxMpOauth2AccessToken = wxMpService.oauth2getAccessToken(code);
            WxOauth2ResDTO wxOauth2ResDto = ConvertUtils.sourceToTarget(wxMpOauth2AccessToken, WxOauth2ResDTO.class);
            return wxOauth2ResDto;
        } catch (WxErrorException e) {
            log.error("调用微信获取OpenId异常，异常信息：{}", e.getError());
            throw new ServiceException(WechatStatusCode.CLIENT_WECHAT_GET_CODE_FAIL);
        }
    }


    @Override
    public WxUserDto getUserMessage(@RequestBody WxOauth2ResDTO wxOauth2ResDto) {
        WxMpService wxMpService = wxService.switchoverTo(website);
        WxMpOAuth2AccessToken wxMpOauth2AccessToken = ConvertUtils.sourceToTarget(wxOauth2ResDto, WxMpOAuth2AccessToken.class);
        try {
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOauth2AccessToken, null);
            return ConvertUtils.sourceToTarget(wxMpUser, WxUserDto.class);
        } catch (WxErrorException e) {
            log.error("获取微信用户信息错误，{}", e.getError());
            throw new ServiceException(WechatStatusCode.CLIENT_WECHAT_GET_WECHAT_USER_INFO_FAIL);
        }
    }
}
