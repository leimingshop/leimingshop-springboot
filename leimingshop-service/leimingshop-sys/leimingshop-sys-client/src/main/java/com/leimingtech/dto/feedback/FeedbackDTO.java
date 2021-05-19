/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.feedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 意见反馈信息表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@Data
@ApiModel(value = "意见反馈信息表")
public class FeedbackDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "会员账号")
    private String memberName;
    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    @ApiModelProperty(value = "手机号码")
    private String mobileNumber;
    @ApiModelProperty(value = "反馈类型（默认1：功能异常、2：优化建议、3：其他反馈）")
    private Integer feedbackType;
    @ApiModelProperty(value = "详细说明")
    private String detailedDescription;
    @ApiModelProperty(value = "图片数组（,分隔）")
    private String imagesArr;
    @ApiModelProperty(value = "反馈来源（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer feedbackSource;
    @ApiModelProperty(value = "反馈判定（0:无效反馈、1:有效反馈、2:重点问题、)")
    private Integer decideType;
    @ApiModelProperty(value = "客服回复")
    private String servicesReply;
    @ApiModelProperty(value = "处理状态(默认0:未处理、1：已处理)")
    private Integer disposeStatus;
    @ApiModelProperty(value = "处理时间")
    private Date disposeDate;
    @ApiModelProperty(value = "处理人")
    private String operator;
    @ApiModelProperty(value = "客服备注")
    private String remark;
    @ApiModelProperty(value = "用户是否删除（默认为0未删除，1已删除）")
    private Integer userDelFlag;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}