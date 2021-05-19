/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.address;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 微信地址实体
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/3/31 11:59
 **/
@Data
@ApiModel(description = "微信地址实体")
public class WechatAddressDTO implements Serializable {

    private static final long serialVersionUID = -6363299634283010560L;

    @NotBlank(message = "收货人姓名不可为空", groups = AddGroup.class)
    @ApiModelProperty(value = "收货人姓名")
    private String userName;

    @NotBlank(message = "收货人手机号码不可为空", groups = AddGroup.class)
    @ApiModelProperty(value = "收货人手机号码")
    private String telNumber;

    @NotBlank(message = "国标收货地址第一级地址（省）不可为空", groups = AddGroup.class)
    @ApiModelProperty(value = "国标收货地址第一级地址（省）")
    private String provinceName;

    @NotBlank(message = "国标收货地址第二级地址（省）不可为空", groups = AddGroup.class)
    @ApiModelProperty(value = "国标收货地址第二级地址（市）")
    private String cityName;

    @ApiModelProperty(value = "国标收货地址第三级地址（区）")
    private String countryName;

    @ApiModelProperty(value = "邮编")
    private String postalCode;

    @ApiModelProperty(value = "详细收货地址信息")
    private String detailInfo;
}