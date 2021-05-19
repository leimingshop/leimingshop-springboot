/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.complain;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 新增订单投诉
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "新增订单投诉")
public class SaveOrderComplainDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    @NotNull(message = "订单ID不能为空", groups = AddGroup.class)
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    @NotNull(message = "订单编号不能为空", groups = AddGroup.class)
    private Long orderSn;

    @ApiModelProperty(value = "投诉类型1:服务相关,2:物流相关,3:售后相关,4:商品相关5:其他")
    @NotNull(message = "投诉类型不能为空", groups = AddGroup.class)
    private Integer type;

    @ApiModelProperty(value = "投诉来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序 ")
    private Integer source;

    @ApiModelProperty(value = "详细说明")
    @NotNull(message = "投诉图片不能为空", groups = AddGroup.class)
    @Length(max = 500, message = "详细说明最多500位", groups = AddGroup.class)
    private String cause;

    @ApiModelProperty(value = "投诉图片")
    private String picture;

    @ApiModelProperty(value = "手机号")
    @NotNull(message = "手机号不能为空", groups = AddGroup.class)
    @Length(max = 11, message = "手机号只能为11位", groups = AddGroup.class)
    private String phone;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @ApiModelProperty(value = "会员账号")
    private String account;
    @ApiModelProperty(value = "店铺ID")
    @NotNull(message = "店铺ID不能为空", groups = AddGroup.class)
    private Long storeId;
    @ApiModelProperty(value = "店铺名称")
    @NotNull(message = "店铺名称不能为空", groups = AddGroup.class)
    private String storeName;
}