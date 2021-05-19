/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺用户表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "StoreUserPhoneEmailDTO")
public class StoreUserPhoneEmailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty(value = "邮箱")
    private String eMail;

    @ApiModelProperty(value = "手机号")
    private String mobilePhone;


}