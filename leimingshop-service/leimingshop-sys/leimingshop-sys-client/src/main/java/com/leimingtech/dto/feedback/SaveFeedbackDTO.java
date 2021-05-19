/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.feedback;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 保存意见反馈
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@Data
@ApiModel(value = "保存意见反馈")
public class SaveFeedbackDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员账号")
    private String memberName;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    @ApiModelProperty(value = "手机号码")
    @NotNull(message = "手机号码不能为空", groups = AddGroup.class)
    @Length(max = 11, message = "请输入正确的手机号码", groups = AddGroup.class)
    private String mobileNumber;

    @ApiModelProperty(value = "反馈类型（默认1：功能异常、2：优化建议、3：其他反馈）")
    @NotNull(message = "反馈类型不能为空", groups = AddGroup.class)
    private Integer feedbackType;

    @ApiModelProperty(value = "详细说明")
    @Length(max = 500, min = 5, message = "详细说明最多500字,最少5字", groups = AddGroup.class)
    @NotNull(message = "详细说明不能为空", groups = AddGroup.class)
    private String detailedDescription;

    @ApiModelProperty(value = "图片数组（,分隔）")
    private String imagesArr;

    @ApiModelProperty(value = "反馈来源（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer feedbackSource;
}