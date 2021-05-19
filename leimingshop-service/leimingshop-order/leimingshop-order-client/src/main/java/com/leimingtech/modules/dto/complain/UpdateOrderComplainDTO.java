/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.complain;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 评价客服
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "评价客服")
public class UpdateOrderComplainDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "评价客服满意度1-2星为差，3-4星为一般，5星为满意")
    @NotNull(message = "满意度不能为空", groups = UpdateGroup.class)
    private Integer customerSatisfaction;
    @ApiModelProperty(value = "评价客服内容")
    private String customerContent;
}