/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 短信消息实体封装
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 9:45
 **/
@Data
@EqualsAndHashCode
public class SmsMessageDTO implements Serializable {

    private static final long serialVersionUID = -3375095116636268708L;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 发送短信参数Json
     */
    private String paramJson;

}
