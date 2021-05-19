/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.address.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 会员地址表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Data
@ApiModel(description = "PcMemberAddressVo")
public class PcMemberAddressVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    /**
     * 收件人名称
     */
    @ApiModelProperty(value = "收件人名称")
    private String trueName;
    /**
     * 收件人电话
     */
    @ApiModelProperty(value = "收件人电话")
    private String mobPhone;
    /**
     * 地区ID
     */
    @ApiModelProperty(value = "地区ID")
    private Long areaId;
    /**
     * 省级id
     */
    @ApiModelProperty(value = "省级id")
    private Long provinceId;
    /**
     * 市级ID
     */
    @ApiModelProperty(value = "市级ID")
    private Long cityId;
    /**
     * 街道ID
     */
    @ApiModelProperty(value = "街道ID")
    private Long stressId;
    /**
     * 地址内容
     */
    @ApiModelProperty(value = "地址内容")
    private String areaInfo;

    /**
     * 邮编
     */
    @ApiModelProperty(value = "邮编")
    private String zipCode;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 是否默认（ 默认为0:非默认，1:已默认）
     */
    @ApiModelProperty(value = "是否默认（ 默认为0:非默认，1:已默认）")
    private Integer defaultFlag;

}
