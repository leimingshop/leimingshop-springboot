/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 原因描述
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-06-10
 */
@Data
@ApiModel(description = "ReasonDescriptionDTO")
public class ReasonDescriptionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "类型（0：退货，1：换货，2：申请退款 ，3：取消订单）")
    private String type;

    @ApiModelProperty(value = "适用角色（0：会员，1：商家，2：平台）")
    private String role;

    @ApiModelProperty(value = "禁止操作（默认为0，为1的时候不可删除、编辑）")
    private Integer forbidOperation;

    @ApiModelProperty(value = "描述内容")
    private String content;

}
