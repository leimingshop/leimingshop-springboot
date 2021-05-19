/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 仲裁表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "仲裁申请")
public class ApplyArbitrationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "售后单号 唯一")
    @NotNull(message = "请选择需要申请仲裁的售后申请", groups = AddGroup.class)
    private Long aftersaleSn;
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "会员账号")
    private String memberName;
    @ApiModelProperty(value = "联系人")
    @NotBlank(message = "联系人不能为空", groups = AddGroup.class)
    private String contacts;
    @ApiModelProperty(value = "联系方式")
    @NotBlank(message = "联系方式不能为空", groups = AddGroup.class)
    private String contactsWay;
    @ApiModelProperty(value = "详细说明")
    private String detailedDescription;
    @ApiModelProperty(value = "图片数组（,分隔）")
    private String imagesArr;
    @ApiModelProperty(value = "仲裁类型（0：售后-退货、1：售后-换货）")
    private Integer arbitrationType;
}
