/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.auth;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


/**
 * 店铺设置
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "SettingStoreAuthDTO")
public class SettingStoreAuthDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "公司名称")
    @Length(max = 50, message = "公司名称不能大于50位", groups = AddGroup.class)
    private String companyName;
    @ApiModelProperty(value = "法人名称")
    @Length(max = 10, message = "法人名称不能超过10位", groups = AddGroup.class)
    private String legalPersonName;
    @ApiModelProperty(value = "营业执照电子版")
    private String electronicBusinessLicense;
    @ApiModelProperty(value = "注册地址")
    @Length(max = 50, message = "注册地址不能超过50位", groups = AddGroup.class)
    private String companyAddress;
    @ApiModelProperty(value = "办公地址")
    @Length(max = 50, message = "办公地址不能超过50位", groups = AddGroup.class)
    private String companyAddressDetail;
    @ApiModelProperty(value = "公司所属省份")
    private Long companyProvinceId;
    @ApiModelProperty(value = "公司所属市")
    private Long companyCityId;
    @ApiModelProperty(value = "公司所区县")
    private Long companyAreaId;
    @ApiModelProperty(value = "办公电话")
    @Length(max = 11, message = "办公电话不能超过11位", groups = AddGroup.class)
    private String companyPhone;
    @ApiModelProperty(value = "注册资金")
    private Integer companyRegisteredCapital;
    @ApiModelProperty(value = "纳税人识别号")
    @Length(max = 50, message = "税号不能超过50位", groups = AddGroup.class)
    private String taxpayerId;
    @ApiModelProperty(value = "开户行名称")
    @Length(max = 20, message = "开户行名称不能超过20位", groups = AddGroup.class)
    private String bankName;
    @ApiModelProperty(value = "银行开户名称")
    @Length(max = 20, message = "银行开户名称不能超过20位", groups = AddGroup.class)
    private String bankAccountName;
    @ApiModelProperty(value = "开户行账号")
    @Length(max = 20, message = "开户行账号不能超过20位", groups = AddGroup.class)
    private String bankAccountNumber;
    @ApiModelProperty(value = "支付宝姓名")
    @Length(max = 10, message = "支付宝姓名不能超过10位", groups = AddGroup.class)
    private String alipayName;
    @ApiModelProperty(value = "支付宝账号名")
    @Length(max = 20, message = "支付宝账号名不能超过20位", groups = AddGroup.class)
    private String alipayAccountNumber;
    @ApiModelProperty(value = "微信姓名")
    @Length(max = 10, message = "微信姓名不能超过10位", groups = AddGroup.class)
    private String wechatName;
    @ApiModelProperty(value = "微信账号")
    @Length(max = 20, message = "微信账号不能超过20位", groups = AddGroup.class)
    private String wechatAccountNumber;


    @ApiModelProperty(value = "店铺名称")
    @Length(max = 20, message = "店铺名称不能超过20位", groups = AddGroup.class)
    private String storeName;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "店铺简介")
    @Length(max = 200, message = "店铺简介不能超过200位", groups = AddGroup.class)
    private String storeIntro;

}