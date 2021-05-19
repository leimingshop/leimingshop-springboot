/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.reason;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ReasonDescriptionSaveDTO
 * @Description
 * @Author huangkeyuan
 * @Date 2019-06-10 17:30
 * @Version 1.0
 */
@Data
@ApiModel(description = "ReasonDescriptionSaveDTO")
public class ReasonDescriptionSaveDTO {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "类型不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "类型（0：退货，1：换货，2：申请退款 ，3：取消订单）选中多个使用逗号拼接")
    private String type;

    @NotBlank(message = "适用角色不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "适用角色（0：会员，1：商家，2：平台）选中多个使用逗号拼接")
    private String role;

    @NotBlank(message = "描述内容不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "描述内容")
    private String content;

    @ApiModelProperty(value = "禁止操作（默认为0，为1的时候不可删除、编辑））")
    private Integer forbidOperation;

}
