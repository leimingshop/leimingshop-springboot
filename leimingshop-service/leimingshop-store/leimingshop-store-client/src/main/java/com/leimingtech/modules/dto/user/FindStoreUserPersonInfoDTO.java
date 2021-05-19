/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 查询用户实体
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "FindStoreUserPersonInfoDTO")
public class FindStoreUserPersonInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登陆账号")
    private String account;
    @ApiModelProperty(value = "手机号")
    private String mobilePhone;
    @ApiModelProperty(value = "手机号加密")
    private String encryptionPhone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "邮箱加密")
    private String encryptionEmail;
    @ApiModelProperty(value = "商户头像")
    private String logo;
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;


}
