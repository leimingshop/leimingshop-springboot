/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.feedback;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 处理反馈
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@Data
@ApiModel(value = "处理反馈")
public class UpdateFeedbackDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "反馈判定（0:无效反馈、1:有效反馈、2:重点问题、)")
    @NotNull(message = "反馈判定不能为空", groups = UpdateGroup.class)
    private Integer decideType;
    @ApiModelProperty(value = "客服回复")
    @NotNull(message = "客服回复不能为空", groups = UpdateGroup.class)
    @Length(max = 500, message = "回复最多最多500字", groups = UpdateGroup.class)
    private String servicesReply;
    @ApiModelProperty(value = "客服备注")
    private String remark;
}