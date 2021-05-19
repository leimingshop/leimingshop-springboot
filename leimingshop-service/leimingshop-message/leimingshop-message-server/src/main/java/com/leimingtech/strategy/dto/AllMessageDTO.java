/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * 私信、短信、APP推送、微信推送
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 9:50
 **/
@Data
@EqualsAndHashCode
public class AllMessageDTO implements Serializable {
    private static final long serialVersionUID = 7579084735348680100L;


    /**
     * 用户信息集合 key: 用户ID   value 用户名称
     */
    private Map<Long, String> memberMap;

    /**
     * 店铺信息集合 key: 用户ID   value 用户名称
     */
    private Map<Long, String> storeMap;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 站内信、短信替换参数
     */
    private Map<String, String> paramMap;

    /**
     * 微信消息替换参数Json
     */
    private String wechatParamsJson;

    /**
     * 微信消息点击跳转
     */
    private String wehcatClickUrl;

    /**
     * 微信用户openId
     */
    private String wechatOpenId;

}
