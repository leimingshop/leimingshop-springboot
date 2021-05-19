/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * 私信发送实体封装
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 9:29
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class PrivateMessageDTO implements Serializable {

    private static final long serialVersionUID = 2324071575374523158L;

    /**
     * 站内信替换参数
     */
    private Map<String, String> paramMap;


    /**
     * 用户信息集合 key: 用户ID   value 用户名称
     */
    private Map<Long, String> memberMap;

    /**
     * 店铺信息集合 key: 店铺用户ID   value 店铺用户名称
     */
    private Map<Long, String> storeMap;
}
