/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 仲裁表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "仲裁表")
public class ArbitrationDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号 唯一")
    private Long aftersaleSn;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "会员账号")
    private String memberName;
    @ApiModelProperty(value = "联系人")
    private String contacts;
    @ApiModelProperty(value = "联系方式")
    private String contactsWay;
    @ApiModelProperty(value = "详细说明")
    private String detailedDescription;
    @ApiModelProperty(value = "图片数组（,分隔）")
    private String imagesArr;
    @ApiModelProperty(value = "仲裁申请时间")
    private Date arbitrationApplyDate;
    @ApiModelProperty(value = "仲裁审核时间")
    private Date arbitrationAuditDate;
    @ApiModelProperty(value = "仲裁状态（1:审核通过、2:审核不通过、默认：3:待审核）")
    private Integer arbitrationStatus;
    @ApiModelProperty(value = "平台审核理由")
    private String auditReason;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "审核人")
    private String auditor;
    @ApiModelProperty(value = "仲裁类型（0：售后-退货、1：售后-换货）")
    private Integer arbitrationType;
    @ApiModelProperty(value = "退货详情")
    private AftersaleReturnDetailDTO returnDetailDTO;
    @ApiModelProperty(value = "换货详情")
    private AftersaleBarterDetailDTO barterDetailDTO;
}
