/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto;

import lombok.Data;

/**
 * 用户微信授权信息
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
@Data
public class WxOauth2ResDTO {

    private String accessToken;

    private Integer expiresIn;

    private String refreshToken;

    private String openId;

    private String scope;

    private String unionId;
}
