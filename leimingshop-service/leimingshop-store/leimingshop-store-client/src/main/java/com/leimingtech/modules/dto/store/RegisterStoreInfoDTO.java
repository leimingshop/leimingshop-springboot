/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 修改注册店铺信息
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "RegisterStoreInfoDTO")
public class RegisterStoreInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺ID")
    @NotNull(message = "店铺ID不能为空", groups = UpdateGroup.class)
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    @NotNull(message = "店铺名称不能为空", groups = UpdateGroup.class)
    @Length(max = 20, message = "店铺名称不能超过20位", groups = UpdateGroup.class)
    private String storeName;

    @ApiModelProperty(value = "店铺logo")
    @NotNull(message = "店铺logo不能为空", groups = UpdateGroup.class)
    private String storeLogo;
    @ApiModelProperty(value = "店铺简介")

    @Length(max = 200, message = "店铺简介不能超过200位", groups = UpdateGroup.class)
    @NotNull(message = "店铺简介不能为空", groups = UpdateGroup.class)
    private String storeIntro;

    @ApiModelProperty(value = "店铺类型")
    @NotNull(message = "店铺类型不能为空", groups = UpdateGroup.class)
    private String storeType;

    @ApiModelProperty("店铺联系人")
    @NotNull(message = "店铺联系人不能为空", groups = UpdateGroup.class)
    private String storeLinkman;

    @ApiModelProperty("联系人电话")
    @Length(max = 11, message = "电话号码最长为11位", groups = UpdateGroup.class)
    @NotNull(message = "联系人电话不能为空", groups = UpdateGroup.class)
    private String linkmanPhone;

    @ApiModelProperty(value = "店铺分类ID")
    private List<StoreGoodsClassDTO> storeGoodsClassDTO;

    @ApiModelProperty(value = "店铺等级")
    private Long gradeId;

    @ApiModelProperty(value = "店铺等级名称")
    private String gradeName;

    @ApiModelProperty("店铺认证Id")
    private Long authId;

    @ApiModelProperty(value = "公司名称")
    @Length(max = 50, message = "公司名称不能大于50位", groups = UpdateGroup.class)
    @NotNull(message = "公司名称不能为空", groups = UpdateGroup.class)
    private String companyName;

    @ApiModelProperty(value = "法人名称")
    @Length(max = 10, message = "法人名称不能超过10位", groups = UpdateGroup.class)
    @NotNull(message = "法人姓名不能为空", groups = UpdateGroup.class)
    private String legalPersonName;

    @ApiModelProperty(value = "办公电话")
    @Length(max = 11, message = "办公电话不能超过11位", groups = UpdateGroup.class)
    @NotNull(message = "办公电话不能为空", groups = UpdateGroup.class)
    private String companyPhone;

    @ApiModelProperty(value = "办公地址")
    @Length(max = 50, message = "办公地址不能超过50位", groups = UpdateGroup.class)
    @NotNull(message = "办公地址不能为空", groups = UpdateGroup.class)
    private String companyAddressDetail;

    @ApiModelProperty(value = "纳税人识别号")
    @Length(max = 50, message = "税号不能超过50位", groups = UpdateGroup.class)
    @NotNull(message = "纳税人识别号不能为空", groups = UpdateGroup.class)
    private String taxpayerId;

    @ApiModelProperty(value = "开户行账号")
    @Length(max = 20, message = "开户行账号不能超过20位", groups = UpdateGroup.class)
    @NotNull(message = "开户行账号不能为空", groups = UpdateGroup.class)
    private String bankAccountNumber;

    @ApiModelProperty(value = "开户行名称")
    @Length(max = 20, message = "开户行名称不能超过20位", groups = UpdateGroup.class)
    @NotNull(message = "开户行名称不能为空", groups = UpdateGroup.class)
    private String bankName;

    @ApiModelProperty("身份证，正面")
    @NotNull(message = "身份证正面不能为空", groups = UpdateGroup.class)
    private String idCardPeoPicture;

    @ApiModelProperty("身份证，反面")
    @NotNull(message = "身份证反面不能为空", groups = UpdateGroup.class)
    private String idCardNatPicture;

    @ApiModelProperty(value = "营业执照电子版")
    @NotNull(message = "营业执照不能为空", groups = UpdateGroup.class)
    private String electronicBusinessLicense;

    @ApiModelProperty("纳税人资格证")
    @NotNull(message = "纳税人资格证不能为空", groups = UpdateGroup.class)
    private String taxpayerPicture;

    @ApiModelProperty("银行开户许可证")
    @NotNull(message = "银行开户许可证不能为空", groups = UpdateGroup.class)
    private String bankAccountPicture;
    @ApiModelProperty("10 待审核 20 审核通过 30 审核拒绝 40 待提交")
    private Integer registerAuditStatus;
    @ApiModelProperty("审核原因")
    private String registerAuditCause;
    @ApiModelProperty("审核时间")
    private Date updateDate;


}
